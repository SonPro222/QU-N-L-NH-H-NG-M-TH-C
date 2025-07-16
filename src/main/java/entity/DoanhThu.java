package entity;

import java.sql.Date;
/**
 *
 * @author dangt
 */
public class DoanhThu {
    private int maDT;
    private Date ngay;
    private double tongThu;
    private double tongChi;
    private String ghiChu;

    // Getter & Setter
    public int getMaDT() { return maDT; }
    public void setMaDT(int maDT) { this.maDT = maDT; }

    public Date getNgay() { return ngay; }
    public void setNgay(Date ngay) { this.ngay = ngay; }

    public double getTongThu() { return tongThu; }
    public void setTongThu(double tongThu) { this.tongThu = tongThu; }

    public double getTongChi() { return tongChi; }
    public void setTongChi(double tongChi) { this.tongChi = tongChi; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public double getLoiNhuan() { return tongThu - tongChi; }
}
