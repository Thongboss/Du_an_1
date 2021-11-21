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
    private String GiaTri;

    public KhoiLuong() {
    }

    public KhoiLuong(int IDKhoiLuong, String MaKhoiLuong, String GiaTri) {
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

    public String getGiaTri() {
        return GiaTri;
    }

    public void setGiaTri(String GiaTri) {
        this.GiaTri = GiaTri;
    }

    @Override
    public String toString() {
        return GiaTri;
    }

}
