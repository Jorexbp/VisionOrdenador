package Entrenamiento;

import java.io.File;

public class Limpiador {

	private static void limpiarCarasDetectadas() {
		File carpetaDeteccion = new File("Detecciones");
		for (File foto : carpetaDeteccion.listFiles()) {
			if(!foto.isDirectory()) {
				foto.delete();
			}
		}
		System.out.println("Limpieza finalizada");
	}
	
	public static void main(String[] args) {
	limpiarCarasDetectadas();
	}

}
