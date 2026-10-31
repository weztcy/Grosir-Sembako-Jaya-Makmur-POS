package com.grosir.sembako.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.grosir.sembako.model.*;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataRepository {
    private static final String DATA_DIR = "data";
    private static final String PROD_FILE = DATA_DIR + "/products.json";
    private static final String TRX_FILE = DATA_DIR + "/transactions.json";
    private static DataRepository instance;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final List<Product> products = new ArrayList<>();
    private final List<Transaction> transactions = new ArrayList<>();

    public static synchronized DataRepository getInstance() {
        if (instance == null) instance = new DataRepository();
        return instance;
    }

    private DataRepository() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdirs();
        loadProducts();
        loadTransactions();
    }

    private void loadProducts() {
        File f = new File(PROD_FILE);
        if (!f.exists()) {
            products.addAll(DataSeeder.createInitialProducts());
            saveProducts();
            return;
        }
        try (Reader r = new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8)) {
            Type t = new TypeToken<List<Product>>(){}.getType();
            List<Product> list = gson.fromJson(r, t);
            if (list != null) products.addAll(list);
        } catch (Exception e) {
            products.addAll(DataSeeder.createInitialProducts());
            saveProducts();
        }
    }

    private void loadTransactions() {
        File f = new File(TRX_FILE);
        if (!f.exists()) {
            transactions.addAll(DataSeeder.createInitialTransactions());
            saveTransactions();
            return;
        }
        try (Reader r = new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8)) {
            Type t = new TypeToken<List<Transaction>>(){}.getType();
            List<Transaction> list = gson.fromJson(r, t);
            if (list != null) transactions.addAll(list);
        } catch (Exception e) {
            transactions.addAll(DataSeeder.createInitialTransactions());
            saveTransactions();
        }
    }

    public synchronized void saveProducts() {
        try (Writer w = new OutputStreamWriter(new FileOutputStream(PROD_FILE), StandardCharsets.UTF_8)) {
            gson.toJson(products, w);
        } catch (Exception ignored) {}
    }

    public synchronized void saveTransactions() {
        try (Writer w = new OutputStreamWriter(new FileOutputStream(TRX_FILE), StandardCharsets.UTF_8)) {
            gson.toJson(transactions, w);
        } catch (Exception ignored) {}
    }

    public synchronized List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public synchronized Product getProductByCode(String code) {
        if (code == null) return null;
        for (Product p : products) {
            if (p.getCode().equalsIgnoreCase(code.trim())) return p;
        }
        return null;
    }

    public synchronized void saveOrUpdateProduct(Product product) {
        int idx = -1;
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getCode().equalsIgnoreCase(product.getCode())) {
                idx = i;
                break;
            }
        }
        if (idx >= 0) products.set(idx, product);
        else products.add(product);
        saveProducts();
    }

    public synchronized boolean deleteProduct(String code) {
        boolean removed = products.removeIf(p -> p.getCode().equalsIgnoreCase(code));
        if (removed) saveProducts();
        return removed;
    }

    public synchronized void reduceStock(String code, int qty) {
        Product p = getProductByCode(code);
        if (p != null) {
            p.setStock(Math.max(0, p.getStock() - qty));
            saveProducts();
        }
    }

    public synchronized void restoreStock(String code, int qty) {
        Product p = getProductByCode(code);
        if (p != null) {
            p.setStock(p.getStock() + qty);
            saveProducts();
        }
    }

    public synchronized List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    public synchronized void saveTransaction(Transaction t) {
        transactions.add(0, t);
        for (TransactionItem item : t.getItems()) {
            reduceStock(item.getProductCode(), item.getQuantity());
        }
        saveTransactions();
    }

    public synchronized boolean deleteTransaction(String invoiceNumber) {
        Transaction found = null;
        for (Transaction t : transactions) {
            if (t.getInvoiceNumber().equalsIgnoreCase(invoiceNumber)) {
                found = t;
                break;
            }
        }
        if (found != null) {
            for (TransactionItem item : found.getItems()) {
                restoreStock(item.getProductCode(), item.getQuantity());
            }
            transactions.remove(found);
            saveTransactions();
            return true;
        }
        return false;
    }

    public synchronized String generateNextInvoiceNumber() {
        String todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "NOTA-" + todayStr + "-";
        int maxSeq = 0;
        for (Transaction t : transactions) {
            if (t.getInvoiceNumber() != null && t.getInvoiceNumber().startsWith(prefix)) {
                try {
                    int seq = Integer.parseInt(t.getInvoiceNumber().substring(prefix.length()));
                    if (seq > maxSeq) maxSeq = seq;
                } catch (Exception ignored) {}
            }
        }
        return String.format("%s%04d", prefix, maxSeq + 1);
    }

    public synchronized void resetToDefaults() {
        products.clear();
        products.addAll(DataSeeder.createInitialProducts());
        saveProducts();
        transactions.clear();
        transactions.addAll(DataSeeder.createInitialTransactions());
        saveTransactions();
    }
}
