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
public class NhomHang {

    private String MaNhom;
    private String TenNhom, GhiChu;

    public NhomHang() {
    }

    public NhomHang(String MaNhom, String TenNhom, String GhiChu) {
        this.MaNhom = MaNhom;
        this.TenNhom = TenNhom;
        this.GhiChu = GhiChu;
    }

    public String getMaNhom() {
        return MaNhom;
    }

    public void setMaNhom(String MaNhom) {
        this.MaNhom = MaNhom;
    }

    public String getTenNhom() {
        return TenNhom;
    }

    public void setTenNhom(String TenNhom) {
        this.TenNhom = TenNhom;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String GhiChu) {
        this.GhiChu = GhiChu;
    }

    

}
