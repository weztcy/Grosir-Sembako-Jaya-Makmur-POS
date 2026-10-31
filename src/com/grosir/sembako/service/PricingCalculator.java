package com.grosir.sembako.service;

import com.grosir.sembako.model.PaymentType;

/**
 * Logika perhitungan bisnis transaksi grosir sembako:
 * 1. Penentuan Harga Jual berdasarkan tier Harga Beli:
 *    - > Rp 200.000 : Margin 10%
 *    - > Rp 100.000 : Margin 15%
 *    - > Rp 50.000  : Margin 20%
 *    - > Rp 25.000  : Margin 30%
 *    - <= Rp 25.000 : Margin 50%
 * 2. Pajak Pertambahan Nilai (PPN 11%)
 * 3. Bunga Kredit Tempo: 50% jika tempo > 2 minggu, selain itu 0%
 * 4. Hadiah Souvenir (Khusus Tunai/Cash):
 *    - Rp 100.000 - Rp 500.000 : Mangkok Keramik
 *    - > Rp 500.000 - Rp 750.000 : Payung Lipat
 *    - > Rp 750.000 - Rp 1.000.000 : Tas Belanja
 *    - > Rp 1.000.000 : Voucher Belanja Rp 100rb
 */
public class PricingCalculator {

    public static final double DEFAULT_TAX_RATE = 0.11; // 11% PPN
    public static final double CREDIT_INTEREST_RATE = 0.50; // 50% jika tempo > 2 minggu

    public static double calculateSellingPrice(double purchasePrice) {
        if (purchasePrice <= 0) return 0;
        double markupPercentage = getMarkupPercentage(purchasePrice);
        double markup = purchasePrice * (markupPercentage / 100.0);
        return Math.round((purchasePrice + markup) / 100.0) * 100.0;
    }

    public static int getMarkupPercentage(double purchasePrice) {
        if (purchasePrice > 200_000) return 10;
        if (purchasePrice > 100_000) return 15;
        if (purchasePrice > 50_000)  return 20;
        if (purchasePrice > 25_000)  return 30;
        return 50;
    }

    public static String getTierDescription(double purchasePrice) {
        if (purchasePrice > 200_000) return "> Rp 200.000 (Margin 10%)";
        if (purchasePrice > 100_000) return "Rp 100.001 - Rp 200.000 (Margin 15%)";
        if (purchasePrice > 50_000)  return "Rp 50.001 - Rp 100.000 (Margin 20%)";
        if (purchasePrice > 25_000)  return "Rp 25.001 - Rp 50.000 (Margin 30%)";
        return "<= Rp 25.000 (Margin 50%)";
    }

    public static double calculateTax(double totalJual) {
        if (totalJual <= 0) return 0;
        return Math.round(totalJual * DEFAULT_TAX_RATE);
    }

    public static double getInterestRate(PaymentType paymentType, int tempoWeeks) {
        if (paymentType == PaymentType.CREDIT && tempoWeeks > 2) {
            return CREDIT_INTEREST_RATE;
        }
        return 0.0;
    }

    public static double calculateInterest(double totalJual, PaymentType paymentType, int tempoWeeks) {
        if (paymentType == PaymentType.CREDIT && tempoWeeks > 2) {
            return Math.round(totalJual * CREDIT_INTEREST_RATE);
        }
        return 0.0;
    }

    public static double calculateTotalPayment(double totalJual, double taxAmount, double interestAmount) {
        return totalJual + taxAmount + interestAmount;
    }

    public static String determineSouvenir(PaymentType paymentType, double totalBayar) {
        if (paymentType != PaymentType.CASH) {
            return "Tidak Ada";
        }
        if (totalBayar >= 100_000 && totalBayar <= 500_000) {
            return "Mangkok Keramik";
        } else if (totalBayar > 500_000 && totalBayar <= 750_000) {
            return "Payung Lipat";
        } else if (totalBayar > 750_000 && totalBayar <= 1_000_000) {
            return "Tas Belanja";
        } else if (totalBayar > 1_000_000) {
            return "Voucher Belanja Rp 100rb";
        } else {
            return "Tidak Ada";
        }
    }

    public static String getNextGiftTierMessage(PaymentType paymentType, double totalBayar) {
        if (paymentType != PaymentType.CASH) {
            return "Tips: Gunakan pembayaran Cash untuk mendapatkan hadiah souvenir langsung!";
        }
        if (totalBayar < 100_000) {
            return "Belanja Rp " + String.format("%,.0f", (100_000 - totalBayar)) + " lagi untuk dapat Mangkok Keramik!";
        } else if (totalBayar <= 500_000) {
            return "Belanja Rp " + String.format("%,.0f", (500_001 - totalBayar)) + " lagi untuk dapat Payung Lipat!";
        } else if (totalBayar <= 750_000) {
            return "Belanja Rp " + String.format("%,.0f", (750_001 - totalBayar)) + " lagi untuk dapat Tas Belanja!";
        } else if (totalBayar <= 1_000_000) {
            return "Belanja Rp " + String.format("%,.0f", (1_000_001 - totalBayar)) + " lagi untuk dapat Voucher Rp 100rb!";
        } else {
            return "Selamat! Transaksi ini berhak mendapat Voucher Belanja Rp 100rb!";
        }
    }
}
