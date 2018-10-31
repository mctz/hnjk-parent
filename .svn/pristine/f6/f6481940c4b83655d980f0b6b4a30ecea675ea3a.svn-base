package com.hnjk.core.annotation;

import java.rmi.RemoteException;
import java.util.Map;

public interface IRemotingService {

	String simpleInvoker(String args) throws RemoteException;
	
	Map<String, Object> remotingInvoker(final String sql,Map<String, Object> condition) throws RemoteException;
	
	void remotingScheduleInvoker() throws RemoteException;
}
