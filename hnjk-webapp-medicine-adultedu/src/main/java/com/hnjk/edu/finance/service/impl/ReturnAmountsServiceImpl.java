package com.hnjk.edu.finance.service.impl;

import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.finance.model.ReturnAmounts;
import com.hnjk.edu.finance.service.IReturnAmountsService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
import lombok.Cleanup;
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
import java.util.*;

/**
 * Function : 已返学费金额 - 服务实现类
 * <p>Author : msl
 * <p>Date   : 2018-08-22
 * <p>Description :
 */
@Transactional
@Service("returnAmountsService")
public class ReturnAmountsServiceImpl extends BaseServiceImpl<ReturnAmounts> implements IReturnAmountsService {

	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;

	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;

	@Override
	public Map<String, Object> importExcelModel(String filePath) {

		Map<String,Object> returnMap = new HashMap<String, Object>();
		int  count = 0;		//总条数
		int successCount = 0;// 成功的条数
		int startRow = 2;	//第一条数据所在行数
		Map<String,Object> validateMap = new HashMap<String, Object>();
		try {
			@Cleanup InputStream inputStream = new FileInputStream(filePath);
			@Cleanup Workbook workbook = WorkbookFactory.create(inputStream);

			StringBuffer returnMsg = new StringBuffer();

			String brSchoolName = "";
			boolean isBrSchool = false;
			User user = SpringSecurityHelper.getCurrentUser();
			if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
				brSchoolName = user.getOrgUnit().getUnitName();
				isBrSchool = true;
			}
			do{
				Sheet sheet = workbook.getSheetAt(0);
				count = sheet.getLastRowNum()-1;
				List<ReturnAmounts> returnAmountsList = new ArrayList<ReturnAmounts>();
				Date operateDate;
				Map<String,Object> condition = new HashMap<String, Object>();
				for (int rowNum=startRow; rowNum <= sheet.getLastRowNum(); rowNum++) {
					Row row = sheet.getRow(rowNum);
					if (row != null) {
						String yearNameStr = ExStringUtils.toString(row.getCell(0));
						String unitNameStr = ExStringUtils.toString(row.getCell(1));
						String countNumStr = ExStringUtils.toString(row.getCell(2));
						String amountsStr = ExStringUtils.toString(row.getCell(3));
						String dateStr = ExStringUtils.toString(row.getCell(4));
						if (ExStringUtils.isBlank(unitNameStr,countNumStr,amountsStr,dateStr)) {
							returnMsg.append("<font color='red'>[第" + (rowNum+1) + "行]</font>"+"所有选项都是必填项！<br>");
							continue;
						}
						OrgUnit unit = orgUnitService.findOrgByUnitName(unitNameStr);
						if (unit == null) {
							returnMsg.append("<font color='red'>[第" + (rowNum+1) + "行]</font>"+"教学点名称有误！<br>");
							continue;
						}
						YearInfo yearInfo = yearInfoService.findUniqueByProperty("yearName",yearNameStr);
						if (isBrSchool) {
							if (!unitNameStr.equals(brSchoolName)) {
								returnMsg.append("<font color='red'>[第" + (rowNum+1) + "行]</font>"+"不允许导入其它教学点数据！<br>");
								continue;
							}
						}
						if (yearInfo == null) {
							returnMsg.append("<font color='red'>[第" + (rowNum+1) + "行]</font>"+"年度名称有误！<br>");
							continue;
						}
						if (!ExStringUtils.isNumeric(countNumStr,1)) {
							returnMsg.append("<font color='red'>[第" + (rowNum+1) + "行]</font>"+"次数必须是数字！<br>");
							continue;
						}
						if (!ExStringUtils.isNumeric(amountsStr,2)) {
							returnMsg.append("<font color='red'>[第" + (rowNum+1) + "行]</font>"+"金额必须是数字！<br>");
							continue;
						}
						condition.clear();
						condition.put("unit.resourceid",unit.getResourceid());
						condition.put("yearInfo.resourceid",yearInfo.getResourceid());
						condition.put("count",countNumStr);
						List<ReturnAmounts> returnAmounts_temp = findByCondition(condition);
						if (returnAmounts_temp != null && returnAmounts_temp.size() > 0) {
							returnMsg.append("<font color='red'>[第" + (rowNum+1) + "行]</font>"+"教学点、年度、次数必须唯一！<br>");
							continue;
						}

						operateDate = ExDateUtils.convertToDateEN(dateStr);
						ReturnAmounts returnAmounts = new ReturnAmounts();
						returnAmounts.setUnit(unit);
						returnAmounts.setYearInfo(yearInfo);
						returnAmounts.setCount(Integer.parseInt(countNumStr));
						returnAmounts.setAmounts(Double.parseDouble(amountsStr));
						returnAmounts.setOperateDate(operateDate);
						returnAmountsList.add(returnAmounts);
					}
				}
				if (returnMsg.length() == 0) {
					successCount = returnAmountsList.size();
					batchSaveOrUpdate(returnAmountsList);
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
}
