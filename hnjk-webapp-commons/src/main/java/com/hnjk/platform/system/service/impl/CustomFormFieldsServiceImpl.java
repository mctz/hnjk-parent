package com.hnjk.platform.system.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.platform.system.model.CustomFormFields;
import com.hnjk.platform.system.service.ICustomFormFieldsService;

@Service("customFormFieldsService")
@Transactional
public class CustomFormFieldsServiceImpl extends BaseServiceImpl<CustomFormFields> implements
		ICustomFormFieldsService {

	
}
