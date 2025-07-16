/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uui;

/**
 *
 * @author ACER
 */

import entity.NhanVien;

public class Auth {
    public static NhanVien nhanVienDangNhap = null;

    public static boolean isLogin() {
        return nhanVienDangNhap != null;
    }

    public static void logout() {
        nhanVienDangNhap = null;
    }
}
