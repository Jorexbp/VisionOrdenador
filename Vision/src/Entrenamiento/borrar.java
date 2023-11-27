package Entrenamiento;

import java.io.IOException;

public class borrar {

	private static void crearSamples() {
		String carpetaDestino = "C:\\Users\\Alumno\\Desktop\\Carpeta Origen_Destino\\Destino";
		String carpetaOrigen = "C:/Users/Alumno/Desktop/Carpet Origen_Destino/Origen";

		String posTxt = "" + carpetaDestino + "\\pos.txt";
		String posVec = "" + carpetaDestino + "\\pos.vec";
		String nSamples = Integer.toString(50);

		String cmd = "cmd /c start cmd.exe /k ";
		String comandoSamples = cmd + "lib\\samples\\opencv_createsamples.exe -info \"" + posTxt
				+ "\" -w 24 -h 24 -num \"" + nSamples + "\" -vec \"" + posVec + "\"";
		try {
			Runtime.getRuntime().exec(comandoSamples);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void crearVec() {
		String carpetaOrigen = "C:/Users/Alumno/Desktop/Carpet Origen_Destino/Origen/pos";

		String carpetaDestino = "C:\\Users\\Alumno\\Desktop\\Carpeta Origen_Destino\\Destino\\pos.txt";
		String cmd = "cmd /c start cmd.exe /k ";

		String comando = cmd + "lib\\annotation\\opencv_annotation.exe --annotations = \"" + carpetaDestino
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
