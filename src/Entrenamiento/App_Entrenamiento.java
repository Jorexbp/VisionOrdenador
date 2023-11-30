package Entrenamiento;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
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
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

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
	private static boolean anotacionCreada, neg, pos;
	private static String datos = "";
	private static JLabel lcrearsample;
	private static JButton bcrearsample;
	private static JLabel lcrearXML;
	private static JButton bcrearXML;
	private static String carpetaOriginalPositiva, carpetaOriginalNegativa;

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
			textareainformativa.append("\nDirección positiva> " + carpetaOriginalPositiva);

		} else {
			textareainformativa.append("No");
			textareainformativa.append("\nDirección positiva> Nula");

		}
		textareainformativa.append("\nFotos negativas > ");

		if (neg) {
			textareainformativa.append("Sí");
			textareainformativa.append("\nDirección negativa> " + carpetaOriginalNegativa);

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

	private static void crearAnotacionNegativa(String carpetaOriginal) {
		File origen = new File(carpetaOriginal);

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
			neg = true;
			if (pos && neg) {
				cambiarAUsable(lcrearsample, bcrearsample);
			}
			rellenarTextArea();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}
//	crearAnotacionNegativa(carpetaOriginalNegativa);

	private static void cambiarNombres(String carpetaOriginal) {
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

	private static void detectarRectangulos(String carpetaOriginal) { // Fotos aqui
		try {
			cambiarNombres(carpetaOriginal);
			File origen = new File(carpetaOriginal);
			File[] archivos = origen.listFiles();

			File destino = new File(carpetaDestino);
			if (!destino.exists()) {
				destino.mkdirs();
			}

			int[] coords = new int[4];
			for (File archivo : archivos) {
				if (esArchivoDeImagen(archivo)) {

					System.out.println(archivo);
					BufferedImage originalImage = ImageIO.read(archivo);

					Mat imageCara = Redimensionador.bufferedImageToMat(originalImage);
					byte[] bytesMat = DeteccionCara.detectarCara(imageCara);
					imageCara = Imgcodecs.imdecode(new MatOfByte(bytesMat), Imgcodecs.IMREAD_UNCHANGED);
					coords = DetectorAnotations.detectarCoordenadas(imageCara); // CON EL REC ROJO

					escribirAnotation(archivo.getName(), coords);

				}
			}

			try {
				FileWriter fw = new FileWriter(new File(carpetaOriginal + "/pos.txt"));
				fw.write(datos);
				fw.close();
				// carpetaOriginalNegativa =
				// seleccionarCarpeta(JFileChooser.FILES_AND_DIRECTORIES);

				pos = true;
				if (pos && neg) {
					cambiarAUsable(lcrearsample, bcrearsample);
				}
			} catch (IOException e) {

				e.printStackTrace();
			}

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
		datos += dir + "  1  " + coords[0] + " " + coords[1] + " " + coords[2] + " " + coords[3] + "\n";
		anotacionCreada = true;
	}

	private static void crearSamples() {
		String addrSample = "lib\\samples\\opencv_createsamples.exe";
		String posTxt = carpetaOriginalPositiva + "\\pos.txt";
		String posVec = carpetaDestino + "\\pos.vec";
		String nSamples = Integer.toString(100);

		String cmd = "cmd /c start cmd.exe /k ";
		String comandoSamples = cmd + addrSample + " -info \"" + posTxt + "\" -w 24 -h 24 -num \"" + nSamples
				+ "\" -vec \"" + posVec + "\"";
		try {
			Runtime.getRuntime().exec(comandoSamples);
			rellenarTextArea();
			cambiarAUsable(lcrearXML, bcrearXML);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void crearXML() {

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

	public App_Entrenamiento() {
		setTitle("Entrenamiento de modelos - Jorge Barba Polán");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1221, 652);
		setExtendedState(MAXIMIZED_BOTH);
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
				carpetaOriginalPositiva = seleccionarCarpeta(JFileChooser.FILES_AND_DIRECTORIES);
				detectarRectangulos(carpetaOriginalPositiva);
			}
		});
		befotospos.setEnabled(false);
		befotospos.setFont(new Font("Dialog", Font.BOLD, 14));
		befotospos.setBounds(480, 149, 117, 23);
		contentPane.add(befotospos);

		bfotosneg = new JButton("Seleccionar");
		bfotosneg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carpetaOriginalNegativa = seleccionarCarpeta(JFileChooser.FILES_AND_DIRECTORIES);
				crearAnotacionNegativa(carpetaOriginalNegativa);
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

		lcrearsample = new JLabel("Crear samples");
		lcrearsample.setHorizontalAlignment(SwingConstants.CENTER);
		lcrearsample.setForeground(new Color(2, 0, 255));
		lcrearsample.setFont(new Font("Dialog", Font.BOLD, 14));
		lcrearsample.setEnabled(false);
		lcrearsample.setBounds(847, 114, 117, 24);
		contentPane.add(lcrearsample);

		bcrearsample = new JButton("Crear");
		bcrearsample.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				crearSamples();
			}
		});
		bcrearsample.setFont(new Font("Dialog", Font.BOLD, 14));
		bcrearsample.setEnabled(false);
		bcrearsample.setBounds(852, 149, 117, 23);
		contentPane.add(bcrearsample);

		bcrearXML = new JButton("Crear");
		bcrearXML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				crearXML();
			}
		});
		bcrearXML.setFont(new Font("Dialog", Font.BOLD, 14));
		bcrearXML.setEnabled(false);
		bcrearXML.setBounds(1029, 149, 117, 23);
		contentPane.add(bcrearXML);

		lcrearXML = new JLabel("Crear modelo");
		lcrearXML.setHorizontalAlignment(SwingConstants.CENTER);
		lcrearXML.setForeground(new Color(2, 0, 255));
		lcrearXML.setFont(new Font("Dialog", Font.BOLD, 14));
		lcrearXML.setEnabled(false);
		lcrearXML.setBounds(1024, 114, 117, 24);
		contentPane.add(lcrearXML);

		rellenarTextArea();
	}
}
