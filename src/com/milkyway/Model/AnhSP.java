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
public class AnhSP {

    private String MaAnhSP;
    private String TenAnhSP;

    public AnhSP() {
    }

    public AnhSP(String MaAnhSP, String TenAnhSP) {
        this.MaAnhSP = MaAnhSP;
        this.TenAnhSP = TenAnhSP;
    }

    public String getMaAnhSP() {
        return MaAnhSP;
    }

    public void setMaAnhSP(String MaAnhSP) {
        this.MaAnhSP = MaAnhSP;
    }

    public String getTenAnhSP() {
        return TenAnhSP;
    }

    public void setTenAnhSP(String TenAnhSP) {
        this.TenAnhSP = TenAnhSP;
    }

}
