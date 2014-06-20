package core;


/**
 *  Any client holding an instance of RemoteObjectReference will be able to
 *  remotely invoke methods of remote objects on server side. RemoteObjectReference
 *  may be passed as arguments and results of remote method invocation
 *  
 * @author alex
 *
 */

public class RemoteObjectReference {

    private String IPAddress;
    private int port;
    private int objectKey;
    private String interfaceName;

    public RemoteObjectReference (String IPAddress, int port, int objectKey, String interfaceName) {
    	this.IPAddress = IPAddress;
    	this.port = port;
    	this.objectKey = objectKey;
    	this.interfaceName = interfaceName;
    }

    // creates a new stub object and returns it
    public Object localise() {
    	String stubClassName = interfaceName + "_stub";
    	Class<?> stubClass;
    	Object stub = null;
		try {
			stubClass = Class.forName(stubClassName);
			stub = stubClass.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return stub;
    }
    
    // getters
    public String getIP() {
    	return this.IPAddress;
    }
    
    public int getPort() {
    	return this.port;
    }
    
    public int getObjectKey() {
    	return this.objectKey;
    }
    
    public String getInterfaceName() {
    	return this.interfaceName;
    }
		
}
