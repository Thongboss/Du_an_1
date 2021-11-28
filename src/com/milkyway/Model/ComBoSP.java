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
public class ComBoSP {
    
    private int IDComboSP;
    private String MaComboSP, TenComboSP;
    private int SoLuongSP;
    private double TongGia;
    private Date NgayTao, NgayHetHan;
    private String Barcode, GhiChu, AnhComboSP;

    public ComBoSP() {
    }

    public ComBoSP(int IDComboSP, String MaComboSP, String TenComboSP, int SoLuongSP, double TongGia, Date NgayTao, Date NgayHetHan, String Barcode, String GhiChu, String AnhComboSP) {
        this.IDComboSP = IDComboSP;
        this.MaComboSP = MaComboSP;
        this.TenComboSP = TenComboSP;
        this.SoLuongSP = SoLuongSP;
        this.TongGia = TongGia;
        this.NgayTao = NgayTao;
        this.NgayHetHan = NgayHetHan;
        this.Barcode = Barcode;
        this.GhiChu = GhiChu;
        this.AnhComboSP = AnhComboSP;
    }

    public int getIDComboSP() {
        return IDComboSP;
    }

    public void setIDComboSP(int IDComboSP) {
        this.IDComboSP = IDComboSP;
    }

    public String getMaComboSP() {
        return MaComboSP;
    }

    public void setMaComboSP(String MaComboSP) {
        this.MaComboSP = MaComboSP;
    }

    public String getTenComboSP() {
        return TenComboSP;
    }

    public void setTenComboSP(String TenComboSP) {
        this.TenComboSP = TenComboSP;
    }

    public int getSoLuongSP() {
        return SoLuongSP;
    }

    public void setSoLuongSP(int SoLuongSP) {
        this.SoLuongSP = SoLuongSP;
    }

    public double getTongGia() {
        return TongGia;
    }

    public void setTongGia(double TongGia) {
        this.TongGia = TongGia;
    }

    public Date getNgayTao() {
        return NgayTao;
    }

    public void setNgayTao(Date NgayTao) {
        this.NgayTao = NgayTao;
    }

    public Date getNgayHetHan() {
        return NgayHetHan;
    }

    public void setNgayHetHan(Date NgayHetHan) {
        this.NgayHetHan = NgayHetHan;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String Barcode) {
        this.Barcode = Barcode;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String GhiChu) {
        this.GhiChu = GhiChu;
    }

    public String getAnhComboSP() {
        return AnhComboSP;
    }

    public void setAnhComboSP(String AnhComboSP) {
        this.AnhComboSP = AnhComboSP;
    }
}
