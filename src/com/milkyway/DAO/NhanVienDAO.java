/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Model.NhanVien;
import com.milkyway.Utils.JDBCHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DaiAustinYersin
 */
public class NhanVienDAO extends MilkyWayDAO<NhanVien, String> {

    String INSERT_SQL = "{Call SP_ThemNhanVien(?,?,?,?,?,?,?,?,?,?,?,?)}";
    String Update_SQL = "UPDATE [dbo].[NhanVien]\n"
            + "SET [HoTen] = ?,[GioiTinh] = ?,[NgaySinh] = ?,[SDT] = ?,[CMND] = ?,[Email] = ?,[HinhAnh] = ?,[VaiTro] = ?,[GhiChu] = ?\n"
            + "WHERE [MaNV] = ?";
    String delete_SQL = "Delete From NhanVien WHERE MaNV =?";
    String Select_All = "SELECT * FROM NhanVien";
    String Select_By_id = "Select *from NhanVien Where MaNV=?";
    String Select_By_user = "Select *from NhanVien Where TaiKhoan=?";
    String Select_By_UserName = "Select *from NhanVien Where TaiKhoan=?";
    String Update_Reset_pass = "UPDATE NhanVien SET MatKhau=? WHERE TaiKhoan=? and Email=?";
    String Select_manv_danghoatdong = "Select * from NhanVien Where TrangThai=?";
    String update_nvnghiviec = "UPDATE [dbo].[NhanVien] SET [TrangThai] = ? WHERE MaNV = ?";

    @Override
    public void insert(NhanVien entity) {
        JDBCHelper.update(INSERT_SQL, entity.getMaNV(), entity.getTaiKhoan(), entity.getMatKhau(), entity.getHoTen(), entity.isGioiTinh(), entity.getNgaySinh(), entity.getSDT(), entity.getCMND(), entity.getEmail(), entity.getHinhAnh(), entity.isVaiTro(), entity.getGhiChu());
    }

    @Override
    public void update(NhanVien entity) {
        JDBCHelper.update(Update_SQL, entity.getHoTen(), entity.isGioiTinh(), entity.getNgaySinh(), entity.getSDT(), entity.getCMND(), entity.getEmail(), entity.getHinhAnh(), entity.isVaiTro(), entity.getGhiChu(), entity.getMaNV());
    }

    public void UpdatePass(NhanVien entity) {
        JDBCHelper.update(Update_Reset_pass, entity.getMatKhau());
    }

    @Override
    public void delete(String id) {
        JDBCHelper.update(delete_SQL, id);
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
        return selectBySql(Select_All);
    }

    @Override
    protected List<NhanVien> selectBySql(String sql, Object... args) {
        List<NhanVien> lstNhanVien = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                NhanVien entity = new NhanVien();
                entity.setIDNhanVien(rs.getInt("IDNhanVien"));
                entity.setTaiKhoan(rs.getString("TaiKhoan"));
                entity.setMaNV(rs.getString("MaNV"));
                entity.setMatKhau(rs.getBytes("MatKhau"));
                entity.setHoTen(rs.getString("HoTen"));
                entity.setGioiTinh(rs.getBoolean("GioiTinh"));
                entity.setNgaySinh(rs.getDate("NgaySinh"));
                entity.setSDT(rs.getString("SDT"));
                entity.setCMND(rs.getString("CMND"));
                entity.setEmail(rs.getString("Email"));
                entity.setHinhAnh(rs.getString("HinhAnh"));
                entity.setVaiTro(rs.getBoolean("VaiTro"));
                entity.setGhiChu(rs.getString("VaiTro"));
                entity.setTrangThai(rs.getBoolean("TrangThai"));
                entity.setSalt(rs.getBytes("salt"));

                lstNhanVien.add(entity);
            }
            rs.getStatement().getConnection().close();
            return lstNhanVien;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public NhanVien selectByUser(String id) {
        List<NhanVien> list = this.selectBySql(Select_By_user, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public void updateNhanVienNghiViec(boolean trangthai, String Manv) {
        JDBCHelper.update(update_nvnghiviec, trangthai, Manv);
    }

    public List<NhanVien> selectNhanVienDangHoatDong(boolean TrangThai) {
        return selectBySql(Select_manv_danghoatdong, TrangThai);
    }
}
