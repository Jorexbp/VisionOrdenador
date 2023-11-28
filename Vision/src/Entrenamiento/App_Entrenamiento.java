package Entrenamiento;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import OpenCV.DeteccionCara;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class App_Entrenamiento extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static JTextArea textareainformativa;
	@SuppressWarnings("unused")
	private static String carpetaOrigen, carpetaDestino, carpetaOriginal;
	private JButton belegircarpetadestino;
	private static String escritorioUsuario = System.getProperty("user.home").concat("\\Desktop");
	private JButton btncarpetadefecto;
	private static File carpetaPadre = new File(escritorioUsuario + "\\Carpeta_Origen_Destino");
	private static JLabel lcargafotospos;
	private static JButton befotospos;
	private static JLabel lcargafotosneg;
	private static JButton bfotosneg;
	private static JButton bcrearanotacion;
	private static JLabel lejecutaranotacion;
	private static boolean anotacionCreada, neg, pos;
	private static String datos = "";
	private static JLabel lcrearsample;
	private static JButton bcrearsample;
	private JMenuItem menuopccargar;
	private JMenu menuopcpadre;

	/**
	 * Launch the application.
	 * 
	 * @author Jorge Barba Polán
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App_Entrenamiento frame = new App_Entrenamiento();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	private static void rellenarTextArea() {
		textareainformativa.setText("Carpeta origen > ");
		if (carpetaOrigen == null) {
			textareainformativa.append("No seleccionada");
		} else {
			textareainformativa.append(carpetaOrigen);
			cambiarAUsable(lcargafotospos, befotospos);
		}
		textareainformativa.append("\nCarpeta destino > ");
		if (carpetaDestino == null) {
			textareainformativa.append("No seleccionada");
		} else {
			textareainformativa.append(carpetaDestino);
			cambiarAUsable(lcargafotosneg, bfotosneg);
		}
		textareainformativa.append("\nFotos positivas > ");

		if (pos) {
			textareainformativa.append("Sí");
			textareainformativa.append("\nDirección positiva> " + carpetaOrigen + "\\pos");

		} else {
			textareainformativa.append("No");
			textareainformativa.append("\nDirección positiva> Nula");

		}
		textareainformativa.append("\nFotos negativas > ");

		if (neg) {
			textareainformativa.append("Sí");
			textareainformativa.append("\nDirección negativa> " + carpetaOrigen + "\\neg");

		} else {
			textareainformativa.append("No");
			textareainformativa.append("\nDirección negativa> Nula");

		}

		textareainformativa.append("\nAnotación creada > ");

		if (anotacionCreada) {
			textareainformativa.append("Sí");
			textareainformativa.append("\nDirección de la anotación > " + carpetaDestino + "\\pos.txt");

		} else {
			textareainformativa.append("No");
			textareainformativa.append("\nDirección de la anotación > Nula");

		}

	}

	private static void cambiarAUsable(JLabel lcargafotospos2, JButton befotospos2) {
		lcargafotospos2.setEnabled(true);
		befotospos2.setEnabled(true);
	}

	private static void copiarFotosACarpeta(String carpetaDestino) {
		try {
			File origen = new File(seleccionarCarpeta(JFileChooser.FILES_AND_DIRECTORIES));
			File[] archivos = origen.listFiles();

			File destino = new File(carpetaDestino);
			if (!destino.exists()) {
				destino.mkdirs();
			}
			int c = 0;
			for (File archivo : archivos) {
				if (esArchivoDeImagen(archivo)) {

					BufferedImage originalImage = ImageIO.read(archivo);
					// Redimensionao
					originalImage = Redimensionador.resizeImage(originalImage, 300, 300);
					// Detectar cara
					Mat imageCara = Redimensionador.bufferedImageToMat(originalImage);
					byte[] bytesMat = DeteccionCara.detectarCara(imageCara);

					ImageIO.write(Redimensionador.byteArrayToRenderedImage(bytesMat), "jpg",
							new File(destino + "/foto_res" + c + ".jpg"));

					c++;
				}
			}

			if (carpetaDestino.contains("pos")) {
				pos = true;
			} else {
				neg = true;
			}
			if (neg && pos) {
				cambiarAUsable(lejecutaranotacion, bcrearanotacion);

			}

			carpetaOriginal = origen.getAbsolutePath();
			rellenarTextArea();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void copiarSamplesExe() {
		String dirDestino = carpetaPadre.getAbsolutePath();
		String dirDestPos = carpetaOrigen;

		String addrP = carpetaDestino + "/pos.txt";
		String addrS = "lib\\samples\\opencv_createsamples.exe";
		String addrW = "lib\\samples\\opencv_world3415.dll";

		Path sourceS = Paths.get(addrS);
		Path sourceW = Paths.get(addrW);
		Path sourceP = Paths.get(addrP);

		Path destinationDir = Paths.get(dirDestino);
		Path destDir = Paths.get(dirDestPos);

		try {
			if (!Files.exists(destinationDir.resolve(sourceS.getFileName()))) {
				Files.copy(sourceS, destinationDir.resolve(sourceS.getFileName()));
			}
			if (!Files.exists(destDir.resolve(sourceP.getFileName()))) {
				Files.copy(sourceP, destDir.resolve(sourceP.getFileName()));
			}

			if (!Files.exists(destinationDir.resolve(sourceW.getFileName()))) {
				Files.copy(sourceW, destinationDir.resolve(sourceW.getFileName()));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean esArchivoDeImagen(File archivo) {
		String nombreArchivo = archivo.getName().toLowerCase();
		return nombreArchivo.endsWith(".jpg") || nombreArchivo.endsWith(".jpeg") || nombreArchivo.endsWith(".png")
				|| nombreArchivo.endsWith(".gif") || nombreArchivo.endsWith(".bmp") || nombreArchivo.endsWith(".tif")
				|| nombreArchivo.endsWith(".tiff");
	}

	private static boolean avisarBorrado(String carpeta) {
		File carpetaNueva = new File(carpeta);
		if (carpetaNueva.exists()) {
			return JOptionPane.showConfirmDialog(null,
					"¿Quiere sobreescribir la carpeta?\n" + carpeta) == JOptionPane.OK_OPTION;

		}
		return true;
	}

	private static void reiniciarCarpetas(String carpeta) {

		if (!carpetaPadre.exists()) {
			carpetaPadre.mkdir();
		} else {
			for (File f : carpetaPadre.listFiles()) {
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

	private static void carpetaCrearTipo(String dir, String tipo) {
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

	private static void crearCarpetasPorDefecto(String nombreCarpetaOrigen, String nombreCarpetaDestino) {
		if (nombreCarpetaOrigen == null || nombreCarpetaDestino == null)
			return;

		nombreCarpetaOrigen = escritorioUsuario + "\\Carpeta_Origen_Destino\\" + nombreCarpetaOrigen;
		nombreCarpetaDestino = escritorioUsuario + "\\Carpeta_Origen_Destino\\" + nombreCarpetaDestino;

		if (avisarBorrado(nombreCarpetaOrigen))
			reiniciarCarpetas(nombreCarpetaOrigen);

		if (avisarBorrado(nombreCarpetaDestino))
			reiniciarCarpetas(nombreCarpetaDestino);

		carpetaOrigen = nombreCarpetaOrigen;
		carpetaDestino = nombreCarpetaDestino;

		carpetaCrearTipo(carpetaOrigen, "pos");
		carpetaCrearTipo(carpetaOrigen, "neg");

		rellenarTextArea();
	}

	private static String seleccionarCarpeta(int JFileOpcion) {
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

	private static void crearAnotacionNegativa() {
		String dirNeg = carpetaOrigen + "/neg";
		String datos = "";

		File carpeta = new File(dirNeg);
		for (File fichero : carpeta.listFiles()) {
			datos += fichero.getAbsolutePath() + "\n";
		}

		try {
			FileWriter fw = new FileWriter(carpetaDestino + "/neg.txt");
			fw.write(datos);
			fw.close();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	private static void ejecutarOpenCVannotations() {
		String fotosPositivas = carpetaOrigen + "/pos";

		crearAnotacionNegativa();
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		File carpetaPos = new File(fotosPositivas);
		int[] coords = new int[4];

		for (File imagen : carpetaPos.listFiles()) {
			if (!imagen.isDirectory()) {
				if (imagen.getAbsolutePath().contains("jpg")) {

					Mat imageMat = Imgcodecs.imread(imagen.getAbsolutePath());
					Imgcodecs.imwrite(imagen.getAbsolutePath(), DetectorAnotations.detectarCara(imageMat));

//		            String ImagenRec = "foto_res" + c + ".jpg";
//		            Imgcodecs.imwrite(ImagenRec, imageMat);

					coords = DetectorAnotations.detectarCoordenadas(imageMat);
					System.out.println(
							"File: " + imagen.getAbsolutePath() + " W: " + imageMat.cols() + " H:" + imageMat.rows());

					escribirAnotation(imagen.getAbsolutePath(), coords);
				}
			}
		}

		rellenarTextArea();
		String destinoAnotacion = carpetaDestino + "/pos.txt";

		try {
			FileWriter fw = new FileWriter(new File(destinoAnotacion));
			fw.write(datos);
			fw.close();

			cambiarAUsable(lcrearsample, bcrearsample);
			copiarSamplesExe();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	private static void escribirAnotation(String dir, int[] coords) {
		for (int i = 0; i < coords.length; i++) {
			if (coords[i] < 0) {
				return;
			}
		}
		dir = dir.substring(dir.indexOf("Origen\\"));

		datos += dir + "  1  " + coords[0] + " " + coords[1] + " " + coords[2] + " " + coords[3] + "\n";
		anotacionCreada = true;

	}

	private static void crearSamples() {
		// C:\Users\Alumno\Desktop\Carpeta_Origen_Destino\pos\foto_res0.jpg
		String addr = carpetaOrigen;
		String addrSample = carpetaPadre + "/opencv_createsamples.exe";
		String posTxt = addr + "/pos.txt";
		String posVec = addr + "/pos.vec";
		String nSamples = Integer.toString(100);
		// SAMPLES.EXE NECESITA SER PADRE DE LAS FOTOS
		String cmd = "cmd /c start cmd.exe /k ";
		String comandoSamples = cmd + addrSample + " -info \"" + posTxt + "\" -w 24 -h 24 -num \"" + nSamples
				+ "\" -vec \"" + posVec + "\"";
		try {
			Runtime.getRuntime().exec(comandoSamples);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void cargarOpciones() {

		carpetaDestino = "C:/Users/Alumno/Desktop/Carpeta_Origen_Destino/Destino";
		carpetaOrigen = "C:/Users/Alumno/Desktop/Carpeta_Origen_Destino/Origen";
		carpetaOriginal = "C:/Users/Alumno/Desktop/Fotos";

		pos = true;
		neg = true;
		anotacionCreada = true;
		cambiarAUsable(lcrearsample, bcrearsample);
		cambiarAUsable(lejecutaranotacion, bcrearanotacion);

		rellenarTextArea();
	}

	public App_Entrenamiento() {
		setTitle("Entrenamiento de modelos - Jorge Barba Polán");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1221, 652);
		setExtendedState(MAXIMIZED_BOTH);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		menuopcpadre = new JMenu("New menu");
		menuopcpadre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		menuBar.add(menuopcpadre);

		menuopccargar = new JMenuItem("Cargar opciones");
		menuopccargar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargarOpciones();
			}
		});
		menuopccargar.setFont(new Font("Segoe UI", Font.BOLD, 13));
		menuopccargar.setHorizontalAlignment(SwingConstants.CENTER);
		menuopcpadre.add(menuopccargar);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		JLabel ltitulo = new JLabel("Entrenamiento de modelos");
		ltitulo.setHorizontalAlignment(SwingConstants.CENTER);
		ltitulo.setForeground(new Color(0, 0, 255));
		ltitulo.setFont(new Font("Dialog", Font.BOLD, 26));
		ltitulo.setBounds(365, 11, 550, 93);
		contentPane.add(ltitulo);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 79, 1288, 24);
		contentPane.add(separator);

		JLabel lcarpetaorigen = new JLabel("Carpeta origen");
		lcarpetaorigen.setForeground(new Color(2, 0, 255));
		lcarpetaorigen.setHorizontalAlignment(SwingConstants.CENTER);
		lcarpetaorigen.setFont(new Font("Dialog", Font.BOLD, 14));
		lcarpetaorigen.setBounds(75, 114, 117, 24);
		contentPane.add(lcarpetaorigen);

		JButton belegircarpetaorigen = new JButton("Seleccionar");
		belegircarpetaorigen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carpetaOrigen = seleccionarCarpeta(JFileChooser.DIRECTORIES_ONLY);
				rellenarTextArea();
			}
		});
		belegircarpetaorigen.setFont(new Font("Dialog", Font.BOLD, 14));
		belegircarpetaorigen.setBounds(80, 149, 117, 23);
		contentPane.add(belegircarpetaorigen);

		textareainformativa = new JTextArea();
		textareainformativa.setForeground(new Color(255, 255, 255));
		textareainformativa.setBackground(new Color(0, 0, 0));
		textareainformativa.setEditable(false);
		textareainformativa.setFont(new Font("Consolas", Font.PLAIN, 14));

		textareainformativa.setBounds(72, 320, 1135, 284);
		contentPane.add(textareainformativa);

		JLabel lcarpetadestino = new JLabel("Carpeta destino");
		lcarpetadestino.setHorizontalAlignment(SwingConstants.CENTER);
		lcarpetadestino.setForeground(new Color(2, 0, 255));
		lcarpetadestino.setFont(new Font("Dialog", Font.BOLD, 14));
		lcarpetadestino.setBounds(230, 114, 117, 24);
		contentPane.add(lcarpetadestino);

		belegircarpetadestino = new JButton("Seleccionar");
		belegircarpetadestino.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carpetaDestino = seleccionarCarpeta(JFileChooser.DIRECTORIES_ONLY);
				rellenarTextArea();
			}
		});
		belegircarpetadestino.setFont(new Font("Dialog", Font.BOLD, 14));
		belegircarpetadestino.setBounds(235, 149, 117, 23);
		contentPane.add(belegircarpetadestino);

		JLabel lcarpetaspordefecto = new JLabel("Usar carpetas por defecto: ");
		lcarpetaspordefecto.setHorizontalAlignment(SwingConstants.CENTER);
		lcarpetaspordefecto.setForeground(new Color(2, 0, 255));
		lcarpetaspordefecto.setFont(new Font("Dialog", Font.BOLD, 14));
		lcarpetaspordefecto.setBounds(75, 211, 194, 24);
		contentPane.add(lcarpetaspordefecto);

		btncarpetadefecto = new JButton("Nombrar");
		btncarpetadefecto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				crearCarpetasPorDefecto(JOptionPane.showInputDialog("Introduce el nombre de la carpeta origen: "),
						JOptionPane.showInputDialog("Introduce el nombre de la carpeta destino: "));
			}
		});
		btncarpetadefecto.setFont(new Font("Dialog", Font.BOLD, 12));
		btncarpetadefecto.setBounds(279, 214, 89, 23);
		contentPane.add(btncarpetadefecto);

		JLabel lblseCrearnEn = new JLabel("(Se crearán en el escritorio)");
		lblseCrearnEn.setHorizontalAlignment(SwingConstants.CENTER);
		lblseCrearnEn.setForeground(new Color(2, 0, 255));
		lblseCrearnEn.setFont(new Font("Dialog", Font.BOLD, 10));
		lblseCrearnEn.setBounds(75, 233, 194, 24);
		contentPane.add(lblseCrearnEn);

		lcargafotospos = new JLabel("Fotos positivas");
		lcargafotospos.setEnabled(false);
		lcargafotospos.setHorizontalAlignment(SwingConstants.CENTER);
		lcargafotospos.setForeground(new Color(2, 0, 255));
		lcargafotospos.setFont(new Font("Dialog", Font.BOLD, 14));
		lcargafotospos.setBounds(475, 114, 117, 24);
		contentPane.add(lcargafotospos);

		befotospos = new JButton("Seleccionar");
		befotospos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copiarFotosACarpeta(carpetaOrigen + "/pos");
			}
		});
		befotospos.setEnabled(false);
		befotospos.setFont(new Font("Dialog", Font.BOLD, 14));
		befotospos.setBounds(480, 149, 117, 23);
		contentPane.add(befotospos);

		bfotosneg = new JButton("Seleccionar");
		bfotosneg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copiarFotosACarpeta(carpetaOrigen + "/neg");
			}
		});
		bfotosneg.setEnabled(false);
		bfotosneg.setFont(new Font("Dialog", Font.BOLD, 14));
		bfotosneg.setBounds(651, 149, 117, 23);
		contentPane.add(bfotosneg);

		lcargafotosneg = new JLabel("Fotos negativas");
		lcargafotosneg.setEnabled(false);
		lcargafotosneg.setHorizontalAlignment(SwingConstants.CENTER);
		lcargafotosneg.setForeground(new Color(2, 0, 255));
		lcargafotosneg.setFont(new Font("Dialog", Font.BOLD, 14));
		lcargafotosneg.setBounds(646, 114, 117, 24);
		contentPane.add(lcargafotosneg);

		bcrearanotacion = new JButton("Crear");
		bcrearanotacion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ejecutarOpenCVannotations();
			}
		});
		bcrearanotacion.setFont(new Font("Dialog", Font.BOLD, 14));
		bcrearanotacion.setEnabled(false);
		bcrearanotacion.setBounds(889, 150, 117, 23);
		contentPane.add(bcrearanotacion);

		lejecutaranotacion = new JLabel("Crear anotacion");
		lejecutaranotacion.setHorizontalAlignment(SwingConstants.CENTER);
		lejecutaranotacion.setForeground(new Color(2, 0, 255));
		lejecutaranotacion.setFont(new Font("Dialog", Font.BOLD, 14));
		lejecutaranotacion.setEnabled(false);
		lejecutaranotacion.setBounds(884, 115, 117, 24);
		contentPane.add(lejecutaranotacion);

		lcrearsample = new JLabel("Crear samples");
		lcrearsample.setHorizontalAlignment(SwingConstants.CENTER);
		lcrearsample.setForeground(new Color(2, 0, 255));
		lcrearsample.setFont(new Font("Dialog", Font.BOLD, 14));
		lcrearsample.setEnabled(false);
		lcrearsample.setBounds(1037, 114, 117, 24);
		contentPane.add(lcrearsample);

		bcrearsample = new JButton("Crear");
		bcrearsample.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				crearSamples();
			}
		});
		bcrearsample.setFont(new Font("Dialog", Font.BOLD, 14));
		bcrearsample.setEnabled(false);
		bcrearsample.setBounds(1042, 149, 117, 23);
		contentPane.add(bcrearsample);

		rellenarTextArea();
	}
}
