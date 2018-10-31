package com.hnjk.edu.basedata.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.basedata.model.ClassInfo;
/**
 * 
  * @description:班级基础数据接口
  * @author xiangy  
  * @date 2013-12-4 上午10:29:21 
  * @version V1.0
 */
public interface IClassService extends IBaseService<ClassInfo> {
	/**
	 * 
	  * @description:分页查询
	  * @author xiangy  
	  * @date 2013-12-4 上午10:28:56 
	  * @version V1.0
	  * @param conditon
	  * @param objPage
	  * @return
	  * @throws ServiceException
	 */
	Page findClassByCondition(Map<String,Object>conditon ,Page objPage) throws ServiceException;
	/**
	 * 
	  * @description:根据Id获取班级名称
	  * @author xiangy  
	  * @date 2013-12-10 下午02:36:36 
	  * @version V1.0
	  * @param classId
	  * @return
	  * @throws ServiceException
	 */
	String getClassName(String classId) throws ServiceException;

}
