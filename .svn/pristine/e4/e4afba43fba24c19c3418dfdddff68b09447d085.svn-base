package com.hnjk.cache;

import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.SysConfiguration;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IUserService;

/** 
 * 监听session的创建、失效和销毁
 * @author Zik, 广东学苑教育发展有限公司
 * @since Jul 21, 2016 10:57:31 AM 
 * 
 */
public class CustomSessionListener implements HttpSessionListener {

	private Logger userLonginLog =LoggerFactory.getLogger("userLoginFile");
	
	@Override
	public void sessionCreated(HttpSessionEvent se) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		try {
			ServletContext application = se.getSession().getServletContext();
			ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(application);
			IUserService userService = (IUserService)applicationContext.getBean("userService");
			User user = (User)se.getSession().getAttribute("loginLongUser");
			String userName = null;
			if(user!=null){
				userName = user.getUsername();
			}
//		    Map<String, String> userNameMap = (Map<String, String>)application.getAttribute("userNameMap");
			Map<String, HttpSession> userSessoinMap = (Map<String, HttpSession>)application.getAttribute("userSessionMap");
			  // 获取登录方式
            String loginType = "single";
            SysConfiguration config =CacheAppManager.getSysConfigurationByCode("loginType");
            if(config!=null){
            	loginType = config.getParamValue();
            }
            String userKey = ExStringUtils.trimToEmpty(userName);
            if("multi".equals(loginType)){//多点登录
            	userKey += "_"+se.getSession().getId();
            } 
			if(ExStringUtils.isNotEmpty(userKey) && userSessoinMap!=null && userSessoinMap.containsKey(userKey)){
				int totalLonginNum = application.getAttribute("totalLonginNum")==null?0:(Integer)application.getAttribute("totalLonginNum");
				totalLonginNum = (totalLonginNum-1)>0?(totalLonginNum-1):0;
				application.setAttribute("totalLonginNum", totalLonginNum);
				userSessoinMap.remove(userKey);
				application.setAttribute("userSessionMap", userSessoinMap);
				userLonginLog.info("login:"+ExDateUtils.getCurrentDateTimeStr()+" 目前在线总人数： "+ totalLonginNum);
				// 设置在线时长
				Date today = new Date();
				int loginLong = calculateLoginLong(user.getStartTime(), user.getEndTime(), user.getStartTimeTemp(), today);
				if("student".equals(user.getUserType())){
					IStudentInfoService studentinfoservice = (IStudentInfoService)applicationContext.getBean("studentinfoservice");
					StudentInfo studentInfo = studentinfoservice.getByUserId(user.getResourceid());
					studentinfoservice.updateLoginLongInfo(" update edu_roll_studentinfo set loginlong=? where resourceid=? ", (studentInfo.getLoginLong()==null?0:studentInfo.getLoginLong())+loginLong,studentInfo.getResourceid());
				}else{
					userService.updateLoginLongInfo(" update hnjk_sys_users set starttime=?,endtime=?,loginlong=? where resourceid=? ", user.getStartTimeTemp(),today,(user.getLoginLong()==null?0:user.getLoginLong())+loginLong,user.getResourceid());
				}
				/*user.setLoginLong((user.getLoginLong()==null?0:user.getLoginLong())+loginLong);
				user.setStartTime(user.getStartTimeTemp());
				user.setEndTime(today);
				userService.merge(user);
				userService.flush();*/
				UserOperationLogsHelper.saveUserOperationLogs("", "8", UserOperationLogs.LOGOUT, "账号退出，当前登录模式(是否允许多账号登录)："+loginType);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 计算在线时长
	 * @param startTime
	 * @param endTime
	 * @param startTimeTemp
	 * @param endTimeTemp
	 * @return
	 */
	private Integer calculateLoginLong(Date startTime, Date endTime, Date startTimeTemp, Date endTimeTemp) {
		int loginLong = 0;
		if(startTime==null || endTime==null || startTimeTemp.after(endTime)){
			long loginTime = endTimeTemp.getTime()-startTimeTemp.getTime();
			loginLong = Long.valueOf(loginTime).intValue()/1000;
		} else if(startTimeTemp.before(startTime) &&  endTimeTemp.after(endTime)) {
			long loginTime = (startTime.getTime()-startTimeTemp.getTime())+(endTimeTemp.getTime()-endTime.getTime());
			loginLong = Long.valueOf(loginTime).intValue()/1000;
		} else if (startTimeTemp.after(startTime) &&  endTimeTemp.after(endTime)) {
			long loginTime = endTimeTemp.getTime()-endTime.getTime();
			loginLong = Long.valueOf(loginTime).intValue()/1000;
		} else if(startTimeTemp.before(startTime) && endTimeTemp.before(endTime) && endTimeTemp.after(startTime)){
			long loginTime = startTime.getTime()-startTimeTemp.getTime();
			loginLong = Long.valueOf(loginTime).intValue()/1000;
		}
		
		return loginLong;
	}


}
