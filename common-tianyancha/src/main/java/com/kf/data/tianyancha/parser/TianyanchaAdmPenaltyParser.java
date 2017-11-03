package com.kf.data.tianyancha.parser;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.kf.data.mybatis.entity.TycCompanyAdmPenaltyCrawler;

import net.sf.json.JSONObject;

/***
 * 
 * @Title: TianyanchaAbnormalOperationParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 行政处罚
 * @author liangyt
 * @date 2017年10月11日 下午2:14:10
 * @version V1.0
 */
public class TianyanchaAdmPenaltyParser extends TianyanchaBasePaser {

	/***
	 * 行政处罚
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void admPenaltyParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_punish");
				if (contentNodes.size() > 0) {
					Elements pageElements = contentNodes.first().select(".company_pager");
					if (pageElements.size() > 0) {
						Elements totalElements = pageElements.first().select(".total");
						if (totalElements.size() > 0 && pageIndex == 2) {
							String totalStr = totalElements.first().text().trim();
							totalStr = totalStr.replace("共", "");
							totalStr = totalStr.replace("页", "");
							if (totalStr.isEmpty()) {
								pageNum = 0;
							} else {
								pageNum = Integer.parseInt(totalStr);
							}
						}
						if (pageIndex <= pageNum) {
							Elements liElements = pageElements.select("li");
							if (liElements.size() < 3) {
								break;
							}
							WebElement nextPageBt = driver.findElement(
									By.xpath("//*[@id=\"_container_punish\"]/div/div[last()]/ul/li[last()]/a"));
							((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageBt);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							document = Jsoup.parse(driver.getPageSource(), driver.getCurrentUrl());
							paseNode(document, companyName, companyId);
							if (liElements.last().classNames().contains("disabled")) {
								break;
							}
							pageIndex++;
						} else {
							break;
						}

					} else {
						break;
					}

				} else {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}

	/****
	 * 行政处罚
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_punish");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					if (tdElements.size() == 4) {
						// openPunishPopup({"content":"没收违法所得 11508.65 元;罚款
						// 80000.0
						// 元","regNum":"310108000095592","punishNumber":"浦市监案处字〔2016〕第150201610696号","name":"上海好来喜糖业烟酒有限公司","base":"sh","decisionDate":"2016-07-18","type":"经营者采用财物或者其他手段进行贿赂以销售或者购买商品","legalPersonName":"顾传宏","departmentName":"上海市浦东新区市场监督管理局","publishDate":"2016-07-18"})
						String text = tdElements.get(3).select("span").first().attr("onclick");
						text = StringUtils.substringBetween(text, "openPunishPopup({", "})");
						text = "{" + text + "}";
						JSONObject obj = JSONObject.fromObject(text);
						String agency = obj.getString("departmentName");
						String dtEffective = obj.getString("decisionDate");
						String legalPerson = obj.getString("legalPersonName");
						String orderNumber = obj.getString("punishNumber");
						String penaltyContent = obj.getString("content");
						String type = obj.getString("type");
						TycCompanyAdmPenaltyCrawler tycCompanyAdmPenaltyCrawler = new TycCompanyAdmPenaltyCrawler();
						tycCompanyAdmPenaltyCrawler.setAgency(agency);
						tycCompanyAdmPenaltyCrawler.setCompanyId(companyId);
						tycCompanyAdmPenaltyCrawler.setCompanyName(companyName);
						tycCompanyAdmPenaltyCrawler.setCreatedAt(new Date());
						tycCompanyAdmPenaltyCrawler.setDtEffective(dtEffective);
						tycCompanyAdmPenaltyCrawler.setLegalPerson(legalPerson);
						tycCompanyAdmPenaltyCrawler.setOrderNumber(orderNumber);
						tycCompanyAdmPenaltyCrawler.setPenaltyContent(penaltyContent);
						tycCompanyAdmPenaltyCrawler.setRemark(null);
						tycCompanyAdmPenaltyCrawler.setStatus((byte) 0);
						tycCompanyAdmPenaltyCrawler.setType(type);
						tycCompanyAdmPenaltyCrawler.setUpdatedAt(new Date());
						sendJson(tycCompanyAdmPenaltyCrawler, "tyc_company_adm_penalty");

					}

				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}
}
