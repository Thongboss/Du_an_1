/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.Model;

/**
 *
 * @author hoang
 */
public class DongSP {
private int IDDongSP,IDThuongHieu;
    private String MaDongSP;
    
    private String TenDongSP, GhiChu;
    private boolean TrangThai;

    public DongSP() {
    }

    public DongSP(int IDDongSP, int IDThuongHieu, String MaDongSP, String TenDongSP, String GhiChu, boolean TrangThai) {
        this.IDDongSP = IDDongSP;
        this.IDThuongHieu = IDThuongHieu;
        this.MaDongSP = MaDongSP;
        this.TenDongSP = TenDongSP;
        this.GhiChu = GhiChu;
        this.TrangThai = TrangThai;
    }

    public int getIDDongSP() {
        return IDDongSP;
    }

    public void setIDDongSP(int IDDongSP) {
        this.IDDongSP = IDDongSP;
    }

    public int getIDThuongHieu() {
        return IDThuongHieu;
    }

    public void setIDThuongHieu(int IDThuongHieu) {
        this.IDThuongHieu = IDThuongHieu;
    }

    public String getMaDongSP() {
        return MaDongSP;
    }

    public void setMaDongSP(String MaDongSP) {
        this.MaDongSP = MaDongSP;
    }

    public String getTenDongSP() {
        return TenDongSP;
    }

    public void setTenDongSP(String TenDongSP) {
        this.TenDongSP = TenDongSP;
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
