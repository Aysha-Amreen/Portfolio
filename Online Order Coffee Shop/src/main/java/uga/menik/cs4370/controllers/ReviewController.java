package uga.menik.cs4370.controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import uga.menik.cs4370.models.ExpandedItem;
import uga.menik.cs4370.models.Item;
import uga.menik.cs4370.services.ReviewService;
import uga.menik.cs4370.services.ItemService;
import uga.menik.cs4370.services.UserService;
import uga.menik.cs4370.models.User;

/**
 * Handles /profile URL and its sub URLs.
 */
@Controller
@RequestMapping("/review")
public class ReviewController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final ItemService itemService;

    /**
     * See notes in AuthInterceptor.java regarding how this works 
     * through dependency injection and inversion of control.
     */
    @Autowired
    public ReviewController(UserService userService, ReviewService reviewService, ItemService itemService) {
        this.userService = userService;
        this.reviewService = reviewService;
        this.itemService = itemService;
    }

    /**
     * Handles request to add reviews to items.
     */
    @PostMapping("/{itemId}")
    public ModelAndView webpage(@PathVariable("itemId") String itemId, @RequestParam(name = "error", required = false) String error) {
        ModelAndView mv = new ModelAndView("review_page");
        Item item = itemService.getSpecificItem(itemId);
        
        try {
            mv.addObject("revItem", item);
        } catch (Exception e) {
            System.err.println("Some error occured!" + e.getMessage());
            // If an error occured, show the error message to the user.
            String errorMessage = e.getMessage();
            mv.addObject("errorMessage", errorMessage);
        }

        return mv;
    } // ModelAndView webpage
    
    /**
     * Handles request to add reviews to items.
     */
    @PostMapping("/{itemId}/createreview")
    public String createReview(@PathVariable("itemId") String itemId, @RequestParam("review") String reviewText) {
        boolean success = false;

        User loggedInUser = userService.getLoggedInUser();
        success = reviewService.createReview(reviewText, itemId, loggedInUser);
        
        if (success) {
            // Redirect the if post creation is successful.
            return "redirect:/item/" + itemId;
        } else {
            // Redirect the user with an error message if there was an error.
            String message = URLEncoder.encode("Failed to create the post. Please try again.", StandardCharsets.UTF_8);
            return "redirect:/?error=" + message;
        }
    }    
}