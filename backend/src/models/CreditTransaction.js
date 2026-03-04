const mongoose = require('mongoose');

const creditTransactionSchema = new mongoose.Schema({
    transactionId: { type: String, required: true, unique: true },
    partyId: { type: String, required: true, ref: 'CreditParty' },
    vehicleId: { type: String, required: true, ref: 'Vehicle' },
    vehicleNumber: { type: String, required: true },
    managerId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
    fuelType: { type: String, required: true, enum: ['PETROL', 'DIESEL'] },
    liters: { type: Number, required: true, min: 0.1 },
    pricePerLiter: { type: Number, required: true, min: 0 },
    amount: { type: Number, required: true },
    paymentType: { type: String, required: true, enum: ['CREDIT'], default: 'CREDIT' },
    date: { type: Date, default: Date.now },
    pumpNumber: { type: Number, required: true }
}, { timestamps: true });

creditTransactionSchema.index({ partyId: 1 });
creditTransactionSchema.index({ vehicleNumber: 1 });
creditTransactionSchema.index({ date: -1 });

module.exports = mongoose.model('CreditTransaction', creditTransactionSchema);
