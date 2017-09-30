package com.kf.data.tianyancha.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kf.data.fetcher.Fetcher;
import com.kf.data.fetcher.tools.KfConstant;
import com.kf.data.tianyancha.core.TianyanchaCrawler;

/***
 * 
 * @author lyt
 * 
 *         天眼查 base解析
 *
 */
public class TianyanchaBasePaser {

	public final static Logger logger = LoggerFactory.getLogger(TianyanchaCrawler.class);

	public Element getNodeByCssPath(Document document, String cssPath) {
		return getNodeByCssPath(document, cssPath, 0);
	}

	public Element getNodeByCssPath(Document document, String cssPath, int index) {
		Elements elments = document.select(cssPath);
		if (elments.size() != 0) {
			Element element = elments.get(index);
			return element;
		} else {
			return null;
		}
	}

	public Date stringToDate(String time) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;

	}

	public Date timestampToDate(String time) {
		long lt = new Long(time);
		Date date = new Date(lt);
		return date;
	}

	public String dateToString(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = timestampToDate(time);
		String format = sdf.format(date);
		return format;

	}
	public String dateTo8char(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = timestampToDate(time);
		String format = sdf.format(date);
		return format;

	}
	
	

	public void sendJson(Object object, String type) {
		try {
			String url = KfConstant.saveJsonIp;
			Map<String, String> params = new HashMap<String, String>();
			Gson gson = new GsonBuilder().create();
			params.put("json", gson.toJson(object));
			params.put("type", type);
			Fetcher.getInstance().postSave(url, params, null, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
