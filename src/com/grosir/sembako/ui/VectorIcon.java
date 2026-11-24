package com.grosir.sembako.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

/**
 * High-performance, anti-aliased vector icon renderer for Java Swing.
 * Renders crisp scalable glyphs without font dependency, eliminating missing character boxes.
 */
public class VectorIcon implements Icon {
    private final String type;
    private final int width;
    private final int height;
    private final Color color;

    public VectorIcon(String type, int size, Color color) {
        this(type, size, size, color);
    }

    public VectorIcon(String type, int width, int height, Color color) {
        this.type = type != null ? type.toLowerCase() : "";
        this.width = width;
        this.height = height;
        this.color = color;
    }

    @Override
    public int getIconWidth() { return width; }

    @Override
    public int getIconHeight() { return height; }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.translate(x, y);

        double sx = width / 100.0;
        double sy = height / 100.0;
        g2.scale(sx, sy);

        Color paintColor = color != null ? color : (c != null ? c.getForeground() : Color.DARK_GRAY);
        g2.setColor(paintColor);

        paintGlyph(g2);
        g2.dispose();
    }

    private void paintGlyph(Graphics2D g2) {
        switch (type) {
            case "cart":
                g2.fillOval(25, 82, 16, 16);
                g2.fillOval(72, 82, 16, 16);
                g2.setStroke(new BasicStroke(8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                Path2D cart = new Path2D.Float();
                cart.moveTo(10, 16); cart.lineTo(24, 16); cart.lineTo(36, 68);
                cart.lineTo(82, 68); cart.lineTo(92, 32); cart.lineTo(28, 32);
                g2.draw(cart);
                break;
            case "box":
                g2.setStroke(new BasicStroke(7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRoundRect(14, 25, 72, 60, 8, 8);
                g2.drawLine(14, 46, 86, 46);
                g2.drawLine(50, 46, 50, 72);
                g2.drawLine(35, 14, 65, 14);
                g2.drawLine(35, 14, 25, 25);
                g2.drawLine(65, 14, 75, 25);
                break;
            case "chart":
                g2.setStroke(new BasicStroke(7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(14, 85, 86, 85);
                g2.fillRect(20, 52, 15, 33);
                g2.fillRect(42, 32, 15, 53);
                g2.fillRect(64, 16, 15, 69);
                break;
            case "book":
                g2.setStroke(new BasicStroke(7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRoundRect(18, 14, 64, 72, 8, 8);
                g2.drawLine(30, 32, 70, 32);
                g2.drawLine(30, 48, 70, 48);
                g2.drawLine(30, 64, 55, 64);
                break;
            case "store":
                g2.setStroke(new BasicStroke(7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                Path2D roof = new Path2D.Float();
                roof.moveTo(12, 42); roof.lineTo(50, 14); roof.lineTo(88, 42); roof.closePath();
                g2.fill(roof);
                g2.fillRect(20, 45, 12, 40);
                g2.fillRect(44, 45, 12, 40);
                g2.fillRect(68, 45, 12, 40);
                g2.fillRect(14, 85, 72, 8);
                break;
            default:
                paintGlyphPart2(g2);
                break;
        }
    }

    private void paintGlyphPart2(Graphics2D g2) {
        switch (type) {
            case "plus":
                g2.setStroke(new BasicStroke(12f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(50, 20, 50, 80);
                g2.drawLine(20, 50, 80, 50);
                break;
            case "trash":
                g2.setStroke(new BasicStroke(7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(14, 28, 86, 28);
                g2.drawRoundRect(38, 15, 24, 13, 4, 4);
                Path2D can = new Path2D.Float();
                can.moveTo(24, 28); can.lineTo(30, 86); can.lineTo(70, 86); can.lineTo(76, 28);
                g2.draw(can);
                g2.drawLine(42, 40, 42, 74);
                g2.drawLine(58, 40, 58, 74);
                break;
            case "edit":
                g2.setStroke(new BasicStroke(7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                Path2D pen = new Path2D.Float();
                pen.moveTo(70, 16); pen.lineTo(84, 30); pen.lineTo(36, 78);
                pen.lineTo(16, 84); pen.lineTo(22, 64); pen.closePath();
                g2.draw(pen);
                g2.drawLine(58, 28, 72, 42);
                break;
            case "refresh":
                g2.setStroke(new BasicStroke(8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawArc(18, 18, 64, 64, 45, 270);
                Path2D arr = new Path2D.Float();
                arr.moveTo(74, 44); arr.lineTo(88, 28); arr.lineTo(62, 28); arr.closePath();
                g2.fill(arr);
                break;
            case "save":
                g2.setStroke(new BasicStroke(7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRoundRect(18, 18, 64, 64, 8, 8);
                g2.fillRect(32, 20, 36, 22);
                g2.fillRect(28, 54, 44, 26);
                break;
            case "print":
                g2.setStroke(new BasicStroke(7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRect(28, 14, 44, 24);
                g2.drawRoundRect(14, 38, 72, 38, 8, 8);
                g2.fillRect(28, 66, 44, 22);
                g2.drawLine(24, 52, 34, 52);
                break;
            case "credit_card":
                g2.setStroke(new BasicStroke(7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRoundRect(14, 24, 72, 52, 8, 8);
                g2.fillRect(14, 36, 72, 12);
                g2.fillRect(22, 58, 20, 8);
                break;
            case "gift":
                g2.setStroke(new BasicStroke(7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRoundRect(14, 28, 72, 18, 4, 4);
                g2.drawRect(22, 46, 56, 42);
                g2.fillRect(44, 28, 12, 60);
                g2.drawOval(28, 12, 20, 16);
                g2.drawOval(52, 12, 20, 16);
                break;
            case "receipt":
                g2.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRoundRect(20, 14, 60, 74, 4, 4);
                g2.drawLine(32, 30, 68, 30);
                g2.drawLine(32, 44, 68, 44);
                g2.drawLine(32, 58, 68, 58);
                g2.drawLine(32, 72, 52, 72);
                break;
            case "database":
                g2.setStroke(new BasicStroke(7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawOval(20, 14, 60, 20);
                g2.drawLine(20, 24, 20, 74);
                g2.drawLine(80, 24, 80, 74);
                g2.drawArc(20, 34, 60, 20, 180, 180);
                g2.drawArc(20, 54, 60, 20, 180, 180);
                g2.drawArc(20, 74, 60, 20, 180, 180);
                break;
            case "warning":
                g2.setStroke(new BasicStroke(8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                Path2D tri = new Path2D.Float();
                tri.moveTo(50, 14); tri.lineTo(88, 82); tri.lineTo(12, 82); tri.closePath();
                g2.draw(tri);
                g2.drawLine(50, 38, 50, 60);
                g2.fillOval(45, 69, 10, 10);
                break;
            case "check":
                g2.setStroke(new BasicStroke(12f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                Path2D chk = new Path2D.Float();
                chk.moveTo(18, 52); chk.lineTo(40, 76); chk.lineTo(84, 24);
                g2.draw(chk);
                break;
            case "close":
                g2.setStroke(new BasicStroke(12f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(22, 22, 78, 78);
                g2.drawLine(78, 22, 22, 78);
                break;
            case "money":
                g2.setStroke(new BasicStroke(7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRoundRect(14, 24, 72, 52, 6, 6);
                g2.drawOval(38, 38, 24, 24);
                g2.fillOval(20, 30, 8, 8);
                g2.fillOval(72, 62, 8, 8);
                break;
            case "tag":
                g2.setStroke(new BasicStroke(7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                Path2D tag = new Path2D.Float();
                tag.moveTo(20, 50); tag.lineTo(50, 20); tag.lineTo(85, 20);
                tag.lineTo(85, 55); tag.lineTo(55, 85); tag.closePath();
                g2.draw(tag);
                g2.fillOval(68, 32, 10, 10);
                break;
            case "clock":
                g2.setStroke(new BasicStroke(7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawOval(15, 15, 70, 70);
                g2.drawLine(50, 50, 50, 28);
                g2.drawLine(50, 50, 68, 50);
                break;
            case "dot":
            default:
                g2.fillOval(20, 20, 60, 60);
                break;
        }
    }
}