package Entrenamiento;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Positivo_negativo {

	private static void crearCarpetaNegativos() {
		File carpetaPositivos = new File("C:\\Users\\Alumno\\Desktop\\Samples personas\\RazaNegra");
		
		String addrOrigen = "C:\\Users\\Alumno\\Desktop\\Samples personas\\RazaBlanca";
		String addrFin = "C:\\\\Users\\\\Alumno\\\\Desktop\\\\Samples personas\\\\neg\\negative.txt";
		copiarFichero(carpetaPositivos,addrFin,"negative");
			
		// C:\Users\Alumno\Desktop\opencv\build\x64\vc16\bin\opencv_annotation.exe --annotations=pos.txt --images=RazaBlanca/
		// C:\Users\Alumno\Desktop\opencv_3.4.15\build\x64\vc15\bin\opencv_createsamples.exe -info pos.txt -w 24 -h 24 -num 100 -vec pos.vec
		// es personas>C:\Users\Alumno\Desktop\opencv_3.4.15\build\x64\vc15\bin\opencv_traincascade.exe -data cascade/ -vec pos.vec -bg neg.txt -w 24 -h 24 -numPos 90 -numNeg 100
	}



private static void copiarFichero(File fichero,String addrFin,String modo)  {
		
		BufferedWriter f;
		try {
			String nombres="";
			for (File fic : fichero.listFiles()) {
				nombres += modo+"\\"+fic.getName()+"\n";
			}
			
			
			f = new BufferedWriter(new FileWriter(addrFin.trim(), false));
			
			
			
			f.write(nombres);
			f.newLine();
			f.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	public static void main(String args[]) {
		crearCarpetaNegativos();
		
	}

}
