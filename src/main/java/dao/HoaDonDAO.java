/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import entity.HoaDon;
import entity.HoaDonChiTiet;
import java.util.List;

public interface HoaDonDAO {
    int insertReturnId(HoaDon hd);
    HoaDon findChuaThanhToanTheoBan(int maBan);
    boolean updateTrangThai(HoaDon hd);
    boolean updateTongTien(HoaDon hd);
    HoaDon findById(int maHD);
    List<HoaDonChiTiet> findByHoaDonId(int maHD);
     void insert(HoaDon hd);
    void update(HoaDon hd);
     int xoaHoaDonRac();  
     void deleteById(int maHD);

}
