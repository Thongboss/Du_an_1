/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.Model;

import java.util.Date;

/**
 *
 * @author hoang
 */
public class SanPham {

    private String MaSP; 
    private int IDLoaiSP, IDDongSP;
    private String TenSP;
    private Date NgayXK;
    private String GhiChu;
    private boolean TrangThai;

    public SanPham() {
    }

    public SanPham(String MaSP, int MaLoai, int MaDongSP, String TenSP, String GhiChu, Date NgayXK, boolean TrangThai) {
        this.MaSP = MaSP;
        this.IDLoaiSP = MaLoai;
        this.IDDongSP = MaDongSP;
        this.TenSP = TenSP;
        this.GhiChu = GhiChu;
        this.NgayXK = NgayXK;
        this.TrangThai = TrangThai;
    }

    public String getMaSP() {
        return MaSP;
    }

    public void setMaSP(String MaSP) {
        this.MaSP = MaSP;
    }

    public int getIDLoaiSP() {
        return IDLoaiSP;
    }

    public void setIDLoaiSP(int IDLoaiSP) {
        this.IDLoaiSP = IDLoaiSP;
    }

    public int getIDDongSP() {
        return IDDongSP;
    }

    public void setIDDongSP(int IDDongSP) {
        this.IDDongSP = IDDongSP;
    }

    public String getTenSP() {
        return TenSP;
    }

    public void setTenSP(String TenSP) {
        this.TenSP = TenSP;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String GhiChu) {
        this.GhiChu = GhiChu;
    }

    public Date getNgayXK() {
        return NgayXK;
    }

    public void setNgayXK(Date NgayXK) {
        this.NgayXK = NgayXK;
    }

    public boolean isTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(boolean TrangThai) {
        this.TrangThai = TrangThai;
    }

}
