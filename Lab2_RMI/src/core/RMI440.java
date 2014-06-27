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

public class RMI440 implements Runnable {
	
	private static String host;
	private static int port;
	private static int objectKeyGenerator = 0;
	private static Map<Integer, Object> objectMap;
	
	private String registryHost;
	private int registryPort;
	private ServerSocket serverSoc;
	
	
    public RMI440() {
    	
    }
	
	// constructor
	public RMI440(String registryhost, int registryport) {
		try {
			host = (InetAddress.getLocalHost()).getHostAddress();
			port = RMIConstants.RMI_PORT;
			this.registryHost = registryhost;
			this.registryPort = registryport;
			objectMap = new HashMap<Integer, Object>();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} 
	}
		
	// launch RMI440 server process
	public void run() {
		try {
			serverSoc = new ServerSocket(port);		
											
			// continuously accept connection request
			while (true) {
				System.out.println("RMI440 engine waiting for next connection request...");
				Socket socket = serverSoc.accept();
				
				// get the most recent service list from registry
				RMIRegistry registry = LocateRMIRegistry.getRegistry(registryHost, registryPort);
				Map<String, String> services = registry.getServicesList();
				
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
					
					// retrieve the implementation class name from services map
					String serviceName = roRef.getInterfaceName();
					String implClassName = services.get(serviceName);
					Class<?> implClass = Class.forName(implClassName);
					
					// create a new instance of local object reference
					localReference = implClass.newInstance();
					
					// add to the hashmap
					objectMap.put(objectKeyGenerator, localReference);
					objectKeyGenerator++;
				} else {
					localReference = objectMap.get(roRef.getObjectKey());
				}
				
				// check whether arguments contain any remote object reference
				Object[] arguments = request.getArguments();
				if (arguments != null) {
					for (int i = 0; i < arguments.length; i++) {
						if (arguments[i] instanceof Remote440) {
							request.setArguments(i, objectMap.get(((Stub) arguments[i]).getReference().getObjectKey()));
						}
					}
				}
								
				// invoke method on local object reference
				request.invoke(localReference);
				
				// examine the return type
				Object returnObj = request.getReturnValue();
				if (returnObj != null) {
					if (isRemoteObject(returnObj)) {
						
						String interfaceName = returnObj.getClass().getInterfaces()[0].getSimpleName();
						// build a new RemoteObjectReference and place a new entry in the object map
						RemoteObjectReference ror = new RemoteObjectReference(host, port, interfaceName);
						ror.setObjectKey(objectKeyGenerator);
						objectMap.put(objectKeyGenerator, returnObj);
						objectKeyGenerator++;
						request.setReturnValue(ror);
						
					}
				}
				
				// send back result of invocation
				if (request.getArguments() != null) {
					for (int i = 0; i < request.getArguments().length; i++) {
						request.setArguments(i, null);
					}
				}		
				out.writeObject(request);
				System.out.println("RMI has delivered back the invocation result...");
				
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
	
	// helper: returns true if an object is a remote object
	private boolean isRemoteObject(Object obj) {
		
		if (obj instanceof Remote440) {
			return true;
		}
		return false;
	}

}
