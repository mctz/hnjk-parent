package com.hnjk.platform.system.service;

public class CityWeather implements java.io.Serializable{

	private static final long serialVersionUID = -6167361968423353363L;
	
	private String day_of_week;//礼拜几
	
	private String template;//温度
	
	private String weather;//天气描述
	
	private String icon;//图标
	
	private String icontitle;//标题tips

	/**
	 * @return the day_of_week
	 */
	public String getDay_of_week() {
		return day_of_week;
	}

	/**
	 * @param day_of_week the day_of_week to set
	 */
	public void setDay_of_week(String day_of_week) {
		this.day_of_week = day_of_week;
	}

	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * @return the weather
	 */
	public String getWeather() {
		return weather;
	}

	/**
	 * @param weather the weather to set
	 */
	public void setWeather(String weather) {
		this.weather = weather;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return the icontitle
	 */
	public String getIcontitle() {
		return icontitle;
	}

	/**
	 * @param icontitle the icontitle to set
	 */
	public void setIcontitle(String icontitle) {
		this.icontitle = icontitle;
	}

	
	
	
	
}
