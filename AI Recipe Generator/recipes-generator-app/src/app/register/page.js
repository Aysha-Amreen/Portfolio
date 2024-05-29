'use client'
import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import axios from 'axios';


import '../css/App.css';
import '../css/RecipeCard.css';

function Register() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:8082/api/user', { username, password });
      setMessage('User created successfully');
      setUsername('');
      setPassword('');
    } catch (err) {
      setMessage(err.response.data.message);
    }
  };

  return (
    <div className='card'>
      <h1>Register</h1>
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
            <button id='create' onClick={handleRegister}>Register</button>
          </div>
          <Link href={'/login'}>Login</Link>
        </div>
      </form>
        {message && <p>{message}</p>}
    </div>
  );
}

export default Register;