package Entrenamiento;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import OpenCV.CamaraEntrenamiento;

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
