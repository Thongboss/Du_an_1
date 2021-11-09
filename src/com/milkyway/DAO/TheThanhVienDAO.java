/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Model.TheThanhVien;
import com.milkyway.Utils.JDBCHelper;
import com.milkyway.Utils.JDBCSupporter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

/**
 *
 * @author DaiAustinYersin
 */
public class TheThanhVienDAO extends MilkyWayDao<TheThanhVien, String> {

    String insert_sql = (String) "insert into TheThanhVien(TenKH,GioiTinh,NgaySinh,SDT,CMND,Email,HinhAnh,NguoiTao,NgayTao,NgayHetHan,TrangThai)Values(?,?,?,?,?,?,?,?,?,?)";
    String update_sql = (String) "UPDATE  TheThanhVien SET TenKH = ? ,SET GioiTinh = ?,SET NgaySinh = ?,SET SDT = ?,"
            + " SET CMND = ?,SET Email = ?,SET HinhAnh = ?,SET NguoiTao =?, SET NgayTao = ? ,SET NgayHetHan=?, SET TrangThai = ? WHERE MaTheTV =?";
    String delete_sql = "DELETE TheThanhVien where MaTheTV = ?";
    String select_all_sql = " select * from TheThanhVien";
    String select_Byid_sql = " select * from TheThanhVien Where MaTheTV";

    @Override
    public void insert(TheThanhVien Entity) {
        JDBCSupporter.executeUpdate(insert_sql, Entity.getTenKH(), Entity.isGioiTinh(), Entity.getNgaySinh(), Entity.getSDT(),
                Entity.getCMND(), Entity.getEmail(), Entity.getHinhAnh(), Entity.getNguoiTao(), Entity.getNgayTao(), Entity.getNgayHetHan(), Entity.isTrangThai());
    }

    @Override
    public void update(TheThanhVien Entity) {
        JDBCSupporter.executeUpdate(insert_sql, Entity.getTenKH(), Entity.isGioiTinh(), Entity.getNgaySinh(), Entity.getSDT(), Entity.getCMND(), Entity.getEmail(),
                Entity.getHinhAnh(), Entity.getNguoiTao(), Entity.getNgayTao(), Entity.getNgayHetHan(), Entity.isTrangThai(), Entity.getMaTheTV());
    }

    @Override
    public void delete(String id) {
        JDBCSupporter.executeUpdate(delete_sql, id);
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
        return this.selectBySql(insert_sql);
    }

    @Override
    protected List<TheThanhVien> selectBySql(String sql, Object... args) {

        List<TheThanhVien> list = new ArrayList<>();
        try {

            ResultSet rs = JDBCSupporter.executeQuery(sql, args);
            while (rs.next()) {

                TheThanhVien Entity = new TheThanhVien();
                Entity.setTenKH(rs.getString("MaTheTV"));
                Entity.setTenKH(rs.getString("TenKH"));
                Entity.setGioiTinh(rs.getBoolean("GioiTinh"));

                Entity.setNgaySinh(rs.getDate("NgaySinh"));

                Entity.setSDT(rs.getString("SDT"));
                Entity.setCMND(rs.getString("CMND"));
                Entity.setHinhAnh("HinhAnh");
                Entity.setNguoiTao(rs.getInt("NguoiTao"));
                Entity.setNgayTao(rs.getDate("NgayTao"));
                Entity.setNgayHetHan(rs.getDate("NgayHetHan"));
                Entity.setTrangThai(rs.getBoolean("TrangThai"));
                list.add(Entity);

            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
