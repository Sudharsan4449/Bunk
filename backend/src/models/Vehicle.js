const mongoose = require('mongoose');

const vehicleSchema = new mongoose.Schema({
    vehicleId: { type: String, required: true, unique: true },
    partyId: { type: String, required: true, ref: 'CreditParty' },
    vehicleNumber: { type: String, required: true, unique: true }, // Prevent duplicate globally.
    driverName: { type: String },
    driverPhone: { type: String }
}, { timestamps: true });

vehicleSchema.index({ vehicleNumber: 1 }, { unique: true });
vehicleSchema.index({ partyId: 1 });

module.exports = mongoose.model('Vehicle', vehicleSchema);
