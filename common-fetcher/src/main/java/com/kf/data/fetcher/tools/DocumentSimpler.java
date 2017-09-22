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

	public Element simpleDocument(Document document) {
		Elements divElements = document.body().children();
		Element newBody = new Element(Tag.valueOf("div"), "");
		for (Element divElement : divElements) {
			if (divElement.tagName().equals("div")) {
				Elements childElements = divElement.children();
				for (Element childElement : childElements) {
					if (childElement.tagName().startsWith("h") || childElement.tagName().equals("b")) {
						Element pElement = new Element(Tag.valueOf("p"), "");
						pElement.text(childElement.text().trim());
						newBody.appendChild(pElement);
					} else if (childElement.tagName().equals("table")) {
						String style = childElement.toString();
						style = style.replace("\r\n", "");
						style = style.replace(" ", "");
						style = style.replace(" ", "");
						if (style.contains("border:solid#5B9BD51.0pt") || style.contains(":solidblack1.0pt")
								|| style.contains("border:solid#5A9AD41.0pt;")
								|| style.contains("border:solid#4F81BD1.0pt")
								|| style.contains("border:solid#9CC2E51.0pt;")
								|| style.contains("border:solid#BDD6EE1.0pt;")
								|| style.contains("border:solid#5B9BD41.0pt")) {
							childElement = new TableRepairer().repairTable(childElement);
							newBody.appendChild(childElement);
						} else {

						}
					} else {
						boolean isHeader = false;
						String childText = childElement.text().trim();
						childText = replacekong(childText);
						if (childText.isEmpty()) {
							continue;
						}
						if (childText.contains(".......")) {
							continue;
						}

						if (childText.length() < 100) {
							for (String titleTag : titleTags) {
								if (childText.contains(titleTag)) {
									Element pElement = new Element(Tag.valueOf("p"), "");
									pElement.text(childElement.text().trim());
									newBody.appendChild(pElement);
									isHeader = true;
									break;
								}
							}
						}
						if (!isHeader) {
							if (childElement.tagName().equals("p")) {
								if (childText.length() >= 3 && childText.length() < 50) {
									Element pElement = new Element(Tag.valueOf("p"), "");
									pElement.text(childElement.text().trim());
									newBody.appendChild(pElement);
								}

							}

						}

					}

				}

			}

		}
		return newBody;
	}

	/***
	 * 
	 * @param preEnd
	 * @return
	 */
	public static String replacekong(String text) {
		text = text.replace("  ", "");
		text = text.replace(" ", "");
		text = text.replace("	", "");
		text = text.replace(" ", "");
		text = text.replace(" ", "");
		text = text.replace("	", "");
		text = text.replace(" ", "");
		text = text.replace(" ", "");
		text = text.replace("&nbsp;", "");
		return text;
	}

	public Element simpleDocument2(Document document) {
		Elements divElements = document.body().children();
		Element newBody = new Element(Tag.valueOf("div"), "");
		for (Element divElement : divElements) {
			if (divElement.tagName().equals("div")) {
				Elements childElements = divElement.children();
				for (Element childElement : childElements) {
					if (childElement.tagName().equals("table")) {
						newBody.appendChild(childElement);
					} else {
						Element pElement = new Element(Tag.valueOf("p"), "");
						pElement.text(childElement.text().trim());
						newBody.appendChild(pElement);
					}
				}

			}

		}
		String newBodyText = newBody.text();
		newBodyText = replacekong(newBodyText);
		if (newBodyText.contains("释义")) {
			Element newBody2 = new Element(Tag.valueOf("div"), "");
			Elements childrens = newBody.children();
			boolean isAppend = false;
			for (Element element : childrens) {
				if (isAppend) {
					if (element.toString().contains(".......")) {
						continue;
					}
					newBody2.appendChild(element);
				} else {
					String text = element.text();
					text = replacekong(text);
					if (text.contains("释义")) {
						if (element.text().contains(".......")) {

						} else {
							isAppend = true;
						}
					}
				}
			}
			return newBody2;
		} else {
			return newBody;
		}

	}
}
