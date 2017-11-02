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

import com.kf.data.mybatis.entity.TycCompanyInvestOutsideCrawler;

/***
 * 
 * @Title: TianyanchaInvestOutSideParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 对外投资解析
 * @author liangyt
 * @date 2017年10月11日 下午2:14:10
 * @version V1.0
 */
public class TianyanchaInvestOutSideParser extends TianyanchaBasePaser {

	/***
	 * 对外投资
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void investAbroadParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_invest");
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
									By.xpath("//*[@id=\"_container_invest\"]/div/div[last()]/ul/li[last()]/a"));
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

				}
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}

	/****
	 * 对外投资解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_invest");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					TycCompanyInvestOutsideCrawler tycCompanyInvestOutsideCrawler = new TycCompanyInvestOutsideCrawler();
					String investCompanyName = tdElements.get(0).text().trim();
					String investMoney = tdElements.get(3).text().trim();
					String investRadio = tdElements.get(4).text().trim();
					String legalRepresentative = tdElements.get(1).text().trim();
					if (legalRepresentative.contains("他有")) {
						legalRepresentative = StringUtils.substringBefore(legalRepresentative, "他有");
					}
					String registeredCapital = tdElements.get(2).text().trim();
					String registerStatus = tdElements.get(6).text().trim();
					String registerTime = tdElements.get(5).text().trim();
					tycCompanyInvestOutsideCrawler.setCompanyId(companyId);
					tycCompanyInvestOutsideCrawler.setCompanyName(companyName);
					tycCompanyInvestOutsideCrawler.setCreatedAt(new Date());
					tycCompanyInvestOutsideCrawler.setInvestCompanyName(investCompanyName);
					tycCompanyInvestOutsideCrawler.setInvestMoney(investMoney);
					tycCompanyInvestOutsideCrawler.setInvestRadio(investRadio);
					tycCompanyInvestOutsideCrawler.setLegalRepresentative(legalRepresentative);
					tycCompanyInvestOutsideCrawler.setRegisteredCapital(registeredCapital);
					tycCompanyInvestOutsideCrawler.setRegisterStatus(registerStatus);
					tycCompanyInvestOutsideCrawler.setRegisterTime(registerTime);
					tycCompanyInvestOutsideCrawler.setStatus((byte) 0);
					tycCompanyInvestOutsideCrawler.setUpdatedAt(new Date());
					sendJson(tycCompanyInvestOutsideCrawler, "tyc_company_invest_outside");

				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}
}
