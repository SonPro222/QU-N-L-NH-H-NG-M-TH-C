/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;



import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class XJdbc {
    private static Connection connection;
    

  private static final  String URL = "jdbc:sqlserver://localhost:1433;databaseName=QUANLYNHAHANGAMTHUCVIETNAM;encrypt=true;trustServerCertificate=true;characterEncoding=UTF-8";

          //String URL = "jdbc:sqlserver://localhost:1433;databaseName=QUANLYNHAHANGAMTHUCVIETNAM;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "sa";
    private static final String PASSWORD = "123456";
    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // Driver SQL Server
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static Connection openConnection() {

        var driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        try {
            if (!XJdbc.isReady()) {
                Class.forName(driver);
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
    public static void closeConnection() {
        try {
            if (XJdbc.isReady()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static PreparedStatement prepareStatement(String sql, Object... args) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            stmt.setObject(i + 1, args[i]);
        }
        return stmt;
    }
    public static Object value(String sql, Object... args) {
        try (
                Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getObject(1);
            }
            rs.close();
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thực hiện truy vấn giá trị đơn: " + e.getMessage(), e);
        }
    }
    public static boolean isReady() {
        try {
            return (connection != null && !connection.isClosed());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int executeUpdate(String sql, Object... args) {

        try {
            PreparedStatement stmt = XJdbc.prepareStatement(sql, args);

            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // In ra lỗi thật
            throw new RuntimeException("Lỗi khi executeUpdate: " + e.getMessage(), e);
        }
    }

    public static int update(String sql, Object... args) {
        try (
                Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            return ps.executeUpdate(); // trả về số dòng ảnh hưởng
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi truy vấn: " + e.getMessage(), e);
        }
    }

    public static ResultSet executeQuery(String sql, Object... values) {
        try {
            var stmt = XJdbc.getStmt(sql, values);
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("LỖI SQL: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static <T> T getValue(String sql, Object... values) {
        try {
            var resultSet = XJdbc.executeQuery(sql, values);
            if (resultSet.next()) {
                return (T) resultSet.getObject(1);
            }
            return null;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T getSingleBean(Class<T> type, String sql, Object... args) {
        List<T> list = getBeanList(type, sql, args);
        return list.isEmpty() ? null : list.get(0);
    }

    public static <T> List<T> getBeanList(Class<T> type, String sql, Object... args) {
        List<T> list = new ArrayList<>();
        try (
                ResultSet rs = executeQuery(sql, args)) {
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
            while (rs.next()) {
                T bean = type.getDeclaredConstructor().newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = meta.getColumnLabel(i);
                    Object value = rs.getObject(i);
                    Field field;
                    try {
                        field = type.getDeclaredField(columnName);
                    } catch (NoSuchFieldException e) {
                        continue; // Không có field tương ứng thì bỏ qua
                    }
                    field.setAccessible(true);
                    field.set(bean, value);
                }
                list.add(bean);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace(); // In ra lỗi thật
            throw new RuntimeException("Lỗi khi executeUpdate: " + e.getMessage(), e);
        }

    }

    private static PreparedStatement getStmt(String sql, Object... values) throws SQLException {
        var conn = XJdbc.openConnection();
        var stmt = sql.trim().startsWith("{") ? conn.prepareCall(sql) : conn.prepareStatement(sql);
        for (int i = 0; i < values.length; i++) {
            stmt.setObject(i + 1, values[i]);
        }
        System.out.println("️ Đang chuẩn bị statement: " + sql);

        return stmt;
    }

    public static void main(String[] args) {
        demo1();
        demo2();
        demo3();
    }

    private static void demo1() {
        String sql = "SELECT * FROM Drinks WHERE UnitPrice BETWEEN ? AND ?";
        var rs = XJdbc.executeQuery(sql, 1.5, 5.0);
    }

    private static void demo2() {
        String sql = "SELECT max(UnitPrice) FROM Drinks WHERE UnitPrice > ?";
        var maxPrice = XJdbc.getValue(sql, 1.5);
    }

    private static void demo3() {
        String sql = "DELETE FROM Drinks WHERE UnitPrice < ?";
        var count = XJdbc.executeUpdate(sql, 0.0);
    }

    public static ResultSet query(String sql, Object... args) {
    try {
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            ps.setObject(i + 1, args[i]);
        }
        return ps.executeQuery();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

    public static long executeInsertAndReturnId(String sql, Object... args) {
        try (
                Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            return -1;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi insert có trả về ID: " + e.getMessage(), e);
        }
    }}
