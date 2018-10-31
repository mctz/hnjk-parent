package com.hnjk.security.verifyimage;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.octo.captcha.service.CaptchaServiceException;



/**
 * 校验码图片输出Servlet. <p>
 * 这个Servlet用来在前台生成验证码图片.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-5-13上午10:45:52
 * @see 
 * @version 1.0
 */
public class ImageCaptchaServlet extends HttpServlet {
 
	private static final long serialVersionUID = -7511589752907012076L;

	@Override
	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException,
             IOException {           
         try {  
             CaptchaServiceSingleton.getInstance().writeCaptchaImage( httpServletRequest, httpServletResponse);  
           
         } catch (IllegalArgumentException e) {  
             httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);  
             return;  
         } catch (CaptchaServiceException e) {  
             httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  
             return;  
         }  
     }  
}
