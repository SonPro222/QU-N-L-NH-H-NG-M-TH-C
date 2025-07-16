/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.ChamCong;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ACER
 */
public interface ChamCongDAO {
    void insert(ChamCong cc);
    void update(ChamCong cc);
    void delete(int maChamCong);
    ChamCong findById(int maChamCong);
    List<ChamCong> findByNgay(Date ngay);
    List<ChamCong> findByNhanVien(int maNV);
     public boolean exists(int maNV, Date ngay);
     public Date findNgayBatDau(int maNV);
     public List<ChamCong> findByNhanVienAndMonth(int maNV, int thang, int nam);
}
