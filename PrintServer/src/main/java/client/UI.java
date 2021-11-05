package client;

import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import services.PrinterService;

public class UI {
	
	public void startLoop(Scanner input, PrinterService service) throws RemoteException {
		
		while (!input.hasNext("exit")) {
			
			String i = input.nextLine();
			//System.out.println("i: " + i);
			//printOptions();
			//String n = input.next();
			
			if(i.equals("print")) { printLogic(input, service); } 
			  else if (i.equals("1")) { printOptions(); } 
			  else if (i.equals("queue")) { queue(service, input); }
			  else if (i.equals("top")) { topQueueLogic(service, input); }
			  else if (i.equals("start")) { start(service); }
			  else if (i.equals("stop")) { stop(service); }
			  else if (i.equals("restart")) { restart(service); }
			  else if (i.equals("status")) { status(service, input); }
		}
	}
	
	public void status(PrinterService service, Scanner input) {
		
		System.out.println("Write name of printer\n");
		String printerName = input.nextLine();
		
		try {
			String ret = service.status(printerName);
			System.out.println(ret + "\n");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		printOptions();
		
	}
	
	public void start(PrinterService service) {
		
		System.out.println("Starting server ..");
		try {
			service.start();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		printOptions();
	}
	
	public void stop(PrinterService service) {
		
		System.out.println("Stopping server ..");
		try {
			service.stop();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		printOptions();
	}
	
	public void restart(PrinterService service) {
		
		System.out.println("Restarting server ..");
		try {
			service.restart();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		printOptions();
	}
	
	public void queue(PrinterService service, Scanner input) {
		
		System.out.println("Write name of printer\n");
		String printerName = input.nextLine();
		
		try {
			System.out.println(service.queue(printerName));
			System.out.println("");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		printOptions();
	}
	
	public void topQueueLogic(PrinterService service, Scanner input) throws RemoteException {
		
		System.out.println("Write name of printer");
		String printerName = input.nextLine();
		
		System.out.println("Write job#");

		int job = Integer.parseInt(input.nextLine());
		System.out.println(printerName + ": job# " + Integer.toString(job) + " sent to top of queue!\n\n");
	
		service.topQueue(printerName, job);
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		printOptions();
		
	}
	
	public void printLogic(Scanner input, PrinterService service) {
		System.out.println("Please write name of printer");
		String printerName = input.nextLine();
		
		System.out.println("Please write name of file");
		String fileName = input.nextLine();
		
		try {
			service.print(fileName, printerName);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		System.out.println("Print sent to printerName\n\n");
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		printOptions();
	}
	

	public boolean initialOptions(Scanner input) {
		 System.out.println("Press (1) for automated tests\nPress (2) for manuel");	 
		 String choice = input.nextLine();
		 if(choice.equals("1")) {
			 return true;
		 } else {
			 return false;
		 }
	}
	
	
	public void printOptions() {
		
	    System.out.println("+---COMMAND --------------DESCRIPTION---------------------------+");
	    System.out.println("' print         │ prints file filename on the specified printer '");
	    System.out.println("' queue         │ lists the print queue for a given printer     '");
	    System.out.println("' topQueue      │ moves job to the top of the queue             '");
	    System.out.println("' start         │ starts the print server                       '");
	    System.out.println("' stop          │ stops the print se                            '");
	    System.out.println("' restart       │ stops, clears and restart server              '");
	    System.out.println("' status        │ prints status of printer                      '");
	    System.out.println("' exit          │ exits application                             '");
	    System.out.println("+---------------------------------------------------------------+");
	}



	

}
