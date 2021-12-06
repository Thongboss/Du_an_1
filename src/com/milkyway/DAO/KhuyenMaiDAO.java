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
import com.milkyway.Model.SanPham;

/**
 *
 * @author ASUS
 */
public class KhuyenMaiDAO extends MilkyWayDAO<KhuyenMai, String> {

    final String Insert_sql = "INSERT INTO [dbo].[KhuyenMai]([MaKM] ,[TenKM],[IDSanPham],[IDDongSP],[ThoiGianBatDau],[ThoiGianKetThuc],[GiamGia],[MoTa])VALUES(?,?,?,?,?,?,?,?)";
    final String update_sql = "UPDATE [dbo].[KhuyenMai] SET [TenKM] = ? , [ThoiGianKetThuc] =?,[GiamGia] =?,[MoTa] = ? WHERE MaKM = ?";
    final String Select_By_id = "Select * from KhuyenMai Where MaKM=?";
    final String Select_all = "Select * from KhuyenMai";
    final String Select_All_ConHan = "Select * from KhuyenMai where ThoiGianKetThuc >= getdate()";
    final String Select_All_HetHan = "Select * from KhuyenMai where ThoiGianKetThuc < getdate()";
    final String Insert_sql_dongsp = "INSERT INTO [dbo].[KhuyenMai]([MaKM],[TenKM],[IDSanPham],[IDDongSP],[ThoiGianBatDau],[ThoiGianKetThuc],[GiamGia],[MoTa]) values(?,?,?,?,?,?,?,?)";
    final String View_Table = "select MaKM, TenKM, TenDongSP, TenSP, ThoiGianBatDau, ThoiGianKetThuc,GiamGia,MoTa from KhuyenMai join DongSP on KhuyenMai.IDDongSP = DongSP.IDDongSP\n" +
"left outer join SanPham on KhuyenMai.IDSanPham = SanPham.IDSanPham";
    final String select_SP = "SELECT * FROM SANPHAM";
    

    @Override
    public void insert(KhuyenMai entity) {
        JDBCHelper.update(Insert_sql, entity.getMaKM(),entity.getTenKM(), entity.getIDSanPham(), entity.getIDDongSP(), entity.getThoiGianBatDau(), entity.getThoiGianKetThuc(), entity.getGiamGia(), entity.getMoTa());
    }

    @Override
    public void update(KhuyenMai entity) {
        JDBCHelper.update(update_sql,entity.getTenKM(),entity.getThoiGianKetThuc(), entity.getGiamGia(),entity.getMoTa(), entity.getMaKM());
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
     public void insertDSP(KhuyenMai entity) {
         
        JDBCHelper.update(Insert_sql_dongsp, entity.getMaKM(),entity.getTenKM(),entity.getIDSanPham()== 0 ? null : entity.getIDSanPham(), entity.getIDDongSP(), entity.getThoiGianBatDau(), entity.getThoiGianKetThuc(), entity.getGiamGia(), entity.getMoTa());
    }
      public void insertSP(KhuyenMai entity) {
         
        JDBCHelper.update(Insert_sql_dongsp, entity.getMaKM(),entity.getTenKM(),entity.getIDSanPham(), entity.getIDDongSP()==0? null : entity.getIDDongSP(), entity.getThoiGianBatDau(), entity.getThoiGianKetThuc(), entity.getGiamGia(), entity.getMoTa());
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
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
        public List<Object[]> ViewTable(){
            try {
                String cols[] = {"MaKM", "TenKM", "TenDongSP", "TenSP", "ThoiGianBatDau","ThoiGianKetThuc","GiamGia","MoTa"};
                return this.getListOfArray(View_Table, cols);
                
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
         private List<SanPham> selectBySP(String sql, Object... args) {
        try {
            List<SanPham> list = new ArrayList<>();
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                SanPham ttv = new SanPham();
                ttv.setGhiChu(rs.getString("GhiChu"));
                ttv.setIDDongSP(rs.getInt("IDDongSP"));
                ttv.setIDLoaiSP(rs.getInt("IDLoaiSP"));
                ttv.setIDSanPham(rs.getInt("IDSanPham"));
                ttv.setMaSP(rs.getString("MaSP"));
                ttv.setNgayXK(rs.getDate("NgayXK"));
                ttv.setTenSP(rs.getString("TenSP"));
                ttv.setTrangThai(rs.getBoolean("TrangThai"));
               
                list.add(ttv);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<SanPham> loadListSP() {
        return this.selectBySP(select_SP);
    }
}
