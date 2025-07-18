/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package uui;

import dao.NhanVienDAO;
import dao.UserDAO;
import dao.impl.NhanVienDAOImpl;
import dao.impl.UserDAOImpl;
import entity.NhanVien;
import entity.User;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import ui.manager.QuanLyNhaHang;
import util.XAuth;
import util.XDialog;
import org.mindrot.jbcrypt.BCrypt;

public class Login extends javax.swing.JDialog {

    public Login(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setTitle("Đăng Nhập");
        setResizable(false);
        setLocationRelativeTo(null);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });
    }
    UserDAO dao = new UserDAOImpl();
    NhanVienDAO nvDAO = new NhanVienDAOImpl();
    private void login() {
    String username = txtUsername.getText().trim();
    String password = new String(txtPassword.getPassword()).trim();

    if (username.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập!");
        return;
    }

    if (password.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu!");
        return;
    }

    // Tìm User từ tên đăng nhập
    User user = dao.findById(username);
    if (user == null) {
        JOptionPane.showMessageDialog(this, "Tài khoản không tồn tại!");
        return;
    }

    // Sử dụng BCrypt để so sánh mật khẩu đã băm
    if (!BCrypt.checkpw(password, user.getMatkhau())) {
        JOptionPane.showMessageDialog(this, "Sai mật khẩu!");
        return;
    }

    // Kiểm tra tài khoản
    if (!user.getTendangnhap().equals(username)) {
        JOptionPane.showMessageDialog(this, "Sai tài khoản");
        return;
    }

    // Lấy thông tin nhân viên từ mã nhân viên trong tài khoản
    NhanVien nv = nvDAO.findById(user.getMaNV());
    if (nv == null) {
        return;
    }

    // Đăng nhập thành công
    Auth.nhanVienDangNhap = nv;
    XAuth.user = user;

    JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
    this.dispose(); // đóng form
}
//    private void login() {
//        String username = txtUsername.getText().trim();
//        String password = new String(txtPassword.getPassword()).trim();
//
//        if (username.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập!");
//            return;
//        }
//
//        if (password.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu!");
//            return;
//        }
//
//        // Tìm User từ tên đăng nhập
//        User user = dao.findById(username);
//        if (user == null) {
//            JOptionPane.showMessageDialog(this, "Tài khoản không tồn tại!");
//            return;
//        }
//
//        if (!user.getMatkhau().equals(password)) {
//            JOptionPane.showMessageDialog(this, "Sai mật khẩu!");
//            return;
//        }   
//        if (!user.getTendangnhap().equals(username)) {
//            JOptionPane.showMessageDialog(this, "Sai tài khoản");
//            return;
//        }
//        NhanVien nv = nvDAO.findById(user.getMaNV());
//        if (nv == null) {
//            return;
//        }
//
//        Auth.nhanVienDangNhap = nv;
//        XAuth.user = user;
//      
//    
//        JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
//        this.dispose(); // đóng form
//    }

    public void exit() {
        if (XDialog.confirm("Bạn muốn kết thúc?")) {
            System.exit(0);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        txtUsername = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ẨM THỰC VIỆT NAM");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(166, 20, 250, 40));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Tài Khoản");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 120, 154, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Mật Khẩu");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 190, 154, -1));

        txtPassword.setBackground(new java.awt.Color(153, 153, 153));
        txtPassword.setForeground(new java.awt.Color(0, 0, 0));
        txtPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPasswordActionPerformed(evt);
            }
        });
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPasswordKeyReleased(evt);
            }
        });
        getContentPane().add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 220, 210, 30));

        jButton2.setText("Đăng Nhập");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 300, 101, 33));

        jButton3.setText("Kết Thúc");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 300, 101, 33));

        txtUsername.setBackground(new java.awt.Color(153, 153, 153));
        txtUsername.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 150, 210, 30));

        jCheckBox1.setText("Hiển Thị Mật Khẩu");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        getContentPane().add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 260, -1, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/login1.jpg"))); // NOI18N
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 580, 360));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPasswordActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        login();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            login();
        }
    }//GEN-LAST:event_txtPasswordKeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        exit();// TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
         jCheckBox1.addActionListener(e -> {
            if (jCheckBox1.isSelected()) {
                txtPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar('*');
            }
        });  // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
