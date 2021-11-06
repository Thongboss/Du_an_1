/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.entity;

import java.util.Date;

/**
 *
 * @author DaiAustinYersin
 */
public class NhanVien {

    private int MaNV;
    private byte[] Password;
    private String HoTen;
    private boolean GioiTinh;
    private Date NgaySinh;
    private String SDT;
    private String CMND;
    private String Email;
    private String HinhAnh;
    private Date NgayTao;
    private int NguoiTao;
    private Date NgayHetHan;
    private boolean TrangThai;

    public NhanVien() {
    }

    public NhanVien(int MaNV, byte[] Password, String HoTen, boolean GioiTinh, Date NgaySinh, String SDT, String CMND, String Email, String HinhAnh, Date NgayTao, int NguoiTao, Date NgayHetHan, boolean TrangThai) {
        this.MaNV = MaNV;
        this.Password = Password;
        this.HoTen = HoTen;
        this.GioiTinh = GioiTinh;
        this.NgaySinh = NgaySinh;
        this.SDT = SDT;
        this.CMND = CMND;
        this.Email = Email;
        this.HinhAnh = HinhAnh;
        this.NgayTao = NgayTao;
        this.NguoiTao = NguoiTao;
        this.NgayHetHan = NgayHetHan;
        this.TrangThai = TrangThai;
    }

    public int getMaNV() {
        return MaNV;
    }

    public void setMaNV(int MaNV) {
        this.MaNV = MaNV;
    }

    public byte[] getPassword() {
        return Password;
    }

    public void setPassword(byte[] Password) {
        this.Password = Password;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String HoTen) {
        this.HoTen = HoTen;
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

    public Date getNgayTao() {
        return NgayTao;
    }

    public void setNgayTao(Date NgayTao) {
        this.NgayTao = NgayTao;
    }

    public int getNguoiTao() {
        return NguoiTao;
    }

    public void setNguoiTao(int NguoiTao) {
        this.NguoiTao = NguoiTao;
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
