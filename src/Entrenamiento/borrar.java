package Entrenamiento;

public class borrar {

	public static void main(String[] args) {
		String carpetaPadre = "C:/Users/Alumno/Desktop/moviendodeverdad";
		String carpetaNegativa = "C:/Users/Alumno/Desktop/MuchasFotos/Neg";
		Metodos_app.carpetaPos = "C:/Users/Alumno/Desktop/MuchasFotos/pos";
		Metodos_app.carpetaNeg=carpetaNegativa;
		Metodos_app.crearXML(carpetaPadre, carpetaNegativa);
	}

}
