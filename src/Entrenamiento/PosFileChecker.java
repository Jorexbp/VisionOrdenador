package Entrenamiento;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PosFileChecker {
	public static void main(String[] args) {
		String posFilePath = "C:\\Users\\Alumno\\Desktop\\Carpeta Origen_Destino\\Destino\\pos.txt";

		try (BufferedReader br = new BufferedReader(new FileReader(posFilePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				// Split the line into parts
				line = line.substring(line.indexOf("  1  ")+5);
				String[] parts = line.split(" ");

				// Assuming the format is: image_file_path x y width height
					// Parse values
					int x = Integer.parseInt(parts[0]);
					int y = Integer.parseInt(parts[1]);
					int width = Integer.parseInt(parts[2]);
					int height = Integer.parseInt(parts[3]);

					// Check validity
					if (x < 0 || y < 0 || width <= 0 || height <= 0) {
						System.out.println("Invalid ROI in line: " + line);
					}
					// Add additional checks if needed
					if(x+width> 550 || y+height>550) {
						System.out.println("Dimensiones " + line);
						
					
				} 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
