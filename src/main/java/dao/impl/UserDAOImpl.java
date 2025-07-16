/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import java.util.List;
import dao.UserDAO;
import entity.NhanVien;
import entity.User;
import util.XJdbc;
import util.XQuery;

public class UserDAOImpl implements UserDAO {

    String createSql = "INSERT INTO TAIKHOAN (TENDANGNHAP, MATKHAU, VAITRO) VALUES (?, ?, ?)";
    String updateSql = "UPDATE TAIKHOAN SET MATKHAU = ?, VAITRO = ? WHERE TENDANGNHAP = ?";
    String deleteSql = "DELETE FROM TAIKHOAN WHERE TENDANGNHAP = ?";
    String findAllSql = "SELECT * FROM TAIKHOAN";
    String findByIdSql = "SELECT * FROM TAIKHOAN WHERE TENDANGNHAP = ?";

    @Override
    public User create(User entity) {
        XJdbc.executeUpdate(createSql,
                entity.getTendangnhap(),
                entity.getMatkhau(),
                entity.getVaitro()
        );
        return entity;
    }

    public void update(User entity) {
        System.out.println("Username cần cập nhật: " + entity.getTendangnhap());

        int rows = XJdbc.executeUpdate(updateSql,
                entity.getMatkhau(),
                entity.getTendangnhap()
        );
        System.out.println("Số dòng được cập nhật: " + rows);
    }

    public void deleteById(String username) {
        XJdbc.executeUpdate(deleteSql, username);
    }

    public List<User> findAll() {
        return XQuery.getEntityList(User.class, findAllSql);
    }

    public User findById(String username) {
        return XQuery.getSingleBean(User.class, findByIdSql, username);
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
