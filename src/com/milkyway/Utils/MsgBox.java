/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.Utils;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author DaiAustinYersin
 */
public class MsgBox {

    public static void alert(Component parent, String mes) {
        JOptionPane.showMessageDialog(parent, mes, "Phầm mềm quản lý đại lý sữa Milky Way", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static boolean confirm(Component parent, String mes) {
        int result = JOptionPane.showConfirmDialog(parent, mes, "Phầm mềm quản lý đại lý sữa Milky Way", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
    
    public static String prompt(Component parent, String mes) {
        return JOptionPane.showInputDialog(parent, mes, "Phầm mềm quản lý đại lý sữa Milky Way", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static String prompt1(Component parent, String mes) {
        return JOptionPane.showInputDialog(parent, mes, 1);
    }
}
