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
    private int IDLoaiHang, IDDongSP;
    private Date ThoiGianBatDau, ThoiGianKetThuc;
    private int GiamGia;
    private String MoTa;

    public KhuyenMai() {
    }

    public KhuyenMai(int IDKhuyenMai, String MaKM, String TenKM, int IDLoaiHang, int IDDongSP, Date ThoiGianBatDau, Date ThoiGianKetThuc, int GiamGia, String MoTa) {
        this.IDKhuyenMai = IDKhuyenMai;
        this.MaKM = MaKM;
        this.TenKM = TenKM;
        this.IDLoaiHang = IDLoaiHang;
        this.IDDongSP = IDDongSP;
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

    public int getIDLoaiHang() {
        return IDLoaiHang;
    }

    public void setIDLoaiHang(int IDLoaiHang) {
        this.IDLoaiHang = IDLoaiHang;
    }

    public int getIDDongSP() {
        return IDDongSP;
    }

    public void setIDDongSP(int IDDongSP) {
        this.IDDongSP = IDDongSP;
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
