/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.Utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

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

    public static void openAndInsertHinhAnh(String folder, JLabel lbl) {
        JFileChooser fileChooser = new JFileChooser("img/" + folder);
        fileChooser.setMultiSelectionEnabled(false);
        FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("Image", "jpg", "png");
        fileChooser.setFileFilter(fileNameExtensionFilter);
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            ImageUtils.save(folder, file);
            ImageIcon icon = ImageUtils.read(folder, file.getName());
            lbl.setIcon(resizeImg(icon, lbl));
            lbl.setToolTipText(file.getName());
        } else {
            MsgBox.alert(null, "Bạn chưa chọn ảnh");
        }
    }

    public static ImageIcon resizeImg(ImageIcon icon, JLabel lbl) {
//        ImageIcon icon = new ImageIcon("img/" + folder + "/" + imgName);
        Image image = icon.getImage();
        ImageIcon resizedImg = new ImageIcon(image.getScaledInstance(lbl.getWidth(), lbl.getHeight(), Image.SCALE_SMOOTH));
        return resizedImg;
    }

    public static void createBarCodeImage(String image_name, String myString) {
        try {

            Code128Bean code128 = new Code128Bean();
            code128.setHeight(15f);
            code128.setModuleWidth(0.3);
            code128.setQuietZone(10);
            code128.doQuietZone(true);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(baos, "image/x-png", 300, BufferedImage.TYPE_BYTE_BINARY, false, 0);
            code128.generateBarcode(canvas, myString);
            canvas.finish();

            //write to png file
            FileOutputStream fos = new FileOutputStream("img\\BarCode\\" + image_name + ".png");
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
