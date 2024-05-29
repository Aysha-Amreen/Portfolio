'use client';
import React, { useEffect, useState } from 'react';
import { useSearchParams } from 'next/navigation';  
import axios from 'axios';
import '../css/recipeView.css';

const Home = () => {
    const DUMMY_RECIPE = {
        id: 'r1',
        title: 'Beef Ragu',
        image: '/resources/beef-ragu.jpg',
        description: 'A hearty Italian sauce of slow-cooked beef served over pasta.',
        ingredients: [
             'beef, 1 lb',
             'onion, 1' ,
             'garlic, 2 cloves' ,
             'tomato sauce, 1 can',
        ],
        instructions: [
            'Heat oil in a large pot over medium heat.',
            'Add beef and cook until browned.',
            'Add onion and garlic, and cook until softened.',
            'Add tomato sauce and simmer for 1 hour.'
        ]
    };
    let recipe = DUMMY_RECIPE;

    const params = useSearchParams();
    const recipeId = params.get('id');
    const userId = params.get('user');
    console.log('Test: ', params.toString());
    console.log('Params: ', params);
    console.log('recipeId: ', recipeId);
    console.log('userId: ', userId);


    const [loadedRecipe, setLoadedRecipe] = useState(DUMMY_RECIPE);
    const serverUrl = 'http://localhost:8082/api/recipe';
    //use axios to get async recipe data from the server
    const fetchRecipe = async (e) => {
        if (!recipeId) return; // Do not fetch if recipeId is undefined or not present
        try {
            const response = await axios.get(`${serverUrl}/${recipeId}`);
            console.log(response);
            setLoadedRecipe(response.data);
        } catch (error) {
            console.log(error);
        }
    }
    
    useEffect(() => {
        fetchRecipe();
    }, [recipeId])

    // Render loader or no data message if no recipe data is available
    if (!loadedRecipe) {
        return <p>Loading recipe...</p>; // Show loading or handle no recipe data
    }

    //print all the recipe data to the console
    console.log('Recipe: ', loadedRecipe);
    console.log('Ingredients: ', loadedRecipe.ingredients);
    //print recipe in terminal

    return (
        <div>
            <img src={loadedRecipe.image} alt={loadedRecipe.title} />
            <h1>{loadedRecipe.title}</h1>
            <p>{loadedRecipe.description}</p>
            <h2>Ingredients:</h2>
            <ul>
                {loadedRecipe.ingredients.map((ingredient, index) => (
                    <li key={index}>{ingredient}</li>
                ))}
            </ul>
            <h2>Instructions:</h2>
            <ol>
                {loadedRecipe.instructions.map((instruction, index) => (
                    <li key={index}>{instruction}</li>
                ))}
            </ol>
        </div>
    );
};

export default Home;