package com.kf.data.approved.parser;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class CompanyNameParser extends BaseParser {

	/****
	 * 解析挂牌公司名称
	 * 
	 * @param html
	 * @return
	 */
	public String parserCompanyName(String html) {
		Document document = Jsoup.parse(html);
		String content = document.text();
		content = content.replace(" ", "###");
		content = content.replace("	", "###");
		content = content.replace(" ", "###");
		String result = null;
		if (content.contains("关于同意") && content.contains("股票在")) {
			result = StringUtils.substringBetween(content, "关于同意", "股票在");
		} else if (content.contains("关于同意") && content.contains("在全国中小企业")) {
			result = StringUtils.substringBetween(content, "关于同意", "在全国中小企业");
		} else {
			result = StringUtils.substringBefore(content, "：");
			if (result.length() > 60) {
				result = StringUtils.substringBefore(content, ":");
			}
		}
		if (result != null) {
			if (result.contains("你公司报送的")) {
				result = StringUtils.substringBetween(content, "关于同意", "在全国中小企业");
			}
			if (result.contains("你公司报送的")) {
				result = StringUtils.substringBetween(content, "关于同意", "股票");
			}
			if (result.contains("股票")) {
				result = StringUtils.substringBefore(result, "股票");
			}
		}
		return result;
	}
}
