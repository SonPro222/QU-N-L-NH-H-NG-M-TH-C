/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import lombok.Data;
import java.util.Date;

@Data
public class ChamCong {
    private int maChamCong;
    private int maNV;
    private Date ngayCham;
    private boolean coMat;
    private String ghiChu;
}
