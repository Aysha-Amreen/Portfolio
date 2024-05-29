import React, { useState } from 'react';

import '../css/Hdr.css';

const Hdr = (props) => {

  const [query, setQuery] = useState(''); // Initializes the query state

  const queryHandler = (event) => {
    setQuery(event.target.value); // Updates the query state on every change in the input field
  };

  const searchHandler = () => {
    console.log("User searched: ", query); // Logs the query made to the console
  };

  return (
    <div className='hdr'>
      <img id='logo' src='../resources/logo.png' alt='logo'/>
      <h1>AI Generated Recipes</h1>
      {props.children}       
    </div>
  );
};

export default Hdr;