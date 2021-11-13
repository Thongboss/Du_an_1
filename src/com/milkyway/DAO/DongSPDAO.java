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
public class DongSPDAO extends MilkyWayDao<DongSP, String> {
    String INSERT_SQL = "INSERT INTO DongSP(MaDongSP,TenDongSP,GhiChu,TrangThai) values(?,?,?)";
    String UPDATE_SQL = "update DongSP set  TenDongSP = ? Ghi Chu =? TrangThai = ? where MaDongSP = ?";
    String DELETE_SQL = "Delete from DongSP where MaDongSP =?";
    String SELECT_ALL = "Select * from DongSP";
    String SELECT_BY_ID = "Select * from DongSP where MaDongSP=?";

    @Override
    public void insert(DongSP entity) {
        try {
            JDBCHelper.update(INSERT_SQL, entity.getMaDongSP(),entity.getTenDongSP(),entity.getGhiChu(),entity.isTrangThai());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(DongSP entity) {
      
        try {
              JDBCHelper.update(UPDATE_SQL, entity.getTenDongSP(),entity.getGhiChu(),entity.isTrangThai());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        try {
            JDBCHelper.update(DELETE_SQL, id);
        } catch (Exception e) {
        }
    }

    @Override
    public DongSP selectById(String id) {
        List<DongSP> list = this.selectBySql(SELECT_BY_ID,id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
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
            while(rs.next()){
                DongSP entity = new DongSP();
                entity.setMaDongSP(rs.getString("MaDongSP"));
                entity.setTenDongSP(rs.getString("TenDongSP"));
                
                entity.setGhiChu(rs.getString("GhiChu"));
                entity.setTrangThai(rs.getBoolean("TrangThai"));
                
            }
            rs.getStatement().getConnection().close();
            return lstDongSP;
        } catch (Exception e) {
                e.printStackTrace();
            throw new RuntimeException(e);
        }
          
    }

   
    
}
