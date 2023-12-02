package BBDD_Postgres;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionBBDD {
	public static void main(String[] args) {
		
		Connection con = null;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/JavaJDBC");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
