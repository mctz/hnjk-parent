package com.hnjk.edu.portal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.Cleanup;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.ZipUtils;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.portal.model.Article;
import com.hnjk.edu.portal.service.IArticleService;
import com.hnjk.edu.recruit.model.EnrolleeInfo;
import com.hnjk.edu.recruit.model.RecruitPlan;
import com.hnjk.edu.recruit.service.IEnrolleeInfoService;
import com.hnjk.edu.recruit.service.IRecruitPlanService;
import com.hnjk.edu.recruit.util.zipUtil;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.security.service.IOrgUnitService;

/**
 * 站点内容.
 * <code>SiteController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-27 下午05:30:20
 * @see 
 * @version 1.0
 */
@Controller
public class SiteController extends FileUploadAndDownloadSupportController {
	private static final long serialVersionUID = 1L;
		
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;	
	
	@Autowired
	@Qualifier("recruitPlanService")
	private IRecruitPlanService recruitPlanService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("articleService")
	private IArticleService articleService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("enrolleeInfoService")
	private IEnrolleeInfoService enrolleeInfoService;
		
	/**
	 * 下载附件
	 * @param request
	 * @param response
	 * @throws WebException
	 * @throws IOException
	 */
	@RequestMapping(value="/portal/site/article/download.html",method = RequestMethod.GET)
	public void downloadTemplate(HttpServletRequest request,HttpServletResponse response) throws WebException, IOException{
		String resourceid = request.getParameter("id");
		String articleId = request.getParameter("articleId");
		try{
			if(!ExStringUtils.isEmpty(resourceid)){//单个文件下载
				Attachs attachs = attachsService.get(resourceid);
				String filePath = attachs.getSerPath()+File.separator+attachs.getSerName();
				downloadFile(response, attachs.getAttName(), filePath,false);
			}else if (ExStringUtils.isNotEmpty(articleId)) {//打包下载
				Article article = articleService.get(articleId);
				String zipFile = article.getTitle()+".zip";
				String rootPath = Constants.EDU3_DATAS_LOCALROOTPATH + File.separator + "article_ZipFiles";
				List<Attachs> attachList = attachsService.findByHql("from "+Attachs.class.getName()+ " where isDeleted=0 and formId = ? order by uploadTime desc", articleId);
				if(attachList!=null && attachList.size()==1){
					Attachs attachs = attachList.get(0);
					String filePath = attachs.getSerPath()+File.separator+attachs.getSerName();
					downloadFile(response, attachs.getAttName(), filePath,false);
				}else if (attachList!=null && attachList.size()>1) {
					List<File> fileList = new ArrayList<File>();
					File file = new File(rootPath);  
					if(!file.exists()){  
					    file.mkdirs();  
					} 
					for (Attachs attachs : attachList) {
						String filePath = attachs.getSerPath()+File.separator+attachs.getSerName();
						File old_file = new File(filePath);
						File new_fileFile = new File(rootPath+File.separator+attachs.getAttName());
						
						copyFile(old_file, new_fileFile);
						fileList.add(new_fileFile);
					}
					zipUtil thread = new zipUtil(zipFile, rootPath, fileList);
					Thread t1=new Thread(thread);
					t1.start();
					Thread.sleep(1000);
					downloadFile(response, article.getTitle()+".zip", rootPath + File.separator + zipFile,false);
				}
			}		
		}catch (Exception e) {
			logger.error("下载文章附件出错：{}",e.fillInStackTrace());
		}
	}
	
	@RequestMapping(value="/portal/site/article/download1.html",method = RequestMethod.GET)
	public void downloadTemplate1(@RequestParam("id") String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException, IOException{
		try{
			/*String filePath="" ;
			String path=CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue();
			path += File.separator + "enrollMaterial" + File.separator;*/
			String filePath = "";
			String rootPath = File.separator + "TheAdmissionNotice" + File.separator;
			if(!ExStringUtils.isEmpty(resourceid)){
				Dictionary dictionary = dictionaryService.get(resourceid);
				String name = dictionary.getDictName();
				String value = dictionary.getDictValue();
				String noticeNo = request.getParameter("noticeNo");
				if(name.endsWith("录取通知书")){
					String subdir = request.getParameter("subdir");
					value = (StringUtils.isEmpty(subdir) ? "":subdir)+File.separator+noticeNo+".jpg";
					filePath = request.getSession().getServletContext().getRealPath(rootPath+value);
					EnrolleeInfo enrolleeInfo = enrolleeInfoService.findUniqueByProperty("matriculateNoticeNo", noticeNo);
					if(enrolleeInfo!=null){
						enrolleeInfo.setIsPrint("Y");
						enrolleeInfoService.update(enrolleeInfo);
					}
				}else {
					filePath = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+rootPath+value;
				}
				
				File file = new File(filePath);
				if(!file.exists()){
					throw new Exception("该文件不存在！");
				}
				downloadFile(response, value, filePath,false);
				/*if(resourceid.equals("1")){
					name="安徽医科大学成人教育学院2017级专升本新生入学须知.doc";
					filePath= path+name;
				}else if(resourceid.equals("2")){
					name="新生登记表.doc";
					filePath= path+name;
				}else if(resourceid.equals("3")){
					name="专升本学生有效毕业文凭保证书.doc";
					filePath= path+name;
				}else if(resourceid.equals("5")){
					name="安徽医科大学成人教育学院2017级专科新生报到须知.doc";
					filePath= path+name;
				}else{
					name=resourceid+".jpg";
					String subdir = request.getParameter("subdir");
					filePath = request.getSession().getServletContext()
							.getRealPath(File.separator + "TheAdmissionNotice"
									+ (StringUtils.isEmpty(subdir) ? "" : File.separator + subdir) + File.separator
									+ resourceid + ".jpg");
				}*/
			
//				String i=CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue();
//				String filePath = i+name+".doc";
				
			}			
		}catch (Exception e) {
			logger.error("下载文章附件出错：{}",e.fillInStackTrace());
		}
	}
	

	
	/**
	 * 学生服务连接
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping(value="/portal/service/student/service.html",method = RequestMethod.GET)
	public String webUserService(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws Exception{
		
		String forwardurl = ExStringUtils.trimToEmpty(request.getParameter("url"));
		String brschoolId = ExStringUtils.trimToEmpty(request.getParameter("uid"));
		
		//List<Channel> cachChannelList = getPortalChannelList(Constants.BOOLEAN_NO);
		//List<Channel> navHeaderList = new ArrayList<Channel>();//获取头部导航栏目		
		//if(!cachChannelList.isEmpty()){
	//		for(Channel channel:cachChannelList){
			//	if(channel.getChannelPosition().equals("NAV")){
	//				navHeaderList.add(channel);
			//	}			
		//	}
	//	}
		
		List<RecruitPlan> recruitPlanList=new ArrayList<RecruitPlan>();
		try {
			recruitPlanList = recruitPlanService.getPublishedPlanList("");
		} catch (Exception e) {
			logger.error("学生服务连接-获取开放的招生批次出错：{}",e.fillInStackTrace());
		}
		
		if (ExCollectionUtils.isEmpty(recruitPlanList) ) {
			model.addAttribute("hasPublishedPlan",false);
		} else {
			model.addAttribute("hasPublishedPlan",true);
		}
	//	model.addAttribute("channelList", navHeaderList);
		
		boolean serviceFlag = false;
		HttpSession session = request.getSession(false);		
		if(null != session && null != session.getAttribute("serviceFlag")){	
			 serviceFlag = (Boolean)session.getAttribute("serviceFlag");			
		}
		model.addAttribute("serviceFlag", serviceFlag);
		
		if(!ExStringUtils.isEmpty(forwardurl)){
			model.addAttribute("forwardurl", forwardurl);		
		}
		if (ExStringUtils.isNotEmpty(brschoolId)) {
			model.addAttribute("uid", brschoolId);		
		}
		return "/edu3/portal/site/service";
	}
	
	/**
	 * 常用下载页面
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 * @author Git
	 */
	@RequestMapping("/downloads.html")
	public String loginPage(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{	
		
		//读取文件列表
		File file = new File(Constants.EDU3_DATAS_LOCALROOTPATH+"/downloads");
		List<File> fileList = null;
		if(file.isDirectory()){
			fileList = new ArrayList<File>();
			for(File tmp : file.listFiles()){
				if(tmp.isFile()){
					fileList.add(tmp);
				}
			}
		}
		
		model.addAttribute("fileList", fileList);
		model.addAttribute("rootUrl", CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());
		return "/edu3/portal/common_download";
	}
	
	public void copyFile(File fromFile,File toFile) throws IOException{
        @Cleanup FileInputStream ins = new FileInputStream(fromFile);
        @Cleanup FileOutputStream out = new FileOutputStream(toFile);
        byte[] b = new byte[1024];
        int n=0;
        while((n=ins.read(b))!=-1){
            out.write(b, 0, n);
        }
    }
}