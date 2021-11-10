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

    private String MaQG;
    private String TenQG;

    public XuatXu() {
    }

    public XuatXu(String MaQG, String TenQG) {
        this.MaQG = MaQG;
        this.TenQG = TenQG;
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
