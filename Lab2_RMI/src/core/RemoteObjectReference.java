package core;

import java.io.Serializable;

/*
 *  Any client holding an instance of RemoteObjectReference will be able to
 *  remotely invoke methods of remote objects on server side. RemoteObjectReference
 *  may be passed as arguments and results of remote method invocation
 *  
 * @author alex
 *
 */
public class RemoteObjectReference implements Serializable {

	private static final long serialVersionUID = -7514708045819316612L;
	private String IPAddress;
    private int port;
    private int objectKey;
    private String interfaceName;

    /**
     * Constructor
     * @param IPAddress
     * @param port
     * @param interfaceName
     */
    public RemoteObjectReference (String IPAddress, int port, String interfaceName) {
    	this.IPAddress = IPAddress;
    	this.port = port;
    	this.interfaceName = interfaceName;
    	this.objectKey = -1;
    }

    /**
     * creates a new stub object, attach an ROR on this object, and returns it
     * @return
     */
    public Object localise() {
    	String stubClassName = "services." + interfaceName + "_stub";
    	Class<?> stubClass;
    	Object stub = null;
		try {
			stubClass = Class.forName(stubClassName);
			stub = stubClass.newInstance();
			// attach a the current remote object reference
			((Stub)stub).attachReference(this);
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
    
    // setters
    public void setObjectKey(int objectKey) {
    	this.objectKey = objectKey;
    }
    
    public void setInterfaceName (String interfaceName) {
    	this.interfaceName = interfaceName;
    }
		
}
