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
public class KhuyenMaiDao extends MilkyWayDAO<KhuyenMai, String> {

    String Insert_sql = "INSERT INTO [dbo].[KhuyenMai]([MaKM] ,[TenKM],[IDLoaiHang],[IDDongSP],[IDHinhThucThanhToan],[ThoiGianBatDau],[ThoiGianKetThuc],[GiamGia],[MoTa])VALUES(?,?,?,?,?,?,?,?,?)";
    String update_sql = "UPDATE [dbo].[KhuyenMai] SET [IDDongSP] = ?,[IDHinhThucThanhToan] = ?,[ThoiGianBatDau] = ?,[ThoiGianKetThuc] =?,[GiamGia] =?,[MoTa] = ? WHERE MaKM = ?";
    String Select_By_id = "Select * from KhuyenMai Where MaKM=?";
    String Select_all = "Select * from KhuyenMai";

    @Override
    public void insert(KhuyenMai entity) {

        JDBCHelper.update(Insert_sql, entity.getMaKM(),entity.getTenKM(), entity.getIDLoaiHang(), entity.getIDDongSP(), entity.getIDHinhThucThanhToan(), entity.getThoiGianBatDau(), entity.getThoiGianKetThuc(), entity.getGiamGia(), entity.getMoTa());

    }

    @Override
    public void update(KhuyenMai entity) {
        JDBCHelper.update(update_sql, entity.getIDDongSP(), entity.getIDHinhThucThanhToan(), entity.getThoiGianBatDau(), entity.getThoiGianKetThuc(), entity.getGiamGia(),entity.getMoTa(), entity.getMaKM());
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
                entity.setIDLoaiHang(rs.getInt("IDLoaiHang"));
                entity.setIDDongSP(rs.getInt("IDDongSP"));
                entity.setIDHinhThucThanhToan(rs.getInt("IDHinhThucThanhToan"));
                entity.setThoiGianBatDau(rs.getDate("ThoiGianBatDau"));
                entity.setThoiGianKetThuc(rs.getDate("ThoiGianKetThuc"));
                entity.setGiamGia(rs.getInt("giamgia"));
                entity.setMoTa(rs.getString("MoTa"));
                listKM.add(entity);
            }

            rs.getStatement().getConnection().close();
            return listKM;
        } catch (Exception e) {

            throw new RuntimeException(e);
        }

    }

}
