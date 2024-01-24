package control;

import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

import jssc.SerialPort;
import jssc.SerialPortException;

public class ControlServo {
	private SerialPort serialPort = new SerialPort("COM4");
	private boolean parar;
	private ReentrantLock bloqueo = new ReentrantLock();

	public ControlServo() {
	}

	public void buscar() {
		try {
			bloqueo.lock();
			serialPort.openPort();

			serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			while (!Buscador.hayCoincidencia() && !parar) {
				
				for (int angle = 0; angle <= 90; angle ++) {
					System.out.println(parar);
					if(parar) {
						
						break;
					}
						
					System.out.println(angle);
					sendAngle(angle);
					
				}
				
			}
			try {
				Thread.sleep(250);
				
			}catch (Exception e) {
				// TODO: handle exception
			}
			serialPort.writeString(90 + "\n");
			
			serialPort.closePort();
			bloqueo.unlock();
			parar = false;
		} catch (SerialPortException e) {
			e.printStackTrace();
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
