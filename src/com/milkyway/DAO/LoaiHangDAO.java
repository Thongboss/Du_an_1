/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;
import com.milkyway.Model.LoaiHang;
import com.milkyway.Utils.JDBCHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hoang
 */
public class LoaiHangDAO extends MilkyWayDAO<LoaiHang, String> {

    String INSERT_SQL = "INSERT INTO [dbo].[LoaiHang]([MaLoai],[IDNhomHang],[TenLoai],[GhiChu])\n"
            + "     VALUES(?,?,?,?)";
    String UPDATE_SQL = "UPDATE [dbo].[LoaiHang]\n"
            + "   SET [IDNhomHang] = ?,[TenLoai] = ?,[GhiChu] = ?\n"
            + " WHERE [MaLoai] = ?";
    String DELETE_SQL = "Delete from LoaiHang where MaLoai =?";
    String SELECT_ALL = "Select * from LoaiHang";
    String SELECT_BY_ID = "Select * from LoaiHang where MaLoai=?";

    @Override
    public void insert(LoaiHang entity) {
        JDBCHelper.update(INSERT_SQL, entity.getMaLoai(), entity.getIDNhomHang(), entity.getTenLoai(), entity.getGhiChu());
    }

    @Override
    public void update(LoaiHang entity) {
        JDBCHelper.update(UPDATE_SQL, entity.getIDNhomHang(), entity.getTenLoai(), entity.getGhiChu(), entity.getMaLoai());
    }

    @Override
    public void delete(String id) {
        JDBCHelper.update(DELETE_SQL, id);
    }

    @Override
    public LoaiHang selectById(String id) {
        List<LoaiHang> list = this.selectBySql(SELECT_BY_ID, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public List<LoaiHang> selectByNhomHang(String id) {
        return selectBySql("select * from LoaiHang where IDNhomHang = ?", id);
    }
    
    public LoaiHang selectByTenLoai(String name) {
        List<LoaiHang> list = selectBySql("select * from LoaiHang where TenLoai = ?", name);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<LoaiHang> selectAll() {
        return this.selectBySql(SELECT_ALL);
    }

    @Override
    protected List<LoaiHang> selectBySql(String sql, Object... args) {
        try {
            List<LoaiHang> lstLoaiHang = new ArrayList<>();
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                LoaiHang entity = new LoaiHang();
                entity.setIDLoaiHang(rs.getInt("IDLoaiHang"));
                entity.setMaLoai(rs.getString("MaLoai"));
                entity.setIDNhomHang(rs.getInt("IDNhomHang"));
                entity.setTenLoai(rs.getString("TenLoai"));
                entity.setGhiChu(rs.getString("GhiChu"));
                lstLoaiHang.add(entity);
            }
            rs.getStatement().getConnection().close();
            return lstLoaiHang;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public LoaiHang SelectByidLoaiHang(int idLoaiHang){
       List<LoaiHang> list = selectBySql("select * from LoaiHang where IDLoaiHang = ?", idLoaiHang);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}
