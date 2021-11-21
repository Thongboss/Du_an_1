/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Model.KhoiLuong;
import com.milkyway.Utils.JDBCHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DaiAustinYersin
 */
public class KhoiLuongDAO extends MilkyWayDAO<KhoiLuong, String> {

    final String INSERT_SQL = "INSERT INTO [dbo].[KhoiLuong]([MaKhoiLuong],[GiaTri]) VALUES(?,?)";
    final String UPDATE_SQL = "UPDATE [dbo].[KhoiLuong] SET [GiaTri] = ? WHERE [MaKhoiLuong] = ?";
    final String DELETE_SQL = "DELETE FROM [dbo].[KhoiLuong] WHERE [MaKhoiLuong] = ?";
    final String SELECT_BY_ID_SQL = "SELECT * FROM [dbo].[KhoiLuong] WHERE [MaKhoiLuong] = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM [dbo].[KhoiLuong]";
    
    @Override
    public void insert(KhoiLuong entity) {
        JDBCHelper.update(INSERT_SQL, entity.getMaKhoiLuong(), entity.getGiaTri());
    }

    @Override
    public void update(KhoiLuong entity) {
        JDBCHelper.update(UPDATE_SQL, entity.getGiaTri(), entity.getMaKhoiLuong());
    }

    @Override
    public void delete(String id) {
        JDBCHelper.update(DELETE_SQL, id);
    }

    @Override
    public KhoiLuong selectById(String id) {
        List<KhoiLuong> lst = selectBySql(SELECT_BY_ID_SQL, id);
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }
    
    public KhoiLuong selectByGiaTri(String val) {
        List<KhoiLuong> list = selectBySql("select * from KhoiLuong where GiaTri = ?", val);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<KhoiLuong> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    protected List<KhoiLuong> selectBySql(String sql, Object... args) {
        List<KhoiLuong> lst = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {                
                KhoiLuong kl = new KhoiLuong(rs.getInt(1), rs.getString(2), rs.getString(3));
                lst.add(kl);
            }
            rs.getStatement().close();
            return lst;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
