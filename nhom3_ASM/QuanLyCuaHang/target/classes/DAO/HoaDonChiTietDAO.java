/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Entity.HoaDonChiTiet;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.JDBCHelper;

/**
 *
 * @author bangp
 */
public class HoaDonChiTietDAO extends StoreDAO<HoaDonChiTiet, String>{
    
    String INSERT = "INSERT INTO HOADONCHITIET(MAHD, MASP, SOLUONG, GIABAN) VALUES(?, ?, ?, ?)";
    String UPDATE = "UPDATE HOADONCHITIET SET MASP = ?, MAKH = ?, SOLUONG = ?, GIABAN = ? WHERE MAHD = ?";
    String DELETE = "DELETE FROM HOADONCHITIET WHERE MAHD = ?";
    String SELECTALL = "SELECT * FROM HOADONCHITIET";
    String SELECTBYID = "SELECT * FROM HOADONCHITIET WHERE MAHD = ?";

    @Override
    public void insert(HoaDonChiTiet entity) {
        JDBCHelper.update(INSERT, entity.getMaHD(), entity.getMaSP(), entity.getSoLuong(), entity.getGiaBan());
    }

    @Override
    public void update(HoaDonChiTiet entity) {
        JDBCHelper.update(UPDATE, entity.getMaSP(), entity.getSoLuong(), entity.getGiaBan(), entity.getMaHD());
    }

    @Override
    public void delete(String id) {
        JDBCHelper.update(DELETE, id);

    }

    @Override
    public HoaDonChiTiet selectById(String id) {
        List<HoaDonChiTiet> list = this.selectBySql(SELECTBYID, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<HoaDonChiTiet> selectAll() {
        return  this.selectBySql(SELECTALL);
    }

    @Override
    protected List<HoaDonChiTiet> selectBySql(String sql, Object... args) {
        List<HoaDonChiTiet> list = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                HoaDonChiTiet entity = new HoaDonChiTiet();
                entity.setMaSP(rs.getString("MASP"));
                entity.setMaHD(rs.getString("MAHD"));
                entity.setGiaBan(rs.getDouble("GIABAN"));
                entity.setSoLuong(rs.getInt("SOLUONG"));
                list.add(entity);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public List<HoaDonChiTiet> selectByHoaDonID(String id){
        return selectBySql(SELECTBYID, id);
    }
     public List<HoaDonChiTiet> selectByHoaDonIDAndIDSP(String maHD, String maSP){
         String sql = "SELECT * FROM HOADONCHITIET WHERE MAHD = ? AND MASP = ? ";
        return selectBySql(sql,maHD, maSP);
    }
     
     public List<HoaDonChiTiet> selectByIdHDCT(String id) {
       String sql = "SELECT * FROM HOADONCHITIET WHERE MAHD = ? ";
       return selectBySql(sql, id);
    }

    
    
}
