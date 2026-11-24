package com.grosir.sembako.service;

import com.grosir.sembako.model.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Data awal sembako grosir dan contoh transaksi.
 */
public class DataSeeder {

    public static List<Product> createInitialProducts() {
        List<Product> list = new ArrayList<>();
        list.add(new Product("BRG-001", "Beras Ramos Premium 5kg", "Beras", 68000, 81600, 65, "Sak", 10));
        list.add(new Product("BRG-002", "Beras Pandan Wangi 10kg", "Beras", 145000, 166800, 35, "Sak", 5));
        list.add(new Product("BRG-003", "Beras Rojolele Super 25kg", "Beras", 340000, 374000, 24, "Sak", 5));
        list.add(new Product("BRG-004", "Minyak Goreng Bimoli 2L", "Minyak", 36000, 46800, 80, "Pcs", 15));
        list.add(new Product("BRG-005", "Minyak Sania Karton (6x2L)", "Minyak", 210000, 231000, 30, "Karton", 6));
        list.add(new Product("BRG-006", "Minyak Filma Jerigen 5L", "Minyak", 88000, 105600, 35, "Jerigen", 8));
        list.add(new Product("BRG-007", "Gula Pasir Gulaku 1kg", "Gula", 17500, 26300, 120, "Kg", 20));
        list.add(new Product("BRG-008", "Gula Pasir Kristal Karung 50kg", "Gula", 780000, 858000, 18, "Sak", 4));
        list.add(new Product("BRG-009", "Tepung Segitiga Biru 1kg", "Tepung", 12500, 18800, 90, "Kg", 15));
        list.add(new Product("BRG-010", "Tepung Cakra Kembar Karung 25kg", "Tepung", 275000, 302500, 20, "Karung", 4));
        list.add(new Product("BRG-011", "Telur Ayam Ras 1 Karpet", "Telur", 52000, 62400, 45, "Karpet", 10));
        list.add(new Product("BRG-012", "Telur Bebek Asin Brebes Tray (30)", "Telur", 85000, 102000, 4, "Tray", 8));
        list.add(new Product("BRG-013", "Indomie Goreng Dus (40 pcs)", "Mie Instan", 112000, 128800, 55, "Dus", 10));
        list.add(new Product("BRG-014", "Indomie Ayam Bawang Dus (40 pcs)", "Mie Instan", 108000, 124200, 40, "Dus", 10));
        list.add(new Product("BRG-015", "Kopi Kapal Api Spesial Dus", "Minuman", 120000, 138000, 42, "Dus", 8));
        list.add(new Product("BRG-016", "Teh Celup Sosro Bal (50 Box)", "Minuman", 135000, 155300, 32, "Bal", 6));
        list.add(new Product("BRG-017", "Susu Frisian Flag Karton (48 kl)", "Susu", 560000, 616000, 16, "Karton", 4));
        list.add(new Product("BRG-018", "Sirup Marjan Cocopandan Dus (12)", "Minuman", 230000, 253000, 3, "Dus", 6));
        list.add(new Product("BRG-019", "Kecap Manis Bango 1.5L Pouch", "Bumbu", 42000, 54600, 60, "Pcs", 10));
        list.add(new Product("BRG-020", "Garam Dapur Cap Kapal Bal (20)", "Bumbu", 32000, 41600, 48, "Bal", 10));
        list.add(new Product("BRG-021", "Bawang Merah Brebes Super 5kg", "Bumbu Segar", 160000, 184000, 22, "Karung", 5));
        list.add(new Product("BRG-022", "Bawang Putih Honan Super 5kg", "Bumbu Segar", 150000, 172500, 25, "Karung", 5));
        list.add(new Product("BRG-023", "Margarin Blue Band Master 15kg", "Bahan Kue", 320000, 352000, 12, "Dus", 3));
        list.add(new Product("BRG-024", "Sunlight Jeruk Nipis 700ml Dus", "Kebersihan", 165000, 189800, 28, "Dus", 5));
        return list;
    }

    public static List<Transaction> createInitialTransactions() {
        List<Transaction> list = new ArrayList<>();
        LocalDateTime base = LocalDateTime.now();

        // 1. Cash kecil < 100k (Tidak Ada Hadiah)
        list.add(buildTrx("NOTA-20261018-0001", base.minusDays(5).withHour(8).withMinute(20),
                "Warung Mbak Sri - Tembalang", "081234111222", PaymentType.CASH, 0, 100000, "LUNAS", "Belanja eceran harian",
                new TransactionItem("BRG-004", "Minyak Goreng Bimoli 2L", "Pcs", 36000, 46800, 1),
                new TransactionItem("BRG-007", "Gula Pasir Gulaku 1kg", "Kg", 17500, 26300, 1)));

        // 2. Cash tier 100k-500k -> Hadiah Mangkok Keramik
        list.add(buildTrx("NOTA-20261019-0002", base.minusDays(4).withHour(10).withMinute(15),
                "Toko Sumber Berkah - Ungaran", "081234567890", PaymentType.CASH, 0, 400000, "LUNAS", "Kulakan toko sembako",
                new TransactionItem("BRG-004", "Minyak Goreng Bimoli 2L", "Pcs", 36000, 46800, 4),
                new TransactionItem("BRG-007", "Gula Pasir Gulaku 1kg", "Kg", 17500, 26300, 5)));

        // 3. Cash tier 500k-750k -> Hadiah Payung Lipat
        list.add(buildTrx("NOTA-20261020-0003", base.minusDays(3).withHour(13).withMinute(45),
                "Katering Bu Harsono - Semarang", "081390123456", PaymentType.CASH, 0, 700000, "LUNAS", "Bahan baku katering",
                new TransactionItem("BRG-001", "Beras Ramos Premium 5kg", "Sak", 68000, 81600, 4),
                new TransactionItem("BRG-005", "Minyak Sania Karton (6x2L)", "Karton", 210000, 231000, 1)));

        // 4. Cash tier 750k-1M -> Hadiah Tas Belanja
        list.add(buildTrx("NOTA-20261021-0004", base.minusDays(2).withHour(11).withMinute(30),
                "Warung Bu Hj. Aminah - Semarang", "082198765432", PaymentType.CASH, 0, 900000, "LUNAS", "Kulakan rutin warung",
                new TransactionItem("BRG-001", "Beras Ramos Premium 5kg", "Sak", 68000, 81600, 5),
                new TransactionItem("BRG-013", "Indomie Goreng Dus (40 pcs)", "Dus", 112000, 128800, 3)));

        // 5. Cash jumbo > 1M -> Hadiah Voucher Belanja Rp 100rb
        list.add(buildTrx("NOTA-20261021-0005", base.minusDays(2).withHour(15).withMinute(10),
                "Koperasi Warga Mandiri - Salatiga", "085712345678", PaymentType.CASH, 0, 2200000, "LUNAS", "Sembako bulanan koperasi",
                new TransactionItem("BRG-003", "Beras Rojolele Super 25kg", "Sak", 340000, 374000, 3),
                new TransactionItem("BRG-005", "Minyak Sania Karton (6x2L)", "Karton", 210000, 231000, 2),
                new TransactionItem("BRG-017", "Susu Frisian Flag Karton (48 kl)", "Karton", 560000, 616000, 1)));

        // 6. Kredit tempo 1 minggu (<= 2 minggu -> Bunga 0%, status BELUM LUNAS)
        list.add(buildTrx("NOTA-20261022-0006", base.minusDays(1).withHour(9).withMinute(40),
                "Minimarket Barokah - Demak", "085612349876", PaymentType.CREDIT, 1, 0, "BELUM LUNAS", "Mitra grosir tempo 1 minggu",
                new TransactionItem("BRG-013", "Indomie Goreng Dus (40 pcs)", "Dus", 112000, 128800, 4),
                new TransactionItem("BRG-014", "Indomie Ayam Bawang Dus (40 pcs)", "Dus", 108000, 124200, 3)));

        // 7. Kredit tempo 2 minggu (<= 2 minggu -> Bunga 0%, status LUNAS)
        list.add(buildTrx("NOTA-20261022-0007", base.minusDays(1).withHour(14).withMinute(0),
                "Restoran Padang Salero - Semarang", "081987654321", PaymentType.CREDIT, 2, 1215228, "LUNAS", "Pelunasan tempo transfer bank",
                new TransactionItem("BRG-003", "Beras Rojolele Super 25kg", "Sak", 340000, 374000, 2),
                new TransactionItem("BRG-021", "Bawang Merah Brebes Super 5kg", "Karung", 160000, 184000, 1),
                new TransactionItem("BRG-022", "Bawang Putih Honan Super 5kg", "Karung", 150000, 172500, 1)));

        // 8. Kredit tempo 3 minggu (> 2 minggu -> Bunga 50%!, status BELUM LUNAS)
        list.add(buildTrx("NOTA-20261023-0008", base.withHour(10).withMinute(25),
                "Grosir Mitra Usaha - Kendal", "087812345678", PaymentType.CREDIT, 3, 0, "BELUM LUNAS", "Tempo 3 minggu dengan bunga 50%",
                new TransactionItem("BRG-005", "Minyak Sania Karton (6x2L)", "Karton", 210000, 231000, 4),
                new TransactionItem("BRG-010", "Tepung Cakra Kembar Karung 25kg", "Karung", 275000, 302500, 2)));

        // 9. Kredit tempo 4 minggu (> 2 minggu -> Bunga 50%!, status BELUM LUNAS)
        list.add(buildTrx("NOTA-20261023-0009", base.withHour(11).withMinute(50),
                "Toko Kelontong Berkah Ibu - Boyolali", "081298712345", PaymentType.CREDIT, 4, 0, "BELUM LUNAS", "Tempo 4 minggu bunga 50%",
                new TransactionItem("BRG-008", "Gula Pasir Kristal Karung 50kg", "Sak", 780000, 858000, 1),
                new TransactionItem("BRG-011", "Telur Ayam Ras 1 Karpet", "Karpet", 52000, 62400, 4)));

        // 10. Cash menengah tier 100k-500k -> Hadiah Mangkok Keramik
        list.add(buildTrx("NOTA-20261023-0010", base.withHour(13).withMinute(15),
                "Kantin Mahasiswa Undip - Semarang", "085234567891", PaymentType.CASH, 0, 350000, "LUNAS", "Kantin kampus bayar tunai",
                new TransactionItem("BRG-009", "Tepung Segitiga Biru 1kg", "Kg", 12500, 18800, 6),
                new TransactionItem("BRG-004", "Minyak Goreng Bimoli 2L", "Pcs", 36000, 46800, 3),
                new TransactionItem("BRG-019", "Kecap Manis Bango 1.5L Pouch", "Pcs", 42000, 54600, 1)));

        return list;
    }

    private static Transaction buildTrx(String inv, LocalDateTime dt, String cust, String phone,
                                        PaymentType pType, int tempoWeeks, double cashPaid, String status, String notes,
                                        TransactionItem... items) {
        Transaction t = new Transaction();
        t.setInvoiceNumber(inv);
        t.setTransactionDate(dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        t.setCustomerName(cust);
        t.setCustomerPhone(phone);
        t.setPaymentType(pType);
        t.setTempoWeeks(tempoWeeks);
        if (tempoWeeks > 0) {
            t.setDueDate(dt.plusWeeks(tempoWeeks).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }

        double subtotal = 0;
        for (TransactionItem item : items) {
            t.addItem(item);
            subtotal += item.getSubtotal();
        }
        t.setTotalJual(subtotal);
        t.setTaxRate(0.11);
        t.setTaxAmount(PricingCalculator.calculateTax(subtotal));
        t.setInterestRate(PricingCalculator.getInterestRate(pType, tempoWeeks));
        t.setInterestAmount(PricingCalculator.calculateInterest(subtotal, pType, tempoWeeks));
        t.setTotalBayar(PricingCalculator.calculateTotalPayment(subtotal, t.getTaxAmount(), t.getInterestAmount()));

        t.setCashPaid(cashPaid);
        t.setChangeAmount(pType == PaymentType.CASH ? Math.max(0, cashPaid - t.getTotalBayar()) : 0);
        t.setSouvenir(PricingCalculator.determineSouvenir(pType, t.getTotalBayar()));
        t.setStatus(status);
        t.setNotes(notes);
        return t;
    }
}
