package entity;

import lombok.Data;
import java.util.Date;

@Data
public class HoaDon {
    private int maHD;
    private int maBan;
    private int maNV;
    private String TenNV;
    private Date ngayLap;
    private String trangThai;
    private double tongTien;
    
}
