package logic;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class Database {
	Connection c = null;
	Statement stmt = null;
	Crypto crypto = new Crypto();
	String path = "log\\";

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
	
	public String getUserPermissions(String user, String permission) {
		String query = "select * from permissions where user = ?";
		ResultSet permissionsInfo = getQueryResult(query, user);
		String permissions = getStringFromResultSet(permissionsInfo, permission);
		
		return permissions;
		
	}

	public String[] getCredentials(String user) {
		String query = "select * from users where user = ?";
		ResultSet userInfo = getQueryResult(query, user);
		String password = getStringFromResultSet(userInfo, "password");
		String salt = getStringFromResultSet(userInfo, "salt");

		return new String[] {password, salt};
	}
	
	private void sqlStatement(String sqlStmt) {
					try {
						stmt = c.createStatement();
						stmt.executeUpdate(sqlStmt);
						stmt.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}			
	}
	
	private void dummyData() {
		// populate tables
		sqlStatement("insert into users values ('jeff','" + crypto.hash("password22", "22-10-2021:21.18zz") + "','22-10-2021:21.18zz')");
		sqlStatement("insert into users values ('alice','" + crypto.hash("password22", "22-10-2021:21.18zz") + "','22-10-2021:21.18zz')");
		sqlStatement("insert into users values ('bob','" + crypto.hash("password22", "22-10-2021:21.18zz") + "','22-10-2021:21.18zz')");
		sqlStatement("insert into users values ('cecilia','" + crypto.hash("password22", "22-10-2021:21.18zz") + "','22-10-2021:21.18zz')");
		sqlStatement("insert into users values ('david','" + crypto.hash("password22", "22-10-2021:21.18zz") + "','22-10-2021:21.18zz')");
		sqlStatement("insert into users values ('erica','" + crypto.hash("password22", "22-10-2021:21.18zz") + "','22-10-2021:21.18zz')");
		sqlStatement("insert into users values ('fred','" + crypto.hash("password22", "22-10-2021:21.18zz") + "','22-10-2021:21.18zz')");
		sqlStatement("insert into users values ('george','" + crypto.hash("password22", "22-10-2021:21.18zz") + "','22-10-2021:21.18zz')");
		
		sqlStatement("insert into permissions values ('jeff',1,1,1,1,1,1,1,1,1)");
		sqlStatement("insert into permissions values ('alice',1,1,1,1,1,1,1,1,1)");
		sqlStatement("insert into permissions values ('bob',0,0,0,1,1,1,1,1,1)");
		sqlStatement("insert into permissions values ('cecilia',1,1,1,0,0,1,0,0,0)");
		sqlStatement("insert into permissions values ('david',1,1,0,0,0,0,0,0,0)");
		sqlStatement("insert into permissions values ('erica',1,1,0,0,0,0,0,0,0)");
		sqlStatement("insert into permissions values ('fred',1,1,0,0,0,0,0,0,0)");
		sqlStatement("insert into permissions values ('george',1,1,0,0,0,0,0,0,0)");
	}
	
	private boolean tableExists(String tableName){
        try{
            DatabaseMetaData md = c.getMetaData();
            ResultSet rs = md.getTables(null, null, tableName, null);
            if(rs.next()) {
            	return true;
            }
        }catch(SQLException ex){
          System.out.print(ex);
        }
        return false;
    }

	public void initialiseDatabase() {
		try {
			
			  File file = new File ("database\\database.db");

			  if(file.exists()) {
				  c = DriverManager.getConnection("jdbc:sqlite:database\\database.db");
			     } else {
			    	 System.out.println("Database does not exists. See Database.java line 115");

			    	 c = DriverManager.getConnection("jdbc:sqlite:database\\database.db");

			    	 if(tableExists("users")){
			    		 //System.out.println("table exists already");
			    		} else {
			    			sqlStatement("create table users (user varchar(20), password varchar(200), salt varchar(200))");
							sqlStatement("create table permissions (user varchar(20), "
																	+ "print boolean(0,1), "
																	+ "queue boolean(0,1), "
																	+ "topQueue boolean(0,1), "
																	+ "start boolean(0,1), "
																	+ "stop boolean(0,1), "
																	+ "restart boolean(0,1), "
																	+ "status boolean(0,1), "
																	+ "readConfig boolean(0,1), "
																	+ "setConfig boolean(0,1)"
																	+ ")");
							// populates the database									
							dummyData();
			    		}	
			    	 
			     }
		} catch ( Exception e ) {
			System.err.println("[Server]: " + e.getClass().getName() + " --> " + e.getMessage() );
			System.exit(0);
		}
	}
	
	public void disconnect() {
		try {
			c.close();
			//stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createNewUser(String uid, String password, String salt, Session session, Log log) {
		
		sqlStatement("insert into users values ('" + uid + "','" + crypto.hash(password, salt) + "','" + salt + "')");
		sqlStatement("insert into permissions values ('" + uid + "',0,0,0,0,0,0,0,0,0)");
		System.out.println(uid + " has been created! Assigned with no privileges yet. Contact a manager");
		
		try {
			log.writeLogEntry("[server]: created user: " + uid, path + "server.log");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
