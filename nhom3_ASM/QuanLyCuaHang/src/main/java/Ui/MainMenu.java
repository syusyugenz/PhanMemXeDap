/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Ui;

import Entity.SanPham;
import com.formdev.flatlaf.util.Animator;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Method;
import java.util.logging.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import utils.Auth;
import utils.MainThongBao;

/**
 *
 * @author drbf1
 */
public class MainMenu extends javax.swing.JFrame {

    /**
     * Creates new form MainMenu
     */
    public MainMenu() {
        initComponents();
        setLocationRelativeTo(null);
        initJFram(this, pnMovingBar);
        TrangChu tc = new TrangChu();
        setTrangChu(tc);
        lblUser.setText(Auth.user.getHoTen());
        openMenuBar();
        setOpenSized();
       
    }

    private int x;
    private int y;
    private int width = 270;
    private int height = 760;
    private JFrame frame = this;
   
    private boolean isTrangChu;

    void setTrangChu(JComponent com) {
        pnContainer.removeAll();
        pnContainer.add(com);
        pnContainer.repaint();
        pnContainer.revalidate();
        isTrangChu = true;
    }

    private void openMenuBar() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < width; i++) {
                    pnMenu.setSize(i, height);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        }).start();
        lblOpenMenu.setVisible(false);
        lblUser.setVisible(true);
    }

    void openTrangChu() {
        if (isTrangChu) {
            openMenuBar();
            setOpenSized();
            TrangChu tc = new TrangChu();
            setTrangChu(tc);
        }
    }

    void closeTrangChu() {
        if ( isTrangChu) {
            TrangChu tc = new TrangChu();
            setTrangChu(tc);
            closeMenuBar();
            setCloseSized();
        }
    }

    private void closeMenuBar() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = width; i > 0; i--) {
                    pnMenu.setSize(i, height);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        }).start();
        lblOpenMenu.setVisible(true);
        lblUser.setVisible(false);
    }

    private void setOpenSized() {
        frame.setSize(frame.getWidth() + width, frame.getHeight());
        pnMain.setPreferredSize(new Dimension(pnMain.getWidth() + width, pnMain.getHeight()));
        pnRight.setLocation(pnRight.getX() + width, pnRight.getY());
        this.setLocationRelativeTo(null);
    }

    private void setCloseSized() {
        this.setSize(this.getWidth() - width, this.getHeight());
        pnMain.setPreferredSize(new Dimension(pnMain.getWidth() - width, pnMain.getHeight()));
        pnRight.setLocation(pnRight.getX() - width, pnRight.getY());
        this.setLocationRelativeTo(null);
    }

    public void setEnterCur() {
        setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
    }

    public void setExitCur() {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR) {
        });
    }

    private void setForm(JComponent com) {
        pnContainer.removeAll();
        pnContainer.add(com);
        pnContainer.repaint();
        pnContainer.revalidate();
        closeMenuBar();
        setCloseSized();
        isTrangChu = false;
    }

    public void enteredHover(JLabel label) {
        label.setForeground(Color.red);
        setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
    }

    public void exitedHover(JLabel label) {
        label.setForeground(Color.white);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR) {
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnMain = new javax.swing.JPanel();
        pnRight = new javax.swing.JPanel();
        pnContainer = new javax.swing.JPanel();
        pnMovingBar = new javax.swing.JPanel();
        lblOpenMenu = new javax.swing.JLabel();
        titleBar = new utils.TitleBar();
        pnMenu = new javax.swing.JPanel();
        lblClose = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        pnSanPham = new javax.swing.JPanel();
        lblSelectSP = new javax.swing.JLabel();
        pnThongKe = new javax.swing.JPanel();
        lblSelectThongKe = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        pnKhachHang = new javax.swing.JPanel();
        lblSelectKH = new javax.swing.JLabel();
        pnKho = new javax.swing.JPanel();
        lblSelectKho = new javax.swing.JLabel();
        pnNhanVien = new javax.swing.JPanel();
        lblSelectNV = new javax.swing.JLabel();
        pnGioHang = new javax.swing.JPanel();
        lblSelectGioHang = new javax.swing.JLabel();
        pnHoaDon = new javax.swing.JPanel();
        lblSelectHoaDon = new javax.swing.JLabel();
        lblTroVe = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        pnMain.setBackground(new java.awt.Color(255, 255, 255));
        pnMain.setPreferredSize(new java.awt.Dimension(968, 712));

        pnRight.setBackground(new java.awt.Color(255, 255, 255));
        pnRight.setPreferredSize(new java.awt.Dimension(968, 712));

        pnContainer.setBackground(new java.awt.Color(255, 255, 255));
        pnContainer.setPreferredSize(new java.awt.Dimension(968, 680));
        pnContainer.setLayout(new java.awt.BorderLayout());

        pnMovingBar.setBackground(new java.awt.Color(255, 255, 255));

        lblOpenMenu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOpenMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/list.png"))); // NOI18N
        lblOpenMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblOpenMenuMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnMovingBarLayout = new javax.swing.GroupLayout(pnMovingBar);
        pnMovingBar.setLayout(pnMovingBarLayout);
        pnMovingBarLayout.setHorizontalGroup(
            pnMovingBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnMovingBarLayout.createSequentialGroup()
                .addComponent(lblOpenMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 839, Short.MAX_VALUE))
        );
        pnMovingBarLayout.setVerticalGroup(
            pnMovingBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnMovingBarLayout.createSequentialGroup()
                .addComponent(lblOpenMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnRightLayout = new javax.swing.GroupLayout(pnRight);
        pnRight.setLayout(pnRightLayout);
        pnRightLayout.setHorizontalGroup(
            pnRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnRightLayout.createSequentialGroup()
                .addComponent(pnMovingBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(titleBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnRightLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 963, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnRightLayout.setVerticalGroup(
            pnRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnRightLayout.createSequentialGroup()
                .addGroup(pnRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnMovingBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(pnContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE))
        );

        pnMenu.setBackground(new java.awt.Color(0, 204, 204));
        pnMenu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 153, 255)));
        pnMenu.setPreferredSize(new java.awt.Dimension(270, 712));
        pnMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblClose.setBackground(new java.awt.Color(0, 0, 0));
        lblClose.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblClose.setForeground(new java.awt.Color(255, 255, 255));
        lblClose.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblClose.setText("X");
        lblClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCloseMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblCloseMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblCloseMouseExited(evt);
            }
        });
        pnMenu.add(lblClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 0, 40, 40));

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        pnMenu.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 150, 130, 10));

        pnSanPham.setBackground(new java.awt.Color(164, 195, 162));

        lblSelectSP.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSelectSP.setForeground(new java.awt.Color(255, 255, 255));
        lblSelectSP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSelectSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/products.png"))); // NOI18N
        lblSelectSP.setText("Sản Phẩm");
        lblSelectSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSelectSPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblSelectSPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblSelectSPMouseExited(evt);
            }
        });

        javax.swing.GroupLayout pnSanPhamLayout = new javax.swing.GroupLayout(pnSanPham);
        pnSanPham.setLayout(pnSanPhamLayout);
        pnSanPhamLayout.setHorizontalGroup(
            pnSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblSelectSP, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
        );
        pnSanPhamLayout.setVerticalGroup(
            pnSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblSelectSP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        pnMenu.add(pnSanPham, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 270, 40));

        pnThongKe.setBackground(new java.awt.Color(164, 195, 162));

        lblSelectThongKe.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSelectThongKe.setForeground(new java.awt.Color(255, 255, 255));
        lblSelectThongKe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSelectThongKe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/description.png"))); // NOI18N
        lblSelectThongKe.setText("Thống Kê Và Báo Cáo");
        lblSelectThongKe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSelectThongKeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblSelectThongKeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblSelectThongKeMouseExited(evt);
            }
        });

        javax.swing.GroupLayout pnThongKeLayout = new javax.swing.GroupLayout(pnThongKe);
        pnThongKe.setLayout(pnThongKeLayout);
        pnThongKeLayout.setHorizontalGroup(
            pnThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblSelectThongKe, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
        );
        pnThongKeLayout.setVerticalGroup(
            pnThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblSelectThongKe, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        pnMenu.add(pnThongKe, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 520, 270, 40));

        jLabel4.setBackground(new java.awt.Color(0, 204, 204));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 3, 26)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logoxedap.PNG"))); // NOI18N
        pnMenu.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 260, -1));

        pnKhachHang.setBackground(new java.awt.Color(164, 195, 162));

        lblSelectKH.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSelectKH.setForeground(new java.awt.Color(255, 255, 255));
        lblSelectKH.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSelectKH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/tourist.png"))); // NOI18N
        lblSelectKH.setText("Khách Hàng");
        lblSelectKH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSelectKHMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblSelectKHMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblSelectKHMouseExited(evt);
            }
        });

        javax.swing.GroupLayout pnKhachHangLayout = new javax.swing.GroupLayout(pnKhachHang);
        pnKhachHang.setLayout(pnKhachHangLayout);
        pnKhachHangLayout.setHorizontalGroup(
            pnKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblSelectKH, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
        );
        pnKhachHangLayout.setVerticalGroup(
            pnKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblSelectKH, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        pnMenu.add(pnKhachHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, 270, 40));

        pnKho.setBackground(new java.awt.Color(164, 195, 162));

        lblSelectKho.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSelectKho.setForeground(new java.awt.Color(255, 255, 255));
        lblSelectKho.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSelectKho.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/storage-stacks.png"))); // NOI18N
        lblSelectKho.setText("Kho");
        lblSelectKho.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSelectKhoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblSelectKhoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblSelectKhoMouseExited(evt);
            }
        });

        javax.swing.GroupLayout pnKhoLayout = new javax.swing.GroupLayout(pnKho);
        pnKho.setLayout(pnKhoLayout);
        pnKhoLayout.setHorizontalGroup(
            pnKhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnKhoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblSelectKho, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnKhoLayout.setVerticalGroup(
            pnKhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnKhoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblSelectKho, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnMenu.add(pnKho, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 420, 270, 40));

        pnNhanVien.setBackground(new java.awt.Color(164, 195, 162));

        lblSelectNV.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSelectNV.setForeground(new java.awt.Color(255, 255, 255));
        lblSelectNV.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSelectNV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/employee.png"))); // NOI18N
        lblSelectNV.setText("Nhân Viên");
        lblSelectNV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSelectNVMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblSelectNVMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblSelectNVMouseExited(evt);
            }
        });

        javax.swing.GroupLayout pnNhanVienLayout = new javax.swing.GroupLayout(pnNhanVien);
        pnNhanVien.setLayout(pnNhanVienLayout);
        pnNhanVienLayout.setHorizontalGroup(
            pnNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblSelectNV, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
        );
        pnNhanVienLayout.setVerticalGroup(
            pnNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblSelectNV, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        pnMenu.add(pnNhanVien, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 320, 270, 40));

        pnGioHang.setBackground(new java.awt.Color(164, 195, 162));

        lblSelectGioHang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSelectGioHang.setForeground(new java.awt.Color(255, 255, 255));
        lblSelectGioHang.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSelectGioHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/trolley (1).png"))); // NOI18N
        lblSelectGioHang.setText("Giỏ Hàng");
        lblSelectGioHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSelectGioHangMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblSelectGioHangMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblSelectGioHangMouseExited(evt);
            }
        });

        javax.swing.GroupLayout pnGioHangLayout = new javax.swing.GroupLayout(pnGioHang);
        pnGioHang.setLayout(pnGioHangLayout);
        pnGioHangLayout.setHorizontalGroup(
            pnGioHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblSelectGioHang, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
        );
        pnGioHangLayout.setVerticalGroup(
            pnGioHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblSelectGioHang, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        pnMenu.add(pnGioHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 220, 270, 40));

        pnHoaDon.setBackground(new java.awt.Color(164, 195, 162));

        lblSelectHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSelectHoaDon.setForeground(new java.awt.Color(255, 255, 255));
        lblSelectHoaDon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSelectHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/bill.png"))); // NOI18N
        lblSelectHoaDon.setText("Hóa Đơn");
        lblSelectHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSelectHoaDonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblSelectHoaDonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblSelectHoaDonMouseExited(evt);
            }
        });

        javax.swing.GroupLayout pnHoaDonLayout = new javax.swing.GroupLayout(pnHoaDon);
        pnHoaDon.setLayout(pnHoaDonLayout);
        pnHoaDonLayout.setHorizontalGroup(
            pnHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblSelectHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
        );
        pnHoaDonLayout.setVerticalGroup(
            pnHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblSelectHoaDon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        pnMenu.add(pnHoaDon, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 470, 270, 40));

        lblTroVe.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTroVe.setForeground(new java.awt.Color(255, 255, 255));
        lblTroVe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/GioiThieu.png"))); // NOI18N
        lblTroVe.setText(" Trở về");
        lblTroVe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTroVeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblTroVeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblTroVeMouseExited(evt);
            }
        });
        pnMenu.add(lblTroVe, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 670, 90, 40));

        lblUser.setBackground(new java.awt.Color(255, 255, 255));
        lblUser.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblUser.setForeground(new java.awt.Color(255, 255, 255));
        lblUser.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/user.png"))); // NOI18N
        lblUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblUserMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblUserMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblUserMouseExited(evt);
            }
        });
        pnMenu.add(lblUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 210, 50));

        javax.swing.GroupLayout pnMainLayout = new javax.swing.GroupLayout(pnMain);
        pnMain.setLayout(pnMainLayout);
        pnMainLayout.setHorizontalGroup(
            pnMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnMainLayout.createSequentialGroup()
                .addComponent(pnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnRight, javax.swing.GroupLayout.DEFAULT_SIZE, 975, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        pnMainLayout.setVerticalGroup(
            pnMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnMainLayout.createSequentialGroup()
                .addComponent(pnRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnMain, javax.swing.GroupLayout.DEFAULT_SIZE, 975, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCloseMouseClicked
        // TODO add your handling code here:
        if (isTrangChu) {
            closeTrangChu();
        } else {
            closeMenuBar();
            setCloseSized();
        }


    }//GEN-LAST:event_lblCloseMouseClicked

    private void lblCloseMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCloseMouseEntered
        // TODO add your handling code here:
        setEnterCur();
    }//GEN-LAST:event_lblCloseMouseEntered

    private void lblCloseMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCloseMouseExited
        // TODO add your handling code here:
        setExitCur();
    }//GEN-LAST:event_lblCloseMouseExited

    private void lblSelectGioHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSelectGioHangMouseClicked
        // TODO add your handling code here:
        setForm(new GioHang());
    }//GEN-LAST:event_lblSelectGioHangMouseClicked

    private void lblSelectGioHangMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSelectGioHangMouseEntered
        // TODO add your handling code here:
        enteredHover(lblSelectGioHang);
    }//GEN-LAST:event_lblSelectGioHangMouseEntered

    private void lblSelectGioHangMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSelectGioHangMouseExited
        // TODO add your handling code here:
        exitedHover(lblSelectGioHang);
    }//GEN-LAST:event_lblSelectGioHangMouseExited

    private void lblOpenMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblOpenMenuMouseClicked
        // TODO add your handling code here:
        if (isTrangChu) {
            openTrangChu();
        } else {
            openMenuBar();
            setOpenSized();
        }
    }//GEN-LAST:event_lblOpenMenuMouseClicked

    private void lblSelectSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSelectSPMouseClicked
        // TODO add your handling code here:
        setForm(new QLySanPham());
    }//GEN-LAST:event_lblSelectSPMouseClicked

    private void lblSelectSPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSelectSPMouseEntered
        // TODO add your handling code here:
        enteredHover(lblSelectSP);
    }//GEN-LAST:event_lblSelectSPMouseEntered

    private void lblSelectSPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSelectSPMouseExited
        // TODO add your handling code here:
        exitedHover(lblSelectSP);
    }//GEN-LAST:event_lblSelectSPMouseExited

    private void lblSelectNVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSelectNVMouseClicked
        // TODO add your handling code here:
        setForm(new QLyNhanVien());
    }//GEN-LAST:event_lblSelectNVMouseClicked

    private void lblSelectNVMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSelectNVMouseEntered
        // TODO add your handling code here:
        enteredHover(lblSelectNV);
    }//GEN-LAST:event_lblSelectNVMouseEntered

    private void lblSelectNVMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSelectNVMouseExited
        // TODO add your handling code here:
        exitedHover(lblSelectNV);
    }//GEN-LAST:event_lblSelectNVMouseExited

    private void lblUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblUserMouseClicked
        // TODO add your handling code here:
        ThongTinTaiKhoan tttk = new ThongTinTaiKhoan(this, true);
        tttk.setLocate(lblUser.getX() + 50, lblUser.getY() + 90);
        tttk.setVisible(true);
    }//GEN-LAST:event_lblUserMouseClicked

    private void lblSelectThongKeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSelectThongKeMouseClicked
        // TODO add your handling code here:
        if(!Auth.isManager()){
            MainThongBao.alert(this, "Bạn không có quyền xem doanh thu!");
            return;
        }
        setForm(new ThongKeTongHop());
    }//GEN-LAST:event_lblSelectThongKeMouseClicked

    private void lblSelectThongKeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSelectThongKeMouseEntered
        // TODO add your handling code here:
        enteredHover(lblSelectThongKe);
    }//GEN-LAST:event_lblSelectThongKeMouseEntered

    private void lblSelectThongKeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSelectThongKeMouseExited
        // TODO add your handling code here:
        exitedHover(lblSelectThongKe);
    }//GEN-LAST:event_lblSelectThongKeMouseExited

    private void lblSelectKHMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSelectKHMouseClicked
        // TODO add your handling code here:
        setForm(new QLyKhachHang());
    }//GEN-LAST:event_lblSelectKHMouseClicked

    private void lblSelectKHMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSelectKHMouseEntered
        // TODO add your handling code here:
        enteredHover(lblSelectKH);
    }//GEN-LAST:event_lblSelectKHMouseEntered

    private void lblSelectKHMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSelectKHMouseExited
        // TODO add your handling code here:
        exitedHover(lblSelectKH);
    }//GEN-LAST:event_lblSelectKHMouseExited

    private void lblSelectKhoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSelectKhoMouseClicked
        // TODO add your handling code here:
        setForm(new QLyKho());
    }//GEN-LAST:event_lblSelectKhoMouseClicked

    private void lblSelectKhoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSelectKhoMouseEntered
        // TODO add your handling code here:
        enteredHover(lblSelectKho);
    }//GEN-LAST:event_lblSelectKhoMouseEntered

    private void lblSelectKhoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSelectKhoMouseExited
        // TODO add your handling code here:
        exitedHover(lblSelectKho);
    }//GEN-LAST:event_lblSelectKhoMouseExited

    private void lblSelectHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSelectHoaDonMouseClicked
        // TODO add your handling code here:
        setForm(new QLyHoaDon());
    }//GEN-LAST:event_lblSelectHoaDonMouseClicked

    private void lblSelectHoaDonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSelectHoaDonMouseExited
        // TODO add your handling code here:
        exitedHover(lblSelectHoaDon);
    }//GEN-LAST:event_lblSelectHoaDonMouseExited

    private void lblSelectHoaDonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSelectHoaDonMouseEntered
        // TODO add your handling code here:
        enteredHover(lblSelectHoaDon);
    }//GEN-LAST:event_lblSelectHoaDonMouseEntered

    private void lblTroVeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTroVeMouseClicked
        // TODO add your handling code here:
        this.dispose();
        new DangNhap(this, true).setVisible(true);
    }//GEN-LAST:event_lblTroVeMouseClicked

    private void lblUserMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblUserMouseEntered
        // TODO add your handling code here:
        enteredHover(lblUser);
    }//GEN-LAST:event_lblUserMouseEntered

    private void lblUserMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblUserMouseExited
        // TODO add your handling code here:
        exitedHover(lblUser);
    }//GEN-LAST:event_lblUserMouseExited

    private void lblTroVeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTroVeMouseEntered
        // TODO add your handling code here:
        enteredHover(lblTroVe);
    }//GEN-LAST:event_lblTroVeMouseEntered

    private void lblTroVeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTroVeMouseExited
        // TODO add your handling code here:
        exitedHover(lblTroVe);
    }//GEN-LAST:event_lblTroVeMouseExited

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel4;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblClose;
    private javax.swing.JLabel lblOpenMenu;
    private javax.swing.JLabel lblSelectGioHang;
    private javax.swing.JLabel lblSelectHoaDon;
    private javax.swing.JLabel lblSelectKH;
    private javax.swing.JLabel lblSelectKho;
    private javax.swing.JLabel lblSelectNV;
    private javax.swing.JLabel lblSelectSP;
    private javax.swing.JLabel lblSelectThongKe;
    private javax.swing.JLabel lblTroVe;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPanel pnContainer;
    private javax.swing.JPanel pnGioHang;
    private javax.swing.JPanel pnHoaDon;
    private javax.swing.JPanel pnKhachHang;
    private javax.swing.JPanel pnKho;
    private javax.swing.JPanel pnMain;
    private javax.swing.JPanel pnMenu;
    private javax.swing.JPanel pnMovingBar;
    private javax.swing.JPanel pnNhanVien;
    private javax.swing.JPanel pnRight;
    private javax.swing.JPanel pnSanPham;
    private javax.swing.JPanel pnThongKe;
    private utils.TitleBar titleBar;
    // End of variables declaration//GEN-END:variables

    class RoundedPanel extends JPanel {

        private int cornerRadius = 5;

        public RoundedPanel(int radius) {
            super();
            cornerRadius = radius;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            Graphics2D g2 = (Graphics2D) g;
            int with = getWidth();
            int height = getHeight();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, with - 1, height - 1, arcs.width, arcs.height);
        }
    }

    public void initJFram(JFrame frame, JPanel panel) {
        this.frame = frame;

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (frame.getExtendedState() == JFrame.NORMAL && SwingUtilities.isLeftMouseButton(e)) {
                    x = e.getXOnScreen();
                    y = e.getYOnScreen();
                }
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int deltaX = e.getXOnScreen() - x;
                int deltaY = e.getYOnScreen() - y;
                int newX = frame.getLocation().x + deltaX;
                int newY = frame.getLocation().y + deltaY;
                frame.setLocation(newX, newY);
                x = e.getXOnScreen();
                y = e.getYOnScreen();
            }
        });
    }
}
