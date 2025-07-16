package entity;

import lombok.Data;

@Data
public class NhanVien {
    private int maNV;
    private String tenNV;
    private String sdt;
    private String chucVu;
    private double luong;
    private int soNgayLam;
    private int soNgayNghi;
}
