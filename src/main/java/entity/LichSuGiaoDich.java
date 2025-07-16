package entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LichSuGiaoDich {
    private int maHD; // Mã hóa đơn
    private int maBan; // Mã bàn
    private int maNV; // Mã nhân viên
    private Date ngayLap; // Ngày lập
    private String trangThai; // Trạng thái
    private double tongTien; 
    private String tenNhanVien; 
    private String tenBan; 
  

    
}
