/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Model.ThuongHieu;
import com.milkyway.Utils.JDBCHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hoang
 */
public class ThuongHieuDAO extends MilkyWayDAO<ThuongHieu, String> {

    String INSERT_SQL = "INSERT INTO [dbo].[ThuongHieu]([MaTH],[TenTH],[GhiChu]) VALUES(?,?,?)";
    String UPDATE_SQL = "UPDATE [dbo].[ThuongHieu]\n"
            + "SET [TenTH] = ?,[GhiChu] = ?,[TrangThai] = ?\n"
            + "WHERE [MaTH] = ?";
    String DELETE_SQL = "Delete from ThuongHieu where MaTH =?";
    String SELECT_ALL = "Select * from ThuongHieu";
    String SELECT_BY_ID = "Select * from ThuongHieu where MaTH=?";
    String SELECT_BY_STATUS = "select * from ThuongHieu where TrangThai = ?";

    @Override
    public void insert(ThuongHieu entity) {
        JDBCHelper.update(INSERT_SQL, entity.getMaTH(), entity.getTenTh(), entity.getGhiChu());
    }

    @Override
    public void update(ThuongHieu entity) {
        JDBCHelper.update(UPDATE_SQL, entity.getTenTh(), entity.getGhiChu(), entity.isTrangthai(), entity.getMaTH());
    }

    @Override
    public void delete(String id) {
        JDBCHelper.update(DELETE_SQL, id);
    }

    @Override
    public ThuongHieu selectById(String id) {
        List<ThuongHieu> list = this.selectBySql(SELECT_BY_ID, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    
    public List<ThuongHieu> selectByStatus(boolean status) {
        return this.selectBySql(SELECT_BY_STATUS, status);
    }

    @Override
    public List<ThuongHieu> selectAll() {
        return this.selectBySql(SELECT_ALL);
    }

    @Override
    protected List<ThuongHieu> selectBySql(String sql, Object... args) {
        try {
            List<ThuongHieu> lstThuongHieu = new ArrayList<>();
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                ThuongHieu entity = new ThuongHieu();
                entity.setIDThuongHieu(rs.getInt("IDThuongHieu"));
                entity.setMaTH(rs.getString("MaTH"));
                entity.setTenTh(rs.getString("TenTH"));
                entity.setGhiChu(rs.getString("GhiChu"));
                entity.setTrangthai(rs.getBoolean("TrangThai"));
                lstThuongHieu.add(entity);
            }
            rs.getStatement().getConnection().close();
            return lstThuongHieu;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
