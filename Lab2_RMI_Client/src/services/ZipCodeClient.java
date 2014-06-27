package services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;

import core.Naming440;
import core.RMIConstants;
import core.RemoteException440;

public class ZipCodeClient { 
 
	public static void main(String[] args) {
	
		try {
			// manually supply arguments
			String host = InetAddress.getLocalHost().getHostName();
			int port = RMIConstants.REGISTRY_PORT;
			String serviceName = "ZipCodeService";
			String fileName = "zipcodes.txt";
			
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			
			ZipCodeService zcs = (ZipCodeService) Naming440.lookup(host, port, serviceName);
						
			if (zcs == null) {
				System.out.println("Shit! I've got a NullPointer!");
			} else {
				System.out.println("Stub generated successfully!");
			}

			// reads the data and make a "local" zip code list.
			// later this is sent to the server.
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


