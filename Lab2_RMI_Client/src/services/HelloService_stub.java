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

/*
 * Stub class for HelloService which has the same method names
 * as the methods which need to be invoked remotely
 * The implementation of these method is way different than
 * that in the Hello class.
 * It marshals parameters and send message to RMI440 and also
 * receives messages from RMI440 and unmarshals it.
 */
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
	
	/**
	 * Create an RMIMessage described by "sayHello", establish connection, and deliver message to RMI440
	 * then receive return value from RMI440
	 */
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
	
	/**
	 * Create an RMIMessage described by "setName" and parameter
	 * and follow the same steps as the other stub methods
	 */
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
	
	/**
	 * Create an RMIMessage described by "newHello" and
	 * follow the same steps as the other stub methods
	 */
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
	
	/**
	 * Create an RMIMessage described by "introduce" and 
	 * follow the same steps as the other stub methods
	 */
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
	
	/**
	 * Deliver RMIMessage to the communication module and wait for reply message
	 * @param message
	 * @throws RemoteException440
	 */
	public void deliver(RMIMessage message) throws RemoteException440 {
		try {
			outputStream.writeObject(message);
			// if no object key generated, then wait for the assigned object key
			if (roRef.getObjectKey() == -1) {
				Integer assignedKey = (Integer) inputStream.readObject();
				roRef.setObjectKey(assignedKey);
			}
			// wait here until reply
			replyMessage = (RMIMessage) inputStream.readObject();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RemoteException440(e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RemoteException440(e.getMessage());
		}
	}
	
	/**
	 * helper for establishing connection to remote host
	 * @throws RemoteException440
	 */
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
	
	/**
	 * retrieve the return value of the method invocation
	 * @return
	 */
	public Object getReturnValue() {
		return replyMessage.getReturnValue();
	}

	/**
	 * attach a remote reference
	 */
	@Override
	public void attachReference(RemoteObjectReference roRef) {
		this.roRef = roRef;		
	}

	/**
	 * return a remote reference
	 */
	@Override
	public RemoteObjectReference getReference() {
		return this.roRef;
	}
	
}
