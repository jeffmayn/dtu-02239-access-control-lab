package logic;

import java.io.IOException;

public class AccessControl {
	
	Log log = new Log();
	String path = "log\\";
	
	public boolean hasPermission(String user, Database db, String permission) {
		
		boolean retVal;
		
		String permissions = db.getUserPermissions(user, permission);
		if(permissions.equals("1")) {
			retVal = true;
		} else {
			System.out.println(user + " dont have permission to " + permission);
			try {
				log.writeLogEntry("[" + user + "]: was denied permission to: " + permission, path + "server.log");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			retVal = false;
		}
		return retVal;
		
	}

}
