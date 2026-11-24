package com.grosir.sembako.util;

import com.grosir.sembako.model.*;
import com.grosir.sembako.service.DataRepository;
import com.grosir.sembako.ui.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class CaptureScreenshots {

    public static void main(String[] args) {
        try {
            MainFrame.setupFlatLafTheme();

            File dir = new File("screenshots");
            if (!dir.exists()) dir.mkdirs();

            MainFrame frame = new MainFrame();
            frame.setSize(1280, 840);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.toFront();

            // Reflection to access internal tabs and transaction panel
            Field tabField = MainFrame.class.getDeclaredField("tabbedPane");
            tabField.setAccessible(true);
            JTabbedPane tabs = (JTabbedPane) tabField.get(frame);

            Field trxField = MainFrame.class.getDeclaredField("transactionPanel");
            trxField.setAccessible(true);
            TransactionPanel trxPanel = (TransactionPanel) trxField.get(frame);

            // Populate Kasir POS with live sample cart
            DataRepository repo = DataRepository.getInstance();
            Product p1 = repo.getProductByCode("BRG-002"); // Beras Pandan Wangi 10kg
            Product p2 = repo.getProductByCode("BRG-005"); // Minyak Sania Karton
            Product p3 = repo.getProductByCode("BRG-013"); // Indomie Goreng Dus

            Field cartField = TransactionPanel.class.getDeclaredField("cart");
            cartField.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<TransactionItem> cart = (List<TransactionItem>) cartField.get(trxPanel);
            cart.clear();
            if (p1 != null) cart.add(new TransactionItem(p1.getCode(), p1.getName(), p1.getUnit(), p1.getPurchasePrice(), p1.getSellingPrice(), 2));
            if (p2 != null) cart.add(new TransactionItem(p2.getCode(), p2.getName(), p2.getUnit(), p2.getPurchasePrice(), p2.getSellingPrice(), 3));
            if (p3 != null) cart.add(new TransactionItem(p3.getCode(), p3.getName(), p3.getUnit(), p3.getPurchasePrice(), p3.getSellingPrice(), 4));

            Field txtCashPaid = TransactionPanel.class.getDeclaredField("txtCashPaid");
            txtCashPaid.setAccessible(true);
            JTextField txtCash = (JTextField) txtCashPaid.get(trxPanel);
            txtCash.setText("1800000");

            Method updateTableMethod = TransactionPanel.class.getDeclaredMethod("updateCartTable");
            updateTableMethod.setAccessible(true);
            updateTableMethod.invoke(trxPanel);

            Method recalcMethod = TransactionPanel.class.getDeclaredMethod("recalculateBill");
            recalcMethod.setAccessible(true);
            recalcMethod.invoke(trxPanel);

            Field cartTableField = TransactionPanel.class.getDeclaredField("cartTable");
            cartTableField.setAccessible(true);
            JTable tbl = (JTable) cartTableField.get(trxPanel);

            // 1. Capture Kasir POS
            tabs.setSelectedIndex(0);
            frame.validate();
            Thread.sleep(300);
            captureComponent(frame, new File(dir, "1_kasir_pos.png"));
            System.out.println("Saved screenshots/1_kasir_pos.png");

            // 2. Capture Master Produk & Stok
            tabs.setSelectedIndex(1);
            frame.getContentPane().doLayout();
            captureComponent(frame, new File(dir, "2_master_produk.png"));
            System.out.println("Saved screenshots/2_master_produk.png");

            // 3. Capture Laporan Transaksi
            tabs.setSelectedIndex(2);
            frame.getContentPane().doLayout();
            captureComponent(frame, new File(dir, "3_laporan_transaksi.png"));
            System.out.println("Saved screenshots/3_laporan_transaksi.png");

            // 4. Capture Aturan Bisnis
            tabs.setSelectedIndex(3);
            frame.getContentPane().doLayout();
            captureComponent(frame, new File(dir, "4_aturan_bisnis.png"));
            System.out.println("Saved screenshots/4_aturan_bisnis.png");

            // 5. Capture Struk Thermal Dialog
            List<Transaction> trxList = repo.getAllTransactions();
            Transaction sampleTrx = trxList.get(Math.min(4, trxList.size() - 1));
            ReceiptDialog dlg = new ReceiptDialog(frame, sampleTrx);
            dlg.setModal(false);
            dlg.setSize(540, 720);
            dlg.setLocationRelativeTo(null);
            dlg.setVisible(true);
            dlg.toFront();
            Thread.sleep(400);
            captureComponent(dlg, new File(dir, "5_struk_thermal.png"));
            System.out.println("Saved screenshots/5_struk_thermal.png");
            dlg.dispose();

            frame.dispose();

            System.out.println("ALL SCREENSHOTS CAPTURED SUCCESSFULLY!");
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private static void captureComponent(Component comp, File targetFile) throws Exception {
        int w = comp.getWidth();
        int h = comp.getHeight();
        if (w <= 0) w = 1280;
        if (h <= 0) h = 840;

        comp.setSize(w, h);
        comp.doLayout();
        comp.validate();

        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, w, h);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        if (comp instanceof RootPaneContainer) {
            ((RootPaneContainer) comp).getRootPane().printAll(g2);
        } else {
            comp.printAll(g2);
        }
        g2.dispose();

        ImageIO.write(image, "png", targetFile);
    }
}

