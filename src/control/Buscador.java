package control;

import javax.swing.ImageIcon;

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

import jssc.SerialPortException;

public class Buscador {
	private static String modelo;
	private static boolean coincidencia = false;
	private static ControlServo servo = new ControlServo();
	private boolean existeServo;
	private static Thread hiloBuscar = new Thread();

	public static boolean hayCoincidencia() {
		return coincidencia;
	}

	public static void setModelo(String mod) {
		modelo = mod;
	}

	public static String getModelo() {
		return modelo;
	}

	public byte[] mostrarInfoCara(Mat imagen) {
		MatOfRect caras = new MatOfRect();
		@SuppressWarnings("unused")
		ImageIcon icono;

		Mat frameGris = new Mat();
		Imgproc.cvtColor(imagen, frameGris, Imgproc.COLOR_BGR2GRAY);

		Imgproc.equalizeHist(frameGris, frameGris);

		int altura = frameGris.height();
		int tamañoCara = 0;
		if (Math.round(altura * 0.2f) > 0) {
			tamañoCara = Math.round(altura * 0.2f);
		}

		CascadeClassifier cascadaCara = new CascadeClassifier();

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

		Rect[] caraArr = caras.toArray();
		int numeroCaras = 0;
		for (int i = 0; i < caraArr.length; i++) {
			Imgproc.rectangle(imagen, caraArr[i], new Scalar(0, 0, 255), 3);

			String label = "Cara";
			numeroCaras++;

			Point puntoTexto = new Point(caraArr[i].tl().x, caraArr[i].br().y + 30);

			Imgproc.putText(imagen, label, puntoTexto, 2, 1.1, new Scalar(255, 255, 255), 2);

		}

		if (!existeServo && numeroCaras <= 0) {
			coincidencia = false;
			existeServo = true;

			hiloBuscar = new Thread(new Runnable() {
				@Override
				public void run() {
					servo.buscar();
				}
			});
			hiloBuscar.start();

		}

//		if (existeServo && numeroCaras <= 0) {
//			coincidencia = false;
//
//			hiloBuscar = new Thread(new Runnable() {
//				@Override
//				public void run() {
//					servo.buscar();
//				}
//			});
//			hiloBuscar.start();
//		} else

		if (numeroCaras > 0) {
			hiloBuscar.interrupt();
			try {
				servo.parar();
			} catch (SerialPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			coincidencia = true;
			existeServo = false;
		} else {
			coincidencia = false;
			hiloBuscar = new Thread(new Runnable() {
				@Override
				public void run() {
					servo.buscar();
				}
			});
			hiloBuscar.start();
			existeServo = true;
		}

		String nCaras = "Coincidenias encontradas: " + Integer.toString(numeroCaras);
		Point puntoTexto = new Point(20, 50);
		Imgproc.putText(imagen, nCaras, puntoTexto, 2, 1.1, new Scalar(0, 255, 255), 2);

		final MatOfByte buffer = new MatOfByte();
		Imgcodecs.imencode(".jpg", imagen, buffer);

		byte[] datosImagen = buffer.toArray();

		return datosImagen;
	}

}