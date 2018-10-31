package com.hnjk.edu.teaching.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 制作水印工具类
 * @author Zik
 *
 */
public class MakeWatermarkUtil {
	
	private static final String PICTRUE_FORMATE_JPG = "jpg";

	/** 
	* 把图片印刷到图片上
	* 
	* @param pressImg --
	* 水印文件
	* @param targetImg --
	* 目标文件
	* @param x
	* --x坐标
	* @param y
	* --y坐标
	*/
	public final static void pressImage(String pressImg, String targetImg, int x, int y) {
		try {
			// 目标文件
			File _file = new File(targetImg);
			Image src = ImageIO.read(_file);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = image.createGraphics();
			g.drawImage(src, 0, 0, wideth, height, null);
			// 水印文件
			File _filebiao = new File(pressImg);
			Image src_biao = ImageIO.read(_filebiao);
			int wideth_biao = src_biao.getWidth(null);
			int height_biao = src_biao.getHeight(null);
			g.drawImage(src_biao, (wideth - wideth_biao) / 2, (height - height_biao) / 2, 
					wideth_biao, height_biao, null);
			g.dispose();
			// 水印文件结束
			FileOutputStream out = new FileOutputStream(targetImg);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
			out.close();		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 
	* 打印文字水印图片
	* 
	* @param pressText
	* --文字
	* @param targetImg --
	* 目标图片
	* @param fontName --
	* 字体名
	* @param fontStyle --
	* 字体样式
	* @param color --
	* 字体颜色
	* @param fontSize --
	* 字体大小
	* @param x --
	* 偏移量
	* @param y
	*/
//	public static void pressText(String pressText, String targetImg,String fontName, 
//			int fontStyle, int color, int fontSize, int x, int y) {
//		try {
//			File _file = new File(targetImg);
//			Image src = ImageIO.read(_file);
//			int wideth = src.getWidth(null);
//			int height = src.getHeight(null);
//			// 水印文字开始
//			BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
//			Graphics g = image.createGraphics();
//			g.drawImage(src, 0, 0, wideth, height, null);
//			g.setColor(new Color(color,false));
//			g.setFont(new Font(fontName, fontStyle, fontSize));
//			g.drawString(pressText, wideth - fontSize - x, height - fontSize/ 2 - y);
//			g.dispose();
//			// 水印文字结束
//			FileOutputStream out = new FileOutputStream(targetImg);
//			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//			encoder.encode(image);
//			out.close();
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//	}
	
	/**
     * 添加文字水印
     * @param targetImg 目标图片路径，如：C://myPictrue//1.jpg
     * @param pressText 水印文字， 如：中国证券网
     * @param fontName 字体名称，    如：宋体
     * @param fontStyle 字体样式，如：粗体和斜体(Font.BOLD|Font.ITALIC)
     * @param fontSize 字体大小，单位为像素
     * @param color 字体颜色
     * @param x 水印文字距离目标图片左侧的偏移量，如果x<0, 则在正中间
     * @param y 水印文字距离目标图片上侧的偏移量，如果y<0, 则在正中间
     * @param alpha 透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     * @param angle 倾斜角度
*/
    public static void pressText(String targetImg, String pressText, String fontName, int fontStyle, int fontSize, Color color, int x, int y, float alpha,int angle) {
        try {
            File file = new File(targetImg);
            
            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, width, height, null);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setColor(color);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));            
            g.rotate(Math.toRadians(-36),width/2,height/2);
            int width_1 = fontSize * getLength(pressText);
            int height_1 = fontSize;
            int widthDiff = width - width_1;
            int heightDiff = height - height_1;
            if(x < 0){
                x = widthDiff / 2;
            }else if(x > widthDiff){
                x = widthDiff;
            }
            if(y < 0){
                y = heightDiff / 2;
            }else if(y > heightDiff){
                y = heightDiff;
            }
            
            g.drawString(pressText, x, y + height_1);
            g.dispose();
            ImageIO.write(bufferedImage, PICTRUE_FORMATE_JPG, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
          * 图片缩放
          * @param filePath 图片路径
          * @param height 高度
          * @param width 宽度
          * @param bb 比例不对时是否需要补白
     */
    public static void resize(String filePath, int height, int width, boolean bb) {
        try {
            double ratio = 0; //缩放比例    
            File f = new File(filePath);   
            BufferedImage bi = ImageIO.read(f);   
            Image itemp = bi.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);   
            //计算比例   
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {   
                if (bi.getHeight() > bi.getWidth()) {   
                    ratio = (new Integer(height)).doubleValue() / bi.getHeight();   
                } else {   
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();   
                }   
                AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);   
                itemp = op.filter(bi, null);   
            }   
            if (bb) {   
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);   
                Graphics2D g = image.createGraphics();   
                g.setColor(Color.white);   
                g.fillRect(0, 0, width, height);   
                if (width == itemp.getWidth(null)) {
					g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);
				} else {
					g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);
				}
                g.dispose();   
                itemp = image;   
            }
            ImageIO.write((BufferedImage) itemp, "jpg", f);   
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static int getFontSize(String path,int w_len,int h_len){
    	
        int result=24;
		try {
			 File file = new File(path);
	         Image image;
			 image = ImageIO.read(file);
			 int width = image.getWidth(null);
	         int height = image.getHeight(null);
	         result = (width/w_len+height/h_len)/2;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
    	return result;
    }
    
    /**
     * 获取字符长度，一个汉字作为 1 个字符, 一个英文字母作为 0.5 个字符
     * @param text
     * @return 字符长度，如：text="中国",返回 2；text="test",返回 2；text="中国ABC",返回 4.
*/
    public static int getLength(String text) {
        int textLength = text.length();
        int length = textLength;
        for (int i = 0; i < textLength; i++) {
            if (String.valueOf(text.charAt(i)).getBytes().length > 1) {
                length++;
            }
        }
        return (length % 2 == 0) ? length / 2 : length / 2 + 1;
    }

	
	public static void main(String[] args) {
//		pressText("黄万值","F:1.png","font-weight", Font.BOLD,0,10,200, 100);
//		pressText("pressText","F:\\01.jpg","font-weight",Font.BOLD,0,10,200,100);
//		pressText("F:\\03.png", "仅供广州大学学籍卡使用", "宋体", Font.BOLD|Font.ITALIC, 20, Color.WHITE, 100, 150, 0.8f,-30);
		//pressImage("F:/2.png", "F:/1.png", 0, 0);
//		String dateStr = String.format("%ty-%1$tm-%1$td %1$tH:%1$tM:%1$tS", new Date());
//		System.out.println(dateStr);
		int fontSize = getFontSize("F:\\01.jpg",260,160)*18;
		System.out.println(88);
		pressText("F:\\03.jpg", "仅供广州大学学籍卡使用", "宋体", Font.BOLD|Font.ITALIC, fontSize, Color.RED, -1, -1, 0.8f,-30);
	}
}
