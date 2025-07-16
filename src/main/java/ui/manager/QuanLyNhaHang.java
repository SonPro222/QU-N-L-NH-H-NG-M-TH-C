/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.manager;

import Itf.QuanLyNhaHangController;
import java.sql.Time;
import java.text.SimpleDateFormat;
import ui.manager.QuanLyTaiKhoan;
import util.XAuth;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Timer;
import uui.Login;

public class QuanLyNhaHang extends javax.swing.JDialog implements QuanLyNhaHangController {

    public QuanLyNhaHang(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setTitle("Quản Lý Nhà Hàng Ẩm Thực Việt Nam");
        setSize(1000, 600);            // <-- kích thước mong muốn
        setResizable(false);          // <-- không cho kéo giãn
        setLocationRelativeTo(null);
        init();
    }

    public void init() {
        Timer timer = new Timer(1000, e -> {
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");
            lblThoiGian.setText("Thời gian: " + sdf.format(now));
        });
        timer.start();
        this.showLogin(null);
        if (XAuth.user == null) {
            System.exit(0); // Không có user => thoát
            return;
        }
        if (!"quan ly".equalsIgnoreCase(XAuth.user.getVaitro())) {
            btnQuanLyNhanVien.setVisible(false);
            btnDoanhThu.setVisible(false);
            btnQuanLyChiTieu.setVisible(false);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnQuanLyMonAn = new javax.swing.JButton();
        btnKetThuc = new javax.swing.JButton();
        btnNhanVien = new javax.swing.JButton();
        btnQuanLyNhanVien = new javax.swing.JButton();
        btnDoanhThu = new javax.swing.JButton();
        btnQuanLyChiTieu = new javax.swing.JButton();
        btnBanAn = new javax.swing.JButton();
        lblThoiGian = new javax.swing.JLabel();
        ptnLichSu1 = new javax.swing.JButton();
        btnQuanLyNhanVien1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1051, -1));

        btnQuanLyMonAn.setBackground(new java.awt.Color(102, 102, 102));
        btnQuanLyMonAn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnQuanLyMonAn.setForeground(new java.awt.Color(0, 51, 51));
        btnQuanLyMonAn.setText("Quản Lý Món Ăn");
        btnQuanLyMonAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuanLyMonAnActionPerformed(evt);
            }
        });
        getContentPane().add(btnQuanLyMonAn, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, 250, 40));

        btnKetThuc.setBackground(new java.awt.Color(102, 102, 102));
        btnKetThuc.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnKetThuc.setForeground(new java.awt.Color(0, 51, 51));
        btnKetThuc.setText("Kết Thúc");
        btnKetThuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKetThucActionPerformed(evt);
            }
        });
        getContentPane().add(btnKetThuc, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 490, 250, 40));

        btnNhanVien.setBackground(new java.awt.Color(102, 102, 102));
        btnNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnNhanVien.setForeground(new java.awt.Color(0, 51, 51));
        btnNhanVien.setText("Quản Lý Nhân Viên");
        btnNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNhanVienActionPerformed(evt);
            }
        });
        getContentPane().add(btnNhanVien, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, 250, 40));

        btnQuanLyNhanVien.setBackground(new java.awt.Color(102, 102, 102));
        btnQuanLyNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnQuanLyNhanVien.setForeground(new java.awt.Color(0, 51, 51));
        btnQuanLyNhanVien.setText("Quản lý Tài Khoản");
        btnQuanLyNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuanLyNhanVienActionPerformed(evt);
            }
        });
        getContentPane().add(btnQuanLyNhanVien, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, 250, 40));

        btnDoanhThu.setBackground(new java.awt.Color(102, 102, 102));
        btnDoanhThu.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnDoanhThu.setForeground(new java.awt.Color(0, 51, 51));
        btnDoanhThu.setText("Doanh Thu");
        btnDoanhThu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoanhThuActionPerformed(evt);
            }
        });
        getContentPane().add(btnDoanhThu, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 250, 40));

        btnQuanLyChiTieu.setBackground(new java.awt.Color(102, 102, 102));
        btnQuanLyChiTieu.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnQuanLyChiTieu.setForeground(new java.awt.Color(0, 51, 51));
        btnQuanLyChiTieu.setText("Quản Lý Chi Tiêu");
        btnQuanLyChiTieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuanLyChiTieuActionPerformed(evt);
            }
        });
        getContentPane().add(btnQuanLyChiTieu, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, 250, 40));

        btnBanAn.setBackground(new java.awt.Color(102, 102, 102));
        btnBanAn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnBanAn.setForeground(new java.awt.Color(0, 51, 51));
        btnBanAn.setText("Quản Lý Bàn ");
        btnBanAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBanAnActionPerformed(evt);
            }
        });
        getContentPane().add(btnBanAn, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 250, 40));

        lblThoiGian.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblThoiGian.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(lblThoiGian, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 480, 30));

        ptnLichSu1.setBackground(new java.awt.Color(102, 102, 102));
        ptnLichSu1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        ptnLichSu1.setForeground(new java.awt.Color(0, 51, 51));
        ptnLichSu1.setText("Lịch Sử");
        ptnLichSu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ptnLichSu1ActionPerformed(evt);
            }
        });
        getContentPane().add(ptnLichSu1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 250, 40));

        btnQuanLyNhanVien1.setBackground(new java.awt.Color(102, 102, 102));
        btnQuanLyNhanVien1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnQuanLyNhanVien1.setForeground(new java.awt.Color(0, 51, 51));
        btnQuanLyNhanVien1.setText("Chấm Công Nhân Viên");
        btnQuanLyNhanVien1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuanLyNhanVien1ActionPerformed(evt);
            }
        });
        getContentPane().add(btnQuanLyNhanVien1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, 250, 40));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/hinhnen.png"))); // NOI18N
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 560));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnQuanLyMonAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuanLyMonAnActionPerformed
        this.showQuanLyMonAn(null);        // TODO add your handling code here:
    }//GEN-LAST:event_btnQuanLyMonAnActionPerformed

    private void btnKetThucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKetThucActionPerformed
        this.exit();  // TODO add your handling code here:
    }//GEN-LAST:event_btnKetThucActionPerformed

    private void btnNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNhanVienActionPerformed
        this.showQuanLyNhanVien(null); // TODO add your handling code here:
    }//GEN-LAST:event_btnNhanVienActionPerformed

    private void btnQuanLyNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuanLyNhanVienActionPerformed
        this.showQuanLyTaiKhoan(null);        // TODO add your handling code here:
    }//GEN-LAST:event_btnQuanLyNhanVienActionPerformed

    private void btnDoanhThuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoanhThuActionPerformed
        this.showThongKeDoanhThu(null);        // TODO add your handling code here:
    }//GEN-LAST:event_btnDoanhThuActionPerformed

    private void btnQuanLyChiTieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuanLyChiTieuActionPerformed
        this.showQuanLyChiTieu(null);  // TODO add your handling code here:
    }//GEN-LAST:event_btnQuanLyChiTieuActionPerformed

    private void btnBanAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBanAnActionPerformed
        this.showBanAn(null);        // TODO add your handling code here:
    }//GEN-LAST:event_btnBanAnActionPerformed

    private void ptnLichSu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ptnLichSu1ActionPerformed
        this.showLichSuGiaoDich(null);        // TODO add your handling code here:

    }//GEN-LAST:event_ptnLichSu1ActionPerformed

    private void btnQuanLyNhanVien1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuanLyNhanVien1ActionPerformed
        this.showQuanLyChamCong(null);  // TODO add your handling code here:
    }//GEN-LAST:event_btnQuanLyNhanVien1ActionPerformed
  
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                QuanLyNhaHang dialog = new QuanLyNhaHang(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBanAn;
    private javax.swing.JButton btnDoanhThu;
    private javax.swing.JButton btnKetThuc;
    private javax.swing.JButton btnNhanVien;
    private javax.swing.JButton btnQuanLyChiTieu;
    private javax.swing.JButton btnQuanLyMonAn;
    private javax.swing.JButton btnQuanLyNhanVien;
    private javax.swing.JButton btnQuanLyNhanVien1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblThoiGian;
    private javax.swing.JButton ptnLichSu1;
    // End of variables declaration//GEN-END:variables
}
