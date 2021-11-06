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

    private int MaHD, ID;
    private int SoLuong;
    private double DonGia;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(int MaHD, int ID, int SoLuong, double DonGia) {
        this.MaHD = MaHD;
        this.ID = ID;
        this.SoLuong = SoLuong;
        this.DonGia = DonGia;
    }

    public int getMaHD() {
        return MaHD;
    }

    public void setMaHD(int MaHD) {
        this.MaHD = MaHD;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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
