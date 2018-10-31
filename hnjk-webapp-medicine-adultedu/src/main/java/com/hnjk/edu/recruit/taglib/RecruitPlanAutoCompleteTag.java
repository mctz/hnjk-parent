package com.hnjk.edu.recruit.taglib;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.jsp.JspException;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.extend.taglib.BaseTagSupport;

/**
 * 招生批次自动填充.
 * <code>RecruitPlanAutoCompleteTag</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2012-1-12 下午04:41:30
 * @see 
 * @version 1.0
 */
public class RecruitPlanAutoCompleteTag extends BaseTagSupport{

	private static final long serialVersionUID = -1913001571420246474L;

	private String id;//ID
	
	private String name;//name
	
	private String tabindex;//排序号，唯一
	
	private String value;//默认值
	
	private String style;//样式
	
	private String classCss; //样式相当html中class

	
	@Override
	public int doStartTag() throws JspException {
		StringBuffer bf = new StringBuffer();
		bf.append("<script type=\"text/javascript\">");
		bf.append("$(document).ready(function(){");
		bf.append("$(\"select[class*=flexselect]\").flexselect();");
		bf.append("});");
		bf.append("</script>");
		bf.append("<select  class=\"flexselect\" id="+id+" name="+name+" tabindex="+tabindex+"");
		if(ExStringUtils.isNotEmpty(style)){
			bf.append(" style="+style+"");
		}
		bf.append(" >");
		bf.append("<option value=''></option>");
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select t.resourceid,t.yearid,t.recruitplanname,y.yearname ,t.term ")
				.append("from edu_recruit_recruitplan t ,edu_base_year y ")	
				.append(" where t.yearid=y.resourceid and t.isdeleted=0 ")
				.append("order by y.firstyear desc,t.term desc,t.recruitplanname desc");
			
			IBaseSupportJdbcDao baseJdbcDao = (IBaseSupportJdbcDao)SpringContextHolder.getBean("baseSupportJdbcDao");
			try {
				List<RecruitplanAutoCompleteVo> list = baseJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), RecruitplanAutoCompleteVo.class, null);
				
				if(null != list && list.size()>0){
					List<RecruitplanAutoCompleteVo> newList = new LinkedList<RecruitplanAutoCompleteVo>();
					//ExCollectionUtils.addAll(newList, list.iterator());
					
					String oldYearid = "";
					boolean isYear = false;					
					for(RecruitplanAutoCompleteVo plan : list){//遍历List,如果是同一个年度，则增加一个新的年度						
						String yearId = plan.getYearid();						
						if(oldYearid == "" || !yearId.equals(oldYearid)){
							isYear  = true;
						}
						if(isYear){
							//加入年份
							RecruitplanAutoCompleteVo vo = new RecruitplanAutoCompleteVo();
						    vo.setYearid(yearId);
							vo.setYearname(plan.getYearname());
							//newList.add(vo);
						}
						newList.add(plan);
						
						isYear  = false;
						oldYearid = yearId;
					}
					if(!newList.isEmpty()){//遍历新list，构造option
						for(RecruitplanAutoCompleteVo vo : newList){
							if(null == vo ) {
								continue;
							}
							bf.append("<option ");							
							if(ExStringUtils.isEmpty(vo.getResourceid())){//年度
								if(ExStringUtils.isNotBlank(value) && value.equals(vo.getYearid())) {
									bf.append(" selected ");
								}
								bf.append(" value='").append(vo.getYearid()+"'>&lt;b&gt;"+vo.getYearname()+"&lt;/b&gt;");
							}else{//招生计划
								if(ExStringUtils.isNotBlank(value) && value.equals(vo.getResourceid())) {
									bf.append(" selected ");
								}
								bf.append(" value='").append(vo.getResourceid()+"'>&lt;span style='padding-left:8px' &gt;"+vo.getRecruitplanname()+"&lt;/span&gt;");
							}
							bf.append("</option>");
						}
					}
				}
			} catch (Exception e) {				
			}
			bf.append("</select>");		
		
			this.pageContext.getOut().append(bf.toString());
		} catch (IOException e) {
			logger.error("输出招生批次自动填充组件出错："+e.fillInStackTrace());
		}
		return super.doStartTag();
	}

	/**
	 * @return the id
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the tabindex
	 */
	public String getTabindex() {
		return tabindex;
	}

	/**
	 * @param tabindex the tabindex to set
	 */
	public void setTabindex(String tabindex) {
		this.tabindex = tabindex;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * @param style the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * @return the classCss
	 */
	public String getClassCss() {
		return classCss;
	}

	/**
	 * @param classCss the classCss to set
	 */
	public void setClassCss(String classCss) {
		this.classCss = classCss;
	}
	
	
	
	
}
