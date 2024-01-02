package Entrenamiento;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;


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
	public static String carpetaPos;
	public static String carpetaNeg;
	private static ReentrantLock bloqueo = new ReentrantLock();
	private static Condition condicion = bloqueo.newCondition();

	public static void setCarpetaPositiva(String carpetaOriginalPositiva) {
		carpetaPos = carpetaOriginalPositiva;
	}

	public static void setCarpetaNegativa(String carpetaOriginalNegativa) {
		carpetaNeg = carpetaOriginalNegativa;
	}

	public static void cambiarAUsable(javax.swing.JComponent lcargafotospos2, javax.swing.JComponent befotospos2) {
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

	public static void reiniciarCarpetas(String carpeta) {

		File directory = new File(carpeta);
		if (!directory.exists()) {
			directory.mkdir();
		} else {
			for (File files : directory.listFiles()) {
				files.delete();
			}
		}
		File modelos = new File(carpeta + "/modelos");

		if (!modelos.exists()) {
			modelos.mkdir();
		}

	}

	public static String crearCarpetasPorDefecto(String nombreCarpetaDestino) {
		if (nombreCarpetaDestino == null)
			return null;

		nombreCarpetaDestino = System.getProperty("user.home") + "\\Desktop\\" + nombreCarpetaDestino;

		if (avisarBorrado(nombreCarpetaDestino))
			reiniciarCarpetas(nombreCarpetaDestino);

		return nombreCarpetaDestino;
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

	public static void crearAnotacionNegativa(String carpetaOriginalNegativa) {
		File origen = new File(carpetaOriginalNegativa);

		String datos = "";
		carpetaNeg = carpetaOriginalNegativa;
		for (File fichero : origen.listFiles()) {
			if (esArchivoDeImagen(fichero)) {
				datos += fichero.getAbsolutePath() + "\n";
			}

		}

		try {
			if (new File(carpetaOriginalNegativa + "/neg.txt").exists()) {
				new File(carpetaOriginalNegativa + "/neg.txt").delete();
			}
			FileWriter fw = new FileWriter(carpetaOriginalNegativa + "/neg.txt");
			fw.write(datos);
			fw.close();

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

	public static void detectarRectangulos(String carpetaOriginal, String carpetaDestino) { // Fotos aqui

		JFrame frame = new JFrame("Cargando fotos...");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBounds(425, 250, 450, 150);
		frame.setUndecorated(true);
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

		if (new File(carpetaOriginal + "\\pos.txt").exists()) {
			new File(carpetaOriginal + "\\pos.txt").delete();

		}
		File origen = new File(carpetaOriginal);
		File[] archivos = origen.listFiles();

		SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
			@Override
			protected Void doInBackground() throws Exception {

				int[] coords = new int[4];
				int i = 0;

				for (File archivo : archivos) {
					if (esArchivoDeImagen(archivo)) {

						BufferedImage originalImage = ImageIO.read(archivo);

						Mat imageCara = Redimensionador.bufferedImageToMat(originalImage);
						byte[] bytesMat = DeteccionCara.detectarCara(imageCara);

						imageCara = Imgcodecs.imdecode(new MatOfByte(bytesMat), Imgcodecs.IMREAD_UNCHANGED);
						coords = DetectorAnotations.detectarCoordenadas(imageCara); // CON EL REC ROJO

						escribirAnotation(archivo.getName(), coords, carpetaOriginal);
						frame.toFront();

						i++;
						publish(i);

					}
				}

				frame.dispose();
				JFrame completionFrame = new JFrame("Carga completada");
				JLabel completionLabel = new JLabel("La carga ha sido completada exitosamente");
				completionLabel.setHorizontalAlignment(SwingConstants.CENTER);
				completionFrame.add(completionLabel);
				completionFrame.setSize(300, 100);
				completionFrame.setLocationRelativeTo(null);
				completionFrame.setVisible(true);
				completionFrame.setResizable(false);
				completionFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				Thread.sleep(2500);
				completionFrame.dispose();
				return null;
			}

			@Override
			protected void process(java.util.List<Integer> chunks) {
				for (int value : chunks) {
					progressBar.setValue(value);
				}
			}
		};
//		if (new File(carpetaOriginal).exists()) {
//			new File(carpetaOriginal).delete();
//		}

		worker.execute();

		frame.setVisible(true);

	}

	public static void crearPositivos(String carpetaOriginal, String carpetaDestino) {
		if (new File(carpetaOriginal + "\\pos.txt").exists()) {
			new File(carpetaOriginal + "\\pos.txt").delete();

		}
		File origen = new File(carpetaOriginal);
		File[] archivos = origen.listFiles();

		int[] coords = new int[4];

		for (File archivo : archivos) {
			if (esArchivoDeImagen(archivo)) {

				BufferedImage originalImage;
				try {
					originalImage = ImageIO.read(archivo);
					Mat imageCara = Redimensionador.bufferedImageToMat(originalImage);
					byte[] bytesMat = DeteccionCara.detectarCara(imageCara);

					imageCara = Imgcodecs.imdecode(new MatOfByte(bytesMat), Imgcodecs.IMREAD_UNCHANGED);
					coords = DetectorAnotations.detectarCoordenadas(imageCara); // CON EL REC ROJO

					escribirAnotation(archivo.getName(), coords, carpetaOriginal);

				} catch (IOException e) {
					
					e.printStackTrace();
				}

			}
		}
	}

	public static void escribirAnotation(String dir, int[] coords, String carpetaOriginal) {
		for (int i = 0; i < coords.length; i++) {
			if (coords[i] < 0) {
				return;
			}
		}
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(carpetaOriginal + "\\pos.txt"), true);

			String datos = dir + "  1  " + coords[0] + " " + coords[1] + " " + coords[2] + " " + coords[3] + "\n";
			System.out.println(datos);
			fw.write(datos);
			fw.close();
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

	public static void crearSamples(String carpetaOriginalPositiva, String carpetaDestino, String subTXT, int iter) {

		bloqueo.lock();
		String addrSample = "lib\\samples\\opencv_createsamples.exe";
		String posTxt = carpetaOriginalPositiva + "\\" + subTXT;

		String posVec = carpetaDestino + "\\pos.vec";
		int numeroSamples = calcularNumSamples(carpetaOriginalPositiva);
		String nSamples = Integer.toString(numeroSamples * 10);
		if (new File(posVec).exists()) {
			new File(posVec).delete();
		}

		String cmd = "cmd /c start cmd.exe /k ";
		String comandoSamples = cmd + addrSample + " -info \"" + posTxt + "\" -w 24 -h 24 -num \"" + nSamples
				+ "\" -vec \"" + posVec + "\"";
		try {
			
			Process procesoSamples = Runtime.getRuntime().exec(comandoSamples);
			while (new File(posVec).exists() && iter != 1) {
				condicion.await();
			}

			procesoSamples.waitFor();

			condicion.signal();
			bloqueo.unlock();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static int calcularNumSamples(String carpetaOriginalPositiva) {
		File carpeta = new File(carpetaOriginalPositiva);
		carpetaPos = carpetaOriginalPositiva;
		File[] archivos = carpeta.listFiles();

		return archivos.length;
	}

	public static String crearXML(String carpetaPadre, String carpetaOriginalNegativa) {
		bloqueo.lock();

		int nStages = calcularNumSamples(carpetaPos) / 13;
		int numPos = (int) (calcularNumSamples(carpetaPos) / 1.5);
		int numNeg = calcularNumSamples(carpetaNeg);
		String destino = carpetaPadre;

		String dirVec = carpetaPadre + "/pos.vec";

		String dirTxtNeg = carpetaOriginalNegativa + "\\neg.txt";
		String dirExe = "lib\\traincascade\\opencv_traincascade.exe";
		String comTR = dirExe + " -data " + destino + " -vec " + dirVec + " -bg " + dirTxtNeg + " -w 24 -h 24 -numPos "
				+ numPos + " -numNeg " + numNeg + " -numStages " + nStages + " -mode ALL";

		String cmd = "cmd /c start cmd.exe /c ";
		String comandoSamples = cmd + comTR;

		try {

			for (File fic : new File(destino).listFiles()) {
				while ((fic.getAbsolutePath().contains("stage") && !fic.isDirectory())
						|| fic.getAbsolutePath().contains("params")) {
					Thread.sleep(500);
					condicion.await();
				}

			}
			Thread.sleep(1500);
			Runtime.getRuntime().exec(comandoSamples);
			while (!new File(destino + "/cascade.xml").exists()) {
				Thread.sleep(500);
			}

			if (new File(destino + "/modelos/cascade.xml").exists())
				new File(destino + "/modelos/cascade.xml").delete();

			Path pathOrigen = Paths.get(destino + "/cascade.xml");
			Path pathDestino = Paths.get(destino + "/modelos/cascade.xml");
			Files.move(pathOrigen, pathDestino);

			for (File fic : new File(destino).listFiles()) {
				if (!fic.isDirectory()) {
					fic.delete();
				}

			}

			condicion.signal();
			bloqueo.unlock();

			return destino + "/modelos/cascade.xml";

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String copiarFichero(File archivoOrigen, String dirDestino) {

		Path source = Paths.get(archivoOrigen.toString());
		Path destination = Paths.get(dirDestino);

		try {
			Files.copy(source, destination.resolve(source.getFileName()));
			return destination.toString() + "\\" + archivoOrigen.getName();
		} catch (IOException e) {
			System.err.println("Ha ocurrido un error al copiar el archivo: " + e.getMessage());
			return null;
		}
	}

	public static boolean crearAnotaciones(String carpetaFotos, String carpetaDestino, String nombreFicheroTXT) {

		JOptionPane.showMessageDialog(null,
				"Para identificar el objeto en las fotos pinche en la esquina superior izquierda del objeto y mueva\n el ratón hasta la esquina inferior derecha, para confirmar la selección pulse c, para pasar a la\n siguiente foto pulse n, para eliminar una selección insatisfactoria sin confirmar comience\n otra selección y si está confirmado pulse d.");

		String ejecutableAnotacion = "lib\\annotation\\opencv_annotation.exe";

		String comandoAnotacion = ejecutableAnotacion + " --annotations=" + carpetaFotos + "\\" + nombreFicheroTXT
				+ " --images=" + carpetaFotos;

		try {

			Process pCrearAnotaciones = Runtime.getRuntime().exec(comandoAnotacion);
			while (pCrearAnotaciones.waitFor() != 0) {

			}

			return true;
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		return false;
	}

	private static void quitarDireccionAbsoluta(String dir) {

		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(dir));
			String line;
			String datos = "";
			while ((line = br.readLine()) != null) {
				datos += line.substring(line.lastIndexOf("\\") + 1) + "\n";
			}
			br.close();

			File del = new File(dir);
			del.delete();

			FileWriter fw = new FileWriter(dir);
			fw.write(datos);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void reciclarFotosDenegadas(String carpetaPadre, String carpetaOriginal) {

		String nombreSubCarpeta = carpetaOriginal + "\\denegadas";

		moverImagenesDesdeArchivo(nombreSubCarpeta + "\\fotos_denegadas.txt", nombreSubCarpeta);
		crearAnotaciones(nombreSubCarpeta, carpetaPadre, "fotos_denegadas.txt");

		unirFicherosTXT(nombreSubCarpeta, "fotos_denegadas.txt", "fotos_confirmadas.txt");

		moverContenido(nombreSubCarpeta, carpetaOriginal);

		quitarDireccionAbsoluta(carpetaOriginal + "\\pos.txt");

	}

	public static String crearSubCarpeta(String carpetaOriginal, String string) {
		File subCarpeta = new File(carpetaOriginal + "\\" + string);
		if (subCarpeta.exists())
			subCarpeta.delete();
		else
			subCarpeta.mkdir();

		return carpetaOriginal + "\\" + string;

	}

	public static void unirFicherosTXT(String carpetaOriginal, String... ficheros) {

		File archivoSalida = new File(carpetaOriginal + "\\pos.txt");

		try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivoSalida))) {

			for (String nombreFichero : ficheros) {
				File ficheroEntrada = new File(carpetaOriginal + "\\" + nombreFichero);

				try (BufferedReader lector = new BufferedReader(new FileReader(ficheroEntrada))) {
					String linea;

					while ((linea = lector.readLine()) != null) {
						escritor.write(linea);
						escritor.newLine();
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void moverContenido(String carpetaOrigen, String carpetaDestino) {
		File directorioOrigen = new File(carpetaOrigen);
		File directorioDestino = new File(carpetaDestino);

		if (!directorioDestino.exists()) {
			directorioDestino.mkdirs();
		}

		File[] archivos = directorioOrigen.listFiles();

		if (archivos != null) {
			for (File archivo : archivos) {
				try {
					File nuevoArchivo = new File(directorioDestino, archivo.getName());

					if (!archivo.renameTo(nuevoArchivo)) {
						System.out.println("No se pudo mover el archivo " + archivo.getName());
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void moverImagenesDesdeArchivo(String rutaArchivo, String carpetaDestino) {
		if (!new File(rutaArchivo).exists())
			return;
		File directorioDestino = new File(carpetaDestino);
		if (!directorioDestino.exists()) {
			directorioDestino.mkdirs();
		}

		try (BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo))) {
			String linea;
			while ((linea = lector.readLine()) != null) {
				File archivo = new File(linea);
				if (archivo.isFile() && esArchivoDeImagen(archivo)) {
					try {
						File nuevoArchivo = new File(directorioDestino, archivo.getName());
						if (!archivo.renameTo(nuevoArchivo)) {
							System.out.println("No se pudo mover el archivo " + archivo.getName());
						}
					} catch (SecurityException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
