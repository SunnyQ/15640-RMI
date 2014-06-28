package services;

import java.net.InetAddress;

import core.Naming440;
import core.RMIConstants;

/*
 * Client implementation of Hello Service
 * tests all the remote invoking methods provided by Hello Server
 */
public class HelloClient {

    // This takes one command line argument: A person's first name
    public static void main (String []args) {
	    try {
	    	String host = InetAddress.getLocalHost().getHostName();
			int port = RMIConstants.REGISTRY_PORT;
			String serviceName = "HelloService";
			
	        HelloService hello = (HelloService) Naming440.lookup(host, port, serviceName);
	
	        hello.setName("Wenting");
	        String response;
	        response = hello.sayHello();
	        System.out.println(response);
	        HelloService hello2 = hello.newHello();
	        hello2.setName("YiJie");
	        response = hello2.introduce(hello);
	        System.out.println(response);
	
	    } catch (Exception e) {
		    // Bad things can happen to good people
		    e.printStackTrace();
	    }
    }
}
