package OperadorBBDD;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Entrenamiento.App_Entrenamiento;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JTable;

import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Hashtable;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class App_GestorBBDD extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel modelo = new DefaultTableModel() {

		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int row, int column) {
			return false;
		}

		@Override // Método indica que los datos han cambiado
		public void fireTableDataChanged() {
			super.fireTableDataChanged();
		}
	};
	private JButton btndroptable;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App_GestorBBDD frame = new App_GestorBBDD();
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
	public App_GestorBBDD() {
		setTitle("Entrenamiento de modelos - Jorge Barba Polán");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1221, 652);
		setExtendedState(MAXIMIZED_BOTH);
		setAutoRequestFocus(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblGestorBbddDe = new JLabel("Gestor BBDD de modelos");
		lblGestorBbddDe.setHorizontalAlignment(SwingConstants.CENTER);
		lblGestorBbddDe.setForeground(Color.BLUE);
		lblGestorBbddDe.setFont(new Font("Dialog", Font.BOLD, 26));
		lblGestorBbddDe.setBounds(365, 0, 550, 93);
		contentPane.add(lblGestorBbddDe);

		JButton bvolver = new JButton("");
		bvolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		URL url2 = App_Entrenamiento.class.getResource("/volver.png");
		Icon icon = new ImageIcon(url2);
		bvolver.setIcon(icon);
		bvolver.setFont(new Font("Dialog", Font.BOLD, 14));
		bvolver.setFocusPainted(false);
		bvolver.setContentAreaFilled(false);
		bvolver.setBorderPainted(false);
		bvolver.setBounds(10, 0, 74, 52);
		contentPane.add(bvolver);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 68, 1288, 24);
		contentPane.add(separator);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 84, 1205, 375);
		contentPane.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		table.getTableHeader().setEnabled(false);

		modelo = Metodos_BBDD.crearColumnas(modelo);
		table.setModel(modelo);

		btndroptable = new JButton("Borrar Tabla");
		btndroptable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Metodos_BBDD.borrarTabla("Modelos");
			}
		});
		btndroptable.setForeground(new Color(255, 0, 0));
		btndroptable.setFont(new Font("Tahoma", Font.BOLD, 14));
		btndroptable.setBounds(1005, 496, 125, 30);
		contentPane.add(btndroptable);

		Hashtable<String, String> columnasBBDD = new Hashtable<String, String>();
		columnasBBDD.put("Nombre", "String"); // Ultimo en el orden
		columnasBBDD.put("Tamaño_KiB", "Double");
		columnasBBDD.put("Fecha", "Date");
		columnasBBDD.put("XML", "XML"); // Primero en el orden

		
		System.out.println(Metodos_BBDD.crearTabla("Modelos", columnasBBDD, 3));
		
//		for (String s : Metodos_BBDD.ordenColumnas("Modelos")) {
//			System.out.println(s);
//		}

		Metodos_BBDD.insertarRegistroInicial();
		modelo = Metodos_BBDD.insertarRegistrosAJTable("Modelos",modelo);
		table.setModel(modelo);
		
		table = Metodos_BBDD.centrarRegistrosEnJTable(table);
	}
}
