package Entrenamiento;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class DetectorAnotations {
	public static void main(String[] args) {
		try {
			// Carga la imagen desde el archivo

			File carpetaImagen = new File("C:/Users/Alumno/Desktop/Carpeta Origen_Destino/Origen/pos");
			for (File foto : carpetaImagen.listFiles()) {
				if (!foto.isDirectory() && foto.getAbsolutePath().contains("_a")) {
					BufferedImage imagen = ImageIO.read(foto);

					detectarCoordenadas(imagen);
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int[] detectarCoordenadas(BufferedImage imagen) {
		// Tamaño de la imagen
		int ancho = imagen.getWidth();
		int alto = imagen.getHeight();
		int[] coords = new int[4];

		int xInicial = -1;
		int yInicial = -1;
		int anchoRectangulo = 0;
		int altoRectangulo = 0;
		Color colorRojo = new Color(255, 0, 0);

		for (int y = 0; y < alto; y++) {
			for (int x = 0; x < ancho; x++) {
				Color colorPixel = new Color(imagen.getRGB(x, y));

				if (esColorRojo(colorPixel, colorRojo)) {

					if (xInicial == -1) {
						xInicial = x;
						yInicial = y;
					}

					anchoRectangulo = x - xInicial + 1;
					altoRectangulo = y - yInicial + 1;
				}
			}

		}
		coords[0] = xInicial;
		coords[1] = yInicial;
		coords[2] = anchoRectangulo;
		coords[3] = altoRectangulo;
		System.out.println("Rectángulo Rojo encontrado en coordenadas: (" + xInicial + ", " + yInicial + ")");
		System.out.println("Ancho del rectángulo: " + anchoRectangulo);
		System.out.println("Alto del rectángulo: " + altoRectangulo);
		return coords;
	}

	private static boolean esColorRojo(Color colorPixel, Color colorRojo) {
		int umbral = 30;
		return Math.abs(colorPixel.getRed() - colorRojo.getRed()) < umbral
				&& Math.abs(colorPixel.getGreen() - colorRojo.getGreen()) < umbral
				&& Math.abs(colorPixel.getBlue() - colorRojo.getBlue()) < umbral;
	}
}
