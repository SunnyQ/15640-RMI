package core;

import java.net.*;
import java.io.*;

/**
 * This class provides a static method to retrieve a RMIRegistry on specified
 * host via specified port
 * 
 * @author alex
 */
public class LocateRMIRegistry { 
	/**
	 * Retrieve RMIRegistry on server
	 * @param host Host IP address
	 * @param port Host port to connect to
	 * @return RMI Registry if found, null if not found
	 * @throws RemoteException440 
	 */
    public static RMIRegistry getRegistry(String host, int port) {
    	
    	Socket socket = null;
		try {
			// open socket.
		    socket = new Socket(host, port);
		    
		    // get TCP streams and wrap them. 
		    BufferedReader in = new BufferedReader(new InputStreamReader (socket.getInputStream()));
		    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		    
		    // ask.
		    out.println("who are you?");
		    
		    // gets answer.
		    if ((in.readLine()).equals("RMIRegistry")) {
		    	System.out.println("RMIRegistry found!");
		    	socket.close(); 
			    return new RMIRegistry(host, port);
			}
		    else {
		    	socket.close(); 
			    System.out.println("RMIRegistry not found...");
			} 
		} catch (Exception e) { 
			e.printStackTrace();
		} 
		return null;
	}
}
