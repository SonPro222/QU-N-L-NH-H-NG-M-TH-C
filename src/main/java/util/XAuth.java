/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import entity.User;

/**
 *
 * @author ACER
 */
public class XAuth {
    public static User user = User.builder()
            .tendangnhap(null)
            .build(); // biến user này sẽ được thay thế sau khi đăng nhập
}