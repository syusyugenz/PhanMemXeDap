/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Ui;

import DAO.ThongKeDAO;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;
import utils.XDate;

/**
 *
 * @author admin
 */
public class ThongKeTongHop extends javax.swing.JPanel {

    /**
     * Creates new form ThongKeTongHop
     */
    public ThongKeTongHop() {
        initComponents();
        fillTableDoanhThu();
        fillTableSanPhamBanRa();
        dcTimKiem.setDateFormatString("dd/MM/yyyy");
    }
    String date;
    ThongKeDAO tkDAO = new ThongKeDAO();

    void fillTableDoanhThu() {
        DefaultTableModel model = (DefaultTableModel) tblDoanhThu.getModel();
        model.setRowCount(0);
        if (dcTimKiem.getDate() == null) {
            date = "";
        } else {
            date = XDate.toString(dcTimKiem.getDate(), "yyyy-MM-dd");
        }
        List<Object[]> list = tkDAO.getDoanhThu(date);
        for (Object[] row : list) {
            model.addRow(new Object[]{row[0], row[1], new DecimalFormat("###0").format(row[2]), new DecimalFormat("####0").format(row[3]),});
        }
    }

    void fillTableSanPhamBanRa() {
        DefaultTableModel model = (DefaultTableModel) tblSanPhamBanRa.getModel();
        model.setRowCount(0);

        List<Object[]> list = tkDAO.getSanPhamBanRa();
        for (Object[] row : list) {
            model.addRow(new Object[]{row[0], row[1], row[2], row[3], row[4]
            });
        }
    }

    void printDoanhThuReport() {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();

            HSSFSheet sheet = workbook.createSheet("Doanh Thu");
            HSSFCellStyle style = createStyleForTitle(workbook);
            HSSFCellStyle bordersCellStyle = setBorderCellStyle(workbook);
            int rownum = 0;
            Cell cell;
            Row row;
            row = sheet.createRow(rownum);

            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("Tên sản phẩm");
            cell.setCellStyle(style);

            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue("Số lượng bán ra");
            cell.setCellStyle(style);

            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue("Giá bán");
            cell.setCellStyle(style);

            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue("Tổng tiền");
            cell.setCellStyle(style);

            if (dcTimKiem.getDate() == null) {
                date = "";
            } else {
                date = XDate.toString(dcTimKiem.getDate(), "yyyy-MM-dd");
            }
            List<Object[]> list = tkDAO.getDoanhThu(date);

            if (list != null) {
                for (Object[] tk : list) {
                    rownum++;
                    row = sheet.createRow(rownum);

                    cell = row.createCell(0, CellType.STRING);
                    cell.setCellValue((String) tk[0]);
                    cell.setCellStyle(bordersCellStyle);

                    cell = row.createCell(1, CellType.NUMERIC);
                    cell.setCellValue((int) tk[1]);
                    cell.setCellStyle(bordersCellStyle);

                    cell = row.createCell(2, CellType.NUMERIC);
                    cell.setCellValue(new DecimalFormat("###0 VND").format((double) tk[2]));
                    cell.setCellStyle(bordersCellStyle);

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellValue(new DecimalFormat("###0 VND").format((double) tk[3]));
                    cell.setCellStyle(bordersCellStyle);

                }
                JFileChooser chooser = new JFileChooser("/Desktop");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("EXCEL FILES", ".xls", ".xlsx", ".xln");
                chooser.setFileFilter(filter);
                chooser.setDialogTitle("Lưu về");
                int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
                autosizeColumn(sheet, numberOfColumn);
                int value = chooser.showSaveDialog(null);
                if (value == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    File f = new File(file + ".xls");
                    FileOutputStream fos = new FileOutputStream(f);
                    workbook.write(fos);
                    fos.close();
                    if (JOptionPane.showConfirmDialog(this, "In Thành Công! Bạn có muốn mở file ngay không?") == JOptionPane.YES_OPTION) {
                        Desktop.getDesktop().browse(f.toURI());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private HSSFCellStyle setBorderCellStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);

        return style;
    }

    private void autosizeColumn(Sheet sheet, int lastColumn) {
        for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
    }

    private HSSFCellStyle createStyleForTitle(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        font.setColor(IndexedColors.RED.getIndex());
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        return style;
    }

    void printSanPhamBanRaReport() {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();

            HSSFSheet sheet = workbook.createSheet("Doanh Thu");
            HSSFCellStyle style = createStyleForTitle(workbook);
            HSSFCellStyle bordersCellStyle = setBorderCellStyle(workbook);
            int rownum = 0;
            Cell cell;
            Row row;
            row = sheet.createRow(rownum);

            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("Mã sản phẩm");
            cell.setCellStyle(style);

            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue("Tên sản phẩm");
            cell.setCellStyle(style);

            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue("Số lượng nhập vào");
            cell.setCellStyle(style);

            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue("Đơn vị tính");
            cell.setCellStyle(style);

            cell = row.createCell(4, CellType.STRING);
            cell.setCellValue("Số lượng bán ra");
            cell.setCellStyle(style);

            List<Object[]> list = tkDAO.getSanPhamBanRa();

            if (list != null) {
                for (Object[] tk : list) {
                    rownum++;
                    row = sheet.createRow(rownum);

                    cell = row.createCell(0, CellType.STRING);
                    cell.setCellValue((String) tk[0]);
                    cell.setCellStyle(bordersCellStyle);

                    cell = row.createCell(1, CellType.NUMERIC);
                    cell.setCellValue((String) tk[1]);
                    cell.setCellStyle(bordersCellStyle);

                    cell = row.createCell(2, CellType.NUMERIC);
                    cell.setCellValue((int) tk[2]);
                    cell.setCellStyle(bordersCellStyle);

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellValue((String) tk[3]);
                    cell.setCellStyle(bordersCellStyle);

                    cell = row.createCell(4, CellType.NUMERIC);
                    cell.setCellValue((int) tk[4]);
                    cell.setCellStyle(bordersCellStyle);

                }
                int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
                autosizeColumn(sheet, numberOfColumn);

                JFileChooser chooser = new JFileChooser("/Desktop");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("EXCEL FILES", ".xls", ".xlsx", ".xln");
                chooser.setFileFilter(filter);
                chooser.setDialogTitle("Lưu về");

                int value = chooser.showSaveDialog(null);
                if (value == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    File f = new File(file + ".xls");
                    FileOutputStream fos = new FileOutputStream(f);
                    workbook.write(fos);
                    fos.close();
                    if (JOptionPane.showConfirmDialog(this, "In Thành Công! Bạn có muốn mở file ngay không?") == JOptionPane.YES_OPTION) {
                        Desktop.getDesktop().browse(f.toURI());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    void createChart(){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        //Set dữ liệu
        ThongKeDAO tkDAO = new ThongKeDAO();
        List<Object[]> list = tkDAO.getDoanhThu(date);
        for (Object[] row : list) {
            dataset.addValue((double) row[3], (String) "Tổng số tiền bán được", (String) row[0]);
        }
        JFreeChart chart = ChartFactory.createBarChart3D(
                "Biểu đồ doanh thu " + date,
                "Tên Sản Phẩm",
                "Số tiền (VND)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );  
        ChartFrame cf = new ChartFrame("Thống kê doanh thu", chart);
        cf.pack();
        cf.setSize(new Dimension(950,600));
        cf.setVisible(true);
        cf.setLocationRelativeTo(null);
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDoanhThu = new javax.swing.JTable();
        btnXuatDoanhThu = new javax.swing.JButton();
        btnBieuDoCot = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        dcTimKiem = new com.toedter.calendar.JDateChooser();
        btnTimKiem = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSanPhamBanRa = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Thống kê tổng hợp");

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tblDoanhThu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Tên sản phẩm", "Số lượng bán ra", "Giá bán", "Tổng tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblDoanhThu);

        btnXuatDoanhThu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/excel1.png"))); // NOI18N
        btnXuatDoanhThu.setText("Xuất thống kê");
        btnXuatDoanhThu.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnXuatDoanhThu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatDoanhThuActionPerformed(evt);
            }
        });

        btnBieuDoCot.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/bar-chart.png"))); // NOI18N
        btnBieuDoCot.setText("Biểu Đồ Doanh Thu");
        btnBieuDoCot.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnBieuDoCot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBieuDoCotActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/search.png"))); // NOI18N
        btnTimKiem.setText("Tìm");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dcTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTimKiem)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dcTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 913, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnBieuDoCot, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnXuatDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBieuDoCot, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXuatDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );

        jTabbedPane1.addTab("Doanh thu", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        tblSanPhamBanRa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Số lượng nhập vào", "Đơn vị tính", "Số lượng bán ra"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblSanPhamBanRa);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/excel1.png"))); // NOI18N
        jButton2.setText("Xuất File Excel");
        jButton2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 913, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
        );

        jTabbedPane1.addTab("Sản phẩm bán ra", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 942, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 610, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        // TODO add your handling code here:
        fillTableDoanhThu();
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void btnXuatDoanhThuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatDoanhThuActionPerformed
        // TODO add your handling code here:
        printDoanhThuReport();
    }//GEN-LAST:event_btnXuatDoanhThuActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        printSanPhamBanRaReport();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnBieuDoCotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBieuDoCotActionPerformed
        // TODO add your handling code here:
        if (date == "") {
            return;
        }
        if(tblDoanhThu.getRowCount() <=0){
            return; 
        }
        createChart();
    }//GEN-LAST:event_btnBieuDoCotActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBieuDoCot;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXuatDoanhThu;
    private com.toedter.calendar.JDateChooser dcTimKiem;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tblDoanhThu;
    private javax.swing.JTable tblSanPhamBanRa;
    // End of variables declaration//GEN-END:variables
}
