package services;

import java.io.Serializable;

//  a class required for the ZipCodeService interface 
public class ZipCodeList implements Serializable {

	private static final long serialVersionUID = -4966374210089123302L;
	
	String city;
    String ZipCode;
    ZipCodeList next;

    public ZipCodeList(String c, String z, ZipCodeList n) {
		city=c;
		ZipCode=z;
		next=n;
    }
}
