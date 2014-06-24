package services;

//a client for ZipCodeServer.
//it uses ZipCodeServer as an interface, and test
//all methods.

//It reads data from a file containing the service name and city-zip 
//pairs in the following way:
//city1
//zip1
//...
//...
//end.

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;

import core.LocateRMIRegistry;
import core.RMIConstants;
import core.RMIRegistry;
import core.RemoteException440;
import core.RemoteObjectReference;

public class ZipCodeClient { 
 
	public static void main(String[] args) {
		
//		String host = args[0];
//		int port = Integer.parseInt(args[1]);
//		String serviceName = args[2];
//		String fileName = args[3];
	
		try {
			// manually supply arguments
			String host = InetAddress.getLocalHost().getHostName();
			int port = RMIConstants.REGISTRY_PORT;
			String serviceName = RMIConstants.SERVICE_NAME;
			String fileName = "zipcodes.txt";
			
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			
			// locate the registry and get a Remote Object Reference
			RMIRegistry registry = LocateRMIRegistry.getRegistry(host, port);

			System.out.println("Registry found");
			RemoteObjectReference roRef = registry.lookup(serviceName);
			if (roRef == null) {
				System.out.println("Service not found...");
			}

			// get (create) the stub out of ror.
			ZipCodeService_stub zcs = (ZipCodeService_stub) roRef.localise();
			if (zcs == null) {
				System.out.println("Shit! I've got a NullPointer!");
			} else {
				System.out.println("Stub generated successfully!");
			}
			
			// attach RemoteObjectReference
			zcs.attachReference(roRef);

			// reads the data and make a "local" zip code list.
			// later this is sent to the server.
			// again no error check!
			ZipCodeList l = null;
			boolean flag = true;
			while (flag) {
				String city = in.readLine();
				String code = in.readLine();
				if (city == null)
					flag = false;
				else
					l = new ZipCodeList(city.trim(), code.trim(), l);
		    }
			// the final value of l should be the initial head of 
			// the list.
			
			// we print out the local zipcodelist.
			System.out.println("This is the original list.");
			ZipCodeList temp = l;
			while (temp !=null) {
				System.out.println
				    ("city: "+temp.city+", "+
				     "code: "+temp.ZipCode);       
				temp = temp.next;                        
		    }
		
			// test the initialise.
			zcs.initialize(l);
			System.out.println("\n Server initalised.");

			// test the find.
			System.out.println("\n This is the remote list given by find.");
			temp = l;
			while (temp !=null) {
				// here is a test.
				String res = zcs.find(temp.city);
				System.out.println("city: "+temp.city+", "+ "code: "+res);
				temp=temp.next;
		    }        		
			
			// test the findall.
			System.out.println("\n This is the remote list given by findall.");
			// here is a test.
			temp = zcs.findAll();
			while (temp !=null) {
				System.out.println("city: "+temp.city+", "+"code: "+temp.ZipCode);
				temp=temp.next;
		    }        		
		
			// test the printall.
			System.out.println("\n We test the remote site printing.");
			// here is a test.
			zcs.printAll();
			in.close();
		} catch (RemoteException440 re) {
			System.out.println(re.getMessage());
	    } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
		
}

