package com.hnjk.edu.teaching.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.teaching.model.OrderCourseSetting;
import com.hnjk.edu.teaching.service.IOrderCourseSettingService;
import com.hnjk.edu.teaching.vo.OrderCourseSettingFormVo;
import com.hnjk.extend.plugin.excel.util.DateUtils;
/**
 * 
 * <code>预约管理接口实现OrderCourseSettingServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-11 下午03:43:19
 * @see 
 * @version 1.0
 */

@Service("orderCourseSettingService")
@Transactional
public class OrderCourseSettingServiceImpl extends BaseServiceImpl<OrderCourseSetting> 
										   implements IOrderCourseSettingService{
		
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Override
	@Transactional
	public boolean changeGradeOrderCourseStatus(String gradid, String isOpened) throws ServiceException {
		try {
			String [] ids = gradid.split(",");
			for (int i = 0; i < ids.length; i++) {
				OrderCourseSetting setting = this.findUniqueByProperty("resourceid", ids[i]);
				if (null!=setting) {
					setting.setIsOpened(isOpened);
					this.saveOrUpdate(setting);
				}else {
					break;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Page findOrderCourseSettingByCondition(Map<String, Object> condition, Page page) throws ServiceException {
		StringBuffer hql = new StringBuffer("from OrderCourseSetting setting ");
		if(condition.containsKey("isOpened")){
			hql.append(" where setting.isOpened=:isOpened");
		}else {
			hql.append(" where  setting.isDeleted=0");
		}
		if (condition.containsKey("yearInfo")) {
			hql.append(" and setting.yearInfo.resourceid=:yearInfo");
		}
		if (condition.containsKey("term")) {
			hql.append(" and setting.term=:term");
		}
		if(condition.containsKey("grade")){
			hql.append(" and setting.grade.resourceid=:grade");
		}
		if(condition.containsKey("startDate")){
			hql.append(" and setting.startDate >= to_date('"+condition.get("startDate")+"','yyyy-MM-dd')");	
		}
		if(condition.containsKey("endDate")){
			hql.append(" and setting.endDate <= to_date('"+condition.get("endDate")+"','yyyy-MM-dd')");
		}
		
		hql.append(" order by setting.yearInfo.firstYear desc");
		return  exGeneralHibernateDao.findByHql(page, hql.toString(), condition);
	}
	@Override
	@Transactional
	public Map saveOrUpdateOrderCourseSetting(OrderCourseSettingFormVo vo) throws ServiceException {
		Map resultMap = new HashMap();
		try {
			if (ExStringUtils.isNotBlank(vo.getResourceid())) {
				
				OrderCourseSetting setting = this.get(vo.getResourceid());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date startDate = sdf.parse(vo.getStartDate());
				Date endDate =   sdf.parse(vo.getEndDate());
				YearInfo yearInfo = yearInfoService.get(vo.getYearInfo());
				setting.setYearInfo(yearInfo);
				//Grade grade = gradeService.get(vo.getGrade());
				//setting.setGrade(grade);
				setting.setResourceid(vo.getResourceid());
				setting.setStartDate(startDate);
				setting.setEndDate(endDate);
				setting.setIsOpened(vo.getIsOpened());
				setting.setLimitOrderNum(Integer.parseInt(vo.getLimitOrderNum()));
				this.saveOrUpdate(setting);
				
				resultMap.put("result", true);
				resultMap.put("setting", setting);
				
				return resultMap;
				
			} else {
				
				StringBuffer hql = new StringBuffer();
				hql.append("from OrderCourseSetting setting where  setting.isDeleted=? and setting.isOpened=?");
				List list = this.findByHql(hql.toString(), 0,Constants.BOOLEAN_YES);
				if (null!= list && !list.isEmpty()&&Constants.BOOLEAN_YES.equals(vo.getIsOpened())) {
					resultMap.put("result", false);
					resultMap.put("msg", "同一时间只允许开放一个年度的预约权限！");
				}else {
					OrderCourseSetting setting = new OrderCourseSetting();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date startDate = sdf.parse(vo.getStartDate());
					Date endDate = sdf.parse(vo.getEndDate());
					//Grade grade = gradeService.get(vo.getGrade());
					//setting.setGrade(grade);
					YearInfo yearInfo = yearInfoService.get(vo.getYearInfo());
					setting.setYearInfo(yearInfo);
					setting.setTerm(vo.getTerm());
					setting.setResourceid(vo.getResourceid());
					setting.setStartDate(startDate);
					setting.setEndDate(endDate);
					setting.setIsOpened(vo.getIsOpened());
					setting.setLimitOrderNum(Integer.parseInt(vo.getLimitOrderNum()));
					this.saveOrUpdate(setting);
					
					resultMap.put("result", true);
					resultMap.put("setting", setting);
				}
				return resultMap;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("msg", "操作出错！");
		}
		
		return resultMap;
	}

	@Override
	public List<OrderCourseSetting> findOrderCourseSettingByYearInfo() throws ServiceException {
		Date date =  ExDateUtils.addDays(new Date(), 60);//预约课程一般是在前一个学期结束预约下一个学期的课程
		String orderDate=DateUtils.getFormatDate(date, "yyyy-MM-dd");
		StringBuffer hql = new StringBuffer();
		hql.append(" from OrderCourseSetting setting where setting.isDeleted=0");
		hql.append(" and to_char(to_date('"+orderDate+"','yyyy-MM-dd'),'yyyy-MM-dd') >= (to_char(setting.yearInfo.firstMondayOffirstTerm,'yyyy-MM-dd'))");
		hql.append(" and to_char(to_date('"+orderDate+"','yyyy-MM-dd'),'yyyy-MM-dd') <= (to_char(setting.yearInfo.firstMondayOfSecondTerm+setting.yearInfo.secondTermWeekNum*7,'yyyy-MM-dd'))");
		hql.append(" and setting.isOpened='Y'");
		
		return (List<OrderCourseSetting>) this.exGeneralHibernateDao.findByHql(hql.toString());
	}

	@Override
	public List<OrderCourseSetting> findOpenedSetting() throws ServiceException {
		String hql ="from OrderCourseSetting setting where setting.isDeleted=0 and setting.isOpened='Y'";
		List<OrderCourseSetting> list = (List<OrderCourseSetting>) this.exGeneralHibernateDao.findByHql(hql);
		return list;
	}
}
