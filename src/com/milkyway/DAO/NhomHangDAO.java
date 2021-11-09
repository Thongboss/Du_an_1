/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Model.NhanVien;
import com.milkyway.Model.NhomHang;
import com.milkyway.Utils.JDBCHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hoang
 */
public class NhomHangDAO extends MilkyWayDao<NhomHang, String>{
    String INSERT_SQL = "Insert into NhomHang(MaNhom,TenNhom,GhiChu) values (?,?,?)";
    String UPDATE_SQL = "Update NhomHang TenNhom =?,GhiChu=? where MaNhom = ?";
    String DELETE_SQL = "Delete from NhomHang where MaNhom =?";
    String SELECT_ALL = "Select * from NhomHang";
    String SELECT_BY_ID = "Select * from NhomHang where MaNhom=?";

    @Override
    public void insert(NhomHang entity) {
        try {
            JDBCHelper.update(INSERT_SQL, entity.getMaNhom(),entity.getTenNhom(),entity.getGhiChu());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(NhomHang entity) {
        try {
            JDBCHelper.update(UPDATE_SQL, entity.getTenNhom(),entity.getGhiChu());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        try {
            JDBCHelper.update(DELETE_SQL, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public NhomHang selectById(String id) {
        List<NhomHang> list = this.selectBySql(SELECT_BY_ID, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<NhomHang> selectAll() {
        return this.selectBySql(SELECT_ALL);
    }

    @Override
    protected List<NhomHang> selectBySql(String sql, Object... args) {
        try {
            List<NhomHang> lstNhomHang =  new ArrayList<>();
            ResultSet rs = JDBCHelper.query(sql, args);
            while(rs.next()){
                NhomHang entity = new NhomHang();
                entity.setMaNhom("MaNhom");
                entity.setTenNhom("TenNhom");
                entity.setGhiChu("GhiChu");
            }
             rs.getStatement().getConnection().close();
            return lstNhomHang;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        
    }
    
}
