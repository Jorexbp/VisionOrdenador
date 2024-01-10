package OpenCV;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.github.sarxos.webcam.Webcam;

import Entrenamiento_Manual.CamaraEntrenamiento;
import inicio.PantallaInicial;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;

public class SeleccionCamara extends JFrame {

	private static final long serialVersionUID = 1L;
	public static String dirMod;
	private JPanel contentPane;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SeleccionCamara frame = new SeleccionCamara();
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
	public SeleccionCamara() {
		setTitle("Selección de cámara");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(400, 250, 383, 129);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Seleccione una cámara");
		lblNewLabel.setForeground(new Color(0, 0, 255));
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 13));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(20, 11, 200, 14);
		contentPane.add(lblNewLabel);

		List<Webcam> webcams = Webcam.getWebcams();
		String[] namesArray = new String[webcams.stream().map(Webcam::getName).toArray(String[]::new).length + 1];
		for (int i = 0; i < namesArray.length; i++) {
			if (i == 0)
				namesArray[i] = "Seleccionar";
			else
				namesArray[i] = webcams.stream().map(Webcam::getName).toArray(String[]::new)[i - 1];
		}

		JComboBox<String> comboCamara = new JComboBox<String>();
		comboCamara.setModel(new DefaultComboBoxModel<String>(namesArray));

		comboCamara.setBounds(30, 39, 190, 30);
		contentPane.add(comboCamara);

		JButton bok = new JButton("Aceptar");
		bok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (dirMod != null) {
					CamaraEntrenamiento.dirCarpeta(dirMod);
					dispose();
				} else if (comboCamara.getSelectedIndex() != 0) {
					dispose();
					Camara camara = new Camara();
					camara.setIdCamara(comboCamara.getSelectedIndex() - 1);
					camara.comenzarCamara();
					dispose();
				}else if (namesArray.length == 1) {
					JOptionPane.showMessageDialog(null, "No se han detectado cámaras");
					dispose();
					new PantallaInicial(0).setVisible(true);
				}
			}
		});
		bok.setFont(new Font("Dialog", Font.BOLD, 13));
		bok.setForeground(new Color(0, 128, 0));
		bok.setBounds(259, 43, 89, 23);
		contentPane.add(bok);
	}

	
}
