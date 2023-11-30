package Entrenamiento;
import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class Redimensionador {

    public static void main(String[] args) throws IOException {
    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    	File out = new File("C:/Users/alumno/Desktop/Carpeta Origen_Destino/Origen/pos");
        File carpeta = new File("C:/Users/Alumno/Desktop/Fotos");
        int c =0;
        for (File fi : carpeta.listFiles()) {
            BufferedImage originalImage = ImageIO.read(fi);
            BufferedImage resizedImage = resizeImage(originalImage, 300, 300);
            System.out.println(fi.getAbsolutePath() + " W: " + resizedImage.getWidth() + " H:" + resizedImage.getHeight());
            ImageIO.write(resizedImage, "jpg", new File(out + "/foto_res" + c+".jpg"));
			
            c++;
        }
        System.out.println("Redimensionamiento acabado");
    }
    public static Mat bufferedImageToMat(BufferedImage bi) {
    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        int type = CvType.CV_8UC4;
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), type);
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }
    public static RenderedImage byteArrayToRenderedImage(byte[] byteArray) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
        BufferedImage bufferedImage = ImageIO.read(bis);
        return bufferedImage;
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g.dispose();
        return resizedImage;
    }
}