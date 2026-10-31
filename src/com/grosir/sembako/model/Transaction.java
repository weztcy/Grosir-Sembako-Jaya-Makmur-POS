package com.grosir.sembako.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model data transaksi lengkap (Nota Transaksi).
 * Mencakup rincian item barang, perhitungan harga jual, PPN, bunga kredit,
 * metode pembayaran, uang tunai, kembalian, dan souvenir/hadiah belanja.
 */
public class Transaction {
    private String invoiceNumber;      // Nomor Nota (e.g. NOTA-20261023-0001)
    private String transactionDate;    // Format: yyyy-MM-dd HH:mm:ss
    private String customerName;       // Nama Pelanggan / Toko Cabang
    private String customerPhone;      // Nomor Kontak Pelanggan
    private List<TransactionItem> items = new ArrayList<>();

    private double totalJual;          // Total harga jual sebelum pajak & bunga (Subtotal)
    private PaymentType paymentType;   // CASH / CREDIT
    private int tempoWeeks;            // Durasi tempo dalam satuan minggu (jika kredit)
    private String dueDate;            // Tanggal Jatuh Tempo
    private double taxRate;            // Persentase PPN (0.11 untuk 11%)
    private double taxAmount;          // Nominal PPN
    private double interestRate;       // Persentase Bunga Kredit (50% jika tempo > 2 minggu, else 0%)
    private double interestAmount;     // Nominal Bunga Kredit
    private double totalBayar;         // totalJual + taxAmount + interestAmount
    private double cashPaid;           // Uang tunai yang diserahkan pelanggan
    private double changeAmount;       // Uang kembalian
    private String souvenir;           // Hadiah / Souvenir yang didapatkan
    private String status;             // Status pembayaran ("LUNAS" / "BELUM LUNAS")
    private String notes;              // Catatan tambahan

    public Transaction() {
        this.paymentType = PaymentType.CASH;
        this.taxRate = 0.11;
        this.status = "LUNAS";
        this.souvenir = "Tidak Ada";
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public List<TransactionItem> getItems() {
        return items;
    }

    public void setItems(List<TransactionItem> items) {
        this.items = items != null ? items : new ArrayList<>();
    }

    public void addItem(TransactionItem item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(item);
    }

    public double getTotalJual() {
        return totalJual;
    }

    public void setTotalJual(double totalJual) {
        this.totalJual = totalJual;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public int getTempoWeeks() {
        return tempoWeeks;
    }

    public void setTempoWeeks(int tempoWeeks) {
        this.tempoWeeks = tempoWeeks;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(double interestAmount) {
        this.interestAmount = interestAmount;
    }

    public double getTotalBayar() {
        return totalBayar;
    }

    public void setTotalBayar(double totalBayar) {
        this.totalBayar = totalBayar;
    }

    public double getCashPaid() {
        return cashPaid;
    }

    public void setCashPaid(double cashPaid) {
        this.cashPaid = cashPaid;
    }

    public double getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(double changeAmount) {
        this.changeAmount = changeAmount;
    }

    public String getSouvenir() {
        return souvenir;
    }

    public void setSouvenir(String souvenir) {
        this.souvenir = souvenir;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getTotalQuantity() {
        if (items == null) return 0;
        int sum = 0;
        for (TransactionItem item : items) {
            sum += item.getQuantity();
        }
        return sum;
    }

    public double getTotalProfit() {
        if (items == null) return 0;
        double sum = 0;
        for (TransactionItem item : items) {
            sum += item.getProfit();
        }
        return sum;
    }
}
