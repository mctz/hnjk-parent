package com.hnjk.edu.netty.server;

import java.net.InetSocketAddress;

public interface Server {
	
	 public interface TransmissionProtocol{

	    }

	    // 服务器使用的协议
	    public enum TRANSMISSION_PROTOCOL implements TransmissionProtocol {
	        TCP,UDP
	    }

	    TransmissionProtocol getTransmissionProtocol();
	    // 启动服务器
	    void startServer() throws Exception;

	    void startServer(int port) throws Exception;;

	    void startServer(InetSocketAddress socketAddress) throws Exception;

	    // 关闭服务器
	    void stopServer() throws Exception;

	    InetSocketAddress getSocketAddress();

}
