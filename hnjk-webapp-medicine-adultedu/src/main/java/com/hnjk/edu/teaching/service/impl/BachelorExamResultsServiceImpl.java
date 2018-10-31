package com.hnjk.edu.teaching.service.impl;

import com.hnjk.cache.InitAppDataServiceImpl;
import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.BachelorExamResults;
import com.hnjk.edu.teaching.service.IBachelorExamResultsService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.SysConfiguration;
import com.hnjk.platform.system.service.ISysConfigurationService;
import com.hnjk.security.service.IOrgUnitService;
import lombok.Cleanup;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Transactional
@Service("bachelorExamResultsService")

public class BachelorExamResultsServiceImpl extends BaseServiceImpl<BachelorExamResults> implements IBachelorExamResultsService {
	
	@Autowired
	@Qualifier("sysConfigurationService")
	private ISysConfigurationService sysConfigurationService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	private InitAppDataServiceImpl initAppDataServiceImpl;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	private StringBuffer returnMsg = new StringBuffer();
	private Map<String,Object> validateMap;
	
	private int  count = 0;		//总条数
	private int successCount = 0;// 成功的条数
	private int startRow =3;	//第一条数据所在行数
	List<Integer> ordinalList = new ArrayList<Integer>();
	List<String> seatNumber = new ArrayList<String>();
	List<Integer> yearList = new ArrayList<Integer>();
	List<Integer> termList = new ArrayList<Integer>();
	List<String> studyNoList = new ArrayList<String>();

	/**
	 * 根据条件获取英语成绩信息
	 * @param condition objPage
	 * @return Page
	 */
	@Override
	@Transactional(readOnly=true)
	public Page findBachelorExamByHql(Map<String, Object> condition,
									  Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		
		StringBuffer hql = new StringBuffer(" from "+BachelorExamResults.class.getSimpleName()+" c where 1=1 and c.isDeleted = :isDeleted ");
		values.put("isDeleted", 0);

		if(condition.containsKey("yearInfoId")){
			hql.append(" and c.yearInfo.resourceid=:yearInfoId ");
			values.put("yearInfoId", condition.get("yearInfoId"));
		}
		if(condition.containsKey("term")){
			hql.append(" and c.term=:term ");
			values.put("term", condition.get("term"));
		}
		if(condition.containsKey("search_bachelorExam_branchSchool")){
			hql.append(" and c.unit.resourceid=:brSchoolId ");
			values.put("brSchoolId", condition.get("search_bachelorExam_branchSchool"));
		}
		if(condition.containsKey("search_bachelorExam_grade")){
			hql.append(" and c.studentInfo.grade.resourceid=:gradeId ");
			values.put("gradeId", condition.get("search_bachelorExam_grade"));
		}
		if(condition.containsKey("search_bachelorExam_classic")){
			hql.append(" and c.studentInfo.classic.resourceid=:classicId ");
			values.put("classicId", condition.get("search_bachelorExam_classic"));
		}
		if(condition.containsKey("search_bachelorExam_teachingType")){
			hql.append(" and c.studentInfo.teachingType=:teachingType ");
			values.put("teachingType", condition.get("search_bachelorExam_teachingType"));
		}
		if(condition.containsKey("search_bachelorExam_major")){
			hql.append(" and c.studentInfo.major.resourceid=:majorId ");
			values.put("majorId", condition.get("search_bachelorExam_major"));
		}
		if(condition.containsKey("search_bachelorExam_classes")){
			hql.append(" and c.studentInfo.classes.resourceid=:classesId ");
			values.put("classesId", condition.get("search_bachelorExam_classes"));
		}
		if(condition.containsKey("search_bachelorExam_examNo")){
			hql.append(" and c.examNo=:examNo ");
			values.put("examNo", condition.get("search_bachelorExam_examNo"));
		}
		
		if(condition.containsKey("studentid")){
			hql.append(" and c.studentInfo.studyNo =:studentid ");
			values.put("studentid", condition.get("studentid"));
		}	
		if(condition.containsKey("studentNO")){
			hql.append(" and c.studentNO =:studentNO ");
			values.put("studentNO", condition.get("studentNO"));
		}	
		if(condition.containsKey("studentName")){
			hql.append(" and c.studentName like :studentName ");
			values.put("studentName", "%"+condition.get("studentName")+"%");
		}
					
		if(ExStringUtils.isNotEmpty(objPage.getOrderBy())){
			hql.append(" order by c."+objPage.getOrderBy().replace(",", ",c.") +" "+ objPage.getOrder());
		}
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}
	
	@Override
	public Map<String, Object> analysisBachelorExamFile(String filePath) {
		// TODO Auto-generated method stub
		Map<String,Object> returnMap = new HashMap<String, Object>();
		try {
			successCount=0;
			returnMsg.setLength(0);
			startRow =3;	//第一条数据所在行数
			ordinalList.clear();
			seatNumber.clear();
			yearList.clear();
			termList.clear();
			studyNoList.clear();
			@Cleanup InputStream inputStream = new FileInputStream(filePath);
			@Cleanup Workbook workbook = WorkbookFactory.create(inputStream);
			// Read the Sheet
			//for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			do{
				Sheet sheet = workbook.getSheetAt(0);

				// Read the Row
				Row firstRow = sheet.getRow(0);
				if (firstRow == null) {
					returnMsg.append("<font color='red'></font>读取的文件内容为空！");
					continue;
				}
				if(ExStringUtils.isEmpty(ExStringUtils.toString((firstRow.getCell(0))))){
					returnMsg.append("<font color='red'>[第1行]</font>标题不能为空！");
					continue;
				}
				String titlt = ExStringUtils.toString((firstRow.getCell(0)));
				String time =titlt.substring(titlt.indexOf("时间:")+3);
				if(ExStringUtils.isEmpty(time) || !time.contains("年") || !time.contains("月") || !time.contains("日")){
					returnMsg.append("<font color='red'>[第1行]</font>标题格式错误，请填写正确时间！");
					continue;
				}
				count = sheet.getLastRowNum()-2;
				for (int rowNum=startRow; rowNum <= sheet.getLastRowNum(); rowNum++) {
					Row row = sheet.getRow(rowNum);
					if (row != null) {
						String examResults = ExStringUtils.toString(row.getCell(6));
						String ordinalStr = ExStringUtils.toString(row.getCell(0));
						String studyNoStr = ExStringUtils.toString(row.getCell(3));
						String examNoStr = ExStringUtils.toString(row.getCell(1));
						String seatNoStr = ExStringUtils.toString(row.getCell(2));
						String studentNameStr = ExStringUtils.toString(row.getCell(4));
						String unitNameStr = ExStringUtils.toString(row.getCell(5));
						String yearNameStr = ExStringUtils.toString(row.getCell(7));
						String termStr = ExStringUtils.toString(row.getCell(8));
						//判断学号
						if(ExStringUtils.isEmpty(studyNoStr)){
							returnMsg.append("<font color='red'>[第" + (rowNum+1) + "行]</font>学号不能为空").append("</br>");
							continue;
						}
						//判断其他信息
						if(	ExStringUtils.isEmpty(ordinalStr) ||	ExStringUtils.isEmpty(examNoStr) ||	ExStringUtils.isEmpty(seatNoStr) || ExStringUtils.isEmpty(studentNameStr)
								/*|| ExStringUtils.isEmpty(unitNameStr)*/ || ExStringUtils.isEmpty(yearNameStr) || ExStringUtils.isEmpty(termStr)){
							returnMsg.append("<font color='red'>[学号:" + ExStringUtils.toString(studyNoStr) + "]</font>该学生其他信息不能为空").append("</br>");
							continue;
						}

						validateMap = validateImportDatas(ordinalStr,examNoStr,seatNoStr,studyNoStr,
								studentNameStr, unitNameStr,examResults,yearNameStr,termStr,time);

						boolean isPass = (Boolean)validateMap.get("isPass");
						if(!isPass){
							returnMsg.append((String)validateMap.get("msg") + "</br>");
							continue;
						}
						BachelorExamResults bachelorExamResults = (BachelorExamResults) validateMap.get("bachelorExamResults");
						saveOrUpdate(bachelorExamResults);
						successCount++;
					}
				}
			}while(false);
			returnMap.put("totalCount", (count < 0 ? 0 : count));
			returnMap.put("successCount", successCount);
			returnMap.put("message", returnMsg.toString());
			
		} catch (FileNotFoundException e) {
			logger.error("导入成绩文件不存在", e);
			returnMap.put("message", "导入失败");
		} catch (IOException e) {
			logger.error("读取成绩文件出错", e);
			returnMap.put("message", "导入失败");
		} catch (Exception e) {
			logger.error("解析成绩文件出错", e);
			returnMap.put("message", "导入失败");
		} 
		return returnMap;
	}
	
	/**
	 * 根据条件获取唯一英语成绩信息
	 * @param condition
	 * @return
	 */
	@Override
	public BachelorExamResults findUniqueBachelorExamInfo(Map<String, Object> condition) {
		List<Object> paramValues = new ArrayList<Object>(); 
		String hql = " from "+BachelorExamResults.class.getSimpleName()+" c where c.isDeleted = 0 ";
		if(condition.containsKey("ordinal")){
			hql += " and c.ordinal=? ";
			paramValues.add(condition.get("ordinal"));
		}
		if(condition.containsKey("examNo")){
			hql += " and c.examNo=? ";
			paramValues.add(condition.get("examNo"));
		}
		if(condition.containsKey("seatNo")){
			hql += " and c.seatNo=? ";
			paramValues.add(condition.get("seatNo"));
		}
		if(condition.containsKey("studyNo")){
			hql += " and c.studentInfo.studyNo=? ";
			paramValues.add(condition.get("studyNo"));
		}
		if(condition.containsKey("studentInfo")){
			hql += " and c.studentInfo=? ";
			paramValues.add(condition.get("studentInfo"));
		}
		if(condition.containsKey("yearInfo")){
			hql += " and c.yearInfo=? ";
			paramValues.add(condition.get("yearInfo"));
		}
		if(condition.containsKey("term")){
			hql += " and c.term=? ";
			paramValues.add(condition.get("term"));
		}
		if(condition.containsKey("studentNO")){
			hql += " and c.studentNO=? ";
			paramValues.add(condition.get("studentNO"));
		}
		if(condition.containsKey("studentName")){
			hql += " and c.studentName=? ";
			paramValues.add(condition.get("studentName"));
		}
		return findUnique(hql, paramValues.toArray());
	}
	
	/**
	 * 根据条件获取学生学士学位英语成绩信息列表
	 * 
	 * 	@param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<BachelorExamResults> findByCondition(Map<String, Object> condition) throws ServiceException {
		StringBuffer hql = new StringBuffer(" from "+BachelorExamResults.class.getSimpleName()+" c where c.isDeleted = 0 ");
		Map<String,Object> values =  new HashMap<String, Object>();
		if(condition.containsKey("search_bachelorExam_branchSchool")){
			hql.append(" and c.unit.resourceid=:brSchoolId ");
			values.put("brSchoolId", condition.get("search_bachelorExam_branchSchool"));
		}
		if(condition.containsKey("search_bachelorExam_grade")){
			hql.append(" and c.studentInfo.grade.resourceid=:gradeId ");
			values.put("gradeId", condition.get("search_bachelorExam_grade"));
		}
		if(condition.containsKey("search_bachelorExam_classic")){
			hql.append(" and c.studentInfo.classic.resourceid=:classicId ");
			values.put("classicId", condition.get("search_bachelorExam_classic"));
		}
		if(condition.containsKey("search_bachelorExam_teachingType")){
			hql.append(" and c.studentInfo.teachingType=:teachingType ");
			values.put("teachingType", condition.get("search_bachelorExam_teachingType"));
		}
		if(condition.containsKey("search_bachelorExam_major")){
			hql.append(" and c.studentInfo.major.resourceid=:majorId ");
			values.put("majorId", condition.get("search_bachelorExam_major"));
		}
		if(condition.containsKey("search_bachelorExam_classes")){
			hql.append(" and c.studentInfo.classes.resourceid=:classesId ");
			values.put("classesId", condition.get("search_bachelorExam_classes"));
		}
		if(condition.containsKey("search_bachelorExam_examNo")){
			hql.append(" and c.examNo=:examNo ");
			values.put("examNo", condition.get("search_bachelorExam_examNo"));
		}
		if(condition.containsKey("studentid")){
			hql.append(" and c.studentInfo.studyNo =:studentid ");
			values.put("studentid", condition.get("studentid"));
		}	
		if(condition.containsKey("studentNO")){
			hql.append(" and c.studentNO =:studentNO ");
			values.put("studentNO", condition.get("studentNO"));
		}	
		if(condition.containsKey("studentName")){
			hql.append(" and c.studentName =:studentName ");
			values.put("studentName", condition.get("studentName").toString());
		}
		hql.append(" order by c.examResults desc");
		return  (List<BachelorExamResults>) exGeneralHibernateDao.findByHql(hql.toString(), values);
	}

	    /**
		 * 检导入成绩信息的准确性和合法性
		 * 
		 * @param ordinal
		 * @param examNo
		 * @param seatNo
		 * @param studyNo
		 * @param studentName
		 * @param unitName
		 * @param examResults
		 * @param yearName
		 * @param term
		 * @param time
		 * @return
		 */
	    private  Map<String, Object> validateImportDatas (String ordinal,String examNo,String seatNo,
	    		String studyNo, String studentName,String unitName,String examResults,String yearName,String term,String time) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			BachelorExamResults bachelorExamResults = null;
			boolean isPass = true;
			String msg = "";
			do{
				int iyear = 0;
				int iterm = 0;
				int iseatNo = 0;
				int iordinal = 0;
				int bachelorType = 0;
				ordinal = ExStringUtils.removeEnd(ordinal, ".0").trim();
				examNo = ExStringUtils.removeEnd(examNo, ".0").trim();
				seatNo = ExStringUtils.removeEnd(seatNo, ".0").trim();
				studyNo =  ExStringUtils.removeEnd(studyNo, ".0").trim();
				studentName = ExStringUtils.trim(studentName);
				unitName = ExStringUtils.trim(unitName);
				examResults = ExStringUtils.trim(examResults);
				yearName = ExStringUtils.removeEnd(yearName, ".0").trim();
				term = ExStringUtils.removeEnd(term, ".0").trim();
				try {
					iyear = Integer.parseInt(yearName);
					iterm = Integer.parseInt(term);
					iseatNo = Integer.parseInt(seatNo);
					iordinal = Integer.parseInt(ordinal);
				} catch (Exception e) {
					isPass = false;
					// TODO: handle exception
					returnMsg.append("<font color='red'>[学号:" + studyNo + "]</font>该学生的数据格式有误！").append("</br>");
                    continue;
				}
				
				//判断学号
            	if(studyNoList.size()>0 && studyNoList.contains(studyNo)){
            		isPass = false;
            		returnMsg.append("<font color='red'>[学号:" + studyNo + "]</font>该学生的学号有重复！").append("</br>");
                    continue;
            	}else{
            		studyNoList.add(studyNo);
            	}
            	//判断年度
            	if(yearList.size()>0 && !yearList.contains(iyear)){
            		isPass = false;
            		returnMsg.append("<font color='red'>[学号:" + studyNo + "]</font>该学生的年度与其他不同").append("</br>");
                    continue;
            	}else{
            		yearList.add(iyear);
            	}
            	//判断学期
            	if(termList.size()>0 && !termList.contains(iterm)){
            		isPass = false;
            		returnMsg.append("<font color='red'>[学号:" + studyNo + "]</font>该学生的学期与其他不同").append("</br>");
                    continue;
            	}else{
            		termList.add(iterm);
            	}
            	//判断序号
            	if(ordinalList.contains(iordinal)){
            		isPass = false;
            		returnMsg.append("<font color='red'>[学号:" + studyNo + "]</font>该学生的序号已存在").append("</br>");
                    continue;
            	}else{
            		ordinalList.add(iordinal);
            	}
            	//判断考场号和座位号
            	if(seatNumber.contains(examNo+"_"+iseatNo)){
            		isPass = false;
            		returnMsg.append("<font color='red'>[学号:" + studyNo + "]</font>该学生的考场号和座位号已存在").append("</br>");
                	continue;
            	}else{
            		seatNumber.add(examNo+"_"+iseatNo);
            	}
            	
				/*StudentInfo studentInfo = studentInfoService.findUniqueByProperty("studyNo", studyNo);
				if(studentInfo==null){
					isPass = false;
					msg = "<font color='red'>[学号：" + studyNo + "]</font>该学生不存在";
					continue;
				}*/
				for(int k=0; k<1; k++){
					YearInfo yearInfo = yearInfoService.findUniqueByProperty("firstYear", Long.valueOf(yearName));
					/*// 判断该学号和姓名是否正确
					if(!(studentInfo != null && studentInfo.getStudentName().equals(studentName))) {
						isPass = false;
						msg = "<font color='red'>[学号：" + studyNo + "]</font>该学生姓名不正确";
						continue;
					}
					
					// 判断教学点信息是否有误
					if(!studentInfo.getBranchSchool().getUnitName().equals(unitName)){
						isPass = false;
						msg = "<font color='red'>[学号：" + studyNo + "]</font>该学生的教学点信息不正确";
						continue;
					}*/
					
					
					//获取统考成绩单中允许的成绩分值项
					List<Dictionary> allowScore = CacheAppManager.getChildren("CodeAllowStateExamresultsImportSore");
					Map<String,String> alp = new HashMap<String, String>();
					for (Dictionary dic:allowScore) {
						alp.put(ExStringUtils.trim(dic.getDictName()), ExStringUtils.trim(dic.getDictValue()));
					}
					if(ExStringUtils.isNotBlank(examResults)){
						String regex = "^(\\d|[1-9]\\d|1[0-4][0-9]|150)(\\.\\d)?$";
						if (alp.containsKey(examResults)) {
							bachelorType = Integer.parseInt(alp.get(examResults));
						}else if(!Pattern.matches(regex, examResults)) {
							isPass = false;
							msg = "<font color='red'>[学号：" + studyNo + "]</font>该学生的成绩不合法";
							continue;
						}
					}
					// 序号为数字
					if(!ordinal.matches("[0-9]+")) {
						isPass = false;
						msg = "<font color='red'>[学号：" + studyNo + "]</font>该学生的序号必须为整数";
						continue;
					}
					// 座位号为数字
					if(!seatNo.matches("[0-9]+")) {
						isPass = false;
						msg = "<font color='red'>[学号：" + studyNo + "]</font>该学生的座位号必须为整数";
						continue;
					}
					// 年级为数字
					if(!yearName.matches("[0-9]+")) {
						isPass = false;
						msg = "<font color='red'>[学号：" + studyNo + "]</font>该学生的年级必须为整数";
						continue;
					}
					// 学期为1、2
					if(!("1".equals(term) || "2".equals(term))) {
						isPass = false;
						msg = "<font color='red'>[学号：" + studyNo + "]</font>该学生的学期必须为1或2";
						continue;
					}
					// 判断数据库中是否已存在该学生数据
					Map<String, Object> condition = new HashMap<String, Object>();
					condition.put("studentNO", studyNo);
					condition.put("yearInfo", yearInfo);
					condition.put("term", iterm);
					BachelorExamResults result3 = findUniqueBachelorExamInfo(condition);
					if(result3 != null){
						bachelorExamResults = result3;
					}else {
						bachelorExamResults = new BachelorExamResults();
					}
					condition.clear();
					condition.put("ordinal", iordinal);
					condition.put("yearInfo", yearInfo);
					condition.put("term", iterm);
					
					BachelorExamResults result1 = findUniqueBachelorExamInfo(condition);
					if(result1!=null && !result1.getStudentNO().equals(studyNo)){
						isPass = false;
						msg = "<font color='red'>[学号：" + studyNo + "]</font>该学生的序号已存在";
						continue;
					}
					condition.clear();
					condition.put("examNo", examNo);
					condition.put("seatNo", iseatNo);
					condition.put("yearInfo", yearInfo);
					condition.put("term", iterm);
					BachelorExamResults result2 = findUniqueBachelorExamInfo(condition);
					if(result2!=null  && !result2.getStudentNO().equals(studyNo)){
						isPass = false;
						msg = "<font color='red'>[学号：" + studyNo + "]</font>该学生的考场号及座位号已存在";
						continue;
					}
					
					/*//如果当前数据库中没有该学生信息
					if(result3==null) {
						if(result1!=null){
							isPass = false;
							msg = "<font color='red'>[学号：" + studyNo + "]</font>该学生的序号已存在";
							continue;
						}
						if(result2!=null){
							isPass = false;
							msg = "<font color='red'>[学号：" + studyNo + "]</font>该学生的考场号及座位号已存在";
							continue;
						}
						bachelorExamResults =  new BachelorExamResults();
						
					}else{	//当前数据库有该学生信息
						//判断修改的序号是否存在
						if(result1!=null && !result1.getStudentInfo().getStudyNo().equals(studyNo)){
							isPass = false;
							msg = "<font color='red'>[学号：" + studyNo + "]</font>该学生的序号已存在";
							continue;
						}
						//判断修改的考场号及座位号是否存在
						if(result2!=null && !result2.getStudentInfo().getStudyNo().equals(studyNo)){
							isPass = false;
							msg = "<font color='red'>[学号：" + studyNo + "]</font>该学生的考场号及座位号已存在";
							continue;
						}
						bachelorExamResults = result3;
					}*/
					
					bachelorExamResults.setOrdinal(iordinal);
	                bachelorExamResults.setExamNo(examNo);
	                bachelorExamResults.setSeatNo(iseatNo);
	                bachelorExamResults.setStudentNO(studyNo);
//	                bachelorExamResults.setStudentInfo(studentInfo);
	                bachelorExamResults.setStudentName(studentName);
//	                bachelorExamResults.setUnit(studentInfo.getBranchSchool());
	                bachelorExamResults.setUnitName(unitName);
	                bachelorExamResults.setBachelorType(bachelorType);
	                bachelorExamResults.setExamResults(examResults);
	                bachelorExamResults.setExamTime(time);
	                bachelorExamResults.setYearInfo(yearInfo);
	                bachelorExamResults.setTerm(iterm);
	                
				}
			}while(false);
			resultMap.put("isPass", isPass);
			resultMap.put("msg", msg);
			if(isPass) {
				//resultMap.put("studentInfo", studentInfo);
				resultMap.put("bachelorExamResults", bachelorExamResults);
			}
			return resultMap;
		}

		@Override
		public void updateBatch(String batch) {
			// TODO Auto-generated method stub
			SysConfiguration entity = sysConfigurationService.findUniqueByProperty("paramCode","bachelor.exam.batch");
			if(entity!=null){
				entity.setParamValue(batch);
			}
			sysConfigurationService.updateSysConfiguration(entity);
		}

		@Override
		public Map<String, Object> getTopExamByStudyNo(String studyNo) {
			// TODO Auto-generated method stub
			StringBuilder builder = new StringBuilder();
			builder.append("select be.studentno,be.studentname,be.examresults,nvl(d.dictvalue,be.examresults)score from edu_teach_bachelorexamresults be");
			builder.append(" left join hnjk_sys_dict d on d.dictcode like 'CodeAllowStateExamresultsImportSore%' and d.dictname=be.examresults");
			builder.append(" where be.isdeleted=0 and be.studentno=?");
			builder.append(" order by nvl(d.dictvalue,nvl(be.examresults,0)) desc");
			try {
				List<Map<String, Object>> resultsList = baseSupportJdbcDao.getBaseJdbcTemplate().findForList(builder.toString(), new Object[]{studyNo});
				if(resultsList!=null && resultsList.size()>0){
					return resultsList.get(0);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
}
