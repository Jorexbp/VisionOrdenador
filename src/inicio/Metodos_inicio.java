package inicio;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JComponent;
import javax.swing.JTextArea;

public class Metodos_inicio {
	public static void recogerLeyPrivacidadYLeyProteccionDeDatos(JTextArea textArea) {

		File leyPriv = new File("leyes\\privacidad.txt");
		File leyProt = new File("leyes\\proteccion.txt");
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
