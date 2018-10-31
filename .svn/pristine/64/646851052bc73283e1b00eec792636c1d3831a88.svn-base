package com.hnjk.edu.textbook.service;

import java.util.Map;

import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.basedata.model.TextBook;

public interface ITextBookService extends IBaseService<TextBook> {
	
	Page getPageBycondition(Map<String, Object> condition, Page objPage);

	boolean saveTextBook(TextBook textbook);

	boolean deleteTextBook(String resourceids);

	boolean settingTextBook(String resourceids, String flag);

}
