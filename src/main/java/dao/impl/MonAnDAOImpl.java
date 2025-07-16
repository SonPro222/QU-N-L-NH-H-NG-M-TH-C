package dao.impl;

import dao.MonAnDAO;
import entity.MonAn;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.XJdbc;

public class MonAnDAOImpl implements MonAnDAO {

    @Override
    public MonAn create(MonAn monAn) {
        String sql = "INSERT INTO MonAn (TenMonAn, HinhAnh) VALUES (?, ?)";
        try (Connection conn = XJdbc.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, monAn.getTenMonAn());
            ps.setString(2, monAn.getHinhAnh());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    monAn.setMaMonAn(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return monAn;
    }

    @Override
    public void update(MonAn monAn) {
        String sql = "UPDATE MonAn SET TenMonAn = ?, HinhAnh = ? WHERE MaMonAn = ?";
        try (Connection conn = XJdbc.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, monAn.getTenMonAn());
            ps.setString(2, monAn.getHinhAnh());
            ps.setInt(3, monAn.getMaMonAn());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM MonAn WHERE MaMonAn = ?";
        try (Connection conn = XJdbc.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<MonAn> findAll() {
        List<MonAn> list = new ArrayList<>();
        String sql = "SELECT * FROM MonAn";
        try (Connection conn = XJdbc.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                MonAn monAn = new MonAn();
                monAn.setMaMonAn(rs.getInt("MaMonAn"));
                monAn.setTenMonAn(rs.getString("TenMonAn"));
                monAn.setHinhAnh(rs.getString("HinhAnh"));
                list.add(monAn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public MonAn findById(Integer id) {
        MonAn monAn = null;
        String sql = "SELECT * FROM MonAn WHERE MaMonAn = ?";
        try (Connection conn = XJdbc.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    monAn = new MonAn();
                    monAn.setMaMonAn(rs.getInt("MaMonAn"));
                    monAn.setTenMonAn(rs.getString("TenMonAn"));
                    monAn.setHinhAnh(rs.getString("HinhAnh"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return monAn;
    }

    public MonAn findByName(String tenMon) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
    
