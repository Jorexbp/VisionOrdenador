package Entrenamiento;

import java.nio.file.Files;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import OpenCV.DeteccionCara;

public class borrar {
	private static String datos = "";
	private static String carpetaOriginal;
	private static String carpetaOriginalNegativa = "C:/Users/Alumno/Desktop/Samples_personas/RazaNegra";
	private static String carpetaPadre = "C:/Users/Alumno/Desktop/Carpeta_Origen_Destino";
	private static String carpetaDestino = "C:/Users/Alumno/Desktop/Carpeta_Origen_Destino/Destino";
	private static String carpetaOriginalPositiva = "C:/Users/Alumno/Desktop/Fotos";

	private static void crearXML() {

		int nStages = 10;
		int numPos = 20;
		int numNeg = 15;
		String destino = carpetaPadre + "/cascade/";

		String dirVec = carpetaDestino + "/pos.vec";
		
		String dirTxtNeg = carpetaOriginalNegativa + "/neg.txt";
		String dirExe = "lib\\traincascade\\opencv_traincascade.exe";
		String comTR = dirExe + " -data " + destino + " -vec " + dirVec + " -bg " + dirTxtNeg + " -w 24 -h 24 -numPos "
				+ numPos + " -numNeg " + numNeg + " -numStages " + nStages;

		String cmd = "cmd /c start cmd.exe /k ";
		String comandoSamples = cmd + comTR;
		try {
			Runtime.getRuntime().exec(comandoSamples);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void crearSamples() {
		String addrSample = "lib\\samples\\opencv_createsamples.exe";
		String posTxt = carpetaOriginalPositiva + "\\pos.txt";
		String posVec = carpetaDestino + "\\pos.vec";
		String nSamples = Integer.toString(100);

		String cmd = "cmd /c start cmd.exe /k ";
		String comandoSamples = cmd + addrSample + " -info \"" + posTxt + "\" -w 24 -h 24 -num \"" + nSamples
				+ "\" -vec \"" + posVec + "\"";
		try {
			Runtime.getRuntime().exec(comandoSamples);
//			rellenarTextArea();
//			cambiarAUsable(lcrearXML, bcrearXML);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		crearXML();
	}

}
