/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.GUI;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 *
 * @author van95
 */
public class FIlePDF {

    public static void main(String[] args) {
        // Tạo đối tượng tài liệu
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);

        try {

            // Tạo đối tượng PdfWriter
            PdfWriter.getInstance(document, new FileOutputStream("PDF\\milkyway.pdf"));

            // Mở file để thực hiện ghi
            document.open();

            // Thêm nội dung sử dụng add function
//            document.add(new Paragraph("MilkyWay"));
//
//            Anchor anchorTarget = new Anchor("Thống kê thu - chi một ngày");
//            anchorTarget.setName("BackToTop");
//            document.add(anchorTarget);

            // Đóng File
            Paragraph title1 = new Paragraph("Chapter 1",
                    FontFactory.getFont(FontFactory.HELVETICA, 18, new CMYKColor(0, 255, 255, 17)));

            Chapter chapter1 = new Chapter(title1, 1);
            document.add(title1);
            document.add(chapter1);

            chapter1.setNumberDepth(0);
            Section section1 = chapter1.addSection(title1);

            Paragraph someSectionText = new Paragraph("This text comes as part of section 1 of chapter 1.");

            section1.add(someSectionText);

            someSectionText = new Paragraph("Following is a 3 X 2 table.");

            section1.add(someSectionText);
            document.add(section1);
//            Phrase phrase = new Phrase();
//            for (int i = 0; i < 100; i++) {
//                Chunk chunk = new Chunk("Viblo");
//                phrase.add(chunk);
//            }
//            document.add(phrase);
            PdfPTable t = new PdfPTable(3);
            t.setSpacingBefore(25);
            t.setSpacingAfter(25);

            PdfPCell c1 = new PdfPCell(new Phrase("Header1"));
            t.addCell(c1);
            PdfPCell c2 = new PdfPCell(new Phrase("Header2"));
            t.addCell(c2);
            PdfPCell c3 = new PdfPCell(new Phrase("Header3"));
            t.addCell(c3);

            t.addCell("1.1");
            t.addCell("1.2");
            t.addCell("1.3");

            document.add(t);
            document.close();
            System.out.println("Write file succes!");
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }
}
