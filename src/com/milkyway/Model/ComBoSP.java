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
public class ComBoSP {
    
    private int IDComboSP;
    private String MaComboSP, TenComboSP;
    private int IDChiTietSP, SoLuongSP, SoLuongComboSP;
    private double DonGia;
    private Date NgayTao;
    private String GhiChu, AnhComboSP;
    private boolean TrangThai;

    public ComBoSP() {
    }

    public ComBoSP(int IDComboSP, String MaComboSP, String TenComboSP, int IDChiTietSP, int SoLuongSP, int SoLuongComboSP, double DonGia, Date NgayTao, String GhiChu, String AnhComboSP, boolean TrangThai) {
        this.IDComboSP = IDComboSP;
        this.MaComboSP = MaComboSP;
        this.TenComboSP = TenComboSP;
        this.IDChiTietSP = IDChiTietSP;
        this.SoLuongSP = SoLuongSP;
        this.SoLuongComboSP = SoLuongComboSP;
        this.DonGia = DonGia;
        this.NgayTao = NgayTao;
        this.GhiChu = GhiChu;
        this.AnhComboSP = AnhComboSP;
        this.TrangThai = TrangThai;
    }

    public int getIDComboSP() {
        return IDComboSP;
    }

    public void setIDComboSP(int IDComboSP) {
        this.IDComboSP = IDComboSP;
    }

    public String getMaComboSP() {
        return MaComboSP;
    }

    public void setMaComboSP(String MaComboSP) {
        this.MaComboSP = MaComboSP;
    }

    public String getTenComboSP() {
        return TenComboSP;
    }

    public void setTenComboSP(String TenComboSP) {
        this.TenComboSP = TenComboSP;
    }

    public int getIDChiTietSP() {
        return IDChiTietSP;
    }

    public void setIDChiTietSP(int IDChiTietSP) {
        this.IDChiTietSP = IDChiTietSP;
    }

    public int getSoLuongSP() {
        return SoLuongSP;
    }

    public void setSoLuongSP(int SoLuongSP) {
        this.SoLuongSP = SoLuongSP;
    }

    public int getSoLuongComboSP() {
        return SoLuongComboSP;
    }

    public void setSoLuongComboSP(int SoLuongComboSP) {
        this.SoLuongComboSP = SoLuongComboSP;
    }

    public double getDonGia() {
        return DonGia;
    }

    public void setDonGia(double DonGia) {
        this.DonGia = DonGia;
    }

    public Date getNgayTao() {
        return NgayTao;
    }

    public void setNgayTao(Date NgayTao) {
        this.NgayTao = NgayTao;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String GhiChu) {
        this.GhiChu = GhiChu;
    }

    public String getAnhComboSP() {
        return AnhComboSP;
    }

    public void setAnhComboSP(String AnhComboSP) {
        this.AnhComboSP = AnhComboSP;
    }

    public boolean isTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(boolean TrangThai) {
        this.TrangThai = TrangThai;
    }
}
