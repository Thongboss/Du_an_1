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
public class LoaiHang {

    private String MaLoai;
    private int IDNhomHang;
    private String TenLoai, GhiChu;

    public LoaiHang() {
    }

    public LoaiHang(String MaLoai, int MaNhom, String TenLoai, String GhiChu) {
        this.MaLoai = MaLoai;
        this.IDNhomHang = MaNhom;
        this.TenLoai = TenLoai;
        this.GhiChu = GhiChu;
    }

    public String getMaLoai() {
        return MaLoai;
    }

    public void setMaLoai(String MaLoai) {
        this.MaLoai = MaLoai;
    }

    public int getIDNhomHang() {
        return IDNhomHang;
    }

    public void setIDNhomHang(int IDNhomHang) {
        this.IDNhomHang = IDNhomHang;
    }

    public String getTenLoai() {
        return TenLoai;
    }

    public void setTenLoai(String TenLoai) {
        this.TenLoai = TenLoai;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String GhiChu) {
        this.GhiChu = GhiChu;
    }

}
