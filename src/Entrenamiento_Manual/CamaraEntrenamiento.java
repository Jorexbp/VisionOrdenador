package Entrenamiento_Manual;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

import inicio.PantallaInicial;

public class CamaraEntrenamiento extends JFrame {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	// Pantalla de la camara
	private JLabel pantallaCamara;
	private JButton btnCapturar;
	private VideoCapture capturaVideo;
	private Mat imagen;
	private boolean clicked;

	public static void dirCarpeta(String dirCar) {
		JOptionPane.showMessageDialog(null,
				"Asegúrese de que la cámara pueda ver el objeto a identificar, la toma de captura de pantalla ocurre cada 1.25 segundos");
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				CamaraEntrenamiento camara = new CamaraEntrenamiento();
				// Empezar la camara en nuevo Thread

				new Thread(new Runnable() {
					@Override
					public void run() {
						camara.iniciarComponentes();
						camara.iniciarCamara(dirCar);
					}
				}).start();
			}
		});
	}

	private void iniciarComponentes() {
		// GUI
		setLayout(null);

		pantallaCamara = new JLabel();
		pantallaCamara.setBounds(0, 0, 640, 480);
		add(pantallaCamara);

		btnCapturar = new JButton("Parar");
		btnCapturar.setBounds(325, 480, 120, 40);
		add(btnCapturar);

		btnCapturar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clicked = true;
			}
		});

		setSize(new Dimension(640, 560));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public void iniciarCamara(String dirCar) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		capturaVideo = new VideoCapture(0);
		imagen = new Mat();
		byte[] datosImagen;
		int c = 0;
		ImageIcon icono = null;
		while (true) {
			try {
				Thread.sleep(1250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			capturaVideo.read(imagen);

			final MatOfByte buffer = new MatOfByte();
			Imgcodecs.imencode(".jpg", imagen, buffer);

			datosImagen = buffer.toArray();

			icono = new ImageIcon(datosImagen);

			Imgcodecs.imwrite(dirCar + "/f_" + c + ".jpg", imagen);

			pantallaCamara.setIcon(icono);
			c++;

			if (clicked) {
				new PantallaInicial(0).setVisible(true);
				dispose();

				break;

			}
		}
	}

}
