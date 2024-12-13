/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Ui;

import DAO.HoaDonDAO;
import DAO.KhachHangDAO;
import Entity.ChucVu;
import Entity.HoaDon;
import Entity.KhachHang;
import Entity.NhanVien;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import utils.Auth;
import utils.MainThongBao;

/**
 *
 * @author admin
 */
public class QLyKhachHang extends javax.swing.JPanel {

    /**
     * Creates new form QuanLyKhachHang
     */
    public QLyKhachHang() {
        initComponents();
        fillToTable();

    }
    HoaDonDAO hdDAO = new HoaDonDAO();
    KhachHangDAO khDAO = new KhachHangDAO();
    int diem;

    public void fillToTable() {
        DefaultTableModel model = (DefaultTableModel) tblKhachHang.getModel();
        model.setRowCount(0);
        List<KhachHang> list = khDAO.selectAll();
        for (KhachHang kh : list) {
            List<Object[]> hdObject = hdDAO.selectByMaKH(kh.getMaKH());
            int soDonHang = 0;
            for (Object[] objects : hdObject) {
                soDonHang = (int) objects[0];
            }
            model.addRow(new Object[]{
                kh.getMaKH(), kh.getTenKH(), kh.getSdt(), kh.getTichDiem(), soDonHang});
        }

    }

    public boolean checkDelete() {
        int i = tblKhachHang.getSelectedRow();
        boolean check = true;
        List<KhachHang> nh = khDAO.selectAll();
        DefaultTableModel model = (DefaultTableModel) tblKhachHang.getModel();
        int soHoaDon = (int) model.getValueAt(i, 4);
        if (soHoaDon > 0) {
            JOptionPane.showMessageDialog(this, "Không thể xóa khách hàng này!");
            check = false;
        }
        return check;
    }

    public void delete() {
        int i = tblKhachHang.getSelectedRow();
        if (i < 0) {
            return;
        }
        DefaultTableModel model = (DefaultTableModel) tblKhachHang.getModel();
        if (MainThongBao.confirm(this, "Bạn có thật sự muốn xóa nhân viên này?")) {
            try {
                int maKH = (int) model.getValueAt(i, 0);
                khDAO.delete(maKH);
                this.fillToTable();
                MainThongBao.alert(this, "Xóa thành công!");
            } catch (Exception e) {
                e.printStackTrace();
                MainThongBao.alert(this, "Xóa thất bại!");
            }
        }
    }

    boolean checkValid() {
        boolean check = true;
        int i = tblKhachHang.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) tblKhachHang.getModel();
        String tenKH = (String) model.getValueAt(i, 1);
        String sdt = (String) model.getValueAt(i, 2);
        int maKH = (int) model.getValueAt(i, 0);
        if (tenKH.isBlank()) {
            MainThongBao.alert(this, "Tên khách hàng không được để trống!");
            check = false;
        } else {
            String pattenName = "^[^!-@]+$";
            if (!tenKH.matches(pattenName)) {
                MainThongBao.alert(this, "Sai định dạng tên!");
                check = false;

            }
        }
        if (sdt.isBlank()) {
            MainThongBao.alert(this, "Số điện thoại khách hàng không được để trống!");
            check = false;
        } else {
            String pattern = "^(032|033|034|035|036|037|038|039|096|097|098|086|083|084|085|081|082|088|091|094|070|079|077|076|078|090|093|089|056|058|092|059|099)[0-9]{7}$";
            if (!sdt.matches(pattern)) {
                MainThongBao.alert(this, "Số điện thoại không đúng định dạng!");
                check = false;
            }
        }
        return check;
    }

    void update() {
        int i = tblKhachHang.getSelectedRow();
        if (i < 0) {
            return;
        }
        DefaultTableModel model = (DefaultTableModel) tblKhachHang.getModel();
        String tenKH = (String) model.getValueAt(i, 1);
        char[] charArray = tenKH.toCharArray();
        boolean foundSpace = true;
        for (int j = 0; j < charArray.length; j++) {
            if (Character.isLetter(charArray[j])) {
                if (foundSpace) {
                    charArray[j] = Character.toUpperCase(charArray[j]);
                    foundSpace = false;
                }
            } else {
                foundSpace = true;
            }
        }
        tenKH = String.valueOf(charArray);
        String sdt = (String) model.getValueAt(i, 2);
        int maKH = (int) model.getValueAt(i, 0);
        KhachHang kh = khDAO.selectById(maKH);
        kh.setTenKH(tenKH);
        kh.setSdt(sdt);
        khDAO.update(kh);
        MainThongBao.alert(this, "Cập nhật thành công!");

        this.fillToTable();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btnCapNhat = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKhachHang = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 255));
        jLabel1.setText("Quản lý khách hàng");

        btnCapNhat.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCapNhat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/update.png"))); // NOI18N
        btnCapNhat.setText("Cập nhật");
        btnCapNhat.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatActionPerformed(evt);
            }
        });

        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/delete.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        tblKhachHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã Khách Hàng", "Tên Khách Hàng", "Số Điện Thoại", "Điểm Tích Lũy", "Tổng Đơn Hàng Đã Mua"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKhachHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKhachHangMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tblKhachHangMouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(tblKhachHang);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 934, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(31, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 877, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(26, 26, 26)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 492, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(20, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(33, 33, 33)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 944, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
        // TODO add your handling code here:
        if (Auth.isManager()) {
            if (checkValid()) {
                update();
            }
        } else {
            MainThongBao.alert(jPanel1, "Bạn không có quyền cập nhật thông tin khách hàng!");
        }
    }//GEN-LAST:event_btnCapNhatActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        if (Auth.isManager()) {
            if (checkDelete()) {
                delete();
            }
        } else {
            MainThongBao.alert(jPanel1, "Bạn không có quyền xóa khách hàng!");
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void tblKhachHangMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKhachHangMouseEntered

    }//GEN-LAST:event_tblKhachHangMouseEntered

    private void tblKhachHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKhachHangMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_tblKhachHangMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnXoa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblKhachHang;
    // End of variables declaration//GEN-END:variables
}
