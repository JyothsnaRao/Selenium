package First;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnection {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// System.out.println("Hi");

		try {
			Class.forName("com.mysql.jdbc.Driver");
			String dbUrl = "jdbc:mysql://localhost:3306/envirta";
			String User = "root";
			String password = "admin";
			Connection con = DriverManager.getConnection(dbUrl, User, password);
			System.out.println("CONNECTED");
			
			Statement stmt = con.createStatement();
		    ResultSet rs = stmt.executeQuery("SELECT * FROM food");
		    
		    while(rs.next())
		    {
		        System.out.println(rs.getString(1)); 
		    }
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
	}
}
