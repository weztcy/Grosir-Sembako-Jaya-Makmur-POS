package com.grosir.sembako.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class UIHelper {
    public static final Color PRIMARY_COLOR = new Color(30, 64, 175);       // Rich Blue #1E40AF
    public static final Color PRIMARY_DARK = new Color(15, 23, 42);         // Slate 900 #0F172A
    public static final Color ACCENT_GREEN = new Color(16, 185, 129);       // Emerald #10B981
    public static final Color ACCENT_AMBER = new Color(245, 158, 11);       // Amber #F59E0B
    public static final Color ACCENT_RED = new Color(239, 68, 68);          // Rose #EF4444
    public static final Color ACCENT_PURPLE = new Color(124, 58, 237);      // Violet #7C3AED
    public static final Color BG_CARD = Color.WHITE;
    public static final Color BG_SURFACE = new Color(248, 250, 252);        // Slate 50
    public static final Color BORDER_COLOR = new Color(226, 232, 240);      // Slate 200
    public static final Color TEXT_MAIN = new Color(15, 23, 42);
    public static final Color TEXT_MUTED = new Color(100, 116, 139);

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_BIG_AMOUNT = new Font("Segoe UI", Font.BOLD, 22);

    private static final DecimalFormat RUPIAH_FORMAT;
    static {
        DecimalFormatSymbols sym = new DecimalFormatSymbols(new Locale("id", "ID"));
        sym.setGroupingSeparator('.');
        sym.setDecimalSeparator(',');
        RUPIAH_FORMAT = new DecimalFormat("Rp #,##0", sym);
    }

    public static String formatRupiah(double amount) {
        return RUPIAH_FORMAT.format(amount);
    }

    public static Icon getVectorIcon(String type, int size, Color color) {
        return new VectorIcon(type, size, color);
    }

    public static JButton createButton(String text, Icon icon) {
        JButton btn = new JButton(text);
        if (icon != null) {
            btn.setIcon(icon);
            btn.setIconTextGap(8);
        }
        btn.setFont(FONT_REGULAR);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JButton createPrimaryButton(String text) {
        return createPrimaryButton(text, null);
    }

    public static JButton createPrimaryButton(String text, Icon icon) {
        JButton btn = new JButton(text);
        if (icon != null) {
            btn.setIcon(icon);
            btn.setIconTextGap(8);
        }
        btn.setFont(FONT_BOLD);
        btn.setBackground(PRIMARY_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JButton createSuccessButton(String text) {
        return createSuccessButton(text, null);
    }

    public static JButton createSuccessButton(String text, Icon icon) {
        JButton btn = new JButton(text);
        if (icon != null) {
            btn.setIcon(icon);
            btn.setIconTextGap(8);
        }
        btn.setFont(FONT_BOLD);
        btn.setBackground(ACCENT_GREEN);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JButton createDangerButton(String text) {
        return createDangerButton(text, null);
    }

    public static JButton createDangerButton(String text, Icon icon) {
        JButton btn = new JButton(text);
        if (icon != null) {
            btn.setIcon(icon);
            btn.setIconTextGap(8);
        }
        btn.setFont(FONT_BOLD);
        btn.setBackground(ACCENT_RED);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));
        return card;
    }

    public static JPanel createStatCard(String title, String value, String subtitle, Color accentColor) {
        return createStatCard(title, value, subtitle, null, accentColor);
    }

    public static JPanel createStatCard(String title, String value, String subtitle, String iconType, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(8, 4));
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                        BorderFactory.createEmptyBorder(10, 12, 10, 12)
                )
        ));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel lblTitle = new JLabel(title.toUpperCase());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblTitle.setForeground(TEXT_MUTED);
        top.add(lblTitle, BorderLayout.WEST);

        if (iconType != null && !iconType.isEmpty()) {
            JLabel lblIcon = new JLabel(new VectorIcon(iconType, 16, accentColor));
            top.add(lblIcon, BorderLayout.EAST);
        }

        JLabel lblVal = new JLabel(value);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblVal.setForeground(accentColor);

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(TEXT_MUTED);

        card.add(top, BorderLayout.NORTH);
        card.add(lblVal, BorderLayout.CENTER);
        card.add(lblSub, BorderLayout.SOUTH);
        return card;
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(32);
        table.setFont(FONT_REGULAR);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(241, 245, 249));

        table.getTableHeader().setFont(FONT_BOLD);
        table.getTableHeader().setBackground(new Color(241, 245, 249));
        table.getTableHeader().setForeground(new Color(51, 65, 85));
        table.getTableHeader().setPreferredSize(new Dimension(0, 36));

        table.setSelectionBackground(new Color(224, 238, 255));
        table.setSelectionForeground(TEXT_MAIN);
    }

    public static DefaultTableCellRenderer createCurrencyRenderer() {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.RIGHT);
        return r;
    }

    public static DefaultTableCellRenderer createCenterRenderer() {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.CENTER);
        return r;
    }

    public static DefaultTableCellRenderer createStatusBadgeRenderer() {
        return new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                c.setHorizontalAlignment(SwingConstants.CENTER);
                c.setIcon(null);
                String val = value == null ? "" : value.toString();
                if (!isSelected) {
                    if ("LUNAS".equalsIgnoreCase(val)) {
                        c.setForeground(new Color(5, 150, 105)); // Emerald
                        c.setIcon(new VectorIcon("check", 12, new Color(5, 150, 105)));
                        c.setText(" LUNAS");
                        c.setFont(FONT_BOLD);
                    } else if ("BELUM LUNAS".equalsIgnoreCase(val)) {
                        c.setForeground(new Color(217, 119, 6)); // Amber
                        c.setIcon(new VectorIcon("clock", 12, new Color(217, 119, 6)));
                        c.setText(" TEMPO");
                        c.setFont(FONT_BOLD);
                    }
                }
                return c;
            }
        };
    }

    public static DefaultTableCellRenderer createSouvenirBadgeRenderer() {
        return new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                c.setIcon(null);
                String val = value == null ? "" : value.toString();
                if (!isSelected) {
                    if (val.isEmpty() || val.equals("-") || val.equalsIgnoreCase("Tidak Ada")) {
                        c.setForeground(TEXT_MUTED);
                        c.setText("-");
                    } else {
                        c.setForeground(ACCENT_PURPLE);
                        c.setFont(FONT_BOLD);
                        c.setIcon(new VectorIcon("gift", 13, ACCENT_PURPLE));
                        c.setText(" " + val);
                    }
                }
                return c;
            }
        };
    }

    public static DefaultTableCellRenderer createStockRenderer() {
        return new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                c.setHorizontalAlignment(SwingConstants.CENTER);
                c.setIcon(null);
                try {
                    int stock = Integer.parseInt(value.toString());
                    if (!isSelected) {
                        if (stock <= 5) {
                            c.setForeground(ACCENT_RED);
                            c.setFont(FONT_BOLD);
                            c.setIcon(new VectorIcon("warning", 13, ACCENT_RED));
                            c.setText(" " + stock + " (Kritis)");
                        } else {
                            c.setForeground(TEXT_MAIN);
                            c.setText(String.valueOf(stock));
                        }
                    }
                } catch (Exception ignored) {}
                return c;
            }
        };
    }
}

