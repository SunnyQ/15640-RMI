package core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * There is at least one instance of the stub class for each remote object in use
 * within the JVM. The stub's job is to handle the marshalling of the method invocation
 * into a message, delivery of the message to the communication module, and the reverse of
 * this process, all the way to the client object, upon the methods return
 * 
 * @author alex
 */
public class Remote440_stub {
	
	// instance variables
	private RemoteObjectReference roRef;
	private RMIMessage replyMessage;
	private Socket socket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	
	// default constructor
	public Remote440_stub() {
		 
	}
	
	// attach Remote Object Reference
	public void attachReference(RemoteObjectReference roRef) {
		this.roRef = roRef;
	}

	// invoke the sayHello method of the remote interface
	public void invokeSayHello(String arg) {		
		
		// check whether reference is attached
		if (roRef == null) {
			throw new RuntimeException("No Remote Object Reference attached!!");
		}
		
		// create a RMIMessage
		RMIMessage message = new RMIMessage("sayHello", new String[]{arg});
		
		// establish connection to remote host and deliver RMIMessage
		try {
			socket = new Socket(roRef.getIP(), roRef.getPort());
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
			deliver(message);			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}
	
	// deliver RMIMessage to the communication module and wait for reply message
	public void deliver(RMIMessage message) {
		try {
			outputStream.writeObject(message);
			replyMessage = (RMIMessage) inputStream.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Object getReturnValue() {
		return replyMessage.getReturnValue();
	}
}
