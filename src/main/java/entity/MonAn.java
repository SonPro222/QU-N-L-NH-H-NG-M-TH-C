package entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonAn {
    private int maMonAn;
    private String tenMonAn;
    private String hinhAnh; // chỉ lưu tên file
}
