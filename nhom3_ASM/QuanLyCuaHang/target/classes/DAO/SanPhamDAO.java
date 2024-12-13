/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Entity.SanPham;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.JDBCHelper;

/**
 *
 * @author bangp
 */
public class SanPhamDAO extends StoreDAO<SanPham, String>{
    
    String INSERT = "INSERT INTO SANPHAM(MASP, TENSP, DONGIA, DONVITINH, HINHANH, PHANLOAI, TRONGLUONG, SOLUONG) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    String UPDATE = "UPDATE SANPHAM SET TENSP = ?, DONGIA = ?, DONVITINH = ?, HINHANH = ?, PHANLOAI = ?, TRONGLUONG = ?, SOLUONG = ? WHERE MASP = ?";
    String DELETE = "DELETE FROM SANPHAM WHERE MASP = ?";
    String SELECTALL = "SELECT * FROM SANPHAM";
    String SELECTBYID = "SELECT * FROM SANPHAM WHERE MASP = ?";

    @Override
    public void insert(SanPham entity) {
        JDBCHelper.update(INSERT, entity.getMaSP(), entity.getTenSP(), entity.getDonGia(),
                entity.getDonViTinh(), entity.getHinhAnh(), entity.getPhanLoai(), entity.getTrongLuong(), entity.getSoLuong());
    }

    @Override
    public void update(SanPham entity) {
        JDBCHelper.update(UPDATE, entity.getTenSP(), entity.getDonGia(), entity.getDonViTinh(),
                entity.getHinhAnh(), entity.getPhanLoai(), entity.getTrongLuong(),entity.getSoLuong(), entity.getMaSP());
    }

    @Override
    public void delete(String id) {
        JDBCHelper.update(DELETE, id);
    }

    @Override
    public SanPham selectById(String id) {
        List<SanPham> list = this.selectBySql(SELECTBYID, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<SanPham> selectAll() {
        return this.selectBySql(SELECTALL);
    }

    @Override
    protected List<SanPham> selectBySql(String sql, Object... args) {
        List<SanPham> list = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                SanPham entity = new SanPham();
                entity.setMaSP(rs.getString("MASP"));
                entity.setTenSP(rs.getString("TENSP"));
                entity.setDonGia(rs.getFloat("DONGIA"));
                entity.setDonViTinh(rs.getString("DONVITINH"));
                entity.setHinhAnh(rs.getString("HINHANH"));
                entity.setTrongLuong(rs.getString("TRONGLUONG"));
                entity.setPhanLoai(rs.getString("PHANLOAI"));
                entity.setSoLuong(rs.getInt("SOLUONG"));
                list.add(entity);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public List<SanPham> selectByKeyword(String keyword){
        String sql = "SELECT * FROM SANPHAM WHERE TENSP LIKE ?";
        return this.selectBySql(sql, "%"+keyword+"%");
    }
    public List<SanPham> selectByKeywordAndQuantity(String keyword){
        String sql = "SELECT * FROM SANPHAM WHERE TENSP LIKE ? AND SOLUONG !=0";
        return this.selectBySql(sql, "%"+keyword+"%");
    }
    
     public List<SanPham> selectNotInCourse(int maKho, String key){
        String sql = "SELECT * FROM SANPHAM WHERE TENSP LIKE ? AND MASP NOT IN(SELECT MASP FROM PHIEUNHAPKHO WHERE MAPHIEUNHAP = ?)";
        return this.selectBySql(sql, "%"+key+"%",maKho);
    }

    
    
}
