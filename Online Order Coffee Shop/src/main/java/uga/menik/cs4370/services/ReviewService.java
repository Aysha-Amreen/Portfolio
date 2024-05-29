package uga.menik.cs4370.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uga.menik.cs4370.models.Review;
import uga.menik.cs4370.models.User;

@Service
public class ReviewService {

    private final DataSource dataSource;
    private final UserService userService;
    private final ItemService itemService;

    @Autowired
    public ReviewService(DataSource dataSource, UserService userService, ItemService itemService) {
        this.dataSource = dataSource;
        this.userService = userService;
        this.itemService = itemService;
    } // ReviewService


    /**
     * Creates a new review with the given content for the logged-in user.
     * @param reviewText The content of the review.
     * @param itemId     The item if of the item being reviewed
     * @param user       The user who is creating the review.
     * @return true if the review is successfully created, false otherwise.
     */
    public boolean createReview(String reviewText, String itemId, User user) {
        // Adjusted SQL statement to match the provided table schema.
        final String sql = "INSERT INTO review (username, item_id, review_date, review_text) VALUES (?, ?, NOW(), ?)";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, itemId);
            pstmt.setString(3, reviewText);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating review: " + e.getMessage());
            return false;
        } // try
    } // createReview


    /**
     * Returns a specific item's reviews made by any user
     * @param itemId the item who's reviews you want to retrieve
     * @return a list of every review made for the specified item
     */
    public Review findReviewById(String reviewId) {
        final String sql = "SELECT * FROM reviews WHERE review_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, reviewId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String userId = rs.getString("username");
                    String reviewText = rs.getString("review_text");
                    String postDate = rs.getString("review_date");
                    String formattedDate = itemService.formatDate(postDate);
                    String item = rs.getString("item_id");
    
                    User user = userService.getSpecificUser(userId);
                    
                    return new Review(
                        reviewId,
                        reviewText,
                        formattedDate,
                        user,
                        itemService.getSpecificItem(item)
                    );
                } // if
            } // try (inner)
        } catch (SQLException e) {
            System.err.println("Error fetching review by ID: " + e.getMessage());
        } // try
        return null; // Consider throwing a custom exception or handling this case appropriately
    } // findReviewById

} // reviewService