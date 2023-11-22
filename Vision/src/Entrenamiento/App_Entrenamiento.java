package Entrenamiento;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class App_Entrenamiento extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static JTextArea textareainformativa;
	private static String carpetaOrigen, carpetaDestino, carpetaOriginal;
	private JButton belegircarpetadestino;
	private static String escritorioUsuario = System.getProperty("user.home").concat("\\Desktop");
	private JButton btncarpetadefecto;
	private static File carpetaPadre = new File(escritorioUsuario + "\\Carpeta Origen_Destino");
	private static JLabel lcargafotospos;
	private static JButton befotospos;
	private static JLabel lcargafotosneg;
	private static JButton bfotosneg;
	private static JButton bcrearanotacion;
	private static JLabel lejecutaranotacion;
	private static boolean anotacionCreada;

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

	private static void copiarFotosACarpeta(String carpeta, String tipo) {

		File origen = new File(seleccionarCarpeta(JFileChooser.FILES_AND_DIRECTORIES));
		File[] archivos = origen.listFiles();

		File destino = new File(carpeta + "\\" + tipo);
		try {
			for (File archivo : archivos) {
				Path origenPath = archivo.toPath();
				Path destinoPath = new File(destino, archivo.getName()).toPath();
				Files.copy(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
			}
			carpetaOriginal = origen.getAbsolutePath();
			cambiarAUsable(lejecutaranotacion, bcrearanotacion);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		}
		File directory = new File(carpeta);
		if (!directory.exists()) {
			directory.mkdir();
		}

		for (File file : directory.listFiles()) {

			file.delete();

		}

	}

	private static void carpetaCrearTipo(String tipo) {
		File carpetaSubHijo;

		for (int i = 0; i < 2; i++) {
			carpetaSubHijo = new File(carpetaOrigen + "\\" + tipo);
			if (!carpetaSubHijo.exists()) {
				carpetaSubHijo.mkdir();
			}
		}

	}

	private static void crearCarpetasPorDefecto(String nombreCarpetaOrigen, String nombreCarpetaDestino) {
		if (nombreCarpetaOrigen == null || nombreCarpetaDestino == null)
			return;

		nombreCarpetaOrigen = escritorioUsuario + "\\Carpeta Origen_Destino\\" + nombreCarpetaOrigen;
		nombreCarpetaDestino = escritorioUsuario + "\\Carpeta Origen_Destino\\" + nombreCarpetaDestino;

		if (avisarBorrado(nombreCarpetaOrigen))
			reiniciarCarpetas(nombreCarpetaOrigen);

		if (avisarBorrado(nombreCarpetaDestino))
			reiniciarCarpetas(nombreCarpetaDestino);

		carpetaOrigen = nombreCarpetaOrigen;
		carpetaDestino = nombreCarpetaDestino;

		carpetaCrearTipo("pos");
		carpetaCrearTipo("neg");

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

	private static void ejecutarOpenCVannotations() {
		// COMANDO MALO HACER QUE SEA GLOBAL
		String destinoAnotacion = carpetaDestino + "/pos.txt";
		String comando1 = "cmd /c start cmd.exe /c \" cd C:/Users/Alumno/git/VisionOrdenador/Vision/lib/annotation && opencv_annotation.exe"
				+ " --annotations=\"" + destinoAnotacion + "\"" + " --images=\"" + carpetaOrigen+"/pos" + "\""; // COMANDO
																											// MALO

		// --annotations=pos.txt --images=RazaBlanca/
		try {
			Process proceso = Runtime.getRuntime().exec(comando1);
			if (proceso.waitFor() == 0) {
				anotacionCreada = true;
			}
		} catch (IOException | InterruptedException e) {

		}
		rellenarTextArea();
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
				copiarFotosACarpeta(carpetaOrigen, "pos");
			}
		});
		befotospos.setEnabled(false);
		befotospos.setFont(new Font("Dialog", Font.BOLD, 14));
		befotospos.setBounds(480, 149, 117, 23);
		contentPane.add(befotospos);

		bfotosneg = new JButton("Seleccionar");
		bfotosneg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copiarFotosACarpeta(carpetaOrigen, "neg");
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

		rellenarTextArea();
	}
}
