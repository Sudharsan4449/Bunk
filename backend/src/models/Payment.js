const mongoose = require('mongoose');

const paymentSchema = new mongoose.Schema({
    paymentId: { type: String, required: true, unique: true },
    partyId: { type: String, required: true, ref: 'CreditParty' },
    amount: { type: Number, required: true, min: 1 },
    method: { type: String, required: true, enum: ['Cash', 'Bank Transfer', 'UPI'] },
    date: { type: Date, default: Date.now },
    receivedBy: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
    notes: { type: String }
}, { timestamps: true });

paymentSchema.index({ partyId: 1 });
paymentSchema.index({ date: -1 });

module.exports = mongoose.model('Payment', paymentSchema);
