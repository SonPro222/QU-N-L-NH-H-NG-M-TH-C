package dao.impl;

import dao.HoaDonChiTietDAO;
import entity.HoaDonChiTiet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.XJdbc;

public class HoaDonChiTietDAOImpl implements HoaDonChiTietDAO {

   private final String INSERT_SQL = "INSERT INTO CT_HOADON (MaHD, MaChiTiet, TenMon, SoLuong, DonGia) VALUES (?, ?, ?, ?, ?)";

@Override
public void insert(HoaDonChiTiet ct) {
    try (
        Connection con = XJdbc.getConnection();
        PreparedStatement ps = con.prepareStatement(INSERT_SQL)
    ) {
        ps.setInt(1, ct.getMaHD());
        ps.setInt(2, ct.getMaChiTiet());
        ps.setString(3, ct.getTenMon());
        ps.setInt(4, ct.getSoLuong());
        ps.setDouble(5, ct.getDonGia());
        ps.executeUpdate();
    } catch (SQLException e) {
        throw new RuntimeException("Lỗi khi thêm CT_HOADON: " + e.getMessage(), e);
    }
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
public void deleteByHoaDonId(int maHD) {
    String sql = "DELETE FROM CT_HOADON WHERE MaHD = ?";
    try (
        Connection conn = XJdbc.getConnection(); 
        PreparedStatement stmt = conn.prepareStatement(sql)
    ) {
        stmt.setInt(1, maHD);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Lỗi khi xóa chi tiết hóa đơn theo MaHD: " + maHD, e);
    }
}

@Override
public void updateSoLuong(HoaDonChiTiet ct) {
    String sql = "UPDATE CT_HOADON SET SoLuong = ? WHERE MaHD = ? AND MaChiTiet = ?";
    try (
        Connection conn = XJdbc.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)
    ) {
        ps.setInt(1, ct.getSoLuong());
        ps.setInt(2, ct.getMaHD());
        ps.setInt(3, ct.getMaChiTiet());
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Lỗi khi cập nhật số lượng món: " + e.getMessage(), e);
    }
}

@Override
public double tinhTongTienTheoHoaDon(int maHD) {
    String sql = "SELECT SUM(SoLuong * DonGia) AS TongTien FROM CT_HOADON WHERE MaHD = ?";
    try (
        Connection conn = XJdbc.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)
    ) {
        ps.setInt(1, maHD);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getDouble("TongTien");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Lỗi khi tính tổng tiền hóa đơn: " + e.getMessage(), e);
    }
    return 0;
}



}
