package server;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import services.PrinterServant;

public class ApplicationServer {
	Registry registry;

	public void begin(int portnumber) {
		startServer(portnumber);	
	}

	public void startServer(int port) {
		try {
			registry = LocateRegistry.createRegistry(port);
			registry.rebind("printer", new PrinterServant());
		} catch (RemoteException e) {
			e.printStackTrace();
		}		
	}
	
	public void stopServer() {
		System.out.println("stopping rmi server.");
	    try {
			UnicastRemoteObject.unexportObject(registry, true);
		} catch (NoSuchObjectException e) {
			e.printStackTrace();
		}
	    System.exit(0);
	}
	
	public static void main(String[] args) throws RemoteException {	
	}

}
