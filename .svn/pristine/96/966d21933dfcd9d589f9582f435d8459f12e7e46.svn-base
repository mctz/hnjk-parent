package com.hnjk.edu.netty.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import com.hnjk.edu.finance.service.IReconciliationService;
import com.hnjk.edu.finance.service.ITempStudentFeeService;
import com.hnjk.edu.netty.common.NettyConstants;
import com.hnjk.edu.netty.common.StringUtil;
import com.hnjk.edu.netty.vo.PaymentResp;
import com.hnjk.edu.netty.vo.QueryAssessmentResp;
import com.hnjk.edu.netty.vo.ResponseBase;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
@Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<Object> {
	
	public NettyServerHandler(){
		super();
	};
	
	private static final Logger LOG = LoggerFactory.getLogger(NettyServerHandler.class);
	
	private String respMsg;
	
	@Autowired
	@Qualifier("tempStudentFeeService")
	private ITempStudentFeeService tempStudentFeeService;
	
	
	@Override
	public boolean isSharable() {
//		LOG.info("==============handler-sharable==============");
		return super.isSharable();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
//		LOG.info("==============channel-register==============");
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
//		LOG.info("==============channel-active==============");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//		ctx.channel().remoteAddress();
//		LOG.info("==============channel-inactive==============");
	}
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
//		LOG.info("==============channel-unregister==============");
	}
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception{
//		LOG.info("==============有一客户端连入=============="+ctx.channel().remoteAddress());
	}
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception{
//		LOG.info("==============有一客户端断开=============="+ctx.channel().remoteAddress());
	}
	WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
	ITempStudentFeeService is = (ITempStudentFeeService) wac
			.getBean("tempStudentFeeService");
	IReconciliationService ir = (IReconciliationService) wac.getBean("iReconciliationService");
	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception{
		ResponseBase resp = new ResponseBase();
		String dealCode="";
		String headCode="";
		String req = (String)msg;
		try {
			
			int  msgLength=StringUtil.strCnlength(req);
			headCode=req.substring(0,4).trim();			
			dealCode = req.substring(4,7);
			if(msgLength-4!=Integer.valueOf(headCode)){
				resp.setHeadCode(req.substring(0,4));
				resp.setDealCode(dealCode);
				resp.setRespMsg("请求的数据包长度不正确！");
				resp.setRespCode(NettyConstants.RESP_CODE_020);
				respMsg=resp.getResponseBase();
			}else{
				switch (msgLength) {
				case NettyConstants.DEAL_CODE_LENGTH_510://对账
					if(dealCode.equalsIgnoreCase(NettyConstants.DEAL_CODE_510)){//匹配
						//业务处理 返回响应内容
						respMsg = ir.saveReconciliation(req.substring(4));
					}else{//无法识别交易码，返回其他错误 022
						resp.setHeadCode(""+NettyConstants.RESP_CODE_LENGTH_510);
						resp.setDealCode(dealCode);
						resp.setRespMsg("无法识别交易码"+dealCode);
						resp.setRespCode(NettyConstants.RESP_CODE_022);
						respMsg=resp.getResponseBase();
					}
					break;
					
				case NettyConstants.DEAL_CODE_LENGTH_520://查询应缴金额			
					QueryAssessmentResp qa = new QueryAssessmentResp();
					if(dealCode.equalsIgnoreCase(NettyConstants.DEAL_CODE_520)){//匹配	
						respMsg = is.queryAssessment(req.substring(4));
					}else{//无法识别交易码，返回其他错误 022
						qa.setHeadCode(""+NettyConstants.RESP_CODE_LENGTH_520);
						qa.setDealCode(dealCode);
						qa.setRespCode(NettyConstants.RESP_CODE_022);
						qa.setRespMsg("无法识别的交易码："+dealCode);
						respMsg=qa.getQueryAssessment();				
					}
					break;
					
				case NettyConstants.DEAL_CODE_LENGTH_540://缴费
					PaymentResp py = new PaymentResp();
					if(dealCode.equalsIgnoreCase(NettyConstants.DEAL_CODE_540)){//匹配
						respMsg=is.paymentRequest(req.substring(4));
						LOG.info("请求包信息："+req);
					}else{//无法识别交易码，返回其他错误 022
						py.setHeadCode(""+NettyConstants.RESP_CODE_LENGTH_540);
						py.setDealCode(dealCode);
						py.setRespCode(NettyConstants.RESP_CODE_022);
						py.setRespMsg("无法识别交易码"+dealCode);
						respMsg=py.getPayment();
					}
					break;

				default://数据包长度错误，返回020			
					String respStr = req.substring(0,4)+dealCode+NettyConstants.RESP_CODE_020+"请求的数据包长度不正确！";
//					if(StringUtil.strCnlength(respStr)>msgLength){
//						msgLength = StringUtil.strCnlength(respStr);
//					}
					respMsg = StringUtil.fillWithSpace(respStr, msgLength);
					break;
				}
			}
			
		}catch (StringIndexOutOfBoundsException e){
			respMsg="请求的数据包长度不正确！";
			e.printStackTrace();
		}catch (Exception e) {
			resp.setHeadCode(req.substring(0,4));
			resp.setDealCode(dealCode);
			resp.setRespMsg("接口发生异常，请联系服务端管理员！");
			resp.setRespCode(NettyConstants.RESP_CODE_022);
			respMsg=resp.getResponseBase();
			e.printStackTrace();
		} finally {
			ctx.write(respMsg);
		}	
	}
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
		//添加事件监听，当数据flush完成后，关闭 channel ，释放资源
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
		cause.printStackTrace();
		ctx.close();
	}
}
