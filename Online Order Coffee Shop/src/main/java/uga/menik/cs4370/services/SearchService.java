package uga.menik.cs4370.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uga.menik.cs4370.models.Review;

@Service
public class SearchService {

    private final DataSource dataSource;
    private final UserService userService;
    private final ReviewService reviewService;

    @Autowired
    public SearchService(DataSource dataSource, UserService userService, ReviewService reviewService) {
        this.dataSource = dataSource;
        this.userService = userService;
        this.reviewService = reviewService;
    } // SearchService controller


    // THIS ENTIRE FILE SHOULD BE USED FOR SEARCH FEATURE: SEARCH FOR ITEMS THAT USE A USER INPUT INGREDIENT
    // KAT DOESN'T WANT TO DO THIS SO IF SOMEONE ELSE CAN TAKE IT THAT'D BE APPRECIATED :)


    /**
     * Adds hashtags by parsing the content of the most recent post made 
     * by the logged-in user. This function is called immediately after a post 
     * is made.
     * @param postContent Content of post.
     */
    public void hashtagPost(String postContent) {

        String currentId = userService.getLoggedInUser().getUserId();
        String postID = reviewService.getMostRecentReviewID(currentId, postContent);
        System.out.println("PostID: " + postID);
        System.out.println("Post content: " + postContent);

        if (!postID.equals("")) {
            // Compile regex to a pattern of the hashtag
            Pattern pattern = Pattern.compile("#\\w+");
            // Create a matcher for the input string
            Matcher matcher = pattern.matcher(postContent);

            // Find and record all hashtags
            while (matcher.find()) {
                System.out.println("matched: " + matcher.group());
                boolean success = addHashtag(matcher.group(), postID);
                if (!success) {
                    System.err.println("Error hashtaging post");            
                } // if
            } // while
        } // if
    } // hashtagPost
    
    /**
     * Adds hashtags by parsing the the content of the most recent post made 
     * by the logged-in user. This function is called immediately after a post 
     * is made.
     * @return true if the hashtag(s) are successfully created, false otherwise.
     */
    public boolean addHashtag(String hashtag, String postID) {
        // SQL statement to create new hashtag relation.
        final String sql = "INSERT INTO hashtag (hashTag, postID) VALUES (?, ?)";
        
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, hashtag);
            pstmt.setString(2, postID);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error hashtaging review: " + e.getMessage());
            return false;
        } // try
    } // addHashtags

    /**
    * This function should query and return all posts that 
    * have the hashtag being searched. 
    * @param searchTags search terms
    * @return List of hashtagged reviews
    */
    public List<Review> getHashtaggedPosts(String searchTags) {
        
        List<Review> hashtaggedPosts = new ArrayList<>();

        // Compile regex to a pattern of the hashtag
        Pattern pattern = Pattern.compile("#\\w+");
        // Create a matcher for the input string
        Matcher matcher = pattern.matcher(searchTags);
        System.out.println("searchTag: " + searchTags);

        List<String> hashtags = new ArrayList<>();
        // Find and record all hashtags
        while (matcher.find()) {
            System.out.println("matched: " + matcher.group());
            hashtags.add(matcher.group());
        } // while

        if (!hashtags.isEmpty()) {
            List<String> hashtaggedPostIds = getHashtaggedIDs(hashtags);
            for (String postId : hashtaggedPostIds) {
                hashtaggedPosts.add(reviewService.findReviewById(postId));
            } // for
        } // if

        return hashtaggedPosts;
    } // getHashtaggedPosts

   /**
    * This function returns postIds of posts with the the hashtag.  
    * @param hashtag the hashTag
    * @return list of hashtagged review IDs.
    */
    public List<String> getHashtaggedIDs(List<String> hashtags) {
        List<String> hashtagPostIds = new ArrayList<>();
        if (hashtags.isEmpty()) return hashtagPostIds;

        // Dynamically build part of the WHERE clause for each hashtag.
        StringJoiner whereInJoiner = new StringJoiner(" AND ");
        for (int i = 0; i < hashtags.size(); i++) {
            whereInJoiner.add("EXISTS (SELECT 1 FROM hashtag h" + i + 
                            " WHERE h" + i + ".postId = p.postId AND h" + i + ".hashTag = ?)");
        } // for
        
        // Complete SQL query selecting posts that have all specified hashtags
        final String sql = "SELECT p.postId FROM post p WHERE " + whereInJoiner.toString()
                + " ORDER BY p.postDate DESC";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the hashtags in the prepared statement
            for (int i = 0; i < hashtags.size(); i++) {
                pstmt.setString(i + 1, hashtags.get(i));
            } // for

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String hashtaggedId = rs.getString("postId");
                    hashtagPostIds.add(hashtaggedId);
                } // while
            } // try

            return hashtagPostIds;

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            return new ArrayList<>();
        } // try
    } // getHashtaggedIDs
    
} // SearchService
