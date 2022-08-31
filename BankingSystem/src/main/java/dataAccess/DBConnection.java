package dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	public static Connection getCon() {
        Connection con = null;
        try {
        	Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/BankingSystem";
            String un = "root";
            String pw = "Mysqldata#1";
             con = DriverManager.getConnection(url, un, pw);
             System.out.println("Connection Done");
            return con;
        } catch (SQLException | ClassNotFoundException e ) {
        	return null;
        }
    }

}
