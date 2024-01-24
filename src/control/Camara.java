package control;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import inicio.PantallaInicial;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Camara extends JFrame {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	// Pantalla de la camara
	private JLabel pantallaCamara;
	private JButton btnCapturar, btnExtremos, btnCamara, btnCaras;
	private VideoCapture capturaVideo;
	private Mat imagen;
	private int idCamara;
	private Buscador buscador = new Buscador();

	public void setIdCamara(int idCamara) {
		this.idCamara = idCamara;
	}

	private boolean clicked, camara = true;

	public Camara(String modelo) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Buscador.setModelo(modelo);
		// System.out.println(DeteccionCara.getModelo());

	}

	public void comenzarCamara() {
	
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Camara camara = new Camara();

				camara.iniciarComponentes();
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

	public Camara() {

	}

	private void iniciarComponentes() {

		// GUI

		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		pantallaCamara = new JLabel();
		pantallaCamara.setBounds(0, 0, 640, 480);
		getContentPane().add(pantallaCamara);

		btnCapturar = new JButton("Capturar");
		btnCapturar.setBounds(325, 480, 120, 40);
		getContentPane().add(btnCapturar);

		btnCapturar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clicked = true;
			}
		});

		btnExtremos = new JButton("Ver Extremos");
		btnExtremos.setBounds(175, 480, 120, 40);
		getContentPane().add(btnExtremos);

		btnExtremos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				camara = false;

			}
		});
		btnCaras = new JButton("Volver");
		btnCaras.setBounds(0, 480, 120, 40);
		getContentPane().add(btnCaras);

		btnCaras.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				camara = false;
				dispose();
				try {
					capturaVideo.release();
				} catch (Exception pol) {

				}

				capturaVideo = null;
				new PantallaInicial(0).setVisible(true);

			}
		});

		btnCamara = new JButton("Ver Cámara");
		btnCamara.setBounds(475, 480, 120, 40);
		getContentPane().add(btnCamara);

		btnCamara.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				camara = true;

			}
		});

		setSize(new Dimension(640, 560));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					capturaVideo.release();
				} catch (Exception le) {

				}
				capturaVideo = null;
				new PantallaInicial(0).setVisible(true);
			}
		});
	}

	public void iniciarCamara() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		// TODO preguntar que camara quiere usar
		capturaVideo = new VideoCapture(idCamara);
		imagen = new Mat();
		@SuppressWarnings("unused")
		byte[] datosImagen;

		ImageIcon icono = null;
		while (true) {
			try {
				if (capturaVideo == null) {
					break;
				}
				capturaVideo.read(imagen);

				final MatOfByte buffer = new MatOfByte();
				Imgcodecs.imencode(".jpg", imagen, buffer);

				datosImagen = buffer.toArray();

				if (camara) {

					icono = new ImageIcon(buscador.mostrarInfoCara(imagen));

				}
				pantallaCamara.setIcon(icono);
				if (clicked) {
					String nombre = JOptionPane.showInputDialog(this, "Introduzca el nombre de la imagen");
					if (nombre == null) {
						nombre = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
					}

					clicked = false;

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// capturaVideo = null;
	}

	public static void main(String[] args) {
		Camara camara = new Camara();
		camara.comenzarCamara();
	}

}
