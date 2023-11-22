package Entrenamiento;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Positivo_negativo {

	private static void crearCarpetaNegativos() {
		File carpetaPositivos = new File("C:/Users/Alumno/Desktop/Samples personas/RazaNegra");

		String addrOrigen = "C:/Users/Alumno/Desktop/Samples personas/RazaBlanca";
		String addrFin = "C://Users//Alumno//Desktop//Samples personas//neg/negative.txt";
		
		// C:\Users\Alumno\Desktop\opencv\build\x64\vc16\bin\opencv_annotation.exe
		 //--annotations=pos.txt --images=RazaBlanca
		// C:\Users\Alumno\Desktop\opencv_3.4.15\build\x64\vc15\bin\opencv_createsamples.exe
		//-info pos.txt -w 24 -h 24 -num 100 -vec pos.vec
		//C:\Users\Alumno\Desktop\opencv_3.4.15\build\x64\vc15\bin\opencv_traincascade.exe
		//-data cascade -vec pos.vec -bg neg.txt -w 24 -h 24 -numPos 90 -numNeg 100
		//-numStages 10
	}

	private static void ejecutarOpenCVannotations() {
		String carpetaDestino = "C:/Users/Alumno/Desktop/Carpeta Origen_Destino/Destino";
		String carpetaOriginal = "C:/Users/Alumno/Desktop/Samples personas/RazaBlanca";
		
		String destinoAnotacion = carpetaDestino + "/pos.txt";
		String comando1 = "cmd /c start cmd.exe /k \" cd C:/Users/Alumno/git/VisionOrdenador/Vision/lib/annotation && opencv_annotation.exe"
				+ " --annotations=\"" + destinoAnotacion + "\"" + " --images=\"" + carpetaOriginal + "\""; // COMANDO
		
		

		 
		try {
			Process proceso = Runtime.getRuntime().exec(comando1);
			
		} catch (IOException e) {

		}
	}

	public static void main(String args[]) {
		ejecutarOpenCVannotations();

	}

}
