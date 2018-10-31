package com.hnjk.core.support.remoting;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

/**
 * 扩展httpInvokerServiceExporter,增加压缩流功能.
 * @author hzg
 *
 */
public class ExHttpInvokerServiceExporter extends HttpInvokerServiceExporter{
	
	//压缩
	@Override
	protected InputStream decorateInputStream(HttpServletRequest request,	InputStream is) throws IOException {
		if ("gzip".equals(request.getHeader("Compression"))) {//启用gzip压缩	
			return new GZIPInputStream(is);
		}
		else {
			return is;
		}
		
	}
	
	//解压缩	
	@Override
	protected OutputStream decorateOutputStream(HttpServletRequest request,	HttpServletResponse response, OutputStream os) throws IOException {
		if ("gzip".equals(request.getHeader("Compression"))) {//启用gzip压缩
			return new GZIPOutputStream(os);
		}
		else {
			return os;
		}
	}

	
}
