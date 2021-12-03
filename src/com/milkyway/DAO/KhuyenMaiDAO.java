/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Utils.JDBCHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.milkyway.Model.KhuyenMai;

/**
 *
 * @author ASUS
 */
public class KhuyenMaiDAO extends MilkyWayDAO<KhuyenMai, String> {

    final String Insert_sql = "INSERT INTO [dbo].[KhuyenMai]([MaKM] ,[TenKM],[IDSanPham],[IDDongSP],[ThoiGianBatDau],[ThoiGianKetThuc],[GiamGia],[MoTa])VALUES(?,?,?,?,?,?,?,?)";
    final String update_sql = "UPDATE [dbo].[KhuyenMai] SET [IDSanPham] = ?, [IDDongSP] = ?,[ThoiGianBatDau] = ?,[ThoiGianKetThuc] =?,[GiamGia] =?,[MoTa] = ? WHERE MaKM = ?";
    final String Select_By_id = "Select * from KhuyenMai Where MaKM=?";
    final String Select_all = "Select * from KhuyenMai";
    final String Select_All_ConHan = "Select * from KhuyenMai where ThoiGianKetThuc >= getdate()";
    final String Select_All_HetHan = "Select * from KhuyenMai where ThoiGianKetThuc < getdate()";

    @Override
    public void insert(KhuyenMai entity) {
        JDBCHelper.update(Insert_sql, entity.getMaKM(),entity.getTenKM(), entity.getIDSanPham(), entity.getIDDongSP(), entity.getThoiGianBatDau(), entity.getThoiGianKetThuc(), entity.getGiamGia(), entity.getMoTa());
    }

    @Override
    public void update(KhuyenMai entity) {
        JDBCHelper.update(update_sql, entity.getIDSanPham(), entity.getIDDongSP(),  entity.getThoiGianBatDau(), entity.getThoiGianKetThuc(), entity.getGiamGia(),entity.getMoTa(), entity.getMaKM());
    }

    @Override
    public void delete(String id) {
    }

    @Override
    public KhuyenMai selectById(String id) {
        List<KhuyenMai> list = this.selectBySql(Select_By_id, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<KhuyenMai> selectAll() {
        return selectBySql(Select_all);
    }
    
    public List<KhuyenMai> selectAllConHan() {
        return selectBySql(Select_All_ConHan);
    }
    
    public List<KhuyenMai> selectAllHetHan() {
        return selectBySql(Select_All_HetHan);
    }

    @Override
    protected List<KhuyenMai> selectBySql(String sql, Object... args) {
        List<KhuyenMai> listKM = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                KhuyenMai entity = new KhuyenMai();
                entity.setIDKhuyenMai(rs.getInt("IDKhuyenMai"));
                entity.setMaKM(rs.getString("MaKM"));
                entity.setTenKM(rs.getString("TenKM"));
                entity.setIDSanPham(rs.getInt("IDSanPham"));
                entity.setIDDongSP(rs.getInt("IDDongSP"));
               
                entity.setThoiGianBatDau(rs.getDate("ThoiGianBatDau"));
                entity.setThoiGianKetThuc(rs.getDate("ThoiGianKetThuc"));
                entity.setGiamGia(rs.getInt("giamgia"));
                entity.setMoTa(rs.getString("MoTa"));
                listKM.add(entity);
            }

            rs.getStatement().getConnection().close();
            return listKM;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
