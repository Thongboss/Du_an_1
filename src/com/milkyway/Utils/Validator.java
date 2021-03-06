/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.Utils;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author DaiAustinYersin
 */
public class Validator {

    public static boolean isNull(JTextField txt, String mes, StringBuilder sb) {
        if (txt.getText().trim().isEmpty()) {
            sb.append(mes).append("\n");
            txt.setBackground(Color.YELLOW);
            return true;
        } else {
            txt.setBackground(Color.WHITE);
            return false;
        }
    }

    public static boolean isNull(JTextArea txt, String mes, StringBuilder sb) {
        if (txt.getText().trim().isEmpty()) {
            sb.append(mes).append("\n");
            txt.setBackground(Color.YELLOW);
            return true;
        } else {
            txt.setBackground(Color.WHITE);
            return false;
        }
    }

    public static boolean isNull(JPasswordField pass, String mes, StringBuilder sb) {
        if (new String(pass.getPassword()).isEmpty()) {
            sb.append(mes).append("\n");
            pass.setBackground(Color.YELLOW);
            return true;
        } else {
            pass.setBackground(Color.WHITE);
            return false;
        }
    }
    
    public static boolean isNull(JDateChooser date, String mes, StringBuilder sb) {
        if (date.getDate() == null) {
            sb.append(mes).append("\n");
            date.setBackground(Color.YELLOW);
            return true;
        } else {
            date.setBackground(Color.WHITE);
            return false;
        }
    }

    public static boolean checkNgaySinh(JDateChooser date, StringBuilder sb) {
        try {
            if (date.getDate() == null) {
                sb.append("Ng??y sinh ch??a nh???p").append("\n");
                return false;
            } else if (date.getDate().after(new Date())) {
                sb.append("Ng??y sinh kh??ng h???p l???").append("\n");
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            sb.append("Ng??y sinh kh??ng h???p l???").append("\n");
            return false;
        }
    }
    
    public static boolean checkHanSD(JDateChooser date, StringBuilder sb) {
        try {
            if (date.getDate() == null) {
                sb.append("H???n s??? d???ng ch??a nh???p").append("\n");
                return false;
            } else if (date.getDate().before(new Date())) {
                sb.append("H???n s??? d???ng kh??ng h???p l???").append("\n");
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            sb.append("H???n s??? d???ng kh??ng h???p l???").append("\n");
            return false;
        }
    }
    
    public static boolean checkNgayHetHan(Date date, StringBuilder sb) {
        try {
            if (date == null) {
                sb.append("Ng??y h???t h???n ch??a nh???p").append("\n");
                return false;
            } else if (date.before(new Date())) {
                sb.append("Ng??y h???t h???n kh??ng h???p l???").append("\n");
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            sb.append("Ng??y h???t h???n kh??ng h???p l???").append("\n");
            return false;
        }
    }

    public static boolean checkId(JTextField txt, String str, StringBuilder sb, String mes) {
        if (txt.getText().equalsIgnoreCase(str)) {
            sb.append(mes).append("\n");
            txt.setBackground(Color.red);
            return false;
        } else {
            txt.setBackground(Color.white);
            return true;
        }
    }

    public static boolean confirmPass(JPasswordField pass1, JPasswordField pass2, StringBuilder sb) {
        if (Arrays.equals(pass1.getPassword(), pass2.getPassword())) {
            pass2.setBackground(Color.white);
            return true;
        } else {
            pass2.setBackground(Color.red);
            sb.append(pass2.getName()).append(" kh??ng gi???ng v???i ").append(pass1.getName());
            return false;
        }
    }

    public static boolean checkEmail(JTextField txt, StringBuilder sb) {
        if (isNull(txt, "Email ch??a nh???p", sb)) {
            return false;
        }

        Pattern p = Pattern.compile("\\w+@\\w+\\.\\w+");
        Matcher m = p.matcher(txt.getText());

        if (!m.find()) {
            sb.append("Email kh??ng h???p l???\n");
            txt.setBackground(Color.red);
            return false;
        }

        txt.setBackground(Color.white);
        return true;
    }

    public static boolean checkMoney(JTextField txt, StringBuilder sb) {
        boolean ok = true;
        if (isNull(txt, txt.getName() + " ch??a nh???p", sb)) {
            return false;
        }

        try {
            double tien = XCurrency.toDouble(txt);
            if (tien <= 0) {
                sb.append(txt.getName()).append(" ph???i l???n h??n 0").append("\n");
                txt.setBackground(Color.red);
                ok = false;
            }
        } catch (NumberFormatException e) {
            sb.append(txt.getName()).append(" ph???i l?? s??? d????ng").append("\n");
            txt.setBackground(Color.red);
            ok = false;
        }

        if (ok) {
            txt.setBackground(Color.white);
        }
        return ok;
    }

    public static boolean checkDiem(String txt, StringBuilder sb) {
        boolean ok = true;
        if (txt.isEmpty()) {
            sb.append("??i???m ch??a nh???p").append("\n");
            return false;
        }

        try {
            double diem = Double.parseDouble(txt);
            if (diem < -1 || diem > 10) {
                sb.append("??i???m ph???i t??? 1 ?????n 10").append("\n");
                ok = false;
            }
        } catch (NumberFormatException e) {
            sb.append("??i???m ph???i l?? s???").append("\n");
            ok = false;
        }

        if (ok) {
        }

        return ok;
    }

    public static boolean checkSoNguyenDuong(Object obj, StringBuilder sb) {
        boolean ok = true;
        try {
            if (Integer.parseInt(obj.toString()) < 0) {
                sb.append("Gi?? tr??? ph???i l?? s??? nguy??n d????ng").append("\n");
                ok = false;
            }
        } catch (Exception e) {
            sb.append("Gi?? tr??? ph???i l?? s??? nguy??n d????ng").append("\n");
            ok = false;
        }
        return ok;
    }
}
