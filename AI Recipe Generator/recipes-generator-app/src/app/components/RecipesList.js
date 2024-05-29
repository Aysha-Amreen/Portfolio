import React from 'react';
import Recipe from './Recipe';
import "../css/RecipesList.css";

const RecipesList = ({ items, onEdit, onDelete }) => {
    return (
        <div className='recipe-grid'>
            {items.map((recipe) => (
                <Recipe
                    key={recipe._id}
                    id={recipe._id}
                    title={recipe.title}
                    img={recipe.image}
                    description={recipe.description}
                    onEdit={onEdit}
                    onDelete={onDelete}
                />
            ))}
        </div>
    );
}

export default RecipesList;
