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

    public List<Object[]> locDL(Date ngaybd, Date ngaykt) {
        try {
            String cols[] = {"MaHD", "MaNV", "MaTheTV", "TongTien", "NgayLap", "GhiChu", "TrangThai"};
            return this.getListOfArray(sql_Loc, cols, ngaybd, ngaykt);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
