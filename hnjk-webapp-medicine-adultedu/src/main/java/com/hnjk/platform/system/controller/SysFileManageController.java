package com.hnjk.platform.system.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hnjk.core.foundation.utils.*;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.platform.system.model.SysConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.base.model.AttachInfo;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.recruit.model.EnrolleeInfo;
import com.hnjk.edu.recruit.service.IEnrolleeInfoService;
import com.hnjk.edu.teaching.util.MakeWatermarkUtil;
import com.hnjk.extend.plugin.excel.config.ExcelConfigInfo;
import com.hnjk.extend.plugin.excel.config.ExcelConfigPropertyParam;
import com.hnjk.extend.plugin.excel.config.IExcelConfigManager;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 通用文件管理控制类.
 * <code>SysFileManageController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-8-6 下午03:24:22
 * @see 
 * @version 1.0
 */
@Controller
public class SysFileManageController extends FileUploadAndDownloadSupportController{

	private static final long serialVersionUID = -4509095732643415302L;

	/*
	 * 系统文件存储规则：
	 * exportfiles - 从服务器导出的文件存放处
	   importfiles - 从客户端导入的文件存放处
	   portal - 门户的附件及图片
			-attachs 门户文章附件
			-images 门户文章图片
			-links 门户连接图片
		users - 用户的附件及图片
		common
			- students
			 	- 系统自动创建目录（按日期）
					-学籍照片（身份证_photo）
					-毕业证(身份证_edu)
					-身份证（身份证_cert）
	 */
	
	
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;//注入附件服务
	
	@Autowired
	@Qualifier("excelConfigManager")
	private IExcelConfigManager excelConfigManager;//注入EXCEL配置服务
	
	@Autowired
	@Qualifier("enrolleeInfoService")
	private IEnrolleeInfoService enrolleeInfoService;
	
	/**
	 * 自定义导出
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/filemanage/showcustomerexport.html")
	public String showCustomerExcelExport(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException{
		//获取EXCEL配置
		try {
			//学习中心
			String branchSchool = ExStringUtils.defaultIfEmpty(request.getParameter("branchSchool"), "");
			//招生批次
			String recruitPlan = ExStringUtils.defaultIfEmpty(request.getParameter("recruitPlan"),"");
			//招生专业
			String major = ExStringUtils.defaultIfEmpty(request.getParameter("major"),"");
			//Excel配置model
			String excelModel = ExStringUtils.defaultIfEmpty(request.getParameter("excelModel"), "");
			//自定义导出的请求处理路径
			String exportRequestPath = ExStringUtils.defaultIfEmpty(request.getParameter("exportRequestPath"), "");
			
			Map<String,Object> condition = new HashMap<String,Object>();//用于中转导出数据中用到的参数
			List<ExcelConfigPropertyParam> paramList = new ArrayList<ExcelConfigPropertyParam>();
			
			if(ExStringUtils.isNotEmpty(branchSchool)) {
				condition.put("branchSchool", branchSchool);
			}
			if(ExStringUtils.isNotEmpty(recruitPlan)) {
				condition.put("recruitPlan", recruitPlan);
			}
			if(ExStringUtils.isNotEmpty(major)) {
				condition.put("major", major);
			}
			if(ExStringUtils.isNotEmpty(exportRequestPath)) {
				condition.put("exportRequestPath", exportRequestPath);
			}
			if(ExStringUtils.isNotEmpty(excelModel)) {
				condition.put("excelModel",excelModel);
			}
			
			ExcelConfigInfo excelConfigInfo = excelConfigManager.getModel(excelModel, "");
			Map<String,ExcelConfigPropertyParam> columnMap = excelConfigInfo.getColumnMap();//列属性

			for (String key : columnMap.keySet()) {
				paramList.add(columnMap.get(key));
			}

			map.addAttribute("condition",condition);
			map.addAttribute("paramList", paramList);
			
		} catch (Exception e) {
			logger.error("获取EXCEL配置出错：{}",e.fillInStackTrace());
		}	
		
		return "/common/excel/customer-export";
	}
	
	
	/**
	 * 附件上传
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/filemanage/upload.html")
	public void uploadFileProcess(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String, Object> returnMap = uploadFile(request, response);
		Exception ep = (Exception)returnMap.get("ep");
		if(ep != null){
			throw new WebException("上传文件出错："+ep.getMessage());
		}else {
			//返回改文件信息
			renderText(response,returnMap.get("attId")+"|"+returnMap.get("attSerName"));		
		}
	}
	
	/**
	 * 附件上传new(返回JSON格式数据)
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/filemanage/uploader.html")
	public void uploadFileProcessJson(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String, Object> returnMap = uploadFile(request, response);
		//返回改文件信息
		renderJson(response, JsonUtils.mapToJson(returnMap));
	}

	/**
	 * 上传文件
	 * @param request
	 * @param response
	 * @return
	 */
	private Map<String, Object> uploadFile(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> returnMap = new HashMap<String, Object>(5);
		String success = "OK";
		String error = null;
		String attId = null;
		String attSerName = null;
		Exception ep = null;
		//获取文件存储位置,格式目录+','分割，如 abc,def 表示存储在服务器的abc\def目录下
		String storePath   		 = ExStringUtils.defaultIfEmpty(request.getParameter("storePath"), "");				
		String replaceName 		 = ExStringUtils.defaultIfEmpty(request.getParameter("replaceName"), "");//获取替换的别名		
		String formType    		 = ExStringUtils.trimToEmpty(request.getParameter("formType"));//获取表单业务类型		
		String formId      		 = ExStringUtils.trimToEmpty(request.getParameter("formId"));//业务表单的ID，用于上传附件同时需要保存业务表单ID的情况		
		String isStoreToDatabase = ExStringUtils.defaultIfEmpty(request.getParameter("isStoreToDatabase"),"Y");//是否需要存数据库，默认存数据库		
		String fillinName = ExStringUtils.trimToEmpty(request.getParameter("fillinName"));//上传人姓名		
		String fillinNameId = ExStringUtils.trimToEmpty(request.getParameter("fillinNameId"));//上传人ID
		String acceptType = ExStringUtils.trimToEmpty(request.getParameter("acceptType"));// 允许上传类型,有多个用"|"分隔,如：png|jpg
		String attachId = ExStringUtils.trimToEmpty(request.getParameter("attachId"));// 附件ID
		
		// 操作人信息
		if(ExStringUtils.isEmpty(fillinNameId)||ExStringUtils.isEmpty(fillinName)){
			User currentUser = SpringSecurityHelper.getCurrentUser();
			if(currentUser != null){
				fillinNameId = currentUser.getResourceid();
				fillinName = currentUser.getCnName();
			}
		}
		
		//设置默认存储路径
		if(ExStringUtils.isNotEmpty(storePath)){
			String distPath = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue() ;
			String[] arr = storePath.split(",");
			for(int i=0;i<arr.length;i++){
				if(ExStringUtils.isNotBlank(arr[i])){
					distPath += arr[i]+File.separator;
				}
			}
			this.setDistfilepath(distPath);
		}
		//设置允许上传类型,默认是全局参数设置的类型
		//web.uploadfile.accept
		String _acceptType = CacheAppManager.getSysConfigurationByCode("web.uploadfile.accept").getParamValue();
		if(ExStringUtils.isNotEmpty(acceptType)){
			_acceptType = acceptType;
		}
		this.setAcepttype(_acceptType);
		//设置存储别名
		if(ExStringUtils.isNotEmpty(replaceName)){
			Map<String, String> replaceMap = new HashMap<String, String>(0);
			replaceMap.put("Filedata", replaceName);//表单域
			this.setReplaceNameMap(replaceMap);
		}else{
			this.setReplaceNameMap(null);
		}
		
		Attachs attachs = null;
		
		//数据库里添加记录 //存储文件
		try {
			List<AttachInfo> list = doUploadFile(request, response,null);//上传
			if(null != list && list.size()>0){
				AttachInfo attachInfo = list.get(0);				
				attachs = new Attachs();
				ExBeanUtils.copyProperties(attachs, attachInfo);
				
				if("Y".equals(isStoreToDatabase)){
					if(ExStringUtils.isNotEmpty(attachId)){
						Attachs _attachs =attachsService.get(attachId);
						if(_attachs!=null && _attachs.getIsDeleted()==0){
							ExBeanUtils.copyProperties(_attachs, attachInfo);
							_attachs.setResourceid(attachId);
							//设置当前操作人					
							_attachs.setFillinName(fillinName);
							_attachs.setFillinNameId(fillinNameId);
							//设置业务表单类型
							_attachs.setFormType(formType);
							if(ExStringUtils.isNotEmpty(formId)){
								_attachs.setFormId(formId);
							}
							// 更新数据
							attachsService.saveOrUpdate(_attachs);
						}
						attachs = _attachs;
					} else {
						//设置当前操作人					
						attachs.setFillinName(fillinName);
						attachs.setFillinNameId(fillinNameId);
						//设置业务表单类型
						attachs.setFormType(formType);
						if(ExStringUtils.isNotEmpty(formId)){
							attachs.setFormId(formId);
						}
						//写入数据库
						attachsService.saveOrUpdate(attachs);
					}
				}
				attId = attachs.getResourceid();
				attSerName = attachs.getSerName();
			}
			
		} catch (Exception e) {					
			logger.error("保存文件出错:{}",e.fillInStackTrace());
//			throw new WebException("上传文件出错："+e.getMessage());
			success = "fail";
			error = "上传文件失败";
			ep = e;
		} finally {
			returnMap.put("success", true);
			returnMap.put("result", success);
			returnMap.put("error", error);
			returnMap.put("attId", attId);
			returnMap.put("attSerName", attSerName);
			returnMap.put("ep", ep);
		}
		return returnMap;
	}
	
	
	/**
	 * 附件下载
	 * @param request
	 * @param response
	 * @throws WebException
	 * @throws IOException
	 */
	@RequestMapping(value={"/edu3/framework/filemanage/download.html","/edu3/framework/filemanage/public/download.html"},method = RequestMethod.GET)
	public void downloadTemplate(@RequestParam("id") String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException, IOException{
		try{
			if(ExStringUtils.isNotBlank(resourceid)){
				Attachs attachs = attachsService.get(resourceid);
				String filePath = attachs.getSerPath()+File.separator+attachs.getSerName();
				downloadFile(response, attachs.getAttName(), filePath,false);
			}
		}catch (Exception e) {
			logger.error("下载附件出错：{}",e.fillInStackTrace());
		}
	}
	
	/**
	 * 系统附件配置路径，简单下载
	 * @param subPath 子目录
	 * @param request
	 * @param response
	 * @throws IOException
	 * @author Git
	 */
	@RequestMapping("/edu3/framework/filemanage/public/simpleDownload.html")
	public void simpleDownload(@RequestParam("subPath") String subPath,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
 
		String name = request.getParameter("name");
		//logger.info(Charset.defaultCharset().toString());
		String charset = ExStringUtils.showCharset(name);
		String resourceid = request.getParameter("resourceid");
		logger.info("name="+name+"--------encode1="+ExStringUtils.getEncodeURIComponentByOne(name));
		if (ExStringUtils.isMessyCode(name)) {
			name = ExStringUtils.getEncodeURIComponentByOne(name);
		}
		logger.info("name="+name);
		try {
			EnrolleeInfo enrolleeInfo = enrolleeInfoService.get(resourceid);
			if(enrolleeInfo!=null){
				enrolleeInfo.setIsPrint("Y");
				enrolleeInfoService.update(enrolleeInfo);
			}
			downloadFile(response, name, Constants.EDU3_DATAS_LOCALROOTPATH + File.separator + subPath + File.separator + name, false);
			
		} catch (IOException e) {
			logger.error("简单下载文件出错：{}", e.fillInStackTrace());
		}
	}
	
	@RequestMapping(value="/edu3/framework/filemanage/preview.html")
	public void showImage(HttpServletRequest request,HttpServletResponse response) throws WebException{
		OutputStream outputStream = null;
		try {
			String filePath = ExStringUtils.trimToEmpty(request.getParameter("storePath"));//文件路径
			String fileName = ExStringUtils.trimToEmpty(request.getParameter("fileName"));//文件名
			
			if(ExStringUtils.isNotEmpty(filePath) && ExStringUtils.isNotEmpty(fileName) ){
				String distPath = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue() ;//文件所在目录
				String[] arr = filePath.split(",");
				for(int i=0;i<arr.length;i++){
					distPath += arr[i]+File.separator;
				}	
				if(fileName.indexOf("/")>=0){
					fileName.replace("/", File.separator);
				}
				InputStream inStream = new FileInputStream( distPath+fileName);// 文件的存放路径
				//输出到浏览器
				 response.reset();
				 outputStream = response.getOutputStream();               
	           	   
	             response.setContentType("image/jpeg"); 
	             byte[] b = new byte[100];
	     		 int len;
	     		 while ((len = inStream.read(b)) > 0) {
					 response.getOutputStream().write(b, 0, len);
				 }
	     		 inStream.close();
	             outputStream.flush();  
	             outputStream.close();  
	             outputStream = null;// no close twice  
				//downloadFile(response, "图片预览", distPath+fileName,false);
			}
			
		} catch (Exception e) {
			logger.error("下载附件出错：{}",e.fillInStackTrace());
		}finally {  
            if (outputStream != null) {  
                try {  
                    outputStream.close();  
                } catch (IOException ex) {  
                }  
            } 
		}
	}
	
	/**
	 * 附件删除
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/filemanage/delete.html")
	public void deleteFile(String fileid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		String fileId = ExStringUtils.trimToEmpty(fileid);
		
		try{
			Attachs attachs = attachsService.get(fileId);
			FileUtils.delFile(attachs.getSerPath()+File.separator+attachs.getSerName());
			if(ExStringUtils.isNotEmpty(attachs.getCacheWebPath())){
				FileUtils.delFile(attachs.getCacheWebPath());
			}
			logger.debug("删除附件：{}",attachs.getAttName());
			
			attachsService.truncate(attachs);
						
		}catch (Exception e) {
			renderText(response, "300");
			logger.error("删除附件出错：{}",e.fillInStackTrace());
			
		}
		renderText(response, "200");
	}
	

	
	/**
	 * 编辑器图片上传
	 * 文件上传路径参数必须传入，这个参数规定了文件访问的路径.
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/filemanage/editor/upload.html")
	public void uploadFile(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String result = "";					
		//返回给编辑器的图片路径 
		String imgUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
						+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue();//拿到附件映射的全局URL
		
		//设置默认存储路径
		//获取文件存储位置,格式目录+','分割，如 abc,def 表示存储在服务器的abc\def目录下
		String storePath = request.getParameter("storePath");
		if(ExStringUtils.isNotEmpty(storePath)){
			String distPath = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue();//跟目录
			String[] arr = storePath.split(",");
			for(int i=0;i<arr.length;i++){
				distPath += arr[i]+File.separator;
				imgUrl += arr[i] +"/";
			}
			this.setDistfilepath(distPath);
		}
		
		String textAreaId = ExStringUtils.defaultIfEmpty(request.getParameter("textAreaId"), "content");
		
		//设置上传类型
		this.setAcepttype("jpg|gif|png|bmp");
		try {	
			int width = 240;
			int height =180;
			List<AttachInfo> list = doUploadFile(request, response,null);
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = null;
			//由于目前是并没有多文件上传，因此直接取width & height
			for (Iterator it = multipartRequest.getFileNames(); it.hasNext();) {//遍历文件
				String key = (String) it.next();
					file = multipartRequest.getFile(key);
					BufferedImage img = ImageIO.read(file.getInputStream());
					width = img.getWidth();
					height = img.getHeight();
			}
			AttachInfo attachInfo = list.get(0);			
			 result ="<script type=\"text/javascript\">parent.KE.plugin['image'].insert('"+textAreaId+"','"+ imgUrl+attachInfo.getSerName() + "','" 
			 + attachInfo.getAttName() + "','"+ width+ "','" + height + "','" + 0+ "','" + "left" +"');" +
           		"alert('图片上传成功！');" +"</script>";

		} catch (Exception e) {
			result = "<script type=\"text/javascript\">图片上传失败，原因：\n"+e.getMessage()+"</script>";
			logger.error("上传文件失败：{}",e.fillInStackTrace());
		}
		logger.debug(result);
		renderHtml(response, result);
	}
	
	
	/**
	 * 编辑器浏览服务器上的图片
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/framework/filemanage/editor/browser.html")
	public void fileManager(HttpServletRequest request,HttpServletResponse response) throws WebException{		     
        // 根目录路径，可以指定绝对路径，比如 /var/www/attached/
		String spath = request.getParameter("spath");
		
		//根据path参数，设置各路径和URL（除根目录之外所有子目录名称）   
        String path = request.getParameter("path");
        String root_path = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue();        
        String root_url = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
						+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue();//拿到附件映射的全局URL
       
        if(!ExStringUtils.isEmpty(spath)){//设置浏览根目录
        	String[] arr = spath.split(",");
			for(int i=0;i<arr.length;i++){
				root_path += arr[i]+File.separator;
				root_url += arr[i]+"/";
			}
        }
          
        //当前路径
        String current_path,current_url,current_dir_path="",moveup_dir_path = "";   
               
        if (path == null || "".equals(path)) {
            current_path = root_path;
            current_url = root_url;
            current_dir_path = "";
            moveup_dir_path = "";
        } else {
        	  current_path = root_path + path;
              current_url = root_url + path;
              current_dir_path = path;
              //    去掉当前目录之后即为上级目录路径
              moveup_dir_path = current_dir_path.substring(0, getUpIndex(current_dir_path));
        }

        //    排序形式，name or size or type
        String order = request.getParameter("order");
        order = (order == null) ? "name" : order.toLowerCase();
        
        //    目录不存在或不是目录
        File file = new File(current_path);
        if (!file.exists()) {
        	renderHtml(response, "目录或文件不存在...");           
            return;
        }

        //遍历目录取得文件信息       
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        File dir = new File(current_path);
		File[] files = dir.listFiles();

        for (int i = 0; i < files.length; i++) {
            Map<String, Object> file_list = new HashMap<String, Object>();
            File f_file = files[i];
            String filename = f_file.getName();
           
            //    是否是目录
            boolean is_dir = false;

            if (f_file.getPath().equals(root_path) || f_file.isDirectory()) {
                is_dir = true;
            }

            long filesize = 0;
            //    是否是图片
            boolean is_photo = false,
            //    如果是目录，目录下是否有图片
            has_file = true;
            String filetype = "";
            String[] file_ext = filename.trim().split("\\.");
            if (file_ext.length > 1) {
                filesize = f_file.length();
                is_photo = getExtType(file_ext[1]);
                has_file = false;
                filetype = file_ext[1];
            }

            file_list.put("is_dir", is_dir);        //是否文件夹
            file_list.put("has_file", has_file);    //文件夹是否包含文件
            file_list.put("filesize", filesize);    //文件大小
            file_list.put("is_photo", is_photo);    //是否图片
            file_list.put("filetype", filetype);    //文件类别，用扩展名判断
            file_list.put("filename", filename);     //文件名，包含扩展名      
            file_list.put("datetime", f_file.lastModified()); //文件最后一次访问时间   
           
            list.add(file_list);
        }
       
        //    利用比较器进行排序
        Collections.sort(list,getComparator(order));
       
        //对排序后的List进行构建json字符串
        StringBuffer file_list_str = new StringBuffer();
        file_list_str.append("[");
               
        for (int i=0; i < list.size(); i++){
            Map<String, Object> map = (Map<String, Object>) list.get(i);
            System.out.println(list.size()+"//"+i+"=="+map);
            file_list_str.append("{");                   
            file_list_str.append("\"is_dir\":").append(Boolean.parseBoolean(map.get("is_dir").toString())).append(",");
            file_list_str.append("\"has_file\":").append(Boolean.parseBoolean(map.get("has_file").toString())).append(",");
            file_list_str.append("\"filesize\":\"").append(Long.parseLong(map.get("filesize").toString())).append("\",");
            file_list_str.append("\"is_photo\":").append(Boolean.parseBoolean(map.get("is_photo").toString())).append(",");
            file_list_str.append("\"filetype\":\"").append(map.get("filetype").toString()).append("\",");
             file_list_str.append("\"filename\":\"").append(map.get("filename").toString()).append("\",");            
            SimpleDateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");           
            file_list_str.append("\"datetime\":\"").append(dateFormat.format(new Date(Long.parseLong(map.get("datetime").toString())))).append("\"}");
            if (i < files.length - 1) {
                file_list_str.append(",");
            }
        }
        file_list_str.append("]");
       
        StringBuffer json_str = new StringBuffer();
        //相对于根目录的上一级目录
        json_str.append("{\"moveup_dir_path\":\"" + moveup_dir_path).append("\",");
        //相对于根目录的当前目录
        json_str.append("\"current_dir_path\":\"" + current_dir_path).append("\",");
        //当前目录的URL
        json_str.append("\"current_url\":\"" + current_url).append("\",");
        //文件数
        json_str.append("\"total_count\":\"" + list.size()).append("\",");
        //文件列表数组
        json_str.append("\"file_list\":").append(file_list_str).append("}");
            
        renderText(response, json_str.toString());
	}
	@RequestMapping("/edu3/filemanage/pressText.html")
	public void pressText(HttpServletRequest request,HttpServletResponse response){
		String _path = request.getParameter("path");
		String root_path = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue();
		String path=root_path+_path;
		List<Dictionary> list = CacheAppManager.getChildren("sys_file_certcard_presstext");
		Map<String,Object> map = new HashMap<String, Object>();
		for(Dictionary d:list){
			map.put(d.getDictName(), d.getDictValue());
		}
		int resize_h = Integer.valueOf(map.get("resize_h").toString());
		int resize_w =Integer.valueOf(map.get("resize_w").toString());
//		MakeWatermarkUtil.resize(path,resize_h,resize_w,false);
		String pressText=map.get("pressText").toString();
		String fontName=map.get("fontName").toString();
		int fontStyle=Integer.valueOf(map.get("fontStyle").toString());
		int _fontSize=Integer.valueOf(map.get("fontSize").toString());
		int fontSize =MakeWatermarkUtil.getFontSize(path, resize_w, resize_h)*_fontSize;
		String _color=map.get("color").toString();
		String[] rgb = _color.split(",");
		int r = Integer.parseInt(rgb[0]);
		int g = Integer.parseInt(rgb[1]);
		int b = Integer.parseInt(rgb[2]);
		Color color = new Color(r,g,b);
		int x=Integer.valueOf(map.get("x").toString());
		int y=Integer.valueOf(map.get("y").toString());
		float alpha =Float.parseFloat(map.get("alpha").toString());
		int angle=Integer.parseInt(map.get("angle").toString());
		MakeWatermarkUtil.pressText(path, pressText, fontName,fontStyle, fontSize, color, x, y,alpha,angle);
		//renderHtml(response, html, headers);
	}

	@RequestMapping("/system/ActiveXConfigDownload.html")
	public void downloadActiveXConfig(HttpServletRequest request,HttpServletResponse response) {
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");//文件输出服务器路径
		GUIDUtils.init();
		File disFile = new File(getDistfilepath()+ File.separator + "ActiveXConfig.bat");
		try {
			StringBuilder content = new StringBuilder(1000);
			if (disFile.exists()) {
				content.append(FileUtils.readFile(disFile.getAbsolutePath()));
			}else {
				disFile.createNewFile();
			}

			String baseServerUrl = request.getParameter("baseServerUrl");
			String SERVER_NAME = baseServerUrl;
			if(baseServerUrl.contains(":")){
				SERVER_NAME = baseServerUrl.split("//")[1].split(":")[0];
			}
			String SERVER_IP = ExStringUtils.getIP(SERVER_NAME);
			if (content.indexOf(SERVER_IP)<0 || content.indexOf(baseServerUrl)<0) {
				content.setLength(0);
				content.append("@echo off").append(System.getProperty("line.separator"));
				content.append("title  ★  IE ActiveX 插件启用脚本   ★").append(System.getProperty("line.separator"));
				content.append("echo   正在启用IE的 ActiveX控件，请稍候...").append(System.getProperty("line.separator"));
				content.append("ping 127.0.0.1 -n 2 >nul 2>nul").append(System.getProperty("line.separator"));
				content.append("set regpath=HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\Zones\\2").append(System.getProperty("line.separator"));
				content.append("cls").append(System.getProperty("line.separator"));
				content.append("echo   本脚本可快速启用IE的 ActiveX控件").append(System.getProperty("line.separator"));
				content.append("echo   正在进行 ZONE2(受信用的站点) 的设置...").append(System.getProperty("line.separator"));
				content.append("@reg add \"%regpath%\" /v \"1001\" /d \"0\" /t REG_DWORD /f").append(System.getProperty("line.separator"));
				content.append("@reg add \"%regpath%\" /v \"1004\" /d \"0\" /t REG_DWORD /f").append(System.getProperty("line.separator"));
				content.append("@reg add \"%regpath%\" /v \"1200\" /d \"0\" /t REG_DWORD /f").append(System.getProperty("line.separator"));
				content.append("@reg add \"%regpath%\" /v \"1201\" /d \"0\" /t REG_DWORD /f").append(System.getProperty("line.separator"));
				content.append("@reg add \"%regpath%\" /v \"1208\" /d \"0\" /t REG_DWORD /f").append(System.getProperty("line.separator"));
				content.append("@reg add \"%regpath%\" /v \"1209\" /d \"0\" /t REG_DWORD /f").append(System.getProperty("line.separator"));
				content.append("@reg add \"%regpath%\" /v \"1400\" /d \"0\" /t REG_DWORD /f").append(System.getProperty("line.separator"));
				content.append("@reg add \"%regpath%\" /v \"1402\" /d \"0\" /t REG_DWORD /f").append(System.getProperty("line.separator"));
				content.append("@reg add \"%regpath%\" /v \"1405\" /d \"0\" /t REG_DWORD /f").append(System.getProperty("line.separator"));
				content.append("@reg add \"%regpath%\" /v \"1406\" /d \"0\" /t REG_DWORD /f").append(System.getProperty("line.separator"));
				content.append("@reg add \"%regpath%\" /v \"1607\" /d \"0\" /t REG_DWORD /f").append(System.getProperty("line.separator"));
				content.append("@reg add \"%regpath%\" /v \"1609\" /d \"0\" /t REG_DWORD /f").append(System.getProperty("line.separator"));
				content.append("@reg add \"%regpath%\" /v \"1804\" /d \"0\" /t REG_DWORD /f").append(System.getProperty("line.separator"));
				content.append("@reg add \"%regpath%\" /v \"2200\" /d \"0\" /t REG_DWORD /f").append(System.getProperty("line.separator"));
				content.append("@reg add \"%regpath%\" /v \"2201\" /d \"0\" /t REG_DWORD /f").append(System.getProperty("line.separator"));
				content.append("@reg add \"%regpath%\" /v \"2300\" /d \"0\" /t REG_DWORD /f").append(System.getProperty("line.separator"));
				content.append(":ex").append(System.getProperty("line.separator"));
				content.append("cls").append(System.getProperty("line.separator"));
				content.append("echo 正在添加受信任站点,关闭弹窗功能...").append(System.getProperty("line.separator"));
				content.append("reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\ZoneMap\\Ranges\" /v @ /t REG_SZ /d \"\" /f").append(System.getProperty("line.separator"));
				content.append("reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\ZoneMap\\Ranges\\Range1\" /v \"http\" /t REG_DWORD  /d 00000002 /f").append(System.getProperty("line.separator"));
				content.append("reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\ZoneMap\\Ranges\\Range1\" /v \":Range\" /t REG_SZ /d \"").append(SERVER_IP).append("\" /f").append(System.getProperty("line.separator"));
				content.append("reg add \"HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\ZoneMap\\Domains\\").append(SERVER_NAME).append("\" /v http /t REG_DWORD /d 0x00000002 /f").append(System.getProperty("line.separator"));
				//配置java例外站点
				content.append("echo ").append(baseServerUrl).append(">>%USERPROFILE%\\AppData\\LocalLow\\Sun\\Java\\Deployment\\security\\exception.sites").append(System.getProperty("line.separator"));
				content.append("ping 127.0.0.1 -n 8 >nul 2>nul").append(System.getProperty("line.separator"));
				content.append("echo   设置完毕，请打开IE测试一下是否正常，若不正常，建议关闭所有IE再次运行本脚本。").append(System.getProperty("line.separator"));
				content.append("echo   本程序稍后自动关闭...").append(System.getProperty("line.separator"));
				content.append("exit)").append(System.getProperty("line.separator"));
				FileUtils.writeFile(disFile.getAbsolutePath(),content.toString(),2);
			}
			downloadFile(response,"启用ActiveX插件.bat", disFile.getAbsolutePath(), false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断文件是否为图片
	 * @param file_suffix
	 * @return
	 */
	private boolean getExtType(String file_suffix) {
        String[] ext_arr = new String[] { "gif", "jpg", "jpeg", "png", "bmp" };
        boolean ext_type = false;
        for (int i = 0; i < ext_arr.length; i++) {
            if (file_suffix != null && file_suffix.equals(ext_arr[i])) {
                ext_type = true;
                break;
            }
        }
        return ext_type;
    }

   /**
    * 对文件进行排序
    * @param order
    * @return
    */
    private java.util.Comparator getComparator(final String order) {
        return new java.util.Comparator() {
            //    Override the method compare
            @Override
			@SuppressWarnings("unchecked")
			public int compare(Object a1, Object b1) {
                Map<String, Object> a = (Map<String, Object>) a1;
                Map<String, Object> b = (Map<String, Object>) b1;

                boolean a_dir = Boolean.parseBoolean(a.get("is_dir").toString());
                boolean b_dir = Boolean.parseBoolean(b.get("is_dir").toString());

                if (a_dir && !b_dir) {
                    return -1;
                } else if (!a_dir && b_dir) {
                    return 1;
                } else {
                    if ("size".equals(order)) {
                        long a_size = Long.parseLong(a.get("filesize").toString());
                        long b_size = Long.parseLong(b.get("filesize").toString());

                        if (a_size > b_size) {
                            return 1;
                        } else if (a_size < b_size) {
                            return -1;
                        } else {
                            return 0;
                        }
                    } else if ("type".equals(order)) {
                        return a.get("filetype").toString().compareTo(
                                b.get("filetype").toString());
                    } else {
                        return a.get("filename").toString().compareTo(
                                b.get("filename").toString());
                    }
                }
         
             }   
        };
    }

    /**
     * 获取上级目录的位置
     * @param current_dir_path
     * @return
     */
    private int getUpIndex(String current_dir_path) {
        int dex = current_dir_path.length()-1;
        int j=0;
        for (int i = dex; i >= 0; i--) {
            if (current_dir_path.charAt(i)=='/') {
				j++;
			}
            if (j == 2){
                dex = i+1;
                break;
            }           
        }
        if (j==1){
            dex = 0;
        }
        return dex;
    }
}
