package dao.impl;

import dao.DoanhThuDAO;
import entity.DoanhThu;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import util.XJdbc;

public class DoanhThuDAOImpl implements DoanhThuDAO {

    @Override
    public List<DoanhThu> findAll() {
        List<DoanhThu> list = new ArrayList<>();
        String sql = "SELECT * FROM DOANHTHU ORDER BY Ngay DESC";
        try (Connection con = XJdbc.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DoanhThu dt = new DoanhThu();
                dt.setMaDT(rs.getInt("MaDT"));
                dt.setNgay(rs.getDate("Ngay"));
                dt.setTongThu(rs.getDouble("TongThu"));
                dt.setTongChi(rs.getDouble("TongChi"));
                dt.setGhiChu(rs.getString("GhiChu"));
                list.add(dt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
   @Override
public void capNhatDoanhThuTuHoaDonVaChiTieu() {
    String sqlThu = """
        SELECT CAST(NgayLap AS DATE) AS Ngay, SUM(TongTien) AS TongThu
        FROM HOADON
        WHERE TrangThai = N'Đã thanh toán'
        GROUP BY CAST(NgayLap AS DATE)
    """;

    String sqlChi = """
        SELECT Ngay, SUM(SoTien) AS TongChi
        FROM CHITIEU
        GROUP BY Ngay
    """;

    try (Connection con = XJdbc.getConnection()) {

        // === 1. Tổng Thu ===
        try (
            PreparedStatement psThu = con.prepareStatement(sqlThu);
            ResultSet rsThu = psThu.executeQuery()
        ) {
            while (rsThu.next()) {
                Date ngay = rsThu.getDate("Ngay");
                double tongThu = rsThu.getDouble("TongThu");

                DoanhThu dt = findByDate(ngay);
                if (dt == null) {
                    XJdbc.update("INSERT INTO DOANHTHU (Ngay, TongThu) VALUES (?, ?)", ngay, tongThu);
                } else {
                    XJdbc.update("UPDATE DOANHTHU SET TongThu = ? WHERE Ngay = ?", tongThu, ngay);
                }
            }
        }

        // === 2. Tổng Chi ===
        try (
            PreparedStatement psChi = con.prepareStatement(sqlChi);
            ResultSet rsChi = psChi.executeQuery()
        ) {
            while (rsChi.next()) {
                Date ngay = rsChi.getDate("Ngay");
                double tongChi = rsChi.getDouble("TongChi");

                DoanhThu dt = findByDate(ngay);
                if (dt == null) {
                    XJdbc.update("INSERT INTO DOANHTHU (Ngay, TongChi) VALUES (?, ?)", ngay, tongChi);
                } else {
                    XJdbc.update("UPDATE DOANHTHU SET TongChi = ? WHERE Ngay = ?", tongChi, ngay);
                }
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

@Override
public List<DoanhThu> findByDateRange(Date from, Date to) {
    List<DoanhThu> list = new ArrayList<>();
    String sql = "SELECT * FROM DOANHTHU WHERE Ngay BETWEEN ? AND ? ORDER BY Ngay DESC";

    try (Connection con = XJdbc.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setDate(1, new java.sql.Date(from.getTime()));
        ps.setDate(2, new java.sql.Date(to.getTime()));
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            DoanhThu dt = new DoanhThu();
            dt.setMaDT(rs.getInt("MaDT"));
            dt.setNgay(rs.getDate("Ngay"));
            dt.setTongThu(rs.getDouble("TongThu"));
            dt.setTongChi(rs.getDouble("TongChi"));
            dt.setGhiChu(rs.getString("GhiChu"));
            list.add(dt);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}



    @Override
    public void insertOrUpdate(DoanhThu dt) {
        DoanhThu existing = findByDate(dt.getNgay());
        if (existing == null) {
            // Insert
            String sql = "INSERT INTO DOANHTHU (Ngay, TongThu, TongChi, GhiChu) VALUES (?, ?, ?, ?)";
            XJdbc.update(sql, dt.getNgay(), dt.getTongThu(), dt.getTongChi(), dt.getGhiChu());
        } else {
            // Update
            String sql = "UPDATE DOANHTHU SET TongThu = ?, TongChi = ?, GhiChu = ? WHERE Ngay = ?";
            XJdbc.update(sql, dt.getTongThu(), dt.getTongChi(), dt.getGhiChu(), dt.getNgay());
        }
    }

    public DoanhThu findByDate(java.util.Date date) {
        String sql = "SELECT * FROM DOANHTHU WHERE Ngay = ?";
        try (Connection con = XJdbc.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            ps.setDate(1, sqlDate);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                DoanhThu dt = new DoanhThu();
                dt.setMaDT(rs.getInt("MaDT"));
                dt.setNgay(rs.getDate("Ngay"));
                dt.setTongThu(rs.getDouble("TongThu"));
                dt.setTongChi(rs.getDouble("TongChi"));
                dt.setGhiChu(rs.getString("GhiChu"));
                return dt;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public DoanhThu findByDate(java.sql.Date date) {
        String sql = "SELECT * FROM DOANHTHU WHERE Ngay = ?";
        try (Connection con = XJdbc.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, date);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                DoanhThu dt = new DoanhThu();
                dt.setMaDT(rs.getInt("MaDT"));
                dt.setNgay(rs.getDate("Ngay"));
                dt.setTongThu(rs.getDouble("TongThu"));
                dt.setTongChi(rs.getDouble("TongChi"));
                dt.setGhiChu(rs.getString("GhiChu"));
                return dt;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<DoanhThu> findByMonthYear(Integer thang, Integer nam) {
        List<DoanhThu> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM DOANHTHU WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (thang != null) {
            sql.append(" AND MONTH(Ngay) = ?");
            params.add(thang);
        }
        if (nam != null) {
            sql.append(" AND YEAR(Ngay) = ?");
            params.add(nam);
        }
        sql.append(" ORDER BY Ngay DESC");

        try (Connection con = XJdbc.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DoanhThu dt = new DoanhThu();
                dt.setMaDT(rs.getInt("MaDT"));
                dt.setNgay(rs.getDate("Ngay"));
                dt.setTongThu(rs.getDouble("TongThu"));
                dt.setTongChi(rs.getDouble("TongChi"));
                dt.setGhiChu(rs.getString("GhiChu"));
                list.add(dt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void insertThuCong(String ngay, double tongThu, double tongChi, String ghiChu) {
        String sql = "INSERT INTO DOANHTHU (Ngay, TongThu, TongChi, GhiChu) VALUES (?, ?, ?, ?)";
        try {
            java.sql.Date sqlDate = java.sql.Date.valueOf(ngay);
            XJdbc.update(sql, sqlDate, tongThu, tongChi, ghiChu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
