/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Model.DonViTinh;
import com.milkyway.Utils.JDBCHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DaiAustinYersin
 */
public class DonViTinhDAO extends MilkyWayDAO<DonViTinh, String> {
    
    final String INSERT_SQL = "INSERT INTO [dbo].[DonViTinh]([MaDVT],[TenDVT]) VALUES(?,?)";
    final String UPDATE_SQL = "UPDATE [dbo].[DonViTinh] SET [TenDVT] = ? WHERE [MaDVT] = ?";
    final String DELETE_SQL = "DELETE FROM [dbo].[DonViTinh] WHERE [MaDVT] = ?";
    final String SELECT_BY_ID_SQL = "SELECT * FROM [dbo].[DonViTinh] WHERE [MaDVT] = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM [dbo].[DonViTinh]";

    @Override
    public void insert(DonViTinh entity) {
        JDBCHelper.update(INSERT_SQL, entity.getMaDVT(), entity.getTenDVT());
    }

    @Override
    public void update(DonViTinh entity) {
        JDBCHelper.update(UPDATE_SQL, entity.getTenDVT(), entity.getMaDVT());
    }

    @Override
    public void delete(String id) {
        JDBCHelper.update(DELETE_SQL, id);
    }

    @Override
    public DonViTinh selectById(String id) {
        List<DonViTinh> lst = selectBySql(SELECT_BY_ID_SQL, id);
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }
    
    public DonViTinh selectByTenDVT(String name) {
        List<DonViTinh> list = selectBySql("select * from DonViTinh where TenDVT = ?", name);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<DonViTinh> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    protected List<DonViTinh> selectBySql(String sql, Object... args) {
        List<DonViTinh> lst = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {                
                DonViTinh dvt = new DonViTinh(rs.getInt(1), rs.getString(2), rs.getString(3));
                lst.add(dvt);
            }
            rs.getStatement().getConnection().close();
            return lst;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
