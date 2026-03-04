const express = require('express');
const router = express.Router();
const { recordPayment, getPaymentsByParty } = require('../controllers/paymentController');
const { verifyToken, requireAdmin } = require('../middleware/authMiddleware');

router.post('/', verifyToken, requireAdmin, recordPayment);
router.get('/party/:partyId', verifyToken, requireAdmin, getPaymentsByParty); // Payments history might be Admin-only

module.exports = router;
