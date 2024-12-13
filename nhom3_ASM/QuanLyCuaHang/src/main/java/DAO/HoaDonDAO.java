/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Entity.HoaDon;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.JDBCHelper;

/**
 *
 * @author bangp
 */
public class HoaDonDAO extends StoreDAO<HoaDon, Integer>{
    
    String INSERT = "INSERT INTO HOADON(MAHD, MANV, MAKH, NGAYTAO) VALUES(?, ?, ?, ?)";
    String UPDATE = "UPDATE HOADON SET MANV = ?, MAKH = ?, NGAYTAO = ? WHERE MAHD = ?";
    String DELETE = "DELETE FROM HOADON WHERE MAHD = ?";
    String SELECTALL = "SELECT * FROM HOADON ORDER BY NGAYTAO ASC";
    String SELECTBYID = "SELECT * FROM HOADON WHERE MAHD = ?";

    @Override
    public void insert(HoaDon entity) {
        JDBCHelper.update(INSERT, entity.getMaHD(), entity.getMaNV(), entity.getMaKH(), entity.getNgayTao());    }

    @Override
    public void update(HoaDon entity) {
        JDBCHelper.update(UPDATE, entity.getMaNV(), entity.getMaKH(), entity.getNgayTao(), entity.getMaHD());
    }

    @Override
    public void delete(Integer id) {
        JDBCHelper.update(DELETE, id);
    }
    
    public void delete(String id) {
        JDBCHelper.update(DELETE, id);
    }

    @Override
    public HoaDon selectById(Integer id) {
        List<HoaDon> list = this.selectBySql(SELECTBYID, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<HoaDon> selectAll() {
        return this.selectBySql(SELECTALL);    }

    @Override
    protected List<HoaDon> selectBySql(String sql, Object... args) {
        List<HoaDon> list = new ArrayList<HoaDon>();
        try {
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                HoaDon entity = new HoaDon();
                entity.setMaNV(rs.getString("MANV"));
                entity.setMaHD(rs.getString("MAHD"));
                entity.setMaKH(rs.getInt("MAKH"));
                entity.setNgayTao(rs.getString("NGAYTAO"));
                list.add(entity);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<HoaDon> selectByDate(String time) {
        String sql = "Select * from HOADON where NGAYTAO LIKE ? order by ngaytao DESC";
        return selectBySql(sql, "%" + time + "%");
    }

    private List<Object[]> getListOfArray(String sql, String[] cols, Object... args) {
        try {
            List<Object[]> list = new ArrayList<>();
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                Object[] vals = new Object[cols.length];
                for (int i = 0; i < cols.length; i++) {
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

    public List<Object[]> selectByMaKH(int maKH) {
        String sql = "select count(mahd) as TongHoaDonDaMua from hoadon where makh like ?";
        String[] cols = {"TongHoaDonDaMua"};
        return this.getListOfArray(sql, cols, maKH);
    }
    
}
