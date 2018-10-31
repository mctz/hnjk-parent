package com.hnjk.security.verifyimage;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.GradientBackgroundGenerator;
import com.octo.captcha.component.image.color.ColorGenerator;
import com.octo.captcha.component.image.color.RandomListColorGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.DecoratedRandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.textpaster.textdecorator.BaffleTextDecorator;
import com.octo.captcha.component.image.textpaster.textdecorator.TextDecorator;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.ImageCaptcha;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 随即验证码生成服务. <p>
 * 通过一个单例提供唯一验证码.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-5-13上午10:35:02
 * @see com.octo.captcha.engine.image.ListImageCaptchaEngine
 * @version 1.0
 */
public class CaptchaServiceSingleton extends ListImageCaptchaEngine {

	protected static Logger logger = LoggerFactory.getLogger(CaptchaServiceSingleton.class);
	     
   
     private static final String NUMERIC_CHARS = "123456789";// No numeric 0  (因为用户不要识别)
   
     private static final String UPPER_ASCII_CHARS = "ACDEFGHJKLMNPQRSTXYZ";// upper No B I O U V W   
    
     private static CaptchaServiceSingleton instance = new CaptchaServiceSingleton();  //实例   
    
   
     ImageCaptcha imageCaptcha = null;  
     
     //单例
     private CaptchaServiceSingleton() {}  
   
     public static CaptchaServiceSingleton getInstance() {  
         return instance;  
     }  
     
     /**
      * 初始化图片生成工厂
      */
     @Override
	 protected void buildInitialFactories() {
         try {  
             
        	 //生成验证码.没有字母O和o,以及数字0,以免用户很难区分  
        	 WordGenerator wordGenerator = new RandomWordGenerator(NUMERIC_CHARS + UPPER_ASCII_CHARS);  
             Color[] color = new Color[]{new Color(13, 141, 20),new Color(25, 74, 180), new Color(171, 24, 4),new Color(185, 149, 3)};
             ColorGenerator colorGenerator = new RandomListColorGenerator(color);
             //背景图生成
             BackgroundGenerator backgroundGenerator = new GradientBackgroundGenerator(56, 26,Color.white, Color.lightGray);
             //图形中的验证码字体样式
             TextDecorator textDecorator = new BaffleTextDecorator(0, colorGenerator);
             TextPaster textPaster = new DecoratedRandomTextPaster(4,4,colorGenerator,true,new TextDecorator[]{textDecorator});
             Font[] fonts = { new Font("Verdana", Font.BOLD, 12),new Font("Tahoma", Font.BOLD,12),new Font("宋体", Font.BOLD,14) };
             FontGenerator fontGenerator = new RandomFontGenerator(12,14, fonts);

             WordToImage wordToImage = new ComposedWordToImage(fontGenerator,backgroundGenerator, textPaster);

             this.addFactory(new GimpyFactory(wordGenerator, wordToImage));
          
         } catch (Exception ex) {  
             logger.error("产生校验码初始化异常" + ex);             
         }  
     } 
     
     /** 
      * 用当期的servlet response写图片
      *  
      * @param request  HttpServletRequest 
      * @param response HttpServletResponse 
      * @throws IOException 
      */  
     public void writeCaptchaImage(HttpServletRequest request, HttpServletResponse response) throws IOException {  
   
         imageCaptcha = getNextImageCaptcha();  
         HttpSession session = request.getSession();  
         session.setAttribute("imageCaptcha", imageCaptcha);  
         BufferedImage image = (BufferedImage) imageCaptcha.getChallenge();  
   
         OutputStream outputStream = null;  
         try {  
             outputStream = response.getOutputStream();               
             response.setHeader("Cache-Control", "no-store");  
             response.setHeader("Pragma", "no-cache");  
             response.setDateHeader("Expires", 0);  
   
             response.setContentType("image/jpeg");  
   
             JPEGImageEncoder encoder = JPEGCodec .createJPEGEncoder(outputStream);  
             encoder.encode(image);  
   
             outputStream.flush();  
             outputStream.close();  
             outputStream = null;// no close twice  
         } catch (IOException ex) {  
             logger.error("产生图片异常: " + ex);  
             throw ex;  
         } finally {  
             if (outputStream != null) {  
                 try {  
                     outputStream.close();  
                 } catch (IOException ex) {  
                 }  
             }  
             imageCaptcha.disposeChallenge();  
         }  
     }  
     
     /**
      * 校验-验证后就将清除session
      * @param validateCode
      * @param session
      * @return
      */
     public boolean validateCaptchaResponse(String validateCode,  HttpSession session) {  
         boolean flag = true;  
         try {  
             imageCaptcha = (ImageCaptcha) session.getAttribute("imageCaptcha");  
             if (imageCaptcha == null) {                  
                 return false;  
             }  
            if(ExStringUtils.isEmpty(validateCode)){
            	return false;
            }
             validateCode = validateCode.toUpperCase();// use upper case for easier usage  
             flag = (imageCaptcha.validateResponse(validateCode)).booleanValue();  
             session.removeAttribute("imageCaptcha");  
             return flag;  
         } catch (Exception ex) {  
             //ex.printStackTrace();  
             logger.error("校验码校验异常:" + ex);  
             return false;  
         }  
     }  
     
     /**
      * 校验验证码-验证后不清除session
      * @param validateCode
      * @param session
      * @return
      */
     public boolean validateAuthCode(String validateCode,  HttpSession session) {  
         boolean flag = true;  
         try {  
             imageCaptcha = (ImageCaptcha) session.getAttribute("imageCaptcha");  
             if (imageCaptcha == null) {                  
                 return false;  
             }  
            if(ExStringUtils.isEmpty(validateCode)){
            	return false;
            }
             validateCode = validateCode.toUpperCase();// use upper case for easier usage  
             flag = (imageCaptcha.validateResponse(validateCode)).booleanValue();  
             return flag;  
         } catch (Exception ex) {  
             logger.error("校验码校验异常:" + ex);  
             return false;  
         }  
     }  
     
   
}
