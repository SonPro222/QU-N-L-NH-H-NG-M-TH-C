/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author ACER
 */
import entity.NhanVien;
import entity.User;

public interface UserDAO extends CrudDAO<User, String> {
     User create(User entity);
     boolean exists(String username);
     public boolean existsByMaNV(int maNV);
     
}