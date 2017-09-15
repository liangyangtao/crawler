package com.kf.data.fetcher.tools;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

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

	public KfConstant() {
		super();
		init();
	}

	public static void init() {
		try {
			String path = KfConstant.class.getClassLoader().getResource("").toURI().getPath();

			File f = new File(path + File.separator + "serverip.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(f);

			NodeList serverNode = doc.getElementsByTagName("root");
			for (int i = 0; i < serverNode.getLength(); i++) {
				serverIp = doc.getElementsByTagName("serverip").item(i).getFirstChild().getNodeValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
