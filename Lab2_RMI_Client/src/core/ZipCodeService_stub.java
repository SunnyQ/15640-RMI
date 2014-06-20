package core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import core.RMIMessage;
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
	public void initialize(ZipCodeList newlist) {		
		
		// check whether reference is attached
		if (roRef == null) {
			throw new RuntimeException("No Remote Object Reference attached!!");
		}
		
		// create a RMIMessage
		RMIMessage message = new RMIMessage("initialize", new Object[]{newlist});
		// establish connection 
		establishConnection();
		// deliver RMIMessage
		deliver(message);
	}
	
	// invoke the find() method of the remote interface
	public String find(String city) {		
		
		// check whether reference is attached
		if (roRef == null) {
			throw new RuntimeException("No Remote Object Reference attached!!");
		}
		
		// create a RMIMessage
		RMIMessage message = new RMIMessage("find", new Object[]{city});
		// establish connection 
		establishConnection();
		// deliver RMIMessage
		deliver(message);
		return (String) replyMessage.getReturnValue();
	}
	
	// invoke the findAll() method of the remote interface
	public ZipCodeList findAll() {
		// check whether reference is attached
		if (roRef == null) {
			throw new RuntimeException("No Remote Object Reference attached!!");
		}
		
		// create a RMIMessage
		RMIMessage message = new RMIMessage("findAll", null);
		// establish connection 
		establishConnection();
		// deliver RMIMessage
		deliver(message);
		return (ZipCodeList) replyMessage.getReturnValue();
	}
	
	// invoke the printAll() method of the remote interface
	public void printAll() {
		// check whether reference is attached
		if (roRef == null) {
			throw new RuntimeException("No Remote Object Reference attached!!");
		}
		
		// create a RMIMessage
		RMIMessage message = new RMIMessage("printAll", null);
		// establish connection 
		establishConnection();
		// deliver RMIMessage
		deliver(message);
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
	
	// helper for establishing connection to remote host
	private void establishConnection() {
		try {
			socket = new Socket(roRef.getIP(), roRef.getPort());
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// retrieve the return value of the method invocation
	public Object getReturnValue() {
		return replyMessage.getReturnValue();
	}
}
