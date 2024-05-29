'use client'
import React, { useState, useEffect } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import axios from 'axios';
import RecipesList from '../components/RecipesList';
import Hdr from '../components/Hdr';
import '../css/App.css';

const Home = () => {

  const DUMMY_RECIPES = [
    {
      id: 'r1',
      title: 'Beef Ragu',
      img: '/resources/beef-ragu.jpg',
      description: 'A hearty Italian sauce of slow-cooked beef served over pasta.',
      ingredients: 'dummy ingredients...',
      instrusctions: 'dummy instructions...'
    },
    {
      id: 'r2',
      title: 'Creme Brulee',
      img: '/resources/creme-brulee.jpg',
      description: 'A rich custard base topped with a layer of hard caramel.',
      ingredients: 'dummy ingredients...',
      instrusctions: 'dummy instructions...'
    },
    {
      id: 'r3',
      title: 'Butter Chicken',
      img: '/resources/butter-chicken.jpg',
      description: 'A creamy, spiced tomato-based curry with tender chicken pieces.',
      ingredients: 'dummy ingredients...',
      instrusctions: 'dummy instructions...'
    },
    {
      id: 'r4',
      title: 'Tofu Fried Rice',
      img: '/resources/tofu-fried-rice.jpg',
      description: 'A flavorful stir-fry with seasoned tofu and assorted vegetables.',
      ingredients: 'dummy ingredients...',
      instrusctions: 'dummy instructions...'
    },
    {
      id: 'r5',
      title: 'Kung Pao Chicken',
      img: '/resources/kung-pao-chicken.jpg',
      description: 'A spicy, stir-fried Chinese dish with chicken, peanuts, and vegetables.',
      ingredients: 'dummy ingredients...',
      instrusctions: 'dummy instructions...'
    },
    {
      id: 'r6',
      title: 'Chicken Biryani',
      img: '/resources/chicken-biryani.jpg',
      description: 'A fragrant Indian rice dish cooked with marinated chicken and spices.',
      ingredients: 'dummy ingredients...',
      instrusctions: 'dummy instructions...'
    }
  ];

  const router = useRouter();
  const logoutHandler = () => {
    // Programmatically navigate to homepage
    router.push('/');
  };

  const [recipes, setRecipes] = useState(DUMMY_RECIPES);

  const params = useSearchParams();
  const user = params.get('user');
  console.log('User: ', user);

  const fetchRecipes = async (user) => {
    try {
      // Update the URL to include the user-specific endpoint and pass the user ID as a query parameter
      const response = await axios.get(`http://localhost:8082/api/recipe/user`, { params: { userId: user } });
      setRecipes(response.data);
    } catch (error) {
      console.error('Error fetching recipes:', error);
    }
  }

  console.log('Recipes: ', recipes);

  useEffect(() => {
    fetchRecipes(user);
  }, [user]);

  const editHandler = (recipeId) => {
    router.push(`/edit-recipe/?id=${recipeId}`);
  };

  const deleteHandler = async (recipeId) => {
    try {
      console.log("id being deleted: ", recipeId);
      await axios.delete(`http://localhost:8082/api/recipe/${recipeId}`);
      setRecipes(recipes => recipes.filter(recipe => recipe.id !== recipeId));
      alert('Recipe successfully deleted');
    } catch (error) {
      console.error('Failed to delete the recipe:', error);
      alert('Error deleting recipe');
    }
  };

  const createHandler = () => {
    // Programmatically navigate to homepage
    router.push('/create-recipe');
  };

  return (
    <div>
      <Hdr>
        <button className='header-button' onClick={logoutHandler}>logout</button>
      </Hdr>
      <div className='greeting'>
        <h1>Hello Authorized User!</h1>
        <p>
          Input your ingredients, prep time, servings needed, 
          and let us do the rest! With the power of generative AI, 
          we promise to minimize food waste and feed your family.
        </p>
      </div>
      <div className='body'>
        <h1>My recipes</h1>
        <RecipesList items={recipes} onEdit={editHandler} onDelete={deleteHandler}/>
        <button className='footer-button' onClick={createHandler}>Create something new</button>
      </div>      
    </div>
  );
}

export default Home;