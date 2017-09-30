package com.kf.data.fetcher.tools;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

/***
 * 
 * @Title: DocumentSimpler.java
 * @Package com.kf.data.crawler.tools
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017年7月3日 下午5:29:42
 * @version V1.0
 */
public class DocumentSimpler {

	static String[] titleTags = new String[] { "一)", "二)", "三)", "四)", "五)", "六)", "七)", "八)", "九)", "一）", "二）", "三）",
			"四）", "五）", "六）", "七）", "八）", "九）", "1)", "2)", "3)", "4)", "5)", "6)", "7)", "8)", "9)", "1）", "2）", "3）",
			"4）", "5）", "6）", "7）", "8）", "9）", "第一节", "第二节", "第三节", "第四节", "第五节", "第六节", "第七节", "第八节", "第九节", "第十节",
			"第十一节", "第十二节", "第十三节", "第十四节", "第十五节", "一、", "二、", "三、", "四、", "五、", "六、", "七、", "八、", "九、", "十、" };

	public Document simpleDocument(Document document) {
		Elements divElements = document.body().children();
		Element newBody = new Element(Tag.valueOf("div"), "");
		for (Element divElement : divElements) {
			Elements childElements = divElement.children();
			for (Element childElement : childElements) {
				if (childElement.tagName().equals("table")) {
					childElement = new TableRepairer().repairTable(childElement);
					newBody.appendChild(childElement);
				} else if (childElement.tagName().equals("w:sdt")) {
					Elements pElements = childElement.children();
					for (Element element : pElements) {
						if (element.tagName().equals("table")) {
							element = new TableRepairer().repairTable(element);
							newBody.appendChild(element);
						} else {
							Element pElement = new Element(Tag.valueOf("p"), "");
							pElement.text(element.text().trim());
							newBody.appendChild(pElement);
						}
					}

				} else if (childElement.tagName().equals("div")) {
					Elements pElements = childElement.children();
					for (Element element : pElements) {
						if (element.tagName().equals("table")) {
							element = new TableRepairer().repairTable(element);
							newBody.appendChild(element);
						} else {
							Element pElement = new Element(Tag.valueOf("p"), "");
							pElement.text(element.text().trim());
							newBody.appendChild(pElement);
						}
					}

				} else {
					Element pElement = new Element(Tag.valueOf("p"), "");
					pElement.text(childElement.text().trim());
					newBody.appendChild(pElement);
				}
			}

		}
		document.empty();
		document = null;
		Document newdocument = new Document("");
		newdocument.appendChild(newBody);
		return newdocument;
	}

}
