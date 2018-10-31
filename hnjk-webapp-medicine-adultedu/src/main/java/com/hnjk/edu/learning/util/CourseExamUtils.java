package com.hnjk.edu.learning.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import sun.misc.BASE64Decoder;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;

public class CourseExamUtils {
	/**
	 * html转为word
	 * @param html html内容
	 * @param filePath 生成word路径
	 * @throws IOException
	 */
	public static void htmlToDocumentByPoi(String html,String filePath) throws IOException{
		ByteArrayInputStream bs = new ByteArrayInputStream(html.getBytes());
		POIFSFileSystem poifs = new POIFSFileSystem();
		DirectoryEntry directory = poifs.getRoot();
		DocumentEntry documentEntry = directory.createDocument("WordDocument", bs);		
		FileOutputStream ostream = new FileOutputStream(filePath);
		poifs.writeFilesystem(ostream);
		bs.close();
		ostream.close();
	}
	/**
	 * 替换html中的base64图片数据为实际图片
	 * @param html
	 * @param fileRoot 本地路径
	 * @param serRoot 服务器路径
	 * @return
	 */
	public static String replaceBase64Image(String html,String fileRoot,String serRoot){
		File file = new File(fileRoot);
		if(!file.exists()){//文件根目录不存在时创建
			new File(fileRoot).mkdirs();
		}
		String htmlContent = html;
		Pattern pattern = Pattern.compile("\\<img[^>]*src=\"data:image/[^>]*>");
		Matcher matcher = pattern.matcher(html);	
		GUIDUtils.init();
		while(matcher.find()){		//找出base64图片元素	
			String str = matcher.group();		
			String src = ExStringUtils.substringBetween(str, "src=\"", "\"");//src="..."
			String ext = ExStringUtils.defaultIfEmpty(ExStringUtils.substringBetween(str, "data:image/", ";"), "jpg");//图片后缀			
			String base64ImgData = ExStringUtils.substringBetween(str, "base64,", "\"");//图片数据
			if(ExStringUtils.isNotBlank(ext)&&ExStringUtils.isNotBlank(base64ImgData)){
				//data:image/gif;base64,base64编码的gif图片数据
				//data:image/png;base64,base64编码的png图片数据
				if("jpeg".equalsIgnoreCase(ext)){//data:image/jpeg;base64,base64编码的jpeg图片数据
					ext = "jpg";
				} else if("x-icon".equalsIgnoreCase(ext)){//data:image/x-icon;base64,base64编码的icon图片数据
					ext = "ico";
				}
				String fileName = GUIDUtils.buildMd5GUID(false)+"."+ext;//待存储的文件名
				String filePath = fileRoot+File.separator+fileName;//图片路径
				try {
					convertBase64DataToImage(base64ImgData, filePath);//转成文件
					String serPath = serRoot+fileName;//服务器地址
					htmlContent = htmlContent.replace(src, serPath);//替换src为服务器地址
				} catch (IOException e) {
					e.printStackTrace();
				}		
			}			
		}	
		return htmlContent;
	}
	/**
	 * 把base64图片数据转为本地图片
	 * @param base64ImgData
	 * @param filePath
	 * @throws IOException
	 */
	public static void convertBase64DataToImage(String base64ImgData,String filePath) throws IOException {
		BASE64Decoder d = new BASE64Decoder();
		byte[] bs = d.decodeBuffer(base64ImgData);
		FileOutputStream os = new FileOutputStream(filePath);
		os.write(bs);
		os.close();
	}
	
	/**
	 * 把html内容转为文本，保留图片和表格元素
	 * @param html
	 * @return
	 */
	public static String trimHtml2Txt(String html){	
		String[] filterTags = {"img","table","thead","th","tr","td"};
		html = trimHtml2Txt(html, filterTags);
		return html.trim();
	}
	/**
	 * 把html内容转为文本
	 * @param html 需要处理的html文本
	 * @param filterTags 需要保留的html标签样式
	 * @return
	 */
	public static String trimHtml2Txt(String html, String[] filterTags){	
		html = html.replaceAll("\\<head>[\\s\\S]*?</head>(?i)", "");//去掉head
		html = html.replaceAll("\\<!--[\\s\\S]*?-->", "");//去掉注释
		html = html.replaceAll("\\<![\\s\\S]*?>", "");
		html = html.replaceAll("\\<style[^>]*>[\\s\\S]*?</style>(?i)", "");//去掉样式
		html = html.replaceAll("\\<script[^>]*>[\\s\\S]*?</script>(?i)", "");//去掉js
		html = html.replaceAll("\\<w:[^>]+>[\\s\\S]*?</w:[^>]+>(?i)", "");//去掉word标签
		html = html.replaceAll("\\<xml>[\\s\\S]*?</xml>(?i)", "");
		html = html.replaceAll("\\<html[^>]*>|<body[^>]*>|</html>|</body>(?i)", "");
		html = html.replaceAll("\\\r\n|\n|\r", " ");//去掉换行
		html = html.replaceAll("\\<br[^>]*>(?i)", "\n");
		List<String> tags = new ArrayList<String>();
		List<String> s_tags = new ArrayList<String>();
		List<String> halfTag = Arrays.asList(new String[]{"img","table","thead","th","tr","td"});//
		if(filterTags != null && filterTags.length > 0){
			for (String tag : filterTags) {
				tags.add("<"+tag+(halfTag.contains(tag)?"":">"));//开始标签
				if(!"img".equals(tag)) {
					tags.add("</"+tag+">");//结束标签
				}
				s_tags.add("#REPLACETAG"+tag+(halfTag.contains(tag)?"":"REPLACETAG#"));//尽量替换为复杂一点的标记,以免与显示文本混合,如：文本中包含#td、#table等
				if(!"img".equals(tag)) {
					s_tags.add("#REPLACETAG/"+tag+"REPLACETAG#");
				}
			}
		}
		html = ExStringUtils.replaceEach(html, tags.toArray(new String[tags.size()]), s_tags.toArray(new String[s_tags.size()]));				
		html = html.replaceAll("\\</p>(?i)", "\n");
		html = html.replaceAll("\\<[^>]+>", "");
		html = ExStringUtils.replaceEach(html,s_tags.toArray(new String[s_tags.size()]),tags.toArray(new String[tags.size()]));
		html = html.replaceAll("\\&nbsp;", " ");
		return html.trim();
	}
	
	public static void main(String[] args) throws IOException {
		String html = "<div class=WordSection1 style='layout-grid:15.6pt'><p class=MsoNormal><span style='font-family:宋体'>◎题型：单选题</span></p><p class=MsoNormal align=left style='margin-left:-.1pt;text-align:left'><spanstyle='font-family:宋体'>“</span><u><span style='font-family:宋体'>下划线</span></u><spanstyle='font-family:宋体'>”</span><span style='font-family:宋体'>xxxxxxxx<b>加粗</b>xxxxxx（<spanlang=EN-US> &nbsp;&nbsp;</span>）。</span><img src='http://www.google.com/ig/images/weather/thunderstorm.gif'></p><p style='margin:0cm;margin-bottom:.0001pt'><span lang=EN-US style='font-size:10.5pt;color:black'>&nbsp;&nbsp; A.</span><span style='font-size:10.5pt;color:black'>xxxxxxxxxx<i>斜体</i>xxxxxx<span lang=EN-US>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;B.</span><u>下划线</u>xxxxxx<sup>上标</sup></span></p><p style='margin:0cm;margin-bottom:.0001pt'><span lang=EN-US style='font-size:10.5pt;color:black'>&nbsp;&nbsp; C.</span><i><span style='font-size:10.5pt;color:black'>xxxxxx<b>加粗</b>xxxx</span></i><span lang=EN-US style='font-size:10.5pt;color:black'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;D.</span><s><span style='font-size:10.5pt;color:black'>删除线</span></s><spanstyle='font-size:10.5pt;color:black'>xxxxxxxxxx<sub>下标</sub>xxxxxxxxxx</span></p><table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0 style='border-collapse:collapse;border:none'> <tr>  <td width=284 valign=top style='width:213.05pt;border:solid windowtext 1.0pt;  padding:0cm 5.4pt 0cm 5.4pt'>  <p style='margin:0cm;margin-bottom:.0001pt'><span lang=EN-US  style='font-size:10.5pt;color:black'>A1</span></p>  </td>  <td width=284 valign=top style='width:213.05pt;border:solid windowtext 1.0pt;  border-left:none;padding:0cm 5.4pt 0cm 5.4pt'>  <p style='margin:0cm;margin-bottom:.0001pt'><span lang=EN-US  style='font-size:10.5pt;color:black'>A2</span></p>  </td> </tr> <tr>  <td width=284 valign=top style='width:213.05pt;border:solid windowtext 1.0pt;  border-top:none;padding:0cm 5.4pt 0cm 5.4pt'>  <p style='margin:0cm;margin-bottom:.0001pt'><span lang=EN-US  style='font-size:10.5pt;color:black'>B1</span></p>  </td>  <td width=284 valign=top style='width:213.05pt;border-top:none;border-left:  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;  padding:0cm 5.4pt 0cm 5.4pt'>  <p style='margin:0cm;margin-bottom:.0001pt'><span lang=EN-US  style='font-size:10.5pt;color:black'>B2</span></p>  </td> </tr></table><br/><p class=MsoNormal><span style='font-family:宋体;color:red'>【答案：</span><spanlang=EN-US style='color:red'>D</span><span style='font-family:宋体;color:red'>】</span></p><p class=MsoNormal><span lang=EN-US>&nbsp;</span></p><p class=MsoNormal><span lang=EN-US>&nbsp;</span></p></div>";
		//图片、表格、下划线、加粗、删除线、上标、下标
		//String[] filterTags = {"img","table","thead","th","tr","td","u","b","s","i","sup","sub"};
		String[] filterTags = {"img","table","thead","th","tr","td","u","b"};
		System.out.println(trimHtml2Txt(html,filterTags));		
	}
}
