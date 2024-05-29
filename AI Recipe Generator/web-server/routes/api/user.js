const express = require('express');
const router = express.Router();
var bodyParser = require('body-parser');
const bcrypt = require('bcrypt');
const User = require('../../models/User');
const jwt = require('jsonwebtoken');

//get list of users
router.get('/', (req, res) => {
    User.find()
        .then(users => res.json(users))
        .catch(err => res.status(400).json('Error: ' + err));
});

//get user by id
router.get('/:id', (req, res) => {
    User.findById(req.params.id)
        .then(user => res.json(user))
        .catch(err => res.status(400).json('Error: ' + err));
});

//add new user
router.post('/', bodyParser.json(), async (req, res) => {
    try {
        const { username, password } = req.body;

        // Check if user already exists
        const existingUser = await User.findOne({ username });
        if (existingUser) {
            return res.status(400).json({ message: 'User already exists' });
        }
  
        // Hash password
        const hashedPassword = await bcrypt.hash(password, 10);
        
        // Create new user
        const newUser = new User({ username, password: hashedPassword });
        await newUser.save();
  
        res.status(201).json({ message: 'User created successfully' });
    } catch (err) {
        console.error('Error creating user:', err);
        res.status(500).json({ message: 'Internal server error' });
    }
});

router.post('/login', bodyParser.json(), async (req, res) => {
    try {
        const { username, password } = req.body;
        const user = await User.findOne({ username });
        if (!user) {
            return res.status(401).json({ success: false, message: 'Invalid username or password' });
        }

        // Compare the provided password with the stored hashed password
        const isPasswordValid = await bcrypt.compare(password, user.password);
        
        if (isPasswordValid) {
            const token = jwt.sign({ userID: user._id }, 'jwt_secret', { expiresIn: '1h' }); 
            console.log('User ID:', user._id);
            console.log('Generated token:', token);  // Log the token to confirm it's generated
            res.json({ success: true, message: 'Login successful', token: token, id: user._id});
        } else {
            res.status(401).json({ success: false, message: 'Invalid username or password' });
        }
    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, message: 'Internal Server Error' });
    }
});

//update user by id
router.put('/:id', bodyParser.json(), (req, res) => {
    User.findByIdAndUpdate(req.params.id, req.body)
        .then(() => res.json('User updated!'))
        .catch(err => res.status(400).json('Error: ' + err));
});

//delete user by id
router.delete('/:id', (req, res) => {
    User.findByIdAndDelete(req.params.id)
        .then(() => res.json('User deleted.'))
        .catch(err => res.status(400).json('Error: ' + err));
});

module.exports = router;