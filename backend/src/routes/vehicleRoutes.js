const express = require('express');
const router = express.Router();
const { addVehicle, getVehiclesByParty, deleteVehicle } = require('../controllers/vehicleController');
const { verifyToken, requireAdmin } = require('../middleware/authMiddleware');

router.post('/', verifyToken, requireAdmin, addVehicle);
router.get('/party/:partyId', verifyToken, getVehiclesByParty); // Managers need this to assign to a specific vehicle when fueling
router.delete('/:vehicleId', verifyToken, requireAdmin, deleteVehicle);

module.exports = router;
