/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.manager;

import dao.ChiTietMonAnDAO;
import dao.HoaDonChiTietDAO;
import dao.HoaDonDAO;
import dao.impl.ChiTietMonAnDAOImpl;
import dao.impl.HoaDonChiTietDAOImpl;
import dao.impl.HoaDonDAOImpl;
import dao.impl.MonAnDAOImpl;
import entity.ChiTietMonAn;
import entity.HoaDon;
import entity.HoaDonChiTiet;
import entity.MonAn;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import uui.Auth;

/**
 *
 * @author dangt
 */
public class DatMon extends javax.swing.JDialog {

    private int soBan;
    private List<HoaDonChiTiet> dsDatMon = new ArrayList<>();
    private HoaDonDAO hoaDonDAO = new HoaDonDAOImpl();
    private HoaDonChiTietDAO chiTietDAO = new HoaDonChiTietDAOImpl();
    private BanAn parent;
    private HoaDon hoaDonHienTai;
  
    public DatMon(java.awt.Frame owner, BanAn parent, boolean modal, int soBan) {
        super(owner, modal);
        this.parent = parent;
        this.soBan = soBan;
        this.hoaDonHienTai = hoaDonHienTai;
        initComponents();
        setTitle("Đặt món cho Bàn số " + soBan);
        lblSoBan.setText("Đang đặt món cho Bàn số " + soBan);
        setResizable(false);
        setLocationRelativeTo(null);
//        setSize(1000, 600);            // <-- kích thước mong muốn
        setResizable(false);          // <-- không cho kéo giãn
        setupTableStyles();
        fillToTable();
        fillChiTietMonAnTheoMonAn(0);
        hoaDonDAO = new HoaDonDAOImpl();
//       hoaDonHienTai = hoaDonDAO.findChuaThanhToanTheoBan(soBan);
//if (hoaDonHienTai != null) {
//    hienThiHoaDon(hoaDonHienTai);
//    loadChiTietMonAn(hoaDonHienTai.getMaHD());
//} else {
//    taoHoaDonMoi();
//}
hoaDonHienTai = hoaDonDAO.findChuaThanhToanTheoBan(soBan);
System.out.println("🔍 HĐ bàn " + soBan + ": " + (hoaDonHienTai == null ? "KHÔNG TỒN TẠI" : "TỒN TẠI - MaHD: " + hoaDonHienTai.getMaHD()));

if (hoaDonHienTai != null) {
    hienThiHoaDon(hoaDonHienTai);
    loadChiTietMonAn(hoaDonHienTai.getMaHD());
} else {
    taoHoaDonMoi();
}




    }
private void loadChiTietMonAn(int maHD) {
    List<HoaDonChiTiet> list = chiTietDAO.findByHoaDonId(maHD); // lấy từ DB
    dsDatMon.clear();
    dsDatMon.addAll(list);

    DefaultTableModel model = (DefaultTableModel) tblHoaDon.getModel();
    model.setRowCount(0); // xóa cũ

    for (HoaDonChiTiet ct : dsDatMon) {
        double thanhTien = ct.getSoLuong() * ct.getDonGia();
        model.addRow(new Object[]{
            ct.getTenMon(),
            ct.getSoLuong(),
            ct.getDonGia(),
            thanhTien
        });
    }System.out.println("Load lại chi tiết hóa đơn MaHD: " + maHD + ", Số món: " + list.size());

}

   

    private void setupTableStyles() {
        tblBangLoaiMon.setFont(new Font("Arial", Font.BOLD, 20));
        tblBangChiTietMon.setFont(new Font("Arial", Font.BOLD, 15));
        tblBangLoaiMon.setShowHorizontalLines(false);
        tblBangLoaiMon.setShowVerticalLines(false);
        tblBangChiTietMon.setShowHorizontalLines(false);
        tblBangChiTietMon.setShowVerticalLines(false);
    }

    public void fillToTable() {
        DefaultTableModel model = (DefaultTableModel) tblBangLoaiMon.getModel();
        model.setRowCount(0); // Xóa các dòng hiện tại trong bảng

        MonAnDAOImpl dao = new MonAnDAOImpl();
        List<MonAn> monAnList = dao.findAll(); // Lấy tất cả món ăn

        for (MonAn monAn : monAnList) {
            ImageIcon icon = null;
            try {
                if (monAn.getHinhAnh() != null && !monAn.getHinhAnh().isEmpty()) {
                    URL imgURL = getClass().getResource("/images/" + monAn.getHinhAnh());
                    if (imgURL != null) {
                        icon = new ImageIcon(new ImageIcon(imgURL)
                                .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                    }
                }
            } catch (Exception e) {
                System.out.println("Lỗi load ảnh cho món " + monAn.getTenMonAn() + ": " + e.getMessage());
            }
            model.addRow(new Object[]{icon, monAn.getTenMonAn()});
        }

        // Căn giữa ảnh (cột 0)
        tblBangLoaiMon.getColumnModel().getColumn(0).setPreferredWidth(110);
        tblBangLoaiMon.setRowHeight(110);
        tblBangLoaiMon.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                setHorizontalAlignment(SwingConstants.CENTER);
                setVerticalAlignment(SwingConstants.CENTER);
                setFont(new Font("Arial", Font.PLAIN, 20));

                if (value instanceof ImageIcon) {
                    setIcon((ImageIcon) value);
                    setText("");
                } else {
                    setIcon(null);
                    setText("Không có ảnh");
                }
                return this;
            }
        });

        // Căn giữa tên món ăn (cột số 1)
        DefaultTableCellRenderer centerTextRenderer = new DefaultTableCellRenderer();
        centerTextRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerTextRenderer.setVerticalAlignment(SwingConstants.CENTER);
        centerTextRenderer.setFont(new Font("Arial", Font.PLAIN, 20));
        tblBangLoaiMon.getColumnModel().getColumn(1).setCellRenderer(centerTextRenderer);

        // Bắt sự kiện click dòng → load chi tiết món ăn
        tblBangLoaiMon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblBangLoaiMon.getSelectedRow();
                if (row >= 0) {
                    // Lấy MonAn từ danh sách ban đầu để có MaMonAn chính xác
                    // Đảm bảo rằng monAnList vẫn có sẵn hoặc lấy lại từ DAO
                    MonAn selectedMonAn = monAnList.get(row);
                    fillChiTietMonAnTheoMonAn(selectedMonAn.getMaMonAn());
                }
            }
        });
    }

    private void fillChiTietMonAnTheoMonAn(int maMonAn) {
        DefaultTableModel model = (DefaultTableModel) tblBangChiTietMon.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        ChiTietMonAnDAO dao = new ChiTietMonAnDAOImpl();
        List<ChiTietMonAn> list = dao.findByMonAnId(maMonAn); // Truyền maMonAn để lọc

        for (ChiTietMonAn ct : list) {
            ImageIcon icon = null;
            try {
                if (ct.getHinhAnh() != null && !ct.getHinhAnh().isEmpty()) {
                    URL imgURL = getClass().getResource("/images/" + ct.getHinhAnh());
                    if (imgURL != null) {
                        icon = new ImageIcon(new ImageIcon(imgURL)
                                .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                    }
                }
            } catch (Exception e) {
                System.out.println("Lỗi load ảnh cho chi tiết món " + ct.getTenMon() + ": " + e.getMessage());
            }

            Object[] row = {
                ct.getMaChiTiet(), // Cột ẩn
                icon,
                ct.getTenMon(),
                ct.getGia(),
                1 // số lượng mặc định
            };
            model.addRow(row);
        }

        tblBangChiTietMon.setRowHeight(100);

        // Gán Spinner cho cột "Số lượng"
        if (tblBangChiTietMon.getColumnCount() >= 5) {
            tblBangChiTietMon.getColumnModel().getColumn(4).setCellEditor(new SpinnerEditor());
        }

        // Ẩn cột MaCT (cột 0)
        tblBangChiTietMon.getColumnModel().getColumn(0).setMinWidth(0);
        tblBangChiTietMon.getColumnModel().getColumn(0).setMaxWidth(0);
        tblBangChiTietMon.getColumnModel().getColumn(0).setWidth(0);

        // Renderer ảnh (cột 1)
        tblBangChiTietMon.getColumnModel().getColumn(1).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                JLabel lbl = new JLabel();
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setVerticalAlignment(SwingConstants.CENTER);
                if (value instanceof ImageIcon) {
                    lbl.setIcon((ImageIcon) value);
                }
                return lbl;
            }
        });

        // Renderer căn giữa các cột còn lại
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 2; i < tblBangChiTietMon.getColumnCount(); i++) {
            tblBangChiTietMon.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    /////////////////////////// bảng ghi nhớ tạm món ăn ////////////////////
private void themMonVaoBangDaChon() {
    int selectedRow = tblBangChiTietMon.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn món ăn trước khi thêm!");
        return;
    }

    // Lấy dữ liệu từ dòng đang chọn trong bảng Chi tiết món ăn
    int maChiTiet = (int) tblBangChiTietMon.getValueAt(selectedRow, 0);
    String tenMon = (String) tblBangChiTietMon.getValueAt(selectedRow, 2);

    Object giaObj = tblBangChiTietMon.getValueAt(selectedRow, 3);
    double donGia = (giaObj instanceof Number) ? ((Number) giaObj).doubleValue() : 0;

    // Lấy số lượng từ spinner hoặc bảng
    int soLuong = (int) tblBangChiTietMon.getValueAt(selectedRow, 4);

    // Tính thành tiền
    double thanhTien = donGia * soLuong;

    DefaultTableModel model = (DefaultTableModel) tblDaChon.getModel();

    // Kiểm tra xem món ăn đã có trong bảng tblDaChon chưa
    boolean daTonTai = false;
    for (int i = 0; i < model.getRowCount(); i++) {
        Object tenMonDaCo = model.getValueAt(i, 0);
        if (tenMonDaCo != null && tenMon.equals(tenMonDaCo.toString())) {
            // Cộng dồn số lượng
            int soLuongCu = (int) model.getValueAt(i, 1);
            int soLuongMoi = soLuongCu + soLuong;
            double thanhTienMoi = soLuongMoi * donGia; // Cập nhật thành tiền mới

            // Cập nhật lại số lượng và thành tiền
            model.setValueAt(soLuongMoi, i, 1);
            model.setValueAt(thanhTienMoi, i, 3);
            daTonTai = true;
            break;
        }
    }

    // Nếu chưa có thì thêm mới
    if (!daTonTai) {
        Object[] row = {tenMon, soLuong, donGia, thanhTien}; // Thêm cột thành tiền vào
        model.addRow(row);
    }
}


    // nút xóa món tạm 
    private void HuyMonDaChon() {
        int selectedRow = tblDaChon.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món muốn xóa khỏi danh sách đã chọn!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa món này không?", "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            DefaultTableModel model = (DefaultTableModel) tblDaChon.getModel();
            model.removeRow(selectedRow);
        }
    }
//======================BẢNG HÓA ĐƠN =====================

    private void hienThiHoaDon(HoaDon hd) {
    txtMaHD.setText(String.valueOf(hd.getMaHD()));
    txtNgayLap.setText(hd.getNgayLap().toString());

    // ✅ Luôn đặt trạng thái là "Chưa thanh toán" khi hiển thị
     txtTrangThai.setText(hd.getTrangThai());

    loadChiTietMonAn(hd.getMaHD());
    hienthitenNV();
}

 private void hienthitenNV() {
    if (Auth.nhanVienDangNhap != null) {
        txtHoTenNV.setText(Auth.nhanVienDangNhap.getTenNV());
    } else {
        txtHoTenNV.setText("Chưa đăng nhập");
    }
}

private void taoHoaDonMoi() {
    HoaDon hd = new HoaDon();
    hd.setMaBan(soBan);
    hd.setMaNV(Auth.nhanVienDangNhap.getMaNV()); // lấy đúng người đang login
    hd.setNgayLap(new Date());
    hd.setTrangThai("Chưa thanh toán"); // ✅ BẮT BUỘC
    int maHD = hoaDonDAO.insertReturnId(hd);
    
    if (maHD > 0) {
        hoaDonHienTai = hoaDonDAO.findById(maHD);

        // ✅ Cập nhật màu bàn sau khi tạo hóa đơn
        parent.capNhatToanBoBanAn();

        hienThiHoaDon(hoaDonHienTai);
    } else {
        JOptionPane.showMessageDialog(this, "Không thể tạo hóa đơn mới.");
        dispose();
    }
}


    public void setHoaDonHienTai(HoaDon hd) {
        this.hoaDonHienTai = hd;
        hienThiHoaDon(hd); // load lại dữ liệu nếu có
    }

private void datMon() {
    if (Auth.nhanVienDangNhap == null) {
        JOptionPane.showMessageDialog(this, "Không có nhân viên đăng nhập.");
        return;
    }

    DefaultTableModel modelDaChon = (DefaultTableModel) tblDaChon.getModel();
    if (modelDaChon.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "Bạn chưa chọn món nào để đặt.");
        return;
    }

    // Nếu chưa có hóa đơn thì tạo mới
    if (hoaDonHienTai == null || hoaDonHienTai.getMaHD() == 0) {
        HoaDon hd = new HoaDon();
        hd.setMaBan(soBan);
        hd.setMaNV(Auth.nhanVienDangNhap.getMaNV());
        hd.setNgayLap(new Date());
        hd.setTrangThai("Chưa thanh toán");
        int maHD = hoaDonDAO.insertReturnId(hd);
        if (maHD <= 0) {
            JOptionPane.showMessageDialog(this, "Không thể tạo hóa đơn mới.");
            return;
        }
        hoaDonHienTai = hoaDonDAO.findById(maHD);
    }

    ChiTietMonAnDAO ctDAO = new ChiTietMonAnDAOImpl();

    // Lấy danh sách chi tiết món đã có trong hóa đơn
    List<HoaDonChiTiet> chiTietHienTai = chiTietDAO.findByHoaDonId(hoaDonHienTai.getMaHD());

    for (int i = 0; i < modelDaChon.getRowCount(); i++) {
        String tenMon = modelDaChon.getValueAt(i, 0).toString();
        int soLuongMoi = (int) modelDaChon.getValueAt(i, 1);
        double donGia = (double) modelDaChon.getValueAt(i, 2);

        ChiTietMonAn chiTiet = ctDAO.findByTenMon(tenMon);
        if (chiTiet == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy món: " + tenMon);
            continue;
        }

        int maCT = chiTiet.getMaChiTiet();
        boolean daTonTai = false;

        for (HoaDonChiTiet ctDaCo : chiTietHienTai) {
            if (ctDaCo.getMaChiTiet() == maCT) {
                // ✅ Nếu đã có → cộng dồn số lượng
                int tongSoLuong = ctDaCo.getSoLuong() + soLuongMoi;

                HoaDonChiTiet ctCapNhat = new HoaDonChiTiet();
                ctCapNhat.setMaHD(hoaDonHienTai.getMaHD());
                ctCapNhat.setMaChiTiet(maCT);
                ctCapNhat.setSoLuong(tongSoLuong);
                chiTietDAO.updateSoLuong(ctCapNhat);

                daTonTai = true;
                break;
            }
        }

        if (!daTonTai) {
            HoaDonChiTiet ctMoi = new HoaDonChiTiet();
            ctMoi.setMaHD(hoaDonHienTai.getMaHD());
            ctMoi.setMaChiTiet(maCT);
            ctMoi.setTenMon(tenMon);
            ctMoi.setSoLuong(soLuongMoi);
            ctMoi.setDonGia(donGia);
            chiTietDAO.insert(ctMoi);
        }
    }

    // Load lại bảng hóa đơn
    loadChiTietMonAn(hoaDonHienTai.getMaHD());

    // Cập nhật trạng thái bàn
    parent.capNhatTrangThaiBan(soBan);

    // Xóa bảng ghi nhớ tạm
    modelDaChon.setRowCount(0);

    // Tính lại tổng tiền
    capNhatTongTienHoaDon();

    JOptionPane.showMessageDialog(this, "Đặt món thành công!");
}



    private void capNhatTongTienHoaDon() {
        DefaultTableModel model = (DefaultTableModel) tblHoaDon.getModel();
        double tongTien = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            Object ttObj = model.getValueAt(i, 3); // Thành tiền
            if (ttObj instanceof Number) {
                tongTien += ((Number) ttObj).doubleValue();
            }
        }
        lblTongTien.setText("TỔNG TIỀN: " + String.format("%,.0f VND", tongTien));
    }
//============================ XỬ LÝ THANH TOÁN ==========================
private void thanhToan() {
    int maBan = soBan; // hoặc từ nút bàn đang chọn
    HoaDon hoaDon = hoaDonDAO.findChuaThanhToanTheoBan(maBan);
HoaDon hd = hoaDonDAO.findChuaThanhToanTheoBan(maBan);
if (hd == null) {
    System.out.println("❌ Không tìm thấy hóa đơn CHƯA THANH TOÁN cho bàn: " + maBan);
    return;
}

System.out.println("🔍 Hóa đơn cần thanh toán:");
System.out.println("➡ Mã bàn: " + hd.getMaBan());
System.out.println("➡ Mã HD: " + hd.getMaHD());
System.out.println("➡ Trạng thái: " + hd.getTrangThai());
System.out.println("➡ Tổng tiền: " + hd.getTongTien());

    if (hoaDon == null) {
        System.out.println("❌ Không tìm thấy hóa đơn cho bàn: " + maBan);
        JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn cần thanh toán cho bàn " + maBan);
        return;
    }

    System.out.println("✅ Tìm thấy hóa đơn MaHD: " + hoaDon.getMaHD());

    List<HoaDonChiTiet> chiTietList = hoaDonDAO.findByHoaDonId(hoaDon.getMaHD());
    if (chiTietList.isEmpty()) {
        System.out.println("⚠️ Hóa đơn không có món ăn nào.");
        JOptionPane.showMessageDialog(this, "Không có món nào để thanh toán.");
        return;
    }

    double tongTien = 0;
    for (HoaDonChiTiet ct : chiTietList) {
        tongTien += ct.getSoLuong() * ct.getDonGia();
        System.out.println("➕ " + ct.getTenMon() + " x " + ct.getSoLuong() + " = " + (ct.getSoLuong() * ct.getDonGia()));
    }

    hoaDon.setTongTien(tongTien);
    hoaDon.setTrangThai("Đã thanh toán");

    boolean updateTien = hoaDonDAO.updateTongTien(hoaDon);
    System.out.println(" Cập nhật tổng tiền: " + (updateTien ? "THÀNH CÔNG" : "THẤT BẠI"));

    boolean updateTrangThai = hoaDonDAO.updateTrangThai(hoaDon);
    System.out.println(" Cập nhật trạng thái: " + (updateTrangThai ? "THÀNH CÔNG" : "THẤT BẠI"));

    if (updateTien && updateTrangThai) {
        System.out.println(" Đã thanh toán thành công hóa đơn: " + hoaDon.getMaHD());
        JOptionPane.showMessageDialog(this, "Thanh toán thành công. Tổng tiền: " + tongTien + " VNĐ");

        lamMoiBangMonAn();       // Clear bảng
        resetTongTien();         // Reset label tiền
         parent.capNhatToanBoBanAn();
//         hoaDonDAO.xoaHoaDonRac();
         this.dispose();
    } else {
        System.out.println(" Thanh toán thất bại!");
        JOptionPane.showMessageDialog(this, "Thanh toán thất bại!");
    }
}

private void lamMoiBangMonAn() {
    DefaultTableModel model = (DefaultTableModel) tblDaChon.getModel();
    model.setRowCount(0);
}
private void resetTongTien() {
    lblTongTien.setText("0 VNĐ");
}









    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtMaHD = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtNgayLap = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtTrangThai = new javax.swing.JTextField();
        txtHoTenNV = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        lblSoBan = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblBangLoaiMon = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBangChiTietMon = new javax.swing.JTable();
        lblTongTien = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        btnThanhToan = new javax.swing.JButton();
        btnDat = new javax.swing.JButton();
        btnHuy = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDaChon = new javax.swing.JTable();
        btnThem = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Mã HĐ");

        txtMaHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaHDActionPerformed(evt);
            }
        });

        jLabel3.setText("Ngày lập");

        jLabel4.setText("Trạng thái");

        jLabel6.setText("Tên Nhân Viên");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 68, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNgayLap, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtHoTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNgayLap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHoTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 310, 560, 50));

        lblSoBan.setText("Số bàn");
        getContentPane().add(lblSoBan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 100, 20));

        tblBangLoaiMon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Hình Ảnh", "Loại Món Ăn"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblBangLoaiMon);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(-1, 0, 360, 310));

        tblBangChiTietMon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã CT", "Hình Ảnh", "Tên Món Ăn", "Đơn Giá VND", "Số lượng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblBangChiTietMon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBangChiTietMonMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblBangChiTietMon);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 0, 730, 310));

        lblTongTien.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTongTien.setText("TỔNG TIỀN");
        getContentPane().add(lblTongTien, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 570, -1, -1));

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Tên Món Ăn", "Số Lượng", "Đơn Giá VND", "Thành Tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblHoaDon);

        getContentPane().add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, 630, 190));

        btnThanhToan.setText("Thanh Toán");
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });
        getContentPane().add(btnThanhToan, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 570, 280, -1));

        btnDat.setText("Đặt Món");
        btnDat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDatActionPerformed(evt);
            }
        });
        getContentPane().add(btnDat, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 570, 180, -1));

        btnHuy.setText("Hủy Món ");
        btnHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyActionPerformed(evt);
            }
        });
        getContentPane().add(btnHuy, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 570, 170, -1));

        jLabel5.setText("Hóa Đơn");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 60, -1));

        tblDaChon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tên Món Ăn", "Số Lượng", "Đơn Gía", "Thành Tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblDaChon);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 370, 450, 190));

        btnThem.setText("Thêm Món Ăn");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });
        getContentPane().add(btnThem, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 330, 450, 30));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtMaHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaHDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaHDActionPerformed

    private void tblBangChiTietMonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBangChiTietMonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblBangChiTietMonMouseClicked

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        thanhToan();
          // TODO add your handling code here:
    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void tblHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblHoaDonMouseClicked

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        themMonVaoBangDaChon();   // TODO add your handling code here:
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed
        HuyMonDaChon();  // TODO add your handling code here:
    }//GEN-LAST:event_btnHuyActionPerformed

    private void btnDatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDatActionPerformed
      datMon();  // TODO add your handling code here:
    }//GEN-LAST:event_btnDatActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDat;
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnThem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lblSoBan;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JTable tblBangChiTietMon;
    private javax.swing.JTable tblBangLoaiMon;
    private javax.swing.JTable tblDaChon;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTextField txtHoTenNV;
    private javax.swing.JTextField txtMaHD;
    private javax.swing.JTextField txtNgayLap;
    private javax.swing.JTextField txtTrangThai;
    // End of variables declaration//GEN-END:variables
}

class SpinnerEditor extends AbstractCellEditor implements TableCellEditor {

    private final JSpinner spinner;

    public SpinnerEditor() {
        spinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1)); // min=1, max=100, step=1
        spinner.setFont(new Font("Arial", Font.PLAIN, 16));
    }

    @Override
    public Object getCellEditorValue() {
        return spinner.getValue();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        if (value instanceof Integer) {
            spinner.setValue(value);
        } else {
            spinner.setValue(1);
        }
        return spinner;
    }
}
