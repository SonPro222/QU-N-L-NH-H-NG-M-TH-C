package ui.manager;

import dao.DoanhThuDAO;
import dao.impl.DoanhThuDAOImpl;
import entity.DoanhThu;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import util.ExcelExporter;

public class ThongKeDoanhThu extends JDialog {

    private JTable tblDoanhThu;
    private JLabel lblTongThu, lblTongChi, lblLoiNhuan;
    private JComboBox<String> cboThang, cboNam;
    private JButton btnLoc, btnXuatExcel, btnThemThuCong;
    private JButton btnCapNhat;
    private DoanhThuDAO doanhThuDAO = new DoanhThuDAOImpl();

    public ThongKeDoanhThu(Frame parent, boolean modal) {
        super(parent, modal);
        setTitle("Thống kê doanh thu");
        setSize(800, 500);
        setLocationRelativeTo(null);

        initComponents();
        fillComboBox();
        loadTableData(null, null);
        
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // North
        JPanel pnlTop = new JPanel(new FlowLayout());
        cboThang = new JComboBox<>();
        cboNam = new JComboBox<>();
        btnLoc = new JButton("Lọc");
        btnXuatExcel = new JButton("Xuất Excel");
        btnThemThuCong = new JButton("Thêm thủ công");

        pnlTop.add(new JLabel("Tháng:"));
        pnlTop.add(cboThang);
        pnlTop.add(new JLabel("Năm:"));
        pnlTop.add(cboNam);
        pnlTop.add(btnLoc);
        pnlTop.add(btnXuatExcel);
        pnlTop.add(btnThemThuCong);
        add(pnlTop, BorderLayout.NORTH);

        // Table
        tblDoanhThu = new JTable();
        tblDoanhThu.setModel(new DefaultTableModel(
                new Object[]{"Ngày", "Tổng Thu", "Tổng Chi", "Lợi Nhuận", "Ghi chú"}, 0
        ));
        add(new JScrollPane(tblDoanhThu), BorderLayout.CENTER);

        
        // South
        JPanel pnlBottom = new JPanel(new GridLayout(1, 3));
        lblTongThu = new JLabel("Tổng thu: 0", SwingConstants.CENTER);
        lblTongChi = new JLabel("Tổng chi: 0", SwingConstants.CENTER);
        lblLoiNhuan = new JLabel("Lợi nhuận: 0", SwingConstants.CENTER);

        pnlBottom.add(lblTongThu);
        pnlBottom.add(lblTongChi);
        pnlBottom.add(lblLoiNhuan);
        add(pnlBottom, BorderLayout.SOUTH);

        // Events
        btnLoc.addActionListener(e -> locDoanhThu());
        btnXuatExcel.addActionListener(e -> xuatExcel());
        btnThemThuCong.addActionListener(e -> themDoanhThuThuCong());
        btnCapNhat = new JButton("Cập nhật tự động");
        pnlTop.add(btnCapNhat);
        btnCapNhat.addActionListener(e -> {
            doanhThuDAO.capNhatDoanhThuTuHoaDonVaChiTieu();
            JOptionPane.showMessageDialog(this, "Đã cập nhật doanh thu & chi tiêu từ dữ liệu.");
            locDoanhThu();
        });

    }

    private void fillComboBox() {
        cboThang.addItem("Tất cả");
        for (int i = 1; i <= 12; i++) {
            cboThang.addItem("Tháng " + i);
        }

        cboNam.addItem("Tất cả");
        for (int y = 2023; y <= 2030; y++) {
            cboNam.addItem(String.valueOf(y));
        }
    }

   private void loadTableData(Integer thang, Integer nam) {
    List<DoanhThu> list = doanhThuDAO.findByMonthYear(thang, nam);
    DefaultTableModel model = (DefaultTableModel) tblDoanhThu.getModel();
    model.setRowCount(0);

    double tongThu = 0, tongChi = 0;
    DecimalFormat df = new DecimalFormat("#,###");

    for (DoanhThu dt : list) {
        tongThu += dt.getTongThu();
        tongChi += dt.getTongChi();
        model.addRow(new Object[]{
            dt.getNgay(),
            df.format(dt.getTongThu()) + " VND",
            df.format(dt.getTongChi()) + " VND",
            df.format(dt.getTongThu() - dt.getTongChi()) + " VND",
            dt.getGhiChu()
        });
    }

    lblTongThu.setText("Tổng thu: " + df.format(tongThu) + " VND");
    lblTongChi.setText("Tổng chi: " + df.format(tongChi) + " VND");
    lblLoiNhuan.setText("Lợi nhuận: " + df.format(tongThu - tongChi) + " VND");
}


    private void locDoanhThu() {
        Integer thang = cboThang.getSelectedIndex() == 0 ? null : cboThang.getSelectedIndex();
        Integer nam = cboNam.getSelectedIndex() == 0 ? null : Integer.parseInt((String) cboNam.getSelectedItem());
        loadTableData(thang, nam);
    }

    private void themDoanhThuThuCong() {
        String ngay = JOptionPane.showInputDialog(this, "Nhập ngày (yyyy-MM-dd):");
        String thu = JOptionPane.showInputDialog(this, "Tổng thu:");
        String chi = JOptionPane.showInputDialog(this, "Tổng chi:");
        String ghiChu = JOptionPane.showInputDialog(this, "Ghi chú:");

        try {
            double tongThu = Double.parseDouble(thu);
            double tongChi = Double.parseDouble(chi);
            doanhThuDAO.insertThuCong(ngay, tongThu, tongChi, ghiChu);
            JOptionPane.showMessageDialog(this, "Thêm doanh thu thành công!");
            locDoanhThu();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm doanh thu: " + e.getMessage());
        }
    }

    private void xuatExcel() {
        try {
            ExcelExporter.exportTable(tblDoanhThu, "DoanhThu.xlsx");
            JOptionPane.showMessageDialog(this, "Xuất Excel thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi xuất Excel: " + e.getMessage());
        }
    }

}
