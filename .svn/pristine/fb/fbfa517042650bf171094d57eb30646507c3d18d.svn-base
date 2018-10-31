package com.hnjk.core.dao.jdbc;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import org.junit.Test;

import com.hnjk.core.BaseTest;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;

public class AccessLogsJdbcSupportDaoTest extends BaseTest{
	
	

	@Test	
	public void testParseAndSaveAccessLogs() throws Exception {
		
		IBaseSupportJdbcDao jdbcDao = (IBaseSupportJdbcDao)springContextForUnitTestHolder.getBean("baseSupportJdbcDao");
								
		File parent = new File("f:/logs/");
		
		String[] logFiles = parent.list();
		
		for(String file : logFiles){
			if(file.indexOf("2013-01")>=0){//6月份数据
				System.out.println("insert "+file+" to database...");
				testBigDataInsert("f:/logs/"+file,jdbcDao);
			}
			
		}
		
	}
	
	private void testBigDataInsert(String fileName,IBaseSupportJdbcDao jdbcDao) throws Exception {
		
		List<Object[]> objects = new ArrayList<Object[]>();
		long length = new File(fileName).length();
		MappedByteBuffer in = new RandomAccessFile(fileName,"r").getChannel().map(FileChannel.MapMode.READ_ONLY,0,length);
		CharBuffer bf = CharBuffer.allocate(6048);	
		
		long startime = System.currentTimeMillis(); 
		
		int index = 0;
		while(index < length){
			char c = (char)in.get(index++);				
			if(c == '\r' || c == '\n'){	
				AccessLogs log = parseAccessLogs(new String(bf.array(),0,bf.position()));
				if(log != null){
					objects.add(log.getArray());
				}
				if(objects.size() > 10000){
					//batchSaveOrUpdate(logs);
					//flush();
					jdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate("insert into hnjk_sys_accesslogs l " +
							"(l.resourceid,l.ipaddress,l.accesstime,l.protocol,l.serverstatus,l.netflow,l.runningtime,l.url,l.clientos,l.clientbrowser,l.isdeleted,l.version,l.username)"+ 
							"values(?,?,?,?,?,?,?,?,?,?,?,?,?)", objects);
					System.out.println("====>insert records:"+objects.size()+", total time :"+(System.currentTimeMillis()-startime)+"ms.");
					objects.clear();
					startime = System.currentTimeMillis();
					objects = new ArrayList<Object[]>();					
				}
				bf.clear();
			}else{
				bf.put(c);
			}
		}
		
		startime = System.currentTimeMillis();
		
		if(objects.size()>0){			
			jdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate("insert into hnjk_sys_accesslogs l " +
					"(l.resourceid,l.ipaddress,l.accesstime,l.protocol,l.serverstatus,l.netflow,l.runningtime,l.url,l.clientos,l.clientbrowser,l.isdeleted,l.version,l.username)"+ 
					"values(?,?,?,?,?,?,?,?,?,?,?,?,?)", objects);
			System.out.println("====>insert records:"+objects.size()+", total time :"+(System.currentTimeMillis()-startime)+"ms.");
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
					log.setUrl(ExStringUtils.substringBefore(ExStringUtils.substringBefore(res[1], "?"), "#"));
					if(log.getUrl().indexOf("/edu3")>-1){
						//去掉 web root context
						log.setUrl(log.getUrl().substring(5, log.getUrl().length()));
					}
					if(log.getUrl().length()>255)
						log.setUrl(log.getUrl().substring(0, 255));
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
				log.setClientBrowser(getClientBrowserOrOs(bos,"browser"));
				log.setClientOs(getClientBrowserOrOs(bos,"os"));
			}
		}
		return log;
	}
	
	///favicon.ico;/edu3/j_spring_security_check;/edu3/edu3/framework/;/edu3/edu3/login.html;/edu3/j_spring_security_logout
	private boolean testValidLog(String str){
		if(str.indexOf("Googlebot")>-1 || 
				str.indexOf("Yahoo! Slurp China")>-1 ||
				str.indexOf("/favicon.ico")>-1 ||
				str.indexOf("/edu3/j_spring_security_check")>-1 ||
				str.indexOf("/edu3/edu3/framework/")>-1 ||
				str.indexOf("/edu3/edu3/login.html")>-1 ||
				str.indexOf("/edu3/j_spring_security_logout")>-1 ||
				str.indexOf(".html")==-1){
			return false;
		}
		return true;
	}
	//获取客户端浏览器或系统类型
	private String getClientBrowserOrOs(String key,String type) {
		Map<String, Object> browsMap = new HashMap<String, Object>();
		browsMap.put("MSIE 6", "IE6");
		browsMap.put("MSIE 7", "IE7");
		browsMap.put("MSIE 8", "IE8");
		browsMap.put("MSIE 9", "IE9");
		browsMap.put("Firefox", "Firefox");
		browsMap.put("Chrome", "Chrome");
		browsMap.put("Opera", "Opera");
		browsMap.put("OTHER", "OTHER");
		
		Map<String, Object> osMap = new HashMap<String, Object>();
		osMap.put("Windows NT 5.1", "WINDOWS_XP");
		osMap.put("Windows NT 6.0", "WINDOWS_VISTA");
		osMap.put("Windows NT 6.1", "WINDOWS_7");
		osMap.put("Linux", "LINUX");
		osMap.put("Mac OS X", "MAC_OS");
		osMap.put("Windows NT 5.0", "WINDOWS_2000");
		osMap.put("Windows NT 5.2", "WINDOWS_2003");
		osMap.put("OTHER", "OTHER");
		Set<Entry<String, Object>> set = browsMap.entrySet();
		if(type.equals("os")){
			set = osMap.entrySet();
		}
		for(Map.Entry<String, Object> entry : set){
			if(key.contains(entry.getKey())){
				return entry.getKey();
			}
		}		
		
		return "OTHER";
	}
	
	@Test
	public void test11() throws Exception{
		System.out.println("/edu3/edu3/learning/interactive/materesource/view.html?mateName=1.3%20%E4%BC%81%E4%B8%9A%E5%9F%BA%E6%9C%AC%E7%BB%8F%E6%B5%8E%E4%B8%9A%E5%8A%A1%E4%B8%8E%E4%BC%9A%E8%AE%A1%E6%96%B9%E7%A8%8B%E5%BC%8F%EF%BC%882%EF%BC%89&mateType=2&mateUrl=http://cs.scutde.net//t16courses/1635-wp010xnbjc/video/150/v010302.asf".length());
	}
}
