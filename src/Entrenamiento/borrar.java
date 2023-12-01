package Entrenamiento;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import OpenCV.DeteccionCara;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class borrar {

	public static void cambiarNombres(String carpetaOriginal, String carpetaDestino) {
		if (carpetaOriginal == null)
			return;
		File origen = new File(carpetaOriginal);
		File[] archivos = origen.listFiles();

		File destino = new File(carpetaDestino);
		if (!destino.exists()) {
			destino.mkdirs();
		}

		int c = 0;
		for (File archivo : archivos) {
			if (esArchivoDeImagen(archivo)) {
				archivo.renameTo(new File(archivo.getAbsolutePath().replace(archivo.getName(), "foto_" + c + ".jpg")));
				c++;
			}
		}

	}

	public static void escribirAnotation(String dir, int[] coords, String datos, String carpetaOriginal) {
		for (int i = 0; i < coords.length; i++) {
			if (coords[i] < 0) {
				return;
			}
		}
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(carpetaOriginal + "/pos.txt"), true);

			datos = dir + "  1  " + coords[0] + " " + coords[1] + " " + coords[2] + " " + coords[3] + "\n";
			fw.write(datos);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static boolean esArchivoDeImagen(File archivo) {
		String nombreArchivo = archivo.getName().toLowerCase();
		return nombreArchivo.endsWith(".jpg") || nombreArchivo.endsWith(".jpeg") || nombreArchivo.endsWith(".png")
				|| nombreArchivo.endsWith(".gif") || nombreArchivo.endsWith(".bmp") || nombreArchivo.endsWith(".tif")
				|| nombreArchivo.endsWith(".tiff");
	}

	public static void detectarRectangulos(String carpetaOriginal, String carpetaDestino, String datos) { // Fotos aqui
		JFrame frame = new JFrame("Cargando fotos...");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(425, 250, 450, 150);
		
		frame.setResizable(false);
		frame.setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("<html>Cargando fotos:<br><i>" + carpetaOriginal + "</i> </html>");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setForeground(new Color(2, 0, 255));
		titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		frame.add(titleLabel, BorderLayout.NORTH);

		JProgressBar progressBar = new JProgressBar();
		// progressBar.setIndeterminate(true);
		progressBar.setStringPainted(true);
		frame.add(progressBar, BorderLayout.CENTER);

		cambiarNombres(carpetaOriginal, carpetaDestino);

		File origen = new File(carpetaOriginal);
		File[] archivos = origen.listFiles();

		SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
			@Override
			protected Void doInBackground() throws Exception {

				int[] coords = new int[4];
				int i = 0;

				for (File archivo : archivos) {
					if (esArchivoDeImagen(archivo)) {

						// System.out.println(archivo);
						BufferedImage originalImage = ImageIO.read(archivo);

						Mat imageCara = Redimensionador.bufferedImageToMat(originalImage);
						byte[] bytesMat = DeteccionCara.detectarCara(imageCara);
						imageCara = Imgcodecs.imdecode(new MatOfByte(bytesMat), Imgcodecs.IMREAD_UNCHANGED);
						coords = DetectorAnotations.detectarCoordenadas(imageCara); // CON EL REC ROJO

						escribirAnotation(archivo.getName(), coords, datos, carpetaOriginal);
						// System.out.println(datos);
						i++;
						publish(i + 1);

					}
				}
				try {
					FileWriter fw = new FileWriter(new File(carpetaOriginal + "/pos.txt"), true);
					fw.write(datos);
					fw.close();

				} catch (IOException e) {

					e.printStackTrace();
				}
				frame.dispose(); // Dispose the frame when the progress reaches 100%
				// Open a new frame with text
				JFrame completionFrame = new JFrame("Carga completada");
				JLabel completionLabel = new JLabel("La carga ha sido completada exitosamente");
				completionLabel.setHorizontalAlignment(SwingConstants.CENTER);
				completionFrame.add(completionLabel);
				completionFrame.setSize(300, 100);
				completionFrame.setLocationRelativeTo(null);
				completionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				completionFrame.setVisible(true);
				completionFrame.setResizable(false);
				completionFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				
				return null;
			}

			@Override
			protected void process(java.util.List<Integer> chunks) {
				for (int value : chunks) {
					progressBar.setValue(value);
				}
			}
		};
		worker.execute();

		frame.setVisible(true);
	}

	public static void main(String[] args) {
		String datos = "";
		// "C:/Users/Alumno/Desktop/MuchasFotos/Pos"
		String carpetaOriginal = "C:/Users/Alumno/Desktop/MuchasFotos/Pos";
		String carpetaDestino = "C:/Users/Alumno/Desktop/Carpeta_Origen_Destino/Destino";
		detectarRectangulos(carpetaOriginal, carpetaDestino, datos);
	}
}
