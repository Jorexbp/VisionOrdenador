package Entrenamiento_Manual;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import Entrenamiento.DetectorAnotations;
import OpenCV.DeteccionCara;

public class LecturaFotos extends JFrame {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	// Pantalla de la camara
	private JLabel pantallaCamara;
	private JButton btnConfirmar, btnDenegar;
	private Mat imagen;
	private boolean confirmado, denegado;

	public static void comenzarCamara(String carpetaFotos) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				LecturaFotos camara = new LecturaFotos();
				// Empezar la camara en nuevo Thread

				new Thread(new Runnable() {
					@Override
					public void run() {
						camara.iniciarCamara(carpetaFotos);
					}
				}).start();
			}
		});
	}

	public LecturaFotos() {
		iniciarComponentes();
	}

	private void iniciarComponentes() {
		// GUI
		setLayout(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		pantallaCamara = new JLabel();
		pantallaCamara.setBounds(20, 0, 640, 480);
		add(pantallaCamara);

		btnConfirmar = new JButton("Confirmar");
		btnConfirmar.setBounds(325, 480, 120, 40);
		add(btnConfirmar);

		btnConfirmar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO confirmar foto, siguiente y guardar las coords y txt de la foto
				confirmado = true;
			}
		});

		btnDenegar = new JButton("Denegar");
		btnDenegar.setBounds(175, 480, 120, 40);
		add(btnDenegar);

		btnDenegar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO denegar foto, siguiente y guardar en un txt la ruta de la foto
				denegado = true;
			}
		});

		setSize(new Dimension(640, 560));
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void iniciarCamara(String carpetaFotos) {
		imagen = new Mat();
		byte[] datosImagen;
		int coords[] = new int[4];
		double ancho, alto;
		String direccionImagen = "";
		while (true) {
			File folder = new File(carpetaFotos);
			File[] files = folder.listFiles();

			for (File file : files) {
				if (file.isFile()) {
					imagen = Imgcodecs.imread(file.getAbsolutePath());
					imagen = Imgcodecs.imdecode(new MatOfByte(DeteccionCara.detectarCara(imagen)),
							Imgcodecs.IMREAD_COLOR);
					ancho = imagen.cols();
					alto = imagen.rows();
					Imgproc.resize(imagen, imagen, new Size(getWidth() - 50, getHeight() - 20));

					final MatOfByte buffer = new MatOfByte();
					Imgcodecs.imencode(".jpg", imagen, buffer);

					datosImagen = buffer.toArray();

					pantallaCamara.setIcon(new ImageIcon(datosImagen));
					while (!confirmado && !denegado) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (confirmado) {
						// TODO Guardo las coords y txt de la foto
						Imgproc.resize(imagen, imagen, new Size(ancho, alto));

						coords = DetectorAnotations.detectarCoordenadas(imagen);
						direccionImagen = file.getAbsolutePath();

					} else {
						// TODO Guardo las coords y txt de la foto en otro txt
					}
					confirmado = denegado = false;

				}
			}

		}
	}

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String carpetaFotos = "";
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				LecturaFotos camara = new LecturaFotos();
				// Empezar la camara en nuevo Thread

				new Thread(new Runnable() {
					@Override
					public void run() {
						camara.iniciarCamara(carpetaFotos);
					}
				}).start();
			}
		});

	}

}
