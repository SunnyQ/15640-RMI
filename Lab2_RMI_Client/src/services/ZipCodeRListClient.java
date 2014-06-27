package services;

//a client for ZipCodeRList.
//it uses ZipCodeRList as an interface, and test
//all methods by printing all data.

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
import java.util.Stack;

import core.LocateRMIRegistry;
import core.RMIConstants;
import core.RMIRegistry;
import core.RemoteException440;
import core.RemoteObjectReference;

public class ZipCodeRListClient { 

	public static void main(String[] args) {
	 
		try {
			// manually supply arguments
			String host = InetAddress.getLocalHost().getHostName();
			int port = RMIConstants.REGISTRY_PORT;
			String serviceName = "ZipCodeRList";
			String fileName = "zipcodes.txt";
		
			// locate the registry and get ror.
			RMIRegistry registry = LocateRMIRegistry.getRegistry(host, port);
			RemoteObjectReference ror = registry.lookup(serviceName);
		
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			
			// get (create) the stub out of ror.
			ZipCodeRList_stub rl = (ZipCodeRList_stub) ror.localise();
			
			// attach reference
			rl.attachReference(ror);
		
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
			
			// test "add".
			System.out.println("testing add.");
			temp = l;
			ZipCodeRList_stub rtemp = rl;
			// use the help of a stack to get the list in the same order as the original file
			// this is actually to fix a bug in the test program
			Stack<ZipCodeList> myStack = new Stack<ZipCodeList>();
			while (temp != null) {
				myStack.push(temp);
				temp = temp.next;                        
			}
			while (!myStack.isEmpty()) {
				temp = myStack.pop();
				rl = rl.add(temp.city, temp.ZipCode);				
			}
			System.out.println("add tested.");
			// rl should contain the initial head of the list.
		
			// test "find" and "next" by printing all. 
			// This is also the test that "add" performed all right.
			System.out.println("\n This is the remote list, printed using find/next.");
			temp = l;
			rtemp = rl;
			while (temp !=null) {
				// here is a test.
				String res = rtemp.find(temp.city);
				System.out.println("city: "+temp.city+", "+
						   "code: "+res);
				temp=temp.next;
				rtemp = rtemp.next();
			}        		
		} catch (RemoteException440 re) {
			System.out.println(re.getMessage());
	    } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
}
