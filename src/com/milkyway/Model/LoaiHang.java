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

<<<<<<< HEAD
    
    private String MaLoai,TenLoai, GhiChu;
=======
    private String MaLoai;
    private int IDNhomHang;
    private String TenLoai, GhiChu;
>>>>>>> 170f4f7d0730640be24170e2ce4d788972caf121

    public LoaiHang() {
    }

<<<<<<< HEAD
    public LoaiHang(String MaLoai, String TenLoai, String GhiChu) {
        this.MaLoai = MaLoai;
=======
    public LoaiHang(String MaLoai, int MaNhom, String TenLoai, String GhiChu) {
        this.MaLoai = MaLoai;
        this.IDNhomHang = MaNhom;
>>>>>>> 170f4f7d0730640be24170e2ce4d788972caf121
        this.TenLoai = TenLoai;
        this.GhiChu = GhiChu;
    }

    public String getMaLoai() {
        return MaLoai;
    }

    public void setMaLoai(String MaLoai) {
        this.MaLoai = MaLoai;
    }

<<<<<<< HEAD
=======
    public int getIDNhomHang() {
        return IDNhomHang;
    }

    public void setIDNhomHang(int IDNhomHang) {
        this.IDNhomHang = IDNhomHang;
    }

>>>>>>> 170f4f7d0730640be24170e2ce4d788972caf121
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
