package com.hnjk.core.foundation.utils;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 图片压缩工具.
 * <code>ImageUtils</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-7-16 下午05:12:48
 * @see 
 * @version 1.0
 */
public class ImageUtils {

	private String inputDir; // 输入图路径

	private String outputDir; // 输出图路径

	private String inputFileName; // 输入图文件名

	private String outputFileName; // 输出图文件名

	private int outputWidth = 120; // 默认输出图片宽

	private int outputHeight = 120; // 默认输出图片高

	private int rate = 0;//比例

	private boolean proportion = true; // 是否等比缩放标记(默认为等比缩放)

	public ImageUtils() {
		// 初始化变量
		this.inputDir = "";
		this.outputDir = "";
		this.inputFileName = "";
		this.outputFileName = "";
		this.outputWidth = 120;
		this.outputHeight = 120;
		this.rate = 0;
	}
	
	

	public void setInputDir(String InputDir) {
		this.inputDir = InputDir;
	}

	public void setOutputDir(String OutputDir) {
		this.outputDir = OutputDir;
	}

	public void setInputFileName(String InputFileName) {
		this.inputFileName = InputFileName;
	}

	public void setOutputFileName(String OutputFileName) {
		this.outputFileName = OutputFileName;
	}

	public void setOutputWidth(int OutputWidth) {
		this.outputWidth = OutputWidth;
	}

	public void setOutputHeight(int OutputHeight) {
		this.outputHeight = OutputHeight;
	}

	public void setW_H(int width, int height) {
		this.outputWidth = width;
		this.outputHeight = height;
	}

	public String s_pic() {		
		// 建立输出文件对象
		File file = new File(outputDir + outputFileName);
		FileOutputStream tempout = null;
		try {
			tempout = new FileOutputStream(file);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		Image img = null;
		Toolkit tk = Toolkit.getDefaultToolkit();
		Applet app = new Applet();
		MediaTracker mt = new MediaTracker(app);
		try {
			img = tk.getImage(inputDir + inputFileName);
			mt.addImage(img, 0);
			mt.waitForID(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (img.getWidth(null) == -1) {
			System.out.println(" can't read,retry!" + "<BR>");
			return "no";
		} else {
			int new_w;
			int new_h;
			if (this.proportion == true) { // 判断是否是等比缩放.
			// 为等比缩放计算输出的图片宽度及高度
				double rate1 = ((double) img.getWidth(null))
						/ (double) outputWidth + 0.1;
				double rate2 = ((double) img.getHeight(null))
						/ (double) outputHeight + 0.1;
				double rate = rate1 > rate2 ? rate1 : rate2;
				new_w = (int) (((double) img.getWidth(null)) / rate);
				new_h = (int) (((double) img.getHeight(null)) / rate);
			} else {
				new_w = outputWidth; // 输出的图片宽度
				new_h = outputHeight; // 输出的图片高度
			}
			BufferedImage buffImg = new BufferedImage(new_w, new_h,	BufferedImage.TYPE_INT_RGB);

			Graphics g = buffImg.createGraphics();

			g.setColor(Color.white);
			g.fillRect(0, 0, new_w, new_h);

			g.drawImage(img, 0, 0, new_w, new_h, null);
			g.dispose();

			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(tempout);
			try {
				encoder.encode(buffImg);
				tempout.close();
			} catch (IOException ex) {
				System.out.println(ex.toString());
			}
		}
		return "ok";
	}

	public String s_pic(String InputDir, String OutputDir,String InputFileName, String OutputFileName) {
		// 输入图路径
		this.inputDir = InputDir;
		// 输出图路径
		this.outputDir = OutputDir;
		// 输入图文件名
		this.inputFileName = InputFileName;
		// 输出图文件名
		this.outputFileName = OutputFileName;
		return s_pic();
	}

	

	public static void main(String[] a) {
		// s_pic(大图片路径,生成小图片路径,大图片文件名,生成小图片文名,生成小图片宽度,生成小图片高度)
		ImageUtils mypic = new ImageUtils();
		mypic.setInputDir("C:\\temp\\");
		mypic.setInputFileName("marco2.jpg");
		mypic.setOutputDir("D:\\temp\\");
		mypic.setOutputFileName("tttt1.jpg");
		mypic.setW_H(60, 60);
		System.out.println("===>"+mypic.s_pic());
//		System.out.println(mypic.s_pic("E:\\Document\\My Pictures\\",
//				"E:\\Document\\My Pictures\\", "topbg-3.gif", "3.gif", 400,
//				400, true));

	}
}
