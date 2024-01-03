package OperadorBBDD;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import Entrenamiento.Metodos_app;

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
	private static Statement statement = null;
	private static ResultSet rs = null;
	private static DatabaseMetaData dbmd;
	private static PreparedStatement preparedStatement;

	public static boolean abrirConexion() {
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
			con.close();
			statement.close();
			rs.close();
			dbmd = null;

			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "No se ha podido cerrar la conexión a la BBDD");
		}
		return false;
	}

	public boolean getEstadoConexion() {
		if (con == null)
			return false;
		else
			return true;
	}

	public static String crearColumnas(Hashtable<String, String> tabla, int primaryKey) {
		String columnas = "";
		for (Map.Entry<String, String> entry : tabla.entrySet()) {

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
				throw new IllegalArgumentException("Tipo de dato no compatible: " + entry.getValue());
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
		abrirConexion(); // Primero abrimos la conexión a la BBDD
		try {

			String query = "create table " + nombreTabla + "(" + crearColumnas(columnas, primaryKey) + ");";

			statement = con.createStatement(); // Creamos la query

			int salida = statement.executeUpdate(query); // Ejecutamos la creación de la tabla

			return salida != 0; // Se ejecutó correctamente y se creó o no la tabla
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			cerrarConexion(); // Asegurando que se cierra la conexión
		}
		return false; // No se ha creado nada por algún error de sintaxis o conexión
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

		try {

			dbmd = con.getMetaData();
			rs = dbmd.getColumns(null, null, nombreTabla.toLowerCase(), "%");
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

		try {
			String val = arrayObjectAInsert(valores);

			String query = "insert into " + nombreTabla + " values(" + val + ");";

			statement = con.createStatement();

			int salida = statement.executeUpdate(query);

			return salida != 0;
		} catch (Exception e) {
			System.out.println(e);
			if (e.toString().contains("duplicada")) {
				JOptionPane.showMessageDialog(null,
						"No puede repetirse el nombre de los modelos\n\tRegistro no insertado");
			} else {
				JOptionPane.showMessageDialog(null,
						"Asegúrese de que el XML sea legible por 'Todos' en\nla pestaña de seguridad\n\tRegistro no insertado ");
			}

		} finally {
			cerrarConexion();
		}
		return false;
	}

	public boolean insertarRegistroParcial(String nombreTabla, Object[] valores, String... campos) {
		abrirConexion();

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

	public static String buscarRegistro(String nombreTabla, int indice) {
		abrirConexion();

		String datosRegistro = "";
		try {
			String query = "SELECT * FROM " + nombreTabla + " WHERE ctid = (SELECT ctid FROM " + nombreTabla
					+ " ORDER BY ctid LIMIT 1 OFFSET ?)";
			preparedStatement = con.prepareStatement(query);

			preparedStatement.setInt(1, indice);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
					datosRegistro += rs.getString(i) + ",";
				}

			}
			datosRegistro = datosRegistro.substring(0, datosRegistro.lastIndexOf(','));

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			cerrarConexion();
		}
		return datosRegistro;
	}

	public static void repintarJTable(JTable tabla, DefaultTableModel modelo) {
		modelo = insertarRegistrosAJTable("Modelos", modelo);
		tabla.setModel(modelo);
		tabla = centrarRegistrosEnJTable(tabla);
	}

	public static boolean borrarRegistro(String nombreTabla, int indice) {
		abrirConexion();

		try {
			String consulta = "DELETE FROM " + nombreTabla + " WHERE ctid = (SELECT ctid FROM " + nombreTabla
					+ " ORDER BY ctid LIMIT 1 OFFSET ?)";

			preparedStatement = con.prepareStatement(consulta);
			preparedStatement.setInt(1, indice);
			int elim = preparedStatement.executeUpdate();

			return elim != 0 ? true : false;

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			cerrarConexion();
		}

		return false;
	}

	public static boolean actualizarRegistro(String nombreTabla, String valorDelCampoIdentificativo, String valorNuevo,
			String campoAModificar, String campoIdentificativoUnico) {
		abrirConexion();

		try {
			String query = "update " + nombreTabla + " set " + campoAModificar + "='" + valorNuevo + "' where "
					+ campoIdentificativoUnico + "='" + valorDelCampoIdentificativo + "';";
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

	public static boolean actualizarRegistroXML(String nombreTabla, int indice, String nuevoNombre,
			String direccionXML) {
		abrirConexion();

		try {
			File archivoXML = new File(direccionXML);

			String query = "	UPDATE " + nombreTabla + " SET nombre = '" + nuevoNombre
					+ "', xml = XMLPARSE(DOCUMENT CONVERT_FROM(pg_read_binary_file('" + archivoXML.getAbsolutePath()
					+ "'), 'UTF8')) WHERE  WHERE ctid = (SELECT ctid FROM " + nombreTabla
					+ " ORDER BY ctid LIMIT 1 OFFSET ?)";
			statement = con.createStatement();
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, indice);
			int act = preparedStatement.executeUpdate(query);
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

	public static boolean borrarTabla(String nombreTabla) {
		abrirConexion();

		try {
			String query = String.format("drop table %s", nombreTabla);
			statement = con.createStatement();
			int borrado = statement.executeUpdate(query);
			return borrado != 0 ? true : false;
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			cerrarConexion();
		}
		return false;
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
			e.printStackTrace();
		}

		return registro;
	}

	public static void descargarModeloXML(String nombreColumnaXML, String nombreArchivo, String nombreTabla,
			int indice) {
		abrirConexion();
		try {
			String query = "SELECT " + nombreColumnaXML + "  FROM " + nombreTabla + " WHERE ctid = (SELECT ctid FROM "
					+ nombreTabla + " ORDER BY ctid LIMIT 1 OFFSET ?) ";
			statement = con.createStatement();
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, indice);
			rs = preparedStatement.executeQuery();

			if (rs.next()) {
				String xmlData = rs.getString(nombreColumnaXML);
				JOptionPane.showMessageDialog(null, "Seleccione una carpeta donde guardar el archivo");
				FileWriter fileWriter = new FileWriter(
						Metodos_app.seleccionarCarpeta(JFileChooser.DIRECTORIES_ONLY) + "/" + nombreArchivo);

				fileWriter.write(xmlData);
				fileWriter.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cerrarConexion();
		}

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

		modelo = new DefaultTableModel() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		modelo = crearColumnas(modelo);
		try {
			String query = String.format("select * from " + nombreTabla);

			statement = con.createStatement();
			rs = statement.executeQuery(query);

			Object[] reg = new Object[rs.getMetaData().getColumnCount()];

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			while (rs.next()) {
				for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
					if (rs.getString(i) != null && i == rs.getMetaData().getColumnCount()) {
						try {
							@SuppressWarnings("unused")
							Document document = builder.parse(new InputSource(new StringReader(rs.getString(i-3))));

							reg[i - 1] = "XML válido";
						} catch (Exception e) {
							reg[i - 1] = "XML NO válido";
						}

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
