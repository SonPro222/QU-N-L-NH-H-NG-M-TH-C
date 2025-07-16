package dao;

import entity.LichSuGiaoDich;
import java.util.List;
import java.util.Date;

public interface LichSuGiaoDichDAO {
    List<LichSuGiaoDich> findAll(); // Lấy tất cả giao dịch
    List<LichSuGiaoDich> findByDateRange(Date fromDate, Date toDate); // Lọc giao dịch theo ngày
    void update(LichSuGiaoDich lichSu); // Cập nhật giao dịch
}
