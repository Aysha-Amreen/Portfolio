package uga.menik.cs4370.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import uga.menik.cs4370.models.Item;
import uga.menik.cs4370.models.Recipe;
import uga.menik.cs4370.models.Review;
import uga.menik.cs4370.models.User;
import uga.menik.cs4370.models.ExpandedItem;
import uga.menik.cs4370.models.Ingredient;

@Service
public class ItemService {

    private final DataSource dataSource;
    private final UserService userService;

    @Autowired
    public ItemService(DataSource dataSource, UserService userService) {
        this.dataSource = dataSource;
        this.userService = userService;
    } // SearchService controller

    /**
     * Retrieves the specified item from an item id.
     * @param userId the item Id in question
     * @return the specified item
     */
    public Item getSpecificItem(String itemId) {
        Item specificItem;
        final String sql = "select * from items where item_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Following line replaces the first place holder with itemId.
            pstmt.setString(1, itemId);

            try (ResultSet rs = pstmt.executeQuery()) {
                // There should only be one row
                if (rs.next()) {
                    String storedId = rs.getString("item_id");
                    boolean isMatch = storedId.equals(itemId);

                    // String itemId, String name, String category, String size, double price, String rec_id
                    if (isMatch) {
                        // Initialize and retain the item.
                        String name = rs.getString("item_name");
                        String cat = rs.getString("item_cat");
                        String size = rs.getString("item_size");
                        double price = rs.getDouble("item_price");
                        String recId = rs.getString("rec_id");

                        specificItem = new Item(itemId, name, cat, size, price, recId, findReviewCountByItem(itemId));
                        return specificItem;
                    } // if
                } // if
            } // try(inner)
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } // try
        
        specificItem = new Item("Does not Exist");    
        return specificItem;
    } // getSpecificItem

    
    /**
     * Retrieves all ingredients for an item from an item id.
     * @param itemId the item Id in question
     * @return the list of ingredients in the item
     */
    public List<Ingredient> getIngredientsByItem(String itemId) {
        System.out.println("Searching for ingredients for item: " + itemId);
        List<Ingredient> ingList = new ArrayList<>();
        final String sql = "select * from items it, recipe_ingredients ri, ingredients ing where it.rec_id = ri.rec_id and ri.ing_id = ing.ing_id and it.item_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Following line replaces the first place holder with itemId.
            pstmt.setString(1, itemId);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Traverse the result rows one at a time.
                while (rs.next()) {
                    String storedId = rs.getString("item_id");
                    boolean isMatch = storedId.equals(itemId);

                    //String ingId, String ingName, int weight, String meas, double price) 
                    if (isMatch) {
                        // Initialize and retain the Ingredient.
                        String id = rs.getString("ing_id");
                        String name = rs.getString("ing_name");
                        int weight = rs.getInt("ing_weight");
                        String meas = rs.getString("ing_meas");
                        double price = rs.getDouble("ing_price");

                        ingList.add(new Ingredient(id, name, weight, meas, price));
                        
                    } // if
                } // while
                return ingList;
            } // try(inner)
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } // try  
        return ingList;
    } // getIngredientsByItem


    /**
    * This function should return all ingredient names listed in the searchtags 
    * @param searchTags search terms
    * @return List of ingredient names
    */
    public List<String> getIngredientNames(String searchTags) {
        System.out.println("PARSING: " + searchTags);
        List<String> ingredients = new ArrayList<>();
        
        // searchTags is a comma separated string. split searchtags on the commas
        ingredients = Arrays.asList(searchTags.split(","));
        System.out.println(ingredients);
        return ingredients;
    } // getIngredientNames


    /**
     * Retrieves all items that use a specified ingredient.
     * @param ingName the ingredient name in question
     * @return the list of items that contain the ingredients in the recipe
     */
    public List<Item> getItemsByIngredients(String ingNames) {
        List<String> ings = getIngredientNames(ingNames);
        List<Item> itemList = new ArrayList<>();
        final String sql = "select * from items it, recipe_ingredients ri, ingredients ing where it.rec_id = ri.rec_id and ri.ing_id = ing.ing_id and ing.ing_name = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int temp = 0;
            while(temp < ings.size()) {
                System.out.println("searching for items that use: " + ings.get(temp));
                pstmt.setString(1, ings.get(temp).trim());

                try (ResultSet rs = pstmt.executeQuery()) {
                    // Traverse the result rows one at a time.
                    while (rs.next()) {
                        String storedId = rs.getString("ing_name");
                        boolean isMatch = storedId.equalsIgnoreCase(ings.get(temp).trim());

                        if (isMatch) {
            
                            String itemId = rs.getString("item_id");
                            itemList.add(getSpecificItem(itemId));
            
                        } // if
                    } // while (inner)
                } // try(inner)
                temp++;
            } // while (outer)
            return itemList;
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } // try     
        return itemList;
    } // getItemsByIngredients


    /**
     * Retrieves the top 5 ordered items
     * @return the top 5 most ordered items
     */
    public List<Item> topItems() {
        List<Item> itemList = new ArrayList<>();
        final String sql = "SELECT i.item_id, COUNT(io.item_id) AS order_count FROM items_orders io JOIN items i ON io.item_id = i.item_id GROUP BY i.item_id ORDER BY order_count DESC LIMIT 5";
        System.out.println(sql);
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                // Traverse the result rows one at a time.
                while (rs.next()) {
                    // Initialize and retain the Item
                    String itemId = rs.getString("item_id");
                    itemList.add(getSpecificItem(itemId));                    
                } // while    
                return itemList;            
            } // try (inner)
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();
        } // try  
        return itemList;
    } // topItems


    /**
     * Retrieves all orderable items
     * @return all items
     */
    public List<Item> allItems() {
        List<Item> itemList = new ArrayList<>();
        final String sql = "select * from items";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                // Traverse the result rows one at a time.
                while (rs.next()) {
                    // Initialize and retain the Item
                    String itemId= rs.getString("item_id");
                    
                    itemList.add(getSpecificItem(itemId));
                } // while    
            return itemList;
            } // try (inner)
            
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } // try
        
        return itemList;
    } // allItems
    

    /**
     * Retrieves all ordered items from a specific category
     * @param category the item category
     * @return all items from the specified category
     */
    public List<Item> getItemsByCategory(String category) {
        List<Item> itemList = new ArrayList<>();
        final String sql = "select * from items where item_cat = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            try (ResultSet rs = pstmt.executeQuery()) {
                // Traverse the result rows one at a time.
                while (rs.next()) {
                    // Initialize and retain the Item
                    String itemId= rs.getString("item_id");

                    itemList.add(getSpecificItem(itemId));
                } // while    
            return itemList;
            } // try (inner)
            
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } // try
        
        return itemList;
    } // getItemsByCategory


    /**
     * Returns an ExpandedItem version of a specific item
     * @param itemId the specific item's id
     * @return an expendedItem object
     */
    public ExpandedItem findExpandedItemById(String itemId) {
        Item item = getSpecificItem(itemId); 

        return new ExpandedItem(
            item.getItemId(), 
            item.getItemName(), 
            item.getItemCategory(),
            item.getItemSize(),
            item.getItemPrice(),
            item.getItemRecId(),
            findReviewCountByItem(itemId),
            findReviewsByItem(itemId),
            getIngredientsByItem(itemId)
        );
    } // findExpandedPostById


    /**
     * Manually formats a date string from "yyyy-MM-dd" to "Mar 22, 2024" format.
     * @param dateStr The date string to format.
     * @return The formatted date string.
     */
    public String formatDate(String dateStr) {
        // Check if the input is null or empty
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return "Invalid Datetime";
        } // try

        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        
        // Split date and time parts
        String[] parts = dateStr.trim().split(" ");
        if (parts.length < 2) {
            return "Invalid Datetime Format";
        } // try
        
        String[] dateParts = parts[0].split("-");
        String[] timeParts = parts[1].split(":");

        // Validate expected parts count for date and time
        if (dateParts.length < 3 || timeParts.length < 3) {
            return "Invalid Datetime Components";
        } // try

        String year = dateParts[0];
        int month;
        String day;
        
        try {
            month = Integer.parseInt(dateParts[1]);
            day = String.valueOf(Integer.parseInt(dateParts[2])); // Direct conversion to remove leading zero
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);
            
            String ampm = hour >= 12 ? "PM" : "AM";
            hour = hour % 12;
            if (hour == 0) hour = 12; // Convert 0 to 12 for 12 AM/PM

            String formattedTime = String.format("%02d:%02d %s", hour, minute, ampm);

            return months[month - 1] + " " + day + ", " + year + ", " + formattedTime;
            
        } catch (NumberFormatException e) {
            // Handle parsing error
            return "Invalid Datetime Components";
        } // try
    } // formatDate


    /**
     * Returns a specific item's reviews made by any user
     * @param Item the item who's reviews you want to retrieve
     * @return a list of every review made for the specified item
     */
    public List<Review> findReviewsByItem(String itemId) {
        final String sql = "SELECT * FROM review WHERE item_id = ?";
        List<Review> reviews = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, itemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String reviewId = rs.getString("review_id");
                    String userId = rs.getString("username");
                    String reviewText = rs.getString("review_text");
                    String reviewDate = rs.getString("review_date");
                    String formattedDate = formatDate(reviewDate);
                    String item = rs.getString("item_id");

                    User user = userService.getSpecificUser(userId);
                    
                    Review review = new Review(
                            reviewId,
                            reviewText,
                            formattedDate,
                            user,
                            getSpecificItem(item)
                    );
                    reviews.add(review);
                } // while
                return reviews;
            } // try (inner)
        } catch (SQLException e) {
            System.err.println("Error fetching review by ID: " + e.getMessage());
        } // try
        return null; // Consider throwing a custom exception or handling this case appropriately
    } // findReviewByItem


    /**
     * Returns a review count of a specific item made by any user
     * @param itemId the item who's review count you want to retrieve
     * @return a count of the reviews
     */
    public int findReviewCountByItem(String itemId) {
        final String sql = "SELECT COUNT(*) as c FROM items, review WHERE review.item_id = items.item_id and items.item_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, itemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                // should only be one row
                if (rs.next()) {
                    return rs.getInt("c");
                } // while
            } // try (inner)
        } catch (SQLException e) {
            System.err.println("Error fetching review count: " + e.getMessage());
        } // try
        return 0;
    } // findReviewCountByItem


    /**
     * Return the recipe of a specific item
     * @param itemId the id of the item you are searching the recipe of
     * @return a recipe item containing the ingredient information if the item recipe
     */
    public Recipe findRecipeByItem(String itemId) {

        //String recipeId, List<Ingredient> ingredients, List<String> quantities
        final String sql = "select * from recipe_ingredients ri, items i, ingredients ing where ri.rec_id = i.rec_id and ri.ing_id = ing.ing_id and i.item_id = ?";
        List<Ingredient> ingredients = new ArrayList<>();
        List<String> quantities = new ArrayList<>();
        String recId = "";
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, itemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    recId = rs.getString("i.rec_id");
                    String ing_id = rs.getString("ri.ing_id");
                    String ingName = rs.getString("ing.ing_name");
                    int weight = rs.getInt("ing.ing_weight");
                    String meas = rs.getString("ing.ing_meas");
                    double price = rs.getDouble("ing.ing_price");
                    int quantity = rs.getInt("ri.quantity");
                    
                    //String ingId, String ingName, int weight, String meas, double price
                    Ingredient ingredient = new Ingredient(
                            ing_id,
                            ingName,
                            weight,
                            meas,
                            price
                    );
                    ingredients.add(ingredient);

                    String q = String.valueOf(quantity) + " " + meas;
                    quantities.add(q);
                } // while

                Recipe recipe = new Recipe(recId, ingredients, quantities);
                return recipe;
            } // try (inner)
        } catch (SQLException e) {
            System.err.println("Error fetching recipe by item: " + e.getMessage());
        } // try
        
        return new Recipe("Does not exist");

    } // findRecipeByItem

} // ItemService
