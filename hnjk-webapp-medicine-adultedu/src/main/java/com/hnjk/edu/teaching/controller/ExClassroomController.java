package com.hnjk.edu.teaching.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.base.model.AttachInfo;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Building;
import com.hnjk.edu.basedata.model.Classroom;
import com.hnjk.edu.basedata.service.IBuildingService;
import com.hnjk.edu.basedata.service.IClassroomService;
import com.hnjk.edu.basedata.vo.ClassroomVo;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.vo.StudentInfoVo;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.extend.plugin.excel.service.IImportExcelService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
/**
 * 成教课室管理.
 * <code>ExClassroomController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2013-4-16 下午01:42:39
 * @see 
 * @version 1.0
 */
@Controller
public class ExClassroomController extends FileUploadAndDownloadSupportController { 
	private static final long serialVersionUID = 7636017693166205684L;

	@Autowired
	@Qualifier("buildingService")
	private IBuildingService buildingService;
	
	@Autowired
	@Qualifier("classroomService")
	private IClassroomService classroomService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;//Excel导出服务
	
	@Autowired
	@Qualifier("importExcelService")
	private IImportExcelService importExcelService;

	/**
	 * 课室列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/exclassroom/list.html")
	public String listClassroom(HttpServletRequest request,Page objPage,ModelMap model) throws WebException{
		objPage.setOrderBy("showOrder");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		String brSchoolid = request.getParameter("brSchoolid");
		String classroomName = ExStringUtils.trimToEmpty(request.getParameter("classroomName"));
		String classroomType = request.getParameter("classroomType");
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			brSchoolid = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}
		
		if(ExStringUtils.isNotEmpty(brSchoolid)) {
			condition.put("brSchoolid", brSchoolid);
		}
		if(ExStringUtils.isNotEmpty(classroomName)) {
			condition.put("classroomName", classroomName);
		}
		if(ExStringUtils.isNotEmpty(classroomType)) {
			condition.put("classroomType", classroomType);
		}

		Page page = classroomService.findClassroomByHql(condition, objPage);
		
		model.addAttribute("classroomPage", page);
		model.addAttribute("condition", condition);
		return "/edu3/basedata/classroom/classroom-list";
	}	
	 
	 /**
	 * 导出课室信息XLS
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/classroominfo/exportXlsClassroom.html")
	public void exportXlsClassroom(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String brSchoolid         = ExStringUtils.trimToEmpty(request.getParameter("brschoolid"));
		String classroomName = ExStringUtils.trimToEmpty(request.getParameter("classroomName"));
		try {
			classroomName = java.net.URLDecoder.decode(classroomName , "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String classroomType 		= ExStringUtils.trimToEmpty(request.getParameter("classroomType"));
		String classroomIds           = ExStringUtils.trimToEmpty(request.getParameter("classroomIds"));
		
		User user 		    = SpringSecurityHelper.getCurrentUser();		
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			brSchoolid = user.getOrgUnit().getResourceid();
		}

		Map<String, Object> condition = new HashMap<String, Object>();
		if(ExStringUtils.isNotEmpty(brSchoolid)) {
			condition.put("branchSchool", brSchoolid);
		}
		if(ExStringUtils.isNotEmpty(classroomName)) {
			condition.put("classroomName", classroomName);
		}
		if(ExStringUtils.isNotEmpty(classroomType)) {
			condition.put("classroomType", classroomType);
		}
		
		String titleStrbuf = "课室信息表";
		//自定义选择导出的数据列
//		String[] excelColumnNames 	  = {"classroomName","seatNum","singleSeatNum","doubleSeatNum","unitName"};//request.getParameterValues("excelColumnName");
//		List<String> filterColumnList = new ArrayList<String>();//定义过滤列
//		if(null !=excelColumnNames && excelColumnNames.length>0){
//			for(int i=0;i<excelColumnNames.length;i++){
//				filterColumnList.add(excelColumnNames[i]);
//			}
//		}
		
		GUIDUtils.init();
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			List<Classroom> list = classroomService.findClassroomByCondition(condition);
			List<ClassroomVo> voList = new ArrayList<ClassroomVo>();
			for (int i = 0; i < list.size(); i++) {
				Classroom classroom = list.get(i);
				ClassroomVo classroomVo = new ClassroomVo();
				classroomVo.setLayerNo(classroom.getLayerNo());
				classroomVo.setUnitNo(classroom.getUnitNo());
				classroomVo.setClassroomName(classroom.getClassroomName());
				classroomVo.setSeatNum(classroom.getSeatNum());
				classroomVo.setSingleSeatNum(classroom.getSingleSeatNum());
				classroomVo.setDoubleSeatNum(classroom.getDoubleSeatNum());
				if (classroom.getBuilding() != null && classroom.getBuilding().getBranchSchool() != null) {
					classroomVo.setUnitName(classroom.getBuilding().getBranchSchool().getUnitName());
				}
				voList.add(classroomVo);
			}
			//为方便查看定位，这里都添加输出
			logger.info("导出课室信息XLS:/edu3/register/studentinfo/exportXlsClassroom.html,username:"+user.getUsername(),user.getUsername());
			
			File excelFile  = null;
			File disFile 	= new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			
			List<String> dictCodeList = new ArrayList<String>();
			dictCodeList.add("CodeClassRoomStyle");
			Map<String , Object> map = dictionaryService.getDictionByMap(dictCodeList, true, IDictionaryService.PREKEY_TYPE_BYCODE);
			
			exportExcelService.initParmasByfile(disFile, "classroomVo", voList,map);
			exportExcelService.getModelToExcel().setHeader(titleStrbuf.toString());//设置大标题
			exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
			WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
			font.setBoldStyle(WritableFont.BOLD);
	        WritableCellFormat format = new WritableCellFormat(font);
			format.setAlignment(Alignment.CENTRE);
	        format.setBackground(Colour.YELLOW);
	        format.setBorder(Border.ALL, BorderLineStyle.THIN);
			exportExcelService.getModelToExcel().setTitleCellFormat(format);
			exportExcelService.getModelToExcel().setRowHeight(300);
			
			excelFile 	= exportExcelService.getExcelFile();//获取导出的文件
			String downloadFileName = "课室信息表.xls";
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response,downloadFileName,excelFile.getAbsolutePath(),true);
			
		}catch (Exception e) {
			logger.error("导出excel文件出错："+e.fillInStackTrace());
			renderHtml(response, "<script>alert('导出excel文件出错:"+e.getMessage()+"')</script>");
		}
		
	}
	/**
	 * 课室Excel导入-路径输入
	 * 
	 * @return
	 */
	@RequestMapping("/edu3/sysmanager/classroominfo/importXlsClassroom.html")
	public String importStudentFactFeeInfo(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws WebException {
		return "/edu3/basedata/classroom/divideclasses-upload";
	}
	
	/**
	 * 导入课室信息XLS
	 * @param request
	 * @param response

	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/classroominfo/doXlsImportClassroom.html")
	public void doXlsImportClassroom(HttpServletRequest request,HttpServletResponse response) throws WebException{

		String exportAct = ExStringUtils.defaultIfEmpty(request.getParameter("act"), "");
		//提示信息字符串
		String  rendResponseStr = "";
		
		List<ClassroomVo> falseList = new ArrayList<ClassroomVo>();
		
		int sucssCount = 0;//成功操作数
		int count = 0;//总数
		int wCount = 0;//未操作总数
		
		User user 		    = SpringSecurityHelper.getCurrentUser();			
		String branchSchool = "";
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) { //校外学习中心只操作本部数据
			branchSchool 	= user.getOrgUnit().getResourceid();					
		}
		
		//获取excel的缴费记录，并插入到数据库中
		if ("import".equals(exportAct)) {
			//设置目标文件路径
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH + request.getParameter("importFile"));
			try {
				//上传文件到服务器
				List<AttachInfo> list = doUploadFile(request, response, null);
				AttachInfo attachInfo = list.get(0);
				//创建EXCEL对象 获得待导入的excel的内容
				File excel = new File(attachInfo.getSerPath() + File.separator+ attachInfo.getSerName());
				
				importExcelService.initParmas(excel, "classroomVo",null);
				importExcelService.getExcelToModel().setSheet(0);// 设置excel sheet 0
				//获得待导入excel内容的List
				List modelList = importExcelService.getModelList();
				if(modelList==null){
					throw new Exception("导入教室模版错误！");
				}
				//转换为对应类型的List
				List<ClassroomVo> classroomVoList = new ArrayList<ClassroomVo>();
				for (int i = 0; i < modelList.size(); i++) {
					ClassroomVo classroom = (ClassroomVo) modelList.get(i);
					classroomVoList.add(classroom);
				}
				//遍历需要导入的List 将对应内容写进DB 并同时检测数据的合法性
				if (null != classroomVoList) {
					GUIDUtils.init();
					for(ClassroomVo classroomVo : classroomVoList){
						count++;
						Page objPage = new Page();
						objPage.setPageNum(1);
						objPage.setPageSize(100000);
						Map<String,Object> condition2 = new HashMap<String,Object>();
						condition2.put("unitName", classroomVo.getUnitName()+"%");
						List<OrgUnit> orgUnitList = orgUnitService.findOrgByCondition(condition2, objPage).getResult();
						//判断是否存在该教学站
						if (orgUnitList.size() > 0) {
							OrgUnit orgUnit = orgUnitList.get(0);
							//所属教学站与账户所属教学站是否符合
							if(!user.getOrgUnit().getResourceid().equals(orgUnit.getResourceid()) && user.getOrgUnit().getUnitName().indexOf("本部") == -1){
								classroomVo.setMessage("所属教学站与账户所属教学站不符！");
								falseList.add(classroomVo);
								continue;
							} else {
								Map<String,Object> condition = new HashMap<String,Object>();//查询条件
								condition.put("classroomname", classroomVo.getClassroomName());
								condition.put("branchSchool", orgUnit.getResourceid());
								List<Classroom> classroomList = classroomService.findClassroomByCondition(condition);
								//判断是否有该课室信息，如果有，则不给予添加
								if(classroomList.size() == 0){
									Classroom classroom = new Classroom();
									classroom.setClassroomName(classroomVo.getClassroomName());
									classroom.setSeatNum(classroomVo.getSeatNum());
									classroom.setSingleSeatNum(classroomVo.getSingleSeatNum());
									classroom.setDoubleSeatNum(classroomVo.getDoubleSeatNum());
									classroom.setLayerNo(classroomVo.getLayerNo());
									classroom.setUnitNo(classroomVo.getUnitNo());
									classroom.setClassroomType("1");
									condition2 = new HashMap<String,Object>();
									condition2.put("branchSchoolId", orgUnit.getResourceid());
									Page objPage2 = new Page();
									objPage2.setPageNum(1);
									objPage2.setPageSize(100000);
									List<Building> buildingList = buildingService.findBuildingByCondition(condition2, objPage2).getResult();
									if (buildingList.size() > 0) {
										Building building = buildingList.get(0);
										classroom.setBuilding(building);
									} else {
										classroomVo.setMessage("所属教学楼不存在！");
										falseList.add(classroomVo);
										continue;
									}
									classroomService.save(classroom);
									sucssCount ++;
								} else {
									classroomVo.setMessage("已经存在该教室！");
									falseList.add(classroomVo);
									continue;
								}
							}
						} else {
							classroomVo.setMessage("该教学站不存在！");
							falseList.add(classroomVo);
							continue;
						}		
					}
				}
				
				//导出课室信息导入失败的信息以及原因
				if(falseList!=null&&falseList.size()>0){
					setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
//					exportExcelService.getModelToExcel().setRowHeight(400);
					
					File excelFile = null;
					String fileName = GUIDUtils.buildMd5GUID(false);
					File disFile 	= new File(getDistfilepath()+ File.separator + fileName + ".xls");
					
					List<String> dictCodeList = new ArrayList<String>();
					dictCodeList.add("CodeClassRoomStyle");
					Map<String , Object> map = dictionaryService.getDictionByMap(dictCodeList, true, IDictionaryService.PREKEY_TYPE_BYCODE);
					
//					CacheAppManager.getSysConfigurationByCode("print.msg.schoolname").getParamValue();
					
					exportExcelService.initParmasByfile(disFile, "classroomVoError", falseList ,map);
					exportExcelService.getModelToExcel().setHeader("教室信息表");//设置大标题
					exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
					WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
					font.setBoldStyle(WritableFont.BOLD);
			        WritableCellFormat format = new WritableCellFormat(font);
					format.setAlignment(Alignment.CENTRE);
			        format.setBackground(Colour.YELLOW);
			        format.setBorder(Border.ALL, BorderLineStyle.THIN);
					exportExcelService.getModelToExcel().setTitleCellFormat(format);
					exportExcelService.getModelToExcel().setRowHeight(300);
					excelFile 	= exportExcelService.getExcelFile();//获取导出的文件
					
					logger.info("获取导出教室失败记录的excel文件:{}",excelFile.getAbsoluteFile());
					
//					downloadFile(response, "导入缴费信息失败名单.xls", excelFile.getAbsolutePath(),true);
					String upLoadurl = "/edu3/sysmanager/classroominfo/doXlsImportClassroom-falseUpload.html";
					rendResponseStr = "{statusCode:200,message:'"+"导入成功"+ sucssCount 
					+"条 | 导入失败"+ (count - sucssCount)
					+"条！',forwardUrl:'"+upLoadurl+"?excelFile="+ fileName +"'};";
				}
				if(ExStringUtils.isBlank(rendResponseStr)){
					rendResponseStr = "{statusCode:200,message:'"+"导入成功"+ sucssCount 
					+"条 | 导入失败"+ (count - sucssCount)
					+"条！',forwardUrl:''};";
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
				rendResponseStr = "{statusCode:300,message:'操作失败!"+e.getMessage()+"'};";
			}
		}
		StringBuffer html = new StringBuffer();
		html.append("<script>");
		html.append("var response = "+rendResponseStr);
		html.append("if(window.parent.donecallback) window.parent.donecallback(response);");
		html.append("</script>");
		renderHtml(response, html.toString());
	}
	/**
	 * 导出导入课室功能失败信息
	 * @param year
	 * @param type
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/classroominfo/doXlsImportClassroom-falseUpload.html")
	public void uploadFalseToImport(String excelFile,HttpServletRequest request,HttpServletResponse response) throws Exception {
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		File disFile = new File(getDistfilepath()+ File.separator + excelFile+".xls");
		downloadFile(response, "导出教室失败信息列表.xls", disFile.getAbsolutePath(),true);
	}	
	/**
	 * 新增编辑课室
	 * @param resourceid
	 * @param buildingId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/exclassroom/input.html")
	public String editClassroom(String resourceid,ModelMap model) throws WebException{
		User user = SpringSecurityHelper.getCurrentUser();
		OrgUnit brSchool = null;
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			brSchool = user.getOrgUnit();
			model.addAttribute("isBrschool", true);
		}
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			Classroom classroom = classroomService.get(resourceid);
			model.addAttribute("classroom", classroom);
		}else{ //----------------------------------------新增
			Classroom classroom = new Classroom();
			if(brSchool != null){
				List<Building> buildingList = buildingService.findByHql("from "+Building.class.getSimpleName()+" where isDeleted=? and branchSchool.resourceid=? ", 0,brSchool.getResourceid());
				if(ExCollectionUtils.isNotEmpty(buildingList)){
					classroom.setBuilding(buildingList.get(0));
					classroom.setShowOrder(classroomService.getNextShowOrder(buildingList.get(0).getResourceid()));
				} else {
					classroom.setBuilding(new Building());
					classroom.getBuilding().setBranchSchool(brSchool);
				}
			} else {
				classroom.setBuilding(new Building());
			}
			model.addAttribute("classroom", classroom);		
		}
		return "/edu3/basedata/classroom/classroom-form";
	}
	/**
	 * 保存课室
	 * @param building
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/exclassroom/save.html")
	public void saveClassroom(Classroom classroom,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			if(classroom.getLayerNo()==null) {
				classroom.setLayerNo(1L);
			}
			String brSchoolid = request.getParameter("brSchoolid");
			
			
					
			if(ExStringUtils.isNotBlank(classroom.getBuildingId())){
				Building building = buildingService.get(classroom.getBuildingId());
				classroom.setBuilding(building);				
			} else if(ExStringUtils.isNotBlank(brSchoolid)){
				List<Building> buildingList = buildingService.findByHql("from "+Building.class.getSimpleName()+" where isDeleted=? and branchSchool.resourceid=? ", 0,brSchoolid);
				if(ExCollectionUtils.isNotEmpty(buildingList)){
					classroom.setBuilding(buildingList.get(0));
					classroom.setShowOrder(classroomService.getNextShowOrder(buildingList.get(0).getResourceid()));
				} else {
					Building building = new Building();
					building.setBranchSchool(orgUnitService.get(brSchoolid));
					building.setBuildingName(building.getBranchSchool().getUnitName());
					building.setMaxLayers(0L);
					building.setMaxUnits(0L);					
					buildingService.save(building);
					
					classroom.setBuilding(building);
				}
			}
			
			if(ExStringUtils.isBlank(classroom.getLayerNo().toString())) {
				throw new WebException("楼号不能为空");
			}
			
			Criterion[] criterion1 = {
					Restrictions.eq("classroomName", classroom.getClassroomName()),
					Restrictions.eq("isDeleted", 0),
					Restrictions.eq("layerNo", classroom.getLayerNo()),
					Restrictions.eq("building.resourceid", classroom.getBuildingId())
					
			};	
		
			List<Classroom> list1 = classroomService.findByCriteria(criterion1);
			/**
			 * 判断同一教学点同一楼号时候又课室名相同
			 */
			if(ExStringUtils.isNotBlank(classroom.getResourceid())){ //--------------------更新
				String claname=classroomService.get(classroom.getResourceid()).getClassroomName();
				if(ExCollectionUtils.isNotEmpty(list1)&&!claname.equals(classroom.getClassroomName())) {
					throw new WebException("同一教学点下，同一楼号教室名称不能重复！");
				}
				Classroom persistClassroom = classroomService.get(classroom.getResourceid());
				ExBeanUtils.copyProperties(persistClassroom, classroom);				
				classroomService.update(persistClassroom);
			}else{ //-------------------------------------------------------------------保存
				if(ExCollectionUtils.isNotEmpty(list1)) {
					throw new WebException("同一教学点下，同一楼号的教室名称不能重复！");
				}
				classroomService.save(classroom);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_BASEDATA_CLASSROOM_INPUT");
			map.put("reloadUrl", request.getContextPath() +"/edu3/sysmanager/exclassroom/input.html?resourceid="+classroom.getResourceid());
		}catch (Exception e) {
			logger.error("保存教室出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

}
