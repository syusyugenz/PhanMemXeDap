/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Entity.TichDiem;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.JDBCHelper;

/**
 *
 * @author bangp
 */
public class TichDiemDAO extends StoreDAO<TichDiem, Integer>{
    
    String INSERT = "INSERT INTO TICHDIEM(MAHD, DIEM) VALUES(?, ?)";
    String UPDATE = "UPDATE TICHDIEM SET MAHD = ?, DIEM = ? WHERE MAPHIEU = ?";
    String DELETE = "DELETE FROM TICHDIEM WHERE MAPHIEU = ?";
    String SELECTALL = "SELECT * FROM TICHDIEM";
    String SELECTBYID = "SELECT * FROM TICHDIEM WHERE MAPHIEU = ?";

    @Override
    public void insert(TichDiem entity) {
        JDBCHelper.update(INSERT, entity.getMaHD(), entity.getDiem());
    }

    @Override
    public void update(TichDiem entity) {
        JDBCHelper.update(UPDATE, entity.getMaHD(), entity.getDiem(), entity.getMaPhieu());
    }

    @Override
    public void delete(Integer id) {
        JDBCHelper.update(DELETE, id);
    }

    @Override
    public TichDiem selectById(Integer id) {
        List<TichDiem> list = this.selectBySql(SELECTBYID, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<TichDiem> selectAll() {
        return this.selectBySql(SELECTALL);
    }

    @Override
    protected List<TichDiem> selectBySql(String sql, Object... args) {
        List<TichDiem> list = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                TichDiem entity = new TichDiem();
                entity.setMaPhieu(rs.getInt("MAPHIEU"));
                entity.setMaHD(rs.getString("MAHD"));
                entity.setDiem(rs.getInt("DIEM"));
                list.add(entity);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    
    
}
