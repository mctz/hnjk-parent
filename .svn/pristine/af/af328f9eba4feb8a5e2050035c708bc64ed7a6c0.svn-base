package com.hnjk.core.foundation.utils;

import lombok.Cleanup;
import org.hibernate.SessionFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.CacheRequest;
import java.net.HttpURLConnection;
import java.net.URLDecoder;

public class ExFileUtils {

	private static final String REPORTS_PATH = File.separator+"reports"+File.separator;
	/**
	 * 文件不存在
	 */
	public static final int NOTEXIST = -1;

	/**
	 * 文件存在
	 */
	public static final int EXIST = 1;

	/**
	 * 文件类型错误
	 */
	public static final int TYPEERROR = 0;

	/**
	 * 文件拷贝
	 * @param orgUrl
	 * @param objUrl
	 */
	public static void copy(String orgUrl, String objUrl) {
		File file = new File(objUrl);
		if (file.exists()) {
			file.delete();
		}
		try {
			@Cleanup FileInputStream in = new FileInputStream(new File(orgUrl));
			@Cleanup FileOutputStream out = new FileOutputStream(new File(objUrl));
			byte[] buff = new byte[512];
			int n = 0;
			while ((n = in.read(buff)) != -1) {
				out.write(buff, 0, n);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.fillInStackTrace();
		}
	}


	/**
	 * 判断文件是否为Excel
	 * @param filePath
	 * @return
	 */
	public static int isExcelFile(String filePath) {
		if (filePath==null || !filePath.matches("^.+\\.(?i)((xls)|(xlsx))$")) {
			return TYPEERROR;
		}else {
			File file = new File(filePath);
			if (file == null || !file.exists()) {
				return NOTEXIST;
			}
		}
		return  EXIST;
	}

	public static String getRealPathForReports(HttpServletRequest request,String jasperFile) {
		try {
			if (jasperFile.startsWith(File.separator)) {
				jasperFile = jasperFile.substring(1,jasperFile.length());
			}
			return URLDecoder.decode(request.getSession().getServletContext().getRealPath(REPORTS_PATH+jasperFile),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
}
