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
public class ComBoSPDAO extends MilkyWayDAO<ComBoSP, String> {

    final String INSERT_SQL = "INSERT INTO [dbo].[ComBoSP]([MaComboSP],[TenComboSP],[IDChiTietSP],[SoLuongSP],[SoLuongComboSP],[DonGia],[GiamGia],[NgayHetHan],[GhiChu],[AnhComBoSP])\n"
            + "     VALUES (?,?,?,?,?,?,?,?,?,?)";
    final String UPDATE_SQL = "UPDATE [dbo].[ComBoSP] SET [TenComboSP] = ?,[SoLuongSP] = ?,[SoLuongComboSP] = ?,[DonGia] = ?,[GiamGia] = ?,[NgayHetHan] = ?,[GhiChu] = ?,[AnhComBoSP] = ?\n"
            + " WHERE [MaComboSP] = ?";
    final String DELETE_SQL = "DELETE FROM [dbo].[ComBoSP] WHERE [MaComboSP] = ?";
    final String SELECT_BY_ID_SQL = "SELECT * FROM [dbo].[ComBoSP] WHERE [MaComboSP] = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM [dbo].[ComBoSP]";

    @Override
    public void insert(ComBoSP entity) {
//        JDBCHelper.update(INSERT_SQL, entity.getMaComboSP(), entity.getTenComboSP(), entity.getIDChiTietSP(), entity.getSoLuongSP(), entity.getSoLuongComboSP(), entity.getDonGia(), entity.getGiamGia(), entity.getNgayHetHan(), entity.getGhiChu(), entity.getAnhComboSP());
    }

    @Override
    public void update(ComBoSP entity) {
//        JDBCHelper.update(UPDATE_SQL, entity.getTenComboSP(), entity.getSoLuongSP(), entity.getSoLuongComboSP(), entity.getDonGia(), entity.getGiamGia(), entity.getNgayHetHan(), entity.getGhiChu(), entity.getAnhComboSP());
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

    @Override
    protected List<ComBoSP> selectBySql(String sql, Object... args) {
        List<ComBoSP> lst = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
//                ComBoSP cb = new ComBoSP(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getDouble(7), rs.getInt(8), rs.getDate(9), rs.getDate(10), rs.getString(11), rs.getString(12));
//                lst.add(cb);
            }
            rs.getStatement().getConnection().close();
            return lst;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
