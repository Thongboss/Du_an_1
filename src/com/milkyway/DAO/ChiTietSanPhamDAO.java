/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Model.ChiTietSanPham;
import com.milkyway.Utils.JDBCHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DaiAustinYersin
 */
public class ChiTietSanPhamDAO extends MilkyWayDAO<ChiTietSanPham, String> {

    final String Insert_SQL = "INSERT INTO [dbo].[ChiTietSanPham]([IDSanPham],[HanSD],[SoLuongTon],[DonGia],[IDXuatXu],[IDKhoiLuong],[IDDonViTinh],[IDAnhSP],[BarCode])\n"
            + "VALUES (?,?,?,?,?,?,?,?,?)";
    final String Update_SQL = "UPDATE [dbo].[ChiTietSanPham]\n"
            + "SET [IDSanPham] = ?,[HanSD] = ?,[SoLuongTon] = ?,[DonGia] = ?,[IDXuatXu] = ?,[IDKhoiLuong] = ?,[IDDonViTinh] = ?,[IDAnhSP] = ?,[BarCode] = ?\n"
            + "WHERE [IDChiTietSP] = ?";
    final String Delete_SQL = "delete from ChiTietSanPham where [IDChiTietSP] = ?";
    final String SelectAll_SQL = "select * from ChiTietSanPham";
    final String SelectByID_SQL = "select * from ChiTietSanPham where [IDChiTietSP] = ?";

    @Override
    public void insert(ChiTietSanPham entity) {
        JDBCHelper.update(Insert_SQL, entity.getIDSanPham(), entity.getHanSD(), entity.getSoLuongTon(), entity.getDonGia(), entity.getIDXuatXu(), entity.getIDKhoiLuong(), entity.getIDDonViTinh(), entity.getIDAnhSP(), entity.getBarCode());
    }

    @Override
    public void update(ChiTietSanPham entity) {
        JDBCHelper.update(Update_SQL, entity.getIDSanPham(), entity.getHanSD(), entity.getSoLuongTon(), entity.getDonGia(), entity.getIDXuatXu(), entity.getIDKhoiLuong(), entity.getIDDonViTinh(), entity.getIDAnhSP(), entity.getBarCode());
    }

    @Override
    public void delete(String id) {
        JDBCHelper.update(Delete_SQL, id);
    }

    @Override
    public ChiTietSanPham selectById(String id) {
        List<ChiTietSanPham> lst = selectBySql(SelectByID_SQL, id);
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }

    @Override
    public List<ChiTietSanPham> selectAll() {
        return this.selectBySql(SelectAll_SQL);
    }

    @Override
    protected List<ChiTietSanPham> selectBySql(String sql, Object... args) {
        try {
            List<ChiTietSanPham> lst = new ArrayList<>();
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {                
                ChiTietSanPham ctsp = new ChiTietSanPham();
                ctsp.setIDChiTietSP(rs.getInt("IDChiTietSP"));
                ctsp.setIDSanPham(rs.getInt("IDSanPham"));
                ctsp.setIDChiTietSP(rs.getInt("HanSD"));
                ctsp.setIDChiTietSP(rs.getInt("SoLuongTon"));
                ctsp.setDonGia(rs.getDouble("DonGia"));
                ctsp.setIDChiTietSP(rs.getInt("IDXuatXu"));
                ctsp.setIDChiTietSP(rs.getInt("IDKhoiLuong"));
                ctsp.setIDChiTietSP(rs.getInt("IDDonViTinh"));
                ctsp.setIDChiTietSP(rs.getInt("IDAnhSP"));
                ctsp.setBarCode(rs.getString("BarCode"));
                lst.add(ctsp);
            }
            rs.getStatement().getConnection().close();
            return lst;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
