package dao.impl;

import dao.HoaDonDAO;
import entity.HoaDon;
import entity.HoaDonChiTiet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.XJdbc;

public class HoaDonDAOImpl implements HoaDonDAO {
public List<HoaDon> select(String sql, Object... args) {
    List<HoaDon> list = new ArrayList<>();
    try {
        ResultSet rs = XJdbc.query(sql, args);
        while (rs.next()) {
            HoaDon hd = new HoaDon();
            hd.setMaHD(rs.getInt("MaHD"));
            hd.setMaBan(rs.getInt("MaBan"));
            hd.setMaNV(rs.getInt("MaNV"));
            hd.setNgayLap(rs.getTimestamp("NgayLap"));
            hd.setTrangThai(rs.getString("TrangThai"));
            hd.setTongTien(rs.getDouble("TongTien"));
            list.add(hd);
        }
        rs.getStatement().getConnection().close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}
@Override
public HoaDon findChuaThanhToanTheoBan(int maBan) {
    String sql = "SELECT * FROM HOADON WHERE MaBan = ? AND TRIM(TrangThai) = N'Chưa thanh toán'";

    try (
        Connection conn = XJdbc.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)
    ) {
        ps.setInt(1, maBan);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            HoaDon hd = new HoaDon();
            hd.setMaHD(rs.getInt("MaHD"));
            hd.setMaBan(rs.getInt("MaBan"));
            hd.setMaNV(rs.getInt("MaNV"));
            hd.setNgayLap(rs.getTimestamp("NgayLap"));
            String trangThai = rs.getString("TrangThai");
            if (trangThai == null) trangThai = "Chưa thanh toán"; // ✅ chống null
            hd.setTrangThai(trangThai);
            hd.setTongTien(rs.getDouble("TongTien"));
            return hd;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
@Override
public boolean updateTrangThai(HoaDon hd) {
    String sql = "UPDATE HOADON SET TRANGTHAI = ? WHERE MAHD = ?";
    try (
        Connection con = XJdbc.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
    ) {
        ps.setString(1, hd.getTrangThai()); // set trạng thái "Đã thanh toán"
        ps.setInt(2, hd.getMaHD());         // set mã hóa đơn
        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

    

@Override
public int insertReturnId(HoaDon hd) {
    String sql = "INSERT INTO HOADON (MaBan, MaNV, TrangThai, NgayLap) VALUES (?, ?, ?, GETDATE())";
    try (
        Connection con = XJdbc.getConnection();
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
    ) {
        ps.setInt(1, hd.getMaBan());
        ps.setInt(2, hd.getMaNV());
        ps.setString(3, hd.getTrangThai() == null ? "Chưa thanh toán" : hd.getTrangThai()); // ✅ fix null
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return -1;
}



@Override
public HoaDon findById(int maHD) {
    String sql = "SELECT * FROM HOADON WHERE MaHD = ?";
    List<HoaDon> list = select(sql, maHD);
    return list.isEmpty() ? null : list.get(0);
}


 

    @Override
public boolean updateTongTien(HoaDon hd) {
    String sql = "UPDATE HoaDon SET TongTien = ? WHERE MaHD = ?";
    try (Connection conn = XJdbc.getConnection(); 
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setDouble(1, hd.getTongTien());
        ps.setInt(2, hd.getMaHD());
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
@Override
public List<HoaDonChiTiet> findByHoaDonId(int maHD) {
    List<HoaDonChiTiet> list = new ArrayList<>();
    String sql = "SELECT * FROM CT_HOADON WHERE MaHD = ?";

    try (
        Connection conn = XJdbc.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)
    ) {
        ps.setInt(1, maHD);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            HoaDonChiTiet ct = new HoaDonChiTiet();
            ct.setMaChiTiet(rs.getInt("MaChiTiet"));
            ct.setMaHD(rs.getInt("MaHD"));
            ct.setTenMon(rs.getString("TenMon"));
            ct.setSoLuong(rs.getInt("SoLuong"));
            ct.setDonGia(rs.getDouble("DonGia"));
            list.add(ct);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}
    @Override
public void insert(HoaDon hoaDon) {
    String sql = "INSERT INTO HOADON (MaBan, NgayLap, TrangThai, MaNV) VALUES (?, ?, ?, ?)";
    try (
        Connection conn = XJdbc.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
    ) {
        ps.setInt(1, hoaDon.getMaBan());
        ps.setDate(2, new java.sql.Date(hoaDon.getNgayLap().getTime()));
        ps.setString(3, hoaDon.getTrangThai());
        ps.setInt(4, hoaDon.getMaNV());
        ps.executeUpdate();

        // Gán lại MaHD sau khi insert
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            hoaDon.setMaHD(rs.getInt(1));
        }

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Lỗi khi thêm hóa đơn: " + e.getMessage());
    }
}

    @Override
    public void update(HoaDon hd) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    @Override
public int xoaHoaDonRac() {
    String sql = """
        DELETE FROM HOADON
        WHERE TrangThai = N'Chưa thanh toán'
        AND MaHD NOT IN (
            SELECT DISTINCT MaHD FROM CT_HOADON
        )
    """;

    try (Connection conn = XJdbc.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        return ps.executeUpdate(); // Trả về số lượng hóa đơn đã xóa
    } catch (Exception e) {
        e.printStackTrace();
        return 0;
    }
}
@Override
public void deleteById(int maHD) {
    String sql = "DELETE FROM HOADON WHERE MaHD = ?";
    try (
        Connection conn = XJdbc.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)
    ) {
        ps.setInt(1, maHD);
        ps.executeUpdate();
    } catch (SQLException e) {
        throw new RuntimeException("Lỗi khi xóa hóa đơn: " + e.getMessage(), e);
    }
}



    
}
