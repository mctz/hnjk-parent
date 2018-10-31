package com.hnjk.platform.system.controller;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.BigDecimalUtil;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonModel;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.service.IAccessLogsJDBCService;
import com.hnjk.platform.system.service.IAccessLogsService;
import com.hnjk.platform.taglib.JstlCustomFunction;
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
public class AccessLogsController extends BaseSupportController {

	private static final long serialVersionUID = 7369678680693289359L;
	
	@Autowired
	@Qualifier("accessLogsService")
	private IAccessLogsService accessLogsService;
	
	@Autowired
	@Qualifier("accessLogsJDBCService")
	private IAccessLogsJDBCService accessLogsJDBCService;
	
	/**
	 * 日志列表
	 * @param accessStartTime
	 * @param accessEndTime
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/accesslogs/list.html")
	public String listAccessLogs(String accessStartTime, String accessEndTime, HttpServletRequest request,Page objPage,ModelMap model) throws WebException{
		objPage.setOrderBy("accessTime");
		objPage.setOrder(Page.DESC);
		Map<String,Object> condition = new HashMap<String,Object>(5);//查询条件
		
		if(ExStringUtils.isNotBlank(accessStartTime)){
			condition.put("accessStartTime", accessStartTime);
		}
		if(ExStringUtils.isNotBlank(accessEndTime)){
			condition.put("accessEndTime", accessEndTime);
		}
		
		Page accessLogsPage = accessLogsService.findAccessLogsByCondition(condition, objPage);
		
		model.addAttribute("accessLogsPage", accessLogsPage);
		model.addAttribute("condition", condition);
		return "/system/accesslogs/accesslogs-list";
	}
	/**
	 * 访问情况统计
	 * @param type
	 * @param accessDate
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/accesslogs/status/list.html")
	public String statAccessLogsStatus(String type, String accessDate, HttpServletRequest request,ModelMap model) throws WebException{
		type = ExStringUtils.defaultIfEmpty(type, "1");
		accessDate = ExStringUtils.trimToEmpty(accessDate);
		if(ExStringUtils.isNotBlank(type)){				
			Map<String, Object> condition = new HashMap<String, Object>(5);
			if(ExStringUtils.isBlank(accessDate)){
				try {	
					if("1".equals(type)){//按日
						accessDate = ExDateUtils.formatDateStr(ExDateUtils.addDays(new Date(), -1), ExDateUtils.PATTREN_DATE);						
					} else if("2".equals(type)){//按月
						accessDate = ExDateUtils.formatDateStr(new Date(), "yyyy-MM");
					} else {
						int month = -2;
						if("6".equals(type)){
							month = -5;
						}
						accessDate = ExDateUtils.formatDateStr(ExDateUtils.addMonths(new Date(), month), "yyyy-MM");						
						String d1 = accessDate+"-01";
						String d2 = ExDateUtils.formatDateStr(ExDateUtils.getMonthEnd(new Date()), "yyyy-MM-dd");
						long days = ExDateUtils.getDateDiffNum(d1, d2)+1;
						condition.put("days", days);
					} 
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			condition.put("type", type);
			condition.put("accessDate", accessDate);
	
			List<Map<String, Object>> list = accessLogsJDBCService.statAccessLogsStatus(condition);				
				
			StringBuffer data1 = new StringBuffer("{name: '用户人数',data: [");
			StringBuffer data2 = new StringBuffer("{name: '资源总数',data: [");
			StringBuffer data3 = new StringBuffer("{name: '流量总数',data: [");
			if(list != null){	
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					if(i>0){
						data1.append(",");
						data2.append(",");
						data3.append(",");
					}
					data1.append(map.get("ipcount"));
					data2.append(map.get("urlcount"));
					data3.append(map.get("netflowcount"));																		
				}
			}
			data1.append("]},");
			data2.append("]},");
			data3.append("]}");			
			
			String title = accessDate;
			String[] date = accessDate.split("\\-");
			if("1".equals(type)){
				model.addAttribute("pointInterval",3600000);//一个小时	
				model.addAttribute("pointStart","Date.UTC("+date[0]+","+(Integer.parseInt(date[1])-1)+","+date[2]+",0,0,0)");
			} else {
				if("3".equals(type)){
					title = "近三个月";
				} else if("6".equals(type)){
					title = "近六个月";
				}
				model.addAttribute("pointInterval",3600000*24);//一天
				model.addAttribute("pointStart","Date.UTC("+date[0]+","+(Integer.parseInt(date[1])-1)+",1,0,0,0)");
			} 
			
			model.addAttribute("title", title);			
			model.addAttribute("condition", condition);
			model.addAttribute("chart1", data1.toString());
			model.addAttribute("chart2", data2.toString());
			model.addAttribute("chart3", data3.toString());
		}
		return "/system/accesslogs/accesslogs-status-list";
	}
	/**
	 * 访问结果分析
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/accesslogs/result/list.html")
	public String statAccessLogsResults(HttpServletRequest request,ModelMap model) throws WebException{
		Double total = accessLogsJDBCService.statAccessLogsResultsTotal();
		fetchChartList("serverstatus",total,model);
		fetchChartList("clientbrowser",total,model);
		fetchChartList("clientos",total,model);
		return "/system/accesslogs/accesslogs-result-list";
	}
	
	private void fetchChartList(String type, Double total,ModelMap model){
		StringBuffer sb = new StringBuffer();
		
		String dictCode = null;	
		String title = "访问结果";	
		if("clientbrowser".equals(type)){
			dictCode = "CodeClientBrowser";
			title = "客户端浏览器";
		} else if("clientos".equals(type)){
			dictCode = "CodeClientOs";
			title = "客户端操作系统";				
		}
		
		List<Map<String, Object>> list = accessLogsJDBCService.statAccessLogsResults(type);
		if(null != list && !list.isEmpty()){					
			for (Map<String, Object> map : list) {
				String dictName = map.get("target").toString();
				if(ExStringUtils.isNotEmpty(dictCode)){
					dictName = JstlCustomFunction.dictionaryCode2Value(dictCode, map.get("target").toString()); 
				}
				String count = map.get("resultcount").toString();
				sb.append("['"+dictName+" ("+count+")',"+BigDecimalUtil.mul(BigDecimalUtil.div(Double.parseDouble(count), total, 4),100)+"],");
				map.put("percent", BigDecimalUtil.div(Double.parseDouble(count), total, 4));
			}			
		}
		model.addAttribute(type+"list", list);
		model.addAttribute(type+"chart", "{type: 'pie',name: '"+title+"',data: ["+sb.toString()+"]}");	
	}
	/**
	 * 访问排行统计
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/accesslogs/top/list.html")
	public String statAccessLogsTop(HttpServletRequest request,ModelMap model) throws WebException{
		List<Map<String, Object>> list1 = accessLogsJDBCService.statAccessLogsTop(1);//并发访问用户数
		List<Map<String, Object>> list2 = accessLogsJDBCService.statAccessLogsTop(2);//访问次数最多的用户
		List<Map<String, Object>> list3 = accessLogsJDBCService.statAccessLogsTop(3);//访问最多的资源
		List<Map<String, Object>> list4 = accessLogsJDBCService.statAccessLogsTop(4);// 处理最慢的资源
		List<Map<String, Object>> list5 = accessLogsJDBCService.statAccessLogsTop(5); //错误做多的资源
		
		model.addAttribute("list1", list1);
		model.addAttribute("list2", list2);
		model.addAttribute("list3", list3);
		model.addAttribute("list4", list4);
		model.addAttribute("list5", list5);
		return "/system/accesslogs/accesslogs-top-list";
	}
	/**
	 * 为导入的日志列表
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/accesslogs/file/list.html")
	public String listAccessLogsFile(HttpServletRequest request,ModelMap model) throws WebException{		
		File dir = new File(CacheAppManager.getSysConfigurationByCode("sys.accesslogs.dir").getParamValue());
		File[] files = dir.listFiles();
		List<JsonModel> accessLogsFiles = new ArrayList<JsonModel>();
		List<String> dates = accessLogsService.accessLogsDate();
		if(null != files){
			for (File f : files) {
				String name = f.getName();
				String date = ExStringUtils.substringBetween(name,"edu3_access_log.", ".txt");
				if(dates==null || !dates.contains(date)){
					JsonModel json = new JsonModel(f.getName(), f.getAbsolutePath(), f.length()+"");
					accessLogsFiles.add(json);
				}			
			}
		}
		model.addAttribute("accessLogsFiles", accessLogsFiles);
		return "/system/accesslogs/accesslogs-file-list";
	}
	/**
	 * 导入访问日志
	 * @param accesslogsPath
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/accesslogs/import.html")
	public void saveRecruitExamPlan(String accesslogsPath,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>(5);
		try {					
			if(ExStringUtils.isNotBlank(accesslogsPath)){
				accessLogsService.parseAndSaveAccessLogs(accesslogsPath);
				map.put("statusCode", 200);
				map.put("message", "导入成功！");
				map.put("forward", request.getContextPath()+"/edu3/system/accesslogs/list.html");
			}			
		}catch (Exception e) {
			logger.error("导入日志出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "导入日志失败！");
		}		
		renderJson(response, JsonUtils.mapToJson(map));
	}

}
