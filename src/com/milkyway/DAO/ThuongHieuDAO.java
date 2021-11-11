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
public class ThuongHieuDAO extends MilkyWayDao<ThuongHieu, String> {
     String INSERT_SQL = "Insert into ThuongHieu(MaTH,TenTH,GhiChu,TrangThai) values (?,?,?,?)";
    String UPDATE_SQL = "Update ThuongHieu set TenTH =? GhiChu =? TrangThai =? where MaTH =? ";
    String DELETE_SQL = "Delete from ThuongHieu where MaTH =?";
    String SELECT_ALL = "Select * from ThuongHieu";
    String SELECT_BY_ID = "Select * from ThuongHieu where MaTH=?";

    @Override
    public void insert(ThuongHieu entity) {
        try {
            JDBCHelper.update(INSERT_SQL, entity.getMaTH(),entity.getTenTh(),entity.getGhiChu(),entity.isTrangthai());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(ThuongHieu entity) {
        try {
            JDBCHelper.update(UPDATE_SQL, entity.getTenTh(),entity.getGhiChu(),entity.isTrangthai());
        } catch (Exception e) {
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
    public ThuongHieu selectById(String id) {
        List<ThuongHieu> list = this.selectBySql(SELECT_BY_ID, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
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
            while (rs.next()){
                ThuongHieu entity = new ThuongHieu();
                entity.setMaTH(rs.getString("MaTH"));
                entity.setTenTh(rs.getString("TenTH"));
                entity.setGhiChu(rs.getString("GhiChu"));
                entity.setTrangthai(rs.getBoolean("TrangThai"));
            }
            rs.getStatement().getConnection().close();
            return lstThuongHieu;
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException(e);
        }
    }
    
}
