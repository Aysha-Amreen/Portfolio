/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.cs4370.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import uga.menik.cs4370.models.User;

/**
 * This is a service class that enables user related functions.
 * The class interacts with the database through a dataSource instance.
 * See authenticate and registerUser functions for examples.
 * This service object is spcial. It's lifetime is limited to a user session.
 * Usual services generally have application lifetime.
 */
@Service
@SessionScope
public class UserService {

    // dataSource enables talking to the database.
    private final DataSource dataSource;
    // passwordEncoder is used for password security.
    private final BCryptPasswordEncoder passwordEncoder;
    // This holds the current user
    private User loggedInUser = null;

    /**
     * See AuthInterceptor notes regarding dependency injection and
     * inversion of control.
     */
    @Autowired
    public UserService(DataSource dataSource) {
        this.dataSource = dataSource;
        this.passwordEncoder = new BCryptPasswordEncoder();
    } // UserService controller

    /**
     * Authenticate user given the username and the password and
     * stores user object for the logged in user in session scope.
     * Returns true if authentication is succesful. False otherwise.
     */
    public boolean authenticate(String username, String password) throws SQLException {
        // Note the ? mark in the query. It is a place holder that we will later replace.
        final String sql = "select * from customer where username = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Following line replaces the first place holder with username.
            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Traverse the result rows one at a time.
                // Note: This specific while loop will only run at most once 
                // since username is unique.
                while (rs.next()) {
                    // Note: rs.get.. functions access attributes of the current row.
                    String storedPasswordHash = rs.getString("password");
                    boolean isPassMatch = passwordEncoder.matches(password, storedPasswordHash);
                    if (isPassMatch) {
                        String userId = rs.getString("username");
                        
                        // Initialize and retain the logged in user.
                        loggedInUser = new User(userId);
                    } // if
                    return isPassMatch;
                } // while
            } // try (inner)
        } // try
        return false;
    } // authenticate

    /**
     * Logs out the user.
     */
    public void unAuthenticate() {
        loggedInUser = null;
    } // unAuthenticate

    /**
     * Checks if a user is currently authenticated.
     */
    public boolean isAuthenticated() {
        return loggedInUser != null;
    } // isAuthenticated

    /**
     * Retrieves the currently logged-in user.
     */
    public User getLoggedInUser() {
        return loggedInUser;
    } // getLoggedInUser

    /**
     * Retrieves the specified user.
     * @param userId the user Id in question
     * @return the specified user
     */
    public User getSpecificUser(String userId) {
        User specificUser;
        final String sql = "select * from customer where username = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Following line replaces the first place holder with username.
            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Traverse the result rows one at a time.
                // Note: This specific while loop will only run at most once 
                // since username is unique.
                while (rs.next()) {
                    // Note: rs.get.. functions access attributes of the current row.
                    String storedId = rs.getString("username");
                    boolean isMatch = storedId.equals(userId);
                    if (isMatch) {

                        // Initialize and retain the logged in user.
                        specificUser = new User(userId);
                        return specificUser;
                    } // if
                } // while
            } // try(inner)
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } // try
        
        specificUser = new User("Does not Exist");
        
        return specificUser;
    } // getSpecificUser  

    /**
     * Registers a new user with the given details.
     * Returns true if registration is successful. If the username already exists,
     * a SQLException is thrown due to the unique constraint violation, which should
     * be handled by the caller.
     */
    public boolean registerUser(String username, String password) throws SQLException {
        final String registerSql = "insert into customer (username, password) values (?, ?)";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement registerStmt = conn.prepareStatement(registerSql)) {
            registerStmt.setString(1, username);
            registerStmt.setString(2, passwordEncoder.encode(password));

            // Execute the statement and check if rows are affected.
            int rowsAffected = registerStmt.executeUpdate();
            return rowsAffected > 0;
        } // try
    } // registerUser

} // userService
