/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.Model;

import java.util.Date;

/**
 *
 * @author DaiAustinYersin
 */
public class ChiTietComboSP {

    private int IDChiTietComboSP, IDComboSP, IDChiTietSP, SoLuong;
    private double DonGia;
    private Date HanSD;
    private String BarCode;

    public ChiTietComboSP() {
    }

    public ChiTietComboSP(int IDChiTietComboSP, int IDComboSP, int IDChiTietSP, int SoLuong, double DonGia, Date HanSD, String BarCode) {
        this.IDChiTietComboSP = IDChiTietComboSP;
        this.IDComboSP = IDComboSP;
        this.IDChiTietSP = IDChiTietSP;
        this.SoLuong = SoLuong;
        this.DonGia = DonGia;
        this.HanSD = HanSD;
        this.BarCode = BarCode;
    }

    public int getIDChiTietComboSP() {
        return IDChiTietComboSP;
    }

    public void setIDChiTietComboSP(int IDChiTietComboSP) {
        this.IDChiTietComboSP = IDChiTietComboSP;
    }

    public int getIDComboSP() {
        return IDComboSP;
    }

    public void setIDComboSP(int IDComboSP) {
        this.IDComboSP = IDComboSP;
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

    public Date getHanSD() {
        return HanSD;
    }

    public void setHanSD(Date HanSD) {
        this.HanSD = HanSD;
    }

    public String getBarCode() {
        return BarCode;
    }

    public void setBarCode(String BarCode) {
        this.BarCode = BarCode;
    }
}
