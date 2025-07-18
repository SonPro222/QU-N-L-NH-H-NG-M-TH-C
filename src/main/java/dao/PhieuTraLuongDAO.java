/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;


import entity.PhieuTraLuong;
import java.util.List;

public interface PhieuTraLuongDAO {
    void insert(PhieuTraLuong phieuTraLuong);
    void update(PhieuTraLuong phieuTraLuong);
    void delete(int maPhieuLuong);
    PhieuTraLuong findById(int maPhieuLuong);
    List<PhieuTraLuong> findByMaNV(int maNV);
    List<PhieuTraLuong> findAll();
}
