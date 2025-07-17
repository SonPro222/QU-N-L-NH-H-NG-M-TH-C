package dao.impl;

import dao.ChiTietMonAnDAO;
import dao.MonAnDAO;
import entity.ChiTietMonAn;
import entity.MonAn;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.XJdbc;

public class ChiTietMonAnDAOImpl implements ChiTietMonAnDAO {

    @Override
    public ChiTietMonAn create(ChiTietMonAn entity) {
        String sql = "INSERT INTO ChiTietMonAn (TenMon, Gia, MaMonAn) VALUES (?, ?, ?)";
        try (Connection conn = XJdbc.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getTenMon());
            ps.setDouble(2, entity.getGia());
            ps.setInt(3, entity.getMaMonAn());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public void update(ChiTietMonAn entity) {
        String sql = "UPDATE ChiTietMonAn SET TenMon = ?, Gia = ?, MaMonAn = ? WHERE MaChiTiet = ?";
        try (Connection conn = XJdbc.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getTenMon());
            ps.setDouble(2, entity.getGia());
            ps.setInt(3, entity.getMaMonAn());
            ps.setInt(4, entity.getMaChiTiet());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM ChiTietMonAn WHERE MaChiTiet = ?";
        try (Connection conn = XJdbc.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ChiTietMonAn> findAll() {
        List<ChiTietMonAn> list = new ArrayList<>();
        String sql = "SELECT ct.MaChiTiet, ct.TenMon, ct.Gia, ct.MaMonAn, ma.TenMonAn, ma.HinhAnh "
                + "FROM ChiTietMonAn ct JOIN MonAn ma ON ct.MaMonAn = ma.MaMonAn";
        try (Connection conn = XJdbc.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ChiTietMonAn ct = new ChiTietMonAn();
                ct.setMaChiTiet(rs.getInt("MaChiTiet"));
                ct.setTenMon(rs.getString("TenMon"));
                ct.setGia(rs.getInt("Gia"));
                ct.setMaMonAn(rs.getInt("MaMonAn"));
                ct.setTenLoaiMon(rs.getString("TenMonAn"));
                ct.setHinhAnh(rs.getString("HinhAnh"));
                list.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ChiTietMonAn findById(Integer id) {
        String sql = "SELECT ct.MaChiTiet, ct.TenMon, ct.Gia, ct.MaMonAn, ma.TenMonAn, ma.HinhAnh "
                + "FROM ChiTietMonAn ct JOIN MonAn ma ON ct.MaMonAn = ma.MaMonAn WHERE ct.MaChiTiet = ?";
        try (Connection conn = XJdbc.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ChiTietMonAn ct = new ChiTietMonAn();
                    ct.setMaChiTiet(rs.getInt("MaChiTiet"));
                    ct.setTenMon(rs.getString("TenMon"));
                    ct.setGia(rs.getInt("Gia"));
                    ct.setMaMonAn(rs.getInt("MaMonAn"));
                    ct.setTenLoaiMon(rs.getString("TenMonAn"));
                    ct.setHinhAnh(rs.getString("HinhAnh"));
                    return ct;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ChiTietMonAn> findByMonAnId(int maMonAn) {
        List<ChiTietMonAn> list = new ArrayList<>();
       String sql = "SELECT ct.MaChiTiet, ct.TenMon, ct.Gia, ct.MaMonAn, ct.HinhAnh, ma.TenMonAn " +
             "FROM ChiTietMonAn ct " +
             "JOIN MonAn ma ON ct.MaMonAn = ma.MaMonAn " +
             "WHERE ct.MaMonAn = ?";
        try (Connection conn = XJdbc.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maMonAn);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietMonAn ct = new ChiTietMonAn();
                    ct.setMaChiTiet(rs.getInt("MaChiTiet"));
                    ct.setTenMon(rs.getString("TenMon"));
                    ct.setGia(rs.getInt("Gia"));
                    ct.setMaMonAn(rs.getInt("MaMonAn"));
                    ct.setTenLoaiMon(rs.getString("TenMonAn"));
                    ct.setHinhAnh(rs.getString("HinhAnh"));
                    list.add(ct);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    @Override
public ChiTietMonAn findByTenMon(String tenMon) {
    String sql = "SELECT * FROM ChiTietMonAn WHERE TenMon = ?";
    try (Connection con = XJdbc.getConnection(); 
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, tenMon);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            ChiTietMonAn ct = new ChiTietMonAn();
            ct.setMaChiTiet(rs.getInt("MaChiTiet"));
            ct.setTenMon(rs.getString("TenMon"));
            ct.setGia(rs.getDouble("Gia"));
            ct.setMaMonAn(rs.getInt("MaMonAn"));
            ct.setHinhAnh(rs.getString("HinhAnh"));
            return ct;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}


    @Override
    public List<ChiTietMonAn> findAllWithMonAn() {
        return findAll();
    }
     public ChiTietMonAn createWithMonAn(String tenLoaiMon, ChiTietMonAn chiTiet) {
    // 1. Thêm MonAn mới trước
    MonAn monAn = new MonAn();
    monAn.setTenMonAn(tenLoaiMon);
    monAn.setHinhAnh(chiTiet.getHinhAnh());
    MonAnDAO monAnDAO = new MonAnDAOImpl();
    monAn = monAnDAO.create(monAn);

   if (monAn.getMaMonAn() <= 0) {
    throw new RuntimeException("Không thể thêm món ăn vào bảng MonAn");
}
    // 2. Thêm ChiTietMonAn với MaMonAn vừa lấy được
    chiTiet.setMaMonAn(monAn.getMaMonAn());
    return this.create(chiTiet);
}
}
