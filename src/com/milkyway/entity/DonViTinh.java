/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.entity;

/**
 *
 * @author ASUS
 */
public class DonViTinh {

    private int MaDVT;
    private String TenDVT;

    public DonViTinh() {
    }
    
    public DonViTinh(int MaDVT, String TenDVT) {
        this.MaDVT = MaDVT;
        this.TenDVT = TenDVT;
    }

    public int getMaDVT() {
        return MaDVT;
    }

    public void setMaDVT(int MaDVT) {
        this.MaDVT = MaDVT;
    }

    public String getTenDVT() {
        return TenDVT;
    }

    public void setTenDVT(String TenDVT) {
        this.TenDVT = TenDVT;
    }

}
