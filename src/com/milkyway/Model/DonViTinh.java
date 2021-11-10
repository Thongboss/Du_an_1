/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.Model;

/**
 *
 * @author ASUS
 */
public class DonViTinh {

    private String MaDVT;
    private String TenDVT;
    private String VietTat;

    public DonViTinh() {
    }

    public DonViTinh(String MaDVT, String TenDVT, String VietTat) {
        this.MaDVT = MaDVT;
        this.TenDVT = TenDVT;
        this.VietTat = VietTat;
    }

    public String getMaDVT() {
        return MaDVT;
    }

    public void setMaDVT(String MaDVT) {
        this.MaDVT = MaDVT;
    }

    public String getTenDVT() {
        return TenDVT;
    }

    public void setTenDVT(String TenDVT) {
        this.TenDVT = TenDVT;
    }

    public String getVietTat() {
        return VietTat;
    }

    public void setVietTat(String VietTat) {
        this.VietTat = VietTat;
    }

}
