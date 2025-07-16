/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.manager;

import dao.NhanVienDAO;
import dao.impl.NhanVienDAOImpl;
import entity.NhanVien;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
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
        
    }
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
    String chucVu = cboChucVu.getSelectedItem().toString();
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

        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblNhanVien = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        txtTenNV = new javax.swing.JTextField();
        txtSDT = new javax.swing.JTextField();
        txtLuong = new javax.swing.JTextField();
        cboChucVu = new javax.swing.JComboBox<>();
        tbnThemNhanVien = new javax.swing.JButton();
        btnXoaNhanVien = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblNhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
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

        jLabel1.setText("Họ Và Tên");

        jLabel2.setText("Số DT");

        jLabel3.setText("Chức Vụ");

        jLabel4.setText("Lương");

        jLabel5.setText("Mã");

        cboChucVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nhan Vien", "Quan Ly", " ", " " }));

        tbnThemNhanVien.setText("Thêm Nhân Viên");
        tbnThemNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbnThemNhanVienActionPerformed(evt);
            }
        });

        btnXoaNhanVien.setText("Xóa Nhân Viên");
        btnXoaNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaNhanVienActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tbnThemNhanVien))
                        .addGap(41, 41, 41)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(14, 14, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cboChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(66, 66, 66)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 127, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(btnXoaNhanVien)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tbnThemNhanVien)
                    .addComponent(btnXoaNhanVien))
                .addGap(18, 18, 18))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 940, 490));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/hinhnen.png"))); // NOI18N
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 530));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tbnThemNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbnThemNhanVienActionPerformed
        themNhanVien(); // TODO add your handling code here:
    }//GEN-LAST:event_tbnThemNhanVienActionPerformed

    private void btnXoaNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaNhanVienActionPerformed
        xoaNhanVien();// TODO add your handling code here:
    }//GEN-LAST:event_btnXoaNhanVienActionPerformed

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
    private javax.swing.JComboBox<String> cboChucVu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblNhanVien;
    private javax.swing.JButton tbnThemNhanVien;
    private javax.swing.JTextField txtLuong;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTenNV;
    // End of variables declaration//GEN-END:variables



}
