package com.grosir.sembako.ui;

import com.grosir.sembako.model.Product;
import com.grosir.sembako.service.PricingCalculator;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class ProductDialog extends JDialog {
    private final Product item;
    private boolean saved = false;

    private JTextField txtCode = new JTextField();
    private JTextField txtName = new JTextField();
    private JComboBox<String> cbCat = new JComboBox<>(new String[]{"Beras", "Minyak", "Gula", "Tepung", "Telur", "Mie Instan", "Kopi & Teh", "Susu", "Bumbu", "Lainnya"});
    private JTextField txtBuy = new JTextField("0");
    private JTextField txtSell = new JTextField("0");
    private JLabel lblRec = new JLabel("Rekomendasi: Rp 0");
    private JSpinner spStock = new JSpinner(new SpinnerNumberModel(10, 0, 100000, 1));
    private JComboBox<String> cbUnit = new JComboBox<>(new String[]{"Pcs", "Sak", "Karton", "Dus", "Kg", "Liter", "Karpet", "Renceng"});
    private JSpinner spMin = new JSpinner(new SpinnerNumberModel(5, 0, 1000, 1));

    public ProductDialog(Frame parent, Product productToEdit) {
        super(parent, productToEdit == null ? "Tambah Produk" : "Edit Produk", true);
        this.item = productToEdit;
        cbCat.setEditable(true);
        cbUnit.setEditable(true);
        lblRec.setForeground(UIHelper.PRIMARY_COLOR);
        init();
        if (item != null) loadData();
    }

    private void init() {
        setSize(450, 480);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(9, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JButton btnRec = new JButton("Pakai Rekomendasi");
        btnRec.addActionListener(e -> applyRec());

        txtBuy.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateRec(); }
            public void removeUpdate(DocumentEvent e) { updateRec(); }
            public void changedUpdate(DocumentEvent e) { updateRec(); }
        });

        form.add(new JLabel("Kode Produk:")); form.add(txtCode);
        form.add(new JLabel("Nama Produk:")); form.add(txtName);
        form.add(new JLabel("Kategori:")); form.add(cbCat);
        form.add(new JLabel("Harga Beli (Rp):")); form.add(txtBuy);
        form.add(lblRec); form.add(btnRec);
        form.add(new JLabel("Harga Jual (Rp):")); form.add(txtSell);
        form.add(new JLabel("Stok:")); form.add(spStock);
        form.add(new JLabel("Satuan:")); form.add(cbUnit);
        form.add(new JLabel("Stok Minimal:")); form.add(spMin);
        add(form, BorderLayout.CENTER);

        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnSave = UIHelper.createPrimaryButton("Simpan", UIHelper.getVectorIcon("save", 14, Color.WHITE));
        JButton btnCancel = UIHelper.createButton("Batal", UIHelper.getVectorIcon("close", 12, UIHelper.TEXT_MAIN));
        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());
        pnl.add(btnSave); pnl.add(btnCancel);
        add(pnl, BorderLayout.SOUTH);
    }

    private void updateRec() {
        try {
            double b = Double.parseDouble(txtBuy.getText().trim());
            double r = PricingCalculator.calculateSellingPrice(b);
            int p = PricingCalculator.getMarkupPercentage(b);
            lblRec.setText("Rekomendasi: " + UIHelper.formatRupiah(r) + " (+" + p + "%)");
        } catch (Exception ex) {
            lblRec.setText("Rekomendasi: Rp 0");
        }
    }

    private void applyRec() {
        try {
            double b = Double.parseDouble(txtBuy.getText().trim());
            txtSell.setText(String.format("%.0f", PricingCalculator.calculateSellingPrice(b)));
        } catch (Exception ignored) {}
    }

    private void loadData() {
        txtCode.setText(item.getCode());
        txtCode.setEditable(false);
        txtName.setText(item.getName());
        cbCat.setSelectedItem(item.getCategory());
        txtBuy.setText(String.format("%.0f", item.getPurchasePrice()));
        txtSell.setText(String.format("%.0f", item.getSellingPrice()));
        spStock.setValue(item.getStock());
        cbUnit.setSelectedItem(item.getUnit());
        spMin.setValue(item.getMinStock());
        updateRec();
    }

    private void save() {
        String c = txtCode.getText().trim();
        String n = txtName.getText().trim();
        if (c.isEmpty() || n.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kode dan Nama produk wajib diisi!");
            return;
        }
        try {
            double b = Double.parseDouble(txtBuy.getText().trim());
            double s = Double.parseDouble(txtSell.getText().trim());
            if (item != null) {
                item.setName(n);
                item.setCategory((String) cbCat.getSelectedItem());
                item.setPurchasePrice(b);
                item.setSellingPrice(s);
                item.setStock((Integer) spStock.getValue());
                item.setUnit((String) cbUnit.getSelectedItem());
                item.setMinStock((Integer) spMin.getValue());
            }
            saved = true;
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Harga harus berupa angka valid!");
        }
    }

    public boolean isSaved() { return saved; }

    public Product getProduct() {
        if (!saved) return null;
        if (item != null) return item;
        return new Product(
            txtCode.getText().trim(), txtName.getText().trim(),
            (String) cbCat.getSelectedItem(),
            Double.parseDouble(txtBuy.getText().trim()),
            Double.parseDouble(txtSell.getText().trim()),
            (Integer) spStock.getValue(),
            (String) cbUnit.getSelectedItem(),
            (Integer) spMin.getValue()
        );
    }
}
