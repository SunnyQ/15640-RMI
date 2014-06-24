package services;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import core.RMIMessage;
import core.RemoteException440;
import core.RemoteObjectReference;

/**
 * There is at least one instance of the stub class for each remote object in use
 * within the JVM. The stub's job is to handle the marshalling of the method invocation
 * into a message, delivery of the message to the communication module, and the reverse of
 * this process, all the way to the client object, upon the methods return
 * 
 * @author alex
 */
public class ZipCodeService_stub {
	
	// instance variables
	private RemoteObjectReference roRef;
	private RMIMessage replyMessage;
	private Socket socket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	
	// default constructor
	public ZipCodeService_stub() {
		 
	}
	
	// attach Remote Object Reference
	public void attachReference(RemoteObjectReference roRef) {
		this.roRef = roRef;
	}

	// invoke the initialize() method of the remote interface
	public void initialize(ZipCodeList newlist) throws RemoteException440 {		
		
		// check whether reference is attached
		if (roRef == null) {
			throw new RuntimeException("No Remote Object Reference attached!!");
		}
		
		// create a RMIMessage
		RMIMessage message = new RMIMessage("initialize", new Object[]{newlist});
		message.attachReference(roRef);
		
		// establish connection 
		establishConnection();
		// deliver RMIMessage
		deliver(message);
		// check for any exception encountered
		if (message.getException() != null) {
			throw new RemoteException440("Error occurred during invocation: "
													+ message.getException());
		}
	}
	
	// invoke the find() method of the remote interface
	public String find(String city) throws RemoteException440 {		
		
		// check whether reference is attached
		if (roRef == null) {
			throw new RuntimeException("No Remote Object Reference attached!!");
		}
		
		// create a RMIMessage
		RMIMessage message = new RMIMessage("find", new Object[]{city});
		message.attachReference(roRef);
		
		// establish connection 
		establishConnection();
		// deliver RMIMessage
		deliver(message);
		// check for any exception encountered
		if (message.getException() != null) {
			throw new RemoteException440("Error occurred during invocation: "
													+ message.getException());
		}
		return (String) replyMessage.getReturnValue();
	}
	
	// invoke the findAll() method of the remote interface
	public ZipCodeList findAll() throws RemoteException440 {
		// check whether reference is attached
		if (roRef == null) {
			throw new RuntimeException("No Remote Object Reference attached!!");
		}
		
		// create a RMIMessage
		RMIMessage message = new RMIMessage("findAll", null);
		message.attachReference(roRef);
		
		// establish connection 
		establishConnection();
		// deliver RMIMessage
		deliver(message);
		// check for any exception encountered
		if (message.getException() != null) {
			throw new RemoteException440("Error occurred during invocation: "
													+ message.getException());
		}
		return (ZipCodeList) replyMessage.getReturnValue();
	}
	
	// invoke the printAll() method of the remote interface
	public void printAll() throws RemoteException440 {
		// check whether reference is attached
		if (roRef == null) {
			throw new RuntimeException("No Remote Object Reference attached!!");
		}
		
		// create a RMIMessage
		RMIMessage message = new RMIMessage("printAll", null);
		message.attachReference(roRef);
		
		// establish connection 
		establishConnection();
		// deliver RMIMessage
		deliver(message);
		// check for any exception encountered
		if (message.getException() != null) {
			throw new RemoteException440("Error occurred during invocation: "
													+ message.getException());
		}
	}
	
	// deliver RMIMessage to the communication module and wait for reply message
	public void deliver(RMIMessage message) throws RemoteException440 {
		try {
			outputStream.writeObject(message);
			// if no object key generated, then wait for the assigned object key
			if (roRef.getObjectKey() == -1) {
				Integer assignedKey = (Integer) inputStream.readObject();
				roRef.setObjectKey(assignedKey);
			}
			replyMessage = (RMIMessage) inputStream.readObject();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RemoteException440(e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RemoteException440(e.getMessage());
		}
	}
	
	// helper for establishing connection to remote host
	private void establishConnection() throws RemoteException440 {
		try {
			socket = new Socket(roRef.getIP(), roRef.getPort());
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new RemoteException440(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RemoteException440(e.getMessage());
		}
	}
	
	// retrieve the return value of the method invocation
	public Object getReturnValue() {
		return replyMessage.getReturnValue();
	}
}
