package com.grosir.sembako.model;

import java.util.Objects;

/**
 * Model data produk sembako grosir.
 * Menyimpan informasi kode, nama, kategori, harga beli, harga jual, dan stok.
 */
public class Product {
    private String code;          // Kode Produk (contoh: BRG-001)
    private String name;          // Nama Produk (contoh: Beras Ramos 5kg)
    private String category;      // Kategori (Beras, Minyak, Gula, dll.)
    private double purchasePrice; // Harga Beli dari supplier (hrgBeli)
    private double sellingPrice;  // Harga Jual ke pembeli (hrgJual)
    private int stock;            // Stok tersedia
    private String unit;          // Satuan (Sak, Karton, Dus, Pcs, Kg, Liter)
    private int minStock;         // Batas stok minimal untuk peringatan

    public Product() {
        this.unit = "Pcs";
        this.minStock = 5;
    }

    public Product(String code, String name, String category, double purchasePrice, 
                   double sellingPrice, int stock, String unit, int minStock) {
        this.code = code;
        this.name = name;
        this.category = category;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.stock = stock;
        this.unit = unit != null ? unit : "Pcs";
        this.minStock = minStock;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getMinStock() {
        return minStock;
    }

    public void setMinStock(int minStock) {
        this.minStock = minStock;
    }

    /**
     * Menghitung keuntungan nominal per unit (Harga Jual - Harga Beli)
     */
    public double getProfitMargin() {
        return sellingPrice - purchasePrice;
    }

    /**
     * Menghitung persentase keuntungan terhadap harga beli (%)
     */
    public double getProfitPercentage() {
        if (purchasePrice <= 0) return 0;
        return (getProfitMargin() / purchasePrice) * 100.0;
    }

    /**
     * Mengecek apakah stok saat ini berada pada batas menipis
     */
    public boolean isLowStock() {
        return stock <= minStock;
    }

    /**
     * Mengecek apakah stok habis
     */
    public boolean isOutOfStock() {
        return stock <= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(code, product.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return code + " - " + name + " (Stok: " + stock + " " + unit + ")";
    }
}
