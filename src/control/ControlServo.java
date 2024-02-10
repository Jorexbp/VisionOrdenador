package control;

import java.util.concurrent.locks.ReentrantLock;

import jssc.SerialPort;
import jssc.SerialPortException;

public class ControlServo {
	private SerialPort serialPort = new SerialPort("COM4");
	private boolean parar;
	private ReentrantLock bloqueo = new ReentrantLock();
	private boolean giroDch = false;
	private int i = 90;

	public ControlServo() {
	}

	public void buscar() {
		try {
			bloqueo.lock();
			serialPort.openPort();

			serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			while (!Buscador.hayCoincidencia() && !parar) {
				if (parar) {
					break;
				}

				
					sendAngle(100);
					Thread.sleep(500);
				
				
				
					sendAngle(80);
					Thread.sleep(500);
				
				if (parar) {

					break;
				}

			}
			try {
				Thread.sleep(500);

			} catch (Exception e) {
				// TODO: handle exception
			}
			//serialPort.writeString(90 + "\n");

			serialPort.closePort();
			bloqueo.unlock();
			parar = false;
		} catch (SerialPortException | InterruptedException e) {


		} finally {
			try {
				serialPort.closePort();
			} catch (SerialPortException e) {

			}
		}
	}

	private void sendAngle(int angle) throws SerialPortException {
		serialPort.writeString(angle + "\n");
	}

	public void parar() throws SerialPortException {
		parar = true;

	}
}
