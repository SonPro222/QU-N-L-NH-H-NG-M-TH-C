/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.PhieuTraLuongDAO;
import entity.PhieuTraLuong;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.XJdbc;

public class PhieuTraLuongDAOImpl implements PhieuTraLuongDAO {

    final String INSERT_SQL = "INSERT INTO PHIEUTRALUONG (MaNV, NgayThanhToan, TongLuong, LuongTru, GhiChu) VALUES (?, ?, ?, ?, ?)";
    final String UPDATE_SQL = "UPDATE PHIEUTRALUONG SET MaNV = ?, NgayThanhToan = ?, TongLuong = ?, LuongTru = ?, GhiChu = ?,  WHERE MaPhieuLuong = ?";
    final String DELETE_SQL = "DELETE FROM PHIEUTRALUONG WHERE MaPhieuLuong = ?";
    final String SELECT_BY_ID_SQL = "SELECT * FROM PHIEUTRALUONG WHERE MaPhieuLuong = ?";
    final String SELECT_BY_MANV_SQL = "SELECT * FROM PHIEUTRALUONG WHERE MaNV = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM PHIEUTRALUONG";

    @Override
    public void insert(PhieuTraLuong phieuTraLuong) {
        XJdbc.update(INSERT_SQL,
                phieuTraLuong.getMaNV(),
                new java.sql.Date(phieuTraLuong.getNgayThanhToan().getTime()),
                phieuTraLuong.getTongLuong(),
                phieuTraLuong.getLuongTru(),
                phieuTraLuong.getGhiChu()
        );
    }

    @Override
    public void update(PhieuTraLuong phieuTraLuong) {
        XJdbc.update(UPDATE_SQL,
                phieuTraLuong.getMaNV(),
                new java.sql.Date(phieuTraLuong.getNgayThanhToan().getTime()),
                phieuTraLuong.getTongLuong(),
                phieuTraLuong.getLuongTru(),
                phieuTraLuong.getGhiChu(),
                phieuTraLuong.getMaPhieuLuong()
        );
    }

    @Override
    public void delete(int maPhieuLuong) {
        XJdbc.update(DELETE_SQL, maPhieuLuong);
    }

    @Override
    public PhieuTraLuong findById(int maPhieuLuong) {
        List<PhieuTraLuong> list = selectBySql(SELECT_BY_ID_SQL, maPhieuLuong);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<PhieuTraLuong> findByMaNV(int maNV) {
        return selectBySql(SELECT_BY_MANV_SQL, maNV);
    }

   @Override
public List<PhieuTraLuong> findAll() {
    String sql = "SELECT phieu.MaPhieuLuong, phieu.MaNV, nv.TenNV, phieu.NgayThanhToan, phieu.TongLuong, phieu.LuongTru, phieu.GhiChu " +
                 "FROM PHIEUTRALUONG phieu " +
                 "JOIN NHANVIEN nv ON phieu.MaNV = nv.MaNV";

    return selectBySql(sql);
}

 private List<PhieuTraLuong> selectBySql(String sql, Object... args) {
    List<PhieuTraLuong> list = new ArrayList<>();
    try (ResultSet rs = XJdbc.executeQuery(sql, args)) {
        while (rs.next()) {
            PhieuTraLuong phieu = new PhieuTraLuong();
            phieu.setMaPhieuLuong(rs.getInt("MaPhieuLuong"));
            phieu.setMaNV(rs.getInt("MaNV"));
            phieu.setTenNV(rs.getString("TenNV"));  // Lấy tên nhân viên từ bảng NHANVIEN
            phieu.setNgayThanhToan(rs.getDate("NgayThanhToan"));
            phieu.setTongLuong(rs.getDouble("TongLuong"));
            phieu.setLuongTru(rs.getDouble("LuongTru"));
            phieu.setGhiChu(rs.getString("GhiChu"));
            list.add(phieu);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

}