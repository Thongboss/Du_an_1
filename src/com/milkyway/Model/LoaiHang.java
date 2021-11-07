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

    private int MaLoai, MaNhom;
    private String TenLoai, GhiChu;

    public LoaiHang() {
    }

    public LoaiHang(int MaLoai, int MaNhom, String TenLoai, String GhiChu) {
        this.MaLoai = MaLoai;
        this.MaNhom = MaNhom;
        this.TenLoai = TenLoai;
        this.GhiChu = GhiChu;
    }

    public int getMaLoai() {
        return MaLoai;
    }

    public void setMaLoai(int MaLoai) {
        this.MaLoai = MaLoai;
    }

    public int getMaNhom() {
        return MaNhom;
    }

    public void setMaNhom(int MaNhom) {
        this.MaNhom = MaNhom;
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
