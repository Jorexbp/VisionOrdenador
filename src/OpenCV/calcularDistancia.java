package OpenCV;

import com.fazecast.jSerialComm.SerialPort;

public class calcularDistancia {
	private SerialPort sp = null;
	public calcularDistancia() {
		sp = SerialPort.getCommPort("COM4");
		sp.setComPortParameters(9600, 8, 1, 0); // default connection settings for Arduino
		sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
	}
	public  int getDistancia() {
	/* TODO ESTO ESTÁ SIN IMPLEMENTAR, FUNCIONA A TRAVÉS DE ARDUINO PARA DETECTAR
	 * LA DISTANCIA DE LOS OBJETOS, FUNCIONA PERO NECESITARÍA SUBIRLO A UN ARDUINO
	 * CON UNA CÁMARA PROPIA 
	 */
	

//		if (!sp.openPort()) {
//			return -1;
//		}
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		Scanner sc = null;
//		InputStream ips = sp.getInputStream();
//		sc = new Scanner(ips);
//		int n = sc.nextInt();
//		sc.close();
		//System.out.println(sc.next());
		return -1;
	}
}
