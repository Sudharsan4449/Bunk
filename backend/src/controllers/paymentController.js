const Payment = require('../models/Payment');
const CreditParty = require('../models/CreditParty');
const crypto = require('crypto');

exports.recordPayment = async (req, res) => {
    try {
        const { partyId, amount, method, notes } = req.body;

        const party = await CreditParty.findOne({ partyId });
        if (!party) return res.status(404).json({ message: 'Party not found' });

        const paymentId = 'PAY-' + crypto.randomBytes(4).toString('hex').toUpperCase();

        const payment = new Payment({
            paymentId,
            partyId,
            amount,
            method,
            receivedBy: req.user.userId,
            notes
        });

        await payment.save();

        // Critical Requirement: Whenever payment is recorded, subtract amount from balance natively.
        party.currentBalance -= amount;
        await party.save();

        res.status(201).json({ payment, updatedBalance: party.currentBalance });
    } catch (error) {
        res.status(500).json({ message: 'Error recording payment', error: error.message });
    }
};

exports.getPaymentsByParty = async (req, res) => {
    try {
        const payments = await Payment.find({ partyId: req.params.partyId }).sort({ date: -1 }).populate('receivedBy', 'email');
        res.json(payments);
    } catch (error) {
        res.status(500).json({ message: 'Error fetching payments', error: error.message });
    }
};
