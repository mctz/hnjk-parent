package com.hnjk.core.foundation.utils;

import java.io.*;
import java.util.zip.ZipInputStream;

import lombok.Cleanup;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

/**
 * 提供一个压缩、解压缩文件的工具类. <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-2-25下午05:44:54
  * @version 1.0
 */
public class ZipUtils {
	
	/*
	 * 对目录的压缩.
	 * @pamar out 压缩文件流
	 * @pamar f 目标文件
	 * @pamar base 基本目录
	 */
	private static void directoryZip(ZipOutputStream out, File f, String base) throws Exception {		
		if (f.isDirectory()) {// 如果传入的是目录
			File[] fl = f.listFiles();			
			out.putNextEntry(new ZipEntry(base + "/"));// 创建压缩的子目录
			if (base.length() == 0) {
				base = "";
			} else {
				base = base + "/";
			}
			for (int i = 0; i < fl.length; i++) {
				directoryZip(out, fl[i], base + fl[i].getName());
			}
		} else {
			// 把压缩文件加入rar中
			out.putNextEntry(new ZipEntry(base));
			@Cleanup FileInputStream in = new FileInputStream(f);
			byte[] bb = new byte[2048];
			int aa = 0;
			while ((aa = in.read(bb)) != -1) {
				out.write(bb, 0, aa);
			}
		}
	}

	/*
	 * 压缩单个文件.
	 * @pamar zos 压缩文件流
	 * @pamar file 目标文件
	 */
	private static void fileZip(ZipOutputStream zos, File file,String replaceName)	throws Exception {
		if (file.isFile()) {			
			zos.putNextEntry(new ZipEntry(ExStringUtils.isNotEmpty(replaceName) ? replaceName:file.getName()));
			@Cleanup FileInputStream fis = new FileInputStream(file);
			byte[] bb = new byte[2048];
			int aa = 0;
			while ((aa = fis.read(bb)) != -1) {
				zos.write(bb, 0, aa);
			}
		} else {
			directoryZip(zos, file, "");
		}
	}
	
	/*
	 * 解压单个文件.
	 * @param zis 压缩文件流
	 * @param file 目标文件
	 */
	private static void fileUnZip(ZipInputStream zis, File file)	throws Exception {
		java.util.zip.ZipEntry zip = zis.getNextEntry();
		
		if (zip == null) {
			return;
		}
		String name = zip.getName();
		File f = new File(file.getAbsolutePath() + "/" + name);
		if (zip.isDirectory()) {
			f.mkdirs();
			fileUnZip(zis, file);
		} else {
			f.createNewFile();
			@Cleanup FileOutputStream fos = new FileOutputStream(f);
			byte[] b = new byte[2048];
			int aa = 0;
			while ((aa = zis.read(b)) != -1) {
				fos.write(b, 0, aa);
			}
			fileUnZip(zis, file);
		}
	}
	
	/**
	 * 解压文件
	 * @param unZipfileName 源文件
	 * @param outputDirectory 目标输出路径
	 */
	public static void fileUnZip(String unZipfileName,String outputDirectory,String encoding){//unZipfileName需要解压的zip文件名
        File file;
        try{
        	org.apache.tools.zip.ZipFile zipFile = new org.apache.tools.zip.ZipFile(unZipfileName,encoding);

            for(java.util.Enumeration entries = zipFile.getEntries(); entries.hasMoreElements();){
                ZipEntry entry = (ZipEntry)entries.nextElement();
                file = new File(outputDirectory+File.separator+entry.getName());

                if(entry.isDirectory()){
                    file.mkdirs();
                }
                else{
                    //如果指定文件的目录不存在,则创建之.
                    File parent = file.getParentFile();
                    if(!parent.exists()){
                        parent.mkdirs();
                    }

					@Cleanup InputStream inputStream = zipFile.getInputStream(entry);
					@Cleanup FileOutputStream fileOut = new FileOutputStream(file);
                    int c;
		            byte[] by=new byte[1024];
		            while((c=inputStream.read(by)) != -1){
		            	fileOut.write(by,0,c);
		            }
                }    
            }
        }catch(Exception ioe){
            ioe.printStackTrace();
        }
    } 
	
	/**
	 * 对目标文件源进行压缩.
	 * @param directory 需要压缩的文件源
	 * @param zipFile 新的压缩文件名
	 */
	public static void zip(String directory, String zipFile) throws Exception{	
			zip(directory, zipFile,null);
		
	}
	
	/**
	 *  对目标文件源进行压缩.
	 * @param directory
	 * @param zipFile
	 * @param replaceName 替换压缩文件名,只针对单个文件压缩.
	 * @throws Exception
	 */
	public static void zip(String directory, String zipFile,String replaceName) throws Exception{
		@Cleanup OutputStream outputStream = new FileOutputStream(zipFile);
		@Cleanup ZipOutputStream zos = new ZipOutputStream(outputStream);
		zos.setEncoding("GBK");
		fileZip(zos, new File(directory),replaceName);
	}
	
	/**
	 * 对目标文件进行解压.
	 * @param directory 解压后的目标文件夹子
	 * @param zipFile 需要解压的压缩包	
	 * @throws IOException 
	 */
	public static void unZip(String directory, String zipFile) throws IOException {
		//ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));			
		@Cleanup ZipFile zis = new ZipFile(zipFile);

		File f = new File(directory);
		f.mkdirs();
//		fileUnZip(zis, f);
//		zis.close();
		if(null != zis){			
			fileUnZip(zipFile, directory,zis.getEncoding());
		}
		//}else{
		//	fileUnZip(zipFile, directory,"UTF-8");
		//}			
		
	}
	
	public static void main(String[] args) throws Exception{
		//zip("c:\\test.csv","c:\\temp.zip",new String("中文.csv".getBytes("GBK"),"UTF-8"));		
		zip("c:\\测试.csv","c:\\temp.zip","学生.csv");
		//unZip("c:\\test", "c:\\test.zip");
	}
	
}
