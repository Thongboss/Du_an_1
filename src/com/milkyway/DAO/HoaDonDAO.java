/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Utils.JDBCHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author van95
 */
public class HoaDonDAO {

    String select_sql = "select MaHD, HoaDon.IDNhanVien, MaNV, HoaDon.IDTheTV, MaTheTV, TongTien,NgayLap,HoaDon.GhiChu,HoaDon.TrangThai from HoaDon\n"
            + "join NhanVien on HoaDon.IDNhanVien = NhanVien.IDNhanVien\n"
            + "left outer join TheThanhVien on TheThanhVien.IDTheTV = HoaDon.IDTheTV";
    String select_by_id_sql = "select MaHD, HoaDon.IDNhanVien, MaNV, HoaDon.IDTheTV, MaTheTV, TongTien,NgayLap,HoaDon.GhiChu,HoaDon.TrangThai from HoaDon\n"
            + "join NhanVien on HoaDon.IDNhanVien = NhanVien.IDNhanVien\n"
            + "left outer join TheThanhVien on TheThanhVien.IDTheTV = HoaDon.IDTheTV\n"
            + "where MaHD = ?";
    String sql_Loc = "select MaHD, HoaDon.IDNhanVien, MaNV, HoaDon.IDTheTV, MaTheTV, TongTien,NgayLap,HoaDon.GhiChu,HoaDon.TrangThai from HoaDon\n"
            + "join NhanVien on HoaDon.IDNhanVien = NhanVien.IDNhanVien\n"
            + "left outer join TheThanhVien on TheThanhVien.IDTheTV = HoaDon.IDTheTV\n"
            + "where NgayLap between ? and ?";
    String sql_DatHang = "select DatHang.MaDatHang,DatHang.IDNhanVien,MaNV,DatHang.IDTheTV,MaTheTV,NgayLap,HoTenKhachHang,DatHang.SDT,DiaChi,TongTien,DatHang.IDHinhThucThanhToan,TenHinhThucThanhToan,DatHang.GhiChu,DatHang.TrangThai from DatHang\n"
            + "join NhanVien on DatHang.IDNhanVien = NhanVien.IDNhanVien\n"
            + "left outer join TheThanhVien on DatHang.IDTheTV = TheThanhVien.IDTheTV\n"
            + "join HinhThucThanhToan on DatHang.IDHinhThucThanhToan = HinhThucThanhToan.IDHinhThucThanhToan\n";
    String dathang_selectID = "select DatHang.MaDatHang,DatHang.IDNhanVien,MaNV,DatHang.IDTheTV,MaTheTV,NgayLap,HoTenKhachHang,DatHang.SDT,DiaChi,TongTien,DatHang.IDHinhThucThanhToan,TenHinhThucThanhToan,DatHang.GhiChu,DatHang.TrangThai from DatHang\n"
            + "join NhanVien on DatHang.IDNhanVien = NhanVien.IDNhanVien\n"
            + "left outer join TheThanhVien on DatHang.IDTheTV = TheThanhVien.IDTheTV\n"
            + "join HinhThucThanhToan on DatHang.IDHinhThucThanhToan = HinhThucThanhToan.IDHinhThucThanhToan\n"
            + "where DatHang.MaDatHang = ?";
    String dathang_update = "update DatHang \n"
            + "set TrangThai = ?,GhiChu = ?\n"
            + "where MaDatHang = ?";
    String dathang_loc = "select DatHang.MaDatHang,DatHang.IDNhanVien,MaNV,DatHang.IDTheTV,MaTheTV,NgayLap,HoTenKhachHang,DatHang.SDT,DiaChi,TongTien,DatHang.IDHinhThucThanhToan,TenHinhThucThanhToan,DatHang.GhiChu,DatHang.TrangThai from DatHang\n"
            + "join NhanVien on DatHang.IDNhanVien = NhanVien.IDNhanVien\n"
            + "left outer join TheThanhVien on DatHang.IDTheTV = TheThanhVien.IDTheTV\n"
            + "join HinhThucThanhToan on DatHang.IDHinhThucThanhToan = HinhThucThanhToan.IDHinhThucThanhToan\n"
            + "where NgayLap between ? and ?";

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

    public List<Object[]> duyet_Bang() {
        try {
            String cols[] = {"MaHD", "MaNV", "MaTheTV", "TongTien", "NgayLap", "GhiChu", "TrangThai"};
            return this.getListOfArray(select_sql, cols);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Object[]> datHangData() {
        try {
            String cols[] = {"MaDatHang", "MaNV", "MaTheTV", "NgayLap", "HoTenKhachHang", "SDT", "DiaChi", "TongTien", "TenHinhThucThanhToan", "GhiChu", "TrangThai"};
            return this.getListOfArray(sql_DatHang, cols);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Object[] selectHoaDonByID(String id) {
        try {
            String cols[] = {"MaHD", "MaNV", "MaTheTV", "TongTien", "NgayLap", "GhiChu", "TrangThai"};
            List<Object[]> lst = getListOfArray(select_by_id_sql, cols, id);
            if (lst.isEmpty()) {
                return null;
            }
            return lst.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Object[] selectDatHangbyID(String ID) {
        try {
            String cols[] = {"MaDatHang", "MaNV", "MaTheTV", "NgayLap", "HoTenKhachHang", "SDT", "DiaChi", "TongTien", "TenHinhThucThanhToan", "GhiChu", "TrangThai"};
            List<Object[]> lstdh = getListOfArray(dathang_selectID, cols, ID);
            if (lstdh.isEmpty()) {
                return null;
            }
            return lstdh.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void update_datHang(String ghichu, String trangthai, String id) {
        try {
            JDBCHelper.update(dathang_update, ghichu, trangthai, id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Object[]> locDL(Date ngaybd, Date ngaykt) {
        try {
            String cols[] = {"MaHD", "MaNV", "MaTheTV", "TongTien", "NgayLap", "GhiChu", "TrangThai"};
            return this.getListOfArray(sql_Loc, cols, ngaybd, ngaykt);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Object[]> locDLFH(Date ngaybd, Date ngaykt) {
        try {
            String cols[] = {"MaDatHang", "MaNV", "MaTheTV", "NgayLap", "HoTenKhachHang", "SDT", "DiaChi", "TongTien", "TenHinhThucThanhToan", "GhiChu", "TrangThai"};
            return this.getListOfArray(dathang_loc, cols, ngaybd, ngaykt);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
