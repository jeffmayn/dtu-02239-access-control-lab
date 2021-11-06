package services;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;

import logic.Crypto;
import logic.Database;
import logic.Log;
import logic.Session;

public class PrinterServant  extends UnicastRemoteObject implements PrinterService {
	ArrayList<Printer> printers = new ArrayList<>();
	Database db = new Database();
	Log log = new Log();
	Crypto crypto = new Crypto();
	Session session = new Session();
	LocalDate localDate = LocalDate.now();
	String path = "log\\";

	String loggedInUser = "";
	boolean loggedIn = false;
	int authAttempts = 0;
	boolean lock = false;
	int lockoutTime = 10000;
	long timestamp = 0;

	public PrinterServant() throws RemoteException {
		super();
	}

	public void print(String filename, String printer) throws RemoteException {
		if(session.getSessionState()) {
			for (Printer p : printers) {
				if(p.printerName.equals(printer)) {
					p.addToQueue(filename); // add print to printer queue
					writeLogEntry(filename, path + printer + ".log");
				}
			}
		}
	}

	public String queue(String printer)throws RemoteException {
		StringBuilder queue = new StringBuilder();
		if(!session.getSessionState()) {
			queue.append("Session expired");
			// System.out.println("Session expired");
		} else {
			queue.append("Queue for printer: ");
			queue.append(printer);
			queue.append("\n");
			for (Printer p : printers) {
				if(p.printerName.equals(printer)) {
					writeLogEntry("[" + loggedInUser + "]: Queue for printer: " + printer, path + "server.log");

					int i = 1;
					for (String job : p.getQueue()) {
						queue.append("<");
						queue.append(i);
						queue.append("> <");
						queue.append(job);
						queue.append(">\n");
						i++;
					}
				}
			}
		}
		return queue.toString();
	}

	public void topQueue(String printer, int job)  throws RemoteException{
		if(session.getSessionState()) {
			for(Printer p : printers) {
				if(p.printerName.equals(printer)) {
					writeLogEntry("[Server]: Moving job" + "[" + job + "] to top.", path + "server.log");
					p.topQueue(job-1);
				}
			}
		}
	}

	public void start() throws RemoteException{
		writeLogEntry("[server]: starting..", path + "server.log");
		db.initialiseDatabase();
		initialisePrinters();
	}

	public void stop() throws RemoteException {
		if(session.getSessionState()) {
			writeLogEntry("[server]: stopping..", path + "server.log");
			db.disconnect();
		}
	}

	public void restart()  throws RemoteException{
		if(session.getSessionState()) {
			writeLogEntry("[server]: restarting..", path + "server.log");
			stop();
			printers.clear();
			start();
		}
	}

	public String status(String printer) throws RemoteException {
		StringBuilder returnMessage = new StringBuilder();
		if(!session.getSessionState()) {
			returnMessage.append("Session expired");
		} else {
			returnMessage.append("status for ");
			returnMessage.append(printer);
			returnMessage.append(": ");
			for(Printer p : printers) {
				if(p.printerName.equals(printer)) {
					returnMessage.append(p.status());
				}
			}
		}
		return returnMessage.toString();
	}

	public String readConfig(String parameter)  throws RemoteException{
		String retVal;
		if(!session.getSessionState()) {
			 retVal = "Session expired";
		} else {
			retVal = "invalid parameter";
			writeLogEntry("[" + loggedInUser + "]: reading server config", path + "server.log");

			if(parameter.equals("lockout time")) {
				retVal = "server configuration. Lockout time = " + (lockoutTime / 1000) + " seconds";
			}
		}
		return retVal;
	}

	public void setConfig(String parameter, String value) throws RemoteException {
		if(session.getSessionState()) {
			if(parameter.equals("lockout time")) {
				writeLogEntry("[" + loggedInUser + "]: sets lockout time to: " + value, path + "server.log");
				lockoutTime = Integer.parseInt(value) * 1000;
			}
		}
	}

	private void initialisePrinters() {
		// printer(boolean color, integer ink level)
        Printer office = new Printer(true, 90);
        office.setPrinterName("office");
        printers.add(office);

        Printer home = new Printer(false, 2);
        home.setPrinterName("home");
        printers.add(home);

        Printer hallway = new Printer(true, 50);
        hallway.setPrinterName("hallway");
        printers.add(hallway);

	}

	private void writeLogEntry(String txt, String path) {
		try {
			log.writeLogEntry(txt, path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String authenticateUser(String uid, String password) throws RemoteException {
		String returnVal;
		loggedIn = session.getSessionState();

		// remove lock if user has exceeded lockout time
		if(System.currentTimeMillis() >= (timestamp + lockoutTime)) {
			lock = false;
			timestamp = 0;
		}

		// get hashes from DB and users login
		String[] credentials = db.getCredentials(uid); 	// users [h(password+salt), salt] from DB
		String h1 = credentials[0];
		String h2 = crypto.hash(password, credentials[1]);

		authAttempts++;
		if(!lock) {
			if(authAttempts < 3) {
				loggedIn = crypto.compareHashes(h1, h2);
				if(loggedIn) {
					session.beginSession(uid);
					loggedInUser = session.getUser();
					returnVal = "Login successful!";
					writeLogEntry("[" + uid + "]: logged in successfully", path + "server.log");
				} else {
					returnVal = "Wrong password or username";
				}
			} else {
				authAttempts = 0;
				lock = true;
				timestamp = System.currentTimeMillis();
				returnVal = "Too many attempts. Try again in " + lockoutTime / 1000 + "seconds";
				writeLogEntry("[" + uid + "]: Too many unsuccessful login attempts. Lock applied", path + "server.log");
			}
		} else {
			writeLogEntry("[" + uid + "]: Too many unsuccessful login attempts", path + "server.log");
			returnVal = "Too many attempts. Try again in " + (lockoutTime - (System.currentTimeMillis() - timestamp)) / 1000 + " seconds";
		}
		return returnVal;
	}
}
