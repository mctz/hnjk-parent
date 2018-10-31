package com.hnjk.edu.teaching.service.impl;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.learning.model.ActiveCourseExam;
import com.hnjk.edu.learning.model.CourseReference;
import com.hnjk.edu.learning.model.MateResource;
import com.hnjk.edu.teaching.model.Syllabus;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.ISyllabusService;
import com.hnjk.extend.taglib.tree.ZTreeNode;

/**
 * 教学大纲管理接口实现
 * <code>SyllabusServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-7-29 下午03:32:37
 * @see 
 * @version 1.0
 */
@Service("syllabusService")
@Transactional
public class SyllabusServiceImpl extends BaseServiceImpl<Syllabus> implements ISyllabusService {

	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Override
	public List<Syllabus> findSyllabusTreeList(String courseId)		throws ServiceException {		
		return findSyllabusTreeList(courseId,false);
	}

	
	@Override
	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<Syllabus> findSyllabusTreeList(String courseId,boolean isCountResources) throws ServiceException {
		List<Syllabus> sybs = null;
		if(ExStringUtils.isNotEmpty(courseId)){
			Session session  = exGeneralHibernateDao.getSessionFactory().getCurrentSession();
			String sql = "select * from edu_teach_syllabustree t where t.isdeleted=0 and t.courseId=:courseId start with t.parentid is null connect by prior t.resourceid=t.parentid order siblings by t.nodeLevel,t.showOrder";
			SQLQuery query = session.createSQLQuery(sql).addEntity(Syllabus.class);
			query.setParameter("courseId",courseId);
			sybs = query.list();
		}
		
		if(null != sybs && isCountResources){//统计这门课程中，每个节点下的素材数量
			StringBuffer bf = new StringBuffer();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("courseId", courseId);
			map.put("isDeleted", 0);
			//map.put("type",2);
			
			bf.append("select a.types,count(a.syllabustreeid) as num,a.syllabustreeid from ( ")
				.append(" select g.syllabustreeid,'guid' types from edu_lear_courseguid g where g.isdeleted=:isDeleted  ")
				.append(" union all  select m.syllabustreeid,'mate' types from edu_lear_mate m  where  m.isdeleted=:isDeleted ")	 
				.append(" union all  select c.syllabustreeid,'exam' types from edu_lear_courseexam c where  c.isdeleted=:isDeleted and c.isPublished='Y' ") 
				.append(" union all select r.syllabustreeid,'ref' types from edu_lear_reference r where r.isdeleted=:isDeleted ")	 
				.append(" ) a")
				.append(" left join edu_teach_syllabustree t on t.resourceid=a.syllabustreeid ")
				.append(" where t.courseid=:courseId")	
				.append(" group by a.types,a.syllabustreeid");
			try {
				List<Map<String, Object>> results = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(bf.toString(), map);
				if(null != results){
					for(Map<String, Object> m : results){//遍历该课程素材数量
						for(Syllabus syllabus : sybs){//遍历该课程所有节点
							if(m.get("syllabustreeid".toUpperCase()).toString().equals(syllabus.getResourceid())){								
								int num = Integer.parseInt(m.get("num".toUpperCase()).toString());
								if(num >0){//如果统计结果大于0，则放入这个节点的扩展map中
									syllabus.getExMetaMap().put(m.get("types".toUpperCase()).toString(),Constants.BOOLEAN_YES);
								}
								break;
							}
						}
					}				
				
					
				}
			} catch (Exception e) {
				logger.error("获取知识结构树下素材数量出错:{}",e.fillInStackTrace());
			}
						
		}
		return sybs;
	}

	@Override
	@Transactional(readOnly=true)
	public Page findSyllabusByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+Syllabus.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("courseId")){//课程id
			hql += " and course.resourceid =:courseId ";
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("syllabusId")){//父节点id
			hql += " and parent.resourceid=:syllabusId ";
			values.put("syllabusId", condition.get("syllabusId"));
		}
		if(condition.containsKey("nodeName")){//节点名称
			hql += " and nodeName like :nodeName ";
			values.put("nodeName", "%"+condition.get("nodeName")+'%');
		}	
		if(condition.containsKey("required")){//教学要求度
			hql += " and required=:required ";
			values.put("required", condition.get("required"));
		}
		if(condition.containsKey("abilityTarget")){//能力目标
			hql += " and abilityTarget=:abilityTarget ";
			values.put("abilityTarget", condition.get("abilityTarget"));
		}
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}
	
	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException {
		if(ids.length>0){
			for(int index=0;index<ids.length;index++){
				deleteSyllabus(ids[index],ids.length);				
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void deleteSyllabus(String resourceid, int size) throws ServiceException {
		Syllabus syllabus = get(resourceid);
		
		Criterion[] criterions = new Criterion[2];
		criterions[0] = Restrictions.eq("isDeleted", 0);
		criterions[1] = Restrictions.eq("parent.resourceid", syllabus.getParent().getResourceid());
		List syllabusList = exGeneralHibernateDao.findByCriteria(Syllabus.class, criterions);
		Syllabus parentSyllabus = null;
		if(syllabusList.size() == size){//该父节点的全部子全删
			parentSyllabus = load(syllabus.getParent().getResourceid());
			parentSyllabus.setIsChild(Constants.BOOLEAN_NO);
			exGeneralHibernateDao.update(parentSyllabus);
		}
		
		Session session  = exGeneralHibernateDao.getSessionFactory().getCurrentSession();
		String sql = "select * from edu_teach_syllabustree t where t.isdeleted=0 start with t.resourceid=:resourceid connect by prior t.resourceid=t.parentid order siblings by t.nodeLevel,t.showOrder";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Syllabus.class);
		query.setParameter("resourceid",resourceid);
		List<Syllabus> sybs = query.list();	
		
		for(Syllabus s : sybs){
			String msg = isHasResource(s.getResourceid());
			if(ExStringUtils.isNotBlank(msg)){
				throw new ServiceException("知识节点: <b>"+s.getSyllabusName()+"</b> 存在 <b>"+msg+"</b> 已经被引用，<b>不允许删除</b>。如果要删除，请先移走所属的随堂练习、学习材料等内容。");
			} else {
				exGeneralHibernateDao.delete(Syllabus.class, s.getResourceid());					
			}
		}
	}

	private String isHasResource(String syllabusId) throws ServiceException{
		String[] modelNames = new String[]{ActiveCourseExam.class.getSimpleName(),MateResource.class.getSimpleName(),CourseReference.class.getSimpleName()};
		String[] msg = new String[]{"随堂练习","学习材料","参考资料"};
		for (int i = 0; i < modelNames.length; i++) {
			if(resourceCount(modelNames[i],syllabusId)){
				return msg[i];
			}
		}
		return "";
	}	
	private boolean resourceCount(String modelName,String syllabusId){
		String hql = "select count(resourceid) from "+modelName+" where isDeleted=? and syllabus.resourceid=? ";
		Long count = exGeneralHibernateDao.findUnique(hql, 0,syllabusId);
		return count>0;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Syllabus> findSyllabusByParentId(String parentId) throws ServiceException {
		Session session  = exGeneralHibernateDao.getSessionFactory().getCurrentSession();
		String sql = "select * from edu_teach_syllabustree t where t.isdeleted=0  start with t.resourceid=:resourceid connect by prior t.resourceid=t.parentid";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Syllabus.class);
		query.setParameter("resourceid",parentId);
		return query.list();
	}

	@Override
	public Syllabus getSyllabusRoot(String courseId) throws ServiceException {
		return exGeneralHibernateDao.findUnique("from Syllabus where isDeleted=0 and course.resourceid=? and parent is null ", courseId);
	}

	@Override
	public Integer getNextShowOrder(String syllabusId) throws ServiceException {
		String hql = "select max(s.showOrder) from "+Syllabus.class.getSimpleName()+" s where s.isDeleted=0 ";
		if(ExStringUtils.isNotEmpty(syllabusId)) {
			hql += "and s.parent.resourceid=? ";
		} else {
			hql += "and s.parent is null ";
		}
		Integer showOrder = exGeneralHibernateDao.findUnique(hql, syllabusId);
		if(showOrder==null) {
			showOrder = 0;
		}
		return ++showOrder;
	}
	
	@Override
	public Long countSyllabusTreeList(String courseId) throws ServiceException {
		String hql = "select count(resourceid) from "+Syllabus.class.getSimpleName()+" where isDeleted=0 and course.resourceid=? ";
		return exGeneralHibernateDao.findUnique(hql, courseId);
	}
	
	/**
	 * 获取知识结构树
	 * @param syllabusList 知识节点列表
	 * @param checkedValues 已选择的值
	 * @param checkable 树类型(菜单树或checkbox树)
	 * @return
	 */
	public static ZTreeNode getSyllabusTree(List<Syllabus> syllabusList,Map<String,String> checkedValues,boolean checkable){
		ZTreeNode zTreeNode = null;		
		ZTreeNode zTreeNode1 = null;	
		
		int levelNum = 0;
		Map<String,ZTreeNode> map = new HashMap<String, ZTreeNode>();
		for(Syllabus syllabus:syllabusList){
			boolean checked = false;	
			String id = syllabus.getResourceid();			
			if(checkedValues.containsKey(id)){//节点被选择
				checked = true;
				if(checkable){
					id += ","+checkedValues.get(id); //加上扩展值
				}
			}
			if(!checkable){//菜单树附上节点类型
				id += ","+ExStringUtils.trimToEmpty(syllabus.getSyllabusType());
			}
			boolean isExpand = false;
			if(levelNum<2 && syllabus.getNodeLevel()== 1L) {
				++levelNum;
			}
			if(checkable || levelNum==1) //菜单树展开第一个一级节点
			{
				isExpand = true;
			}
			if(null==syllabus.getParent()){			
				//							节点名称				  ID  父	 是否打开	 是否选中  		级别									子节点				
				zTreeNode =  new ZTreeNode(syllabus.getNodeName(),id,null,true,checked,syllabus.getSyllabusLevel().intValue(),new LinkedHashSet<ZTreeNode>(),syllabus.getExMetaMap());
				map.put(syllabus.getResourceid(), zTreeNode);
			}else {
				if(null!=map.get(syllabus.getParent().getResourceid())){
					if(!checkable && !checkedValues.isEmpty() && !checked)//菜单树只显示被选择的节点
					{
						continue;
					}
					ZTreeNode parentSyllabus = map.get(syllabus.getParent().getResourceid());
					zTreeNode1 =  new ZTreeNode(syllabus.getNodeName(),id,syllabus.getParent().getResourceid(),isExpand,checked,syllabus.getSyllabusLevel().intValue(),new LinkedHashSet<ZTreeNode>(),syllabus.getExMetaMap());
					map.put(syllabus.getResourceid(), zTreeNode1);
					parentSyllabus.getNodes().add(zTreeNode1);						
				}
			}	
		}		
		map = null;
		zTreeNode1 = null;
		return zTreeNode;
	}
	
	@Override
	public void saveOrUpdateSyllabus(Syllabus syllabus) throws ServiceException {
		if(ExStringUtils.isNotEmpty(syllabus.getCourseId())){
			syllabus.setCourse(courseService.get(syllabus.getCourseId()));
		}
		if(ExStringUtils.isNotEmpty(syllabus.getParentId())){
			Syllabus parent = get(syllabus.getParentId());
			syllabus.setParent(parent);
			parent.setIsChild(Constants.BOOLEAN_YES);
			syllabus.setSyllabusLevel(new Long(parent.getNodeLevel()+1));
			if(ExStringUtils.isBlank(syllabus.getSyllabusType())){
				Long nodeLevle = (syllabus.getSyllabusLevel() > 3L)? 3L :syllabus.getSyllabusLevel();
				syllabus.setSyllabusType(nodeLevle.toString());
			}
		}
		if(ExStringUtils.isNotBlank(syllabus.getResourceid())){ //--------------------更新
			Syllabus persistSyllabus = get(syllabus.getResourceid());
			Syllabus oldParent = persistSyllabus.getParent();
			syllabus.setIsChild(persistSyllabus.getIsChild());
			syllabus.setChildren(persistSyllabus.getChildren());
			
			Long fix = syllabus.getSyllabusLevel()-persistSyllabus.getSyllabusLevel();
			ExBeanUtils.copyProperties(persistSyllabus, syllabus);			
			
			if(ExStringUtils.isNotEmpty(syllabus.getParentId())){	
				if(!syllabus.getParentId().equals(oldParent.getResourceid())){//父节点改变,改变排序号
					syllabus.setShowOrder(getNextShowOrder(syllabus.getParentId()));	
					List<Syllabus> list = findSyllabusByParentId(oldParent.getResourceid());
					if(list!=null && list.size()==1){
						oldParent.setIsChild(Constants.BOOLEAN_NO);
					}					
				}	
				if(fix!=0){//节点级别变更,调整子节点级别
					List<Syllabus> currentChilds = findSyllabusByParentId(persistSyllabus.getResourceid());
					if(ExCollectionUtils.isNotEmpty(currentChilds)){
						for (Syllabus s : currentChilds) {
							if(!s.getResourceid().equals(persistSyllabus.getResourceid())){
								s.setSyllabusLevel(s.getSyllabusLevel()+fix);
								s.setSyllabusType(Long.toString((s.getSyllabusLevel() > 3L)? 3L :s.getSyllabusLevel()));
							}
						}
					}
				}				
			}			
			update(persistSyllabus);
		} else {
			save(syllabus);
		}
		try {
			Syllabus rootSyllabus = getSyllabusRoot(syllabus.getCourse().getResourceid());
			//知识结构树根节点名称与课程名称不同（建立知识结构树后更改了课程名）,修正根节点名称为课程名称
			if(!ExStringUtils.equals(rootSyllabus.getNodeName(), syllabus.getCourse().getCourseName())){
				rootSyllabus.setSyllabusName(syllabus.getCourse().getCourseName());
			}
		} catch (Exception e) {			
		}
	}
}
