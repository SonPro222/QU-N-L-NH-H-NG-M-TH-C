/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import java.util.List;
import dao.UserDAO;
import entity.NhanVien;
import entity.User;
import java.sql.ResultSet;
import util.XJdbc;
import util.XQuery;

public class UserDAOImpl implements UserDAO {

    String createSql = "INSERT INTO TAIKHOAN (TENDANGNHAP, MATKHAU, VAITRO, MaNV) VALUES (?, ?, ?, ?)";
    String updateSql = "UPDATE TAIKHOAN SET MATKHAU = ?, VAITRO = ?, MaNV = ? WHERE TENDANGNHAP = ?";
    String deleteSql = "DELETE FROM TAIKHOAN WHERE TENDANGNHAP = ?";
    String findAllSql = "SELECT * FROM TAIKHOAN";
    String findByIdSql = "SELECT * FROM TAIKHOAN WHERE TENDANGNHAP = ?";

    @Override
    public User create(User entity) {
        XJdbc.executeUpdate(createSql,
                entity.getTendangnhap(),
                entity.getMatkhau(),
                entity.getVaitro(),
                entity.getMaNV() // thêm mã nhân viên
        );
        return entity;
    }

    @Override
    public void update(User entity) {
        int rows = XJdbc.executeUpdate(updateSql,
                entity.getMatkhau(),
                entity.getVaitro(),
                entity.getMaNV(), // thêm dòng cập nhật MaNV
                entity.getTendangnhap()
        );
        System.out.println("Đã cập nhật " + rows + " dòng.");
    }

    @Override
    public void deleteById(String username) {
        XJdbc.executeUpdate(deleteSql, username);
    }

    @Override
    public List<User> findAll() {
        return XQuery.getEntityList(User.class, findAllSql);
    }

    public User findById(String username) {
    String sql = "SELECT * FROM TAIKHOAN WHERE TENDANGNHAP = ?";
    try (ResultSet rs = XJdbc.executeQuery(sql, username)) {
        if (rs.next()) {
            User user = new User();
            user.setTendangnhap(rs.getString("TenDangNhap"));
            user.setMatkhau(rs.getString("MatKhau"));
            user.setVaitro(rs.getString("VaiTro"));
            user.setMaNV(rs.getInt("MaNV")); // <-- Bắt buộc có dòng này!
            return user;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}


    @Override
    public boolean exists(String username) {
        String sql = "SELECT COUNT(*) FROM TAIKHOAN WHERE TENDANGNHAP = ?";
        Integer count = (Integer) XJdbc.value(sql, username);
        return count != null && count > 0;
    }

    @Override
    public List findAllWithMonAn() {
        throw new UnsupportedOperationException("Not supported in UserDAOImpl.");
    }
}
