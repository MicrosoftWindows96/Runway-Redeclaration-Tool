package org.universityofsouthampton.runwayredeclarationtool.utility;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;

public class ExportImagePDF {

    public static BufferedImage createImage(JPanel panel) {
        int w = panel.getWidth();
        int h = panel.getHeight();
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        panel.paint(g);
        g.dispose();
        return bi;
    }

    public static void writeImageToPDF(BufferedImage bi, String path) throws Exception {
        ByteArrayOutputStream base = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", base);
        ImageData data = ImageDataFactory.create(base.toByteArray());

        PdfWriter writer = new PdfWriter(path);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(20, 20, 20, 20); // Top, right, bottom, and left margins


        Image img = new Image(data);
        img.setAutoScale(true);
        document.add(img);
        document.close();
    }



}
