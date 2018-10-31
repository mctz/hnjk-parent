package com.hnjk.platform.system.service;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import com.hnjk.core.foundation.utils.ExStringUtils;
/**
 * 天气预报服务
 * <code>WeatherReportService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-5-28 下午04:46:26
 * @see 
 * @version 1.0
 */
public class WeatherReportService {

	/**默认城市天气*/
	public final static String DEFAULT_CITY = "广州";
		
	/**
	 * 获取城市的天气情况
	 * @param city 城市拼音
	 * @return
	 */
	public static String getCityWeather(String city){		
		InputStream inputstream = null;
		InputStreamReader in = null;
		StringBuilder bd = new StringBuilder();
		try {
			if(ExStringUtils.isEmpty(city)){
				city = DEFAULT_CITY;
			}
			
			URL url = new URL("http://weather.china.xappengine.com/api?city="+city);
			inputstream = url.openStream(); 	
		    in =  new InputStreamReader(inputstream,"UTF-8");
		    BufferedReader bfReader = new BufferedReader(in);
		    
		    String line = null;
		     while ((line = bfReader.readLine()) != null) {
		    	 bd.append(line);
		     }
		  		  
			/*
			URL url = new URL("http://www.google.com/ig/api?hl=zh_cn&weather="+city);
		    InputStream inputstream = url.openStream(); 	
		    InputStreamReader in =  new InputStreamReader(inputstream,"GBK");
			Document document  = XmlDom4JUtils.readXML(in);
			
			Element root = document.getRootElement();
			List<Element> list =  XmlDom4JUtils.findElements("forecast_conditions",XmlDom4JUtils.findElement("weather", root));
		
			for(Element e : list){
				Iterator<Element> e1 = e.elementIterator();
				CityWeather cityWeather = new CityWeather();
				while(e1.hasNext()){
					Element e2 = e1.next();
					if(e2.getName().equals("day_of_week")) cityWeather.setDay_of_week(e2.attributeValue("data"));
					if(e2.getName().equals("low")) cityWeather.setLow(e2.attributeValue("data"));
					if(e2.getName().equals("high")) cityWeather.setHigh(e2.attributeValue("data"));
					if(e2.getName().equals("icon")) cityWeather.setIcon(e2.attributeValue("data"));
					if(e2.getName().equals("condition")) cityWeather.setCondition(e2.attributeValue("data"));					
				}			
				clist.add(cityWeather);
			}
			
			*/
		} catch (Exception e) {
			
		}finally{
			try {
				inputstream.close();
				in.close();
			} catch (IOException e) {				
			}
			
		}
	
		return bd.toString();
	}
	
	
	
	/**
	* 测试用
	* @param args
	* @throws Exception
	*/
	public static void main(String[] args) throws Exception {
		getCityWeather("");
	} 
}
