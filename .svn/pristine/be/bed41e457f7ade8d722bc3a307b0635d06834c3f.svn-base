package com.hnjk.job;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.ContextLoader;

import com.hnjk.core.foundation.utils.SensitivewordFilter;
import com.hnjk.platform.system.service.ISensitiveWordService;

/**
 * 处理定时任务-加载敏感词
 * @author zik, 广东学苑教育发展有限公司
 *
 */
public class SensitiveWordScheduler {

	protected static Logger logger = LoggerFactory.getLogger(SensitiveWordScheduler.class);
	
	@Qualifier("sensitiveWordService")
	@Autowired
	private ISensitiveWordService sensitiveWordService;
	
	/**
	 * 加载敏感词库
	 * @param request
	 */
	public void loadSensitiveWord(){
		ServletContext application = ContextLoader.getCurrentWebApplicationContext().getServletContext();
		// 判断敏感词库是否有更新
		String lastUpdateStr = sensitiveWordService.getLastUpateDate();
		// 获取内存中的敏感词最新的更新时间
		if(application!=null){
			if(!lastUpdateStr.equals(application.getAttribute("SW_lastUpdate"))){
				SensitivewordFilter sensitivewordFilter = sensitiveWordService.getSensitivewordFilter();
				application.setAttribute("sensitivewordFilter", sensitivewordFilter);
				application.setAttribute("SW_lastUpdate", lastUpdateStr);
			}
		}
	}
}
