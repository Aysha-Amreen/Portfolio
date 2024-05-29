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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uga.menik.cs4370.models.Cart;
import uga.menik.cs4370.models.User;
import uga.menik.cs4370.services.CartService;
import uga.menik.cs4370.services.OrderService;
import uga.menik.cs4370.services.ItemService;
import uga.menik.cs4370.services.UserService;

/**
 * This controller handles the menu page.
 */
@Controller
@RequestMapping("/cart")
public class CartController {

    
    private final UserService userService;
    private final ItemService itemService;
    private final CartService cartService;
    private final OrderService orderService;

    @Autowired
    public CartController (UserService userService, ItemService itemService, CartService cartService, OrderService orderService) {
        this.userService = userService;
        this.itemService = itemService;
        this.cartService = cartService;
        this.orderService = orderService;
    } // CartController constructor

    /**
     * This is the specific function that handles the root URL itself.
     * 
     * Note that this accepts a URL parameter called error.
     * The value to this parameter can be shown to the user as an error message.
     * See notes in HashtagSearchController.java regarding URL parameters.
     */
    @GetMapping
    public ModelAndView webpage(@RequestParam(name = "error", required = false) String error) {
        ModelAndView mv = new ModelAndView("cart_page");
        User currentUser = userService.getLoggedInUser();
        Cart cart = cartService.getCart(currentUser);
        double price = cartService.getCartPrice(currentUser);
        
        try {
            mv.addObject("cart", cart);
            mv.addObject("cartPrice", price);
        } catch (Exception e) {
            System.err.println("Some error occured!" + e.getMessage());
            // If an error occured, show the error message to the user.
            String errorMessage = e.getMessage();
            mv.addObject("errorMessage", errorMessage);
        }

        // show no content message if your content list is empty.
        if (cart.getItems().isEmpty()) {
            mv.addObject("isNoContent", true);
        } else {
            mv.addObject("isNoContent", false);
        } // if
        
        return mv;
    } // ModelAndView webpage

    /**
     * 
     */
    @PostMapping("/remove/{itemId}")
    public String removeItem(@PathVariable("itemId") String itemId) {
        
        try {
            User currentUser = userService.getLoggedInUser();
            boolean success = cartService.rmItemFromCart(itemId, currentUser);
            
            if (success) {
                System.out.println("Removed item from Cart!");
                // Redirect the user if the comment adding is a success.
                return "redirect:/cart";
            } else {
                // Redirect the user with an error message if there was an error.
                String message = URLEncoder.encode("Failed to add to cart. Please try again.", 
                        StandardCharsets.UTF_8);
                return "redirect:/cart"+ "?error=" + message;
            } // if
        } catch (Exception e) {
            System.err.println("Some error occured!" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    } // removeItem

    /**
     * 
     */
    @PostMapping("/placeorder")
    public String placeOrder() {
        try {
            User currentUser = userService.getLoggedInUser();
            boolean success = orderService.makeOrder(currentUser);
            
            if (success) {
                System.out.println("Placed Order!");
                // Redirect the user if the comment adding is a success.
                return "redirect:/orders";
            } else {
                // Redirect the user with an error message if there was an error.
                System.out.println(success);
                String message = URLEncoder.encode("Failed to place order. Please try again.", 
                        StandardCharsets.UTF_8);
                return "redirect:/cart"+ "?error=" + message;
            } // if
        } catch (Exception e) {
            System.err.println("Some error occured!" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    } // placeOrder

} // MenuController