/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.manager;

import dao.NhanVienDAO;
import dao.UserDAO;
import dao.impl.NhanVienDAOImpl;
import dao.impl.UserDAOImpl;
import entity.User;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author ACER
 */
public class QuanLyTaiKhoan extends javax.swing.JDialog {

    /**
     * Creates new form QuanLyTaiKhoan
     */
    public QuanLyTaiKhoan(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        fillToTable();
        setTitle("Quản Lý Tài Khoản");
//        setSize(1000, 600);            // <-- kích thước mong muốn
        setResizable(false);          // <-- không cho kéo giãn
        setLocationRelativeTo(null);
        chkManager1.setOpaque(false);
        chkManager1.setContentAreaFilled(false);
        chkManager.setOpaque(false);
        chkManager.setContentAreaFilled(false);
        jLabel7.setOpaque(false);
//       jLabel7.setContentAreaFilled(false);
        chkShowPassword.setOpaque(false);
        chkShowPassword.setContentAreaFilled(false);
    }

    public void fillToTable() {
        DefaultTableModel model = (DefaultTableModel) tblTaiKhoan.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ
        UserDAO dao = new UserDAOImpl();

        List<User> list = dao.findAll(); // lấy danh sách user từ DB
        for (User user : list) {
            String maskedPassword = "*".repeat(user.getMatkhau().length()); // che mật khẩu
            model.addRow(new Object[]{
                user.getTendangnhap(),
                maskedPassword,
                user.getVaitro(),
                user.getMaNV()// Thêm cột quyền
            });
        }

        // Cột 2 (mật khẩu) luôn hiển thị dấu * (nếu bạn muốn bảo vệ hiển thị)
        tblTaiKhoan.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value != null) {
                    setText(value.toString()); // giá trị đã được che từ trước
                } else {
                    setText("");
                }
            }
        });
    }

    public void addAccount() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String confirm = new String(txtConfirmPassword.getPassword()).trim();
        String maNVStr = txtMaNV.getText().trim(); // Giả sử có ô nhập mã NV

        // Kiểm tra trống các trường nhập
        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty() || maNVStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        // Kiểm tra vai trò
        if (!chkManager.isSelected() && !chkManager1.isSelected()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn vai trò (quyền)!");
            return;
        }

        // Kiểm tra mật khẩu khớp
        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu và xác nhận không khớp!");
            return;
        }

        // Kiểm tra username đã tồn tại
        UserDAO userDAO = new UserDAOImpl();
        if (userDAO.exists(username)) {
            JOptionPane.showMessageDialog(this, "Tài khoản đã tồn tại!");
            return;
        }

        // Parse mã nhân viên
        int maNV;
        try {
            maNV = Integer.parseInt(maNVStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên phải là số!");
            return;
        }

        // Kiểm tra mã nhân viên có tồn tại và đã có tài khoản
        NhanVienDAO nvDAO = new NhanVienDAOImpl();
        if (nvDAO.findById(maNV) == null) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên không tồn tại!");
            return;
        }

        // Kiểm tra xem nhân viên đã có tài khoản chưa
        if (userDAO.existsByMaNV(maNV)) {
            JOptionPane.showMessageDialog(this, "Nhân viên này đã có tài khoản!");
            return;
        }

        // Băm mật khẩu trước khi lưu vào cơ sở dữ liệu
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Gán vai trò
        String role = chkManager.isSelected() ? "Quản lý" : "Nhân viên";

        // Tạo đối tượng User
        User user = User.builder()
                .tendangnhap(username)
                .matkhau(hashedPassword) // Lưu mật khẩu đã được băm
                .vaitro(role)
                .maNV(maNV) // Gán mã nhân viên
                .build();

        userDAO.create(user);
        JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công!");
        fillToTable(); // Cập nhật lại bảng nếu có
    }

    public void XoaUser() {
        int row = tblTaiKhoan.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần xóa!");
            return;
        }

        String username = tblTaiKhoan.getValueAt(row, 0).toString(); // Cột 0 là TENDANGNHAP

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa tài khoản: " + username + " không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            UserDAO dao = new UserDAOImpl();
            dao.deleteById(username);
            JOptionPane.showMessageDialog(this, "Đã xóa tài khoản thành công!");

            fillToTable(); // load lại bảng sau khi xóa
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        group = new javax.swing.ButtonGroup();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        txtConfirmPassword = new javax.swing.JPasswordField();
        jLabel8 = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JPasswordField();
        jLabel7 = new javax.swing.JLabel();
        chkManager = new javax.swing.JCheckBox();
        chkManager1 = new javax.swing.JCheckBox();
        chkShowPassword = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTaiKhoan = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 204, 204));
        jLabel3.setText("Đăng ký tài khoản");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 10, 180, 24));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(204, 204, 204));
        jLabel4.setText("Nhập Tên Tài Khoản");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 30, 164, 24));

        txtUsername.setBackground(new java.awt.Color(102, 102, 102));
        txtUsername.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 60, 310, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(204, 204, 204));
        jLabel5.setText("Nhập Mật Khẩu");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 90, 164, 24));

        txtPassword.setBackground(new java.awt.Color(102, 102, 102));
        txtPassword.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 120, 310, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 204, 204));
        jLabel6.setText("Xác Nhận Mật Khẩu");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 150, 164, 24));

        txtConfirmPassword.setBackground(new java.awt.Color(102, 102, 102));
        txtConfirmPassword.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtConfirmPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 180, 310, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(204, 204, 204));
        jLabel8.setText("Mã Nhân Viên");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 210, 164, 24));

        txtMaNV.setBackground(new java.awt.Color(102, 102, 102));
        txtMaNV.setForeground(new java.awt.Color(255, 255, 255));
        txtMaNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaNVActionPerformed(evt);
            }
        });
        getContentPane().add(txtMaNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 240, 310, -1));

        jLabel7.setForeground(new java.awt.Color(204, 204, 204));
        jLabel7.setText("Quyền");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 300, 37, -1));

        group.add(chkManager);
        chkManager.setForeground(new java.awt.Color(204, 204, 204));
        chkManager.setText("Quản Lý");
        chkManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkManagerActionPerformed(evt);
            }
        });
        getContentPane().add(chkManager, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 320, 101, -1));

        group.add(chkManager1);
        chkManager1.setForeground(new java.awt.Color(204, 204, 204));
        chkManager1.setText("Nhân Viên");
        getContentPane().add(chkManager1, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 300, 101, -1));

        chkShowPassword.setForeground(new java.awt.Color(204, 204, 204));
        chkShowPassword.setText("Hiển Thị Mật Khẩu");
        chkShowPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkShowPasswordActionPerformed(evt);
            }
        });
        getContentPane().add(chkShowPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 270, -1, -1));

        jButton2.setBackground(new java.awt.Color(102, 102, 102));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(204, 204, 204));
        jButton2.setText("Đăng Ký");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 280, 130, -1));

        btnClear.setBackground(new java.awt.Color(102, 102, 102));
        btnClear.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnClear.setForeground(new java.awt.Color(204, 204, 204));
        btnClear.setText("Làm Mới");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        getContentPane().add(btnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 320, 130, -1));

        tblTaiKhoan.setBackground(new java.awt.Color(102, 102, 102));
        tblTaiKhoan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tblTaiKhoan.setForeground(new java.awt.Color(204, 204, 204));
        tblTaiKhoan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Tên Tài Khoản", "Mật Khẩu", "Quyền", "Mã NV"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblTaiKhoan);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 400, 330, 110));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 204, 204));
        jLabel2.setText("Danh Sách Tài Khoản");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 360, 210, 24));

        jButton3.setBackground(new java.awt.Color(102, 102, 102));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(204, 204, 204));
        jButton3.setText("Xóa Tài Khoản");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 520, -1, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/quanlytaikhoan (2).png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1040, 560));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chkShowPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkShowPasswordActionPerformed
        chkShowPassword.addActionListener(e -> {
            if (chkShowPassword.isSelected()) {
                txtPassword.setEchoChar((char) 0);
                txtConfirmPassword.setEchoChar((char) 0);
                txtMaNV.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar('*');
                txtConfirmPassword.setEchoChar('*');

            }
        });
    }//GEN-LAST:event_chkShowPasswordActionPerformed
    void clear() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
    }
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        addAccount();    // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        XoaUser();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clear();
    }//GEN-LAST:event_btnClearActionPerformed

    private void txtMaNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaNVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaNVActionPerformed

    private void chkManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkManagerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkManagerActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(QuanLyTaiKhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(QuanLyTaiKhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(QuanLyTaiKhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(QuanLyTaiKhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                QuanLyTaiKhoan dialog = new QuanLyTaiKhoan(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JCheckBox chkManager;
    private javax.swing.JCheckBox chkManager1;
    private javax.swing.JCheckBox chkShowPassword;
    private javax.swing.ButtonGroup group;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblTaiKhoan;
    private javax.swing.JPasswordField txtConfirmPassword;
    private javax.swing.JPasswordField txtMaNV;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
