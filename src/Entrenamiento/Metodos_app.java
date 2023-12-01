package Entrenamiento;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import OpenCV.DeteccionCara;

public class Metodos_app {

	public static void cambiarAUsable(JLabel lcargafotospos2, JButton befotospos2) {
		lcargafotospos2.setEnabled(true);
		befotospos2.setEnabled(true);
	}

	public static boolean esArchivoDeImagen(File archivo) {
		String nombreArchivo = archivo.getName().toLowerCase();
		return nombreArchivo.endsWith(".jpg") || nombreArchivo.endsWith(".jpeg") || nombreArchivo.endsWith(".png")
				|| nombreArchivo.endsWith(".gif") || nombreArchivo.endsWith(".bmp") || nombreArchivo.endsWith(".tif")
				|| nombreArchivo.endsWith(".tiff");
	}

	public static boolean avisarBorrado(String carpeta) {
		File carpetaNueva = new File(carpeta);
		if (carpetaNueva.exists()) {
			return JOptionPane.showConfirmDialog(null,
					"¿Quiere sobreescribir la carpeta?\n" + carpeta) == JOptionPane.OK_OPTION;

		}
		return true;
	}

	public static void reiniciarCarpetas(String carpeta, File carpetaPadre) {

		if (!carpetaPadre.exists()) {
			carpetaPadre.mkdir();
		} else {
			for (File f : carpetaPadre.listFiles()) {
				if (!f.isDirectory()) {
					f.delete();
				}
			}
		}
		File casc = new File(carpetaPadre + "/cascade");

		if (!casc.exists()) {
			casc.mkdir();
		} else {
			for (File f : casc.listFiles()) {
				if (!f.isDirectory()) {
					f.delete();
				}
			}
		}
		File directory = new File(carpeta);
		if (!directory.exists()) {
			directory.mkdir();
		} else {
			for (File files : directory.listFiles()) {
				files.delete();
			}
		}

	}

	public static void carpetaCrearTipo(String dir, String tipo) {
		File carpetaSubHijo;

		carpetaSubHijo = new File(dir + "\\" + tipo);
		if (!carpetaSubHijo.exists()) {
			carpetaSubHijo.mkdir();
		} else {
			for (File files : carpetaSubHijo.listFiles()) {
				files.delete();
			}
		}

	}

	public static String[] crearCarpetasPorDefecto(String nombreCarpetaOrigen, String nombreCarpetaDestino,
			String escritorioUsuario, String carpetaOrigen, String carpetaDestino, File carpetaPadre) {
		if (nombreCarpetaOrigen == null || nombreCarpetaDestino == null)
			return null;

		nombreCarpetaOrigen = escritorioUsuario + "\\Carpeta_Origen_Destino\\" + nombreCarpetaOrigen;
		nombreCarpetaDestino = escritorioUsuario + "\\Carpeta_Origen_Destino\\" + nombreCarpetaDestino;

		if (avisarBorrado(nombreCarpetaOrigen))
			reiniciarCarpetas(nombreCarpetaOrigen, carpetaPadre);

		if (avisarBorrado(nombreCarpetaDestino))
			reiniciarCarpetas(nombreCarpetaDestino, carpetaPadre);

		carpetaOrigen = nombreCarpetaOrigen;
		carpetaDestino = nombreCarpetaDestino;

		carpetaCrearTipo(carpetaOrigen, "pos");
		carpetaCrearTipo(carpetaOrigen, "neg");

		return new String[] { nombreCarpetaOrigen, nombreCarpetaDestino };
	}

	public static String seleccionarCarpeta(int JFileOpcion) {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileOpcion);
		String carpeta = "";
		if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File ficheroCarpeta = jfc.getSelectedFile();
			carpeta = ficheroCarpeta.getAbsolutePath();

		} else {
			return null;
		}
		return carpeta;

	}

	public static void crearAnotacionNegativa(String carpetaOriginalNegativa, javax.swing.JLabel lcrearsample,
			javax.swing.JButton bcrearsample) {
		File origen = new File(carpetaOriginalNegativa);

		String datos = "";

		for (File fichero : origen.listFiles()) {
			if (esArchivoDeImagen(fichero)) {
				datos += fichero.getAbsolutePath() + "\n";
			}

		}

		try {
			FileWriter fw = new FileWriter(carpetaOriginalNegativa + "/neg.txt");
			fw.write(datos);
			fw.close();

			App_Entrenamiento.rellenarTextArea();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

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

	public static void crearSamples(String carpetaOriginalPositiva, String carpetaDestino, JLabel lcrearXML,
			JButton bcrearXML) {
		String addrSample = "lib\\samples\\opencv_createsamples.exe";
		String posTxt = carpetaOriginalPositiva + "\\pos.txt";
		String posVec = carpetaDestino + "\\pos.vec";
		String nSamples = Integer.toString(100);

		String cmd = "cmd /c start cmd.exe /k ";
		String comandoSamples = cmd + addrSample + " -info \"" + posTxt + "\" -w 24 -h 24 -num \"" + nSamples
				+ "\" -vec \"" + posVec + "\"";
		try {
			Runtime.getRuntime().exec(comandoSamples);
			App_Entrenamiento.rellenarTextArea();
			cambiarAUsable(lcrearXML, bcrearXML);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void crearXML(String carpetaPadre, String carpetaOriginalNegativa) {

		int nStages = 10;
		int numPos = 20;
		int numNeg = 15;
		String destino = carpetaPadre + "/cascade";

		String dirVec = carpetaPadre + "\\Destino\\pos.vec";

		String dirTxtNeg = carpetaOriginalNegativa + "\\neg.txt";
		String dirExe = "lib\\traincascade\\opencv_traincascade.exe";
		String comTR = dirExe + " -data " + destino + " -vec " + dirVec + " -bg " + dirTxtNeg + " -w 24 -h 24 -numPos "
				+ numPos + " -numNeg " + numNeg + " -numStages " + nStages;

		String cmd = "cmd /c start cmd.exe /k ";
		String comandoSamples = cmd + comTR;
		try {
			Runtime.getRuntime().exec(comandoSamples);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
