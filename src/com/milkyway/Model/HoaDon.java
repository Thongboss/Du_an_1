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

    private int MaHD;
    private Date NgayLap;
    private int MaNV;
    private int MaTheTV;
    private double TongTien;
    private String GhiChu;
    private boolean TrangThai;

    public HoaDon() {
    }

    public HoaDon(int MaHD, Date NgayLap, int MaNV, int MaTheTV, double TongTien, String GhiChu, boolean TrangThai) {
        this.MaHD = MaHD;
        this.NgayLap = NgayLap;
        this.MaNV = MaNV;
        this.MaTheTV = MaTheTV;
        this.TongTien = TongTien;
        this.GhiChu = GhiChu;
        this.TrangThai = TrangThai;
    }

    public int getMaHD() {
        return MaHD;
    }

    public void setMaHD(int MaHD) {
        this.MaHD = MaHD;
    }

    public Date getNgayLap() {
        return NgayLap;
    }

    public void setNgayLap(Date NgayLap) {
        this.NgayLap = NgayLap;
    }

    public int getMaNV() {
        return MaNV;
    }

    public void setMaNV(int MaNV) {
        this.MaNV = MaNV;
    }

    public int getMaTheTV() {
        return MaTheTV;
    }

    public void setMaTheTV(int MaTheTV) {
        this.MaTheTV = MaTheTV;
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

    public boolean getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(boolean TrangThai) {
        this.TrangThai = TrangThai;
    }
}
