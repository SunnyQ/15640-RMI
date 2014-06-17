package core;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// A general format for representing messages between classes
public class RMIMessage implements Serializable {

	private static final long serialVersionUID = 3716508031587963606L;
	
	// instance variables
	private Object object;	      // target object to invoke method on
	private String methodName;    // method to invoke 
	private Object[] args;        // arguments to the method
	private Exception exception;  // store any exception generated
	private Object returnValue;   // store the return value of the method
	private Class<?> returnType;     // store the return type of the method
	
	// constructor
	public RMIMessage(Object object, String methodName, Object[] args) {
		this.object = object;
		this.methodName = methodName;
		this.args = args;
		exception = null;
		returnValue = null;
		returnType = null;
	}
	
	// invoke the specified method on the specified object with specified arguments
	public void invoke() {
		// check validity
		if (object == null || methodName == null) {
			throw new RuntimeException("Bad invocation...");
		}
		// invoke the method 
		Class<?> objectClass = object.getClass();
		Class<?>[] argsClass = null;
		if (args != null){
			argsClass = new Class<?>[args.length];
			for (int i = 0; i < args.length; i++) {
				argsClass[i] = args[i].getClass();
			}
		} 
		try {
			Method method = objectClass.getMethod(methodName, argsClass);
			returnType = method.getReturnType();   		
			returnValue = method.invoke(object, args);
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			this.exception = e;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			this.exception = e;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			this.exception = e;
		} catch (SecurityException e) {
			e.printStackTrace();
			this.exception = e;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			this.exception = e;
		}
		
	}
	
	// getter and setters
	public Object getObject() {
		return this.object;
	}
	
	public String getMethodName() {
		return this.methodName;
	}
	
	public Object[] getArguments() {
		return this.args;
	}
	
	public Exception getException() {
		return this.exception;
	}
	
	public Object getReturnValue() {
		return this.returnValue;
	}
	
	public Class<?> getReturnType() {
		return this.returnType;
	}

}
