package com.hnjk.edu.learning.controller;

import java.io.File;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.cache.MemcachedManager;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.PersonalRalation;
import com.hnjk.edu.basedata.model.StudentBaseInfo;
import com.hnjk.edu.basedata.model.StudentResume;
import com.hnjk.edu.basedata.service.IEdumanagerService;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IStudentService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.finance.model.StudentPayment;
import com.hnjk.edu.finance.service.IStudentPaymentService;
import com.hnjk.edu.learning.model.BbsGroup;
import com.hnjk.edu.learning.model.BbsSection;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.IBbsGroupService;
import com.hnjk.edu.learning.service.IBbsSectionService;
import com.hnjk.edu.learning.service.IBbsTopicService;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.roll.model.GraduateData;
import com.hnjk.edu.roll.model.StuChangeInfo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IGraduateDataService;
import com.hnjk.edu.roll.service.IStuChangeInfoService;
import com.hnjk.edu.roll.service.IStudentBaseInfoService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.TeachingActivityTimeSetting;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.IStudentExamResultsService;
import com.hnjk.edu.teaching.service.impl.ExamSubServiceImpl;
import com.hnjk.edu.teaching.vo.StudentExamResultsVo;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.model.UserExtends;
/**
 * 
 * <code>学员学习计划 Controller</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-6 下午05:09:04
 * @see 
 * @version 1.0
 */
@Controller
public class StudentLearnPlanController extends FileUploadAndDownloadSupportController{

	private static final long serialVersionUID = -1336011490542925104L;

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
//	@Autowired
//	@Qualifier("teachtaskservice")
//	private ITeachTaskService teachTaskService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
//	@Autowired
//	@Qualifier("stuperpayfeeservice")
//	private IStuPerpayfeeService stuPerpayfeeService;
	
	@Autowired
	@Qualifier("studentExamResultsService")
	private IStudentExamResultsService studentExamResultsService;	
	
	@Autowired
	@Qualifier("bbsGroupService")
	private IBbsGroupService bbsGroupService;
	
	@Autowired
	@Qualifier("bbsSectionService")
	private IBbsSectionService bbsSectionService;
	
	@Autowired
	@Qualifier("bbsTopicService")
	private IBbsTopicService bbsTopicService;
	
	@Autowired
	@Qualifier("edumanagerService")
	private IEdumanagerService edumanagerService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("stuchangeinfoservice")
	private IStuChangeInfoService stuchangeinfoservice;
	
	@Autowired
	@Qualifier("graduatedataservice")
	private IGraduateDataService graduateDataService;
	
	@Autowired
	@Qualifier("stuchangeinfoservice")
	private IStuChangeInfoService stuChangeInfoService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("studentBaseInfoService")
	private IStudentBaseInfoService studentBaseInfoService;	
	
	@Autowired
	@Qualifier("studentPaymentService")
	private IStudentPaymentService studentPaymentService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("memcacheManager")
	private MemcachedManager memcachedManager;
	
	@Autowired
	@Qualifier("studentService")
	private IStudentService studentService;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	
	public String changeStudentLearnStatus(HttpServletRequest request,HttpServletResponse response){
		
		return "";
	}
	
	/**
	 * 课程老师
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/student/coursetask.html")
	public String listStudentCourseTask(HttpServletRequest request, ModelMap model) throws WebException {
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		String roleStudent = CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue();
		if(SpringSecurityHelper.isUserInRole(roleStudent)){// 学生
			User user = SpringSecurityHelper.getCurrentUser();
			String studentId = "";
			if(null!=user.getUserExtends()&&null!=user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
				studentId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
			}		
			if(ExStringUtils.isNotBlank(studentId)){
				StudentInfo student = studentInfoService.get(studentId);
				model.addAttribute("studentInfo", student);
			}
		}		
		return "/edu3/learning/studentleanplan/student-learningtask";
	}
	
	/**
	 * 得到组合的学籍状态
	 * @param model
	 * @return
	 */
	private ModelMap getNewStudentStatus(ModelMap model){
		List<Dictionary> children = CacheAppManager.getChildren("CodeStudentStatus");
		StringBuffer studentStatusRes = new StringBuffer();
		for (Dictionary childDict : children) {
			String code = childDict.getDictName();
			String value = childDict.getDictValue();
			if("11".equals(value)){
				studentStatusRes.append("a"+value+",正常注册,");
				studentStatusRes.append("b"+value+",正常未注册,");
			}else{
				studentStatusRes.append(value+"," +code+",");
			}
		}
		model.addAttribute("stuStatusSet",studentStatusRes.toString());
		return model;
	}
	/**
	 * 我的信息
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/student/studentinfo/view.html")
	public String listStudentInfo(HttpServletRequest request, ModelMap model) throws WebException {
		User user = SpringSecurityHelper.getCurrentUser();
		List<StudentInfo> stuList = studentInfoService.findStuListByUserId(user.getResourceid());			
		try {
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			if(ExCollectionUtils.isNotEmpty(stuList)){
				Map<String,Object> stu = null;
				String degreeStatus = null;
				String degreeApplyStatus = null;
				String isOpen = "N";
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
				String dangqian = df.format(new Date());
				String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();
				String yearInfoId = "";
				String term = "";
				if (null!=yearTerm) {
					String[] ARRYyterm = yearTerm.split("\\.");
					yearInfoId = yearInfoService.getByFirstYear(Long.parseLong(ARRYyterm[0])).getResourceid();
					term = ARRYyterm[1];
					//教学活动时间
					Map<String,Object> map = new HashMap<String, Object>(); //查询参数
					map.put("isDeleted", 0); //是否删除 0：未删除 1：已删除
					map.put("yearid", yearInfoId==null?"":yearInfoId); //年级
					map.put("term", term); //学期
					map.put("acttype", "studyCard"); //活动类型 字典：CodeTeachingActivity
					List<TeachingActivityTimeSetting> listActivity = studentLearnPlanService.findTeachingactivitytime(map); //查看默认年级下是否设置了类型为修改学籍表的活动时间
					if(null != listActivity){
						if(listActivity.size() > 0){
							Date ks = listActivity.get(0).getStartTime(); //开始时间
							Date js = listActivity.get(0).getEndTime(); //结束时间
							Date dq = df.parse(dangqian); //当前时间
							if (dq.getTime()>ks.getTime()&& dq.getTime()< js.getTime()) { //判断现在的时间是否符合
								isOpen = "Y";
							}
						}
					}
				}
				
				for (StudentInfo studentInfo : stuList) {
					stu = new HashMap<String, Object>();
					//因为需要修改籍贯，籍贯需要将一个homeplace的字段转换成省市区三级
					String homePlace = ExStringUtils.trimToEmpty(studentInfo.getStudentBaseInfo().getHomePlace());	
					//------------由于中文的命名引起图片预览不能正常显示，注释掉  20160525
//					if(user.getCnName()!=null){
//						model.addAttribute("storeDir", user.getCnName());	
//					}
					//当前系统时间和页面设置的时间对比大小，以决定是否开启学生修改学籍的页面
					//start
					String start = ExStringUtils.trimToEmpty(studentInfo.getOpenStartDate());
					String end = ExStringUtils.trimToEmpty(studentInfo.getOpenEndDate());	
					model.addAttribute("rollCardStatus", studentInfo.getRollCardStatus());
					if("".equals(start)||"".equals(end)){	
						model.addAttribute("open", "N");
					}else{
						Date ks = df.parse(start);
						Date js = df.parse(end);
						Date dq = df.parse(dangqian);				
						if (dq.getTime()>ks.getTime()&& dq.getTime()< js.getTime()) {
							model.addAttribute("open", "Y");
						}else{
							model.addAttribute("open", "N");
						}
					}
					//end
					
					if("1".equals(studentInfo.getAuditResults())){ //审核不通过或者待审核才可对图片进行修改
						model.addAttribute("stuts", "1");
					}else{
						model.addAttribute("stuts", "2");
					}
					
					if("N".equals(model.get("open"))){ //原先是通过学籍表里设置的时间去判断 现在加了一种在教学活动力设置了整体的修改学籍表时间
						/*//默认年级
						Grade defaultGrade = gradeService.getDefaultGrade();//默认年级
						if(null != defaultGrade){*/
						model.addAttribute("open", isOpen);
						
					}
					
					String[] homePlaces = homePlace.split(",");
					if (homePlaces != null && homePlaces.length > 2) {
						model.addAttribute("homePlaceProvince", homePlaces[0]);
						model.addAttribute("homePlaceCity", homePlaces[1]);
						model.addAttribute("homePlaceDistrict", homePlaces[2]);
					}else{
					//主要是针对有些籍贯数据并没有完整的,分隔 历史导入
					//二级行政单位有以下城市不以市/自治州作为结尾的
					String[] specialCitys = {"延边州","恩施州","湘西州","阿坝州","甘孜州","凉山州","黔东南州","黔南州","黔西南州","德宏州","怒江州","迪庆州","大理州","楚雄州","红河州","文山州","西双版纳州","临夏州","甘南州","海北州","海南州","黄南州","果洛州","玉树州","海西州","克孜勒苏柯州","博尔塔拉州","昌吉州","巴音郭楞州","伊犁州","东城区","西城区","崇文区","宣武区","朝阳区","丰台区","石景山区","海淀区","门头沟区","房山区","通州区","顺义区","昌平区","大兴区","怀柔区","平谷区","密云县","延庆县","延庆镇","和平区","河东区","河西区","南开区","河北区","红桥区","塘沽区","汉沽区","大港区","东丽区","西青区","津南区","北辰区","武清区","宝坻区","蓟县","宁河县","芦台镇","静海县","静海镇","黄浦区","卢湾区","徐汇区","长宁区","静安区","普陀区","闸北区","虹口区","杨浦区","闵行区","宝山区","嘉定区","浦东新区","金山区","松江区","青浦区","南汇区","奉贤区","崇明县","城桥镇","渝中区","大渡口区","江北区","沙坪坝区","九龙坡区","南岸区","北碚区","万盛区","双桥区","渝北区","巴南区","万州区","涪陵区","黔江区","长寿区","綦江县","潼南县","铜梁县","大足县","荣昌县","璧山县","垫江县","武隆县","丰都县","城口县","梁平县","开县","巫溪县","巫山县","奉节县","云阳县","忠县","石柱土家族自治县","彭水苗族土家族自治县","酉阳土家族苗族自治县","秀山土家族苗族自治县","兴安盟","锡林郭勒盟","阿拉善盟","大兴安岭地区","省直辖县级行政单位","省直辖行政单位","毕节地区","铜仁地区","那曲地区","昌都地区","林芝地区","山南地区","日喀则地区","阿里地区","海东地区","自治区直辖县级行政单位","喀什地区","阿克苏地区","和田地区","吐鲁番地区","哈密地区","塔城地区","阿勒泰地区","中西区","东区","九龙城区","观塘区","南区","深水埗区","湾仔区","黄大仙区","油尖旺区","离岛区","葵青区","北区","西贡区","沙田区","屯门区","大埔区","荃湾区","元朗区","澳门特别行政区","台北","高雄","台中","花莲","基隆","嘉义","金门","连江","苗栗","南投","澎湖","屏东","台东","台南","桃园","新竹","宜兰","云林","彰化"};
					homePlace = homePlace.replaceAll(",", "");
					String hpProvince = "";
					String hpCity = "";
					String hpDistrict="";
					if(homePlace.contains("省")){
						hpProvince = homePlace.substring(0, homePlace.indexOf("省")+1);
					}else if(homePlace.contains("特别行政区")||homePlace.contains("自治区")){
						hpProvince = homePlace.substring(0, homePlace.indexOf("区")+1);
					}else if(homePlace.contains("北京市")||homePlace.contains("上海市")||homePlace.contains("天津市")||homePlace.contains("重庆市")) {
						hpProvince = homePlace.substring(0, homePlace.indexOf("市")+1);
					}
					homePlace = homePlace.replaceAll(hpProvince, "");
					if(homePlace.contains("市")){
						hpCity = homePlace.substring(0,homePlace.indexOf("市")+1);
					}else if (homePlace.contains("自治州")){
						hpCity = homePlace.substring(0,homePlace.indexOf("州")+1);
					}
					for (String scity : specialCitys) {
						if(homePlace.contains(scity)){
							hpCity = scity;
							break;
						}
					}
					homePlace = homePlace.replaceAll(hpCity, "");
					hpDistrict=homePlace;
					model.addAttribute("homePlaceProvince", hpProvince);
					model.addAttribute("homePlaceCity", hpCity);
					model.addAttribute("homePlaceDistrict", hpDistrict);
					specialCitys = null;
					}
					//户口所在地
					String residence = ExStringUtils.trimToEmpty(studentInfo.getStudentBaseInfo().getResidence());
					String[] residences = residence.split(",");
					if (residences != null && residences.length > 2) {
						model.addAttribute("HouseholdRegisterationProvince", residences[0]);
						model.addAttribute("HouseholdRegisterationCity", residences[1]);
						model.addAttribute("HouseholdRegisterationDistrict", residences[2]);
					}else{
					//主要是针对有些户口数据并没有完整的,分隔  历史导入
					//二级行政单位有以下城市不以市/自治州作为结尾的
					String[] specialCitys = {"延边州","恩施州","湘西州","阿坝州","甘孜州","凉山州","黔东南州","黔南州","黔西南州","德宏州","怒江州","迪庆州","大理州","楚雄州","红河州","文山州","西双版纳州","临夏州","甘南州","海北州","海南州","黄南州","果洛州","玉树州","海西州","克孜勒苏柯州","博尔塔拉州","昌吉州","巴音郭楞州","伊犁州","东城区","西城区","崇文区","宣武区","朝阳区","丰台区","石景山区","海淀区","门头沟区","房山区","通州区","顺义区","昌平区","大兴区","怀柔区","平谷区","密云县","延庆县","延庆镇","和平区","河东区","河西区","南开区","河北区","红桥区","塘沽区","汉沽区","大港区","东丽区","西青区","津南区","北辰区","武清区","宝坻区","蓟县","宁河县","芦台镇","静海县","静海镇","黄浦区","卢湾区","徐汇区","长宁区","静安区","普陀区","闸北区","虹口区","杨浦区","闵行区","宝山区","嘉定区","浦东新区","金山区","松江区","青浦区","南汇区","奉贤区","崇明县","城桥镇","渝中区","大渡口区","江北区","沙坪坝区","九龙坡区","南岸区","北碚区","万盛区","双桥区","渝北区","巴南区","万州区","涪陵区","黔江区","长寿区","綦江县","潼南县","铜梁县","大足县","荣昌县","璧山县","垫江县","武隆县","丰都县","城口县","梁平县","开县","巫溪县","巫山县","奉节县","云阳县","忠县","石柱土家族自治县","彭水苗族土家族自治县","酉阳土家族苗族自治县","秀山土家族苗族自治县","兴安盟","锡林郭勒盟","阿拉善盟","大兴安岭地区","省直辖县级行政单位","省直辖行政单位","毕节地区","铜仁地区","那曲地区","昌都地区","林芝地区","山南地区","日喀则地区","阿里地区","海东地区","自治区直辖县级行政单位","喀什地区","阿克苏地区","和田地区","吐鲁番地区","哈密地区","塔城地区","阿勒泰地区","中西区","东区","九龙城区","观塘区","南区","深水埗区","湾仔区","黄大仙区","油尖旺区","离岛区","葵青区","北区","西贡区","沙田区","屯门区","大埔区","荃湾区","元朗区","澳门特别行政区","台北","高雄","台中","花莲","基隆","嘉义","金门","连江","苗栗","南投","澎湖","屏东","台东","台南","桃园","新竹","宜兰","云林","彰化"};
					residence = residence.replaceAll(",", "");
					String rProvince = "";
					String rCity = "";
					String rDistrict="";
					if(residence.contains("省")){
						rProvince = residence.substring(0, residence.indexOf("省")+1);
					}else if(residence.contains("特别行政区")||residence.contains("自治区")){
						rProvince = residence.substring(0, residence.indexOf("区")+1);
					}else if(residence.contains("北京市")||residence.contains("上海市")||residence.contains("天津市")||residence.contains("重庆市")) {
						rProvince = residence.substring(0, residence.indexOf("市")+1);
					}
					residence = residence.replaceAll(rProvince, "");
					if(residence.contains("市")){
						rCity = residence.substring(0,residence.indexOf("市")+1);
					}else if (residence.contains("自治州")){
						rCity = residence.substring(0,residence.indexOf("州")+1);
					}
					
					for (String scity : specialCitys) {
						if(residence.contains(scity)){
							rCity = scity;
							break;
						}
					}
					residence = residence.replaceAll(rCity, "");
					rDistrict=residence;
					model.addAttribute("HouseholdRegisterationProvince", rProvince);
					model.addAttribute("HouseholdRegisterationCity", rCity);
					model.addAttribute("HouseholdRegisterationDistrict", rDistrict);
					specialCitys=null;
					}					
					stu.put("studentInfo", studentInfo);

					//查找该学生的考试成绩
					List<StudentExamResultsVo> examResults =  studentExamResultsService.studentExamResultsList(studentInfo,null);
					stu.put("examResults", examResults);
					//查找该学生缴费标准
					Map<String,Object> condition = new HashMap<String, Object>();
					condition.put("studyNo", studentInfo.getStudyNo());
					condition.put("studentInfoId", studentInfo.getResourceid());
					List<StudentPayment> studentPayments =  studentPaymentService.findStudentPaymentByCondition(condition);
					stu.put("stuFactFees", studentPayments);	
//					List<StuPerpayfee> perpayfees = stuPerpayfeeService.findByHql("from "+StuPerpayfee.class.getSimpleName()+" where isDeleted = ? and studentInfo.resourceid = ?", 
//							0,studentInfo.getResourceid());
//					if(null != perpayfees && perpayfees.size()>0){
//						stu.put("stuPerpayfee", perpayfees);
//						stu.put("stuPayfeeRule", perpayfees.get(0).getStudentFeeRule());
//					}	
//					List<StudentFactFee> stuFactFees = stuPerpayfeeService.findStudentFactFeeByCondition(condition);
//					stu.put("stuFactFees", stuFactFees);					
					if(null != user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
						String defaultStudentId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
						if(ExStringUtils.equals(studentInfo.getResourceid(), defaultStudentId)){
							stu.put("isDefaultStudentId", Constants.BOOLEAN_YES);
						}
					}
					// 获取学位信息,是毕业状态才获取
					if("16".equals(studentInfo.getStudentStatus())){
						GraduateData gd = graduateDataService.findByHql(studentInfo);
						if(gd != null){
							degreeStatus = gd.getDegreeStatus();
							degreeApplyStatus = gd.getDegreeApplyStatus();
						}
					}
					stu.put("degreeStatus", degreeStatus);
					stu.put("degreeApplyStatus", degreeApplyStatus);
					result.add(stu);
				}
			}
			
			model.addAttribute("stuList",result);
		} catch (Exception e) {
			logger.error("查看学生学籍出错：{}",e.fillInStackTrace());
		}

		model.addAttribute("schoolCode" ,CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue());
		model.addAttribute("rootUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
				+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());//拿到附件映射的全局URL	
		return "/edu3/learning/studentleanplan/student-studentinfo";
	}
	/**
	 * 我的学籍异动信息
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/student/studentchangeinfo/view.html")
	public String listStudentChangeInfo(HttpServletRequest request, ModelMap model) throws WebException {
		User user = SpringSecurityHelper.getCurrentUser();
		List<StudentInfo> stuList = studentInfoService.findStuListByUserId(user.getResourceid());	
		try {
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			if(ExCollectionUtils.isNotEmpty(stuList)){
				for (StudentInfo studentInfo : stuList) {
					Map<String,Object> tmp = new HashMap<String, Object>(0);
					String key = "学号:"+studentInfo.getStudyNo();
					List<StuChangeInfo> stuChangeInfoList = stuChangeInfoService.findByHql("from "+StuChangeInfo.class.getSimpleName()+" where isDeleted = ? and studentInfo.resourceid = ? order by applicationDate desc ", 0,studentInfo.getResourceid());
					tmp.put("key", key);
					tmp.put("studentid",studentInfo.getResourceid());
					tmp.put("stuChangeInfoList", stuChangeInfoList);
					if(null != user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
						String defaultStudentId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
						if(ExStringUtils.equals(studentInfo.getResourceid(), defaultStudentId)){
							tmp.put("isDefaultStudentId", Constants.BOOLEAN_YES);
						}
					}					
					result.add(tmp);
				}
			}
			model.addAttribute("stuChangeInfoList",result);
		} catch (Exception e) {
			logger.error("查看学生学籍异动出错：{}",e.fillInStackTrace());
		}			
		return "/edu3/learning/studentleanplan/student-studentchangeinfo";
	}
	
	/**
	 * 异步保存学生相片
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/student/studentinfo/ajaxSavePicture.html")
	public void saveStudentPicture(HttpServletRequest request,HttpServletResponse response, ModelMap model) throws WebException {
		Map<String,Object> map = new HashMap<String, Object>(0);
		try {
			String resourceid = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
			String photoPath  = ExStringUtils.trimToEmpty(request.getParameter("photoPath"));
			StudentBaseInfo baseInfo = studentBaseInfoService.get(resourceid);
			baseInfo.setPhotoPath(photoPath);
			studentBaseInfoService.update(baseInfo);
			map.put("statusCode","200" );
		} catch (Exception e) {
			map.put("statusCode","300" );
		}			
		renderJson(response,JsonUtils.mapToJson(map));				
	}
	
	@RequestMapping("/edu3/student/roll-card-form.html")
	public String studentRollInfoCardFormTab(HttpServletRequest request, ModelMap model,String studentId){
		User user 					    	= SpringSecurityHelper.getCurrentUser();
		List<StudentInfo> listStu =studentInfoService.findStuListByUserId(user.getResourceid());
		model.addAttribute("listStu", listStu);
		StudentInfo studentInfo = null;
		if(ExStringUtils.isNotBlank(studentId)){
			studentInfo = studentInfoService.get(studentId);
			String currentIndex = "0";
			if(!studentId.equals(listStu.get(0).getResourceid())){
				currentIndex="1";
			}
			model.addAttribute("currentIndex", currentIndex);
			return studentRollInfoCardForm(request,model,studentInfo);
		}else{
			return studentRollInfoCardForm(request,model,listStu.get(0));
		}
		
	}
	
	/**
	 * 我的学籍卡-表单
	 * @param request
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String studentRollInfoCardForm(HttpServletRequest request, ModelMap model,StudentInfo studentInfo){
		User user 					    	= SpringSecurityHelper.getCurrentUser();
		/**
		 * update 2014-6-24 17:34:10  调整页面写死的学校名称
		 */
		model.addAttribute("schoolCode" ,CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode"));
		model.put("schoolname", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue()+CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue()); //全局参数 :学校名称
		model.put("phoneComfirm", CacheAppManager.getSysConfigurationByCode("phoneComfirm").getParamValue()); //全局参数 :手机短信验证
		model.put("validTime", CacheAppManager.getSysConfigurationByCode("validTime").getParamValue()); //全局参数 :验证码有效时间
		
		if (user.isContainRole("ROLE_STUDENT")||user.isContainRole("ROLE_GRADUATE")) {
						
			studentInfo=ExBeanUtils.isNullOfAll(studentInfo)?studentInfoService.getStudentInfoByUser(user):studentInfo;
			Set<StudentResume> set_res	    = studentInfo.getStudentBaseInfo().getStudentResume();
			Set<PersonalRalation> set_rln   = studentInfo.getStudentBaseInfo().getStudentRalation();
			List<StudentResume> resums 	    = new ArrayList<StudentResume>(set_res);
			List<PersonalRalation> f_rals   = new ArrayList<PersonalRalation>();
			List<PersonalRalation> s_rals   = new ArrayList<PersonalRalation>();
			//获取后5条个人简历记录
			List<StudentResume> subResumes  =  resums.size()>5?resums.subList(resums.size()-5, resums.size()):resums;
			/*重新排序
			Collections.sort(subResumes, new Comparator<StudentResume>(){
				@Override
				public int compare(StudentResume o1, StudentResume o2) {
					return o2.getStartYear().compareTo(o1.getStartYear());
				}
			} );*/
			
//			String name = studentInfo.getStudentBaseInfo().getName();
			
			//List<StuChangeInfo> scList = stuChangeInfoService.findStuListByUserId(name);
//			 String hql = " from "+StuChangeInfo.class.getSimpleName()+" where 1=1 ";
//			 
//			 hql += " and studentInfo.studentName = '"+ name+"' ";
			/**
			 * 此前使用学生名字进行查询，当名字重复时便会关联错误的信息，改为使用studentInfo.resourceid 进行查询   date:20160406
			 */
			List<StuChangeInfo> scList = stuChangeInfoService.findByHql(" from "+StuChangeInfo.class.getSimpleName() + " where isDeleted = ? and  studentInfo.resourceid = ? ",0,studentInfo.getResourceid());
			StringBuffer stuChangeInfo = new StringBuffer();
			String YES="";
			if(scList!=null){
			
			for (StuChangeInfo sci:scList) {
				Map<String,Object> condition = new HashMap<String,Object>();
				
				int num =0;
				if(sci!=null){
					String unit = null;
					String ounit = null;
					if(studentInfo!=null){
						if(studentInfo.getBranchSchool()!=null){
							unit = studentInfo.getBranchSchool().getUnitShortName();//现学院							
						}
					}
					if(sci.getChangeBeforeBrSchool()!=null){	
						ounit = sci.getChangeBeforeBrSchool().getUnitShortName();
						//condition.put("id", id);
						//List<OrgUnit> unList = stuChangeInfoService.findStuListByUnit(id);
						//for(OrgUnit ou:unList){
						//	ounit = ou.getUnitShortName();//原学院
						//}
					}	
					if(unit=="" || unit==null ){unit="-";}
					if(ounit=="" || ounit==null){ounit="-";}
					if(!unit.equals(ounit)){						
						num++;
						stuChangeInfo.append(num+"、"+"学院变更：由"+ounit+"变更为"+unit+";    <br/>");	
						
					}					
				}		
				
				if(sci!=null){	
					String major = studentInfo.getMajor().getMajorName();
					String omajor=sci.getChangeBeforeMajorName();
					if(omajor==null || omajor==""  ){
						if(sci.getChangeBeforeTeachingGuidePlan()!=null){
							omajor = sci.getChangeBeforeTeachingGuidePlan().getTeachingPlan().getMajor().getMajorName();
						}else{
							omajor ="-";
						}
					}else{
						omajor = sci.getChangeBeforeMajorName();//原专业
					}
					if(major=="" || major==null ){
						major="-";}
					if(!major.equals(omajor)){
						num++;
						stuChangeInfo.append(num+"、"+"专业变更：由"+omajor+"变更为"+major+";    <br/>");		
					}
				}
				if(sci!=null){
					String classic = studentInfo.getClassic().getShortName();//现层次					
					String oclassic=sci.getChangeBeforeClassicName();
					if(oclassic==null){
						if(sci.getChangeBeforeTeachingGuidePlan()!=null){
							oclassic= sci.getChangeBeforeTeachingGuidePlan().getTeachingPlan().getClassic().getShortName();
						}else{
							oclassic="-";
						}						
					}else{
						oclassic = sci.getChangeBeforeClassicName();//原层次
					}
					if(classic=="" || classic==null ){classic="-";}
					if(!classic.equals(oclassic)){
						num++;
						stuChangeInfo.append(num+"、"+"层次变更：由"+oclassic+"变更为"+classic+";    <br/>");	
					}
				}
				if(sci!=null){
					String classes= null;
					String oclasses=null;
					if(sci.getChangeBeforeClass()!=null){
						oclasses = sci.getChangeBeforeClass().getClassname();//原班级	
					}	
						if(studentInfo.getClasses()!=null){
							classes = studentInfo.getClasses().getClassname();//现班级		
					}	
						if(oclasses=="" || oclasses==null ){oclasses="-";}
						if(classes=="" || classes==null ){classes="-";}
					if(!classes.equals(oclasses)){
						num++;
						stuChangeInfo.append(num+"、"+"班级变更：由"+oclasses+"变更为"+classes+";    <br/>");						
					}
				}
				if(sci!=null){
					String teachingType = null;
					String oteachType = sci.getChangeBeforeLearingStyle();//原形式	
					if(studentInfo!=null){		
						teachingType = studentInfo.getTeachingType();//现形式	
					}	
					if(teachingType=="" || teachingType==null ){teachingType="-";}
					if(oteachType=="" || oteachType==null){oteachType="-";}
					if("1".equals(teachingType)){
						teachingType="集中面授";
					}else if("2".equals(teachingType)){
						teachingType="业余";
					}else if ("3".equals(teachingType)){
						teachingType="走读";
					}else if ("4".equals(teachingType)){
						teachingType="函授";
					}else if ("5".equals(teachingType)){
						teachingType="相沟通";
					}else if ("6".equals(teachingType)){
						teachingType="培训";
					}else if ("7".equals(teachingType)){
						teachingType="脱产";
					}else if("net".equals(teachingType)){
						teachingType="网络教育";
					}else if ("specialbatch".equals(teachingType)){
						teachingType="特批";
					}
					
					
					if("1".equals(oteachType)){
						oteachType="集中面授";
					}else if("2".equals(oteachType)){
						oteachType="业余";
					}else if ("3".equals(oteachType)){
						oteachType="走读";
					}else if ("4".equals(oteachType)){
						oteachType="函授";
					}else if ("5".equals(oteachType)){
						oteachType="相沟通";
					}else if ("6".equals(oteachType)){
						oteachType="培训";
					}else if ("7".equals(oteachType)){
						oteachType="脱产";
					}else if("net".equals(oteachType)){
						oteachType="网络教育";
					}else if ("specialbatch".equals(oteachType)){
						oteachType="特批";
					}
					if(!teachingType.equals(oteachType)){
						num++;
						stuChangeInfo.append(num+"、"+"形式变更：由"+oteachType+"变更为"+teachingType);
						
					}
				}				
					if(num==0){
						model.put("stuChangeInfo", "<br/>无变更<br/>");
						YES ="YES";
					}else if(num==1){
						model.put("stuChangeInfo","<br/>"+stuChangeInfo);
						YES ="YES";
					}else{
						YES ="YES";
						model.put("stuChangeInfo", stuChangeInfo);
					}
				
				}
				if(YES!="YES"){
					model.put("stuChangeInfo", "<br/>无变更<br/>");
				}
				
				
			}
			
			for (PersonalRalation ralation:set_rln) {
				if (PersonalRalation.RALATIONTYPE_F.equals(ralation.getRalationType())) {
					f_rals.add(ralation);
				}
				if (PersonalRalation.RALATIONTYPE_S.equals(ralation.getRalationType())) {
					s_rals.add(ralation);
				}
			}
			
			List<GraduateData> graduateList = graduateDataService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.eq("studentInfo",studentInfo));
			if (ExCollectionUtils.isNotEmpty(graduateList)) {
				model.put("graduateData", graduateList.get(0));
			}
			Map<String,StuChangeInfo> changeRecord = stuchangeinfoservice.findStudentChangeRecord(studentInfo.getResourceid());
			String[] homePlace = ExStringUtils.isNotBlank(studentInfo.getStudentBaseInfo().getHomePlace())?studentInfo.getStudentBaseInfo().getHomePlace().split(","):null;
			if (homePlace != null && homePlace.length > 0) {
				model.addAttribute("homePlace_province", homePlace[0]);
				model.addAttribute("homePlace_city", homePlace.length > 1 ? homePlace[1] : "");
				model.addAttribute("homePlace_county", homePlace.length > 2 ? homePlace[2] : "");
			}else {
				model.addAttribute("homePlace_province", "广东省");
				model.addAttribute("homePlace_city", "广州市");
				model.addAttribute("homePlace_county", "天河区");
			}
			
			String[] residence = ExStringUtils.isNotBlank(studentInfo.getStudentBaseInfo().getResidence())?studentInfo.getStudentBaseInfo().getResidence().split(","):null;
			if (residence != null && residence.length > 0) {
				model.addAttribute("residence_province", residence[0]);
				model.addAttribute("residence_city", residence.length > 1 ? residence[1] : "");
				model.addAttribute("residence_county", residence.length > 2 ? residence[2] : "");
			}else {
				model.addAttribute("residence_province", "广东省");
				model.addAttribute("residence_city", "广州市");
				model.addAttribute("residence_county", "天河区");
			}
			String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
			model.put("schoolCode", schoolCode);
			model.put("change_1", changeRecord.containsKey("majorChange")?changeRecord.get("majorChange"):null);
			model.put("change_2", changeRecord.containsKey("brSchoolChange")?changeRecord.get("majorChange"):null);
			model.put("change_3", changeRecord.containsKey("otherChange")?changeRecord.get("majorChange"):null);
			model.put("f_rals", f_rals);
			model.put("s_rals", s_rals);
			model.put("resums", subResumes);
			model.put("studentInfo", studentInfo);
			model.put("rootUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
								 + CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());//拿到附件映射的全局URL
		}
		return "/edu3/learning/studentleanplan/student-card";
	}
	/**
	 * 我的学籍卡-保存/提交
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/student/roll-card-save.html")
	public void studentRollInfoCardSave(HttpServletRequest request,HttpServletResponse response){
		String submit = ExStringUtils.trimToEmpty(request.getParameter("type"));
		String msgAuthCode = StringUtils.trimToEmpty(request.getParameter("msgAuthCode"));	// 短信验证码
		String mobile   	  = ExStringUtils.trimToEmpty(request.getParameter("mobile"));
		String phoneComfirm = CacheAppManager.getSysConfigurationByCode("phoneComfirm").getParamValue();
		//家庭主要成员
		String[] f_name       = request.getParameterValues("f_name");
				
		//主要社会关系
		String[] s_name       = request.getParameterValues("s_name");
		Map<String,Object> map= new HashMap<String, Object>();
		try {
			do{
				if("1".equals(phoneComfirm) && "submit".equals(submit)){// 需要手机短信验证 提交学籍卡时才做手机号的验证
					if(ExStringUtils.isEmpty(msgAuthCode)){
						map.put("statusCode",300);
						map.put("message", "手机验证码不能为空！");
						continue;
					}
					String cacheMsgAuthCode = memcachedManager.get(mobile);
					if(ExStringUtils.isEmpty(cacheMsgAuthCode)){
						map.put("statusCode",300);
						map.put("message", "手机验证码不正确或已经过期，请重新获取！");
						continue;
					}
					if(!cacheMsgAuthCode.equals(msgAuthCode)){
						map.put("statusCode",300);
						map.put("message", "手机验证码不正确！");
						continue;
					}
				}
				if(ExStringUtils.isBlank(ExStringUtils.toString(f_name).replaceAll(",", ""))){
					map.put("statusCode",300);
					map.put("message", "家庭主要成员不能为空！");
					continue;
				}
				if(ExStringUtils.isBlank(ExStringUtils.toString(s_name).replaceAll(",", ""))){
					map.put("statusCode",300);
					map.put("message", "主要社会关系不能为空！");
					continue;
				}
				User user 					    	= SpringSecurityHelper.getCurrentUser();
				StudentInfo studentInfo;
				String studyNo = request.getParameter("studyNo");
				if(ExStringUtils.isNotBlank(studyNo)){
					studentInfo 	    = studentInfoService.findUniqueByProperty("studyNo", studyNo);
				}else{
					studentInfo 	    = studentInfoService.getStudentInfoByUser(user);
				}
				
				// 检查该号码是否已被使用
				List<StudentBaseInfo> studentList = studentService.findByHql("from "+StudentBaseInfo.class.getSimpleName()+" s where s.mobile=? and s.isDeleted=0", mobile);
				Set<String> certNumSet = new HashSet<String>();
				if(ExCollectionUtils.isNotEmpty(studentList)) {
					for(StudentBaseInfo stuBase : studentList){
						certNumSet.add(stuBase.getCertNum());
					}
				}
				if(ExCollectionUtils.isNotEmpty(certNumSet) && !certNumSet.contains(studentInfo.getStudentBaseInfo().getCertNum())){
					map.put("statusCode",300);
					map.put("message", "该手机号码已被使用！");
					continue;
				}
				studentInfoService.studentRollInfoCardSave(request, map,studentInfo);
				if (ExStringUtils.isNotEmpty(submit)&& "submit".equals(submit)){
					studentInfo.setRollCardStatus("2");// 已提交
					studentInfoService.update(studentInfo);
					map.put("message", "提交成功！");
				}else{
					studentInfo.setRollCardStatus("1");// 已保存
					map.put("message", "保存成功！");
				}
				studentInfoService.update(studentInfo);
				map.put("statusCode",200);
				map.put("reloadUrl", request.getContextPath() +"/edu3/student/roll-card-form.html?studentId="+studentInfo.getResourceid());
			} while(false);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("statusCode",300);
			if (ExStringUtils.isNotEmpty(submit)&& "submit".equals(submit)){
				map.put("message", "提交失败："+e.fillInStackTrace());
			}
			else{
				map.put("message", "保存失败："+e.fillInStackTrace());
			}
		}
		UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "13", UserOperationLogs.UPDATE,"学生保存/提交学籍卡：结果："+map.get("message")+"。参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
		renderJson(response,JsonUtils.mapToJson(map));
	}
	
	/**
	 * 我的学习小组
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/student/bbsgroup/view.html")
	public String listBbsGroup(HttpServletRequest request, ModelMap model) throws WebException {
		User user = SpringSecurityHelper.getCurrentUser();
		List<BbsGroup> groups = bbsGroupService.findBbsGroupByUser(user.getResourceid());
		model.addAttribute("bbsGroups", groups);
		
		BbsSection bbsGroupSectiopn = bbsSectionService.findUniqueByProperty("sectionCode", CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode.group").getParamValue());
		model.addAttribute("bbsGroupSectiopnId", bbsGroupSectiopn.getResourceid());
		return "/edu3/learning/studentleanplan/student-bbsgroup";
	}
	/**
	 * 我的主题主页
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/student/topic/view.html")
	public String listTopicMain(HttpServletRequest request, Page objPage, ModelMap model) throws WebException {
		return "/edu3/learning/studentleanplan/student-topic-main";
	}
	/**
	 * 我的主题
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/student/topic/view.html")
	public String listTopic(HttpServletRequest request, Page objPage, ModelMap model) throws WebException {
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		Set<Course> courseList = new LinkedHashSet<Course>();//已预约课程列表
		List<String> courseIds = new ArrayList<String>();
		User user = SpringSecurityHelper.getCurrentUser();
		
		List<StudentLearnPlan> plans = studentLearnPlanService.findByHql("from "+StudentLearnPlan.class.getSimpleName()+" where isDeleted=0 and studentInfo.sysUser.resourceid=? ", user.getResourceid());
		if(ExCollectionUtils.isNotEmpty(plans)){
			for (StudentLearnPlan plan : plans) {
				if(plan.getTeachingPlanCourse()!=null){
					courseList.add(plan.getTeachingPlanCourse().getCourse());
					courseIds.add(plan.getTeachingPlanCourse().getCourse().getResourceid());
				} else if(plan.getPlanoutCourse()!=null){
					courseList.add(plan.getPlanoutCourse());
					courseIds.add(plan.getPlanoutCourse().getResourceid());
				}
			}
		}
		model.addAttribute("courseList", courseList);
		
		String courseId = request.getParameter("courseId");	
		String currentIndex = ExStringUtils.defaultIfEmpty(request.getParameter("currentIndex"), "0");
		condition.put("currentIndex", currentIndex);			
		if("0".equals(currentIndex)){//1.随堂问答
			objPage.setOrderBy("course.courseName,fillinDate");
			objPage.setOrder(Page.ASC);			
					
			if(ExStringUtils.isNotEmpty(courseId)){
				condition.put("courseId", courseId);
			}			
			condition.put("sectionCode", CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode").getParamValue());
			condition.put("topicType", "2");
			condition.put("notEmptySyllabus", "Y");
			condition.put("fillinManId", user.getResourceid());
			condition.put("courseIds", ExStringUtils.join(courseIds, ","));
			
			Page bbsTopicListPage = bbsTopicService.findBbsTopicByCondition(condition, objPage);			
			model.addAttribute("bbsTopicListPage", bbsTopicListPage);			
		} else if("1".equals(currentIndex)){//2.小组话题
			objPage.setOrderBy("course.courseName,fillinDate");
			objPage.setOrder(Page.ASC);
			
			List<BbsGroup> groups = bbsGroupService.findBbsGroupByUser(user.getResourceid());
			List<String> groupIds = new ArrayList<String>();
			if(ExCollectionUtils.isNotEmpty(groups)){
				for (BbsGroup group : groups) {
					groupIds.add(group.getResourceid());
				}
			}
			if(ExStringUtils.isNotEmpty(courseId)){
				condition.put("courseId", courseId);
			}			
			condition.put("sectionCode", CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode.group").getParamValue());
			condition.put("courseIds", ExStringUtils.join(courseIds, ","));
			condition.put("groupIds", ExStringUtils.join(groupIds, ","));
			condition.put("topicType", "4");//讨论
			
			Page bbsTopicListPage = bbsTopicService.findBbsTopicByCondition(condition, objPage);			
			model.addAttribute("bbsTopicListPage", bbsTopicListPage);
		} else if("2".equals(currentIndex)){//3.课程论坛主题
			objPage.setOrderBy("course.courseName,fillinDate");
			objPage.setOrder(Page.ASC);
			
			List<String> bbsSectionCodes = new ArrayList<String>();
			bbsSectionCodes.add(CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode").getParamValue());
			bbsSectionCodes.add(CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode.group").getParamValue());
			bbsSectionCodes.add(CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode.feedback").getParamValue());
			
			if(ExStringUtils.isNotEmpty(courseId)){
				condition.put("courseId", courseId);
			}			
			condition.put("fillinManId", user.getResourceid());
			condition.put("courseIds", ExStringUtils.join(courseIds, ","));
			condition.put("notInBbsSectionCodes", ExStringUtils.join(bbsSectionCodes, ","));
			
			Page bbsTopicListPage = bbsTopicService.findBbsTopicByCondition(condition, objPage);			
			model.addAttribute("bbsTopicListPage", bbsTopicListPage);
		} else {
			objPage.setOrderBy("fillinDate");
			objPage.setOrder(Page.ASC);
						
			condition.put("fillinManId", user.getResourceid());
			condition.put("isCourseSection", Constants.BOOLEAN_NO);//非课程版块
			
			Page bbsTopicListPage = bbsTopicService.findBbsTopicByCondition(condition, objPage);			
			model.addAttribute("bbsTopicListPage", bbsTopicListPage);
		}
		model.addAttribute("condition", condition);
		return "/edu3/learning/studentleanplan/student-topic";
	}
	/**
	 * 查看历史成绩
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/student/examresults/list.html")
	public String listExamResults(HttpServletRequest request, Page objPage, ModelMap model) throws WebException {
		User user = SpringSecurityHelper.getCurrentUser();
		List<StudentInfo> stuList = studentInfoService.findStuListByUserId(user.getResourceid());
		List<Map<String, Object>> resultsList = new ArrayList<Map<String,Object>>();
		if(ExCollectionUtils.isNotEmpty(stuList)){			
			String defaultStudentId = "";
			if(null != user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
				defaultStudentId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
			}
			Map<String, Map<String, Object>> electiveExamCountMap = new HashMap<String, Map<String,Object>>();
			try {
				electiveExamCountMap = studentExamResultsService.getElectiveExamCountMapByStuList(stuList);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (StudentInfo studentInfo : stuList) {
				Map<String, Object> examResults = new HashMap<String, Object>();
				List<StudentExamResultsVo> results = new ArrayList<StudentExamResultsVo>();
				try {
					Integer examCount =  Integer.parseInt(electiveExamCountMap.get(studentInfo.getResourceid()).get("examcount").toString());
					List<StudentExamResultsVo> resultsVoList = studentExamResultsService.studentExamResultsList(studentInfo,examCount);
					for (StudentExamResultsVo vo : resultsVoList) {
						//已发布的成绩
						if(Constants.BOOLEAN_YES.equals(vo.getIsNoExam())||Constants.BOOLEAN_YES.equals(vo.getIsDegreeUnitExam())||Constants.BOOLEAN_YES.equals(vo.getIsStateExamResults())||"4".equals(vo.getCheckStatusCode())){
							results.add(vo);
						}								
					}
				} catch (Exception e) {
					logger.error("获取学生历史成绩出错：{}",e.fillInStackTrace());
				}
				//是否替换成绩为“未缴费”
				isDisplayScore(studentInfo, results);
				
				examResults.put("studentInfo", studentInfo);
				examResults.put("examResultsList", results);
				if(defaultStudentId.equals(studentInfo.getResourceid())){
					examResults.put("isDefaultStudentId", Constants.BOOLEAN_YES);
				}					
				resultsList.add(examResults);
			}
			model.addAttribute("resultsList",resultsList);
			model.addAttribute("isDisplayScore", CacheAppManager.getSysConfigurationByCode("no_schooling_no_display_score").getParamValue());
		}
		return "/edu3/learning/studentleanplan/student-examresults";
	}

	/**
	 * @param studentInfo
	 * @param results
	 * @throws ServiceException
	 */
	private void isDisplayScore(StudentInfo studentInfo,
			List<StudentExamResultsVo> results) throws ServiceException {
		String isDisplay =CacheAppManager.getSysConfigurationByCode("no_schooling_no_display_score").getParamValue();//全局参数：N 为不显示成绩，Y为显示成绩
//				String tips="";
		if(isDisplay.equalsIgnoreCase(Constants.BOOLEAN_NO)){
			//获取学生的缴费状态:  已缴费的则跳过，未缴费的则不显示成绩
			String hql4sp="from "+StudentPayment.class.getSimpleName()+" where studentInfo.resourceid =? and isDeleted =0 ";
			StudentPayment sp = studentPaymentService.findUnique(hql4sp, new Object[]{studentInfo.getResourceid()});
			if(null==sp){//没有缴费记录
//						tips="在系统中未找到你的缴费记录，按学校要求，未有缴费记录或未缴费，则不显示成绩。可以【我的个人信息】【我的学籍信息】【缴费信息】中查看缴费情况";
				BLANKALLOrCurrentTerm(results,"");
			}else{
//						tips="你的学费状态为欠费（包括未缴费）。按学校要求，未有缴费记录或未缴费，则不显示成绩。可以【我的个人信息】【我的学籍信息】【缴费信息】中查看缴费情况";
				if(!"1".equals(sp.getChargeStatus())){//缴费状态,字典值,0-未缴费,1-已缴费,-1-欠费
					BLANKALLOrCurrentTerm(results,sp.getChargeStatus());
				}
			}
		}
	}
	private void BLANKALLOrCurrentTerm(List<StudentExamResultsVo> results,String paymentStatus){
		String tips ="没有缴费记录";
		if(ExStringUtils.isNotBlank(paymentStatus)){
			tips=JstlCustomFunction.dictionaryCode2Value("CodeChargeStatus", paymentStatus);
		}
		// Y/N ：Y表示全部学期的成绩都不显示
		String BLANKAll =CacheAppManager.getSysConfigurationByCode("no_schooling_no_display_score_all_or_current").getParamValue();
		if(BLANKAll.equalsIgnoreCase(Constants.BOOLEAN_YES)){
			for (StudentExamResultsVo vo : results){
				vo.setIntegratedScore("<font color='red'>"+tips+"</font>");
			}
		}else{
			String currentTerm=CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();
			String [] strs= currentTerm.split("\\.");
			String year =strs[0];
			String term = strs[1];
			for (StudentExamResultsVo vo : results){
				ExamSub es = examSubService.get(vo.getExamSubId());
				if(es.getYearInfo().getFirstYear().toString().equals(year)&&es.getTerm().equals(term)){
					vo.setIntegratedScore("<font color='red'>"+tips+"</font>");
				}				
			}
		}
	}
	
	/**
	 * 我的教务员
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/student/myteachmaster.html")
	public String listStudentTeachMan(HttpServletRequest request, ModelMap model) throws WebException {
		User currentUser = SpringSecurityHelper.getCurrentUser();
		model.addAttribute("currentUser", currentUser);
		return "/edu3/learning/studentleanplan/student-teachingman";
	}
	
	/**
	 * 学生证打印
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/student/graduate/studentCardPrint.html")
	public void studentCardPrint(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		String studentInfoIds = ExStringUtils.trimToEmpty(request.getParameter("studentInfoIds"));
		String isPdf		  = ExStringUtils.trimToEmpty(request.getParameter("isPdf"));
		String photoFrom		  = ExStringUtils.trimToEmpty(request.getParameter("photoFrom"));
		//flag = "true";  
		try {
			if(ExStringUtils.isNotEmpty(studentInfoIds)){
				
				String[] ids = studentInfoIds.split(",");
				List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>(0);
				int count=0;
				for (String id : ids) {
					count++;
					if(count>100){
						continue;
					}
					List<Map<String,Object>> dataList 	  = new ArrayList<Map<String,Object>>(0);
					StudentInfo studentInfo = studentInfoService.get(id);
					Map<String,Object> map1 = setStudentCardMap(studentInfo,photoFrom);
					dataList.add(map1);
					JRMapCollectionDataSource dataSource  = new JRMapCollectionDataSource(dataList);
					String studentCardPrintStyle = CacheAppManager.getSysConfigurationByCode("studentCardPrintStyle").getParamValue();
					String jasperFileName = "";
					if ("1".equals(studentCardPrintStyle)) {
						jasperFileName = "studentCard_pdf.jasper";
					} else if ("2".equals(studentCardPrintStyle)) {
						jasperFileName = "studentCard_pdf2.jasper";
					}
//					String jasperFile     				  = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+File.separator+"graduatetable"+File.separator+"studentCard.jasper"),"utf-8");
//					if(ExStringUtils.isNotEmpty(isPdf)){
					String jasperFile     				  = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+File.separator+"studentinfo"+File.separator+jasperFileName),"utf-8");

//					}
					map.put("imageRootPath",CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+"common"+File.separator+"students");
					JasperPrint jasperPrint               = JasperFillManager.fillReport(jasperFile, map, dataSource); // 填充报表
					jasperPrints.add(jasperPrint);
				}
				JasperPrint jasperPrint               = new JasperPrint();
				int i = 0;
				//对于批量打印，如果将整个datalist传进去，会出现一堆封面+最后一位学生的信息，这是因为无论是summary抑或是title在一份报表中只会出现一次。故而，要将各份表格分别生成，再重新拼装
				for (JasperPrint print : jasperPrints) {
					if(i==0){
						jasperPrint = print;
						i++;
					}else{
						List<JRBasePrintPage> pages = (List<JRBasePrintPage>)print.getPages();
						for (JRBasePrintPage page : pages) {
							jasperPrint.addPage(page);
						}
					}
				}
				
				if (null!=jasperPrint){
					if(ExStringUtils.isNotEmpty(isPdf)){
						GUIDUtils.init();	
						String filePath        		  = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+File.separator+"exportfiles"+File.separator+GUIDUtils.buildMd5GUID(false)+".pdf";
						JRPdfExporter exporter = new JRPdfExporter();
						exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);   
						exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING,"UTF-8");
						exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,filePath);
						exporter.exportReport(); 
						downloadFile(response,"学生证.pdf",filePath,true);
					}else{
						renderStream(response, jasperPrint);
					}
				}else {
			    	renderHtml(response,"<script>alert('缺少打印数据！')</script>");
				}
			}
		} catch (Exception e) {
			logger.error("学生证打印出错：{}"+e.fillInStackTrace());
			renderHtml(response,"<script>alert('学生证打印出错："+e.getMessage()+"')</script>");
		}
	}
	
	private Map<String,Object> setStudentCardMap(StudentInfo stu,String photoFrom){
		Map<String,Object>		 map  = new HashMap<String, Object>();
		if(null != stu){
			List<String> dictCodeList = new ArrayList<String>();
			dictCodeList.add("CodeSex");
			Map<String , Object> sexMap = dictionaryService.getDictionByMap(dictCodeList, true, IDictionaryService.PREKEY_TYPE_BYCODE);
			 map.put("stuNo",stu.getStudyNo());    			//学号
			 map.put("stuName", stu.getStudentName()); 		//姓名
			 String gender = null == stu.getStudentBaseInfo() ? "" : stu.getStudentBaseInfo().getGender();
			 map.put("stuSex", sexMap.get("CodeSex_"+gender));		//性别
			 //根据字典取性别
			 map.put("schoolName", "继续教育学院");   //学院
			 String photo = null;
			 if (ExStringUtils.isNotEmpty(photoFrom)&& "recruit".equals(photoFrom)) {//判断选择照片来自录取库
				 if(null != stu.getStudentBaseInfo()){
						photo = stu.getStudentBaseInfo().getRecruitPhotoPath();
						if (ExStringUtils.isBlank(photo)) {//查找录取库
							//TODO
							try {
								String sql=" select er.backphotopath from edu_recruit_examinee er where er.KSH=? and er.ZKZH=? ";
								List<Map<String, Object>> mapList=baseSupportJdbcDao.getBaseJdbcTemplate().findForList(sql, new Object[]{stu.getEnrolleeCode(),stu.getExamCertificateNo()});
								if (mapList!=null&&mapList.size()>0) {
									photo=(String) mapList.get(0).get("backphotopath");
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
			}else{//图片来自学籍库
				if(null != stu.getStudentBaseInfo()){
					photo = stu.getStudentBaseInfo().getPhotoPath();
				}
			}
			 if(ExStringUtils.isNotBlank(photo)&&!"/".equals(photo.substring(0, 1))){
				 photo="/"+photo;
			 }
			 map.put("photoPath", ExStringUtils.isBlank(photo) ? "" : photo); //照片
			 
			 if( null == stu.getBranchSchool()){
				 map.put("brSchoolName", "");
			}else{
//				String brSchoolName = stu.getBranchSchool().getUnitName().replace("学习中心", "").replace("*", "");
				String brSchoolName = stu.getBranchSchool().getUnitShortName();
			
				if(brSchoolName.length()>12){
					 map.put("brSchoolName", brSchoolName);
					 map.put("brSchoolName1", "");
				}else{
					 map.put("brSchoolName1", brSchoolName); 
					 map.put("brSchoolName", "");
				}
			}
			 
			 
			if( null == stu.getMajor()){
				 map.put("majorName", "");
			}else{ 
				String majorName = stu.getMajor().getMajorName().replace("A", "").replace("B", "").replace("C", "");
				String[] majorNameaaa = majorName.split("（");
				if(majorNameaaa.length>1){
					String majorName1=majorNameaaa[0];
					String majorName2=majorNameaaa[1]; 
					map.put("majorName", majorName1); 
					map.put("majorName1", "（"+majorName2);
				}else{
					 map.put("majorName", majorName); 
					 map.put("majorName1", ""); 
				}
				 
			}
			 
			 
			 //专业名称
			 map.put("classicName", null == stu.getClassic() ? "" : stu.getClassic().getClassicName());  //培训层次
			 map.put("teachingType", JstlCustomFunction.dictionaryCode2Value("CodeLearningStyle", stu.getTeachingType()));//学习形式
			 String eduyear=null;
			 if(stu.getTeachingPlan()!=null){
				 eduyear=stu.getTeachingPlan().getEduYear();
				 eduyear=eduyear.split("～")[0];
			 }
			 map.put("stuXuezhi", null == eduyear ? "" : eduyear.replace("年", "")+"年");    //学制
			 map.put("gradeName", stu.getGrade().getGradeName());//年级
			 String certtype = null == stu.getStudentBaseInfo() ? "" : stu.getStudentBaseInfo().getCertType(); //证件类型
//			 if("idcard".equals(certtype)){  //身份证号
				 map.put("idCard", null == stu.getStudentBaseInfo() ? "" : stu.getStudentBaseInfo().getCertNum());     
//			 }else{
//				 map.put("idCard", "");
//			 } 
			 stu.getStudyNo().substring(0, 4);
			 Grade _grade = stu.getGrade();
			 String  _term  = "";
			 Long 	 _year  = 0L;
			 String  _month = "";
//			 if(null != _grade){
//				 _term = stu.getGrade().getTerm();
//				 if("1".equals(_term))
//				 {
//					 _year = null == stu.getGrade().getYearInfo() ? -1L : Long.parseLong(stu.getStudyNo().substring(0, 4));
////					 _year = null == stu.getGrade().getYearInfo() ? -1L : stu.getGrade().getYearInfo().getFirstYear()+1L;
//					 _month = "9";
//				 }else if("2".equals(_term))
//				 {
//					 _year = null == stu.getGrade().getYearInfo() ? -1L : Long.parseLong(stu.getStudyNo().substring(0, 4));
////					 _year = null == stu.getGrade().getYearInfo() ? -1L : stu.getGrade().getYearInfo().getFirstYear()+1L; 
//					 _month = "3";
//				 }
//			 }
			 
			 if(null != _grade){
				 _term = stu.getGrade().getTerm();
				 if("1".equals(_term))
				 {
					 _year = null == stu.getGrade().getGradeName() ? -1L : Long.parseLong(stu.getGrade().getGradeName().substring(0, 4));
//					 _year = null == stu.getGrade().getYearInfo() ? -1L : stu.getGrade().getYearInfo().getFirstYear()+1L;
					 _month = "9";
				 }else if("2".equals(_term))
				 {
					 _year = null == stu.getGrade().getGradeName() ? -1L : Long.parseLong(stu.getGrade().getGradeName().substring(0, 4));
//					 _year = null == stu.getGrade().getYearInfo() ? -1L : stu.getGrade().getYearInfo().getFirstYear()+1L; 
					 _month = "3";
				 }
			 }
			 	
			 		 
			 map.put("entranceYear",_year <= 0 ? "" : _year+"");  //入学年月  年份
			 map.put("entranceMonth", _month); //入学年月  月份
			 map.put("entranceDay", "1"); //入学年月 日
			 try {
			 	map.put("inDate", ExDateUtils.formatDateStr(stu.getInDate(), "yyyy年MM月dd日"));
			 } catch (ParseException e) {
			 	e.printStackTrace();
			 }
		}
		return map;
	}
	
	/**
	 * 学生证打印-打印预览
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/register/studentinfo/studentCardPrint.html")
	public String studentCardChoseView(HttpServletRequest request,ModelMap model){
		String studentInfoId = ExStringUtils.trimToEmpty(request.getParameter("studentInfoId"));
		String studentInfoIds = ExStringUtils.trimToEmpty(request.getParameter("studentInfoIds"));
		String isPdf = ExStringUtils.trimToEmpty(request.getParameter("isPdf"));
		String photoFrom = ExStringUtils.trimToEmpty(request.getParameter("photoFrom"));
		model.addAttribute("studentInfoId", studentInfoId);
		model.addAttribute("studentInfoIds", studentInfoIds);
		model.addAttribute("isPdf", isPdf);
		model.addAttribute("photoFrom", photoFrom);
		return "/edu3/roll/graduationStudent/studentCard-printview";
	}
	
}
