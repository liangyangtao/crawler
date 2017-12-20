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
import com.kf.data.mybatis.entity.TycCompanyFinancingCrawler;
import com.kf.data.mybatis.entity.TycEventsInvestInvestorsCrawler;

/***
 * 
 * @Title: TianyanchaRongziParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 融资信息解析
 * @author liangyt
 * @date 2017年9月30日 上午10:21:09
 * @version V1.0
 */
public class TianyanchaRongziParser extends TianyanchaBasePaser {

	/***
	 * 融资历史
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void rongziParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_rongzi");
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
									By.xpath("//*[@id=\"_container_rongzi\"]/div/div[last()]/ul/li[last()]/a"));
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
	 * 融资信息解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_rongzi");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					//
					String uuid = UUIDTools.getUUID();
					Elements tdElements = element.select("td");
					String date = tdElements.get(0).text();
					String step = tdElements.get(1).text();
					String valuation = tdElements.get(2).text();
					String investmentAmount = tdElements.get(3).text();
					String investRatio = tdElements.get(4).text();
					String investorName = tdElements.get(5).text();
					String source = tdElements.get(6).text();
					TycCompanyFinancingCrawler tycCompanyFinancingCrawler = new TycCompanyFinancingCrawler();
					tycCompanyFinancingCrawler.setAmount(investmentAmount);
					// 将company id 变成 event id 进行关联
					tycCompanyFinancingCrawler.setCompanyId(uuid);
					tycCompanyFinancingCrawler.setCompanyName(companyName);
					tycCompanyFinancingCrawler.setCreatedAt(new Date());
					tycCompanyFinancingCrawler.setFinancDate(date);
					tycCompanyFinancingCrawler.setInvestors(investorName);
					tycCompanyFinancingCrawler.setProportion(investRatio);
					tycCompanyFinancingCrawler.setRound(step);
					tycCompanyFinancingCrawler.setSource(source);
					tycCompanyFinancingCrawler.setStatus((byte) 0);
					tycCompanyFinancingCrawler.setUpdatedAt(new Date());
					tycCompanyFinancingCrawler.setValuation(valuation);
					sendJson(tycCompanyFinancingCrawler, "tyc_company_financing");
					try {
						try {
							TycEventsInvestInvestorsCrawler tycEventsInvestInvestorsCrawler = new TycEventsInvestInvestorsCrawler();
							tycEventsInvestInvestorsCrawler.setEventId(uuid);
							tycEventsInvestInvestorsCrawler.setEventName(companyName);
							tycEventsInvestInvestorsCrawler.setInvestorName(investorName);
							tycEventsInvestInvestorsCrawler.setCreatedAt(new Date());
							tycEventsInvestInvestorsCrawler.setUpdatedAt(new Date());
							sendJson(tycEventsInvestInvestorsCrawler, "tyc_events_invest_investors");
						} catch (Exception e) {
							e.printStackTrace();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}
