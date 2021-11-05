package logic;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Log {
	public void writeLogEntry(String txt, String path) throws IOException {
		
		LocalDateTime localDateTime = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");  
		String formatDateTime = localDateTime.format(format);  
		
		File f = new File(path);
		if(f.exists() && !f.isDirectory()) { 
			try {
			    FileWriter fw = new FileWriter(path,true); //the true will append the new data
			    fw.write(formatDateTime + " -- "  + txt + "\n");
			    fw.close();
			}	
			catch(IOException ioe)
			{
			    System.err.println("IOException: " + ioe.getMessage());
			}
		} else {
			FileWriter fw = new FileWriter(path,false); 
			 fw.write(formatDateTime + " -- " + txt + "\n");
		    fw.close();
		}
	}
		
	public void readFile(String path) {
		 try {
		      File myObj = new File(path);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        System.out.println(data);
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
}
