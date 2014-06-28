package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;

/*
 * The RMIRegistery server which runs as a thread
 * that accepts connection from both LocateRMIRegistry and
 * RMIRegistry and handle the relevant requests from
 * instances of these two classes.
 */
public class RegistryServer implements Runnable {
	// listens for "lookup" and "rebind" request
	private HashMap<String, RemoteObjectReference> binding;
	private HashMap<String, String> implClassBinding;
	private ServerSocket serverSoc;
	
	/**
	 * Constructor
	 */
	public RegistryServer() {
		binding = new HashMap<String, RemoteObjectReference>();
		implClassBinding = new HashMap<String, String>();
	}
	
	/**
	 * run() function of the thread
	 */
	public void run() {
		try {
			serverSoc = new ServerSocket(RMIConstants.REGISTRY_PORT);
			Socket socket;
			System.out.println("RegistryServer started, now accepting request...");
			while (true) {
				socket = serverSoc.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader (socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				handleRequest(in, out);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 
	 * private helper for handling request
	 * Commands include:
	 * 					"who are you?" ---- answer RMIRegistry query
	 * 					"lookup"	   ---- search in the binding map if the service is supported
	 * 					"rebind"	   ---- generate an ROR and add it into the binding map 
	 * 										and the implClassBinding map
	 * 					"list"		   ---- get all service and implementation class names from the implClassBinding
	 * @param in
	 * @param out
	 */
	private void handleRequest(BufferedReader in, PrintWriter out) {
		try {
			String from = in.readLine();
			if (from.equals("who are you?")) {
				out.println("RMIRegistry");
			} else if (from.equals("lookup")) {
				String serviceName = in.readLine();
				RemoteObjectReference roRef = binding.get(serviceName);
				if (roRef != null) {				
					// send reply
					out.println("found");
					out.println(roRef.getIP());
					out.println(roRef.getPort());
					out.println(roRef.getInterfaceName());			
				} else {
					out.println("not found");
				}
			} else if (from.equals("rebind")) {
				// read in interfaceName and RemoteObjectReference parameters
				String serviceName = in.readLine();
				String roRef_IP = in.readLine();
				String roRef_Port = in.readLine();
				String roRef_InterfaceName = in.readLine();
				String implClassName = in.readLine();
				
				// create RemoteObjectReference
				RemoteObjectReference roRef = new RemoteObjectReference(roRef_IP, Integer.parseInt(roRef_Port), roRef_InterfaceName);
				
				// bind
				binding.put(serviceName, roRef);
				implClassBinding.put(serviceName, implClassName);				
				
				// send back acknowledgement
				System.out.println("binding successful for " + serviceName);
				out.println("ack");
			} else if (from.equals("list")) {
				String serviceNames = "";
				String implClassNames = "";
				Iterator<String> itr = binding.keySet().iterator();
				while (itr.hasNext()) {
					String serviceName = itr.next();
					String implClassName = implClassBinding.get(serviceName);
					serviceNames += serviceName + " ";
					implClassNames += implClassName + " ";
				}
				out.println(serviceNames);
				out.println(implClassNames);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Start the RMIRegistry Server as a Thread and the RMI440 as a Thread
	 * @param args
	 */
	public static void main(String args[]) {
		RegistryServer rs = new RegistryServer();
		new Thread(rs).start();
		String registryHost = null;
		int registryPort = -1;
		try {
			registryHost = InetAddress.getLocalHost().getHostName();
			registryPort = RMIConstants.REGISTRY_PORT;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		if (registryHost != null && registryPort != -1) {
			RMI440 dispatcher = new RMI440(registryHost, registryPort);
			new Thread(dispatcher).start();
		}
				
	}
}
