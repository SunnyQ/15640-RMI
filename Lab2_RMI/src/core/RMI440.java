package core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

// usage: java RMI440 ZipCodeServerImpl registryhost resigstryport servicename

public class RMI440 {
	
	private static String host;
	private static int port;
	private static int objectKeyGenerator = 0;

	public static void main(String args[]) {
		
		try {
			
			// manually supply arguments for testing
			String implClassName = "services.ZipCodeServiceImpl";
			String registryHost = InetAddress.getLocalHost().getHostName();
			int registryPort = RMIConstants.REGISTRY_PORT;
			String serviceName = RMIConstants.SERVICE_NAME;
			
			host = (InetAddress.getLocalHost()).getHostAddress();
			port = RMIConstants.RMI_PORT;
			ServerSocket serverSoc = new ServerSocket(port);
			
			// locate the RMIRegistry
			RMIRegistry registry = LocateRMIRegistry.getRegistry(registryHost, registryPort);
			if (registry == null) {
				System.out.println("Couldn't locate registry...");
				System.exit(-1);
			}
				
			Class<?> implClass = Class.forName(implClassName);
			
			// bind initial RemoteObjectReference to serviceName
			RemoteObjectReference initialRef = new RemoteObjectReference(host, port,
									implClass.getInterfaces()[0].getSimpleName());  
			registry.rebind(serviceName, initialRef);
								
			// create a hashmap for storing ObjectKey -> Object mapping
			Map<Integer, Object> objectMap = new HashMap<Integer, Object>();
											
			// continuously accept connection request
			while (true) {
				System.out.println("RMI440 engine waiting for next connection request...");
				Socket socket = serverSoc.accept();
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				// read RMIMessage
				RMIMessage request = (RMIMessage) in.readObject();
				// use remote reference to locate local object reference
				RemoteObjectReference roRef = request.getReference();
				Object localReference;
				// check valid object key
				if (roRef.getObjectKey() == -1) {
					// assign a new unique Object Key and send the Key back to client stub
					roRef.setObjectKey(objectKeyGenerator);
					out.writeObject(new Integer(objectKeyGenerator));
					// create a new instance of local object reference
					localReference = implClass.newInstance();
					// add to the hashmap
					objectMap.put(objectKeyGenerator, localReference);
					objectKeyGenerator++;
				} else {
					localReference = objectMap.get(roRef.getObjectKey());
				}			
				// invoke method on local object reference
				request.invoke(localReference);
				out.writeObject(request);
				System.out.println("invocation complete! Return value has been delivered back!");
				// close socket
				socket.close();
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
