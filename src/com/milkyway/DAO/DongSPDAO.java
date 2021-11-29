/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Model.DongSP;
import com.milkyway.Utils.JDBCHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hoang
 */
public class DongSPDAO extends MilkyWayDAO<DongSP, String> {

    String INSERT_SQL = "INSERT INTO [dbo].[DongSP]([MaDongSP],[IDThuongHieu],[TenDongSP],[GhiChu]) VALUES(?,?,?,?)";
    String UPDATE_SQL = "UPDATE [dbo].[DongSP]\n"
            + "SET [IDThuongHieu] = ?,[TenDongSP] = ?,[GhiChu] = ?,[TrangThai] = ? WHERE [MaDongSP] = ?";
    String DELETE_SQL = "Delete from DongSP where MaDongSP =?";
    String SELECT_ALL = "Select * from DongSP";
    String SELECT_BY_ID = "Select * from DongSP where MaDongSP=?";

    @Override
    public void insert(DongSP entity) {
        JDBCHelper.update(INSERT_SQL, entity.getMaDongSP(), entity.getIDThuongHieu(), entity.getTenDongSP(), entity.getGhiChu());
    }

    @Override
    public void update(DongSP entity) {
        JDBCHelper.update(UPDATE_SQL, entity.getIDThuongHieu(), entity.getTenDongSP(), entity.getGhiChu(), entity.isTrangThai(), entity.getMaDongSP());
    }

    @Override
    public void delete(String id) {
        JDBCHelper.update(DELETE_SQL, id);
    }

    @Override
    public DongSP selectById(String id) {
        List<DongSP> list = this.selectBySql(SELECT_BY_ID, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public DongSP selectByTenDong(String name) {
        List<DongSP> list = selectBySql("select * from DongSP where TenDongSP = ?", name);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);

    }

    public List<DongSP> selectByThuongHieuAndStatus(String id, boolean status) {
        return this.selectBySql("select * from DongSP where IDThuongHieu = ? and TrangThai = ?", id, status);
    }

    public List<DongSP> selectByStatus(boolean status) {
        return this.selectBySql("select * from DongSP where TrangThai = ?", status);
    }

    @Override
    public List<DongSP> selectAll() {
        return this.selectBySql(SELECT_ALL);
    }

    @Override
    protected List<DongSP> selectBySql(String sql, Object... args) {
        try {
            List<DongSP> lstDongSP = new ArrayList<>();
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                DongSP entity = new DongSP();
                entity.setIDDongSP(rs.getInt("IDDongSP"));
                entity.setMaDongSP(rs.getString("MaDongSP"));
                entity.setIDThuongHieu(rs.getInt("IDThuongHieu"));
                entity.setTenDongSP(rs.getString("TenDongSP"));
                entity.setGhiChu(rs.getString("GhiChu"));
                entity.setTrangThai(rs.getBoolean("TrangThai"));
                lstDongSP.add(entity);

            }
            rs.getStatement().getConnection().close();
            return lstDongSP;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public DongSP selectbyidDongsp(int IDDongSP) {
        List<DongSP> list = selectBySql("select * from DongSP where IDDongSP = ?", IDDongSP);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

}
