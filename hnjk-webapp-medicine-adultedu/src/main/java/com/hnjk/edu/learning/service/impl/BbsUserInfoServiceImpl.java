package com.hnjk.edu.learning.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.learning.model.BbsUserInfo;
import com.hnjk.edu.learning.service.IBbsUserInfoService;
/**
 * 
 * <code>BbsUserInfoServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-9-25 下午05:13:28
 * @see 
 * @version 1.0
 */
@Transactional
@Service("bbsUserInfoService")
public class BbsUserInfoServiceImpl extends BaseServiceImpl<BbsUserInfo> implements IBbsUserInfoService {
	
}
