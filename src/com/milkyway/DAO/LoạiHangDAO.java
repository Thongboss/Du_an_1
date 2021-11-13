/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Model.LoaiHang;
import com.milkyway.Model.NhomHang;
import com.milkyway.Utils.JDBCHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hoang
 */
public class LoạiHangDAO extends MilkyWayDao<LoaiHang, String>{
    String INSERT_SQL = "INSERT INTO LoaiHang(MaLoai,TenLoai,GhiChu) values(?,?,?)";
    String UPDATE_SQL = "update LoaiHang set  TenLoai = ? Ghi Chu =? where MaLoai = ?";
    String DELETE_SQL = "Delete from LoaiHang where MaLoai =?";
    String SELECT_ALL = "Select * from LoaiHang";
    String SELECT_BY_ID = "Select * from LoaiHang where MaLoai=?";

    @Override
    public void insert(LoaiHang entity) {
        try {
            JDBCHelper.update(INSERT_SQL, entity.getMaLoai(),entity.getTenLoai(),entity.getGhiChu());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(LoaiHang entity) {
        try {
            JDBCHelper.update(UPDATE_SQL, entity.getTenLoai(),entity.getGhiChu());
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
    public LoaiHang selectById(String id) {
        List<LoaiHang> list = this.selectBySql(SELECT_BY_ID, id);
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
             List<LoaiHang> lstLoaiHang =  new ArrayList<>();
            ResultSet rs = JDBCHelper.query(sql, args);
            while(rs.next()){
                LoaiHang entity = new LoaiHang();
                entity.setMaLoai(rs.getString("MaLoai"));
                entity.setTenLoai(rs.getString("TenLoai"));
                entity.setGhiChu(rs.getString("GhiChu"));
            }
             rs.getStatement().getConnection().close();
            return lstLoaiHang;
        } catch (Exception e) {
              e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

   

   
    
}
