package com.hnjk.core.foundation.template;

/**
 * 模板参数. <p>
 * 用来支持根据模板生成html的实现.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-3-11下午08:46:55
 * @see com.gdcn.core.template.FreemarkBuildHelper
 * @version 1.0
 */
public class TemplateParam {

	private String realPath;//真实路径，一般是指服务器的context path

	private String templatePath;//模板路径

	private String saveDirectory;//文件生成保存路径

	private String filePostfix;//文件生成扩展名
	
	private String templateName; //模板名称

	public String getFilePostfix() {
		return filePostfix;
	}

	public void setFilePostfix(String filePostfix) {
		this.filePostfix = filePostfix;
	}

	public String getRealPath() {
		return realPath;
	}

	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	public String getSaveDirectory() {
		return saveDirectory;
	}

	public void setSaveDirectory(String saveDirectory) {
		this.saveDirectory = saveDirectory;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}
	
	
}
