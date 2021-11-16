/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.Utils;

import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

/**
 *
 * @author DaiAustinYersin
 */
public class ImageUtils {

    public static Image getAppIcon() {
        URL url = ImageUtils.class.getResource("/com/milkyway/Icons/logo.png");
        return new ImageIcon(url).getImage();
    }

    public static void save(String folder, File src) {
        File dst = new File("img/" + folder, src.getName());
        if (!dst.getParentFile().exists()) {
            dst.getParentFile().mkdirs();
        }
        try {
            Path from = Paths.get(src.getAbsolutePath());
            Path to = Paths.get(dst.getAbsolutePath());
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static ImageIcon read(String folder, String fileName) {
        File path = new File("img/" + folder, fileName);
        return new ImageIcon(path.getAbsolutePath());
    }
    
    public static void insertHinhAnh(String folder, JLabel lbl) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            ImageUtils.save(folder, file);
            ImageIcon icon = ImageUtils.read(folder, file.getName());
            lbl.setIcon(icon);
            lbl.setToolTipText(file.getName());
        }
    }
}
