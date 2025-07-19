/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.manager;

import dao.NhanVienDAO;
import dao.impl.NhanVienDAOImpl;
import entity.NhanVien;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import util.XJdbc;

/**
 *
 * @author 
 */
public class QuanLyNhanVien extends javax.swing.JDialog {
    
private NhanVienDAO nhanVienDAO = new NhanVienDAOImpl();

    public QuanLyNhanVien(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        fillTableNhanVien();
        setTitle("Quản Lý Nhân Viên");
        cboChucVu.setOpaque(false);
        cboChucVu.setBorder(null);
         cboChucVu1.setOpaque(false);
        cboChucVu1.setBorder(null);
         tblNhanVien.setBackground(new Color(150, 150, 150));
         tblNhanVien.getTableHeader().setBackground(new Color(180, 180, 180));

//       iconpro();

        
    }
//    public void iconpro(){
//    ImageIcon icon = new ImageIcon(getClass().getResource("/images/anhdongpro.gif"));
//jLabel11.setIcon(icon);
//
//    }
 private void fillTableNhanVien() {
    DefaultTableModel model = (DefaultTableModel) tblNhanVien.getModel();
    model.setRowCount(0); // Xóa dữ liệu cũ

    List<Object[]> list = nhanVienDAO.findAllWithUsername();
    for (Object[] row : list) {
        model.addRow(row); // row: {MaNV, TenNV, SDT, ChucVu, Luong, TenDangNhap}
    }
}
 private void themNhanVien() {
    String ten = txtTenNV.getText().trim();
    String sdt = txtSDT.getText().trim();
    String chucVu = cboChucVu.isSelected() ? "Quản lý" : "Nhân viên";
    
    String luongStr = txtLuong.getText().trim();

    // Kiểm tra tên
    if (ten.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Tên nhân viên không được để trống!");
        txtTenNV.requestFocus();
        return;
    }
    if (!ten.matches("^[\\p{L} ]+$")) {
        JOptionPane.showMessageDialog(this, "Tên chỉ được chứa chữ cái và khoảng trắng!");
        txtTenNV.requestFocus();
        return;
    }

    // Kiểm tra số điện thoại
    if (sdt.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Số điện thoại không được để trống!");
        txtSDT.requestFocus();
        return;
    }
    if (!sdt.matches("^\\d{9,11}$")) {
        JOptionPane.showMessageDialog(this, "Số điện thoại phải từ 9 đến 11 chữ số!");
        txtSDT.requestFocus();
        return;
    }

    // Kiểm tra lương
    if (luongStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Lương không được để trống!");
        txtLuong.requestFocus();
        return;
    }

    double luong;
    try {
        luong = Double.parseDouble(luongStr);
        if (luong <= 0) {
            JOptionPane.showMessageDialog(this, "Lương phải là số dương!");
            txtLuong.requestFocus();
            return;
        }
        if (luong < 3_000_000) {
            JOptionPane.showMessageDialog(this, "Lương phải lớn hơn 3 triệu!");
            txtLuong.requestFocus();
            return;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Lương phải là số hợp lệ!");
        txtLuong.requestFocus();
        return;
    }

    // Tạo đối tượng và lưu
    NhanVien nv = new NhanVien();
    nv.setTenNV(ten);
    nv.setSdt(sdt);
    nv.setChucVu(chucVu);
    nv.setLuong(luong);
    nv.setSoNgayLam(0);
    nv.setSoNgayNghi(0);

    int maNV = nhanVienDAO.insertAndReturnId(nv);
    if (maNV != -1) {
        txtMaNV.setText(String.valueOf(maNV));
        JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
        fillTableNhanVien();
    } else {
        JOptionPane.showMessageDialog(this, "Thêm nhân viên thất bại!");
    }
} private void xoaNhanVien() {
    int row = tblNhanVien.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên để xóa!");
        return;
    }

    int maNV = (int) tblNhanVien.getValueAt(row, 0); // Cột 0 là MaNV
    String tenNV = (String) tblNhanVien.getValueAt(row, 1); // Cột 1 là tên nhân viên

    int confirm = JOptionPane.showConfirmDialog(this,
        "Bạn có chắc chắn muốn xóa nhân viên \"" + tenNV + "\" (Mã: " + maNV + ")?",
        "Xác nhận xóa",
        JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        try {
            nhanVienDAO.deleteById(maNV);
            JOptionPane.showMessageDialog(this, "Đã xóa nhân viên thành công!");
            fillTableNhanVien(); // Cập nhật lại bảng
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Xóa thất bại: " + e.getMessage());
        }
    }
}



   
        

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel5 = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtTenNV = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtLuong = new javax.swing.JTextField();
        tbnThemNhanVien = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblNhanVien = new javax.swing.JTable();
        btnXoaNhanVien = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        cboChucVu = new javax.swing.JCheckBox();
        cboChucVu1 = new javax.swing.JCheckBox();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(204, 204, 204));
        jLabel5.setText("Thêm nhân viên");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 120, -1));

        txtMaNV.setBackground(new java.awt.Color(102, 102, 102));
        txtMaNV.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtMaNV.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtMaNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 160, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 204, 204));
        jLabel1.setText("Họ Và Tên");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 72, -1));

        txtTenNV.setBackground(new java.awt.Color(102, 102, 102));
        txtTenNV.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtTenNV.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtTenNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 160, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 204, 204));
        jLabel2.setText("Số DT");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 50, -1));

        txtSDT.setBackground(new java.awt.Color(102, 102, 102));
        txtSDT.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtSDT.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtSDT, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 160, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 204, 204));
        jLabel3.setText("Chức Vụ");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 64, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(204, 204, 204));
        jLabel4.setText("Lương");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 52, -1));

        txtLuong.setBackground(new java.awt.Color(102, 102, 102));
        txtLuong.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtLuong.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtLuong, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 270, 160, -1));

        tbnThemNhanVien.setBackground(new java.awt.Color(102, 102, 102));
        tbnThemNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tbnThemNhanVien.setForeground(new java.awt.Color(255, 255, 255));
        tbnThemNhanVien.setText("Thêm Nhân Viên");
        tbnThemNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbnThemNhanVienActionPerformed(evt);
            }
        });
        getContentPane().add(tbnThemNhanVien, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, 160, -1));

        tblNhanVien.setBackground(new java.awt.Color(102, 102, 102));
        tblNhanVien.setForeground(new java.awt.Color(0, 0, 0));
        tblNhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã NV", "Tên NV", "Số DT", "Chức Vụ", "Lương", "Tên Đăng Nhập"
            }
        ));
        jScrollPane2.setViewportView(tblNhanVien);
        if (tblNhanVien.getColumnModel().getColumnCount() > 0) {
            tblNhanVien.getColumnModel().getColumn(0).setMinWidth(50);
            tblNhanVien.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 390, 590, 180));

        btnXoaNhanVien.setBackground(new java.awt.Color(102, 102, 102));
        btnXoaNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXoaNhanVien.setForeground(new java.awt.Color(255, 255, 255));
        btnXoaNhanVien.setText("Xóa Nhân Viên");
        btnXoaNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaNhanVienActionPerformed(evt);
            }
        });
        getContentPane().add(btnXoaNhanVien, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, 160, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 204, 204));
        jLabel6.setText("Mã Nhân Viên");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 100, -1));

        buttonGroup1.add(cboChucVu);
        cboChucVu.setForeground(new java.awt.Color(204, 204, 204));
        cboChucVu.setSelected(true);
        cboChucVu.setText("Nhân Viên");
        getContentPane().add(cboChucVu, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, 101, -1));

        buttonGroup1.add(cboChucVu1);
        cboChucVu1.setForeground(new java.awt.Color(204, 204, 204));
        cboChucVu1.setText("Quản Lý");
        cboChucVu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboChucVu1ActionPerformed(evt);
            }
        });
        getContentPane().add(cboChucVu1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 230, 101, 20));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/quanlynhanvienok (2).png"))); // NOI18N
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 0, 1070, 590));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tbnThemNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbnThemNhanVienActionPerformed
        themNhanVien(); // TODO add your handling code here:
    }//GEN-LAST:event_tbnThemNhanVienActionPerformed

    private void btnXoaNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaNhanVienActionPerformed
        xoaNhanVien();// TODO add your handling code here:
    }//GEN-LAST:event_btnXoaNhanVienActionPerformed

    private void cboChucVu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboChucVu1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboChucVu1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(QuanLyNhanVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuanLyNhanVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuanLyNhanVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuanLyNhanVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                QuanLyNhanVien dialog = new QuanLyNhanVien(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnXoaNhanVien;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox cboChucVu;
    private javax.swing.JCheckBox cboChucVu1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblNhanVien;
    private javax.swing.JButton tbnThemNhanVien;
    private javax.swing.JTextField txtLuong;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTenNV;
    // End of variables declaration//GEN-END:variables



}
