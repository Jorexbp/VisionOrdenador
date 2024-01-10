package OpenCV;



import com.github.sarxos.webcam.Webcam;

import java.util.List;


public class CameraSelectionApp {

	public static void main(String[] args) {
		List<Webcam> webcams = Webcam.getWebcams();
		for (Webcam webcam : webcams) {
			System.out.println("Cámara encontrada: " + webcam.getName());
		}
	}

}
