/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.cs4370.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import uga.menik.cs4370.models.FollowableUser;
import uga.menik.cs4370.models.Post;

/**
 * This service contains people related functions.
 */
@Service
public class PeopleService {
    private final DataSource dataSource;
    private final PostService postService;
    
    @Autowired
    public PeopleService(DataSource dataSource, PostService postService) {
        this.dataSource = dataSource;
        this.postService = postService;
    }
    
    /**
     * This function should query and return all users that 
     * are followable. The list should not contain the user 
     * with id userIdToExclude.
     */
    public List<FollowableUser> getFollowableUsers(String userIdToExclude) {
       
        List<FollowableUser> followableUsers = new ArrayList<>();
        // Write an SQL query to find the users that are not the current user.
        final String sql = "select * from user where not userId = ?";
        
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Following line replaces the first place holder with userId to exclude.
            pstmt.setString(1, userIdToExclude);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Traverse the result rows one at a time.
                while (rs.next()) {
                    // Note: rs.get... functions access attributes of the current row.
                    String storedID = rs.getString("userId");
                    boolean isMatch = !storedID.equals(userIdToExclude);
                    System.out.println(rs);
                    if (isMatch) {
                        String userId = rs.getString("userId");
                        String firstName = rs.getString("firstName");
                        String lastName = rs.getString("lastName");
                        
                        followableUsers.add(new FollowableUser(userId, firstName, lastName,
                        follows(userIdToExclude, userId), postService.lastActivity(userId)));                      
                    } // if
                } // while
            } // try (inner)
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } // try

        return followableUsers;
    } // getFollowableUsers
  
    /**
     * This function returns all users that are followed by the current user.
     * @param currentUserId the current user's ID
     * @return a list of followable users that the user is currently following
     */
    public List<FollowableUser> getFollowedUsers(String currentUserId) {
        List<FollowableUser> followedUsers = new ArrayList<>();

        // Write an SQL query to find all users that the current user follows. currentUserId = followerId
        final String sql = "select userId, firstName, lastName, followerUserId from user, follow where userId = followeeUserId and followerUserId = ?";

        try (Connection conn = dataSource.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Following line replaces the first place holder with currentUserId
            pstmt.setString(1, currentUserId);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Traverse the result rows one at a time.
                while (rs.next()) {
                    // Note: rs.get... functions access attributes of the current row.
                    String storedID = rs.getString("followerUserId");
                    boolean isMatch = storedID.equals(currentUserId);
                    System.out.println(rs);
                    
                    if (isMatch) {
                        String userId = rs.getString("userId");
                        String firstName = rs.getString("firstName");
                        String lastName = rs.getString("lastName");

                        followedUsers.add(new FollowableUser(userId, firstName, lastName,
                        true, postService.lastActivity(userId)));                      
                    } // if
                } // while
            } // try (inner)
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } // try
        return followedUsers;
    } // getFollowedUsers

    /**
     * Will return every post made by a user that the current user follows in descending 
     * chronological order (most recent post will be first in the list)
     * @param currentUserId the current user's ID
     * @return a list of posts comprised of every post made by someone the current user follows
     */
    public List<Post> getFollowedUsersPosts(String currentUserId) {
        List<FollowableUser> followedUsers = getFollowedUsers(currentUserId);
        List<Post> allPosts = new ArrayList<>();
        followedUsers.forEach(followedUser -> allPosts.addAll(postService.getUserPosts(currentUserId, followedUser.getUserId())));

        // sort in descending order (i.e. most recent date first)
        allPosts.sort((p1, p2) -> p2.getPostDate().compareTo(p1.getPostDate()));

        return allPosts;
    } // getFollowedUSersPosts

    /**
     * This function returns a T/F value based on whether the current user  
     * follows another user.
     * @param currentUserId the current user's ID
     * @param otherId the ID of another user
     * @return true if the current user is following the other user, false if not.
     */
    public boolean follows(String currentUserId, String otherId) {
        List<FollowableUser> allFollows = getFollowedUsers(currentUserId);

        List<String> followIds = new ArrayList<>();
        allFollows.forEach(user -> followIds.add(user.getUserId()));

        if(followIds.contains(otherId)) {
            return true;
        } else {
            return false;
        } // if
    } // follows

    /**
     * This function returns a T/F value if insertion of a new follow relation into the db is successful.
     * @param currentUserId the current user's ID
     * @otherId the ID of another user that the current user is attempting to follow
     */    
    public boolean addFollow(String currentUserId, String otherId) {
        final String sql = "insert into follow (followerUserId, followeeUserId) values(?, ?)";

        try (Connection conn = dataSource.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Following lines replace the first place holder with currentUserId, and the second with otherId
            pstmt.setString(1, currentUserId);
            pstmt.setString(2, otherId);
            int rowsAffected = pstmt.executeUpdate();

            System.out.println(currentUserId + " is now following " + otherId);

            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } // try
        return false;

    } // addFollow

    /**
     * This function returns a T/F value if removal of an existing follow relation into the db is successful.
     */    
    public boolean rmFollow(String currentUserId, String otherId) {
        final String sql = "delete from follow where (followerUserId, followeeUserId) = (?, ?)";

        try (Connection conn = dataSource.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Following lines replace the first place holder with currentUserId, and the second with otherId
            pstmt.setString(1, currentUserId);
            pstmt.setString(2, otherId);
            int rowsAffected = pstmt.executeUpdate();

            System.out.println(currentUserId + " is no longer following " + otherId);

            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } // try
        return false;

    } // addFollow

} // PeopleService
