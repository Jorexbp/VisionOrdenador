package Entrenamiento;

import java.io.IOException;

public class borrar {

	private static void crearSamples() {
		String addr = "C:/Users/Alumno/Desktop/Carpeta_Origen_Destino/Destino";
		String addrSample = "C:/Users/Alumno/Desktop/Carpeta_Origen_Destino/opencv_createsamples.exe";
		String posTxt = addr + "/pos.txt";
		String posVec = addr + "/pos.vec";
		String nSamples = Integer.toString(100);
		// SAMPLES.EXE NECESITA SER PADRE DE LAS FOTOS
		String cmd = "cmd /c start cmd.exe /k ";
		String comandoSamples = cmd
				+ "C:/Users/Alumno/Desktop/Carpeta_Origen_Destino/opencv_createsamples.exe -info \"" + posTxt
				+ "\" -w 24 -h 24 -num \"" + nSamples + "\" -vec \"" + posVec + "\"";
		try {
			Runtime.getRuntime().exec(comandoSamples);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void crearVec() {
		String carpetaOrigen = "C:/Users/Alumno/Desktop/Samples personas/RazaBlanca";

		String carpetaDestino = "C:/Users/Alumno/Desktop/Carpeta Origen_Destino/Destino/pos3.txt";
		String cmd = "cmd /c start cmd.exe /k ";

		String comando = cmd + "lib/annotation/opencv_annotation.exe --annotations = \"" + carpetaDestino
				+ "\" --images=\"" + carpetaOrigen + "\"";

		try {
			Runtime.getRuntime().exec(comando);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		crearSamples();

	}

}
