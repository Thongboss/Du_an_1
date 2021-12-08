/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Model.ChiTietComboSP;
import com.milkyway.Utils.JDBCHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DaiAustinYersin
 */
public class ChiTietComboSPDAO extends MilkyWayDAO<ChiTietComboSP, String>{
    
    final String INSERT_SQL = "INSERT INTO [dbo].[ChiTietComboSP]([IDComboSP],[IDChiTietSP],[SoLuongSP]) VALUES (?,?,?)";
    final String DELETE_SQL = "DELETE FROM [dbo].[ChiTietComboSP] WHERE [IDComboSP] = ?";
    final String SELECT_BY_ID_SQL = "SELECT * FROM [dbo].[ChiTietComboSP] WHERE [IDChiTietComboSP] = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM [dbo].[ChiTietComboSP]";

    @Override
    public void insert(ChiTietComboSP entity) {
        JDBCHelper.update(INSERT_SQL, entity.getIDComboSP(), entity.getIDChiTietSP(), entity.getSoLuongSP());
    }

    @Override
    public void update(ChiTietComboSP entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ChiTietComboSP selectById(String id) {
        List<ChiTietComboSP> lst = selectBySql(SELECT_BY_ID_SQL, id);
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }

    @Override
    public List<ChiTietComboSP> selectAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected List<ChiTietComboSP> selectBySql(String sql, Object... args) {
        List<ChiTietComboSP> lst = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {                
                ChiTietComboSP cb = new ChiTietComboSP();
                cb.setIDChiTietComboSP(rs.getInt(1));
                cb.setIDComboSP(rs.getInt(2));
                cb.setIDChiTietSP(rs.getInt(3));
                lst.add(cb);
            }
            rs.getStatement().close();
            return lst;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
