/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Utils.JDBCHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class ThongKeDAO {

    private List<Object[]> getListOfArray(String sql, String[] cols, Object...args) {
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
    
    public Object[] thongKeSoLuongSanPham() {
        String[] cols = {"SoLuongDangDK", "SoLuongHetHang", "SoLuongSapHetHan", "SoLuongSapHetHang", "SoLuongNgungKD"};
        List<Object[]> lst = getListOfArray("{call SP_ThongKeSoLuongSP()}", cols);
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }
    
    public List<Object[]> thongKeSanPhamMuaNhieuNhat(String tenLoai, String tenDongSP) {
        String[] cols = {"MaSP", "TenSP", "TenLoai", "TenDongSP", "NgayXK", "HanSD", "SoLuongTon", "DonGia", "SoLuongMua", "DoanhThu"};
        return getListOfArray("{call SP_ThongKeSanPhamMuaNhieuNhat(?,?)}", cols, tenLoai, tenDongSP);
    }
    
    public List<Object[]> thongKeSanPhamMuaItNhat(String tenLoai, String tenDongSP) {
        String[] cols = {"MaSP", "TenSP", "TenLoai", "TenDongSP", "NgayXK", "HanSD", "SoLuongTon", "DonGia", "SoLuongMua", "DoanhThu"};
        return getListOfArray("{call SP_ThongKeSanPhamMuaItNhat(?,?)}", cols, tenLoai, tenDongSP);
    }
    
    public List<Object[]> thongKeSanPhamDoanhThuNhieuNhat(String tenLoai, String tenDongSP) {
        String[] cols = {"MaSP", "TenSP", "TenLoai", "TenDongSP", "NgayXK", "HanSD", "SoLuongTon", "DonGia", "SoLuongMua", "DoanhThu"};
        return getListOfArray("{call SP_ThongKeSanPhamDoanhThuNhieuNhat(?,?)}", cols, tenLoai, tenDongSP);
    }
    
    public List<Object[]> thongKeSanPhamDoanhThuItNhat(String tenLoai, String tenDongSP) {
        String[] cols = {"MaSP", "TenSP", "TenLoai", "TenDongSP", "NgayXK", "HanSD", "SoLuongTon", "DonGia", "SoLuongMua", "DoanhThu"};
        return getListOfArray("{call SP_ThongKeSanPhamDoanhThuItNhat(?,?)}", cols, tenLoai, tenDongSP);
    }
}
