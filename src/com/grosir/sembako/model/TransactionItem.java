package com.grosir.sembako.model;

/**
 * Model item produk yang ada di dalam sebuah transaksi/nota penjualan.
 */
public class TransactionItem {
    private String productCode;
    private String productName;
    private String unit;
    private double purchasePrice; // Harga beli saat transaksi untuk menghitung profit
    private double sellingPrice;  // Harga jual per satuan
    private int quantity;         // Jumlah yang dibeli
    private double subtotal;      // sellingPrice * quantity

    public TransactionItem() {}

    public TransactionItem(String productCode, String productName, String unit,
                           double purchasePrice, double sellingPrice, int quantity) {
        this.productCode = productCode;
        this.productName = productName;
        this.unit = unit != null ? unit : "Pcs";
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
        this.subtotal = sellingPrice * quantity;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
        this.subtotal = this.sellingPrice * this.quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.subtotal = this.sellingPrice * this.quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getProfit() {
        return (sellingPrice - purchasePrice) * quantity;
    }
}
