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
public class DatHang {

    private int IDDatHang;
    private String MaDatHang;
    private int IDNhanVien, IDTheTV;
    private Date NgayLap;
    private String HoTenKhachHang, SDT, DiaChi;
    private double TongTien;
    private int IDHinhThucThanhToan;
    private String GhiChu, TrangThai;

    public DatHang() {
    }

    public DatHang(int IDDatHang, String MaDatHang, int IDNhanVien, int IDTheTV, Date NgayLap, String HoTenKhachHang, String SDT, String DiaChi, double TongTien, int IDHinhThucThanhToan, String GhiChu, String TrangThai) {
        this.IDDatHang = IDDatHang;
        this.MaDatHang = MaDatHang;
        this.IDNhanVien = IDNhanVien;
        this.IDTheTV = IDTheTV;
        this.NgayLap = NgayLap;
        this.HoTenKhachHang = HoTenKhachHang;
        this.SDT = SDT;
        this.DiaChi = DiaChi;
        this.TongTien = TongTien;
        this.IDHinhThucThanhToan = IDHinhThucThanhToan;
        this.GhiChu = GhiChu;
        this.TrangThai = TrangThai;
    }

    public int getIDDatHang() {
        return IDDatHang;
    }

    public void setIDDatHang(int IDDatHang) {
        this.IDDatHang = IDDatHang;
    }

    public String getMaDatHang() {
        return MaDatHang;
    }

    public void setMaDatHang(String MaDatHang) {
        this.MaDatHang = MaDatHang;
    }

    public int getIDNhanVien() {
        return IDNhanVien;
    }

    public void setIDNhanVien(int IDNhanVien) {
        this.IDNhanVien = IDNhanVien;
    }

    public int getIDTheTV() {
        return IDTheTV;
    }

    public void setIDTheTV(int IDTheTV) {
        this.IDTheTV = IDTheTV;
    }

    public Date getNgayLap() {
        return NgayLap;
    }

    public void setNgayLap(Date NgayLap) {
        this.NgayLap = NgayLap;
    }

    public String getHoTenKhachHang() {
        return HoTenKhachHang;
    }

    public void setHoTenKhachHang(String HoTenKhachHang) {
        this.HoTenKhachHang = HoTenKhachHang;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String DiaChi) {
        this.DiaChi = DiaChi;
    }

    public double getTongTien() {
        return TongTien;
    }

    public void setTongTien(double TongTien) {
        this.TongTien = TongTien;
    }

    public int getIDHinhThucThanhToan() {
        return IDHinhThucThanhToan;
    }

    public void setIDHinhThucThanhToan(int IDHinhThucThanhToan) {
        this.IDHinhThucThanhToan = IDHinhThucThanhToan;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String GhiChu) {
        this.GhiChu = GhiChu;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String TrangThai) {
        this.TrangThai = TrangThai;
    }
}
