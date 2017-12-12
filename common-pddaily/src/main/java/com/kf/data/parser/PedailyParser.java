package com.kf.data.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PedailyParser {
	
	
	public static String getOrgBySelector(String html,String selector,String key){
		String value = null;
		Elements eles = Jsoup.parse(html).select(selector);
		if(!eles.isEmpty()){
			Element ele = eles.get(0);
			if(ele!=null){
				value = ele.ownText();
				if(StringUtils.isNoneEmpty(value)){ 
					value = value.replace(key, "");
				}
			}
		}
		return value;
	}
	  
	public static String getOrgBySelector(String html,String selector){
		String value = null;
		Elements eles = Jsoup.parse(html).select(selector);
		if(!eles.isEmpty()){
			Element ele = eles.get(0);
			if(ele!=null){
				value = ele.ownText();
			}
		}
		return value;
	}
	
	public static String getImgUrl(String html,String selector){
		String value = null;
		Elements eles = Jsoup.parse(html).select(selector);
		if(!eles.isEmpty()){
			Element ele = eles.get(0);
			if(ele!=null){
				value = ele.attr("src");
			}
		}
		return value;
	}
	
	
	public static String getDescription(String html,String selector){
		String value = "";
		Elements eles = Jsoup.parse(html).select(selector);
		if(!eles.isEmpty()){
			for(Element ele:eles){
				value+=ele.outerHtml();
			}
		}
		
		return value;
	}
	
	public static String getDescription(String html){
		String value = "";
		Elements eles = Jsoup.parse(html).select("div.box-content p");
		if(!eles.isEmpty()){
			for(Element ele:eles){
				value+=ele.outerHtml();
			}
		}
		
		return value;
	}
	
	public static Set<String> getManagerLinks(String html){
		Set<String> links = null;
		Elements eles = Jsoup.parse(html).select("ul.list-pics > li a");
		if(!eles.isEmpty()){
			links = new HashSet<String>();
			for(Element ele:eles){
				String link = ele.attr("href");
				links.add(link);
			}
		}
		return links;
	}
	
	
	
	public static Set<Element> getOrgUrls(String html){
		Set<Element> urls = new HashSet<Element>();
		Elements eles = Jsoup.parse(html).select("a.f16");
		if(!eles.isEmpty()){
			for(Element ele:eles){  
				urls.add(ele);
			}
		}
		
		return urls;
	}
	 
	
	public static String getEventTitle(String html){
		String title = null;
		Elements ele = Jsoup.parse(html).select("div.info > h1");
		if(ele!=null){
			title = ele.text();
		}
		return title;
	}
	
	public static String getValueText(String html,String key){
		String value = ""; 
		Elements ele = Jsoup.parse(html).select("div.info");
		if(ele!=null){ 
			Elements es = ele.select("li:contains("+key+")");
			if(es!=null){
				value = es.text().trim();
				if(StringUtils.isNoneEmpty(value)){ 
					value = value.replace(key, "").trim();
				} 
			}
		}  
		return value;
	}
	
	public static String getValue(String html,String key){
		String value = ""; 
		Elements ele = Jsoup.parse(html).select("div.news-show");
		if(ele!=null){ 
			Element e = ele.select("p:contains("+key+")").first();
			if(e!=null){
				value = e.ownText().trim();
			}
		}  
		return value;
	}
	
	public static String getOrgValue(String html,String key){
		String value = ""; 
		Element ele = Jsoup.parse(html);
		if(ele!=null){
			Element e = ele.select("p:contains("+key+") > a").first();
			if(e!=null){
				value = e.text().trim();
			}
		}  
		return value;
	} 
	
	public static String getDescriptionValue(String html){
		String value = ""; 
		Element ele = Jsoup.parse(html);
		if(ele!=null){
			Elements es = ele.select("#desc > p");
			if(es!=null){ 
				value = es.text().trim();
			}
		}  
		return value;
	} 

}
