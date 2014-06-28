package core;

import java.net.InetAddress;
import java.net.UnknownHostException;

/*
 * The general purpose of this class is to simplify
 * the bind process from the server side and the lookup
 * process from the client side. It wraps up the communication
 * process to RMIRegistry running on the same machine as
 * the server and different machine as client and creates
 * more transparency to application programmers.
 */
public class Naming440 {
	
	// static class variables
	private static String registryHost;
	private static int registryPort;
	
	/**
	 * bind initial reference to the service on RMIRgistry
	 * through generating a new RemoteObjectReference and bind
	 * it on the RMIRegistry along with the service name and
	 * the implement class name
	 * CALL FROM SERVER
	 * @param serviceName
	 * @param object
	 */
	public static void rebind(String serviceName, Object object) {
		// bind initial references to the services via RMIRegistry
		String implClassName = object.getClass().getName();
		// locate the RMIRegistry
    	try {
			registryHost = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		registryPort = RMIConstants.REGISTRY_PORT; 
		RMIRegistry registry = LocateRMIRegistry.getRegistry(registryHost, registryPort);
		if (registry == null) {
			System.out.println("Couldn't locate registry...");
			System.exit(-1);
		}
		
		// implementation class
		Class<?> implClass = null;
		try {
			implClass = Class.forName(implClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// bind initial RemoteObjectReference to serviceName
		if (implClass != null) {
			RemoteObjectReference initialRef = new RemoteObjectReference(registryHost, RMIConstants.RMI_PORT,
					implClass.getInterfaces()[0].getSimpleName());  
			registry.rebind(serviceName, initialRef, implClassName);
		}
	}
	
	/**
	 * look up a service and return a stub of that service
	 * through getting the RMIRegistry and calling its lookup() function
	 * to get the remote object reference and then calling its 
	 * localise() function
	 * CALL FROM CLIENT
	 * @param host
	 * @param port
	 * @param serviceName
	 * @return relevant stub object of the service
	 */
	public static Object lookup(String host, int port, String serviceName) {
		// look up a service and return a stub for the client
		// locate the RMIRegistry		
		RMIRegistry registry = LocateRMIRegistry.getRegistry(host, port);
		RemoteObjectReference roRef = registry.lookup(serviceName);
		if (roRef == null) {
			System.out.println("Service not found...");
		}
		
		return roRef.localise();
	}

}
