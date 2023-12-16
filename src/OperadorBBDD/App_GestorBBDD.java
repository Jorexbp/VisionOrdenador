package OperadorBBDD;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Entrenamiento.App_Entrenamiento;
import inicio.PantallaInicial;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JSeparator;
import javax.swing.JTable;

import java.awt.event.ActionListener;
import java.io.File;
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
	private JButton btnBorrar;
	private JButton btnInsertar;
	private JLabel lmostrar;

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
		bvolver.setBounds(10, 0, 74, 52);
		contentPane.add(bvolver);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 68, 1288, 24);
		contentPane.add(separator);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 84, 1205, 375);
		contentPane.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Dialog", Font.PLAIN, 12));
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

		// System.out.println(Metodos_BBDD.crearTabla("Modelos", columnasBBDD, 3));

//		for (String s : Metodos_BBDD.ordenColumnas("Modelos")) {
//			System.out.println(s);
//		}

		// Metodos_BBDD.insertarRegistroInicial();
		Metodos_BBDD.repintarJTable(table, modelo);

		btnInsertar = new JButton("Insertar modelo");
		btnInsertar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String direcionXML = Metodos_BBDD.seleccionarModelo(JFileChooser.FILES_ONLY);

				if (!direcionXML.trim().toLowerCase().endsWith(".xml")) {
					lmostrar.setText("El archivo seleccionado debe ser un XML");
				} else {

					Object[] registro = Metodos_BBDD.parsearARegistro(new File(direcionXML),
							new File(direcionXML).getName());
					boolean insertado = Metodos_BBDD.insertarRegistroCompleto("Modelos", registro);
					if (!insertado) {
						lmostrar.setText("Registro NO insertado");
						return;
					} else
						lmostrar.setText("Registro insertado");
					Metodos_BBDD.repintarJTable(table, modelo);
				}
			}
		});
		btnInsertar.setForeground(new Color(0, 0, 255));
		btnInsertar.setFont(new Font("Dialog", Font.BOLD, 12));
		btnInsertar.setBounds(30, 481, 150, 45);
		contentPane.add(btnInsertar);

		btnBorrar = new JButton("Borrar modelo");
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					boolean borrado = Metodos_BBDD.borrarPorNombre("Modelos",
							table.getValueAt(table.getSelectedRow(), 0).toString());
					lmostrar.setText(borrado ? "Registro eliminado" : "Registro NO eliminado");
					Metodos_BBDD.repintarJTable(table, modelo);
				} catch (Exception s) {
					lmostrar.setText("Debe seleccionar un registro");

				}
			}
		});
		btnBorrar.setForeground(new Color(255, 0, 0));
		btnBorrar.setFont(new Font("Dialog", Font.BOLD, 12));
		btnBorrar.setBounds(262, 480, 150, 45);
		contentPane.add(btnBorrar);

		lmostrar = new JLabel("");
		lmostrar.setHorizontalAlignment(SwingConstants.CENTER);
		lmostrar.setFont(new Font("Dialog", Font.BOLD, 12));
		lmostrar.setBounds(475, 550, 350, 25);
		contentPane.add(lmostrar);
	}
}
