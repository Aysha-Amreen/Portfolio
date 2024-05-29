'use client'
import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import OpenAI from "openai";
import axios from 'axios';

import AddRecipe from '../components/AddRecipe';
import '../css/App.css';

const Home = () => {

    const router = useRouter();
    const openai = new OpenAI({
        apiKey: 'sk-proj-hFTB8ORm4wbOPhzaP5qmT3BlbkFJpqMLd9F4qZO7SOSsdlt4',
        dangerouslyAllowBrowser: true,
    });

    const cancelHandler = () => {
        // Programmatically navigate to authorized view
        router.push('/authorized-user');
    };

    function formatUserInput(formData) {
        let ingredientsList = [];
        for (let i = 1; i <= 4; i++) { // Assuming there are always 4 ingredients
            if (formData[`ingredient${i}`] && formData[`amount${i}`]) {
                ingredientsList.push(`${formData[`ingredient${i}`]}: ${formData[`amount${i}`]}`);
            }
        }
        return `Ingredients: ${ingredientsList.join(', ')}, Serving Size: ${formData.servingSize}, Cook Time: ${formData.cookTime}, Cuisine Type: ${formData.cuisineType}, Meal Type: ${formData.mealType}, Dietary Restrictions: ${formData.dietaryRestrictions}`;
    }

    async function promptAI(user_input) {
        const completion = await openai.chat.completions.create({
          messages: [
            { 
                role: "system", 
                content: "You are a helpful chef. I will provide you with a list of ingredients with their quantities, serving size, cook time, meal type, cuisine type, and dietary restrictions. You will return a recipe, in JSON format with the fields title (String), description (String), Ingredients (String array), and Instructions (String array), that fulfill the requirements mentioned, with the directions and the instructions." 
            },
            {
                role: "user",
                content: user_input
            }
        ],
          model: "gpt-3.5-turbo",
          response_format: { "type": "json_object" },
        });
        console.log("AI User input: ", user_input);
        return completion.choices[0].message.content;        
    }

    async function fetchImageForRecipe(title) {
        const url = `https://api.unsplash.com/search/photos?page=1&query=${encodeURIComponent(title)}&client_id=NQLRmhm3lK8mD96bVlioRUNxBrXUUQ-d2jYSYDnlk5E`;
    
        try {
            const response = await fetch(url);
            const data = await response.json();
            if (data && data.results && data.results.length > 0) {
                // Return the URL of the first image
                return data.results[0].urls.regular;
            }
            return null; // or a default image URL
        } catch (error) {
            console.error("Failed to fetch image from Unsplash:", error);
            return null; // or a default image URL
        }
    }    

    const recipeGenerator = async (formData) => {
        const userToken = localStorage.getItem('token');  // Retrieve the token from localStorage
        console.log("Retrieved Token:", userToken);

        if (!userToken) {
            console.error("No user token found");
            return;  
        }

        console.log('Form Data Received for Submission:', formData);   
        const user_input = formatUserInput(formData);
        console.log('Formatted User Input:', formData); 
        try {
            const aiResponse = await promptAI(user_input); // await the response
            console.log("AI response: ", aiResponse);
            const aiResponseData = JSON.parse(aiResponse);
            const imageUrl = await fetchImageForRecipe(aiResponseData.title); // Fetch the image based on the title
            const recipeWithImage = { ...aiResponseData, image: imageUrl }; // Store image URL under the key 'image'

            // Post the recipe to the MongoDB database via your API
            const postResult = await axios.post('http://localhost:8082/api/recipe', recipeWithImage, {
                headers: {
                  Authorization: `Bearer ${userToken}`
                }
              });
            console.log('Recipe successfully saved:', postResult.data);

            // Alert user that the recipe was added successfully
            alert("Recipe added successfully!");
    
            // Navigate to the view-recipe page with the new recipe ID
            router.push(`/view-recipe?id=${postResult.data.id}`);
        } catch (error) {
            console.error("Error handling AI response:", error);
            console.log(error.response);
            alert("Failed to add the recipe.");
        }
    };
    
    return (
        <div>
        <AddRecipe onSubmitForm={recipeGenerator} >
            <button id='cancel' onClick={cancelHandler}>Cancel</button>            
        </AddRecipe>
        </div>
    );
}

export default Home;