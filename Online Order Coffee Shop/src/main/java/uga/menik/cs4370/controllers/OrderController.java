package uga.menik.cs4370.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uga.menik.cs4370.models.Item;
import uga.menik.cs4370.models.Order;
import uga.menik.cs4370.services.OrderService;
import uga.menik.cs4370.services.UserService;

/**
 * This controller handles the menu page.
 */
@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderController (OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    } // OrderController constructor

    @GetMapping
    public ModelAndView webpage(@RequestParam(name = "error", required = false) String error) {
        ModelAndView mv = new ModelAndView("order_page");
        List<Order> previousOrders = new ArrayList<>();

        System.out.println("Attempting to load orders from user: " + userService.getLoggedInUser().getUserId());
        try {
            // Following line populates with popular items.
            previousOrders = orderService.getOrders(userService.getLoggedInUser());
            mv.addObject("orders", previousOrders);
        } catch (Exception e) {
            System.err.println("Some error occured!" + e.getMessage());
            // If an error occured, show the error message to the user.
            String errorMessage = e.getMessage();
            mv.addObject("errorMessage", errorMessage);
        }

        // show no content message if your content list is empty.
        if (previousOrders.isEmpty()) {
            mv.addObject("isNoContent", true);
        } else {
            mv.addObject("isNoContent", false);
        } // if
        
        return mv;
    } // ModelAndView webpage    
} // orderController
