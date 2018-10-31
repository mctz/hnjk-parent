package com.hnjk.edu.basedata.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.StudentBaseInfo;
import com.hnjk.edu.basedata.service.IStudentService;

/**
 * <code>StudentServiceImpl</code>基础数据-学生基础信息-服务实现.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-15 下午02:29:52
 * @see 
 * @version 1.0
 */
@Service("studentService")
@Transactional
public class StudentServiceImpl extends BaseServiceImpl<StudentBaseInfo> implements IStudentService{

	@Override
	public boolean checkExistsByIdCardNum(String idCard) {
		Map<String,Object> values =  new HashMap<String, Object>();
		values.put("idCard", idCard);
		String hql = "from StudentBaseInfo stu where  stu.certNum=:idCard and stu.isDeleted=0";
		try {
			List list = exGeneralHibernateDao.findByHql(hql,values );
			if (null!= list && !list.isEmpty()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		
	}
	
}
