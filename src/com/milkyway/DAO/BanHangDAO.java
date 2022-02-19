/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.DAO;

import com.milkyway.Model.ChiTietComboSP;
import com.milkyway.Model.ChiTietDatHang;
import com.milkyway.Model.ChiTietHoaDon;
import com.milkyway.Model.ChiTietSanPham;
import com.milkyway.Model.ComBoSP;
import com.milkyway.Model.DatHang;
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
    String insert_DH = "INSERT INTO [dbo].[DatHang]([MaDatHang],[IDNhanVien],[IDTheTV],[NgayLap],[HoTenKhachHang],[SDT],[DiaChi],[TongTien],[IDHinhThucThanhToan],[GhiChu],[TrangThai])VALUES(?,?,?,?,?,?,?,?,?,?,?)";
    String insert_CTHD = "INSERT INTO [dbo].[ChiTietHoaDon]([IDHoaDon],[IDChiTietSP],[SoLuong],[DonGia]) VALUES(?,?,?,?)";
    String update_TrangThai_HoaDon_By_MaHD = "UPDATE HOADON SET TRANGTHAI = ? WHERE MAHD = ?";
    String update_TrangThai_HoaDon_By_ID = "UPDATE HoaDon SET TrangThai = ? WHERE IDHoaDon = ?";
    String hoadon_tt = "SELECT MAHD, MANV, TongTien, HOADON.TRANGTHAI\n"
            + "FROM HOADON JOIN NHANVIEN ON HOADON.IDNHANVIEN = NHANVIEN.IDNHANVIEN "
            + "WHERE HOADON.TRANGTHAI = ?";
//    String insert_CTDH = "INSERT INTO [dbo].[ChiTietDatHang]([IDDatHang],[IDChiTietSP],[SoLuong],[DonGia])VALUES(?,?,?,?)";
    String insert_CTDH = "INSERT INTO [dbo].[ChiTietDatHang]([IDDatHang],[IDChiTietSP],[SoLuong],[DonGia])VALUES(?,?,?,?)";
    String update_soLuongTon_CTSP_By_ID = "UPDATE ChiTietSanPham SET SoLuongTon = ? WHERE IDChiTietSP = ?";
    String selectSanPhamDangKD = "SELECT MaSP,TenDongSP,TenSP,GiaTri,SoLuongTon,DONGIA,TenAnhSP,SoLuongTon,IDChiTietSP\n"
            + "FROM SANPHAM JOIN CHITIETSANPHAM ON SANPHAM.IDSANPHAM = CHITIETSANPHAM.IDSANPHAM\n"
            + "JOIN DONVITINH ON CHITIETSANPHAM.IDDONVITINH = DONVITINH.IDDONVITINH\n"
            + "JOIN KHOILUONG ON CHITIETSANPHAM.IDKHOILUONG = KHOILUONG.IDKHOILUONG\n"
            + "JOIN XUATXU ON CHITIETSANPHAM.IDXUATXU = XUATXU.IDXUATXU\n"
            + "JOIN DONGSP ON SANPHAM.IDDONGSP = DONGSP.IDDONGSP\n"
            + "JOIN THUONGHIEU ON DONGSP.IDTHUONGHIEU = THUONGHIEU.IDTHUONGHIEU\n"
            + "JOIN AnhSP ON AnhSP.IDAnhSP = CHITIETSANPHAM.IDAnhSP\n"
            + "WHERE SanPham.TrangThai = 1 AND ChiTietSanPham.SoLuongTon > 0";
    String selectSanPhamDangKDByIDAndBarCode = "SELECT MaSP,TenDongSP,TenSP,GiaTri,SoLuongTon,DONGIA,TenAnhSP,SoLuongTon,IDChiTietSP\n"
            + "FROM SANPHAM JOIN CHITIETSANPHAM ON SANPHAM.IDSANPHAM = CHITIETSANPHAM.IDSANPHAM\n"
            + "JOIN DONVITINH ON CHITIETSANPHAM.IDDONVITINH = DONVITINH.IDDONVITINH\n"
            + "JOIN KHOILUONG ON CHITIETSANPHAM.IDKHOILUONG = KHOILUONG.IDKHOILUONG\n"
            + "JOIN XUATXU ON CHITIETSANPHAM.IDXUATXU = XUATXU.IDXUATXU\n"
            + "JOIN DONGSP ON SANPHAM.IDDONGSP = DONGSP.IDDONGSP\n"
            + "JOIN THUONGHIEU ON DONGSP.IDTHUONGHIEU = THUONGHIEU.IDTHUONGHIEU\n"
            + "JOIN AnhSP ON AnhSP.IDAnhSP = CHITIETSANPHAM.IDAnhSP\n"
            + "WHERE SanPham.TrangThai = 1 and Barcode = ?";
    String selectHoaDonCho = "SELECT MASP, TENSP, SOLUONG, ChiTietHoaDon.DonGia, (SOLUONG * ChiTietHoaDon.DonGia) AS THANHTIEN, IDChiTietHD, ChiTietSanPham.IDChiTietSP\n"
            + "FROM HoaDon JOIN ChiTietHoaDon ON HoaDon.IDHoaDon = ChiTietHoaDon.IDHoaDon\n"
            + "JOIN ChiTietSanPham ON ChiTietHoaDon.IDChiTietSP = ChiTietSanPham.IDChiTietSP\n"
            + "JOIN SanPham ON ChiTietSanPham.IDSanPham = SanPham.IDSanPham\n"
            + "WHERE HoaDon.IDHoaDon = ?";
    String select_HD_DG = "SELECT MASP, TENSP, SOLUONG, ChiTietDatHang.DonGia, (SOLUONG * ChiTietDatHang.DonGia) AS THANHTIEN, IDChiTietDatHang, ChiTietSanPham.IDChiTietSP\n"
            + "FROM DatHang JOIN ChiTietDatHang ON ChiTietDatHang.IDDatHang = DatHang.IDDatHang\n"
            + "JOIN ChiTietSanPham ON ChiTietSanPham.IDChiTietSP = ChiTietDatHang.IDChiTietSP\n"
            + "JOIN SanPham ON ChiTietSanPham.IDSanPham = SanPham.IDSanPham\n"
            + "WHERE MaDatHang = ?";
    String duyet_HD = "SELECT NgayLap, SoLuongTon,SoLuong, ChiTietSanPham.IDChiTietSP , IDChiTietHD, HoaDon.IDHoaDon\n"
            + "FROM HoaDon JOIN ChiTietHoaDon ON HoaDon.IDHoaDon = ChiTietHoaDon.IDHoaDon\n"
            + "JOIN ChiTietSanPham ON ChiTietHoaDon.IDChiTietSP = ChiTietSanPham.IDChiTietSP\n"
            + "WHERE TrangThai = N'Chờ thanh toán'";
    String load_combo = "SELECT MaComboSP, TenComboSP, NgayTao, NgayHetHan, SoLuong, DonGia, AnhComBoSP, SoLuong, IDChiTietSP\n"
            + "FROM ComBoSP JOIN ChiTietComboSP ON ComBoSP.IDComboSP = ChiTietComboSP.IDComboSP";
    String select_hddg = "SELECT MaHD, MaNV, TongTien, HoaDon.TrangThai \n"
            + "FROM HOADON JOIN NhanVien ON HoaDon.IDNhanVien = NhanVien.IDNhanVien\n"
            + "WHERE HoaDon.TrangThai = N'Đang giao'";
    String update_Ghichuhd = "UPDATE HoaDon\n"
            + "SET TrangThai = ?\n"
            + "WHERE IDHoaDon = ?";
    String update_ttDH = "UPDATE DatHang\n"
            + "SET TrangThai = ?\n"
            + "WHERE MaDatHang = ?";
    String update_Ghichu_DH = "UPDATE DatHang\n"
            + "SET GhiChu = ?\n"
            + "WHERE MaDatHang = ?";
    String select_DH = "SELECT*FROM DatHang";
    String select_CTDH = "SELECT*FROM ChiTietDatHang";
    String select_CTCB = "SELECT*FROM ChiTietComboSP";
    String hoadon_rong = "delete HoaDon where IDHoaDon not in (select IDHoaDon from ChiTietHoaDon)";
    String select_combo = "SELECT*FROM ComBoSP";
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

    public void insert_DH(DatHang entity) {
        try {
            JDBCHelper.update(insert_DH, entity.getMaDatHang(), entity.getIDNhanVien(), entity.getIDTheTV() == 0 ? null : entity.getIDTheTV(), entity.getNgayLap(), entity.getHoTenKhachHang(), entity.getSDT(), entity.getDiaChi(), entity.getTongTien(), entity.getIDHinhThucThanhToan(), entity.getGhiChu(), entity.getTrangThai());
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

    public void insret_CTDH(ChiTietDatHang entity) {
        try {
            JDBCHelper.update(insert_CTDH, entity.getIDDatHang(), entity.getIDChiTietSP(), entity.getSoLuong(), entity.getDonGia());
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

    public void update_TTDH(String tt, String ma) {
        try {
            JDBCHelper.update(update_ttDH, tt, ma);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void update_Ghichu_HD_By_ID(String ghichu, int id) {
        try {
            JDBCHelper.update(update_Ghichuhd, ghichu, id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void update_Ghichu_DH(String ghichu, String ma) {
        try {
            JDBCHelper.update(update_Ghichu_DH, ghichu, ma);
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

    public List<Object[]> load_HD_DG() {
        try {
            String cols[] = {"MaHD", "MaNV", "TongTien", "TrangThai"};
            return this.getListOfArray(select_hddg, cols);
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

    public List<Object[]> xulyDH(String ma) {
        try {
            String cols[] = {"MASP", "TENSP", "SOLUONG", "DonGia", "THANHTIEN", "IDChiTietDatHang", "IDChiTietSP"};
            return this.getListOfArray(select_HD_DG, cols, ma);
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

    public List<Object[]> loadComBo() {
        try {
            String cols[] = {"MaComboSP", "TenComboSP", "NgayTao", "NgayHetHan", "SoLuong", "DonGia", "AnhComBoSP", "SoLuong", "IDChiTietSP"};
            return this.getListOfArray(load_combo, cols);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Object[] loadSanPhamQuetTuBarCode(String barcode) {
        try {
            String[] cols = {"MASP", "TenDongSP", "TenSP", "GiaTri", "SoLuongTon", "DONGIA", "TenAnhSP", "SOLUONGTON", "IDChiTietSP"};
            List<Object[]> lst = this.getListOfArray(selectSanPhamDangKDByIDAndBarCode, cols, barcode);
            if (lst.isEmpty()) {
                return null;
            }
            return lst.get(0);
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

    private List<ComBoSP> selectBySQLDH(String sql, Object... args) {
        try {
            List<ComBoSP> list = new ArrayList<>();
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                ComBoSP dh = new ComBoSP();
                dh.setAnhComboSP(rs.getString("AnhComBoSP"));
                dh.setBarcode(rs.getString("Barcode"));
                dh.setDonGia(rs.getDouble("Dongia"));
                dh.setGhiChu("Ghichu");
                dh.setIDComboSP(rs.getInt("IDComboSP"));
                dh.setMaComboSP("MaComboSP");
                dh.setNgayHetHan(rs.getDate("NgayHetHan"));
                dh.setNgayTao(rs.getDate("NgayTao"));
                dh.setSoLuong(rs.getInt("SoLuong"));
                dh.setTenComboSP("TenComboSP");
                list.add(dh);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<ComBoSP> loadListComBoSP() {
        return this.selectBySQLDH(select_combo);
    }

    private List<ChiTietComboSP> selectBySQLCTHD(String sql, Object... args) {
        try {
            List<ChiTietComboSP> list = new ArrayList<>();
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                ChiTietComboSP ctcb = new ChiTietComboSP();
                ctcb.setIDChiTietComboSP(rs.getInt("IDChiTietComboSP"));
                ctcb.setIDChiTietSP(rs.getInt("IDChiTietSP"));
                ctcb.setIDComboSP(rs.getInt("IDComboSP"));
                ctcb.setSoLuongSP(rs.getInt("SoLuongSP"));
                list.add(ctcb);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<ChiTietComboSP> loadListCTCB() {
        return this.selectBySQLCTHD(select_CTCB);
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

    private List<DatHang> selectByDH(String sql, Object... args) {
        try {
            List<DatHang> list = new ArrayList<>();
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                DatHang dh = new DatHang();
                dh.setDiaChi(rs.getString("DiaChi"));
                dh.setGhiChu(rs.getString("GhiChu"));
                dh.setHoTenKhachHang(rs.getString("HoTenKhachHang"));
                dh.setIDDatHang(rs.getInt("IDDatHang"));
                dh.setIDHinhThucThanhToan(rs.getInt("IDHinhThucThanhToan"));
                dh.setIDNhanVien(rs.getInt("IDNhanVien"));
                dh.setIDTheTV(rs.getInt("IDTheTV"));
                dh.setMaDatHang(rs.getString("MaDatHang"));
                dh.setNgayLap(rs.getDate("NgayLap"));
                dh.setSDT(rs.getString("SDT"));
                dh.setTongTien(rs.getDouble("TongTien"));
                dh.setTrangThai(rs.getString("TrangThai"));
                list.add(dh);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<DatHang> loadListDH() {
        return this.selectByDH(select_DH);
    }

    private List<ChiTietDatHang> select_CTDH(String sql, Object... args) {
        try {
            List<ChiTietDatHang> list = new ArrayList<>();
            ResultSet rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                ChiTietDatHang ct = new ChiTietDatHang();
                ct.setDonGia(rs.getDouble("DonGia"));
                ct.setIDChiTietDatHang(rs.getInt("IDChiTietDatHang"));
                ct.setIDChiTietSP(rs.getInt("IDChiTietSP"));
                ct.setSoLuong(rs.getInt("SoLuong"));
                ct.setIDDatHang(rs.getInt("IDDatHang"));
                list.add(ct);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<ChiTietDatHang> loadCTDH() {
        return this.select_CTDH(select_CTDH);
    }

    private List<ChiTietHoaDon> selectByCTHD(String sql, Object... args) {
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
        return this.selectByCTHD(select_CTHD);
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
