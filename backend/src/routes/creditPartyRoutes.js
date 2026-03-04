const express = require('express');
const router = express.Router();
const { createParty, getAllParties, getPartyById, updatePartyStatus } = require('../controllers/creditPartyController');
const { verifyToken, requireAdmin } = require('../middleware/authMiddleware');

router.post('/', verifyToken, requireAdmin, createParty);
router.get('/', verifyToken, getAllParties); // Managers can view list to assign fuel!
router.get('/:partyId', verifyToken, getPartyById);
router.put('/:partyId/status', verifyToken, requireAdmin, updatePartyStatus);

module.exports = router;
