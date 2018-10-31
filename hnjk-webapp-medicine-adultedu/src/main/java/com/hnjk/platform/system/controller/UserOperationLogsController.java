package com.hnjk.platform.system.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IUserOperationLogsService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
/**
 * 系统访问日志管理
 * <code>AccessLogsController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-6-20 上午11:54:37
 * @see 
 * @version 1.0
 */
@Controller
public class UserOperationLogsController extends BaseSupportController {

	private static final long serialVersionUID = 7369678680693289359L;
	
	@Autowired
	@Qualifier("userOperationLogsService")
	private IUserOperationLogsService userOperationLogsService;
	
	
	/**
	 * 日志列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/userOperationLogs/list.html")
	public String listOperationLogs(HttpServletRequest request,Page objPage,ModelMap model) throws WebException{
		objPage.setOrderBy("recordTime");
		objPage.setOrder(Page.DESC);
		User user = SpringSecurityHelper.getCurrentUser();
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);//查询条件
		StringBuilder builder = new StringBuilder(" from "+UserOperationLogs.class.getSimpleName()+" where isDeleted=0");
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			String branchSchool = user.getOrgUnit().getResourceid();
			condition.put("brshSchool", "branchSchool");
			model.addAttribute("isBrschool", true);
			if(!SpringSecurityHelper.isUserInRoles("ROLE_BRS_RECRUIT_LEADER","ROLE_BRS_STUDENTSTATUS")){
				condition.put("userid", user.getResourceid());
			}
		}else {
			model.addAttribute("isBrschool", false);
		}
		
		if(condition.containsKey("userid")){
			builder.append(" and userId =:userid");
		}else if (condition.containsKey("brshSchool")) {
			builder.append(" and userId in (select u.resourceid from User u where u.unitId =:brshSchool)");
		}
		if(condition.containsKey("userName")){
			builder.append(" and userName like '"+condition.get("userName").toString()+"%'");
		}
		if(condition.containsKey("modules")){
			builder.append(" and modules =:modules");
		}
		if(condition.containsKey("operationType")){
			builder.append(" and operationType =:operationType");
		}
		if(condition.containsKey("operationContent")){
			builder.append(" and operationContent like '%"+condition.get("operationContent").toString()+"%'");
		}
		if (condition.containsKey("recordStartTime")){
			builder.append(" and recordTime >= to_date('"+condition.get("recordStartTime").toString()+"','yyyy-MM-dd')");
		}
		if (condition.containsKey("recordEndTime")){
			builder.append(" and recordTime <= to_date('"+condition.get("recordEndTime").toString()+"','yyyy-MM-dd')");
		}
		builder.append(" order by recordTime desc");
		Page operationLogsPage = userOperationLogsService.findByHql(objPage, builder.toString(), condition);
		
		model.addAttribute("operationLogsPage", operationLogsPage);
		model.addAttribute("condition", condition);
		return "/system/accesslogs/operationLogs-list";
	}
	
	@RequestMapping("/edu3/system/userOperationLogs/showContent.html")
	public String showContent(String resourceid,ModelMap model){
		UserOperationLogs logs = userOperationLogsService.get(resourceid);
		model.addAttribute("logs", logs);
		return "/system/accesslogs/operationLogs-form";
	}
}
