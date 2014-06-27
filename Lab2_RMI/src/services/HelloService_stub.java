package services;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import core.RMIMessage;
import core.RemoteException440;
import core.RemoteObjectReference;
import core.Stub;

public class HelloService_stub implements HelloService, Stub {
	
	private static final long serialVersionUID = -1870618768947142465L;
	
	// instance variables
	private RemoteObjectReference roRef;
	private RMIMessage replyMessage;
	private transient Socket socket;
	private transient ObjectOutputStream outputStream;
	private transient ObjectInputStream inputStream;
	
	public HelloService_stub() {
		 
	}

	@Override
	public String sayHello() throws RemoteException440 {
		// check whether reference is attached
		if (roRef == null) {
			throw new RuntimeException("No Remote Object Reference attached!!");
		}
		
		// create a RMIMessage
		RMIMessage message = new RMIMessage("sayHello", null);
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
	
	public void setName(String name) throws RemoteException440 {
		// check whether reference is attached
		if (roRef == null) {
			throw new RuntimeException("No Remote Object Reference attached!!");
		}
		
		// create a RMIMessage
		RMIMessage message = new RMIMessage("setName", new Object[]{name});
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
	
	public HelloService newHello() throws RemoteException440 {
		// check whether reference is attached
		if (roRef == null) {
			throw new RuntimeException("No Remote Object Reference attached!!");
		}
		
		// create a RMIMessage
		RMIMessage message = new RMIMessage("newHello", null);
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
		Object returnObj = replyMessage.getReturnValue();
		return (HelloService)((RemoteObjectReference) returnObj).localise();
	}
	
	public String introduce(HelloService hs) throws RemoteException440 {
		// check whether reference is attached
		if (roRef == null) {
			throw new RuntimeException("No Remote Object Reference attached!!");
		}
		
		// create a RMIMessage
		RMIMessage message = new RMIMessage("introduce", new Object[]{hs});
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

	// attach a remote reference
	@Override
	public void attachReference(RemoteObjectReference roRef) {
		this.roRef = roRef;		
	}

	// return a remote reference
	@Override
	public RemoteObjectReference getReference() {
		return this.roRef;
	}
	
	
	

}
