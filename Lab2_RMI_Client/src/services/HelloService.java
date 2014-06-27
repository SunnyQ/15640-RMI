package services;

import core.Remote440;
import core.RemoteException440;

public interface HelloService extends Remote440 {
	public String sayHello() throws RemoteException440;
	public void setName(String name) throws RemoteException440;
	public HelloService newHello() throws RemoteException440;
	public String introduce(HelloService hs) throws RemoteException440;
}
