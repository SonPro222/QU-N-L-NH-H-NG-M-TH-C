package dao;

import entity.ChiTietMonAn;
import java.util.List;

public interface ChiTietMonAnDAO {
    List<ChiTietMonAn> findByMonAnId(int maMonAn);

    // Tạo mới món ăn chi tiết
    ChiTietMonAn create(ChiTietMonAn entity);
   ChiTietMonAn findByTenMon(String tenMon);
    // Cập nhật món ăn chi tiết
    void update(ChiTietMonAn entity);

    // Xóa món ăn chi tiết theo mã
    void deleteById(Integer id);

    // Lấy danh sách tất cả món ăn chi tiết (bao gồm thông tin từ bảng MonAn)
    List<ChiTietMonAn> findAll();

    // Tìm một món ăn chi tiết theo mã
    ChiTietMonAn findById(Integer id);

    // Lấy tất cả dữ liệu chi tiết món ăn kèm thông tin món ăn
    List<ChiTietMonAn> findAllWithMonAn();
}
