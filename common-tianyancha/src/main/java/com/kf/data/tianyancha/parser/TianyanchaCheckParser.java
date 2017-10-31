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

import com.kf.data.mybatis.entity.TycCompanyCheckCrawler;

/***
 * 
 * @Title: TianyanchaCheckParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 抽查检查
 * @author liangyt
 * @date 2017年10月11日 下午2:14:10
 * @version V1.0
 */
public class TianyanchaCheckParser extends TianyanchaBasePaser {

	/***
	 * 抽查检查
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void checkParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_check");
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
							WebElement nextPageBt = driver.findElement(
									By.xpath("//*[@id=\"_container_check\"]/div/div[last()]/ul/li[last()]/a"));
							((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageBt);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							document = Jsoup.parse(driver.getPageSource());
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
	 * 抽查检查
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_check");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					String checkAgency = tdElements.get(3).text().trim();
					String date = tdElements.get(0).text().trim();
					String result = tdElements.get(2).text().trim();
					String type = tdElements.get(1).text().trim();
					TycCompanyCheckCrawler tycCompanyCheckCrawler = new TycCompanyCheckCrawler();
					tycCompanyCheckCrawler.setCheckAgency(checkAgency);
					tycCompanyCheckCrawler.setCompanyId(companyId);
					tycCompanyCheckCrawler.setCompanyName(companyName);
					tycCompanyCheckCrawler.setCreatedAt(new Date());
					tycCompanyCheckCrawler.setDate(date);
					tycCompanyCheckCrawler.setResult(result);
					tycCompanyCheckCrawler.setStatus((byte) 0);
					tycCompanyCheckCrawler.setType(type);
					tycCompanyCheckCrawler.setUpdatedAt(new Date());
					sendJson(tycCompanyCheckCrawler, "tyc_company_check");
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}
}
