/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.entity;

/**
 *
 * @author hoang
 */
public class DongSP {
    private int MaDongSP,MaTH;
    private String TenDongSP,GhiChu;
    private boolean TrangThai;

    public DongSP() {
    }

    public DongSP(int MaDongSP, int MaTH, String TenDongSP, String GhiChu, boolean TrangThai) {
        this.MaDongSP = MaDongSP;
        this.MaTH = MaTH;
        this.TenDongSP = TenDongSP;
        this.GhiChu = GhiChu;
        this.TrangThai = TrangThai;
    }

    public int getMaDongSP() {
        return MaDongSP;
    }

    public void setMaDongSP(int MaDongSP) {
        this.MaDongSP = MaDongSP;
    }

    public int getMaTH() {
        return MaTH;
    }

    public void setMaTH(int MaTH) {
        this.MaTH = MaTH;
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
