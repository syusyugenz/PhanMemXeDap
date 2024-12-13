/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author bangp
 */
public class KhachHang {
    private int maKH;
    private String tenKH;
    private String sdt;
    private int tichDiem;

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public int getTichDiem() {
        return tichDiem;
    }

    public void setTichDiem(int tichDiem) {
        this.tichDiem = tichDiem;
    }
    
    @Override
    public String toString(){
        return this.tenKH + " (" + this.sdt + ")";
    }
}
