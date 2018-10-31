package com.hnjk.platform.system.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Sheet;
import jxl.Workbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.SensitivewordFilter;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.SensitiveWord;
import com.hnjk.platform.system.service.ISensitiveWordService;

/**
 * 敏感词接口实现
 * @author Zik
 *
 */
@Service("sensitiveWordService")
@Transactional
public class SensitiveWordServiceImpl extends BaseServiceImpl<SensitiveWord> implements ISensitiveWordService {

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private  IBaseSupportJdbcDao baseSupportJdbcDao;
	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 */
	@Override
	public Page findByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuffer hql = findByConditionHql(condition, values);
		
		return findByHql(objPage, hql.toString(), values);
	}

	private StringBuffer findByConditionHql(Map<String, Object> condition, Map<String, Object> values) {
		StringBuffer hql = new StringBuffer("from "+SensitiveWord.class.getSimpleName()+" sw where sw.isDeleted=0 ");
		if(condition.containsKey("word")){
			hql.append("and sw.word like :word ");
			values.put("word", "%"+condition.get("word")+"%");
		}
		return hql;
	}

	/**
	 * 批量删除
	 * @param ids
	 */
	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);				
			}
		}
	}

	/**
	 * 根据敏感词获取实体
	 * @param word
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public SensitiveWord getByWord(String word) throws ServiceException {
		String hql = "from "+SensitiveWord.class.getSimpleName()+" sw where sw.isDeleted=0 and sw.word=?";
		return findUnique(hql, word);
	}

	/**
	 * 获取最近的更新时间
	 * @return
	 * @throws ServiceException
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String getLastUpateDate() throws ServiceException {
		String lastDateStr = "";
		try {
			String sql = "select to_char(max(updatedate),'yyyy-mm-dd hh24:mi:ss') lastDate from edu_sys_sensitiveword ";
			Map<String, Object> lastDate = baseSupportJdbcDao.getBaseJdbcTemplate().findForMap(sql, null);
			if(lastDate!=null && lastDate.size()>0){
				lastDateStr = (String)lastDate.get("lastDate");
			}
		} catch (Exception e) {
			logger.error("获取最近的更新时间出错", e);
		}
		return lastDateStr;
	}

	/**
	 * 查询敏感词列表--不分页
	 * @param condition
	 * @return
	 */
	@Override
	public List<SensitiveWord> findByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuffer hql = findByConditionHql(condition, values);
		return findByHql(hql.toString(), values);
	}
	
	/**
	 * 创建敏感词
	 * @param sensitiveWord
	 * @throws ServiceException
	 */
	private SensitiveWord createSensitiveWord(String sensitiveWord) {
		SensitiveWord hasSensitiveWord = getByWord(sensitiveWord);
		do {
			if (hasSensitiveWord != null) {
				continue;
			}
			Date today = new Date();
			hasSensitiveWord = new SensitiveWord();
			hasSensitiveWord.setWord(sensitiveWord);
			hasSensitiveWord.setCreateDate(today);
			hasSensitiveWord.setUpdateDate(today);
		} while (false);
		
		return hasSensitiveWord;
	}
	
	/**
	 * 处理xls格式的敏感词文件
	 * @param filePath
	 * @return
	 * @throws ServiceException
	 */
	private void importSensitiveWordByXLS(String filePath) throws Exception{
		Workbook rwb = null;
		InputStream is = null;
		List<SensitiveWord> sensitiveWordList = new ArrayList<SensitiveWord>();
		List<String> _sensitiveWordList = new ArrayList<String>();
		is = new FileInputStream(filePath);
		rwb = Workbook.getWorkbook(is);
		Sheet sheet = rwb.getSheet(0);
		int countRow = sheet.getRows();
		for (int i = 0; i < countRow; i++) {
			String sensitiveWord = ExStringUtils.trimToEmpty(sheet.getCell(0,i).getContents());
			if(ExStringUtils.isEmpty(sensitiveWord) || _sensitiveWordList.contains(sensitiveWord)){
				continue;
			}
			_sensitiveWordList.add(sensitiveWord);
			sensitiveWordList.add(createSensitiveWord(sensitiveWord));
		}
		// 批量保存
		batchSaveOrUpdate(sensitiveWordList);
		try {
			if(is!=null){
				is.close();
			}
		} catch (IOException e) {
			logger.error("关闭处理xls格式的敏感词文件流出错", e);
		}
	 }
	
	/**
	 * 处理txt,doc,docx格式的敏感词文件
	 * @param filePath
	 * @return
	 * @throws ServiceException
	 */
	 private void importSensitiveWordByText(String filePath) throws Exception{
		 InputStreamReader read = new InputStreamReader(new FileInputStream(filePath),"UTF-8");
		 List<SensitiveWord> sensitiveWordList = new ArrayList<SensitiveWord>();
		 List<String> _sensitiveWordList = new ArrayList<String>();
		 if(read!=null){
			 BufferedReader bufferedReader = new BufferedReader(read);
			 String sensitiveWord = null;
			 while((sensitiveWord = bufferedReader.readLine()) != null){    //读取文件
				 sensitiveWord = ExStringUtils.trimToEmpty(sensitiveWord);
				if(ExStringUtils.isEmpty(sensitiveWord) || _sensitiveWordList.contains(sensitiveWord)){
					continue;
				}
				_sensitiveWordList.add(sensitiveWord);
				sensitiveWordList.add(createSensitiveWord(sensitiveWord));
			 }
			 // 批量保存
			 batchSaveOrUpdate(sensitiveWordList);
			//关闭文件流
			try {
				read.close();
		    } catch(IOException e) {
		    	logger.error("关闭处理txt,doc,docx格式的敏感词文件流出错", e);
		    }
		 }
		 
	  }

    /**
	 * 根据附件导入敏感词
	 * @param attachs
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public void importSensitiveWordByAttachs(Attachs attachs) throws Exception {
		String filePath = attachs.getSerPath()+File.separator+attachs.getSerName();
		if("xls".equals(attachs.getAttType())){
			importSensitiveWordByXLS(filePath);
		}else {
			importSensitiveWordByText(filePath);
		}
	}

	/**
	 * 获取敏感词库实体
	 * @return
	 */
	@Override
	public SensitivewordFilter getSensitivewordFilter() {
//		List<SensitiveWord> sensitiveWordList = findByCondition(new HashMap<String, Object>());
		List<SensitiveWord> sensitiveWordList = findByHql("from "+SensitiveWord.class.getSimpleName()+" where isDeleted=0 ");
		SensitivewordFilter sensitivewordFilter = new SensitivewordFilter();
		sensitivewordFilter.addSensitiveWordToHashMap(getSensitiveWordSet(sensitiveWordList));
		return sensitivewordFilter;
	}

	private Set<String> getSensitiveWordSet(List<SensitiveWord> sensitiveWordList){
		Set<String> sensitiveWordSet = new HashSet<String>();
		if(ExCollectionUtils.isNotEmpty(sensitiveWordList)){
			for(SensitiveWord sw: sensitiveWordList){
				sensitiveWordSet.add(sw.getWord());
			}
		}
		return sensitiveWordSet;
	}
}
