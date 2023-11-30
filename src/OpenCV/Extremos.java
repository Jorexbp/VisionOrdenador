package OpenCV;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Extremos {

	public static byte[] detectarExtremos(Mat imagen) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Mat color = imagen;

		Mat frameGris = new Mat();
		Mat pintar = new Mat();
		Mat anchura = new Mat();

		Imgproc.cvtColor(color, frameGris, Imgproc.COLOR_BGR2GRAY);
		Imgproc.Canny(frameGris, anchura, 50, 150, 3, false);

		anchura.convertTo(pintar, CvType.CV_8U);
		// Imgcodecs.imwrite("Detecciones/Extremos/Extremo1.jpg", pintar);
		
		final MatOfByte buffer = new MatOfByte();
		Imgcodecs.imencode(".jpg", pintar, buffer);

		byte[] datosImagen = buffer.toArray();
		
		return datosImagen;
	}
	public static void main(String[] args) {
		
		

	}

}
