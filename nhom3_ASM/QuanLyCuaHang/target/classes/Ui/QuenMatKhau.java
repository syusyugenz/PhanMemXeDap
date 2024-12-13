/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Ui;

import DAO.NhanVienDAO;
import Entity.NhanVien;
import java.awt.Color;
import java.awt.Image;
import java.util.Properties;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import utils.MainThongBao;
import utils.XImage;

/**
 *
 * @author pc
 */
public class QuenMatKhau extends javax.swing.JFrame {

    /**
     * Creates new form QuenMatKhau
     */
    public QuenMatKhau(JFrame frame, boolean par) {
        initComponents();
        setLocationRelativeTo(null);
    }
    
    JFrame frame = new JFrame();
    int otp;
    NhanVienDAO dao = new NhanVienDAO();

    public boolean check() {
        boolean flag = true;
        String maNV = txtTenTK.getText();

        String email = txtEmail.getText();
        NhanVien nv = dao.selectById(maNV);
        if (nv != null) {
            if (maNV.isBlank()) {
                setErrorMessage(lblCheckTenTK, "Vui lòng nhập tên tài khoản", icon);
                flag = false;
            } else {
                setErrorMessage(lblCheckTenTK, "", "");
            }
            if (!nv.getEmail().equals(email)) {
                flag = false;
                setErrorMessage(lblCheckMail, "Email không đúng!", icon);
            } else {
                setErrorMessage(lblCheckMail, "", "");
            }
        } else {
            MainThongBao.alert(this, "Không tìm thấy tài khoản!");
            flag = false;
        }

        return flag;
    }
    String icon = "warning.jpg";

    public void setErrorMessage(JLabel label, String mess, String icon) {
        label.setText(mess);
        label.setForeground(Color.red);
        Image img = XImage.read(icon).getImage();
        label.setIcon(new ImageIcon(img));
    }

    public void clearErrorMess() {
        setErrorMessage(lblCheckMaXN, "", "");
        setErrorMessage(lblCheckMail, "", "");
        setErrorMessage(lblCheckTenTK, "", "");
    }

    public void getOTP() {
        final String username = "thanhxvh@gmail.com";
	final String password = "mljkrostjvpqgxqu";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2"); 
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(username, password);
            }
        });
        Random ran = new Random();
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("Thanhvh"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(txtEmail.getText())
            );
            message.setSubject("Verify OTP");
            otp = ran.nextInt(9999);
            message.setText("Your OTP is: " + String.valueOf(otp));

            Transport.send(message);
            MainThongBao.alert(this, "Đã gửi mã OTP!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    int width = 430;
    int height = 391;

    void openCPWBar() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < width; i++) {
                    pnDoiMK.setSize(i, height);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        }).start();
    }

    void openMainBar() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < width; i++) {
                    pnMain.setSize(i, height);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        }).start();
    }

    void closeCPWBar() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = width; i > 0; i--) {
                    pnDoiMK.setSize(i, height);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        }).start();
    }

    void closeMainBar() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = width; i > 0; i--) {
                    pnMain.setSize(i, height);
                    pnMain.setLocation(pnMain.getX() + i, pnMain.getY());
                }
            }
        }).start();
    }

    boolean checkMatKhau() {
        boolean flag = true;
        String matKhauMoi = new String(txtMKMoi.getPassword());
        String nhapLaiMK = new String(txtNhapLaiMK.getPassword());
        String patternPass = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,12}$";
        if (matKhauMoi.isBlank()) {
            MainThongBao.alert(this, "Hãy nhập mật khẩu");
            flag = false;
        } else {
            if (matKhauMoi.length() < 8 || matKhauMoi.length() > 12) {
                MainThongBao.alert(this, "Mật khẩu phải từ 8 - 12 kí tự!");
                flag = false;
            } else {
                if (!matKhauMoi.matches(patternPass)) {
                    MainThongBao.alert(this, "Mật khẩu phải có cả chữ và số!");
                    flag = false;
                } else {
                    if (nhapLaiMK.isBlank()) {
                        MainThongBao.alert(this, "Hãy nhập lại mật khẩu");
                        flag = false;
                    } else {
                        if (!matKhauMoi.equals(nhapLaiMK)) {
                            MainThongBao.alert(this, "Mật khẩu không trùng khớp!");
                            flag = false;
                        }
                    }
                }
            }

        }

        return flag;
    }

    public void doiMatKhau() {
        String maNV = txtTenTK.getText();
        NhanVien nv = dao.selectById(maNV);
        String matKhauMoi = new String(txtMKMoi.getPassword());
        String nhapLaiMK = new String(txtNhapLaiMK.getPassword());
        nv.setMatKhau(matKhauMoi);
        dao.update(nv);
        MainThongBao.alert(this, "Đổi mật khẩu thành công!");
        txtMKMoi.setText("");
        txtNhapLaiMK.setText("");
        this.dispose();
        new DangNhap(frame, true).setVisible(true);
    }

    void hienThiPass(JPasswordField pass, JLabel lbl) {
        Image img = XImage.read("theEyes.jpg").getImage();
        lbl.setIcon(new ImageIcon(img));
        pass.setEchoChar((char) 0);
    }

    void hidePass(JPasswordField pass, JLabel lbl) {
        Image img = XImage.read("eyebrow.png").getImage();
        lbl.setIcon(new ImageIcon(img));
        pass.setEchoChar('*');
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
        jLabel3 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        pnMain = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btnXacNhan = new javax.swing.JButton();
        btnXacThuc = new javax.swing.JButton();
        txtTenTK = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        lblCheckTenTK = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        lblCheckMail = new javax.swing.JLabel();
        txtMaXN = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        lblCheckMaXN = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        pnDoiMK = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnDoiMK = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        txtMKMoi = new javax.swing.JPasswordField();
        txtNhapLaiMK = new javax.swing.JPasswordField();
        lblHienThiMKMoi = new javax.swing.JLabel();
        lblHienThiNLMK = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Quay Lai Dang Nhap");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 390, 212, 35));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Logo_TrangChu.jpeg"))); // NOI18N
        jLabel11.setText("jLabel11");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 470, 390));

        pnMain.setBackground(new java.awt.Color(255, 255, 255));
        pnMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/NhanVien.png"))); // NOI18N
        jLabel8.setText("TaiKhoan");
        pnMain.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 80, 30));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/gmail.png"))); // NOI18N
        jLabel9.setText("Email");
        pnMain.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 70, 20));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/DoiMK.png"))); // NOI18N
        jLabel10.setText("MaXacNhan");
        pnMain.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, -1, -1));

        btnXacNhan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/verify.png"))); // NOI18N
        btnXacNhan.setText("XNhan");
        btnXacNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXacNhanActionPerformed(evt);
            }
        });
        pnMain.add(btnXacNhan, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 240, -1, -1));

        btnXacThuc.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXacThuc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/login.png"))); // NOI18N
        btnXacThuc.setText("Xac Thuc Tai Khoan");
        btnXacThuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXacThucActionPerformed(evt);
            }
        });
        pnMain.add(btnXacThuc, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 330, -1, -1));

        txtTenTK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenTKActionPerformed(evt);
            }
        });
        pnMain.add(txtTenTK, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 200, -1));
        pnMain.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 210, 10));
        pnMain.add(lblCheckTenTK, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, 200, 30));

        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });
        pnMain.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, 200, 20));
        pnMain.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 190, 210, 10));
        pnMain.add(lblCheckMail, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 200, 210, 30));

        txtMaXN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaXNActionPerformed(evt);
            }
        });
        pnMain.add(txtMaXN, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 250, 160, -1));
        pnMain.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 280, 190, 10));
        pnMain.add(lblCheckMaXN, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 280, 160, 20));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("QUENMATKHAU");
        pnMain.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 0, 259, 57));

        pnDoiMK.setBackground(new java.awt.Color(255, 255, 255));
        pnDoiMK.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setText("TAO MK MOI");
        pnDoiMK.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 100, 40));

        jLabel6.setText("Nhap MK Moi");
        pnDoiMK.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 80, 30));

        jLabel7.setText("NLai MK Moi");
        pnDoiMK.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 80, 30));

        btnDoiMK.setText("DOI MAT KHAU");
        btnDoiMK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoiMKActionPerformed(evt);
            }
        });
        pnDoiMK.add(btnDoiMK, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 230, 120, 30));
        pnDoiMK.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, 150, 20));
        pnDoiMK.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 183, 160, 10));
        pnDoiMK.add(txtMKMoi, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 150, 20));
        pnDoiMK.add(txtNhapLaiMK, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 150, 150, 20));

        lblHienThiMKMoi.setText("HThi");
        lblHienThiMKMoi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblHienThiMKMoiMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblHienThiMKMoiMouseExited(evt);
            }
        });
        pnDoiMK.add(lblHienThiMKMoi, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 70, -1, -1));

        lblHienThiNLMK.setText("HThi");
        lblHienThiNLMK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblHienThiNLMKMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblHienThiNLMKMouseExited(evt);
            }
        });
        pnDoiMK.add(lblHienThiNLMK, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 160, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnDoiMK, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnMain, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnDoiMK, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnXacNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanActionPerformed
        // TODO add your handling code here:
        if (check()) {
            getOTP();
        }
    }//GEN-LAST:event_btnXacNhanActionPerformed

    private void btnXacThucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacThucActionPerformed
        // TODO add your handling code here:
        if (txtMaXN.getText().isBlank()) {
            MainThongBao.alert(this, "Vui lòng nhập mã xác nhận!");
        } else {
            if (Integer.parseInt(txtMaXN.getText()) != otp) {
                setErrorMessage(lblCheckMaXN, "Mã xác nhận không đúng!", icon);
            } else {
                openCPWBar();
                closeMainBar();
            }
        }
    }//GEN-LAST:event_btnXacThucActionPerformed

    private void txtTenTKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenTKActionPerformed
        // TODO add your handling code here:
        if (!txtTenTK.getText().isBlank()) {
            setErrorMessage(lblCheckTenTK, "", "");
        }
    }//GEN-LAST:event_txtTenTKActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
        if (!txtEmail.getText().isBlank()) {
            setErrorMessage(lblCheckMail, "", "");
        }
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtMaXNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaXNActionPerformed
        // TODO add your handling code here:
        if (!txtMaXN.getText().isBlank()) {
            setErrorMessage(lblCheckMaXN, "", "");
        }
    }//GEN-LAST:event_txtMaXNActionPerformed

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        // TODO add your handling code here:
        this.dispose();
        new DangNhap(this.frame, true).setVisible(true);
    }//GEN-LAST:event_jLabel3MouseClicked

    private void lblHienThiNLMKMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHienThiNLMKMouseExited
        // TODO add your handling code here:
        hidePass(txtNhapLaiMK, lblHienThiNLMK);
    }//GEN-LAST:event_lblHienThiNLMKMouseExited

    private void lblHienThiNLMKMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHienThiNLMKMouseEntered
        // TODO add your handling code here:
        hienThiPass(txtNhapLaiMK, lblHienThiNLMK);
    }//GEN-LAST:event_lblHienThiNLMKMouseEntered

    private void lblHienThiMKMoiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHienThiMKMoiMouseExited
        // TODO add your handling code here:
        hidePass(txtMKMoi, lblHienThiMKMoi);
    }//GEN-LAST:event_lblHienThiMKMoiMouseExited

    private void lblHienThiMKMoiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHienThiMKMoiMouseEntered
        // TODO add your handling code here:
        hienThiPass(txtMKMoi, lblHienThiMKMoi);
    }//GEN-LAST:event_lblHienThiMKMoiMouseEntered

    private void btnDoiMKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoiMKActionPerformed
        // TODO add your handling code here:
        if (checkMatKhau()) {
            doiMatKhau();
        }
    }//GEN-LAST:event_btnDoiMKActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(QuenMatKhau.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuenMatKhau.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuenMatKhau.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuenMatKhau.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            private JFrame frame;
            public void run() {
                new QuenMatKhau(this.frame, true).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDoiMK;
    private javax.swing.JButton btnXacNhan;
    private javax.swing.JButton btnXacThuc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JLabel lblCheckMaXN;
    private javax.swing.JLabel lblCheckMail;
    private javax.swing.JLabel lblCheckTenTK;
    private javax.swing.JLabel lblHienThiMKMoi;
    private javax.swing.JLabel lblHienThiNLMK;
    private javax.swing.JPanel pnDoiMK;
    private javax.swing.JPanel pnMain;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JPasswordField txtMKMoi;
    private javax.swing.JTextField txtMaXN;
    private javax.swing.JPasswordField txtNhapLaiMK;
    private javax.swing.JTextField txtTenTK;
    // End of variables declaration//GEN-END:variables
}
