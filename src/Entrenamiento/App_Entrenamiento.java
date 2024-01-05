package Entrenamiento;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Entrenamiento_Manual.LecturaFotos;
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

import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class App_Entrenamiento extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea textareainformativa;
	private JButton belegircarpetadestino;
	private JButton btncarpetadefecto;
	private JLabel lcargafotospos;
	private JButton befotospos;
	private JLabel lcargafotosneg;
	private JButton bfotosneg;
	private boolean neg, pos, sam, mod, premod;
	private JLabel lcrearsample;
	private JButton bcrearsample;
	private JLabel lcrearXML;
	private JButton bcrearXML;
	private String carpetaOriginalPositiva, carpetaOriginalNegativa, dirPreMod, dirMod, carpetaPadre, posTXT;
	private JButton bvolver;
	private JLabel lcargamodelo;
	private JLabel lprobarmodelo;
	private JButton btnProbar;
	private JButton breinicio;
	private JCheckBox ccomprobarimagen;
	private JSpinner spniteraciones;
	private JLabel lnumiter;

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
	public void rellenarTextArea() {
		textareainformativa.setText("");
		textareainformativa.append("Carpeta destino > ");

		if (carpetaPadre != null) {
			textareainformativa.append(carpetaPadre);
			Metodos_app.cambiarAUsable(lcargafotospos, befotospos);
			Metodos_app.cambiarAUsable(lcargafotosneg, bfotosneg);
		} else {
			textareainformativa.append("Nula");

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
			textareainformativa.append("\nDirección de las samples> " + carpetaPadre + "\\pos.vec");

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
		textareainformativa.append("\nModelo seleccionado > ");

		if (!premod) {
			textareainformativa.append("Por defecto - Caras Humanas");

		} else {
			textareainformativa.append("Usuario - " + dirPreMod);

		}

	}

	public App_Entrenamiento() {
		iniciarComponentes();
	}

	public App_Entrenamiento(String direccionPremodelo) {
		this.dirPreMod = direccionPremodelo;
		premod = true;

		DetectorAnotations.cargarModelo(dirPreMod);
		iniciarComponentes();
	}

	private void iniciarComponentes() {

		setTitle("Entrenamiento de modelos - Jorge Barba Polán");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1250, 665);
		setLocationRelativeTo(null);
		setAutoRequestFocus(true);
		setResizable(false);
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
		lcarpetadestino.setBounds(82, 114, 117, 24);
		contentPane.add(lcarpetadestino);

		belegircarpetadestino = new JButton("Seleccionar");
		belegircarpetadestino.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carpetaPadre = Metodos_app.seleccionarCarpeta(JFileChooser.DIRECTORIES_ONLY);
				rellenarTextArea();
			}
		});
		belegircarpetadestino.setFont(new Font("Dialog", Font.BOLD, 14));
		belegircarpetadestino.setBounds(82, 149, 117, 23);
		contentPane.add(belegircarpetadestino);

		JLabel lcarpetaspordefecto = new JLabel("Usar carpetas por defecto: ");
		lcarpetaspordefecto.setHorizontalAlignment(SwingConstants.CENTER);
		lcarpetaspordefecto.setForeground(new Color(2, 0, 255));
		lcarpetaspordefecto.setFont(new Font("Dialog", Font.BOLD, 14));
		lcarpetaspordefecto.setBounds(72, 221, 194, 24);
		contentPane.add(lcarpetaspordefecto);

		btncarpetadefecto = new JButton("Nombrar");
		btncarpetadefecto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String carp = Metodos_app.crearCarpetasPorDefecto(
						JOptionPane.showInputDialog("Introduce el nombre de la carpeta destino")

				);
				if (carp == null)
					return;
				carpetaPadre = carp;
				rellenarTextArea();
			}
		});
		btncarpetadefecto.setFont(new Font("Dialog", Font.BOLD, 12));
		btncarpetadefecto.setBounds(276, 224, 89, 23);
		contentPane.add(btncarpetadefecto);

		JLabel lblseCrearnEn = new JLabel("(Se crearán en el escritorio)");
		lblseCrearnEn.setHorizontalAlignment(SwingConstants.CENTER);
		lblseCrearnEn.setForeground(new Color(2, 0, 255));
		lblseCrearnEn.setFont(new Font("Dialog", Font.BOLD, 10));
		lblseCrearnEn.setBounds(72, 243, 194, 24);
		contentPane.add(lblseCrearnEn);

		lcargafotospos = new JLabel("Fotos positivas");
		lcargafotospos.setEnabled(false);
		lcargafotospos.setHorizontalAlignment(SwingConstants.CENTER);
		lcargafotospos.setForeground(new Color(2, 0, 255));
		lcargafotospos.setFont(new Font("Dialog", Font.BOLD, 14));
		lcargafotospos.setBounds(433, 114, 117, 24);
		contentPane.add(lcargafotospos);

		befotospos = new JButton("Seleccionar");
		befotospos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carpetaOriginalPositiva = Metodos_app.seleccionarCarpeta(JFileChooser.FILES_AND_DIRECTORIES);

				posTXT = "pos.txt";
				if (ccomprobarimagen.isSelected()) {
					LecturaFotos.comenzarCamara(carpetaOriginalPositiva, carpetaPadre); // HAY QUE ESPERAR A QUE ESTO
																						// ACABE DE EJECUTARSE

					Metodos_app.setCarpetaPositiva(carpetaOriginalPositiva);
				} else {

					Metodos_app.detectarRectangulos(carpetaOriginalPositiva, carpetaPadre);

				}

				pos = true;
				if (pos && neg) {
					Metodos_app.cambiarAUsable(lcrearsample, bcrearsample);
				}
				rellenarTextArea();
			}
		});
		befotospos.setEnabled(false);
		befotospos.setFont(new Font("Dialog", Font.BOLD, 14));
		befotospos.setBounds(433, 149, 117, 23);
		contentPane.add(befotospos);

		bfotosneg = new JButton("Seleccionar");
		bfotosneg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carpetaOriginalNegativa = Metodos_app.seleccionarCarpeta(JFileChooser.FILES_AND_DIRECTORIES);

				Metodos_app.crearAnotacionNegativa(carpetaOriginalNegativa);
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

				Metodos_app.crearSamples(carpetaOriginalPositiva, carpetaPadre, posTXT, 1);
				Metodos_app.cambiarAUsable(lcrearXML, bcrearXML);
				Metodos_app.cambiarAUsable(lnumiter, spniteraciones);

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

				int nIteraciones = (int) spniteraciones.getValue();

				if (nIteraciones == 1) {
					dirMod = Metodos_app.crearXML(carpetaPadre, carpetaOriginalNegativa, 1);

				} else {

					for (int i = 0; i <= nIteraciones; i++) {
						dirMod = Metodos_app.crearXML(carpetaPadre, carpetaOriginalNegativa, i + 1);
						Metodos_app.crearPositivos(carpetaOriginalPositiva, carpetaPadre);
						Metodos_app.crearAnotacionNegativa(carpetaOriginalNegativa);
						Metodos_app.crearSamples(carpetaOriginalPositiva, carpetaPadre, posTXT, i + 1);

						DetectorAnotations.cargarModelo(dirMod);
						LecturaFotos.setModelo(dirMod);

					}
				}
				System.out.println(dirMod);
				File modelo = new File(dirMod);

				String nombreModelo = dirMod.substring(0, dirMod.lastIndexOf('/')) + "/"
						+ JOptionPane.showInputDialog("Introduzca el nombre del modelo") + ".xml";
				System.out.println(nombreModelo);

				if (modelo.renameTo(new File(nombreModelo))) {
					JOptionPane.showMessageDialog(null, "Nombre del modelo creado:\n" + nombreModelo);
				} else {
					JOptionPane.showMessageDialog(null, "No se ha podido cambiar el nombre del modelo");

				}

				dirMod = Metodos_app.copiarFichero(new File(dirMod), carpetaPadre + "/modelos/");
				mod = true;
				Metodos_app.cambiarAUsable(lprobarmodelo, btnProbar);
				rellenarTextArea();
				// TODO HACER BOTON DE REINICIO
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
				LecturaFotos.setModelo(dirMod);
				rellenarTextArea();
			}
		});
		btnCargar.setFont(new Font("Dialog", Font.BOLD, 14));
		btnCargar.setBounds(600, 233, 162, 34);
		contentPane.add(btnCargar);

		lcargamodelo = new JLabel("Cargar modelo");
		lcargamodelo.setHorizontalAlignment(SwingConstants.CENTER);
		lcargamodelo.setForeground(new Color(2, 0, 255));
		lcargamodelo.setFont(new Font("Dialog", Font.BOLD, 14));
		lcargamodelo.setBounds(550, 197, 246, 24);
		contentPane.add(lcargamodelo);

		btnProbar = new JButton("Probar");
		btnProbar.setEnabled(false);
		btnProbar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Camara camara = new Camara(dirMod);
				camara.comenzarCamara();
				dispose();
			}
		});
		btnProbar.setFont(new Font("Dialog", Font.BOLD, 14));
		btnProbar.setBounds(800, 233, 162, 34);
		contentPane.add(btnProbar);

		lprobarmodelo = new JLabel("Probar modelo");
		lprobarmodelo.setEnabled(false);
		lprobarmodelo.setHorizontalAlignment(SwingConstants.CENTER);
		lprobarmodelo.setForeground(new Color(2, 0, 255));
		lprobarmodelo.setFont(new Font("Dialog", Font.BOLD, 14));
		lprobarmodelo.setBounds(750, 197, 246, 24);
		contentPane.add(lprobarmodelo);

		breinicio = new JButton("");
		breinicio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Metodos_app.cambiarANoUsable(lcargafotospos, befotospos, lcargafotosneg, bfotosneg, lcrearsample,
						bcrearsample, btnProbar, lprobarmodelo, lcrearXML, bcrearXML, lnumiter, spniteraciones);
				carpetaOriginalPositiva = carpetaOriginalNegativa = dirPreMod = dirMod = carpetaPadre = posTXT = "";

				rellenarTextArea();
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

		ccomprobarimagen = new JCheckBox("Comprobar imágenes");
		ccomprobarimagen.setForeground(new Color(0, 0, 255));
		ccomprobarimagen.setFont(new Font("Dialog", Font.BOLD, 11));
		ccomprobarimagen.setBounds(415, 179, 147, 23);
		contentPane.add(ccomprobarimagen);

		spniteraciones = new JSpinner();
		spniteraciones.setEnabled(false);
		spniteraciones
				.setModel(new SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
		spniteraciones.setFont(new Font("Dialog", Font.BOLD, 14));
		spniteraciones.setBounds(1052, 233, 50, 30);
		contentPane.add(spniteraciones);

		lnumiter = new JLabel("Iteraciones del entrenamiento");
		lnumiter.setHorizontalAlignment(SwingConstants.CENTER);
		lnumiter.setForeground(new Color(2, 0, 255));
		lnumiter.setFont(new Font("Dialog", Font.BOLD, 14));
		lnumiter.setEnabled(false);
		lnumiter.setBounds(961, 197, 246, 24);
		contentPane.add(lnumiter);

		rellenarTextArea();
	}
}
