package com.hnjk.edu.learning.service.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.learning.model.TeacherCourseFiles;
import com.hnjk.edu.learning.service.ITeacherCourseFilesService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.service.IAttachsService;

/**
 * 教师课程文件服务实现.
 * @author hzg
 *
 */
@Service("teacherCourseFilesService")
@Transactional
public class TeacherCourseFilesServiceImpl extends BaseServiceImpl<TeacherCourseFiles> implements ITeacherCourseFilesService{
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Override
	@Transactional(readOnly=true)
	public List<TeacherCourseFiles> findTeacherFilesTree(String courseId)	throws ServiceException {
		Session session  = exGeneralHibernateDao.getSessionFactory().getCurrentSession();
		
		StringBuilder sql = new StringBuilder();
		sql.append("select t.* from edu_lear_files t where t.isdeleted=:isDeleted and t.courseId=:courseId and t.fileType = :fileType start with t.parentid is null ");
		sql.append(" connect by prior t.resourceid=t.parentid ");
		sql.append(" order siblings by t.nodeLevel,t.showOrder ");
				
		try {
			SQLQuery query = session.createSQLQuery(sql.toString()).addEntity(TeacherCourseFiles.class);
			query.setParameter("isDeleted", 0);
			query.setParameter("courseId", courseId);
			query.setParameter("fileType", 0);
			return query.list();
		} catch (Exception e) {
			throw new ServiceException("查找课程文件树出错:"+e.fillInStackTrace());
		}
	}

	@Override
	public List<TeacherCourseFiles> findTeacherFilesTreeByCondition(Map<String, Object> condition) throws ServiceException {
		Session session  = exGeneralHibernateDao.getSessionFactory().getCurrentSession();
		
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select t.* from edu_lear_files t where t.isdeleted=:isDeleted and t.courseId=:courseId ");
		params.put("isDeleted", 0);
		params.put("courseId", condition.get("courseId"));
		if(condition.containsKey("fileType")){
			sql.append(" and t.fileType = :fileType  ");
			params.put("fileType", condition.get("fileType"));
		}
		if(condition.containsKey("parentId")){
			sql.append(" start with t.parentid = :parentId  ");
			params.put("parentId", condition.get("parentId"));
		} else {
			sql.append(" start with t.parentid is null  ");
		}		
		sql.append(" connect by prior t.resourceid=t.parentid ");
		sql.append(" order siblings by t.nodeLevel,t.showOrder ");
				
		try {
			SQLQuery query = session.createSQLQuery(sql.toString()).addEntity(TeacherCourseFiles.class);
			query.setProperties(params);
			return query.list();
		} catch (Exception e) {
			throw new ServiceException("查找课程文件树出错:"+e.fillInStackTrace());
		}
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page findTeacherFilesByCondition(Map<String, Object> condition,Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuilder hql = getTeacherFilesHqlByCondition(condition, values);
		if(objPage.isOrderBySetted()){
			hql.append(" order by "+objPage.getOrderBy() +" "+ objPage.getOrder());
		}		
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<TeacherCourseFiles> findTeacherFilesByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuilder hql = getTeacherFilesHqlByCondition(condition, values);
		if(condition.containsKey("orderby")){
			hql.append(" order by "+condition.get("orderby"));
		}
		return findByHql(hql.toString(), values);
	}

	/**
	 * 获取hql语句
	 * @param condition
	 * @param hql
	 * @param values
	 */
	private StringBuilder getTeacherFilesHqlByCondition(Map<String, Object> condition, Map<String, Object> values) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from "+TeacherCourseFiles.class.getSimpleName()+" where isDeleted = :isDeleted  ");
		values.put("isDeleted", 0);
		
		if(condition.containsKey("courseId")){//课程id
			hql.append(" and course.resourceid = :courseId ");
			values.put("courseId",String.valueOf(condition.get("courseId")));
		}
		if(condition.containsKey("parentId")){//父节点
			hql.append(" and parent.resourceid = :parentId ");
			values.put("parentId", String.valueOf(condition.get("parentId")));
		}
		if(condition.containsKey("fileName")){//文件名
			hql.append(" and fileName like :fileName ");
			values.put("fileName", "%"+String.valueOf(condition.get("fileName"))+"%");
		}
		if(condition.containsKey("fileType")){//文件类型
			hql.append(" and fileType = :fileType ");
			values.put("fileType", new Integer(String.valueOf(condition.get("fileType"))));
		}
		if(condition.containsKey("isPublished")){//是否发布
			hql.append(" and isPublished = :isPublished ");
			values.put("isPublished",String.valueOf(condition.get("isPublished")));
		}
		if(condition.containsKey("parentFileName")){//上级文件夹
			hql.append(" and parent.fileName =:parentFileName ");
			values.put("parentFileName", String.valueOf(condition.get("parentFileName")));
		}
		if(condition.containsKey("emptyAttach")){//外部链接
			hql.append(" and attach is null ");
		}
		if(condition.containsKey("notInPatentFileNames")){//排除目录
			hql.append(" and parent.fileName not in ("+String.valueOf(condition.get("notInPatentFileNames"))+") ");
		}
		return hql;
	}

	@Override
	public void saveOrUpdateTeacherFiles(TeacherCourseFiles teacherFiles, String[] uploadfileid) throws ServiceException {
		TeacherCourseFiles parent = teacherFiles.getParent();//当前上级文件夹
		if(ExStringUtils.isNotBlank(teacherFiles.getResourceid())){//编辑
			TeacherCourseFiles pTeacherFiles = get(teacherFiles.getResourceid());
			if(teacherFiles.getFileType()==0){//文件夹
				pTeacherFiles.setPermission(teacherFiles.getPermission());
			} else {
				if(pTeacherFiles.getAttach() != null){
					pTeacherFiles.getAttach().setAttName(teacherFiles.getFileName()+"."+pTeacherFiles.getAttach().getAttType());
				} else {//外部附件
					pTeacherFiles.setFileUrl(teacherFiles.getFileUrl());
				}
			}		
			pTeacherFiles.setFileName(teacherFiles.getFileName());//重命名			
			pTeacherFiles.setIsPublished(teacherFiles.getIsPublished());
			pTeacherFiles.setDescription(teacherFiles.getDescription());
			pTeacherFiles.setShowOrder(teacherFiles.getShowOrder());
			update(pTeacherFiles); 
		} else { //新增			
			parent.setIsChild(Constants.BOOLEAN_YES);	
			teacherFiles.setShowOrder(parent.getNextShowOrder());
			if(teacherFiles.getFileType()==0){//文件夹
				parent.getChilds().add(teacherFiles);
				save(teacherFiles);//保存新增文件夹
			} else if(teacherFiles.getFileType()==1){//文件
				if(Constants.BOOLEAN_YES.equals(teacherFiles.getIsOutlink())){//外部附件
					parent.getChilds().add(teacherFiles);
					save(teacherFiles);
				} else if(uploadfileid != null && uploadfileid.length >0){
					for (int i = 0; i < uploadfileid.length; i++) {
						Attachs attach = attachsService.get(uploadfileid[i]);
						TeacherCourseFiles child = new TeacherCourseFiles();
						ExBeanUtils.copyProperties(child, teacherFiles);
						child.setFileName(ExStringUtils.substringBeforeLast(attach.getAttName(), "."));//文件名
						if(Attachs.FILE_SAVETYPE_FTP.equals(attach.getSaveType())){//ftp
							child.setFileUrl(attach.getSerPath()+"/"+attach.getSerName());//服务器路径
						} else {
							String url = attach.getSerPath()+File.separator+attach.getSerName();
							url = ExStringUtils.replace(url, ExStringUtils.substringBefore(url, "users"), CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue()).replace(File.separator, "/");
							child.setFileUrl(url);//服务器路径
						}	
						child.setAttach(attach);
						child.setShowOrder(parent.getNextShowOrder());//排序号
						
						parent.getChilds().add(child);
					}
					update(parent);
				}				
				
			}
		}
	}
	
	@Override
	public void moveTeacherFiles(String[] ids, String moveToId) throws ServiceException {
		TeacherCourseFiles toFiles = get(moveToId);		
		for (String id : ids) {
			TeacherCourseFiles file = get(id);
			if(!file.getParent().getResourceid().equals(toFiles.getResourceid())){//移动到别的目录
				if(file.getFileType()==0){//移动文件夹		
					if(toFiles.getResourceid().equals(file.getResourceid())) {
						throw new ServiceException("无法移动 "+file.getFileName()+": 目标文件夹和源文件夹相同.");
					}
					Map<String, Object> condition = new HashMap<String, Object>();
					condition.put("courseId", toFiles.getCourse().getResourceid());
					condition.put("parentId", file.getResourceid());
					List<TeacherCourseFiles> fileList = findTeacherFilesTreeByCondition(condition);
					if(ExCollectionUtils.isNotEmpty(fileList)){//查出文件夹下所有文件，调整fileLevel
						Integer level = toFiles.getFileLevel()+1 - file.getFileLevel();//增长的等级
						for (TeacherCourseFiles subFile : fileList) {
							if(toFiles.getResourceid().equals(subFile.getResourceid())){//移动目录到自身的子目录
								throw new ServiceException("无法移动 "+file.getFileName()+": 目标文件夹是源文件夹的子文件夹.");
							}
							subFile.setFileLevel(subFile.getFileLevel()+level);
						}
					}
				} 				
				file.setParent(toFiles);
				file.setFileLevel(toFiles.getFileLevel()+1);
				file.setShowOrder(toFiles.getNextShowOrder());
				toFiles.setIsChild(Constants.BOOLEAN_YES);
				toFiles.getChilds().add(file);
			}
		}
	}
	
	@Override
	public void publishTeacherFiles(String[] ids, String isPublished) throws ServiceException {
		Date currentDate = new Date();
		for (String id : ids) {
			TeacherCourseFiles file = get(id);
			file.setIsPublished(isPublished);
			file.setFillinDate(currentDate);
		}
	}
}
