/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.Utils;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author DaiAustinYersin
 */
public class WebcamUtils {

    public static void chupAnh(String fileName) {
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());

        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setFPSDisplayed(true);
        panel.setDisplayDebugInfo(true);
        panel.setImageSizeDisplayed(true);
        panel.setMirrored(true);

        JButton btn = new JButton("Chụp ảnh");
        btn.setBackground(new java.awt.Color(255, 102, 255));
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ImageIO.write(webcam.getImage(), "JPG", new File("img/" + fileName + ".jpg"));
                    MsgBox.alert(null, "Chụp ảnh thành công");
                    webcam.close();
                } catch (IOException ex) {
                    MsgBox.alert(null, "Đường dẫn không hợp lệ");
                }
            }
        });
        
        JFrame frame = new JFrame("Webcam");
        frame.add(panel);
        frame.add(btn);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
