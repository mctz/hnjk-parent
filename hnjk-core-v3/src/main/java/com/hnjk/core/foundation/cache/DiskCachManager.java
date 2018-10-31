package com.hnjk.core.foundation.cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.hnjk.core.foundation.utils.FileUtils;

/**
 * 提供一个磁盘缓存池管理类. <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-2-26下午04:59:59
 * @see 
 * @version 1.0
 */
public class DiskCachManager {

	/**
	 * 将文件内容保存为指定的文件.
	 * @param fileName 指定要存储的文件路径(全路径).
	 * @param content 要存储的内容.
	 * @return 保存完成的文件对象.
	 * @throws IOException
	 */
	static File saveFile(String fileName,byte[] content) throws IOException{
		 File f = new File(fileName);
	        File p = f.getParentFile();
	        if (!p.exists()) {
	            //创建父目录
	            p.mkdirs();
	        }
	        // 写内容
	        FileOutputStream fs = new FileOutputStream(f);
	        fs.write(content);
	        fs.close();
	        return f;
	}
	
	/**
	 * 获取文件缓存内容.
	 * @param fileName 文件路径
	 * @param dateLastModified 最后修改时间
	 * @return 文件内容（字节数组）或<code>null</code>
	 */
	public static byte[] getCacheContent(String fileName,long dateLastModified){
		dateLastModified = simplifyDateLastModified(dateLastModified);
        try {
            File f = new File(fileName);
            if (f.exists()) {
                if (f.lastModified() != dateLastModified) {
                    // 如果最后修改时间不同，则删除
                    f.delete();
                } else {
                    return FileUtils.readFile(f);
                }
            }
        } catch (IOException e) {
            //nothing
        }
        return null;
	}
	
	/**
	 * 将缓存存储到磁盘上.
	 * @param fineName
	 * @param content
	 * @param dateLastModified
	 * @throws IOException
	 */
	public static void saveCacheContent(String fineName, byte[] content, long dateLastModified) throws IOException{
		dateLastModified = simplifyDateLastModified(dateLastModified);//最后修改时间
        File f = saveFile(fineName, content);
        //设置最后修改时间
        f.setLastModified(dateLastModified);
	}
	/**
	 * 将时间精确到秒.
	 * @param dateLastModified
	 * @return
	 */
	private static long simplifyDateLastModified(long dateLastModified){
		return dateLastModified / 1000L;
	}
}
