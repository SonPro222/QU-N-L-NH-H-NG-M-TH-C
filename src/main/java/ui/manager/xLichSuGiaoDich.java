/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.manager;

import dao.HoaDonChiTietDAO;
import entity.LichSuGiaoDich;
import entity.HoaDonChiTiet;
import dao.LichSuGiaoDichDAO;
import dao.impl.HoaDonChiTietDAOImpl;
import dao.impl.LichSuGiaoDichDAOImpl; // Thêm import này nếu chưa có
import util.XJdbc;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.YearMonth;
import javax.swing.JOptionPane;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author KaiserAri
 */
public class xLichSuGiaoDich extends javax.swing.JDialog {

    public xLichSuGiaoDich(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setTitle("Lịch sử giao dịch");
        setSize(1000, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        fillTableHoaDon(); // <-- Gọi hàm fill bảng hóa đơn khi mở form

        tblLSHoaDon.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tblLSHoaDon.getSelectedRow();
                if (selectedRow != -1) {
                    int maHD = (int) tblLSHoaDon.getValueAt(selectedRow, 0); // Lấy MaHD từ cột 0
                    fillChiTietHoaDon(maHD); // Gọi hàm để fill bảng chi tiết
                }
            }
        });

    }

    private LichSuGiaoDichDAO lichSuGiaoDichDAO = new LichSuGiaoDichDAOImpl();

    public void fillTableHoaDon() {
        DefaultTableModel model = (DefaultTableModel) tblLSHoaDon.getModel();
        model.setRowCount(0); // Clear existing data

        List<LichSuGiaoDich> list = lichSuGiaoDichDAO.findAll();
        for (LichSuGiaoDich ls : list) {
            Object[] row = {
                ls.getMaHD(),
                ls.getNgayLap(),
                ls.getTrangThai(),
                ls.getTenNhanVien(),
                ls.getTenBan(),
                ls.getTongTien()
            };
            model.addRow(row);
        }
    }
    private HoaDonChiTietDAO chiTietDAO = new HoaDonChiTietDAOImpl();

    private void fillChiTietHoaDon(int maHD) {
        DefaultTableModel model = (DefaultTableModel) tblChiTietHoaDon.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        List<HoaDonChiTiet> list = chiTietDAO.findByHoaDonId(maHD);
        for (HoaDonChiTiet ct : list) {
            Object[] row = {
                ct.getMaHD(),
                ct.getMaChiTiet(),
                ct.getTenMon(),
                ct.getSoLuong(),
                ct.getDonGia()
            };
            model.addRow(row);
        }
    }

    public void filterHoaDonByDate(String tuNgay, String denNgay) {
        DefaultTableModel model = (DefaultTableModel) tblLSHoaDon.getModel();
        model.setRowCount(0); // Clear existing data

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = sdf.parse(tuNgay);
            Date toDate = sdf.parse(denNgay);

            List<LichSuGiaoDich> list = lichSuGiaoDichDAO.findByDateRange(fromDate, toDate);
            for (LichSuGiaoDich ls : list) {
                Object[] row = {
                    ls.getMaHD(),
                    ls.getNgayLap(),
                    ls.getTrangThai(),
                    ls.getTenNhanVien(),
                    ls.getTenBan(),
                    ls.getTongTien()
                };
                model.addRow(row);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lọc lịch sử giao dịch: " + e.getMessage());
        }
    }

    private Object getTenNhanVien() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTenNhanVien'");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        cbNamThangQuy = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLSHoaDon = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblChiTietHoaDon = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Từ ngày");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Đến Ngày");

        jButton1.setText("Lọc");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        cbNamThangQuy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Năm nay", "Tuần này", "Tháng này", "Qúy này", "Hôm nay" }));
        cbNamThangQuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbNamThangQuyActionPerformed(evt);
            }
        });

        tblLSHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã phiếu", "Ngày tạo", "Trạng thái", "Nhân viên", "Bàn", "Tổng tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblLSHoaDon);

        tblChiTietHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "MaHD", "MaCT", "Tên Món", "Số Lượng", "Đơn Giá"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblChiTietHoaDon);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Lịch Sử Chi Tiết Hóa Đơn");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Lịch Sử Hóa Đơn");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(145, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62)
                .addComponent(cbNamThangQuy, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(144, 144, 144))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(734, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(cbNamThangQuy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(5, 5, 5)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 940, 500));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/hinhnen.png"))); // NOI18N
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1032, 560));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String tuNgay = jTextField1.getText().trim(); // yyyy-MM-dd
        String denNgay = jTextField2.getText().trim(); // yyyy-MM-dd

        // Gọi DAO để lấy danh sách hóa đơn
        List<LichSuGiaoDich> danhSachLoc = lichSuGiaoDichDAO.locHoaDonTheoNgay(tuNgay, denNgay);

        // Đổ vào bảng
        DefaultTableModel model = (DefaultTableModel) tblLSHoaDon.getModel();
        model.setRowCount(0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (LichSuGiaoDich hd : danhSachLoc) {
            model.addRow(new Object[]{
                hd.getMaHD(),
                sdf.format(hd.getNgayLap()),
                hd.getTrangThai(), // Thêm trạng thái
                hd.getTenNhanVien(),
                hd.getTenBan(), // Thêm tên bàn
                hd.getTongTien()
            });

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cbNamThangQuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNamThangQuyActionPerformed
        String selected = (String) cbNamThangQuy.getSelectedItem();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String tuNgay = today.format(formatter);
        String denNgay = today.format(formatter);
        switch (selected) {
            case "Hôm nay":
                tuNgay = today.format(formatter);
                denNgay = today.plusDays(1).format(formatter);
                break;

            case "Tuần này":
                LocalDate monday = today.with(DayOfWeek.MONDAY);
                LocalDate sunday = today.with(DayOfWeek.SUNDAY);
                tuNgay = monday.format(formatter);
                denNgay = sunday.format(formatter);
                break;
            case "Tháng này":
                LocalDate firstDayOfMonth = today.withDayOfMonth(1);
                LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());
                tuNgay = firstDayOfMonth.format(formatter);
                denNgay = lastDayOfMonth.format(formatter);
                break;
            case "Qúy này":
                int currentQuarter = (today.getMonthValue() - 1) / 3 + 1;
                int startMonth = (currentQuarter - 1) * 3 + 1;
                int endMonth = startMonth + 2;
                LocalDate firstDayOfQuarter = LocalDate.of(today.getYear(), startMonth, 1);
                LocalDate lastDayOfQuarter = LocalDate.of(today.getYear(), endMonth, YearMonth.of(today.getYear(), endMonth).lengthOfMonth());
                tuNgay = firstDayOfQuarter.format(formatter);
                denNgay = lastDayOfQuarter.format(formatter);
                break;
            case "Năm nay":
                tuNgay = LocalDate.of(today.getYear(), 1, 1).format(formatter);
                denNgay = LocalDate.of(today.getYear(), 12, 31).format(formatter);
                break;
        }

        // Gọi hàm lọc
        filterHoaDonByDate(tuNgay, denNgay);

        // Gán lại text cho 2 ô ngày (nếu có)
        jTextField1.setText(tuNgay);
        jTextField2.setText(denNgay);

    }//GEN-LAST:event_cbNamThangQuyActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbNamThangQuy;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTable tblChiTietHoaDon;
    private javax.swing.JTable tblLSHoaDon;
    // End of variables declaration//GEN-END:variables
}
