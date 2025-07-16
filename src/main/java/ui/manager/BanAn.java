/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.manager;

import dao.HoaDonChiTietDAO;
import dao.HoaDonDAO;
import dao.impl.HoaDonChiTietDAOImpl;
import dao.impl.HoaDonDAOImpl;
import entity.HoaDon;
import entity.HoaDonChiTiet;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 *
 * @author ACER
 */
public class BanAn extends javax.swing.JDialog {
 private HoaDonDAO hoaDonDAO = new HoaDonDAOImpl();
private Map<Integer, JButton> mapBanAn = new HashMap<>();
private BanAn banAn;
    /**
     * Creates new form BanAn
     */
    public BanAn(java.awt.Frame parent, boolean modal) {
    super(parent, modal);   
        initComponents();
        addButtonEvents();
        capNhatToanBoBanAn();
        setTitle("Bàn Ăn");
           setResizable(false);          // <-- không cho kéo giãn
        setLocationRelativeTo(null);
    }

  private void moFormDatMon(int soBan) {
    DatMon dialog = new DatMon((java.awt.Frame) this.getParent(), this, true, soBan);
    dialog.setVisible(true);
}



  private void addButtonEvents() {
    JButton[] buttons = {
        btnBan01, btnBan2, btnBan3, btnBan4, btnBan5,
        btnBan6, btnBan7, btnBan8, btnBan9, btnBan10,
        btnBan11, btnBan12, btnBan13, btnBan14, btnBan15,
        btnBan16, btnBan17, btnBan18, btnBan19, btnBan20,
        btnBan21, btnBan22, btnBan23, btnBan24, btnBan25,
        btnBan26, btnBan27, btnBan28, btnBan29, btnBan30,
        btnBan31, btnBan32, btnBan33, btnBan34, btnBan35,
        btnBan36, btnBan37, btnBan38, btnBan39, btnBan40
    };

    for (int i = 0; i < buttons.length; i++) {
        final int soBan = i + 1;
        buttons[i].addActionListener(e -> moFormDatMon(soBan));
    }
}

    
 public void capNhatToanBoBanAn() {
    JButton[] buttons = {
        btnBan01, btnBan2, btnBan3, btnBan4, btnBan5,
        btnBan6, btnBan7, btnBan8, btnBan9, btnBan10,
        btnBan11, btnBan12, btnBan13, btnBan14, btnBan15,
        btnBan16, btnBan17, btnBan18, btnBan19, btnBan20,
        btnBan21, btnBan22, btnBan23, btnBan24, btnBan25,
        btnBan26, btnBan27, btnBan28, btnBan29, btnBan30,
        btnBan31, btnBan32, btnBan33, btnBan34, btnBan35,
        btnBan36, btnBan37, btnBan38, btnBan39, btnBan40
    };

    HoaDonDAO hoaDonDAO = new HoaDonDAOImpl();
    HoaDonChiTietDAO chiTietDAO = new HoaDonChiTietDAOImpl();

    for (int i = 0; i < buttons.length; i++) {
        int soBan = i + 1;
        HoaDon hd = hoaDonDAO.findChuaThanhToanTheoBan(soBan);
        if (hd != null) {
            List<HoaDonChiTiet> dsMon = chiTietDAO.findByHoaDonId(hd.getMaHD());
            if (dsMon != null && !dsMon.isEmpty()) {
                buttons[i].setBackground(Color.ORANGE); // Đang phục vụ
            } else {
                buttons[i].setBackground(Color.GRAY); // Có hóa đơn rỗng → vẫn xem như TRỐNG
            }
        } else {
            buttons[i].setBackground(Color.GRAY); // Không có hóa đơn → TRỐNG
        }
    }
}



public void datLaiTrangThaiBan(int soBan) {
    JButton[] buttons = {
        btnBan01, btnBan2, btnBan3, btnBan4, btnBan5,
        btnBan6, btnBan7, btnBan8, btnBan9, btnBan10,
        btnBan11, btnBan12, btnBan13, btnBan14, btnBan15,
        btnBan16, btnBan17, btnBan18, btnBan19, btnBan20,
        btnBan21, btnBan22, btnBan23, btnBan24, btnBan25,
        btnBan26, btnBan27, btnBan28, btnBan29, btnBan30,
        btnBan31, btnBan32, btnBan33, btnBan34, btnBan35,
        btnBan36, btnBan37, btnBan38, btnBan39, btnBan40
    };

    if (soBan >= 1 && soBan <= buttons.length) {
        buttons[soBan - 1].setBackground(Color.GREEN);
    }
}



 public void capNhatTrangThaiBan(int soBan) {
    JButton[] buttons = {
        btnBan01, btnBan2, btnBan3, btnBan4, btnBan5,
        btnBan6, btnBan7, btnBan8, btnBan9, btnBan10,
        btnBan11, btnBan12, btnBan13, btnBan14, btnBan15,
        btnBan16, btnBan17, btnBan18, btnBan19, btnBan20,
        btnBan21, btnBan22, btnBan23, btnBan24, btnBan25,
        btnBan26, btnBan27, btnBan28, btnBan29, btnBan30,
        btnBan31, btnBan32, btnBan33, btnBan34, btnBan35,
        btnBan36, btnBan37, btnBan38, btnBan39, btnBan40
    };

    if (soBan < 1 || soBan > buttons.length) return;

    // === LẤY HÓA ĐƠN CHƯA THANH TOÁN ===
    HoaDon hd = new HoaDonDAOImpl().findChuaThanhToanTheoBan(soBan);

    if (hd != null) {
        // === LẤY CHI TIẾT MÓN CỦA HÓA ĐƠN ===
        List<HoaDonChiTiet> chiTiet = new HoaDonChiTietDAOImpl().findByHoaDonId(hd.getMaHD());

        if (chiTiet != null && !chiTiet.isEmpty()) {
            // ✅ Có món: đổi màu ORANGE (đang phục vụ)
            buttons[soBan - 1].setBackground(Color.ORANGE);
            return;
        }
    }

    // ❌ Không có hóa đơn hoặc không có món: để lại màu gốc (VD: trắng/xanh nhạt)
    buttons[soBan - 1].setBackground(Color.LIGHT_GRAY);
}




    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnBan01 = new javax.swing.JButton();
        btnBan2 = new javax.swing.JButton();
        btnBan3 = new javax.swing.JButton();
        btnBan11 = new javax.swing.JButton();
        btnBan4 = new javax.swing.JButton();
        btnBan5 = new javax.swing.JButton();
        btnBan12 = new javax.swing.JButton();
        btnBan13 = new javax.swing.JButton();
        btnBan6 = new javax.swing.JButton();
        btnBan14 = new javax.swing.JButton();
        btnBan7 = new javax.swing.JButton();
        btnBan15 = new javax.swing.JButton();
        btnBan8 = new javax.swing.JButton();
        btnBan16 = new javax.swing.JButton();
        btnBan9 = new javax.swing.JButton();
        btnBan10 = new javax.swing.JButton();
        btnBan17 = new javax.swing.JButton();
        btnBan18 = new javax.swing.JButton();
        btnBan19 = new javax.swing.JButton();
        btnBan25 = new javax.swing.JButton();
        btnBan26 = new javax.swing.JButton();
        btnBan33 = new javax.swing.JButton();
        btnBan34 = new javax.swing.JButton();
        btnBan35 = new javax.swing.JButton();
        btnBan27 = new javax.swing.JButton();
        btnBan20 = new javax.swing.JButton();
        btnBan28 = new javax.swing.JButton();
        btnBan36 = new javax.swing.JButton();
        btnBan37 = new javax.swing.JButton();
        btnBan29 = new javax.swing.JButton();
        btnBan21 = new javax.swing.JButton();
        btnBan22 = new javax.swing.JButton();
        btnBan30 = new javax.swing.JButton();
        btnBan38 = new javax.swing.JButton();
        btnBan39 = new javax.swing.JButton();
        btnBan31 = new javax.swing.JButton();
        btnBan23 = new javax.swing.JButton();
        btnBan24 = new javax.swing.JButton();
        btnBan32 = new javax.swing.JButton();
        btnBan40 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(499, 5, -1, -1));

        btnBan01.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan01.setText("BÀN 01");
        btnBan01.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan01, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 5, -1, -1));

        btnBan2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan2.setText("BÀN 02");
        btnBan2.setPreferredSize(new java.awt.Dimension(100, 100));
        btnBan2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBan2ActionPerformed(evt);
            }
        });
        getContentPane().add(btnBan2, new org.netbeans.lib.awtextra.AbsoluteConstraints(147, 5, -1, -1));

        btnBan3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan3.setText("BÀN 03");
        btnBan3.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan3, new org.netbeans.lib.awtextra.AbsoluteConstraints(252, 5, -1, -1));

        btnBan11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan11.setText("BÀN 11");
        btnBan11.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan11, new org.netbeans.lib.awtextra.AbsoluteConstraints(252, 110, -1, -1));

        btnBan4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan4.setText("BÀN 04");
        btnBan4.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan4, new org.netbeans.lib.awtextra.AbsoluteConstraints(357, 5, -1, -1));

        btnBan5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan5.setText("BÀN 05");
        btnBan5.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan5, new org.netbeans.lib.awtextra.AbsoluteConstraints(462, 5, -1, -1));

        btnBan12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan12.setText("BÀN 12");
        btnBan12.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan12, new org.netbeans.lib.awtextra.AbsoluteConstraints(357, 110, -1, -1));

        btnBan13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan13.setText("BÀN 13");
        btnBan13.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan13, new org.netbeans.lib.awtextra.AbsoluteConstraints(462, 110, -1, -1));

        btnBan6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan6.setText("BÀN 06");
        btnBan6.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan6, new org.netbeans.lib.awtextra.AbsoluteConstraints(567, 5, -1, -1));

        btnBan14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan14.setText("BÀN 14");
        btnBan14.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan14, new org.netbeans.lib.awtextra.AbsoluteConstraints(567, 110, -1, -1));

        btnBan7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan7.setText("BÀN 07");
        btnBan7.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan7, new org.netbeans.lib.awtextra.AbsoluteConstraints(672, 5, -1, -1));

        btnBan15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan15.setText("BÀN 15");
        btnBan15.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan15, new org.netbeans.lib.awtextra.AbsoluteConstraints(672, 110, -1, -1));

        btnBan8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan8.setText("BÀN 08");
        btnBan8.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan8, new org.netbeans.lib.awtextra.AbsoluteConstraints(777, 5, -1, -1));

        btnBan16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan16.setText("BÀN 16");
        btnBan16.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan16, new org.netbeans.lib.awtextra.AbsoluteConstraints(777, 110, -1, -1));

        btnBan9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan9.setText("BÀN 09");
        btnBan9.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan9, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 110, -1, -1));

        btnBan10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan10.setText("BÀN 10");
        btnBan10.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan10, new org.netbeans.lib.awtextra.AbsoluteConstraints(147, 110, -1, -1));

        btnBan17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan17.setText("BÀN 17");
        btnBan17.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan17, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 215, -1, -1));

        btnBan18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan18.setText("BÀN 18");
        btnBan18.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan18, new org.netbeans.lib.awtextra.AbsoluteConstraints(147, 215, -1, -1));

        btnBan19.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan19.setText("BÀN 19");
        btnBan19.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan19, new org.netbeans.lib.awtextra.AbsoluteConstraints(252, 215, -1, -1));

        btnBan25.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan25.setText("BÀN 25");
        btnBan25.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan25, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 320, -1, -1));

        btnBan26.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan26.setText("BÀN 26");
        btnBan26.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan26, new org.netbeans.lib.awtextra.AbsoluteConstraints(147, 320, -1, -1));

        btnBan33.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan33.setText("BÀN 33");
        btnBan33.setPreferredSize(new java.awt.Dimension(100, 100));
        btnBan33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBan33ActionPerformed(evt);
            }
        });
        getContentPane().add(btnBan33, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 425, -1, -1));

        btnBan34.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan34.setText("BÀN 34");
        btnBan34.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan34, new org.netbeans.lib.awtextra.AbsoluteConstraints(147, 425, -1, -1));

        btnBan35.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan35.setText("BÀN 35");
        btnBan35.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan35, new org.netbeans.lib.awtextra.AbsoluteConstraints(252, 425, -1, -1));

        btnBan27.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan27.setText("BÀN 27");
        btnBan27.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan27, new org.netbeans.lib.awtextra.AbsoluteConstraints(252, 320, -1, -1));

        btnBan20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan20.setText("BÀN 20");
        btnBan20.setPreferredSize(new java.awt.Dimension(100, 100));
        btnBan20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBan20ActionPerformed(evt);
            }
        });
        getContentPane().add(btnBan20, new org.netbeans.lib.awtextra.AbsoluteConstraints(357, 215, -1, -1));

        btnBan28.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan28.setText("BÀN 28");
        btnBan28.setPreferredSize(new java.awt.Dimension(100, 100));
        btnBan28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBan28ActionPerformed(evt);
            }
        });
        getContentPane().add(btnBan28, new org.netbeans.lib.awtextra.AbsoluteConstraints(357, 320, -1, -1));

        btnBan36.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan36.setText("BÀN 36");
        btnBan36.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan36, new org.netbeans.lib.awtextra.AbsoluteConstraints(357, 425, -1, -1));

        btnBan37.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan37.setText("BÀN 37");
        btnBan37.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan37, new org.netbeans.lib.awtextra.AbsoluteConstraints(462, 425, -1, -1));

        btnBan29.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan29.setText("BÀN 29");
        btnBan29.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan29, new org.netbeans.lib.awtextra.AbsoluteConstraints(462, 320, -1, -1));

        btnBan21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan21.setText("BÀN 21");
        btnBan21.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan21, new org.netbeans.lib.awtextra.AbsoluteConstraints(462, 215, -1, -1));

        btnBan22.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan22.setText("BÀN 22");
        btnBan22.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan22, new org.netbeans.lib.awtextra.AbsoluteConstraints(567, 215, -1, -1));

        btnBan30.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan30.setText("BÀN 30");
        btnBan30.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan30, new org.netbeans.lib.awtextra.AbsoluteConstraints(567, 320, -1, -1));

        btnBan38.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan38.setText("BÀN 38");
        btnBan38.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan38, new org.netbeans.lib.awtextra.AbsoluteConstraints(567, 425, -1, -1));

        btnBan39.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan39.setText("BÀN 39");
        btnBan39.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan39, new org.netbeans.lib.awtextra.AbsoluteConstraints(672, 425, -1, -1));

        btnBan31.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan31.setText("BÀN 31");
        btnBan31.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan31, new org.netbeans.lib.awtextra.AbsoluteConstraints(672, 320, -1, -1));

        btnBan23.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan23.setText("BÀN 23");
        btnBan23.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan23, new org.netbeans.lib.awtextra.AbsoluteConstraints(672, 215, -1, -1));

        btnBan24.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan24.setText("BÀN 24");
        btnBan24.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan24, new org.netbeans.lib.awtextra.AbsoluteConstraints(777, 215, -1, -1));

        btnBan32.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan32.setText("BÀN 32");
        btnBan32.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan32, new org.netbeans.lib.awtextra.AbsoluteConstraints(777, 320, -1, -1));

        btnBan40.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBan40.setText("BÀN 40");
        btnBan40.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(btnBan40, new org.netbeans.lib.awtextra.AbsoluteConstraints(777, 425, -1, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/hinhnen.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 930, 540));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBan2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBan2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBan2ActionPerformed

    private void btnBan20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBan20ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBan20ActionPerformed

    private void btnBan28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBan28ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBan28ActionPerformed

    private void btnBan33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBan33ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBan33ActionPerformed

    /**
     * @param args the command line arguments
     */




    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBan01;
    private javax.swing.JButton btnBan10;
    private javax.swing.JButton btnBan11;
    private javax.swing.JButton btnBan12;
    private javax.swing.JButton btnBan13;
    private javax.swing.JButton btnBan14;
    private javax.swing.JButton btnBan15;
    private javax.swing.JButton btnBan16;
    private javax.swing.JButton btnBan17;
    private javax.swing.JButton btnBan18;
    private javax.swing.JButton btnBan19;
    private javax.swing.JButton btnBan2;
    private javax.swing.JButton btnBan20;
    private javax.swing.JButton btnBan21;
    private javax.swing.JButton btnBan22;
    private javax.swing.JButton btnBan23;
    private javax.swing.JButton btnBan24;
    private javax.swing.JButton btnBan25;
    private javax.swing.JButton btnBan26;
    private javax.swing.JButton btnBan27;
    private javax.swing.JButton btnBan28;
    private javax.swing.JButton btnBan29;
    private javax.swing.JButton btnBan3;
    private javax.swing.JButton btnBan30;
    private javax.swing.JButton btnBan31;
    private javax.swing.JButton btnBan32;
    private javax.swing.JButton btnBan33;
    private javax.swing.JButton btnBan34;
    private javax.swing.JButton btnBan35;
    private javax.swing.JButton btnBan36;
    private javax.swing.JButton btnBan37;
    private javax.swing.JButton btnBan38;
    private javax.swing.JButton btnBan39;
    private javax.swing.JButton btnBan4;
    private javax.swing.JButton btnBan40;
    private javax.swing.JButton btnBan5;
    private javax.swing.JButton btnBan6;
    private javax.swing.JButton btnBan7;
    private javax.swing.JButton btnBan8;
    private javax.swing.JButton btnBan9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel89;
    private javax.swing.JPanel jPanel90;
    private javax.swing.JPanel jPanel91;
    private javax.swing.JPanel jPanel92;
    private javax.swing.JPanel jPanel93;
    private javax.swing.JPanel jPanel94;
    // End of variables declaration//GEN-END:variables
}
