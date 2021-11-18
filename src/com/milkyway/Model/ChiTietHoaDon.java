/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.Model;

/**
 *
 * @author Admin
 */
public class ChiTietHoaDon {

    private int IDChiTietHD, IDHoaDon;
    private int IDChiTietSP;
    private int SoLuong;
    private double DonGia;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(int IDChiTietHD, int IDHoaDon, int IDChiTietSP, int SoLuong, double DonGia) {
        this.IDChiTietHD = IDChiTietHD;
        this.IDHoaDon = IDHoaDon;
        this.IDChiTietSP = IDChiTietSP;
        this.SoLuong = SoLuong;
        this.DonGia = DonGia;
    }

    public int getIDChiTietHD() {
        return IDChiTietHD;
    }

    public void setIDChiTietHD(int IDChiTietHD) {
        this.IDChiTietHD = IDChiTietHD;
    }

    public int getIDHoaDon() {
        return IDHoaDon;
    }

    public void setIDHoaDon(int IDHoaDon) {
        this.IDHoaDon = IDHoaDon;
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
