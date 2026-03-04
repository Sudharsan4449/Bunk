require('dotenv').config();
const mongoose = require('mongoose');
const bcrypt = require('bcrypt');
const User = require('./src/models/User');

const seedAdmin = async () => {
    try {
        if (!process.env.MONGO_URI) {
            console.log('No MONGO_URI string found in .env, skipping seed.');
            return;
        }

        await mongoose.connect(process.env.MONGO_URI);

        const email = 'Santhoshvb557@gmail.com';
        const existingAdmin = await User.findOne({ email });

        if (!existingAdmin) {
            const hashedPassword = await bcrypt.hash('password123', 10);
            const admin = new User({
                email,
                password: hashedPassword,
                role: 'ADMIN'
            });
            await admin.save();
            console.log('Admin user seeded successfully');
        } else {
            console.log('Admin user already exists. Forcing a password reset to password123 to ensure correct hash...');
            const hashedPassword = await bcrypt.hash('password123', 10);
            existingAdmin.password = hashedPassword;
            await existingAdmin.save();
            console.log('Admin password forcefully reset.');
        }

        process.exit(0);
    } catch (error) {
        console.error('Error seeding admin:', error);
        process.exit(1);
    }
};

seedAdmin();
