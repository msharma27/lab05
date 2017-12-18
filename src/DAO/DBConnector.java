package DAO;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnector {
	private static Connection con = null;

	private DBConnector() {
		//private constructor 
	}
	public static Connection createConnector() {

		if (con != null) {
			return con;
		}
		String servername = System.getenv("ICSI518_SERVER");
		int portnumber = Integer.parseInt(System.getenv("ICSI518_PORT"));
		String dbname = System.getenv("ICSI518_DB");
		String serverusername = System.getenv("ICSI518_USER");
		String serverpassword = System.getenv("ICSI518_PASSWORD");

		try {
			com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
			ds.setServerName(servername);
			ds.setPortNumber(portnumber);
			ds.setDatabaseName(dbname);
			ds.setUser(serverusername);
			ds.setPassword(serverpassword);

			con = ds.getConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public static void closeConnection() {
		try {
			con.close();
			con = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
