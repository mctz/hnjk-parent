package com.hnjk.edu.resources.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.rao.dao.jdbc.BaseSupportJdbcDao;
import com.hnjk.edu.resources.service.ICourseResourceService;
import com.hnjk.edu.resources.vo.SyllabusVo;
/**
 * 精品课程服务.
 * <code>CourseResourceService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-11-28 下午12:37:50
 * @see 
 * @version 1.0
 */
@Transactional
@Service("courseResourceService")
public class CourseResourceService extends BaseSupportJdbcDao implements ICourseResourceService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public List<SyllabusVo> findSyllabusVoForCourseResource(String courseId, String resType) throws ServiceException {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select s.resourceid,s.nodename syllabusname,s.nodelevel syllabusLevel,s.nodetype syllabusType,s.courseid,s.parentid,s.showorder,count(decode(r.resType,:resType,1,null)) rescount ");
		sql.append(" ,count(decode(r.resType,'0',1,null)) handoutCount,count(decode(r.resType,'1',1,null)) mateCount,count(decode(r.resType,'2',1,null)) courseExamCount ");
		sql.append(" ,(select count(g.resourceid) from edu_lear_courseguid g where g.isdeleted=0 and g.type<>'5' and g.syllabustreeid=s.resourceid) guidCount ");
		sql.append(" from edu_teach_syllabustree s ");
		//sql.append(" left join "+("2".equals(resType)?"edu_lear_courseexam":"edu_lear_mate")+" r on r.syllabustreeid=s.resourceid and r.isdeleted=0 and r.ispublished='Y' ");
		sql.append(" left join (select m.resourceid,m.syllabustreeid, (case when m.matetype='6' then '0' when m.matetype in ('9','11','12','13')  then '2' when m.matetype in ('2','3','4','8','10') then '1' end) as resType from edu_lear_mate m where m.isdeleted=0 and m.ispublished='Y' ");
		sql.append(" union all ");
		sql.append(" select c.resourceid,c.syllabustreeid,'2' as resType from edu_lear_courseexam c where c.isdeleted=0 and c.ispublished='Y' ");
		sql.append(" ) r on r.syllabustreeid=s.resourceid  ");//and r.resType=:resType
		params.put("resType", resType);	
		sql.append(" where s.isdeleted=0 and s.courseid=:courseId ");
		sql.append(" and s.nodelevel<=3 and s.nodelevel>=1");
		params.put("courseId", courseId);		
		sql.append(" group by s.resourceid,s.nodename,s.nodelevel,s.nodetype,s.courseid,s.parentid,s.showorder ");
		sql.append(" order by s.nodelevel,s.showorder,s.resourceid ");
		
		List<SyllabusVo> syllabusList = new ArrayList<SyllabusVo>();
		try {
			syllabusList = getBaseJdbcTemplate().findList(sql.toString(), SyllabusVo.class,params);			
		} catch (Exception e) {
			logger.error("课程学习知识节点列表查询出错:{}",e.fillInStackTrace());			
		}	
		return syllabusList;
	}
	
	@Override
	public List<SyllabusVo> findSyllabusVoForLearningStat(String courseId, String studentId)	throws ServiceException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseId", courseId);
		params.put("studentId", studentId);
		StringBuilder sql = new StringBuilder();
		sql.append(" select rs.*,nvl(cs.examCorrectCount,0) examCorrectCount,nvl(cs.examFinishedCount,0) examFinishedCount,nvl(cs.examDoneCount,0) examDoneCount, ");
		sql.append(" case when rs.handoutcount>0 then st.learhandouttime else null end learhandouttime, ");
		sql.append(" case when rs.matecount>0 then st.learmetetime else null end learmetetime, ");
		sql.append(" case when rs.examcount>0 then st.learcourseexamtime else null end learcourseexamtime  ");
		sql.append(" ,(select count(cc.resourceid) from edu_lear_courseexam cc join edu_lear_exams ss on ss.resourceid=cc.examid where cc.isdeleted=0 and cc.ispublished='Y' and ss.examtype<=5 and cc.syllabustreeid=rs.resourceid) examTotalCount ");
		sql.append(" from ( ");
		sql.append("     select s.resourceid,s.nodename syllabusname,s.courseid,s.rn, count(decode(r.resType,'0',1,null)) handoutcount, count(decode(r.resType,'1',1,null)) matecount, count(decode(r.resType,'2',1,null)) examcount ");
		sql.append("     from ( select t.resourceid,t.nodename,t.courseid,rownum rn from edu_teach_syllabustree t where t.isdeleted = 0 and t.nodelevel>=2 and t.nodelevel<=3 and t.courseId = :courseId start with t.parentid is null connect by prior t.resourceid = t.parentid order siblings by t.nodeLevel, t.showOrder) s   ");
		sql.append("     left join (select m.resourceid,m.syllabustreeid, (case when m.matetype='6' then '0' when m.matetype in ('9','11','12','13') then '2' when m.matetype in ('2','3','4','8','10') then '1' end) as resType from edu_lear_mate m where m.isdeleted=0 and m.ispublished='Y' union all select c.resourceid,c.syllabustreeid,'2' as resType from edu_lear_courseexam c where c.isdeleted=0 and c.ispublished='Y') r on r.syllabustreeid=s.resourceid  ");
		sql.append("     group by s.resourceid,s.nodename,s.courseid,s.rn ");
		sql.append(" ) rs left join edu_lear_stutrace st on rs.resourceid=st.syllabustreeid and st.studentid=:studentId ");
		sql.append(" left join ( ");
		sql.append("     select c.syllabustreeid,count(distinct case when sc.iscorrect='Y' then sc.courseexamid else null end) examCorrectCount, count(distinct case when sc.result is not null then sc.courseexamid else null end) examFinishedCount, count(distinct sc.courseexamid) examDoneCount  ");
		sql.append("     from edu_lear_studentcourseexam sc join edu_lear_courseexam c on c.resourceid=sc.courseexamid and c.isdeleted=0 and c.ispublished='Y' join edu_lear_exams es on c.examid=es.resourceid and es.examtype<=5 and es.isdeleted=0 ");
		sql.append("     where sc.isdeleted=0 and sc.studentid=:studentId group by c.syllabustreeid ");
		sql.append(" ) cs on cs.syllabustreeid=rs.resourceid ");
		sql.append(" order by rs.rn ");
		List<SyllabusVo> syllabusVoList = new ArrayList<SyllabusVo>();
		try {
			syllabusVoList = getBaseJdbcTemplate().findList(sql.toString(), SyllabusVo.class,params);			
		} catch (Exception e) {
			logger.error("课程学习知识节点列表查询出错:{}",e.fillInStackTrace());			
		}	
		return syllabusVoList;
	}
	
	@Override
	public List<SyllabusVo> findSyllabusVoForSearch(String courseId, String keyword) throws ServiceException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseId", courseId);
		params.put("keyword", "%"+keyword+"%");
		StringBuilder sql = new StringBuilder();
		sql.append("select s.resourceid,s.nodename syllabusname,s.courseid,s.rn ,count(decode(r.resType,'0',1,null)) handoutCount,count(decode(r.resType,'1',1,null)) mateCount,count(decode(r.resType,'2',1,null)) courseExamCount ");
		sql.append("     from (select t.resourceid,t.nodename,t.courseid,rownum rn from edu_teach_syllabustree t  where t.isdeleted = 0 and t.nodelevel>=1 and t.nodelevel<=3 and t.courseId = :courseId start with t.parentid is null connect by prior t.resourceid = t.parentid order siblings by t.nodeLevel, t.showOrder) s   ");
		sql.append("     left join (select m.resourceid,m.syllabustreeid, (case when m.matetype='6' then '0' when m.matetype in ('9','11','12','13') then '2' when m.matetype in ('2','3','4','8','10') then '1' end) as resType from edu_lear_mate m where m.isdeleted=0 and m.ispublished='Y' union all select c.resourceid,c.syllabustreeid,'2' as resType from edu_lear_courseexam c where c.isdeleted=0 and c.ispublished='Y') r on r.syllabustreeid=s.resourceid   ");
		sql.append("    where s.nodename like :keyword ");
		sql.append("     group by s.resourceid,s.nodename,s.courseid,s.rn order by s.rn ");
		List<SyllabusVo> syllabusVoList = new ArrayList<SyllabusVo>();
		try {
			syllabusVoList = getBaseJdbcTemplate().findList(sql.toString(), SyllabusVo.class,params);			
		} catch (Exception e) {
			logger.error("精品课程检索出错:{}",e.fillInStackTrace());			
		}	
		return syllabusVoList;
	}
}
