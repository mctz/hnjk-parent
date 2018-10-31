package com.hnjk.edu.netty.common;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hnjk.edu.finance.model.Reconciliation;
import com.hnjk.edu.finance.model.TempStudentFee;
import com.hnjk.edu.finance.service.IReconciliationService;
import com.hnjk.edu.finance.service.ITempStudentFeeService;

public class ReconciliationUtil implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger(ReconciliationUtil.class);

	WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
	ITempStudentFeeService is = (ITempStudentFeeService) wac
			.getBean("tempStudentFeeService");
	IReconciliationService ir = (IReconciliationService) wac.getBean("iReconciliationService");
	private String info;
	
	private Reconciliation entity;	
	
	public ReconciliationUtil(String info,Reconciliation entity){
		this.info = info;
		this.entity = entity;
	}
	@Override
	public void run() {
//		EDU.EMORY.MATHCS.BACKPORT.JAVA.UTIL.CONCURRENT.TIMEUNIT.MILLISECONDS.SLEEP(10000);
		logger.info("创建线程进行自动对账");
		List<TempStudentFee> list = new ArrayList<TempStudentFee>();
		String [] strArray = info.split("\n");
		String  headline = strArray[0];
		//第一行：0-8位是日期，9-17位是总笔数，剩余为总金额
		int totalCount = Integer.valueOf(headline.substring(8, 17).trim());
		int tmpCount = ir.saveReconciliation(list, strArray);
		
		if(totalCount==tmpCount){//当文件中的对账数等于系统中更新的对账数时，更新对账文件的标识为已对账
			Reconciliation tmpEntity = entity;
			tmpEntity.setStatus(Reconciliation.status_yes);
			ir.saveOrUpdate(tmpEntity);
			is.batchSaveOrUpdate(list);
			logger.info("自动对账结束，对账结果：成功");
		}else{
			logger.info("自动对账结束，对账结果：失败,对账文件首行："+headline);
		}
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	public Reconciliation getEntity() {
		return entity;
	}
	public void setEntity(Reconciliation entity) {
		this.entity = entity;
	}
}
