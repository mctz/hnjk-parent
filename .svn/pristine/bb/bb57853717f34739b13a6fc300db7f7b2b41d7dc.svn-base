package com.hnjk.edu.netty.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.edu.netty.common.NettyConstants;
import com.hnjk.platform.system.cache.CacheAppManager;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ipfilter.IpFilterRule;
import io.netty.handler.ipfilter.IpFilterRuleType;
import io.netty.handler.ipfilter.RuleBasedIpFilter;
import io.netty.handler.timeout.IdleStateHandler;

@Service("MyChannelInitializer")
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

	private static final Logger LOG = LoggerFactory
			.getLogger(MyChannelInitializer.class);

	private static final int MAX_IDLE_SECONDS = 60;

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();
		// 添加到pipeline中的handler会被串行处理(PS: 类似工业生产中的流水线)
		StringEncoder strEncoder = new StringEncoder(
				Charset.forName(NettyConstants.CODING));
		StringDecoder strDecoder = new StringDecoder(
				Charset.forName(NettyConstants.CODING));
		pipeline.addLast(strEncoder);
		pipeline.addLast(strDecoder);
		// 添加业务处理时间段
		if (!isBusinessTime()) {// 停止业务接口访问，即拒绝连接
			ch.writeAndFlush("该时间段停止接口访问");
			ch.close();
		} else {
			pipeline.addLast("idleStateCheck", new IdleStateHandler(
					MAX_IDLE_SECONDS, MAX_IDLE_SECONDS, MAX_IDLE_SECONDS));
			// 指定字符串编码器和解码器
			// 是否使用IP过滤白名单
			ConfigPropertyUtil property = ConfigPropertyUtil.getInstance();
			String isUseIpFilter = property.getProperty("server.isUseIpFilter");
			if ("Y".equalsIgnoreCase(isUseIpFilter)) {
				String ipStr = property.getProperty("server.ip.list");
				RuleBasedIpFilter allowHandler = ipFilterHandler(ipStr);
				pipeline.addLast(allowHandler);
			}
			// 使用addLast来添加自己定义的handler到pipeline中
			pipeline.addLast("handler", new NettyServerHandler());
		}

	}

	/**
	 * ip过滤handler
	 * 
	 * @param ipStr
	 * @return
	 */
	private RuleBasedIpFilter ipFilterHandler(String ipStr) {
		final String[] ipList = ipStr.split(",");
		// 定义IP过滤规则
		final IpFilterRule allowFilter = new IpFilterRule() {
			// 对IP地址进行匹配
			@Override
			public boolean matches(InetSocketAddress remoteAddress) {
				boolean result = false;
				InetAddress ip = remoteAddress.getAddress();
				String ipAddress = ip.getHostAddress();
				for (String ipStr : ipList) {
					try {
						// 使用IP 或 域名匹配
						if (ipStr.equals(ipAddress)
								|| InetAddress.getByName(ipStr)
										.getHostAddress()
										.equalsIgnoreCase(ipAddress)) {
							result = true;
							break;
						}
					} catch (UnknownHostException e) {
						result = false;
						e.printStackTrace();
					}
				}
				return result;
			}
			// 规则为允许通过
			@Override
			public IpFilterRuleType ruleType() {
				return IpFilterRuleType.ACCEPT;
			}
		};
		// 初始化handler
		RuleBasedIpFilter allowHandler = new RuleBasedIpFilter(allowFilter) {
			// 重写accept方法
			@Override
			public boolean accept(ChannelHandlerContext ctx,
					InetSocketAddress remoteAddress) {
				return allowFilter.matches(remoteAddress);
			}

			// 重写channelRejected方法，返回拒绝信息后关闭通信通道
			@Override
			protected ChannelFuture channelRejected(ChannelHandlerContext ctx,
					InetSocketAddress remoteAddress) {
				// 返回 ip deny 并关闭通信通道
				ctx.writeAndFlush("ip deny!your ip isn't allow! your ip is:"
						+ remoteAddress.getAddress().getHostAddress());
				ctx.close();
				return null;
			}
		};
		return allowHandler;
	}

	private boolean isBusinessTime() throws Exception {
		boolean result = true;
		Date now = new Date();
		// 时间段
		String timeQuantum = CacheAppManager.getSysConfigurationByCode(
				"public_business_time_quantum").getParamValue();
		String[] _timeQuantum = timeQuantum.split("-");
		// 开始时间
		String startTimeStr = _timeQuantum[0];
		// 结束时间
		String endTimeStr = _timeQuantum[1];
		String today = ExDateUtils.getCurrentDate();
		String today_time = today + " " + startTimeStr;
		Date tomorrowTemp = ExDateUtils.addDays(new Date(), 1);
		String tomorrow = ExDateUtils.formatDateStr(tomorrowTemp, 1);
		String tomorrow_time = tomorrow + " " + endTimeStr;
		// 今天 如2017-11-02 23:00:00
		Date _today = ExDateUtils.parseDate(today_time,
				ExDateUtils.PATTREN_DATE_TIME);
		// 明天 如2017-11-03 01:00:00
		Date _tomorrow = ExDateUtils.parseDate(tomorrow_time,
				ExDateUtils.PATTREN_DATE_TIME);
		if (now.after(_today) && now.before(_tomorrow)) {
			result = false;
		}
		return result;
	}

}
