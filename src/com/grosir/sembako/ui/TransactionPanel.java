package com.grosir.sembako.ui;

import com.grosir.sembako.model.*;
import com.grosir.sembako.service.DataRepository;
import com.grosir.sembako.service.PricingCalculator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransactionPanel extends JPanel {
    private final DataRepository repo = DataRepository.getInstance();
    private final List<TransactionItem> cart = new ArrayList<>();

    private JTextField txtInvoiceNo = new JTextField(14);
    private JTextField txtCustomer = new JTextField("Toko Rejeki - Semarang", 18);
    private JTextField txtPhone = new JTextField(12);

    private JComboBox<Product> cbProduct;
    private JSpinner spQty = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
    private JLabel lblProductInfo = new JLabel("-");

    private JTable cartTable;
    private DefaultTableModel cartModel;

    private JLabel lblSubtotal = new JLabel("Rp 0");
    private JRadioButton rbCash = new JRadioButton("Cash (Tunai)", true);
    private JRadioButton rbCredit = new JRadioButton("Tempo (Kredit)");
    private JSpinner spTempoWeeks = new JSpinner(new SpinnerNumberModel(1, 1, 52, 1));
    private JLabel lblDueDate = new JLabel("-");
    private JLabel lblTax = new JLabel("Rp 0");
    private JLabel lblInterest = new JLabel("Rp 0");
    private JLabel lblTotalPayment = new JLabel("Rp 0");
    private JLabel lblSouvenir = new JLabel("Tidak Ada");
    private JLabel lblSouvenirHint = new JLabel("");
    private JTextField txtCashPaid = new JTextField("0", 12);
    private JLabel lblChange = new JLabel("Rp 0");

    public TransactionPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        initUI();
        resetForm();
    }

    private void initUI() {
        JPanel headerPanel = UIHelper.createCardPanel();
        headerPanel.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        txtInvoiceNo.setEditable(false);
        txtInvoiceNo.setFont(UIHelper.FONT_BOLD);
        txtInvoiceNo.setForeground(UIHelper.PRIMARY_COLOR);

        g.gridx = 0; g.gridy = 0; headerPanel.add(new JLabel("No Nota:"), g);
        g.gridx = 1; g.gridy = 0; headerPanel.add(txtInvoiceNo, g);
        g.gridx = 2; g.gridy = 0; headerPanel.add(new JLabel("Pelanggan/Cabang:"), g);
        g.gridx = 3; g.gridy = 0; headerPanel.add(txtCustomer, g);
        g.gridx = 4; g.gridy = 0; headerPanel.add(new JLabel("No Kontak:"), g);
        g.gridx = 5; g.gridy = 0; headerPanel.add(txtPhone, g);

        // Center Split: Left is Item Selection & Cart, Right is Payment Card
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        // Selection Bar
        JPanel selBar = new JPanel(new BorderLayout(5, 4));
        selBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Pilih Produk Sembako"),
                BorderFactory.createEmptyBorder(2, 6, 4, 6)
        ));

        JPanel rowInput = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        cbProduct = new JComboBox<>();
        cbProduct.setPreferredSize(new Dimension(280, 32));
        cbProduct.addActionListener(e -> onProductSelected());

        spQty.setPreferredSize(new Dimension(65, 32));

        JButton btnAddCart = UIHelper.createPrimaryButton("Tambah ke Nota", UIHelper.getVectorIcon("plus", 14, Color.WHITE));
        btnAddCart.setPreferredSize(new Dimension(170, 32));
        btnAddCart.addActionListener(e -> addToCart());

        rowInput.add(new JLabel("Produk:"));
        rowInput.add(cbProduct);
        rowInput.add(new JLabel("Qty:"));
        rowInput.add(spQty);
        rowInput.add(btnAddCart);

        lblProductInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblProductInfo.setForeground(new Color(71, 85, 105));
        JPanel rowInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        rowInfo.add(lblProductInfo);

        selBar.add(rowInput, BorderLayout.NORTH);
        selBar.add(rowInfo, BorderLayout.SOUTH);

        // Cart Table
        String[] cols = {"No", "Kode", "Nama Produk", "Satuan", "Harga Jual", "Qty", "Subtotal"};
        cartModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        cartTable = new JTable(cartModel);
        UIHelper.styleTable(cartTable);
        cartTable.getColumnModel().getColumn(0).setCellRenderer(UIHelper.createCenterRenderer());
        cartTable.getColumnModel().getColumn(1).setCellRenderer(UIHelper.createCenterRenderer());
        cartTable.getColumnModel().getColumn(3).setCellRenderer(UIHelper.createCenterRenderer());
        cartTable.getColumnModel().getColumn(4).setCellRenderer(UIHelper.createCurrencyRenderer());
        cartTable.getColumnModel().getColumn(5).setCellRenderer(UIHelper.createCenterRenderer());
        cartTable.getColumnModel().getColumn(6).setCellRenderer(UIHelper.createCurrencyRenderer());

        cartTable.getColumnModel().getColumn(0).setPreferredWidth(45);
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(85);
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(210);
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(65);
        cartTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        cartTable.getColumnModel().getColumn(5).setPreferredWidth(55);
        cartTable.getColumnModel().getColumn(6).setPreferredWidth(110);

        JPanel cartContainer = new JPanel(new BorderLayout(5, 5));
        cartContainer.add(selBar, BorderLayout.NORTH);
        cartContainer.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        JPanel cartActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        JButton btnRemoveItem = UIHelper.createDangerButton("Hapus Item Terpilih", UIHelper.getVectorIcon("trash", 14, Color.WHITE));
        btnRemoveItem.addActionListener(e -> removeFromCart());
        cartActions.add(btnRemoveItem);
        cartContainer.add(cartActions, BorderLayout.SOUTH);

        // Right Payment & Calculation Panel
        JPanel rightPanel = createPaymentPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, cartContainer, rightPanel);
        splitPane.setResizeWeight(0.52);
        splitPane.setDividerLocation(640);

        centerPanel.add(headerPanel, BorderLayout.NORTH);
        centerPanel.add(splitPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createPaymentPanel() {
        JPanel p = UIHelper.createCardPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setPreferredSize(new Dimension(460, 520));

        JLabel title = new JLabel(" Rincian Pembayaran");
        title.setIcon(UIHelper.getVectorIcon("credit_card", 18, UIHelper.PRIMARY_COLOR));
        title.setFont(UIHelper.FONT_HEADER);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(title);
        p.add(Box.createVerticalStrut(10));

        ButtonGroup bg = new ButtonGroup();
        bg.add(rbCash); bg.add(rbCredit);
        JPanel payTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        payTypePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        payTypePanel.add(rbCash); payTypePanel.add(rbCredit);
        p.add(payTypePanel);

        // Tempo options
        JPanel tempoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        tempoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tempoPanel.add(new JLabel("Tempo (minggu):"));
        tempoPanel.add(spTempoWeeks);
        tempoPanel.add(new JLabel("Jatuh Tempo:"));
        tempoPanel.add(lblDueDate);
        p.add(tempoPanel);

        rbCash.addActionListener(e -> { tempoPanel.setVisible(false); recalculateBill(); });
        rbCredit.addActionListener(e -> { tempoPanel.setVisible(true); recalculateBill(); });
        spTempoWeeks.addChangeListener(e -> recalculateBill());
        tempoPanel.setVisible(false);

        p.add(Box.createVerticalStrut(10));
        p.add(new JSeparator());
        p.add(Box.createVerticalStrut(8));

        // Subtotal, Tax, Interest, Total
        p.add(createRowPanel("Total Jual (Subtotal):", lblSubtotal, UIHelper.FONT_BOLD));
        p.add(createRowPanel("PPN (11%):", lblTax, UIHelper.FONT_REGULAR));
        p.add(createRowPanel("Bunga Kredit (50% jika >2 mgg):", lblInterest, UIHelper.FONT_REGULAR));

        p.add(Box.createVerticalStrut(8));
        lblTotalPayment.setFont(UIHelper.FONT_BIG_AMOUNT);
        lblTotalPayment.setForeground(UIHelper.ACCENT_GREEN);
        p.add(createRowPanel("TOTAL TAGIHAN:", lblTotalPayment, UIHelper.FONT_BIG_AMOUNT));

        p.add(Box.createVerticalStrut(10));
        // Souvenir Card
        JPanel souvenirBox = new JPanel(new BorderLayout(5, 5));
        souvenirBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIHelper.ACCENT_PURPLE, 1, true),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        souvenirBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblSouvenirTitle = new JLabel(" Hadiah Langsung:");
        lblSouvenirTitle.setIcon(UIHelper.getVectorIcon("gift", 16, UIHelper.ACCENT_PURPLE));
        lblSouvenirTitle.setFont(UIHelper.FONT_BOLD);
        lblSouvenir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSouvenir.setForeground(UIHelper.ACCENT_PURPLE);
        lblSouvenirHint.setIcon(UIHelper.getVectorIcon("check", 12, UIHelper.ACCENT_PURPLE));
        lblSouvenirHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblSouvenirHint.setForeground(UIHelper.TEXT_MUTED);
        souvenirBox.add(lblSouvenirTitle, BorderLayout.NORTH);
        souvenirBox.add(lblSouvenir, BorderLayout.CENTER);
        souvenirBox.add(lblSouvenirHint, BorderLayout.SOUTH);
        p.add(souvenirBox);

        p.add(Box.createVerticalStrut(10));
        // Cash payment and change
        JPanel cashBox = new JPanel(new GridLayout(2, 2, 6, 6));
        cashBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        cashBox.add(new JLabel("Uang Tunai (Rp):"));
        cashBox.add(txtCashPaid);
        cashBox.add(new JLabel("Kembalian:"));
        lblChange.setFont(UIHelper.FONT_BOLD);
        cashBox.add(lblChange);
        p.add(cashBox);

        txtCashPaid.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateChange(); }
            public void removeUpdate(DocumentEvent e) { updateChange(); }
            public void changedUpdate(DocumentEvent e) { updateChange(); }
        });

        p.add(Box.createVerticalGlue());
        p.add(Box.createVerticalStrut(10));

        JButton btnProcess = UIHelper.createSuccessButton("Selesaikan & Cetak Nota", UIHelper.getVectorIcon("save", 16, Color.WHITE));
        btnProcess.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnProcess.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
        btnProcess.addActionListener(e -> processTransaction());
        p.add(btnProcess);

        p.add(Box.createVerticalStrut(6));
        JButton btnReset = UIHelper.createButton("Reset Nota", UIHelper.getVectorIcon("refresh", 14, UIHelper.TEXT_MAIN));
        btnReset.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnReset.setMaximumSize(new Dimension(Short.MAX_VALUE, 32));
        btnReset.addActionListener(e -> resetForm());
        p.add(btnReset);

        return p;
    }

    private JPanel createRowPanel(String label, JLabel valLabel, Font font) {
        JPanel r = new JPanel(new BorderLayout(8, 0));
        r.setOpaque(false);
        r.setAlignmentX(Component.LEFT_ALIGNMENT);
        r.setMaximumSize(new Dimension(Short.MAX_VALUE, 36));
        JLabel l = new JLabel(label);
        l.setFont(font);
        valLabel.setFont(font);
        valLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        r.add(l, BorderLayout.WEST);
        r.add(valLabel, BorderLayout.CENTER);
        return r;
    }

    public void refreshProductList() {
        cbProduct.removeAllItems();
        for (Product p : repo.getAllProducts()) {
            cbProduct.addItem(p);
        }
        onProductSelected();
    }

    private void onProductSelected() {
        Product p = (Product) cbProduct.getSelectedItem();
        if (p != null) {
            lblProductInfo.setText("Harga: " + UIHelper.formatRupiah(p.getSellingPrice()) + " / " + p.getUnit() + " (Stok: " + p.getStock() + ")");
            int maxStock = Math.max(1, p.getStock());
            spQty.setModel(new SpinnerNumberModel(1, 1, maxStock, 1));
        } else {
            lblProductInfo.setText("-");
        }
    }

    private void addToCart() {
        Product p = (Product) cbProduct.getSelectedItem();
        if (p == null) return;
        if (p.getStock() <= 0) {
            JOptionPane.showMessageDialog(this, "Stok habis!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int qty = (Integer) spQty.getValue();
        TransactionItem existing = null;
        for (TransactionItem item : cart) {
            if (item.getProductCode().equalsIgnoreCase(p.getCode())) {
                existing = item;
                break;
            }
        }

        if (existing != null) {
            int newQty = existing.getQuantity() + qty;
            if (newQty > p.getStock()) {
                JOptionPane.showMessageDialog(this, "Melebihi stok (" + p.getStock() + ")!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            existing.setQuantity(newQty);
        } else {
            if (qty > p.getStock()) {
                JOptionPane.showMessageDialog(this, "Melebihi stok (" + p.getStock() + ")!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            cart.add(new TransactionItem(p.getCode(), p.getName(), p.getUnit(), p.getPurchasePrice(), p.getSellingPrice(), qty));
        }

        updateCartTable();
        recalculateBill();
    }

    private void removeFromCart() {
        int row = cartTable.getSelectedRow();
        if (row >= 0 && row < cart.size()) {
            cart.remove(row);
            updateCartTable();
            recalculateBill();
        } else {
            JOptionPane.showMessageDialog(this, "Pilih item yang ingin dihapus!");
        }
    }

    private void updateCartTable() {
        cartModel.setRowCount(0);
        int no = 1;
        for (TransactionItem item : cart) {
            cartModel.addRow(new Object[]{
                no++, item.getProductCode(), item.getProductName(),
                item.getUnit(), UIHelper.formatRupiah(item.getSellingPrice()),
                item.getQuantity(), UIHelper.formatRupiah(item.getSubtotal())
            });
        }
    }

    private void recalculateBill() {
        double subtotal = 0;
        for (TransactionItem item : cart) subtotal += item.getSubtotal();
        lblSubtotal.setText(UIHelper.formatRupiah(subtotal));

        PaymentType payType = rbCash.isSelected() ? PaymentType.CASH : PaymentType.CREDIT;
        int weeks = (Integer) spTempoWeeks.getValue();

        if (payType == PaymentType.CREDIT) {
            LocalDate due = LocalDate.now().plusWeeks(weeks);
            lblDueDate.setText(due.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        } else {
            lblDueDate.setText("-");
        }

        double tax = PricingCalculator.calculateTax(subtotal);
        double interest = PricingCalculator.calculateInterest(subtotal, payType, weeks);
        double total = PricingCalculator.calculateTotalPayment(subtotal, tax, interest);

        lblTax.setText(UIHelper.formatRupiah(tax));
        lblInterest.setText(UIHelper.formatRupiah(interest));
        lblTotalPayment.setText(UIHelper.formatRupiah(total));

        String souvenir = PricingCalculator.determineSouvenir(payType, total);
        lblSouvenir.setText(souvenir);
        lblSouvenirHint.setText(PricingCalculator.getNextGiftTierMessage(payType, total));
        updateChange();
    }

    private void updateChange() {
        try {
            double total = getCalculatedTotal();
            String cashStr = txtCashPaid.getText().trim().replace(".", "").replace(",", "");
            double paid = cashStr.isEmpty() ? 0 : Double.parseDouble(cashStr);
            double change = paid - total;
            lblChange.setText(UIHelper.formatRupiah(Math.max(0, change)));
            if (rbCash.isSelected() && paid < total && paid > 0) {
                lblChange.setForeground(UIHelper.ACCENT_RED);
                lblChange.setText("Kurang: " + UIHelper.formatRupiah(total - paid));
            } else {
                lblChange.setForeground(UIHelper.ACCENT_GREEN);
            }
        } catch (Exception e) {
            lblChange.setText("Rp 0");
        }
    }

    private double getCalculatedTotal() {
        double subtotal = 0;
        for (TransactionItem item : cart) subtotal += item.getSubtotal();
        PaymentType payType = rbCash.isSelected() ? PaymentType.CASH : PaymentType.CREDIT;
        int weeks = (Integer) spTempoWeeks.getValue();
        double tax = PricingCalculator.calculateTax(subtotal);
        double interest = PricingCalculator.calculateInterest(subtotal, payType, weeks);
        return PricingCalculator.calculateTotalPayment(subtotal, tax, interest);
    }

    private void processTransaction() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang belanja masih kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String cust = txtCustomer.getText().trim();
        if (cust.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama pelanggan/toko wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PaymentType payType = rbCash.isSelected() ? PaymentType.CASH : PaymentType.CREDIT;
        int weeks = (Integer) spTempoWeeks.getValue();
        double total = getCalculatedTotal();
        double paid = 0;

        if (payType == PaymentType.CASH) {
            try {
                paid = Double.parseDouble(txtCashPaid.getText().trim().replace(".", "").replace(",", ""));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Nominal pembayaran belum valid!");
                return;
            }
            if (paid < total) {
                JOptionPane.showMessageDialog(this, "Nominal uang tunai kurang dari total tagihan!");
                return;
            }
        }

        Transaction t = new Transaction();
        t.setInvoiceNumber(txtInvoiceNo.getText());
        t.setTransactionDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        t.setCustomerName(cust);
        t.setCustomerPhone(txtPhone.getText().trim());
        t.setItems(new ArrayList<>(cart));

        double subtotal = 0;
        for (TransactionItem i : cart) subtotal += i.getSubtotal();
        t.setTotalJual(subtotal);
        t.setPaymentType(payType);
        t.setTempoWeeks(payType == PaymentType.CREDIT ? weeks : 0);
        if (payType == PaymentType.CREDIT) {
            t.setDueDate(LocalDate.now().plusWeeks(weeks).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        t.setTaxRate(0.11);
        t.setTaxAmount(PricingCalculator.calculateTax(subtotal));
        t.setInterestRate(PricingCalculator.getInterestRate(payType, weeks));
        t.setInterestAmount(PricingCalculator.calculateInterest(subtotal, payType, weeks));
        t.setTotalBayar(total);
        t.setCashPaid(paid);
        t.setChangeAmount(payType == PaymentType.CASH ? (paid - total) : 0);
        t.setSouvenir(PricingCalculator.determineSouvenir(payType, total));
        t.setStatus(payType == PaymentType.CASH ? "LUNAS" : "BELUM LUNAS");

        repo.saveTransaction(t);

        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        ReceiptDialog receiptDlg = new ReceiptDialog(parent, t);
        receiptDlg.setVisible(true);

        resetForm();
    }

    public void resetForm() {
        cart.clear();
        updateCartTable();
        txtInvoiceNo.setText(repo.generateNextInvoiceNumber());
        rbCash.setSelected(true);
        spTempoWeeks.setValue(1);
        txtCashPaid.setText("0");
        refreshProductList();
        recalculateBill();
    }
}
