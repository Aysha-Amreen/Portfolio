package uga.menik.cs4370.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uga.menik.cs4370.models.Cart;
import uga.menik.cs4370.models.Item;
import uga.menik.cs4370.models.User;
import uga.menik.cs4370.models.Order;

@Service
public class OrderService {

    private final DataSource dataSource;
    private final CartService cartService;
    private final ItemService itemService;

    @Autowired
    public OrderService(DataSource dataSource, CartService cartService, ItemService itemService) {
        this.dataSource = dataSource;
        this.cartService = cartService;
        this.itemService = itemService;
    } // OrderService controller


    /**
     * Creates an order for the specified user. Feeds variables into placeOrder to populate the items_orders database
     * @param user the user who is making an order
     * @return true if the order is successfully made and populated, false if not
     */
    public boolean makeOrder(User user) {
        boolean orderPlaced = false;

        Cart cart = cartService.getCart(user);
        List<Item> items = cart.getItems();
        List<Integer> itemQs = cart.getQuantities();

        // if the cart is not empty, attempt to make a new order
        if(!items.isEmpty()) {
            // start the new order
            final String sql = "INSERT INTO orders (username, created_at) VALUES (?, NOW())";

            try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, user.getUserId());

                int rowsAffected = pstmt.executeUpdate();
                // if rowsAffected is zero, then the insert into the orders table failed in some way
                if(rowsAffected == 0) {
                    return false;
                } // if
            } catch (SQLException e) {
                System.err.println("Error adding item: " + e.getMessage());
                return false;
            } // try

            // fetch the most recent order that was just made
            final String sql2 = "select order_id from orders where username = ? order by created_at desc limit 1";
            try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql2)) {
                pstmt.setString(1, user.getUserId());
                // get the most recent order
                try (ResultSet rs = pstmt.executeQuery()) {
                    // result should only have one row
                    if (rs.next()) {
                        // populate the items_orders information
                        String orderId = rs.getString("order_id");
                        orderPlaced = placeOrder(user, orderId, items, itemQs);
                        return orderPlaced;
                    } // while    
                } // try (inner)   
                
            } catch (SQLException e) {
                System.err.println("Error adding item: " + e.getMessage());
                return false;
            } // try
            
        } // if     
    
        return false;
    } // placeOrder


    /**
     * Creates an order for the specified user 
     * @param user      The user who is making an order
     * @param orderId   The id of the order created by makeOrder
     * @param itemList  The list of items from the user's cart
     * @param itemQ     The list of item quantities from the user's cart
     * @return true if the order was successfully placed, false if not
     */
    public boolean placeOrder(User user, String orderId, List<Item> itemList, List<Integer> itemQ) {
        boolean orderPlaced = false;

        if(itemList.isEmpty()) {
            return false;
        } else {

            // add all of the cart's items to the items_orders table
            for(int i = 0; i < itemList.size(); i++) {
                orderPlaced = addItemToOrders(orderId, itemList.get(i), itemQ.get(i));
                
                // if orderPlaced is false, then adding an item to the order went wrong
                if(!orderPlaced) {
                    return orderPlaced;
                } // if
            } // for

            //empty the cart
            boolean empty = cartService.emptyCart(user);
            // if empty is false, emptying the cart went wrong
            if(!empty) {
                return empty;
            } // if
        } // else
        return orderPlaced;
    } // placeOrder


    /**
     * Adds a new item to the items_orders table.
     * @param item     The item being added to the table.
     * @param orderId  The order the item is being added to.
     * @param quantity The amount of the item that is being added to the order
     * @return true if the item is successfully added to the table, false otherwise.
     */
    public boolean addItemToOrders(String orderId, Item item, int quantity) {
        final String sql = "INSERT INTO items_orders (order_id, item_id, quantity) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, orderId);
            pstmt.setString(2, item.getItemId());
            pstmt.setInt(3, quantity);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding item: " + e.getMessage());
            return false;
        } // try
    } // addItemToOrders


    /**
     * Checks the inventory to see if a user's cart can be made based on the available ingredients
     * @param user the user who's cart we are checking
     * @return true if the items in the cart can be made based on the available inventory, false if not
     */
    public boolean checkStock(User user) {

        // returns a table with ingredient id, cart item quantity, recipe_ingredient's ingredient quantity, and inventory ingredient quantity
        final String sql = "select inv.ing_id as ing_id, c.quantity as cq, ri.quantity as rq, inv.quantity as iq from cart c, items i, recipe_ingredients ri, inventory inv where c.username = ? and c.item_id = i.item_id and i.rec_id = ri.rec_id and inv.ing_id\r\n" + //
                        "= ri.ing_id";
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUserId());
            try (ResultSet rs = pstmt.executeQuery()) {
                List<String> ingredients = new ArrayList<>();
                List<Integer> ingredientStock = new ArrayList<>();
                List<Integer> usedIngredients = new ArrayList<>();
                // traverse the rows
                while (rs.next()) {
                    int index = 0;
                    String ing_id = rs.getString("ing_id");
                    // ing needed = cart item count * recipe ingredient count
                    int needed = rs.getInt("cq") * rs.getInt("rq");
                    int stock = rs.getInt("iq");

                    // if lists do not already contain the ingredient in question, add a new element in each
                    if(!ingredients.contains(ing_id)) {
                        ingredients.add(ing_id);
                        ingredientStock.add(stock);
                        usedIngredients.add(needed);
                        index = ingredients.indexOf(ing_id);
                    } else {
                    // if lists already contain the ingredient, update existing needed quantity
                        index = ingredients.indexOf(ing_id);
                        usedIngredients.set(index, usedIngredients.get(index) + needed);
                    } // if

                    // if the ingredients used ever exceeds the stock, return false
                    if(stock - usedIngredients.get(index) < 0) {
                        return false;
                    } // if
                } // while 

                // if the code gets to this point, that means there are enough ingredients in stock to make the item
                return true;
            } // try (inner)   
            
        } catch (SQLException e) {
            System.err.println("Error adding item: " + e.getMessage());
            return false;
        } // try
    } // checkStock


    /**
     * Checks the inventory to see if a specified item of a specified quantity can be made based on the available ingredients
     * @param user the user who's cart we are checking
     * @return true if the items in the cart can be made based on the available inventory, false if not
     */
    public boolean checkStock(String itemId, int quantity) {

        // returns a table with ingredient id, recipe_ingredient's ingredient quantity, and inventory ingredient quantity
        final String sql = "select inv.ing_id as ing_id, ri.quantity as rq, inv.quantity as iq from items i, recipe_ingredients ri, inventory inv where i.item_id = ? and i.rec_id = ri.rec_id and inv.ing_id\r\n" + //
                        "= ri.ing_id";
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, itemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                List<String> ingredients = new ArrayList<>();
                List<Integer> ingredientStock = new ArrayList<>();
                List<Integer> usedIngredients = new ArrayList<>();
                // traverse the rows
                while (rs.next()) {
                    int index = 0;
                    String ing_id = rs.getString("ing_id");
                    // ing needed = cart item count * recipe ingredient count
                    int needed = quantity * rs.getInt("rq");
                    int stock = rs.getInt("iq");

                    // if lists do not already contain the ingredient in question, add a new element in each
                    if(!ingredients.contains(ing_id)) {
                        ingredients.add(ing_id);
                        ingredientStock.add(stock);
                        usedIngredients.add(needed);
                        index = ingredients.indexOf(ing_id);
                    } else {
                    // if lists already contain the ingredient, update existing needed quantity
                        index = ingredients.indexOf(ing_id);
                        usedIngredients.set(index, usedIngredients.get(index) + needed);
                    } // if

                    // if the ingredients used ever exceeds the stock, return false
                    if(stock - usedIngredients.get(index) < 0) {
                        return false;
                    } // if
                } // while 

                // if the code gets to this point, that means there are enough ingredients in stock to make the item
                return true;
            } // try (inner)   
            
        } catch (SQLException e) {
            System.err.println("Error adding item: " + e.getMessage());
            return false;
        } // try
    } // checkStock

    
    /**
     * Updates stock of ingredients when for when an item is added to the cart
     * @param itemId    the item's id
     * @param quantity  the amount of the item
     * @return
     */
    public boolean addItemToCart(String itemId, int quantity) {
        // returns a table with ingredient id, recipe_ingredient's ingredient quantity, and inventory ingredient quantity
        final String sql = "select inv.ing_id as ing_id, ri.quantity as rq, inv.quantity as iq from items i, recipe_ingredients ri, inventory inv where i.item_id = ? and i.rec_id = ri.rec_id and inv.ing_id\r\n" + //
                        "= ri.ing_id";
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, itemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                List<String> ingredients = new ArrayList<>();
                List<Integer> ingredientStock = new ArrayList<>();
                List<Integer> usedIngredients = new ArrayList<>();
                // traverse the rows
                while (rs.next()) {
                    int index = 0;
                    String ing_id = rs.getString("ing_id");
                    // ing needed = item count * recipe ingredient count
                    int needed = quantity * rs.getInt("rq");
                    int stock = rs.getInt("iq");

                    // if lists do not already contain the ingredient in question, add a new element in each
                    if(!ingredients.contains(ing_id)) {
                        ingredients.add(ing_id);
                        ingredientStock.add(stock);
                        usedIngredients.add(needed);
                        index = ingredients.indexOf(ing_id);
                    } else {
                    // if lists already contain the ingredient, update existing needed quantity
                        index = ingredients.indexOf(ing_id);
                        usedIngredients.set(index, usedIngredients.get(index) + needed);
                    } // if

                    // if the ingredients used ever exceeds the stock, return false
                    if(stock - usedIngredients.get(index) < 0) {
                        return false;
                    } // if
                } // while 

                // update the stock
                boolean update = false;
                while(!ingredients.isEmpty()) {
                    update = updateInventory(ingredients.get(0), ingredientStock.get(0) - usedIngredients.get(0));
                    
                    // remove the used information from the lists, loop until all information used
                    ingredientStock.remove(0);
                    ingredients.remove(0);
                    usedIngredients.remove(0);

                    // if update is false, something went wrong. return false
                    if(!update) {
                        return update;
                    } // if
                } // while

                // if the code gets to this point, that means the stock was properly updated
                return true;
            } // try (inner)   
            
        } catch (SQLException e) {
            System.err.println("Error adding item: " + e.getMessage());
            return false;
        } // try
    } // addItemToCart


    /**
     * Updates stock of ingredients when for when an item is removed from the cart
     * @param itemId    the item's id
     * @param quantity  the amount of the item
     * @return
     */
    public boolean removeItemFromCart(String itemId, int quantity) {
        // returns a table with ingredient id, recipe_ingredient's ingredient quantity, and inventory ingredient quantity
        final String sql = "select inv.ing_id as ing_id, ri.quantity as rq, inv.quantity as iq from items i, recipe_ingredients ri, inventory inv where i.item_id = ? and i.rec_id = ri.rec_id and inv.ing_id\r\n" + //
                        "= ri.ing_id";
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, itemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                List<String> ingredients = new ArrayList<>();
                List<Integer> ingredientStock = new ArrayList<>();
                List<Integer> usedIngredients = new ArrayList<>();
                
                // traverse the rows to get the total amount of ingredients used from the item, ingredient ids, and the current stock for each ingredient
                while (rs.next()) {
                    int index = 0;
                    String ing_id = rs.getString("ing_id");
                    // ing needed = quantity * recipe ingredient count
                    int needed = quantity * rs.getInt("rq");
                    int stock = rs.getInt("iq");

                    // if lists do not already contain the ingredient in question, add a new element in each
                    if(!ingredients.contains(ing_id)) {
                        ingredients.add(ing_id);
                        ingredientStock.add(stock);
                        usedIngredients.add(needed);
                        index = ingredients.indexOf(ing_id);
                    } else {
                    // if lists already contain the ingredient, update existing needed quantity
                        index = ingredients.indexOf(ing_id);
                        usedIngredients.set(index, usedIngredients.get(index) + needed);
                    } // if
                } // while 

                // update the stock
                boolean update = false;
                while(!ingredients.isEmpty()) {
                    update = updateInventory(ingredients.get(0), ingredientStock.get(0) + usedIngredients.get(0));
                    
                    // remove the used information from the lists, loop until all information used
                    ingredientStock.remove(0);
                    ingredients.remove(0);
                    usedIngredients.remove(0);

                    // if update is false, something went wrong. return false
                    if(!update) {
                        return update;
                    } // if
                } // while

                // if the code gets to this point, that means the stock was properly updated
                return true;
            } // try (inner)   
            
        } catch (SQLException e) {
            System.err.println("Error adding item: " + e.getMessage());
            return false;
        } // try
    } // addItemToCart

    /**
     * Updates the quantity in inventory of a single specified ingredient
     * @param ing_id        a specific ingredient
     * @param quantity      the amount 
     * @return true if update is successful, false otherwise
     */
    public boolean updateInventory(String ing_id, int quantity) {
        final String sql = "UPDATE inventory SET quantity = ? WHERE ing_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setString(2, ing_id);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating item: " + e.getMessage());
            return false;
        } // try
    } // updateInventory

        /**
     * Retrieves all items in the user's cart and their amounts
     * @param user the user in question
     * @return the user's cart
     */
    public List<Order> getOrders(User user) {
        List<Item> itemList = new ArrayList<>();
        List<Integer> intList = new ArrayList<>();
        List<String> orderIds = new ArrayList<>();
        String date = "";
        String userId = "";
        final String sql = "select * from orders o, items_orders io where o.order_id = io.order_id and o.username = ?";
        List<Order> orderList = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
            pstmt.setString(1, user.getUserId());
            try (ResultSet rs = pstmt.executeQuery()) {
                // Traverse the result rows one at a time.
                // String orderId, String date, String userId, List<Item> items, List<Integer> quantities
                
                while (rs.next()) {
                    String orderId = rs.getString("o.order_id");
                    date = rs.getString("o.created_at");
                    userId = rs.getString("o.username");
                    Item item = itemService.getSpecificItem(rs.getString("io.item_id"));
                    int quantity = rs.getInt("io.quantity");

                    if (orderIds.contains(orderId)) {
                        System.out.println("Order id: " + orderId);
                        System.out.println("Adding item " + item.getItemName() + " to the list of quantity " + quantity);
                        itemList.add(item);
                        intList.add(quantity);
                    } else {
                        orderIds.add(orderId);
                        // if this is the first time encountering a new order, i.e the first time this loop has run
                        if(orderIds.size() > 1) {
                            System.out.println("Adding order: " + orderIds.get(0));
                            // if this is not the first time encountering a new order (i.e. this is not the first time the loop has run)
                            // add the order with the previous orderId to the order list
                            orderList.add(new Order(orderIds.get(0), date, userId, itemList, intList));

                            // clear the previous order from the orderIds
                            orderIds.remove(0);

                            // reset the item and int lists and add the new values
                            itemList = new ArrayList<>();
                            intList = new ArrayList<>();
                            itemList.add(item);
                            intList.add(quantity);
                        } else {
                            itemList.add(item);
                            intList.add(quantity);
                        }// if (inner)
                    } // if
                    
                } // while 
                
            return orderList;
            } // try (inner)        
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } // try
        return orderList;
    } // getOrders

} // OrderService
