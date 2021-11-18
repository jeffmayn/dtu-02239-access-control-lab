package services;
import java.rmi.RemoteException;
import java.rmi.Remote;

public interface PrinterService extends Remote {
	
	void print(String filename, String printer) throws RemoteException;
	
	String queue(String printer) throws RemoteException;
	
	void topQueue(String printer, int job) throws RemoteException;
	
	void start() throws RemoteException;
	
	void stop() throws RemoteException;
	
	void restart() throws RemoteException;
	
	String status(String printer) throws RemoteException;
	
	String readConfig(String parameter) throws RemoteException;
	
	void setConfig(String parameter, String value) throws RemoteException;
	
	String authenticateUser(String uid, String password) throws RemoteException;
	
	void logout() throws RemoteException;
	
	void buildDatabase() throws RemoteException;
	

}
