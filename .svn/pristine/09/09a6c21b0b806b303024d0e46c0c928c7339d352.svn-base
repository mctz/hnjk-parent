package com.hnjk.edu.recruit.util;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;



/*
 * 条形码工具类
 */
public class BarcodeUtil {
	/*
	 * 将内容编译成条形码,用io流输出,输出使用之后记得关流
	 */
	public static InputStream encode(String encodeContent,int width,int height){
		
		OutputStream stream = null;
		try {
			Code128Writer writer = new Code128Writer();

			stream = new ByteArrayOutputStream();
			BitMatrix m = writer.encode(encodeContent, BarcodeFormat.CODE_128, width, height);
			MatrixToImageWriter.writeToStream(m, "JPEG", stream);
			BufferedImage image = MatrixToImageWriter.toBufferedImage(m);
			byte[] buffer = ((ByteArrayOutputStream) stream).toByteArray();
			return new ByteArrayInputStream(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}  finally {
			if (stream != null) {
				try {
					stream.flush();
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static File encode2Image(String encodeContent,int width,int height,String imgPath){
		File file = new File(imgPath);
		try {
			if(!file.exists()){
				Code128Writer writer = new Code128Writer();
				BitMatrix bitMatrix = writer.encode(encodeContent,BarcodeFormat.CODE_128, width, height, null); 
				MatrixToImageWriter.writeToFile(bitMatrix, "JPEG", file);     
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file; 
	}
}
