package Entrenamiento;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import OpenCV.Camara;

import inicio.PantallaInicial;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class App_Entrenamiento extends JFrame {

	private  final long serialVersionUID = 1L;
	private JPanel contentPane;
	private  JTextArea textareainformativa;
	@SuppressWarnings("unused")
	private  String carpetaOrigen, carpetaDestino, carpetaOriginal, dirMod;
	private JButton belegircarpetadestino;
	private  String escritorioUsuario = System.getProperty("user.home").concat("\\Desktop");
	private JButton btncarpetadefecto;
	private  File carpetaPadre = new File(escritorioUsuario + "\\Carpeta_Origen_Destino");
	private  JLabel lcargafotospos;
	private  JButton befotospos;
	private  JLabel lcargafotosneg;
	private  JButton bfotosneg;
	private  boolean neg, pos, sam, mod, premod;
	private  String datos = "";
	private  JLabel lcrearsample;
	private  JButton bcrearsample;
	private  JLabel lcrearXML;
	private  JButton bcrearXML;
	private  String carpetaOriginalPositiva, carpetaOriginalNegativa, dirPreMod;
	private JButton bvolver;
	private JLabel lcargamodelo;
	private JLabel lprobarmodelo;
	private JButton btnProbar;
	private JButton breinicio;

	/**
	 * Launch the application.
	 * 
	 * @author Jorge Barba Polán
	 */
	public  static void main(String[] args) {
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
	public  void rellenarTextArea() {
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
			textareainformativa.append("\nAnotación positiva creada > ");

			textareainformativa.append("Sí");
			textareainformativa
					.append("\nDirección de la anotación positiva> " + carpetaOriginalPositiva + "\\pos.txt");

		} else {
			textareainformativa.append("No");
			textareainformativa.append("\nDirección positiva> Nula");
			textareainformativa.append("\nDirección de la anotación positiva> Nula");

		}

		textareainformativa.append("\nFotos negativas > ");

		if (neg) {
			textareainformativa.append("Sí");
			textareainformativa.append("\nDirección negativa> " + carpetaOriginalNegativa);
			textareainformativa.append("\nAnotación negativa creada > ");
			textareainformativa.append("Sí");
			textareainformativa
					.append("\nDirección de la anotación negativa> " + carpetaOriginalNegativa + "\\neg.txt");

		} else {
			textareainformativa.append("No");
			textareainformativa.append("\nDirección negativa> Nula");
			textareainformativa.append("\nDirección de la anotación negativa> Nula");

		}

		textareainformativa.append("\nSamples creadas > ");

		if (sam) {
			textareainformativa.append("Sí");
			textareainformativa.append("\nDirección de las samples> " + carpetaDestino + "\\pos.vec");

		} else {
			textareainformativa.append("No");
			textareainformativa.append("\nDirección de las samples> Nula");

		}

		textareainformativa.append("\nModelo creado > ");

		if (mod) {
			textareainformativa.append("Sí");
			textareainformativa.append("\nDirección del modelo> " + dirMod);

		} else {
			textareainformativa.append("No");
			textareainformativa.append("\nDirección del modelo> Nula");

		}
		textareainformativa.append("\nPre-modelo seleccionado > ");

		if (!premod) {
			textareainformativa.append("Por defecto - Caras Humanas");

		} else {
			textareainformativa.append("Usuario - " + dirPreMod);

		}

	}

	public App_Entrenamiento() {
		iniciarComponentes();
	}

	private void iniciarComponentes() {

		setTitle("Entrenamiento de modelos - Jorge Barba Polán");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1221, 652);
		setExtendedState(MAXIMIZED_BOTH);
		setAutoRequestFocus(true);
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
				if (carp == null)
					return;
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
				datos = "";
				Metodos_app.detectarRectangulos(carpetaOriginalPositiva, carpetaDestino, datos);
				pos = true;
				if (pos && neg) {
					Metodos_app.cambiarAUsable(lcrearsample, bcrearsample);
				}
				rellenarTextArea();
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
				rellenarTextArea();
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
				sam = true;
				rellenarTextArea();
			}
		});
		bcrearsample.setFont(new Font("Dialog", Font.BOLD, 14));
		bcrearsample.setEnabled(false);
		bcrearsample.setBounds(852, 149, 117, 23);
		contentPane.add(bcrearsample);

		bcrearXML = new JButton("Crear");
		bcrearXML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				dirMod = Metodos_app.crearXML(carpetaPadre.getAbsolutePath(), carpetaOriginalNegativa);
				dirMod = Metodos_app.copiarFichero(new File(dirMod), carpetaPadre + "/modelos");
				mod = true;
				Metodos_app.cambiarAUsable(lprobarmodelo, btnProbar);
				rellenarTextArea();
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

		bvolver = new JButton("");
		bvolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PantallaInicial(0).setVisible(true);
				dispose();
			}
		});
		URL url2 = App_Entrenamiento.class.getResource("/volver.png");
		Icon icon = new ImageIcon(url2);
		bvolver.setIcon(icon);
		bvolver.setFont(new Font("Dialog", Font.BOLD, 14));
		bvolver.setBounds(10, 11, 74, 52);
		bvolver.setContentAreaFilled(false);
		bvolver.setBorderPainted(false);
		bvolver.setFocusPainted(false);
		contentPane.add(bvolver);

		JButton btnCargar = new JButton("Cargar");
		btnCargar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dirPreMod = Metodos_app.seleccionarCarpeta(JFileChooser.FILES_ONLY);
				if (dirPreMod == null)
					return;
				premod = true;

				DetectorAnotations.cargarModelo(dirPreMod);
				rellenarTextArea();
			}
		});
		btnCargar.setFont(new Font("Dialog", Font.BOLD, 14));
		btnCargar.setBounds(541, 233, 162, 34);
		contentPane.add(btnCargar);

		lcargamodelo = new JLabel("Cargar pre-modelo");
		lcargamodelo.setHorizontalAlignment(SwingConstants.CENTER);
		lcargamodelo.setForeground(new Color(2, 0, 255));
		lcargamodelo.setFont(new Font("Dialog", Font.BOLD, 14));
		lcargamodelo.setBounds(498, 197, 246, 24);
		contentPane.add(lcargamodelo);

		btnProbar = new JButton("Probar");
		btnProbar.setEnabled(false);
		btnProbar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Camara camara = new Camara(dirMod);
				camara.comenzarCamara();
			}
		});
		btnProbar.setFont(new Font("Dialog", Font.BOLD, 14));
		btnProbar.setBounds(994, 233, 162, 34);
		contentPane.add(btnProbar);

		lprobarmodelo = new JLabel("Probar modelo");
		lprobarmodelo.setEnabled(false);
		lprobarmodelo.setHorizontalAlignment(SwingConstants.CENTER);
		lprobarmodelo.setForeground(new Color(2, 0, 255));
		lprobarmodelo.setFont(new Font("Dialog", Font.BOLD, 14));
		lprobarmodelo.setBounds(951, 197, 246, 24);
		contentPane.add(lprobarmodelo);

		breinicio = new JButton("");
		breinicio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Metodos_app.reiniciarStrings(carpetaOrigen, carpetaDestino, carpetaOriginal, dirMod,
						carpetaOriginalPositiva, carpetaOriginalNegativa, dirPreMod);

				Metodos_app.reiniciarComponentes(lcargafotospos, befotospos, lcargafotosneg, bfotosneg, lcrearsample,
				bcrearsample, lcrearXML, bcrearXML, btnProbar, lprobarmodelo);

				Metodos_app.reiniciarBooleanos(neg, pos, sam, mod, premod);
				rellenarTextArea();
				System.out.println(carpetaOrigen);
				// TODO NO REINICIA SABE DIOS
				
			}

		
		});
		URL url = App_Entrenamiento.class.getResource("/rein.png");

		Icon icon2 = new ImageIcon(url);
		breinicio.setIcon(icon2);
		breinicio.setFont(new Font("Dialog", Font.BOLD, 14));
		breinicio.setFocusPainted(false);
		breinicio.setContentAreaFilled(false);
		breinicio.setBorderPainted(false);
		breinicio.setBounds(1096, 15, 74, 52);
		contentPane.add(breinicio);

		rellenarTextArea();
	}
}
