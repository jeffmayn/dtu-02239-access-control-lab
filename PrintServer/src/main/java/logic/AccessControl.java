package logic;

import java.io.IOException;

public class AccessControl {
	
	Log log = new Log();
	String path = "log\\";
	
	public boolean hasPermission(String user, Database db, String permissionRequest) {

		boolean permission = db.getUserPermissions(user, permissionRequest);
		if(!permission) {
			System.out.println("<" + user + " don't have access to " + permissionRequest + ">");
			try {
				log.writeLogEntry("[" + user + "]: denied access to: " + permissionRequest, path + "server.log");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			
			
	
		return permission;	
	}
}
