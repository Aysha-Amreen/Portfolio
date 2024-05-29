'use client'
import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import axios from 'axios';

import '../css/App.css';
import '../css/RecipeCard.css';
import { Router } from 'next/router';

function Login() {
  const router = useRouter();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post("http://localhost:8082/api/user/login", {username, password});
      console.log('Login response:', response.data);
      setMessage(response.data.message)
      setUsername('');
      setPassword('');
      const id = response.data.id;
      console.log('User ID:', id);
      const { token } = response.data;
      if (token) {
        localStorage.setItem('token', token); // Save the token in localStorage
        console.log('Token stored:', localStorage.getItem('token')); // Check if token is stored
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        router.push(`/authorized-user?user=${id}`)
      } else {
        console.log('No token received:', response.data);      
      }      
    } catch (err) {
      if (err.response && err.response.data) {
        setMessage(err.response.data.message); // Use server-sent error message
      } else {
        setMessage("An unexpected error occurred."); // Fallback error message
      }
      console.error('Login error:', err.response || err); // Log detailed error
    }
  };

  return (
    <div className='card'>
      <h1>Login</h1>
      <form>
        <div className='cardcontent'>
          <div className='inputs'>
            <input
              type="text"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
          </div>
          <div className='inputs'>
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>
          <div className='card-buttons'>
            <button id='create' onClick={handleLogin}>Login</button>
          </div>
            <Link href={'/register'}>Sign Up</Link>
        </div>
      </form>
        {message && <p>{message}</p>}
    </div>
  );
}

export default Login;