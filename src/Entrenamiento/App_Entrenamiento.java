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
	private static boolean  neg, pos;
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
	public static void rellenarTextArea() {
		textareainformativa.setText("Carpeta origen > ");
		if (carpetaOrigen == null) {
			textareainformativa.append("No seleccionada");
		} else {
			textareainformativa.append(carpetaOrigen);
			Metodos_app.cambiarAUsable(lcargafotospos, befotospos);
		}
		textareainformativa.append("\nCarpeta destino > ");
		if (carpetaDestino == null) {
			textareainformativa.append("No seleccionada");
		} else {
			textareainformativa.append(carpetaDestino);
			Metodos_app.cambiarAUsable(lcargafotosneg, bfotosneg);
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

		textareainformativa.append("\nAnotación positiva creada > ");

		if (pos) {
			textareainformativa.append("Sí");
			textareainformativa.append("\nDirección de la anotación positiva> " + carpetaOriginalPositiva + "\\pos.txt");

		} else {
			textareainformativa.append("No");
			textareainformativa.append("\nDirección de la anotación positiva> Nula");

		}
		
		textareainformativa.append("\nAnotación negativa creada > ");

		if (neg) {
			textareainformativa.append("Sí");
			textareainformativa.append("\nDirección de la anotación negativa> " + carpetaOriginalNegativa + "\\neg.txt");

		} else {
			textareainformativa.append("No");
			textareainformativa.append("\nDirección de la anotación negativa> Nula");

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
				carpetaOrigen = Metodos_app.seleccionarCarpeta(JFileChooser.DIRECTORIES_ONLY);
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
				carpetaDestino = Metodos_app.seleccionarCarpeta(JFileChooser.DIRECTORIES_ONLY);
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
				String[] carp = Metodos_app.crearCarpetasPorDefecto(
						JOptionPane.showInputDialog("Introduce el nombre de la carpeta origen: "),
						JOptionPane.showInputDialog("Introduce el nombre de la carpeta destino: "), escritorioUsuario,
						carpetaOrigen, carpetaDestino, carpetaPadre);
				carpetaOrigen = carp[0];
				carpetaDestino = carp[1];
				rellenarTextArea();
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
				carpetaOriginalPositiva = Metodos_app.seleccionarCarpeta(JFileChooser.FILES_AND_DIRECTORIES);
				datos ="";
				Metodos_app.detectarRectangulos(carpetaOriginalPositiva, carpetaDestino, datos);
				pos = true;
				if (pos && neg) {
					Metodos_app.cambiarAUsable(lcrearsample, bcrearsample);
				}
			}
		});
		befotospos.setEnabled(false);
		befotospos.setFont(new Font("Dialog", Font.BOLD, 14));
		befotospos.setBounds(480, 149, 117, 23);
		contentPane.add(befotospos);

		bfotosneg = new JButton("Seleccionar");
		bfotosneg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carpetaOriginalNegativa = Metodos_app.seleccionarCarpeta(JFileChooser.FILES_AND_DIRECTORIES);

				Metodos_app.crearAnotacionNegativa(carpetaOriginalNegativa, lcrearsample, bcrearsample);
				neg = true;
				if (pos && neg) {
					Metodos_app.cambiarAUsable(lcrearsample, bcrearsample);
				}
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

				Metodos_app.crearSamples(carpetaOriginalPositiva, carpetaDestino, lcrearXML, bcrearXML);
			}
		});
		bcrearsample.setFont(new Font("Dialog", Font.BOLD, 14));
		bcrearsample.setEnabled(false);
		bcrearsample.setBounds(852, 149, 117, 23);
		contentPane.add(bcrearsample);

		bcrearXML = new JButton("Crear");
		bcrearXML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Metodos_app.crearXML(carpetaPadre.getAbsolutePath(), carpetaOriginalNegativa);
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
