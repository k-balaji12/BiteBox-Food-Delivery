package com.app.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/bitebox";

    private static final String USERNAME = "root";

    private static final String PASSWORD = "Narendra1212@";

    public static Connection getConnection() throws SQLException {
    	Connection con=null;
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			 con=DriverManager.getConnection(URL,USERNAME,PASSWORD);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return con;
    }
}
