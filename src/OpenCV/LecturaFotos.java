package OpenCV;

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
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

public class LecturaFotos extends JFrame {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	// Pantalla de la camara
	private JLabel pantallaCamara;
	private JButton btnConfirmar, btnDenegar;
	private VideoCapture capturaVideo;
	private Mat imagen;
	private boolean confirmado, denegado;

	public LecturaFotos(String modelo) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		DeteccionCara.setModelo(modelo);
		// System.out.println(DeteccionCara.getModelo());

	}

	public void comenzarCamara() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				LecturaFotos camara = new LecturaFotos();
				// Empezar la camara en nuevo Thread

				new Thread(new Runnable() {
					@Override
					public void run() {
						camara.iniciarCamara();
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

		pantallaCamara = new JLabel();
		pantallaCamara.setBounds(0, 0, 640, 480);
		add(pantallaCamara);

		btnConfirmar = new JButton("Capturar");
		btnConfirmar.setBounds(325, 480, 120, 40);
		add(btnConfirmar);

		btnConfirmar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO confirmar foto y siguiente
				confirmado = true;
			}
		});

		btnDenegar = new JButton("Ver Extremos");
		btnDenegar.setBounds(175, 480, 120, 40);
		add(btnDenegar);

		btnDenegar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO denegar foto y siguiente
				denegado = true;
			}
		});

		setSize(new Dimension(640, 560));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public void iniciarCamara() {
		imagen = new Mat();
		byte[] datosImagen;

		while (true) {
			File folder = new File("C:\\Users\\Alumno\\Desktop\\PRUEBA");
			File[] files = folder.listFiles();

			for (File file : files) {
				if (file.isFile()) {
					imagen = Imgcodecs.imread(file.getAbsolutePath());
					imagen = Imgcodecs.imdecode(new MatOfByte(DeteccionCara.detectarCara(imagen)),
							Imgcodecs.IMREAD_COLOR);

					final MatOfByte buffer = new MatOfByte();
					Imgcodecs.imencode(".jpg", imagen, buffer);

					datosImagen = buffer.toArray();

					pantallaCamara.setIcon(new ImageIcon(datosImagen));	
					while (!confirmado && !denegado) {
						System.out.println("");
					}
					if (confirmado) {
						
					} else {
						new File(file.getAbsolutePath()).delete();
					}
					confirmado = denegado = false;

				}
			}

		}
	}

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				LecturaFotos camara = new LecturaFotos();
				// Empezar la camara en nuevo Thread

				new Thread(new Runnable() {
					@Override
					public void run() {
						camara.iniciarCamara();
					}
				}).start();
			}
		});

	}

}
