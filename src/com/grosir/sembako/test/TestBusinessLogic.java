package com.grosir.sembako.test;

import com.grosir.sembako.model.*;
import com.grosir.sembako.service.DataRepository;
import com.grosir.sembako.service.PricingCalculator;
import java.util.List;

public class TestBusinessLogic {
    public static void main(String[] args) {
        System.out.println("=== RUNNING BUSINESS LOGIC TESTS ===");
        int passed = 0, failed = 0;

        // 1. Markup tier > 200k (10%)
        if (PricingCalculator.calculateSellingPrice(250000) == 275000) {
            System.out.println(" [PASS] Markup > 200k: 250k -> 275k (10%)");
            passed++;
        } else { System.err.println(" [FAIL] Markup > 200k failed"); failed++; }

        // 2. Markup tier 100k-200k (15%)
        if (PricingCalculator.calculateSellingPrice(150000) == 172500) {
            System.out.println(" [PASS] Markup 100k-200k: 150k -> 172.5k (15%)");
            passed++;
        } else { System.err.println(" [FAIL] Markup 100k-200k failed"); failed++; }

        // 3. Markup tier 50k-100k (20%)
        if (PricingCalculator.calculateSellingPrice(60000) == 72000) {
            System.out.println(" [PASS] Markup 50k-100k: 60k -> 72k (20%)");
            passed++;
        } else { System.err.println(" [FAIL] Markup 50k-100k failed"); failed++; }

        // 4. Markup tier 25k-50k (30%)
        if (PricingCalculator.calculateSellingPrice(30000) == 39000) {
            System.out.println(" [PASS] Markup 25k-50k: 30k -> 39k (30%)");
            passed++;
        } else { System.err.println(" [FAIL] Markup 25k-50k failed"); failed++; }

        // 5. Markup tier <= 25k (50%)
        if (PricingCalculator.calculateSellingPrice(10000) == 15000) {
            System.out.println(" [PASS] Markup <= 25k: 10k -> 15k (50%)");
            passed++;
        } else { System.err.println(" [FAIL] Markup <= 25k failed"); failed++; }

        // 6. PPN 11%
        if (PricingCalculator.calculateTax(1000000) == 110000) {
            System.out.println(" [PASS] PPN 11%: 1,000,000 -> 110,000");
            passed++;
        } else { System.err.println(" [FAIL] PPN 11% failed"); failed++; }

        // 7. Credit tempo <= 2 weeks (0%)
        if (PricingCalculator.calculateInterest(1000000, PaymentType.CREDIT, 2) == 0) {
            System.out.println(" [PASS] Credit tempo 2 weeks interest = 0%");
            passed++;
        } else { System.err.println(" [FAIL] Credit tempo 2 weeks failed"); failed++; }

        // 8. Credit tempo > 2 weeks (50%)
        if (PricingCalculator.calculateInterest(1000000, PaymentType.CREDIT, 3) == 500000) {
            System.out.println(" [PASS] Credit tempo 3 weeks interest = 50%");
            passed++;
        } else { System.err.println(" [FAIL] Credit tempo 3 weeks failed"); failed++; }

        // 9. Souvenirs
        String s1 = PricingCalculator.determineSouvenir(PaymentType.CASH, 90000);
        String s2 = PricingCalculator.determineSouvenir(PaymentType.CASH, 250000);
        String s3 = PricingCalculator.determineSouvenir(PaymentType.CASH, 600000);
        String s4 = PricingCalculator.determineSouvenir(PaymentType.CASH, 850000);
        String s5 = PricingCalculator.determineSouvenir(PaymentType.CASH, 1200000);
        String sCredit = PricingCalculator.determineSouvenir(PaymentType.CREDIT, 1200000);

        if (s1.equals("Tidak Ada") &&
            s2.equals("Mangkok Keramik") &&
            s3.equals("Payung Lipat") &&
            s4.equals("Tas Belanja") &&
            s5.equals("Voucher Belanja Rp 100rb") &&
            sCredit.equals("Tidak Ada")) {
            System.out.println(" [PASS] Souvenir rules and legacy tiers validated (100% match)");
            passed++;
        } else {
            System.err.println(" [FAIL] Souvenir test failed: s1=" + s1 + ", s2=" + s2 + ", s3=" + s3 + ", s4=" + s4 + ", s5=" + s5 + ", sCredit=" + sCredit);
            failed++;
        }

        // 10. Repository & Seeding
        DataRepository repo = DataRepository.getInstance();
        List<Product> products = repo.getAllProducts();
        if (products.size() >= 10) {
            System.out.println(" [PASS] Data repository loaded " + products.size() + " products");
            passed++;
        } else { System.err.println(" [FAIL] Expected >= 10 products"); failed++; }

        // 11. Invoice Generator
        String inv = repo.generateNextInvoiceNumber();
        if (inv != null && inv.startsWith("NOTA-")) {
            System.out.println(" [PASS] Invoice sequence generated: " + inv);
            passed++;
        } else { System.err.println(" [FAIL] Invalid invoice: " + inv); failed++; }

        System.out.println("=== TEST SUMMARY: " + passed + " PASSED, " + failed + " FAILED ===");
        if (failed > 0) System.exit(1);
    }
}
