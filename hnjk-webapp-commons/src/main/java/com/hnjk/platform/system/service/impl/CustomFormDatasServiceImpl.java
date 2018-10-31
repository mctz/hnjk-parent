package com.hnjk.platform.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.platform.system.model.CustomFormDatas;
import com.hnjk.platform.system.model.CustomFormDefine;
import com.hnjk.platform.system.model.CustomFormFields;
import com.hnjk.platform.system.service.ICustomFormDatasService;
import com.hnjk.platform.system.service.ICustomFormService;
/**
 * 自定义表单数据服务接口实现.
 * <code>CustomFormDatasServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-10-24 下午05:11:49
 * @see 
 * @version 1.0
 */
@Transactional
@Service("customFormDatasService")
public class CustomFormDatasServiceImpl extends BaseServiceImpl<CustomFormDatas> implements ICustomFormDatasService {

	@Autowired
	@Qualifier("customFormService")
	private ICustomFormService customFormService;
	
	@Override
	public CustomFormDatas saveOrUpdateCustomFormDatas(HttpServletRequest request) throws ServiceException {
		String resourceid = request.getParameter("customDataId");//数据id
		String customFormId = request.getParameter("customFormId");//表单id
		CustomFormDatas customFormData = new CustomFormDatas();
		CustomFormDefine customForm = null;//自定义表单
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if(ExStringUtils.isNotBlank(resourceid)){ //已存在数据，编辑
			customFormData = get(resourceid);//获取表单数据
			customForm = customFormData.getCustomFormDefine();
		}else{ //新增数据
			customForm = customFormService.get(customFormId);//获取表单
		}	
		
		for (CustomFormFields field : customForm.getFields()) {
			if(field.getParent()==null){//一级属性
				if("list".equals(field.getFormDomType())){//二级属性列表
					Map<String, String[]> childFieldMap = new HashMap<String, String[]>();
					int len = 0;
					for (CustomFormFields child : field.getChilds()) {
						String[] childFileds = request.getParameterValues(child.getFieldCode());
						if(ArrayUtils.isEmpty(childFileds)){
							break;
						}
						len = childFileds.length;
						childFieldMap.put(child.getFieldCode(), childFileds);
					}
					if(len > 0){//存在二级表单
						List<Map<String, Object>> childList = new ArrayList<Map<String,Object>>();
						for (int i = 0; i < len; i++) {
							Map<String, Object> childMap = new HashMap<String, Object>();
							for (String key : childFieldMap.keySet()) {
								childMap.put(key, childFieldMap.get(key)[i]);
							}
							childList.add(childMap);
						}
						jsonMap.put(field.getFieldCode(), childList);
					}
				} else {
					jsonMap.put(field.getFieldCode(), request.getParameter(field.getFieldCode()));
				}
			}					
		}
		
		customFormData.setCustomFormDefine(customForm);
		customFormData.setFormData(JsonUtils.mapToJson(jsonMap));
		
		saveOrUpdate(customFormData);		
		return customFormData;
	}
}
