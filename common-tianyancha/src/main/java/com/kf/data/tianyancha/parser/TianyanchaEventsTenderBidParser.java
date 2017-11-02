package com.kf.data.tianyancha.parser;

import java.util.Date;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.kf.data.mybatis.entity.TycEventsTenderBidCrawler;

/***
 * 
 * @Title: TianyanchaCaseParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 招投标
 * @author liangyt
 * @date 2017年10月11日 下午2:14:10
 * @version V1.0
 */
public class TianyanchaEventsTenderBidParser extends TianyanchaBasePaser {

	/***
	 * 招投标
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void eventsTenderBidParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId, driver);
		int pageIndex = 2;
		int pageNum = 0;
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_bid");
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
									By.xpath("//*[@id=\"_container_bid\"]/div/div[last()]/ul/li[last()]/a"));
							((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageBt);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							document = Jsoup.parse(driver.getPageSource());
							paseNode(document, companyName, companyId, driver);
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
	 * 招投标解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId, WebDriver driver) {
		Elements contentNodes = document.select("#_container_bid");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					String buyer = tdElements.get(2).text().trim();
					String date = tdElements.get(0).text().trim();
					String title = tdElements.get(1).text().trim();

					TycEventsTenderBidCrawler tycEventsTenderBidCrawler = new TycEventsTenderBidCrawler();

					tycEventsTenderBidCrawler.setBuyer(buyer);
					tycEventsTenderBidCrawler.setCompanyId(companyId);
					tycEventsTenderBidCrawler.setCompanyName(companyName);

					tycEventsTenderBidCrawler.setCreatedAt(new Date());
					tycEventsTenderBidCrawler.setDate(date);
					tycEventsTenderBidCrawler.setStatus((byte) 0);
					tycEventsTenderBidCrawler.setTitle(title);
					tycEventsTenderBidCrawler.setUpdatedAt(new Date());
					String currenWindow = driver.getWindowHandle();
					try {
						Element linkElement = tdElements.get(1).select("a").first();
						String reportLink = linkElement.absUrl("href");
						JavascriptExecutor executor = (JavascriptExecutor) driver;
						executor.executeScript("window.open('" + reportLink + "')");
						Set<String> allWindows = driver.getWindowHandles();
						for (String string : allWindows) {
							if (string.equals(currenWindow)) {
								continue;
							} else {
								driver.switchTo().window(string);
								try {
									Thread.sleep(5000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								if (driver.getCurrentUrl().equals(reportLink)) {
									break;
								}
							}
						}
						String reportHtml = driver.getPageSource();
						Document reportDocument = Jsoup.parse(reportHtml, reportLink);
						String judicialText = reportDocument.select(".lawsuit").toString();
						tycEventsTenderBidCrawler.setContent(judicialText);
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						sendJson(tycEventsTenderBidCrawler, "tyc_events_tender_bid");
						driver.close();
						driver.switchTo().window(currenWindow);
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}
}
