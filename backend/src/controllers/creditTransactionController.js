const CreditTransaction = require('../models/CreditTransaction');
const CreditParty = require('../models/CreditParty');
const Vehicle = require('../models/Vehicle');
const crypto = require('crypto');

exports.createTransaction = async (req, res) => {
    try {
        const { partyId, vehicleId, fuelType, liters, pricePerLiter, pumpNumber } = req.body;

        const party = await CreditParty.findOne({ partyId });
        if (!party) return res.status(404).json({ message: 'Party not found' });

        if (party.status !== 'ACTIVE') {
            return res.status(403).json({ message: 'Credit Party is currently blocked.' });
        }

        const vehicle = await Vehicle.findOne({ vehicleId });
        if (!vehicle || vehicle.partyId !== partyId) {
            return res.status(400).json({ message: 'Invalid vehicle assigned to this party' });
        }

        const amount = liters * pricePerLiter;

        // Credit Limit Check
        if (party.currentBalance + amount > party.creditLimit) {
            return res.status(400).json({
                message: 'Credit limit exceeded',
                currentBalance: party.currentBalance,
                creditLimit: party.creditLimit,
                requestedAmount: amount
            });
        }

        const transactionId = 'TXN-' + crypto.randomBytes(5).toString('hex').toUpperCase();

        const transaction = new CreditTransaction({
            transactionId,
            partyId,
            vehicleId,
            vehicleNumber: vehicle.vehicleNumber,
            managerId: req.user.userId,
            fuelType,
            liters,
            pricePerLiter,
            amount,
            pumpNumber
        });

        await transaction.save();

        party.currentBalance += amount;
        await party.save();

        res.status(201).json({ transaction, updatedBalance: party.currentBalance });
    } catch (error) {
        res.status(500).json({ message: 'Error creating transaction', error: error.message });
    }
};

exports.getTransactionsByParty = async (req, res) => {
    try {
        const transactions = await CreditTransaction.find({ partyId: req.params.partyId })
            .sort({ date: -1 })
            .populate('managerId', 'email');
        res.json(transactions);
    } catch (error) {
        res.status(500).json({ message: 'Error fetching transactions', error: error.message });
    }
};
