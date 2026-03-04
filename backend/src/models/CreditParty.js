const mongoose = require('mongoose');

const creditPartySchema = new mongoose.Schema({
    partyId: { type: String, required: true, unique: true },
    partyName: { type: String, required: true },
    companyName: { type: String, required: true },
    contactPerson: { type: String, required: true },
    phone: { type: String, required: true },
    address: { type: String, required: true },
    gstNumber: { type: String }, // Optional
    creditLimit: { type: Number, required: true, min: 0 },
    currentBalance: { type: Number, default: 0 }, // Auto-calculated exclusively.
    status: { type: String, enum: ['ACTIVE', 'BLOCKED'], default: 'ACTIVE' }
}, { timestamps: true });

// Prevent generic schema duplicates cleanly
creditPartySchema.index({ partyId: 1 }, { unique: true });

module.exports = mongoose.model('CreditParty', creditPartySchema);
