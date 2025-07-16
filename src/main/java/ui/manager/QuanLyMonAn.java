                                                                                                    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.manager;

import dao.ChiTietMonAnDAO;
import dao.impl.ChiTietMonAnDAOImpl;
import dao.impl.MonAnDAOImpl;
import entity.ChiTietMonAn;
import entity.MonAn;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import static java.nio.file.Files.list;
import java.nio.file.StandardCopyOption;
import static java.rmi.Naming.list;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static java.util.Collections.list;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import pro.RoundedPanel;
import util.XJdbc;

/**
 *
 * @author
 */
public class QuanLyMonAn extends javax.swing.JDialog {

    public QuanLyMonAn(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setTitle("Quản Lý Món An");
        setResizable(false);
        setLocationRelativeTo(null);
        fillToTable();
        fillChiTietMonAnTheoMonAn(ABORT);
        tblBangLoaiMon.setFont(new Font("Arial", Font.BOLD, 20));
        tblBangChiTietMon.setFont(new Font("Arial", Font.BOLD, 15));
        tblBangLoaiMon.setShowHorizontalLines(false); // Ẩn đường ngang
        tblBangLoaiMon.setShowVerticalLines(false);   // Ẩn đường dọc
        tblBangChiTietMon.setShowVerticalLines(false);   // Ẩn đường dọc
        tblBangChiTietMon.setShowHorizontalLines(false); // Ẩn đường ngang
        fillComboBoxLoaiMon();


    }
    //-------Thêm Hình Anh Sắc Nét-----------------
    public ImageIcon resizeImageIcon(URL imageUrl, int width, int height) {
    ImageIcon icon = new ImageIcon(imageUrl);
    Image image = icon.getImage();
    Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    return new ImageIcon(scaledImage);
}


public void fillToTable() {
    DefaultTableModel model = (DefaultTableModel) tblBangLoaiMon.getModel();
    model.setRowCount(0);  // Xóa các dòng hiện tại trong bảng

    MonAnDAOImpl dao = new MonAnDAOImpl();
    List<MonAn> monAnList = dao.findAll();
    
     //-------Thêm Hình Anh Sắc Nét-----------------
for (MonAn monAn : monAnList) {
    ImageIcon icon = null;

    try {
        if (monAn.getHinhAnh() != null && !monAn.getHinhAnh().isEmpty()) {
            URL imgURL = getClass().getResource("/images/" + monAn.getHinhAnh());
            if (imgURL != null) {
                icon = resizeImageIcon(imgURL, 100, 100); // dùng hàm đã viết
            } else {
                System.out.println("Không tìm thấy ảnh: " + monAn.getHinhAnh());
            }
        }
    } catch (Exception e) {
        System.out.println("Lỗi load ảnh: " + e.getMessage());
    }

    model.addRow(new Object[]{icon, monAn.getTenMonAn()});
}
   

    // Căn giữa ảnh (cột 0)
    tblBangLoaiMon.getColumnModel().getColumn(0).setPreferredWidth(110);
    tblBangLoaiMon.setRowHeight(110);
    tblBangLoaiMon.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);
            setFont(new Font("Arial", Font.PLAIN, 20));

            if (value instanceof ImageIcon) {
                setIcon((ImageIcon) value);
                setText("");
            } else {
                setIcon(null);
                setText("Không có ảnh");
            }

            return this;
        }
    });

    // Căn giữa tên món ăn (cột số 1)
    DefaultTableCellRenderer centerTextRenderer = new DefaultTableCellRenderer();
    centerTextRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    centerTextRenderer.setVerticalAlignment(SwingConstants.CENTER);
    centerTextRenderer.setFont(new Font("Arial", Font.PLAIN, 20));
    tblBangLoaiMon.getColumnModel().getColumn(1).setCellRenderer(centerTextRenderer);

    // Bắt sự kiện click dòng → load chi tiết món ăn
    tblBangLoaiMon.addMouseListener(new MouseAdapter() {
        @Override
     public void mouseClicked(MouseEvent e) {
    int row = tblBangLoaiMon.getSelectedRow();
    if (row >= 0 && row < monAnList.size()) {
        int maMonAn = monAnList.get(row).getMaMonAn();
        fillChiTietMonAnTheoMonAn(maMonAn);
    } else {
        System.out.println("Row out of bounds: " + row + ", monAnList size: " + monAnList.size());
    }
}

    });
}
 
private void fillChiTietMonAnTheoMonAn(int maMonAn) {
    DefaultTableModel model = (DefaultTableModel) tblBangChiTietMon.getModel();
    model.setRowCount(0); // Xóa dữ liệu cũ

    ChiTietMonAnDAO dao = new ChiTietMonAnDAOImpl();
    List<ChiTietMonAn> list = dao.findByMonAnId(maMonAn);

    for (ChiTietMonAn ct : list) {
        ImageIcon icon = null;

        try {
            if (ct.getHinhAnh() != null && !ct.getHinhAnh().isEmpty()) {
                // Đọc ảnh từ resources/images/
                URL imgURL = getClass().getResource("/images/" + ct.getHinhAnh());
                if (imgURL != null) {
                    icon = new ImageIcon(new ImageIcon(imgURL)
                            .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi load ảnh: " + e.getMessage());
        }

        Object[] row = {
            icon,
            ct.getTenMon(),
            ct.getGia(),
            ct.getTenLoaiMon()
        };
        model.addRow(row);
    }

    tblBangChiTietMon.setRowHeight(100);
    tblBangChiTietMon.getColumnModel().getColumn(0).setPreferredWidth(100);

    // Renderer hiển thị ảnh
    tblBangChiTietMon.getColumnModel().getColumn(0).setCellRenderer(new TableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            JLabel lbl = new JLabel();
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setVerticalAlignment(SwingConstants.CENTER);
            if (value instanceof ImageIcon) {
                lbl.setIcon((ImageIcon) value);
            }
            return lbl;
        }
    });

    // Renderer căn giữa các cột còn lại
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    for (int i = 1; i < tblBangChiTietMon.getColumnCount(); i++) {
        tblBangChiTietMon.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
}


    private String tenAnh = null;
    private String duongDanAnh = null;
    private String duongDanAnhLoai = null;
    private String tenAnhLoai = null;

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBangChiTietMon = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblBangLoaiMon = new javax.swing.JTable();
        btnXoaMonAn = new javax.swing.JButton();
        btnXoaLoaiMon = new javax.swing.JButton();
        btnChange = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btnMoDuongDan = new javax.swing.JButton();
        lblHinh = new javax.swing.JLabel();
        txtTen = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtPrice = new javax.swing.JTextField();
        btnThemMoi = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        cboLoai = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblHinhLoai = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtTenLoai = new javax.swing.JTextField();
        btnMoDuongDanLoai = new javax.swing.JButton();
        btnThemMoiLoai = new javax.swing.JButton();
        btnClearLoai = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        jLabel4.setText("jLabel2");

        jTextField5.setText("jTextField3");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Quản Lý Món Ăn- Đồ Uống");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(153, 153, 153));

        tblBangChiTietMon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Hình Ảnh", "Tên Món Ăn", "Đơn Giá VND"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblBangChiTietMon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBangChiTietMonMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblBangChiTietMon);
        if (tblBangChiTietMon.getColumnModel().getColumnCount() > 0) {
            tblBangChiTietMon.getColumnModel().getColumn(0).setMinWidth(130);
            tblBangChiTietMon.getColumnModel().getColumn(0).setMaxWidth(150);
            tblBangChiTietMon.getColumnModel().getColumn(1).setMinWidth(230);
            tblBangChiTietMon.getColumnModel().getColumn(1).setMaxWidth(230);
        }

        tblBangLoaiMon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Hình Ảnh", "Loại Món Ăn"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblBangLoaiMon);
        if (tblBangLoaiMon.getColumnModel().getColumnCount() > 0) {
            tblBangLoaiMon.getColumnModel().getColumn(0).setMinWidth(130);
            tblBangLoaiMon.getColumnModel().getColumn(0).setMaxWidth(200);
        }

        btnXoaMonAn.setText("Xóa Món Ăn");
        btnXoaMonAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaMonAnActionPerformed(evt);
            }
        });

        btnXoaLoaiMon.setText("Xóa Loại Món");
        btnXoaLoaiMon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaLoaiMonActionPerformed(evt);
            }
        });

        btnChange.setText("Sửa Giá");
        btnChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnXoaLoaiMon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(btnXoaMonAn)
                        .addGap(18, 18, 18)
                        .addComponent(btnChange)
                        .addGap(13, 13, 13))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 582, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnXoaLoaiMon)
                    .addComponent(btnXoaMonAn)
                    .addComponent(btnChange))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(" Món Ăn - Đồ Uống", jPanel4);

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(911, 229, -1, 26));

        btnMoDuongDan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnMoDuongDan.setText("Chọn Đường Dẫn Ảnh");
        btnMoDuongDan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoDuongDanActionPerformed(evt);
            }
        });
        jPanel3.add(btnMoDuongDan, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 200, -1, -1));

        lblHinh.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.add(lblHinh, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 40, 120, 150));

        txtTen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenActionPerformed(evt);
            }
        });
        jPanel3.add(txtTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 40, 270, 28));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Loại:");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 70, -1, 26));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Đơn Giá:");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 130, -1, 26));

        txtPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPriceActionPerformed(evt);
            }
        });
        jPanel3.add(txtPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 160, 270, 28));

        btnThemMoi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThemMoi.setText("Thêm Mới");
        btnThemMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemMoiActionPerformed(evt);
            }
        });
        jPanel3.add(btnThemMoi, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 200, -1, 39));

        btnClear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnClear.setText("Làm Mới");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        jPanel3.add(btnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 200, 105, 39));

        cboLoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLoaiActionPerformed(evt);
            }
        });
        jPanel3.add(cboLoai, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 100, 270, 30));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Hình Ảnh");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 10, -1, 26));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Tên Món Ăn/Đồ Uống:");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 10, -1, 26));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(199, 119, -1, 26));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Hình Ảnh");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, 26));

        lblHinhLoai.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.add(lblHinhLoai, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 120, 150));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("Tên Loại:");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, -1, 26));

        txtTenLoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenLoaiActionPerformed(evt);
            }
        });
        jPanel3.add(txtTenLoai, new org.netbeans.lib.awtextra.AbsoluteConstraints(171, 46, 220, 28));

        btnMoDuongDanLoai.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnMoDuongDanLoai.setText("Chọn Đường Dẫn Ảnh");
        btnMoDuongDanLoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoDuongDanLoaiActionPerformed(evt);
            }
        });
        jPanel3.add(btnMoDuongDanLoai, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 160, -1));

        btnThemMoiLoai.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThemMoiLoai.setText("Thêm Mới");
        btnThemMoiLoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemMoiLoaiActionPerformed(evt);
            }
        });
        jPanel3.add(btnThemMoiLoai, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 90, -1, 39));

        btnClearLoai.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnClearLoai.setText("Làm Mới");
        btnClearLoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearLoaiActionPerformed(evt);
            }
        });
        jPanel3.add(btnClearLoai, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 90, 85, 39));

        jTabbedPane1.addTab("Thêm - Sửa Món Ăn", jPanel3);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 920, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 920, 500));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/hinhnen.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 560));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnXoaMonAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaMonAnActionPerformed
        // TODO add your handling code here:
        btnXoaMonAn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblBangChiTietMon.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn món ăn để xóa.");
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Bạn có chắc muốn xóa món ăn này?",
                        "Xác nhận xóa",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }

                try {
                    // Lấy Tên món từ bảng (cột 1 là Tên Món Ăn)
                    String tenMon = tblBangChiTietMon.getValueAt(selectedRow, 1).toString();

                    // Xóa món ăn khỏi CSDL
                    String sql = "DELETE FROM ChiTietMonAn WHERE TenMon = ?";
                    int rows = XJdbc.update(sql, tenMon);

                    if (rows > 0) {
                        JOptionPane.showMessageDialog(null, "✅ Xóa món ăn thành công!");
                        loadBangMonAn(); // Load lại bảng sau khi xóa
                    } else {
                        JOptionPane.showMessageDialog(null, "❌ Không thể xóa món ăn. Có thể không tồn tại trong CSDL.");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "❌ Lỗi khi xóa món ăn: " + ex.getMessage());
                }
                fillChiTietMonAnTheoMonAn(ABORT);
            }
        });

    }//GEN-LAST:event_btnXoaMonAnActionPerformed

    private void btnChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeActionPerformed
        // TODO add your handling code here:
       btnChange.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedRow = tblBangChiTietMon.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn món ăn để sửa giá.");
            return;
        }

        // Lấy tên món từ bảng (cột 1 là tên món)
        String tenMon = tblBangChiTietMon.getValueAt(selectedRow, 1).toString();

        // Tìm chi tiết món theo tên (qua DAO)
        ChiTietMonAnDAO dao = new ChiTietMonAnDAOImpl();
        ChiTietMonAn mon = dao.findByTenMon(tenMon);

        if (mon == null) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy món ăn trong CSDL.");
            return;
        }

        // Nhập giá mới
        String giaStr = JOptionPane.showInputDialog(null, "Nhập giá mới cho món: " + tenMon, "Sửa giá", JOptionPane.QUESTION_MESSAGE);
        if (giaStr == null || giaStr.trim().isEmpty()) return;

        try {
            double giaMoi = Double.parseDouble(giaStr.trim());
            if (giaMoi <= 0) {
                JOptionPane.showMessageDialog(null, "Giá phải lớn hơn 0.");
                return;
            }

            // Cập nhật giá mới
            mon.setGia(giaMoi);
            dao.update(mon);  // ✅ dùng DAO update

            JOptionPane.showMessageDialog(null, "✅ Đã cập nhật giá món ăn!");
            loadBangMonAnTheoLoai(); // Cập nhật lại bảng

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Giá phải là số hợp lệ.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "❌ Lỗi khi cập nhật: " + ex.getMessage());
        }
    }
});


    }//GEN-LAST:event_btnChangeActionPerformed

    private void btnMoDuongDanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoDuongDanActionPerformed
        ActionListener[] listeners = btnMoDuongDan.getActionListeners();
        for (ActionListener listener : listeners) {
            btnMoDuongDan.removeActionListener(listener);
        }//TODO add your handling code here:
        btnMoDuongDan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Chọn hình ảnh");

                // Chỉ chọn file ảnh
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Hình ảnh", "jpg", "jpeg", "png", "gif");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    tenAnh = selectedFile.getName();
                    duongDanAnh = selectedFile.getAbsolutePath();

                    // Hiển thị ảnh lên giao diện
                    ImageIcon icon = new ImageIcon(duongDanAnh);
                    Image img = icon.getImage().getScaledInstance(lblHinh.getWidth(), lblHinh.getHeight(), Image.SCALE_SMOOTH);
                    lblHinh.setIcon(new ImageIcon(img));
                }
            }
        });

    }//GEN-LAST:event_btnMoDuongDanActionPerformed
   ChiTietMonAnDAO monAnDAO = new ChiTietMonAnDAOImpl();

    private void loadBangMonAn() {
    DefaultTableModel model = (DefaultTableModel) tblBangChiTietMon.getModel();
    model.setRowCount(0);

    try {
        List<ChiTietMonAn> list = monAnDAO.findAll(); // ✅ gọi DAO thay vì tự viết SQL

        for (ChiTietMonAn mon : list) {
            ImageIcon icon = null;
            File imgFile = new File("images/" + mon.getHinhAnh());
            if (imgFile.exists()) {
                Image img = new ImageIcon(imgFile.getAbsolutePath())
                        .getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                icon = new ImageIcon(img);
            }

            Object[] row = {
                icon != null ? icon : "Không có ảnh",
                mon.getTenMon(),
                mon.getGia()
            };

            model.addRow(row);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "❌ Lỗi khi tải dữ liệu món ăn: " + e.getMessage());
    }
}



    private void txtTenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenActionPerformed

    private void txtPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPriceActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Xóa nội dung các ô nhập
                txtTen.setText("");
                txtPrice.setText("");

                // Reset combobox về mục đầu tiên
                cboLoai.setSelectedIndex(0);

                // Xóa hình ảnh hiển thị
                lblHinh.setIcon(null);

                // Xóa tên ảnh và đường dẫn ảnh tạm đang lưu
                tenAnh = null;
                duongDanAnh = null;

                // Thông báo nếu cần
                JOptionPane.showMessageDialog(null, "Đã làm mới toàn bộ thông tin.", "Làm mới", JOptionPane.INFORMATION_MESSAGE);
            }
        });

    }//GEN-LAST:event_btnClearActionPerformed

    private void btnThemMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemMoiActionPerformed
        // TODO add your handling code here:
        btnThemMoi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Lấy dữ liệu từ form
                    String tenMon = txtTen.getText().trim();
                    String giaStr = txtPrice.getText().trim();

                    // Giả sử combobox chứa tên loại món tương ứng MaMonAn (1, 2, 3,...)
                    int maMonAn = cboLoai.getSelectedIndex() + 1;

                    // Kiểm tra dữ liệu đầu vào
                    if (tenMon.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Vui lòng nhập tên món.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    if (giaStr.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Vui lòng nhập giá món.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    if (tenAnh == null || duongDanAnh == null) {
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn hình ảnh cho món ăn.", "Thiếu hình ảnh", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    double gia;
                    try {
                        gia = Double.parseDouble(giaStr);
                        if (gia <= 0) {
                            JOptionPane.showMessageDialog(null, "Giá món ăn phải lớn hơn 0.", "Giá không hợp lệ", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Giá món ăn phải là số hợp lệ.", "Lỗi định dạng", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // Tạo thư mục nếu chưa có
                    File folder = new File("images");
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }

                    // Sao chép ảnh vào thư mục images
                    try {
                        File source = new File(duongDanAnh);
                        File dest = new File("images/" + tenAnh);
                        Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Không thể sao chép hình ảnh: " + ex.getMessage(), "Lỗi sao chép ảnh", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Thêm vào CSDL
                    String sql = "INSERT INTO ChiTietMonAn (TenMon, Gia, MaMonAn, HinhAnh) VALUES (?, ?, ?, ?)";
                    int row = XJdbc.update(sql, tenMon, gia, maMonAn, tenAnh);

                    if (row > 0) {
                        JOptionPane.showMessageDialog(null, "✔️ Thêm món ăn thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        // Reset form
                        txtTen.setText("");
                        txtPrice.setText("");
                        lblHinh.setIcon(null);
                        tenAnh = null;
                        duongDanAnh = null;
                    } else {
                        JOptionPane.showMessageDialog(null, "❌ Thêm món ăn thất bại!", "Thất bại", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "⚠️ Đã xảy ra lỗi: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
                }
                fillChiTietMonAnTheoMonAn(ABORT);
            }
        });

    }//GEN-LAST:event_btnThemMoiActionPerformed

    private void txtTenLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenLoaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenLoaiActionPerformed

    private void btnMoDuongDanLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoDuongDanLoaiActionPerformed
        ActionListener[] listeners = btnMoDuongDanLoai.getActionListeners();
        for (ActionListener listener : listeners) {
            btnMoDuongDanLoai.removeActionListener(listener);
        }
        btnMoDuongDanLoai.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Chọn hình ảnh");

                // Chỉ cho phép chọn các file ảnh
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Hình ảnh", "jpg", "jpeg", "png", "gif");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    tenAnhLoai = selectedFile.getName();
                    duongDanAnhLoai = selectedFile.getAbsolutePath();

                    try {
                        ImageIcon icon = new ImageIcon(duongDanAnhLoai);
                        // Nếu label có width và height bằng 0 thì scale sẽ lỗi, nên cần check
                        int width = lblHinhLoai.getWidth() > 0 ? lblHinhLoai.getWidth() : 100;
                        int height = lblHinhLoai.getHeight() > 0 ? lblHinhLoai.getHeight() : 100;

                        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                        lblHinhLoai.setIcon(new ImageIcon(img));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Không thể hiển thị hình ảnh: " + ex.getMessage(), "Lỗi hiển thị", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }//GEN-LAST:event_btnMoDuongDanLoaiActionPerformed

    private void btnThemMoiLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemMoiLoaiActionPerformed
        // TODO add your handling code here:
        btnThemMoiLoai.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String tenLoai = txtTenLoai.getText().trim();

                    if (tenLoai.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Vui lòng nhập tên loại món.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    if (tenAnhLoai == null || duongDanAnhLoai == null) {
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn hình ảnh cho loại món ăn.", "Thiếu hình ảnh", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // Tạo thư mục ảnh nếu chưa có
                    File folder = new File("images");
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }

                    // Sao chép ảnh vào thư mục
                    File source = new File(duongDanAnhLoai);
                    File dest = new File("images/" + tenAnhLoai);
                    Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    // Thêm vào CSDL bảng MonAn
                    String sql = "INSERT INTO MonAn (TenMonAn, HinhAnh) VALUES (?, ?)";
                    int row = XJdbc.update(sql, tenLoai, tenAnhLoai);

                    if (row > 0) {
                        JOptionPane.showMessageDialog(null, "✔️ Thêm loại món ăn thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

                        // Reset form
                        txtTenLoai.setText("");
                        lblHinhLoai.setIcon(null);
                        tenAnhLoai = null;
                        duongDanAnhLoai = null;

                        // Tải lại bảng loại món ăn
                        loadBangMonAnTheoLoai();

                        // Cập nhật lại combobox bên phần Món Ăn - Đồ Uống
                        loadComboBoxLoai();

                    } else {
                        JOptionPane.showMessageDialog(null, "❌ Thêm loại món ăn thất bại!", "Thất bại", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "⚠️ Đã xảy ra lỗi: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
                }
                fillToTable();
            }
        });

    }//GEN-LAST:event_btnThemMoiLoaiActionPerformed

    private void btnClearLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearLoaiActionPerformed
        // TODO add your handling code here:
        btnClearLoai.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Xóa nội dung các ô nhập
                txtTenLoai.setText("");

                // Xóa hình ảnh hiển thị
                lblHinhLoai.setIcon(null);

                // Xóa tên ảnh và đường dẫn ảnh tạm đang lưu
                tenAnhLoai = null;
                duongDanAnhLoai = null;

                // Thông báo nếu cần
                JOptionPane.showMessageDialog(null, "Đã làm mới toàn bộ thông tin.", "Làm mới", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }//GEN-LAST:event_btnClearLoaiActionPerformed

    private void btnXoaLoaiMonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaLoaiMonActionPerformed
        btnXoaLoaiMon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblBangLoaiMon.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn loại món ăn để xóa.");
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xóa loại món ăn này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }

                try {
                    // Lấy tên món ăn từ bảng (cột 1 vì cột 0 là ảnh)
                    String tenMonAn = tblBangLoaiMon.getValueAt(selectedRow, 1).toString();

                    // Xóa ChiTietMonAn trước để tránh lỗi ràng buộc
                    String sql1 = "DELETE FROM ChiTietMonAn WHERE MaMonAn IN (SELECT MaMonAn FROM MonAn WHERE TenMonAn = ?)";
                    XJdbc.update(sql1, tenMonAn);

                    // Sau đó xóa MonAn
                    String sql2 = "DELETE FROM MonAn WHERE TenMonAn = ?";
                    int rowsAffected = XJdbc.update(sql2, tenMonAn);

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "✅ Xóa thành công!");
                        loadBangMonAnTheoLoai();
                    } else {
                        JOptionPane.showMessageDialog(null, "❌ Không tìm thấy hoặc không thể xóa loại món ăn này.");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "❌ Lỗi khi xóa loại món ăn: " + ex.getMessage());
                }
                fillToTable();
            }
        });

    }//GEN-LAST:event_btnXoaLoaiMonActionPerformed
private void fillComboBoxLoaiMon() {
    cboLoai.removeAllItems(); // Xóa dữ liệu cũ

    MonAnDAOImpl dao = new MonAnDAOImpl();
    List<MonAn> list = dao.findAll();

    for (MonAn mon : list) {
        cboLoai.addItem(mon.getTenMonAn());
    }
}

    private void tblBangChiTietMonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBangChiTietMonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblBangChiTietMonMouseClicked

    private void cboLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLoaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboLoaiActionPerformed

    private void loadBangMonAnTheoLoai() {
        int loaiIndex = cboLoai.getSelectedIndex(); // Hoặc cách bạn quản lý loại
        if (loaiIndex == -1) {
            return;
        }

        int maMonAn = loaiIndex + 1;

        DefaultTableModel model = (DefaultTableModel) tblBangChiTietMon.getModel();
        model.setRowCount(0);

        try {
            String sql = "SELECT * FROM ChiTietMonAn WHERE MaMonAn = ?";
            ResultSet rs = XJdbc.executeQuery(sql, maMonAn);

            while (rs.next()) {
                String tenMon = rs.getString("TenMon");
                double gia = rs.getDouble("Gia");
                String tenAnh = rs.getString("HinhAnh");

                ImageIcon icon = null;
                File imgFile = new File("images/" + tenAnh);
                if (imgFile.exists()) {
                    Image img = new ImageIcon(imgFile.getAbsolutePath())
                            .getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(img);
                }

                model.addRow(new Object[]{
                    icon != null ? icon : "Không có ảnh",
                    tenMon,
                    gia
                });
            }

            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "❌ Lỗi khi load bảng món ăn theo loại: " + e.getMessage());
        }
    }

    private void loadComboBoxLoai() {
        try {
            cboLoai.removeAllItems(); // Xóa các mục cũ

            String sql = "SELECT TenMonAn FROM MonAn";
            ResultSet rs = XJdbc.executeQuery(sql);
            while (rs.next()) {
                cboLoai.addItem(rs.getString("TenMonAn")); // hoặc MaMonAn nếu cần
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi load combobox loại món ăn: " + e.getMessage());
        }
    }

//    public static void main(String args[]) {
//
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                QuanLyMonAn dialog = new QuanLyMonAn(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChange;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnClearLoai;
    private javax.swing.JButton btnMoDuongDan;
    private javax.swing.JButton btnMoDuongDanLoai;
    private javax.swing.JButton btnThemMoi;
    private javax.swing.JButton btnThemMoiLoai;
    private javax.swing.JButton btnXoaLoaiMon;
    private javax.swing.JButton btnXoaMonAn;
    private javax.swing.JComboBox<String> cboLoai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JLabel lblHinh;
    private javax.swing.JLabel lblHinhLoai;
    private javax.swing.JTable tblBangChiTietMon;
    private javax.swing.JTable tblBangLoaiMon;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtTen;
    private javax.swing.JTextField txtTenLoai;
    // End of variables declaration//GEN-END:variables
}
