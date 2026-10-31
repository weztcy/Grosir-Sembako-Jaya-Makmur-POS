package com.grosir.sembako.ui;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainFrame extends JFrame {
    private TransactionPanel transactionPanel;
    private ProductPanel productPanel;
    private ReportPanel reportPanel;
    private JTabbedPane tabbedPane;
    private JLabel lblStatusTime;

    public MainFrame() {
        setTitle("Grosir Sembako Makmur POS - Enterprise Desktop Edition");
        setSize(1280, 840);
        setMinimumSize(new Dimension(1100, 720));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        startClock();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout(15, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(15, 23, 42), getWidth(), 0, new Color(30, 41, 59));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        headerPanel.setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 24));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 3));
        titlePanel.setOpaque(false);
        
        JPanel brandRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brandRow.setOpaque(false);
        JLabel lblAppTitle = new JLabel(" GROSIR SEMBAKO JAYA MAKMUR");
        lblAppTitle.setIcon(UIHelper.getVectorIcon("store", 22, Color.WHITE));
        lblAppTitle.setFont(new Font("Segoe UI", Font.BOLD, 21));
        lblAppTitle.setForeground(Color.WHITE);

        JLabel lblBadge = new JLabel(" ENTERPRISE POS ");
        lblBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblBadge.setForeground(new Color(16, 185, 129));
        lblBadge.setOpaque(true);
        lblBadge.setBackground(new Color(6, 78, 59));
        lblBadge.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));

        brandRow.add(lblAppTitle);
        brandRow.add(lblBadge);

        JLabel lblAppSub = new JLabel("Sistem Kasir Grosir • Multi-Tier Margin Markup • PPN 11% • Bunga Tempo Kredit • Hadiah Langsung Tunai");
        lblAppSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblAppSub.setForeground(new Color(148, 163, 184));
        titlePanel.add(brandRow);
        titlePanel.add(lblAppSub);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightHeader.setOpaque(false);

        JLabel lblLiveBadge = new JLabel(" ONLINE");
        lblLiveBadge.setIcon(UIHelper.getVectorIcon("dot", 8, new Color(52, 211, 153)));
        lblLiveBadge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblLiveBadge.setForeground(new Color(52, 211, 153));

        lblStatusTime = new JLabel();
        lblStatusTime.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        lblStatusTime.setForeground(new Color(241, 245, 249));

        rightHeader.add(lblLiveBadge);
        rightHeader.add(lblStatusTime);
        headerPanel.add(rightHeader, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        transactionPanel = new TransactionPanel();
        productPanel = new ProductPanel();
        reportPanel = new ReportPanel();

        tabbedPane.addTab("Kasir POS & Transaksi", UIHelper.getVectorIcon("cart", 16, UIHelper.PRIMARY_COLOR), transactionPanel);
        tabbedPane.addTab("Master Produk & Stok", UIHelper.getVectorIcon("box", 16, UIHelper.PRIMARY_COLOR), productPanel);
        tabbedPane.addTab("Laporan & Riwayat Nota", UIHelper.getVectorIcon("chart", 16, UIHelper.PRIMARY_COLOR), reportPanel);
        tabbedPane.addTab("Aturan Bisnis & Margin", UIHelper.getVectorIcon("book", 16, UIHelper.PRIMARY_COLOR), createHelpPanel());

        tabbedPane.addChangeListener(e -> {
            int idx = tabbedPane.getSelectedIndex();
            if (idx == 0) transactionPanel.refreshProductList();
            else if (idx == 1) productPanel.loadData();
            else if (idx == 2) reportPanel.loadData();
        });
        add(tabbedPane, BorderLayout.CENTER);

        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UIHelper.BORDER_COLOR),
                BorderFactory.createEmptyBorder(7, 20, 7, 20)
        ));
        statusBar.setBackground(Color.WHITE);

        JLabel lblDbInfo = new JLabel(" Basis Data: JSON Persistent Storage • Auto-Commit Aktif");
        lblDbInfo.setIcon(UIHelper.getVectorIcon("database", 14, UIHelper.TEXT_MUTED));
        lblDbInfo.setFont(UIHelper.FONT_REGULAR);
        lblDbInfo.setForeground(UIHelper.TEXT_MUTED);

        JLabel lblVersion = new JLabel("Java Swing POS v2.5 Enterprise • FlatLaf UI • OpenJDK 17");
        lblVersion.setFont(UIHelper.FONT_REGULAR);
        lblVersion.setForeground(UIHelper.TEXT_MUTED);

        statusBar.add(lblDbInfo, BorderLayout.WEST);
        statusBar.add(lblVersion, BorderLayout.EAST);
        add(statusBar, BorderLayout.SOUTH);
    }

    private JPanel createHelpPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        p.setBackground(new Color(248, 250, 252));

        JEditorPane ep = new JEditorPane();
        ep.setContentType("text/html");
        ep.setEditable(false);
        ep.setBackground(Color.WHITE);
        ep.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIHelper.BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(18, 22, 18, 22)
        ));
        ep.setText(
            "<html><body style='font-family: Segoe UI, sans-serif; font-size: 13px; color: #1E293B;'>" +
            "<h2 style='color: #1E40AF; margin-top:0;'>Panduan & Referensi Formula Bisnis Grosir Sembako</h2>" +
            "<p style='color: #64748B;'>Formula penetapan harga, pajak, tempo, dan reward sesuai standar Grosir Sembako.</p>" +
            "<hr style='border: 0; border-top: 1px solid #E2E8F0; margin: 12px 0;'>" +
            "<table width='100%' cellpadding='6' cellspacing='0' style='border-collapse: collapse;'>" +
            "<tr bgcolor='#F1F5F9'><th align='left'>1. Kategori Modal & Margin Markup</th><th align='left'>Markup</th><th align='left'>Contoh Produk</th></tr>" +
            "<tr><td>Modal &gt; Rp 200.000</td><td><b>+10%</b></td><td>Beras Karung 25kg, Gula Sak 50kg</td></tr>" +
            "<tr><td>Modal Rp 100.001 - Rp 200.000</td><td><b>+15%</b></td><td>Indomie Dus, Kopi Dus, Beras 10kg</td></tr>" +
            "<tr><td>Modal Rp 50.001 - Rp 100.000</td><td><b>+20%</b></td><td>Beras 5kg, Telur Ayam Karpet</td></tr>" +
            "<tr><td>Modal Rp 25.001 - Rp 50.000</td><td><b>+30%</b></td><td>Minyak Goreng 2L, Kecap Bango 1.5L</td></tr>" +
            "<tr><td>Modal &le; Rp 25.000</td><td><b>+50%</b></td><td>Gula 1kg, Tepung Segitiga 1kg</td></tr>" +
            "</table><br>" +
            "<table width='100%' cellpadding='6' cellspacing='0' style='border-collapse: collapse;'>" +
            "<tr bgcolor='#F1F5F9'><th align='left'>2. Pajak PPN 11%</th><th align='left'>3. Fasilitas Kredit Tempo</th><th align='left'>4. Hadiah Souvenir (Tunai)</th></tr>" +
            "<tr valign='top'>" +
            "<td><b>11%</b> flat dari Subtotal.</td>" +
            "<td>• &le; 2 mgg: Bunga <b>0%</b><br>• &gt; 2 mgg: Bunga <b>50%</b></td>" +
            "<td>• 100k - 500k: Mangkuk Keramik Cantik<br>• 500k - 750k: Payung Lipat Otomatis<br>• 750k - 1M: Tas Belanja Jumbo Grosir<br>• &gt; 1M: Voucher Belanja Rp 100.000</td>" +
            "</tr></table></body></html>"
        );
        p.add(new JScrollPane(ep), BorderLayout.CENTER);
        return p;
    }

    private void startClock() {
        Timer timer = new Timer(1000, e -> {
            lblStatusTime.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy  HH:mm:ss")));
        });
        timer.start();
    }

    public static void setupFlatLafTheme() {
        try {
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("ProgressBar.arc", 10);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
            UIManager.put("Table.alternateRowColor", new Color(248, 250, 252));
            UIManager.put("Table.selectionBackground", new Color(224, 238, 255));
            UIManager.put("Table.selectionForeground", new Color(15, 23, 42));
            UIManager.put("TabbedPane.tabHeight", 42);
            UIManager.put("TabbedPane.selectedBackground", Color.WHITE);
            UIManager.put("TabbedPane.showTabSeparators", true);
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ignored) {}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            setupFlatLafTheme();
            new MainFrame().setVisible(true);
        });
    }
}
