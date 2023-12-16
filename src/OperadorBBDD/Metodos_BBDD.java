package OperadorBBDD;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

public class Metodos_BBDD {
	private static Connection con = null;
	private static String direccionServidor = "localhost", puertoServidor = "5432", nombreBBDDPostgresql = "Prueba",
			nombreUsuarioPostgresql = "postgres", contrasenaUsuarioPostgresql = "admin";

	public boolean getEstadoConexion() {
		if (con == null)
			return false;
		else
			return true;
	}

	public static boolean abrirConexion() {
		cerrarConexion();
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(
					"jdbc:postgresql://" + direccionServidor + ":" + puertoServidor + "/" + nombreBBDDPostgresql + "",
					nombreUsuarioPostgresql, contrasenaUsuarioPostgresql);
			if (con == null) {
				JOptionPane.showMessageDialog(null, "No se pudo conectar a la BBDD");
			} else
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean cerrarConexion() {
		try {
			con = null;
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "No se ha podido cerra la conexión a la BBDD");
		}
		return false;
	}

	public static String crearColumnas(Hashtable<String, String> tabla, int primaryKey) {
		String columnas = "";
		for (Map.Entry<String, String> entry : tabla.entrySet()) {

			// System.out.println("Nombre: " + entry.getKey() + " - Dato: " +
			// entry.getValue());
			switch (entry.getValue().toLowerCase()) {

			case "integer": {
				columnas += entry.getKey() + " INTEGER, ";
				break;
			}

			case "string": {
				columnas += entry.getKey() + " TEXT, ";

				break;
			}

			case "double": {
				columnas += entry.getKey() + " DOUBLE PRECISION, ";

				break;
			}

			case "date": {
				columnas += entry.getKey() + " DATE, ";

				break;
			}
			case "xml": {
				columnas += entry.getKey() + " XML, ";

				break;
			}
			default:
				throw new IllegalArgumentException("Valor incorrecto: " + entry.getValue());
			}
		}

		if (columnas.lastIndexOf(',') == columnas.length() - 2) {
			columnas = columnas.substring(0, columnas.lastIndexOf(','));
		}
		if (primaryKey != -1) {
			String[] arr = columnas.split(",");
			arr[primaryKey] = arr[primaryKey] + " PRIMARY KEY";
			columnas = "";
			for (int i = 0; i < arr.length; i++) {
				if (i >= arr.length - 1) {
					columnas += arr[i];
					break;
				}

				columnas += arr[i] + ",";
			}

		}

//		System.out.println(columnas);
		return columnas;
	}

	public static DefaultTableModel crearColumnas(DefaultTableModel modelo) {
		modelo.addColumn("Nombre");

		modelo.addColumn("Tamaño en KiB");
		modelo.addColumn("Fecha de creación");
		// modelo.addColumn("Entrenamientos");
		modelo.addColumn("XML");

		return modelo;
	}

	public static boolean crearTabla(String nombreTabla, Hashtable<String, String> columnas, int primaryKey) {
		abrirConexion();
		Statement statement;
		try {
			// System.out.println(crearColumnas(columnas, primaryKey));
			String query = "create table " + nombreTabla + "(" + crearColumnas(columnas, primaryKey) + ");";
			statement = con.createStatement();
			int salida = statement.executeUpdate(query);
			// System.out.println(salida != 0 ? "Tabla creada" : "Tabla NO creada");
			return salida != 0;
		} catch (Exception e) {
			// System.out.println(e);
		} finally {
			cerrarConexion();
		}
		return false;
	}

	private static String arrayObjectAInsert(Object[] valores) {
		String val = "";

		for (int i = 0; i < valores.length; i++) {
			if (valores[i].getClass().getName().equals("java.lang.String")
					|| valores[i].getClass().getName().equals("java.sql.Date")) {
				val += "'" + valores[i] + "',";

			} else if (valores[i].toString().contains("xml")) {
				val += "XMLPARSE(DOCUMENT CONVERT_FROM(pg_read_binary_file(\'" + valores[i].toString()
						+ "\'), 'UTF8')),";
				// SI ESTO DA ERROR DE PERMISOS AÑADIR A UNA CARPETA CON "TODOS" EN SEGURIDAD
			} else
				val += valores[i] + ",";
		}
		if (val.lastIndexOf(',') == val.length() - 1) {
			val = val.substring(0, val.lastIndexOf(','));
		}
		return val.replace("\\", "/");
	}

	public static ArrayList<String> ordenColumnas(String nombreTabla) {
		abrirConexion();
		ArrayList<String> colus = new ArrayList<>();

		DatabaseMetaData dbmd;
		try {

			dbmd = con.getMetaData();
			ResultSet rs = dbmd.getColumns(null, null, nombreTabla.toLowerCase(), "%");
			while (rs.next()) {
				colus.add(rs.getString(4)
				/*
				 * + " : " + rs.getString(6) // TIPO DE DATO
				 */
				);

			}
			// System.out.println(colus);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cerrarConexion();
		}
		return colus;

	}

	public static boolean insertarRegistroCompleto(String nombreTabla, Object[] valores) {
		abrirConexion();
		Statement statement;
		try {

			String val = arrayObjectAInsert(valores);

			// System.out.println(val);
			String query = "insert into " + nombreTabla + " values(" + val + ");";
			statement = con.createStatement();
			int salida = statement.executeUpdate(query);
			// System.out.println(salida != 0 ? "Registro insertado" : "Registro NO
			// insertado");
			return salida != 0;
		} catch (Exception e) {
			System.out.println(e);
			if (e.toString().contains("duplicada")) {
				JOptionPane.showMessageDialog(null,
						"No puede repetirse el nombre de los modelos\n\tRegistro no insertado");
			}

		} finally {
			cerrarConexion();
		}
		return false;
	}

	public boolean insertarRegistroParcial(String nombreTabla, Object[] valores, String... campos) {
		abrirConexion();
		Statement statement;
		try {
			String camposRefact = "";
			for (String string : campos) {
				camposRefact += string + ",";
			}
			camposRefact = camposRefact.substring(0, camposRefact.lastIndexOf(','));

			String val = arrayObjectAInsert(valores);

			String query = "insert into " + nombreTabla + " (" + camposRefact + ") values(" + val + ");";

			statement = con.createStatement();
			int salida = statement.executeUpdate(query);
			// System.out.println(salida != 0 ? "Registro insertado" : "Registro NO
			// insertado");
			return salida != 0;
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			cerrarConexion();
		}
		return false;
	}

	public static void leerDatos(String nombreTabla) {
		abrirConexion();
		Statement statement;
		ResultSet rs = null;
		try {
			String query = String.format("select * from " + nombreTabla);
			statement = con.createStatement();
			rs = statement.executeQuery(query);
			int creg = 1;
			while (rs.next()) {
				System.out.println("Registro " + creg + ": ");
				for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
					System.out.println(rs.getString(i));
				}

			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			cerrarConexion();
		}
	}

	public void buscarPorNombre(String nombreTabla, String name) {
		abrirConexion();
		Statement statement;
		ResultSet rs = null;
		try {
			String query = String.format("select * from %s where name= '%s'", nombreTabla, name);
			statement = con.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				System.out.print(rs.getString("empid") + " ");
				System.out.print(rs.getString("name") + " ");
				System.out.println(rs.getString("address"));

			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			cerrarConexion();
		}
	}

	public static void repintarJTable(JTable tabla, DefaultTableModel modelo) {
		modelo = insertarRegistrosAJTable("Modelos", modelo);
		tabla.setModel(modelo);
		tabla = centrarRegistrosEnJTable(tabla);
	}

	public static boolean borrarPorNombre(String nombreTabla, String name) {
		abrirConexion();
		Statement statement;
		try {
			String query = String.format("delete from %s where nombre='%s'", nombreTabla, name);
			statement = con.createStatement();
			statement.executeUpdate(query);
			return true;
			// System.out.println(salida != 0 ? "Registro borrado" : "Registro NO borrado");
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			cerrarConexion();
		}
		return false;
	}

	public static boolean actualizarRegistro(String nombreTabla, String nombreViejo, String nuevoNombre) {
		abrirConexion();
		Statement statement;
		try {
			String query = String.format("update %s set nombre='%s' where nombre='%s'", nombreTabla, nuevoNombre,
					nombreViejo);
			statement = con.createStatement();
			int act = statement.executeUpdate(query);
			if (act > 0) {
				return true;
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			cerrarConexion();
		}
		return false;
	}

	public static boolean actualizarRegistro(String nombreTabla, String nombreViejo, String nuevoNombre,
			String direccionXML) {
		abrirConexion();
		Statement statement;
		try {
			File archivoXML = new File(direccionXML);

			String query = "	UPDATE " + nombreTabla + " SET nombre = '" + nuevoNombre
					+ "', xml = XMLPARSE(DOCUMENT CONVERT_FROM(pg_read_binary_file('" + archivoXML.getAbsolutePath()
					+ "'), 'UTF8')) WHERE nombre = '" + nombreViejo + "';";
			statement = con.createStatement();
			int act = statement.executeUpdate(query);
			if (act > 0) {
				return true;
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			cerrarConexion();
		}
		return false;
	}

	public static void borrarTabla(String nombreTabla) {
		abrirConexion();
		Statement statement;
		try {
			String query = String.format("drop table %s", nombreTabla);
			statement = con.createStatement();
			statement.executeUpdate(query);
			System.out.println("Tabla borrada");
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			cerrarConexion();
		}
	}

	public static Object[] parsearARegistro(File modeloXML, String nombreModelo) {
		Path path = Paths.get(modeloXML.getAbsolutePath());
		long size;
		Object[] registro = null;
		try {
			size = Files.size(path) / 1024;

			BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);

			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

			registro = new Object[] { modeloXML, size, df.format(new Date(attr.creationTime().toMillis())),
					nombreModelo };
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return registro;
	}

	public static void insertarRegistroInicial() {
		File modeloXML = new File("C:/Users/Alumno/Desktop/haarcascade_frontalface_alt2.xml");
		try {

			Object[] primerRegis = parsearARegistro(modeloXML, "Ejemplo.xml");
			Metodos_BBDD.insertarRegistroCompleto("Modelos", primerRegis);

		} catch (Exception e) {

		}
	}

	public static DefaultTableModel insertarRegistrosAJTable(String nombreTabla, DefaultTableModel modelo) {
		abrirConexion();
		Statement statement;
		ResultSet rs = null;
		modelo = new DefaultTableModel() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}
		};
		modelo = crearColumnas(modelo);
		try {
			String query = String.format("select * from " + nombreTabla);

			statement = con.createStatement();
			rs = statement.executeQuery(query);

			Object[] reg = new Object[rs.getMetaData().getColumnCount()];

			while (rs.next()) {
				for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
					if (rs.getString(i) != null && i == rs.getMetaData().getColumnCount()) {
						reg[i - 1] = "Correcto";
					} else if (i == 1) {
						reg[i - 1] = rs.getString(i + 3);
					} else {
						reg[i - 1] = rs.getString(i);

					}
					// System.out.println(rs.getString(i));

				}
				modelo.addRow(reg);

			}

			return modelo;

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			cerrarConexion();
		}
		return null;
	}

	public static JTable centrarRegistrosEnJTable(JTable tabla) {
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tabla.getColumnCount(); i++) {
			tabla.getColumnModel().getColumn(i).setCellRenderer(tcr);
		}
		return tabla;
	}

	public static String seleccionarModelo(int JFileOpcion) {
		JFileChooser jfc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos .xml", "xml");
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.setFileFilter(filter);
		jfc.setFileSelectionMode(JFileOpcion);
		String carpeta = "";
		if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File ficheroCarpeta = jfc.getSelectedFile();
			carpeta = ficheroCarpeta.getAbsolutePath();

		} else {
			return null;
		}
		return carpeta;

	}

}
