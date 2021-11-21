/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.GUI;

import com.milkyway.Utils.*;
import com.milkyway.Model.*;
import java.security.SecureRandom;

/**
 *
 * @author hoang
 */
public class RandomMail {
 static final String AB = "0123456789ASDFGHJKLZXCVBNMQWERTYUIOPasdfghjklzxcvbnmqwertyuiop"; 
 static SecureRandom rdn = new SecureRandom();
 String randomString(int i){
     StringBuilder sb = new StringBuilder(i);
     for (int j = 0; j < i; j++) {
         sb.append(AB.charAt(rdn.nextInt(AB.length())));
     }
     return sb.toString();
 }
}
