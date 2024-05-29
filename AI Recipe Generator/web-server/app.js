const express = require('express');
const app = express();
const port = 8082;
const mongoose = require('mongoose');
const cors = require('cors');

app.use(cors({origin: true, credentials: true}));
app.use(express.json()); 

//set routing for recipes
const recipes = require('./routes/api/recipe');
app.use('/api/recipe', recipes);

//set routing for users
const users = require('./routes/api/user');
app.use('/api/user', users);

app.get('/', (req, res) => res.send('Hello world!'));

const conn_str = 'mongodb+srv://as58810:BhkWrj6xZtDW8E7I@cluster0.wwmwleq.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0';
mongoose.set('strictQuery', false);
mongoose.connect(conn_str).then(() => {
    app.listen(port, () => console.log(`Server running on port ${port}`))
    console.log('Connected to MongoDB')
});
