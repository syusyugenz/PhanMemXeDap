/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Ui;

import DAO.HoaDonChiTietDAO;
import DAO.HoaDonDAO;
import DAO.KhachHangDAO;
import DAO.PhieuNhapKhoDAO;
import DAO.SanPhamDAO;
import Entity.HoaDon;
import Entity.HoaDonChiTiet;
import Entity.KhachHang;
import Entity.SanPham;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import junit.framework.Assert;
import utils.Auth;
import utils.MainThongBao;
import utils.XDate;

/**
 *
 * @author drbf1
 */
public class GioHang extends javax.swing.JPanel {

    /**
     * Creates new form what
     */
    public GioHang() {
        initComponents();

        lblNhanVien.setText(Auth.user.getHoTen());
        this.i = -1;
        fillTableSP();
        fillKhachHang();
    }
    JFrame frame = new JFrame();
    int i = -1;
    DefaultListModel listModel;
    DefaultTableModel gioHangModel;
    DefaultTableModel spModel;
    HoaDonDAO hdDAO = new HoaDonDAO();
    KhachHangDAO khDAO = new KhachHangDAO();
    SanPhamDAO spDAO = new SanPhamDAO();
    PhieuNhapKhoDAO pnkDAO = new PhieuNhapKhoDAO();
    HoaDonChiTietDAO hdctDAO = new HoaDonChiTietDAO();
    Locale lc = new Locale("vi", "VN");
    String pattern = "###,###.##";
    DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(lc);

    public void fillTableSP() {
        decimalFormat.applyPattern(pattern);
        spModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        spModel.setColumnIdentifiers(new Object[]{
            "Mã Sản Phẩm", "Tên Sản Phẩm", "Đơn Giá", "Số Lượng", "Loại"
        });
        spModel.setRowCount(0);
        String key = txtSearch.getText();
        List<SanPham> list = spDAO.selectByKeywordAndQuantity(key);
        for (SanPham sp : list) {
            spModel.addRow(new Object[]{
                sp.getMaSP(), sp.getTenSP(), decimalFormat.format(sp.getDonGia()), sp.getSoLuong(), sp.getPhanLoai()
            });
        }
        tblDanhSachSP.setModel(spModel);
    }

    void fillKhachHang() {
        listModel = new DefaultListModel();
        String dienThoai = "";
        String ten = "";
        listModel.removeAllElements();
        String tenKH[] = txtTimKhachHang.getText().trim().split("\\d+");
        String getNum = txtTimKhachHang.getText().replaceAll("[^0-9,-\\.^]", ",");
        String num[] = getNum.split(",");
        if (num.length > 0) {
            dienThoai = num[num.length - 1];
        } else {
            dienThoai = "";
        }
        if (tenKH.length > 0) {
            ten = tenKH[0];
        } else {
            ten = "";
        }
        List<KhachHang> danhSachKH = khDAO.selectByKeyWord(ten, dienThoai);
        for (KhachHang item : danhSachKH) {
            listModel.addElement(item);
        }
        JListKH.setModel(listModel);

    }

    private String random() {
        Random random = new Random();
        int hd = 10000 + random.nextInt(90000);
        return Integer.toString(hd);
    }

    void createHoaDon() {
        listModel = (DefaultListModel) JListKH.getModel();
        int j = JListKH.getSelectedIndex();
        if (j < 0) {
            MainThongBao.alert(this, "Hãy chọn thông tin khách hàng trước khi tạo hóa đơn!");
            return;
        } else {
            KhachHang kh = (KhachHang) listModel.getElementAt(j);
            String maHD = "HD" + random();
            txtMaHD.setText(maHD);
            HoaDon hd = new HoaDon();
            hd.setMaHD(maHD);
            hd.setMaNV(Auth.user.getMaNV());
            Date now = new Date();
            hd.setNgayTao(XDate.toString(now, "yyyy-MM-dd"));
            idKH = kh.getMaKH();
            hd.setMaKH(idKH);
            hdDAO.insert(hd);
            btnTaoDon.setEnabled(false);
            MainThongBao.alert(this, "Tạo hóa đơn thành công!");
        }
        this.isCreate = true;
        j = -1;
    }

    public int getTichDiem() {
        KhachHang kh = khDAO.selectById(idKH);
        return kh.getTichDiem();
    }

    int idKH;
    int count = 1;

    void addSanPham() {
        int index = tblDanhSachSP.getSelectedRow();
        if (index >= 0) {
            DefaultTableModel gioHangModel = (DefaultTableModel) tblGioHang.getModel();
            String maSP = (String) tblDanhSachSP.getValueAt(index, 0);

            SanPham sp = spDAO.selectById(maSP);
            String soLuongNhap = MainThongBao.prompt(this, "Nhập số lượng cần mua");
            if (soLuongNhap == null) {
                return;
            }
            try {
                int soLuongMua = Integer.parseInt(soLuongNhap);
                int soLuongHienTai = (int) tblDanhSachSP.getValueAt(index, 3);
                double chiecKhau = sp.getDonGia() * 0.02;
                if (soLuongMua != 0) {
                    if (soLuongMua > 0 && soLuongMua <= soLuongHienTai) {
                        boolean isAddToGiohang = false;
                        int rowIndex = -1;
                        for (int j = 0; j < gioHangModel.getRowCount(); j++) {
                            if (sp.getMaSP().equals(gioHangModel.getValueAt(j, 1))) {
                                isAddToGiohang = true;
                                rowIndex = j;
                                break;
                            }
                        }
                        if (isAddToGiohang) {
                            int soLuongCu = (int) gioHangModel.getValueAt(rowIndex, 3);
                            int soLuongMoi = soLuongCu + soLuongMua;
                            double thanhTienMoi = (sp.getDonGia() * soLuongMoi) + (soLuongMoi * chiecKhau);
                            gioHangModel.setValueAt(soLuongMoi, rowIndex, 3);
                            gioHangModel.setValueAt((String) decimalFormat.format(thanhTienMoi), rowIndex, 5);
                        } else {
                            double thanhTien = (sp.getDonGia() * soLuongMua) + (soLuongMua * chiecKhau);
                            gioHangModel.addRow(new Object[]{
                                count, sp.getMaSP(), sp.getTenSP(), soLuongMua,
                                decimalFormat.format(sp.getDonGia() + chiecKhau),
                                decimalFormat.format(thanhTien)
                            });
                        }
                        int soLuongConLai = soLuongHienTai - soLuongMua;
                        spModel.setValueAt(soLuongConLai, index, 3);
                        txtTongTien.setText(decimalFormat.format(tinhTongGiaTriGioHang(gioHangModel)));
                        double tongTien = tinhTongGiaTriGioHang(gioHangModel);
                        double tienGiamGia = 0;
                        if (getTienGiamGia() < tongTien) {
                            tienGiamGia = getTienGiamGia();
                        }
                        double tienCanThanhToan = tongTien - tienGiamGia;
                        txtTienGiamGia.setText(decimalFormat.format(tienGiamGia));
                        txtCanThanhToan.setText(decimalFormat.format(tienCanThanhToan));
                        this.i = -1;
                        this.count += 1;
                    } else {
                        MainThongBao.alert(this, "Số lượng không hợp lệ!");
                    }

                } else {
                    MainThongBao.alert(this, "Vui lòng chọn sản phẩm!");
                }
            } catch (NumberFormatException e) {
                MainThongBao.alert(this, "Số lượng phải là số!");
                System.out.println(e.getMessage());
            }

        }
    }

    private double tinhTongGiaTriGioHang(DefaultTableModel modelGioHang) {
        double tongGiaTri = 0;
        for (int j = 0; j < modelGioHang.getRowCount(); j++) {
            int soLuongMoi = (int) modelGioHang.getValueAt(j, 3);
            String id = (String) modelGioHang.getValueAt(j, 1);
            SanPham sp = spDAO.selectById(id);
            double chiecKhau = sp.getDonGia() * 0.02;
            double thanhTien = (sp.getDonGia() * soLuongMoi) + (soLuongMoi * chiecKhau);
            tongGiaTri += thanhTien;
        }
        return tongGiaTri;
    }

    void removeSanPham() {
        if (isThanhToan) {
            return;
        }
        if (i >= 0) {
            DefaultTableModel modelGioHang = (DefaultTableModel) tblGioHang.getModel();
            DefaultTableModel modelSP = (DefaultTableModel) tblDanhSachSP.getModel();

            String maSP = (String) modelGioHang.getValueAt(i, 1);
            int soLuongHienTaiGioHang = (int) modelGioHang.getValueAt(i, 3);

            if (soLuongHienTaiGioHang > 1) {
                double donGia = Double.parseDouble(modelGioHang.getValueAt(i, 4).toString()) * 1000;
                double thanhTien = (soLuongHienTaiGioHang - 1) * donGia;
                modelGioHang.setValueAt(soLuongHienTaiGioHang - 1, i, 3);
                modelGioHang.setValueAt(decimalFormat.format(thanhTien), i, 5);
            } else {
                modelGioHang.removeRow(i);
                for (int j = 0; j < modelGioHang.getRowCount(); j++) {
                    modelGioHang.setValueAt(j + 1, j, 0);
                }
                this.count = modelGioHang.getRowCount() + 1;
            }
            txtTongTien.setText(String.valueOf(tinhTongGiaTriGioHang(modelGioHang)));
            double tongTien = Double.parseDouble(txtTongTien.getText());
            double tienGiamGia = Double.parseDouble(txtTienGiamGia.getText());
            if (getTienGiamGia() < tongTien) {
                tienGiamGia = getTienGiamGia();
            }
            double tienCanThanhToan = tongTien - tienGiamGia;
            txtTienGiamGia.setText(decimalFormat.format(tienGiamGia));
            txtCanThanhToan.setText(decimalFormat.format(tienCanThanhToan));

            for (int j = 0; j < modelSP.getRowCount(); j++) {
                if (maSP.equals(modelSP.getValueAt(j, 0))) {
                    int soLuongSPHienTai = (int) modelSP.getValueAt(j, 3);
                    modelSP.setValueAt(soLuongSPHienTai + 1, j, 3);
                    break;
                }
            }
        }
    }

    void ThanhToan() {
        if (!isCreate) {
            MainThongBao.alert(this, "Đơn hàng chưa được tạo!");
            return;
        } else if (txtTienKhachDua.getText().isBlank()) {
            MainThongBao.alert(this, "Chưa nhập số tiền khách trả!");
            return;
        }
        DefaultTableModel modelGioHang = (DefaultTableModel) tblGioHang.getModel();
        DefaultTableModel modelSanPham = (DefaultTableModel) tblDanhSachSP.getModel();
        if (!txtTienKhachDua.getText().isBlank()) {
            try {
                double tienKhachDua = Double.parseDouble(txtTienKhachDua.getText());
                double tienCanThanhToan = Double.parseDouble(txtCanThanhToan.getText());
                if (tienKhachDua < tienCanThanhToan) {
                    MainThongBao.alert(this, "Số tiền khách đưa không đủ!");
                    return;
                } else {
                    for (int j = 0; j < modelGioHang.getRowCount(); j++) {
                        String maHD = txtMaHD.getText();
                        String maSP = (String) modelGioHang.getValueAt(j, 1);
                        SanPham sp = spDAO.selectById(maSP);
                        String id = sp.getMaSP();
                        double giaBan = Double.parseDouble(modelGioHang.getValueAt(j, 4).toString()) * 1000;
                        int soLuong = (int) modelGioHang.getValueAt(j, 3);
                        HoaDonChiTiet entity = new HoaDonChiTiet();
                        entity.setMaHD(maHD);
                        entity.setGiaBan(giaBan);
                        entity.setMaSP(id);
                        entity.setSoLuong(soLuong);
                        hdctDAO.insert(entity);
                    }
                    updateSoLuongSP();
                    updateSoTichDiem();
                    MainThongBao.alert(this, "Thanh toán thành công!");
                    this.isThanhToan = true;
                }
            } catch (NumberFormatException e) {
                MainThongBao.alert(this, "Số tiền không hợp lệ!");
            }
        }

    }

    void updateSoTichDiem() {
        double tienGiamGia = Double.parseDouble(txtTienGiamGia.getText());
        if (tienGiamGia != 0) {
            int index = 0;
            int kq = 1;
            for (int j = 0; j <= tienGiamGia; j++) {
                if (j / 30 == kq) {
                    index += 10;
                    kq++;
                }
            }
            KhachHang kh = khDAO.selectById(idKH);
            int diem = kh.getTichDiem();
            int conLai = diem - index;
            kh.setTichDiem(conLai);
            khDAO.update(kh);
        } else {
            KhachHang entity = khDAO.selectById(idKH);
            if (entity.getTichDiem() > 0) {
                int tichDiem = entity.getTichDiem();
                entity.setTichDiem(getSoDiem() + tichDiem);
                khDAO.update(entity);
            } else {
                entity.setTichDiem(getSoDiem());
                khDAO.update(entity);
            }
        }
    }

    public int getSoDiem() {
        int index = 0;
        int kq = 1;
        double tienCanThanhToan = Double.parseDouble(txtCanThanhToan.getText());
        for (int j = 0; j <= tienCanThanhToan; j++) {
            if (j / 30 == kq) {
                index++;
                kq++;
            }
        }
        return index;
    }

    public double getTienGiamGia() {
        KhachHang kh = khDAO.selectById(idKH);
        int index = 1;
        double tien = 30000;
        double tienGiamGia = 0;
        int kq = 1;
        if (kh.getTichDiem() >= 10) {
            for (int j = 0; j <= kh.getTichDiem(); j++) {
                if (j / 10 == kq) {
                    tienGiamGia += index * tien;
                    index++;
                    kq++;
                }
            }
        }
        return tienGiamGia;
    }

    void updateSoLuongSP() {
        DefaultTableModel modelSanPham = (DefaultTableModel) tblDanhSachSP.getModel();
        for (int j = 0; j < modelSanPham.getRowCount(); j++) {
            String maSP = (String) modelSanPham.getValueAt(j, 0);
            SanPham entity = spDAO.selectById(maSP);
            int soLuong = (int) modelSanPham.getValueAt(j, 3);
            entity.setSoLuong(soLuong);
            spDAO.update(entity);
        }
    }
    boolean isCreate = false;

    public void clear(DefaultTableModel modelGioHang) {
        idKH = 0;
        modelGioHang.setRowCount(0);
        btnTaoDon.setEnabled(true);
        txtTimKhachHang.setText("");
        fillKhachHang();
        txtMaHD.setText("");
        txtCanThanhToan.setText("");
        txtTienGiamGia.setText("");
        txtTongTien.setText("");
        txtTienKhachDua.setText("");
        txtTienThua.setText("");
        fillTableSP();
        this.count = 1;
    }

    private void in(String file) {
        //Tạo thư mục để lưu file outPut.
        JFileChooser chooser = new JFileChooser("/Desktop");
        chooser.setDialogTitle("Lưu về");
        Date dt = new Date();
        SimpleDateFormat d = new SimpleDateFormat("hh-mm-ss dd-MM-yyyy");
        String gio = d.format(dt);
        String name = txtMaHD.getText() + gio;
        chooser.setName(name);
        int value = chooser.showSaveDialog(null);
        if (value == JFileChooser.APPROVE_OPTION) {
            File fileChoose = chooser.getSelectedFile();
            File f = new File(fileChoose + ".txt");
            try {
                File fileDir = new File(f.toURI());
                try (Writer out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(fileDir), "UTF8"))) {
                    out.append(file);
                    out.flush();
                    out.close();
                }
                if (JOptionPane.showConfirmDialog(this, "In Thành Công! Bạn có muốn mở file ngay không?") == JOptionPane.YES_OPTION) {
                    Desktop.getDesktop().browse(f.toURI());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        this.isCreate = false;
    }

    boolean isThanhToan = false;

    private String dinhdanghoadon() {
        Date dt = new Date();
        SimpleDateFormat d = new SimpleDateFormat("hh:mm dd/MM/yyyy");
        String gio = d.format(dt);
        List<HoaDonChiTiet> hdct = hdctDAO.selectByIdHDCT(txtMaHD.getText());

        String temp = "";
        temp += "             Cửa hàng tiện lợi rồng xanh\n\r"
                + "        KDC Hoàng Quân, Cái Răng, Cần Thơ\n"
                + "------------------------------------------------------\n\r"
                + "            PHIẾU THANH TOÁN\n\r"
                + "Mã hóa đơn: " + txtMaHD.getText() + "\n\n"
                + "STT\tTên món\t\t\t\tSố lượng\tThành Tiền\n";

        int i = 0;
        for (HoaDonChiTiet item : hdct) {
            SanPham sp = spDAO.selectById(item.getMaSP());
            String tenSP = sp.getTenSP();
            int soluong = item.getSoLuong();
            double thanhtien = item.getGiaBan() * item.getSoLuong();
            String tam = String.format("%-9s%-35s%-11s%-15s", i + 1, tenSP, soluong, decimalFormat.format(thanhtien));
            temp += tam + "\n";
            i++;
        }
        temp += "------------------------------------------------------\n\r"
                + "Tông tiền: " + txtCanThanhToan.getText() + " VND \n"
                + "Số tiền nhận: " + decimalFormat.format(Double.parseDouble(txtTienKhachDua.getText())) + " VND \n"
                + "Tiền thừa: " + txtTienThua.getText() + " VND \n"
                + "Ngày " + gio + "\n"
                + "Nhân viên: " + Auth.user.getHoTen() + "\n"
                + "------------------------------------------------------\n"
                + "               Hẹn gặp lại quí khách\n"
                + "               Xin chân thành cảm ơn!";
        return temp;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtTongTien = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        btnHuyHD = new javax.swing.JButton();
        btnThanhToan = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel16 = new javax.swing.JLabel();
        lblNhanVien = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        btnThemKH = new javax.swing.JButton();
        txtTimKhachHang = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        JListKH = new javax.swing.JList<>();
        jSeparator4 = new javax.swing.JSeparator();
        txtTienGiamGia = new javax.swing.JTextField();
        txtCanThanhToan = new javax.swing.JTextField();
        txtTienKhachDua = new javax.swing.JTextField();
        txtTienThua = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        txtMaHD = new javax.swing.JTextField();
        btnThanhToan1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnTaoDon = new javax.swing.JButton();
        btnXoaSP = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblGioHang = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDanhSachSP = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        btnThemGH = new javax.swing.JButton();
        btnXoaSP1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(980, 810));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Chi tiết hóa đơn"));

        jLabel1.setText("Nhân viên: ");

        jLabel5.setText("Tổng tiền:");

        txtTongTien.setEditable(false);
        txtTongTien.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("VNĐ");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("VNĐ");

        jLabel9.setText("Cần thanh toán:");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("VNĐ");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("VNĐ");

        jLabel12.setText("Tiền khách đưa:");

        jLabel13.setText("Tiền thừa:");

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("VNĐ");

        btnHuyHD.setText("Hủy hóa đơn");
        btnHuyHD.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnHuyHD.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHuyHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyHDActionPerformed(evt);
            }
        });

        btnThanhToan.setText("Thanh toán");
        btnThanhToan.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(255, 255, 255), null));
        btnThanhToan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        jLabel16.setText("Tiền giảm giá:");

        lblNhanVien.setBackground(new java.awt.Color(255, 255, 255));
        lblNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel19.setText("Tìm khách hàng:");

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        btnThemKH.setText("Thêm Khách Hàng");
        btnThemKH.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnThemKH.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThemKH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnThemKHMouseEntered(evt);
            }
        });
        btnThemKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemKHActionPerformed(evt);
            }
        });

        txtTimKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKhachHangActionPerformed(evt);
            }
        });

        JListKH.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(JListKH);

        txtTienGiamGia.setEditable(false);
        txtTienGiamGia.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txtCanThanhToan.setEditable(false);
        txtCanThanhToan.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txtTienKhachDua.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTienKhachDua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTienKhachDuaActionPerformed(evt);
            }
        });

        txtTienThua.setEditable(false);
        txtTienThua.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel4.setText("Mã hóa đơn:");

        txtMaHD.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtMaHD.setEnabled(false);

        btnThanhToan1.setText("Xuất hóa đơn");
        btnThanhToan1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(255, 255, 255), null));
        btnThanhToan1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThanhToan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToan1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jSeparator5)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(41, 41, 41)
                            .addComponent(btnHuyHD, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtTienGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(25, 25, 25))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(txtCanThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtTienThua, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel14)))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(18, 18, 18)
                            .addComponent(lblNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtTimKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel19))
                            .addGap(18, 18, 18)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnThemKH, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnThanhToan1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(95, 95, 95))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnThemKH, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtTimKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTienGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCanThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTienThua, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(23, 23, 23)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHuyHD, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnThanhToan1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Giỏ hàng"));

        btnTaoDon.setText("Tạo đơn hàng");
        btnTaoDon.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnTaoDon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTaoDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoDonActionPerformed(evt);
            }
        });

        btnXoaSP.setText("Xóa từng sản phẩm");
        btnXoaSP.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnXoaSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaSPActionPerformed(evt);
            }
        });

        tblGioHang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tblGioHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã Sản Phẩm", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblGioHang.setFocusable(false);
        tblGioHang.setGridColor(new java.awt.Color(0, 204, 255));
        tblGioHang.setSelectionBackground(new java.awt.Color(255, 102, 102));
        tblGioHang.setShowGrid(true);
        tblGioHang.getTableHeader().setResizingAllowed(false);
        tblGioHang.getTableHeader().setReorderingAllowed(false);
        tblGioHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGioHangMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblGioHang);
        tblGioHang.getAccessibleContext().setAccessibleDescription("");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnTaoDon, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnXoaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(461, 461, 461))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnTaoDon, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnXoaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Danh sách sản phẩm"));

        tblDanhSachSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã Sản Phẩm", "Tên Sản Phẩm", "Đơn Giá", "Số Lượng", "Loại"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDanhSachSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDanhSachSPMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblDanhSachSP);

        jLabel15.setText("Tìm kiếm:");

        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        btnThemGH.setText("Thêm vào giỏ hàng");
        btnThemGH.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnThemGH.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThemGH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemGHActionPerformed(evt);
            }
        });

        btnXoaSP1.setText("Xóa sản phẩm");
        btnXoaSP1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnXoaSP1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaSP1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnThemGH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnXoaSP1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 601, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemGH, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoaSP1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(12, 22, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnHuyHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyHDActionPerformed
        if (!isCreate) {
            return;
        }
        if (isThanhToan) {
            MainThongBao.alert(this, "Không thể hủy hóa đơn đã thanh toán!");
            return;
        }
        if (MainThongBao.confirm(this, "Bạn có muốn hủy hóa đơn này không?")) {
            DefaultTableModel modelGioHang = (DefaultTableModel) tblGioHang.getModel();
            modelGioHang.setRowCount(0);
            idKH = 0;
            fillTableSP();
            btnTaoDon.setEnabled(true);
            hdDAO.delete(txtMaHD.getText());
            txtMaHD.setText("");
            txtCanThanhToan.setText("");
            txtTienGiamGia.setText("");
            txtTongTien.setText("");
            txtTienKhachDua.setText("");
            txtTienThua.setText("");
            this.count = 1;
            this.isCreate = false;
            this.isThanhToan = false;
            MainThongBao.alert(this, "Đơn hàng đã được hủy!");
        } else {
            return;
        }
    }//GEN-LAST:event_btnHuyHDActionPerformed

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        if (isThanhToan) {
            MainThongBao.alert(this, "Đơn hàng đã được thanh toán!");
            return;
        }
        ThanhToan();
    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void btnTaoDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoDonActionPerformed
        if (isCreate) {
            MainThongBao.alert(this, "Vui lòng xuất hóa đơn trước khi tạo đơn hàng mới!");
            return;
        }
        createHoaDon();
    }//GEN-LAST:event_btnTaoDonActionPerformed

    private void btnXoaSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaSPActionPerformed
        removeSanPham();
    }//GEN-LAST:event_btnXoaSPActionPerformed

    private void tblDanhSachSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDanhSachSPMouseClicked
        // TODO add your handling code here:
        this.i = tblDanhSachSP.getSelectedRow();
    }//GEN-LAST:event_tblDanhSachSPMouseClicked

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
        fillTableSP();
    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased

    }//GEN-LAST:event_txtSearchKeyReleased

    private void btnThemGHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemGHActionPerformed
        if (isThanhToan) {
            MainThongBao.alert(this, "Đơn hàng đã thanh toán không thể thêm sản phẩm!");
            return;
        }
        if (!txtMaHD.getText().isBlank()) {
            addSanPham();
        } else {
            MainThongBao.alert(this, "Vui lòng tạo hóa đơn trước khi thêm sản phẩm!");
        }
    }//GEN-LAST:event_btnThemGHActionPerformed

    private void btnThemKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemKHActionPerformed
        // TODO add your handling code here:
        ThemKhachHang tkh = new ThemKhachHang(frame, true);
        tkh.setLocation(806, 243);
        tkh.setVisible(true);
    }//GEN-LAST:event_btnThemKHActionPerformed

    private void txtTimKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKhachHangActionPerformed
        // TODO add your handling code here:
        fillKhachHang();
    }//GEN-LAST:event_txtTimKhachHangActionPerformed

    private void tblGioHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGioHangMouseClicked
        // TODO add your handling code here:
        this.i = tblGioHang.getSelectedRow();
    }//GEN-LAST:event_tblGioHangMouseClicked

    private void txtTienKhachDuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTienKhachDuaActionPerformed
        // TODO add your handling code here:
        double tienKhachDua = Double.parseDouble(txtTienKhachDua.getText());
        double tienCanThanhToan = Double.parseDouble(txtCanThanhToan.getText()) * 1000;
        double tienThua = tienKhachDua - tienCanThanhToan;
        if (tienThua >= 0) {
            txtTienThua.setText(decimalFormat.format(tienThua));
        } else {
            txtTienThua.setText("Not enough!");
        }
    }//GEN-LAST:event_txtTienKhachDuaActionPerformed

    private void btnXoaSP1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaSP1ActionPerformed
        // TODO add your handling code here:
        if (isThanhToan) {
            return;
        }
        i = tblGioHang.getSelectedRow();
        if (i >= 0) {
            DefaultTableModel modelGioHang = (DefaultTableModel) tblGioHang.getModel();
            DefaultTableModel modelSP = (DefaultTableModel) tblDanhSachSP.getModel();

            String tenSP = (String) modelGioHang.getValueAt(i, 2);
            int soLuongHienTaiGioHang = (int) modelGioHang.getValueAt(i, 3);
            modelGioHang.removeRow(i);
            txtTongTien.setText(String.valueOf(tinhTongGiaTriGioHang(modelGioHang)));
            double tongTien = Double.parseDouble(txtTongTien.getText());
            double tienGiamGia = 0;
            if (getTienGiamGia() < tongTien) {
                tienGiamGia = getTienGiamGia();
            }
            double tienCanThanhToan = tongTien - tienGiamGia;
            txtTienGiamGia.setText(String.valueOf(tienGiamGia));
            txtCanThanhToan.setText(String.valueOf(tienCanThanhToan));

            for (int j = 0; j < modelSP.getRowCount(); j++) {
                if (tenSP.equals(modelSP.getValueAt(j, 1))) {
                    int soLuongHienTaiSach = (int) modelSP.getValueAt(j, 3);
                    modelSP.setValueAt(soLuongHienTaiSach + soLuongHienTaiGioHang, j, 3);
                    break;
                }
            }
            for (int j = 0; j < modelGioHang.getRowCount(); j++) {
                modelGioHang.setValueAt(j + 1, j, 0);
            }
            this.count = modelGioHang.getRowCount() + 1;
        }
    }//GEN-LAST:event_btnXoaSP1ActionPerformed

    private void btnThanhToan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToan1ActionPerformed
        // TODO add your handling code here:
        if (!isCreate) {
            MainThongBao.alert(this, "Chưa có đơn hàng để in!");
            return;
        } else if (!isThanhToan) {
            MainThongBao.alert(this, "Vui lòng thanh toán trước khi xuất hóa đơn!");
            return;
        }
        in(dinhdanghoadon());
        DefaultTableModel modelGioHang = (DefaultTableModel) tblGioHang.getModel();
        clear(modelGioHang);
    }//GEN-LAST:event_btnThanhToan1ActionPerformed

    private void btnThemKHMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnThemKHMouseEntered
        // TODO add your handling code here:
        fillKhachHang();
    }//GEN-LAST:event_btnThemKHMouseEntered


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> JListKH;
    private javax.swing.JButton btnHuyHD;
    private javax.swing.JButton btnTaoDon;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnThanhToan1;
    private javax.swing.JButton btnThemGH;
    private javax.swing.JButton btnThemKH;
    private javax.swing.JButton btnXoaSP;
    private javax.swing.JButton btnXoaSP1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JLabel lblNhanVien;
    private javax.swing.JTable tblDanhSachSP;
    private javax.swing.JTable tblGioHang;
    private javax.swing.JTextField txtCanThanhToan;
    private javax.swing.JTextField txtMaHD;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTienGiamGia;
    private javax.swing.JTextField txtTienKhachDua;
    private javax.swing.JTextField txtTienThua;
    private javax.swing.JTextField txtTimKhachHang;
    private javax.swing.JTextField txtTongTien;
    // End of variables declaration//GEN-END:variables
}
