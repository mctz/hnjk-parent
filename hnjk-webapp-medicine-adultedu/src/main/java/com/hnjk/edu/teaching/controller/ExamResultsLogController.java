package com.hnjk.edu.teaching.controller;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.teaching.model.ExamResultsLog;
import com.hnjk.edu.teaching.service.IExamResultsLogService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 成绩导入导出日志Controller
 * @author luof
 *
 */
@Controller
public class ExamResultsLogController extends BaseSupportController{
	
	@Autowired
	@Qualifier("examResultsLogService")
	private IExamResultsLogService examResultsLogService;
	
	/**
	 * 写入日志
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/examResultsLog/add.html")
	public void addExamResultsLog(HttpServletRequest request,HttpServletResponse response){
		
		String attid       = ExStringUtils.trimToEmpty(request.getParameter("attid"));
		String examInfoId  = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
		String optionType  = ExStringUtils.trimToEmpty(request.getParameter("optionType"));
		User cureentUser   = SpringSecurityHelper.getCurrentUser();
		String logId       = "";
		ExamResultsLog log = new ExamResultsLog();

		log.setExamSubId(examInfoId);
		log.setOptionType(optionType);
		log.setFillinDate(new Date());
		log.setFillinMan(null==cureentUser?"无效Session":cureentUser.getUsername());
		log.setFillinManId(null==cureentUser?"无效Session":cureentUser.getResourceid());
		log.setAttachId(attid);
		try {
			examResultsLogService.save(log);
			logId = log.getResourceid();
			
		} catch (Exception e) {
			logger.error("写入成绩日志出错",e.fillInStackTrace());
		}

		renderJson(response,JsonUtils.objectToJson(logId));
	}
	
	/**
	 * 删除日志
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/examResultsLog/remove.html")
	public void removeResultsLog(HttpServletRequest request,HttpServletResponse response){
		
		String examResultsLogId      = ExStringUtils.trimToEmpty(request.getParameter("examResultsLogId"));
		//String logId                 = ExStringUtils.trimToEmpty(examResultsLogId);
		
		boolean              success = true;
		try {
			ExamResultsLog log = examResultsLogService.get(examResultsLogId);
			examResultsLogService.truncate(log);
			
		} catch (Exception e) {
			//logger.error("写入成绩日志出错",e.fillInStackTrace());
			success = false;
			e.printStackTrace();
		}
		renderJson(response,JsonUtils.objectToJson(success));
	}
}
