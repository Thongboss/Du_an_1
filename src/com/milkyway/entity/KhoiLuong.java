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
public class KhoiLuong {

    private int MaKhoiLuong;
    private String TenKhoiLuong;

    public KhoiLuong() {
    }

    public KhoiLuong(int MaKhoiLuong, String TenKhoiLuong) {
        this.MaKhoiLuong = MaKhoiLuong;
        this.TenKhoiLuong = TenKhoiLuong;
    }

    public int getMaKhoiLuong() {
        return MaKhoiLuong;
    }

    public void setMaKhoiLuong(int MaKhoiLuong) {
        this.MaKhoiLuong = MaKhoiLuong;
    }

    public String getTenKhoiLuong() {
        return TenKhoiLuong;
    }

    public void setTenKhoiLuong(String TenKhoiLuong) {
        this.TenKhoiLuong = TenKhoiLuong;
    }

}
