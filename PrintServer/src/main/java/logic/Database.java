package logic;

import java.awt.List;
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
	
	
	public void assignRoleToUser(String uid, String[] roles, Session session, Log log) {
		
		
		boolean permission = getUserPermissions(session.uid, "admin");
		
		if(permission) {
			String q1 = "select * from users where user = ?";
			ResultSet permissionsInfo1 = getQueryResult(q1, uid);
			String userRoles = getStringFromResultSet(permissionsInfo1, "roles");
			
			String[] userRolesFromDB = userRoles.split("\\s*,\\s*");
			
			final String SEPARATOR = ",";
			StringBuilder roleBuilder = new StringBuilder();
			
			roleBuilder.append(userRoles);
			roleBuilder.append(SEPARATOR);

			for(String roleDB : userRolesFromDB) {
				for(String assigningRole : roles) {
					if(!assigningRole.equals(roleDB)) {	
						
						roleBuilder.append(assigningRole);
						roleBuilder.append(SEPARATOR);	
						
					} else {
						System.out.print(uid + " already has " + roleDB + "-rights!");
					}			
				}
			}
			sqlStatement("update users set roles = '" + roleBuilder + "' where user = '" + uid + "'");	
			try {
				log.writeLogEntry("[server]: user " + uid + " has been granted the roles: " + roleBuilder, path + "server.log");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("<" + session.uid + " is not allowed to grant roles to users>");
			try {
				log.writeLogEntry("[server]: user " + session.uid + " has been denied granting roles to: " + uid, path + "server.log");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public boolean getUserPermissions(String user, String permissionRequest) {
		
		boolean hasPermission = false;
		
		String q1 = "select * from users where user = ?";
		ResultSet permissionsInfo1 = getQueryResult(q1, user);
		String userRoles = getStringFromResultSet(permissionsInfo1, "roles");
		
		String[] roles = userRoles.split("\\s*,\\s*");
		
		for(String role : roles) {
			
		//	System.out.println("roles: " + role);
			
			String query = "select * from permissions where roles = ?";
			ResultSet permissionsInfo = getQueryResult(query, role);
			String permissions = getStringFromResultSet(permissionsInfo, permissionRequest);
			if(permissions.equals("1")){
				hasPermission = true;
			}	
		}

		return hasPermission;
		
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
						e.printStackTrace();
					}			
	}
	
	public void deleteUser(String user, Session session, Log log) {
		
		boolean permission = getUserPermissions(session.uid, "admin");
		
		if(permission) {
			sqlStatement("delete from users where user = '" + user + "'");
			try {
				log.writeLogEntry("[server]: user " + user + " has died in a car crash.. so sad.. deleting him", path + "server.log");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println(session.uid + " is not allowed to delete users");
		}
		
	}

	
	public void createNewUser(String uid, String password, String salt, String[] roles, Session session, Log log) {
		
		
		boolean permission = getUserPermissions(session.uid, "admin");
	
		
		if(permission) {
			final String SEPARATOR = ",";
			StringBuilder roleBuilder = new StringBuilder();

			 for(String role : roles){
				roleBuilder.append(role);
				roleBuilder.append(SEPARATOR);
			 }
			sqlStatement("insert into users values ('" + uid + "','" + crypto.hash(password, salt) + "','" + salt + "', '"+ roleBuilder + "')");
			try {
				log.writeLogEntry("[server]: created user: " + uid, path + "server.log");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println(session.uid + " is not allowed to create new users");
		}
		
	
	}
	
	private void dummyData() {
		// populate tables
		sqlStatement("insert into users values ('jeff','" + crypto.hash("password22", "22-10-2021:21.18zz") + "','22-10-2021:21.18zz', 'manager')");
		sqlStatement("insert into users values ('alice','" + crypto.hash("password22", "22-10-2021:21.18zz") + "','22-10-2021:21.18zz', 'manager')");
		sqlStatement("insert into users values ('bob','" + crypto.hash("password22", "22-10-2021:21.18zz") + "','22-10-2021:21.18zz', 'janitor, service-tech')");
		sqlStatement("insert into users values ('cecilia','" + crypto.hash("password22", "22-10-2021:21.18zz") + "','22-10-2021:21.18zz', 'power-user')");
		sqlStatement("insert into users values ('david','" + crypto.hash("password22", "22-10-2021:21.18zz") + "','22-10-2021:21.18zz', 'user')");
		sqlStatement("insert into users values ('erica','" + crypto.hash("password22", "22-10-2021:21.18zz") + "','22-10-2021:21.18zz', 'user')");
		sqlStatement("insert into users values ('fred','" + crypto.hash("password22", "22-10-2021:21.18zz") + "','22-10-2021:21.18zz', 'user')");
		sqlStatement("insert into users values ('george','" + crypto.hash("password22", "22-10-2021:21.18zz") + "','22-10-2021:21.18zz', 'user')");
		
		sqlStatement("insert into permissions values ('manager',1,1,1,1,1,1,1,1,1,1)");
		sqlStatement("insert into permissions values ('power-user',1,1,1,0,0,1,0,0,0,0)");
		sqlStatement("insert into permissions values ('service-tech',0,0,0,1,1,1,1,1,1,0)");
		sqlStatement("insert into permissions values ('janitor',0,0,0,0,0,1,0,0,0,0)");	
		sqlStatement("insert into permissions values ('user',1,1,0,0,0,0,0,0,0,0)");
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
			    	 // if database file does not exists, uncomment this to create a new database 

			    	 c = DriverManager.getConnection("jdbc:sqlite:database\\database.db");

			    	 if(tableExists("users")){
			    		 //System.out.println("table exists already");
			    		} else {
			    			sqlStatement("create table users (user varchar(20), password varchar(200), salt varchar(200), roles varchar(200))");
							sqlStatement("create table permissions (roles varchar(200), "
																	+ "print boolean(0,1), "
																	+ "queue boolean(0,1), "
																	+ "topQueue boolean(0,1), "
																	+ "start boolean(0,1), "
																	+ "stop boolean(0,1), "
																	+ "restart boolean(0,1), "
																	+ "status boolean(0,1), "
																	+ "readConfig boolean(0,1), "
																	+ "setConfig boolean(0,1),"
																	+ "admin boolean(0,1)"
																	+ ")");
							
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
}
