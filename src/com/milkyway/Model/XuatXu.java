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
public class XuatXu {
private int IDXuatXu;
    private String MaQG;
    private String TenQG;

    public XuatXu() {
    }

    public XuatXu(int IDXuatXu, String MaQG, String TenQG) {
        this.IDXuatXu = IDXuatXu;
        this.MaQG = MaQG;
        this.TenQG = TenQG;
    }

    public int getIDXuatXu() {
        return IDXuatXu;
    }

    public void setIDXuatXu(int IDXuatXu) {
        this.IDXuatXu = IDXuatXu;
    }

    public String getMaQG() {
        return MaQG;
    }

    public void setMaQG(String MaQG) {
        this.MaQG = MaQG;
    }

    public String getTenQG() {
        return TenQG;
    }

    public void setTenQG(String TenQG) {
        this.TenQG = TenQG;
    }

  
}
