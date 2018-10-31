package com.hnjk.core.annotation;

import java.rmi.RemoteException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;

@Service("remotingService")
@Transactional
@Remoting(servicename="remotingService",serviceInterface=IRemotingService.class,
		urlMapping="/remoting/test/remotingService.html",methods={"remotingInvoker,remotingScheduleInvoker"})
public class RemotingServiceImpl implements IRemotingService {

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Override	
	public String simpleInvoker(String args) throws RemoteException {
			return "hellow,"+args;	
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> remotingInvoker(final String sql,Map<String, Object> condition) throws RemoteException {		
		try {
			return baseSupportJdbcDao.getBaseJdbcTemplate().findForMap(sql, condition);
		} catch (Exception e) {
			throw new RemoteException("Remoting exception :"+e.fillInStackTrace());
		}
	}

	@Override
	@Schedule(name="testSchdule",expression="0 */1 * * * ?", desc = "测试调度器", group = "group1")
	public void remotingScheduleInvoker() throws RemoteException {
		System.out.println("this is test.");		
	}
	

}
