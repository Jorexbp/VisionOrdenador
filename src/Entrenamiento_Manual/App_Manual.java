package Entrenamiento_Manual;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Entrenamiento.App_Entrenamiento;
import Entrenamiento.Metodos_app;
import OperadorBBDD.Metodos_BBDD;
import inicio.PantallaInicial;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

public class App_Manual extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lcarpetaorigen;
	private JButton belegircarpetaorigen;
	private String direccionCarpetaFotos, direccionFotosNegativas, direccionCarpetaDestino;
	private JLabel lrecom;
	private JCheckBox cSelecCarpetaFotos;
	private JCheckBox cCantidad;
	private JCheckBox cCrearModelo;
	private JCheckBox cCrearAnotaciones;
	private JCheckBox cSelecCarpetaDestino;
	private JCheckBox cReentrenar;
	private JCheckBox cIdenti;
	private JButton bcarpetadestino;
	private JLabel lcarpetaDestino;
	private JButton bidentificar;
	private JLabel lidentificar;
	private JButton bcrearmodelo;
	private JLabel lcrear;
	private JButton breentrenar;
	private JLabel lreentrenar;
	private JButton bfotosnegativas;
	private JLabel lcarpetaFotosNegativas;
	private JCheckBox cfotosnegaticas;
	private JCheckBox cfotosnegativas;
	private JCheckBox cfotosposi;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App_Manual frame = new App_Manual();
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
	public App_Manual() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1317, 670);
		setLocationRelativeTo(null);
		setAutoRequestFocus(true);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel ltitulo = new JLabel("Creador de Modelos");
		ltitulo.setHorizontalAlignment(SwingConstants.CENTER);
		ltitulo.setForeground(Color.BLUE);
		ltitulo.setFont(new Font("Dialog", Font.BOLD, 26));
		ltitulo.setBounds(371, 0, 550, 93);
		contentPane.add(ltitulo);

		JButton bvolver = new JButton("");
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
		bvolver.setFocusPainted(false);
		bvolver.setContentAreaFilled(false);
		bvolver.setBorderPainted(false);
		bvolver.setBounds(45, 21, 74, 52);
		contentPane.add(bvolver);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 92, 1288, 24);
		contentPane.add(separator);

		lcarpetaorigen = new JLabel("Carpeta positiva");
		lcarpetaorigen.setHorizontalAlignment(SwingConstants.CENTER);
		lcarpetaorigen.setForeground(new Color(2, 0, 255));
		lcarpetaorigen.setFont(new Font("Dialog", Font.BOLD, 14));
		lcarpetaorigen.setBounds(106, 146, 117, 24);
		contentPane.add(lcarpetaorigen);

		belegircarpetaorigen = new JButton("Seleccionar");
		belegircarpetaorigen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"Si la carpeta no contiene un número significativo de imágenes (Ej: 100) no se creará correctamente");
				direccionCarpetaFotos = Metodos_app.seleccionarCarpeta(JFileChooser.DIRECTORIES_ONLY);
				// System.out.println(direccionCarpetaFotos);
				cSelecCarpetaFotos.setSelected(true);
				Metodos_app.cambiarAUsable(lcarpetaFotosNegativas, bfotosnegativas);

			}
		});
		belegircarpetaorigen.setFont(new Font("Dialog", Font.BOLD, 14));
		belegircarpetaorigen.setBounds(92, 181, 143, 32);
		contentPane.add(belegircarpetaorigen);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(0, 352, 1288, 24);
		contentPane.add(separator_1);

		lrecom = new JLabel("Pasos para la creación:");
		lrecom.setHorizontalAlignment(SwingConstants.CENTER);
		lrecom.setForeground(Color.BLUE);
		lrecom.setFont(new Font("Dialog", Font.BOLD, 19));
		lrecom.setBounds(30, 352, 305, 93);
		contentPane.add(lrecom);

		cSelecCarpetaFotos = new JCheckBox("Seleccionar una carpeta con fotos del objeto a identificar");
		cSelecCarpetaFotos.setEnabled(false);
		cSelecCarpetaFotos.setFont(new Font("Dialog", Font.BOLD, 12));
		cSelecCarpetaFotos.setBounds(231, 418, 382, 24);
		contentPane.add(cSelecCarpetaFotos);

		JSeparator separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setBounds(923, 352, 79, 281);
		contentPane.add(separator_2);

		JLabel lblParaUnMejor = new JLabel("Para un mejor modelo");
		lblParaUnMejor.setHorizontalAlignment(SwingConstants.CENTER);
		lblParaUnMejor.setForeground(new Color(0, 128, 0));
		lblParaUnMejor.setFont(new Font("Dialog", Font.BOLD, 19));
		lblParaUnMejor.setBounds(957, 352, 305, 58);
		contentPane.add(lblParaUnMejor);

		cCantidad = new JCheckBox("Usar 100 fotos o más");
		cCantidad.setEnabled(false);
		cCantidad.setFont(new Font("Dialog", Font.BOLD, 12));
		cCantidad.setBounds(1008, 421, 186, 24);
		contentPane.add(cCantidad);

		cSelecCarpetaDestino = new JCheckBox("Seleccionar una carpeta donde guardar los resultados");
		cSelecCarpetaDestino.setFont(new Font("Dialog", Font.BOLD, 12));
		cSelecCarpetaDestino.setEnabled(false);
		cSelecCarpetaDestino.setBounds(231, 497, 382, 24);
		contentPane.add(cSelecCarpetaDestino);

		cCrearAnotaciones = new JCheckBox("Seleccionar el objeto a identificar en las fotos");
		cCrearAnotaciones.setFont(new Font("Dialog", Font.BOLD, 12));
		cCrearAnotaciones.setEnabled(false);
		cCrearAnotaciones.setBounds(231, 546, 382, 24);
		contentPane.add(cCrearAnotaciones);

		cCrearModelo = new JCheckBox("Crear el modelo");
		cCrearModelo.setFont(new Font("Dialog", Font.BOLD, 12));
		cCrearModelo.setEnabled(false);
		cCrearModelo.setBounds(231, 586, 382, 24);
		contentPane.add(cCrearModelo);

		cIdenti = new JCheckBox("Tener fotos con el objeto identificable");
		cIdenti.setFont(new Font("Dialog", Font.BOLD, 12));
		cIdenti.setEnabled(false);
		cIdenti.setBounds(1008, 497, 254, 24);
		contentPane.add(cIdenti);

		cReentrenar = new JCheckBox("Re-entrenar el modelo con otras fotos");
		cReentrenar.setFont(new Font("Dialog", Font.BOLD, 12));
		cReentrenar.setEnabled(false);
		cReentrenar.setBounds(1008, 546, 254, 24);
		contentPane.add(cReentrenar);

		bcarpetadestino = new JButton("Seleccionar");
		bcarpetadestino.setEnabled(false);
		bcarpetadestino.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				direccionCarpetaDestino = Metodos_app.seleccionarCarpeta(JFileChooser.DIRECTORIES_ONLY);
				cSelecCarpetaDestino.setSelected(true);
				Metodos_app.cambiarAUsable(lidentificar, bidentificar);
			}
		});
		bcarpetadestino.setFont(new Font("Dialog", Font.BOLD, 14));
		bcarpetadestino.setBounds(479, 181, 143, 32);

		contentPane.add(bcarpetadestino);

		lcarpetaDestino = new JLabel("Carpeta destino");
		lcarpetaDestino.setEnabled(false);
		lcarpetaDestino.setHorizontalAlignment(SwingConstants.CENTER);
		lcarpetaDestino.setForeground(new Color(2, 0, 255));
		lcarpetaDestino.setFont(new Font("Dialog", Font.BOLD, 14));
		lcarpetaDestino.setBounds(493, 146, 117, 24);
		contentPane.add(lcarpetaDestino);

		bidentificar = new JButton("Comenzar");
		bidentificar.setEnabled(false);
		bidentificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (cfotosposi.isSelected()) {
					// Se usa toda la foto
					Metodos_app.crearAnotacionConTodaLaFoto(direccionCarpetaFotos, direccionCarpetaDestino);

				} else {
					// Ejecución normal
					Metodos_app.crearAnotaciones(direccionCarpetaFotos, direccionCarpetaDestino, "pos.txt");

				}

				Metodos_app.crearAnotacionNegativa(direccionFotosNegativas);
				Metodos_app.cambiarAUsable(lcrear, bcrearmodelo);
				cCrearAnotaciones.setSelected(true);
			}
		});

		bidentificar.setFont(new Font("Dialog", Font.BOLD, 14));
		bidentificar.setBounds(677, 181, 143, 32);
		contentPane.add(bidentificar);

		lidentificar = new JLabel("Identificar el objeto");
		lidentificar.setEnabled(false);
		lidentificar.setHorizontalAlignment(SwingConstants.CENTER);
		lidentificar.setForeground(new Color(2, 0, 255));
		lidentificar.setFont(new Font("Dialog", Font.BOLD, 14));
		lidentificar.setBounds(664, 146, 156, 24);
		contentPane.add(lidentificar);

		bcrearmodelo = new JButton("Comenzar");
		bcrearmodelo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				direccionCarpetaFotos = "C:/Users/Alumno/Desktop/FotosEjemplo";
//				direccionCarpetaDestino = "C:/Users/Alumno/Desktop/DestinoEjemplo";
//				direccionFotosNegativas = "C:/Users/Alumno/Desktop/MuchasFotos/Neg";
//				Metodos_app.carpetaPos = direccionCarpetaFotos;
//				Metodos_app.carpetaNeg = direccionFotosNegativas;

				Metodos_app.quitarDireccionAbsoluta(direccionCarpetaFotos + "/pos.txt");
				Metodos_app.crearSamples(direccionCarpetaFotos, direccionCarpetaDestino, "pos.txt", 1);
				String direcionXML = Metodos_app.crearXML(direccionCarpetaDestino, direccionFotosNegativas, 1);

				Metodos_app.cambiarAUsable(lreentrenar, breentrenar);
				cCrearModelo.setSelected(true);

				JOptionPane.showMessageDialog(null, "Modelo creado");

				if (JOptionPane.showConfirmDialog(null,
						"¿Desea insertar este modelo a la base de datos?") == JOptionPane.OK_OPTION) {

					String nombre = JOptionPane.showInputDialog("Introduzca un nombre para el modelo identificativo");
					String fichero = direcionXML.replace(new File(direcionXML).getName(), nombre) + ".xml";

					boolean cambioNombre = new File(direcionXML).renameTo(new File(fichero));
					if (cambioNombre)
						direcionXML = fichero;

					boolean todos = Metodos_app.añadirUsuarioTodosAArchivo(direcionXML);
					if (!todos) {
						JOptionPane.showMessageDialog(null,
								"No se ha podido añadir 'Todos' al archivo\nNo se puede insertar\n\nArchivo guardado en: "
										+ direcionXML);
						return;
					}

					Object[] registro = Metodos_BBDD.parsearARegistro(new File(direcionXML),
							new File(direcionXML).getName());
					boolean insertado = Metodos_BBDD.insertarRegistroCompleto("Modelos", registro);

					JOptionPane.showMessageDialog(null,
							insertado ? "Registro insertado" : "Error al insertar el registro");
				}

			}
		});
		bcrearmodelo.setEnabled(false);
		bcrearmodelo.setFont(new Font("Dialog", Font.BOLD, 14));
		bcrearmodelo.setBounds(873, 181, 143, 32);
		contentPane.add(bcrearmodelo);

		lcrear = new JLabel("Crear modelo");
		lcrear.setEnabled(false);
		lcrear.setHorizontalAlignment(SwingConstants.CENTER);
		lcrear.setForeground(new Color(2, 0, 255));
		lcrear.setFont(new Font("Dialog", Font.BOLD, 14));
		lcrear.setBounds(860, 146, 156, 24);
		contentPane.add(lcrear);

		breentrenar = new JButton("Entrenar");
		breentrenar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new App_Entrenamiento().setVisible(true);
				dispose();
			}
		});
		breentrenar.setEnabled(false);
		breentrenar.setFont(new Font("Dialog", Font.BOLD, 14));
		breentrenar.setBounds(1063, 181, 143, 32);
		contentPane.add(breentrenar);

		lreentrenar = new JLabel("Entrenar el modelo");
		lreentrenar.setEnabled(false);
		lreentrenar.setHorizontalAlignment(SwingConstants.CENTER);
		lreentrenar.setForeground(new Color(2, 0, 255));
		lreentrenar.setFont(new Font("Dialog", Font.BOLD, 14));
		lreentrenar.setBounds(1050, 146, 156, 24);
		contentPane.add(lreentrenar);

		bfotosnegativas = new JButton("Seleccionar");
		bfotosnegativas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				direccionFotosNegativas = Metodos_app.seleccionarCarpeta(JFileChooser.DIRECTORIES_ONLY);
				Metodos_app.crearAnotacionNegativa(direccionFotosNegativas);

				Metodos_app.cambiarAUsable(lcarpetaDestino, bcarpetadestino);
				cfotosnegaticas.setSelected(true);
			}
		});
		bfotosnegativas.setFont(new Font("Dialog", Font.BOLD, 14));
		bfotosnegativas.setEnabled(false);
		bfotosnegativas.setBounds(279, 181, 143, 32);
		contentPane.add(bfotosnegativas);

		lcarpetaFotosNegativas = new JLabel("Carpeta negativa");
		lcarpetaFotosNegativas.setHorizontalAlignment(SwingConstants.CENTER);
		lcarpetaFotosNegativas.setForeground(new Color(2, 0, 255));
		lcarpetaFotosNegativas.setFont(new Font("Dialog", Font.BOLD, 14));
		lcarpetaFotosNegativas.setEnabled(false);
		lcarpetaFotosNegativas.setBounds(291, 146, 131, 24);
		contentPane.add(lcarpetaFotosNegativas);

		cfotosnegaticas = new JCheckBox("Seleccionar una carpeta con fotos de lo que NO se quiere identificar");
		cfotosnegaticas.setFont(new Font("Dialog", Font.BOLD, 12));
		cfotosnegaticas.setEnabled(false);
		setLocationRelativeTo(null);
		cfotosnegaticas.setBounds(231, 452, 428, 24);
		contentPane.add(cfotosnegaticas);

		cfotosnegativas = new JCheckBox("Tener fotos negativas buenas");
		cfotosnegativas.setFont(new Font("Dialog", Font.BOLD, 12));
		cfotosnegativas.setEnabled(false);
		cfotosnegativas.setBounds(1008, 454, 254, 24);
		contentPane.add(cfotosnegativas);

		cfotosposi = new JCheckBox("Usar las imágenes al completo sin especificaciones (+Rápido +Consumo)");
		cfotosposi.setForeground(new Color(0, 0, 255));
		cfotosposi.setFont(new Font("Dialog", Font.BOLD, 11));
		cfotosposi.setBounds(90, 288, 443, 24);
		contentPane.add(cfotosposi);
	}
}
