package com.hnjk.edu.roll.service;

import java.util.Map;

import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.roll.model.GraduateNograduateSetting;

/**
 * 学生延迟毕业申请时间段
 * <code>IGraduateNoSettingService</code><p>
 * @author gchw
 * @since： 2012-04-12 上午10:12:00
 * @see 
 * @version 1.0
 */
public interface IGraduateNoSettingService extends IBaseService<GraduateNograduateSetting>{
	Page findGraduateByCondition(Map<String, Object> condition, Page objPage);

	void batchCascadeDelete(String[] split);

}
