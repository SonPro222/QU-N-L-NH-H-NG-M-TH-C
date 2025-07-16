package dao;

import entity.HoaDonChiTiet;
import java.util.List;

public interface HoaDonChiTietDAO {
    void insert(HoaDonChiTiet ct);
    List<HoaDonChiTiet> findByHoaDonId(int maHD);
    public void deleteByHoaDonId(int maHD);
     void updateSoLuong(HoaDonChiTiet ct);
     double tinhTongTienTheoHoaDon(int maHD);
}
