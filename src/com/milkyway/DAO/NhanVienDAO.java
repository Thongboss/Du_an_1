/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Model.NhanVien;
import com.milkyway.Utils.JDBCHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DaiAustinYersin
 */
public class NhanVienDAO extends MilkyWayDao<NhanVien, String> {

    String INSERT_SQL = "INSERT INTO NhanVien(MạtKhau,HoTen,GioiTinh,NgaySinh,SDT,CMND,Email,HinhAnh,VaiTro,GhiChu,TrangThai,Salt)VALUES(?,?,?,?,?,? ,?,?,?,?,?,?,?)";
    String Update_SQL = "Update NhanVien HoTen = ?,GioiTinh=?,NgaySinh=?,SDT=?,CMND=?,Email=?,HinhAnh=?,VaiTro=?,GhiChu=?,TrangThai=?,Salt=? Where MaNV = ?";
    String delete_SQL = "Delete From NhanVien WHERE MaNV =?";
    String Select_All = "select*from NhanVien";
    String Select_By_id = "Select *from NhanVien Where MaNV=?";

    @Override
    public void insert(NhanVien entity) {
        try {
            JDBCHelper.update(INSERT_SQL, entity.getMatKhau(), entity.getHoTen(), entity.isGioiTinh(), entity.getNgaySinh(), entity.getNgaySinh(), entity.getCMND(), entity.getEmail(), entity.isVaiTro(), entity.getGhiChu(), entity.isTrangThai(), entity.getSalt());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(NhanVien entity) {
        try {
            JDBCHelper.update(Update_SQL, entity.getHoTen(), entity.isGioiTinh(), entity.getNgaySinh(), entity.getSDT(), entity.getCMND(), entity.getEmail(), entity.getHinhAnh(), entity.isVaiTro(), entity.getGhiChu(), entity.isTrangThai(), entity.getSalt());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        try {
            JDBCHelper.update(delete_SQL, id);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public NhanVien selectById(String id) {
        List<NhanVien> list = this.selectBySql(Select_By_id, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<NhanVien> selectAll() {
        return this.selectBySql(Select_All);
    }

    @Override
    protected List<NhanVien> selectBySql(String sql, Object... args) {
<<<<<<< HEAD
//        try {
//            List<NhanVien> lstNhanVien = new ArrayList<>();
//                        ResultSet rs = JDBCHelper.query(sql, args);
//
//            while (rs.next()) {
//                NhanVien entity = new NhanVien();
//                entity.setMaNV(rs.getInt("MaNV"));
//                entity.setTaiKhoan(rs.getString("TaiKhoan"));
//                entity.setMatKhau(rs.get); ()
//                entity.setHoTen(rs.getString("HoTen"));
//                
//                
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
=======
        List<NhanVien> lstNhanVien = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.query(sql, args);
>>>>>>> 8b3e4ac7fa29717793168d3c4f5adb48cc9fcc0c

            while (rs.next()) {
                NhanVien entity = new NhanVien();
                entity.setMaNV(rs.getInt("MaNV"));
                entity.setTaiKhoan(rs.getString("TaiKhoan"));
//                entity.setMatKhau(rs.get); ()
                entity.setHoTen(rs.getString("HoTen"));
                lstNhanVien.add(entity);
            }
            rs.getStatement().getConnection().close();
            return lstNhanVien;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

