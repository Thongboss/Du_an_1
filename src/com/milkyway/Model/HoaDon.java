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

    private String MaHD;
    private int IDNhanVien;
    private int IDTheTV;
    private Date NgayLap;
    private double TongTien;
    private String GhiChu;
    private boolean TrangThai;

    public HoaDon() {
    }

    public HoaDon(String MaHD, int IDNhanVien, int IDTheTV, Date NgayLap, double TongTien, String GhiChu, boolean TrangThai) {
        this.MaHD = MaHD;
        this.IDNhanVien = IDNhanVien;
        this.IDTheTV = IDTheTV;
        this.NgayLap = NgayLap;
        this.TongTien = TongTien;
        this.GhiChu = GhiChu;
        this.TrangThai = TrangThai;
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

    public boolean isTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(boolean TrangThai) {
        this.TrangThai = TrangThai;
    }
}
