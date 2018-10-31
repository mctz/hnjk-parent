package com.hnjk.edu.recruit.util;

import java.io.*;
import java.util.List;

import lombok.Cleanup;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class zipUtil implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger(zipUtil.class);
	private static int bufSize = 8096;
	
	private String filename;
	private String temp_path;
	
	private List<File> list;	
	
	public zipUtil (String filename,String temp_path,List<File> list) {
		this.filename = filename;
		this.temp_path = temp_path;
		this.list = list;		
	}
	
	@Override
	public void run() {
		long start = System.currentTimeMillis();
		File file = new File(temp_path);
		File zipFile = new File(temp_path + File.separator + filename);
		
		try{
			@Cleanup OutputStream outputStream = new FileOutputStream(zipFile);
			@Cleanup BufferedOutputStream bos = new BufferedOutputStream(outputStream);
			@Cleanup ZipOutputStream zipOut = new ZipOutputStream(bos);
			zipOut.setEncoding("GBK");
			zipOut.setComment(file.getName());
			if (file.isDirectory()) {
			   File zf = null;
				for (int i = 0; i < list.size(); ++i) {
					zf = list.get(i);
					ZipEntry jarEntry = new ZipEntry(zf.getName());
					@Cleanup InputStream is = new FileInputStream(zf);
					@Cleanup BufferedInputStream bis = new BufferedInputStream(is);
					zipOut.putNextEntry(jarEntry);
					byte[] buf = new byte[bufSize];
					int len;
				    while ((len = bis.read(buf)) >= 0) {
						zipOut.write(buf, 0, len);
				    }
				    logger.info("[" + zf.getName() + "] zip to File:[" + filename + "] success ");
				}
			}
			long end = System.currentTimeMillis();
			logger.info("打包时间："+(end-start)+"毫秒");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getTemp_path() {
		return temp_path;
	}

	public void setTemp_path(String temp_path) {
		this.temp_path = temp_path;
	}

	public List<File> getList() {
		return list;
	}

	public void setList(List<File> list) {
		this.list = list;
	}
}
