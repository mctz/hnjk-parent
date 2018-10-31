package com.hnjk.core.foundation.utils;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

/**
 * 日期绑定器，目前支持 yyyy-MM-dd ，yyyy-MM-dd HH:mm ，yyyy-MM-dd HH:mm:ss
 * 
 * @author 广东学苑教育发展有限公司.
 * @since:2009-6-12下午02:41:54
 * @see 
 * @version 1.0
 */
public class CustomDateEditor extends PropertyEditorSupport {

	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private DateFormat dateTimeShortFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	private final boolean allowEmpty;

	public CustomDateEditor(boolean allowEmpty) {
		this.allowEmpty = allowEmpty;
	}

	/**
	 * 根据格式绑定数据
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			setValue(null);
		} else {
			try {
				text = text.trim();
				if (text.length() == 10) {
					this.dateFormat.setLenient(false);
					setValue(this.dateFormat.parse(text));
				} else if (text.length() == 16) {
					this.dateFormat.setLenient(false);
					setValue(this.dateTimeShortFormat.parse(text));
				} else if (text.length() == 19) {
					setValue(this.dateTimeFormat.parse(text));
				}else if(text.length() == 8){
					this.dateFormat.setLenient(false);
					setValue(this.timeFormat.parse(text));
				} else {
					throw new RuntimeException("日期格式不对，必须为：yyyy-MM-dd或者yyyy-MM-dd HH:mm:ss");
				}
			} catch (ParseException ex) {
				IllegalArgumentException iae = new IllegalArgumentException(
						"日期格式不对，必须为：yyyy-MM-dd或者yyyy-MM-dd HH:mm:ss");
				iae.initCause(ex);
				throw iae;
			}
		}
	}

	/**
	 * Format the Date as String, using the specified DateFormat.
	 */
	@Override
	public String getAsText() {
		Date value = (Date) getValue();
		return (value != null ? this.dateFormat.format(value) : "");
	}

}
