package com.hnjk.platform.taglib;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.configuration.json.JsonBinder;
import com.hnjk.core.support.context.Constants;
import com.hnjk.extend.taglib.BaseTagSupport;
import com.hnjk.extend.taglib.function.JstlCustFunction;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 附件上传标签
 * @author Marco.hu
 *
 */
public class AttachUploadTag extends BaseTagSupport{

	private static final long serialVersionUID = -5327086681601918917L;
	
	private String baseStorePath;//基本的存储路径(请使用,间隔)，如果是图片，则表示为图片的显示根路径
	
	private String fileExt =  CacheAppManager.getSysConfigurationByCode("web.uploadfile.accept").getParamValue();//支持的附件格式，默认取系统配置的附件类型
	
	private String fileSize = CacheAppManager.getSysConfigurationByCode("web.uploadfile.filesize").getParamValue();//允许上传的附件大小，默认去系统配置附件大小
	
	private String isMulti = "true";//是否允许上传多个文件，默认允许
	
	private Integer uploadLimit = 10;//允许上传的文件个数
	
	transient private List<Attachs> attachList;//附件列表
	
	private String hiddenInputName = "attachId";//附件ID隐藏域名,如果为上次图片是，则传入图片路径字段名
	
	private String uploadType = "attach";//attach-附件 image - 图片
	
	private String imageSize = "180*140";//图片显示尺寸 width*height 默认为180*140
	
	private String defaultImagePath ;//默认的图片文件
	
	private String extendStorePath;//扩展的存储路径
	
	private String replaceName;//附件替代名
	
	private String formType;//表单类型
	
	private String formId;//表单ID
	
	private String isStoreToDatabase;//是否需要存数据库 Y|N
	
	private String pageType = "sys";//页面类型：sys - 系统 , other - 其他
	
	@Override
	public int doEndTag() throws JspException {
		
		StringBuffer sb = new StringBuffer();
		
		String tocken = ExStringUtils.getRandomString(6);//获取一个6位随机数，作为ID前缀	
		
		sb.append("<script type=\"text/javascript\"> ");		
		sb.append("$(function(){");
		sb.append("$(\"#_"+tocken+"uploadify_attachs\").uploadify({ ");
		String storePath = baseStorePath;
		if(ExStringUtils.isNotEmpty(extendStorePath)){
			storePath += ","+extendStorePath;
		}
		sb.append(" 'script' 	:      '"+getBaseUrl()+"/edu3/filemanage/upload.html',");//portal,attachs,${storeDir}
		sb.append(" 'auto'      : 		true,");
		
		Map<String, Object> scriptDataMap = new HashMap<String, Object>();//参数
		//设置上传人信息
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		scriptDataMap.put("fillinName", cureentUser.getCnName());
		scriptDataMap.put("fillinNameId", cureentUser.getResourceid());
		scriptDataMap.put("storePath", storePath);		
		if(ExStringUtils.isNotEmpty(replaceName)) {
			scriptDataMap.put("replaceName", replaceName);
		}
		if(ExStringUtils.isNotEmpty(formType)) {
			scriptDataMap.put("formType", formType);
		}
		if(ExStringUtils.isNotEmpty(formId)) {
			scriptDataMap.put("formId", formId);
		}
		
		if("image".equals(uploadType)){//如果为上次图片则不允许多文件上传
			sb.append(" 'multi'     : 		false,");
			sb.append("'fileDesc'   : '支持格式:jpg/gif/png',");			
			scriptDataMap.put("isStoreToDatabase", Constants.BOOLEAN_NO);
			sb.append("'fileExt'    : '*.JPG;*.GIF;*.PNG;', ");	
			sb.append("'buttonImg'  : '"+getBaseUrl()+"/jscript/jquery.uploadify/images/icon_upload_image.png', ");
		}else if("excel".equals(uploadType)){//excel
			sb.append(" 'multi'     : 		"+isMulti+",");
			sb.append("'fileDesc'   : '支持格式:xls',");
			scriptDataMap.put("isStoreToDatabase", Constants.BOOLEAN_YES);
			sb.append("'fileExt'    : '*.xls;', ");
			sb.append("'buttonImg'  : '"+getBaseUrl()+"/jscript/jquery.uploadify/images/icon_upload.png', ");
		} else{
			sb.append(" 'multi'     : 		"+isMulti+",");
			sb.append("'fileDesc'   : '支持格式:"+convertFileExt()+"',");
			scriptDataMap.put("isStoreToDatabase", Constants.BOOLEAN_YES);
			sb.append("'fileExt'    : '"+convertFileExt()+"', ");
			sb.append("'buttonImg'  : '"+getBaseUrl()+"/jscript/jquery.uploadify/images/icon_upload.png', ");
		}	
		//sb.append("'uploadLimit': "+uploadLimit+",");
		sb.append("'scriptData' : "+JsonBinder.buildNonDefaultBinder().toJson(scriptDataMap)+",");//参数
		sb.append("'sizeLimit'  : "+fileSize+",");           
		//上传唯一性校验
		if("attach".equals(uploadType) || "excel".equals(uploadType)){
			sb.append("onSelect: function(e, queueId, fileObj){");
			sb.append(" var uploadLimit = '"+uploadLimit+"';");
			sb.append("var attachList = $(\"#_"+tocken+"uploadifyQueue\").children('li');");
			
			sb.append("if(attachList && attachList.length >=uploadLimit){");
			sb.append("sys".equals(pageType) ? "alertMsg.error" : "alert");
			sb.append("('只允许上传"+uploadLimit+"个文件.');$(\"#_"+tocken+"uploadify_attachs\").uploadifyCancel('\"+queueId+\"');return false;}");
			
			sb.append("},");	
		}
	
		
		sb.append("onComplete: function (event, queueID, fileObj, response, data) { "); //回调               	
		sb.append("var result = response.split('|'); "); 	        
		if("image".equals(uploadType)){
			sb.append(" var imageStorePath = '"+getRootUrl()+CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue()+baseStorePath.replace(",", "/")+"/"+extendStorePath+"/'+result[1];");
			sb.append("$('#_"+tocken+"facePic').attr('src',imageStorePath);");
			sb.append("$('#_"+tocken+hiddenInputName+"').val('"+extendStorePath+"/'+result[1]);");
			
		}else{
			sb.append("$(\"#_"+tocken+"uploadifyQueue\").append(\"<li style='padding-bottom:6px' id='\"+result[0]+\"'>");		
			sb.append("<img src='"+getBaseUrl()+"/jscript/jquery.uploadify/images/attach.png' style='cursor:pointer;height:10px'>");
			sb.append("&nbsp;&nbsp;\"+fileObj.name+\"&nbsp;&nbsp;<img src='"+getBaseUrl()+"/jscript/jquery.uploadify/images/cancel.png'  onClick=_"+tocken+"deleteAttachFile('\"+result[0]+\"');  style='cursor:pointer;height:10px'></li>\");");
			sb.append("$(\"#_"+tocken+"attachList\").append(\"<input type='hidden' id='"+hiddenInputName+"' name='"+hiddenInputName+"' value='\"+result[0]+\"'>\");");
						
		}
		
		sb.append("},"); 
		sb.append("onError: function(event, queueID, fileObj,errorObj) {  ");
		sb.append(" if(errorObj.type=='File Size'){");//文件大小超过限制
		sb.append("sys".equals(pageType) ? "alertMsg.error" : "alert");
		sb.append("('单个文件大小不能超过"+JstlCustFunction.formatFileSize(Long.valueOf(ExStringUtils.trimToEmpty(fileSize)))+"'); ");
		sb.append(" } else { ");//其他错误
		sb.append("sys".equals(pageType) ? "alertMsg.error" : "alert");
		sb.append("('文件' + fileObj.name + '上传失败. '+errorObj.type + ' Error: ' + errorObj.info);"); 
		sb.append(" }  ");	
		sb.append("$('#_"+tocken+"uploadify_attachs').uploadifyClearQueue(); ");
		sb.append("}");
		
		sb.append("});");
		if("image".equals(uploadType)){//如果是图片，则显示默认的图片
			
			if(ExStringUtils.isNotEmpty(defaultImagePath)){
				sb.append(" $('#_"+tocken+"facePic').attr('src','"+getRootUrl()+CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue()+baseStorePath.replace(",", "/")+"/"+defaultImagePath+"'); ");
			}
			 
		       
		}
		sb.append(" });");
		sb.append("</script>");
	
		//附件HTML
		//TODO 选择文件功能
		if("image".equals(uploadType)){
			String[] arr = imageSize.split("\\*");
			sb.append("<img id='_"+tocken+"facePic' src='"+ getBaseUrl()+"/themes/default/images/img_preview.png' width='"+arr[0]+"' height='"+arr[1]+"'/>");
			sb.append("<div id=\"filelist1\"></div>");
			//sb.append("<div class=\"button\"><div class=\"buttonContent\"><button type=\"button\"  onclick=\"_;\">选择图片</button></div></div>&nbsp;&nbsp;&nbsp;&nbsp;");
			sb.append("<input name='_"+tocken+"uploadify_attachs'  id='_"+tocken+"uploadify_attachs' type='file'/>");	
			sb.append("<input type='hidden' name='"+hiddenInputName+"' id='_"+tocken+hiddenInputName+"' value='"+defaultImagePath+"'/>");		
			 
		}else{
			sb.append("<div id=\"_"+tocken+"attachList\" style=\"display:none\">");	
			if(null != attachList && !attachList.isEmpty())	{
				for(Attachs attach : getAttachList()){
					sb.append("<input type='hidden' id='"+hiddenInputName+"' name='"+hiddenInputName+"' value='"+attach.getResourceid()+"'>");
				}
			}
			sb.append("</div>");
			sb.append("<div id=\"_"+tocken+"uploadifyQueue\">");
			if(null != attachList && !attachList.isEmpty())	{
				for(Attachs attach : getAttachList()){
					sb.append("<li style='padding-bottom:6px' id='"+attach.getResourceid()+"'>");
					sb.append("<img src='"+getBaseUrl()+"/jscript/jquery.uploadify/images/attach.png' style='cursor:pointer;height:10px'>&nbsp;&nbsp; ");
					sb.append("<a onclick=\"_"+tocken+"downloadAttachFile('"+attach.getResourceid()+"')\" href=\"##\">");
					sb.append(attach.getAttName()+"&nbsp;&nbsp;");				
					sb.append("</a>");
					sb.append("<img src='"+getBaseUrl()+"/jscript/jquery.uploadify/images/cancel.png' onClick=\"_"+tocken+"deleteAttachFile('"+attach.getResourceid()+"')\" style='cursor:pointer;height:10px'>");
					sb.append("</li>");
				}
			}
			
			sb.append("</div>");	
			//sb.append("<div id=\"_"+tocken+"uploadifyQueue\"></div>");
			sb.append("<input type=\"file\" name=\"uploadify_attachs\" id=\"_"+tocken+"uploadify_attachs\" />");
		}
		
		
	
		
		//删除附件JS	
		sb.append("<script type=\"text/javascript\"> ");
		if("attach".equals(uploadType) || "excel".equals(uploadType)){
			sb.append("function _"+tocken+"deleteAttachFile(attid){ ");  //删除附件
			if("sys".equals(pageType)){
				sb.append("			alertMsg.confirm(\"确定要删除这个附件？\", {");
				sb.append("			okCall: function(){");
				sb.append("				var url = '"+getBaseUrl()+"/edu3/framework/filemanage/delete.html';"); 
				sb.append("				$.get(url,{fileid:attid},function(data){");
				sb.append("				$(\"#_"+tocken+"uploadifyQueue > li[id='\"+attid+\"']\").remove();");
				sb.append("				$(\"#"+hiddenInputName+"[value='\"+attid+\"']\").remove();");
				sb.append("			});");		
				sb.append("		}");
				sb.append("});"); 
			}else{
				sb.append(" if(confirm(\"确定要删除这个附件？\")){");
				sb.append("$.get('"+getBaseUrl()+"/edu3/framework/filemanage/delete.html',{fileid:attid},function(data){");
				sb.append("$(\"#_"+tocken+"uploadifyQueue > li[id='\"+attid+\"']\").remove();");
				sb.append("$(\"#"+hiddenInputName+"[value='\"+attid+\"']\").remove();");
				sb.append("});}");
			}
			sb.append("}");
			sb.append("function _"+tocken+"downloadAttachFile(attid){");//下载附件
			sb.append("var url = '"+getBaseUrl()+"/edu3/framework/filemanage/download.html?id='+attid;");
			sb.append("var elemIF = document.createElement(\"iframe\");");  
			sb.append("elemIF.src = url;");  
			sb.append("elemIF.style.display = \"none\"; "); 
			sb.append("document.body.appendChild(elemIF);"); 
			sb.append("}");
			
		}
		sb.append("</script>");
		JspWriter writer = this.pageContext.getOut();
		try {
			writer.append(sb.toString());
		} catch (IOException e) {
			
		}
		return EVAL_PAGE;
	}
	
	private String convertFileExt(){
		String s = "";
		if(ExStringUtils.isNotEmpty(fileExt)){
			String[] arr = fileExt.split("\\|");
			for(int i=0;i<arr.length;i++){
				s += "*."+arr[i].toUpperCase()+";";
			}
		}
		return s;
	}
	
	

	/**
	 * @return the replaceName
	 */
	public String getReplaceName() {
		return replaceName;
	}

	/**
	 * @param replaceName the replaceName to set
	 */
	public void setReplaceName(String replaceName) {
		this.replaceName = replaceName;
	}

	/**
	 * @return the formType
	 */
	public String getFormType() {
		return formType;
	}

	/**
	 * @param formType the formType to set
	 */
	public void setFormType(String formType) {
		this.formType = formType;
	}

	/**
	 * @return the formId
	 */
	public String getFormId() {
		return formId;
	}

	/**
	 * @param formId the formId to set
	 */
	public void setFormId(String formId) {
		this.formId = formId;
	}

	/**
	 * @return the isStoreToDatabase
	 */
	public String getIsStoreToDatabase() {
		return isStoreToDatabase;
	}

	/**
	 * @param isStoreToDatabase the isStoreToDatabase to set
	 */
	public void setIsStoreToDatabase(String isStoreToDatabase) {
		this.isStoreToDatabase = isStoreToDatabase;
	}

	/**
	 * @return the baseStorePath
	 */
	public String getBaseStorePath() {
		return baseStorePath;
	}

	/**
	 * @param baseStorePath the baseStorePath to set
	 */
	public void setBaseStorePath(String baseStorePath) {
		this.baseStorePath = baseStorePath;
	}

	/**
	 * @return the extendStorePath
	 */
	public String getExtendStorePath() {
		return extendStorePath;
	}

	/**
	 * @param extendStorePath the extendStorePath to set
	 */
	public void setExtendStorePath(String extendStorePath) {
		this.extendStorePath = extendStorePath;
	}

	/**
	 * @return the defaultImagePath
	 */
	public String getDefaultImagePath() {
		return defaultImagePath;
	}

	/**
	 * @param defaultImagePath the defaultImagePath to set
	 */
	public void setDefaultImagePath(String defaultImagePath) {
		this.defaultImagePath = defaultImagePath;
	}

	/**
	 * @return the imageSize
	 */
	public String getImageSize() {
		return imageSize;
	}

	/**
	 * @param imageSize the imageSize to set
	 */
	public void setImageSize(String imageSize) {
		this.imageSize = imageSize;
	}

	/**
	 * @return the uploadType
	 */
	public String getUploadType() {
		return uploadType;
	}

	/**
	 * @param uploadType the uploadType to set
	 */
	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}

	/**
	 * @return the hiddenInputName
	 */
	public String getHiddenInputName() {
		return hiddenInputName;
	}

	/**
	 * @param hiddenInputName the hiddenInputName to set
	 */
	public void setHiddenInputName(String hiddenInputName) {
		this.hiddenInputName = hiddenInputName;
	}

	
	

	/**
	 * @return the isMulti
	 */
	public String getIsMulti() {
		return isMulti;
	}

	/**
	 * @param isMulti the isMulti to set
	 */
	public void setIsMulti(String isMulti) {
		this.isMulti = isMulti;
	}

	/**
	 * @return the fileExt
	 */
	public String getFileExt() {
		return fileExt;
	}

	/**
	 * @param fileExt the fileExt to set
	 */
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	/**
	 * @return the fileSize
	 */
	public String getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * @return the attachList
	 */
	public List<Attachs> getAttachList() {
		return attachList;
	}

	/**
	 * @param attachList the attachList to set
	 */
	public void setAttachList(List<Attachs> attachList) {
		this.attachList = attachList;
	}

	/**
	 * @return the uploadLimit
	 */
	public Integer getUploadLimit() {
		return uploadLimit;
	}

	/**
	 * @param uploadLimit the uploadLimit to set
	 */
	public void setUploadLimit(Integer uploadLimit) {
		this.uploadLimit = uploadLimit;
	}

	/**
	 * @return the pageType
	 */
	public String getPageType() {
		return pageType;
	}

	/**
	 * @param pageType the pageType to set
	 */
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	
	
	
}
