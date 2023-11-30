package Entrenamiento;


import java.io.File;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

public class DetectorAnotations {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		File carpetaImagen = new File("C:/Users/Alumno/Desktop/Carpeta Origen_Destino/Origen/pos");
		for (File foto : carpetaImagen.listFiles()) {
			if (!foto.isDirectory()) {
				Mat imageMat = Imgcodecs.imread(foto.getAbsolutePath());

				Imgcodecs.imwrite(foto.getAbsolutePath(), detectarCara(imageMat));
				detectarCoordenadas(imageMat);
			}

		}
	}

	public static Mat detectarCara(Mat imagen) {
		MatOfRect caras = new MatOfRect();

		// Convertir a escala de grises
		Mat frameGris = new Mat();
		Imgproc.cvtColor(imagen, frameGris, Imgproc.COLOR_BGR2GRAY);

		// Mejora de contraste para poder tener un mejor resultado
		Imgproc.equalizeHist(frameGris, frameGris);

		int altura = frameGris.height();
		int tamañoCara = 0;
		if (Math.round(altura * 0.2f) > 0) {
			tamañoCara = Math.round(altura * 0.2f);
		}

		// Deteccion de caras
		CascadeClassifier cascadaCara = new CascadeClassifier();

		// Cargar el XML de dataset de caras
		cascadaCara.load("Datasets/haarcascade_frontalface_alt2.xml");
		cascadaCara.detectMultiScale(frameGris, caras, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
				new Size(tamañoCara, tamañoCara), new Size());

		// Dibujar rectángulos rojos en la imagen original
		Rect[] caraArr = caras.toArray();
		for (int i = 0; i < caraArr.length; i++) {
			// Dibujar
			Imgproc.rectangle(imagen, caraArr[i].tl(), caraArr[i].br(), new Scalar(0, 0, 255), 5);
		}

		// Escribir a fichero
		final MatOfByte buffer = new MatOfByte();
		Imgcodecs.imencode(".jpg", imagen, buffer);

		Mat imageMat = Imgcodecs.imdecode(buffer, Imgcodecs.IMREAD_UNCHANGED);
		return imageMat;
	}

	public static int[] detectarCoordenadas(Mat imagen) {
		// Tamaño de la imagen
		int ancho = imagen.cols();
		int alto = imagen.rows();
		int[] coords = new int[4];

		int xInicial = -1;
		int yInicial = -1;
		int anchoRectangulo = 0;
		int altoRectangulo = 0;
		Scalar colorRojo = new Scalar(0, 0, 255); // BGR format for red in OpenCV

		for (int y = 0; y < alto; y++) {
			for (int x = 0; x < ancho; x++) {
				double[] pixelValue = imagen.get(y, x); // OpenCV uses row, column indexing
				Scalar colorPixel = new Scalar(pixelValue);

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
//
//		System.out.println("Rectángulo Rojo encontrado en coordenadas: (" + xInicial + ", " + yInicial + ")");
//		System.out.println("Ancho del rectángulo: " + anchoRectangulo);
//		System.out.println("Alto del rectángulo: " + altoRectangulo);

		return coords;
	}

	private static boolean esColorRojo(Scalar colorPixel, Scalar colorRojo) {
		double threshold = 50.0;

		double diffB = colorPixel.val[0] - colorRojo.val[0];
		double diffG = colorPixel.val[1] - colorRojo.val[1];
		double diffR = colorPixel.val[2] - colorRojo.val[2];

		double distance = Math.sqrt(diffB * diffB + diffG * diffG + diffR * diffR);

		return distance < threshold;
	}

}
