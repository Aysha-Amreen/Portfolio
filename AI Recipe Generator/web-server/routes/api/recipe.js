const express = require('express');
const router = express.Router();
var bodyParser = require('body-parser');

const Recipe = require('../../models/Recipe');
const { authenticateToken } = require('../../middleware/authMiddleware');

// Get list of recipes by user
router.get('/user', (req, res) => {
    const userId = req.query.userId;  // Assuming 'userId' is passed as a query parameter
    if (!userId) {
        return res.status(400).json('Error: User ID is required');
    }
    Recipe.find({ user: userId })  // Assuming 'user' field holds the user ID in your Recipe schema
        .then(recipes => res.json(recipes))
        .catch(err => res.status(400).json('Error: ' + err));
});

//get list of recipes
router.get('/', (req, res) => {
    Recipe.find()
        .then(recipes => res.json(recipes))
        .catch(err => res.status(400).json('Error: ' + err));
});

//get recipe by id
router.get('/:id', (req, res) => {
    Recipe.findById(req.params.id)
        .then(recipe => res.json(recipe))
        .catch(err => res.status(400).json('Error: ' + err));
});

// Add new recipe
router.post('/', bodyParser.json(), authenticateToken, (req, res) => {
    console.log(req.user.userID); // Accessing userID from token
    const newRecipe = new Recipe({
        ...req.body,
        user: req.user.userID // Attaching user ID from token        
    });

    newRecipe.save()
        .then(recipe => {
            res.json({ message: 'Recipe added!', id: recipe._id });
        })
        .catch(err => {
            console.error(err); // Log the full error
            res.status(500).json('Error: ' + err); // Change to 500 to reflect server error
        });
});

//update recipe by id
router.put('/:id', bodyParser.json(), (req, res) => {
    Recipe.findByIdAndUpdate(req.params.id, req.body)
        .then(() => res.json('Recipe updated!'))
        .catch(err => res.status(400).json('Error: ' + err));
});

//delete recipe by id
router.delete('/:id', (req, res) => {
    Recipe.findByIdAndDelete(req.params.id)
        .then(() => res.json('Recipe deleted.'))
        .catch(err => res.status(400).json('Error: ' + err));
});


module.exports = router;