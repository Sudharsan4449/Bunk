require('dotenv').config();
const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');

const authRoutes = require('./src/routes/authRoutes');

const app = express();

// Middleware
app.use(express.json());
app.use(cors());

// Routes
app.use('/api/auth', authRoutes);

// Database connection
const PORT = process.env.PORT || 3000;
const MONGO_URI = process.env.MONGO_URI;

if (MONGO_URI) {
    mongoose.connect(MONGO_URI)
        .then(() => {
            console.log('Connected to MongoDB');
            app.listen(PORT, () => {
                console.log(`Server running on port ${PORT}`);
            });
        })
        .catch((err) => {
            console.error('MongoDB connection error:', err);
        });
} else {
    console.log('MONGO_URI is not set. Please add it to .env files. Starting server without DB (endpoints will fail).');
    app.listen(PORT, () => {
        console.log(`Server running on port ${PORT} (NO DB CONNECTION)`);
    });
}
