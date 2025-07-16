package dao.impl;

import dao.NhanVienDAO;
import entity.NhanVien;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import util.XJdbc;
import util.XQuery;

public class NhanVienDAOImpl implements NhanVienDAO {
    final String INSERT_SQL = "INSERT INTO NHANVIEN (TenNV, SDT, ChucVu, Luong, SoNgayLam, SoNgayNghi) VALUES (?, ?, ?, ?, ?,  ?)";
    final String UPDATE_SQL = "UPDATE NHANVIEN SET TenNV = ?, SDT = ?, ChucVu = ?, Luong = ?, SoNgayLam = ?, SoNgayNghi = ?,  WHERE MaNV = ?";
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
            nv.getSoNgayNghi()
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
            nv.getMaNV()
        );
    }

@Override
public void deleteById(Integer maNV) {
    try (Connection conn = XJdbc.getConnection()) {
        conn.setAutoCommit(false); // Bắt đầu transaction
        try (PreparedStatement ps1 = conn.prepareStatement("DELETE FROM CHAMCONG WHERE MaNV = ?")) {
            ps1.setInt(1, maNV);
            ps1.executeUpdate();
        }

        try (PreparedStatement ps2 = conn.prepareStatement("DELETE FROM TAIKHOAN WHERE MaNV = ?")) {
            ps2.setInt(1, maNV);
            ps2.executeUpdate();
        }

        try (PreparedStatement ps3 = conn.prepareStatement("DELETE FROM NHANVIEN WHERE MaNV = ?")) {
            ps3.setInt(1, maNV);
            int result = ps3.executeUpdate();

            if (result == 0) {
                conn.rollback();
                throw new RuntimeException("Không tìm thấy nhân viên để xóa.");
            }

            conn.commit();
            System.out.println(">> Đã xóa nhân viên và các dữ liệu liên quan: MaNV = " + maNV);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Xóa nhân viên thất bại! Lỗi: " + e.getMessage());
    }
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
@Override
public List<Object[]> findAllWithUsername() {
    String sql = "SELECT NV.MaNV, NV.TenNV, NV.SDT, NV.ChucVu, NV.Luong, TK.TENDANGNHAP " +
                 "FROM NHANVIEN NV LEFT JOIN TAIKHOAN TK ON NV.MaNV = TK.MaNV";
    List<Object[]> list = new ArrayList<>();

    try (ResultSet rs = XJdbc.executeQuery(sql)) {
        while (rs.next()) {
            Object[] row = {
                rs.getInt("MaNV"),
                rs.getString("TenNV"),
                rs.getString("SDT"),
                rs.getString("ChucVu"),
                String.format("%,.0f", rs.getDouble("Luong")),
                rs.getString("TENDANGNHAP") // Có thể null nếu chưa có tài khoản
            };
            list.add(row);
        }
        rs.getStatement().getConnection().close();
    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
    }
    return list;
}
@Override
public int insertAndReturnId(NhanVien nv) {
    String sql = "INSERT INTO NHANVIEN (TenNV, SDT, ChucVu, Luong, SoNgayLam, SoNgayNghi) VALUES (?, ?, ?, ?, 0, 0)";
    try (
        Connection conn = XJdbc.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
    ) {
        ps.setString(1, nv.getTenNV());
        ps.setString(2, nv.getSdt());
        ps.setString(3, nv.getChucVu());
        ps.setDouble(4, nv.getLuong());

        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1); // Trả về mã nhân viên mới được tạo
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return -1;
}

  

}
