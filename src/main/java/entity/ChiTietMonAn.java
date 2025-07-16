package entity;

import lombok.Data;

@Data
public class ChiTietMonAn {
    private int maChiTiet;
    private String tenMon;
    private double gia;
    private int maMonAn; // kiểu int vì MaMonAn là số trong DB
    private String tenLoaiMon; // từ bảng MonAn
    private String hinhAnh;
}
