package com.kf.data.parser;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kf.data.fetcher.Fetcher;
import com.kf.data.fetcher.tools.KfConstant;

/***
 * 
 * @Title: CtripBaseParser.java
 * @Package com.kf.data.parser
 * @Description: 携程base parser
 * @author liangyt
 * @date 2017年10月30日 下午7:04:10
 * @version V1.0
 */
public class CtripBaseParser {
	public static Log logger = LogFactory.getLog(CtripBaseParser.class);

	/***
	 * 获取文本
	 * 
	 * @param element
	 * @param csspath
	 * @return
	 */
	public String getElementTextByCssPath(Element element, String csspath) {
		Elements textElements = element.select(csspath);
		if (textElements.size() > 0) {
			return textElements.first().text();
		} else {
			return null;
		}

	}

	/***
	 * 
	 * 
	 * @param object
	 * @param type
	 */
	public static void sendJson(Object object, String type) {
		String url = KfConstant.saveJsonIp;
		Map<String, String> params = new HashMap<String, String>();
		Gson gson = new GsonBuilder().create();
		params.put("json", gson.toJson(object));
		params.put("type", type);
		Fetcher.getInstance().postSave(url, params, null, "utf-8");
	}
}
