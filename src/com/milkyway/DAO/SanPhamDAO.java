/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Model.SanPham;
import com.milkyway.Utils.JDBCHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hoang
 */
public class SanPhamDAO extends MilkyWayDAO<SanPham, String> {

    final String Insert_SQL = "INSERT INTO [dbo].[SanPham]([MaSP],[IDLoaiSP],[IDDongSP],[TenSP],[GhiChu])\n"
            + "VALUES(?,?,?,?,?)";
    final String Update_SQL = "UPDATE [dbo].[SanPham]\n"
            + "SET [IDLoaiSP] = ?,[IDDongSP] = ?,[TenSP] = ?,[GhiChu] = ?,[TrangThai] = ?\n"
            + " WHERE [MaSP] = ?";
    final String Delete_SQL = "DELETE FROM SanPham where MaSP = ?";
    final String SelectAll_SQL = "select * from SanPham MaSP";
    final String SelectByID_SQL = "select * from SanPham MaSP = ?";
    final String select_All_About_SanPham = "	select MaSP, TenSP, TenLoai, TenDongSP, NgayXK, HanSD, SoLuongTon, DonGia, TenQG, GiaTri, TenDVT, BarCode, a.GhiChu, a.TrangThai, TenAnhSP\n"
            + "	from SanPham a join ChiTietSanPham b on a.IDSanPham = b.IDSanPham\n"
            + "	join LoaiHang c on c.IDLoaiHang = a.IDLoaiSP\n"
            + "	join DongSP d on d.IDDongSP = a.IDDongSP\n"
            + "	join XuatXu e on e.IDXuatXu = b.IDXuatXu\n"
            + "	join DonViTinh f on f.IDDonViTinh = b.IDDonViTinh\n"
            + "	join AnhSP g on g.IDAnhSP = b.IDAnhSP\n"
            + "	join KhoiLuong h on h.IDKhoiLuong = b.IDKhoiLuong\n"
            + "	where a.TrangThai = ?";

    @Override
    public void insert(SanPham entity) {
        JDBCHelper.update(Insert_SQL, entity.getMaSP(), entity.getIDLoaiSP(), entity.getIDDongSP(), entity.getTenSP(), entity.getGhiChu());
    }

    @Override
    public void update(SanPham entity) {
        JDBCHelper.update(Update_SQL, entity.getIDLoaiSP(), entity.getIDDongSP(), entity.getTenSP(), entity.getGhiChu(), entity.isTrangThai());
    }

    @Override
    public void delete(String id) {
        JDBCHelper.update(Delete_SQL, id);
    }

    @Override
    public SanPham selectById(String id) {
        List<SanPham> lst = selectBySql(SelectByID_SQL, id);
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }

    @Override
    public List<SanPham> selectAll() {
        return this.selectBySql(SelectAll_SQL);
    }

    @Override
    protected List<SanPham> selectBySql(String sql, Object... args) {
        try {
            List<SanPham> lst = new ArrayList<>();
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setIDSanPham(rs.getInt(1));
                sp.setMaSP(rs.getString(2));
                sp.setIDLoaiSP(rs.getInt("IDLoaiSP"));
                sp.setIDDongSP(rs.getInt("IDDongSP"));
                sp.setTenSP(rs.getString("TenSP"));
                sp.setNgayXK(rs.getDate("NgayXK"));
                sp.setGhiChu(rs.getString("GhiChu"));
                sp.setTrangThai(rs.getBoolean("TrangThai"));
                lst.add(sp);
            }
            rs.getStatement().getConnection().close();
            return lst;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
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

    public List<Object[]> getAllAboutSanPhamNgungKD() {
        String[] cols = {"MaSP", "TenSP", "DonGia", "TenQG", "GiaTri", "TenDVT", "TenAnhSP", "BarCode"};
        return this.getListOfArray(select_All_About_SanPham, cols, false);
    }
}
