package BBDD_Postgres;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class ConexionBBDD {
	private Connection con = null;

	public boolean getEstadoConexion() {
		if (con == null)
			return false;
		else
			return true;
	}

	public boolean abrirConexion() {
		cerrarConexion();
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Prueba", "postgres", "admin");
			if (con == null) {
				JOptionPane.showMessageDialog(null, "No se pudo conectar a la BBDD");
			} else
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean cerrarConexion() {
		try {
			con = null;
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "No se ha podido cerra la conexión a la BBDD");
		}
		return false;
	}

	public String crearColumnas(Hashtable<String, String> tabla, int primaryKey) {
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

	public void crearTabla(String nombreTabla, Hashtable<String, String> columnas, int primaryKey) {
		abrirConexion();
		Statement statement;
		try {
			String query = "create table " + nombreTabla + "(" + crearColumnas(columnas, primaryKey) + ");";
			statement = con.createStatement();
			statement.executeUpdate(query);
			System.out.println("Tabla creada");
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			cerrarConexion();
		}
	}

	private String arrayObjectAInsert(Object[] valores) {
		String val = "";

		for (int i = 0; i < valores.length; i++) {
			if (valores[i].getClass().getName().equals("java.lang.String")
					|| valores[i].getClass().getName().equals("java.sql.Date")) {
				val += "'" + valores[i] + "',";
			} else if (valores[i].toString().contains("xml")) {
				val += "XMLPARSE(DOCUMENT CONVERT_FROM(pg_read_binary_file(\'" + valores[i].toString()
						+ "\'), 'UTF8')),";
			} else
				val += valores[i] + ",";
		}
		if (val.lastIndexOf(',') == val.length() - 1) {
			val = val.substring(0, val.lastIndexOf(','));
		}
		return val.replace("\\", "/");
	}

	public ArrayList<String> ordenColumnas(String nombreTabla) {
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

	public void insertarRegistroCompleto(String nombreTabla, Object[] valores) {
		abrirConexion();
		Statement statement;
		try {

			String val = arrayObjectAInsert(valores);

			// System.out.println(val);
			String query = "insert into " + nombreTabla + " values(" + val + ");";
			statement = con.createStatement();
			statement.executeUpdate(query);
			System.out.println("Registro insertado");
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			cerrarConexion();
		}
	}

	public void insertarRegistroParcial(String nombreTabla, Object[] valores, String... campos) {
		abrirConexion();
		Statement statement;
		try {
			String camposRefact = "";
			for (String string : campos) {
				camposRefact += string + ",";
			}
			camposRefact = camposRefact.substring(0, camposRefact.lastIndexOf(','));

			String val = arrayObjectAInsert(valores);
			System.out.println(val);
			
			
			String query = "insert into " + nombreTabla + " (" + camposRefact + ") values(" + val + ");";
		
			statement = con.createStatement();
			statement.executeUpdate(query);
			System.out.println("Registro insertado");
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			cerrarConexion();
		}
	}

	public void leerDatos(String nombreTabla) {
		abrirConexion();
		Statement statement;
		ResultSet rs = null;
		try {
			String query = String.format("select * from %s", nombreTabla);
			statement = con.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				System.out.print(rs.getString("empid") + " ");
				System.out.print(rs.getString("name") + " ");
				System.out.println(rs.getString("Address") + " ");
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			cerrarConexion();
		}
	}

	public void actualizarTabla(String nombreTabla, String old_name, String new_name) {
		abrirConexion();

		// TODO ESTE METODO NO ACTUALIZA CORRECTAMENTE
		Statement statement;
		try {
			String query = String.format("update %s set name='%s' where name='%s'", nombreTabla, new_name, old_name);
			statement = con.createStatement();
			statement.executeUpdate(query);
			System.out.println("Registro actualizado");
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

	public void buscarPorID(String nombreTabla, int id) {
		abrirConexion();
		Statement statement;
		ResultSet rs = null;
		try {
			String query = String.format("select * from %s where empid= %s", nombreTabla, id);
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

	public void borrarPorNombre(String nombreTabla, String name) {
		abrirConexion();
		Statement statement;
		try {
			String query = String.format("delete from %s where name='%s'", nombreTabla, name);
			statement = con.createStatement();
			statement.executeUpdate(query);
			System.out.println("Registro borrado");
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			cerrarConexion();
		}
	}

	public void borrarPorID(String nombreTabla, int id) {
		abrirConexion();
		Statement statement;
		try {
			String query = String.format("delete from %s where empid= %s", nombreTabla, id);
			statement = con.createStatement();
			statement.executeUpdate(query);
			System.out.println("Registro borrado");
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			cerrarConexion();
		}
	}

	public void borrarTabla(String nombreTabla) {
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

}
