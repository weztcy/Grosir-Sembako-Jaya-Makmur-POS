package com.grosir.sembako.ui;

import com.grosir.sembako.model.PaymentType;
import com.grosir.sembako.model.Transaction;
import com.grosir.sembako.model.TransactionItem;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;

public class ReceiptDialog extends JDialog {
    private final Transaction t;
    private JTextArea receiptArea;

    public ReceiptDialog(Frame parent, Transaction transaction) {
        super(parent, "Nota Transaksi - " + transaction.getInvoiceNumber(), true);
        this.t = transaction;
        initUI();
    }

    private void initUI() {
        setSize(500, 640);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        receiptArea = new JTextArea();
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        receiptArea.setEditable(false);
        receiptArea.setText(generateReceiptText());
        receiptArea.setCaretPosition(0);

        add(new JScrollPane(receiptArea), BorderLayout.CENTER);

        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnPrint = UIHelper.createPrimaryButton("Cetak / Print", UIHelper.getVectorIcon("print", 14, Color.WHITE));
        JButton btnSaveTxt = UIHelper.createButton("Simpan .TXT", UIHelper.getVectorIcon("save", 14, UIHelper.TEXT_MAIN));
        JButton btnClose = UIHelper.createButton("Tutup", UIHelper.getVectorIcon("close", 12, UIHelper.TEXT_MAIN));

        btnPrint.addActionListener(e -> {
            try {
                if (receiptArea.print()) JOptionPane.showMessageDialog(this, "Nota berhasil dikirim ke printer.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal mencetak: " + ex.getMessage());
            }
        });

        btnSaveTxt.addActionListener(e -> saveToFile());
        btnClose.addActionListener(e -> dispose());

        pnl.add(btnSaveTxt);
        pnl.add(btnPrint);
        pnl.add(btnClose);
        add(pnl, BorderLayout.SOUTH);
    }

    private String generateReceiptText() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================\n");
        sb.append("         TOKO GROSIR SEMBAKO SUMBER REZEKI              \n");
        sb.append("    Jl. Pemuda No. 88, Semarang | Telp: (024) 87654321   \n");
        sb.append("========================================================\n");
        sb.append(String.format("No Nota     : %s\n", t.getInvoiceNumber()));
        sb.append(String.format("Tanggal     : %s\n", t.getTransactionDate()));
        sb.append(String.format("Pelanggan   : %s\n", t.getCustomerName()));
        if (t.getCustomerPhone() != null && !t.getCustomerPhone().isEmpty()) {
            sb.append(String.format("No Telp     : %s\n", t.getCustomerPhone()));
        }
        sb.append(String.format("Pembayaran  : %s\n", t.getPaymentType().getDisplayName()));
        if (t.getPaymentType() == PaymentType.CREDIT) {
            sb.append(String.format("Tempo       : %d Minggu (Jatuh Tempo: %s)\n", t.getTempoWeeks(), t.getDueDate()));
        }
        sb.append("--------------------------------------------------------\n");
        sb.append(String.format("%-22s %5s %12s %13s\n", "Item Barang", "Qty", "Harga", "Subtotal"));
        sb.append("--------------------------------------------------------\n");

        for (TransactionItem item : t.getItems()) {
            String name = item.getProductName();
            if (name.length() > 22) name = name.substring(0, 19) + "...";
            sb.append(String.format("%-22s %5d %12s %13s\n",
                    name, item.getQuantity(),
                    UIHelper.formatRupiah(item.getSellingPrice()),
                    UIHelper.formatRupiah(item.getSubtotal())));
        }

        sb.append("--------------------------------------------------------\n");
        sb.append(String.format("%-30s: %22s\n", "Total Jual (Subtotal)", UIHelper.formatRupiah(t.getTotalJual())));
        sb.append(String.format("%-30s: %22s\n", "PPN (11%)", UIHelper.formatRupiah(t.getTaxAmount())));
        if (t.getInterestAmount() > 0) {
            sb.append(String.format("%-30s: %22s\n", "Bunga Kredit (50%)", UIHelper.formatRupiah(t.getInterestAmount())));
        }
        sb.append("========================================================\n");
        sb.append(String.format("%-30s: %22s\n", "TOTAL BAYAR", UIHelper.formatRupiah(t.getTotalBayar())));
        
        if (t.getPaymentType() == PaymentType.CASH) {
            sb.append(String.format("%-30s: %22s\n", "Tunai Diterima", UIHelper.formatRupiah(t.getCashPaid())));
            sb.append(String.format("%-30s: %22s\n", "Kembalian", UIHelper.formatRupiah(t.getChangeAmount())));
        } else {
            sb.append(String.format("%-30s: %22s\n", "Status Bayar", t.getStatus()));
        }
        
        sb.append("--------------------------------------------------------\n");
        sb.append(String.format("Hadiah/Souvenir : %s\n", t.getSouvenir()));
        if (t.getNotes() != null && !t.getNotes().isEmpty()) {
            sb.append(String.format("Catatan         : %s\n", t.getNotes()));
        }
        sb.append("========================================================\n");
        sb.append("        Terima Kasih Atas Kepercayaan Anda!             \n");
        sb.append("========================================================\n");
        return sb.toString();
    }

    private void saveToFile() {
        JFileChooser ch = new JFileChooser();
        ch.setSelectedFile(new File(t.getInvoiceNumber() + ".txt"));
        if (ch.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (FileWriter w = new FileWriter(ch.getSelectedFile())) {
                w.write(generateReceiptText());
                JOptionPane.showMessageDialog(this, "Nota berhasil disimpan.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan: " + ex.getMessage());
            }
        }
    }
}
