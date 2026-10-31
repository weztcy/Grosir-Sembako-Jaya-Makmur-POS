package com.grosir.sembako.ui;

import com.grosir.sembako.model.Product;
import com.grosir.sembako.service.DataRepository;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class ProductPanel extends JPanel {
    private final DataRepository repo = DataRepository.getInstance();
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtSearch = new JTextField(15);
    private JComboBox<String> cbCatFilter;
    private JLabel lblTotalSku, lblTotalUnits, lblAssetValue, lblLowStock;

    public ProductPanel() {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        initUI();
        loadData();
    }

    private void initUI() {
        JPanel north = new JPanel(new BorderLayout(10, 10));

        // 4 KPI Summary Cards
        JPanel kpiPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        kpiPanel.add(UIHelper.createStatCard("Total Varian SKU", "0", "Katalog Aktif", "box", UIHelper.PRIMARY_COLOR));
        kpiPanel.add(UIHelper.createStatCard("Total Stok Fisik", "0", "Unit / Sak / Dus", "tag", UIHelper.PRIMARY_DARK));
        kpiPanel.add(UIHelper.createStatCard("Nilai Aset Modal", "Rp 0", "Kalkulasi Modal", "money", UIHelper.ACCENT_GREEN));
        kpiPanel.add(UIHelper.createStatCard("Peringatan Stok", "0", "Perlu Restock Segera", "warning", UIHelper.ACCENT_AMBER));

        lblTotalSku = (JLabel) ((JPanel) kpiPanel.getComponent(0)).getComponent(1);
        lblTotalUnits = (JLabel) ((JPanel) kpiPanel.getComponent(1)).getComponent(1);
        lblAssetValue = (JLabel) ((JPanel) kpiPanel.getComponent(2)).getComponent(1);
        lblLowStock = (JLabel) ((JPanel) kpiPanel.getComponent(3)).getComponent(1);
        north.add(kpiPanel, BorderLayout.NORTH);

        JPanel filterBar = UIHelper.createCardPanel();
        filterBar.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 4));
        filterBar.add(new JLabel("Cari Produk/Kode:"));
        filterBar.add(txtSearch);
        filterBar.add(Box.createHorizontalStrut(10));
        filterBar.add(new JLabel("Kategori:"));
        cbCatFilter = new JComboBox<>(new String[]{"Semua Kategori", "Beras", "Minyak", "Gula", "Tepung", "Telur", "Mie Instan", "Minuman", "Susu", "Bumbu", "Bumbu Segar", "Bahan Kue", "Kebersihan"});
        filterBar.add(cbCatFilter);

        JButton btnFilter = new JButton("Filter");
        btnFilter.addActionListener(e -> applyFilter());
        txtSearch.addActionListener(e -> applyFilter());
        cbCatFilter.addActionListener(e -> applyFilter());
        filterBar.add(btnFilter);

        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(e -> { txtSearch.setText(""); cbCatFilter.setSelectedIndex(0); applyFilter(); });
        filterBar.add(btnReset);
        north.add(filterBar, BorderLayout.SOUTH);
        add(north, BorderLayout.NORTH);

        String[] cols = {"Kode", "Nama Produk", "Kategori", "Harga Beli", "Harga Jual", "Margin Keuntungan", "Stok", "Satuan", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        UIHelper.styleTable(table);

        // Apply Custom Table Renderers
        table.getColumnModel().getColumn(0).setCellRenderer(UIHelper.createCenterRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(UIHelper.createCenterRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(UIHelper.createCurrencyRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(UIHelper.createCurrencyRenderer());
        table.getColumnModel().getColumn(5).setCellRenderer(UIHelper.createCurrencyRenderer());
        table.getColumnModel().getColumn(6).setCellRenderer(UIHelper.createStockRenderer());
        table.getColumnModel().getColumn(7).setCellRenderer(UIHelper.createCenterRenderer());
        table.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                c.setHorizontalAlignment(SwingConstants.CENTER);
                c.setIcon(null);
                String val = value == null ? "" : value.toString();
                if (!isSelected) {
                    if (val.startsWith("Aman")) {
                        c.setForeground(new Color(5, 150, 105));
                        c.setIcon(UIHelper.getVectorIcon("check", 12, new Color(5, 150, 105)));
                        c.setText(" Aman");
                        c.setFont(UIHelper.FONT_BOLD);
                    } else if (val.startsWith("Menipis")) {
                        c.setForeground(new Color(217, 119, 6));
                        c.setIcon(UIHelper.getVectorIcon("warning", 12, new Color(217, 119, 6)));
                        c.setText(" Menipis");
                        c.setFont(UIHelper.FONT_BOLD);
                    } else if (val.startsWith("Habis")) {
                        c.setForeground(new Color(220, 38, 38));
                        c.setIcon(UIHelper.getVectorIcon("close", 12, new Color(220, 38, 38)));
                        c.setText(" Habis");
                        c.setFont(UIHelper.FONT_BOLD);
                    }
                }
                return c;
            }
        });

        table.getColumnModel().getColumn(0).setPreferredWidth(75);
        table.getColumnModel().getColumn(1).setPreferredWidth(230);
        table.getColumnModel().getColumn(2).setPreferredWidth(95);
        table.getColumnModel().getColumn(3).setPreferredWidth(95);
        table.getColumnModel().getColumn(4).setPreferredWidth(95);
        table.getColumnModel().getColumn(5).setPreferredWidth(130);
        table.getColumnModel().getColumn(6).setPreferredWidth(55);
        table.getColumnModel().getColumn(7).setPreferredWidth(60);
        table.getColumnModel().getColumn(8).setPreferredWidth(85);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        JButton btnAdd = UIHelper.createPrimaryButton("Tambah Produk", UIHelper.getVectorIcon("plus", 14, Color.WHITE));
        JButton btnEdit = UIHelper.createButton("Ubah", UIHelper.getVectorIcon("edit", 14, UIHelper.TEXT_MAIN));
        JButton btnDel = UIHelper.createDangerButton("Hapus", UIHelper.getVectorIcon("trash", 14, Color.WHITE));
        JButton btnReload = UIHelper.createButton("Muat Ulang", UIHelper.getVectorIcon("refresh", 14, UIHelper.TEXT_MAIN));

        btnAdd.addActionListener(e -> addProduct());
        btnEdit.addActionListener(e -> editProduct());
        btnDel.addActionListener(e -> deleteProduct());
        btnReload.addActionListener(e -> loadData());

        actions.add(btnReload); actions.add(btnDel); actions.add(btnEdit); actions.add(btnAdd);
        add(actions, BorderLayout.SOUTH);
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<Product> products = repo.getAllProducts();
        int low = 0;
        int totalUnits = 0;
        double totalAsset = 0;

        for (Product p : products) {
            String st = "Aman";
            if (p.isOutOfStock()) { st = "Habis"; low++; }
            else if (p.isLowStock()) { st = "Menipis (≤ " + p.getMinStock() + ")"; low++; }

            totalUnits += p.getStock();
            totalAsset += (p.getPurchasePrice() * p.getStock());

            String margin = UIHelper.formatRupiah(p.getProfitMargin()) + " (+" + String.format("%.0f", p.getProfitPercentage()) + "%)";
            tableModel.addRow(new Object[]{
                p.getCode(), p.getName(), p.getCategory(),
                UIHelper.formatRupiah(p.getPurchasePrice()),
                UIHelper.formatRupiah(p.getSellingPrice()),
                margin, p.getStock(), p.getUnit(), st
            });
        }
        lblTotalSku.setText(products.size() + " SKU");
        lblTotalUnits.setText(String.format("%,d Unit", totalUnits));
        lblAssetValue.setText(UIHelper.formatRupiah(totalAsset));
        lblLowStock.setText(low + " Perlu Restock");
    }

    private void applyFilter() {
        String text = txtSearch.getText().trim().toLowerCase();
        String cat = (String) cbCatFilter.getSelectedItem();
        sorter.setRowFilter(new RowFilter<DefaultTableModel, Object>() {
            public boolean include(Entry<? extends DefaultTableModel, ?> entry) {
                String c = entry.getStringValue(0).toLowerCase();
                String n = entry.getStringValue(1).toLowerCase();
                String k = entry.getStringValue(2);
                boolean ms = text.isEmpty() || c.contains(text) || n.contains(text);
                boolean mk = cat == null || cat.equals("Semua Kategori") || k.equalsIgnoreCase(cat);
                return ms && mk;
            }
        });
    }

    private void addProduct() {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        ProductDialog dlg = new ProductDialog(parent, null);
        dlg.setVisible(true);
        if (dlg.isSaved()) {
            Product p = dlg.getProduct();
            if (repo.getProductByCode(p.getCode()) != null) {
                JOptionPane.showMessageDialog(this, "Kode produk '" + p.getCode() + "' sudah terdaftar!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            repo.saveOrUpdateProduct(p);
            loadData();
        }
    }

    private void editProduct() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih produk yang ingin diedit!");
            return;
        }
        int mRow = table.convertRowIndexToModel(row);
        String code = (String) tableModel.getValueAt(mRow, 0);
        Product p = repo.getProductByCode(code);
        if (p == null) return;
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        ProductDialog dlg = new ProductDialog(parent, p);
        dlg.setVisible(true);
        if (dlg.isSaved()) {
            repo.saveOrUpdateProduct(p);
            loadData();
        }
    }

    private void deleteProduct() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih produk yang ingin dihapus!");
            return;
        }
        int mRow = table.convertRowIndexToModel(row);
        String code = (String) tableModel.getValueAt(mRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Hapus produk " + code + " ?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            repo.deleteProduct(code);
            loadData();
        }
    }
}
