package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


// listens for "lookup" and "rebind" request
public class RegistryServer {
	
	private HashMap<String, RemoteObjectReference> binding;
	private ServerSocket serverSoc;
	
	// constructor
	public RegistryServer() {
		binding = new HashMap<String, RemoteObjectReference>();
	}
	
	// launch server
	public void launch() {
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

	// private helper for handling request 
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
				
				// create RemoteObjectReference
				RemoteObjectReference roRef = new RemoteObjectReference(roRef_IP, Integer.parseInt(roRef_Port), roRef_InterfaceName);
				
				// bind
				binding.put(serviceName, roRef);
				
				// send back acknowledgement
				out.println("ack");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public static void main(String args[]) {
		RegistryServer rs = new RegistryServer();
		rs.launch();
	}
}
