package com.hnjk.edu.work.service.impl;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.work.model.UserTimeManage;
import com.hnjk.edu.work.service.IUserTimeManageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Function : 学生工作时间管理 - 接口实现类
 * <p>Author : Administrator
 * <p>Date   : 2018-07-19
 * <p>Description :
 */
@Transactional
@Service("userTimeManageService")
public class UserTimeManageServiceImpl extends BaseServiceImpl<UserTimeManage> implements IUserTimeManageService {


}
