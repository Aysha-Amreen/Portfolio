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


@Service
public class BookmarkService {

    private final DataSource dataSource;
    @Autowired
    public BookmarkService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * This function returns a T/F value based on whether the current user  
     * has bookmarked a specific post.
     * @param currentUserId the current user's ID
     * @param postId the ID of the post
     * @return true if the current user has bookmarked the post, false if not.
     */
    public boolean bookmarked(String currentUserId, String postId) {
        List<String> bookmarkedPostIds = getBookmarkedIDs(currentUserId);
        
        if(bookmarkedPostIds.contains(postId)) {
            return true;
        } else {
            return false;
        } // if        
    } // bookmarked
    
    /**
     * This function returns postIds of posts bookmarked by the current user.  
     * @param currentUserId the current user's ID
     * @return list of bookmarked IDs.
     */
    public List<String> getBookmarkedIDs(String currentUserId) {
        List<String> bookmarkedPostIds = new ArrayList<>();
        // SQL query to find posts bookmarked by the current user.
        final String sql = "SELECT * FROM bookmark WHERE userId = ?";
        
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, currentUserId);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Traverse the result rows one at a time.
                while (rs.next()) {
                    System.out.println(rs);
                    String bookmarkedId = rs.getString("postId");
                    bookmarkedPostIds.add(bookmarkedId);                      
                } // while
            } // try (inner)
            
            return bookmarkedPostIds;

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            return bookmarkedPostIds;
        } // try
    } // getBookmarkedIDs

    /**
     * Bookmarks an existing post for the logged-in user.
     * @param postID post to bookmark.
     * @param currentID logged in user.
     * @return true if the bookmark is successfully created, false otherwise.
     */
    public boolean addBookmark(String postID, String currentID) {
        // SQL statement to create new bookmark relation.
        final String sql = "INSERT INTO bookmark (postID, userId) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, postID);
            pstmt.setString(2, currentID);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error bookmarking post: " + e.getMessage());
            return false;
        }
    } // addBookmark

    /**
     * Unbookmarks a bookmarked post for the logged-in user.
     * @param postID post to unbookmark.
     * @param currentID logged in user.
     * @return true if the bookmark is successfully created, false otherwise.
     */
    public boolean removeBookmark(String postID, String currentID) {
        // SQL statement to remove a bookmark relation.
        final String sql = "DELETE FROM bookmark WHERE postID = ? AND userId = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, postID);
            pstmt.setString(2, currentID);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error bookmarking post: " + e.getMessage());
            return false;
        }
    } // remove Bookmark

} // BookmarkService
