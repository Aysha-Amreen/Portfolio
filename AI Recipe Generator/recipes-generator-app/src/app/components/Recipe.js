import React from 'react';
import Link from 'next/link';
import '../css/Recipe.css';

const Recipe = (props) => {

    const editHandler = () => {
        router.push(`/view-recipe?id=${postResult.data.id}`);
    };

    const deleteHandler = () => {
        alert("Delete functionality needs to be added");
    };

    return ( 
        <div className='recipe-item' key={props.id}>
            <Link href={`/view-recipe/?id=${props.id}`} passHref>
                <h2>{props.title}</h2>
                <img src={props.img} alt={props.title}/>
                <p>{props.description}</p>
            </Link>    
            <div className='buttons'>
                    <button className='component-button' onClick={() => props.onEdit(props.id)}>edit</button>
                    <button className='component-button' onClick={() => { 
                        props.onDelete(props.id);
                        location.reload(); 
                    }}>delete</button>
            </div>    
        </div>
    )
}

export default Recipe;
