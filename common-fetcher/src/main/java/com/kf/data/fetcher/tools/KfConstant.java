package com.kf.data.fetcher.tools;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @Title: KfConstant.java
 * @Package com.kf.data.crawler.tianyancha.tools
 * @Description: 获取配置的服务器地址
 * @author liangyt
 * @date 2017年5月2日 下午6:04:36
 * @version V1.0
 */
public class KfConstant {

	public static String serverIp = "";
	public static String esClusterName = "";
	public static int esPort = 9300;
	public static String esUrl = "";
	public static String esIndexName = "";
	public static String esDataType = "";

	public static String saveJsonIp = "";

	public static String minaServerIp = "";

	public static String emsHost = "";
	public static String emsPort = "";
	public static String emsUsername = "";
	public static String emsPassword = "";
	public static String emsFromAddress = "";
	public static String emsReciver = "";

	public KfConstant() {
		super();
		init();
	}

	public static void init() {
		try {
			String path = KfConstant.class.getClassLoader().getResource("").toURI().getPath();
			File file = new File(path + File.separator + "serverip.xml");
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(file);
			Element rootElement = document.getRootElement();
			serverIp = rootElement.elementTextTrim("serverip");
			esClusterName = rootElement.elementTextTrim("esClusterName").trim();
			esPort = Integer.parseInt(rootElement.elementTextTrim("esPort").trim());
			esUrl = rootElement.elementTextTrim("esUrl").trim();
			esIndexName = rootElement.elementTextTrim("esIndexName").trim();
			esDataType = rootElement.elementTextTrim("esDataType").trim();
			saveJsonIp = rootElement.elementTextTrim("saveJsonIp");
			minaServerIp = rootElement.elementTextTrim("minaServerIp");
			emsHost = rootElement.elementTextTrim("emsHost");
			emsPort = rootElement.elementTextTrim("emsPort");
			emsUsername = rootElement.elementTextTrim("emsUsername");
			emsPassword = rootElement.elementTextTrim("emsPassword");
			emsFromAddress = rootElement.elementTextTrim("emsFromAddress");
			emsReciver = rootElement.elementTextTrim("emsReciver");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
