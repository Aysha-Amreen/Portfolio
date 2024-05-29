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

import uga.menik.cs4370.models.Post;

@Service
public class HashtagService {

    private final DataSource dataSource;
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public HashtagService(DataSource dataSource, UserService userService, PostService postService) {
        this.dataSource = dataSource;
        this.userService = userService;
        this.postService = postService;
    }

    /**
     * Adds hashtags by parsing the content of the most recent post made 
     * by the logged-in user. This function is called immediately after a post 
     * is made.
     * @param postContent Content of post.
     */
    public void hashtagPost(String postContent) {

        String currentId = userService.getLoggedInUser().getUserId();
        String postID = postService.getMostRecentPostID(currentId, postContent);
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
                }
            }
        }
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
            System.err.println("Error hashtaging post: " + e.getMessage());
            return false;
        }
    } // addHashtags

    /**
    * This function should query and return all posts that 
    * have the hashtag being searched. 
    * @param searchTags search terms
    * @return List of bookmarked posts
    */
    public List<Post> getHashtaggedPosts(String searchTags) {
        
        List<Post> hashtaggedPosts = new ArrayList<>();
        String currentId = userService.getLoggedInUser().getUserId();

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
        }

        if (!hashtags.isEmpty()) {
            List<String> hashtaggedPostIds = getHashtaggedIDs(hashtags);
            for (String postId : hashtaggedPostIds) {
                hashtaggedPosts.add(postService.findPostById(currentId, postId));
            }
        }

        return hashtaggedPosts;
    } // getBookmarkedPosts

   /**
    * This function returns postIds of posts with the the hashtag.  
    * @param hashtag the hashTag
    * @return list of bookmarked IDs.
    */
    public List<String> getHashtaggedIDs(List<String> hashtags) {
        List<String> hashtagPostIds = new ArrayList<>();
        if (hashtags.isEmpty()) return hashtagPostIds;

        // Dynamically build part of the WHERE clause for each hashtag.
        StringJoiner whereInJoiner = new StringJoiner(" AND ");
        for (int i = 0; i < hashtags.size(); i++) {
            whereInJoiner.add("EXISTS (SELECT 1 FROM hashtag h" + i + 
                            " WHERE h" + i + ".postId = p.postId AND h" + i + ".hashTag = ?)");
        }
        
        // Complete SQL query selecting posts that have all specified hashtags
        final String sql = "SELECT p.postId FROM post p WHERE " + whereInJoiner.toString()
                + " ORDER BY p.postDate DESC";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the hashtags in the prepared statement
            for (int i = 0; i < hashtags.size(); i++) {
                pstmt.setString(i + 1, hashtags.get(i));
            }

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
    } // getBookmarkedIDs
    
} // HashtagService
