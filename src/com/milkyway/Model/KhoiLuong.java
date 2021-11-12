/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.Model;

/**
 *
 * @author ASUS
 */
public class KhoiLuong {
private int IDKhoiLuong;
    private String MaKhoiLuong;
    private int GiaTri;

    public KhoiLuong() {
    }

    public KhoiLuong(int IDKhoiLuong, String MaKhoiLuong, int GiaTri) {
        this.IDKhoiLuong = IDKhoiLuong;
        this.MaKhoiLuong = MaKhoiLuong;
        this.GiaTri = GiaTri;
    }

    public int getIDKhoiLuong() {
        return IDKhoiLuong;
    }

    public void setIDKhoiLuong(int IDKhoiLuong) {
        this.IDKhoiLuong = IDKhoiLuong;
    }

    public String getMaKhoiLuong() {
        return MaKhoiLuong;
    }

    public void setMaKhoiLuong(String MaKhoiLuong) {
        this.MaKhoiLuong = MaKhoiLuong;
    }

    public int getGiaTri() {
        return GiaTri;
    }

    public void setGiaTri(int GiaTri) {
        this.GiaTri = GiaTri;
    }

  

}
