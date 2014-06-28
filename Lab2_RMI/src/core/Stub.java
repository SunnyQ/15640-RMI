package core;

/*
 * Interface for general Stub
 * implemented by every specific stub class
 */
public interface Stub {
	
	/**
	 * attach an ROR on a stub object
	 * @param roRef
	 */
	public void attachReference(RemoteObjectReference roRef);
	
	/**
	 * retrieve ROR on a specific stub object
	 * @return
	 */
	public RemoteObjectReference getReference();
}
