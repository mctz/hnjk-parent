package com.hnjk.edu.teaching.helper;

import lombok.Cleanup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
/**
 * <code>准考证打印工具类.
 * <p>用于获取学生照片，当给定目录中不存在图片文件时用默认图片
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2011-11-22 下午05:24:57
 * @see 
 * @version 1.0
 */
public class ExamCardImageHelper {
	public static InputStream getImage(String baseUrl,String imagePath){
		InputStream is = null;
		try {
			File file  = null;
			if (imagePath.startsWith("/")) {
				file   = new File(baseUrl+imagePath);
			}else {
				file   = new File(baseUrl+File.separator+imagePath);
			}
			if (file.exists()) {
				is        = new FileInputStream(file);
			}else {
				is        = new FileInputStream(baseUrl+File.separator+"default.jpg");
			}
			
		} catch (Exception e) {
			try {
				is         = new FileInputStream(baseUrl+File.separator+"default.jpg");
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		return is;
	}
}
