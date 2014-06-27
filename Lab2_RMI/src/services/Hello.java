package services;

import java.io.Serializable;

import core.Naming440;
import core.RemoteException440;

public class Hello implements HelloService, Serializable {

	private static final long serialVersionUID = 5448908991166885112L;

	private static final String serviceName = "HelloService";
	
	// instance variables
	private String name;

	public Hello() throws RemoteException440 {
		name = null;  
	}	

    public String sayHello() throws RemoteException440 {
    	return "Hello! My name is " + name + "!"; 
    }
    
    public void setName(String name) throws RemoteException440 {
    	this.name = name;
    }
    
    public HelloService newHello() throws RemoteException440 {
    	return new Hello();
    }
    
    public String introduce(HelloService hs) throws RemoteException440 {
    	return "****** lets get introduced! ******\n" + 
    			sayHello() + "Nice to meet you!" + "\n" + 
    			hs.sayHello() + "Nice to meet you!\n" + 
    			"**********************************";
    }
    
    public String getName() {
    	return this.name;
    }
    
    // This is actually the starting point that registers the object, &c
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
