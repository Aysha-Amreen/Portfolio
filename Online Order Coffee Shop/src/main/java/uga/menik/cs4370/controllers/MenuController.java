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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uga.menik.cs4370.models.Item;
import uga.menik.cs4370.services.ItemService;

/**
 * This controller handles the menu page.
 */
@Controller
@RequestMapping("/menu")
public class MenuController {

    
    private final ItemService itemService;

    @Autowired
    public MenuController (ItemService itemService) {
        this.itemService = itemService;
    } // MenuController constructor

    /**
     * This is the specific function that handles the root URL itself.
     * 
     * Note that this accepts a URL parameter called error.
     * The value to this parameter can be shown to the user as an error message.
     * See notes in HashtagSearchController.java regarding URL parameters.
     */
    @GetMapping
    public ModelAndView webpage(@RequestParam(name = "error", required = false) String error) {
        ModelAndView mv = new ModelAndView("menu_page");
        List<Item> hotDrinks = new ArrayList<>();
        List<Item> coldDrinks = new ArrayList<>();
        List<Item> snacks = new ArrayList<>();
        List<Item> menuItems = new ArrayList<>();

        try {
            // Following line populates with popular items.
            hotDrinks = itemService.getItemsByCategory("Hot Drinks");
            coldDrinks = itemService.getItemsByCategory("Cold Drinks");
            snacks = itemService.getItemsByCategory("Snacks");
            menuItems.addAll(hotDrinks);
            menuItems.addAll(coldDrinks);
            menuItems.addAll(snacks);
            
            mv.addObject("hotDrinks", hotDrinks);
            mv.addObject("coldDrinks", coldDrinks);
            mv.addObject("snacks", snacks);
            mv.addObject("menuItems", menuItems);
        } catch (Exception e) {
            System.err.println("Some error occured!" + e.getMessage());
            // If an error occured, show the error message to the user.
            String errorMessage = e.getMessage();
            mv.addObject("errorMessage", errorMessage);
        }

        // show no content message if your content list is empty.
        if (menuItems.isEmpty()) {
            mv.addObject("isNoContent", true);
        } else {
            mv.addObject("isNoContent", false);
        } // if
        
        return mv;
    } // ModelAndView webpage

} // MenuController