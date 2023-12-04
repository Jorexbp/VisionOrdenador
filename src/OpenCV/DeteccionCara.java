package OpenCV;

import javax.swing.ImageIcon;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

public class DeteccionCara {
	private static String modelo;

	public static void setModelo(String mod) {
		modelo = mod;
	}

	public static String getModelo() {
		return modelo;
	}

	public static byte[] detectarCara(Mat imagen) {
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
		cascadaCara.load("C:\\Users\\Alumno\\git\\VisionOrdenador\\Vison\\resources\\haarcascade_frontalface_alt2.xml");

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

		byte[] datosImagen = buffer.toArray();

		return datosImagen;
	}

	public static byte[] detectarCara(Mat imagen, int c, String dirCar) {
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
		cascadaCara.load("C:\\Users\\Alumno\\git\\VisionOrdenador\\Vison\\resources\\haarcascade_frontalface_alt2.xml");
		cascadaCara.detectMultiScale(frameGris, caras, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
				new Size(tamañoCara, tamañoCara), new Size());

		// Dibujar rectángulos rojos en la imagen original
		Rect[] caraArr = caras.toArray();
		for (int i = 0; i < caraArr.length; i++) {
			// Dibujar
			Imgproc.rectangle(imagen, caraArr[i].tl(), caraArr[i].br(), new Scalar(0, 0, 255), 5);

			Imgcodecs.imwrite(dirCar + "/f_" + c + ".jpg", imagen);
		}

		// Escribir a fichero
		final MatOfByte buffer = new MatOfByte();
		Imgcodecs.imencode(".jpg", imagen, buffer);

		byte[] datosImagen = buffer.toArray();

		return datosImagen;
	}

	public static byte[] mostrarInfoCara(Mat imagen) {
		MatOfRect caras = new MatOfRect();
		@SuppressWarnings("unused")
		ImageIcon icono;

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
		if (modelo == null) {
			// URL url =
			// DeteccionCara.class.getResource("/haarcascade_frontalface_alt2.xml");

			cascadaCara.load(
					"C:\\Users\\Alumno\\git\\VisionOrdenador\\Vison\\resources\\haarcascade_frontalface_alt2.xml");

		} else {

			cascadaCara.load(modelo);

		}
		cascadaCara.detectMultiScale(frameGris, caras, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
				new Size(tamañoCara, tamañoCara), new Size());

		// Escribir a fichero
		Rect[] caraArr = caras.toArray();
		int numeroCaras = 0;
		calcularDistancia cD = new calcularDistancia();
		for (int i = 0; i < caraArr.length; i++) {
			// Dibujar
			Imgproc.rectangle(imagen, caraArr[i], new Scalar(0, 0, 255), 3);

			// Texto al lado del rectangulo
			String label = "Cara";
			String labelDist = "Dist: " + cD.getDistancia();
			numeroCaras++;
			// Posicion debajo del rectangulo
			Point puntoTexto = new Point(caraArr[i].tl().x, caraArr[i].br().y + 30);
			Point puntoDist = new Point(caraArr[i].tl().x, caraArr[i].br().y + 60);

			// Dibujar el texto se puede personalizar mucho pero no se el fontFamily como
			Imgproc.putText(imagen, label, puntoTexto, 2, 1.1, new Scalar(255, 255, 255), 2);
			Imgproc.putText(imagen, labelDist, puntoDist, 2, 1.1, new Scalar(255, 255, 255), 2);

			// getDistancia
		}
		String nCaras = "Caras encontradas: " + Integer.toString(numeroCaras);
		Point puntoTexto = new Point(20, 50);
		Imgproc.putText(imagen, nCaras, puntoTexto, 2, 1.1, new Scalar(0, 255, 255), 2);

		final MatOfByte buffer = new MatOfByte();
		Imgcodecs.imencode(".jpg", imagen, buffer);

		byte[] datosImagen = buffer.toArray();

		return datosImagen;
	}

	public static void guardarImagen(Mat imagen, String dir) {
		Imgcodecs.imwrite(dir, imagen);
	}

	public static byte[] detecarSoloCara(Mat imagen) {
		MatOfRect caras = new MatOfRect();

		// Convertir a escala de grises
		Mat frameGris = new Mat();
		Imgproc.cvtColor(imagen, frameGris, Imgproc.COLOR_BGR2GRAY);

		// Mejora de contraste para obtener un mejor resultado
		Imgproc.equalizeHist(frameGris, frameGris);

		int altura = frameGris.height();
		int tamañoCara = Math.round(altura * 0.2f);

		CascadeClassifier cascadaCara = new CascadeClassifier();

		// Cargar el XML de dataset de caras
		cascadaCara.load("Datasets/haarcascade_frontalface_alt2.xml");
		cascadaCara.detectMultiScale(frameGris, caras, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
				new Size(tamañoCara, tamañoCara), new Size());

		Rect[] caraArr = caras.toArray();
		final MatOfByte buffer = new MatOfByte();

		for (Rect caraRect : caraArr) {

			Mat imagenCara = new Mat(imagen, caraRect);

			Imgcodecs.imencode(".jpg", imagenCara, buffer);

		}

		return buffer.toArray();
	}

	public static void main(String[] args) {
		// Cargo la pinche libreria
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		// Lea la imagen de la carpeta
		Mat imagen = Imgcodecs.imread("imagenes/ejemplo2.jpg");

		detectarCara(imagen);
	}
}
