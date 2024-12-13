/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Entity.ChucVu;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.JDBCHelper;

/**
 *
 * @author bangp
 */
public class ChucVuDAO extends StoreDAO<ChucVu, String>{

    String INSERT = "INSERT INTO CHUCVU(MACV, LUONG, CHUCVU) VALUES(?, ?, ?)";
    String UPDATE = "UPDATE CHUCVU SET LUONG = ?, CHUCVU = ? WHERE MACV = ?";
    String DELETE = "DELETE FROM CHUCVU WHERE MACV = ?";
    String SELECTALL = "SELECT * FROM CHUCVU WHERE MACV != 'CV001'";
    String SELECTBYID = "SELECT * FROM CHUCVU WHERE MACV = ?";
    @Override
    public void insert(ChucVu entity) {
        JDBCHelper.update(INSERT, entity.getMaCV(), entity.getTenCV(), entity.getLuong());
    }

    @Override
    public void update(ChucVu entity) {
        JDBCHelper.update(UPDATE, entity.getTenCV(), entity.getLuong(), entity.getMaCV());
    }

    @Override
    public void delete(String id) {
        JDBCHelper.update(DELETE, id);
    }

    @Override
    public ChucVu selectById(String id) {
        List<ChucVu> list = this.selectBySql(SELECTBYID, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<ChucVu> selectAll() {
        return  this.selectBySql(SELECTALL);
    }

    @Override
    protected List<ChucVu> selectBySql(String sql, Object... args) {
        List<ChucVu> list = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                ChucVu entity = new ChucVu();
                entity.setMaCV(rs.getString("MACV"));
                entity.setTenCV(rs.getString("CHUCVU"));
                entity.setLuong(rs.getDouble("LUONG"));
                
                list.add(entity);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public List<ChucVu> selectByKeyword(String key){
        String sql = "SELECT * FROM CHUCVU WHERE CHUCVU LIKE ?";
        return selectBySql(sql, "%"+key+"%");
    }
    
    public ChucVu selectQuanLy(){
        String sql = "SELECT * FROM CHUCVU WHERE MACV ='CV01'";
        List<ChucVu> list = this.selectBySql(sql);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    
}
