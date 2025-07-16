package dao.impl;

import dao.NhanVienDAO;
import entity.NhanVien;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.XJdbc;
import util.XQuery;

public class NhanVienDAOImpl implements NhanVienDAO {

    final String INSERT_SQL = "INSERT INTO NHANVIEN (TenNV, SDT, ChucVu, Luong, SoNgayLam, SoNgayNghi, TenDangNhap) VALUES (?, ?, ?, ?, ?, ?, ?)";
    final String UPDATE_SQL = "UPDATE NHANVIEN SET TenNV = ?, SDT = ?, ChucVu = ?, Luong = ?, SoNgayLam = ?, SoNgayNghi = ?, TenDangNhap = ? WHERE MaNV = ?";
    final String DELETE_SQL = "DELETE FROM NHANVIEN WHERE MaNV = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM NHANVIEN";
    final String SELECT_BY_ID_SQL = "SELECT * FROM NHANVIEN WHERE MaNV = ?";

    @Override
    public void insert(NhanVien nv) {
        XJdbc.update(INSERT_SQL,
            nv.getTenNV(),
            nv.getSdt(),
            nv.getChucVu(),
            nv.getLuong(),
            nv.getSoNgayLam(),
            nv.getSoNgayNghi(),
            nv.getTenDangNhap()
        );
    }

    @Override
    public void update(NhanVien nv) {
        XJdbc.update(UPDATE_SQL,
            nv.getTenNV(),
            nv.getSdt(),
            nv.getChucVu(),
            nv.getLuong(),
            nv.getSoNgayLam(),
            nv.getSoNgayNghi(),
            nv.getTenDangNhap(),
            nv.getMaNV()
        );
    }

    @Override
    public void deleteById(Integer id) {
        XJdbc.update(DELETE_SQL, id);
    }

    @Override
    public NhanVien findById(Integer id) {
        List<NhanVien> list = selectBySql(SELECT_BY_ID_SQL, id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<NhanVien> findAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public List<NhanVien> selectBySql(String sql, Object... args) {
        List<NhanVien> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(sql, args);
            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getInt("MaNV"));
                nv.setTenNV(rs.getString("TenNV"));
                nv.setSdt(rs.getString("SDT"));
                nv.setChucVu(rs.getString("ChucVu"));
                nv.setLuong(rs.getDouble("Luong"));
                nv.setSoNgayLam(rs.getInt("SoNgayLam"));
                nv.setSoNgayNghi(rs.getInt("SoNgayNghi"));
                nv.setTenDangNhap(rs.getString("TenDangNhap"));
                list.add(nv);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return list;
    }

    // ✅ Tìm kiếm theo từ khóa (mã hoặc tên)
    public List<NhanVien> findByKeyword(String keyword) {
        try {
            int maNV = Integer.parseInt(keyword);
            return selectBySql(
                "SELECT * FROM NHANVIEN WHERE MaNV = ? OR TenNV LIKE ?",
                maNV, "%" + keyword + "%"
            );
        } catch (NumberFormatException e) {
            return selectBySql(
                "SELECT * FROM NHANVIEN WHERE TenNV LIKE ?",
                "%" + keyword + "%"
            );
        }
    }
    public NhanVien findByTenDangNhap(String tenDangNhap) {
    String sql = "SELECT * FROM NHANVIEN WHERE TenDangNhap = ?";
    List<NhanVien> list = selectBySql(sql, tenDangNhap);
    return list.isEmpty() ? null : list.get(0);
}

    @Override
     public NhanVien findNhanVienByTenDangNhap(String tenDangNhap) {
        String sql = "SELECT * FROM NHANVIEN WHERE TenDangNhap = ?";
        return XQuery.getSingleBean(NhanVien.class, sql, tenDangNhap);
    }
public String getTenNVFromMaNV(int maNV) {
    String sql = "SELECT TenNV FROM NHANVIEN WHERE MaNV = ?";
    try (
        Connection conn = XJdbc.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)
    ) {
        ps.setInt(1, maNV);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("TenNV");  // Lấy tên nhân viên từ bảng NHANVIEN
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;  // Trả về null nếu không tìm thấy nhân viên
}

  

}
