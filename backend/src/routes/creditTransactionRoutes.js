const express = require('express');
const router = express.Router();
const { createTransaction, getTransactionsByParty } = require('../controllers/creditTransactionController');
const { verifyToken, requireAdmin } = require('../middleware/authMiddleware');

// Managers create transactions
router.post('/', verifyToken, createTransaction);
router.get('/party/:partyId', verifyToken, getTransactionsByParty);

module.exports = router;
