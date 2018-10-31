package com.hnjk.edu.basedata.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.basedata.service.IStudentResumeService;


/**
 * 学生简历
 * <code>StudentResumeController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-5-5 下午07:40:53
 * @see 
 * @version 1.0
*/
@Controller
public class StudentResumeController extends BaseSupportController{

	private static final long serialVersionUID = 6561054865215958114L;
	
	@Autowired
	@Qualifier("studentResumeService")
	private IStudentResumeService studentResumeService;
	

	
	/**
	 * 删除简历
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/delResume.html")
	public void delResume(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String ids = request.getParameter("ids");//checkBox多个删除
		if(StringUtils.isNotEmpty(ids)){
			try {
				studentResumeService.batchDelete(ids.split(","));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
