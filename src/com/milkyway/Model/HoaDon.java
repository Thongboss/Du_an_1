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

    private int IDHoaDon;
    private String MaHD;
    private int IDNhanVien;
    private int IDTheTV;
    private int IDHinhThucThanhToan;
    private Date NgayLap;
    private double TongTien;
    private String GhiChu;
    private String TrangThai;

    public HoaDon() {
    }

    public HoaDon(int IDHoaDon, String MaHD, int IDNhanVien, int IDTheTV, int IDHinhThucThanhToan, Date NgayLap, double TongTien, String GhiChu, String TrangThai) {
        this.IDHoaDon = IDHoaDon;
        this.MaHD = MaHD;
        this.IDNhanVien = IDNhanVien;
        this.IDTheTV = IDTheTV;
        this.IDHinhThucThanhToan = IDHinhThucThanhToan;
        this.NgayLap = NgayLap;
        this.TongTien = TongTien;
        this.GhiChu = GhiChu;
        this.TrangThai = TrangThai;
    }

    public int getIDHoaDon() {
        return IDHoaDon;
    }

    public void setIDHoaDon(int IDHoaDon) {
        this.IDHoaDon = IDHoaDon;
    }

    public String getMaHD() {
        return MaHD;
    }

    public void setMaHD(String MaHD) {
        this.MaHD = MaHD;
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

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String GhiChu) {
        this.GhiChu = GhiChu;
    }

    public String isTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String TrangThai) {
        this.TrangThai = TrangThai;
    }

}
