/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Model.TheThanhVien;
import com.milkyway.Utils.JDBCHelper;
import java.sql.ResultSet;
import java.util.ArrayList;

import java.util.List;

/**
 *
 * @author DaiAustinYersin
 */
public class TheThanhVienDAO extends MilkyWayDAO<TheThanhVien, String> {

    String insert_sql = "INSERT INTO [dbo].[TheThanhVien]([MaTheTV],[TenKH],[GioiTinh],[NgaySinh],[SDT],[CMND],[Email],[HinhAnh],[NgayTao],[NgayHetHan],[GhiChu])\n"
            + "     VALUES(?,?,?,?,?,?,?,?,?,?,?)";
    String update_sql = "UPDATE  TheThanhVien SET TenKH = ? ,SET GioiTinh = ?,SET NgaySinh = ?,SET SDT = ?,"
            + " SET CMND = ?,SET Email = ?,SET HinhAnh = ?,SET NguoiTao =?, SET NgayTao = ? ,SET NgayHetHan=?,SET GhiChu=?, SET TrangThai = ? WHERE MaTheTV =?";
    String delete_sql = "DELETE TheThanhVien where MaTheTV = ?";
    String select_all_sql = " select * from TheThanhVien";
    String select_Byid_sql = " select * from TheThanhVien Where MaTheTV = ?";

    @Override
    public void insert(TheThanhVien Entity) {
        JDBCHelper.update(insert_sql, Entity.getMaTheTV(), Entity.getTenKH(), Entity.isGioiTinh(), Entity.getNgaySinh(), Entity.getSDT(),
                Entity.getCMND(), Entity.getEmail(), Entity.getHinhAnh(), Entity.getNgayTao(), Entity.getNgayHetHan(), Entity.getGhiChu());
    }

    @Override
    public void update(TheThanhVien Entity) {
        JDBCHelper.update(insert_sql, Entity.getTenKH(), Entity.isGioiTinh(), Entity.getNgaySinh(), Entity.getSDT(), Entity.getCMND(), Entity.getEmail(),
                Entity.getHinhAnh(), Entity.getNgayTao(), Entity.getNgayHetHan(), Entity.getMaTheTV());
    }

    @Override
    public void delete(String id) {
        JDBCHelper.update(delete_sql, id);
    }

    @Override
    public TheThanhVien selectById(String id) {
        List<TheThanhVien> list = this.selectBySql(select_Byid_sql, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<TheThanhVien> selectAll() {
        return this.selectBySql(select_all_sql);
    }

    @Override
    protected List<TheThanhVien> selectBySql(String sql, Object... args) {

        List<TheThanhVien> list = new ArrayList<>();
        try {

            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {

                TheThanhVien Entity = new TheThanhVien();
                Entity.setIDTheTV(rs.getInt("IDTheTV"));
                Entity.setMaTheTV(rs.getString("MaTheTV"));
                Entity.setTenKH(rs.getString("TenKH"));
                Entity.setGioiTinh(rs.getBoolean("GioiTinh"));

                Entity.setNgaySinh(rs.getDate("NgaySinh"));

                Entity.setSDT(rs.getString("SDT"));
                Entity.setCMND(rs.getString("CMND"));
                Entity.setEmail(rs.getString("Email"));
                Entity.setHinhAnh("HinhAnh");
                Entity.setNgayTao(rs.getDate("NgayTao"));
                Entity.setNgayHetHan(rs.getDate("NgayHetHan"));
                Entity.setGhiChu(rs.getString("GhiChu"));
                list.add(Entity);

            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
