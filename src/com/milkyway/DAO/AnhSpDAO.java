/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Model.AnhSP;
import com.milkyway.Utils.JDBCHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DaiAustinYersin
 */
public class AnhSpDAO extends MilkyWayDAO<AnhSP, String> {

    final String INSERT_SQL = "INSERT INTO [dbo].[AnhSP]([MaAnhSP],[TenAnhSP])VALUES(?,?)";
    final String UPDATE_SQL = "UPDATE [dbo].[AnhSP] SET [TenAnhSP] = ? WHERE [MaAnhSP] = ?";
    final String DELETE_SQL = "DELETE FROM [dbo].[AnhSP] WHERE [MaAnhSP] = ?";
    final String SELECT_BY_ID_SQL = "SELECT * FROM [dbo].[AnhSP] WHERE [MaAnhSP] = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM [dbo].[AnhSP]";

    @Override
    public void insert(AnhSP entity) {
        JDBCHelper.update(INSERT_SQL, entity.getMaAnhSP(), entity.getTenAnhSP());
    }

    @Override
    public void update(AnhSP entity) {
        JDBCHelper.update(UPDATE_SQL, entity.getTenAnhSP(), entity.getMaAnhSP());
    }

    @Override
    public void delete(String id) {
        JDBCHelper.update(DELETE_SQL, id);
    }

    @Override
    public AnhSP selectById(String id) {
        List<AnhSP> lst = selectBySql(SELECT_BY_ID_SQL, id);
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }

    @Override
    public List<AnhSP> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    protected List<AnhSP> selectBySql(String sql, Object... args) {
        try {
            List<AnhSP> lst = new ArrayList<>();
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {                
                AnhSP entity = new AnhSP(rs.getInt(1), rs.getString(2), rs.getString(3));
                lst.add(entity);
            }
            rs.getStatement().close();
            return lst;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
