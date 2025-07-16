/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author ACER
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ACER
 */
public class JdbcHelper {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:sqlserver://localhost;databaseName=CafePoLy";
        String user = "sa";
        String pass = "123456";
        return DriverManager.getConnection(url, user, pass);
    }
}