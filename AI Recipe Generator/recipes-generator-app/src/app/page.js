'use client'
import { useRouter } from 'next/navigation';

import RecipesList from './components/RecipesList';
import Hdr from './components/Hdr';
import './css/App.css';

const Home = () => {

  const DUMMY_RECIPES = [
    {
      id: '662d56e90bf33707c3e4ad39',
      title: 'Beef Ragu',
      image: '/resources/beef-ragu.jpg',
      description: 'A hearty Italian sauce of slow-cooked beef served over pasta.'
    },
    {
      id: 'r2',
      title: 'Creme Brulee',
      image: '/resources/creme-brulee.jpg',
      description: 'A rich custard base topped with a layer of hard caramel.'
    },
    {
      id: 'r3',
      title: 'Butter Chicken',
      image: '/resources/butter-chicken.jpg',
      description: 'A creamy, spiced tomato-based curry with tender chicken pieces.'
    },
    {
      id: 'r4',
      title: 'Tofu Fried Rice',
      image: '/resources/tofu-fried-rice.jpg',
      description: 'A flavorful stir-fry with seasoned tofu and assorted vegetables.'
    },
    {
      id: 'r5',
      title: 'Kung Pao Chicken',
      image: '/resources/kung-pao-chicken.jpg',
      description: 'A spicy, stir-fried Chinese dish with chicken, peanuts, and vegetables.'
    },
    {
      id: 'r6',
      title: 'Chicken Biryani',
      image: '/resources/chicken-biryani.jpg',
      description: 'A fragrant Indian rice dish cooked with marinated chicken and spices.'
    }
  ];

  const recipes = DUMMY_RECIPES;
  
  const router = useRouter();
  const loginHandler = () => {
    // Programmatically navigate to authorized view
    router.push('/login');
  };

  return (
    <div>
      <Hdr>
        <button className='header-button' onClick={loginHandler}>login/signup</button>
      </Hdr>
      <div className='greeting'>
          <h2>Hello User!</h2>
          <p>
            Input your ingredients, prep time, servings needed, 
            and let us do the rest! With the power of generative AI, 
            we promise to minimize food waste and feed your family.
          </p>
      </div>
      <div className='body'>                
        <RecipesList items={recipes}/>        
        <button className='footer-button' onClick={loginHandler}>Get Started</button>
      </div>      
    </div>
  );
}

export default Home;