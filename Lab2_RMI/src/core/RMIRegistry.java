package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class RMIRegistry { 
	// regitry's IP address and port number
    private String IPAddress;
    private int port;
    
    public RMIRegistry(String IPAddress, int port)
    {
    	this.IPAddress = IPAddress;
    	this.port = port;
    }

    // look up a RemoteObjectReference by name
    public RemoteObjectReference lookup(String serviceName)
    {
		Socket socket = null;
		RemoteObjectReference roRef = null;
		
		try {
			// open socket
			socket = new Socket(IPAddress, port);
			System.out.println("socket made.");
		    
			// get TCP streams and wrap them. 
			BufferedReader in = new BufferedReader(new InputStreamReader (socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			System.out.println("stream made.");
			
			// send request and interface name (service name)
			out.println("lookup");
			out.println(serviceName);
			
			System.out.println("command and service name sent.");
			
			// branch according to the reply.
			String reply = in.readLine();
			
			if (reply.equals("found")) {
				System.out.println("service found!");
		
				// receive RemoteObjectReference data, without check.
				String roRefIP = in.readLine();		
				System.out.println(roRefIP);		
				int roRefPort = Integer.parseInt(in.readLine());
				System.out.println(roRefPort);		
				int roRefObjKey = Integer.parseInt(in.readLine());
				System.out.println(roRefObjKey);	
				String roRefInterfaceName = in.readLine();	
				System.out.println(roRefInterfaceName);
					
				// create RemoteObjectReference
				roRef = new RemoteObjectReference(roRefIP, roRefPort, roRefObjKey, roRefInterfaceName);
				
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

    // rebind a RemoteObjectReference
    public void rebind(String serviceName, RemoteObjectReference roRef) {
    	
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
			out.println(roRef.getObjectKey());
			out.println(roRef.getInterfaceName());
		
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
