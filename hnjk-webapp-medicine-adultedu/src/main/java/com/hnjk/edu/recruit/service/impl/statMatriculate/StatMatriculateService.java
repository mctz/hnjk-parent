/*
 * 统计录取情况业务
 */
package com.hnjk.edu.recruit.service.impl.statMatriculate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.rao.dao.hibernate.BaseSupportHibernateDao;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.recruit.model.EnrolleeInfo;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.service.IOrgUnitService;

/**
 * @author 
 */
@Service("statMatriculateService")
@Transactional
public class StatMatriculateService extends BaseSupportHibernateDao{
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
   
	/**统计录取情况，
	 * @param recruitPlan
	 * @param major
	 * @param classic
	 * @param isMatriculate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  List<ResultBean> statMatriculate(String recruitPlan,String major, String classic, String isMatriculate) {
		
      String hqlMajorIdList = "select recruitmajor.major.resourceid from RecruitMajor recruitmajor where " +
      		"recruitmajor.recruitPlan.resourceid = '"  +recruitPlan+"' group by recruitmajor.major.resourceid";
      List<String> majorIdList = (List<String>) exGeneralHibernateDao.findByHql(hqlMajorIdList);//招生专业
      List<Major> majorList = new ArrayList<Major>();

            for (String  id:  majorIdList)
            {                  
                majorList.add((Major) exGeneralHibernateDao.get(Major.class, id));
            }         
            
            String hqlClassicIdList = "select recruitmajor.classic.resourceid from RecruitMajor recruitmajor where " +
      		"recruitmajor.recruitPlan.resourceid = '"  +recruitPlan+"' group by recruitmajor.classic.resourceid";
      List<String> classicIdList = (List<String>) exGeneralHibernateDao.findByHql(hqlClassicIdList);//招生层次
      List<Classic> classicList = new ArrayList<Classic>();
      for (String  id:  classicIdList)
      {
            
    	  classicList.add((Classic) exGeneralHibernateDao.get(Classic.class, id));
      }
         
    // List<BranchSchool> schoolList = (List<BranchSchool>) exGeneralHibernateDao.getAll(BranchSchool.class );
      List<OrgUnit> orgUnitList = orgUnitService.findOrgUnitListByType(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue());//校外学习中心

            List<ResultBean> resultList = new ArrayList<ResultBean>();
            ResultBean staticNames = new ResultBean();

            staticNames.setSchoolName("学习中心");
            staticNames.setClassic(null);

            ResultBean countBean = new ResultBean();
            countBean.setSchoolName("总计");
            staticNames.setClassic("层次");

            resultList.add(staticNames);

            List majorName = staticNames.getCounts();
            boolean hasInitNames = false;
          
            
            String sql = "select new "
                + ReportStateBean.class.getName()
                + "(enrolleeinfo.branchSchool.resourceid, "
                + "enrolleeinfo.recruitMajor.major.resourceid, "
                + "enrolleeinfo.recruitMajor.classic.resourceid, "
                + "count(distinct enrolleeinfo.resourceid)) from "
                + EnrolleeInfo.class.getName()
                + " enrolleeinfo where enrolleeinfo.recruitMajor.recruitPlan.resourceid = ?";
        if ("Y".equals(isMatriculate))
        {
        
            sql += " and enrolleeinfo.isMatriculate = 'Y'";
        }
        else if ("N".equals(isMatriculate))
        {
            sql += " and (enrolleeinfo.isMatriculate != 'Y' or enrolleeinfo.isMatriculate is null)";
        }
        if(!"".equals(major))
        {
        	sql += "and enrolleeinfo.recruitMajor.major.resourceid ='"+major+"'";
        }
        if(!"".equals(classic))
        {
        	sql += "and enrolleeinfo.recruitMajor.classic.resourceid ='"+classic+"'";
        }
        sql += " group by enrolleeinfo.branchSchool.resourceid, "
                + "enrolleeinfo.recruitMajor.major.resourceid, "
                + "enrolleeinfo.recruitMajor.classic.resourceid";

        List<ReportStateBean> beanList = (List<ReportStateBean>) exGeneralHibernateDao.findByHql(sql, recruitPlan);
        
        
           
            Map<ReportStateKey, Integer> state = new HashMap<ReportStateKey, Integer>();
            for (ReportStateBean bean : beanList)
            {
                state.put(bean.getKey(), bean.getCount());
            }
            for (Iterator schoolIter = orgUnitList.iterator(); schoolIter.hasNext();)
            {
            	OrgUnit school = (OrgUnit) schoolIter.next();
                for (Iterator classicIter = classicList.iterator(); classicIter.hasNext();)
                {
                    Classic classicObject = (Classic) classicIter.next();
                    ResultBean bean = new ResultBean();
                    bean.setSchoolName(school.getUnitName());
                    bean.setClassic(classicObject.getClassicName());
                    int index = 0;
                    int enrolleeCount = 0;
                    for (Iterator majorIter = majorList.iterator(); majorIter
                            .hasNext(); index++)
                    {
                        Major majorObject = (Major) majorIter.next();
                        if (!hasInitNames)
                        {
                            majorName.add(index, majorObject.getMajorName());
                        }
                        Integer count = state.get(new ReportStateKey(school.getResourceid(), majorObject.getResourceid(),
                                classicObject.getResourceid()));
                        if (count != null)
                        {
                            bean.getCounts().add(index, "" + count);
                        }
                        else
                        {
                            bean.getCounts().add(index, "");
                        }
                        if (count != null)
                        {
                            enrolleeCount += count;
                        }
                        int orgin = 0;
                        try
                        {
                            orgin = Integer.parseInt((String) countBean
                                    .getCounts().get(index));
                        }
                        catch (Exception e)
                        {
                            orgin = 0;
                        }
                        if (count != null)
                        {
                            orgin += count;
                        }
                        try
                        {
                            countBean.getCounts().set(index, "" + orgin);
                        }
                        catch (Exception e)
                        {
                            countBean.getCounts().add(index, "" + orgin);
                        }
                    }
                    if (!hasInitNames)
                    {
                        majorName.add(index, "合计");
                    }
                    bean.getCounts().add(index, "" + enrolleeCount);
                    int total = 0;
                    try
                    {
                        total = Integer.parseInt((String) countBean.getCounts()
                                .get(index));
                    }
                    catch (Exception e)
                    {
                        total = 0;
                    }
                    total += enrolleeCount;
                    try
                    {
                        countBean.getCounts().set(index, "" + total);
                    }
                    catch (Exception e)
                    {
                        countBean.getCounts().add(index, "" + total);
                    }
                    hasInitNames = true;
                    resultList.add(bean);
                }
            }
            resultList.add(countBean);
           
            return resultList;
           
      
     
	}
	
}