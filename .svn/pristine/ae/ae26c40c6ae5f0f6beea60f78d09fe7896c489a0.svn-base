package com.hnjk.edu.finance.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.finance.model.TextbookFee;
import com.hnjk.edu.finance.service.ITextbookFeeService;
import com.hnjk.edu.finance.vo.TextbookFeeVo;

/** 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年7月31日 下午3:57:55 
 * 
 */
@Service("textbookFeeService")
@Transactional
public class TextbookFeeServiceImpl extends BaseServiceImpl<TextbookFee> implements ITextbookFeeService {

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;

	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 */
	@Override
	public Page findByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>(5);
		StringBuffer hql = new StringBuffer(500);
		hql.append(" from "+TextbookFee.class.getSimpleName()+" where isDeleted=0 ");
		if(condition.containsKey("yearId")){
			hql.append(" and yearInfo.resourceid=:yearId ");
			values.put("yearId", condition.get("yearId"));
		}
		if(condition.containsKey("majorId")){
			hql.append(" and major.resourceid=:majorId ");
			values.put("majorId", condition.get("majorId"));
		}
		
		hql.append(" order by ").append(objPage.getOrderBy()).append(" ").append(objPage.getOrder());
		
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}
	
	/**
	 * 批量删除
	 * @param split
	 */
	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException{
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);				
				logger.info("批量删除="+id);
			}
		}
	}

	/**
	 * 根据年级和专业获取教材费信息
	 * 
	 * @param yearId
	 * @param majorId
	 * @return
	 */
	@Override
	public TextbookFee findByYearAndMajor(String yearId, String majorId) {
		StringBuffer hql = new StringBuffer(100);
		hql.append("from ").append(TextbookFee.class.getSimpleName())
		     .append(" where isDeleted=0 and yearInfo.resourceid=? and major.resourceid=? ");
		
		return findUnique(hql.toString(), yearId,majorId);
	}

	/**
	 * 处理导入年教材费标准逻辑
	 * @param textbookFeeList
	 * @param yearId
	 * @return
	 */
	@Override
	public Map<String, Object> handleTextbookFee(List<TextbookFeeVo> textbookFeeList, String yearId) {
		Map<String, Object> retrunMap = new HashMap<String, Object>();
		int statusCode = 200;
		StringBuffer msg = new StringBuffer(""); // 出错信息
		// 失败记录
		List<TextbookFeeVo> failList = new ArrayList<TextbookFeeVo>(100);
		List<TextbookFee> saveList =null;
		try {
			do {
				if (ExCollectionUtils.isEmpty(textbookFeeList)) {
					statusCode = 300;
					msg.append("没有年教材费标准数据");
					continue;
				}
				YearInfo year = yearInfoService.get(yearId);
				if(year == null){
					statusCode = 300;
					msg.append("该年度不存在");
					continue;
				}
				saveList = new ArrayList<TextbookFee>(150);
				List<String> existList = new ArrayList<String>(500);
				
				String reg = "[0-9]+.?[0-9]*";// 用来验证分数是否是数值
				String moneyStr = null;
				Map<String, Major> majorMap = majorService.getMajorMapByCode();
				
				// 处理年教材费标准
				String existKey = null;
				Major major = null;
				TextbookFee textbookFee = null;
				for(TextbookFeeVo tf : textbookFeeList){
					// 金额处理
					moneyStr = tf.getMoney();
					if (!moneyStr.matches(reg)) {
						msg.append("[<font color='red'>专业编号：").append(tf.getMajorCode()).append("</font>],原因：金额只能为数字; </br>");
						tf.setErrorMsg("金额只能为数字");
						failList.add(tf);
						continue;
					}
					major = majorMap.get(tf.getMajorCode());
					// 判断专业是否存在
					if(major == null){
						msg.append("[<font color='red'>专业编号：").append(tf.getMajorCode()).append("</font>],原因：该专业不存在; </br>");
						tf.setErrorMsg("该专业不存在");
						failList.add(tf);
						continue;
					}
					// 判断专业名称是否正确
					if(!tf.getMajorName().equals(major.getMajorName())){
						msg.append("[<font color='red'>专业编号：").append(tf.getMajorCode()).append("</font>],原因：专业名称不正确; </br>");
						tf.setErrorMsg("专业名称不正确");
						failList.add(tf);
						continue;
					}
					existKey = yearId + major.getMajorCode();
					if(existList.contains(existKey)){
						msg.append("[<font color='red'>专业编号：").append(tf.getMajorCode()).append("</font>],原因：有重复的记录; </br>");
						tf.setErrorMsg("有重复的记录");
						failList.add(tf);
						continue;
					}
					existList.add(existKey);
				   
					// 判断是否已存在
					TextbookFee hasTextbookFee = findByYearAndMajor(yearId, major.getResourceid());
					if(hasTextbookFee != null ){
						msg.append("[<font color='red'>专业编号：").append(tf.getMajorCode()).append("</font>],原因：该年度和专业已存在一条教材费标准记录; </br>");
						tf.setErrorMsg("该年度和专业已存在一条教材费标准记录");
						failList.add(tf);
						continue;
					}
					BigDecimal money =BigDecimal.valueOf(Double.parseDouble(moneyStr)).setScale(2, BigDecimal.ROUND_HALF_UP);
					// 新增记录
					textbookFee = new TextbookFee();
					textbookFee.setMajor(major);
					textbookFee.setYearInfo(year);
					textbookFee.setMoney(money.doubleValue());
					
					// 加入列表
					saveList.add(textbookFee);
				}
				// 保存
				if(ExCollectionUtils.isNotEmpty(saveList)) {
					batchSaveOrUpdate(saveList);
				}
				
			} while (false);
			
			if(msg.length() >0) {
				statusCode = 400;
			}
		} catch (Exception e) {
			logger.error("处理导入年教材费标准出错", e);
			statusCode = 300;
			msg.setLength(0);
			msg.append("处理导入年教材费标准失败！");
		} finally {
			retrunMap.put("statusCode", statusCode);
			retrunMap.put("message", msg.toString());
			retrunMap.put("failList", failList);
		}
		
		return retrunMap;
	}

	
	
}
