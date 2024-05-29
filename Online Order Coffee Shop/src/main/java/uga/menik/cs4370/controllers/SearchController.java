/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.cs4370.controllers;

import java.util.ArrayList;
import java.util.List;
import java.net.URLDecoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uga.menik.cs4370.models.Item;
import uga.menik.cs4370.services.ItemService;

/**
 * Handles /Search URL and possibly others.
 * At this point no other URLs.
*/ 
@Controller
@RequestMapping("/ingredientsearch")
public class SearchController {

    private final ItemService itemService;
    
    @Autowired
    public SearchController(ItemService itemService) {
        this.itemService = itemService;        
    } // SearchController constructor
    
    /**
     * This function handles the /Search URL itself.
     * This URL can process a request parameter with name searches.
     * In the browser the URL will look something like below:
     * http://localhost:8081/Search?searchs=%23amazing+%23fireworks
     * Note: the value of the searches is URL encoded.
    */ 
    @GetMapping
    public ModelAndView webpage(@RequestParam(name = "ingredients") String ingredients) {
        System.out.println("User is searching: " + ingredients);

        // See notes on ModelAndView in BookmarksController.java.
        ModelAndView mv = new ModelAndView("search_page");
        List<Item> items = new ArrayList<>();

        try {
            String decodedSearches = URLDecoder.decode(ingredients, "UTF-8");
            System.out.println("decoded search: " + decodedSearches);
            // Following line populates with actual data from the database.
            items = itemService.getItemsByIngredients(decodedSearches);
            mv.addObject("searchItems", items);
        } catch (Exception e) {
            System.err.println("Some error occured!" + e.getMessage());
            // If an error occured, show the error message to the user.
            String errorMessage = e.getMessage();
            mv.addObject("errorMessage", errorMessage);
        }
        
        // show no content message if your content list is empty.
        if (items.isEmpty()) {
            mv.addObject("isNoContent", true);
        } else {
            mv.addObject("isNoContent", false);
        } // if
        
        return mv;
    } // ModelAndView webpage
    
    
} // SearchController

