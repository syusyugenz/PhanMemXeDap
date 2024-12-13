/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Ui;

import DAO.NhanVienDAO;
import Entity.NhanVien;
import java.awt.Color;
import java.awt.Image;
import java.net.PasswordAuthentication;
import java.util.Properties;
import java.util.Random;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import utils.Auth;
import utils.MainThongBao;
import utils.XImage;

/**
 *
 * @author drbf1
 */
public class DoiMatKhau extends javax.swing.JDialog {

    /**
     * Creates new form QuenMatKhau
     */
    public DoiMatKhau(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
    }

    NhanVienDAO dao = new NhanVienDAO();

    public void doiMatKhau() {
        String maNV = Auth.user.getMaNV();
        NhanVien nv = dao.selectById(maNV);
        String matKhauMoi = new String(txtMKMoi.getPassword());
        if (check()) {
            nv.setMatKhau(matKhauMoi);
            dao.update(nv);
            MainThongBao.alert(this, "Đổi mật khẩu thành công!");
            clear();
        }
    }
    String icon = "warning.jpg";

    public void setErrorMessage(JLabel label, String mess, String icon) {
        label.setText(mess);
        label.setForeground(Color.red);
        Image img = XImage.read(icon).getImage();
        label.setIcon(new ImageIcon(img));
    }

    public void clearErrorMess() {
        setErrorMessage(lblCheckMKCu, "", "");
        setErrorMessage(lblCheckMKMoi, "", "");
        setErrorMessage(lblCheckNhapLaiMK, "", "");
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

    boolean check() {
        boolean flag = true;
        String matKhauMoi = new String(txtMKMoi.getPassword());
        String nhapLaiMK = new String(txtNhapLaiMK.getPassword());
        String matKhauCu = new String(txtMatKhauCu.getPassword());
        if (matKhauCu.isBlank()) {
            setErrorMessage(lblCheckMKCu, "Vui lòng nhập mật khẩu!", icon);
            flag = false;
        } else {
            setErrorMessage(lblCheckMKCu, "", "");
            if (!matKhauCu.equals(Auth.user.getMatKhau())) {
                setErrorMessage(lblCheckMKCu, "Mật khẩu không đúng!", icon);
                flag = false;
            }
        }
        String patternPass = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,12}$";
        if (matKhauMoi.isBlank()) {
            setErrorMessage(lblCheckMKMoi, "Vui lòng nhập mật khẩu mới!", icon);
            flag = false;
        } else {
            setErrorMessage(lblCheckMKMoi, "", "");
            if (matKhauMoi.equals(matKhauCu)) {
                setErrorMessage(lblCheckMKMoi, "Mật khẩu mới phải khác mật khẩu cũ!", icon);
                flag = false;
            } else {
                if (matKhauMoi.length() < 8 || matKhauMoi.length() > 12) {
                    setErrorMessage(lblCheckMKMoi, "Mật khẩu phải từ 8 - 12 kí tự!", icon);
                    flag = false;
                } else {
                    setErrorMessage(lblCheckMKMoi, "", "");
                    if (!matKhauMoi.matches(patternPass)) {
                        setErrorMessage(lblCheckMKMoi, "Mật khẩu phải có cả chữ và số!", icon);
                        flag = false;
                    }
                }
            }
        }
        if (nhapLaiMK.isBlank()) {
            setErrorMessage(lblCheckNhapLaiMK, "Vui lòng nhập mật khẩu!", icon);
            flag = false;
        } else {
            setErrorMessage(lblCheckNhapLaiMK, "", "");
            if (!nhapLaiMK.equals(matKhauMoi)) {
                setErrorMessage(lblCheckNhapLaiMK, "Mật khẩu không trùng khớp!", icon);
                flag = false;
            } else {
                if (nhapLaiMK.length() < 8 || nhapLaiMK.length() > 12) {
                    setErrorMessage(lblCheckNhapLaiMK, "Mật khẩu phải từ 8 - 12 kí tự!", icon);
                    flag = false;
                } else {
                    setErrorMessage(lblCheckNhapLaiMK, "", "");
                    if (!nhapLaiMK.matches(patternPass)) {
                        setErrorMessage(lblCheckNhapLaiMK, "Mật khẩu phải có cả chữ và số!", icon);
                        flag = false;
                    }
                }
            }
        }
        return flag;
    }

    void clear() {
        txtMKMoi.setText("");
        txtMatKhauCu.setText("");
        txtNhapLaiMK.setText("");
    }
    JFrame frame = new JFrame();
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnDoiMK = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnDoiMK = new javax.swing.JButton();
        lblTitle1 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        txtNhapLaiMK = new javax.swing.JPasswordField();
        txtMKMoi = new javax.swing.JPasswordField();
        jLabel10 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        txtMatKhauCu = new javax.swing.JPasswordField();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblCheckNhapLaiMK = new javax.swing.JLabel();
        lblCheckMKCu = new javax.swing.JLabel();
        lblCheckMKMoi = new javax.swing.JLabel();
        lblHienThiNLMK = new javax.swing.JLabel();
        lblHienThiMKCu = new javax.swing.JLabel();
        lblHienThiMKMoi = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Đổi Mật Khẩu");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnDoiMK.setBackground(new java.awt.Color(255, 255, 255));
        pnDoiMK.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/user1.png"))); // NOI18N
        jLabel5.setText("Mật khẩu mới:");
        pnDoiMK.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 150, -1, 30));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/verified.png"))); // NOI18N
        jLabel6.setText("Nhập lại mật khẩu:");
        pnDoiMK.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 220, -1, 30));

        btnDoiMK.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDoiMK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/DoiMK.png"))); // NOI18N
        btnDoiMK.setText("Đổi mật khẩu");
        btnDoiMK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoiMKActionPerformed(evt);
            }
        });
        pnDoiMK.add(btnDoiMK, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 290, -1, 40));

        lblTitle1.setFont(new java.awt.Font("Sitka Banner", 1, 24)); // NOI18N
        lblTitle1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle1.setText("Tạo Mật Khẩu Mới");
        pnDoiMK.add(lblTitle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, 220, 40));

        jSeparator7.setBackground(new java.awt.Color(0, 204, 204));
        jSeparator7.setForeground(new java.awt.Color(0, 204, 204));
        pnDoiMK.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 240, 240, 10));

        jSeparator8.setBackground(new java.awt.Color(0, 204, 204));
        jSeparator8.setForeground(new java.awt.Color(0, 204, 204));
        pnDoiMK.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 170, 240, 10));

        txtNhapLaiMK.setBorder(null);
        txtNhapLaiMK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNhapLaiMKActionPerformed(evt);
            }
        });
        pnDoiMK.add(txtNhapLaiMK, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 210, 210, 30));

        txtMKMoi.setBorder(null);
        txtMKMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMKMoiActionPerformed(evt);
            }
        });
        pnDoiMK.add(txtMKMoi, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 140, 210, 30));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/user1.png"))); // NOI18N
        jLabel10.setText("Mật khẩu cũ:");
        pnDoiMK.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 80, -1, 30));

        jSeparator9.setBackground(new java.awt.Color(0, 204, 204));
        jSeparator9.setForeground(new java.awt.Color(0, 204, 204));
        pnDoiMK.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 100, 240, 10));

        txtMatKhauCu.setBorder(null);
        txtMatKhauCu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMatKhauCuActionPerformed(evt);
            }
        });
        pnDoiMK.add(txtMatKhauCu, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 70, 210, 30));

        jPanel1.setBackground(new java.awt.Color(153, 153, 255));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/LogoMainMenu1.jpg"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 330, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 340, Short.MAX_VALUE)
        );

        pnDoiMK.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 330, 340));

        lblCheckNhapLaiMK.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCheckNhapLaiMK.setForeground(new java.awt.Color(255, 0, 51));
        pnDoiMK.add(lblCheckNhapLaiMK, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 250, 240, 30));

        lblCheckMKCu.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCheckMKCu.setForeground(new java.awt.Color(255, 0, 51));
        pnDoiMK.add(lblCheckMKCu, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 110, 240, 30));

        lblCheckMKMoi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCheckMKMoi.setForeground(new java.awt.Color(255, 0, 51));
        pnDoiMK.add(lblCheckMKMoi, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 180, 240, 30));

        lblHienThiNLMK.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHienThiNLMK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/eyebrow.png"))); // NOI18N
        lblHienThiNLMK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHienThiNLMKMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblHienThiNLMKMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblHienThiNLMKMouseExited(evt);
            }
        });
        pnDoiMK.add(lblHienThiNLMK, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 210, 30, 30));

        lblHienThiMKCu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHienThiMKCu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/eyebrow.png"))); // NOI18N
        lblHienThiMKCu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblHienThiMKCuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblHienThiMKCuMouseExited(evt);
            }
        });
        pnDoiMK.add(lblHienThiMKCu, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 70, 30, 30));

        lblHienThiMKMoi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHienThiMKMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/eyebrow.png"))); // NOI18N
        lblHienThiMKMoi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblHienThiMKMoiMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblHienThiMKMoiMouseExited(evt);
            }
        });
        pnDoiMK.add(lblHienThiMKMoi, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 140, 30, 30));

        getContentPane().add(pnDoiMK, new org.netbeans.lib.awtextra.AbsoluteConstraints(-1, 2, 790, 340));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDoiMKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoiMKActionPerformed
        // TODO add your handling code here:
        doiMatKhau();
    }//GEN-LAST:event_btnDoiMKActionPerformed

    private void txtMatKhauCuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMatKhauCuActionPerformed
        // TODO add your handling code here:
        String matKhauCu = new String(txtMatKhauCu.getPassword());
        if (!matKhauCu.isBlank()) {
            setErrorMessage(lblCheckMKCu, "", "");
        }
    }//GEN-LAST:event_txtMatKhauCuActionPerformed

    private void txtMKMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMKMoiActionPerformed
        // TODO add your handling code here:
        String matKhauMoi = new String(txtMKMoi.getPassword());
        if (!matKhauMoi.isBlank()) {
            setErrorMessage(lblCheckMKMoi, "", "");
        }
    }//GEN-LAST:event_txtMKMoiActionPerformed

    private void txtNhapLaiMKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNhapLaiMKActionPerformed
        // TODO add your handling code here:
        String nhapLaiMK = new String(txtNhapLaiMK.getPassword());
        if (!nhapLaiMK.isBlank()) {
            setErrorMessage(lblCheckNhapLaiMK, "", "");
        }
    }//GEN-LAST:event_txtNhapLaiMKActionPerformed

    private void lblHienThiMKCuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHienThiMKCuMouseEntered
        // TODO add your handling code here:
        hienThiPass(txtMatKhauCu, lblHienThiMKCu);
    }//GEN-LAST:event_lblHienThiMKCuMouseEntered

    private void lblHienThiMKCuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHienThiMKCuMouseExited
        // TODO add your handling code here:
        hidePass(txtMatKhauCu, lblHienThiMKCu);
    }//GEN-LAST:event_lblHienThiMKCuMouseExited

    private void lblHienThiMKMoiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHienThiMKMoiMouseEntered
        // TODO add your handling code here:
        hienThiPass(txtMKMoi, lblHienThiMKMoi);
    }//GEN-LAST:event_lblHienThiMKMoiMouseEntered

    private void lblHienThiMKMoiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHienThiMKMoiMouseExited
        // TODO add your handling code here:
        hidePass(txtMKMoi, lblHienThiMKMoi);
    }//GEN-LAST:event_lblHienThiMKMoiMouseExited

    private void lblHienThiNLMKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHienThiNLMKMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblHienThiNLMKMouseClicked

    private void lblHienThiNLMKMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHienThiNLMKMouseEntered
        // TODO add your handling code here:
        hienThiPass(txtNhapLaiMK, lblHienThiNLMK);
    }//GEN-LAST:event_lblHienThiNLMKMouseEntered

    private void lblHienThiNLMKMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHienThiNLMKMouseExited
        // TODO add your handling code here:
        hidePass(txtNhapLaiMK, lblHienThiNLMK);
    }//GEN-LAST:event_lblHienThiNLMKMouseExited

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
            java.util.logging.Logger.getLogger(DoiMatKhau.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DoiMatKhau.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DoiMatKhau.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DoiMatKhau.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DoiMatKhau dialog = new DoiMatKhau(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDoiMK;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLabel lblCheckMKCu;
    private javax.swing.JLabel lblCheckMKMoi;
    private javax.swing.JLabel lblCheckNhapLaiMK;
    private javax.swing.JLabel lblHienThiMKCu;
    private javax.swing.JLabel lblHienThiMKMoi;
    private javax.swing.JLabel lblHienThiNLMK;
    private javax.swing.JLabel lblTitle1;
    private javax.swing.JPanel pnDoiMK;
    private javax.swing.JPasswordField txtMKMoi;
    private javax.swing.JPasswordField txtMatKhauCu;
    private javax.swing.JPasswordField txtNhapLaiMK;
    // End of variables declaration//GEN-END:variables
}
