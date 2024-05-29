'use client'
import '../css/App.css';
import '../css/RecipeCard.css';
import React, { useState } from 'react';

const EditRecipe = ({ recipe, onUpdate }) => {
    const [editedRecipe, setEditedRecipe] = useState({ ...recipe });

    const handleChange = (event) => {
        const { name, value } = event.target;
        setEditedRecipe({ ...editedRecipe, [name]: value });
    };

    const handleUpdate = () => {
        onUpdate(editedRecipe);
    };

    return (
        <div className='card'>
            <form>
            <div className='cardcontent'>
                <h2>Edit Recipe</h2>
                <div className='inputs'>
                    <label>Title:</label>
                    <input
                        type="text"
                        name="title"
                        value={editedRecipe.title}
                        onChange={handleChange}
                    />
                </div>
                <div className='inputs'>
                    <label>Image URL:</label>
                    <input
                        type="text"
                        name="img"
                        value={editedRecipe.img}
                        onChange={handleChange}
                    />
                </div>
                <div className='inputs'>
                    <label>Description:</label>
                    <input
                        type="text"
                        name="description"
                        value={editedRecipe.description}
                        onChange={handleChange}
                    />
                </div>
            <div>
                <button id='create' onClick={handleUpdate}>Update</button>
            </div>
            </div>
            </form>
        </div>
    );
};

export default EditRecipe;