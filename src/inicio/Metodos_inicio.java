package inicio;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;

import javax.swing.JComponent;
import javax.swing.JTextArea;

import Entrenamiento.App_Entrenamiento;

public class Metodos_inicio {
	public static void recogerLeyPrivacidadYLeyProteccionDeDatos(JTextArea textArea) {
		URL url = App_Entrenamiento.class.getResource("/privacidad.txt");
		URL url2 = App_Entrenamiento.class.getResource("/proteccion.txt");
		File leyPriv = new File(url.getFile());
		File leyProt = new File(url2.getFile());
		Scanner sc = null;

		try {
			sc = new Scanner(leyPriv);

			while (sc.hasNextLine())
				textArea.append(sc.nextLine());

			sc.close();
			sc = new Scanner(leyProt);
			textArea.append("\n\n");

			while (sc.hasNextLine())
				textArea.append(sc.nextLine());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			sc.close();
		}

	}

	public static void visibilidad(boolean bool, javax.swing.JComponent... componente) {
		for (JComponent jComponent : componente) {
			jComponent.setVisible(bool);
		}
	}
}
