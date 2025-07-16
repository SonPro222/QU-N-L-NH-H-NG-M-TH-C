/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;
import dao.ChiTieuDao;
import entity.ChiTieu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import util.XJdbc;
import java.time.LocalDate;

/**
 *
 * @author ACER
 */
public class ChiTieuDaoImpl implements ChiTieuDao {
  
    @Override
    public List<ChiTieu> findByChiTieuId(int MaChiTieu) {
         List<ChiTieu> list = new ArrayList<>();
    String sql = "SELECT * FROM CHITIEU WHERE MaCT = ?";

    try (Connection conn = XJdbc.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, MaChiTieu);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            ChiTieu ct = new ChiTieu();
            ct.setMaChiTieu(rs.getInt("MaCT"));
            Date date = rs.getDate("Ngay");
            ct.setNgay(date != null ? date.toString() : null);
            ct.setSoTien(rs.getFloat("SoTien"));
            ct.setMoTa(rs.getString("MoTa"));
            list.add(ct);
        }

    } catch (SQLException e) {
        System.err.println("Lỗi truy vấn ChiTieu theo mã: " + e.getMessage());
    }
    return list;
    }

    @Override
    public void create(ChiTieu ct) {
        String sql = "INSERT INTO CHITIEU (Ngay, SoTien, MoTa) VALUES (?, ?, ?)";
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Kiểm tra null và định dạng ngày
            if (ct.getNgay() == null) {
                stmt.setNull(1, java.sql.Types.DATE);
            } else {
                stmt.setDate(1, java.sql.Date.valueOf(ct.getNgay()));
            }
            stmt.setFloat(2, ct.getSoTien());
            stmt.setString(3, ct.getMoTa());
            stmt.executeUpdate();
            System.out.println("✔ Thêm chi tiêu thành công!");
        } catch (SQLException | IllegalArgumentException e) {
            System.err.println("❌ Lỗi khi thêm chi tiêu: " + e.getMessage());
        }
    }
    
    @Override
    public List<ChiTieu> findAll() {
        List<ChiTieu> list = new ArrayList<>();
        String sql = "SELECT * FROM CHITIEU ORDER BY Ngay DESC";
    
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                ChiTieu ct = new ChiTieu();
                ct.setMaChiTieu(rs.getInt("MaCT"));
                Date date = rs.getDate("Ngay");
                ct.setNgay(date != null ? date.toString() : null);
                ct.setSoTien(rs.getFloat("SoTien"));
                ct.setMoTa(rs.getString("MoTa"));
                list.add(ct);
            }
    
        } catch (SQLException e) {
            System.err.println(" Lỗi khi lấy danh sách chi tiêu: " + e.getMessage());
        }
    
        return list;
    }

    @Override
    public float TongChiTieuTheoNgay(LocalDate ngay) {
        String sql = "SELECT SUM(SoTien) FROM CHITIEU WHERE Ngay >= ? AND Ngay < ?";
        float tong = 0;
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(ngay));
            stmt.setDate(2, java.sql.Date.valueOf(ngay.plusDays(1)));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tong = rs.getFloat(1);
                if (rs.wasNull()) tong = 0;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tính tổng chi theo ngày: " + e.getMessage());
        }
        return tong;
    }

    @Override
    public float TongChiTieuTheoThang(int thang, int nam) {
        String sql = "SELECT SUM(SoTien) FROM CHITIEU WHERE MONTH(Ngay) = ? AND YEAR(Ngay) = ?";
        float tong = 0;

        try (Connection conn = XJdbc.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, thang);
            stmt.setInt(2, nam);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tong = rs.getFloat(1);
                if (rs.wasNull()) tong = 0;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tính tổng chi theo tháng: " + e.getMessage());
        }

        return tong;
    }

    @Override
    public float TongChiTieuTheoNam(int nam) {
        String sql = "SELECT SUM(SoTien) FROM CHITIEU WHERE YEAR(Ngay) = ?";
        float tong = 0;
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, nam);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tong = rs.getFloat(1);
                if (rs.wasNull()) tong = 0;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tính tổng chi theo năm: " + e.getMessage());
        }

        return tong;
    }
    
    @Override
public float TongChiTrongKhoang(LocalDate tuNgay, LocalDate denNgay) {
    String sql = "SELECT SUM(SoTien) FROM CHITIEU WHERE Ngay BETWEEN ? AND ?";
    float tong = 0;

    try (Connection conn = XJdbc.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setDate(1, java.sql.Date.valueOf(tuNgay));
        stmt.setDate(2, java.sql.Date.valueOf(denNgay));

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            tong = rs.getFloat(1);
            if (rs.wasNull()) tong = 0;
        }

    } catch (SQLException e) {
        System.err.println("❌ Lỗi tính tổng chi trong khoảng: " + e.getMessage());
    }

    return tong;
}

}
