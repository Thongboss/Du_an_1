/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.Model;

import java.util.Date;

/**
 *
 * @author Admin
 */
public class TheThanhVien {

    private int IDTheTV;
    private String MaTheTV;
    private String TenKH;
    private boolean GioiTinh;
    private Date NgaySinh;
    private String SDT, CMND, Email, HinhAnh;
    private int NguoiTao;
    private Date NgayTao, NgayHetHan;
    private boolean TrangThai;

    public TheThanhVien() {
    }

    public TheThanhVien(int IDTheTV, String MaTheTV, String TenKH, boolean GioiTinh, Date NgaySinh, String SDT, String CMND, String Email, String HinhAnh, int NguoiTao, Date NgayTao, Date NgayHetHan, boolean TrangThai) {
        this.IDTheTV = IDTheTV;
        this.MaTheTV = MaTheTV;
        this.TenKH = TenKH;
        this.GioiTinh = GioiTinh;
        this.NgaySinh = NgaySinh;
        this.SDT = SDT;
        this.CMND = CMND;
        this.Email = Email;
        this.HinhAnh = HinhAnh;
        this.NguoiTao = NguoiTao;
        this.NgayTao = NgayTao;
        this.NgayHetHan = NgayHetHan;
        this.TrangThai = TrangThai;
    }

    public int getIDTheTV() {
        return IDTheTV;
    }

    public void setIDTheTV(int IDTheTV) {
        this.IDTheTV = IDTheTV;
    }

    public String getMaTheTV() {
        return MaTheTV;
    }

    public void setMaTheTV(String MaTheTV) {
        this.MaTheTV = MaTheTV;
    }

    public String getTenKH() {
        return TenKH;
    }

    public void setTenKH(String TenKH) {
        this.TenKH = TenKH;
    }

    public boolean isGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(boolean GioiTinh) {
        this.GioiTinh = GioiTinh;
    }

    public Date getNgaySinh() {
        return NgaySinh;
    }

    public void setNgaySinh(Date NgaySinh) {
        this.NgaySinh = NgaySinh;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getCMND() {
        return CMND;
    }

    public void setCMND(String CMND) {
        this.CMND = CMND;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String HinhAnh) {
        this.HinhAnh = HinhAnh;
    }

    public int getNguoiTao() {
        return NguoiTao;
    }

    public void setNguoiTao(int NguoiTao) {
        this.NguoiTao = NguoiTao;
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

    public boolean isTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(boolean TrangThai) {
        this.TrangThai = TrangThai;
    }

}
