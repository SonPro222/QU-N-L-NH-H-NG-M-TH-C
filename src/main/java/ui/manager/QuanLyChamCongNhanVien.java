/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.manager;

import dao.ChamCongDAO;
import dao.NhanVienDAO;
import dao.impl.ChamCongDAOImpl;
import dao.impl.NhanVienDAOImpl;
import entity.ChamCong;
import entity.NhanVien;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import util.TimeRange;
import util.XDate;

public class QuanLyChamCongNhanVien extends javax.swing.JDialog {

    NhanVienDAO nhanVienDAO = new NhanVienDAOImpl(); // khai báo ngoài hàm nếu cần
    ChamCongDAO chamCongDAO = new ChamCongDAOImpl(); // DAO chấm công
    private String ngayChamCongCu = null;
    boolean daChamCongHomNay = false;
    LocalDate now = LocalDate.now();
    int thang = now.getMonthValue();
    int nam = now.getYear();

    public QuanLyChamCongNhanVien(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        fillTableChamCongTheoNhanVien();
        fillNgayHienTai();
        
        fillBangLuongTheoThang(thang, nam);
           setResizable(false);          // <-- không cho kéo giãn
        setLocationRelativeTo(null);
        setTitle("Quản Lý Chấm Công");
    }
    //========== fill dữ liệu nhân viên =============/

    public void fillTableChamCongTheoNhanVien() {
        String ngayHienTaiStr = XDate.format(XDate.now(), "yyyy-MM-dd");
        Date ngayHienTai = XDate.parse(ngayHienTaiStr, "yyyy-MM-dd");

        // Nếu chưa fill hoặc đã sang ngày mới
        if (ngayChamCongCu == null || !ngayChamCongCu.equals(ngayHienTaiStr)) {
            DefaultTableModel model = (DefaultTableModel) tblChamCong.getModel();
            model.setRowCount(0); // Xóa dữ liệu cũ

            List<NhanVien> listNV = nhanVienDAO.findAll();
            boolean tatCaDaCham = true; // giả định ban đầu

            for (NhanVien nv : listNV) {
                boolean daCham = chamCongDAO.exists(nv.getMaNV(), ngayHienTai);

                if (!daCham) {
                    // Nếu chưa chấm công thì thêm vào bảng
                    model.addRow(new Object[]{
                        nv.getMaNV(),
                        nv.getTenNV(),
                        ngayHienTai,
                        false, // mặc định chưa đi làm
                        "" // ghi chú trống
                    });
                    tatCaDaCham = false; // còn người chưa chấm công
                }
            }

            // Nếu tất cả đã chấm công rồi thì disable nút
            btnChamCong.setEnabled(!tatCaDaCham);
            tblChamCong.setEnabled(!tatCaDaCham); // khóa bảng nếu đã chấm xong

            // Ghi nhớ ngày đã fill để không lặp lại
            ngayChamCongCu = ngayHienTaiStr;
        }
    }

    public void fillNgayHienTai() {
        Date ngayHienTai = TimeRange.today().getBegin(); // Lấy ngày bắt đầu hôm nay
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // format ngày
        txtNgayHienTai.setText(sdf.format(ngayHienTai)); // Hiển thị lên textfield
        txtNgayHienTai.setEnabled(false);
    }

    private void chamCongNhanVien() {
        DefaultTableModel model = (DefaultTableModel) tblChamCong.getModel();
        int rowCount = model.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            int maNV = (Integer) model.getValueAt(i, 0);
            Date ngay = (Date) model.getValueAt(i, 2);
            boolean coMat = (Boolean) model.getValueAt(i, 3);
            String ghiChu = (String) model.getValueAt(i, 4);

            // Kiểm tra trùng ngày
            if (chamCongDAO.exists(maNV, ngay)) {
                continue; // Bỏ qua nếu đã chấm công
            }

            // Tạo đối tượng chấm công
            ChamCong cc = new ChamCong();
            cc.setMaNV(maNV);
            cc.setNgayCham(ngay);
            cc.setCoMat(coMat);
            cc.setGhiChu(ghiChu);

            // Ghi vào DB
            chamCongDAO.insert(cc);
        }

        // Hiển thị thông báo thành công
        JOptionPane.showMessageDialog(this, "Đã chấm công thành công!");

        // Không cho chấm lại
        btnChamCong.setEnabled(false);

        // KHÓA KHẢ NĂNG CHỈNH SỬA BẢNG SAU KHI CHẤM
        tblChamCong.setEnabled(false);
        fillBangLuongTheoThang(thang, nam); // nếu không bị che khuất bởi cùng tên biến

    }

    public class ChamCongService {

        private ChamCongDAO chamCongDAO = new ChamCongDAOImpl();
        private NhanVienDAO nhanVienDAO = new NhanVienDAOImpl();

        public List<Object[]> thongKeCongVaLuongTheoThang(int thang, int nam) {
            List<Object[]> ketQua = new ArrayList<>();
            List<NhanVien> listNV = nhanVienDAO.findAll();

            for (NhanVien nv : listNV) {
                List<ChamCong> listCC = chamCongDAO.findByNhanVienAndMonth(nv.getMaNV(), thang, nam);

                int soNgayLam = 0;
                int soNgayNghi = 0;
                for (ChamCong cc : listCC) {
                    if (cc.isCoMat()) {
                        soNgayLam++;
                    } else {
                        soNgayNghi++;
                    }
                }

                int soNghiBiTru = Math.max(0, soNgayNghi - 2); // Trừ từ ngày thứ 3
                double luongCoDinh = nv.getLuong();
                double luong1Ngay = luongCoDinh / 28.0; // CHIA 28
                double truLuong = soNghiBiTru * luong1Ngay;
                double tongLuong = soNgayLam * luong1Ngay;

                DecimalFormat df = new DecimalFormat("#,###.##");
                ketQua.add(new Object[]{
                    nv.getMaNV(),
                    nv.getTenNV(),
                    findNgayBatDau(nv.getMaNV()),
                    soNgayLam,
                    soNgayNghi,
                    df.format(truLuong),
                    df.format(tongLuong)
                });
            }

            return ketQua;
        }

        private Date findNgayBatDau(int maNV) {
            return chamCongDAO.findNgayBatDau(maNV); // cần viết trong DAO
        }
    }

    public void fillBangLuongTheoThang(int thang, int nam) {
        ChamCongService chamCongService = new ChamCongService();
        List<Object[]> data = chamCongService.thongKeCongVaLuongTheoThang(thang, nam);

        DefaultTableModel model = (DefaultTableModel) tblBangLuong.getModel();
        model.setRowCount(0);

        for (Object[] row : data) {
            model.addRow(row);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblChamCong = new javax.swing.JTable();
        btnChamCong = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtNgayHienTai = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBangLuong = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblChamCong.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã NV", "Tên NV", "Ngày ", "Có Mặt", "Ghi chú"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblChamCong);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 940, 200));

        btnChamCong.setText("Chấm Công");
        btnChamCong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChamCongActionPerformed(evt);
            }
        });
        jPanel1.add(btnChamCong, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 260, 300, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Chấm Công Ngày :");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 130, -1));

        txtNgayHienTai.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jPanel1.add(txtNgayHienTai, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, 170, -1));

        tblBangLuong.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã NV", "Tên NV", " Ngày Bắt Đầu Làm", "Số Ngày Làm", "Só Ngày Nghỉ", "Trừ Lương", "Tổng Lương"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblBangLuong);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 290, 940, 170));

        jButton1.setText("Trả Lương Nhân Viên");
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 470, 290, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 940, 500));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/hinhnen.png"))); // NOI18N
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1010, 550));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnChamCongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChamCongActionPerformed
        chamCongNhanVien(); // TODO add your handling code here:
    }//GEN-LAST:event_btnChamCongActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChamCong;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblBangLuong;
    private javax.swing.JTable tblChamCong;
    private javax.swing.JTextField txtNgayHienTai;
    // End of variables declaration//GEN-END:variables
}
