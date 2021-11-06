/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.entity;

/**
 *
 * @author ASUS
 */
public class ThuongHieu {

    private String MaTH, TenTh, GhiChu;
    private boolean Trangthai;

    public ThuongHieu() {
    }

    public ThuongHieu(String MaTH, String TenTh, String GhiChu, boolean Trangthai) {
        this.MaTH = MaTH;
        this.TenTh = TenTh;
        this.GhiChu = GhiChu;
        this.Trangthai = Trangthai;
    }

    public String getMaTH() {
        return MaTH;
    }

    public void setMaTH(String MaTH) {
        this.MaTH = MaTH;
    }

    public String getTenTh() {
        return TenTh;
    }

    public void setTenTh(String TenTh) {
        this.TenTh = TenTh;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String GhiChu) {
        this.GhiChu = GhiChu;
    }

    public boolean isTrangthai() {
        return Trangthai;
    }

    public void setTrangthai(boolean Trangthai) {
        this.Trangthai = Trangthai;
    }

}
