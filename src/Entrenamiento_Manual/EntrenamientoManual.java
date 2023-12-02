package Entrenamiento_Manual;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import Entrenamiento.Metodos_app;

public class EntrenamientoManual {

	private static void crearCamara() {
		JOptionPane.showMessageDialog(null, "Seleccione una carpeta para guardar las fotos temporalmente");
		CamaraEntrenamiento.comenzarCamara(Metodos_app.seleccionarCarpeta(JFileChooser.DIRECTORIES_ONLY));
		// camara.comenzarCamara();
	}

	public static void main(String[] args) {
		crearCamara();
	}
}
