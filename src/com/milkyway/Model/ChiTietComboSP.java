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
public class ChiTietComboSP {

    private int IDChiTietComboSP, IDComboSP, IDChiTietSP;

    public ChiTietComboSP() {
    }

    public ChiTietComboSP(int IDChiTietComboSP, int IDComboSP, int IDChiTietSP) {
        this.IDChiTietComboSP = IDChiTietComboSP;
        this.IDComboSP = IDComboSP;
        this.IDChiTietSP = IDChiTietSP;
    }

    public int getIDChiTietComboSP() {
        return IDChiTietComboSP;
    }

    public void setIDChiTietComboSP(int IDChiTietComboSP) {
        this.IDChiTietComboSP = IDChiTietComboSP;
    }

    public int getIDComboSP() {
        return IDComboSP;
    }

    public void setIDComboSP(int IDComboSP) {
        this.IDComboSP = IDComboSP;
    }

    public int getIDChiTietSP() {
        return IDChiTietSP;
    }

    public void setIDChiTietSP(int IDChiTietSP) {
        this.IDChiTietSP = IDChiTietSP;
    }
}
