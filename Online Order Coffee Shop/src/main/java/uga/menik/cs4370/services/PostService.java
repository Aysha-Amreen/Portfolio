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

import uga.menik.cs4370.models.Post;
import uga.menik.cs4370.models.User;
import uga.menik.cs4370.models.ExpandedPost;
import uga.menik.cs4370.models.Comment;

@Service
public class PostService {

    private final DataSource dataSource;
    private final UserService userService;
    private final BookmarkService bookmarkService;

    @Autowired
    public PostService(DataSource dataSource, UserService userService, BookmarkService bookmarkService) {
        this.dataSource = dataSource;
        this.userService = userService;
        this.bookmarkService = bookmarkService;
    }

    /**
     * Creates a new post with the given content for the logged-in user.
     * @param content The content of the post.
     * @param user The user who is creating the post.
     * @return true if the post is successfully created, false otherwise.
     */
    public boolean createPost(String postText, User user) {
        // Adjusted SQL statement to match the provided table schema.
        final String sql = "INSERT INTO post (userId, postDate, postText) VALUES (?, NOW(), ?)";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, postText);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating post: " + e.getMessage());
            return false;
        }
    } // createPost

    /**
     * Returns the most recent posting date of a user.
     * @param userId The user who we are looking for their most recent post date.
     */
    public String lastActivity(String userId) {
        final String sql = "select * from post where userId = ? order by postDate desc";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
            // Traverse the result rows one at a time.
            while (rs.next()) {
                // Note: rs.get... functions access attributes of the current row.
                String storedID = rs.getString("userId");
                boolean isMatch = storedID.equals(userId);
                System.out.println(rs);
                // the first match should be the most recent post
                if (isMatch) {
                    String date = formatDate(rs.getString("postDate")); 
                    return date;                    
                } // if
            } // while
        } // try (inner)
            
        } catch (SQLException e) {
            System.err.println("Error getting last activity: " + e.getMessage());
            return "";
        }
        return "";
    } // lastActivity 
    
    /**
     * Manually formats a date string from "yyyy-MM-dd" to "Mar 22, 2024" format.
     * @param dateStr The date string to format.
     * @return The formatted date string.
     */
    private String formatDate(String dateStr) {
        // Check if the input is null or empty
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return "Invalid Datetime";
        }

        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        
        // Split date and time parts
        String[] parts = dateStr.trim().split(" ");
        if (parts.length < 2) {
            return "Invalid Datetime Format";
        }
        
        String[] dateParts = parts[0].split("-");
        String[] timeParts = parts[1].split(":");

        // Validate expected parts count for date and time
        if (dateParts.length < 3 || timeParts.length < 3) {
            return "Invalid Datetime Components";
        }

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
        }
    } // formatDate

    /**
     * Returns all posts made by a specific user
     * @param currentUserId the current user's ID
     * @param userId the user who's posts you want to retrieve
     * @return a list of every post made by the specified user
     */
    public List<Post> getUserPosts(String currentUserId, String userId) {
        List<Post> userPosts = new ArrayList<>();
        final String sql = "select * from post where userId = ? order by postDate desc";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
            // Traverse the result rows one at a time.
                while (rs.next()) {
                    // Note: rs.get... functions access attributes of the current row.
                    String storedID = rs.getString("userId");
                    boolean isMatch = storedID.equals(userId);
                    System.out.println(rs);
                    if (isMatch) {
                        String date = rs.getString("postDate"); 
                        String postId = rs.getString("postId"); 
                        String postText = rs.getString("postText"); 
                        String formattedDate = formatDate(date);

                        userPosts.add(
                                new Post(postId, postText, formattedDate, 
                                userService.getSpecificUser(userId), heartCount(postId), 
                                findCommentsByPostId(postId).size(), 
                                hearted(currentUserId, postId), 
                                bookmarkService.bookmarked(currentUserId, postId)));  
                    } // if
                } // while
            } // try (inner)
            
        } catch (SQLException e) {
            System.err.println("Error getting user posts: " + e.getMessage());
        } // try
        return userPosts;
    } // getUserPosts

    /**
     * This function returns all users that are followed by the current user.
     * @param currentUserId the current user's ID
     * @return a list of followable users that the user is currently following
     */
    public List<String> getHeartedPosts(String currentUserId) {
        List<String> heartedPostIds = new ArrayList<>();

        // Write an SQL query to find all users that the current user follows. currentUserId = followerId
        final String sql = "select * from heart where userId = ?";

        try (Connection conn = dataSource.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Following line replaces the first place holder with currentUserId
            pstmt.setString(1, currentUserId);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Traverse the result rows one at a time.
                while (rs.next()) {
                    // Note: rs.get... functions access attributes of the current row.
                    String storedID = rs.getString("userId");
                    boolean isMatch = storedID.equals(currentUserId);
                    System.out.println(rs);
                    
                    if (isMatch) {
                        heartedPostIds.add(rs.getString("postId"));                      
                    } // if
                } // while
            } // try (inner)
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } // try
        return heartedPostIds;
    } // getHeartedPosts

    /**
     * This function returns a T/F value based on whether the current user  
     * has liked a specific post.
     * @param currentUserId the current user's ID
     * @param postId the ID of the post
     * @return true if the current user has hearted the post, false if not.
     */
    public boolean hearted(String currentUserId, String postId) {
        List<String> allHearts = getHeartedPosts(currentUserId);

        List<String> postIds = new ArrayList<>();
        allHearts.forEach(post -> postIds.add(post));

        if(postIds.contains(postId)) {
            return true;
        } else {
            return false;
        } // if
    } // hearted

    /**
     * This function returns a T/F value if insertion of a new heart relation into the db is successful.
     * @param currentUserId the current user's ID
     * @param postId the post ID that the user has liked
     */    
    public boolean addHeart(String currentUserId, String postId) {
        final String sql = "insert into heart (postId, userId) values(?, ?)";

        try (Connection conn = dataSource.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Following lines replace the first place holder with currentUserId, and the second with otherId
            pstmt.setString(2, currentUserId);
            pstmt.setString(1, postId);
            int rowsAffected = pstmt.executeUpdate();

            System.out.println(currentUserId + " has liked " + postId);

            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } // try
        return false;

    } // addHeart

    /**
     * This function returns a T/F value if deletion of a heart relation into the db is successful.
     * @param currentUserId the current user's ID
     * @param postId the post ID that the user has removed a like from
     */    
    public boolean rmHeart(String currentUserId, String postId) {
        final String sql = "delete from heart where (postId, userId) = (?, ?)";

        try (Connection conn = dataSource.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Following lines replace the first place holder with currentUserId, and the second with otherId
            pstmt.setString(2, currentUserId);
            pstmt.setString(1, postId);
            int rowsAffected = pstmt.executeUpdate();

            System.out.println(currentUserId + " has liked " + postId);

            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } // try
        return false;

    } // rmHeart

    /**
     * Will return an integer amount of likes a post has received
     * @param postId the post ID that we are searching for  
     * @return the amount of likes the post has received
     */
    public Integer heartCount(String postId) {
        final String sql = "select * from heart where postId = ?";
        int counter = 0;
        try (Connection conn = dataSource.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Following line replaces the first place holder with currentUserId
            pstmt.setString(1, postId);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Traverse the result rows one at a time.
                while (rs.next()) {
                    counter++;
                } // while
            } // try
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } // try

        return counter;
    } // heartCount

    /**
     * This function should query and return all posts that 
     * are bookmarked by user. 
     * @param userID current user
     * @return List of bookmarked posts
     */
    public List<Post> getBookmarkedPosts(String userId) {
       
        List<String> bookmarkedPostIds = bookmarkService.getBookmarkedIDs(userId);
        List<Post> bookmarkedPosts = new ArrayList<>();

        for (String postId : bookmarkedPostIds) {
            bookmarkedPosts.add(findPostById(userId, postId));
        }

        return bookmarkedPosts;
    } // getBookmarkedPosts

    /**
     * Finds the most recent post made by the userID.
     * @return BasicPost that is the most recent post
     */
    public String getMostRecentPostID(String userID, String postContent) {
        // sql statement to find most recent post by user
        final String sql = "SELECT * FROM post WHERE userId = ? ORDER BY CAST(postId AS UNSIGNED) DESC LIMIT 1";
        String postID = "";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userID);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Move the cursor to the first (and only, in this case) row of the result set
                if (rs.next()) { // Check if the result set is not empty
                    // Now it's safe to access the data
                    if (postContent.equals(rs.getString("postText"))) {
                        postID = rs.getString("postId");
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error processing result set: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving postID: " + e.getMessage());
        }
        return postID;
    } // getMostRecentPostID

    public Post findPostById(String currentUserId, String postId) {
        final String sql = "SELECT * FROM post WHERE postId = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String userId = rs.getString("userId");
                    String postText = rs.getString("postText");
                    String postDate = rs.getString("postDate");
                    String formattedDate = formatDate(postDate);
    
                    // Fetch the user for this post
                    User user = userService.getSpecificUser(userId);
    
                    // Adjust the logic to fetch these values if available in your schema or through additional queries.
                    return new Post(
                            postId,
                            postText,
                            formattedDate,
                            user,
                            heartCount(postId), 
                            findCommentsByPostId(postId).size(), 
                            hearted(currentUserId, postId), 
                            bookmarkService.bookmarked(currentUserId, postId)
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching post by ID: " + e.getMessage());
        }
        return null; // Consider throwing a custom exception or handling this case appropriately
    } // findPostById

    public List<Comment> findCommentsByPostId(String postId) {
        List<Comment> comments = new ArrayList<>();
        final String sql = "SELECT * FROM comment c, user u WHERE c.userId = u.userId and c.postId = ? ORDER BY c.commentDate DESC";
    
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, postId);
    
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User(
                        rs.getString("userId"),
                        rs.getString("firstName"),
                        rs.getString("lastName")
                    );

                    String formattedDate = formatDate(rs.getString("commentDate"));
                    Comment comment = new Comment(
                        rs.getString("commentId"),
                        rs.getString("commentText"),
                        formattedDate,
                        user
                    );
                    comments.add(comment);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding comments by post ID: " + e.getMessage());
        }
        return comments;
    } // findCommentsByPostId

    public ExpandedPost findExpandedPostById(String currentUserId, String postId) {
        Post post = findPostById(currentUserId, postId); 
        List<Comment> comments = findCommentsByPostId(postId);

        // Assuming Post has a constructor that matches these parameters.
        return new ExpandedPost(
            post.getPostId(), 
            post.getContent(), 
            post.getPostDate(),
            post.getUser(), 
            post.getHeartsCount(), 
            comments.size(), 
            post.getHearted(), 
            post.isBookmarked(), 
            comments);
    } // findExpandedPostById

    public boolean createComment(String postId, String userId, String commentText)  {
        final String sql = "INSERT INTO comment (postId, userId, commentDate, commentText) VALUES (?, ?, NOW(), ?)";
    
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, postId);
            pstmt.setString(2, userId);
            pstmt.setString(3, commentText);
    
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
    
        } catch (SQLException e) {
            System.err.println("Error creating comment: " + e.getMessage());
            return false;
        }
    } // createComment
} // postService
