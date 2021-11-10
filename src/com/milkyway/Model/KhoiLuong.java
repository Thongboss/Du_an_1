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

    private String MaKhoiLuong;
    private int GiaTri;

    public KhoiLuong() {
    }

    public KhoiLuong(String MaKhoiLuong, int GiaTri) {
        this.MaKhoiLuong = MaKhoiLuong;
        this.GiaTri = GiaTri;
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
