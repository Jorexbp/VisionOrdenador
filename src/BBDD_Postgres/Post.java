package BBDD_Postgres;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JOptionPane;

public class Post {
	public static void main(String args[]) {

		ConexionBBDD db = new ConexionBBDD();
		 db.borrarTabla("PruebaJava");


		Hashtable<String, String> ht = new Hashtable<>();

		ht.put("Nombre", "String");
		ht.put("N_Entrenamientos", "Integer");
		ht.put("Tamano", "Double");
		ht.put("Fecha", "Date");
		ht.put("Modelo", "XML");

		db.crearTabla("PruebaJava", ht,4);
//		
//		
//		
//		//db.insertarRegistroCompleto("PruebaJava", args);
//		
//		String estadoConexion = db.getEstadoConexion() ? "abierta":"cerrada";
//		System.out.println("Conexion "+estadoConexion);

//		ArrayList<String> cols = db.ordenColumnas("PruebaJava");
//		Object[] val = new Object[cols.size()];
//		for (int i = 0; i < val.length; i++) {
//			val[i] = JOptionPane.showInputDialog("Columna: " + cols.get(i));
//		}
//
//		db.insertarRegistroCompleto("PruebaJava", val);
		
		Object obj[] = new Object[] {"ModPru.xml","NomModPru"};
		db.insertarRegistroParcial("PruebaJava",obj , "Modelo","Nombre");

	}
}
