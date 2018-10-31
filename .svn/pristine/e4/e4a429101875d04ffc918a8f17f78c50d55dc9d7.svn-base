package com.hnjk.edu.portal.taglib;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.portal.model.Channel;
import com.hnjk.extend.taglib.BaseTagSupport;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 文章栏目标签，提供栏目树和栏目select两种.
 * <code>ChannelTag</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-28 上午10:07:19
 * @see 
 * @version 1.0
 */
public class ChannelTag extends BaseTagSupport{

	private static final long serialVersionUID = 983245772966980324L;
	
	transient private List<Channel> channelList;//栏目列表
	
	private String viewType;//视图类型，select
	
	private String defaultValue;//默认值
	
	private String id = "channelId"; //
	
		
	@Override
	public int doStartTag() throws JspException {
		if(null == channelList){
			return super.doStartTag();
		}
		try {
			StringBuilder sb = new StringBuilder();
			//获取当前用户
			User cureentUser = SpringSecurityHelper.getCurrentUser();		   
			if("select".equals(viewType)){
				sb.append("<select name=\"channelId\" id=\""+id+"\" size=\"1\">");
				if(cureentUser.getOrgUnit().getUnitType().
						indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
					sb.append("<option value=''>请选择...</option>");
				}
				for(Channel channel : channelList){
					//判断当前用户组织是否为校外学习中心，如果是，则只显示校外学习中心的栏目
					if(cureentUser.getOrgUnit().getUnitType().
							indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0
							&& channel.getIsOpenBrSchool().equals(Constants.BOOLEAN_NO)
						){
						
						continue;
					}
					sb.append("<option value="+channel.getResourceid());
					if(defaultValue.equals(channel.getResourceid())){
						sb.append(" selected ");
					}
					sb.append(" >");
					for(int i=1;i<=channel.getChannelLevel();i++){
						sb.append("&nbsp;&nbsp;");
					}
					sb.append(channel.getChannelName());
					sb.append("</option>");
				}				
				sb.append("</select>");
			}
			JspWriter writer = this.pageContext.getOut();
			writer.append(sb.toString());
		} catch (Exception e) {
			logger.error("载入栏目标签出错：{}",e.fillInStackTrace());
		}		
		
		return EVAL_PAGE;
	}

	public List<Channel> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<Channel> channelList) {
		this.channelList = channelList;
	}

	
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}
		
}
