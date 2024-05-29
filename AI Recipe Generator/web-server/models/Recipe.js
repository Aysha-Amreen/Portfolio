const mongoose = require('mongoose');

const recipeSchema = new mongoose.Schema({
    id: {
        type: String,
        required: false
    },
    title: {
        type: String,
        required: true,
    },
    ingredients: {
        type: [String],
        required: true
    },
    instructions: {
        type: [String],
        required: true
    },
    image: {
        type: String,
        required: false
    },
    user: {
        type: String,
        required: true
    }
});

module.exports = Recipe = mongoose.model('recipe', recipeSchema);
