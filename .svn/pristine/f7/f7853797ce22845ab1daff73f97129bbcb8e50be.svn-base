package com.hnjk.edu.teaching.service.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.BaseSupportJdbcDao;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.recruit.model.RecruitExamLogs;
import com.hnjk.edu.recruit.model.RecruitExamStudentAnswer;
import com.hnjk.edu.recruit.service.IRecruitExamStudentAnswerService;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.service.IExamPaperCorrectService;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.vo.OnlineExamResulstsVo;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
/**
 * 阅卷管理服务.
 * <code>ExamPaperCorrectServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-11-15 下午04:16:16
 * @see 
 * @version 1.0
 */
@Transactional
@Service("examPaperCorrectService")
public class ExamPaperCorrectServiceImpl extends BaseSupportJdbcDao implements IExamPaperCorrectService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	@Qualifier("recruitExamStudentAnswerService")
	private IRecruitExamStudentAnswerService recruitExamStudentAnswerService;
	
	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;
	
	@Override
	@Transactional(readOnly=true)
	public Page findCourseExamForCorrectByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//批次id，课程id，课程编码，课程名称，试题id，未作答人数，答卷人数，未批阅人数，已批阅人数
		sql.append(" select r.noAnswerNum,r.answerNum,r.uncorrectNum,r.correctNum,r.courseid,r.examid,m.question,m.answer,m.examType,m.examNodeType,c.coursecode,c.coursename ");
		if(condition.containsKey("examSubId")){//考试批次
			sql.append(" ,'"+condition.get("examSubId")+"' examsubid");
		}
		sql.append(" from ( ");
		sql.append("     select es.courseid,d.examid, ");
		sql.append("     count(case when a.answer is null then 1 else null end) noAnswerNum, ");//未作答
		sql.append("     count(a.resourceid) answerNum, ");//作答人数
		sql.append("     count(decode(nvl(a.iscorrect,'W'),'W',-1,null)) uncorrectNum, ");//未批阅
		sql.append("     count(decode(nvl(a.iscorrect,'W'),'W',null,1)) correctNum ");//已批阅
		sql.append("     from edu_recruit_stuanswer a ");
		sql.append("     join edu_lear_expaperdetails d on d.resourceid=a.examid ");
		sql.append("     join edu_lear_exams es on es.resourceid=d.examid  ");
		sql.append("     where a.isdeleted=0  ");
		if(condition.containsKey("examSubId")){//考试批次
			sql.append(" and exists ( ");
			sql.append("     select t.resourceid from  edu_recruit_stustates t join edu_teach_examinfo ei on ei.resourceid = t.examinfoid ");
			sql.append("     where t.isdeleted = 0 and t.status = 2 and ei.ismachineexam = 'Y' and t.studentinfoid = a.studentinfoid and t.exampaperid = a.exampaperid ");
			sql.append("     and ei.examsubid = ? ");
			sql.append(" ) ");
			params.add(condition.get("examSubId"));
		}
		sql.append("     and es.examtype>=4 and es.examtype<=5 and es.isOnlineAnswer='Y' ");//填空题，论述题,在线作答题目类型
		sql.append("     and d.isdeleted=0 and es.isdeleted=0");
		if(condition.containsKey("courseId")){//课程
			sql.append(" and es.courseid=? ");
			params.add(condition.get("courseId"));
		}		
		sql.append("     group by es.courseid,d.examid ");
		sql.append(" ) r join edu_lear_exams m on r.examid=m.resourceid join edu_base_course c on c.resourceid=m.courseid ");
		sql.append(" order by c.coursecode,m.showorder,m.resourceid ");		
		return getBaseJdbcTemplate().findListMap(objPage, sql.toString(), params.toArray());
	}	
	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public Map<String, Object> getStudentExamAnswer(String examSubId, String examId, String answerid, String isFinishCorrect) throws ServiceException {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select '"+examSubId+"' examsubid,a.resourceid answerid,d.score,d.examid,a.answer stuanswer,nvl(a.result,0) result,a.iscorrect,es.question,es.answer,sum(1) over() - 1 as remainCount ");
		sql.append("     from edu_recruit_stuanswer a ");
		sql.append("     join edu_lear_expaperdetails d on d.resourceid=a.examid  ");
		sql.append("     join edu_lear_exams es on es.resourceid=d.examid ");		
		sql.append("     where a.isdeleted=0 ");
		if(ExStringUtils.isNotBlank(answerid)){//如果有上一份试卷的id，查询大于该id的数据
			sql.append("     and a.resourceid > ? ");
			params.add(answerid);
		} 
		if(!"Y".equals(isFinishCorrect)){//未完成批阅，定位到下一份未批阅的答卷
			sql.append(" and a.iscorrect is null ");
		}
		sql.append(" 	and exists ( ");
		sql.append("     	select t.resourceid from edu_recruit_stustates t join edu_teach_examinfo ei on ei.resourceid = t.examinfoid ");
		sql.append("     	where t.isdeleted = 0 and t.status = 2 and ei.ismachineexam = 'Y' and t.studentinfoid = a.studentinfoid and t.exampaperid = a.exampaperid ");
		sql.append("     	and ei.examsubid = ? ");
		sql.append(" 	) ");
		params.add(examSubId);
		sql.append("     and d.examid=? ");//试题id
		params.add(examId);		 
		sql.append(" 	and d.isdeleted=0 and es.isdeleted=0 ");
		sql.append("     order by a.resourceid ");
		final RowMapper rowMapper = new ColumnMapRowMapper();
		return (Map<String, Object>) getBaseJdbcTemplate().getOriginalJdbcTemplate().query(sql.toString(), params.toArray(), new ResultSetExtractor() {
			@Override
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) { 
					return rowMapper.mapRow(rs, 0);//取第一个数据
				}
				return Collections.EMPTY_MAP;
			}
		});
	}
	
	@Override
	public void batchZeroExamPapers(String examSubId, String[] ids) throws ServiceException {
		String sql = "update edu_recruit_stuanswer t set t.result=0,t.iscorrect='Y' where t.resourceid in ( select a.resourceid from edu_recruit_stuanswer a join edu_lear_expaperdetails d on d.resourceid=a.examid and d.isdeleted=0 join edu_recruit_stustates t on t.studentinfoid=a.studentinfoid and t.exampaperid=a.exampaperid and t.isdeleted=0 join edu_teach_examinfo ei on ei.resourceid=t.examinfoid and ei.isdeleted=0 where a.isdeleted=0 and a.iscorrect is null and a.answer is null and d.examid in ('"+ExStringUtils.join(ids,"','")+"') and ei.examsubid=? )";
		getBaseJdbcTemplate().getJdbcTemplate().update(sql, examSubId);		
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page findOnlineExamResulstsVoByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select r.resourceid,r.studentid,s.studyno,s.studentname,r.writtenscore,r.writtenMachineScore,r.writtenOnlineHandworkScore,r.checkstatus,b.batchname,c.coursename ");
		sql.append(" from edu_teach_examresults r ");
		sql.append(" join edu_roll_studentinfo s on s.resourceid=r.studentid ");
		sql.append(" join edu_teach_examinfo ei on ei.resourceid=r.examinfoid ");
		sql.append(" join edu_teach_examsub b on b.resourceid=ei.examsubid ");
		sql.append(" join edu_base_course c on c.resourceid=r.courseid ");
		sql.append(" where r.isdeleted=0 and ei.ismachineexam='Y' ");//机考
		sql.append(" and ei.examType='2' ");//在线作答类型
		sql.append(" ");
		//有考试记录的学生
		sql.append(" and exists (select t.resourceid from edu_recruit_stustates t where t.isdeleted=0 and t.status=2 and r.studentid=t.studentinfoid and  r.examinfoid=t.examinfoid ) ");
		if(condition.containsKey("examSubId")){//批次
			sql.append(" and r.examsubid=? ");
			param.add(condition.get("examSubId"));
		}
		if(condition.containsKey("courseId")){//课程
			sql.append(" and r.courseid=? ");
			param.add(condition.get("courseId"));
		}
		if(condition.containsKey("studyNo")){//学号
			sql.append(" and s.studyno =? ");
			param.add(condition.get("studyNo"));
		}
		if(condition.containsKey("studentName")){//姓名
			sql.append(" and s.studentname like ? ");
			param.add("%"+condition.get("studentName")+"%");
		}
		if(condition.containsKey("checkStatus")){
			sql.append(" and r.checkstatus =? ");
			param.add(condition.get("checkStatus"));
		}
		sql.append(" order by ei.examsubid,s.studyno,r.resourceid ");
		return getBaseJdbcTemplate().findList(objPage, sql.toString(), param.toArray(), OnlineExamResulstsVo.class);
	}
	
	@Override
	public Page findExamCourseForCorrectByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		String sql = "select distinct ei.examsubid,ei.courseid,c.coursecode,c.coursename,s.batchname from edu_teach_examinfo ei join edu_base_course c on c.resourceid=ei.courseid join edu_teach_examsub s on s.resourceid=ei.examsubid where ei.isdeleted=0 and ei.ismachineexam='Y' and ei.examsubid=? and ei.examType='2' order by c.coursecode";//在线作答类型
		final long rowCount = getBaseJdbcTemplate().getOriginalJdbcTemplate().queryForLong("select count(distinct ei.courseid) from "+ExStringUtils.substringBeforeLast(ExStringUtils.substringAfter(sql, "from"), "order by"), new Object[]{condition.get("examSubId")});  
		objPage.setTotalCount(new Long(rowCount).intValue());
		objPage.setAutoCount(false);
		return getBaseJdbcTemplate().findListMap(objPage, sql, new Object[]{condition.get("examSubId")});
	}
	
	@Override
	public void saveExamPaperCorrectScore(String examSubId, RecruitExamStudentAnswer answer, Double score) throws ServiceException {
		String sql = "select nvl(sum(a.result),0) result from edu_recruit_stuanswer a join edu_lear_expaperdetails d on d.resourceid=a.examid join edu_lear_exams s on s.resourceid=d.examid where a.isdeleted=0 and a.iscorrect='Y' and s.examtype>=4 and s.examtype<=5 and s.isonlineanswer='Y' and a.studentinfoid=? and a.exampaperid=?";
		Long result = getBaseJdbcTemplate().getOriginalJdbcTemplate().queryForLong(sql, new Object[]{answer.getStudentInfo().getResourceid(),answer.getCourseExamPapers().getResourceid()});
		Double total = result + score - (answer.getResult()==null?0.0:answer.getResult().doubleValue());
		answer.setIsCorrect(Constants.BOOLEAN_YES);//设置为已批改
		answer.setResult(score);//设置分数
		recruitExamStudentAnswerService.update(answer);
		
		List<ExamResults> examResultList = examResultsService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("examsubId", examSubId),Restrictions.eq("course.resourceid", answer.getCourseExamPapers().getCourse().getResourceid()),Restrictions.eq("studentInfo.resourceid", answer.getStudentInfo().getResourceid()));
		if(ExCollectionUtils.isNotEmpty(examResultList)){//更新非客观题在线作答成绩
			ExamResults r = examResultList.get(0);			
			r.setWrittenOnlineHandworkScore(total.toString());
			r.setWrittenScore(BigDecimal.valueOf(total+Double.valueOf(ExStringUtils.defaultIfEmpty(r.getWrittenMachineScore(), "0"))).divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP).toString());
			
			User user = SpringSecurityHelper.getCurrentUser();
			r.setFillinDate(new Date());
			r.setFillinMan(user.getCnName());
			r.setFillinManId(user.getResourceid());
			
			r.setEnableLogHistory(true);//记录日志
			examResultsService.update(r);
		}	
	}
	
	@Override
	public void submitOnlineExamResults(String examSubId, String[] courseIds) throws ServiceException {
		StringBuffer countSql = new StringBuffer();
		countSql.append("select count(a.resourceid) from edu_recruit_stuanswer a ")
		.append(" join edu_lear_expaperdetails d on d.resourceid=a.examid and d.isdeleted=0 ")
		.append(" join edu_lear_exams es on es.resourceid=d.examid and es.isdeleted=0 ")
		.append(" join edu_recruit_stustates t on t.studentinfoid=a.studentinfoid and t.exampaperid=a.exampaperid and t.isdeleted=0 and t.status=2 ")
		.append(" join edu_teach_examinfo ei on ei.resourceid=t.examinfoid  ")
		.append(" where a.isdeleted=0 and es.examtype>=4 and es.examtype<=5 and ei.ismachineexam='Y' and es.isonlineanswer='Y' ")
		.append(" and a.iscorrect is null ")
		.append(" and ei.examsubid=? and ei.courseid in ('"+ExStringUtils.join(courseIds,"','")+"') ");
		long count = getBaseJdbcTemplate().getOriginalJdbcTemplate().queryForLong(countSql.toString(), new Object[]{examSubId});
		if(count > 0){
			throw new ServiceException("还有<b>"+count+"</b>份答卷未批阅,请批阅后再提交成绩");
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from "+ExamResults.class.getSimpleName()+" r where r.isDeleted=0")		
		.append(" and exists ( from "+RecruitExamLogs.class.getSimpleName()+" s where s.isDeleted=0 and s.status=2 and s.examInfo.resourceid=r.examInfo.resourceid and s.studentInfo.resourceid=r.studentInfo.resourceid)")
		.append(" and r.examsubId=:examSubId and r.course.resourceid in (:courseIds) and r.examInfo.isMachineExam='Y' ")
		.append(" and r.examInfo.examType='2' ")//在线作答
		.append(" and r.checkStatus = '0' ");//未提交的
		condition.put("examSubId", examSubId);
		condition.put("courseIds", Arrays.asList(courseIds));		
		List<ExamResults> examResultList = examResultsService.findByHql(hql.toString(), condition);
		if(ExCollectionUtils.isNotEmpty(examResultList)){
			for (ExamResults examResults : examResultList) {
				examResults.setCheckStatus("1");//提交成绩
			}
		}
	}
	
	@Override
	public void saveRecorreExamScore(String examResultId, String[] answerid, String[] score) throws ServiceException {		
		if(ExStringUtils.isNotBlank(examResultId) && answerid != null){
			ExamResults examResult = examResultsService.get(examResultId);
			Double addtotal = 0.0;
			for (int i = 0; i < answerid.length; i++) {
				RecruitExamStudentAnswer answer = recruitExamStudentAnswerService.get(answerid[i]);
				double addscore = Double.valueOf(score[i]) - (answer.getResult()!=null?answer.getResult().doubleValue():0.0);
				if(answer.getResult()!=null && addscore==0) {
					continue;
				}
				addtotal += addscore;
				
				answer.setIsCorrect(Constants.BOOLEAN_YES);
				answer.setResult(Double.valueOf(score[i]));				
			}
			Double total = Double.valueOf(ExStringUtils.defaultIfEmpty(examResult.getWrittenOnlineHandworkScore(), "0"))+addtotal;
			examResult.setWrittenOnlineHandworkScore(BigDecimal.valueOf(total).divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP).toString());
			examResult.setWrittenScore(BigDecimal.valueOf(total+Double.valueOf(ExStringUtils.defaultIfEmpty(examResult.getWrittenMachineScore(), "0"))).divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP).toString());
			examResult.setEnableLogHistory(true);
		}
	}
}
