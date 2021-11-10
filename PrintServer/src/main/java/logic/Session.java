package logic;

public class Session {	
	long loginStarted = 0;
	boolean sessionAlive = false;
	String uid = "";
	int time = 10;
	
	public Session() {};
	
	public void beginSession(String uid) {
		this.sessionAlive = true;
		this.uid = uid;
		this.loginStarted = System.currentTimeMillis();
	}
	
	public long getSessionTime() {
		return System.currentTimeMillis() - loginStarted;
	}
	
	public boolean getSessionState() {
		long elapsedTime = System.currentTimeMillis() - loginStarted;
		
		if(elapsedTime < time*1000) {
			this.sessionAlive = true;	
		} else {
			killSession();
		}	
		return this.sessionAlive;
	}
	
	public boolean killSession() {
		this.sessionAlive = false;
		this.loginStarted = 0;
		this.uid = "";
		return false;
	}
	
	public String getUser() {
		return this.uid;
	}
}
