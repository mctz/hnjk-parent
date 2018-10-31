package com.hnjk.core.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class TestSocketServer implements Runnable{

	Selector selector;
	   
    boolean running;
   
    SocketChannel sc;
    
    public TestSocketServer(){
        running=true;
       
    }
    public void init() {
       try {
           sc = SocketChannel.open();
           sc.configureBlocking(false);
           sc.connect(new InetSocketAddress("localhost", 2345));
          
          
       } catch (IOException ex) {
           
       }
    }
  
   public static void main(String[] args){
      
	   TestSocketServer client=new TestSocketServer();
       new Thread(client).start();
   }
    
      
   public void execute(){
      
       int num=0;
       try {
           while (!sc.finishConnect()) {
           }
       } catch (IOException ex) {
           
       }
       ReadKeyBoard rkb=new ReadKeyBoard();
       new Thread(rkb).start();
       while(running){
           try {
              
               ByteBuffer buffer=ByteBuffer.allocate(1024);
               buffer.clear();
               StringBuffer sb=new StringBuffer();
               Thread.sleep(500);
              
               while((num=sc.read(buffer))>0){
                   sb.append(new String(buffer.array(),0,num));
                   buffer.clear();
               }
               if(sb.length()>0) System.out.println(sb.toString());
               if(sb.toString().toLowerCase().trim().equals("bye")){
                   System.out.println("closed....");
                  
                   sc.close();
                   sc.socket().close();
                   rkb.close();
                   running=false;
               }
           } catch (InterruptedException ex) {
              
           } catch (IOException ex) {
               
           }
         }
    }
   
   public void run() {
       init();
       execute();
   }
   
 class ReadKeyBoard implements Runnable{
   boolean running2=true;
   public ReadKeyBoard(){
      
   }
   public void close(){
       running2=false;
   }
   
   public void run() {
       BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
       while(running2){
           try {
               System.out.println("enter some commands:");
           String str = reader.readLine();
           System.out.println("get commands:"+str);
           sc.write(ByteBuffer.wrap(str.getBytes()));
       } catch (IOException ex) {
         
       }
       }
   }
}
	
}
