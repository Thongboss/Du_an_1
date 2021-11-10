/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.Model;

/**
 *
 * @author DaiAustinYersin
 */
public class HinhThucThanhToan {

    private String MaHinhThucThanhToan;
    private String TenHinhThucThanhToan;
    private String GhiChu;

    public HinhThucThanhToan() {
    }

    public HinhThucThanhToan(String MaHinhThucThanhToan, String TenHinhThucThanhToan, String GhiChu) {
        this.MaHinhThucThanhToan = MaHinhThucThanhToan;
        this.TenHinhThucThanhToan = TenHinhThucThanhToan;
        this.GhiChu = GhiChu;
    }

    public String getMaHinhThucThanhToan() {
        return MaHinhThucThanhToan;
    }

    public void setMaHinhThucThanhToan(String MaHinhThucThanhToan) {
        this.MaHinhThucThanhToan = MaHinhThucThanhToan;
    }

    public String getTenHinhThucThanhToan() {
        return TenHinhThucThanhToan;
    }

    public void setTenHinhThucThanhToan(String TenHinhThucThanhToan) {
        this.TenHinhThucThanhToan = TenHinhThucThanhToan;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String GhiChu) {
        this.GhiChu = GhiChu;
    }
}
