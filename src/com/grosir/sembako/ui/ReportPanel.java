package com.grosir.sembako.ui;

import com.grosir.sembako.model.*;
import com.grosir.sembako.service.DataRepository;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class ReportPanel extends JPanel {
    private final DataRepository repo = DataRepository.getInstance();

    private JLabel lblTotalRevenue, lblTotalTrx, lblCashTrx, lblCreditTrx, lblTotalTax, lblTotalInterest;
    private JTextField txtSearch = new JTextField(12);
    private JComboBox<String> cbPaymentFilter = new JComboBox<>(new String[]{"Semua Pembayaran", "Cash", "Credit"});
    private JComboBox<String> cbStatusFilter = new JComboBox<>(new String[]{"Semua Status", "LUNAS", "BELUM LUNAS"});

    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTable itemsTable;
    private DefaultTableModel itemsModel;

    public ReportPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        initUI();
        loadData();
    }

    private void initUI() {
        JPanel northPanel = new JPanel(new BorderLayout(8, 8));

        JPanel kpiPanel = new JPanel(new GridLayout(1, 6, 6, 6));
        kpiPanel.add(UIHelper.createStatCard("Total Omzet", "Rp 0", "Penjualan", "money", UIHelper.PRIMARY_COLOR));
        kpiPanel.add(UIHelper.createStatCard("Total Nota", "0", "Transaksi", "receipt", UIHelper.PRIMARY_DARK));
        kpiPanel.add(UIHelper.createStatCard("Tunai (Cash)", "0", "Lunas", "money", UIHelper.ACCENT_GREEN));
        kpiPanel.add(UIHelper.createStatCard("Tempo (Kredit)", "0", "Bertahap", "clock", UIHelper.ACCENT_AMBER));
        kpiPanel.add(UIHelper.createStatCard("Total PPN 11%", "Rp 0", "Pajak", "chart", UIHelper.PRIMARY_COLOR));
        kpiPanel.add(UIHelper.createStatCard("Bunga Kredit", "Rp 0", "Tempo > 2 Mgg", "chart", UIHelper.ACCENT_RED));

        lblTotalRevenue = (JLabel) ((JPanel) kpiPanel.getComponent(0)).getComponent(1);
        lblTotalTrx = (JLabel) ((JPanel) kpiPanel.getComponent(1)).getComponent(1);
        lblCashTrx = (JLabel) ((JPanel) kpiPanel.getComponent(2)).getComponent(1);
        lblCreditTrx = (JLabel) ((JPanel) kpiPanel.getComponent(3)).getComponent(1);
        lblTotalTax = (JLabel) ((JPanel) kpiPanel.getComponent(4)).getComponent(1);
        lblTotalInterest = (JLabel) ((JPanel) kpiPanel.getComponent(5)).getComponent(1);

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        filterBar.add(new JLabel("Cari Nota/Pelanggan:")); filterBar.add(txtSearch);
        filterBar.add(new JLabel("Bayar:")); filterBar.add(cbPaymentFilter);
        filterBar.add(new JLabel("Status:")); filterBar.add(cbStatusFilter);

        JButton btnFilter = new JButton("Filter");
        btnFilter.addActionListener(e -> applyFilter());
        txtSearch.addActionListener(e -> applyFilter());
        cbPaymentFilter.addActionListener(e -> applyFilter());
        cbStatusFilter.addActionListener(e -> applyFilter());
        filterBar.add(btnFilter);

        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(e -> { txtSearch.setText(""); cbPaymentFilter.setSelectedIndex(0); cbStatusFilter.setSelectedIndex(0); applyFilter(); });
        filterBar.add(btnReset);

        northPanel.add(kpiPanel, BorderLayout.NORTH);
        northPanel.add(filterBar, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);

        String[] cols = {"No Nota", "Tanggal", "Pelanggan", "Jenis Bayar", "Total Jual", "PPN (11%)", "Bunga", "Total Bayar", "Souvenir", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        UIHelper.styleTable(table);

        // Apply Custom Table Renderers
        table.getColumnModel().getColumn(0).setCellRenderer(UIHelper.createCenterRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(UIHelper.createCenterRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(UIHelper.createCenterRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(UIHelper.createCurrencyRenderer());
        table.getColumnModel().getColumn(5).setCellRenderer(UIHelper.createCurrencyRenderer());
        table.getColumnModel().getColumn(6).setCellRenderer(UIHelper.createCurrencyRenderer());
        table.getColumnModel().getColumn(7).setCellRenderer(UIHelper.createCurrencyRenderer());
        table.getColumnModel().getColumn(8).setCellRenderer(UIHelper.createSouvenirBadgeRenderer());
        table.getColumnModel().getColumn(9).setCellRenderer(UIHelper.createStatusBadgeRenderer());

        table.getColumnModel().getColumn(0).setPreferredWidth(140);
        table.getColumnModel().getColumn(1).setPreferredWidth(130);
        table.getColumnModel().getColumn(2).setPreferredWidth(190);
        table.getColumnModel().getColumn(3).setPreferredWidth(95);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(90);
        table.getColumnModel().getColumn(6).setPreferredWidth(85);
        table.getColumnModel().getColumn(7).setPreferredWidth(110);
        table.getColumnModel().getColumn(8).setPreferredWidth(140);
        table.getColumnModel().getColumn(9).setPreferredWidth(95);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) showSelectedDetails();
        });

        String[] itemCols = {"Kode", "Nama Produk", "Satuan", "Harga Jual", "Qty", "Subtotal"};
        itemsModel = new DefaultTableModel(itemCols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        itemsTable = new JTable(itemsModel);
        UIHelper.styleTable(itemsTable);
        itemsTable.getColumnModel().getColumn(0).setCellRenderer(UIHelper.createCenterRenderer());
        itemsTable.getColumnModel().getColumn(2).setCellRenderer(UIHelper.createCenterRenderer());
        itemsTable.getColumnModel().getColumn(3).setCellRenderer(UIHelper.createCurrencyRenderer());
        itemsTable.getColumnModel().getColumn(4).setCellRenderer(UIHelper.createCenterRenderer());
        itemsTable.getColumnModel().getColumn(5).setCellRenderer(UIHelper.createCurrencyRenderer());

        itemsTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        itemsTable.getColumnModel().getColumn(1).setPreferredWidth(260);
        itemsTable.getColumnModel().getColumn(2).setPreferredWidth(70);
        itemsTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        itemsTable.getColumnModel().getColumn(4).setPreferredWidth(60);
        itemsTable.getColumnModel().getColumn(5).setPreferredWidth(130);

        JPanel detailPnl = new JPanel(new BorderLayout());
        detailPnl.setBorder(BorderFactory.createTitledBorder("Rincian Item Produk Terpilih"));
        detailPnl.add(new JScrollPane(itemsTable), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(table), detailPnl);
        split.setResizeWeight(0.65);
        split.setDividerLocation(260);
        add(split, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        JButton btnViewReceipt = UIHelper.createPrimaryButton("Cetak / Buka Struk", UIHelper.getVectorIcon("receipt", 14, Color.WHITE));
        JButton btnExportCsv = UIHelper.createButton("Ekspor CSV", UIHelper.getVectorIcon("chart", 14, UIHelper.TEXT_MAIN));
        JButton btnDelete = UIHelper.createDangerButton("Batalkan Nota", UIHelper.getVectorIcon("trash", 14, Color.WHITE));
        JButton btnRefresh = UIHelper.createButton("Muat Ulang", UIHelper.getVectorIcon("refresh", 14, UIHelper.TEXT_MAIN));

        btnViewReceipt.addActionListener(e -> viewReceipt());
        btnExportCsv.addActionListener(e -> exportToCsv());
        btnDelete.addActionListener(e -> deleteTransaction());
        btnRefresh.addActionListener(e -> loadData());

        actions.add(btnRefresh); actions.add(btnExportCsv); actions.add(btnDelete); actions.add(btnViewReceipt);
        add(actions, BorderLayout.SOUTH);
    }

    public void loadData() {
        tableModel.setRowCount(0);
        itemsModel.setRowCount(0);
        List<Transaction> list = repo.getAllTransactions();

        double totalOmzet = 0, totalTax = 0, totalInterest = 0;
        int cashCount = 0, creditCount = 0;

        for (Transaction t : list) {
            totalOmzet += t.getTotalBayar();
            totalTax += t.getTaxAmount();
            totalInterest += t.getInterestAmount();

            if (t.getPaymentType() == PaymentType.CASH) cashCount++;
            else creditCount++;

            tableModel.addRow(new Object[]{
                t.getInvoiceNumber(), t.getTransactionDate(), t.getCustomerName(),
                t.getPaymentType().getDisplayName(), UIHelper.formatRupiah(t.getTotalJual()),
                UIHelper.formatRupiah(t.getTaxAmount()), UIHelper.formatRupiah(t.getInterestAmount()),
                UIHelper.formatRupiah(t.getTotalBayar()), t.getSouvenir(), t.getStatus()
            });
        }

        lblTotalRevenue.setText(UIHelper.formatRupiah(totalOmzet));
        lblTotalTrx.setText(String.valueOf(list.size()));
        lblCashTrx.setText(String.valueOf(cashCount));
        lblCreditTrx.setText(String.valueOf(creditCount));
        lblTotalTax.setText(UIHelper.formatRupiah(totalTax));
        lblTotalInterest.setText(UIHelper.formatRupiah(totalInterest));

        if (!list.isEmpty()) {
            table.setRowSelectionInterval(0, 0);
            showSelectedDetails();
        }
    }

    private void applyFilter() {
        String text = txtSearch.getText().trim().toLowerCase();
        String pay = (String) cbPaymentFilter.getSelectedItem();
        String stat = (String) cbStatusFilter.getSelectedItem();

        sorter.setRowFilter(new RowFilter<DefaultTableModel, Object>() {
            public boolean include(Entry<? extends DefaultTableModel, ?> entry) {
                String inv = entry.getStringValue(0).toLowerCase();
                String cust = entry.getStringValue(2).toLowerCase();
                String p = entry.getStringValue(3);
                String s = entry.getStringValue(9);

                boolean matchText = text.isEmpty() || inv.contains(text) || cust.contains(text);
                boolean matchPay = pay == null || pay.startsWith("Semua") || p.toLowerCase().contains(pay.toLowerCase());
                boolean matchStat = stat == null || stat.startsWith("Semua") || s.equalsIgnoreCase(stat);
                return matchText && matchPay && matchStat;
            }
        });
    }

    private void showSelectedDetails() {
        int row = table.getSelectedRow();
        itemsModel.setRowCount(0);
        if (row < 0) return;
        int mRow = table.convertRowIndexToModel(row);
        String inv = (String) tableModel.getValueAt(mRow, 0);
        for (Transaction t : repo.getAllTransactions()) {
            if (t.getInvoiceNumber().equalsIgnoreCase(inv)) {
                for (TransactionItem item : t.getItems()) {
                    itemsModel.addRow(new Object[]{
                        item.getProductCode(), item.getProductName(), item.getUnit(),
                        UIHelper.formatRupiah(item.getSellingPrice()), item.getQuantity(),
                        UIHelper.formatRupiah(item.getSubtotal())
                    });
                }
                break;
            }
        }
    }

    private void viewReceipt() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang ingin dilihat struknya!");
            return;
        }
        int mRow = table.convertRowIndexToModel(row);
        String inv = (String) tableModel.getValueAt(mRow, 0);
        for (Transaction t : repo.getAllTransactions()) {
            if (t.getInvoiceNumber().equalsIgnoreCase(inv)) {
                Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
                new ReceiptDialog(parent, t).setVisible(true);
                return;
            }
        }
    }

    private void deleteTransaction() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang ingin dibatalkan!");
            return;
        }
        int mRow = table.convertRowIndexToModel(row);
        String inv = (String) tableModel.getValueAt(mRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Batalkan transaksi " + inv + " dan kembalikan stok barang?",
            "Konfirmasi Pembatalan", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            repo.deleteTransaction(inv);
            loadData();
            JOptionPane.showMessageDialog(this, "Transaksi " + inv + " berhasil dibatalkan dan stok dikembalikan.");
        }
    }

    private void exportToCsv() {
        JFileChooser ch = new JFileChooser();
        ch.setSelectedFile(new File("Laporan_Transaksi_Grosir.csv"));
        if (ch.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (FileWriter w = new FileWriter(ch.getSelectedFile())) {
                w.write("No Nota,Tanggal,Pelanggan,Jenis Bayar,Total Jual,PPN,Bunga,Total Bayar,Souvenir,Status\n");
                for (int r = 0; r < table.getRowCount(); r++) {
                    StringBuilder sb = new StringBuilder();
                    for (int c = 0; c < table.getColumnCount(); c++) {
                        String val = table.getValueAt(r, c).toString().replace("\"", "\"\"");
                        sb.append("\"").append(val).append("\"");
                        if (c < table.getColumnCount() - 1) sb.append(",");
                    }
                    sb.append("\n");
                    w.write(sb.toString());
                }
                JOptionPane.showMessageDialog(this, "Laporan berhasil diekspor ke CSV!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal mengekspor: " + ex.getMessage());
            }
        }
    }
}
