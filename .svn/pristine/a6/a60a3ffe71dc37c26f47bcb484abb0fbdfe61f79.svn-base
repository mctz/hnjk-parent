package com.hnjk.edu.textbook.sevice.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.TextBook;
import com.hnjk.edu.textbook.service.ITextBookService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

@Transactional
@Service("iTextBookService")
public class TextBookServiceImpl extends BaseServiceImpl<TextBook> implements ITextBookService {
	
	@Override
	public Page getPageBycondition(Map<String,Object> condition,Page objPage){
		objPage.setOrder(Page.ASC);
		objPage.setOrderBy("course.courseCode");
		objPage.setPageSize(100);
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = "from "+TextBook.class.getSimpleName()+" where isDeleted=:isDeleted ";
		values.put("isDeleted", 0);
		if(condition.containsKey("isUsed")){
			hql+=" and isUsed =:isUsed ";
			values.put("isUsed", condition.get("isUsed"));
		}
		if(condition.containsKey("courseid")){
			hql+=" and course.resourceid =:courseid ";
			values.put("courseid", condition.get("courseid"));
		}
		
		return findByHql(objPage, hql, values);
		
	}
	@Override
	public boolean saveTextBook(TextBook textbook){
		TextBook tmp;
		if(ExStringUtils.isNotBlank(textbook.getResourceid())){
			tmp = this.get(textbook.getResourceid());
		}else{
			tmp = new TextBook();
		}
		tmp.setBookName(textbook.getBookName());
		tmp.setBookSerial(textbook.getBookSerial());
		tmp.setEditor(textbook.getEditor());
		tmp.setCourse(textbook.getCourse());
		tmp.setPress(textbook.getPress());
		tmp.setPrice(textbook.getPrice());
		tmp.setUpdatedate(new Date());
		tmp.setIsUsed("N");
		tmp.setUser(SpringSecurityHelper.getCurrentUser());
		boolean result=false;
		try {
			this.saveOrUpdate(tmp);
			result= true;
		} catch (Exception e) {
			logger.error("执行方法 saveQuestion() :保存 QuestionBank 实体时失败");
			
		}
		return result;
	}
	
	@Override
	public boolean deleteTextBook(String resourceids){
		boolean result=true;
		
		List<TextBook> list = new ArrayList<TextBook>();
		User user = SpringSecurityHelper.getCurrentUser();
		for(String resourceid : resourceids.split("\\,")){
			TextBook qb = this.get(resourceid);
			if(qb!=null){
				qb.setIsDeleted(1);
				qb.setUpdatedate(new Date());
				qb.setUser(user);
				list.add(qb);
			}
		}
		if(list.size()>0){
			this.batchSaveOrUpdate(list);			
		}else{
			result = false;
		}
		return result;
	}
	@Override
	public boolean settingTextBook(String resourceids,String flag){
		boolean result = true;
		List<TextBook> list = new ArrayList<TextBook>();
		User user = SpringSecurityHelper.getCurrentUser();
		for(String resourceid : resourceids.split("\\,")){
			TextBook qb = this.get(resourceid);
			if(qb!=null){
				qb.setIsUsed(flag);
				qb.setUpdatedate(new Date());
				qb.setUser(user);
				list.add(qb);
			}
		}
		if(list.size()>0){
			this.batchSaveOrUpdate(list);			
		}else{
			result = false;
		}
		
		return result;
	}
}
