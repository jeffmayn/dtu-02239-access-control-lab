package client;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import services.PrinterService;

public class Client {
	PrinterService service;
	
	static UI ui = new UI();
	static Scanner input = new Scanner(System.in);  // Create a Scanner object
	
	public void begin(int portnumber) throws RemoteException {
		try {
			 service = (PrinterService) Naming.lookup("rmi://localhost:" + portnumber + "/printer");
		} 
		catch (MalformedURLException e) { e.printStackTrace(); } 
		catch (RemoteException e) 		{ e.printStackTrace(); } 
		catch (NotBoundException e) 	{ e.printStackTrace(); }

				// boot up server: creates database & printers
				service.start(); 
				
				// login & authenticate user
				if(login(service)) {	
					// automated tests
					if(ui.initialOptions(input)) {
						
						System.out.println(service.status("office"));
						service.print("text1.txt","office");
						service.print("text2.txt","office");
						service.print("text3.txt","office");
						service.print("text4.txt","office");
						service.print("text5.txt","office");
						System.out.println(service.queue("office"));
						service.topQueue("office", 3);
						System.out.println(service.queue("office"));
		
						service.print("text4.txt","home");
						System.out.println(service.queue("home"));
		
						service.print("text6.txt","office");
						service.restart();
						
						service.print("passwords.txt","office");
						service.print("sometext.txt","office");
						System.out.println(service.queue("office"));
						
						System.out.println(service.readConfig("lockout time"));
						service.setConfig("lockout time", "20");
						System.out.println(service.readConfig("lockout time"));
						
						try {
							TimeUnit.SECONDS.sleep(11);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println(service.status("office"));
					// manual
					} else {
						ui.printOptions();		
						ui.startLoop(input, service);
					}		
				}
	}

	public boolean login(PrinterService service) throws RemoteException {
		String auth = "";
		
		while (!auth.equals("Login successful!")) {
		    System.out.println("Enter your username");
			String userName = input.nextLine();  
			
		    System.out.println("Enter your password");
			String password = input.nextLine();  
			
			auth = service.authenticateUser(userName, password);		
			
			System.out.println(auth);
		}
		return true;
	}
}
