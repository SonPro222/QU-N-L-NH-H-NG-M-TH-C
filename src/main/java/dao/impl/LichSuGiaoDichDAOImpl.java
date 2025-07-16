package dao.impl;

import dao.LichSuGiaoDichDAO;
import entity.LichSuGiaoDich;
import util.XJdbc;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class LichSuGiaoDichDAOImpl implements LichSuGiaoDichDAO {

  @Override
public List<LichSuGiaoDich> findAll() {
    List<LichSuGiaoDich> list = new ArrayList<>();
    String sql = """
        SELECT hd.MaHD, hd.MaBan, hd.MaNV, hd.NgayLap, 
               hd.TrangThai, hd.TongTien,
               nv.TenNV, b.TenBan
        FROM HOADON hd
        JOIN NHANVIEN nv ON hd.MaNV = nv.MaNV
        JOIN BANAN b ON hd.MaBan = b.MaBan
        ORDER BY hd.NgayLap DESC
    """;

    try (Connection conn = XJdbc.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            LichSuGiaoDich ls = new LichSuGiaoDich();
            ls.setMaHD(rs.getInt("MaHD"));
            ls.setMaBan(rs.getInt("MaBan"));
            ls.setMaNV(rs.getInt("MaNV"));
            ls.setNgayLap(rs.getTimestamp("NgayLap"));
            ls.setTrangThai(rs.getString("TrangThai"));
            ls.setTongTien(rs.getDouble("TongTien"));
            ls.setTenNhanVien(rs.getString("TenNV"));
            ls.setTenBan(rs.getString("TenBan"));
            list.add(ls);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}


@Override
public List<LichSuGiaoDich> findByDateRange(Date fromDate, Date toDate) {
    List<LichSuGiaoDich> list = new ArrayList<>();
    String sql = """
        SELECT hd.MaHD, hd.MaBan, hd.MaNV, hd.NgayLap,
               hd.TrangThai, hd.TongTien,
               nv.TenNV, b.TenBan
        FROM HOADON hd
        JOIN NHANVIEN nv ON hd.MaNV = nv.MaNV
        JOIN BANAN b ON hd.MaBan = b.MaBan
        WHERE hd.NgayLap BETWEEN ? AND ?
        ORDER BY hd.NgayLap DESC
    """;

    try (Connection conn = XJdbc.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setDate(1, new java.sql.Date(fromDate.getTime()));
        ps.setDate(2, new java.sql.Date(toDate.getTime()));

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            LichSuGiaoDich ls = new LichSuGiaoDich();
            ls.setMaHD(rs.getInt("MaHD"));
            ls.setMaBan(rs.getInt("MaBan"));
            ls.setMaNV(rs.getInt("MaNV"));
            ls.setNgayLap(rs.getTimestamp("NgayLap"));
            ls.setTrangThai(rs.getString("TrangThai"));
            ls.setTongTien(rs.getDouble("TongTien"));
            ls.setTenNhanVien(rs.getString("TenNV"));
            ls.setTenBan(rs.getString("TenBan"));
            list.add(ls);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}


@Override
public void update(LichSuGiaoDich lichSu) {
    String sql = "UPDATE HOADON SET TrangThai = ?, NgayThanhToan = ? WHERE MaHD = ?";

    try (Connection conn = XJdbc.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, lichSu.getTrangThai());
//        ps.setTimestamp(2, new Timestamp(lichSu.getNgayThanhToan().getTime()));
        ps.setInt(3, lichSu.getMaHD());

        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



    // Phương thức select chung
}
