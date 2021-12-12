/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.Model;

import java.util.Date;

/**
 *
 * @author Admin
 */
public class HoaDon {

    private int IDHoaDon,IDChiTietHoaDon,SoLuong;
    private String MaHD,TenSP,MaNV,MaTheTV;
    private int IDNhanVien;
    private int IDTheTV;
    private int IDHinhThucThanhToan;
    private Date NgayLap;
    private double TongTien,DonGia;
    private String GhiChu;
    private String TrangThai;

    public HoaDon() {
    }

    public HoaDon(int IDHoaDon, int IDChiTietHoaDon, int SoLuong, String MaHD, String TenSP, String MaNV, String MaTheTV, int IDNhanVien, int IDTheTV, int IDHinhThucThanhToan, Date NgayLap, double TongTien, double DonGia, String GhiChu, String TrangThai) {
        this.IDHoaDon = IDHoaDon;
        this.IDChiTietHoaDon = IDChiTietHoaDon;
        this.SoLuong = SoLuong;
        this.MaHD = MaHD;
        this.TenSP = TenSP;
        this.MaNV = MaNV;
        this.MaTheTV = MaTheTV;
        this.IDNhanVien = IDNhanVien;
        this.IDTheTV = IDTheTV;
        this.IDHinhThucThanhToan = IDHinhThucThanhToan;
        this.NgayLap = NgayLap;
        this.TongTien = TongTien;
        this.DonGia = DonGia;
        this.GhiChu = GhiChu;
        this.TrangThai = TrangThai;
    }

    public int getIDHoaDon() {
        return IDHoaDon;
    }

    public void setIDHoaDon(int IDHoaDon) {
        this.IDHoaDon = IDHoaDon;
    }

    public int getIDChiTietHoaDon() {
        return IDChiTietHoaDon;
    }

    public void setIDChiTietHoaDon(int IDChiTietHoaDon) {
        this.IDChiTietHoaDon = IDChiTietHoaDon;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int SoLuong) {
        this.SoLuong = SoLuong;
    }

    public String getMaHD() {
        return MaHD;
    }

    public void setMaHD(String MaHD) {
        this.MaHD = MaHD;
    }

    public String getTenSP() {
        return TenSP;
    }

    public void setTenSP(String TenSP) {
        this.TenSP = TenSP;
    }

    public String getMaNV() {
        return MaNV;
    }

    public void setMaNV(String MaNV) {
        this.MaNV = MaNV;
    }

    public String getMaTheTV() {
        return MaTheTV;
    }

    public void setMaTheTV(String MaTheTV) {
        this.MaTheTV = MaTheTV;
    }

    public int getIDNhanVien() {
        return IDNhanVien;
    }

    public void setIDNhanVien(int IDNhanVien) {
        this.IDNhanVien = IDNhanVien;
    }

    public int getIDTheTV() {
        return IDTheTV;
    }

    public void setIDTheTV(int IDTheTV) {
        this.IDTheTV = IDTheTV;
    }

    public int getIDHinhThucThanhToan() {
        return IDHinhThucThanhToan;
    }

    public void setIDHinhThucThanhToan(int IDHinhThucThanhToan) {
        this.IDHinhThucThanhToan = IDHinhThucThanhToan;
    }

    public Date getNgayLap() {
        return NgayLap;
    }

    public void setNgayLap(Date NgayLap) {
        this.NgayLap = NgayLap;
    }

    public double getTongTien() {
        return TongTien;
    }

    public void setTongTien(double TongTien) {
        this.TongTien = TongTien;
    }

    public double getDonGia() {
        return DonGia;
    }

    public void setDonGia(double DonGia) {
        this.DonGia = DonGia;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String GhiChu) {
        this.GhiChu = GhiChu;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String TrangThai) {
        this.TrangThai = TrangThai;
    }
}
