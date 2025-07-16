/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.ChiTieu;
import java.time.LocalDate;
import java.util.List;

public interface ChiTieuDao {
    void create(ChiTieu ct); // sửa lại tên hàm
    List<ChiTieu> findByChiTieuId(int MaChiTieu);
    List<ChiTieu> findAll();
    float TongChiTieuTheoNgay(LocalDate ngay);
    float TongChiTieuTheoThang(int thang, int nam); // sửa lại tham số và kiểu trả về
    float TongChiTieuTheoNam(int nam); // sửa lại tham số và kiểu trả về
    float TongChiTrongKhoang(LocalDate tuNgay, LocalDate denNgay);

}
