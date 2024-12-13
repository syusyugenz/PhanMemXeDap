/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Entity.KhachHang;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.JDBCHelper;

/**
 *
 * @author bangp
 */
public class KhachHangDAO extends StoreDAO<KhachHang, Integer>{
    
    String INSERT = "INSERT INTO KHACHHANG(TENKH, SDT, Diem) VALUES(?, ?, ?)";
    String UPDATE = "UPDATE KHACHHANG SET TENKH = ?, SDT = ?, Diem = ? WHERE MAKH = ?";
    String DELETE = "DELETE FROM KHACHHANG WHERE MAKH = ?";
    String SELECTALL = "SELECT * FROM KHACHHANG";
    String SELECTBYID = "SELECT * FROM KHACHHANG WHERE MAKH = ?";


    @Override
    public void insert(KhachHang entity) {
        JDBCHelper.update(INSERT, entity.getTenKH(), entity.getSdt(), entity.getTichDiem());
    }

    @Override
    public void update(KhachHang entity) {
        JDBCHelper.update(UPDATE, entity.getTenKH(), entity.getSdt(),entity.getTichDiem(), entity.getMaKH());
    }

    @Override
    public void delete(Integer id) {
        JDBCHelper.update(DELETE, id);
    }

    @Override
    public KhachHang selectById(Integer id) {
        List<KhachHang> list = this.selectBySql(SELECTBYID, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<KhachHang> selectAll() {
        return this.selectBySql(SELECTALL);
    }

    @Override
    protected List<KhachHang> selectBySql(String sql, Object... args) {
        List<KhachHang> list = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                KhachHang entity = new KhachHang();
                entity.setMaKH(rs.getInt("MAKH"));
                entity.setSdt(rs.getString("SDT"));
                entity.setTenKH(rs.getString("TENKH"));
                entity.setTichDiem(rs.getInt("DIEM"));
                list.add(entity);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public List<KhachHang> selectByKeyWord(String tenKH, String SDT) {
        String sql = "SELECT * FROM KHACHHANG WHERE TENKH LIKE ? OR SDT LIKE ? ";
        return this.selectBySql(sql, "%"+tenKH+"%", "%"+SDT+"%");
    }

    

    
    
}
