package services;

import core.Remote440;
import core.RemoteException440;

public interface ZipCodeRList extends Remote440 {
    public String find(String city) throws RemoteException440;
    public ZipCodeRList add(String city, String zipcode) throws RemoteException440;
    public ZipCodeRList next() throws RemoteException440;   
}