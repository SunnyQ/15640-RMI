package core;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Each remote object implements a remote interface that specifies
 * which of its methods can be invoked remotely
 * 
 * @author alex
 *
 */

public interface Remote440 extends Remote {
	
	public void sayHello(String name) throws RemoteException;
	
}
