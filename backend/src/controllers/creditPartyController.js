const CreditParty = require('../models/CreditParty');
const crypto = require('crypto');

exports.createParty = async (req, res) => {
    try {
        const { partyName, companyName, contactPerson, phone, address, gstNumber, creditLimit } = req.body;

        const partyId = 'CP-' + crypto.randomBytes(4).toString('hex').toUpperCase();

        const newParty = new CreditParty({
            partyId,
            partyName,
            companyName,
            contactPerson,
            phone,
            address,
            gstNumber,
            creditLimit
        });

        await newParty.save();
        res.status(201).json(newParty);
    } catch (error) {
        res.status(500).json({ message: 'Error creating credit party', error: error.message });
    }
};

exports.getAllParties = async (req, res) => {
    try {
        const parties = await CreditParty.find().sort({ createdAt: -1 });
        res.json(parties);
    } catch (error) {
        res.status(500).json({ message: 'Error fetching credit parties', error: error.message });
    }
};

exports.getPartyById = async (req, res) => {
    try {
        const party = await CreditParty.findOne({ partyId: req.params.partyId });
        if (!party) return res.status(404).json({ message: 'Party not found' });
        res.json(party);
    } catch (error) {
        res.status(500).json({ message: 'Error fetching party', error: error.message });
    }
};

exports.updatePartyStatus = async (req, res) => {
    try {
        const { status } = req.body;
        if (!['ACTIVE', 'BLOCKED'].includes(status)) return res.status(400).json({ message: 'Invalid status' });

        const party = await CreditParty.findOneAndUpdate(
            { partyId: req.params.partyId },
            { status },
            { new: true }
        );
        if (!party) return res.status(404).json({ message: 'Party not found' });
        res.json(party);
    } catch (error) {
        res.status(500).json({ message: 'Error updating party', error: error.message });
    }
};
