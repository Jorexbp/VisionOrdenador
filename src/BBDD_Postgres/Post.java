package BBDD_Postgres;

import java.sql.Connection;

public class Post {
	public static void main(String args[]) {
		ConexionBBDD db = new ConexionBBDD();
		Connection conn = db.getConexion();
		db.createTable(conn,"employee");
        //db.insert_row(conn,"employee","Rajat","India");
       // db.update_name(conn,"employee","Rahul","Rajat");
       // db.search_by_name(conn,"employee","Rajat");
       // db.delete_row_by_name(conn,"employee","Rajat");
     //   db.delete_row_by_id(conn,"employee",2);
      //  db.read_data(conn,"employee");
        db.delete_table(conn,"employee");
		
	}
}
