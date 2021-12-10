/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.Model;

/**
 *
 * @author DaiAustinYersin
 */
public class ChiTietDatHang {

    private int IDChiTietDatHang, IDDatHang, IDChiTietSP, SoLuong;
    private double DonGia;

    public ChiTietDatHang() {
    }

    public ChiTietDatHang(int IDChiTietDatHang, int IDDatHang, int IDChiTietSP, int SoLuong, double DonGia) {
        this.IDChiTietDatHang = IDChiTietDatHang;
        this.IDDatHang = IDDatHang;
        this.IDChiTietSP = IDChiTietSP;
        this.SoLuong = SoLuong;
        this.DonGia = DonGia;
    }

    public int getIDChiTietDatHang() {
        return IDChiTietDatHang;
    }

    public void setIDChiTietDatHang(int IDChiTietDatHang) {
        this.IDChiTietDatHang = IDChiTietDatHang;
    }

    public int getIDDatHang() {
        return IDDatHang;
    }

    public void setIDDatHang(int IDDatHang) {
        this.IDDatHang = IDDatHang;
    }

    public int getIDChiTietSP() {
        return IDChiTietSP;
    }

    public void setIDChiTietSP(int IDChiTietSP) {
        this.IDChiTietSP = IDChiTietSP;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int SoLuong) {
        this.SoLuong = SoLuong;
    }

    public double getDonGia() {
        return DonGia;
    }

    public void setDonGia(double DonGia) {
        this.DonGia = DonGia;
    }
}
