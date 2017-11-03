package com.kf.data.tianyancha.parser;

import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.kf.data.fetcher.tools.ReportDataFormat;
import com.kf.data.mybatis.entity.TycCompanyCommonstockChangeCrawler;

/***
 * 
 * @Title: TianyanchaCommonstockChangeParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 天眼查股本变动信息解析
 * @author liangyt
 * @date 2017年9月30日 下午3:01:32
 * @version V1.0
 */
public class TianyanchaCommonstockChangeParser extends TianyanchaBasePaser {

	/***
	 * 股本变动
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void commonstockChangeParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_equityChange");
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
									By.xpath("//*[@id=\"_container_equityChange\"]/div/div[last()]/ul/li[last()]/a"));
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

	/***
	 * 天眼查股本变动信息解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_equityChange");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					String date = tdElements.get(0).text();
					String reason = tdElements.get(1).text();
					String aAllEquity = tdElements.get(2).text();
					String aCirculationEquity = tdElements.get(3).text();
					String aLimitEquity = tdElements.get(4).text();
					TycCompanyCommonstockChangeCrawler tycCompanyCommonstockChangeCrawler = new TycCompanyCommonstockChangeCrawler();
					tycCompanyCommonstockChangeCrawler.setaAllEquity(ReportDataFormat.bigUnitChange(aAllEquity));
					tycCompanyCommonstockChangeCrawler
							.setaCirculationEquity(ReportDataFormat.bigUnitChange(aCirculationEquity));
					tycCompanyCommonstockChangeCrawler.setaLimitEquity(ReportDataFormat.bigUnitChange(aLimitEquity));
					tycCompanyCommonstockChangeCrawler.setCompanyId(companyId);
					tycCompanyCommonstockChangeCrawler.setCompanyName(companyName);
					tycCompanyCommonstockChangeCrawler.setCreatedAt(new Date());
					tycCompanyCommonstockChangeCrawler.setDate(stringToDate(date));
					tycCompanyCommonstockChangeCrawler.setReason(reason);
					tycCompanyCommonstockChangeCrawler.setStatus((byte) 0);
					tycCompanyCommonstockChangeCrawler.setUpdatedAt(new Date());
					sendJson(tycCompanyCommonstockChangeCrawler, "tyc_company_commonstock_change");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}
