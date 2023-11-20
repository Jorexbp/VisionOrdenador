package OpenCV;

import javax.swing.ImageIcon;

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

public class DeteccionCara {

	public static byte[] detectarYGuardar(Mat imagen) {
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
		cascadaCara.load("Datasets/haarcascade_frontalface_alt2.xml");
		cascadaCara.detectMultiScale(frameGris, caras, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
				new Size(tamañoCara, tamañoCara), new Size());

		// Escribir a fichero
		Rect[] caraArr = caras.toArray();
		for (int i = 0; i < caraArr.length; i++) {
			// Dibujar
			Imgproc.rectangle(imagen, caraArr[i], new Scalar(0, 0, 255), 3);

		}

		Imgcodecs.imwrite("detecciones/deteccion.jpg", imagen);

		final MatOfByte buffer = new MatOfByte();
		Imgcodecs.imencode(".jpg", imagen, buffer);

		byte[] datosImagen = buffer.toArray();

		return datosImagen;
	}

	public static void main(String[] args) {
		// Cargo la pinche libreria
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		// Lea la imagen de la carpeta
		Mat imagen = Imgcodecs.imread("imagenes/ejemplo2.jpg");

		//
		detectarYGuardar(imagen);
	}
}
