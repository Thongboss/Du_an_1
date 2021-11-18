/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.Model;

import java.util.Date;

/**
 *
 * @author hoang
 */
public class ChiTietSanPham {

    private int IDChiTietSP, IDSanPham;
    private Date HanSD;
    private int SoLuongTon;
    private double DonGia;
    private int IDXuatXu, IDKhoiLuong, IDDonViTinh, IDAnhSP;
    private String BarCode;

    public ChiTietSanPham() {
    }

    public ChiTietSanPham(int IDChiTietHD, int IDSanPham, Date HanSD, int SoLuongTon, double DonGia, int IDXuatXu, int IDKhoiLuong, int IDDonViTinh, int IDAnhSP, String BarCode) {
        this.IDChiTietSP = IDChiTietHD;
        this.IDSanPham = IDSanPham;
        this.HanSD = HanSD;
        this.SoLuongTon = SoLuongTon;
        this.DonGia = DonGia;
        this.IDXuatXu = IDXuatXu;
        this.IDKhoiLuong = IDKhoiLuong;
        this.IDDonViTinh = IDDonViTinh;
        this.IDAnhSP = IDAnhSP;
        this.BarCode = BarCode;
    }

    public int getIDChiTietSP() {
        return IDChiTietSP;
    }

    public void setIDChiTietSP(int IDChiTietSP) {
        this.IDChiTietSP = IDChiTietSP;
    }

    public int getIDSanPham() {
        return IDSanPham;
    }

    public void setIDSanPham(int IDSanPham) {
        this.IDSanPham = IDSanPham;
    }

    public int getSoLuongTon() {
        return SoLuongTon;
    }

    public void setSoLuongTon(int SoLuongTon) {
        this.SoLuongTon = SoLuongTon;
    }

    public int getIDXuatXu() {
        return IDXuatXu;
    }

    public void setIDXuatXu(int IDXuatXu) {
        this.IDXuatXu = IDXuatXu;
    }

    public int getIDKhoiLuong() {
        return IDKhoiLuong;
    }

    public void setIDKhoiLuong(int IDKhoiLuong) {
        this.IDKhoiLuong = IDKhoiLuong;
    }

    public int getIDDonViTinh() {
        return IDDonViTinh;
    }

    public void setIDDonViTinh(int IDDonViTinh) {
        this.IDDonViTinh = IDDonViTinh;
    }

    public int getIDAnhSP() {
        return IDAnhSP;
    }

    public void setIDAnhSP(int IDAnhSP) {
        this.IDAnhSP = IDAnhSP;
    }

    public String getBarCode() {
        return BarCode;
    }

    public void setBarCode(String BarCode) {
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
