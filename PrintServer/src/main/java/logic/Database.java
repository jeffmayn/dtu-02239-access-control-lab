package logic;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class Database {
	Connection c = null;
	Statement stmt = null;
	Crypto crypto = new Crypto();
	
	public String[] getCredentials(String user) {
		String sql = "select * from users where user = '" + user + "'";
		String password = "";
		String salt = "";
				try {
					stmt = c.createStatement();
					ResultSet result = stmt.executeQuery(sql);
					
					while(result.next()) {
						password = result.getString("password");	
						salt = result.getString("salt");
					}		
				} catch (SQLException e) {
					System.out.println("ERROR!!!");
					e.printStackTrace();
				}	
				return new String[] {password, salt};	
	}
	
	public void initialiseDatabase() throws RemoteException {
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
