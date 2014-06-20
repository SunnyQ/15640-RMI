package core;

/*For example, it  shows how you can get the host name etc.,
(well you can hardwire it if you like, I should say),
or how you can make a class out of classname as a string.

This just shows one design option. Other options are
possible. We assume there is a unique skeleton for each
remote object class (not object) which is called by CM 
by static methods for unmarshalling etc. We can do without
it, in which case CM does marshalling/unmarhshalling.
Which is simpler, I cannot say, since both have their
own simpleness and complexity.
*/

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class RMI440 {
	
	private static String host;
	private static int port;

	// It will use a hash table, which contains ROR together with
	// reference to the remote object.

	public static void main(String args[]) {
		
		String implClassName = args[0];
		String registryHost = args[1];
		int registryPort = Integer.parseInt(args[2]);	
		String serviceName = args[3];		

		try {
			// it should have its own port. assume you hardwire it.
			host = (InetAddress.getLocalHost()).getHostName();
			port = 12345;
			ServerSocket serverSoc = new ServerSocket(port);
			
			// locate the RMIRegistry
			RMIRegistry registry = LocateRMIRegistry.getRegistry(registryHost, registryPort);
					
			// it now have two classes from MainClassName: 
			// (1) the class itself (say ZipCodeServerImpl) and
			// (2) its skeleton.
			Class<?> implClass = Class.forName(implClassName);
//			Class<?> implSkeleton = Class.forName("services." + implClassName+"_skel");
			
			// you should also create a remote object table here.
			// it is a table of a ROR and a skeleton 
			ReferenceTable table = new ReferenceTable();
			
			// after that, you create one remote object of implClass.
			Object object = implClass.newInstance();
			
			// then register it into the table.
			table.addObj(host, port, object);
											
			// Now we go into a loop.
			// The code is far from optimal but in any way you can get basics.
			// Actually you should use multiple threads, or this easily
			// deadlocks. But for your implementation I do not ask it.
			// For design, consider well.
			while (true) {
				// receives an invocation request and create input/output stream
				Socket socket = serverSoc.accept();
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				// read RMIMessage
				RMIMessage request = (RMIMessage) in.readObject();
				// binds the reference to the serviceName in RMIRegistry
				registry.rebind(serviceName, request.getReference());
				// gets the real object reference from table.
				Object localReference = table.findObj(request.getReference());
				// (5) Either:
				//      -- using the interface name, asks the skeleton,
				//         together with the object reference, to unmartial
				//         and invoke the real object.
				//      -- or do unmarshalling directly and involkes that
				//         object directly.
				request.invoke(localReference);
				// (6) receives the return value, which (if not marshalled
				//     you should marshal it here) and send it out to the 
				//     the source of the invoker.
				out.writeObject(request);
				System.out.println("invocation complete! Return value has been delivered back!");
				// (7) closes the socket.
				serverSoc.close();
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
