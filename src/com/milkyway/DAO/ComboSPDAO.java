/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Model.ComBoSP;
import com.milkyway.Utils.JDBCHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DaiAustinYersin
 */
public class ComboSPDAO extends MilkyWayDAO<ComBoSP, String> {

    final String INSERT_SQL = "INSERT INTO [dbo].[ComBoSP]([MaComboSP],[TenComboSP],[SoLuongSP],[TongGia],[NgayHetHan],[Barcode],[GhiChu],[AnhComBoSP])\n"
            + "	VALUES (?,?,?,?,?,?,?,?)";
    final String UPDATE_SQL = "UPDATE [dbo].[ComBoSP] SET [TenComboSP] = ?,[SoLuongSP] = ?,[TongGia] = ?,[NgayHetHan] = ?,[Barcode] = ?,[GhiChu] = ?,[AnhComBoSP] = ?\n"
            + " WHERE [MaComboSP] = ?";
    final String DELETE_SQL = "DELETE FROM [dbo].[ComBoSP] WHERE [MaComboSP] = ?";
    final String SELECT_BY_ID_SQL = "SELECT * FROM [dbo].[ComBoSP] WHERE [MaComboSP] = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM [dbo].[ComBoSP]";

    @Override
    public void insert(ComBoSP entity) {
        JDBCHelper.update(INSERT_SQL, entity.getMaComboSP(), entity.getTenComboSP(), entity.getSoLuongSP(), entity.getTongGia(), entity.getNgayHetHan(), entity.getBarcode(), entity.getGhiChu(), entity.getAnhComboSP());
    }

    @Override
    public void update(ComBoSP entity) {
        JDBCHelper.update(UPDATE_SQL, entity.getTenComboSP(), entity.getSoLuongSP(), entity.getTongGia(), entity.getNgayHetHan(), entity.getBarcode(), entity.getGhiChu(), entity.getAnhComboSP(), entity.getMaComboSP());
    }

    @Override
    public void delete(String id) {
        JDBCHelper.update(DELETE_SQL, id);
    }

    @Override
    public ComBoSP selectById(String id) {
        List<ComBoSP> lst = selectBySql(SELECT_BY_ID_SQL, id);
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }

    @Override
    public List<ComBoSP> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }
    
    public List<ComBoSP> selectAll_ConHan() {
        return selectBySql("select * from ComBoSP where NgayHetHan >= getdate()");
    }
    
    public List<ComBoSP> selectAll_HetHan() {
        return selectBySql("select * from ComBoSP where NgayHetHan < getdate()");
    }

    @Override
    protected List<ComBoSP> selectBySql(String sql, Object... args) {
        List<ComBoSP> lst = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {                
                ComBoSP cb = new ComBoSP();
                cb.setIDComboSP(rs.getInt(1));
                cb.setMaComboSP(rs.getString(2));
                cb.setTenComboSP(rs.getString(3));
                cb.setSoLuongSP(rs.getInt(4));
                cb.setTongGia(rs.getDouble(5));
                cb.setNgayTao(rs.getDate(6));
                cb.setNgayHetHan(rs.getDate(7));
                cb.setBarcode(rs.getString(8));
                cb.setGhiChu(rs.getString(9));
                cb.setAnhComboSP(rs.getString(10));
                lst.add(cb);
            }
            rs.getStatement().close();
            return lst;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
