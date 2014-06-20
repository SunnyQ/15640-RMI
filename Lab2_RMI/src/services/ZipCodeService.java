package services;

import core.Remote440;
import core.RemoteException440;

public interface ZipCodeService extends Remote440 {
    public void initialise(ZipCodeList newlist) throws RemoteException440;
    public String find(String city) throws RemoteException440;
    public ZipCodeList findAll() throws RemoteException440;
    public void printAll() throws RemoteException440;
}
