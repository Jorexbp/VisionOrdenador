package inicio;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatLightLaf;

import Entrenamiento.App_Entrenamiento;

public class PantallaInicial extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel ltit;
	private JTextArea textArea;
	private JCheckBox cacepta;
	private JButton bcont;
	private JScrollPane scrollPane;
	private JLabel lseguntit;
	private JButton bentrenador, bcrearpremodelo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (UnsupportedLookAndFeelException e1) {

		}
		UIManager.put("Button.arc", 999);
		UIManager.put("Component.arc", 999);
		UIManager.put("ProgressBar.arc", 50);
		UIManager.put("TextComponent.arc", 999);
		UIManager.put("Component.innerFocusWidth", 1);
		UIManager.put("Button.innerFocusWidth", 1);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PantallaInicial frame = new PantallaInicial();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 */

	public PantallaInicial() {
		setTitle("Entrenador de modelos - Jorge Barba Polán");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 614, 536);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		ltit = new JLabel(
				"<html>Bienvenido al entrenador de modelos para identificación de objetos a través del procesamiento de imagenes. Lea y confirme si esta de acuerdo con la ley de privacidad y protección de datos</html>");
		ltit.setHorizontalAlignment(SwingConstants.CENTER);
		ltit.setForeground(new Color(0, 0, 255));
		ltit.setFont(new Font("Tahoma", Font.BOLD, 14));
		ltit.setBounds(10, 11, 580, 87);
		contentPane.add(ltit);

		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 132, 580, 273);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane.setViewportView(textArea);

		cacepta = new JCheckBox("ACEPTAR");
		cacepta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cacepta.isSelected()) {
					bcont.setEnabled(true);
				} else {
					bcont.setEnabled(false);
				}
			}
		});
		cacepta.setFont(new Font("Dialog", Font.BOLD, 12));
		cacepta.setBounds(68, 444, 99, 23);
		contentPane.add(cacepta);

		bcont = new JButton("Continuar");
		bcont.setEnabled(false);
		bcont.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Metodos_inicio.visibilidad(false, ltit, scrollPane, cacepta, bcont);
				Metodos_inicio.visibilidad(true, lseguntit, bentrenador, bcrearpremodelo);
			}
		});
		bcont.setFont(new Font("Dialog", Font.BOLD, 12));
		bcont.setBounds(455, 436, 135, 39);
		contentPane.add(bcont);

		lseguntit = new JLabel("¿Que desea hacer?");
		lseguntit.setVisible(false);
		lseguntit.setHorizontalAlignment(SwingConstants.CENTER);
		lseguntit.setFont(new Font("Dialog", Font.BOLD, 14));
		lseguntit.setBounds(93, 82, 380, 39);
		contentPane.add(lseguntit);

		bentrenador = new JButton("Entrenar con un pre-modelo");
		bentrenador.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new App_Entrenamiento().setVisible(true);
				dispose();
			}
		});
		bentrenador.setFont(new Font("Dialog", Font.BOLD, 12));
		bentrenador.setEnabled(true);
		bentrenador.setVisible(false);
		bentrenador.setBounds(43, 300, 236, 44);
		contentPane.add(bentrenador);

		bcrearpremodelo = new JButton("Crear un pre-modelo");
		bcrearpremodelo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO app de crear el premodelo similar al entrenamiento pero mas sencillo
			}
		});
		bcrearpremodelo.setFont(new Font("Dialog", Font.BOLD, 12));
		bcrearpremodelo.setEnabled(true);
		bcrearpremodelo.setVisible(false);
		bcrearpremodelo.setBounds(300, 300, 236, 44);
		contentPane.add(bcrearpremodelo);
		Metodos_inicio.recogerLeyPrivacidadYLeyProteccionDeDatos(this.textArea);
	}
}
