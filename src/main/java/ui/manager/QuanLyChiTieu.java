/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.manager;

import dao.ChiTieuDao;
import entity.ChiTieu;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import dao.impl.ChiTieuDaoImpl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import javax.swing.JOptionPane;
/**
 *
 * @author ACER
 */
public class QuanLyChiTieu extends javax.swing.JDialog{

    /**
     * Creates new form QuanLyChiTieu
     */
    public QuanLyChiTieu(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        fillTableChiTieu();
    }
//    public void addChiTieu(){
//   try {
//        // Lấy và xử lý dữ liệu đầu vào
//        String soTienStr = txtSoTien.getText().trim();
//        String moTa = txtGhiChu.getText().trim();
//
//        // 🔴 Kiểm tra trống
//        if (soTienStr.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "⚠ Vui lòng nhập số tiền!");
//            txtSoTien.requestFocus();
//            return;
//        }
//
//        // 🔴 Kiểm tra có phải số hợp lệ không
//        float soTien;
//        try {
//            soTien = Float.parseFloat(soTienStr);
//            if (soTien <= 0) {
//                JOptionPane.showMessageDialog(this, "⚠ Số tiền phải lớn hơn 0!");
//                txtSoTien.requestFocus();
//                return;
//            }
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(this, "⚠ Số tiền phải là số!");
//            txtSoTien.requestFocus();
//            return;
//        }
//
//      
//        int choice = JOptionPane.showConfirmDialog(this,
//            "Bạn có chắc chắn muốn thêm chi tiêu với số tiền: " + soTien + " không?",
//            "Xác nhận thêm", JOptionPane.YES_NO_OPTION);
//
//        if (choice != JOptionPane.YES_OPTION) {
//            return; // Người dùng không đồng ý
//        }
//
//        // ✅ Tạo đối tượng chi tiêu
//        ChiTieu ct = new ChiTieu();
//        ct.setNgay(java.time.LocalDate.now().toString()); // yyyy-MM-dd
//        ct.setSoTien(soTien);
//        ct.setMoTa(moTa);
//
//        // ✅ Gọi DAO để thêm
//        ChiTieuDao dao = new ChiTieuDaoImpl();
//        dao.create(ct);
//
//        JOptionPane.showMessageDialog(this, "  Thêm chi tiêu thành công!");
////        clearFormChiTieu();
//        fillTableChiTieu();
//
//    } catch (Exception e) {
//        JOptionPane.showMessageDialog(this, " Lỗi: " + e.getMessage());
//        e.printStackTrace();
//    }}
//    public void fillTableChiTieu() {
//    DefaultTableModel model = (DefaultTableModel) tblChiTieu.getModel();
//    model.setRowCount(0); // Xóa bảng trước khi đổ mới
//   
//    ChiTieuDao dao = new ChiTieuDaoImpl(); // ✅ CHUẨN
//
//    List<ChiTieu> list = dao.findAll(); // Gọi hàm lấy toàn bộ chi tiêu
//
//    for (ChiTieu ct : list) {
//        Object[] row = {
//            ct.getMaChiTieu(),
//            ct.getNgay(),
//            ct.getSoTien(),
//            ct.getMoTa()
//        };
//        model.addRow(row);
//    }
//}
   
public void addChiTieu() {
    try {
        // Lấy và xử lý dữ liệu đầu vào
        String soTienStr = txtSoTien.getText().trim();
        String moTa = txtGhiChu.getText().trim();

        // 🔴 Kiểm tra trống
        if (soTienStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠ Vui lòng nhập số tiền!");
            txtSoTien.requestFocus();
            return;
        }

        // 🔴 Kiểm tra có phải số hợp lệ không
        float soTien;
        try {
            // Dùng BigDecimal để parse và kiểm tra
            BigDecimal soTienBD = new BigDecimal(soTienStr);
            if (soTienBD.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "⚠ Số tiền phải lớn hơn 0!");
                txtSoTien.requestFocus();
                return;
            }
            // Gán lại về float
            soTien = soTienBD.floatValue();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "⚠ Số tiền phải là số!");
            txtSoTien.requestFocus();
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn thêm chi tiêu với số tiền: " + soTien + " không?",
            "Xác nhận thêm", JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) {
            return; // Người dùng không đồng ý
        }

        // ✅ Tạo đối tượng chi tiêu
        ChiTieu ct = new ChiTieu();
        ct.setNgay(java.time.LocalDate.now().toString()); // yyyy-MM-dd
        ct.setSoTien(soTien);
        ct.setMoTa(moTa);

        // ✅ Gọi DAO để thêm
        ChiTieuDao dao = new ChiTieuDaoImpl();
        dao.create(ct);

        JOptionPane.showMessageDialog(this, "  Thêm chi tiêu thành công!");
        // clearFormChiTieu();
        fillTableChiTieu();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, " Lỗi: " + e.getMessage());
        e.printStackTrace();
    }
}

public void fillTableChiTieu() {
    DefaultTableModel model = (DefaultTableModel) tblChiTieu.getModel();
    model.setRowCount(0); // Xóa bảng trước khi đổ mới

    ChiTieuDao dao = new ChiTieuDaoImpl();

    List<ChiTieu> list = dao.findAll(); // Gọi hàm lấy toàn bộ chi tiêu

    // ✅ Định dạng số tiền để tránh E notation
    DecimalFormat df = new DecimalFormat("#,##0.##");

    for (ChiTieu ct : list) {
        Object[] row = {
            ct.getMaChiTieu(),
            ct.getNgay(),
            df.format(ct.getSoTien()), // ✅ Format float để hiển thị đẹp
            ct.getMoTa()
        };
        model.addRow(row);
    }
}


   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblChiTieu = new javax.swing.JTable();
        txtTongTien = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        btnLocTheo = new javax.swing.JButton();
        txtToDate = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtSoTien = new javax.swing.JTextField();
        txtGhiChu = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblChiTieu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã CT", "Ngày", "Số Tiền", "Mô Tả"
            }
        ));
        jScrollPane1.setViewportView(tblChiTieu);
        if (tblChiTieu.getColumnModel().getColumnCount() > 0) {
            tblChiTieu.getColumnModel().getColumn(0).setMinWidth(50);
            tblChiTieu.getColumnModel().getColumn(0).setMaxWidth(50);
            tblChiTieu.getColumnModel().getColumn(1).setMinWidth(100);
            tblChiTieu.getColumnModel().getColumn(1).setMaxWidth(100);
            tblChiTieu.getColumnModel().getColumn(2).setMinWidth(150);
            tblChiTieu.getColumnModel().getColumn(2).setMaxWidth(150);
        }

        txtTongTien.setFont(new java.awt.Font("Segoe UI", 1, 19)); // NOI18N
        txtTongTien.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("Tổng Chi Tiêu:");

        btnLocTheo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnLocTheo.setText("LỌC");
        btnLocTheo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLocTheoActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Từ Năm/Tháng/Ngày: ");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Đến Năm/Tháng/Ngày: ");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLocTheo, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(txtFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtToDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(btnLocTheo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 920, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Danh Sách Chi Tiêu", jPanel2);

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 204, 204));
        jLabel2.setText("Mô Tả");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, 54, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 204, 204));
        jLabel3.setText("Số Tiền");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 43, 54, -1));

        txtSoTien.setBackground(new java.awt.Color(204, 204, 204));
        txtSoTien.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.add(txtSoTien, new org.netbeans.lib.awtextra.AbsoluteConstraints(73, 78, 330, 30));

        txtGhiChu.setBackground(new java.awt.Color(204, 204, 204));
        txtGhiChu.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.add(txtGhiChu, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, 340, 190));

        jButton1.setBackground(new java.awt.Color(102, 102, 102));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("Làm Mới");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 370, -1, -1));

        jButton2.setBackground(new java.awt.Color(102, 102, 102));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setText("Nhập Chi Tiêu");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 370, 150, -1));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/hinhnen.png"))); // NOI18N
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -30, 920, 460));

        jTabbedPane1.addTab("Nhập Chi Tiêu", jPanel3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 920, 460));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/hinhnen.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 528));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
     addChiTieu(); 
      txtSoTien.setText("");
    txtGhiChu.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    txtSoTien.setText("");
    txtGhiChu.setText("");// TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnLocTheoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLocTheoActionPerformed
        // TODO add your handling code here:
        ChiTieuDao dao = new ChiTieuDaoImpl();

    try {
        String tuNgayStr = txtFromDate.getText().trim();
        String denNgayStr = txtToDate.getText().trim();

        if (tuNgayStr.isEmpty() || denNgayStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ ngày bắt đầu và kết thúc!");
            return;
        }

        LocalDate tuNgay = LocalDate.parse(tuNgayStr);
        LocalDate denNgay = LocalDate.parse(denNgayStr);

        float tong = dao.TongChiTrongKhoang(tuNgay, denNgay); // ✅ đúng hàm

        txtTongTien.setText(String.format("%,.0f VNĐ", tong));
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Định dạng ngày không hợp lệ (yyyy-MM-dd)!");
    }
    }//GEN-LAST:event_btnLocTheoActionPerformed

  


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLocTheo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tblChiTieu;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtGhiChu;
    private javax.swing.JTextField txtSoTien;
    private javax.swing.JTextField txtToDate;
    private javax.swing.JTextField txtTongTien;
    // End of variables declaration//GEN-END:variables
}
