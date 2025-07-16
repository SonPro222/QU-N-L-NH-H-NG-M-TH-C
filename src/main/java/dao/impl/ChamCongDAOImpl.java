/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.ChamCongDAO;
import entity.ChamCong;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import util.XJdbc;

/**
 *
 * @author ACER
 */
public class ChamCongDAOImpl implements ChamCongDAO {

    final String INSERT_SQL = "INSERT INTO CHAMCONG (MaNV, NgayCham, CoMat, GhiChu) VALUES (?, ?, ?, ?)";
    final String UPDATE_SQL = "UPDATE CHAMCONG SET MaNV = ?, NgayCham = ?, CoMat = ?, GhiChu = ? WHERE MaChamCong = ?";
    final String DELETE_SQL = "DELETE FROM CHAMCONG WHERE MaChamCong = ?";
    final String SELECT_BY_ID_SQL = "SELECT * FROM CHAMCONG WHERE MaChamCong = ?";
    final String SELECT_BY_NGAY_SQL = "SELECT * FROM CHAMCONG WHERE NgayCham = ?";
    final String SELECT_BY_MANV_SQL = "SELECT * FROM CHAMCONG WHERE MaNV = ?";

    @Override
    public void insert(ChamCong cc) {
        XJdbc.update(INSERT_SQL,
                cc.getMaNV(),
                new java.sql.Date(cc.getNgayCham().getTime()),
                cc.isCoMat(),
                cc.getGhiChu()
        );
    }

    @Override
    public void update(ChamCong cc) {
        XJdbc.update(UPDATE_SQL,
                cc.getMaNV(),
                new java.sql.Date(cc.getNgayCham().getTime()),
                cc.isCoMat(),
                cc.getGhiChu(),
                cc.getMaChamCong()
        );
    }

    @Override
    public void delete(int maChamCong) {
        XJdbc.update(DELETE_SQL, maChamCong);
    }

    @Override
    public ChamCong findById(int maChamCong) {
        List<ChamCong> list = selectBySql(SELECT_BY_ID_SQL, maChamCong);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<ChamCong> findByNgay(Date ngay) {
        return selectBySql(SELECT_BY_NGAY_SQL, new java.sql.Date(ngay.getTime()));
    }

    @Override
    public List<ChamCong> findByNhanVien(int maNV) {
        return selectBySql(SELECT_BY_MANV_SQL, maNV);
    }

    private List<ChamCong> selectBySql(String sql, Object... args) {
        List<ChamCong> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(sql, args);
            while (rs.next()) {
                ChamCong cc = new ChamCong();
                cc.setMaChamCong(rs.getInt("MaChamCong"));
                cc.setMaNV(rs.getInt("MaNV"));
                cc.setNgayCham(rs.getDate("NgayCham"));
                cc.setCoMat(rs.getBoolean("CoMat"));
                cc.setGhiChu(rs.getString("GhiChu"));
                list.add(cc);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean exists(int maNV, Date ngay) {
        String sql = "SELECT COUNT(*) FROM CHAMCONG WHERE MaNV = ? AND NgayCham = ?";
        try {
            ResultSet rs = XJdbc.executeQuery(sql, maNV, new java.sql.Date(ngay.getTime()));
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<ChamCong> findByNhanVienAndMonth(int maNV, int thang, int nam) {
        String sql = """
        SELECT * FROM ChamCong 
        WHERE MaNV = ? 
        AND MONTH(NgayCham) = ? 
        AND YEAR(NgayCham) = ?
    """;
        return selectBySql(sql, maNV, thang, nam);
    }

    @Override
    public Date findNgayBatDau(int maNV) {
        String sql = """
        SELECT TOP 1 NgayCham FROM ChamCong 
        WHERE MaNV = ? 
        ORDER BY NgayCham ASC
    """;
        try (
                Connection con = XJdbc.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maNV);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDate("NgayCham");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
