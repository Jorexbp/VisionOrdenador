package Entrenamiento;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Redimensionador {

	public static void main(String[] args) {
		String inputFolderPath = "C:/Users/Alumno/Desktop/Samples personas/RazaBlanca";
		String outputFolderPath = "C:/Users/Alumno/Desktop/Carpeta Origen_Destino/Destino";
		resizePhotos(inputFolderPath, outputFolderPath, 300, 300);
		System.out.println("Redimensionamiento acabado");
	}

	public static void resizePhotos(String inputFolderPath, String outputFolderPath, int targetWidth,
			int targetHeight) {
		File inputFolder = new File(inputFolderPath);
		File outputFolder = new File(outputFolderPath);

		if (!outputFolder.exists()) {
			outputFolder.mkdirs();
		}

		File[] imageFiles = inputFolder
				.listFiles((dir, name) -> name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpeg"));

		if (imageFiles != null) {
			for (File imageFile : imageFiles) {
				try {
					BufferedImage originalImage = ImageIO.read(imageFile);
					BufferedImage resizedImage = resizeImage(originalImage, targetWidth, targetHeight);

					File outputFile = new File(outputFolder, imageFile.getName());
					ImageIO.write(resizedImage, "png", outputFile);
					// Desktop.getDesktop().open(outputFile);
					// System.out.println("Imagen redimensionada: " + outputFile.getAbsolutePath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
		Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
		BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = outputImage.createGraphics();
		g2d.drawImage(resultingImage, 0, 0, null);
		g2d.dispose();

		return outputImage;
	}
}
