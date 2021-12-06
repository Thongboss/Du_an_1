/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Model.ChiTietSanPham;
import com.milkyway.Model.DongSP;
import com.milkyway.Model.SanPham;
import com.milkyway.Utils.JDBCHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author hoang
 */
public class SanPhamDAO {
    
    final String select_All_About_SanPham = "{call SP_SelectAllSanPham(?)}";
    final String select_All_About_SanPham_By_ID = "{call SP_SelectAllSanPhamByID(?,?)}";
    final String insert_All_About_SanPham = "{call SP_InsertAllSanPham(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    final String update_SanPham = "{call SP_UpdateSanPham(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    final String update_TrangThai_SanPham_By_Id = "update SanPham set TrangThai = ? where MaSP = ?";
    final String select_SanPham_SapHetHan = "{call SP_SelectAllSanPhamSapHetHan()}";
    final String select_SanPham_By_BarCode = "{call SP_SelectAllSanPhamByBarCode(?,?)}";
    final String select_SP = "SELECT * FROM SANPHAM";
    
    public void insertSanPham(Object[] obj) {
        SanPham sp = new SanPham();
        sp.setMaSP(obj[0].toString());
        sp.setTenSP(obj[1].toString());
        sp.setIDLoaiSP(Integer.parseInt(obj[2].toString()));
        sp.setIDDongSP(Integer.parseInt(obj[3].toString()));
        sp.setGhiChu(obj[12].toString());
        ChiTietSanPham ctsp = new ChiTietSanPham();
        ctsp.setHanSD(Date.class.cast(obj[5]));
        ctsp.setSoLuongTon(Integer.parseInt(obj[6].toString()));
        ctsp.setDonGia(Double.parseDouble(obj[7].toString()));
        ctsp.setIDXuatXu(Integer.parseInt(obj[8].toString()));
        ctsp.setIDKhoiLuong(Integer.parseInt(obj[10].toString()));
        ctsp.setIDDonViTinh(Integer.parseInt(obj[9].toString()));
        ctsp.setIDAnhSP(Integer.parseInt(obj[14].toString()));
        ctsp.setBarCode(obj[11].toString());
        JDBCHelper.update(insert_All_About_SanPham, sp.getMaSP(), sp.getIDLoaiSP(), sp.getIDDongSP(), sp.getTenSP(), sp.getGhiChu(), ctsp.getIDSanPham(), ctsp.getHanSD(), ctsp.getSoLuongTon(), ctsp.getDonGia(), ctsp.getIDXuatXu(), ctsp.getIDKhoiLuong(), ctsp.getIDDonViTinh(), ctsp.getIDAnhSP(), ctsp.getBarCode());
    }
    
    public void updateSanPham(Object[] obj) {
        SanPham sp = new SanPham();
        sp.setMaSP(obj[0].toString());
        sp.setTenSP(obj[1].toString());
        sp.setIDLoaiSP(Integer.parseInt(obj[2].toString()));
        sp.setIDDongSP(Integer.parseInt(obj[3].toString()));
        sp.setGhiChu(obj[12].toString());
        ChiTietSanPham ctsp = new ChiTietSanPham();
        ctsp.setHanSD(Date.class.cast(obj[5]));
        ctsp.setSoLuongTon(Integer.parseInt(obj[6].toString()));
        ctsp.setDonGia(Double.parseDouble(obj[7].toString()));
        ctsp.setIDXuatXu(Integer.parseInt(obj[8].toString()));
        ctsp.setIDKhoiLuong(Integer.parseInt(obj[10].toString()));
        ctsp.setIDDonViTinh(Integer.parseInt(obj[9].toString()));
        ctsp.setIDAnhSP(Integer.parseInt(obj[14].toString()));
        ctsp.setBarCode(obj[11].toString());
        JDBCHelper.update(update_SanPham, ctsp.getHanSD(), ctsp.getSoLuongTon(), ctsp.getDonGia(), ctsp.getIDXuatXu(), ctsp.getIDKhoiLuong(), ctsp.getIDDonViTinh(), ctsp.getIDAnhSP(), ctsp.getBarCode(), sp.getMaSP(), sp.getIDLoaiSP(), sp.getIDDongSP(), sp.getTenSP(), sp.getGhiChu());
    }
    
    public void updateTrangThaiById(boolean status, String id) {
        JDBCHelper.update(update_TrangThai_SanPham_By_Id, status, id);
    }

    private List<Object[]> getListOfArray(String sql, String[] cols, Object... args) {
        try {
            List<Object[]> list = new ArrayList<>();
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                Object[] vals = new Object[cols.length];
                for (int i = 0; i < cols.length; i++) {
                    vals[i] = rs.getObject(cols[i]);
                }
                list.add(vals);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Object[]> getAllAboutSanPhamDangKD() {
        String[] cols = {"MaSP", "TenSP", "TenLoai", "TenDongSP", "NgayXK", "HanSD", "SoLuongTon", "DonGia", "TenQG", "GiaTri", "TenDVT", "BarCode", "GhiChu", "TrangThai", "TenAnhSP"};
        return this.getListOfArray(select_All_About_SanPham, cols, true);
    }
    
    public Object[] getAllAboutSanPhamDangKDByID(String id) {
        String[] cols = {"MaSP", "TenSP", "TenLoai", "TenDongSP", "NgayXK", "HanSD", "SoLuongTon", "DonGia", "TenQG", "GiaTri", "TenDVT", "BarCode", "GhiChu", "TrangThai", "TenAnhSP"};
        List<Object[]> lst =  this.getListOfArray(select_All_About_SanPham_By_ID, cols, true, id);
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }

    public List<Object[]> getAllAboutSanPhamNgungKD() {
        String[] cols = {"MaSP", "TenSP", "TenLoai", "TenDongSP", "NgayXK", "HanSD", "SoLuongTon", "DonGia", "TenQG", "GiaTri", "TenDVT", "BarCode", "GhiChu", "TrangThai", "TenAnhSP"};
        return this.getListOfArray(select_All_About_SanPham, cols, false);
    }
    
    public Object[] getAllAboutSanPhamNgungKDByID(String id) {
        String[] cols = {"MaSP", "TenSP", "TenLoai", "TenDongSP", "NgayXK", "HanSD", "SoLuongTon", "DonGia", "TenQG", "GiaTri", "TenDVT", "BarCode", "GhiChu", "TrangThai", "TenAnhSP"};
        List<Object[]> lst =  this.getListOfArray(select_All_About_SanPham_By_ID, cols, false, id);
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }
    
    public List<Object[]> getAllAboutSanPhamSapHetHan() {
        String[] cols = {"MaSP", "TenSP", "TenLoai", "TenDongSP", "NgayXK", "HanSD", "SoLuongTon", "DonGia", "TenQG", "GiaTri", "TenDVT", "BarCode", "GhiChu", "TrangThai", "TenAnhSP"};
        return this.getListOfArray(select_SanPham_SapHetHan, cols);
    }
    
    public Object[] getAllAboutSanPhamDangKDByBarCode(String barcode) {
        String[] cols = {"MaSP", "TenSP", "TenLoai", "TenDongSP", "NgayXK", "HanSD", "SoLuongTon", "DonGia", "TenQG", "GiaTri", "TenDVT", "BarCode", "GhiChu", "TrangThai", "TenAnhSP"};
        List<Object[]> lst =  this.getListOfArray(select_SanPham_By_BarCode, cols, true, barcode);
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }
    
    public int select_Id_ChiTietSP_By_MaSP(String id) {
        try {
            ResultSet rs = JDBCHelper.query("select IDChiTietSP from ChiTietSanPham where IDSanPham = (select IDSanPham from SanPham where MaSP = ?)", id);
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void update_SoLuong_By_MaSP(String maSP, int soLuong) {
        JDBCHelper.update("update ChiTietSanPham set SoLuongTon = ? where IDSanPham = (select IDSanPham from SanPham where MaSP = ?)", soLuong, maSP);
    }
        public SanPham select_SPbyID(String name) {
       List<SanPham> lst = selectBySql("select * from SanPham where TenSP =?", name);
            if (lst.isEmpty()) {
                return null;
                
            }
            return lst.get(0);
    }
        public SanPham selectbyIDSanPham(int IDSanPham){
           List<SanPham> list = selectBySql("Select * from SanPham where IDSanPham = ?", IDSanPham);
           if(list.isEmpty()){
               return null;
           }
           return list.get(0);
       }
       protected List<SanPham> selectBySql(String sql, Object... args){
           try {
               List<SanPham> lst = new ArrayList<>();
               ResultSet rs = new JDBCHelper().query(sql, args);
               while (rs.next()){
                   SanPham entity = new SanPham();
                   entity.setIDSanPham(rs.getInt("IDSanPham"));
                   entity.setMaSP(rs.getString("MaSP"));
                   entity.setIDLoaiSP(rs.getInt("IDLoaiSP"));
                   entity.setIDDongSP(rs.getInt("IDDongSP"));
                   entity.setTenSP(rs.getString("TenSP"));
                   entity.setNgayXK(rs.getDate("NgayXK"));
                   entity.setGhiChu(rs.getString("GhiChu"));
                   entity.setTrangThai(rs.getBoolean("TrangThai"));
                   lst.add(entity);
               }
               rs.getStatement().getConnection().close();
               return lst;
           } catch (Exception e) {
                e.printStackTrace();
            throw new RuntimeException(e);
           }
       }
      
}
