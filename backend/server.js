require('dotenv').config();
const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');

const authRoutes = require('./src/routes/authRoutes');

const app = express();

// Middleware
app.use(express.json());
const corsOptions = {
    origin: '*',
    methods: ['GET', 'POST', 'PUT', 'DELETE'],
    credentials: true,
};
app.use(cors(corsOptions));

// Routes
app.use('/api/auth', authRoutes);

// Health Endpoint
app.get('/api/health', (req, res) => {
    res.status(200).send('OK');
});

// Root Endpoint
app.get('/', (req, res) => {
    res.json({ message: 'Welcome to the FuelTrack Pro API! The server is running successfully.' });
});

// Admin seed logic function
const seedAdmin = async () => {
    try {
        const User = require('./src/models/User');
        const bcrypt = require('bcrypt');
        const adminEmail = 'Santhoshvb557@gmail.com';
        const existingAdmin = await User.findOne({ email: new RegExp(`^${adminEmail}$`, 'i') });

        if (!existingAdmin) {
            const hashedPassword = await bcrypt.hash('password123', 10);
            await User.create({
                email: adminEmail,
                password: hashedPassword,
                role: 'ADMIN'
            });
            console.log('✅ Admin user successfully seeded.');
        } else {
            console.log('⚡ Admin user already exists, skipping seed.');
        }
    } catch (err) {
        console.error('❌ Failed to seed Admin user:', err);
    }
}

// Database connection
const PORT = process.env.PORT || 3000;
const MONGO_URI = process.env.MONGO_URI;

if (MONGO_URI) {
    mongoose.connect(MONGO_URI)
        .then(async () => {
            console.log('✅ Connected to MongoDB Atlas successfully.');
            await seedAdmin();
            app.listen(PORT, '0.0.0.0', () => {
                console.log(`🚀 Server running on port ${PORT} at 0.0.0.0`);
            });
        })
        .catch((err) => {
            console.error('❌ MongoDB connection failed:', err.message);
            console.warn('⚠️ Starting server anyway to serve health check, but DB routes will fail.');
            app.listen(PORT, '0.0.0.0', () => {
                console.log(`🚀 Server running on port ${PORT} but WITHOUT DATABASE connection.`);
            });
        });
} else {
    console.log('❌ MONGO_URI is not set. Please add it to .env file.');
    app.listen(PORT, '0.0.0.0', () => {
        console.log(`🚀 Server running on port ${PORT} (NO DB CONNECTION)`);
    });
}
