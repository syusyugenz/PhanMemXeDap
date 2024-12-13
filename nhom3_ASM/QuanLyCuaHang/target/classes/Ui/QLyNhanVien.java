/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Ui;

import DAO.ChucVuDAO;
import DAO.HoaDonDAO;
import DAO.NhanVienDAO;
import Entity.ChucVu;
import Entity.HoaDon;
import Entity.NhanVien;
import java.awt.Color;
import java.awt.Image;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import utils.Auth;
import utils.MainThongBao;
import utils.XDate;
import utils.XImage;

/**
 *
 * @author admin
 */
public class QLyNhanVien extends javax.swing.JPanel {

    /**
     * Creates new form NhanVien
     */
    public QLyNhanVien() {
        initComponents();
        this.i = -1;
        fillToTable();
        fillComboBox();
        txtLuong.setEnabled(false);
        dcNgayLamViec.setDateFormatString("dd-MM-yyyy");
        dcNgaySinh.setDateFormatString("dd-MM-yyyy");
        updateStatus();
        Date d = dcNgaySinh.getDate();
    }

    Locale lc = new Locale("vi", "VN");
    String pattern = "###,###.##";
    DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(lc);
    DefaultTableModel model;
    NhanVienDAO dao = new NhanVienDAO();
    HoaDonDAO HDdao = new HoaDonDAO();
    ChucVuDAO CVdao = new ChucVuDAO();
    int i = -1;
    LocalDate now = LocalDate.now();

    //Hàm check chức vụ
    public void checkRole() {
        if (!Auth.isManager()) {
            rdoNV.setSelected(true);
            rdoQL.setEnabled(false);
        } else {

            rdoQL.setEnabled(true);
        }
    }

    //Check nhân viên có tồn tại trong mí cái kia khác khum
    public boolean checkExists() {
        boolean check = true;
        List<HoaDon> nh = HDdao.selectAll();

        for (HoaDon list : nh) {
            if (list.getMaNV().equalsIgnoreCase(txtMaNV.getText())) {
                check = false;
                break;
            }
        }

        if (!check) {
            JOptionPane.showMessageDialog(this, "Nhân viên vẫn còn đang làm việc!");
        }
        return check;
    }

    //Đổ dữ liệu vào bảng
    public void fillToTable() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setRowCount(0);
        model.setColumnIdentifiers(new Object[]{
            "Mã nhân viên", "Họ tên", "Giới tính", "Email", "SDT", "Ngày sinh", "Vai trò", "Loại công việc", "Ngày vào làm"
        });
        try {

            String key = txtTim.getText();
            List<NhanVien> list = dao.selectByKeyword(key);

            for (NhanVien nv : list) {
                ChucVu cv = CVdao.selectById(nv.getMaCV());
                model.addRow(new Object[]{
                    nv.getMaNV(), nv.getHoTen(), nv.isGioiTinh() ? "Nam" : "Nữ", nv.getEmail(), nv.getSdt(),
                    nv.getNgaySinh(), nv.isVaiTro() ? "Quản lý" : "Nhân Viên", cv.getTenCV(), nv.getNgayVaoLam()
                }
                );
            }

            tblDanhSach.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }

    void fillCboQuanLy() {
        cboLoaiCV.removeAllItems();
        ChucVu list = CVdao.selectQuanLy();
        cboLoaiCV.addItem(list.getTenCV());
    }

    void fillComboBox() {
        List<ChucVu> list = CVdao.selectAll();
        cboLoaiCV.removeAllItems();
        for (ChucVu kh : list) {
            cboLoaiCV.addItem(kh.getTenCV());
        }
    }

    public void setErrorMessage(JLabel lbl, String message, String icon) {
        lbl.setText(message);
        lbl.setForeground(Color.red);
        Image img = XImage.read(icon).getImage();
        lbl.setIcon(new ImageIcon(img));

    }
    
    String iconName = "warning.jpg";

    public void updateStatus() {
        boolean edit = (this.i >= 0);
        boolean first = (this.i == 0);
        boolean last = (this.i == tblDanhSach.getRowCount() - 1);

        txtMaNV.setEditable(!edit);
        dcNgayLamViec.setEnabled(!edit);
        btnThem.setEnabled(!edit);
        btnSua.setEnabled(edit);
        btnXoa.setEnabled(edit);
        if (!Auth.isManager()) {
            rdoNV.setEnabled(!edit);
            rdoQL.setEnabled(!edit);
        }
        btnFirst.setEnabled(edit && !first);
        btnPrev.setEnabled(edit && !first);
        btnLast.setEnabled(edit && !last);
        btnNext.setEnabled(edit && !last);
    }

    public void setForm(NhanVien nv) {
        txtMaNV.setText(nv.getMaNV());
        txtHoTen.setText(nv.getHoTen());
        txtMatKhau.setText(nv.getMatKhau());
        txtDiaChi.setText(nv.getDiaChi());
        txtEmail.setText(nv.getEmail());
        txtSDT.setText(nv.getSdt());

        if (nv.getNgaySinh() != null) {
            dcNgaySinh.setDate(XDate.toDate(nv.getNgaySinh(), "yyyy-MM-dd"));
        } else {
            dcNgaySinh.setDate(null);
        }
        if (nv.getNgayVaoLam() != null) {
            dcNgayLamViec.setDate(XDate.toDate(nv.getNgayVaoLam(), "yyyy-MM-dd"));
        } else {
            dcNgayLamViec.setDate(null);
        }

        if (nv.isVaiTro()) {
            rdoQL.setSelected(true);
            fillCboQuanLy();
        } else if (!nv.isVaiTro()) {
            rdoNV.setSelected(true);
            fillComboBox();
        } else {
            btnVaiTro.clearSelection();
        }
        if (this.i >= 0) {
            cboLoaiCV.setSelectedItem(tblDanhSach.getValueAt(this.i, 8).toString());
        }

        if (nv.isGioiTinh()) {
            rdoNam.setSelected(true);
        } else if (!nv.isGioiTinh()) {
            rdoNu.setSelected(true);
        } else {
            btnGioiTinh.clearSelection();
        }
        jTabbedPane3.setSelectedIndex(0);
    }

    public NhanVien getForm() {
        NhanVien nv = new NhanVien();
        nv.setMaNV(txtMaNV.getText());
        String message = txtHoTen.getText();
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
        nv.setHoTen(message);
        nv.setDiaChi(txtDiaChi.getText());
        nv.setEmail(txtEmail.getText());
        nv.setMaCV(getMaCV());
        nv.setMatKhau(new String(txtMatKhau.getPassword()));
        nv.setNgaySinh(XDate.toString(dcNgaySinh.getDate(), "yyyy-MM-dd"));
        nv.setSdt(txtSDT.getText());
        nv.setNgayVaoLam(XDate.toString(dcNgayLamViec.getDate(), "yyyy-MM-dd"));

        if (rdoNV.isSelected()) {
            nv.setVaiTro(false);
        } else if (rdoQL.isSelected()) {
            nv.setVaiTro(true);
        }

        if (rdoNam.isSelected()) {
            nv.setGioiTinh(true);
        } else if (rdoNu.isSelected()) {
            nv.setGioiTinh(false);
        }

        return nv;
    }

    public void clearErrorMess() {
        setErrorMessage(lblMaNV, "", "");
        setErrorMessage(lblChucVu, "", "");
        setErrorMessage(lblTenNV, "", "");
        setErrorMessage(lblEmail, "", "");
        setErrorMessage(lblGioiTinh, "", "");
        setErrorMessage(lblMatKhau, "", "");
        setErrorMessage(lblNgayVaoLam, "", "");
        setErrorMessage(lblNgaySinh, "", "");
        setErrorMessage(lblVaiTro, "", "");
        setErrorMessage(lblSDT, "", "");

    }

    public void clearForm() {
        this.i = -1;
        NhanVien nv = new NhanVien();
        this.setForm(nv);
        this.updateStatus();
        if (Auth.isManager()) {
            btnVaiTro.clearSelection();
            rdoQL.setEnabled(true);
            rdoNV.setEnabled(true);
        }
        clearErrorMess();
    }

    public void edit() {
        String maNV = (String) tblDanhSach.getValueAt(this.i, 0);
        NhanVien nv = dao.selectById(maNV);
        this.setForm(nv);
        this.updateStatus();
        if (Auth.user.getMaNV().equals(txtMaNV.getText())) {
            rdoNV.setEnabled(false);
            rdoQL.setEnabled(false);
        } else {
            rdoNV.setEnabled(true);
            rdoQL.setEnabled(true);
        }
        jTabbedPane3.setSelectedIndex(0);
    }

    public void insert() {
        if (Auth.isManager()) {
            NhanVien nv = getForm();
            try {
                dao.insert(nv);
                this.fillToTable();
                this.clearForm();
                JOptionPane.showMessageDialog(this, "Thêm mới thành công!");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(this, "Thêm mới thất bại");
            }

        } else {
            JOptionPane.showMessageDialog(this, "Bạn không có quyền thêm nhân viên");
        }
        clearForm();
    }

    public void update() {
        if (Auth.isManager()) {
            NhanVien nv = getForm();
            try {
                dao.update(nv);
                this.fillToTable();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
            }

        } else {
            JOptionPane.showMessageDialog(this, "Bạn không có quyền cập nhật thông tin");
        }
        clearForm();
    }

    public void delete() {
        if (!Auth.isManager()) {
            JOptionPane.showMessageDialog(this, "Bạn không có quyền xóa nhân viên!");
        } else {
            String maNV = txtMaNV.getText();
            if (maNV.equals(Auth.user.getMaNV())) {
                JOptionPane.showMessageDialog(this, "Bạn không thể xóa chính mình!");
            } else if (!checkExists()) {
                return;
            } else if (JOptionPane.showConfirmDialog(this, "Bạn có thật sự muốn xóa nhân viên này?") == JOptionPane.YES_OPTION) {
                try {
                    dao.delete(maNV);
                    this.fillToTable();
                    this.clearForm();
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!");
                    System.out.println(e.getMessage());
                }
            }
        }
        clearForm();
    }

    public boolean checkInsert() {
        boolean flagForm = true;
        if (txtMaNV.getText().isBlank()) {
            setErrorMessage(lblMaNV, "Hãy nhập mã nhân viên", iconName);
            flagForm = false;
        } else {
            setErrorMessage(lblMaNV, "", "");
            List<NhanVien> nv = dao.selectAll();
            for (NhanVien item : nv) {
                if (item.getMaNV().equalsIgnoreCase(txtMaNV.getText())) {
                    setErrorMessage(lblMaNV, "Mã nhân viên đã tồn tại", iconName);
                    flagForm = false;
                    break;
                } else if (txtMaNV.getText().length() > 7) {
                    setErrorMessage(lblMaNV, "Mã nhân viên không quá 7 kí tự", iconName);
                    flagForm = false;
                }
            }

        }
        String pattenName = "^[^!-@]+$";
        if (txtHoTen.getText().isBlank()) {
            setErrorMessage(lblTenNV, "Hãy nhập tên nhân viên", iconName);
            flagForm = false;
        } else {
            setErrorMessage(lblTenNV, "", "");
            if (!txtHoTen.getText().matches(pattenName)) {
                setErrorMessage(lblTenNV, "Sai định dạng tên!", iconName);
                flagForm = false;
            }
        }

        String pattenSDT = "^(032|033|034|035|036|037|038|039|096|097|098|086|083|084|085|081|082|088|091|094|070|079|077|076|078|090|093|089|056|058|092|059|099)[0-9]{7}$";
        if (txtSDT.getText().isBlank()) {
            setErrorMessage(lblSDT, "Hãy nhập SĐT", iconName);
            flagForm = false;
        } else {
            setErrorMessage(lblSDT, "", "");
            if (!txtSDT.getText().matches(pattenSDT)) {
                setErrorMessage(lblSDT, "SĐT không đúng định dạng!", iconName);
                flagForm = false;
            }
        }
        if (dcNgayLamViec.getDate() == null) {
            setErrorMessage(lblNgayVaoLam, "Hãy nhập ngày vào làm", iconName);
            flagForm = false;
        } else {
            Date ngayLamViec = dcNgayLamViec.getDate();
            LocalDate nlv = ngayLamViec.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int yearLV = nlv.getYear();
            int month = nlv.getMonthValue();
            int day = nlv.getDayOfMonth();
            setErrorMessage(lblNgayVaoLam, "", "");
            if (((now.getYear() - yearLV) >= 0) && ((now.getMonthValue() - month) >= 0) && ((now.getDayOfMonth() - day > 0))) {
                flagForm = false;
                setErrorMessage(lblNgayVaoLam, "Ngày vào làm không phù hợp", "warning.jpg");
            }
        }
        String patternPass = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,12}$";
        if (new String(txtMatKhau.getPassword()).isBlank()) {
            setErrorMessage(lblMatKhau, "Hãy nhập mật khẩu", iconName);
            flagForm = false;
        } else {
            setErrorMessage(lblMatKhau, "", "");
            if (txtMatKhau.getPassword().length < 8 || txtMatKhau.getPassword().length > 12) {
                setErrorMessage(lblMatKhau, "Mật khẩu phải từ 8 - 12 kí tự!", iconName);
                flagForm = false;
            } else {
                setErrorMessage(lblMatKhau, "", "");
                if (!new String(txtMatKhau.getPassword()).matches(patternPass)) {
                    setErrorMessage(lblMatKhau, "Mật khẩu phải có cả chữ và số!", iconName);
                    flagForm = false;
                }
            }
        }
        if (!rdoNV.isSelected() && !rdoQL.isSelected()) {
            setErrorMessage(lblChucVu, "Hãy chọn chức vụ", iconName);
            flagForm = false;
        } else {
            setErrorMessage(lblChucVu, "", "");
        }

        if (!rdoNam.isSelected() && !rdoNu.isSelected()) {
            setErrorMessage(lblGioiTinh, "Hãy chọn giới tính", iconName);
            flagForm = false;
        } else {
            setErrorMessage(lblGioiTinh, "", "");
        }

        String pantentEmail = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$";
        if (txtEmail.getText().isBlank()) {
            setErrorMessage(lblEmail, "Hãy nhập Email", iconName);
            flagForm = false;
        } else {
            setErrorMessage(lblEmail, "", "");
            if (!txtEmail.getText().matches(pantentEmail)) {
                setErrorMessage(lblEmail, "Sai định dạng Email", iconName);
                flagForm = false;
            }
        }

        if (dcNgaySinh.getDate() == null) {
            setErrorMessage(lblNgaySinh, "Hãy nhập ngày sinh", iconName);
            flagForm = false;
        } else {
            setErrorMessage(lblNgaySinh, "", "");
            Date ngaySinh = dcNgaySinh.getDate();
            LocalDate dd = ngaySinh.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int year = dd.getYear();
            if ((now.getYear() - year) < 18) {
                setErrorMessage(lblNgaySinh, "Nhân viên phải từ 18 tuổi", iconName);
                flagForm = false;

            }
        }

        if (!rdoNV.isSelected() && !rdoQL.isSelected()) {
            setErrorMessage(lblVaiTro, "Hãy chọn vai trò", iconName);
            flagForm = false;
        } else {
            setErrorMessage(lblVaiTro, "", "");
        }

        return flagForm;
    }
    public boolean checkUpdate() {
        boolean flagForm = true;
       

        
        String pattenName = "^[^!-@]+$";
        if (txtHoTen.getText().isBlank()) {
            setErrorMessage(lblTenNV, "Hãy nhập tên nhân viên", iconName);
            flagForm = false;
        } else {
            setErrorMessage(lblTenNV, "", "");
            if (!txtHoTen.getText().matches(pattenName)) {
                setErrorMessage(lblTenNV, "Sai định dạng tên!", iconName);
                flagForm = false;
            }
        }

        String pattenSDT = "^(032|033|034|035|036|037|038|039|096|097|098|086|083|084|085|081|082|088|091|094|070|079|077|076|078|090|093|089|056|058|092|059|099)[0-9]{7}$";
        if (txtSDT.getText().isBlank()) {
            setErrorMessage(lblSDT, "Hãy nhập SĐT", iconName);
            flagForm = false;
        } else {
            setErrorMessage(lblSDT, "", "");
            if (!txtSDT.getText().matches(pattenSDT)) {
                setErrorMessage(lblSDT, "SĐT không đúng định dạng!", iconName);
                flagForm = false;
            }
        }
       
        String patternPass = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,12}$";
        if (new String(txtMatKhau.getPassword()).isBlank()) {
            setErrorMessage(lblMatKhau, "Hãy nhập mật khẩu", iconName);
            flagForm = false;
        } else {
            setErrorMessage(lblMatKhau, "", "");
            if (txtMatKhau.getPassword().length < 8 || txtMatKhau.getPassword().length > 12) {
                setErrorMessage(lblMatKhau, "Mật khẩu phải từ 8 - 12 kí tự!", iconName);
                flagForm = false;
            } else {
                setErrorMessage(lblMatKhau, "", "");
                if (!new String(txtMatKhau.getPassword()).matches(patternPass)) {
                    setErrorMessage(lblMatKhau, "Mật khẩu phải có cả chữ và số!", iconName);
                    flagForm = false;
                }
            }
        }
        if (!rdoNV.isSelected() && !rdoQL.isSelected()) {
            setErrorMessage(lblChucVu, "Hãy chọn chức vụ", iconName);
            flagForm = false;
        } else {
            setErrorMessage(lblChucVu, "", "");
        }

        if (!rdoNam.isSelected() && !rdoNu.isSelected()) {
            setErrorMessage(lblGioiTinh, "Hãy chọn giới tính", iconName);
            flagForm = false;
        } else {
            setErrorMessage(lblGioiTinh, "", "");
        }

        String pantentEmail = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$";
        if (txtEmail.getText().isBlank()) {
            setErrorMessage(lblEmail, "Hãy nhập Email", iconName);
            flagForm = false;
        } else {
            setErrorMessage(lblEmail, "", "");
            if (!txtEmail.getText().matches(pantentEmail)) {
                setErrorMessage(lblEmail, "Sai định dạng Email", iconName);
                flagForm = false;
            }
        }

        if (dcNgaySinh.getDate() == null) {
            setErrorMessage(lblNgaySinh, "Hãy nhập ngày sinh", iconName);
            flagForm = false;
        } else {
            setErrorMessage(lblNgaySinh, "", "");
            Date ngaySinh = dcNgaySinh.getDate();
            LocalDate dd = ngaySinh.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int year = dd.getYear();
            if ((now.getYear() - year) < 18) {
                setErrorMessage(lblNgaySinh, "Nhân viên phải từ 18 tuổi", iconName);
                flagForm = false;

            }
        }

        if (!rdoNV.isSelected() && !rdoQL.isSelected()) {
            setErrorMessage(lblVaiTro, "Hãy chọn vai trò", iconName);
            flagForm = false;
        } else {
            setErrorMessage(lblVaiTro, "", "");
        }

        return flagForm;
    }
    public void getLuong() {
        try {
            String id = (String) cboLoaiCV.getSelectedItem();
            List<ChucVu> list = CVdao.selectByKeyword(id);
            for (ChucVu cv : list) {
                txtLuong.setText(decimalFormat.format(cv.getLuong()) + "");

            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    public String getMaCV() {
        String maCV = "";
        try {
            String key = (String) cboLoaiCV.getSelectedItem();

            List<ChucVu> list = CVdao.selectByKeyword(key);
            for (ChucVu cv : list) {
                maCV = cv.getMaCV();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return maCV;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGioiTinh = new javax.swing.ButtonGroup();
        btnVaiTro = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        rdoNam = new javax.swing.JRadioButton();
        rdoNu = new javax.swing.JRadioButton();
        txtMaNV = new javax.swing.JTextField();
        txtHoTen = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtSDT = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtDiaChi = new javax.swing.JTextArea();
        rdoQL = new javax.swing.JRadioButton();
        rdoNV = new javax.swing.JRadioButton();
        jLabel35 = new javax.swing.JLabel();
        txtMatKhau = new javax.swing.JPasswordField();
        cboLoaiCV = new javax.swing.JComboBox<>();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        dcNgayLamViec = new com.toedter.calendar.JDateChooser();
        dcNgaySinh = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        txtLuong = new javax.swing.JTextField();
        lblMaNV = new javax.swing.JLabel();
        lblTenNV = new javax.swing.JLabel();
        lblGioiTinh = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblSDT = new javax.swing.JLabel();
        lblNgaySinh = new javax.swing.JLabel();
        lblChucVu = new javax.swing.JLabel();
        lblMatKhau = new javax.swing.JLabel();
        lblNgayVaoLam = new javax.swing.JLabel();
        lblVaiTro = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblDanhSach = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        txtTim = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Quản Lý Nhân Viên");

        jTabbedPane3.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setPreferredSize(new java.awt.Dimension(25, 25));

        jLabel25.setText("Mã nhân viên:");

        jLabel26.setText("Họ và tên:");

        jLabel27.setText("Giới tính:");

        jLabel28.setText("Email:");

        jLabel29.setText("Số điện thoại:");

        jLabel30.setText("Ngày sinh:");

        jLabel31.setText("Địa chỉ:");

        jLabel32.setText("Vai trò:");

        jLabel33.setText("Loại công việc:");

        jLabel34.setText("Ngày vào làm:");

        rdoNam.setBackground(new java.awt.Color(255, 255, 255));
        btnGioiTinh.add(rdoNam);
        rdoNam.setText("Nam");
        rdoNam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNamActionPerformed(evt);
            }
        });

        rdoNu.setBackground(new java.awt.Color(255, 255, 255));
        btnGioiTinh.add(rdoNu);
        rdoNu.setText("Nữ");
        rdoNu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNuActionPerformed(evt);
            }
        });

        txtMaNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaNVActionPerformed(evt);
            }
        });

        txtHoTen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHoTenActionPerformed(evt);
            }
        });

        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });

        txtSDT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSDTActionPerformed(evt);
            }
        });

        txtDiaChi.setColumns(20);
        txtDiaChi.setRows(5);
        jScrollPane4.setViewportView(txtDiaChi);

        rdoQL.setBackground(new java.awt.Color(255, 255, 255));
        btnVaiTro.add(rdoQL);
        rdoQL.setText("Quản lí");
        rdoQL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoQLActionPerformed(evt);
            }
        });

        rdoNV.setBackground(new java.awt.Color(255, 255, 255));
        btnVaiTro.add(rdoNV);
        rdoNV.setText("Nhân viên");
        rdoNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNVActionPerformed(evt);
            }
        });

        jLabel35.setText("Mật khẩu:");

        txtMatKhau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMatKhauActionPerformed(evt);
            }
        });

        cboLoaiCV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLoaiCVActionPerformed(evt);
            }
        });

        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/save.png"))); // NOI18N
        btnThem.setText("Thêm");
        btnThem.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/update.png"))); // NOI18N
        btnSua.setText("Sửa");
        btnSua.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/delete.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/DoiMK.png"))); // NOI18N
        btnLamMoi.setText("Làm mới");
        btnLamMoi.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        btnFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/first.png"))); // NOI18N
        btnFirst.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/prev.png"))); // NOI18N
        btnPrev.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/next.png"))); // NOI18N
        btnNext.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/last.png"))); // NOI18N
        btnLast.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        jLabel2.setText("Lương (VNĐ):");

        txtLuong.setEditable(false);
        txtLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLuongActionPerformed(evt);
            }
        });

        lblMaNV.setBackground(new java.awt.Color(255, 255, 255));
        lblMaNV.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblMaNV.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblMaNV.setPreferredSize(new java.awt.Dimension(25, 25));

        lblTenNV.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTenNV.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTenNV.setPreferredSize(new java.awt.Dimension(25, 25));

        lblGioiTinh.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblGioiTinh.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblGioiTinh.setPreferredSize(new java.awt.Dimension(25, 25));

        lblEmail.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblEmail.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEmail.setPreferredSize(new java.awt.Dimension(25, 25));

        lblSDT.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSDT.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSDT.setPreferredSize(new java.awt.Dimension(25, 25));

        lblNgaySinh.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNgaySinh.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNgaySinh.setPreferredSize(new java.awt.Dimension(25, 25));

        lblChucVu.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblChucVu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        lblMatKhau.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblMatKhau.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblMatKhau.setPreferredSize(new java.awt.Dimension(25, 25));

        lblNgayVaoLam.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNgayVaoLam.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNgayVaoLam.setPreferredSize(new java.awt.Dimension(25, 25));

        lblVaiTro.setBackground(new java.awt.Color(255, 255, 255));
        lblVaiTro.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblVaiTro.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblVaiTro.setPreferredSize(new java.awt.Dimension(25, 25));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lblMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(jLabel25)
                            .addGap(18, 18, 18)
                            .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel34)
                                        .addGap(18, 18, 18))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGap(3, 3, 3)
                                                .addComponent(jLabel27)))
                                        .addGap(35, 35, 35)))
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(rdoNam, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rdoNu, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(dcNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblNgayVaoLam, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dcNgayLamViec, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(57, 57, 57)
                        .addComponent(jLabel35))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(jLabel31)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(53, 53, 53))
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel33)
                                                .addComponent(jLabel2))
                                            .addGap(18, 18, 18)))
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(rdoNV)
                                                .addGap(18, 18, 18)
                                                .addComponent(rdoQL))
                                            .addComponent(cboLoaiCV, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtMatKhau)
                                            .addComponent(lblMatKhau, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblChucVu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblVaiTro, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(96, 96, 96)
                                .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdoNV)
                            .addComponent(rdoQL)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblVaiTro, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33)
                    .addComponent(cboLoaiCV, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rdoNam)
                                    .addComponent(rdoNu))
                                .addGap(4, 4, 4)
                                .addComponent(lblGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0)
                                .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dcNgayLamViec, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addComponent(lblNgayVaoLam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addComponent(lblSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dcNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(lblNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(lblMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(133, 133, 133)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(39, 39, 39))
        );

        jTabbedPane3.addTab("Cập nhật", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

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
        tblDanhSach.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDanhSachMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblDanhSach);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm nhân viên theo họ tên:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14), new java.awt.Color(255, 0, 0))); // NOI18N

        txtTim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTim, javax.swing.GroupLayout.DEFAULT_SIZE, 871, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(txtTim, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 907, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(125, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Danh sách", jPanel5);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 87, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 960, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTabbedPane3, javax.swing.GroupLayout.Alignment.LEADING))))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 612, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
        if (this.i > 0) {
            this.i--;
            this.edit();
        }
    }//GEN-LAST:event_btnPrevActionPerformed

    private void txtLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLuongActionPerformed
        // TODO add your handling code here
    }//GEN-LAST:event_txtLuongActionPerformed

    private void cboLoaiCVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLoaiCVActionPerformed
        // TODO add your handling code here:
        if (cboLoaiCV.getSelectedIndex() >= 0) {
            setErrorMessage(lblChucVu, "", "");
        }
        getLuong();
    }//GEN-LAST:event_cboLoaiCVActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        if (Auth.isManager()) {
            if (checkInsert()) {
                insert();
                btnVaiTro.clearSelection();
                btnGioiTinh.clearSelection();
            }
        } else {
            MainThongBao.alert(this, "Bạn không có quyền thêm nhân viên!");
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        // TODO add your handling code here:
        clearForm();
        btnGioiTinh.clearSelection();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        if (Auth.isManager()) {
            if (checkUpdate()) {
                update();
                btnVaiTro.clearSelection();
                btnGioiTinh.clearSelection();
            }
        } else {
            MainThongBao.alert(this, "Bạn không có quyền cập nhật thông tin nhân viên!");
        }

    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        if (Auth.isManager()) {
            delete();
            btnVaiTro.clearSelection();
            btnGioiTinh.clearSelection();
        } else {
            MainThongBao.alert(this, "Bạn không có quyền xóa nhân viên!");
        }
    }//GEN-LAST:event_btnXoaActionPerformed

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

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        // TODO add your handling code here:
        this.i = 0;
        this.edit();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void tblDanhSachMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDanhSachMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.i = tblDanhSach.getSelectedRow();
            this.edit();
            clearErrorMess();
            if (Auth.user.getMaNV().equals(txtMaNV.getText())) {
                rdoNV.setEnabled(false);
                rdoQL.setEnabled(false);
            } else {
                rdoNV.setEnabled(true);
                rdoQL.setEnabled(true);
            }
        }

    }//GEN-LAST:event_tblDanhSachMouseClicked

    private void rdoNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNVActionPerformed
        // TODO add your handling code here:
        if (rdoNV.isSelected()) {
            fillComboBox();
            setErrorMessage(lblVaiTro, "", "");
        }
    }//GEN-LAST:event_rdoNVActionPerformed

    private void rdoQLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoQLActionPerformed
        // TODO add your handling code here:
        if (rdoQL.isSelected()) {
            fillCboQuanLy();
            setErrorMessage(lblVaiTro, "", "");
        }
    }//GEN-LAST:event_rdoQLActionPerformed

    private void txtMaNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaNVActionPerformed
        // TODO add your handling code here:
        if (!txtMaNV.getText().isBlank()) {
            setErrorMessage(lblMaNV, "", "");
        }
    }//GEN-LAST:event_txtMaNVActionPerformed

    private void txtHoTenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHoTenActionPerformed
        // TODO add your handling code here:
        if (!txtHoTen.getText().isBlank()) {
            setErrorMessage(lblTenNV, "", "");
        }
    }//GEN-LAST:event_txtHoTenActionPerformed

    private void rdoNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNamActionPerformed
        // TODO add your handling code here:
        if (rdoNam.isSelected()) {
            setErrorMessage(lblGioiTinh, "", "");
        }
    }//GEN-LAST:event_rdoNamActionPerformed

    private void rdoNuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNuActionPerformed
        // TODO add your handling code here:
        if (rdoNu.isSelected()) {
            setErrorMessage(lblGioiTinh, "", "");
        }
    }//GEN-LAST:event_rdoNuActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
        if (!txtEmail.getText().isBlank()) {
            setErrorMessage(lblEmail, "", "");
        }
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtSDTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSDTActionPerformed
        // TODO add your handling code here:
        if (!txtSDT.getText().isBlank()) {
            setErrorMessage(lblSDT, "", "");
        }
    }//GEN-LAST:event_txtSDTActionPerformed

    private void txtMatKhauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMatKhauActionPerformed
        // TODO add your handling code here:
        if (!new String(txtMatKhau.getPassword()).isBlank()) {
            setErrorMessage(lblMatKhau, "", "");
        }
    }//GEN-LAST:event_txtMatKhauActionPerformed

    private void txtTimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimActionPerformed
        // TODO add your handling code here:
        fillToTable();
    }//GEN-LAST:event_txtTimActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFirst;
    private javax.swing.ButtonGroup btnGioiTinh;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.ButtonGroup btnVaiTro;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboLoaiCV;
    private com.toedter.calendar.JDateChooser dcNgayLamViec;
    private com.toedter.calendar.JDateChooser dcNgaySinh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JLabel lblChucVu;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblGioiTinh;
    private javax.swing.JLabel lblMaNV;
    private javax.swing.JLabel lblMatKhau;
    private javax.swing.JLabel lblNgaySinh;
    private javax.swing.JLabel lblNgayVaoLam;
    private javax.swing.JLabel lblSDT;
    private javax.swing.JLabel lblTenNV;
    private javax.swing.JLabel lblVaiTro;
    private javax.swing.JRadioButton rdoNV;
    private javax.swing.JRadioButton rdoNam;
    private javax.swing.JRadioButton rdoNu;
    private javax.swing.JRadioButton rdoQL;
    private javax.swing.JTable tblDanhSach;
    private javax.swing.JTextArea txtDiaChi;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtLuong;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JPasswordField txtMatKhau;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTim;
    // End of variables declaration//GEN-END:variables
//code by thuysvy
}
