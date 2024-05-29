/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.cs4370.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uga.menik.cs4370.models.Review;
import uga.menik.cs4370.services.ReviewService;
import uga.menik.cs4370.services.UserService;

/**
 * Handles /profile URL and its sub URLs.
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {

    // UserService has user login and registration related functions.
    private final UserService userService;
    private final ReviewService postService;

    /**
     * See notes in AuthInterceptor.java regarding how this works 
     * through dependency injection and inversion of control.
     */
    @Autowired
    public ProfileController(UserService userService, ReviewService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    /**
     * This function handles /profile URL itself.
     * This serves the webpage that shows reviews of the logged in user.
     */
    @GetMapping
    public ModelAndView profileOfLoggedInUser() {
        System.out.println("User is attempting to view profile of the logged in user.");
        return profileOfSpecificUser(userService.getLoggedInUser().getUserId());
    }

    /**
     * This function handles /profile/{userId} URL.
     * This serves the webpage that shows reviews of a speific user given by userId.
     * See comments in PeopleController.java in followUnfollowUser function regarding 
     * how path variables work.
     */
    @GetMapping("/{userId}")
    public ModelAndView profileOfSpecificUser(@PathVariable("userId") String userId) {
        System.out.println("User is attempting to view profile: " + userId);
        
        // See notes on ModelAndView in BookmarksController.java.
        ModelAndView mv = new ModelAndView("reviews_page");
        List<Review> reviews = new ArrayList<>();

        try {
            // Following line populates the profile data with all reviews made by a specific user.
            reviews = postService.getUserReviews(userService.getLoggedInUser().getUserId(), userId);
            mv.addObject("reviews", reviews);
        } catch (Exception e) {
            System.err.println("Some error occured!" + e.getMessage());
            // If an error occured, show the error message to the user.
            String errorMessage = e.getMessage();
            mv.addObject("errorMessage", errorMessage);
        }

        // show no content message if your content list is empty.
        if (reviews.isEmpty()) {
            mv.addObject("isNoContent", true);
        } else {
            mv.addObject("isNoContent", false);
        }

        return mv;
    }
    
}