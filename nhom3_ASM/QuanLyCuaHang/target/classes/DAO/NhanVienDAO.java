/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Entity.NhanVien;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.JDBCHelper;

/**
 *
 * @author bangp
 */
public class NhanVienDAO extends StoreDAO<NhanVien, String >{
    
    String INSERT = "INSERT INTO NHANVIEN(MANV, HOTEN, GIOITINH, EMAIL, SDT, NGAYSINH, DIACHI, VAITRO, MATKHAU, MACV, NGAYVAOLAM) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String UPDATE = "UPDATE NHANVIEN SET HOTEN = ?, GIOITINH = ?, EMAIL = ?, SDT = ?, NGAYSINH = ?, DIACHI = ?, VAITRO = ?, MATKHAU = ?, MACV = ?, NGAYVAOLAM = ? WHERE MANV = ?";
    String DELETE = "DELETE FROM NHANVIEN WHERE MANV = ?";
    String SELECTALL = "SELECT * FROM NHANVIEN";
    String SELECTBYID = "SELECT * FROM NHANVIEN WHERE MANV = ?";

    @Override
    public void insert(NhanVien entity) {
        JDBCHelper.update(INSERT, entity.getMaNV(), entity.getHoTen(), entity.isGioiTinh(), 
                entity.getEmail(), entity.getSdt(), entity.getNgaySinh(), entity.getDiaChi(),
                entity.isVaiTro(), entity.getMatKhau(), entity.getMaCV(), entity.getNgayVaoLam());
    }

    @Override
    public void update(NhanVien entity) {
        JDBCHelper.update(UPDATE, entity.getHoTen(), entity.isGioiTinh(), 
                entity.getEmail(), entity.getSdt(), entity.getNgaySinh(), entity.getDiaChi(),
                entity.isVaiTro(), entity.getMatKhau(), entity.getMaCV(), entity.getNgayVaoLam(),
                entity.getMaNV());
    }

    @Override
    public void delete(String id) {
        JDBCHelper.update(DELETE, id);
    }

    @Override
    public NhanVien selectById(String id) {
        List<NhanVien> list = this.selectBySql(SELECTBYID, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);    }

    @Override
    public List<NhanVien> selectAll() {
        return  this.selectBySql(SELECTALL);
    }

    @Override
    protected List<NhanVien> selectBySql(String sql, Object... args) {
        List<NhanVien> list = new ArrayList<NhanVien>();
        try {
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                NhanVien entity = new NhanVien();
                entity.setMaNV(rs.getString("MANV"));
                entity.setMatKhau(rs.getString("MATKHAU"));
                entity.setHoTen(rs.getString("HOTEN"));
                entity.setVaiTro(rs.getBoolean("VAITRO"));
                entity.setDiaChi(rs.getString("DIACHI"));
                entity.setEmail(rs.getString("EMAIL"));
                entity.setGioiTinh(rs.getBoolean("GIOITINH"));
                entity.setMaCV(rs.getString("MaCV"));
                entity.setSdt(rs.getString("SDT"));
                entity.setNgayVaoLam(rs.getString("NGAYVAOLAM"));
                entity.setNgaySinh(rs.getString("NGAYSINH"));
                list.add(entity);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public List<NhanVien> selectByKeyword(String key){
        String sql = "SELECT * FROM NHANVIEN WHERE HOTEN LIKE ?";
        return selectBySql(sql, "%"+key + "%");
    }

    
    
}
