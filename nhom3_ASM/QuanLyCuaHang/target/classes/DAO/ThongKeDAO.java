/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.JDBCHelper;

/**
 *
 * @author bangp
 */
public class ThongKeDAO {

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
    public List<Object[]> getDoanhThu(String date){
        String sql = "{CALL SP_DoanhThu(?)}";
        String[] cols = {"TenSP", "SoLuongBanRa", "GiaBan", "TongTien"};
        return this.getListOfArray(sql, cols, date);
    }
    public List<Object[]> getSanPhamBanRa(){
        String sql = "{CALL SP_SanPhamBanRa}";
        String[] cols = {"masp", "tensp", "soLuongNhapKho","DonViTinh" ,"soLuongBanRa"};
        return this.getListOfArray(sql, cols);
    }
   
    
    
}
