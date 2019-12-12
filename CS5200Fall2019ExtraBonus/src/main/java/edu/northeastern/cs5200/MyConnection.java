package edu.northeastern.cs5200;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://jjdatabase.c6zuuyi0sch9.us-east-2.rds.amazonaws.com/hw6";
	private static final String USER = "JJMiao";
	private static final String PASSWORD = "12345678";

	public static java.sql.Connection getConnection() throws ClassNotFoundException, SQLException {
    	Class.forName(DRIVER);
    	return DriverManager.getConnection(URL, USER, PASSWORD);
	}
	
	public static void closeConnection(java.sql.Connection conn) {
	   	 try {
	   		 conn.close();
	   	 } catch (SQLException e) {
	   		 // TODO Auto-generated catch block
	   		 e.printStackTrace();
	   	 }
	}

}
