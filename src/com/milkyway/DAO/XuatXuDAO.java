/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Model.XuatXu;
import com.milkyway.Utils.JDBCHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DaiAustinYersin
 */
public class XuatXuDAO extends MilkyWayDAO<XuatXu, String> {
    
    final String INSERT_SQL = "INSERT INTO [dbo].[XuatXu]([MaQG],[TenQG]) VALUES(?,?)";
    final String UPDATE_SQL = "UPDATE [dbo].[XuatXu] SET [TenQG] = ? WHERE [MaQG] = ?";
    final String DELETE_SQL = "DELETE FROM [dbo].[XuatXu] WHERE [MaQG] = ?";
    final String SELECT_BY_ID_SQL = "SELECT * FROM [dbo].[XuatXu] WHERE [MaQG] = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM [dbo].[XuatXu]";

    @Override
    public void insert(XuatXu entity) {
        JDBCHelper.update(INSERT_SQL, entity.getMaQG(), entity.getTenQG());
    }

    @Override
    public void update(XuatXu entity) {
        JDBCHelper.update(UPDATE_SQL, entity.getTenQG(), entity.getMaQG());
    }

    @Override
    public void delete(String id) {
        JDBCHelper.update(DELETE_SQL, id);
    }

    @Override
    public XuatXu selectById(String id) {
        List<XuatXu> lst = selectBySql(SELECT_BY_ID_SQL, id);
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }
    
    public XuatXu selectByTenQG(String name) {
        List<XuatXu> list = selectBySql("select * from XuatXu where TenQG = ?", name);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<XuatXu> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    protected List<XuatXu> selectBySql(String sql, Object... args) {
        List<XuatXu> lst = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {                
                XuatXu kl = new XuatXu(rs.getInt(1), rs.getString(2), rs.getString(3));
                lst.add(kl);
            }
            rs.getStatement().close();
            return lst;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
