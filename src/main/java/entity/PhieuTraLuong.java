/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.Date;

import lombok.Data;

@Data
public class PhieuTraLuong {
    private int maPhieuLuong;
    private int maNV;
    private String tenNV;
    private Date ngayThanhToan;
    private double tongLuong;
    private double luongTru;
    private String ghiChu;
}
