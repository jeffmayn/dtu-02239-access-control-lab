package test;
import java.rmi.RemoteException;

import client.Client;
import server.ApplicationServer;

public class Main{
	
	static int port = 5053;
	
	static ApplicationServer server = new ApplicationServer();
	static Client client = new Client();

	public static void main(String[] args) throws RemoteException {
		server.begin(port);
		client.begin(port);
		//server.stopServer();
	}

}
