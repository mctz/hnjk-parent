package com.hnjk.edu.resources.taglib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.edu.portal.model.Channel;
import com.hnjk.edu.portal.service.IChannelService;
import com.hnjk.extend.taglib.BaseTagSupport;
/**
 * 精品课程栏目标签.
 * <code>CourseChannelTag</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-11-27 下午02:15:04
 * @see 
 * @version 1.0
 */
public class CourseChannelTag extends BaseTagSupport {
	private static final long serialVersionUID = -1957754857644371476L;
	
	private String courseId;//课程id
	
	@Override
	public int doStartTag() throws JspException {
		try {
			Map<String, String> directMap = subUrlToParentUrlMap();
			String resUrl = ExStringUtils.substringAfter(getrequest().getRequestURI(), ExStringUtils.trimToEmpty(getrequest().getContextPath()));//资源路径
			if(directMap.containsKey(resUrl)) {
				resUrl = directMap.get(resUrl);//父链接高亮显示
			}
			StringBuilder sb = new StringBuilder();
			sb.append("<div id=\"navigation\"><ul>");
			sb.append("<li "+(ExStringUtils.endsWith(resUrl, ".jsp")?"class=\"over\"":"")+"><a href=\""+getBaseUrl()+"/resource/course/index.html?courseId="+ExStringUtils.trimToEmpty(courseId)+"\" title=\"首页\">首页</a></li>");
			String channelid = "";
			if("/resource/course/ref.html".equals(resUrl)){
				channelid = getrequest().getParameter("channelid");
			}
			if(ExStringUtils.isNotBlank(courseId)){
				IChannelService channelService = SpringContextHolder.getBean("channelService");				
				Map<String,Object> condition = new HashMap<String,Object>();
				condition.put("courseId", courseId);
				condition.put("channelLevel", 1);
				condition.put("channelStatus", 0);
				condition.put("channelPosition", "NAV");//头部
				List<Channel> channelList = channelService.findCourseChannelByCondition(condition);
				
				if(ExCollectionUtils.isNotEmpty(channelList)){
					for (Channel channel : channelList) {
						for (Channel child : channel.getChildren()) {
							if(resUrl.equalsIgnoreCase(child.getChannelHref())){
								resUrl = channel.getChannelHref();//把子级栏目链接映射为父级链接
								break;
							}
						}
					}
					int size = channelList.size();
					for (int i = 0; i < size; i++) {
						Channel channel = channelList.get(i);
						sb.append("<li ");
						if(ExStringUtils.equals(resUrl, channel.getChannelHref()) || channel.getResourceid().equals(channelid)){
							sb.append("class=\"over"+(i==size-1?" end":"")+"\"");
						} else {
							sb.append((i==size-1?"class=\"end\"":""));
						}
						sb.append(">");
						sb.append("<a title=\""+channel.getChannelContent()+"\" ");
						String url = channel.getChannelHref();
						if(!"#".equals(url)){
							if("outlink".equals(channel.getChannelType())){//外部链接
								sb.append(" target=\"_blank\" ");
								url = channel.getChannelHref();
							} else if("locallinkA".equals(channel.getChannelType())){//本地附件
								url = getRootUrl()+channel.getChannelHref();
							} else if("locallink".equals(channel.getChannelType())){//本地链接
								sb.append(" target=\"_blank\" ");
								url = getBaseUrl()+channel.getChannelHref()+"?courseId="+channel.getCourse().getResourceid();
							} else if("outlocallink".equals(channel.getChannelType())){//本地打开外部网页
								url = getBaseUrl()+"/resource/course/ref.html?channelid="+channel.getResourceid();
							} else {//本地链接
								url = getBaseUrl()+channel.getChannelHref()+"?courseId="+channel.getCourse().getResourceid();
							}							
						}
						sb.append(" href=\""+url+"\" >"+channel.getChannelName()+"</a>");
						sb.append("</li>");
					}
				}
			}			
			sb.append("</ul></div>");
			JspWriter writer = this.pageContext.getOut();
			writer.append(sb.toString());
		} catch (Exception e) {
			logger.error("载入栏目标签出错：{}",e.fillInStackTrace());
		}		
		
		return EVAL_PAGE;
	}	
	
	public Map<String, String> subUrlToParentUrlMap(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("/resource/course/exercise.html", "/resource/course/exercisebatch.html");//作业系统答题页面
		map.put("/resource/course/mocktest.html", "/resource/course/exercisebatch.html");//在线自测系统
		map.put("/resource/course/studentexercise.html","/resource/course/exerciseshow.html");//作业展示查看
		return map;
	}
	
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
}
