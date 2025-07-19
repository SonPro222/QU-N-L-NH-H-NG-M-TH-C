/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uui;


import uui.WelcomeScreen;
import ui.manager.QuanLyNhaHang;
import javax.swing.JFrame;

public class MainApp {
    public static void main(String[] args) {
        // Hiển thị màn hình welcome
      WelcomeScreen welcome = new WelcomeScreen();
welcome.setVisible(true);

new Thread(() -> {
    long start = System.currentTimeMillis();
    while (System.currentTimeMillis() - start < 6000) {
        welcome.progressValue = (int)((System.currentTimeMillis() - start) / 60);
        welcome.repaint();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    welcome.dispose(); // đóng splash
    new QuanLyNhaHang(new JFrame(), true).setVisible(true); // mở giao diện chính
}).start();

    }
}
