/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.entity;

import java.util.Date;

/**
 *
 * @author hoang
 */
public class ChiTietSanPham {

    private int ID, MaSP;
    private Date HanSD;
    private int SoLuongTon;
    private double DonGia;
    private int MaXuatXu, MaKhoiLuong, MaDVT, MaAnhSP, BarCode;

    public ChiTietSanPham() {
    }

    public ChiTietSanPham(int ID, int MaSP, Date HanSD, int SoLuongTon, double DonGia, int MaXuatXu, int MaKhoiLuong, int MaDVT, int MaAnhSP, int BarCode) {
        this.ID = ID;
        this.MaSP = MaSP;
        this.HanSD = HanSD;
        this.SoLuongTon = SoLuongTon;
        this.DonGia = DonGia;
        this.MaXuatXu = MaXuatXu;
        this.MaKhoiLuong = MaKhoiLuong;
        this.MaDVT = MaDVT;
        this.MaAnhSP = MaAnhSP;
        this.BarCode = BarCode;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getMaSP() {
        return MaSP;
    }

    public void setMaSP(int MaSP) {
        this.MaSP = MaSP;
    }

    public int getSoLuongTon() {
        return SoLuongTon;
    }

    public void setSoLuongTon(int SoLuongTon) {
        this.SoLuongTon = SoLuongTon;
    }

    public int getMaXuatXu() {
        return MaXuatXu;
    }

    public void setMaXuatXu(int MaXuatXu) {
        this.MaXuatXu = MaXuatXu;
    }

    public int getMaKhoiLuong() {
        return MaKhoiLuong;
    }

    public void setMaKhoiLuong(int MaKhoiLuong) {
        this.MaKhoiLuong = MaKhoiLuong;
    }

    public int getMaDVT() {
        return MaDVT;
    }

    public void setMaDVT(int MaDVT) {
        this.MaDVT = MaDVT;
    }

    public int getMaAnhSP() {
        return MaAnhSP;
    }

    public void setMaAnhSP(int MaAnhSP) {
        this.MaAnhSP = MaAnhSP;
    }

    public int getBarCode() {
        return BarCode;
    }

    public void setBarCode(int BarCode) {
        this.BarCode = BarCode;
    }

    public Date getHanSD() {
        return HanSD;
    }

    public void setHanSD(Date HanSD) {
        this.HanSD = HanSD;
    }

    public double getDonGia() {
        return DonGia;
    }

    public void setDonGia(double DonGia) {
        this.DonGia = DonGia;
    }

}
