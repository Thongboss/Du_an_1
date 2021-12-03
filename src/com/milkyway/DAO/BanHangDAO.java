/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Model.ChiTietHoaDon;
import com.milkyway.Model.ChiTietSanPham;
import com.milkyway.Model.HinhThucThanhToan;
import com.milkyway.Model.HoaDon;
import com.milkyway.Model.NhanVien;
import com.milkyway.Model.TheThanhVien;
import com.milkyway.Utils.JDBCHelper;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

/**
 *
 * @author PC
 */
public class BanHangDAO {

    String insert_HD = "INSERT INTO [dbo].[HoaDon]([MaHD],[IDNhanVien],[IDTheTV],[IDHinhThucThanhToan],[TongTien],[GhiChu],[TrangThai])"
            + " VALUES(?,?,?,?,?,?,?)";
    String insert_CTHD = "INSERT INTO [dbo].[ChiTietHoaDon]([IDHoaDon],[IDChiTietSP],[SoLuong],[DonGia]) VALUES(?,?,?,?)";
    String update_TrangThai_HoaDon_By_MaHD = "UPDATE HOADON SET TRANGTHAI = ? WHERE MAHD = ?";
    String update_TrangThai_HoaDon_By_ID = "UPDATE HoaDon SET TrangThai = ? WHERE IDHoaDon = ?";
    String hoadon_tt = "SELECT MAHD, MANV, TongTien, HOADON.TRANGTHAI\n"
            + "FROM HOADON JOIN NHANVIEN ON HOADON.IDNHANVIEN = NHANVIEN.IDNHANVIEN "
            + "WHERE HOADON.TRANGTHAI = ?";
    String update_soLuongTon_CTSP_By_ID = "UPDATE ChiTietSanPham SET SoLuongTon = ? WHERE IDChiTietSP = ?";
    String selectSanPhamDangKD = "SELECT MaSP,TenDongSP,TenSP,GiaTri,SoLuongTon,DONGIA,TenAnhSP,SoLuongTon,IDChiTietSP\n"
            + "FROM SANPHAM JOIN CHITIETSANPHAM ON SANPHAM.IDSANPHAM = CHITIETSANPHAM.IDSANPHAM\n"
            + "JOIN DONVITINH ON CHITIETSANPHAM.IDDONVITINH = DONVITINH.IDDONVITINH\n"
            + "JOIN KHOILUONG ON CHITIETSANPHAM.IDKHOILUONG = KHOILUONG.IDKHOILUONG\n"
            + "JOIN XUATXU ON CHITIETSANPHAM.IDXUATXU = XUATXU.IDXUATXU\n"
            + "JOIN DONGSP ON SANPHAM.IDDONGSP = DONGSP.IDDONGSP\n"
            + "JOIN THUONGHIEU ON DONGSP.IDTHUONGHIEU = THUONGHIEU.IDTHUONGHIEU\n"
            + "JOIN AnhSP ON AnhSP.IDAnhSP = CHITIETSANPHAM.IDAnhSP\n"
            + "WHERE SanPham.TrangThai = 1";
    String selectHoaDonCho = "SELECT MASP, TENSP, SOLUONG, ChiTietHoaDon.DonGia, (SOLUONG * ChiTietHoaDon.DonGia) AS THANHTIEN, IDChiTietHD, ChiTietSanPham.IDChiTietSP\n"
            + "FROM HoaDon JOIN ChiTietHoaDon ON HoaDon.IDHoaDon = ChiTietHoaDon.IDHoaDon\n"
            + "JOIN ChiTietSanPham ON ChiTietHoaDon.IDChiTietSP = ChiTietSanPham.IDChiTietSP\n"
            + "JOIN SanPham ON ChiTietSanPham.IDSanPham = SanPham.IDSanPham\n"
            + "WHERE HoaDon.IDHoaDon = ?";
    String duyet_HD = "SELECT NgayLap, SoLuongTon,SoLuong, ChiTietSanPham.IDChiTietSP , IDChiTietHD, HoaDon.IDHoaDon\n"
            + "FROM HoaDon JOIN ChiTietHoaDon ON HoaDon.IDHoaDon = ChiTietHoaDon.IDHoaDon\n"
            + "JOIN ChiTietSanPham ON ChiTietHoaDon.IDChiTietSP = ChiTietSanPham.IDChiTietSP\n"
            + "WHERE TrangThai = N'Chờ thanh toán'";
    String hoadon_rong = "delete HoaDon where IDHoaDon not in (select IDHoaDon from ChiTietHoaDon)";
    String delete_cthd = "DELETE ChiTietHoaDon WHERE IDChiTietHD = ?";
    String select_NV = "SELECT * FROM NHANVIEN";
    String select_TTV = "SELECT * FROM THETHANHVIEN";
    String select_HTTT = "SELECT * FROM HINHTHUCTHANHTOAN";
    String select_HD = "SELECT * FROM HOADON";
    String select_CTHD = "SELECT * FROM ChiTietHOADON";
    String select_CTSP = "SELECT * FROM CHITIETSANPHAM";
    String select_SP = "SELECT * FROM SANPHAM";
    String select_DSP = "SELECT * FROM DONGSP";
    String select_TH = "SELECT * FROM THUONGHIEU";
    String select_DVT = "SELECT * FROM DONVITINH";
    String select_LH = "SELECT * FROM LOAIHANG";
    String select_NH = "SELECT * FROM NHOMHANG";
    String select_ANH = "SELECT * FROM ANHSP";
    String select_KL = "SELECT * FROM KHOILUONG";
    String select_XX = "SELECT * FROM XUATXU";

    public void insert_HD(HoaDon entity) {
        try {
            JDBCHelper.update(insert_HD, entity.getMaHD(), entity.getIDNhanVien(), entity.getIDTheTV() == 0 ? null : entity.getIDTheTV(), entity.getIDHinhThucThanhToan(), entity.getTongTien(), entity.getGhiChu(), entity.getTrangThai());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void insert_CTHD(ChiTietHoaDon entity) {
        try {
            JDBCHelper.update(insert_CTHD, entity.getIDHoaDon(), entity.getIDChiTietSP(), entity.getSoLuong(), entity.getDonGia());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void update_TrangThai_HoaDon_By_MaHD(HoaDon entity) {
        try {
            JDBCHelper.update(update_TrangThai_HoaDon_By_MaHD, entity.getTrangThai(), entity.getMaHD());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void delete_cthd(int id) {
        try {
            JDBCHelper.update(delete_cthd, id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void delete_hd_rong() {
        try {
            JDBCHelper.update(hoadon_rong);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void update_TrangThai_HoaDon_By_ID(String tt, int id) {
        try {
            JDBCHelper.update(update_TrangThai_HoaDon_By_ID, tt, id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void update_soLuongTon_CTSP_By_ID(int slt, int id) {
        try {
            JDBCHelper.update(update_soLuongTon_CTSP_By_ID, slt, id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void update_CTSP(ChiTietSanPham entity) {
        try {
            JDBCHelper.update(update_soLuongTon_CTSP_By_ID, entity.getSoLuongTon(), entity.getIDChiTietSP());
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
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Object[]> duyet_HD() {
        try {
            String cols[] = {"NgayLap", "SoLuongTon", "SoLuong", "IDChiTietSP", "IDChiTietHD", "IDHoaDon"};
            return this.getListOfArray(duyet_HD, cols);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Object[]> xuLyHoaDon(int id) {
        try {
            String cols[] = {"MASP", "TENSP", "SOLUONG", "DonGia", "THANHTIEN", "IDChiTietHD", "IDChiTietSP"};
            return this.getListOfArray(selectHoaDonCho, cols, id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Object[]> loadHoaDon(String tt) {
        try {
            String cols[] = {"MAHD", "MANV", "TongTien", "TRANGTHAI"};
            return this.getListOfArray(hoadon_tt, cols, tt);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Object[]> loadSanPham() {
        try {
            String cols[] = {"MASP", "TenDongSP", "TenSP", "GiaTri", "SoLuongTon", "DONGIA", "TenAnhSP", "SOLUONGTON", "IDChiTietSP"};
            return this.getListOfArray(selectSanPhamDangKD, cols);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private List<NhanVien> selectBySQLNV(String sql, Object... args) {
        try {
            List<NhanVien> list = new ArrayList<>();
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                boolean gt, tt;
                NhanVien nv = new NhanVien();
                nv.setCMND(rs.getString("CMND"));
                nv.setEmail(rs.getString("Email"));
                nv.setGhiChu(rs.getString("GhiChu"));
                if (rs.getInt("GIOITINH") == 0) {
                    gt = true;
                } else {
                    gt = false;
                }
                nv.setGioiTinh(gt);
                nv.setHinhAnh(rs.getString("HINHANH"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setIDNhanVien(rs.getInt("IDNhanVien"));
                nv.setMaNV(rs.getString("MANV"));
                nv.setMatKhau(rs.getBytes("MatKhau"));
                nv.setNgaySinh(rs.getDate("NGAYSINH"));
                nv.setSDT(rs.getString("SDT"));
                nv.setSalt(rs.getBytes("SALT"));
                nv.setTaiKhoan(rs.getString("TAIKHOAN"));
                nv.setTrangThai(rs.getBoolean("TRANGTHAI"));
                nv.setVaiTro(rs.getBoolean("VAITRO"));
                list.add(nv);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<NhanVien> loadListNV() {
        return this.selectBySQLNV(select_NV);
    }

    private List<TheThanhVien> selectBySQLTTV(String sql, Object... args) {
        try {
            List<TheThanhVien> list = new ArrayList<>();
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                TheThanhVien ttv = new TheThanhVien();
                ttv.setCMND(rs.getString("CMND"));
                ttv.setEmail(rs.getString("Email"));
                ttv.setGhiChu(rs.getString("GhiChu"));
                ttv.setGioiTinh(rs.getBoolean("GioiTinh"));
                ttv.setHinhAnh(rs.getString("HinhAnh"));
                ttv.setIDTheTV(rs.getInt("IDTHETV"));
                ttv.setMaTheTV(rs.getString("MATHETV"));
                ttv.setNgayHetHan(rs.getDate("NGAYHETHAN"));
                ttv.setNgaySinh(rs.getDate("NGAYSINH"));
                ttv.setNgayTao(rs.getDate("NGAYTAO"));
                ttv.setSDT(rs.getString("SDT"));
                ttv.setTenKH(rs.getString("TenKH"));
                list.add(ttv);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<TheThanhVien> loadListTTV() {
        return this.selectBySQLTTV(select_TTV);
    }

    private List<HinhThucThanhToan> selectBySQLHTTT(String sql, Object... args) {
        try {
            List<HinhThucThanhToan> list = new ArrayList<>();
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                HinhThucThanhToan httt = new HinhThucThanhToan();
                httt.setGhiChu(rs.getString("GHICHU"));
                httt.setIDHinhThucThanhToan(rs.getInt("IDhinhthucthanhtoan"));
                httt.setMaHinhThucThanhToan(rs.getString("MAHinhThucThanhToan"));
                httt.setTenHinhThucThanhToan(rs.getString("TenHinhThucThanhToan"));
                list.add(httt);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<HinhThucThanhToan> loadListHTTT() {
        return this.selectBySQLHTTT(select_HTTT);
    }

    private List<HoaDon> selectBySQLHD(String sql, Object... args) {
        try {
            List<HoaDon> list = new ArrayList<>();
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setGhiChu(rs.getString("GhiChu"));
                hd.setIDHinhThucThanhToan(rs.getInt("IDHinhThucThanhToan"));
                hd.setIDHoaDon(rs.getInt("IDHoaDon"));
                hd.setIDNhanVien(rs.getInt("IDNhanVien"));
                hd.setIDTheTV(rs.getInt("IDTheTV"));
                hd.setMaHD(rs.getString("MaHD"));
                hd.setNgayLap(rs.getDate("NgayLap"));
                hd.setTongTien(rs.getDouble("TongTien"));
                hd.setTrangThai(rs.getString("TrangThai"));
                list.add(hd);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<HoaDon> loadListHD() {
        return this.selectBySQLHD(select_HD);
    }

    private List<ChiTietHoaDon> selectBySQLCTHD(String sql, Object... args) {
        try {
            List<ChiTietHoaDon> list = new ArrayList<>();
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                ChiTietHoaDon cthd = new ChiTietHoaDon();
                cthd.setDonGia(rs.getDouble("DonGia"));
                cthd.setIDChiTietHD(rs.getInt("IDChiTietHD"));
                cthd.setIDChiTietSP(rs.getInt("IDChiTietSP"));
                cthd.setIDHoaDon(rs.getInt("IDHoaDon"));
                cthd.setSoLuong(rs.getInt("SoLuong"));
                list.add(cthd);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<ChiTietHoaDon> loadListCTHD() {
        return this.selectBySQLCTHD(select_CTHD);
    }

    private List<ChiTietSanPham> selectBySQLCTSP(String sql, Object... args) {
        try {
            List<ChiTietSanPham> list = new ArrayList<>();
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                ChiTietSanPham ctsp = new ChiTietSanPham();
                ctsp.setBarCode(rs.getString("BarCode"));
                ctsp.setDonGia(rs.getDouble("DonGia"));
                ctsp.setHanSD(rs.getDate("HanSD"));
                ctsp.setIDAnhSP(rs.getInt("IDANHSP"));
                ctsp.setIDChiTietSP(rs.getInt("IdChiTietSP"));
                ctsp.setIDDonViTinh(rs.getInt("IDDonViTinh"));
                ctsp.setIDKhoiLuong(rs.getInt("IDKhoiLuong"));
                ctsp.setIDSanPham(rs.getInt("IDSanPham"));
                ctsp.setIDXuatXu(rs.getInt("IDXuatXu"));
                ctsp.setSoLuongTon(rs.getInt("SoLuongTon"));
                list.add(ctsp);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<ChiTietSanPham> loadListCTSP() {
        return this.selectBySQLCTSP(select_CTSP);
    }
}
