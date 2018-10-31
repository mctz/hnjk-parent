package com.hnjk.core.foundation.m3u8.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import lombok.Cleanup;


/** 
 * @author Zik, 广东学苑教育发展有限公司
 * @since Aug 30, 2016 11:46:04 AM 
 * 
 */
public class PlayerM3U8Uitls {

	public static int downloadM3u8(String url) {
		int totalTime = 0;
		do{
			if(ExStringUtils.isEmpty(url) || !(url.lastIndexOf(".")>-1 && "m3u8".equals(url.substring(url.lastIndexOf(".")+1).toLowerCase()))){
				continue;
			}
			URI uri = URI.create(url);
			try {
				@Cleanup InputStream in = uri.toURL().openStream();
				Playlist playList = Playlist.parse(in);
//				System.out.println(playList.isRate() ? "TRUE" : "FLASH");
				String newPath = "";
				if (playList.isRate()) {
					boolean h = false, m = false, l = false;
					String hStr = "", mStr = "", lStr = "";
					List<Element> es = playList.getElements();
					for (int i = 0; i < es.size(); i++) {
						if (es.get(i).getRate() == Element.HIGHT_RATE) {
							h = true;
							hStr = es.get(i).getURI().toString();
						} else if (es.get(i).getRate() == Element.MIDDLE_RATE) {
							m = true;
							mStr = es.get(i).getURI().toString();
						} else if (es.get(i).getRate() == Element.LOW_RATE) {
							l = true;
							lStr = es.get(i).getURI().toString();
						}
					}
					if (l) {
						newPath = lStr;
					} else if (m) {
						newPath = mStr;
					} else if (h) {
						newPath = hStr;
					}
					
					//相对地址url拼接
					if(!newPath.startsWith("http://") && !newPath.startsWith("https://")){
						String uriPrefix = getUriPrefix(url); 
						newPath = uriPrefix + "/" + newPath;
					}
					uri = URI.create(newPath);
					in = uri.toURL().openStream();
					playList = Playlist.parse(in);
				}
				List<Element> list = playList.getElements();
				if(ExCollectionUtils.isNotEmpty(list)){
					for(Element e : list){
						totalTime += e.getDuration();
					}
				}
//				return writeM3u8(list, -1, url);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while(false);
		return totalTime;
	}
	
	private static String getUriPrefix(String uri){
		int index = uri.lastIndexOf("/");
		if(index > 0){
			return uri.substring(0, index);
		}
		return uri;
	}

	public static void main(String[] args) {
		String url = "http://202.192.18.50/feonline/4447/V/V44470101/V44470101.m3u8";
//		String url = "http://cs.scutde.net/t16courses/1622-hjgvtc0qve/video/150/v021000.asf";
		System.out.println("-----"+PlayerM3U8Uitls.downloadM3u8(url));
	}
}
