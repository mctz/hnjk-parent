package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.OrderCourseSetting;
import com.hnjk.edu.teaching.vo.OrderCourseSettingFormVo;
/**
 * 
 * <code>年度预约管理接口IOrderCourseSettingService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-11 下午03:37:38
 * @see 
 * @version 1.0
 */
public interface IOrderCourseSettingService extends IBaseService<OrderCourseSetting> {
	/**
	 * 设置年级预约管理状态
	 * @return
	 * @throws ServiceException
	 */
	public boolean changeGradeOrderCourseStatus(String gradid,String isOpened) throws ServiceException;
	/**
	 * 根据条件查询预约管理表
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	public Page findOrderCourseSettingByCondition(Map<String, Object> condition, Page objPage)throws ServiceException;
	
	/**
	 * 根据年级预约权限FormVo更新或保存OrderCourseSetting
	 * @param vo
	 * @return
	 * @throws ServiceException
	 */
	public Map saveOrUpdateOrderCourseSetting(OrderCourseSettingFormVo vo)throws ServiceException;
	
	/**
	 * 根据年份查询OrderCourseSetting对象
	 * @return
	 * @throws ServiceException
	 */
	public List<OrderCourseSetting> findOrderCourseSettingByYearInfo()throws ServiceException;
	
	/**
	 * 查找开放的OrderCourseSetting
	 * @return
	 * @throws ServiceException
	 */
	public List<OrderCourseSetting> findOpenedSetting ()throws ServiceException; 

}
