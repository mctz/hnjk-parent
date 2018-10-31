package com.hnjk.edu.recruit.service.impl.stat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.support.base.service.BaseServiceImpl;

/**
 * 这个类用来返回统计数字,这里通过SQL的group by子句来实现
 * 
 * @author
 * @since
 */

@Service("statisticBusiness")
@Transactional
public class StatisticBusiness  extends BaseServiceImpl implements IStatService {

	
	private final static int SIGNUP = 1;
	private final static int ENTRANCE = 2;
	private final static int NOTEST = 3;

	/**
	 * this is a complex hql that use the group by and join many tables but here
	 * doesn't use the join keyword
	 * 
	 * @param plan
	 *            the planguid
	 * @return String the hql
	 */
	@Override
	public  String getSql(String plan, boolean isCenter, String branch,
						  int type) {
		StringBuffer sb = new StringBuffer(200);
		sb.append("select ");
		sb.append("e.branchSchool.unitShortName,");
		sb.append("e.recruitMajor.classic.classicName,");
		sb.append("e.recruitMajor.major.majorName,");
		sb.append("count(e),");
		sb.append("count(e),");
		sb.append("count(e),");
		sb.append("count(e) ");
		sb.append("from ");
		sb.append("EnrolleeInfo e");
		sb.append(" where ");
		sb.append("e.recruitMajor.recruitPlan.resourceid='");
		sb.append(plan);
		sb.append("' ");
		sb.append("and e.isDeleted='0' ");
		if (!isCenter) {
			sb.append("and e.branchSchool.resourceid='");
			sb.append(branch);
			sb.append("' ");
		}
		switch (type) {
		case SIGNUP:
			sb.append("and e.signupFlag='Y' ");
			break;

		case NOTEST:
			sb.append("and e.signupFlag='Y' and e.isApplyNoexam='Y' and e.noExamFlag='Y' ");
			break;                                

		case ENTRANCE:
			sb.append("and e.signupFlag='Y' and e.entranceflag='Y' ");
			break;

		default:
			break;
		}
		sb.append("group by ");
		sb.append("e.branchSchool.unitShortName , e.recruitMajor.classic.classicName , e.recruitMajor.major.majorName");
		return sb.toString();
	}

	/**
	 * 统计免试资格
	 * 
	 * @param plan
	 * @param isCenter
	 * @param branch
	 * @return
	 * @throws Exception
	 */
	@Override
	@SuppressWarnings("unchecked")
	public  List getNoExamStat(String plan, boolean isCenter,
			String branch) throws Exception {
		List stat = getStatList(plan, isCenter, branch, NOTEST);
		Map map = new TreeMap();
		for (int i = 0; i < stat.size(); i++) {
			Statistic s = (Statistic) stat.get(i);

			StatView sv = new StatView();
			sv.setBranch(s.getBranch());
			sv.setClassic(s.getClassic());

			if (map.containsValue(sv)) {
				StatView old = (StatView) map.get(sv);
				old.set(s.getMajor(), s.getNotest());
				// 行向统计
				old.setTotal(old.getTotal() + s.getNotest());
			} else {

				sv.set(s.getMajor(), s.getNotest());
				sv.setTotal(s.getNotest());
				map.put(sv, sv);
			}
		}
		List res = new ArrayList(map.keySet());
		addStat(res);
		return res;
	}

	/**
	 * 统计入学资格
	 * 
	 * @param plan
	 * @param isCenter
	 * @param branch
	 * @return
	 * @throws Exception
	 */
	@Override
	@SuppressWarnings("unchecked")
	public  List getEntranceStat(String plan, boolean isCenter,
			String branch) throws Exception {
		List stat = getStatList(plan, isCenter, branch, ENTRANCE);
		Map map = new TreeMap();
		for (int i = 0; i < stat.size(); i++) {
			Statistic s = (Statistic) stat.get(i);

			StatView sv = new StatView();
			sv.setBranch(s.getBranch());
			sv.setClassic(s.getClassic());

			if (map.containsValue(sv)) {
				StatView old = (StatView) map.get(sv);
				old.set(s.getMajor(), s.getEntrance());
				// 行向统计
				old.setTotal(old.getTotal() + s.getEntrance());
			} else {

				sv.set(s.getMajor(), s.getEntrance());
				sv.setTotal(s.getEntrance());
				map.put(sv, sv);
			}
		}
		List res = new ArrayList(map.keySet());
		addStat(res);
		return res;
	}

	/**
	 * 统计报名资格
	 * 
	 * @param stat
	 *            List of Statistic
	 * @return List of StatView
	 * @throws Exception 
	 */

	// 现无法判断用户属于哪个学习中心，未传入isCenter参数
	// public static List getSignupStat(String plan,boolean isCenter,String
	// branch) throws Exception {
	@Override
	@SuppressWarnings("unchecked")
	public  List<StatView> getEnrollStat(String plan, boolean isCenter,
			String branch) throws Exception  {
		List stat = getStatList(plan, isCenter, branch, SIGNUP);
		Map map = new TreeMap();
		for (int i = 0; i < stat.size(); i++) {
			Statistic s = (Statistic) stat.get(i);

			// 这里每次要生产一个StatView
			StatView sv = new StatView();
			sv.setBranch(s.getBranch());
			sv.setClassic(s.getClassic());

			if (map.containsValue(sv)) {
				StatView old = (StatView) map.get(sv);
				old.set(s.getMajor(), s.getSignup());
				// 行向统计
				old.setTotal(old.getTotal() + s.getSignup());
			} else {
				sv.set(s.getMajor(), s.getSignup());
				sv.setTotal(s.getSignup());
				map.put(sv, sv);
			}
		}
		List res = new ArrayList(map.keySet());
		addStat(res);
		return res;
	}

	/**
	 * 统计结果 加上最后的合计行
	 */
	@SuppressWarnings("unchecked")
	private  void addStat(List statview) {
		StatView total = new StatView();
		int sum = 0;
		total.setBranch("合计");
		total.setClassic("");
		List<String> majornamelist = null;

		majornamelist = exGeneralHibernateDao.getHibernateTemplate().find(
				"select m.majorName from Major m order by m.majorName");

		for (int i = 0; i < statview.size(); i++) {
			StatView temp = (StatView) statview.get(i);
			Map<String, Integer> tempMap = temp.getMajors();
			Map<String, Integer> totalMap = total.getMajors();
			for (String name : majornamelist) {
				Integer count = totalMap.get(name);
				Integer tempcount = tempMap.get(name);
				if (count == null) {
					count = new Integer(0);
				}
				if (tempcount == null) {
					tempcount = new Integer(0);
				}
				totalMap.put(name, count + tempcount);
			}
			sum += temp.getTotal();
		}
		total.setTotal(sum);
		statview.add(total);
	}

	/**
	 * get the gross statistic List
	 * 
	 * @param plan
	 *            planguid
	 * @return Statistic List
	 * @throws Exception
	 */
	@Override
	@SuppressWarnings("unchecked")
	public  List getStatList(String plan, boolean isCenter,
			String branch, int type) throws Exception {
		String sql = getSql(plan, isCenter, branch, type);
		
		
		HibernateTemplate ht = exGeneralHibernateDao.getHibernateTemplate();
		List list  = ht.find(sql);

		List res = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Object[] objs = (Object[]) list.get(i);
			Statistic s = new Statistic();
			s.setBranch((String) objs[0]);
			s.setClassic((String) objs[1]);
			s.setMajor((String) objs[2]);
			s.setSignup(Integer.valueOf( objs[3].toString()));
			s.setEntrance(Integer.valueOf( objs[4].toString()));
			s.setNotest(Integer.valueOf( objs[5].toString()));
			s.setTotal(Integer.valueOf( objs[6].toString()));
			res.add(s);
		}
		return res;
	}
	
}
