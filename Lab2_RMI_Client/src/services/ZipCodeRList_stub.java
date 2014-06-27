package services;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import core.RMIMessage;
import core.RemoteException440;
import core.RemoteObjectReference;

public class ZipCodeRList_stub implements ZipCodeRList {
	
	// instance variables
	private RemoteObjectReference roRef;
	private RMIMessage replyMessage;
	private Socket socket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	
	// default constructor
	public ZipCodeRList_stub() {
		 
	}
	
	// attach Remote Object Reference
	public void attachReference(RemoteObjectReference roRef) {
		this.roRef = roRef;
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
	
	// invoke the add() method of the remote interface
	public ZipCodeRList_stub add(String city, String zipcode) throws RemoteException440 {
		// check whether reference is attached
		if (roRef == null) {
			throw new RuntimeException("No Remote Object Reference attached!!");
		}
		
		// create a RMIMessage
		RMIMessage message = new RMIMessage("add", new Object[]{city, zipcode});
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
		return (ZipCodeRList_stub) replyMessage.getReturnValue();
	}
	
	// invoke the next() method of the remote interface
	public ZipCodeRList_stub next() throws RemoteException440 {
		// check whether reference is attached
		if (roRef == null) {
			throw new RuntimeException("No Remote Object Reference attached!!");
		}
		
		// create a RMIMessage
		RMIMessage message = new RMIMessage("next", null);
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
		return (ZipCodeRList_stub) replyMessage.getReturnValue();
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
			if (replyMessage.getReturnValue() instanceof RemoteObjectReference) {
				// construct a new stub as the return value
				ZipCodeRList_stub newStub = new ZipCodeRList_stub();
				newStub.attachReference((RemoteObjectReference)replyMessage.getReturnValue());
				replyMessage.setReturnValue(newStub);
			}
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
