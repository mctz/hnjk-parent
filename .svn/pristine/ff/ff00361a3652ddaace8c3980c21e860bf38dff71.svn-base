package com.hnjk.platform.system.service.impl;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.AccessLogs;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.service.IAccessLogsService;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.util.*;

/**
 * 系统访问日志管理服务接口实现.
 * <code>AccessLogsServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-6-16 下午04:20:36
 * @see 
 * @version 1.0
 */
@Transactional
@Service("accessLogsService")
public class AccessLogsServiceImpl extends BaseServiceImpl<AccessLogs> implements IAccessLogsService {

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Override
	public void parseAndSaveAccessLogs(String fileName) throws Exception {
		String sql = "insert into hnjk_sys_accesslogs (RESOURCEID, IPADDRESS, USERNAME, ACCESSTIME, URL, PROTOCOL, SERVERSTATUS, NETFLOW, RUNNINGTIME, CLIENTBROWSER, CLIENTOS, ISDELETED, VERSION) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		List<Object[]> param = new ArrayList<Object[]>();		
		
		long length = new File(fileName).length();
		MappedByteBuffer in = new RandomAccessFile(fileName,"r").getChannel().map(FileChannel.MapMode.READ_ONLY,0,length);
		CharBuffer bf = CharBuffer.allocate(4096);	
		
		GUIDUtils.init();
		int index = 0;
		while(index < length){
			char c = (char)in.get(index++);				
			if(c == '\r' || c == '\n'){	
				AccessLogs log = parseAccessLogs(new String(bf.array(),0,bf.position()));
				if(log != null){
					param.add(new Object[]{
							GUIDUtils.buildMd5GUID(false),
							log.getIpaddress(),log.getUsername(),log.getAccessTime(),
							log.getUrl(),log.getProtocol(),log.getServerStatus(),
							log.getNetFlow(),log.getRunningTime(),
							log.getClientBrowser(),log.getClientOs(),0,0L
					});
				}
				if(param.size() > 10000){
					baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate(sql, param);
					param.clear();
				}
				bf.clear();
			}else{
				bf.put(c);
			}
		}
		if(param.size()>0){
			baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate(sql, param);
			param.clear();
		}
	}
	
	
	//解析字串为日志对象
	private AccessLogs parseAccessLogs(String str) throws Exception {
		AccessLogs log = null;
		if(testValidLog(str)){
			StringTokenizer stringTokenizer = new StringTokenizer(str, "$");
			Map<Integer, String> map = new HashMap<Integer, String>();
		
			for (int i = 1; stringTokenizer.hasMoreTokens(); i++) {
				String temp = ExStringUtils.trimToEmpty(stringTokenizer.nextToken());
				map.put(i, temp);	
			}	
			
			//127.0.0.1$ 
			//admin$
			//[16/Jun/2011:17:16:21 +0800]$ 
			//POST /hnjk/edu3/portal/message/list.html HTTP/1.1$ 
			//200$ 
			//20172$ 
			//1.234$ 
			//Mozilla/5.0 (Windows NT 5.1; rv:2.0.1) Gecko/20100101 Firefox/4.0.1
			if(null != map && map.size()==8){
				log = new AccessLogs();
				//ip
				log.setIpaddress(map.get(1));
				//用户
				String username = ExStringUtils.trimToEmpty(map.get(2));
				if(!"-".equals(username)){
					log.setUsername(username);
				}
				//日期
				log.setAccessTime(ExDateUtils.parseUSDate(map.get(3),"'['dd/MMM/yyyy:HH:mm:ss Z']'"));
				//资源
				String[] res = map.get(4).split("\\ ");
				if(res != null && res.length==3){	
					String url = ExStringUtils.trimToEmpty(ExStringUtils.substringBefore(ExStringUtils.substringBefore(res[1], "?"), "#"));
					if(url.indexOf(".html")==-1){//非法请求
						return null;
					}
					log.setUrl(url);
					log.setProtocol(res[2]);
				}
				//访问状态
				log.setServerStatus(Integer.parseInt(map.get(5)));
				//流量
				log.setNetFlow(Long.parseLong(map.get(6).replace("-", "0")));
				//处理时间
				log.setRunningTime(Double.parseDouble(map.get(7)));
				//客户端信息
				String bos = map.get(8);
				log.setClientBrowser(getClientBrowserOrOs(bos,"CodeClientBrowser"));
				log.setClientOs(getClientBrowserOrOs(bos,"CodeClientOs"));
			}
		}
		return log;
	}
	
	private boolean testValidLog(String str){
		if(str.indexOf("Googlebot")>-1 || str.indexOf("Yahoo! Slurp China")>-1 || str.indexOf(".html")==-1){
			return false;
		}
		return true;
	}
	//获取客户端浏览器或系统类型
	private String getClientBrowserOrOs(String bos, String dictCode) {
		if(ExStringUtils.isNotEmpty(bos)){
			List<Dictionary> list = CacheAppManager.getChildren(dictCode);
			for (Dictionary dict : list) {
				if(bos.contains(dict.getDictValue())){
					return dict.getDictValue();
				}
			}
		}
		return "OTHER";
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page findAccessLogsByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>(20);
		String hql = " from "+AccessLogs.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("serverStatus")){
			hql += " and serverStatus =:serverStatus ";
			values.put("serverStatus", condition.get("serverStatus"));
		}
		try {
			if(condition.containsKey("accessStartTime")){//访问开始时间
				hql += " and accessTime >=:accessStartTime ";
				values.put("accessStartTime", ExDateUtils.parseDate(condition.get("accessStartTime").toString(), ExDateUtils.PATTREN_DATE_TIME));
			}
			if(condition.containsKey("accessEndTime")){//访问结束时间
				hql += " and accessTime <=:accessEndTime ";
				values.put("accessEndTime", ExDateUtils.parseDate(condition.get("accessEndTime").toString(), ExDateUtils.PATTREN_DATE_TIME));
			}
		} catch (ParseException e) {
			throw new ServiceException(e);
		}		
		if(ExStringUtils.isNotEmpty(objPage.getOrderBy())){
			hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		}		
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}
	
	@Override
	public List<String> accessLogsDate() throws ServiceException {
		Session session  = exGeneralHibernateDao.getSessionFactory().getCurrentSession();
		String sql = " select distinct  to_char(t.accesstime,'yyyy-MM-dd') accessdate from hnjk_sys_accesslogs t where t.isdeleted=0 ";
		SQLQuery query = session.createSQLQuery(sql);
		return query.list();
	}
}
