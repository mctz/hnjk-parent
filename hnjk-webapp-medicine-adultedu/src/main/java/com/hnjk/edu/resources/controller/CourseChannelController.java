package com.hnjk.edu.resources.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.portal.model.Channel;
import com.hnjk.edu.portal.service.IChannelService;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
/**
 * 精品课程栏目管理.
 * <code>CourseChannelController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-11-26 下午04:38:52
 * @see 
 * @version 1.0
 */
@Controller
public class CourseChannelController extends BaseSupportController {
	private static final long serialVersionUID = 8193490830647524312L;
	
	@Autowired
	@Qualifier("channelService")
	private IChannelService channelService;	
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	/**
	 * 精品课程栏目
	 * @param courseId
	 * @param channelName
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/resources/coursechannel/list.html")
	public String listCourseChannel(String courseId, String channelName, String resourceid, ModelMap model) throws WebException{
		Map<String,Object> condition = new HashMap<String,Object>();
		if(ExStringUtils.isNotEmpty(channelName)){
			condition.put("channelName", channelName);
		}
		if(ExStringUtils.isNotEmpty(courseId)){//课程
			condition.put("courseId", courseId);
		}
		model.addAttribute("condition",condition);
		
		if(ExStringUtils.isNotBlank(courseId)){			
			List<Channel> channelList = channelService.findCourseChannelByCondition(condition);
			model.addAttribute("channelList", channelList);
			
			if(ExCollectionUtils.isEmpty(channelList)){
				Channel parentChannel = new Channel();
				parentChannel.setResourceid("");
				parentChannel.setChannelName("根栏目");
				parentChannel.setCourse(courseService.get(courseId));
				parentChannel.setChannelLevel(0);
				
				channelList.add(parentChannel);				
			}
		}		
		return "/edu3/resources/coursechannel/coursechannel-list";	
	}
	/**
	 * 新增编辑课程栏目
	 * @param courseId
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/resources/coursechannel/input.html")
	public String editCourseChannel(String courseId, String resourceid, ModelMap model) throws WebException{
		Channel courseChannel = null;
		Course course = null;
		List<Channel> channelList = null;
		if(ExStringUtils.isNotEmpty(resourceid)){//编辑
			courseChannel = channelService.get(resourceid);
			course = courseChannel.getCourse();
			courseId = course.getResourceid();
		} 
		if(ExStringUtils.isNotBlank(courseId)){					
			Map<String,Object> condition = new HashMap<String,Object>();
			condition.put("courseId", courseId);
			channelList = channelService.findCourseChannelByCondition(condition);
			model.addAttribute("channelList", channelList);		
			
			if(course==null) {
				course = courseService.get(courseId);
			}
		}
		
		if(ExStringUtils.isBlank(resourceid)){
			courseChannel = new Channel();				
			courseChannel.setCourse(course);
			courseChannel.setChannelType("normal");
			courseChannel.setChannelPosition("NAV");
			courseChannel.setShowOrder(channelList.size());
			if(ExCollectionUtils.isEmpty(channelList)){
				Channel parentChannel = new Channel();
				parentChannel.setResourceid("");
				parentChannel.setChannelName("根栏目");
				parentChannel.setCourse(course);
				parentChannel.setChannelLevel(0);
				
				channelList.add(parentChannel);				
				courseChannel.setParent(parentChannel);
			}
		}
		model.addAttribute("courseChannel", courseChannel);
		return "/edu3/resources/coursechannel/coursechannel-form";	
	}	
	
	/**
	 * 保存精品课程栏目
	 * @param channel
	 * @param courseId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/resources/coursechannel/save.html")
	public void saveCourseChannel(Channel channel, String courseId, HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			User user = SpringSecurityHelper.getCurrentUser();
			if(ExStringUtils.isNotBlank(courseId)){//设置课程
				Course course =courseService.get(courseId);
				channel.setCourse(course);
			}
			if(ExStringUtils.isNotBlank(channel.getParentId())){
				Channel parentChannel = channelService.get(channel.getParentId());
				channel.setParent(parentChannel);
			} else {
				Channel parentChannel = new Channel();
				parentChannel.setChannelName("根栏目");
				parentChannel.setCourse(channel.getCourse());
				parentChannel.setChannelLevel(0);	
				parentChannel.setShowOrder(0);
				parentChannel.setFillinManId(user.getResourceid());
				parentChannel.setFillinMan(user.getCnName());	
				
				channelService.save(parentChannel);//创建根栏目
				channel.setParent(parentChannel);
			}
			channel.setIsChild(Constants.BOOLEAN_YES);//设置自己为叶子栏目
			channel.getParent().setIsChild(Constants.BOOLEAN_NO);//设置父不是叶子
			channel.setChannelLevel(channel.getParent().getChannelLevel()+1);//栏目级别设置父栏目+1
			channel.setFillinManId(user.getResourceid());
			channel.setFillinMan(user.getCnName());
			channel.setChannelHref(ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(channel.getChannelHref()), "#"));
			if(ExStringUtils.isNotEmpty(channel.getResourceid())){
				Channel persistchannel = channelService.get(channel.getResourceid());
				ExBeanUtils.copyProperties(persistchannel,channel);
				channelService.update(persistchannel);
			} else {				
				channelService.save(channel);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_METARES_COURSECHANNEL");
			map.put("reloadUrl", request.getContextPath() +"/edu3/resources/coursechannel/list.html?courseId="+ExStringUtils.trimToEmpty(courseId));
		}catch (Exception e) {
			logger.error("保存栏目出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 删除精品课程栏目
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/resources/coursechannel/remove.html")
	public void removeCourseChannel(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();	
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				channelService.batchDelete(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");	
			}
		} catch (Exception e) {
			logger.error("删除栏目出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除失败！"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

}
