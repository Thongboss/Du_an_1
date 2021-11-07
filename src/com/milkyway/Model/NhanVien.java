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
public class NhanVien {

    private int MaNV;
    private String TaiKhoan;
    private byte[] MatKhau;
    private String HoTen;
    private boolean GioiTinh;
    private Date NgaySinh;
    private String SDT;
    private String CMND;
    private String Email;
    private String HinhAnh;
    private boolean VaiTro;
    private boolean TrangThai;
    private byte[] Salt;

    public NhanVien() {
    }

    public NhanVien(int MaNV, String TaiKhoan, byte[] MatKhau, String HoTen, boolean GioiTinh, Date NgaySinh, String SDT, String CMND, String Email, String HinhAnh, boolean VaiTro, boolean TrangThai, byte[] Salt) {
        this.MaNV = MaNV;
        this.TaiKhoan = TaiKhoan;
        this.MatKhau = MatKhau;
        this.HoTen = HoTen;
        this.GioiTinh = GioiTinh;
        this.NgaySinh = NgaySinh;
        this.SDT = SDT;
        this.CMND = CMND;
        this.Email = Email;
        this.HinhAnh = HinhAnh;
        this.VaiTro = VaiTro;
        this.TrangThai = TrangThai;
        this.Salt = Salt;
    }

    public int getMaNV() {
        return MaNV;
    }

    public void setMaNV(int MaNV) {
        this.MaNV = MaNV;
    }

    public String getTaiKhoan() {
        return TaiKhoan;
    }

    public void setTaiKhoan(String TaiKhoan) {
        this.TaiKhoan = TaiKhoan;
    }

    public byte[] getMatKhau() {
        return MatKhau;
    }

    public void setMatKhau(byte[] MatKhau) {
        this.MatKhau = MatKhau;
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

    public boolean isVaiTro() {
        return VaiTro;
    }

    public void setVaiTro(boolean VaiTro) {
        this.VaiTro = VaiTro;
    }

    public boolean isTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(boolean TrangThai) {
        this.TrangThai = TrangThai;
    }

    public byte[] getSalt() {
        return Salt;
    }

    public void setSalt(byte[] Salt) {
        this.Salt = Salt;
    }
}
