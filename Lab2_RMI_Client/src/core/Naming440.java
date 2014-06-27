package core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;


public class Naming440 {
	
	// static class variables
	private static String registryHost;
	private static int registryPort;
	
	// bind initial references to the services via RMIRegistry
	public static void rebind(String serviceName, Object object) {
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
	
	// look up a service and return a stub for the client
	public static Object lookup(String host, int port, String serviceName) {
		
		// locate the RMIRegistry		
		RMIRegistry registry = LocateRMIRegistry.getRegistry(host, port);
		RemoteObjectReference roRef = registry.lookup(serviceName);
		if (roRef == null) {
			System.out.println("Service not found...");
		}
		
		return roRef.localise();
	}

	
}