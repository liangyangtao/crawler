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

import com.kf.data.fetcher.tools.UUIDTools;
import com.kf.data.mybatis.entity.TycEventsInvestCrawler;
import com.kf.data.mybatis.entity.TycEventsInvestInvestorsCrawler;

/***
 * 
 * @Title: TianyanchaInvestParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 投资事件
 * @author liangyt
 * @date 2017年10月11日 下午2:14:10
 * @version V1.0
 */
public class TianyanchaInvestParser extends TianyanchaBasePaser {

	/***
	 * 投资事件
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void investParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_touzi");
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
									By.xpath("//*[@id=\"_container_touzi\"]/div/div[last()]/ul/li[last()]/a"));
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
	 * 投资事件解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_touzi");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {

					String uuid = UUIDTools.getUUID();
					Elements tdElements = element.select("td");
					String area = tdElements.get(5).text().trim();
					String business = tdElements.get(7).text().trim();
					String date = tdElements.get(0).text().trim();
					String industry = tdElements.get(6).text().trim();
					String investmentAmount = tdElements.get(2).text().trim();
					Elements imgElements = tdElements.get(4).select("img");
					String productLogo = null;
					if (imgElements.size() > 0) {
						productLogo = imgElements.first().attr("src");
					}
					String step = tdElements.get(1).text().trim();
					String product = tdElements.get(4).text().trim();

					TycEventsInvestCrawler tycEventsInvestCrawler = new TycEventsInvestCrawler();
					tycEventsInvestCrawler.setArea(area);
					tycEventsInvestCrawler.setBusiness(business);
					// 将company id 变成 event id 进行关联
					tycEventsInvestCrawler.setCompanyId(uuid);
					tycEventsInvestCrawler.setCompanyName(companyName);
					tycEventsInvestCrawler.setCreatedAt(new Date());
					tycEventsInvestCrawler.setDate(date);
					tycEventsInvestCrawler.setIndustry(industry);
					tycEventsInvestCrawler.setInvestmentAmount(investmentAmount);
					tycEventsInvestCrawler.setProduct(product);
					tycEventsInvestCrawler.setProductLogo(productLogo);
					tycEventsInvestCrawler.setStatus((byte) 0);
					tycEventsInvestCrawler.setStep(step);
					tycEventsInvestCrawler.setUpdatedAt(new Date());
					sendJson(tycEventsInvestCrawler, "tyc_events_invest");
					try {
						Element investorElement = tdElements.get(3);
						Elements divElements = investorElement.select("div");
						for (Element divElement : divElements) {
							try {
								String eventName = divElement.text();
								TycEventsInvestInvestorsCrawler tycEventsInvestInvestorsCrawler = new TycEventsInvestInvestorsCrawler();
								tycEventsInvestInvestorsCrawler.setEventId(uuid);
								tycEventsInvestInvestorsCrawler.setEventName(companyName);
								tycEventsInvestInvestorsCrawler.setInvestorName(eventName);
								tycEventsInvestInvestorsCrawler.setCreatedAt(new Date());
								tycEventsInvestInvestorsCrawler.setUpdatedAt(new Date());
								sendJson(tycEventsInvestInvestorsCrawler, "tyc_events_invest_investors");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}
}
