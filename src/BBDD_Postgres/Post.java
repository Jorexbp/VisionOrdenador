package BBDD_Postgres;

import java.util.Hashtable;

public class Post {
	public static void main(String args[]) {
		
		ConexionBBDD db = new ConexionBBDD();
		//db.borrarTabla("PruebaJava");
		
		

		
//		Object[] array = new Object[]{"Hola", 12, 4.5, 's'};
//		for (Object elemento : array) {
//		    System.out.println(elemento.getClass().getName());
//		}
		
		Hashtable<String,String> ht = new Hashtable<>();
		
		ht.put("Nombre", "String"	);
		ht.put("N_Entrenamientos", "Integer");
		ht.put("Tamano", "Double");
		ht.put("Fecha", "Date");
		ht.put("Modelo", "XML");
		
		db.crearTabla("PruebaJava",ht);
//		
//		Object[] valoresRegistro = new Object[] {};
//		
//		//db.insertarRegistroCompleto("PruebaJava", args);
//		
//		String estadoConexion = db.getEstadoConexion() ? "abierta":"cerrada";
//		System.out.println("Conexion "+estadoConexion);
		
db.ordenColumnas("PruebaJava");
		
	
		
		
	}
}
