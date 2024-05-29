'use client'
import { useState, useEffect } from 'react';

import '../css/RecipeCard.css'

const AddRecipe = (props) => {
  
  const [enteredIngredient1, setIngredient1] = useState('');
  const [enteredIngredient2, setIngredient2] = useState('');
  const [enteredIngredient3, setIngredient3] = useState('');
  const [enteredIngredient4, setIngredient4] = useState('');
  const [enteredAmount1, setAmount1] = useState('');
  const [enteredAmount2, setAmount2] = useState('');
  const [enteredAmount3, setAmount3] = useState('');
  const [enteredAmount4, setAmount4] = useState('');
  const [enteredSize, setSize] = useState('');
  const [enteredTime, setTime] = useState('');
  const [enteredCuisine, setCuisine] = useState('');
  const [enteredMeal, setMeal] = useState('');
  const [enteredRestrictions, setRestrictions] = useState('');

  const ingredient1ChangeHandler = (event) => {
    setIngredient1(event.target.value);
  };
  const ingredient2ChangeHandler = (event) => {
    setIngredient2(event.target.value);
  };
  const ingredient3ChangeHandler = (event) => {
    setIngredient3(event.target.value);
  };
  const ingredient4ChangeHandler = (event) => {
    setIngredient4(event.target.value);
  };
  const amount1ChangeHandler = (event) => {
    setAmount1(event.target.value);
  };
  const amount2ChangeHandler = (event) => {
    setAmount2(event.target.value);
  };
  const amount3ChangeHandler = (event) => {
    setAmount3(event.target.value);
  };
  const amount4ChangeHandler = (event) => {
    setAmount4(event.target.value);
  };
  const sizeChangeHandler = (event) => {
    setSize(event.target.value);
  };
  const timeChangeHandler = (event) => {
    setTime(event.target.value);
  };
  const cuisineChangeHandler = (event) => {
    setCuisine(event.target.value);
  };
  const mealChangeHandler = (event) => {
    setMeal(event.target.value);
  };
  const restrictionsChangeHandler = (event) => {
    setRestrictions(event.target.value);
  };

  const submitHandler = (event) => {
    event.preventDefault();

    const formData = {
      ingredient1: enteredIngredient1,
      amount1: enteredAmount1,
      ingredient2: enteredIngredient2,
      amount2: enteredAmount2,
      ingredient3: enteredIngredient3,
      amount3: enteredAmount3,
      ingredient4: enteredIngredient4,
      amount4: enteredAmount4,
      servingSize: enteredSize,
      cookTime: enteredTime,
      cuisineType: enteredCuisine,
      mealType: enteredMeal,
      dietaryRestrictions: enteredRestrictions
    };

    props.onSubmitForm(formData);
    
    console.log(formData);

    setIngredient1('');
    setIngredient2('');
    setIngredient3('');
    setIngredient4('');
    setAmount1('');
    setAmount2('');
    setAmount3('');
    setAmount4('');
    setSize('');
    setTime('');
    setCuisine('');
    setMeal('');
    setRestrictions('');
  };

  return (
    <div className='card'>
      <h1>Generate your own recipe!</h1>
      <form onSubmit={submitHandler}>  
        <div className='cardcontent'>
        <h3>Ingredients: </h3>        
          <div className='inputs'>
            <label>Ingredient 1: </label>
            <input
              type='text'
              value={enteredIngredient1}
              onChange={ingredient1ChangeHandler}
            />
            <label> Amount: </label>
            <input
              type='text'
              value={enteredAmount1}
              onChange={amount1ChangeHandler}
            />
          </div>
          <div className='inputs'>
            <label>Ingredient 2: </label>
            <input
              type='text'
              value={enteredIngredient2}
              onChange={ingredient2ChangeHandler}
            />
            <label> Amount: </label>
            <input
              type='text'
              value={enteredAmount2}
              onChange={amount2ChangeHandler}
            />
          </div>
          <div className='inputs'>
            <label>Ingredient 3: </label>
            <input
              type='text'
              value={enteredIngredient3}
              onChange={ingredient3ChangeHandler}
            />
            <label> Amount: </label>
            <input
              type='text'
              value={enteredAmount3}
              onChange={amount3ChangeHandler}
            />
          </div>
          <div className='inputs'>
            <label>Ingredient 4: </label>
            <input
              type='text'
              value={enteredIngredient4}
              onChange={ingredient4ChangeHandler}
            />
            <label> Amount: </label>
            <input
              type='text'
              value={enteredAmount4}
              onChange={amount4ChangeHandler}
            />
          </div>
          <h3>Serving Size: </h3>
          <div className='inputs'>
            <input
              type='number'
              min='1.0'
              step='1.0'
              value={enteredSize}
              onChange={sizeChangeHandler}
            />
          </div>
          <h3>Total Cooking Time (in minutes): </h3>
          <div className='inputs'>
            <input
              type='number'
              min='1.0'
              step='1.0'
              value={enteredTime}
              onChange={timeChangeHandler}
            />
          </div>
          <h3>Cuisine Type: </h3>
          <div className='inputs'>
            <input
              type='text'
              value={enteredCuisine}
              onChange={cuisineChangeHandler}
            />
          </div>
          <h3>Meal Type: </h3>
          <div className='inputs'>
            <select
              value={enteredMeal}
              onChange={mealChangeHandler}
            >
              <option value="">Select...</option>
              <option value="breakfast">Breakfast</option>
              <option value="lunch">Lunch</option>
              <option value="dinner">Dinner</option>
              <option value="snacks">Snacks</option>
              <option value="dessert">Dessert</option>
            </select>
          </div>
          <h3>Dietary Restrictions: </h3>
          <div className='inputs'>
            <input
              type='text'
              value={enteredRestrictions}
              onChange={restrictionsChangeHandler}
            />
          </div>
        </div>
        <div className='card-buttons'>
          <button id='create' type='submit'>Generate Recipe</button>
          {props.children}
        </div>
      </form>
    </div>
  );
}

export default AddRecipe;