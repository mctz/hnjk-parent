package com.hnjk.edu.netty.server;

import java.net.InetSocketAddress;
import java.util.Map;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;

public class NettyConfig {
	
	private Map<ChannelOption<?>, Object> channelOptions;

    // reactor多线程模型中的acceptor
    private NioEventLoopGroup bossGroup;

    // reactor多线程模型中的threadPool
    private NioEventLoopGroup workerGroup;

    //bossGroup的线程数
    private int bossThreadCount;

    //workerGroup的线程数
    private int workerThreadCount;
    private InetSocketAddress socketAddress;
    private int portNumber = 18090;
    protected ChannelInitializer<? extends Channel> channelInitializer;

    public Map<ChannelOption<?>, Object> getChannelOptions() {
        return channelOptions;
    }

    public void setChannelOptions(
            Map<ChannelOption<?>, Object> channelOptions) {
        this.channelOptions = channelOptions;
    }

    public synchronized NioEventLoopGroup getBossGroup() {
        if (null == bossGroup) {
            if (0 >= bossThreadCount) {
                bossGroup = new NioEventLoopGroup();
            } else {
                bossGroup = new NioEventLoopGroup(bossThreadCount);
            }
        }
        return bossGroup;
    }

    public void setBossGroup(NioEventLoopGroup bossGroup) {
        this.bossGroup = bossGroup;
    }

    public synchronized NioEventLoopGroup getWorkerGroup() {
        if (null == workerGroup) {
            if (0 >= workerThreadCount) {
                workerGroup = new NioEventLoopGroup();
            } else {
                workerGroup = new NioEventLoopGroup(workerThreadCount);
            }
        }
        return workerGroup;
    }

    public void setWorkerGroup(NioEventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }

    public int getBossThreadCount() {
        return bossThreadCount;
    }

    public void setBossThreadCount(int bossThreadCount) {
        this.bossThreadCount = bossThreadCount;
    }

    public int getWorkerThreadCount() {
        return workerThreadCount;
    }

    public void setWorkerThreadCount(int workerThreadCount) {
        this.workerThreadCount = workerThreadCount;
    }

    public synchronized InetSocketAddress getSocketAddress() {
        if (null == socketAddress) {
            socketAddress = new InetSocketAddress(portNumber);
        }
        return socketAddress;
    }

    public void setSocketAddress(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }


}
