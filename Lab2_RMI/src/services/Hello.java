package services;

import java.io.Serializable;

import core.Naming440;
import core.RemoteException440;

/*
 * Implementation of Hello server which implements HelloService
 * Methods which supported by remote call are:
 * 					void setName(String name)
 * 					String sayHello()
 * 				    HelloService newHello()
 * 					String introduce(HelloService hs)
 */
public class Hello implements HelloService, Serializable {

	private static final long serialVersionUID = 5448908991166885112L;

	private static final String serviceName = "HelloService";
	
	// instance variables
	private String name;

	public Hello() throws RemoteException440 {
		name = null;  
	}	
	
	/**
	 * Print ones name
	 */
    public String sayHello() throws RemoteException440 {
    	return "Hello! My name is " + name + "!"; 
    }
    
    /**
     * Set ones name
     */
    public void setName(String name) throws RemoteException440 {
    	this.name = name;
    }
    
    /**
     * Return a new HelloService object which plays a role of remote object reference
     * in the actual test case
     */
    public HelloService newHello() throws RemoteException440 {
    	return new Hello();
    }
    
    /**
     * Use a remote object reference as parameter of this method
     * and print both the name of the parameter and the name of the
     * calling object
     */
    public String introduce(HelloService hs) throws RemoteException440 {
    	return "****** lets get introduced! ******\n" + 
    			sayHello() + "Nice to meet you!" + "\n" + 
    			hs.sayHello() + "Nice to meet you!\n" + 
    			"**********************************";
    }
    
    public String getName() {
    	return this.name;
    }
    
    /**
     * Starting point for server to register the object on RMIRegistry Server
     * @param args
     */
 	public static void main (String[] args) {
 	    try {
 	    	// Instantiate an instance of this class -- create a Hello object
 	    	Hello server = new Hello();
 	
 	    	// Tie the name "HelloService" to the Hello object we just created
 	    	Naming440.rebind (serviceName, server);
 	
 		    // Just a console message
 		    System.out.println ("Hello Server ready");
 		    
 	    } catch (Exception e) {
 	      // Bad things happen to good people
 	      e.printStackTrace();
 	    }
 	}
}
