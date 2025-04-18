package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBConnection {
//    private static Connection connection;
//    public static Connection getConnection() {
//        if (connection != null) return connection;
//        try {
//            String url = DBPropertyUtil.getPropertyString("db.properties");
//            if (url == null) throw new SQLException("Connection string is null");
//            connection = DriverManager.getConnection(url);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return connection;
//    }
	public static void main(String[] agrs) {
		Connection conn = null;
		try {
			String dbURL = "jdbc:mysql://localhost:3306/ecommerce";
			String dbUName = "root";
			String dbPass = "Javasree2003#";
			conn = DriverManager.getConnection(dbURL, dbUName, dbPass);
			if(conn != null) {
				System.out.println("Connected to the database");
			}					
		}
		catch (SQLException ex) {
			System.out.println("An error ocured while connecting to the database");
			System.out.println(ex.getMessage());
		}
	}
}