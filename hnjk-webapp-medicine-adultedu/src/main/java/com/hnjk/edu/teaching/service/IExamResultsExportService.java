package com.hnjk.edu.teaching.service;

import java.util.zip.ZipOutputStream;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.service.impl.ExamResultExportServiceImpl.GenFile;
import com.hnjk.security.model.User;

/**
 * 处理成绩导入导出Interface
 * @author luof
 *
 */
public interface IExamResultsExportService extends IBaseService<ExamResults>{
	
	public GenFile generateZipStreamFromAchievement(ExamSub examSub,String user,
				 String[] checkstatuscodes, ZipOutputStream zos)throws Exception;
	
	public String  writeOperateLog(ExamSub examSub,User user,String fileDIR,String filename,String operateType) throws ServiceException;
	
}
