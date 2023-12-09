package Entrenamiento_Manual;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import Entrenamiento.DetectorAnotations;
import Entrenamiento.Metodos_app;
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
	
	public static void setModelo(String dirMod) {
		DeteccionCara.setModelo(dirMod);
	}

	public static void comenzarCamara(String carpetaFotos, String carpetaPadre) {
		EventQueue.invokeLater(() -> {
			LecturaFotos camara = new LecturaFotos();

			new Thread(() -> {
				try {
					if (carpetaFotos == null) {
						return;
					}
					camara.iniciarCamara(carpetaFotos, carpetaPadre);
					Metodos_app.reciclarFotosDenegadas(carpetaPadre,carpetaFotos);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}).start();
		

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
		btnConfirmar.setMnemonic('C');

		btnConfirmar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				confirmado = true;
			}
		});

		btnDenegar = new JButton("Denegar");
		btnDenegar.setBounds(175, 480, 120, 40);
		btnDenegar.setMnemonic('D');
		add(btnDenegar);

		btnDenegar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				denegado = true;
			}
		});

		setSize(new Dimension(640, 560));
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void iniciarCamara(String carpetaFotos, String carpetaPadre) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		imagen = new Mat();
		byte[] datosImagen;
		int coords[] = new int[4];
		double ancho, alto;
		String direccionImagen = "";
		File folder = new File(carpetaFotos);
		File[] files = folder.listFiles();
		FileWriter fw = null;
		if (new File(carpetaPadre + "\\fotos_confirmadas.txt").exists())
			new File(carpetaPadre + "\\fotos_confirmadas.txt").delete();

		if (new File(carpetaPadre + "\\fotos_denegadas.txt").exists())
			new File(carpetaPadre + "\\fotos_denegadas.txt").delete();

		for (File file : files) {
			if (file.isFile()) {

				imagen = Imgcodecs.imread(file.getAbsolutePath());
				try {
					imagen = Imgcodecs.imdecode(new MatOfByte(DeteccionCara.detectarCara(imagen)),
							Imgcodecs.IMREAD_COLOR);
				} catch (Exception e) {

					break;
				}
				ancho = imagen.cols();
				alto = imagen.rows();
				Imgproc.resize(imagen, imagen, new Size(getWidth() - 50, getHeight() - 20));

				final MatOfByte buffer = new MatOfByte();
				Imgcodecs.imencode(".jpg", imagen, buffer);

				datosImagen = buffer.toArray();

				pantallaCamara.setIcon(new ImageIcon(datosImagen));
				while (!confirmado && !denegado) {
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				coords = DetectorAnotations.detectarCoordenadas(imagen);

				if (confirmado && (coords[0] > 0 && coords[1] > 0 && coords[2] > 0 && coords[3] > 0)) {

					Imgproc.resize(imagen, imagen, new Size(ancho, alto));

					direccionImagen = file.getAbsolutePath();
					fw = new FileWriter(new File(carpetaPadre + "\\fotos_confirmadas.txt"), true);
					// System.out.println(carpetaOriginal + "\\" + dir);
					direccionImagen = direccionImagen + "  1  " + coords[0] + " " + coords[1] + " " + coords[2] + " "
							+ coords[3] + "\n";

					fw.write(direccionImagen);
					fw.close();

				} else {
					direccionImagen = file.getAbsolutePath();

					fw = new FileWriter(new File(carpetaPadre + "\\fotos_denegadas.txt"), true);
					// System.out.println(carpetaOriginal + "\\" + dir);

					fw.write(direccionImagen + "\n");
					fw.close();
				}
				confirmado = denegado = false;

			}

		}
		
		dispose();
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
						try {
							camara.iniciarCamara(carpetaFotos, "");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		});

	}

}
