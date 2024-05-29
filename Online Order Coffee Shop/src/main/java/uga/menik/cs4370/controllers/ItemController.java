package uga.menik.cs4370.controllers;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uga.menik.cs4370.models.User;
import uga.menik.cs4370.models.ExpandedItem;
import uga.menik.cs4370.models.Item;
import uga.menik.cs4370.models.Recipe;
import uga.menik.cs4370.services.UserService;
import uga.menik.cs4370.services.ItemService;
import uga.menik.cs4370.services.CartService;

/**
 * Handles /profile URL and its sub URLs.
 */
@Controller
@RequestMapping("/item")
public class ItemController {

    private final UserService userService;
    private final ItemService itemService;
    private final CartService cartService;

    /**
     * See notes in AuthInterceptor.java regarding how this works 
     * through dependency injection and inversion of control.
     */
    @Autowired
    public ItemController(UserService userService, ItemService itemService, CartService cartService) {
        this.userService = userService;
        this.itemService = itemService;
        this.cartService = cartService;
    }

    /**
     * This function handles /profile/{userId} URL.
     * This serves the webpage that shows reviews of a speific user given by userId.
     * See comments in PeopleController.java in followUnfollowUser function regarding 
     * how path variables work.
     */
    @GetMapping("/{itemId}")
    public ModelAndView specificItem(@PathVariable("itemId") String itemId) {
        System.out.println("User is attempting to view item: " + itemId);
        
        ModelAndView mv = new ModelAndView("item_page");
        ExpandedItem specificItem = itemService.findExpandedItemById(itemId);
        Recipe recipe = itemService.findRecipeByItem(itemId);
        System.out.println(specificItem.getReviews().get(0).getUser().getProfileImagePath());
        
        try {
            mv.addObject("item", specificItem);
            mv.addObject("recipe", recipe);
        } catch (Exception e) {
            System.err.println("Some error occured!" + e.getMessage());
            // If an error occured, show the error message to the user.
            String errorMessage = e.getMessage();
            mv.addObject("errorMessage", errorMessage);
        }

        // show no content message if your content list is empty.
        if (specificItem.getReviews().isEmpty()) {
            mv.addObject("isNoContent", true);
            System.out.println("0 reviews");
        } else {
            mv.addObject("isNoContent", false);
        } // if

        return mv;
    }

    /**
     * 
     */
    @PostMapping("/{itemId}/addtocart")
    public String addToCart(@PathVariable("itemId") String itemId, @RequestParam("quantity") int quantity) {
        
        try {
            User currentUser = userService.getLoggedInUser();
            boolean success = cartService.addItemToCart(itemId, currentUser, quantity);
            
            if (success) {
                System.out.println("Added to Cart!");
                // Redirect the user if the comment adding is a success.
                return "redirect:/cart";
            } else {
                // Redirect the user with an error message if there was an error.
                String message = URLEncoder.encode("Failed to add to cart. Please try again.", 
                        StandardCharsets.UTF_8);
                return "redirect:/item/" + itemId + "?error=" + message;
            } // if
        } catch (Exception e) {
            System.err.println("Some error occured!" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    } // addToCart
    
}