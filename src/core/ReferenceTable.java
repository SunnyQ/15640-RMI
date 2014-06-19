package core;

import java.util.HashMap;

/**
 * ReferenceTable implemented using a hash table, keeps track of the one-to-one mapping
 * between RemoteObjectReferences and Local Objects. This table is maintained at ServerSide
 *  
 * @author alex
 */
public class ReferenceTable {
	
	
	private static int objectCounter = 0; // Generates a new objectKey for each remote object
	private HashMap<RemoteObjectReference, Object> referenceTable;
    
    // make a new table. 
    public ReferenceTable() {
    	referenceTable = new HashMap<RemoteObjectReference, Object>();
	}

    // add a remote object to the table. 
    public void addObj(String host, int port, Object object) {
    	Class<?> objectClass = object.getClass();
    	String interfaceName = objectClass.getInterfaces()[0].getName();
    	RemoteObjectReference roRef = new RemoteObjectReference(host, port, objectCounter, interfaceName);
    	referenceTable.put(roRef, object);
    	objectCounter++;  // increment the internal counter to ensure unique ID
	}

    // given a RemoteObjectReference(key), find the corresponding object.
    public Object findObj(RemoteObjectReference roRef) {
	    return referenceTable.get(roRef);
	}
}
