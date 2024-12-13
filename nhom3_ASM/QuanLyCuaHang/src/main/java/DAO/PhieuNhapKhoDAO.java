/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Entity.PhieuNhapKho;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.JDBCHelper;

/**
 *
 * @author bangp
 */
public class PhieuNhapKhoDAO extends StoreDAO<PhieuNhapKho, Integer>{
    
    String INSERT = "INSERT INTO PhieuNhapKho(MASP, SOLUONGNHAP, NgayNhapKho) VALUES(?, ?, ?)";
    String UPDATE = "UPDATE PhieuNhapKho SET MASP = ?, SOLUONGNHAP = ?, NGAYNHAPKHO WHERE MAPHIEUNHAP = ?";
    String DELETE = "DELETE FROM PhieuNhapKho WHERE MAPHIEUNHAP = ?";
    String SELECTALL = "SELECT * FROM PhieuNhapKho";
    String SELECTBYID = "SELECT * FROM PhieuNhapKho WHERE MAPHIEUNHAP = ?";

    @Override
    public void insert(PhieuNhapKho entity) {
        JDBCHelper.update(INSERT, entity.getMaSP(), entity.getSoLuong(), entity.getNgayNhapHang());
    }

    @Override
    public void update(PhieuNhapKho entity) {
        JDBCHelper.update(UPDATE, entity.getMaSP(), entity.getSoLuong(),entity.getNgayNhapHang(), entity.getMaPhieuNhap());
    }

    @Override
    public void delete(Integer id) {
         JDBCHelper.update(DELETE, id);
    }

    @Override
    public PhieuNhapKho selectById(Integer id) {
        List<PhieuNhapKho> list = this.selectBySql(SELECTBYID, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<PhieuNhapKho> selectAll() {
        return this.selectBySql(SELECTALL);
    }

    @Override
    protected List<PhieuNhapKho> selectBySql(String sql, Object... args) {
        List<PhieuNhapKho> list = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                PhieuNhapKho entity = new PhieuNhapKho();
                entity.setMaPhieuNhap(rs.getInt("MAPHIEUNHAP"));
                entity.setMaSP(rs.getString("MASP"));
                entity.setSoLuong(rs.getInt("SOLUONGNHAP"));
                entity.setNgayNhapHang(rs.getString("NGAYNHAPKHO"));
                list.add(entity);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public List<PhieuNhapKho> selectByDate(String time) {
        String sql = "Select * from PHIEUNHAPKHO where NGAYNHAPKHO LIKE ?";
        return selectBySql(sql, "%"+time+"%");
    }
    private List<Object[]> getListOfArray(String sql, String[]cols, Object...args ){
        try {
            List<Object[]> list = new ArrayList<>();
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {                
                Object[]vals = new Object[cols.length];
                for(int i = 0; i < cols.length; i++){
                    vals[i] = rs.getObject(cols[i]);
                }
                list.add(vals);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public List<Object[]> selectByMaSP(String maSP){
        String sql = "Select MASP, SUM(SOLUONGNHAP) as SOLUONGNHAP from PHIEUNHAPKHO GROUP BY MASP HAVING MASP LIKE ?";
        String[] cols = {"MASP", "SOLUONGNHAP"};
        return this.getListOfArray(sql, cols, maSP);
    }

    

    
    
}
