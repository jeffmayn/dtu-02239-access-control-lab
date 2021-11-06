package logic;

import java.sql.*;
import java.util.Random;

public class Database {
	Connection c = null;
	Statement stmt = null;
	Crypto crypto = new Crypto();

	private ResultSet getQueryResult(String query, String... args) {

		//String sql = "select * from users where user = '" + user + "'";
		try {
			PreparedStatement ps = c.prepareStatement(query);
			for (int i = 0; i < args.length; i++) {
				ps.setString(i+1, args[i]);
			}
			ResultSet result = ps.executeQuery();
			return (result.next()) ? result : null;
		} catch (SQLException e) {
			System.out.println("Error getting query result.");
			//e.printStackTrace();
		}
		return null;
	}

	private String getStringFromResultSet(ResultSet rs, String columnLabel){
		try {
			return rs.getString(columnLabel);
		} catch (SQLException e) {
			System.out.println("Error retrieving " + columnLabel);
		} catch (NullPointerException e) {
			System.out.println("Error: ResultSet is empty (null). Cannot retrieve " + columnLabel);
		}
		return "";
	}

	public String[] getCredentials(String user) {
		String query = "select * from users where user = ?";
		ResultSet userInfo = getQueryResult(query, user);
		String password = getStringFromResultSet(userInfo, "password");
		String salt = getStringFromResultSet(userInfo, "salt");

		return new String[] {password, salt};
	}

	public void initialiseDatabase() {
		try {
			Random rand = new Random();
			int rand_int = rand.nextInt(1000);
			c = DriverManager.getConnection("jdbc:sqlite:database\\database" + rand_int + ".db");

			// create table
			String sql = "create table users (user varchar(20), password varchar(200), salt varchar(200))";
			stmt = c.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();

			// populate table (user, password)
			stmt = c.createStatement();

			sql = "insert into users values ('admin','admin', '22-10-2021:21.18xx')";
			stmt.executeUpdate(sql);

			sql = "insert into users values ('jeff','" + crypto.hash("password22", "22-10-2021:21.18zz") + "','22-10-2021:21.18zz')";
			stmt.executeUpdate(sql);
			stmt.close();

		} catch ( Exception e ) {
			System.err.println("[Server]: " + e.getClass().getName() + " --> " + e.getMessage() );
			System.exit(0);
		}
	}

	public void disconnect() {
		try {
			c.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
