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

    private int IDDonViTinh;
    private String MaDVT;
    private String TenDVT;

    public DonViTinh() {
    }

    public DonViTinh(int IDDonViTinh, String MaDVT, String TenDVT) {
        this.IDDonViTinh = IDDonViTinh;
        this.MaDVT = MaDVT;
        this.TenDVT = TenDVT;
    }

    public int getIDDonViTinh() {
        return IDDonViTinh;
    }

    public void setIDDonViTinh(int IDDonViTinh) {
        this.IDDonViTinh = IDDonViTinh;
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

}
