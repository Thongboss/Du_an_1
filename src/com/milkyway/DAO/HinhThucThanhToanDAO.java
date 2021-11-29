/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Model.HinhThucThanhToan;
import com.milkyway.Utils.JDBCHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DaiAustinYersin
 */
public class HinhThucThanhToanDAO extends MilkyWayDAO<HinhThucThanhToan, String> {

    String INSERT_SQL = "INSERT INTO [dbo].[HinhThucThanhToan]([MaHinhThucThanhToan],[TenHinhThucThanhToan],[GhiChu])\n"
            + "     VALUES(?,?,?)";
    String UPDATE_SQL = "UPDATE [dbo].[HinhThucThanhToan]\n"
            + "SET [TenHinhThucThanhToan] = ?,[GhiChu] = ?\n"
            + "WHERE [MaHinhThucThanhToan] = ?";
    String DELETE_SQL = "DELETE FROM [dbo].[HinhThucThanhToan] WHERE [MaHinhThucThanhToan] = ?";
    String SELECT_ALL = "Select * from HinhThucThanhToan";
    String SELECT_BY_ID = "Select * from HinhThucThanhToan where MaHinhThucThanhToan=?";

    @Override
    public void insert(HinhThucThanhToan entity) {
        JDBCHelper.update(INSERT_SQL, entity.getMaHinhThucThanhToan(), entity.getTenHinhThucThanhToan(), entity.getGhiChu());
    }

    @Override
    public void update(HinhThucThanhToan entity) {
        JDBCHelper.update(UPDATE_SQL, entity.getTenHinhThucThanhToan(), entity.getGhiChu(), entity.getIDHinhThucThanhToan());
    }

    @Override
    public void delete(String id) {
        JDBCHelper.update(DELETE_SQL, id);
    }

    @Override
    public HinhThucThanhToan selectById(String id) {
        List<HinhThucThanhToan> list = this.selectBySql(SELECT_BY_ID, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<HinhThucThanhToan> selectAll() {
        return this.selectBySql(SELECT_ALL);
    }

    @Override
    protected List<HinhThucThanhToan> selectBySql(String sql, Object... args) {
        try {
            List<HinhThucThanhToan> lstHinhThucThanhToan = new ArrayList<>();
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                HinhThucThanhToan entity = new HinhThucThanhToan();
                entity.setIDHinhThucThanhToan(rs.getInt("IDHinhThucThanhToan"));
                entity.setMaHinhThucThanhToan(rs.getString("MaHinhThucThanhToan"));
                entity.setTenHinhThucThanhToan(rs.getString("TenHinhThucThanhToan"));
                entity.setGhiChu(rs.getString("GhiChu"));
                lstHinhThucThanhToan.add(entity);
            }
            rs.getStatement().getConnection().close();
            return lstHinhThucThanhToan;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public HinhThucThanhToan selectByTenhinhThucThanhToan(String name) {
        List<HinhThucThanhToan> list = selectBySql("select * from HinhThucThanhToan where TenHinhThucThanhToan = ?", name);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public HinhThucThanhToan selectByIDhinhThucThanhToan(int idHinhThucThanhToan) {
        List<HinhThucThanhToan> list = selectBySql("select * from HinhThucThanhToan where idHinhThucThanhToan = ?", idHinhThucThanhToan);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    
}
