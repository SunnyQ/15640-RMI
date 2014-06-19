package test;

import java.io.Serializable;
import java.rmi.RemoteException;

import core.Remote440;


public class Employee implements Serializable, Remote440 {
	
	private static final long serialVersionUID = 3106923159988464826L;
	private String name;
	private String company;
	
	public Employee(String name, String company) {
		this.name = name;
		this.company = company;
	}

	// method which supports remote invocation
	@Override
	public void sayHello(String mood) throws RemoteException {
		System.out.println("Hi, my name is: " + name);
		System.out.println("My employer is: " + company);
		System.out.println("I am feeling " + mood + " today!");
		
	}
}
