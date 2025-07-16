package dao;

import entity.DoanhThu;
import java.sql.Date;
import java.util.List;

public interface DoanhThuDAO {
    List<DoanhThu> findAll();
    DoanhThu findByDate(Date date);
    List<DoanhThu> findByMonthYear(Integer thang, Integer nam);
    void insertOrUpdate(DoanhThu dt);
    void insertThuCong(String ngay, double tongThu, double tongChi, String ghiChu);
    void capNhatDoanhThuTuHoaDonVaChiTieu();
    List<DoanhThu> findByDateRange(Date from, Date to);


}
