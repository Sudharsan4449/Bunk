const Vehicle = require('../models/Vehicle');
const CreditParty = require('../models/CreditParty');
const crypto = require('crypto');

exports.addVehicle = async (req, res) => {
    try {
        const { partyId, vehicleNumber, driverName, driverPhone } = req.body;

        // Ensure party exists
        const party = await CreditParty.findOne({ partyId });
        if (!party) return res.status(404).json({ message: 'Party not found' });

        // Ensure global vehicle number uniqueness
        const existingVehicle = await Vehicle.findOne({ vehicleNumber: new RegExp(`^${vehicleNumber}$`, 'i') });
        if (existingVehicle) return res.status(400).json({ message: 'Vehicle number already exists across the system' });

        const vehicleId = 'VH-' + crypto.randomBytes(4).toString('hex').toUpperCase();

        const newVehicle = new Vehicle({
            vehicleId,
            partyId,
            vehicleNumber,
            driverName,
            driverPhone
        });

        await newVehicle.save();
        res.status(201).json(newVehicle);
    } catch (error) {
        res.status(500).json({ message: 'Error adding vehicle', error: error.message });
    }
};

exports.getVehiclesByParty = async (req, res) => {
    try {
        const vehicles = await Vehicle.find({ partyId: req.params.partyId }).sort({ createdAt: -1 });
        res.json(vehicles);
    } catch (error) {
        res.status(500).json({ message: 'Error fetching vehicles', error: error.message });
    }
};

exports.deleteVehicle = async (req, res) => {
    try {
        const vehicle = await Vehicle.findOneAndDelete({ vehicleId: req.params.vehicleId });
        if (!vehicle) return res.status(404).json({ message: 'Vehicle not found' });
        res.json({ message: 'Vehicle deleted successfully' });
    } catch (error) {
        res.status(500).json({ message: 'Error deleting vehicle', error: error.message });
    }
};
