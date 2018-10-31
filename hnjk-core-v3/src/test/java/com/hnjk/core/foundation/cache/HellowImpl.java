package com.hnjk.core.foundation.cache;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HellowImpl extends UnicastRemoteObject implements Hellow {

	
	protected HellowImpl() throws RemoteException {
		super();
		
	}

	private static final long serialVersionUID = -7558169533497410484L;

	@Override
	public void sayHellow(String name) throws RemoteException {
		System.out.println("hellow,"+name);
	}

}
