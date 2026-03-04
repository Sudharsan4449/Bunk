const User = require('../models/User');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

exports.login = async (req, res) => {
    try {
        const { email, password } = req.body;
        console.log(`=> Login request received for email: ${email}`);

        const user = await User.findOne({ email: new RegExp(`^${email}$`, 'i') });
        if (!user) {
            console.log(`=> Login fail: User not found for ${email}`);
            return res.status(401).json({ message: 'Invalid credentials' });
        }

        const isMatch = await bcrypt.compare(password, user.password);
        if (!isMatch) {
            console.log(`=> Login fail: Wrong password for ${email}`);
            return res.status(401).json({ message: 'Invalid credentials' });
        }

        const token = jwt.sign(
            { userId: user._id, role: user.role, email: user.email },
            process.env.JWT_SECRET,
            { expiresIn: '24h' }
        );

        console.log(`=> Login success: Validated ${email} as ${user.role}`);
        res.json({
            token,
            role: user.role,
            email: user.email
        });
    } catch (error) {
        console.error('=> Login error:', error);
        res.status(500).json({ message: 'Server error' });
    }
};
