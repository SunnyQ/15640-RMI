package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/*
 * The main purpose of this class is to support some basic
 * communication with Registry Server such as lookup a service, 
 * getServiceList, and rebind services with ROR and implClassName with Registry Server
 * 
 */
public class RMIRegistry { 
	// regitry's IP address and port number
    private String IPAddress;
    private int port;
    
    /**
     * Constructor with two parameters
     * @param IPAddress
     * @param port
     */
    public RMIRegistry(String IPAddress, int port)
    {
    	this.IPAddress = IPAddress;
    	this.port = port;
    }

    /**
     * Communicate with the RMIRegistry to return the RemoteObjectReference
     * correspondent with the given service name.
     * @param serviceName
     * @return RemoteObjectReference if found
     * 		   null if not found
     */
    public RemoteObjectReference lookup(String serviceName)
    {
    	// look up a RemoteObjectReference by name
		Socket socket = null;
		RemoteObjectReference roRef = null;
		
		try {
			// open socket
			socket = new Socket(IPAddress, port);
		    
			// get TCP streams and wrap them. 
			BufferedReader in = new BufferedReader(new InputStreamReader (socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			
			// send request and interface name (service name)
			out.println("lookup");
			out.println(serviceName);
			
			// branch according to the reply.
			String reply = in.readLine();
			
			if (reply.equals("found")) {
				System.out.println("service found!");
		
				// receive RemoteObjectReference data, without check.
				String roRefIP = in.readLine();		
				int roRefPort = Integer.parseInt(in.readLine());
				String roRefInterfaceName = in.readLine();	
					
				// create RemoteObjectReference
				roRef = new RemoteObjectReference(roRefIP, roRefPort, roRefInterfaceName);
				
			} else {
				System.out.println("service not found...");
				roRef = null;
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// close socket
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
			
		// return RemoteObjectReference
		return roRef;
    }
    
    /**
     * Get the services list and the corresponding implementation classes from Registry Server
     * @return Map contains the list of services and implementation classes
     */
    public Map<String, String> getServicesList() {
    	// get the services list from registry
    	Socket socket = null;
		Map<String, String> services = new HashMap<String, String>();
		try {
			// open socket
			socket = new Socket(IPAddress, port);
		    
			// get TCP streams and wrap them. 
			BufferedReader in = new BufferedReader(new InputStreamReader (socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			
			// send list request
			out.println("list");
			
			// read back service and implClass names and form HashMap
			String[] serviceNames = in.readLine().split(" ");
			String[] implClassNames = in.readLine().split(" "); 
			
			for (int i = 0; i < serviceNames.length; i++) {
				services.put(serviceNames[i], implClassNames[i]);
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// close socket
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return services;
    }

    /**
     * rebind a RemoteObjectReference, its relevant service name and implClassName into Registry Server
     * @param serviceName
     * @param roRef
     * @param implClassName
     */
    public void rebind(String serviceName, RemoteObjectReference roRef, String implClassName) {
    	
    	// open socket
		Socket socket = null;
		try {			
			socket = new Socket(IPAddress, port);
			// get TCP streams and wrap them. 
			BufferedReader in = new BufferedReader(new InputStreamReader (socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		
			// it is a rebind request, with a service name and ROR.
			out.println("rebind");
			out.println(serviceName);
			out.println(roRef.getIP());
			out.println(roRef.getPort()); 
			out.println(roRef.getInterfaceName());
			out.println(implClassName);
		
			// wait for acknowledgement
			String ack = in.readLine();
		
			// close the socket.
			socket.close();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }
} 
