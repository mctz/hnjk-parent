package com.hnjk.edu.finance.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;



import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.FileUtils;
import com.hnjk.core.foundation.utils.FtpUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.finance.model.Reconciliation;
import com.hnjk.edu.finance.model.TempStudentFee;
import com.hnjk.edu.finance.service.IReconciliationService;
import com.hnjk.edu.finance.service.ITempStudentFeeService;
import com.hnjk.edu.netty.common.NettyConstants;
import com.hnjk.edu.netty.common.ReconciliationUtil;
import com.hnjk.edu.netty.common.StringUtil;
import com.hnjk.edu.netty.vo.ReconciliationInfo;
import com.hnjk.edu.netty.vo.ReconciliationReq;
import com.hnjk.edu.netty.vo.ReconciliationResp;

@Service("iReconciliationService")
public class ReconciliationServiceImpl extends BaseServiceImpl<Reconciliation> implements IReconciliationService {
	
	@Autowired
	@Qualifier("tempStudentFeeService")
	private ITempStudentFeeService tempStudentFeeService;

	@Override
	public Page getReconciliationPage(Map<String, Object> condition,Page objPage) throws Exception{
		objPage.setPageSize(100);
		objPage.setOrderBy("uploadDate");
		objPage.setOrder("desc");
		String hql = "from "+Reconciliation.class.getSimpleName()+" where isDeleted=0 ";
		Page page = this.findByHql(objPage, hql, condition);
		return page;
	}
	@Override
	public String saveReconciliation(String request) throws Exception{
		String respMsg="";
		ReconciliationReq rreq = new ReconciliationReq(request);
		Reconciliation entity = new Reconciliation();
		ReconciliationResp resp = new ReconciliationResp();
		try {
			//1、验证FTP服务器上是否有这个文件
			String downloadDir = initFtpServer();
			String fileName=downloadDir+rreq.getFileName();			
			int isExist = FtpUtils.download("", rreq.getFileName(), fileName);
			if(isExist==0){//下载文件成功，表示文件存在
				//2、保存文件信息
				entity.setFileName(rreq.getFileName());
				entity.setTotalCount(Integer.valueOf(rreq.getTotalCount()));
				entity.setTotalFee(Double.valueOf(rreq.getTotalfee()));
				entity.setUploadDate(ExDateUtils.parseDate(rreq.getDate(),"yyyyMMdd"));
				this.saveOrUpdate(entity);
				//进行对账
				String info = FileUtils.readFile(fileName);
				ReconciliationUtil thread = new ReconciliationUtil(info, entity);
				Thread t1=new Thread(thread);
				t1.start();
				//3、返回响应信息
				resp.setDealCode(rreq.getDealCode());
				resp.setRespCode(NettyConstants.RESP_CODE_000);
			}else{
				resp.setDealCode(rreq.getDealCode());
				resp.setRespCode(NettyConstants.RESP_CODE_022);
				resp.setRespMsg("下载文件失败："+rreq.getFileName());
			}
		} catch (Exception e) {			
			resp.setDealCode(rreq.getDealCode());
			resp.setRespCode(NettyConstants.RESP_CODE_022);
			resp.setRespMsg("Server端保存对账文件信息时发生错误");
			e.printStackTrace();
		} finally{
			FtpUtils.close();//关闭FTP服务
			resp.setHeadCode(StringUtil.fillWithSpace(String.valueOf(NettyConstants.RESP_CODE_LENGTH_510),4));
			respMsg = resp.getResponseBase();
		}
		return respMsg;
	}
	/**
	 * @return
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	@Override
	public String initFtpServer() throws IOException, NumberFormatException {
//		Properties prop = new Properties();//属性集合对象
//		InputStream ins=SensitiveWordController.class.getClassLoader().getResourceAsStream("sys_conf.properties"); 
//		BufferedReader br= new BufferedReader(new InputStreamReader(ins));  
//		prop.load(br);
		ConfigPropertyUtil property = ConfigPropertyUtil.getInstance();
		String server=property.getProperty("ftp.ip");
		int port =Integer.valueOf(property.getProperty("ftp.port"));
		String user = property.getProperty("ftp.user");
		String password = property.getProperty("ftp.password");
		String path = property.getProperty("ftp.path");
		String downloadDir = property.getProperty("ftp.downloadDir");
		FtpUtils.getFtpConnect(server, port, user, password, path);
		return downloadDir;
	}
	
	@Override
	public int saveReconciliation(List<TempStudentFee> list, String[] strArray) {
		int tmpCount=0;
		
		for(int i=1;i<strArray.length;i++){//因为第一行是日期及总笔数等，跳过，从下标1开始
			String line=strArray[i];
			ReconciliationInfo ri = new ReconciliationInfo(line);
			String dealSerail = ri.getDealSerial();
			String fee =ri.getFee();			
			TempStudentFee entity=tempStudentFeeService.isSync(dealSerail,fee);
			if(entity!=null){				
				entity.setIsReconciliation(Constants.BOOLEAN_YES);
				tmpCount++;
				list.add(entity);
			}	 
			
		}
		return tmpCount;
	}
	@Override
	public boolean deleteReconciliation(String resourceids){
		boolean result=true;
		List<Reconciliation> list = new ArrayList<Reconciliation>();
		for(String resourceid : resourceids.split("\\,")){
			Reconciliation re = this.get(resourceid);
			if(re!=null){
				re.setIsDeleted(1);
				list.add(re);
			}
		}
		if(list.size()>0){
			this.batchSaveOrUpdate(list);
			
		}else{
			result = false;
		}
		
		return result;
		
	}
}
