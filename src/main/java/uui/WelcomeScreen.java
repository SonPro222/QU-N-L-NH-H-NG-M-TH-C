/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uui;

/**
 *
 * @author ACER
 */
import javax.swing.*;
import java.awt.*;
import Itf.QuanLyNhaHangController;

import javax.swing.*;
import java.awt.*;

public class WelcomeScreen extends JWindow {

    public int progressValue = 0; // Giá trị phần trăm
    private String welcomeText = "Nhà Hàng Ẩm Thực Xin Chào"; // Dòng chữ chạy
    private int welcomeTextX = 500; // Vị trí bắt đầu của chữ

    public WelcomeScreen() {
        setSize(500, 500);
        setLocationRelativeTo(null); // Căn giữa màn hình
        setBackground(new Color(0, 0, 0, 0)); // Nền trong suốt
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ vòng tròn
        for (int i = 0; i < 5; i++) {
            g2d.setColor(new Color(0, 255, 0, 30)); // xanh lá nhạt với độ trong suốt
            g2d.setStroke(new BasicStroke(10 + i)); // tăng độ rộng để tạo ánh sáng lan tỏa
            g2d.drawArc(125 - i, 125 - i, 150 + i * 2, 150 + i * 2, 0, 360);
        }// Bóng nền đen nhạt
        g2d.setColor(new Color(0, 0, 0, 100)); // màu đen bán trong suốt
        g2d.setStroke(new BasicStroke(10));
        g2d.drawArc(130, 130, 150, 150, 0, 360); // hơi lệch vị trí để tạo hiệu ứng shadow

// Vòng chính
        g2d.setColor(new Color(0, 255, 0));
        g2d.setStroke(new BasicStroke(5));
        g2d.drawArc(125, 125, 150, 150, 0, 360);

//        g2d.setColor(new Color(0, 255, 0));
//        g2d.setStroke(new BasicStroke(5));
//        
//        g2d.drawArc(125, 125, 150, 150, 0, 360);
        // Vẽ phần trăm
        String percentText = progressValue + "%";
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        FontMetrics fm = g2d.getFontMetrics();
        int x = 125 + (150 - fm.stringWidth(percentText)) / 2;
        int y = 125 + (150 - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(percentText, x, y);

        // Chữ chạy
        g2d.setFont(new Font("Arial", Font.PLAIN, 40));
        g2d.drawString(welcomeText, welcomeTextX, 50);

        if (welcomeTextX > -300) {
            welcomeTextX -= 6;
        } else {
            welcomeTextX = getWidth(); // bắt đầu lại từ phải
        }
    }
}
