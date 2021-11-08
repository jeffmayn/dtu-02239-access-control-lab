package logic;


public class AccessControl {
	
	public boolean hasPermission(String user, Database db, String permission) {
		
		boolean retVal;
		
		String permissions = db.getUserPermissions(user, permission);
		if(permissions.equals("1")) {
			retVal = true;
		} else {
			System.out.println(user + " dont have permission to " + permission);
			retVal = false;
		}
		return retVal;
		
	}

}
