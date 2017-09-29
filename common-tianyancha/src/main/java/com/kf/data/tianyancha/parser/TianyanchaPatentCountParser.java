package com.kf.data.tianyancha.parser;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.mybatis.entity.TycCompanyPatentCrawlerWithBLOBs;

import net.sf.json.JSONObject;

/**
 * @TianyanchaPatentCountParser.java
 * @2017年4月22日
 * @author yinxin
 * @TianyanchaPatentCountParser
 * @注释：专利信息
 */
public class TianyanchaPatentCountParser extends TianyanchaBasePaser {

	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_patent");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					if (tdElements.size() == 5) {
						String text = tdElements.get(4).select("span").first().attr("onclick");
						text = StringUtils.substringBetween(text, "openPatentPopup({", "})");
						text = "{" + text + "}";
						JSONObject obj = JSONObject.fromObject(text);
						String classification_main_number = obj.getString("mainCatNum");
						String publish_number = obj.getString("applicationPublishNum");
						String proxy_agency = "";
						try {
							proxy_agency = obj.getString("agency");
						} catch (Exception e) {
							logger.info(companyName + "agency 不存在");
						}
						String inventor = obj.getString("inventor");
						String agent = "";
						try {
							agent = obj.getString("agent");
						} catch (Exception e) {
							logger.info(companyName + "agent 不存在");
							// e.printStackTrace();
						}
						String application_number = obj.getString("patentNum");
						String classification_number = obj.getString("allCatNum");
						String patent_name = obj.getString("patentName");
						String summary = obj.getString("abstracts");
						String address = obj.getString("address");
						String application_date = obj.getString("applicationTime");
						String patent_type = obj.getString("patentType");
						String applicant = obj.getString("applicantName");
						String publish_date = obj.getString("applicationPublishTime");
						publish_date = publish_date.replace("-", "");
						TycCompanyPatentCrawlerWithBLOBs tycCompanyPatentCrawler = new TycCompanyPatentCrawlerWithBLOBs();
						tycCompanyPatentCrawler.setCompanyId(companyId);
						tycCompanyPatentCrawler.setCompanyName(companyName);
						tycCompanyPatentCrawler.setSummary(summary);
						tycCompanyPatentCrawler.setCreatedAt(new Date());
						tycCompanyPatentCrawler.setClassificationMainNumber(classification_main_number);
						tycCompanyPatentCrawler.setClassificationNumber(classification_number);
						tycCompanyPatentCrawler.setPublishNumber(publish_number);
						tycCompanyPatentCrawler.setProxyAgency(proxy_agency);
						tycCompanyPatentCrawler.setInventor(inventor);
						tycCompanyPatentCrawler.setAgent(agent);
						tycCompanyPatentCrawler.setApplicationNumber(application_number);
						tycCompanyPatentCrawler.setPatentName(patent_name);
						tycCompanyPatentCrawler.setAddress(address);
						tycCompanyPatentCrawler
								.setApplicationDate(Integer.parseInt(stringToDate(application_date).getTime() + ""));
						tycCompanyPatentCrawler.setPatentType(patent_type);
						tycCompanyPatentCrawler.setApplicant(applicant);
						tycCompanyPatentCrawler.setPublishDate(publish_date);
						sendJson(tycCompanyPatentCrawler, "tyc_company_patent");
					}

				} catch (Exception e) {
					continue;
				}

			}
		}

	}

}
