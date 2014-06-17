package test;

import java.io.Serializable;

import core.RMIMessage;


public class Employee implements Serializable {
	
	private static final long serialVersionUID = 3106923159988464826L;
	private String name;
	private String company;
	
	public Employee(String name, String company) {
		this.name = name;
		this.company = company;
	}
		
	public void introduce(String mood)
	{
		System.out.println("Hi, my name is: " + name);
		System.out.println("I am feeling " + mood + " today!");
	}
	
	public String getInfo() {
		return "[" + name + "," + company + "]";
	}
	
	// test driver for RMIMessage
	public static void main (String[] args) {
		Employee employee = new Employee("Alex", "Apple");
		// test with arguments but without return value
		RMIMessage message = new RMIMessage(employee, "introduce", new String[]{"awesome"});
		message.invoke(); 
		// check for potential exceptions occurred during method invocation
		if (message.getException() == null) {
			System.out.println("invocation successful!");
		} else {
			System.out.println("encounered exception during invocation: " + message.getException());
		}
		// test that has return value
		RMIMessage message2 = new RMIMessage(employee, "getInfo", null);
		message2.invoke();
		System.out.println("return value: " + message2.getReturnValue());
		// check for potential exceptions occurred during method invocation
		if (message2.getException() == null) {
			System.out.println("invocation successful!");
		} else {
			System.out.println("encounered exception during invocation: " + message2.getException());
		}
	}
}
