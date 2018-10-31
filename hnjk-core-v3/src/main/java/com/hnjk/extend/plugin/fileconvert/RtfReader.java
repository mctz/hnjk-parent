package com.hnjk.extend.plugin.fileconvert;

import lombok.Cleanup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.rtf.RTFEditorKit;

/**
 * 读取RTF文档内容.
 * <code>RtfReader</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2011-5-9 下午02:40:57
 * @see 
 * @version 1.0
 */
public class RtfReader {

	public static String getTextFromRtf(String filePath) {
		String result = null;
		File file = new File(filePath);
		RTFEditorKit rtf = new RTFEditorKit();
		try {
			DefaultStyledDocument styledDoc = new DefaultStyledDocument();
			@Cleanup InputStream is = new FileInputStream(file);
			rtf.read(is, styledDoc, 0);		
			String encode = getEncoding(styledDoc.getText(0, styledDoc.getLength()));
			if("UTF-8".equals(encode) || "ISO-8859-1".equals(encode)){
				result = new String(styledDoc.getText(0, styledDoc.getLength()).getBytes("ISO-8859-1"),"GB2312");
			}else{
				result = new String(styledDoc.getText(0, styledDoc.getLength()).getBytes(encode),"GB2312");
			}
			
			// 提取文本，读取中文需要使用ISO8859_1编码，否则会出现乱码
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getEncoding(String str) { 
		String encode = "ISO-8859-1";   
	       try {   
	           if (str.equals(new String(str.getBytes(encode), encode))) {   
	                String s1 = encode;   
	               return s1;   
	            }   
	        } catch (Exception exception1) {   
	      }   
         encode = "GB2312";   
       try {   
           if (str.equals(new String(str.getBytes(encode), encode))) {   
                String s = encode;   
               return s;   
            }   
        } catch (Exception exception) {   
        }   
       
        encode = "UTF-8";   
       try {   
           if (str.equals(new String(str.getBytes(encode), encode))) {   
                String s2 = encode;   
               return s2;   
            }   
        } catch (Exception exception2) {   
        }   
        encode = "GBK";   
       try {   
           if (str.equals(new String(str.getBytes(encode), encode))) {   
                String s3 = encode;   
               return s3;   
            }   
        } catch (Exception exception3) {   
        }   
       return "";   
    }   

	
	
	public static void main(String[] args) throws Exception{
		String s = "中国";
		String s1 = new String(s.getBytes("ISO8859_1"),"UTF-8");
		String encode = getEncoding(s1);
		System.out.println(encode);
		String temp = new String(s.getBytes(encode), "GB2312");   
		System.out.println(temp);
	}
}
