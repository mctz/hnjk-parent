package com.hnjk.platform.system.service.jdbcimpl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.jdbc.BaseSupportJdbcDao;
import com.hnjk.platform.system.service.IAccessLogsJDBCService;

/**
 * 访问日志统计. <code>AccessLogsJDBCServiceImpl</code>
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-6-20 下午03:02:56
 * @see
 * @version 1.0
 */
@Transactional
@Service("accessLogsJDBCService")
public class AccessLogsJDBCServiceImpl extends BaseSupportJdbcDao implements IAccessLogsJDBCService {

	@Override
	public List<Map<String, Object>> statAccessLogsStatus(Map<String, Object> condition) throws ServiceException {
		String type = condition.get("type").toString();		
		String accessDate = condition.get("accessDate").toString();
		StringBuffer sb = new StringBuffer();
		
		sb.append(" select sec.sectime, count(distinct s.ipaddress) ipcount, count(s.url) urlcount, sum(nvl(s.netflow,0)) netflowcount from ");
		if("1".equals(type)){	//按日				
			sb.append(" (select to_char(to_date('"+accessDate+"','yyyy-MM-dd')+(level-1)/24,'yyyy-MM-dd HH24') sectime from dual connect by level<=25) sec ");
			sb.append(" left join ");
			sb.append(" (select t.*,to_char(t.accesstime,'yyyy-MM-dd HH24') secdate from hnjk_sys_accesslogs t where to_char(t.accesstime,'yyyy-MM-dd') ='"+accessDate+"'  ) s"); 
		} else if("2".equals(type)) {//按月			
			sb.append(" (select to_char(to_date('"+accessDate+"','yyyy-MM')+(level-1),'yyyy-MM-dd') sectime from dual connect by level<=to_number(to_char(last_day(to_date('"+accessDate+"','yyyy-MM')),'dd')) ) sec ");
			sb.append(" left join ");
			sb.append(" (select t.*,to_char(t.accesstime,'yyyy-MM-dd') secdate from hnjk_sys_accesslogs t where to_char(t.accesstime,'yyyy-MM') ='"+accessDate+"'  ) s"); 
		} else {//近三月,近六个月
			sb.append(" (select to_char(to_date('"+accessDate+"','yyyy-MM')+(level-1),'yyyy-MM-dd') sectime from dual connect by level<="+condition.get("days")+" ) sec ");
			sb.append(" left join ");
			sb.append(" (select t.*,to_char(t.accesstime,'yyyy-MM-dd') secdate from hnjk_sys_accesslogs t where to_char(t.accesstime,'yyyy-MM') >='"+accessDate+"'  ) s"); 
		}
		sb.append(" on sec.sectime = s.secdate ");
		sb.append(" group by sec.sectime ");
		sb.append(" order by sec.sectime ");		
		
		try {
			List<Map<String, Object>> list = this.baseJdbcTemplate.findForListMap(sb.toString(), null);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}				
		return null;
	}
	
	@Override
	public List<Map<String, Object>> statAccessLogsResults(String type) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		
		sb.append(" select t."+type+" target,count(*) resultcount from hnjk_sys_accesslogs t ");
		sb.append(" group by t."+type);
		sb.append(" order by resultcount desc ");
		try {
			List<Map<String, Object>> list = this.baseJdbcTemplate.findForListMap(sb.toString(), null);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}				
		return null;
	}
	
	@Override
	public Double statAccessLogsResultsTotal() throws ServiceException {
		String sql = " select count(*) totalresult from hnjk_sys_accesslogs t ";
		
		try {
			Long total = this.baseJdbcTemplate.findForLong(sql, null);
			return total.doubleValue();
		} catch (Exception e) {
			e.printStackTrace();
		}				
		return 0.0;
	}
	
	@Override
	public List<Map<String, Object>> statAccessLogsTop(int type) throws ServiceException {
		StringBuffer sb = new StringBuffer();		
		switch (type) {
			case 1://并发访问用户数
				sb.append(" select t.accesstime,count(distinct t.ipaddress) ipcount from hnjk_sys_accesslogs t ");
				sb.append(" group by t.accesstime ");
				sb.append(" order by ipcount desc ");
				break;
			case 2://访问次数最多的用户
				sb.append(" select t.ipaddress,count(*) ipcount from hnjk_sys_accesslogs t ");
				sb.append(" group by t.ipaddress ");
				sb.append(" order by ipcount desc ");
				break;
			case 3://访问最多的资源
				sb.append(" select t.url,count(t.url) morecount from hnjk_sys_accesslogs t ");
				sb.append(" group by t.url ");
				sb.append(" order by morecount desc ");
				break;
			case 4:// 处理最慢的资源
				sb.append(" select t.url,t.runningtime,t.accesstime from hnjk_sys_accesslogs t ");
				sb.append(" order by t.runningtime desc ");
				break;
			case 5: //错误做多的资源
				sb.append(" select t.url,count(*) count500 from hnjk_sys_accesslogs t  ");
				sb.append(" where t.serverstatus=500 ");
				sb.append(" group by t.url ");
				sb.append(" order by count500 desc ");
				break;
		}
		try {
			String sql = "select * from ("+sb.toString()+" ) where rownum<11";
			List<Map<String, Object>> list = this.baseJdbcTemplate.findForListMap(sql, null);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}				
		return null;
	}
}
