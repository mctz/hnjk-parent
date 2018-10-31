package com.hnjk.edu.portal.controller;

import java.util.ArrayList;
import java.util.Date;
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
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.portal.model.Article;
import com.hnjk.edu.portal.model.Channel;
import com.hnjk.edu.portal.model.HelpChannel;
import com.hnjk.edu.portal.service.IArticleService;
import com.hnjk.edu.portal.service.IChannelService;
import com.hnjk.edu.portal.service.IHelperChannelService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IRoleService;

/**
 * <code>ChannelController</code>门户网站栏目管理.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-20 下午02:21:37
 * @see 
 * @version 1.0
 */
@Controller
public class ChannelController extends BaseSupportController {
	private static final long serialVersionUID = 1856925083078082139L;
	
	@Autowired
	@Qualifier("channelService")
	private IChannelService channelService;	
	
	@Autowired
	@Qualifier("articleService")
	private IArticleService articleService;
	
	@Autowired
	@Qualifier("helperChannelService")
	private IHelperChannelService helperChannelService;
	
	@Autowired
	@Qualifier("roleService")
	private IRoleService roleService;
	
	/**
	 * 门户栏目列表
	 * @param objPage
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/portal/manage/channel/list.html")
	public String channelList(Page objPage, String channelName,String channelPosition, ModelMap model) throws WebException{
		//objPage.setOrderBy("fillinDate");
		//objPage.setOrder(Page.DESC);
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		if(ExStringUtils.isNotEmpty(channelName)){
			condition.put("channelName", channelName);
		}
		if(ExStringUtils.isNotEmpty(channelPosition)){
			condition.put("channelPosition", channelPosition);
		}
		model.addAttribute("condition",condition);
		//Page page = channelService.findChannelByCondition(condition, objPage);
		List<Channel> channelList = channelService.findChannelByParentAndchild(condition);
		model.addAttribute("channelList", channelList);
		return "/edu3/portal/system/channel/channel-list";
	}
	
	/**
	 * 新增门户栏目表单
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/portal/manage/channel/input.html")
	public String addChannel(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){
			Channel channel = channelService.get(resourceid);
			model.addAttribute("channel", channel);
		}else{
			model.addAttribute("channel", new Channel());
		}
		//获取所有栏目列表		
		List<Channel> allChanList = channelService.findChannelByParentAndchild();
		model.addAttribute("allChanList", allChanList);
		model.addAttribute("storeDir", model.addAttribute("storeDir", ExDateUtils.DATE_PATH_FORMAT.format(new Date())));//设置附件上传自动创建目录
		return "/edu3/portal/system/channel/channel-form";
	}
	
	
	/**
	 * 保存门户栏目
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/portal/manage/channel/save.html")
	public void saveChannel(Channel channel,String channelId,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		//处理父子关系
		if(!ExStringUtils.isEmpty(channelId)){
			Channel parentChannel = channelService.get(channelId);
			channel.setParent(parentChannel);
			channel.setIsChild(Constants.BOOLEAN_YES);//设置自己为叶子栏目
			channel.getParent().setIsChild(Constants.BOOLEAN_NO);//设置父不是叶子
			channel.setChannelLevel(channel.getParent().getChannelLevel()+1);//栏目级别设置父栏目+1
		}
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			if(!ExStringUtils.isEmpty(channel.getResourceid())){
				Channel persistchannel = channelService.load(channel.getResourceid());
				ExBeanUtils.copyProperties(persistchannel,channel);
				channelService.update(persistchannel);
			}else{
				//设置填写人信息
				User user = SpringSecurityHelper.getCurrentUser();
				channel.setFillinManId(user.getResourceid());
				channel.setFillinMan(user.getCnName());
				channelService.save(channel);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_PORTAL_CHANNEL");
			map.put("reloadUrl", request.getContextPath() +"/edu3/portal/manage/channel/input.html?resourceid="+channel.getResourceid());
		}catch (Exception e) {
			logger.error("保存栏目出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
		
		//return Constants.REQUEST_REDIRECT+"/edu3/portal/manage/channel/input.html?resourceid="+channel.getResourceid();
	}
	
	/**
	 * 删除
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/portal/manage/channel/remove.html")
	public void removeChannel(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();	
		try {
			if(!ExStringUtils.isEmpty(resourceid)){
				StringBuilder msg = new StringBuilder();
				String[] Ids = resourceid.split("\\,");
				List<Article> articleList;
				Map<String,Object> condition = new HashMap<String,Object>();
				for(String channelId:Ids){
					condition.clear();
					condition.put("channelId", channelId);
					articleList = articleService.findArticleByCondition(condition);
					if(articleList.size()>0){
						msg.append("栏目<font color='green'>"+channelService.get(channelId).getChannelName()+"</font>下有文章信息，不能删除！");
						continue;
					}
					channelService.delete(channelId);
				}
				if(ExStringUtils.isNotEmpty(msg)){
					throw new Exception(msg.toString());
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/portal/manage/channel/list.html");
			}
		} catch (Exception e) {
			logger.error("删除栏目出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 文章选择器
	 * @param objPage
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/chanel/selector/article.html")
	public String articleSelector(Page objPage,HttpServletRequest request,ModelMap model) throws WebException{
		objPage.setOrderBy("fillinDate");
		objPage.setOrder(Page.DESC);
					
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		String title = ExStringUtils.trimToEmpty(request.getParameter("title"));
		if(ExStringUtils.isNotEmpty(title)){
			condition.put("title", title);
		}
	
		String channelId = ExStringUtils.trimToEmpty(request.getParameter("channelId"));
		if(ExStringUtils.isNotEmpty(channelId) && !"0".equals(channelId)){
			condition.put("channelId", channelId);
		}
		
		//if(ExStringUtils.isNotEmpty(auditStatus)){
			condition.put("auditStatus", Article.AUDIT_STATUS_PASS);
		//}
		
		model.addAttribute("condition", condition);
		
		List<Channel> allChanList = channelService.findChannelByParentAndchild();//查找所有栏目列表
		model.addAttribute("allChanList", allChanList);
		Page page = articleService.findArticleByCondition(condition, objPage);
		model.addAttribute("articleList", page);
		String idsN = ExStringUtils.trimToEmpty(request.getParameter("idsN"));
		if(ExStringUtils.isEmpty(idsN)){
			idsN = "channelHref";
		}
		condition.put("idsN", idsN);
		model.addAttribute("idsN", idsN);
		return "/edu3/portal/system/channel/selector-article";
	}
	/**
	 * 在线帮助栏目查询
	 * @param objPage
	 * @param helperChannelName
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/portal/manage/helper/channel/list.html")
	public String helperChannelList(Page objPage, String helperChannelName, ModelMap model) throws WebException{
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		if(ExStringUtils.isNotEmpty(helperChannelName)){
			condition.put("helpChannelName", helperChannelName);
		}
		condition.put("helpChannelNameN", "系统根栏目");
		List<HelpChannel> list = helperChannelService.findByHql(" from " +HelpChannel.class.getName()+" where channelName=?", "系统根栏目");
		if(list.size()>0){
			condition.put("parentCode", list.get(0).getResourceid());
		}
		model.addAttribute("condition",condition);
		List<HelpChannel> helperChannelList = helperChannelService.findHelpChannelByParentAndchild(condition);
		//处理在线帮助列表的显示顺序
		if(null==helperChannelName&&ExCollectionUtils.isNotEmpty(helperChannelList)){
			helperChannelList = sortTree(helperChannelList);
		}
		model.addAttribute("helperChannelList", helperChannelList);
		//得到关联用户类型
		List<String> roleNameList = new ArrayList<String>(0);
		for (HelpChannel helpChannel : helperChannelList) {
			String roleName = roleCodeToName(helpChannel);
			roleNameList.add(roleName);
		}
		Object[] rolesNames =roleNameList.toArray();
		model.addAttribute("rolesNames",rolesNames);
		return "/edu3/portal/system/help/channel-list";
	}
	/**
	 * 将查找到的在线帮助栏目表组合排列成同一层级按照showOrder排序的树状遍历顺序
	 * @param originalTree
	 * @return
	 */
	private List<HelpChannel> sortTree(List<HelpChannel> originalTree){
		List<HelpChannel> desTree = new ArrayList<HelpChannel>(0);
		//获得最大的栏目层级
		int maxChannelLevel = 0;
		for (HelpChannel helpChannel : originalTree) {
			if(maxChannelLevel<helpChannel.getChannelLevel()){
				maxChannelLevel = helpChannel.getChannelLevel();
			}
		}
		//从系统根栏目开始，从1级到n级，每一级按照showOrder排列，得到一个符合顺序的临时表
		List<HelpChannel> tmpList = new ArrayList<HelpChannel>(0);
		for (int i = 0; i <=maxChannelLevel; i++) {
			List<HelpChannel> tmpListinLoop = new ArrayList<HelpChannel>(0);
			tmpListinLoop=helperChannelService.findByHql(" from "+HelpChannel.class.getName()+" where channelLevel=? and isDeleted = 0 order by showOrder", i);
			for (HelpChannel helpChannel : tmpListinLoop) {
				tmpList.add(helpChannel);
			}
		}
		//得到树状遍历排序的list
		desTree = getChild(tmpList, desTree, tmpList.get(0));
		return desTree; 
	}
	/**
	 * 递归方法：查找node的子节点
	 * @param list
	 * @param result
	 * @param node
	 * @return
	 */
	private List<HelpChannel> getChild(List<HelpChannel> list,List<HelpChannel> result,HelpChannel node){
		for (HelpChannel helpChannel : list) {
			if(null!=helpChannel.getParent()){//当遍历节点不为根节点时
				if(node.getResourceid().equals(helpChannel.getParent().getResourceid())){
					result.add(helpChannel);
					getChild(list, result, helpChannel);
				}
			}
		}
		return result;
	}
	
	/**
	 * 单条栏目记录编辑（供新增和修改之用）
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/portal/manage/helper/channel/input.html")
	public String addHelperChannel(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		//如果执行的方法是新增，每次进入到这个方法都需要重新给定一个resourceid的值
		String method = request.getParameter("method");
		model.addAttribute("method", method);
		String isAdd = null;
		//目前情况下，当method为空时，为修改栏目时的编辑状态；
		//当method为add时，为新增栏目时的编辑状态
		//区别在于，修改栏目时，是需要添加一个已知resourceid的栏目对象的，而在新增栏目时添加一个新建的栏目对象
		if(method==null){
			isAdd = "N";
		}else{
			if("add".equals(method)){
				isAdd="Y";
			}
		}
		//首先检查有没有根栏目
		if(helperChannelService.findByHql(" from "+HelpChannel.class.getName()+" where channelName =?","系统根栏目" ).size()==0){
			HelpChannel helpChannel = new HelpChannel();
			helpChannel.setIsChild("N");
			helpChannel.setChannelName("系统根栏目");
			helpChannel.setChannelLevel(0);
			helperChannelService.save(helpChannel);
			
		}
		//当为修改且有传入的resourceid值时
		if("N".equals(isAdd) &&ExStringUtils.isNotBlank(resourceid)){
			HelpChannel helpChannel = helperChannelService.get(resourceid);
			model.addAttribute("helpChannel", helpChannel);
			String parentId = helpChannel.getParent().getResourceid();
			model.addAttribute("parentId",parentId);
			String relateRolesName = roleCodeToName(helpChannel);
			model.addAttribute("relateRolesName",relateRolesName);
		}else{
			//当为新增时
			if("Y".equals(isAdd)){
				model.addAttribute("helpChannel", new HelpChannel());
			}
		}
		//获取所有栏目列表(已排序)
		List<HelpChannel> allChanList = getAllChanList();
		model.addAttribute("allChanList", allChanList);
		return "/edu3/portal/system/help/channel-form";
	}
	/**
	 * 获得栏目编辑界面的排序栏目列表
	 * @param helpChannel
	 * @return
	 */
	private List<HelpChannel> getAllChanList(){
		List<HelpChannel> allChanList = helperChannelService.findHelpChannelByParentAndchild();
		//排序的方法丢失根栏目
		List<HelpChannel> allChanListTmp = sortTree(allChanList);
		allChanList = new ArrayList<HelpChannel>(0);
		//得到系统根栏目
		List<HelpChannel> roots = helperChannelService.findByHql(" from "+HelpChannel.class.getName()+" where channelName='系统根栏目'");
		if(roots.size()>0){
			HelpChannel root = roots.get(0);
			allChanList.add(root);
		}
		for (HelpChannel tmp : allChanListTmp) {
			allChanList.add(tmp);
		}
		return allChanList;
	}
	/**
	 * 由用户类型代码到用户名称的转换
	 * @param helpChannel
	 * @return
	 */
	private String roleCodeToName(HelpChannel helpChannel){
		if(null==helpChannel.getRelateRoles()){
			return "";
		}
		String[] rolesCodes = helpChannel.getRelateRoles().split(",");
		StringBuffer relateRolesName = new StringBuffer();
		for (String string : rolesCodes) {
			List<Role> roles = roleService.findByHql(" from "+Role.class.getName()+" where roleCode =?", string);
			if(roles.size()>0){
				relateRolesName.append(roles.get(0).getRoleName()+",");
			}
		}
		if(relateRolesName.toString().contains(",")){
			relateRolesName = relateRolesName.deleteCharAt(relateRolesName.lastIndexOf(","));
		}
		return relateRolesName.toString();
	}
	
	/**
	 * 获得某个节点的所有子节点
	 * @param listChild
	 * @param list
	 * @param channel
	 */
	private void getChildRenNodes(List<String> listChild ,List<HelpChannel> list ,HelpChannel channel){
		
		for (HelpChannel helpChannel : list) {
			if (null!=helpChannel.getParent()) {
				if (helpChannel.getParent().getResourceid().equals(channel.getResourceid())) {
					getChildRenNodes(listChild,list, helpChannel);
				}
			}
			
		}
		listChild.add(channel.getResourceid());
	}
	/**
	 * 在线帮助栏目保存
	 * @param helpChannel
	 * @param channelId
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/portal/manage/helper/channel/save.html")
	public void saveHelpChannel(HelpChannel helpChannel,String parentId,String channelChannelId,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String method = request.getParameter("method");
		Integer errorCode = 0;
		model.addAttribute("method", method);
		String isAdd = null;
		if(method==null){
			isAdd = "N";
		}else{
			if("add".equals(method)){
				isAdd="Y";
			}
		}
		if(method==null){//编辑
			//处理父子关系(只针对编辑父栏目时)
			if (!parentId.equals(channelChannelId)) {
				//父子关系的变更必须经过校验
				//父节点为本节点时，禁止
				if(channelChannelId.equals(helpChannel.getResourceid())){
					errorCode = 1;
				}else{
					//父节点为本节点的子节点时，禁止
					List<String> childRes = new ArrayList<String>(0);
					List<HelpChannel> list = helperChannelService.getAll();
					getChildRenNodes(childRes, list, helpChannel);
					if(childRes.contains(channelChannelId)){
						errorCode = 2;
					}else{
						//本节点存在子节点时，禁止或将子节点以下的所有层级的子节点一起修改  更新channelLevel
						if("N".equals(helpChannel.getIsChild())){
							errorCode = 3;
						}
					}
				}
			}
		}
		HelpChannel parentChannel = helperChannelService.get(channelChannelId);
		helpChannel.setParent(parentChannel);
		helpChannel.setIsChild(Constants.BOOLEAN_YES);//设置自己为叶子栏目
		helpChannel.getParent().setIsChild(Constants.BOOLEAN_NO);//设置父不是叶子
		helpChannel.setChannelLevel(helpChannel.getParent().getChannelLevel()+1);//栏目级别设置父栏目+1
		
		Map<String,Object> map = new HashMap<String, Object>();
		String failMessage = "保存失败！";
		try{
			switch (errorCode) {
			case 1:
				failMessage = "【"+helpChannel.getChannelName()+"】不能设置成【"+helpChannel.getChannelName()+"】的父节点。"; 
				throw new RuntimeException();
			
			case 2:
				failMessage = "【"+helpChannel.getChannelName()+"】不可将自己的子节点设置成自己的父节点。"; 
				throw new RuntimeException();
			
			case 3:
				failMessage = "【"+helpChannel.getChannelName()+"】存在子节点，暂时不支持编辑其父节点。"; 
				throw new RuntimeException();
			default: break;
			}
			if(!ExStringUtils.isEmpty(helpChannel.getResourceid())){
				HelpChannel persistchannel = helperChannelService.load(helpChannel.getResourceid());
				ExBeanUtils.copyProperties(persistchannel,helpChannel);
				helperChannelService.update(persistchannel);
			}else{
				//设置填写人信息
				User user = SpringSecurityHelper.getCurrentUser();
				helpChannel.setFillinManId(user.getResourceid());
				helpChannel.setFillinMan(user.getCnName());
				//在保存之前当前保存的目录下判断待保存的栏目名不与之前的栏目名重复
				List<HelpChannel> check_list = helperChannelService.findByHql(" from "+HelpChannel.class.getName()+" where channelName=? and channelLevel=? and parent.resourceid=? and isDeleted =0 ", new Object[]{helpChannel.getChannelName(),helpChannel.getChannelLevel(),helpChannel.getParent().getResourceid()});
				if("Y".equals(isAdd) &&check_list.size()>0){
					failMessage = "父栏目【"+helpChannel.getParent().getChannelName()+"】下已经存在相同名字的栏目名【"+helpChannel.getChannelName()+"】，请确定同一个父栏目下的同一级不存在相同的栏目名。"; 
					throw new RuntimeException();
				}
				
				helperChannelService.save(helpChannel);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_HELP_CHANNEL");
			if("Y".equals(isAdd)){
				map.put("reloadUrl", request.getContextPath() +"/edu3/portal/manage/helper/channel/input.html?resourceid="+helpChannel.getResourceid()+"&method=add");
			}else{
				map.put("reloadUrl", request.getContextPath() +"/edu3/portal/manage/helper/channel/input.html?resourceid="+helpChannel.getResourceid());
			}
			
		}catch (Exception e) {
			logger.error("保存栏目出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message",failMessage );
		}
		renderJson(response, JsonUtils.mapToJson(map));
		
	}
	
	
	/**
	 * 在线帮助栏目删除
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/portal/manage/helper/channel/remove.html")
	public void removeHelpChannel(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();	
		try {
			if(!ExStringUtils.isEmpty(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					helperChannelService.batchDelete(resourceid.split("\\,"));
				}else{//单个删除
					helperChannelService.delete(resourceid);
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/portal/manage/help/channel/list.html");
			}
		} catch (Exception e) {
			logger.error("删除栏目出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	@RequestMapping("/edu3/portal/manage/helper/channel/validate.html")
	public void validate(String resid,HttpServletRequest request,HttpServletResponse response)throws WebException{
		List<HelpChannel> channelList = helperChannelService.findByHql(" from "+HelpChannel.class.getName()+" where resourceid='"+resid+"'");
		String roleName = roleCodeToName(channelList.get(0));
		String roleCode = channelList.get(0).getRelateRoles();
		Map<String,String> map = new HashMap<String, String>(0);
		map.put("roleName", roleName);
		map.put("roleCode", roleCode);
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
