package uga.menik.cs4370.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uga.menik.cs4370.models.Cart;
import uga.menik.cs4370.models.User;
import uga.menik.cs4370.models.Item;

@Service
public class CartService {

    private final DataSource dataSource;
    private final ItemService itemService;

    @Autowired
    public CartService(DataSource dataSource, ItemService itemService) {
        this.dataSource = dataSource;
        this.itemService = itemService;
    } // CartService controller


    /**
     * Adds a new item to the cart for the logged-in user.
     * @param item     The item being added to the cart.
     * @param user     The user who is adding something to their cart.
     * @param quantity The amount of the item that is being added to the cart
     * @return true if the item is successfully added to the cart, false otherwise.
     */
    public boolean addItemToCart(String itemId, User user, int quantity) {
        final String sql = "INSERT INTO cart (username, item_id, quantity) VALUES (?, ?, ?)";

        System.out.println("attempting to insert item: " + itemId);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(2, itemId);
            pstmt.setString(1, user.getUserId());
            pstmt.setInt(3, quantity);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding item: " + e.getMessage());
            return false;
        } // try
    } // addItemToCart


    /**
     * Removes an existing item from the cart of the logged-in user.
     * @param item The item being removed from the cart.
     * @param user The user who is removing something from their cart.
     * @return true if the item is successfully added to the cart, false otherwise.
     */  
    public boolean rmItemFromCart(String itemId, User user) {
        final String sql = "delete from cart where (username, item_id) = (?, ?)";

        try (Connection conn = dataSource.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Following lines replace the first place holder with the current userId, and the second with itemId
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, itemId);
            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } // try
        return false;
    } // rmItemFromCart

    /**
     * Clears the cart of the logged-in user.
     * @param user The user who is clearing their cart.
     * @return true if the cart is successfully cleared, false otherwise.
     */  
    public boolean emptyCart(User user) {
        final String sql = "select * from cart where username = ?";
        boolean complete = false;

        try (Connection conn = dataSource.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUserId());
            List<Item> itemList = new ArrayList<>();

            // get all items in the user's cart
            try (ResultSet rs = pstmt.executeQuery()) {
                // Traverse the result rows one at a time.
                while (rs.next()) {
                    // Initialize and retain the Item
                    String itemId = rs.getString("item_id");
                    itemList.add(itemService.getSpecificItem(itemId));
                } // while    
            
                while(!itemList.isEmpty()) {
                    // remove the first element from the itemList from the cart, then drop the item from itemList
                    complete = rmItemFromCart(itemList.get(0).getItemId(), user);
                    itemList.remove(0);

                    // if rmItemFromCart failed, complete will be false. Some error occured, so we stop clearing the cart
                    if(!complete) {
                        return complete;
                    } // if
                } // while
            } // try (inner)
            return complete;
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } // try
        return complete;
    } // emptyCart
    

    /**
     * Retrieves all items in the user's cart and their amounts
     * @param user the user in question
     * @return the user's cart
     */
    public Cart getCart(User user) {
        List<Item> itemList = new ArrayList<>();
        List<Integer> intList = new ArrayList<>();
        final String sql = "select * from cart where username = ?";
        
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
            pstmt.setString(1, user.getUserId());
            try (ResultSet rs = pstmt.executeQuery()) {
                // Traverse the result rows one at a time.
                while (rs.next()) {
                    // Initialize and retain the Item
                    String itemId = rs.getString("item_id");
                    int itemQ = rs.getInt("quantity");

                    itemList.add(itemService.getSpecificItem(itemId));
                    intList.add(itemQ);
                } // while    
            return new Cart(user.getUserId(), itemList, intList);
            } // try (inner)        
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } // try
        return new Cart(user.getUserId(), itemList, intList);
    } // getCart


    /**
     * Retrieves the price of all items in the user's cart
     * @param user the user in question
     * @return the user's cart
     */
    public double getCartPrice(User user) {
        System.out.println("fetching the cart of user: " + user.getUserId());
        final String sql = "select (c.quantity * i.item_price) as total from cart c, items i where c.item_id = i.item_id and c.username = ?";
        double total = 0;
        double t = 0;
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
            pstmt.setString(1, user.getUserId());
            try (ResultSet rs = pstmt.executeQuery()) {
                // should only have one row
                while (rs.next()) {
                    total = total + rs.getDouble("total");
                } // while   
            t = Math.round(total * 100.0) / 100.0;
            System.out.println("total: " + String.valueOf(t)); 
            return t;
            } // try (inner)  
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } // try

        return Math.round(total * 100.0) / 100.0;
    } // getCartPrice

} // CartService
