/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.cs4370.controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uga.menik.cs4370.models.FollowableUser;
import uga.menik.cs4370.services.PeopleService;
import uga.menik.cs4370.services.UserService;

/**
 * Handles /people URL and its sub URL paths.
 */
@Controller
@RequestMapping("/people")
public class PeopleController {
    // Inject UserService and PeopleService instances.
    // See LoginController.java to see how to do this.
    // Hint: Add a constructor with @Autowired annotation.
    
    // UserService has user login and registration related functions.
    private final UserService userService;
    private final PeopleService peopleService;

    @Autowired
    public PeopleController(UserService userService, PeopleService peopleService) {
        this.userService = userService;
        this.peopleService = peopleService;
    }

    /**
     * Serves the /people web page.
     * 
     * Note that this accepts a URL parameter called error.
     * The value to this parameter can be shown to the user as an error message.
     * See notes in HashtagSearchController.java regarding URL parameters.
     */
    @GetMapping
    public ModelAndView webpage(@RequestParam(name = "error", required = false) String error) {
        // See notes on ModelAndView in BookmarksController.java.
        ModelAndView mv = new ModelAndView("people_page");
        List<FollowableUser> followableUsers = new ArrayList<>();

        try {
            // Following line populates followable user data.
            followableUsers = peopleService.getFollowableUsers( userService.getLoggedInUser().getUserId() );
            mv.addObject("users", followableUsers);
        } catch (Exception e) {
            System.err.println("Some error occured!" + e.getMessage());
            // If an error occured, show the error message to the user.
            String errorMessage = e.getMessage();
            mv.addObject("errorMessage", errorMessage);
        }

        // show no content message if your content list is empty.
        if (followableUsers.isEmpty()) {
            mv.addObject("isNoContent", true);
        } else {
            mv.addObject("isNoContent", false);
        }
        
        return mv;
    }

    /**
     * This function handles user follow and unfollow.
     * Note the URL has parameters defined as variables ie: {userId} and {isFollow}.
     * Follow and unfollow is handled by submitting a get type form to this URL 
     * by specifing the userId and the isFollow variables.
     * Learn more here: https://www.w3schools.com/tags/att_form_method.asp
     * An example URL that is handled by this function looks like below:
     * http://localhost:8081/people/1/follow/false
     * The above URL assigns 1 to userId and false to isFollow.
     */
    @GetMapping("{userId}/follow/{isFollow}")
    public String followUnfollowUser(@PathVariable("userId") String userId,
            @PathVariable("isFollow") Boolean isFollow) {
        System.out.println("User is attempting to follow/unfollow a user:");
        System.out.println("\tuserId: " + userId);
        System.out.println("\tisFollow: " + isFollow);
         
        String currentId = userService.getLoggedInUser().getUserId();

        if(!isFollow) {
            //attempt to remove an existing follow
            try {
                boolean followSuccess = peopleService.rmFollow(currentId, userId);
                if (followSuccess) {
                    // Redirect the user if the following is a success.
                    return "redirect:/people";
                } else {
                    // If the follow fails for some reason, redirect to people page with a message.
                    String message = URLEncoder
                            .encode("Follow deletion failed. Please try again.", "UTF-8");
                    return "redirect:/people?error=" + message;
                }
            } catch (Exception e) {
                // If the follow fails for some reason, redirect the user with an error message.
                String message = URLEncoder.encode("Failed to unfollow the user. Please try again.",
                StandardCharsets.UTF_8);
                return "redirect:/people?error=" + message;
            } // try
        } else {
            //attempt to add a new follow
            try {
                boolean followSuccess = peopleService.addFollow(currentId, userId);
                if (followSuccess) {
                    // Redirect the user if the following is a success.
                    return "redirect:/people";
                } else {
                    // If the follow fails for some reason, redirect to people page with a message.
                    String message = URLEncoder
                            .encode("Follow registration failed. Please try again.", "UTF-8");
                    return "redirect:/people?error=" + message;
                }
            } catch (Exception e) {
                // If the follow fails for some reason, redirect the user with an error message.
                String message = URLEncoder.encode("Failed to follow the user. Please try again.",
                StandardCharsets.UTF_8);
                return "redirect:/people?error=" + message;
            } // try
        } // if
    } // followUnfollowUser

} // People Controller
