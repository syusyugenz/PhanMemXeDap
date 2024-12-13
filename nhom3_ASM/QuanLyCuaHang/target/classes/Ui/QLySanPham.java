/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Ui;

import DAO.HoaDonChiTietDAO;
import DAO.PhieuNhapKhoDAO;
import DAO.SanPhamDAO;
import Entity.HoaDon;
import Entity.HoaDonChiTiet;
import Entity.PhieuNhapKho;
import Entity.SanPham;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Image;
import java.io.File;
import java.net.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import utils.Auth;
import utils.MainThongBao;
import utils.XImage;

/**
 *
 * @author admin
 */
public class QLySanPham extends javax.swing.JPanel {

    /**
     * Creates new form QLySanPham
     */
    public QLySanPham() {
        initComponents();
        fillToTable();
        updateStatus();
        this.i = -1;
    }

    DefaultTableModel model;
    SanPhamDAO dao = new SanPhamDAO();
    PhieuNhapKhoDAO pnkDAO = new PhieuNhapKhoDAO();
    int i = -1;

    public boolean checkExists() {
        boolean check = true;
        List<PhieuNhapKho> pnk = pnkDAO.selectAll();

        for (PhieuNhapKho list : pnk) {
            if (list.getMaSP().equalsIgnoreCase(txtMaSP.getText())) {
                check = false;
                break;
            }
        }

        if (!check) {
            MainThongBao.alert(this, "Sản phẩm vẫn đang được bán!");
        }
        return check;
    }

    public void fillToTable() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setRowCount(0);
        model.setColumnIdentifiers(new Object[]{
            "Mã sản phẩm", "Tên sản phẩm", "Đơn giá", "Phân loại", "Trọng lượng", "Số lượng", "Hình ảnh"
        });
        try {
            String keyWord = txtTimKiem.getText();
            List<SanPham> list = dao.selectByKeyword(keyWord);
            for (SanPham entity : list) {
                String soLuong;
                if (entity.getSoLuong() == 0) {
                    soLuong = "Đã hết hàng";
                } else {
                    soLuong = String.valueOf(entity.getSoLuong());
                }
                model.addRow(new Object[]{
                    entity.getMaSP(), entity.getTenSP(), new DecimalFormat("####0 VNĐ").format(entity.getDonGia()),
                    entity.getPhanLoai(), entity.getTrongLuong(), soLuong, entity.getHinhAnh()
                });
            }
            tblDanhSach.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    public void updateStatus() {
        boolean edit = (this.i >= 0);
        boolean first = (this.i == 0);
        boolean last = (this.i == tblDanhSach.getRowCount() - 1);

        txtMaSP.setEditable(!edit);
        btnThem.setEnabled(!edit);
        btnSua.setEnabled(edit);
        btnXoa.setEnabled(edit);

        btnFirst.setEnabled(edit && !first);
        btnPrev.setEnabled(edit && !first);
        btnLast.setEnabled(edit && !last);
        btnNext.setEnabled(edit && !last);
    }

    public void clearForm() {
        SanPham entity = new SanPham();
        this.setForm(entity);
        this.i = -1;
        this.updateStatus();
        clearErrorMess();
        btnTrongLuong.clearSelection();
        lblHinhAnh.setText("Hình Ảnh");
    }

    public void edit() {
        String maSP = tblDanhSach.getValueAt(this.i, 0).toString();
        SanPham sp = dao.selectById(maSP);
        this.setForm(sp);
        this.updateStatus();
        jTabbedPane1.setSelectedIndex(0);
    }

    public void setForm(SanPham entity) {
        if (entity.getDonGia() != 0) {
            Locale locale = new Locale("vi", "VN");
            String pattern = "####0";
            DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
            decimalFormat.applyPattern(pattern);
            txtDonGia.setText(decimalFormat.format(entity.getDonGia()));
        } else {
            txtDonGia.setText("");
        }
        txtTenSP.setText(entity.getTenSP());
        txtDonViTinh.setText(entity.getDonViTinh());
        txtMaSP.setText(entity.getMaSP());
        if (entity.getDonGia() != 0) {
            String trongLuong[] = entity.getTrongLuong().split("\\s");
            txtTrongLuong.setText(trongLuong[0]);
        } else {
            txtTrongLuong.setText("");
        }
        if (entity.getTrongLuong() == null) {
            btnTrongLuong.clearSelection();
        } else if (entity.getTrongLuong().toLowerCase().contains("kg")) {
            rdoKG.setSelected(true);
        } else if (entity.getTrongLuong().toLowerCase().contains("l")) {
            rdoLit.setSelected(true);
        }
        if (entity.getPhanLoai() == null) {
            cboPhanLoai.setSelectedIndex(0);
        } else {
            cboPhanLoai.setSelectedItem(entity.getPhanLoai());
        }
        if (entity.getHinhAnh() != null) {
            lblHinhAnh.setToolTipText(entity.getHinhAnh());
            Image img = XImage.read(entity.getHinhAnh()).getImage();
            Image img2 = img.getScaledInstance(lblHinhAnh.getWidth(), lblHinhAnh.getHeight(), Image.SCALE_SMOOTH);
            lblHinhAnh.setIcon(new ImageIcon(img2));
            lblHinhAnh.setText("");
        } else {
            lblHinhAnh.setIcon(null);
        }
    }

    public SanPham getForm() {
        SanPham entity = new SanPham();
        SanPham sp = dao.selectById(txtMaSP.getText());
        if (sp != null) {
            entity.setSoLuong(sp.getSoLuong());
        } else {
            entity.setSoLuong(0);
        }
        String maSP = txtMaSP.getText();
        entity.setMaSP(maSP.substring(0, 2).toUpperCase() + maSP.substring(2, maSP.length()));
        String message = txtTenSP.getText();
        char[] charArray = message.toCharArray();
        boolean foundSpace = true;
        for (int i = 0; i < charArray.length; i++) {       
            if (Character.isLetter(charArray[i])) {
                if (foundSpace) {      
                    charArray[i] = Character.toUpperCase(charArray[i]);
                    foundSpace = false;
                }
            } else {
                foundSpace = true;
            }
        }
        message = String.valueOf(charArray);
        entity.setTenSP(message);
        entity.setDonGia(Double.parseDouble(txtDonGia.getText()));
        entity.setDonViTinh(txtDonViTinh.getText().substring(0, 1).toUpperCase() + txtDonViTinh.getText().substring(1, txtDonViTinh.getText().length()));
        entity.setPhanLoai(cboPhanLoai.getSelectedItem().toString());
        if (rdoKG.isSelected()) {
            entity.setTrongLuong((txtTrongLuong.getText() + " " + rdoKG.getText()));
        } else if (rdoLit.isSelected()) {
            entity.setTrongLuong(txtTrongLuong.getText() + " " + rdoLit.getText().toLowerCase().substring(0, 1));
        }
        entity.setHinhAnh(lblHinhAnh.getToolTipText());
        return entity;
    }

    public void chonAnh() {
        if (fileChooser.showOpenDialog(this) == fileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            XImage.save(file);
            ImageIcon icon = XImage.read(file.getName());
            Image img1 = icon.getImage();
            Image img2 = img1.getScaledInstance(lblHinhAnh.getWidth(), lblHinhAnh.getHeight(), Image.SCALE_SMOOTH);
            lblHinhAnh.setIcon(new ImageIcon(img2));
            lblHinhAnh.setText("");
            lblHinhAnh.setToolTipText(file.getName());
        }
    }

    public void update() {
        SanPham entity = getForm();
        try {
            dao.update(entity);
            this.fillToTable();
            this.clearForm();
            MainThongBao.alert(this, "Cập nhật thành công!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            MainThongBao.alert(this, "Cập nhật thất bại");
        }

    }

    public void insert() {
        SanPham entity = getForm();
        try {
            dao.insert(entity);
            this.fillToTable();
            this.clearForm();
            MainThongBao.alert(this, "Thêm mới thành công!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            MainThongBao.alert(this, "Thêm mới thất bại");
        }

    }

    public void delete() {
        if (!Auth.isManager()) {
            MainThongBao.alert(this, "Bạn không có quyền xóa sản phẩm!");
        } else {
            String maSP = txtMaSP.getText();
            if (MainThongBao.confirm(this, "Bạn có thật sự muốn xóa sản phẩm này?")) {
                try {
                    dao.delete(maSP);
                    this.fillToTable();
                    this.clearForm();
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!");
                }
            }
        }
    }

    public void setErrorMessage(JLabel label, String mess, String icon) {
        label.setText(mess);
        label.setForeground(Color.red);
        Image img = XImage.read(icon).getImage();
        label.setIcon(new ImageIcon(img));
    }

    public void clearErrorMess() {
        setErrorMessage(lblCheckDVT, "", "");
        setErrorMessage(lblCheckMa, "", "");
        setErrorMessage(lblCheckTenSP, "", "");
        setErrorMessage(lblCheckTrongLuong, "", "");
        setErrorMessage(lblDonGia, "", "");
        setErrorMessage(lblCheckPhanLoai, "", "");
    }

    String icon = "warning.jpg";

    public boolean checkID() {
        boolean flag = true;
        List<SanPham> sp = dao.selectAll();
        for (SanPham item : sp) {
            if (item.getMaSP().equalsIgnoreCase(txtMaSP.getText())) {
                setErrorMessage(lblCheckMa, "Mã sản phẩm đã tồn tại!", icon);
                flag = false;
                break;
            }

        }
        return flag;
    }

    public boolean check() {
        boolean flag = true;
        List<SanPham> sp = dao.selectAll();
        if (txtMaSP.getText().isBlank()) {
            setErrorMessage(lblCheckMa, "Vui lòng nhập mã sản phẩm!", icon);
            flag = false;
        } else {
            setErrorMessage(lblCheckMa, "", "");
            String pattern = "^[SP|sp]+[0-9]{2}$";
            if (!txtMaSP.getText().matches(pattern)) {
                setErrorMessage(lblCheckMa, "Định dạng mã sản phẩm không đúng!", icon);
                flag = false;
            }
        }

        if (txtTenSP.getText().isBlank()) {
            setErrorMessage(lblCheckTenSP, "Vui lòng nhập tên sản phẩm!", icon);
            flag = false;
        } else {
            setErrorMessage(lblCheckTenSP, "", "");
        }

        if (txtTenSP.getText().isBlank()) {
            setErrorMessage(lblCheckTenSP, "Vui lòng nhập tên sản phẩm!", icon);
            flag = false;
        } else {
            setErrorMessage(lblCheckTenSP, "", "");
        }

        if (txtDonViTinh.getText().isBlank()) {
            setErrorMessage(lblCheckDVT, "Vui lòng nhập đơn vị sản phẩm!", icon);
            flag = false;
        } else {
            setErrorMessage(lblCheckDVT, "", "");
        }

        if (txtDonGia.getText().isBlank()) {
            setErrorMessage(lblDonGia, "Vui lòng nhập đơn giá!", icon);
            flag = false;
        } else {
            setErrorMessage(lblDonGia, "", "");
            try {
                double donGia = Double.parseDouble(txtDonGia.getText());
                if (donGia <= 0) {
                    setErrorMessage(lblDonGia, "Đơn giá phải lớn hơn 0!", icon);
                    flag = false;
                }
            } catch (Exception e) {
                setErrorMessage(lblDonGia, "Đơn giá phải là số!", icon);
                flag = false;
            }
        }
        if (cboPhanLoai.getSelectedIndex() == 0) {
            setErrorMessage(lblCheckPhanLoai, "Vui lòng chọn phân loại sản phẩm!", icon);
            flag = false;
        } else {
            setErrorMessage(lblCheckPhanLoai, "", "");
        }
        if (!rdoKG.isSelected() && !rdoLit.isSelected()) {
            setErrorMessage(lblCheckTrongLuong, "Vui lòng chọn đơn vị trọng lượng!", icon);
            flag = false;
        } else {
            setErrorMessage(lblCheckTrongLuong, "", "");
            if (txtTrongLuong.getText().isBlank()) {
                setErrorMessage(lblCheckTrongLuong, "Vui lòng nhập trọng lượng!", icon);
                flag = false;
            } else {
                setErrorMessage(lblCheckTrongLuong, "", "");
                try {
                    double trongLuong = Double.parseDouble(txtTrongLuong.getText());
                    if (trongLuong <= 0) {
                        setErrorMessage(lblCheckTrongLuong, "Trọng lượng phải lớn hơn 0!", icon);
                        flag = false;
                    }
                } catch (Exception e) {
                    setErrorMessage(lblCheckTrongLuong, "Trọng lượng phải là số!", icon);
                    flag = false;
                }
            }
        }
        return flag;
    }

    void setClearErrorMess(JTextField txt, JLabel lbl) {
        if (!txt.getText().isBlank()) {
            setErrorMessage(lbl, "", "");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        btnTrongLuong = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDanhSach = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        txtTrongLuong = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtTenSP = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtMaSP = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        lblHinhAnh = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtDonGia = new javax.swing.JTextField();
        txtDonViTinh = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        btnLast = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        cboPhanLoai = new javax.swing.JComboBox<>();
        lblCheckTrongLuong = new javax.swing.JLabel();
        lblCheckMa = new javax.swing.JLabel();
        lblDonGia = new javax.swing.JLabel();
        lblCheckPhanLoai = new javax.swing.JLabel();
        lblCheckTenSP = new javax.swing.JLabel();
        lblCheckDVT = new javax.swing.JLabel();
        rdoLit = new javax.swing.JRadioButton();
        rdoKG = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblZalo = new javax.swing.JLabel();
        lblFacebook = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        tblDanhSach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblDanhSach.getTableHeader().setResizingAllowed(false);
        tblDanhSach.getTableHeader().setReorderingAllowed(false);
        tblDanhSach.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDanhSachMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblDanhSach);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/search.png"))); // NOI18N
        jLabel4.setText("Tìm Kiếm:");

        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(txtTimKiem)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1032, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1060, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Danh Sách", jPanel1);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtTrongLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTrongLuongActionPerformed(evt);
            }
        });
        jPanel4.add(txtTrongLuong, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 300, 230, 30));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText("Đơn vị tính (Chiếc,Hộp,Bánh):");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 260, 230, 40));

        txtTenSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenSPActionPerformed(evt);
            }
        });
        jPanel4.add(txtTenSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 180, 230, 30));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Mã sản phẩm: ");
        jPanel4.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 60, -1, 30));

        txtMaSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaSPActionPerformed(evt);
            }
        });
        jPanel4.add(txtMaSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 90, 230, 30));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("Tên sản phẩm: ");
        jPanel4.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 150, -1, 30));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("Đơn giá (VNĐ):");
        jPanel4.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 60, -1, 30));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblHinhAnh.setBackground(new java.awt.Color(255, 255, 255));
        lblHinhAnh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHinhAnh.setText("Hình Ảnh");
        lblHinhAnh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHinhAnhMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblHinhAnh, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblHinhAnh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 280, 220));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setText("Trọng lượng:");
        jPanel4.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 240, 230, 30));

        txtDonGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDonGiaActionPerformed(evt);
            }
        });
        jPanel4.add(txtDonGia, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 90, 230, 30));

        txtDonViTinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDonViTinhActionPerformed(evt);
            }
        });
        jPanel4.add(txtDonViTinh, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 300, 230, 30));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel18.setText("Phân loại:");
        jPanel4.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 150, -1, 30));

        btnLast.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/last.png"))); // NOI18N
        btnLast.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });
        jPanel4.add(btnLast, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 420, 100, 40));

        btnLamMoi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/refresh (1)111.png"))); // NOI18N
        btnLamMoi.setText("Mới");
        btnLamMoi.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });
        jPanel4.add(btnLamMoi, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 420, 100, 40));

        btnSua.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/update.png"))); // NOI18N
        btnSua.setText("Sửa");
        btnSua.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });
        jPanel4.add(btnSua, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 350, 100, 40));

        btnThem.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/add.png"))); // NOI18N
        btnThem.setText("Thêm");
        btnThem.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });
        jPanel4.add(btnThem, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 100, 40));

        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/delete.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });
        jPanel4.add(btnXoa, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 420, 100, 40));

        btnNext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/next.png"))); // NOI18N
        btnNext.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });
        jPanel4.add(btnNext, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 420, 100, 40));

        btnFirst.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/first.png"))); // NOI18N
        btnFirst.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });
        jPanel4.add(btnFirst, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 420, 100, 40));

        btnPrev.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/prev.png"))); // NOI18N
        btnPrev.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });
        jPanel4.add(btnPrev, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 420, 100, 40));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("Hình Ảnh:");
        jPanel4.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, -1, 30));

        cboPhanLoai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Chọn phân loại sản phẩm--", "Yên Xe Đạp", "Phuộc", "Bánh Xe ", "Lốp ", "Xe Đạp", " " }));
        cboPhanLoai.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cboPhanLoai.setFocusable(false);
        cboPhanLoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPhanLoaiActionPerformed(evt);
            }
        });
        jPanel4.add(cboPhanLoai, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 180, 230, 30));

        lblCheckTrongLuong.setBackground(new java.awt.Color(255, 255, 255));
        lblCheckTrongLuong.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCheckTrongLuong.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jPanel4.add(lblCheckTrongLuong, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 330, 230, 20));

        lblCheckMa.setBackground(new java.awt.Color(255, 255, 255));
        lblCheckMa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCheckMa.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jPanel4.add(lblCheckMa, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 120, 230, 20));

        lblDonGia.setBackground(new java.awt.Color(255, 255, 255));
        lblDonGia.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDonGia.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jPanel4.add(lblDonGia, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 120, 230, 20));

        lblCheckPhanLoai.setBackground(new java.awt.Color(255, 255, 255));
        lblCheckPhanLoai.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCheckPhanLoai.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jPanel4.add(lblCheckPhanLoai, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 210, 230, 20));

        lblCheckTenSP.setBackground(new java.awt.Color(255, 255, 255));
        lblCheckTenSP.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCheckTenSP.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jPanel4.add(lblCheckTenSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 210, 230, 20));

        lblCheckDVT.setBackground(new java.awt.Color(255, 255, 255));
        lblCheckDVT.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCheckDVT.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jPanel4.add(lblCheckDVT, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 330, 230, 20));

        rdoLit.setBackground(new java.awt.Color(255, 255, 255));
        btnTrongLuong.add(rdoLit);
        rdoLit.setText("Lít");
        rdoLit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoLitActionPerformed(evt);
            }
        });
        jPanel4.add(rdoLit, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 270, 50, -1));

        rdoKG.setBackground(new java.awt.Color(255, 255, 255));
        btnTrongLuong.add(rdoKG);
        rdoKG.setText("KG");
        rdoKG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoKGActionPerformed(evt);
            }
        });
        jPanel4.add(rdoKG, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 270, 50, -1));

        jTabbedPane1.addTab("Cập Nhật", jPanel4);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 204));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Quản Lý Sản Phẩm");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("Liên Hệ Nhà Phân Phối Sản Phẩm:");

        lblZalo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblZalo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon_of_Zalo.svg (1).png"))); // NOI18N
        lblZalo.setText("Zalo");
        lblZalo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblZaloMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblZaloMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblZaloMouseExited(evt);
            }
        });

        lblFacebook.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblFacebook.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/facebook.png"))); // NOI18N
        lblFacebook.setText("Facebook");
        lblFacebook.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblFacebookMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblFacebookMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblFacebookMouseExited(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("Chuyên Về Các Mặt Hàng:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 153, 51));
        jLabel7.setText("Phụ Tùng Xe");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 153, 51));
        jLabel8.setText("Xe đạp Đường Trường");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 153, 51));
        jLabel9.setText("Xe Đạp Điện");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 153, 51));
        jLabel10.setText("Các Loại Yên");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblZalo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(lblFacebook)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 292, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel5)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addGap(32, 32, 32)
                        .addComponent(jLabel10)))
                .addGap(164, 164, 164))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFacebook, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblZalo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(jLabel8))
        );

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(324, 324, 324))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void lblFacebookMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFacebookMouseClicked
        // TODO add your handling code here:
        try {
            Desktop.getDesktop().browse(new URL("https://www.facebook.com/profile.php?id=100083698931899").toURI());
        } catch (Exception e) {
        }
    }//GEN-LAST:event_lblFacebookMouseClicked

    private void lblFacebookMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFacebookMouseEntered
        // TODO add your handling code here:
        lblFacebook.setForeground(new Color(0, 51, 255));
    }//GEN-LAST:event_lblFacebookMouseEntered

    private void lblFacebookMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFacebookMouseExited
        // TODO add your handling code here:
        lblFacebook.setForeground(new Color(0, 0, 0));
    }//GEN-LAST:event_lblFacebookMouseExited

    private void lblZaloMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblZaloMouseClicked
        // TODO add your handling code here:
        try {
            Desktop.getDesktop().browse(new URL("https://zalo.me/0932805261").toURI());
        } catch (Exception e) {
        }
    }//GEN-LAST:event_lblZaloMouseClicked

    private void lblZaloMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblZaloMouseEntered
        // TODO add your handling code here:
        lblZalo.setForeground(new Color(0, 51, 255));
    }//GEN-LAST:event_lblZaloMouseEntered

    private void lblZaloMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblZaloMouseExited
        // TODO add your handling code here:
        lblZalo.setForeground(new Color(0, 0, 0));
    }//GEN-LAST:event_lblZaloMouseExited

    private void tblDanhSachMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDanhSachMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.i = tblDanhSach.getSelectedRow();
            this.edit();
            clearErrorMess();
        }
    }//GEN-LAST:event_tblDanhSachMouseClicked

    private void lblHinhAnhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHinhAnhMouseClicked
        // TODO add your handling code here:
        chonAnh();
    }//GEN-LAST:event_lblHinhAnhMouseClicked

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        if (Auth.isManager()) {
            if (check()) {
                update();
            }
        } else {
            MainThongBao.alert(this, "Bạn không có quyền cập nhật sản phẩm !");
        }
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        // TODO add your handling code here:
        clearForm();

    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
        this.fillToTable();
        this.clearForm();
        this.i = -1;
        updateStatus();
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        if (Auth.isManager()) {
            if (checkID() && check()) {
                insert();
            }
        } else {
            MainThongBao.alert(this, "Bạn không có quyền thêm sản phẩm !");
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        if (Auth.isManager()) {
            if (checkExists()) {
                delete();
            }
        } else {
            MainThongBao.alert(this, "Bạn không có quyền xóa sản phẩm !");
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        // TODO add your handling code here:
        this.i = 0;
        this.edit();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
        if (this.i > 0) {
            this.i--;
            this.edit();
        }
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        if (this.i < tblDanhSach.getRowCount() - 1) {
            this.i++;
            this.edit();
        }
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        // TODO add your handling code here:
        this.i = tblDanhSach.getRowCount() - 1;
        this.edit();
    }//GEN-LAST:event_btnLastActionPerformed

    private void txtMaSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaSPActionPerformed
        // TODO add your handling code here:
        setClearErrorMess(txtMaSP, lblCheckMa);
    }//GEN-LAST:event_txtMaSPActionPerformed

    private void txtTenSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenSPActionPerformed
        // TODO add your handling code here:
        setClearErrorMess(txtTenSP, lblCheckTenSP);
    }//GEN-LAST:event_txtTenSPActionPerformed

    private void txtDonViTinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDonViTinhActionPerformed
        // TODO add your handling code here:
        setClearErrorMess(txtDonViTinh, lblCheckDVT);
    }//GEN-LAST:event_txtDonViTinhActionPerformed

    private void txtDonGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDonGiaActionPerformed
        // TODO add your handling code here:
        setClearErrorMess(txtDonGia, lblDonGia);
    }//GEN-LAST:event_txtDonGiaActionPerformed

    private void cboPhanLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPhanLoaiActionPerformed
        // TODO add your handling code here:
        if (cboPhanLoai.getSelectedIndex() != 0) {
            setErrorMessage(lblCheckPhanLoai, "", "");
        }
    }//GEN-LAST:event_cboPhanLoaiActionPerformed

    private void txtTrongLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTrongLuongActionPerformed
        // TODO add your handling code here:
        setClearErrorMess(txtTrongLuong, lblCheckTrongLuong);
    }//GEN-LAST:event_txtTrongLuongActionPerformed

    private void rdoKGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoKGActionPerformed
        // TODO add your handling code here:
        if (rdoKG.isSelected()) {
            setErrorMessage(lblCheckTrongLuong, "", "");
        }
    }//GEN-LAST:event_rdoKGActionPerformed

    private void rdoLitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoLitActionPerformed
        // TODO add your handling code here:
        if (rdoLit.isSelected()) {
            setErrorMessage(lblCheckTrongLuong, "", "");
        }
    }//GEN-LAST:event_rdoLitActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.ButtonGroup btnTrongLuong;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboPhanLoai;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblCheckDVT;
    private javax.swing.JLabel lblCheckMa;
    private javax.swing.JLabel lblCheckPhanLoai;
    private javax.swing.JLabel lblCheckTenSP;
    private javax.swing.JLabel lblCheckTrongLuong;
    private javax.swing.JLabel lblDonGia;
    private javax.swing.JLabel lblFacebook;
    private javax.swing.JLabel lblHinhAnh;
    private javax.swing.JLabel lblZalo;
    private javax.swing.JRadioButton rdoKG;
    private javax.swing.JRadioButton rdoLit;
    private javax.swing.JTable tblDanhSach;
    private javax.swing.JTextField txtDonGia;
    private javax.swing.JTextField txtDonViTinh;
    private javax.swing.JTextField txtMaSP;
    private javax.swing.JTextField txtTenSP;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txtTrongLuong;
    // End of variables declaration//GEN-END:variables
}
