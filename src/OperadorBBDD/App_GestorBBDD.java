package OperadorBBDD;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Entrenamiento.App_Entrenamiento;
import inicio.Metodos_inicio;
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
import javax.swing.JTable;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

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
	private JButton btnBorrar;
	private JButton btnInsertar;
	private JLabel lmostrar;
	private JButton btnActualizar;
	private JInternalFrame jifActualizarRegistro;
	private JLabel lnomjif;
	private JTextField tnombrejif;
	private JTextField ttamañojif;
	private JTextField tfechajif;
	private String direccionXML, nombreViejo;
	private JButton btndescargar;

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
		setLocationRelativeTo(null);
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
		btnInsertar.setBounds(130, 481, 150, 45);
		contentPane.add(btnInsertar);

		btnBorrar = new JButton("Borrar modelo");
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					boolean borrado = false;
					if (JOptionPane.showConfirmDialog(null, "¿Quiere eliminar el registro: "
							+ table.getValueAt(table.getSelectedRow(), 0).toString() + "?") == JOptionPane.OK_OPTION) {

						borrado = Metodos_BBDD.borrarRegistro("Modelos", table.getSelectedRow());
						Metodos_BBDD.repintarJTable(table, modelo);
					}
					lmostrar.setText(borrado ? "Registro eliminado" : "Registro NO eliminado");

				} catch (Exception s) {
					lmostrar.setText("Debe seleccionar un registro");

				}
			}
		});
		btnBorrar.setForeground(new Color(255, 0, 0));
		btnBorrar.setFont(new Font("Dialog", Font.BOLD, 12));
		btnBorrar.setBounds(650, 480, 150, 45);
		contentPane.add(btnBorrar);

		lmostrar = new JLabel("");
		lmostrar.setHorizontalAlignment(SwingConstants.CENTER);
		lmostrar.setFont(new Font("Dialog", Font.BOLD, 12));
		lmostrar.setBounds(475, 550, 350, 25);
		contentPane.add(lmostrar);

		btnActualizar = new JButton("Actualizar modelo");
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					lmostrar.setText("");
					direccionXML = null;
					nombreViejo = table.getValueAt(table.getSelectedRow(), 0).toString();
					tnombrejif.setText(table.getValueAt(table.getSelectedRow(), 0).toString().substring(0,
							table.getValueAt(table.getSelectedRow(), 0).toString().indexOf(".xml")));
					ttamañojif.setText(table.getValueAt(table.getSelectedRow(), 1).toString() + " KiB");
					tfechajif.setText(table.getValueAt(table.getSelectedRow(), 2).toString());

					jifActualizarRegistro.setVisible(true);

					Metodos_inicio.visibilidad(false, scrollPane, btnActualizar, btnBorrar, btnInsertar, btndescargar);

					Metodos_BBDD.repintarJTable(table, modelo);
				} catch (Exception s) {
					lmostrar.setText("Debe seleccionar un registro");

				}

			}
		});
		btnActualizar.setForeground(new Color(0, 128, 0));
		btnActualizar.setFont(new Font("Dialog", Font.BOLD, 12));
		btnActualizar.setBounds(390, 481, 150, 45);
		contentPane.add(btnActualizar);

		jifActualizarRegistro = new JInternalFrame("Actualizar registro");
		jifActualizarRegistro.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				Metodos_inicio.visibilidad(true, scrollPane, btnActualizar, btnBorrar, btnInsertar, btndescargar);
			}
		});
		jifActualizarRegistro.setVisible(false);
		jifActualizarRegistro.setClosable(true);
		jifActualizarRegistro.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jifActualizarRegistro.setBounds(241, 150, 750, 175);
		contentPane.add(jifActualizarRegistro);
		jifActualizarRegistro.getContentPane().setLayout(null);

		lnomjif = new JLabel("Nombre");
		lnomjif.setHorizontalAlignment(SwingConstants.CENTER);
		lnomjif.setFont(new Font("Dialog", Font.BOLD, 12));
		lnomjif.setBounds(45, 10, 85, 25);
		jifActualizarRegistro.getContentPane().add(lnomjif);

		tnombrejif = new JTextField();
		tnombrejif.setHorizontalAlignment(SwingConstants.CENTER);
		tnombrejif.setFont(new Font("Dialog", Font.BOLD, 12));
		tnombrejif.setBounds(25, 45, 125, 25);
		jifActualizarRegistro.getContentPane().add(tnombrejif);
		tnombrejif.setColumns(10);

		JLabel ltamañojif = new JLabel("Tamaño en KiB");
		ltamañojif.setHorizontalAlignment(SwingConstants.CENTER);
		ltamañojif.setFont(new Font("Dialog", Font.BOLD, 12));
		ltamañojif.setBounds(215, 10, 120, 25);
		jifActualizarRegistro.getContentPane().add(ltamañojif);

		ttamañojif = new JTextField();
		ttamañojif.setEnabled(false);
		ttamañojif.setHorizontalAlignment(SwingConstants.CENTER);
		ttamañojif.setFont(new Font("Dialog", Font.BOLD, 12));
		ttamañojif.setColumns(10);
		ttamañojif.setBounds(213, 45, 125, 25);
		jifActualizarRegistro.getContentPane().add(ttamañojif);

		JLabel lfechajif = new JLabel("Fecha de creación");
		lfechajif.setHorizontalAlignment(SwingConstants.CENTER);
		lfechajif.setFont(new Font("Dialog", Font.BOLD, 12));
		lfechajif.setBounds(407, 10, 120, 25);
		jifActualizarRegistro.getContentPane().add(lfechajif);

		tfechajif = new JTextField();
		tfechajif.setHorizontalAlignment(SwingConstants.CENTER);
		tfechajif.setFont(new Font("Dialog", Font.BOLD, 12));
		tfechajif.setEnabled(false);
		tfechajif.setColumns(10);
		tfechajif.setBounds(405, 45, 125, 25);
		jifActualizarRegistro.getContentPane().add(tfechajif);

		JLabel lmodeloXML = new JLabel("Modelo XML");
		lmodeloXML.setHorizontalAlignment(SwingConstants.CENTER);
		lmodeloXML.setFont(new Font("Dialog", Font.BOLD, 12));
		lmodeloXML.setBounds(597, 10, 120, 25);
		jifActualizarRegistro.getContentPane().add(lmodeloXML);

		JButton btnNewButton = new JButton("Cambiar Modelo");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				direccionXML = Metodos_BBDD.seleccionarModelo(JFileChooser.FILES_ONLY);
				Path path = Paths.get(direccionXML);
				try {
					long size = Files.size(path) / 1024;

					BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

					ttamañojif.setText(size + " KiB");
					tfechajif.setText(df.format(new Date(attr.creationTime().toMillis())));

				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
		btnNewButton.setFont(new Font("Dialog", Font.BOLD, 10));
		btnNewButton.setBounds(595, 47, 125, 25);
		jifActualizarRegistro.getContentPane().add(btnNewButton);

		JButton btnConfirmar = new JButton("Confirmar");
		btnConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean actualizado = false;
				if (direccionXML == null) {

					actualizado = Metodos_BBDD.actualizarRegistro("Modelos", nombreViejo,
							tnombrejif.getText().concat(".xml"), "nombre", "nombre");
				} else {
					actualizado = Metodos_BBDD.actualizarRegistroXML("Modelos", table.getSelectedRow(),
							tnombrejif.getText().concat(".xml"), direccionXML);
				}

				lmostrar.setText(actualizado ? "Registro actualizado" : "Registro NO actualizado");

				Metodos_BBDD.repintarJTable(table, modelo);

				jifActualizarRegistro.dispose();

				Metodos_inicio.visibilidad(true, scrollPane, btnActualizar, btnBorrar, btnInsertar);

			}
		});
		btnConfirmar.setForeground(new Color(0, 128, 0));
		btnConfirmar.setFont(new Font("Dialog", Font.BOLD, 12));
		btnConfirmar.setBounds(595, 90, 125, 25);
		jifActualizarRegistro.getContentPane().add(btnConfirmar);

		btndescargar = new JButton("Descargar modelo");
		btndescargar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() != -1) {

					try {
						lmostrar.setText("");
						Metodos_BBDD.descargarModeloXML("xml", table.getValueAt(table.getSelectedRow(), 0).toString(),
								"Modelos", table.getSelectedRow());
						lmostrar.setText("Modelo descargado");
					} catch (Exception a) {
						lmostrar.setText("Debe seleccionar un registro");
					}
				} else
					lmostrar.setText("Debe seleccionar un registro");

			}

		});
		btndescargar.setForeground(new Color(0, 128, 128));
		btndescargar.setFont(new Font("Dialog", Font.BOLD, 12));
		btndescargar.setBounds(900, 480, 150, 45);
		contentPane.add(btndescargar);

	}
}
