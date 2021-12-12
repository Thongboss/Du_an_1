/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Model.TheThanhVien;
import com.milkyway.Utils.JDBCHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class ThongKeDAO {

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

    public Object[] thongKeSoLuongSanPham() {
        String[] cols = {"SoLuongDangDK", "SoLuongHetHang", "SoLuongSapHetHan", "SoLuongSapHetHang", "SoLuongNgungKD"};
        List<Object[]> lst = getListOfArray("{call SP_ThongKeSoLuongSP()}", cols);
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }

    public List<Object[]> thongKeSanPhamMuaNhieuNhat(String tenLoai, String tenDongSP) {
        String[] cols = {"MaSP", "TenSP", "TenLoai", "TenDongSP", "NgayXK", "HanSD", "SoLuongTon", "DonGia", "SoLuongMua", "DoanhThu"};
        return getListOfArray("{call SP_ThongKeSanPhamMuaNhieuNhat(?,?)}", cols, tenLoai, tenDongSP);
    }

    public List<Object[]> thongKeSanPhamMuaItNhat(String tenLoai, String tenDongSP) {
        String[] cols = {"MaSP", "TenSP", "TenLoai", "TenDongSP", "NgayXK", "HanSD", "SoLuongTon", "DonGia", "SoLuongMua", "DoanhThu"};
        return getListOfArray("{call SP_ThongKeSanPhamMuaItNhat(?,?)}", cols, tenLoai, tenDongSP);
    }

    public List<Object[]> thongKeSanPhamDoanhThuNhieuNhat(String tenLoai, String tenDongSP) {
        String[] cols = {"MaSP", "TenSP", "TenLoai", "TenDongSP", "NgayXK", "HanSD", "SoLuongTon", "DonGia", "SoLuongMua", "DoanhThu"};
        return getListOfArray("{call SP_ThongKeSanPhamDoanhThuNhieuNhat(?,?)}", cols, tenLoai, tenDongSP);
    }

    public List<Object[]> thongKeSanPhamDoanhThuItNhat(String tenLoai, String tenDongSP) {
        String[] cols = {"MaSP", "TenSP", "TenLoai", "TenDongSP", "NgayXK", "HanSD", "SoLuongTon", "DonGia", "SoLuongMua", "DoanhThu"};
        return getListOfArray("{call SP_ThongKeSanPhamDoanhThuItNhat(?,?)}", cols, tenLoai, tenDongSP);
    }

    public Object[] thongKeDoanhThu() {
        String[] cols = {"DoanhThuCaNam", "DoanhThuHomNay", "DoanhThu7NgayGanNhat", "DoanhThuThangNay"};
        List<Object[]> lst = getListOfArray("{call SP_ThongKeDoanhThu()}", cols);
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }
    
    public Object[] thongKeDoanhThuTheoNam() {
        String[] cols = {"Nam", "TongDoanhThu", "ThangCoDoanhThuCaoNhat", "ThangCoDoanhThuThapNhat", "doanhThuTrungBinhThang"};
        List<Object[]> lst = getListOfArray("{call SP_ThongKeDoanhThuTheoNam()}", cols);
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }
    
    public List<Object[]> thongKeDoanhThuTheoKhoangThoiGian(Date ngayBD, Date ngayKT) {
        String[] cols = {"NgayLap", "DoanhThu", "SoHoaDonDaThanhToan"};
        return getListOfArray("{call SP_ThongKeDoanhThuTheoKhoangThoiGian(?,?)}", cols, ngayKT, ngayBD);
    }

    protected List<TheThanhVien> selectBySql(String sql, Object... args) {
        List<TheThanhVien> list = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                TheThanhVien Entity = new TheThanhVien();
                Entity.setIDTheTV(rs.getInt("IDTheTV"));
                Entity.setMaTheTV(rs.getString("MaTheTV"));
                Entity.setTenKH(rs.getString("TenKH"));
                Entity.setGioiTinh(rs.getBoolean("GioiTinh"));
                Entity.setNgaySinh(rs.getDate("NgaySinh"));
                Entity.setSDT(rs.getString("SDT"));
                Entity.setCMND(rs.getString("CMND"));
                Entity.setEmail(rs.getString("Email"));
                Entity.setHinhAnh(rs.getString("HinhAnh"));
                Entity.setNgayTao(rs.getDate("NgayTao"));
                Entity.setNgayHetHan(rs.getDate("NgayHetHan"));
                Entity.setGhiChu(rs.getString("GhiChu"));
                Entity.setDiem(rs.getInt("Diem"));
                list.add(Entity);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<TheThanhVien> selectSapHetHan() {
        return selectBySql("select * from  thethanhvien where  DATEDIFF(day,GETDATE(), NgayHetHan) <=7 and  DATEDIFF(day,GETDATE(), NgayHetHan ) >0;");
    }

    public List<TheThanhVien> selectTheThanhVienTaoTrongThang() {
        return selectBySql("select * from  thethanhvien where  DATEDIFF(MONTH,NgayTao,GETDATE() ) <=1;");
    }

    public List<Object[]> ThongKeThanhVien() {
        List<Object[]> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                String sql = "{call sp_ThongKeThanhVien}";
                rs = JDBCHelper.query(sql);
                while (rs.next()) {
                    Object[] model = {
                        rs.getString("MatheTV"),
                        rs.getString("TenKH"),
                        rs.getBoolean("GioiTinh") ? "Nam" : "Nữ",
                        rs.getDate("NgaySinh"),
                        rs.getString("SDT"),
                        rs.getString("CMND"),
                        rs.getString("Email"),
                        rs.getDate("NgayTao"),
                        rs.getDate("NgayHetHan"),
                        rs.getInt("Diem")
                    };
                    list.add(model);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }

        } catch (Exception e) {
            throw new RuntimeException();
        }
        return list;
    }
    
    public List<Object[]> getSoLuongTheThanhVien() {
        List<Object[]> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                String sql = "{call sp_SoLuongTheTV}";
                rs = JDBCHelper.query(sql);
                while (rs.next()) {
                    Object[] model = {
                        rs.getString("Nam"),
                        rs.getString("SoLuong"),
                        rs.getString("Dautien"),
                        rs.getString("CuoiCung")
                    };
                    list.add(model);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }

        } catch (Exception e) {
            throw new RuntimeException();
        }
        return list;
    }
}
