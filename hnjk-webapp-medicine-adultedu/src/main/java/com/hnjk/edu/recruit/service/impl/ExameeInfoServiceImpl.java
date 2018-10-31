package com.hnjk.edu.recruit.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.FileUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.NumberUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.hibernate.ExGeneralHibernateDao;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.model.StudentBaseInfo;
import com.hnjk.edu.basedata.service.IClassicService;
import com.hnjk.edu.finance.service.IStudentPaymentService;
import com.hnjk.edu.recruit.helper.ExameeInfoConfig;
import com.hnjk.edu.recruit.helper.ExameeInfoField;
import com.hnjk.edu.recruit.helper.ExameeInfoHelper;
import com.hnjk.edu.recruit.model.EnrolleeInfo;
import com.hnjk.edu.recruit.model.ExameeInfo;
import com.hnjk.edu.recruit.model.Predistribution;
import com.hnjk.edu.recruit.model.RecruitMajor;
import com.hnjk.edu.recruit.model.RecruitPlan;
import com.hnjk.edu.recruit.service.IEnrolleeInfoService;
import com.hnjk.edu.recruit.service.IExameeInfoService;
import com.hnjk.edu.recruit.service.IRecruitMajorService;
import com.hnjk.edu.recruit.service.IRecruitPlanService;
import com.hnjk.edu.recruit.vo.ExameeInfoCancelPrintVo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;

/**
 * 招生管理服务接口实现. <code>RecruitManageServiceImpl</code>
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-8-30 下午02:15:04
 * @see
 * @version 1.0
 */
@Transactional
@Service("exameeInfoService")
public class ExameeInfoServiceImpl extends BaseServiceImpl<ExameeInfo>
		implements IExameeInfoService {
	/** dbf数据检查通过数据列表 */
	public static final String SUCCESSLIST = "SUCCESSLIST";
	/** dbf数据检查错误数据列表 */
	public static final String ERRORLIST = "ERRORLIST";
	/** 错误日志记录key */
	public static final String FLAG = "message";
	/** dbf数据字段映射model类型EnrolleeInfo */
	public static final String ENROLLEEINFO = "EnrolleeInfo";
	/** dbf数据字段映射model类型StudentBaseInfo */
	public static final String STUDENTBASEINFO = "StudentBaseInfo";
	/** 准考证号字段 */
	public static final String ZKZH = "ZKZH";
	/** 专业编码字段 */
	public static final String ZYDM = "ZYDM";
	/** 考生号字段 */
	public static final String KSH = "KSH";
	/** 成绩字段前缀 */
	public static final String EXAMINFOSCORE_PREFIX = "CJX";
	public static final String EXAMINFOWISH_PREFIX = "ZYDH";
	/** 考生相片前缀 */
	public static final String PHOTOF = "F";
	public static final String PHOTOZ = "Z";
	/** 院校代码 */
	public static final String SCHOOLCODE = "10571";

	public static final String UNDISTRIBUTED = "未分配";

	@Autowired
	@Qualifier("recruitPlanService")
	private IRecruitPlanService recruitPlanService;

	@Autowired
	@Qualifier("enrolleeInfoService")
	private IEnrolleeInfoService enrolleeInfoService;

	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;

	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;

	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;

	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;// 学籍服务

	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;

	@Autowired
	@Qualifier("studentPaymentService")
	private IStudentPaymentService studentPaymentService;

	@Autowired
	@Qualifier("recruitMajorService")
	private IRecruitMajorService recruitMajorService;

	/**
	 * 审批招生专业
	 * 
	 * @param recruitPlanId
	 * @param filePath
	 */
	@Override
	public int importRecruitMajor(String recruitPlanId, String filePath)
			throws ServiceException {
		int passSize = 0;
		RecruitPlan recruitPlan = recruitPlanService.get(recruitPlanId);
		List<Map<String, Object>> majorList = ExameeInfoHelper
				.dbfToListMap(filePath);// 从dbf中读取招生专业数据
		Set<String> majorCodeSet = new HashSet<String>();// 专业编码集合
		for (Map<String, Object> map : majorList) {
			majorCodeSet.add(map.get(ZYDM).toString());
		}
		for (RecruitMajor recruitMajor : recruitPlan.getRecruitMajor()) {
			if (majorCodeSet.contains(recruitMajor.getRecruitMajorCode())) {
				recruitMajor.setStatus(1L);// 审批通过
				passSize++;
			} else {
				recruitMajor.setStatus(0L);// 审批不通过
			}
		}
		recruitPlanService.update(recruitPlan);
		logger.info("成功审批 " + passSize + " 条招生专业");
		return passSize;
	}

	/**
	 * 导入考生信息
	 * 
	 * @param recruitPlanId
	 *            导入批次
	 * @param filePath
	 *            dbf文件
	 * @return 返回信息
	 * @throws ServiceException
	 */
	@Override
	public Map<String, Object> importExameeInfo(String recruitPlanId,
												String attachId, String ip) throws ServiceException {
		Attachs attach = attachsService.get(attachId);// 导入文件附件
		String fileName = attach.getAttName();// //导入文件名称
		String filePath = attach.getSerPath() + File.separator
				+ attach.getSerName();// //导入文件路径
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 1.读取dbf数据，转换为Map列表
		List<Map<String, Object>> dbfMapList = ExameeInfoHelper
				.dbfToListMap(filePath);
		if (ExCollectionUtils.isNotEmpty(dbfMapList)) {
			RecruitPlan recruitPlan = recruitPlanService.get(recruitPlanId);// 招生批次
			Map<String, RecruitMajor> recruitMajorMap = getRecruitMajorMap(recruitPlan);// 审批通过的招生专业
			ExameeInfoConfig config = ExameeInfoHelper.getExameeInfoConfig();// 字段检查条件配置
			// 导入数据是否需要和国家专业去转换 判断是否设置数据字典admission.replace.majorcode
			List<Dictionary> listdict = dictionaryService.findByHql(" from "
					+ Dictionary.class.getSimpleName()
					+ " where dictCode = 'admission.replace.majorcode' ");
			Map<String, Object> dictOrResCheckFieldMap = getDictOrResCheckFieldMap(
					recruitPlan, listdict);// 字典值集合
			// 2.检查数据合法性:空值检查，字典值检查，外键数据检查等
			Map<String, Object> validateMap = validateExameeInfo(dbfMapList,
					config, dictOrResCheckFieldMap);// 检查数据合法性
			List<Map<String, Object>> successList = (List<Map<String, Object>>) validateMap
					.get(SUCCESSLIST);// 检查通过的数据
			List<Map<String, Object>> errorList = (List<Map<String, Object>>) validateMap
					.get(ERRORLIST);// 检查不通过的数据
			// 3.把检查通过的数据转为v3数据,并保存转换后的考生信息列表
			Map<String, EnrolleeInfo> enrolleeInfoMap = getEnrolleeInfoMap(recruitPlanId);// 已导入的EnrolleeInfo数据
			List<OrgUnit> noorgUnits = orgUnitService.findByHql(" from "
					+ OrgUnit.class.getSimpleName()
					+ " where isDeleted = 0 and trim(unitName) = '未分配' ");
			List<OrgUnit> allorgUnits = orgUnitService
					.findByHql(" from "
							+ OrgUnit.class.getSimpleName()
							+ " where isDeleted = 0 and unitType='brSchool' order by unitCode asc ");
			List<Dictionary> listdictClassic = dictionaryService
					.findByHql(" from "
							+ Dictionary.class.getSimpleName()
							+ " where dictCode = 'studyno.replace.classiccode' ");
			List<EnrolleeInfo> enrolleeInfoList = changeExameeInfoToEnrolleeInfo(
					successList, config, enrolleeInfoMap, recruitMajorMap,
					resultMap, recruitPlanId, allorgUnits, noorgUnits,
					listdictClassic, listdict);

			enrolleeInfoService.batchSaveOrUpdateEnrolleeInfo(enrolleeInfoList);

			// 再生成准学号

			// //生成缴费信息
			// studentPaymentService.createStudentPayment(enrolleeInfoList);
			// 4.剔除已经注册生成学籍的学生
			Set<Map<String, Object>> deleteSet = new HashSet<Map<String, Object>>();
			for (Map<String, Object> map : successList) {
				if (map.containsKey(FLAG)) {
					deleteSet.add(map);
				}
			}
			successList.removeAll(deleteSet);
			errorList.addAll(deleteSet);

			// 5.保存ExameeInfo考生信息
			Map<String, String> exameeInfoMap = getExameeInfoMap(recruitPlanId);// 查询批次下已导入数据
			saveOrUpdateExameeInfo(successList, exameeInfoMap, config,
					recruitPlanId);// 更新保存考生信息

			// 6.记录导入日志
			String userName = SpringSecurityHelper.getCurrentUserName();
			String currentDate = "";
			try {
				currentDate = ExDateUtils.formatDateStr(new Date(),
						ExDateUtils.PATTREN_DATE_TIME);
			} catch (ParseException e) {
			}
			List<String> errorMessageList = new ArrayList<String>();
			for (Map<String, Object> errorMap : errorList) {
				String failMessage = fileName + "/ZKZH:" + errorMap.get(ZKZH)
						+ "(导入失败，原因: " + errorMap.get(FLAG) + ").";
				String log = ExStringUtils.join(new String[] { currentDate, ip,
						"", userName, "导入录取数据", failMessage }, ",");
				errorMessageList.add(failMessage);
				logger.info(log);// 记录导入失败日志
			}
			if (resultMap.get("recruitMajorErrors") != null) {
				List<Map<String, String>> errors = (List<Map<String, String>>) resultMap
						.get("recruitMajorErrors");
				for (Map<String, String> error : errors) {
					String failMessage = fileName + "/ZKZH:" + error.get(ZKZH)
							+ "(导入失败，原因: 专业编码为" + error.get("majorCode")
							+ "的专业未审核通过或者不存在).";
					String log = ExStringUtils.join(new String[] { currentDate,
							ip, "", userName, "导入录取数据", failMessage }, ",");
					errorMessageList.add(failMessage);
					logger.info(log);// 记录导入失败日志
				}
			}
			String message = successList.size() > 0 ? ("<span style='color:green;'>导入考生信息成功记录为"
					+ successList.size() + "条.</span>")
					: "";
			resultMap.put("successCount", successList.size());
			if (ExCollectionUtils.isNotEmpty(errorMessageList)) {
				resultMap.put("errorMessageList", errorMessageList);
				resultMap.put("errorCount", errorMessageList.size());
				message += "<br/><span style='color:red;'>导入失败记录为"
						+ errorMessageList.size() + "条.</span>";
			}
			resultMap.put("message", message);
		}
		return resultMap;
	}

	/**
	 * 获取审批通过的招生专业
	 * 
	 * @param recruitPlan
	 *            批次
	 * @return
	 */
	public Map<String, RecruitMajor> getRecruitMajorMap(RecruitPlan recruitPlan) {
		Map<String, RecruitMajor> recruitMajorMap = new HashMap<String, RecruitMajor>();
		for (RecruitMajor major : recruitPlan.getRecruitMajor()) {
			if (major.getStatus() != null && major.getStatus() == 1L
					&& major.getIsDeleted() == 0) {// 审批通过的专业
			// recruitMajorMap.put(major.getRecruitMajorCode(), major);
				recruitMajorMap.put(major.getResourceid(), major);
			}
		}
		return recruitMajorMap;
	}

	/**
	 * 获取字段值或编码值集合
	 * 
	 * @return key:字典编码_字典值(外键名称_外键编码),value:字典值(外键id)
	 */
	public Map<String, Object> getDictOrResCheckFieldMap(
			RecruitPlan recruitPlan, List<Dictionary> listdict) {
		List<String> dictList = Arrays.asList(new String[] {
				"CodeForeignLanguage", "CodeCharacteristic", "CodeRecruitType",
				"CodeSex", "CodeNation", "CodePolitics", "CodeMajorAttribute",
				"CodeEducationalLevel", "CodeMajorCatogery" });
		Map<String, Object> dictOrResCheckFieldMap = dictionaryService
				.getDictionByMap(dictList, true,
						IDictionaryService.PREKEY_TYPE_BYCODE);
		// 专业
		for (RecruitMajor recruitMajor : recruitPlan.getRecruitMajor()) {
			dictOrResCheckFieldMap.put(
					"RecruitMajor_" + recruitMajor.getRecruitMajorCode(),
					recruitMajor.getMajor().getMajorName());
		}
		// 层次
		List<Classic> classicList = classicService.findByCriteria(Restrictions
				.eq("isDeleted", 0));
		if (null != listdict && listdict.size() > 0) { // 需要替换
			for (Classic classic : classicList) {
				String rpcode = JstlCustomFunction
						.dictionaryCode2Name("admission.replace.majorcode",
								classic.getClassicCode());
				if (ExStringUtils.isNotBlank(rpcode)) {
					dictOrResCheckFieldMap.put("Classic_" + rpcode,
							classic.getClassicName());
				} else {
					dictOrResCheckFieldMap.put(
							"Classic_" + classic.getClassicCode(),
							classic.getClassicName());
				}
			}
		} else {
			for (Classic classic : classicList) {
				dictOrResCheckFieldMap.put(
						"Classic_" + classic.getClassicCode(),
						classic.getClassicName());
			}
		}

		return dictOrResCheckFieldMap;
	}

	/**
	 * 检查dbf数据合法性
	 * 
	 * @param dbfMapList
	 * @return
	 */
	public Map<String, Object> validateExameeInfo(
			List<Map<String, Object>> dbfMapList, ExameeInfoConfig config,
			Map<String, Object> dictOrResCheckFieldMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> successList = new ArrayList<Map<String, Object>>();// 检查通过的数据
		List<Map<String, Object>> errorList = new ArrayList<Map<String, Object>>();// 检查不通过的数据

		Map<String, ExameeInfoField> checkConfigMap = config
				.getAllFieldListToMap();
		if (ExCollectionUtils.isNotEmpty(dbfMapList)) {
			for (Map<String, Object> map : dbfMapList) {
				Map<String, Object> displayNameMap = new HashMap<String, Object>();
				for (Map.Entry<String, Object> entry : map.entrySet()) {// 对每个字段值进行检查
					if (checkConfigMap.containsKey(entry.getKey())) {// 当前字段为检查字段
						ExameeInfoField checkMap = checkConfigMap.get(entry
								.getKey());// 检查条件
						// 空值检查,如果是必选项但值为空，转入错误数据列表
						if (Constants.BOOLEAN_NO.equals(checkMap.getIsNull())
								&& (entry.getValue() == null || ExStringUtils
										.isBlank(entry.getValue().toString()))) {
							if ("WYYZDM".equals(entry.getKey())
									|| "BYRQ".equals(entry.getKey())
									|| "BYXX".equals(entry.getKey())) { // 为空也可导入

							} else {
								map.put(FLAG, checkMap.getTitleName() + "("
										+ entry.getKey() + ")字段值为空");// 错误原因日志
								break;
							}
						}
						// 字典值检查,检查字典值是否存在:
						if (ExStringUtils.isNotBlank(checkMap
								.getCodeTableName())
								&& entry.getValue() != null
								&& ExStringUtils.isNotBlank(entry.getValue()
										.toString())
								&& !dictOrResCheckFieldMap.containsKey(checkMap
										.getCodeTableName()
										+ "_"
										+ entry.getValue())) {
							map.put(FLAG,
									checkMap.getTitleName() + "("
											+ entry.getKey() + ")字段字典值不存在,"
											+ checkMap.getCodeTableName()
											+ ": " + entry.getValue());
							break;
						}
						// 外键编码数据检查:
						if (ExStringUtils.isNotBlank(checkMap
								.getReferenceName())
								&& entry.getValue() != null
								&& ExStringUtils.isNotBlank(entry.getValue()
										.toString())
								&& !dictOrResCheckFieldMap.containsKey(checkMap
										.getReferenceName()
										+ "_"
										+ entry.getValue())) {
							map.put(FLAG,
									checkMap.getTitleName() + "("
											+ entry.getKey() + ")字段关联数据不存在: "
											+ entry.getValue());
							break;
						}
						// 加入专业、层次显示名称
						if (ExStringUtils.isNotBlank(checkMap
								.getReferenceName())
								&& ExStringUtils.isNotBlank(checkMap
										.getDisplayName())) {// 存在外键关联数据
							displayNameMap
									.put(checkMap.getDisplayName(),
											dictOrResCheckFieldMap.get(checkMap
													.getReferenceName()
													+ "_"
													+ (entry.getValue() != null ? entry
															.getValue()
															.toString() : "")));// 专业、层次等获取显示名称
						}
					}
				}
				if (displayNameMap != null) {
					map.putAll(displayNameMap);
				}
				if (map.containsKey(FLAG)) {
					errorList.add(map);// 数据不合法
				} else {
					successList.add(map);// 数据合法性检查通过
				}
			}
		}

		resultMap.put(SUCCESSLIST, successList);
		resultMap.put(ERRORLIST, errorList);
		return resultMap;
	}

	/**
	 * 获取已导入报名信息
	 * 
	 * @param recruitPlanId
	 *            批次id
	 * @return
	 */
	public Map<String, EnrolleeInfo> getEnrolleeInfoMap(String recruitPlanId) {
		String uniqueId = CacheAppManager.getSysConfigurationByCode(
				"exameeInfo.uniqueId").getParamValue();// 导入报名信息唯一标示
		// key:准考证号,value:EnrolleeInfo对象
		Map<String, EnrolleeInfo> enrolleeInfoMap = new HashMap<String, EnrolleeInfo>();
		String hql = " from " + EnrolleeInfo.class.getSimpleName()
				+ " where isDeleted=? ";// and
										// recruitMajor.recruitPlan.resourceid=?
										// ";
		List<EnrolleeInfo> enrolleeInfoList = enrolleeInfoService.findByHql(
				hql, 0);// , recruitPlanId);
		for (EnrolleeInfo enrolleeInfo : enrolleeInfoList) {
			if ("0".equals(uniqueId)) {// 准考证号
				enrolleeInfoMap.put(enrolleeInfo.getExamCertificateNo(),
						enrolleeInfo);
			} else if ("1".equals(uniqueId)) {// 考生号
				enrolleeInfoMap.put(enrolleeInfo.getEnrolleeCode(),
						enrolleeInfo);
			}
		}
		return enrolleeInfoMap;
	}

	/**
	 * 转换dbf数据为v3数据
	 * 
	 * @param dbfMapList
	 * @param enrolleeInfoMap
	 *            当年已导入数据,key:准考证号,value:EnrolleeInfo
	 * @param recruitMajorMap
	 *            招生专业,key:招生专业编码,value:招生专业
	 * @return
	 */
	public List<EnrolleeInfo> changeExameeInfoToEnrolleeInfo(
			List<Map<String, Object>> dbfMapList, ExameeInfoConfig config,
			Map<String, EnrolleeInfo> enrolleeInfoMap,
			Map<String, RecruitMajor> recruitMajorMap,
			Map<String, Object> resultMap, String recruitPlanId,
			List<OrgUnit> allorgUnits, List<OrgUnit> noorgUnits,
			List<Dictionary> listdict, List<Dictionary> classicDic) {
		List<EnrolleeInfo> enrolleeInfoList = new ArrayList<EnrolleeInfo>();
		// 由于根据导入的教学站获得学号前缀，故而，生成学号的位置变化了
		// Map<String, String> maxStudentNoSuffixMap =
		// getMaxStudentNoSuffixMap(enrolleeInfoMap.values());//学生学号当前流水号，key:前缀,
		// value:流水号
		User user = SpringSecurityHelper.getCurrentUser();
		String userId = user.getResourceid();
		String userName = user.getCnName();
		Date currentDate = new Date();
		// 缺省学习中心
		OrgUnit defaultunit = null;
		try {
			String defaultunitId = JstlCustomFunction.getSysConfigurationValue(
					"defaultUnitId", "server");
			defaultunit = orgUnitService.get(defaultunitId);
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		if (null == defaultunit) {
			if (allorgUnits.size() > 0) {
				defaultunit = allorgUnits.get(0);
			}
		}
		List<Map<String, String>> errors = new ArrayList<Map<String, String>>();// 不通过的数据
		List<Map<String, Object>> errorList = new ArrayList<Map<String, Object>>();// 不通过的数据
		String uniqueId = CacheAppManager.getSysConfigurationByCode(
				"exameeInfo.uniqueId").getParamValue();// 导入报名信息唯一标示

		for (Map<String, Object> info : dbfMapList) {
			EnrolleeInfo ei = null;
			StudentBaseInfo bi = null;

			// 是否需要转换层次代码
			if (null != classicDic && classicDic.size() > 0) {
				String ccdm = JstlCustomFunction.dictionaryCode2Value(
						"admission.replace.majorcode", info.get("CCDM") + "");
				if (ExStringUtils.isNotBlank(ccdm)) {
					info.put("CCDM", ccdm);
				}
			}

			if ("0".equals(uniqueId)
					&& enrolleeInfoMap.containsKey(info.get(ZKZH))) {// 当前准考证号存在已导入数据
				ei = enrolleeInfoMap.get(info.get(ZKZH));
				if (Constants.BOOLEAN_YES.equals(ei.getRegistorFlag())) {// 已经注册生成学籍,不导入,归入导入失败列表
					info.put(FLAG, "ZKZH:(" + info.get(ZKZH) + ")已经注册生成学籍,不能导入");
					continue;
				}
				bi = ei.getStudentBaseInfo();
			} else if ("1".equals(uniqueId)
					&& enrolleeInfoMap.containsKey(info.get(KSH))) {// 当前考生号存在已导入数据
				ei = enrolleeInfoMap.get(info.get(KSH));
				if (Constants.BOOLEAN_YES.equals(ei.getRegistorFlag())) {// 已经注册生成学籍,不导入,归入导入失败列表
					info.put(FLAG, "KSH:(" + info.get(KSH) + ")已经注册生成学籍,不能导入");
					continue;
				}
				bi = ei.getStudentBaseInfo();
			} else {// 导入的是新数据
				ei = new EnrolleeInfo();
				bi = new StudentBaseInfo();
				ei.setStudentBaseInfo(bi);
			}
			Map<String, ExameeInfoField> checkConfigMap = config
					.getAllFieldListToMap();
			for (Map.Entry<String, ExameeInfoField> cfgMap : checkConfigMap
					.entrySet()) {
				Object obj = null;
				String fieldName = null;
				ExameeInfoField cfg = cfgMap.getValue();
				if (ENROLLEEINFO.equals(cfg.getModelType())) {// EnrolleeInfo的映射字段不为空
					obj = ei;
				} else if (STUDENTBASEINFO.equals(cfg.getModelType())) {// StudentBaseInfo的映射字段不为空
					obj = bi;
				}
				fieldName = ExStringUtils.trimToEmpty(cfg.getMappingFiled());
				if (obj != null && ExStringUtils.isNotBlank(fieldName)) {
					try {
						Object value = info.get(cfgMap.getKey());
						if (value != null
								&& ("totalPoint".equals(fieldName) || "originalPoint"
										.equals(fieldName))) {// 原始总分或总分取整
							value = Double.valueOf(value.toString())
									.longValue();
						}

						if (value != null && "branchSchool".equals(fieldName)) {
							List<OrgUnit> orgUnits = orgUnitService
									.findByHql(
											" from "
													+ OrgUnit.class
															.getSimpleName()
													+ " where isDeleted = 0 and trim(unitName) = ? ",
											value);
							if (orgUnits.size() > 0) {
								value = orgUnits.get(0);
							} else {
								String unitId = JstlCustomFunction
										.getSysConfigurationValue(
												"defaultUnitId", "server");
								OrgUnit unit = orgUnitService.get(unitId);
								if (null != unit) {
									value = unit;
								} else {
									orgUnits = orgUnitService
											.findByHql(" from "
													+ OrgUnit.class
															.getSimpleName()
													+ " where isDeleted = 0 and unitType='brSchool' order by unitCode asc ");
									if (orgUnits.size() > 0) {
										value = orgUnits.get(0);
									} else {
										value = null;
									}
								}
							}
						} else if (value == null
								&& "branchSchool".equals(fieldName)) {
							value = defaultunit;
						}

						if ("graduateDate".equals(fieldName)
								&& ExStringUtils.isBlank(value + "")) {
							continue;
						}
						ExBeanUtils.setFieldValue(obj, fieldName, value);// 设置字段值
					} catch (Exception e) {
						System.out.println(fieldName);
						throw new ServiceException("设置" + fieldName + "字段值错误",
								e.fillInStackTrace());
					}
				}
			}
			RecruitMajor recruitMajor = null;
			for (Map.Entry<String, RecruitMajor> major : recruitMajorMap
					.entrySet()) {
				RecruitMajor mj = major.getValue();
				Classic classic = mj.getClassic();
				String lqzy = info.get("LQZY") + "";
				String ccdm = info.get("CCDM") + "";
				// System.out.println("info信息：["+info.get("LQZY")+"/"+info.get("CCDM")+"】major信息：【"+mj.getRecruitMajorCode()+"/"+classic.getClassicCode()+"】");
				if (lqzy != null
						&& (lqzy.trim()).equals(mj.getRecruitMajorCode())
						&& ccdm != null
						&& (ccdm.trim()).equals(classic.getClassicCode())) {
					recruitMajor = major.getValue();
					break;
				}
			}
			// RecruitMajor recruitMajor =
			// recruitMajorMap.get(ei.getRecruitMajorCode());
			if (recruitMajor == null) {// 招生专业不存在
				Map<String, String> error = new HashMap<String, String>();
				error.put("majorCode", ei.getRecruitMajorCode());
				error.put(ZKZH, info.get(ZKZH).toString());
				errors.add(error);
				errorList.add(info);
				continue;
			}
			info.put("LQZYMC", recruitMajor.getMajor().getMajorName());
			ei.setRecruitMajor(recruitMajor);// 招生专业
			ei.setGrade(recruitMajor.getRecruitPlan().getGrade());// 年级
			ei.setTeachingType(recruitMajor.getTeachingType());// 学习形式
			ei.setStutyMode(recruitMajor.getTeachingType());// 学习形式
			// ei.setBranchSchool(recruitMajor.getBrSchool());//学习中心
			int count = 0;
			RecruitMajor m = null;
			for (Map.Entry<String, RecruitMajor> major : recruitMajorMap
					.entrySet()) {
				RecruitMajor mj = major.getValue();
				Classic classic = mj.getClassic();
				String lqzy = info.get("LQZY") + "";
				String ccdm = info.get("CCDM") + "";
				// System.out.println("info信息：["+info.get("LQZY")+"/"+info.get("CCDM")+"】major信息：【"+mj.getRecruitMajorCode()+"/"+classic.getClassicCode()+"】");
				if (lqzy != null
						&& (lqzy.trim()).equals(mj.getRecruitMajorCode())
						&& ccdm != null
						&& (ccdm.trim()).equals(classic.getClassicCode())) {
					m = major.getValue();
					count++;
				}
			}
			int c = 0;
			if (info.get("LQZY") != null && info.get("CCDM") != null) {
				String sql = "select count(m.resourceid) c "
						+ " from EDU_RECRUIT_MAJOR m join EDU_BASE_CLASSIC c on m.classic=c.resourceid "
						+ " where m.recruitplanid=:recruitplanid "
						+ " and c.classiccode=:classiccode "
						+ " and m.recruitmajorcode=:recruitmajorcode "
						+ " and m.isdeleted=0 and m.status=1 "
						+ " group by m.brschoolid";
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("recruitplanid", recruitPlanId);
				condition.put("classiccode", info.get("CCDM"));
				condition.put("recruitmajorcode", info.get("LQZY"));
				try {
					List<Map<String, Object>> returnList = baseSupportJdbcDao
							.getBaseJdbcTemplate().findForListMap(sql,
									condition);
					c = returnList.size();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (count >= 2 || count == 0 || m == null || c >= 2) {
				if (noorgUnits.size() > 0) {
					ei.setBranchSchool(noorgUnits.get(0));
				}
			} else {
				ei.setBranchSchool(m.getBrSchool());
			}
			// 报名资格
			ei.setSignupFlag(Constants.BOOLEAN_YES);
			ei.setSignupDate(currentDate);
			// 免试资格
			ei.setIsApplyNoexam(Constants.BOOLEAN_YES);
			ei.setNoExamFlag(Constants.BOOLEAN_YES);
			ei.setNoexamCheckMan(userName);
			ei.setNoexamCheckManId(userId);
			ei.setNoexamCheckDate(currentDate);
			ei.setNoExamCheckMemo("");
			// 入学资格
			ei.setEntranceflag(Constants.BOOLEAN_YES);
			ei.setEntranceCheckMan(userName);
			ei.setEntranceCheckManId(userId);
			ei.setEntranceCheckDate(currentDate);
			// 录取
			ei.setIsMatriculate(Constants.BOOLEAN_YES);
			// 生成学号
			// 由于根据导入的教学站获得学号前缀，故而，生成学号的位置变化了
			// if(ExStringUtils.isBlank(ei.getMatriculateNoticeNo())){
			// ei.setMatriculateNoticeNo(genStudentNo(ei,
			// maxStudentNoSuffixMap));
			// }
			if (ExStringUtils
					.isNotBlank(ExStringUtils.trim(info.get("XH") + ""))) { // 如果传入有学号
																			// 则不必生产学号了
				ei.setMatriculateNoticeNo(ExStringUtils.trim(info.get("XH")
						+ ""));
			}
			// CJX01应为原始分，配置在exameeinfo-config.xml文件中
			// ei.setOriginalPoint(ei.getTotalPoint());

			bi.setCertType("idcard");
			bi.setMobile(bi.getContactPhone());
			bi.setMarriage("10");
			if (null == ei.getMatriculateNoticeNo()
					|| "null".equals(ei.getMatriculateNoticeNo())) {
				ei.setMatriculateNoticeNo("");
			}

			ei.setCCMD(info.get("CCDM") + ""); // 层次代码
			ei.setKSH(info.get("KSH") + "");// 考生号
			// ZF是入学总分，应在exameeinfo-config.xml文件中设置，
			// 之前是CJX01作为入学总分，据各学校反馈入学总分应是ZF
			/*
			 * String zf = null != info.get("ZF") ? info.get("ZF")+"" :
			 * "0";//总分，TODO:以后这块可以优化 boolean iszfok = true; Long zfd = 0L; try
			 * { zfd = Math.round(Double.parseDouble(zf)); } catch (Exception e)
			 * { iszfok = false; } if(iszfok){ ei.setTotalPoint(zfd); }
			 */
			enrolleeInfoList.add(ei);

			info.put("XZ", recruitMajor.getStudyperiod());// 学制
			info.put("XXXSDM", recruitMajor.getTeachingType());// 学习形式

		}
		if (errors.size() > 0) {
			resultMap.put("recruitMajorErrors", errors);
			dbfMapList.removeAll(errorList);
		}
		// Map<String, String> maxStudentNoSuffixMap =
		// getMaxStudentNoSuffixMap(enrolleeInfoList);//学生学号当前流水号，key:前缀,
		// value:流水号
		List<Map<String, Object>> maxStudentNoSuffixMap_list = null;
		// List<Map<String, Object>> maxStudentNoSuffixMap_list =
		// enrolleeInfoService.getMaxStudyNoPrefixMap(enrolleeInfoList);//学生学号当前流水号，key:前缀,
		// value:流水号
		// 准学号生成
		if ("1".equals(CacheAppManager
				.getSysConfigurationByCode("studentinfo.studynorule")
				.getParamValue())) {// 教科
			maxStudentNoSuffixMap_list = enrolleeInfoService
					.getMaxStudyNoPrefixMap(enrolleeInfoList);// 学生学号当前流水号，key:前缀,
																// value:流水号
		} else if ("2".equals(CacheAppManager
				.getSysConfigurationByCode("studentinfo.studynorule")
				.getParamValue())) {// 广东医
			maxStudentNoSuffixMap_list = enrolleeInfoService
					.genMaxStudyNoPrefixMap(enrolleeInfoList);// 学生学号当前流水号，key:前缀,
																// value:流水号
		} else if ("3".equals(CacheAppManager
				.getSysConfigurationByCode("studentinfo.studynorule")
				.getParamValue())) {// 广大
			maxStudentNoSuffixMap_list = enrolleeInfoService
					.genMaxStudyNoPrefixMap2(enrolleeInfoList);// 学生学号当前流水号，key:前缀,
																// value:流水号
		} else if ("4".equals(CacheAppManager
				.getSysConfigurationByCode("studentinfo.studynorule")
				.getParamValue())) { // 安徽医学院学号生成
			maxStudentNoSuffixMap_list = enrolleeInfoService
					.genMaxStudyNoPrefixAHY();// 学生学号当前流水号，key:前缀, value:流水号
		} else if ("7".equals(CacheAppManager
				.getSysConfigurationByCode("studentinfo.studynorule")
				.getParamValue())) {// 右江医
			maxStudentNoSuffixMap_list = enrolleeInfoService
					.getMaxStudyNoPrefixMapRegistered(1, 7, 8, 3);
		} else {// 默认，学号是教科
			maxStudentNoSuffixMap_list = enrolleeInfoService
					.getMaxStudyNoPrefixMap(enrolleeInfoList);// 学生学号当前流水号，key:前缀,
																// value:流水号
		}
		Map<String, String> maxStudentNoSuffixMap = new HashMap<String, String>(
				0);
		for (Map<String, Object> map : maxStudentNoSuffixMap_list) {
			maxStudentNoSuffixMap.put(map.get("p").toString(), map.get("s")
					.toString());
		}
		if (null != CacheAppManager
				.getSysConfigurationByCode("studentinfo.studynorule")
				&& "4".equals(CacheAppManager
				.getSysConfigurationByCode("studentinfo.studynorule")
				.getParamValue())) {
			enrolleeInfoList = paixu(enrolleeInfoList);
		}
		for (EnrolleeInfo enrolleeInfo : enrolleeInfoList) {
			// if(ExStringUtils.isBlank(enrolleeInfo.getMatriculateNoticeNo())){//如果还未有学号则产生
			String studyNo = "";
			if ("1".equals(CacheAppManager
					.getSysConfigurationByCode("studentinfo.studynorule")
					.getParamValue())) {// 教科
				studyNo = getStudentNo(enrolleeInfo, maxStudentNoSuffixMap);
			} else if ("2".equals(CacheAppManager
					.getSysConfigurationByCode("studentinfo.studynorule")
					.getParamValue())) {// 广东医
				studyNo = genStudentNo(enrolleeInfo, maxStudentNoSuffixMap);
			} else if ("3".equals(CacheAppManager
					.getSysConfigurationByCode("studentinfo.studynorule")
					.getParamValue())) {// 广大
				studyNo = genStudentNo2(enrolleeInfo, maxStudentNoSuffixMap);
			} else if ("4".equals(CacheAppManager
					.getSysConfigurationByCode("studentinfo.studynorule")
					.getParamValue())) { // 安徽医学院
				// List<Dictionary> listdict =
				// dictionaryService.findByHql(" from "+Dictionary.class.getSimpleName()+" where dictCode = 'studyno.replace.classiccode' ");
				studyNo = genStudentAHY(enrolleeInfo, maxStudentNoSuffixMap,
						listdict);
			} else if ("7".equals(CacheAppManager
					.getSysConfigurationByCode("studentinfo.studynorule")
					.getParamValue())) {// 右江医
				studyNo = genStudentYJY(enrolleeInfo, maxStudentNoSuffixMap,
						listdict);
			}
			enrolleeInfo.setMatriculateNoticeNo(studyNo);
			// }
		}
		return enrolleeInfoList;
	}

	// 学生排序 总分 考生号
	private List<EnrolleeInfo> paixu(List<EnrolleeInfo> enrolleeInfoList) {

		Collections.sort(enrolleeInfoList, new Comparator<EnrolleeInfo>() {
			@Override
			public int compare(EnrolleeInfo a, EnrolleeInfo b) {
				int diff = 0;
				if (a.getTotalPoint() != null && b.getTotalPoint() != null) {
					diff = b.getTotalPoint().compareTo(a.getTotalPoint());
					if (diff == 0 && null != a.getExamCertificateNo()
							&& null != b.getExamCertificateNo()) {
						return a.getExamCertificateNo().compareTo(
								b.getExamCertificateNo());
					}
				}
				return diff;
			}
		});

		// for(int i = 0; i < enrolleeInfoList.size()-1 ; i++){
		// for(int ii = 0; ii < enrolleeInfoList.size()-1-i; ii++){
		// EnrolleeInfo ei = enrolleeInfoList.get(i);
		// EnrolleeInfo eii = enrolleeInfoList.get(ii);
		// Long zfi = null != ei.getTotalPoint() ? ei.getTotalPoint() : 0L;
		// Long zfii = null != eii.getTotalPoint() ? eii.getTotalPoint() : 0L;
		// if(zfi < zfii){
		// enrolleeInfoList.set(i,eii);
		// enrolleeInfoList.set(ii,ei);
		// }else if(zfi == zfii){
		// String kshi = ExStringUtils.isNotBlank(ei.getExamCertificateNo()) ?
		// ei.getExamCertificateNo() : "0";
		// String kshii = ExStringUtils.isNotBlank(eii.getExamCertificateNo()) ?
		// eii.getExamCertificateNo() : "0";
		// Long l1 = Long.parseLong(kshi);
		// Long l2 = Long.parseLong(kshii);
		// if(l1 > l2){
		// enrolleeInfoList.set(i,eii);
		// enrolleeInfoList.set(ii,ei);
		// }
		// }
		// }
		// }
		return enrolleeInfoList;
	}

	/**
	 * 获取已导入考生数据
	 * 
	 * @param recruitPlanId
	 * @return key:准考证号,value:resourceid
	 */
	public Map<String, String> getExameeInfoMap(String recruitPlanId) {
		String uniqueId = CacheAppManager.getSysConfigurationByCode(
				"exameeInfo.uniqueId").getParamValue();// 导入报名信息唯一标示
		Map<String, String> exameeInfoMap = new HashMap<String, String>();
		String sql = "select t.resourceid,t.ZKZH,t.KSH from edu_recruit_examinee t where t.isdeleted=? and t.recruitplanid=?";
		try {
			List<Map<String, Object>> list = baseSupportJdbcDao
					.getBaseJdbcTemplate().findForList(sql,
							new Object[] { 0, recruitPlanId });
			if (ExCollectionUtils.isNotEmpty(list)) {
				for (Map<String, Object> map : list) {
					if ("0".equals(uniqueId)) {// 准考证号
						exameeInfoMap.put(map.get(ZKZH).toString(),
								map.get("resourceid").toString());// 准考证号:id
					} else if ("1".equals(uniqueId)) {// 考生号
						exameeInfoMap.put(map.get(KSH).toString(),
								map.get("resourceid").toString());// 考生号:id
					}
				}
			}
		} catch (Exception e) {
			logger.error("获取已导入考生信息出错.", e.fillInStackTrace());
			throw new ServiceException("获取已导入考生信息出错.", e.fillInStackTrace());
		}
		return exameeInfoMap;
	}

	/**
	 * 保存或更新ExameeInfo
	 * 
	 * @param exameeInfoList
	 *            待导入数据
	 * @param exameeInfoMap
	 *            已导入数据
	 * @param fieldSet
	 */
	public void saveOrUpdateExameeInfo(
			List<Map<String, Object>> exameeInfoList,
			Map<String, String> exameeInfoMap, ExameeInfoConfig config,
			String recruitplanid) {
		List<Map<String, Object>> saveList = new ArrayList<Map<String, Object>>();// 新增列表
		List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();// 更新列表
		List<Map<String, Object>> scoreList = new ArrayList<Map<String, Object>>();// 成绩更新列表
		List<Map<String, Object>> wishList = new ArrayList<Map<String, Object>>();// 志愿更新列表

		Set<String> exameeInfoField = new HashSet<String>(config
				.getFieldListToMap().keySet());// 考生信息字段
		Set<String> exameeInfoScoreField = config.getExameeInfoScoreFieldMap()
				.keySet();// 成绩字段
		Set<String> exameeInfoWishField = config.getExameeInfoWishFieldMap()
				.keySet();// 志愿信息字段
		Set<String> updatField = new HashSet<String>();// 更新字段
		exameeInfoField.add("recruitplanid");
		exameeInfoField.add("LQZYMC");// 录取专业名称
		exameeInfoField.add("CCMC");// 层次名称
		exameeInfoField.add("XZ");// 学制
		exameeInfoField.add("XXXSDM");// 学习形式
		for (String f : exameeInfoField) {
			if (!"BXDW".equals(f)) {
				updatField.add(f + "=:" + f);
			}
		}
		exameeInfoField.add("KSZT");// 考生状态
		exameeInfoField.add("resourceid");
		exameeInfoField.add("isDeleted");
		exameeInfoField.add("version");
		exameeInfoField.remove("BXDW");
		GUIDUtils.init();
		String uniqueId = CacheAppManager.getSysConfigurationByCode(
				"exameeInfo.uniqueId").getParamValue();// 导入报名信息唯一标示
		for (Map<String, Object> map : exameeInfoList) {
			if ("0".equals(uniqueId)
					&& exameeInfoMap.containsKey(map.get(ZKZH))) {// 准考证号,已导入，加入更新列表
				map.put("examineeid", exameeInfoMap.get(map.get(ZKZH)));
				updateList.add(map);
			} else if ("1".equals(uniqueId)
					&& exameeInfoMap.containsKey(map.get(KSH))) {// 考生号,已导入，加入更新列表
				map.put("examineeid", exameeInfoMap.get(map.get(KSH)));
				updateList.add(map);
			} else {
				map.put("examineeid", GUIDUtils.buildMd5GUID(false));// 生成resourceid
				map.put("resourceid", map.get("examineeid"));
				map.put("recruitplanid", recruitplanid);
				map.put("isDeleted", 0);
				map.put("version", 0);
				map.put("KSZT", "5");
				saveList.add(map);
			}
			map.put("recruitplanid", recruitplanid);
			// 成绩字段
			Map<String, Object> scoreMap = null;
			for (String scoreField : exameeInfoScoreField) {// 每次导入都将相应成绩清空，重新生成
			// if(map.get(scoreField)!=null){//成绩不为空才导入
				if (map.get(scoreField) != null
						&& ExStringUtils.isNotBlank(map.get(scoreField)
								.toString())) {// 成绩不为空才导入
					scoreMap = new HashMap<String, Object>();
					scoreMap.put("resourceid", GUIDUtils.buildMd5GUID(false));
					scoreMap.put("examineeid", map.get("examineeid"));
					scoreMap.put("scorecode", ExStringUtils.substringAfter(
							scoreField, EXAMINFOSCORE_PREFIX));
					scoreMap.put("scorevalue", map.get(scoreField));
					scoreMap.put("isdeleted", 0);
					scoreMap.put("version", 0);
					scoreList.add(scoreMap);
				}
			}
			// 志愿信息字段
			Map<String, Object> wishMap = null;
			for (String wishField : exameeInfoWishField) {// 每次导入都将相应志愿清空，重新生成
				if (map.get(wishField) != null
						&& ExStringUtils.isNotBlank(map.get(wishField)
								.toString())) {// 志愿信息不为空
					wishMap = new HashMap<String, Object>();
					wishMap.put("resourceid", GUIDUtils.buildMd5GUID(false));
					wishMap.put("examineeid", map.get("examineeid"));
					wishMap.put("ZYBM", map.get(wishField));// 专业编码
					String showOrder = ExStringUtils.substringAfter(wishField,
							EXAMINFOWISH_PREFIX);
					wishMap.put("ZYMC", map.get("ZYMC" + showOrder));
					wishMap.put("showorder", showOrder);
					wishMap.put("isdeleted", 0);
					wishMap.put("version", 0);
					wishList.add(wishMap);
				}
			}

		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 修改录取信息（注：重复导入）
		for (int i = 0; i < updateList.size(); i++) {
			Map<String, Object> update = updateList.get(i);
			if (update.get("examineeid") == null
					|| ExStringUtils.isBlank(update.get("examineeid")
							.toString())) {
				continue;
			}
			ExameeInfo exameeInfo = this.get(update.get("examineeid")
					.toString());
			exameeInfo.setXM(update.get("XM") != null ? update.get("XM")
					.toString() : "");
			exameeInfo.setTXDZ(update.get("TXDZ") != null ? update.get("TXDZ")
					.toString() : "");
			exameeInfo.setKSH(update.get("KSH") != null ? update.get("KSH")
					.toString() : "");
			exameeInfo.setWYYZDM(update.get("WYYZDM") != null ? update.get(
					"WYYZDM").toString() : "");
			if (update.get("GZRQ") != null
					&& ExStringUtils.isNotBlank(update.get("GZRQ").toString())) {
				try {
					// Date date = sdf.parse(update.get("GZRQ").toString());
					Date date = (Date) update.get("GZRQ");
					exameeInfo.setGZRQ(date);
				} catch (Exception e) {
					logger.error("GZRQ=" + update.get("GZRQ") + "出错", e);
					// e.printStackTrace();
				}
			}
			exameeInfo.setYZBM(update.get("YZBM") != null ? update.get("YZBM")
					.toString() : "");
			exameeInfo.setSFZH(update.get("SFZH") != null ? update.get("SFZH")
					.toString() : "");
			exameeInfo.setBYXX(update.get("BYXX") != null ? update.get("BYXX")
					.toString() : "");
			exameeInfo.setXZ(update.get("XZ") != null ? Double
					.parseDouble(update.get("XZ").toString()) : 0D);
			exameeInfo.setLXDH(update.get("LXDH") != null ? update.get("LXDH")
					.toString() : "");
			exameeInfo.setXBDM(update.get("XBDM") != null ? update.get("XBDM")
					.toString() : "");
			exameeInfo.setCCDM(update.get("CCDM") != null ? update.get("CCDM")
					.toString() : "");
			exameeInfo.setXXXSDM(update.get("XXXSDM") != null ? update.get(
					"XXXSDM").toString() : "");
			exameeInfo.setLQZYMC(update.get("LQZYMC") != null ? update.get(
					"LQZYMC").toString() : "");
			exameeInfo.setWHCDDM(update.get("WHCDDM") != null ? update.get(
					"WHCDDM").toString() : "");
			exameeInfo.setZYLBDM(update.get("ZYLBDM") != null ? update.get(
					"ZYLBDM").toString() : "");
			exameeInfo.setZZMMDM(update.get("ZZMMDM") != null ? update.get(
					"ZZMMDM").toString() : "");
			exameeInfo.setMZDM(update.get("MZDM") != null ? update.get("MZDM")
					.toString() : "");
			exameeInfo.setBKZYSXDM(update.get("BKZYSXDM") != null ? update.get(
					"BKZYSXDM").toString() : "");
			exameeInfo.setLQZY(update.get("LQZY") != null ? update.get("LQZY")
					.toString() : "");
			exameeInfo.setZKZH(update.get("ZKZH") != null ? update.get("ZKZH")
					.toString() : "");
			exameeInfo.setZSLBDM(update.get("ZSLBDM") != null ? update.get(
					"ZSLBDM").toString() : "");
			if (update.get("CSRQ") != null
					&& ExStringUtils.isNotBlank(update.get("CSRQ").toString())) {
				try {
					// Date date = sdf.parse(update.get("CSRQ").toString());
					Date date = (Date) update.get("CSRQ");
					exameeInfo.setCSRQ(date);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			exameeInfo.setKSTZBZ(update.get("KSTZBZ") != null ? update.get(
					"KSTZBZ").toString() : "");
			exameeInfo.setCCMC(update.get("CCMC") != null ? update.get("CCMC")
					.toString() : "");
			RecruitPlan recruitPlan = recruitPlanService.get(update.get(
					"recruitplanid").toString());
			exameeInfo.setRecruitPlan(recruitPlan);
			// 更新录取信息（注：重导入）
			this.update(exameeInfo);
		}
		// 新增
		for (int i = 0; i < saveList.size(); i++) {
			Map<String, Object> save = saveList.get(i);
			ExameeInfo exameeInfo = new ExameeInfo();
			exameeInfo.setIsDeleted(0);
			exameeInfo.setVersion(0L);
			exameeInfo.setKSZT("5");
			exameeInfo.setXM(save.get("XM") != null ? save.get("XM").toString()
					: "");
			exameeInfo.setTXDZ(save.get("TXDZ") != null ? save.get("TXDZ")
					.toString() : "");
			exameeInfo.setKSH(save.get("KSH") != null ? save.get("KSH")
					.toString() : "");
			exameeInfo.setWYYZDM(save.get("WYYZDM") != null ? save
					.get("WYYZDM").toString() : "");
			if (save.get("GZRQ") != null
					&& ExStringUtils.isNotBlank(save.get("GZRQ").toString())) {
				try {
					// Date date = sdf.parse(save.get("GZRQ").toString());
					Date date = (Date) save.get("GZRQ");
					exameeInfo.setGZRQ(date);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (save.get("BYRQ") != null
					&& ExStringUtils.isNotBlank(save.get("BYRQ").toString())) {
				try {
					// Date date = sdf.parse(save.get("BYRQ").toString());
					Date date = (Date) save.get("BYRQ");
					exameeInfo.setBYRQ(date);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			exameeInfo.setYZBM(save.get("YZBM") != null ? save.get("YZBM")
					.toString() : "");
			exameeInfo.setSFZH(save.get("SFZH") != null ? save.get("SFZH")
					.toString() : "");
			exameeInfo.setBYXX(save.get("BYXX") != null ? save.get("BYXX")
					.toString() : "");
			exameeInfo.setXZ(save.get("XZ") != null ? Double.parseDouble(save
					.get("XZ").toString()) : 0D);
			exameeInfo.setLXDH(save.get("LXDH") != null ? save.get("LXDH")
					.toString() : "");
			exameeInfo.setXBDM(save.get("XBDM") != null ? save.get("XBDM")
					.toString() : "");
			exameeInfo.setCCDM(save.get("CCDM") != null ? save.get("CCDM")
					.toString() : "");
			exameeInfo.setXXXSDM(save.get("XXXSDM") != null ? save
					.get("XXXSDM").toString() : "");
			exameeInfo.setLQZYMC(save.get("LQZYMC") != null ? save
					.get("LQZYMC").toString() : "");
			exameeInfo.setWHCDDM(save.get("WHCDDM") != null ? save
					.get("WHCDDM").toString() : "");
			exameeInfo.setZYLBDM(save.get("ZYLBDM") != null ? save
					.get("ZYLBDM").toString() : "");
			exameeInfo.setZZMMDM(save.get("ZZMMDM") != null ? save
					.get("ZZMMDM").toString() : "");
			exameeInfo.setMZDM(save.get("MZDM") != null ? save.get("MZDM")
					.toString() : "");
			exameeInfo.setBKZYSXDM(save.get("BKZYSXDM") != null ? save.get(
					"BKZYSXDM").toString() : "");
			exameeInfo.setLQZY(save.get("LQZY") != null ? save.get("LQZY")
					.toString() : "");
			exameeInfo.setZKZH(save.get("ZKZH") != null ? save.get("ZKZH")
					.toString() : "");
			exameeInfo.setZSLBDM(save.get("ZSLBDM") != null ? save
					.get("ZSLBDM").toString() : "");
			if (save.get("CSRQ") != null
					&& ExStringUtils.isNotBlank(save.get("CSRQ").toString())) {
				try {
					// Date date = sdf.parse(save.get("CSRQ").toString());
					Date date = (Date) save.get("CSRQ");
					exameeInfo.setCSRQ(date);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			exameeInfo.setKSTZBZ(save.get("KSTZBZ") != null ? save
					.get("KSTZBZ").toString() : "");
			exameeInfo.setCCMC(save.get("CCMC") != null ? save.get("CCMC")
					.toString() : "");
			RecruitPlan recruitPlan = recruitPlanService.get(save.get(
					"recruitplanid").toString());
			exameeInfo.setRecruitPlan(recruitPlan);
			// 保存录取信息
			this.save(exameeInfo);
		}
		// 清空成绩
		String deleteScoreSql = "delete from edu_recruit_examineescore where examineeid=:examineeid";
		// 清空志愿
		String deleteWishSql = "delete from edu_recruit_examineewish where examineeid=:examineeid";
		// 更新数据
		// String updateSql =
		// "update edu_recruit_examinee set "+ExStringUtils.join(updatField,",")+" where resourceid=:examineeid ";
		// 保存新数据
		// String saveSql =
		// "insert into edu_recruit_examinee ("+ExStringUtils.join(exameeInfoField,",")+") values(:"+ExStringUtils.join(exameeInfoField,",:")+")";
		// 保存成绩数据
		String saveScoreSql = "insert into edu_recruit_examineescore (resourceid,examineeid,scorecode,scorevalue,isdeleted,version) values (:resourceid,:examineeid,:scorecode,:scorevalue,:isdeleted,:version)";
		// 保存志愿信息
		String saveWishSql = "insert into edu_recruit_examineewish (resourceid,examineeid,ZYBM,ZYMC,showorder,isdeleted,version) values (:resourceid,:examineeid,:ZYBM,:ZYMC,:showorder,:isdeleted,:version)";
		baseSupportJdbcDao
				.getBaseJdbcTemplate()
				.getJdbcTemplate()
				.batchUpdate(deleteScoreSql,
						updateList.toArray(new Map[updateList.size()]));// 删除旧的成绩信息
		baseSupportJdbcDao
				.getBaseJdbcTemplate()
				.getJdbcTemplate()
				.batchUpdate(deleteWishSql,
						updateList.toArray(new Map[updateList.size()]));// 删除旧的志愿信息
		// baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate(updateSql,
		// updateList.toArray(new Map[updateList.size()]));//更新考生信息
		// baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate(saveSql,
		// saveList.toArray(new Map[saveList.size()]));//保存新增数据
		baseSupportJdbcDao
				.getBaseJdbcTemplate()
				.getJdbcTemplate()
				.batchUpdate(saveScoreSql,
						scoreList.toArray(new Map[scoreList.size()]));// 保存成绩数据
		baseSupportJdbcDao
				.getBaseJdbcTemplate()
				.getJdbcTemplate()
				.batchUpdate(saveWishSql,
						wishList.toArray(new Map[wishList.size()]));// 保存志愿数据
	}

	/**
	 * 获取学号最大流水号集合
	 * 
	 * @param enrolleeInfoList
	 * @return
	 */
	public Map<String, String> getMaxStudentNoSuffixMap(
			Collection<EnrolleeInfo> enrolleeInfoList) {
		Map<String, String> maxStudentIdSuffixMap = new HashMap<String, String>();
		for (EnrolleeInfo enrolleeInfo : enrolleeInfoList) {
			if (ExStringUtils.isNotBlank(enrolleeInfo.getMatriculateNoticeNo())) {
				String prefix = genStudentNoPrefix(enrolleeInfo);// 获得学号前缀
				String suffix = ExStringUtils.substring(
						enrolleeInfo.getMatriculateNoticeNo(), prefix.length());// 流水号
				if (ExStringUtils.isEmpty(suffix)) {
					suffix = "0001";
				}
				if (maxStudentIdSuffixMap.containsKey(prefix)) {
					if (Integer.valueOf(suffix) < Integer
							.valueOf(maxStudentIdSuffixMap.get(prefix))) {
						suffix = maxStudentIdSuffixMap.get(prefix);
					}
				}
				maxStudentIdSuffixMap.put(prefix, suffix);
			}
		}
		return maxStudentIdSuffixMap;
	}

	/**
	 * 获取学生学号
	 * 
	 * @param ei
	 * @param maxStudentNoSuffixMap
	 * @return
	 */
	public String genStudentNo(EnrolleeInfo ei,
			Map<String, String> maxStudentNoSuffixMap) {
		// 学号前9位
		String prefix = genStudentNoPrefix(ei);
		String prefix_exist = "";
		String matriculateNoticeNo = ei.getMatriculateNoticeNo();
		if (ExStringUtils.isNotBlank(matriculateNoticeNo)) {
			prefix_exist = matriculateNoticeNo.substring(0, 9);
		}
		if (!prefix_exist.equals(prefix)) {// 前缀不同时
			// 10-12位流水号
			String suffix = "";
			if (!maxStudentNoSuffixMap.containsKey(prefix)) {
				suffix = "000";
			} else {
				suffix = maxStudentNoSuffixMap.get(prefix);
				if ("999".equals(suffix)) {
					throw new ServiceException("没有空余的学号了！");
				}
			}
			suffix = increase(suffix);
			maxStudentNoSuffixMap.put(prefix, suffix);// 更新为当前流水号
			return prefix + suffix;
		} else {
			return matriculateNoticeNo;
		}
	}

	public String genStudentNo2(EnrolleeInfo ei,
			Map<String, String> maxStudentNoSuffixMap) {
		// 学号前9位
		String prefix = genStudentNoPrefix(ei);
		String prefix_exist = "";
		String matriculateNoticeNo = ei.getMatriculateNoticeNo();
		if (ExStringUtils.isNotBlank(matriculateNoticeNo)) {
			prefix_exist = matriculateNoticeNo.substring(0, 9);
		}
		if (!prefix_exist.equals(prefix)) {// 前缀不同时
			// 10-13位流水号
			String suffix = "";
			if (!maxStudentNoSuffixMap.containsKey(prefix)) {
				suffix = "000";
			} else {
				suffix = maxStudentNoSuffixMap.get(prefix);
				if ("999".equals(suffix)) {
					throw new ServiceException("没有空余的学号了！");
				}
			}
			suffix = increase(suffix);
			maxStudentNoSuffixMap.put(prefix, suffix);// 更新为当前流水号
			return prefix + suffix;
		} else {
			return matriculateNoticeNo;
		}
	}

	/**
	 * 右江医 10 位 教学点代码+专业层次代码+年级代码+学生代码，
	 * 其中头两位数字为教学点代码、第2-4位数字为专业层次代码、第5-6位数字为年级代码、最后三位数字为学生代码，
	 * 例如0110416001为校本部专升本临床专业16级1号学生
	 */
	@Override
	public String genStudentYJY(EnrolleeInfo ei,
								Map<String, String> maxStudentNoSuffixMap, List<Dictionary> listdict) {
		String prefix = genStudentYJYPrefix(ei, listdict);
		String prefix_exist = "";
		String matriculateNoticeNo = ei.getMatriculateNoticeNo();
		if (ExStringUtils.isNotBlank(matriculateNoticeNo)) {
			prefix_exist = matriculateNoticeNo.substring(0, 7);
		}
		if (!prefix_exist.equals(prefix)) {// 前缀不同时
			// 8-10位流水号
			String suffix = "";
			if (!maxStudentNoSuffixMap.containsKey(prefix)) {
				suffix = "000";
			} else {
				suffix = maxStudentNoSuffixMap.get(prefix);
				if ("999".equals(suffix)) {
					throw new ServiceException("没有空余的学号了！");
				}
			}
			suffix = increase(suffix);
			maxStudentNoSuffixMap.put(prefix, suffix);// 更新为当前流水号
			return prefix + suffix;
		} else {
			return matriculateNoticeNo;
		}
	}

	private String genStudentYJYPrefix(EnrolleeInfo ei,
			List<Dictionary> listdict) {
		String prefix = "";
		try {
			// 1-2 教学点
			prefix += ei.getBranchSchool().getUnitCode4StuNo();
			// prefix +=
			// ei.getRecruitMajor().getRecruitPlan().getYearInfo().getFirstYear().toString().substring(2,4);
			// 3-5 专业
			prefix += ei.getRecruitMajor().getMajor().getMajorCode();

			// 6-7 年级
			prefix += ei.getRecruitMajor().getRecruitPlan().getYearInfo()
					.getFirstYear().toString().substring(2, 4);

		} catch (Exception e) {
			logger.error("获取右江医学院学号前缀错误;" + e.fillInStackTrace());
		}
		return prefix;
	}

	// 安徽医学院学号生成 共12位
	// 15:年份 3：成人教育 1：层次 02:专业 0001:流水号
	public String genStudentAHY(EnrolleeInfo ei,
			Map<String, String> maxStudentNoSuffixMap, List<Dictionary> listdict) {
		String prefix = genStudentAHYPrefix(ei, listdict);
		String prefix_exist = "";
		String matriculateNoticeNo = ei.getMatriculateNoticeNo();
		if (ExStringUtils.isNotBlank(matriculateNoticeNo)) {
			prefix_exist = matriculateNoticeNo.substring(0, 8);
		}
		if (!prefix_exist.equals(prefix)) {// 前缀不同时
			// 7-10位流水号
			String suffix = "";
			if (!maxStudentNoSuffixMap.containsKey(prefix)) {
				suffix = "0000";
			} else {
				suffix = maxStudentNoSuffixMap.get(prefix);
				if ("9999".equals(suffix)) {
					throw new ServiceException("没有空余的学号了！");
				}
			}
			suffix = increase(suffix);
			maxStudentNoSuffixMap.put(prefix, suffix);// 更新为当前流水号
			return prefix + suffix;
		} else {
			return matriculateNoticeNo;
		}
	}

	// 安徽医学院学号前缀
	private String genStudentAHYPrefix(EnrolleeInfo ei,
			List<Dictionary> listdict) {
		String prefix = "";
		try {
			// 0-4 年份
			prefix += ei.getRecruitMajor().getRecruitPlan().getYearInfo()
					.getFirstYear().toString().substring(2, 4);
			// 5 编制
			try {
				prefix += CacheAppManager.getSysConfigurationByCode(
						"school.studyno.compile").getParamValue();
			} catch (Exception e) {
				logger.error("获取安徽医学院学号编制单位错误（全局参数：school.studyno.compile）;"
						+ e.fillInStackTrace());
			}
			// 6 层次
			if (null != listdict && listdict.size() > 0) { // 层次需要转换
				prefix += JstlCustomFunction.dictionaryCode2Name(
						"studyno.replace.classiccode", ei.getCCMD());
			} else {
				prefix += ei.getCCMD();
			}
			// 7-8 专业
			prefix += ei.getRecruitMajor().getMajor().getMajorCode();
		} catch (Exception e) {
			logger.error("获取安徽医学院学号前缀错误;" + e.fillInStackTrace());
		}
		return prefix;
	}

	public String getStudentNo(EnrolleeInfo ei,
			Map<String, String> maxStudentNoSuffixMap) {
		// 学号前3位
		String prefix = getStudentNoPrefix(ei);
		String prefix_exist = "";
		String matriculateNoticeNo = ei.getMatriculateNoticeNo();
		if (ExStringUtils.isNotBlank(matriculateNoticeNo)) {
			prefix_exist = matriculateNoticeNo.substring(0, 3);
		}
		if (!prefix_exist.equals(prefix)) {// 前缀不同时
			// 4-8位流水号
			String suffix = "";
			if (!maxStudentNoSuffixMap.containsKey(prefix)) {
				suffix = "00000";
			} else {
				suffix = maxStudentNoSuffixMap.get(prefix);
				if ("99999".equals(suffix)) {
					throw new ServiceException("没有空余的学号了！");
				}
			}
			suffix = increase(suffix);
			maxStudentNoSuffixMap.put(prefix, suffix);// 更新为当前流水号
			return prefix + suffix;
		} else {
			return matriculateNoticeNo;
		}
	}

	public String getStudentNoPrefix(EnrolleeInfo ei) {
		String prefix = "";
		try {
			// String genStudentType =
			// JstlCustomFunction.getSysConfigurationValue("sys.default.genStudentType",
			// "server");
			// if("gdm".equals(genStudentType)){
			// 第1-2位 年份（公元后两位），
			String year = ei.getRecruitMajor().getRecruitPlan().getYearInfo()
					.getFirstYear().toString().substring(2, 4);
			prefix += year;
			// 第3位 培养层次编号，
			// 根据CCMD替换层次
			String classicEnd = ei.getRecruitMajor().getClassic().getEndPoint();
			// 导入数据是否需要和国家专业去转换 判断是否设置数据字典admission.replace.majorcode
			/*
			 * List<Dictionary> listdict =
			 * dictionaryService.findByHql(" from "+Dictionary
			 * .class.getSimpleName
			 * ()+" where dictCode = 'studyno.replace.classiccode' "); if(null
			 * != listdict && listdict.size() > 0){ //需要替换
			 */String rpcode = JstlCustomFunction.dictionaryCode2Value(
					"studyno.replace.classiccode", ei.getCCMD());
			if (ExStringUtils.isNotBlank(rpcode)) {
				prefix += rpcode;
			} else {
				prefix += "本科".equals(classicEnd) ? "5" : "6";
			}

			/*
			 * }else{ prefix += "本科".equals(classicEnd)?"5":"6"; }
			 */
			// }
		} catch (Exception e) {
		} finally {
			return prefix;
		}
	}

	public String genStudentNoPrefix(EnrolleeInfo ei) {
		String prefix = "";
		try {
			String genStudentType = JstlCustomFunction
					.getSysConfigurationValue("sys.default.genStudentType",
							"server");
			if ("gdm".equals(genStudentType)) {
				// 第1-2位 年份（公元后两位），
				Long year = ei.getRecruitMajor().getRecruitPlan().getYearInfo()
						.getFirstYear()
						+ ("2".equals(ei.getRecruitMajor().getRecruitPlan()
								.getTerm()) ? 1L : 0L);
				prefix += String.valueOf(year).substring(2);
				// 第3位 培养层次编号，
				String classicEnd = ei.getRecruitMajor().getClassic()
						.getEndPoint();
				prefix += "本科".equals(classicEnd) ? "5" : "6";
				// 第4位 学习形式编号，
				prefix += "2";// 需求只写了业余是2
				// 第5-7位 专业编号，先按需求的对应关系 实在不行取系统的recruitmajorCode
				String majorName = ei.getRecruitMajor().getMajor()
						.getMajorName();
				String majorCode = "";
				// if("临床医学".equals(majorName)){majorCode = "301";}
				// else if("护理学".equals(majorName)){majorCode = "701";}
				// else if("医学检验".equals(majorName)){majorCode = "304";}
				// else if("药学".equals(majorName)){majorCode = "801";}
				// else if("信息管理与信息系统".equals(majorName)){majorCode = "704";}
				// else if("医学检验技术".equals(majorName)){majorCode = "354";}
				// else if("医学影像技术".equals(majorName)){majorCode = "353";}
				// else if(ExStringUtils.isEmpty(majorCode)){
				// majorCode = ei.getRecruitMajor().getRecruitMajorCode();
				// majorCode = ExStringUtils.substring(majorCode,
				// majorCode.length()-3, majorCode.length());
				// }
				// majorCode = ei.getRecruitMajor().getMajorCodeForStudyNo();
				majorCode = ExStringUtils.substring(majorCode,
						majorCode.length() - 3, majorCode.length());
				prefix += majorCode;
				// 第8-9位 教学站编号，
				String school = ei.getBranchSchool().getUnitCode();
				prefix += school.substring(school.length() - 2);
			}
		} catch (Exception e) {
			// 第1-2位 年份（公元后两位），
			Long year = ei.getRecruitMajor().getRecruitPlan().getYearInfo()
					.getFirstYear()
					+ ("2".equals(ei.getRecruitMajor().getRecruitPlan()
							.getTerm()) ? 1L : 0L);
			prefix += String.valueOf(year).substring(2);
			// 第3位 培养层次编号，
			String classicEnd = ei.getRecruitMajor().getClassic().getEndPoint();
			prefix += "本科".equals(classicEnd) ? "5" : "6";
			// 第4位 学习形式编号，
			prefix += "2";// 需求只写了业余是2
			// 第5-7位 专业编号，先按需求的对应关系 实在不行取系统的recruitmajorCode
			// 20171220:在表结构中增加 字段用于编学号，不再代码中写死
			String majorCode = ExStringUtils.isBlank(ei.getRecruitMajor()
					.getMajor().getMajorCode4StudyNo()) ? "000" : ei
					.getRecruitMajor().getMajor().getMajorCode4StudyNo();

			// String majorName =
			// ei.getRecruitMajor().getMajor().getMajorName();
			// String majorCode ="";
			// if("临床医学".equals(majorName)){majorCode = "301";}
			// else if("护理学".equals(majorName) ||
			// "护理".equals(majorName)){majorCode = "701";}
			// else if("医学检验".equals(majorName)){majorCode = "304";}
			// else if("医学影像学".equals(majorName)){majorCode = "303";}
			// else if("药学".equals(majorName)){majorCode = "801";}
			// else if("信息管理与信息系统".equals(majorName)){majorCode = "704";}
			// else if("医学检验技术".equals(majorName)){majorCode = "354";}
			// else if("医学影像技术".equals(majorName)){majorCode = "353";}
			// else if("口腔医学".equals(majorName) &&
			// "本科".equals(classicEnd)){majorCode = "401";}
			// else if("口腔医学".equals(majorName) &&
			// !"本科".equals(classicEnd)){majorCode = "406";}
			// else if("中药学".equals(majorName)){
			// majorCode = "802";
			// } else if ("中药".equals(majorName)){
			// majorCode = "302";
			// }else if(ExStringUtils.isEmpty(majorCode)){
			// majorCode = ei.getRecruitMajor().getRecruitMajorCode();
			// majorCode = ExStringUtils.substring(majorCode,
			// majorCode.length()-3, majorCode.length());
			// }
			prefix += majorCode;
			// 第8-9位 教学站编号，
			String school = ei.getBranchSchool().getUnitCode();
			prefix += school.substring(school.length() - 2);
		} finally {
			return prefix;
		}
	}

	public String increase(String num) {
		char[] chars = num.toCharArray();
		for (int i = num.length() - 1; i >= 0; i--) {
			if (chars[i] == '9') {
				chars[i] = '0';
			} else {
				chars[i]++;
				break;
			}
		}
		return new String(chars);
	}

	@Override
	public Map<String, Object> importExameeInfoPhoto(String recruitPlanId,
			String tempZipPath) throws ServiceException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 1.解压上传文件到临时目录tempZipPath
		// 2.找出考生对应的相片，记录对应的考生不存在的相片
		Map<String, Map<String, Object>> exameeInfoMap = getExameeInfoAndEnrolleeInfo(recruitPlanId);// 获取考生信息
		if (exameeInfoMap.size() == 0) {
			throw new ServiceException("考生信息数据还未导入!");
		}
		String _prefix = "";
		Set<Map<String, Object>> exameeInfoSet = new HashSet<Map<String, Object>>();// 待更新相片的考生
		List<String> noExameeInfoPhotoList = new ArrayList<String>();// 对应的考生不存在的相片
		File[] photoList = new File(tempZipPath).listFiles();
		if (ArrayUtils.isNotEmpty(photoList)) {
			String disPath = Constants.EDU3_DATAS_LOCALROOTPATH + "common"
					+ File.separator + "students" + File.separator;// 图片实际存储路径前缀
			for (File file : photoList) {
				String fileName = file.getName();// F010300318.JPG,Z010300318.JPG(F/Z+考生号)
				String prefix = ExStringUtils.upperCase(ExStringUtils
						.substring(fileName, 0, 1));// 文件名首字母
				if (ExStringUtils.isEmpty(_prefix)) {
					_prefix = prefix;
				}
				String ksh = ExStringUtils.substringBefore(ExStringUtils
						.upperCase(ExStringUtils.substring(fileName, 1)),
						".JPG");// 考生号
				if (exameeInfoMap.containsKey(ksh)) {// 已导入考生信息
					Map<String, Object> map = exameeInfoMap.get(ksh);
					map.put(prefix, "/" + map.get("SIGNUPDATE") + "/"
							+ fileName);// 网络路径
					map.put("fileName" + prefix, fileName);// 相片名称
					map.put("tempPath" + prefix, file.getAbsolutePath());// 原存储路径
					map.put("disPath", disPath + map.get("SIGNUPDATE")
							+ File.separator);// //图片实际存储目录
					map.put("servPath" + prefix,
							disPath + map.get("SIGNUPDATE") + File.separator
									+ fileName);// 新的实际存储路径
					exameeInfoSet.add(map);
				} else {
					noExameeInfoPhotoList.add("不存在考生号为" + ksh + "的考生: "
							+ fileName);// 不存在考生
				}
			}
			// 3.把检查通过的相片复制到实际存储目录下,更新考生相片
			List<Map<String, Object>> failList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> map : exameeInfoSet) {
				try {
					if (map.get("disPath") != null) {// 创建相片存储目录
						if (!new File(map.get("disPath").toString()).exists()) {
							new File(map.get("disPath").toString()).mkdirs();
						}
					}
					if (map.get("tempPath" + PHOTOF) != null) {// 复制主相片
						FileUtils.copyFile(map.get("tempPath" + PHOTOF)
								.toString(), map.get("servPath" + PHOTOF)
								.toString());// 复制图片到指定目录
					}
					if (map.get("tempPath" + PHOTOZ) != null) {// 复制备份相片
						FileUtils.copyFile(map.get("tempPath" + PHOTOZ)
								.toString(), map.get("servPath" + PHOTOZ)
								.toString());
					}
				} catch (IOException e) {
					e.printStackTrace();
					failList.add(map);// 复制失败
				}
			}
			// 把相片复制到实际存储目录过程失败的相片移出更新列表
			if (ExCollectionUtils.isNotEmpty(failList)) {
				exameeInfoSet.removeAll(failList);
				for (Map<String, Object> map : failList) {
					noExameeInfoPhotoList.add("导入学生相片失败: "
							+ (map.get("fileName" + PHOTOF) != null ? map
									.get("fileName" + PHOTOF) : "")
							+ " "
							+ (map.get("fileName" + PHOTOZ) != null ? map
									.get("fileName" + PHOTOZ) : ""));
				}
			}
			String message = "";
			if (ExCollectionUtils.isNotEmpty(exameeInfoSet)) {
				String exameeInfoSql = "update edu_recruit_examinee set mainPhotoPath=:F,backPhotoPath=:Z where resourceid=:EXAMINEEID";
				String baseInfoSql = "update edu_base_student set recruitphotopath=:"
						+ _prefix + " where resourceid=:STUDENTID";
				baseSupportJdbcDao
						.getBaseJdbcTemplate()
						.getJdbcTemplate()
						.batchUpdate(
								exameeInfoSql,
								exameeInfoSet.toArray(new Map[exameeInfoSet
										.size()]));
				baseSupportJdbcDao
						.getBaseJdbcTemplate()
						.getJdbcTemplate()
						.batchUpdate(
								baseInfoSql,
								exameeInfoSet.toArray(new Map[exameeInfoSet
										.size()]));
				message += "<span style='color:green;'>成功导入的相片数: "
						+ exameeInfoSet.size() + "</span>";
				logger.info("成功导入的相片数: " + exameeInfoSet.size());
			}
			// 4.记录导入失败照片
			if (ExCollectionUtils.isNotEmpty(noExameeInfoPhotoList)) {
				message += "<br/><span style='color:red;'>导入失败的相片数: "
						+ noExameeInfoPhotoList.size() + "</span>";
				for (String msg : noExameeInfoPhotoList) {
					logger.info(msg);
				}
				resultMap.put("errorMessageList", noExameeInfoPhotoList);
			}
			resultMap.put("message", message);
		}
		return resultMap;
	}

	/**
	 * 获取考生信息表和报名信息表id,考生号,报名日期,相片路径
	 * 
	 * @param recruitPlanId
	 * @return
	 */
	public Map<String, Map<String, Object>> getExameeInfoAndEnrolleeInfo(
			String recruitPlanId) {
		Map<String, Map<String, Object>> resultMap = new HashMap<String, Map<String, Object>>();
		StringBuilder sql = new StringBuilder();
		// 考生信息id,考生号,基本信息id,报名日期字符串,相片路径
		sql.append(" select t.resourceid examineeid,t.ksh,s.resourceid studentid,to_char(ei.signupdate,'yyyy_MM') signupdate,mainPhotoPath F,backPhotoPath Z ");
		sql.append(" from edu_recruit_examinee t ");
		sql.append(" join edu_recruit_enrolleeinfo ei on t.zkzh=ei.examcertificateno and ei.isdeleted=0 ");
		sql.append(" join edu_base_student s on ei.studentbaseinfoid=s.resourceid ");
		sql.append(" where t.isdeleted=0 and t.recruitplanid=? ");
		try {
			List<Map<String, Object>> list = baseSupportJdbcDao
					.getBaseJdbcTemplate().findForList(sql.toString(),
							new Object[] { recruitPlanId });
			if (ExCollectionUtils.isNotEmpty(list)) {
				for (Map<String, Object> map : list) {
					resultMap.put(map.get(KSH).toString(), map);
				}
			}
		} catch (Exception e) {
			logger.error("查询考生信息出错", e.fillInStackTrace());
		}
		return resultMap;
	}

	@Override
	public Page findExameeInfoByConditionWithJDBC(Page objPage,
			Map<String, Object> condition) throws ServiceException {
		List<String> values = new ArrayList<String>(0);
		try {
			StringBuffer sql = findExameeInfoByConditionSql(condition, values);
			objPage = baseSupportJdbcDao.getBaseJdbcTemplate().findListMap(
					objPage, sql.toString(), values.toArray());
		} catch (Exception e) {
			logger.error("查询考生信息出错", e.fillInStackTrace());
		}
		return objPage;
	}

	/**
	 * 根据条件查询考生报名信息SQL
	 * 
	 * @param condition
	 * @param values
	 * @return
	 */
	private StringBuffer findExameeInfoByConditionSql(
			Map<String, Object> condition, List<String> values) {
		StringBuffer sql = new StringBuffer();
		// sql.append("select distinct e.resourceid,e.KSZT,e.XM,rp.recruitPlanname,e.KSH,e.SFZH,e.LQZYMC,e.ZKZH,e.XBDM,to_char(e.CSRQ,'yyyy-MM-dd') CSRQ, e.KSZT,ei.MATRICULATENOTICENO,u.unitname,e.LXDH,erm.majorid "
		// );
		sql.append("select distinct e.resourceid,e.KSZT,e.XM,rp.recruitPlanname,e.KSH,e.SFZH,bm.majorname LQZYMC,e.ZKZH,e.XBDM,to_char(e.CSRQ,'yyyy-MM-dd') CSRQ, e.KSZT, ");
		sql.append(" ei.MATRICULATENOTICENO,u.unitname,e.LXDH,erm.majorid,ei.NOREPORTREASON,ei.ISSTUDYFOLLOW,ei.MEMO,ei.ENROLLNO,ei.resourceid enrolleeId,ei.isPrint,nvl(ei.REGISTER_FLAG,'N') REGISTERFLAG ");
		sql.append("from EDU_RECRUIT_EXAMINEE e inner join EDU_RECRUIT_RECRUITPLAN rp on e.RECRUITPLANID = rp.resourceid and rp.isdeleted = 0  ");
		sql.append("left join EDU_RECRUIT_ENROLLEEINFO ei on e.ZKZH = ei.EXAMCERTIFICATENO and e.KSH=ei.ENROLLEECODE and ei.isdeleted=0 ");
		// sql.append("left join edu_recruit_major erm on erm.recruitplanid = rp.resourceid and e.LQZY = erm.recruitmajorcode and erm.isdeleted=0 and erm.resourceid=ei.recruitmajorid ");
		sql.append("left join edu_recruit_major erm on erm.recruitplanid = rp.resourceid and erm.isdeleted=0 and erm.resourceid=ei.recruitmajorid ");
		sql.append("left join edu_base_major bm on erm.majorid = bm.resourceid and bm.isdeleted=0 ");// 录取专业要取基础专业
		sql.append("join EDU_BASE_CLASSIC cl on (erm.classic=cl.resourceid or erm.classic=cl.CLASSICCODE) and e.CCDM=cl.CLASSICCODE and cl.isdeleted=0 ");// 新加：or
																																							// erm.classic=cl.CLASSICCODE
		sql.append("left join HNJK_SYS_UNIT u on ei.branchschoolid = u.resourceid and u.isdeleted=0 ");
		sql.append("inner join EDU_BASE_STUDENT s on ei.STUDENTBASEINFOID=s.resourceid and e.sfzh=s.certnum and s.isdeleted = 0");
		sql.append(" where e.isdeleted = 0 and ei.isdeleted = 0 ");
		if (condition.containsKey("recruitPlanId")) {// 招生批次
			sql.append(" and rp.resourceid=? ");
			values.add(condition.get("recruitPlanId").toString());
		}
		if (condition.containsKey("name")) {// 姓名
			sql.append(" and e.XM like ? ");
			values.add("%" + condition.get("name") + "%");
		}

		if (condition.containsKey("major")) {//
			sql.append(" and erm.majorid = ? ");
			values.add(condition.get("major").toString());
		}
		if (condition.containsKey("classic")) {//
			sql.append(" and erm.classic = ? ");
			values.add(condition.get("classic").toString());
		}
		if (condition.containsKey("enrolleeCode")) {// 考生号
			sql.append(" and e.KSH=? ");
			values.add(condition.get("enrolleeCode").toString());
		}
		if (condition.containsKey("certNum")) {// 证件号
			sql.append(" and e.SFZH=? ");
			values.add(condition.get("certNum").toString());
		}
		if (condition.containsKey("examCertificateNo")) {// 准考证号
			sql.append(" and e.ZKZH=? ");
			values.add(condition.get("examCertificateNo").toString());
		}
		if (condition.containsKey("kszt")) {// 考生状态
			sql.append(" and e.KSZT=? ");
			values.add(condition.get("kszt").toString());
		}
		if (condition.containsKey("isExistsPhoto")) {// 是否存在照片
			if (Constants.BOOLEAN_YES.equals(condition.get("isExistsPhoto"))) {
				sql.append(" and e.mainPhotoPath is not null or e.backPhotoPath is not null ");
			} else {
				sql.append(" and e.mainPhotoPath is null and e.backPhotoPath is null");
			}
		}
		if (condition.containsKey("branchSchool")) {// 有学校条件
			sql.append(" and ei.branchschoolid = ?  ");
			values.add(condition.get("branchSchool").toString());
		}

		// 2014-3-18 增加录取未注册,录取已注册的查询条件，可以在数据库改
		if (condition.containsKey("registorFlag")) {
			if (!condition.containsKey("kszt")) {// 考生状态
				sql.append(" and (e.KSZT='5' or e.KSZT='7' or e.KSZT='8' or e.KSZT='10')");
			}

			if ("N".equals(condition.get("registorFlag").toString())) {
				sql.append(" and (ei.REGISTER_FLAG is null or ei.REGISTER_FLAG='N')");
			} else if ("Y".equals(condition.get("registorFlag").toString())) {
				sql.append(" and ei.REGISTER_FLAG = 'Y' ");
			}

			// if("Y".equals(condition.get("registorFlag").toString())){
			// sql.append("and ei.MATRICULATENOTICENO not exists (select t.studyNo from edu_roll_studentinfo t join EDU_RECRUIT_ENROLLEEINFO info on t.studyNo = info.matriculateNoticeNo and t.isDeleted =0)");
			// }
		}
		if (condition.containsKey("isPrint")) {
			if ("Y".equals(condition.get("isPrint").toString())) {
				sql.append(" and ei.isPrint = 'Y' ");
			} else {
				sql.append(" and ei.isPrint != 'Y' ");
			}
		}
		sql.append(" order by ei.MATRICULATENOTICENO ");
		return sql;
	}

	@Override
	public List<Map<String, Object>> findOrgUnitByConditionWithJDBC(
			Map<String, Object> condition) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select u.resourceid,u.unitname,u.unitcode ");
		sql.append("from EDU_RECRUIT_MAJOR m inner join EDU_RECRUIT_RECRUITPLAN rp on m.RECRUITPLANID = rp.resourceid ");
		sql.append("inner join HNJK_SYS_UNIT u on m.BRSCHOOLID=u.resourceid ");
		// sql.append("inner join EDU_RECRUIT_EXAMINEE e on e.RECRUITPLANID = rp.resourceid and e.LQZY=m.RECRUITMAJORCODE ");
		sql.append("inner join EDU_RECRUIT_EXAMINEE e on e.RECRUITPLANID = rp.resourceid and e.LQZY=m.RECRUITMAJORCODE and e.xxxsdm=m.teachingtype ");
		sql.append("inner join EDU_BASE_MAJOR mj on m.MAJORID = mj.resourceid ");
		sql.append("inner join EDU_BASE_CLASSIC cl on m.classic=cl.resourceid and e.CCDM=cl.CLASSICCODE ");
		sql.append("where u.isdeleted=0 ");
		sql.append("and e.isdeleted=0 and rp.isdeleted=0 and mj.isdeleted=0 and cl.isdeleted=0 and m.isdeleted=0 ");
		sql.append("and m.status=1 ");
		if (condition.containsKey("examineeid")) {
			sql.append(" and e.resourceid in (" + condition.get("examineeid")
					+ ")");
		}
		if (condition.containsKey("recruitPlanname")) {
			sql.append(" and rp.recruitPlanname=:recruitPlanname");
			values.put("recruitPlanname", condition.get("recruitPlanname"));
		}
		if (condition.containsKey("majorName")) {
			sql.append(" and mj.majorName=:majorName");
			values.put("majorName", condition.get("majorName"));
		}
		/*
		 * if(condition.containsKey("isUnique") &&
		 * condition.get("isUnique").equals("Y")){//分配教学站时用
		 * sql.append(" group by u.resourceid,u.unitname,u.unitcode "
		 * );//去掉了m.resourceid }else {
		 */
		sql.append(" group by u.resourceid,u.unitname,u.unitcode,m.classic,m.teachingtype,m.majorid ");
		// }
		sql.append(" order by u.unitcode ");
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>(
				0);
		try {
			returnList = baseSupportJdbcDao.getBaseJdbcTemplate()
					.findForListMap(sql.toString(), values);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> findExameeInfoByConditionWithJDBC(
			Map<String, Object> condition) throws ServiceException {
		Map<String, Map<String, Object>> resultMap = new HashMap<String, Map<String, Object>>();
		List<String> values = new ArrayList<String>(0);
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct e.resourceid,e.KSZT,e.XM,rp.recruitPlanname,e.KSH,e.SFZH,bm.majorname LQZYMC,e.ZKZH,e.XBDM,to_char(e.CSRQ,'yyyy-MM-dd') CSRQ, e.KSZT,ei.MATRICULATENOTICENO,u.unitname,e.LXDH,e.MZDM,e.ZZMMDM,e.TXDZ ");
		sql.append("from EDU_RECRUIT_EXAMINEE e inner join EDU_RECRUIT_RECRUITPLAN rp on e.RECRUITPLANID = rp.resourceid and rp.isdeleted=0 ");
		sql.append("left join EDU_RECRUIT_ENROLLEEINFO ei on e.ZKZH = ei.EXAMCERTIFICATENO and ei.isdeleted=0 ");
		// sql.append("left join edu_recruit_major erm on erm.recruitplanid = rp.resourceid and e.LQZY = erm.recruitmajorcode  and erm.resourceid=ei.recruitmajorid and erm.isdeleted=0 ");
		sql.append("left join edu_recruit_major erm on erm.recruitplanid = rp.resourceid and erm.isdeleted=0 and erm.resourceid=ei.recruitmajorid ");
		sql.append("left join edu_base_major bm on erm.majorid = bm.resourceid and bm.isdeleted=0 ");// 录取专业要取基础专业
		sql.append("join EDU_BASE_CLASSIC cl on erm.classic=cl.resourceid and e.CCDM=cl.CLASSICCODE ");
		sql.append("left join HNJK_SYS_UNIT u on ei.branchschoolid = u.resourceid ");
		sql.append("inner join EDU_BASE_STUDENT s on ei.STUDENTBASEINFOID=s.resourceid and e.sfzh=s.certnum and s.isdeleted=0 ");
		sql.append(" where e.isdeleted = 0 ");
		if (condition.containsKey("recruitPlanId")) {// 招生批次
			sql.append(" and rp.resourceid=? ");
			values.add(condition.get("recruitPlanId").toString());
		}
		if (condition.containsKey("name")) {// 姓名
			sql.append(" and e.XM like ? ");
			values.add("%" + condition.get("name") + "%");
		}

		if (condition.containsKey("major")) {//
			sql.append(" and erm.majorid = ? ");
			values.add(condition.get("major").toString());
		}
		if (condition.containsKey("classic")) {//
			sql.append(" and erm.classic = ? ");
			values.add(condition.get("classic").toString());
		}
		if (condition.containsKey("enrolleeCode")) {// 考生号
			sql.append(" and e.KSH=? ");
			values.add(condition.get("enrolleeCode").toString());
		}
		if (condition.containsKey("certNum")) {// 证件号
			sql.append(" and e.SFZH=? ");
			values.add(condition.get("certNum").toString());
		}
		if (condition.containsKey("examCertificateNo")) {// 准考证号
			sql.append(" and e.ZKZH=? ");
			values.add(condition.get("examCertificateNo").toString());
		}
		if (condition.containsKey("kszt")) {// 考生状态
			sql.append(" and e.KSZT=? ");
			values.add(condition.get("kszt").toString());
		}
		if (condition.containsKey("isExistsPhoto")) {// 是否存在照片
			if (Constants.BOOLEAN_YES.equals(condition.get("isExistsPhoto"))) {
				sql.append(" and e.mainPhotoPath is not null ");
			} else {
				sql.append(" and e.mainPhotoPath is null ");
			}
		}
		if (condition.containsKey("branchSchool")) {// 有学校条件
			sql.append(" and ei.branchschoolid = ?  ");
			values.add(condition.get("branchSchool").toString());
		}
		if (condition.containsKey("resIds")) {// ID
			sql.append(" and e.resourceid in (")
					.append(condition.get("resIds")).append(")");
			// values.add(condition.get("resIds").toString());
		}

		// 2014-3-18 增加录取未注册,录取已注册的查询条件，可以在数据库改
		if (condition.containsKey("registorFlag")) {
			if (!condition.containsKey("kszt")) {// 考生状态
				sql.append(" and (e.KSZT=5 or e.KSZT=7 or e.KSZT=8)");
			}

			if ("N".equals(condition.get("registorFlag").toString())) {
				sql.append(" and ei.isDeleted = 0 AND (ei.REGISTER_FLAG is null or ei.REGISTER_FLAG='N')");
			} else if ("Y".equals(condition.get("registorFlag").toString())) {
				sql.append(" and ei.isDeleted = 0 AND ei.REGISTER_FLAG = 'Y' ");
			}

			// if("Y".equals(condition.get("registorFlag").toString())){
			// sql.append("and ei.MATRICULATENOTICENO not exists (select t.studyNo from edu_roll_studentinfo t join EDU_RECRUIT_ENROLLEEINFO info on t.studyNo = info.matriculateNoticeNo and t.isDeleted =0)");
			// }
		}
		sql.append(" order by ei.MATRICULATENOTICENO ");
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>(
				0);
		try {
			returnList = baseSupportJdbcDao.getBaseJdbcTemplate().findForList(
					sql.toString(), values.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> findEnrollStatisticalInfo(
			Map<String, Object> condition) throws ServiceException {
		Map<String, Map<String, Object>> resultMap = new HashMap<String, Map<String, Object>>();
		List<String> values = new ArrayList<String>(0);
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct rownum ,e.XM,rp.recruitPlanname,e.KSH,e.SFZH,bm.majorname LQZYMC,e.ZKZH,e.XBDM,u.unitname,e.LXDH,cl.CLASSICNAME,erm.TEACHINGTYPE,NVL(ei.REGISTER_FLAG, 'N') ISREPORT,si.TOTALPOINT ");
		sql.append(",decode(ei.register_flag,'Y','Y','N') register_flag,decode(ei.isprint,'Y','Y','N') isDownload,ei.memo ");
		sql.append(" from EDU_RECRUIT_EXAMINEE e inner join EDU_RECRUIT_RECRUITPLAN rp on e.RECRUITPLANID = rp.resourceid and rp.isdeleted=0 ");
		sql.append("left join EDU_RECRUIT_ENROLLEEINFO ei on e.ZKZH = ei.EXAMCERTIFICATENO and ei.isdeleted=0 ");
		sql.append("left join edu_recruit_major erm on erm.recruitplanid = rp.resourceid and e.LQZY = erm.recruitmajorcode  and erm.resourceid=ei.recruitmajorid and erm.isdeleted=0 ");
		sql.append("left join edu_base_major bm on erm.majorid = bm.resourceid and bm.isdeleted=0 ");// 录取专业要取基础专业
		sql.append("join EDU_BASE_CLASSIC cl on erm.classic=cl.resourceid and e.CCDM=cl.CLASSICCODE ");
		sql.append("left join HNJK_SYS_UNIT u on ei.branchschoolid = u.resourceid ");
		sql.append("inner join EDU_BASE_STUDENT s on ei.STUDENTBASEINFOID=s.resourceid and e.sfzh=s.certnum and s.isdeleted=0 ");
		sql.append("left join edu_roll_studentinfo si on si.STUDENTBASEINFOID=s.resourceid ");
		sql.append("left join edu_roll_classes rc on rc.resourceid=si.classesid");
		// sql.append("left join hnjk_sys_users ur on ur.resourceid=rc.classesmasterid");
		sql.append(" where e.isdeleted = 0 ");
		if (condition.containsKey("recruitPlanId")) {// 招生批次
			sql.append(" and rp.resourceid=? ");
			values.add(condition.get("recruitPlanId").toString());
		}
		if (condition.containsKey("major")) {//
			sql.append(" and erm.majorid = ? ");
			values.add(condition.get("major").toString());
		}
		if (condition.containsKey("classic")) {//
			sql.append(" and erm.classic = ? ");
			values.add(condition.get("classic").toString());
		}
		if (condition.containsKey("kszt")) {// 考生状态
			sql.append(" and e.KSZT=? ");
			values.add(condition.get("kszt").toString());
		}
		if (condition.containsKey("branchSchool")) {// 有学校条件
			sql.append(" and ei.branchschoolid = ?  ");
			values.add(condition.get("branchSchool").toString());
		}
		if (condition.containsKey("resIds")) {// ID
			sql.append(" and e.resourceid in (")
					.append(condition.get("resIds")).append(")");
		}
		// 2014-3-18 增加录取未注册,录取已注册的查询条件，可以在数据库改
		if (condition.containsKey("registorFlag")) {
			if (!condition.containsKey("kszt")) {// 考生状态
				sql.append(" and (e.KSZT=5 or e.KSZT=7 or e.KSZT=8)");
			}

			if ("N".equals(condition.get("registorFlag").toString())) {
				sql.append(" and ei.isDeleted = 0 AND (ei.REGISTER_FLAG is null or ei.REGISTER_FLAG='N')");
			} else if ("Y".equals(condition.get("registorFlag").toString())) {
				sql.append(" and ei.isDeleted = 0 AND ei.REGISTER_FLAG = 'Y' ");
			}
		}
		// 2016-11-29增加班主任查询条件
		if (condition.containsKey("masterid")) {// 有学校条件
			sql.append(" and rc.classesmasterid = ?  ");
			values.add(condition.get("masterid").toString());
		}
		sql.append(" order by rownum ");
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>(
				0);
		try {
			returnList = baseSupportJdbcDao.getBaseJdbcTemplate().findForList(
					sql.toString(), values.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> findEnrollStatisticalResult(
			Map<String, Object> condition) throws ServiceException {
		Map<String, Map<String, Object>> resultMap = new HashMap<String, Map<String, Object>>();
		List<String> values = new ArrayList<String>(0);
		StringBuilder sql = new StringBuilder();
		if (condition.containsKey("forResult")) {// 按照层次统计结果
			sql.append("select * from (select decode(grouping(t.classicname)+grouping(t.teachingtype),1,'合计（'||t.classicname||'）',2,'合计（总）',t.classicname||t.teachingtype) classicname,sum(t.total) total,sum(t.reg) reg,sum(t.notreg) notreg,to_char(avg(t.proportion),'999d99')||'%' proportion from (");
		}
		sql.append("select bm.majorname,ci.classicname,rm.teachingtype,count(*) total, ");
		sql.append("count(case when ee.register_flag='Y' then 1  else null end) reg, ");
		sql.append("count(case when ee.register_flag!='Y' then 1  else null end) notreg, ");
		sql.append("to_char(count(case when ee.register_flag='Y' then 1  else null end)*100/count(*),'999d99')");
		if (!condition.containsKey("forResult")) {
			sql.append("||'%'");
		}
		sql.append(" proportion from edu_recruit_examinee ei ");
		sql.append("left join edu_recruit_enrolleeinfo ee on ee.examcertificateno=ei.zkzh and ee.isdeleted=0 ");
		sql.append("left join edu_recruit_major rm on rm.recruitplanid=ei.recruitplanid and rm.resourceid=ee.recruitmajorid and rm.isdeleted=0 ");
		sql.append("left join hnjk_sys_unit u on u.resourceid=ee.branchschoolid ");//and u.resourceid=rm.brschoolid
		sql.append("left join edu_base_major bm on bm.resourceid=rm.majorid ");
		sql.append("left join edu_base_classic ci on ci.resourceid=rm.classic and ei.ccdm=ci.classiccode ");
		sql.append("where ei.isdeleted=0 ");

		if (condition.containsKey("branchSchool")) {// 教学点
			sql.append(" and u.resourceid = ?  ");
			values.add(condition.get("branchSchool").toString());
		}
		if (condition.containsKey("recruitPlanId")) {// 招生批次
			sql.append(" and ei.recruitplanid=? ");
			values.add(condition.get("recruitPlanId").toString());
		}
		if (condition.containsKey("major")) {// 专业
			sql.append(" and rm.majorid = ? ");
			values.add(condition.get("major").toString());
		}
		if (condition.containsKey("classic")) {// 层次
			sql.append(" and rm.classic = ? ");
			values.add(condition.get("classic").toString());
		}
		if (condition.containsKey("resIds")) {// ID
			sql.append(" and ei.resourceid in (")
					.append(condition.get("resIds")).append(")");
		}
		sql.append("group by rm.majorid,bm.majorname,rm.classic,ci.classicname,rm.teachingtype ");
		sql.append("order by ci.classicname,bm.majorname ");
		if (condition.containsKey("forResult")) {
			sql.append(") t group by rollup(classicname,teachingtype)) tt where tt.classicname like '%合计%'");
		}
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>(
				0);
		try {
			returnList = baseSupportJdbcDao.getBaseJdbcTemplate().findForList(
					sql.toString(), values.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnList;
	}

	@Override
	@Transactional(readOnly = true)
	public Page findExameeInfoByCondition(Page objPage,
			Map<String, Object> condition) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuilder hql = getExameeInfoHql(condition, values);
		if (objPage.isOrderBySetted()) {
			hql.append(" order by " + objPage.getOrderBy() + " "
					+ objPage.getOrder());
		}
		return findByHql(objPage, hql.toString(), values);
	}

	public StringBuilder getExameeInfoHql(Map<String, Object> condition,
			Map<String, Object> values) {
		StringBuilder hql = new StringBuilder();
		hql.append("from " + ExameeInfo.class.getSimpleName()
				+ " e where isDeleted=:isDeleted ");
		values.put("isDeleted", 0);
		if (condition.containsKey("recruitPlanId")) {// 招生批次
			hql.append(" and recruitPlan.resourceid=:recruitPlanId ");
			values.put("recruitPlanId", condition.get("recruitPlanId"));
		}
		if (condition.containsKey("name")) {// 姓名
			hql.append(" and XM like :name ");
			values.put("name", "%" + condition.get("name") + "%");
		}
		if (condition.containsKey("enrolleeCode")) {// 考生号
			hql.append(" and KSH=:enrolleeCode ");
			values.put("enrolleeCode", condition.get("enrolleeCode"));
		}
		if (condition.containsKey("certNum")) {// 证件号
			hql.append(" and SFZH=:certNum ");
			values.put("certNum", condition.get("certNum"));
		}
		if (condition.containsKey("examCertificateNo")) {// 准考证号
			hql.append(" and ZKZH=:examCertificateNo ");
			values.put("examCertificateNo", condition.get("examCertificateNo"));
		}
		if (condition.containsKey("kszt")) {// 考生状态
			hql.append(" and KSZT=:kszt ");
			values.put("kszt", condition.get("kszt"));
		}
		if (condition.containsKey("isExistsPhoto")) {// 是否存在照片
			if (Constants.BOOLEAN_YES.equals(condition.get("isExistsPhoto"))) {
				hql.append(" and mainPhotoPath is not null ");
			} else {
				hql.append(" and mainPhotoPath is null ");
			}
		}
		if (condition.containsKey("branchSchool")) {// 有学校条件
			hql.append(" and exists (from "
					+ EnrolleeInfo.class.getSimpleName()
					+ " ei where ei.examCertificateNo = e.ZKZH and ei.branchSchool.resourceid = :branchSchool ) ");
			values.put("branchSchool", condition.get("branchSchool"));
		}

		return hql;
	}

	@Override
	public void cancelExameeInfo(String[] ids) throws ServiceException {
		if (ids != null && ids.length > 0) {
			List<String> examCertificateNoList = new ArrayList<String>();
			List<String> enrolleeCodeList = new ArrayList<String>();// 考生号
			for (String id : ids) {
				ExameeInfo exameeInfo = get(id);
				exameeInfo.setKSZT("6");// 注销的考生状态
				examCertificateNoList.add(exameeInfo.getZKZH());
				enrolleeCodeList.add(exameeInfo.getKSH());
			}
			List<EnrolleeInfo> enrolleeInfoList = enrolleeInfoService
					.findByCriteria(Restrictions.eq("isDeleted", 0),
							Restrictions.in("examCertificateNo",
									examCertificateNoList), Restrictions.in(
									"enrolleeCode", enrolleeCodeList));
			for (EnrolleeInfo enrolleeInfo : enrolleeInfoList) {
				if (Constants.BOOLEAN_YES.equals(enrolleeInfo.getRegistorFlag())) {// 已经注册，不能注销
					throw new ServiceException("考生号为"+ enrolleeInfo.getEnrolleeCode()+ "的考生已经注册，无法注销入学资格.");
				} else {
					enrolleeInfo.setEntranceflag(Constants.BOOLEAN_NO);// 入学资格不通过
					enrolleeInfo.setIsMatriculate(Constants.BOOLEAN_NO);// 未录取
				}
			}
		}
	}

	@Override
	public void recoveryExameeInfo(String[] ids) throws ServiceException {
		if (ids != null && ids.length > 0) {
			List<String> examCertificateNoList = new ArrayList<String>();
			for (String id : ids) {
				ExameeInfo exameeInfo = get(id);
				if ("6".equals(exameeInfo.getKSZT())) {
					examCertificateNoList.add(exameeInfo.getZKZH());
				}
				exameeInfo.setKSZT("5");
			}
			List<EnrolleeInfo> enrolleeInfoList = enrolleeInfoService
					.findByCriteria(Restrictions.eq("isDeleted", 0),
							Restrictions.in("examCertificateNo",
									examCertificateNoList));
			for (EnrolleeInfo enrolleeInfo : enrolleeInfoList) {
				enrolleeInfo.setEntranceflag(Constants.BOOLEAN_YES);// 入学资格通过
				// enrolleeInfo.setMatriculateNoticeNo(null);
				enrolleeInfo.setIsMatriculate(Constants.BOOLEAN_YES);// 未录取
			}

		}
	}

	/**
	 * 申请审核注销入学资格
	 */
	@Override
	public void auditCancelExameeInfo(String[] ids, String status, String type)
			throws ServiceException {
		if (ids != null && ids.length > 0) {
			String kszt = null;
			if ("Apply".equals(status)) {
				if ("keepStudent".equals(type)) {
					kszt = "9";// 申请保留学籍中
				} else {
					kszt = "7";// 申请注销入学资格
				}
				for (String id : ids) {
					ExameeInfo exameeInfo = get(id);
					exameeInfo.setKSZT(kszt);
				}
			} else if ("N".equals(status)) {
				if ("keepStudent".equals(type)) {
					kszt = "5";// 已录取
				} else {
					kszt = "8";// 审核不通过注销入学资格
				}
				for (String id : ids) {
					ExameeInfo exameeInfo = get(id);
					exameeInfo.setKSZT(kszt);
				}
			} else if ("Y".equals(status)) {
				if ("keepStudent".equals(type)) {
					kszt = "10";// 已录取
				} else {
					kszt = "6";// 注销入学资格
				}
				List<String> examCertificateNoList = new ArrayList<String>();
				for (String id : ids) {
					ExameeInfo exameeInfo = get(id);
					exameeInfo.setKSZT(kszt);
					examCertificateNoList.add(exameeInfo.getZKZH());
				}
				List<EnrolleeInfo> enrolleeInfoList = enrolleeInfoService
						.findByCriteria(Restrictions.eq("isDeleted", 0),
								Restrictions.in("examCertificateNo",
										examCertificateNoList));
				for (EnrolleeInfo enrolleeInfo : enrolleeInfoList) {
					if (Constants.BOOLEAN_YES.equals(enrolleeInfo.getRegistorFlag())) {// 已经注册，不能注销
						throw new ServiceException("考生号为"+ enrolleeInfo.getEnrolleeCode()+ "的考生已经注册, 无法进行此操作.");
					} else if ("cancel".equals(type)) {
						enrolleeInfo.setEntranceflag(Constants.BOOLEAN_NO);// 入学资格不通过
						enrolleeInfo.setIsMatriculate(Constants.BOOLEAN_NO);// 未录取
					}
				}
			}
		}
	}

	@Override
	public void withDrawExameeInfo(String[] ids) throws ServiceException {
		if (ids != null && ids.length > 0) {
			List<String> examCertificateNoList = new ArrayList<String>();
			for (String id : ids) {
				ExameeInfo exameeInfo = get(id);
				exameeInfo.setKSZT("6");// 注销的考生状态
				examCertificateNoList.add(exameeInfo.getZKZH());
			}
			List<EnrolleeInfo> enrolleeInfoList = enrolleeInfoService
					.findByCriteria(Restrictions.eq("isDeleted", 0),
							Restrictions.in("examCertificateNo",
									examCertificateNoList));
			for (EnrolleeInfo enrolleeInfo : enrolleeInfoList) {
				enrolleeInfo.setEntranceflag(Constants.BOOLEAN_NO);// 入学资格不通过
				enrolleeInfo.setIsMatriculate(Constants.BOOLEAN_NO);// 未录取
				List<StudentInfo> students = studentInfoService
						.findByHql(" from " + StudentInfo.class.getSimpleName()
								+ " where isDeleted = 0 and studyno = '"
								+ enrolleeInfo.getMatriculateNoticeNo() + "' ");
				if (0 < students.size()) {
					StudentInfo student = students.get(0);
					student.setIsDeleted(1);
					student.setEnterAuditStatus(Constants.BOOLEAN_NO);
					studentInfoService.update(student);
				}
			}
		}
	}

	/**
	 * 查找注销学生信息
	 * 
	 * @return
	 * @throws ServiceException
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ExameeInfoCancelPrintVo> findExameeinfoCancel()
			throws ServiceException {
		List<ExameeInfoCancelPrintVo> examcancellist1 = new ArrayList<ExameeInfoCancelPrintVo>();
		@SuppressWarnings("rawtypes")
		List examcancellist = new ArrayList();

		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" select ");
			sql.append(" rownum as numb, ");
			sql.append(" rp.recruitPlanname as zspc, ");
			sql.append(" e.KSH as ksh, ");
			sql.append(" e.XM as xm, ");
			sql.append(" e.SFZH as zjhm, ");
			sql.append(" e.LQZYMC as lqzy, ");
			sql.append(" e.ZKZH as zkzh, ");
			sql.append(" (CASE WHEN e.XBDM='1' THEN '男' WHEN e.XBDM='2' THEN '女' END) as xb, ");
			sql.append(" e.kszt as kszt, ");
			sql.append(" to_char(e.CSRQ,'yyyy-MM-dd') as csrq, ");
			sql.append(" u.unitname as jxd, ");
			sql.append(" ei.MATRICULATENOTICENO as zxh, ");
			sql.append(" e.LXDH as lxdh, ");
			sql.append(" '' as bz, ");
			sql.append(" '' as yy ");
			sql.append(" from EDU_RECRUIT_EXAMINEE e ");
			sql.append(" inner join EDU_RECRUIT_RECRUITPLAN rp on e.RECRUITPLANID = rp.resourceid ");
			sql.append(" left join EDU_RECRUIT_ENROLLEEINFO ei on e.ZKZH = ei.EXAMCERTIFICATENO ");
			sql.append(" left join HNJK_SYS_UNIT u on ei.branchschoolid = u.resourceid ");
			sql.append(" where e.isdeleted = 0 ");
			sql.append(" and e.kszt = '6'  order by rp.startdate ,e.ksh ,e.resourceid desc ");

			Session session = exGeneralHibernateDao.getSessionFactory()
					.getCurrentSession();
			examcancellist = session.createSQLQuery(sql.toString()).list();

			List<String> dictList = Arrays
					.asList(new String[] { "CodeEnrollStatus" });
			Map<String, Object> dictOrResCheckFieldMap = dictionaryService
					.getDictionByMap(dictList, true,
							IDictionaryService.PREKEY_TYPE_BYCODE);

			for (int i = 0; i < examcancellist.size(); i++) {
				Object[] objects = (Object[]) examcancellist.get(i);
				ExameeInfoCancelPrintVo e1 = new ExameeInfoCancelPrintVo();
				if (objects != null) {
					e1.setNumb(i + 1 + "");
					e1.setZspc(objects[1] == null ? "" : objects[1].toString());
					e1.setKsh(objects[2] == null ? "" : objects[2].toString());
					e1.setXm(objects[3] == null ? "" : objects[3].toString());
					e1.setZjhm(objects[4] == null ? "" : objects[4].toString());
					e1.setLqzy(objects[5] == null ? "" : objects[5].toString());
					e1.setZkzh(objects[6] == null ? "" : objects[6].toString());
					e1.setXb(objects[7] == null ? "" : objects[7].toString());
					// dictOrResCheckFieldMap.put("kszt",
					// objects[8].toString());
					if (dictOrResCheckFieldMap.containsKey("CodeEnrollStatus_"
							+ objects[8].toString())) {
						objects[8] = dictOrResCheckFieldMap
								.get("CodeEnrollStatus_"
										+ objects[8].toString());
					}
					e1.setKszt(objects[8] == null ? "" : objects[8].toString());
					e1.setCsrq(objects[9] == null ? "" : objects[9].toString());
					e1.setJxd(objects[10] == null ? "" : objects[10].toString());
					e1.setZxh(objects[11] == null ? "" : objects[11].toString());
					e1.setLxdh(objects[12] == null ? "" : objects[12]
							.toString());
					e1.setYy(objects[13] == null ? "" : objects[13].toString());
					e1.setBz(objects[14] == null ? "" : objects[14].toString());
				}
				examcancellist1.add(e1);
			}
		} catch (Exception e) {
			logger.error("查找注销学生信息:{}", e.fillInStackTrace());
		}
		return examcancellist1;
	}

	@Override
	public String getMoblie(String resourceids) {

		List<String> returnList = new ArrayList<String>();
		List<ExameeInfo> list = new ArrayList<ExameeInfo>();
		String[] resourceid = resourceids.split("\\,");

		list = this.findByCriteria(Restrictions.eq("isDeleted", 0),
				Restrictions.in("resourceid", resourceid),
				Restrictions.isNotNull("LXDH"));
		if (list.size() > 0) {
			for (ExameeInfo e : list) {
				if (NumberUtils.isCellPhone(e.getLXDH())) {
					returnList.add(e.getLXDH());
				}
			}
		}
		if (returnList.size() > 0) {
			String returnStr = StringUtils.join(returnList, ",");
			return returnStr;
		} else {
			return "";
		}
	}

	@Override
	public int getSelectCount(Map<String, Object> condition) throws Exception {
		List<String> values = new ArrayList<String>(0);
		StringBuffer sql = new StringBuffer();
		sql.append(findExameeInfoByConditionSql(condition, values));
		List<Map<String, Object>> list = baseSupportJdbcDao
				.getBaseJdbcTemplate().findForList(sql.toString(),
						values.toArray());
		int count = 0;
		if (list.size() > 0) {
			count = list.size();
		}
		return count;
	}

	@Override
	public String getMoblieBySelect(Map<String, Object> condition)
			throws Exception {
		List<String> returnList = new ArrayList<String>();
		List<String> values = new ArrayList<String>(0);
		StringBuffer sql = new StringBuffer();
		sql.append(findExameeInfoByConditionSql(condition, values));
		List<Map<String, Object>> list = baseSupportJdbcDao
				.getBaseJdbcTemplate().findForList(sql.toString(),
						values.toArray());
		if (list.size() > 0) {
			for (Map<String, Object> map : list) {
				String mobile = (String) map.get("LXDH");
				if (NumberUtils.isCellPhone(mobile)) {
					returnList.add(mobile);
				}
			}
		}
		if (returnList.size() > 0) {
			String returnStr = StringUtils.join(returnList, ",");
			return returnStr;
		} else {
			return "";
		}
	}

	@Override
	public Page getPredistributionPage(Page objPage,
			Map<String, Object> condition) throws Exception {
		objPage.setOrder(Page.ASC);
		objPage.setOrderBy("recruitMajor.recruitMajorName");
		// Page page = null;
		try {
			StringBuffer hql = new StringBuffer();
			Map<String, Object> values = new HashMap<String, Object>();
			hql.append("from "
					+ EnrolleeInfo.class.getSimpleName()
					+ " where isDeleted=:isDeleted and registorFlag=:registorFlag and entranceflag=:entranceflag and branchSchool.unitName=:unitName ");
			values.put("isDeleted", 0);
			values.put("registorFlag", "N");
			values.put("entranceflag", "Y");
			values.put("unitName", UNDISTRIBUTED);
			if (condition.containsKey("auditStatus")) {
				hql.append(" and predistribution.auditStatus =:auditStatus");
				values.put("auditStatus", condition.get("auditStatus"));
			}
			if (condition.containsKey("recruitPlanId")) {
				hql.append(" and recruitMajor.recruitPlan.resourceid =:recruitPlanId");
				values.put("recruitPlanId", condition.get("recruitPlanId"));
			}
			if (condition.containsKey("classic")) {
				hql.append(" and recruitMajor.classic.resourceid =:classic");
				values.put("classic", condition.get("classic"));
			}
			if (condition.containsKey("major")) {
				hql.append(" and recruitMajor.major.resourceid =:major");
				values.put("major", condition.get("major"));
			}
			if (condition.containsKey("examcertificateno")) {
				hql.append(" and examcertificateno =:examcertificateno");
				values.put("examcertificateno", condition.get("examcertificateno"));
			}
			if (condition.containsKey("name")) {
				hql.append(" and studentBaseInfo.name =:name");
				values.put("name", condition.get("name"));
			}
			if (condition.containsKey("certNum")) {
				hql.append(" and studentBaseInfo.certNum =:certNum");
				values.put("certNum", condition.get("certNum"));
			}
			
			List<EnrolleeInfo> result = enrolleeInfoService.findByHql(
					hql.toString(), values);
			if (ExCollectionUtils.isNotEmpty(result)) {
				if (condition.containsKey("branchSchool")) {// 教学点对应的教学点招生专业中有这个学生，才显示出来
					List<RecruitMajor> rmList = recruitMajorService
							.findByUnitid(condition.get("branchSchool")
									.toString());
					
					if (ExCollectionUtils.isNotEmpty(rmList)) {
						Set<String> _rmList = new HashSet<String>();
						for(RecruitMajor rm:rmList){
							_rmList.add(rm.getMajor().getResourceid());
						}
						for (Iterator<EnrolleeInfo> it = result.iterator(); it
								.hasNext();) {
							EnrolleeInfo ei = it.next();
							Major rj = ei.getRecruitMajor().getMajor();
							if (!_rmList.contains(rj.getResourceid())) {// 招生专业列表中不包含学生的招生专业，移除
								it.remove();
							}
						}

					}
				}

				objPage.setAutoCount(true);
				objPage.setTotalCount(result.size());
				objPage.setResult(result.subList((objPage.getPageNum() - 1)
						* objPage.getPageSize(), objPage.getPageSize()
						* objPage.getPageNum() > result.size() ? result.size()
						: objPage.getPageSize() * objPage.getPageNum()));
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("查询未分配出错");
		}
		return objPage;
	}

	@Override
	public Map<String, Object> handleApplyDistribute(String enrollids) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuilder message = new StringBuilder();
		int statusCode = 200;
		try {
			if (ExStringUtils.isNotBlank(enrollids)) {
				User currentUser = SpringSecurityHelper.getCurrentUser();
				List<Predistribution> listPD = new ArrayList<Predistribution>();
				List<EnrolleeInfo> listEI = new ArrayList<EnrolleeInfo>();
				for (String enrollid : enrollids.split(",")) {
					EnrolleeInfo ei = enrolleeInfoService.get(enrollid);
					Predistribution pd;
					if(ExBeanUtils.isNotNullOfAll(ei.getPredistribution())){
						pd = ei.getPredistribution();
						statusCode=300;
						
						message.append("<br>学生："+ei.getStudentBaseInfo().getName()+" 身份证号"+ei.getStudentBaseInfo().getCertNum()+" 被用户 "+pd.getApplyUser().getCnName()+" 申请到 "+pd.getNextUnit().getUnitName()+"教学点，不能重复申请");
					}else{
						pd = new Predistribution();
						pd.setApplyDate(new Date());
						pd.setApplyUser(currentUser);
						pd.setPreUnit(ei.getBranchSchool());
						pd.setNextUnit(currentUser.getOrgUnit());
						pd.setAuditStatus(Predistribution.APPLY_WAIT);
						listPD.add(pd);
						ei.setPredistribution(pd);
						listEI.add(ei);
						message.setLength(0);
						message.append("申请成功！");
					}
				}
				if (ExCollectionUtils.isNotEmpty(listPD)) {
					exGeneralHibernateDao.batchSaveOrUpdate(listPD);
				}
				if (ExCollectionUtils.isNotEmpty(listEI)) {
					enrolleeInfoService.batchSaveOrUpdate(listEI);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			statusCode = 300;
			message.setLength(0);
			message.append(e.getMessage());
		} finally {
			map.put("message", message.toString());
			map.put("statusCode", statusCode);
		}
		return map;
	}
	@Override
	public Map<String, Object> handleDeleteApplyDistribute(String resourceid) {
		Map<String, Object> map = new HashMap<String, Object>();
		String message = "撤销成功！";
		int statusCode = 200;
		try {
			if (ExStringUtils.isNotBlank(resourceid)) {
				EnrolleeInfo ei = enrolleeInfoService.get(resourceid);
				Predistribution pd = new Predistribution();
				pd.setIsDeleted(1);
				ei.setPredistribution(null);
				exGeneralHibernateDao.saveOrUpdate(pd);
				enrolleeInfoService.saveOrUpdate(ei);

			}
		} catch (Exception e) {
			e.printStackTrace();
			statusCode = 300;
			message = e.getMessage();
		} finally {
			map.put("message", message);
			map.put("statusCode", statusCode);
		}
		return map;
	}
	@Override
	public Map<String,Object> handleApplyDistributeResult(String resourceid,String auditFlag){
		Map<String, Object> map = new HashMap<String, Object>();
		String message = "操作成功！";
		int statusCode = 200;
		try {
			if (ExStringUtils.isNotBlank(resourceid)) {
				User currentUser = SpringSecurityHelper.getCurrentUser();
				List<Predistribution> listPD = new ArrayList<Predistribution>();
				List<EnrolleeInfo> listEI = new ArrayList<EnrolleeInfo>();
				for (String enrollid : resourceid.split(",")) {
					EnrolleeInfo ei = enrolleeInfoService.get(enrollid);
					Predistribution pd = ei.getPredistribution();
					if(ExBeanUtils.isNotNullOfAll(pd)){
						pd.setAuditUser(currentUser);
						pd.setAuditDate(new Date());
						pd.setAuditStatus(auditFlag);
						listPD.add(pd);
						if(auditFlag.equals(Predistribution.APPLY_NOPASS)){
							//不通过的情况下，将去掉预分配
							ei.setPredistribution(null);						
						}else{
							//通过的话， 修改学生的教学点为申请的教学点
							ei.setBranchSchool(pd.getApplyUser().getOrgUnit());
						}
						listEI.add(ei);
					}
					
				}
				if (ExCollectionUtils.isNotEmpty(listPD)) {
					exGeneralHibernateDao.batchSaveOrUpdate(listPD);
				}
				if (ExCollectionUtils.isNotEmpty(listEI)) {
					enrolleeInfoService.batchSaveOrUpdate(listEI);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			statusCode = 300;
			message = e.getMessage();
		} finally {
			map.put("message", message);
			map.put("statusCode", statusCode);
		}
		return map;
	}
}
