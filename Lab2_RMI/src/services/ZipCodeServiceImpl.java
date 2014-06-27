package services;

import core.Naming440;
import core.RemoteException440;


//in implementation, you do not have to extend this as in Java RMI. 
//in your design, however, you can do so.
//it is assumed that this is not directly called but as in:
//
//java yourRMI ZipCodeServerImpl registryhost resigstryport servicename
//
//therefore it does not contain main: new object creation, binding etc. is 
//done via your RMI.

public class ZipCodeServiceImpl implements ZipCodeService {
	
	private static final String serviceName = "ZipCodeService";
	
	private ZipCodeList l;
	
	// this is a constructor.
	public ZipCodeServiceImpl() throws RemoteException440 {
		l=null;
	}

	// when this is called, marshalled data
	// should be sent to this remote object,
	// and reconstructed.
	public void initialize(ZipCodeList newlist) throws RemoteException440 {
		l=newlist;
	}

	// basic function: gets a city name, returns the zip code.
	public String find(String request) throws RemoteException440 {
		// search the list.
		ZipCodeList temp = l;
		while (temp != null && !temp.city.equals(request))	
			temp = temp.next;
		
		// the result is either null or we found the match.
		if (temp==null)
		    return null;
		else
		    return temp.ZipCode;
	}

	// this very short method should send the marshalled 
	// whole list to the local site.
	public ZipCodeList findAll() throws RemoteException440 {
		return l;
	}

    // this method does printing in the remote site, not locally.
	public void printAll() throws RemoteException440 {
		ZipCodeList temp=l;
		while (temp !=null) {
			System.out.println
			    ("city: "+temp.city+", "+
			     "code: "+temp.ZipCode+"\n");        
			temp = temp.next;                        
		}
	}
	
	
	public static void main(String[] args) {
		try {
			
			// instantiate an instance of this class
			ZipCodeServiceImpl zcs = new ZipCodeServiceImpl();
			// tie the new instance to the Object we just created
			Naming440.rebind(serviceName, zcs);
			// print out a console message
			System.out.println(serviceName + " ready");
			
		} catch (RemoteException440 e) {
			e.printStackTrace();
		}
	}
	
}
