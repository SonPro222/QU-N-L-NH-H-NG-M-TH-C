/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.manager;

import dao.ChamCongDAO;
import dao.ChiTieuDao;
import dao.NhanVienDAO;
import dao.PhieuTraLuongDAO;
import dao.impl.ChamCongDAOImpl;
import dao.impl.ChiTieuDaoImpl;
import dao.impl.NhanVienDAOImpl;
import dao.impl.PhieuTraLuongDAOImpl;
import entity.ChamCong;
import entity.ChiTieu;
import entity.NhanVien;
import entity.PhieuTraLuong;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.TimeRange;
import util.XDate;

public class QuanLyChamCongNhanVien extends javax.swing.JDialog {

    ChiTieuDao chiTieuDao = new ChiTieuDaoImpl();
    NhanVienDAO nhanVienDAO = new NhanVienDAOImpl(); // khai báo ngoài hàm nếu cần
    ChamCongDAO chamCongDAO = new ChamCongDAOImpl(); // DAO chấm công
    PhieuTraLuongDAO phieuTraLuongDAO = new PhieuTraLuongDAOImpl();
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
        fillPhieuTraLuongToTable();
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

    //====================TRẢ LƯƠNG NHÂN VIÊN =====================//
    public void paySalary() {
        // Lấy ngày tháng hiện tại
        Date currentDate = new Date();  // Ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        int month = calendar.get(Calendar.MONTH) + 1;  // Tháng
        int year = calendar.get(Calendar.YEAR);

//        if (calendar.get(Calendar.DAY_OF_MONTH) != 1) {
//            JOptionPane.showMessageDialog(null, "Lương chỉ được trả vào ngày 1 của mỗi tháng.");
//            return;
//        }
        List<NhanVien> listNV = nhanVienDAO.findAll();

        // Khởi tạo Workbook cho Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Lương tháng " + month);

        // Tiêu đề cho bảng Excel
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Mã NV", "Tên NV", "Ngày Thanh Toán", "Tổng Lương", "Trừ Lương"};
        for (int i = 0; i < columns.length; i++) {
            headerRow.createCell(i).setCellValue(columns[i]);
        }

        double totalSalaryPaid = 0;

        int rowNum = 1;
        for (NhanVien nv : listNV) {
            List<ChamCong> listCC = chamCongDAO.findByNhanVienAndMonth(nv.getMaNV(), month, year);

            int soNgayLam = 0;
            int soNgayNghi = 0;
            for (ChamCong cc : listCC) {
                if (cc.isCoMat()) {
                    soNgayLam++;
                } else {
                    soNgayNghi++;
                }
            }

            int soNghiBiTru = Math.max(0, soNgayNghi - 2); // Trừ từ ngày nghỉ thứ 3
            double luongCoDinh = nv.getLuong();
            double luong1Ngay = luongCoDinh / 28.0;  // Chia cho 28 để tính lương ngày
            double truLuong = soNghiBiTru * luong1Ngay;
            double tongLuong = soNgayLam * luong1Ngay;

            // Thêm dòng vào bảng Excel
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(nv.getMaNV());
            row.createCell(1).setCellValue(nv.getTenNV());
            row.createCell(2).setCellValue(currentDate.toString()); // Chuyển ngày thành chuỗi
            row.createCell(3).setCellValue(tongLuong);
            row.createCell(4).setCellValue(truLuong);

            totalSalaryPaid += tongLuong;

            // Lưu thông tin trả lương vào bảng PHIEUTRALUONG
            PhieuTraLuong phieu = new PhieuTraLuong();
            phieu.setMaNV(nv.getMaNV());
            phieu.setNgayThanhToan(currentDate);
            phieu.setTongLuong(tongLuong);
            phieu.setLuongTru(truLuong);
            phieu.setGhiChu("Trả lương tháng " + month);

            // Gọi phương thức insert để lưu vào cơ sở dữ liệu
            phieuTraLuongDAO.insert(phieu);
        }

        // Ghi tổng lương vào bảng ThuChi
        ChiTieu thuChi = new ChiTieu();  // Tạo đối tượng ThuChi đúng
        thuChi.setSoTien(totalSalaryPaid);  // Gán tổng số tiền trả lương vào chi tiêu
        thuChi.setNgay(currentDate);  // Sử dụng ngày hiện tại cho ThuChi
        thuChi.setMoTa("Trả lương nhân viên tháng " + month);

        // Gọi phương thức lưu vào bảng ThuChi
        ChiTieuDao thuChiDao = new ChiTieuDaoImpl();
        thuChiDao.create(thuChi);  // Lưu thông tin vào bảng ThuChi

        // Xóa bảng chấm công (tblBangLuong) sau khi trả lương
        tblBangLuong.clearSelection();  // Nếu bạn sử dụng JTable, làm mới bảng
        // Gọi phương thức xóa hoặc làm trống bảng chấm công (tùy thuộc vào yêu cầu)

        // Xuất dữ liệu ra file Excel
        try (FileOutputStream fileOut = new FileOutputStream("LuongThang" + month + ".xlsx")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fillPhieuTraLuongToTable();
        JOptionPane.showMessageDialog(null, "Lương đã được trả cho tất cả nhân viên và xuất Excel thành công.");
    }

    public void fillPhieuTraLuongToTable() {
        // Lấy model của bảng
        DefaultTableModel model = (DefaultTableModel) tblBangLuongChiTiet1.getModel();

        // Xóa dữ liệu cũ trong bảng
        model.setRowCount(0);

        // Truy vấn dữ liệu từ DAO
        PhieuTraLuongDAO phieuTraLuongDAO = new PhieuTraLuongDAOImpl();
        List<PhieuTraLuong> listPhieuTraLuong = phieuTraLuongDAO.findAll();

        // Duyệt qua danh sách và điền dữ liệu vào bảng
        for (PhieuTraLuong phieu : listPhieuTraLuong) {
            // Thêm một dòng mới vào bảng
            model.addRow(new Object[]{
                phieu.getMaPhieuLuong(),
                phieu.getMaNV(),
                phieu.getTenNV(),
                phieu.getNgayThanhToan(), // Ngày trả lương
                phieu.getLuongTru(),
                phieu.getTongLuong(),
                phieu.getGhiChu() // Ghi chú
            });
        }
    }

    public void loc() {
        String maNVStr = txtMaNV.getText().trim();
        String tenNVKeyword = txtTennhanvien.getText().trim().toLowerCase();
        String thangNamStr = txtNgaythanhtoan.getText().trim(); // yyyy/MM

        Integer maNV = null;
        if (!maNVStr.isEmpty()) {
            try {
                maNV = Integer.parseInt(maNVStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Mã nhân viên phải là số.");
                return;
            }
        }

        Date tuNgay = null, denNgay = null;
        if (!thangNamStr.isEmpty()) {
            tuNgay = XDate.parse("01/" + thangNamStr, "dd/yyyy/MM");
            if (tuNgay == null) {
                JOptionPane.showMessageDialog(this, "Tháng thanh toán sai định dạng. Ví dụ: 2025/07");
                return;
            }
            LocalDate start = tuNgay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
            denNgay = Date.from(end.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        // 🔍 Gọi DAO hoặc lấy tất cả
        List<PhieuTraLuong> list;
        if (maNV != null && tuNgay != null && denNgay != null) {
            list = phieuTraLuongDAO.findByMaNVAndNgay(maNV, tuNgay, denNgay);
        } else {
            list = phieuTraLuongDAO.findAll();
        }

        // ✅ Lọc thêm theo tên nhân viên (nếu nhập)
        if (!tenNVKeyword.isEmpty()) {
            list = list.stream()
                    .filter(p -> p.getTenNV().toLowerCase().contains(tenNVKeyword))
                    .toList();
        }

        // 📋 Hiển thị dữ liệu ra bảng
        DefaultTableModel model = (DefaultTableModel) tblBangLuongChiTiet1.getModel();
        model.setRowCount(0);
        for (PhieuTraLuong phieu : list) {
            model.addRow(new Object[]{
                phieu.getMaPhieuLuong(),
                phieu.getMaNV(),
                phieu.getTenNV(),
                XDate.format(phieu.getNgayThanhToan(), "dd/MM/yyyy"),
                phieu.getLuongTru(),
                phieu.getTongLuong(),
                phieu.getGhiChu()
            });
        }

        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy phiếu trả lương phù hợp.");
        }
    }
 private void fillBangLuong1() {
    DefaultTableModel model = (DefaultTableModel) tblBangLuong.getModel();
    model.setRowCount(0); // Xóa dữ liệu cũ

    String maNVStr = txtMaNhanVien.getText().trim();
    String tenNVKeyword = txtTenNhanVien.getText().trim().toLowerCase();

    Integer maNVFilter = null;
    if (!maNVStr.isEmpty()) {
        try {
            maNVFilter = Integer.parseInt(maNVStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên phải là số nguyên.");
            return;
        }
    }

    ChamCongService chamCongService = new ChamCongService();
    LocalDate now = LocalDate.now();
    int thang = now.getMonthValue();
    int nam = now.getYear();

    List<Object[]> allData = chamCongService.thongKeCongVaLuongTheoThang(thang, nam);
    List<Object[]> filtered = new ArrayList<>();

    for (Object[] row : allData) {
        Integer maNVData;
        String tenNVData;

        try {
            maNVData = Integer.valueOf(row[0].toString().trim());
        } catch (Exception e) {
            continue;
        }

        tenNVData = (row[1] != null) ? row[1].toString().toLowerCase() : "";

        boolean matchMa = (maNVFilter == null) || maNVData.equals(maNVFilter);
        boolean matchTen = tenNVKeyword.isEmpty() || tenNVData.contains(tenNVKeyword);

        if (matchMa && matchTen) {
            filtered.add(row);
        }
    }

    for (Object[] row : filtered) {
        model.addRow(new Object[]{
            row[0], // Mã NV
            row[1], // Tên NV
            row[2], // Ngày bắt đầu
            row[3], // Số ngày làm
            row[4], // Số ngày nghỉ
            row[5], // Trừ lương
            row[6]  // Tổng lương
        });
    }

    if (filtered.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên phù hợp.");
    }
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblChamCong = new javax.swing.JTable();
        btnChamCong = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtNgayHienTai = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBangLuong = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtTenNhanVien = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtMaNhanVien = new javax.swing.JTextField();
        btnBangLuong = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblBangLuongChiTiet1 = new javax.swing.JTable();
        btnLocbangluong = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtTennhanvien = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtNgaythanhtoan = new javax.swing.JTextField();
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
        if (tblChamCong.getColumnModel().getColumnCount() > 0) {
            tblChamCong.getColumnModel().getColumn(0).setMinWidth(50);
            tblChamCong.getColumnModel().getColumn(0).setMaxWidth(50);
            tblChamCong.getColumnModel().getColumn(3).setMinWidth(50);
            tblChamCong.getColumnModel().getColumn(3).setMaxWidth(50);
        }

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 900, 140));

        btnChamCong.setText("Chấm Công");
        btnChamCong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChamCongActionPerformed(evt);
            }
        });
        jPanel1.add(btnChamCong, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 200, 300, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Chấm Công Ngày :");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 130, -1));

        txtNgayHienTai.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jPanel1.add(txtNgayHienTai, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, 170, -1));

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
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblBangLuong);
        if (tblBangLuong.getColumnModel().getColumnCount() > 0) {
            tblBangLuong.getColumnModel().getColumn(0).setMinWidth(60);
            tblBangLuong.getColumnModel().getColumn(0).setMaxWidth(60);
            tblBangLuong.getColumnModel().getColumn(3).setMinWidth(100);
            tblBangLuong.getColumnModel().getColumn(3).setMaxWidth(100);
            tblBangLuong.getColumnModel().getColumn(4).setMinWidth(100);
            tblBangLuong.getColumnModel().getColumn(4).setMaxWidth(100);
        }

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 900, 170));

        jButton1.setText("Trả Lương Nhân Viên");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 410, -1, -1));

        jLabel6.setText("Tên Nhân Viên :");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 420, -1, -1));

        txtTenNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenNhanVienActionPerformed(evt);
            }
        });
        jPanel1.add(txtTenNhanVien, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 410, 130, -1));

        jLabel7.setText("Mã Nhân Viên :");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 420, -1, -1));

        jLabel8.setText("Mã Nhân Viên :");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 420, -1, -1));

        txtMaNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaNhanVienActionPerformed(evt);
            }
        });
        jPanel1.add(txtMaNhanVien, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 410, 130, -1));

        btnBangLuong.setText("Lọc");
        btnBangLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBangLuongActionPerformed(evt);
            }
        });
        jPanel1.add(btnBangLuong, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 410, -1, -1));

        jTabbedPane1.addTab("Chấm Công", jPanel1);

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblBangLuongChiTiet1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã ", "MaNV", "Tên Nhân Viên", "Ngày Thanh Toán", "Trù Lương", "Tổng Lương", "Ghi Chú"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tblBangLuongChiTiet1);
        if (tblBangLuongChiTiet1.getColumnModel().getColumnCount() > 0) {
            tblBangLuongChiTiet1.getColumnModel().getColumn(0).setMinWidth(50);
            tblBangLuongChiTiet1.getColumnModel().getColumn(0).setMaxWidth(50);
            tblBangLuongChiTiet1.getColumnModel().getColumn(1).setMinWidth(60);
            tblBangLuongChiTiet1.getColumnModel().getColumn(1).setMaxWidth(60);
        }

        jPanel3.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 900, 350));

        btnLocbangluong.setText("Lọc");
        btnLocbangluong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLocbangluongActionPerformed(evt);
            }
        });
        jPanel3.add(btnLocbangluong, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 390, -1, -1));

        jLabel3.setText("MaNV : ");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, -1, -1));

        txtMaNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaNVActionPerformed(evt);
            }
        });
        jPanel3.add(txtMaNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 390, 90, -1));

        jLabel4.setText("Tên Nhân Viên :");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 400, -1, -1));
        jPanel3.add(txtTennhanvien, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 390, 150, -1));

        jLabel5.setText("Ngày Thanh Toán :");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 400, -1, -1));
        jPanel3.add(txtNgaythanhtoan, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 390, 130, -1));

        jTabbedPane1.addTab("Bảng Lương Chi Tiết", jPanel3);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 900, 480));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/hinhnen.png"))); // NOI18N
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1010, 550));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnChamCongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChamCongActionPerformed
        chamCongNhanVien(); // TODO add your handling code here:
    }//GEN-LAST:event_btnChamCongActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        paySalary();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtMaNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaNVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaNVActionPerformed

    private void btnLocbangluongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLocbangluongActionPerformed
        loc(); // TODO add your handling code here:

    }//GEN-LAST:event_btnLocbangluongActionPerformed

    private void btnBangLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBangLuongActionPerformed
        // TODO add your handling code here:
        fillBangLuong1();

    }//GEN-LAST:event_btnBangLuongActionPerformed

    private void txtTenNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenNhanVienActionPerformed
    }//GEN-LAST:event_txtTenNhanVienActionPerformed

    private void txtMaNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaNhanVienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaNhanVienActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBangLuong;
    private javax.swing.JButton btnChamCong;
    private javax.swing.JButton btnLocbangluong;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tblBangLuong;
    private javax.swing.JTable tblBangLuongChiTiet1;
    private javax.swing.JTable tblChamCong;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtMaNhanVien;
    private javax.swing.JTextField txtNgayHienTai;
    private javax.swing.JTextField txtNgaythanhtoan;
    private javax.swing.JTextField txtTenNhanVien;
    private javax.swing.JTextField txtTennhanvien;
    // End of variables declaration//GEN-END:variables
}
