package uui;
import java.awt.*;
import javax.swing.*;

public class KieuChuDep extends JComponent {
    private String text;
    private Font font;
    private Color color;
    private boolean doBong;
    private boolean inDam;
    private boolean nghieng;

    public KieuChuDep(String text) {
        this.text = text;
        this.font = new Font("Segoe UI", Font.PLAIN, 36);
        this.color = Color.BLACK;
        this.doBong = true;
        this.inDam = false;
        this.nghieng = false;
    }

    public void setFontStyle(boolean inDam, boolean nghieng) {
        int style = Font.PLAIN;
        if (inDam && nghieng) style = Font.BOLD | Font.ITALIC;
        else if (inDam) style = Font.BOLD;
        else if (nghieng) style = Font.ITALIC;
        this.font = font.deriveFont(style);
        this.inDam = inDam;
        this.nghieng = nghieng;
        repaint();
    }

    public void setColor(Color color) {
        this.color = color;
        repaint();
    }

    public void setDoBong(boolean doBong) {
        this.doBong = doBong;
        repaint();
    }

    public void setText(String text) {
        this.text = text;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setFont(font);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        FontMetrics fm = g2.getFontMetrics();
        int x = 10;
        int y = fm.getAscent() + 10;

        if (doBong) {
            g2.setColor(Color.GRAY); // màu bóng
            g2.drawString(text, x + 3, y + 3);
        }

        g2.setColor(color);
        g2.drawString(text, x, y);

        g2.dispose();
    }
}

