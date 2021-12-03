/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.Model;

import java.util.Date;

/**
 *
 * @author DaiAustinYersin
 */
public class KhuyenMai {

    private int IDKhuyenMai;
    private String MaKM, TenKM;
    private int IDSanPham, IDDongSP, IDHinhThucThanhToan;
    private Date ThoiGianBatDau, ThoiGianKetThuc;
    private int GiamGia;
    private String MoTa;

    public KhuyenMai() {
    }

    public KhuyenMai(int IDKhuyenMai, String MaKM, String TenKM, int IDSanPham, int IDDongSP, int IDHinhThucThanhToan, Date ThoiGianBatDau, Date ThoiGianKetThuc, int GiamGia, String MoTa) {
        this.IDKhuyenMai = IDKhuyenMai;
        this.MaKM = MaKM;
        this.TenKM = TenKM;
        this.IDSanPham = IDSanPham;
        this.IDDongSP = IDDongSP;
        this.IDHinhThucThanhToan = IDHinhThucThanhToan;
        this.ThoiGianBatDau = ThoiGianBatDau;
        this.ThoiGianKetThuc = ThoiGianKetThuc;
        this.GiamGia = GiamGia;
        this.MoTa = MoTa;
    }

    public int getIDKhuyenMai() {
        return IDKhuyenMai;
    }

    public void setIDKhuyenMai(int IDKhuyenMai) {
        this.IDKhuyenMai = IDKhuyenMai;
    }

    public String getMaKM() {
        return MaKM;
    }

    public void setMaKM(String MaKM) {
        this.MaKM = MaKM;
    }

    public String getTenKM() {
        return TenKM;
    }

    public void setTenKM(String TenKM) {
        this.TenKM = TenKM;
    }

    public int getIDSanPham() {
        return IDSanPham;
    }

    public void setIDSanPham(int IDSanPham) {
        this.IDSanPham = IDSanPham;
    }

    public int getIDDongSP() {
        return IDDongSP;
    }

    public void setIDDongSP(int IDDongSP) {
        this.IDDongSP = IDDongSP;
    }

    public int getIDHinhThucThanhToan() {
        return IDHinhThucThanhToan;
    }

    public void setIDHinhThucThanhToan(int IDHinhThucThanhToan) {
        this.IDHinhThucThanhToan = IDHinhThucThanhToan;
    }

    public Date getThoiGianBatDau() {
        return ThoiGianBatDau;
    }

    public void setThoiGianBatDau(Date ThoiGianBatDau) {
        this.ThoiGianBatDau = ThoiGianBatDau;
    }

    public Date getThoiGianKetThuc() {
        return ThoiGianKetThuc;
    }

    public void setThoiGianKetThuc(Date ThoiGianKetThuc) {
        this.ThoiGianKetThuc = ThoiGianKetThuc;
    }

    public int getGiamGia() {
        return GiamGia;
    }

    public void setGiamGia(int GiamGia) {
        this.GiamGia = GiamGia;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String MoTa) {
        this.MoTa = MoTa;
    }

    
}
