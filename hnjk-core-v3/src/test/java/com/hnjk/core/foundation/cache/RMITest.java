package com.hnjk.core.foundation.cache;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.junit.Before;
import org.junit.Test;

public class RMITest {

	@Before
	public void setUp() throws Exception{
		Hellow hellow = new HellowImpl();
		LocateRegistry.createRegistry(8888);  	   
        Naming.bind("rmi://localhost:8888/hellow", hellow); 
       
	}
	
	@Test
	public void testHasBind() throws Exception{
		try {			 
		  Registry registry  = LocateRegistry.getRegistry("localhost", 8888);			 	 
		  if(null != registry.list()){
			  String[] list = registry.list();	
			  for(String str : list){
				  System.out.println("已绑定..."+str);
			  }			 	
		  }					
			
		} catch (RemoteException e) {
			System.out.println("未绑定...");			
			}
		
	}
	
	@Test
	public void testInvoker() throws Exception{
		Registry registry  = LocateRegistry.getRegistry("localhost", 8888);
		if(null != registry){
			Hellow hellow = (Hellow)registry.lookup("hellow");
			hellow.sayHellow("Marco");
		}
	}
	
	@Test
	public void testUnbind() throws Exception{
		Registry registry  = LocateRegistry.getRegistry("localhost", 8888);
		boolean result = UnicastRemoteObject.unexportObject(registry, true);
		if(result){
			System.out.println("已解除绑定...");
		}else{
			System.out.println("未解除绑定...");
		}
	}
}
