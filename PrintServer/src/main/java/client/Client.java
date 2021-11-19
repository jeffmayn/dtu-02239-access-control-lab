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


				System.out.println("\n\n------------------------------------\nPress (1) for automated tests\nPress (2) for exit");
				while (!input.hasNext("exit")) {
					String i = input.nextLine();	
					
					if(i.equals("1")) {
						testProgram1(service); // test for all functionalities (including deleting bob and adding Henry & Ida)
						testProgram2(service); // test that add bob again, and deletes henry & ida
						System.out.println("\n\n------------------------------------\nPress (1) for automated tests\nPress (2) for exit");
					}
					
					if(i.equals("2")) {
						System.out.println("\nApplication has stopped.");
						System.exit(0);
					}
				}
	
	}
	
	public void deleteUser(PrinterService service, String uid) {
		try {
			service.deleteUser(uid);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void assignUserRoles(PrinterService service, String uid, String roles) {
		
		String[] listRoles = roles.split("\\s*,\\s*");
		
		try {
			service.assignRolestoUser(uid, listRoles);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}
	
	public void createNewUser(PrinterService service, String name, String password, String salt, String roles) {
		
		String[] listRoles = roles.split("\\s*,\\s*");
		
		try {
			service.createNewUser(name, password, salt, listRoles);
		} catch (RemoteException e) {
			e.printStackTrace();
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
	
	public void testProgram2(PrinterService service) throws RemoteException {
				// Creating new user Bob (since he was deleted in test program 1)
				// logging in as Alice since she is manager and can add people
				System.out.println(service.authenticateUser("alice", "password22"));
				service.start();
				System.out.println("Creating user: Bob with role: janitor, service-tech");
				createNewUser(service, "bob", "password22", "saltyMcSalty222", "janitor, service-tech");
				
				// sleep for one second
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				System.out.println("Retiring George and Ida..");
				service.deleteUser("henry");
				service.deleteUser("ida");
				service.logout();
				
		
	}
	
	public void testProgram1(PrinterService service) throws RemoteException {
		
		// log in as Bob
		System.out.println("loggin in as Bob");
		System.out.println(service.authenticateUser("bob", "password22"));		
		service.start();
		System.out.println(service.status("office"));
		service.print("text1.txt","office");
		service.print("text2.txt","office");
		service.print("text3.txt","office");
		service.print("text4.txt","office");
		System.out.println(service.queue("office"));
		service.topQueue("office", 3);
		System.out.println(service.queue("office"));
		System.out.println(service.readConfig("lockout time"));
		service.setConfig("lockout time", "2");
		System.out.println(service.readConfig("lockout time"));
		System.out.println(service.queue("office"));
		service.restart();
		service.stop();
		
		service.logout();

		// sleep for one second
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		

		// log in as Alice
		System.out.println("loggin in as Alice");
		System.out.println(service.authenticateUser("alice", "password22"));		
		service.start();
		System.out.println(service.status("office"));
		service.print("text1.txt","office");
		service.print("text2.txt","office");
		service.print("text3.txt","office");
		service.print("text4.txt","office");
		System.out.println(service.queue("office"));
		service.topQueue("office", 3);
		System.out.println(service.queue("office"));
		System.out.println(service.readConfig("lockout time"));
		service.setConfig("lockout time", "2");
		System.out.println(service.readConfig("lockout time"));
		System.out.println(service.queue("office"));
		service.restart();

		service.logout();

		// sleep for one second
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// log in as Cecilia
		System.out.println("loggin in as Cecilia");
		System.out.println(service.authenticateUser("cecilia", "password22"));		
		service.start();
		System.out.println(service.status("office"));
		service.print("text1.txt","office");
		service.print("text2.txt","office");
		service.print("text3.txt","office");
		service.print("text4.txt","office");
		System.out.println(service.queue("office"));
		service.topQueue("office", 3);
		System.out.println(service.queue("office"));
		System.out.println(service.readConfig("lockout time"));
		service.setConfig("lockout time", "2");
		System.out.println(service.readConfig("lockout time"));
		System.out.println(service.queue("office"));
		service.restart();
		service.stop();
		
		service.logout();

		// sleep for one second
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// log in as David
		System.out.println("loggin in as David");
		System.out.println(service.authenticateUser("david", "password22"));		
		service.start();
		System.out.println(service.status("office"));
		service.print("text1.txt","office");
		service.print("text2.txt","office");
		service.print("text3.txt","office");
		service.print("text4.txt","office");
		System.out.println(service.queue("office"));
		service.topQueue("office", 3);
		System.out.println(service.queue("office"));
		System.out.println(service.readConfig("lockout time"));
		service.setConfig("lockout time", "2");
		System.out.println(service.readConfig("lockout time"));
		System.out.println(service.queue("office"));
		service.restart();
		service.stop();
		
		service.logout();

		// sleep for one second
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// log in as Erica
		System.out.println("loggin in as Erica");
		System.out.println(service.authenticateUser("erica", "password22"));		
		service.start();
		System.out.println(service.status("office"));
		service.print("text1.txt","office");
		service.print("text2.txt","office");
		service.print("text3.txt","office");
		service.print("text4.txt","office");
		System.out.println(service.queue("office"));
		service.topQueue("office", 3);
		System.out.println(service.queue("office"));
		System.out.println(service.readConfig("lockout time"));
		service.setConfig("lockout time", "2");
		System.out.println(service.readConfig("lockout time"));
		System.out.println(service.queue("office"));
		service.restart();
		service.stop();
		
		service.logout();

		// sleep for one second
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// log in as Fred
		System.out.println("loggin in as Fred");
		System.out.println(service.authenticateUser("fred", "password22"));		
		service.start();
		System.out.println(service.status("office"));
		service.print("text1.txt","office");
		service.print("text2.txt","office");
		service.print("text3.txt","office");
		service.print("text4.txt","office");
		System.out.println(service.queue("office"));
		service.topQueue("office", 3);
		System.out.println(service.queue("office"));
		System.out.println(service.readConfig("lockout time"));
		service.setConfig("lockout time", "2");
		System.out.println(service.readConfig("lockout time"));
		System.out.println(service.queue("office"));
		service.restart();
	//	service.stop();
		
		service.logout();

		// sleep for one second
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// log in as George
		System.out.println("loggin in as George");
		System.out.println(service.authenticateUser("george", "password22"));		
		service.start();
		System.out.println(service.status("office"));
		service.print("text1.txt","office");
		service.print("text2.txt","office");
		service.print("text3.txt","office");
		service.print("text4.txt","office");
		System.out.println(service.queue("office"));
		service.topQueue("office", 3);
		System.out.println(service.queue("office"));
		System.out.println(service.readConfig("lockout time"));
		service.setConfig("lockout time", "2");
		System.out.println(service.readConfig("lockout time"));
		System.out.println(service.queue("office"));
		service.restart();
		service.stop();
		
		service.logout();

		// sleep for one second
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Creating new user Henry (need to login as alice because managers
		// are the only ones who can create new users
		System.out.println(service.authenticateUser("alice", "password22"));
		service.start();
		System.out.println("Creating user: Henry with role: user");
		createNewUser(service, "henry", "password22", "saltyMcSalty222", "user");
		
		// sleep for one second
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Creating new user Ida
		System.out.println("Creating user: Ida with role: Power User");
		createNewUser(service, "ida", "password22", "saltyMcSalty222", "power-user");
		
		// sleep for one second
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// delete Bob
		System.out.println("Retiring Bob..");
		service.deleteUser("bob");
		assignUserRoles(service, "george", "service-tech");
		service.logout();
		
		// sleep for one second
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// log in as Henry
		System.out.println("loggin in as Henry");
		System.out.println(service.authenticateUser("henry", "password22"));		
		service.start();
		System.out.println("Henry tries to privilege escalates his access rights..");
		assignUserRoles(service, "henry", "manager"); // henry tries to privilege escalates his access rights
		System.out.println(service.status("office"));
		service.print("text1.txt","office");
		service.print("text2.txt","office");
		service.print("text3.txt","office");
		service.print("text4.txt","office");
		System.out.println(service.queue("office"));
		service.topQueue("office", 3);
		System.out.println(service.queue("office"));
		System.out.println(service.readConfig("lockout time"));
		service.setConfig("lockout time", "2");
		System.out.println(service.readConfig("lockout time"));
		System.out.println(service.queue("office"));
		service.restart();
		service.stop();
		
		service.logout();
		
		// log in as Ida
		System.out.println("loggin in as Ida");
		System.out.println(service.authenticateUser("ida", "password22"));		
		service.start();
		System.out.println(service.status("office"));
		service.print("text1.txt","office");
		service.print("text2.txt","office");
		service.print("text3.txt","office");
		service.print("text4.txt","office");
		System.out.println(service.queue("office"));
		service.topQueue("office", 3);
		System.out.println(service.queue("office"));
		System.out.println(service.readConfig("lockout time"));
		service.setConfig("lockout time", "2");
		System.out.println(service.readConfig("lockout time"));
		System.out.println(service.queue("office"));
		service.restart();
		service.stop();
		
		service.logout();
		
		
	}
}
