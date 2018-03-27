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

import com.kf.data.mybatis.entity.TycCompanyCaseCrawler;

/***
 * 
 * @Title: TianyanchaCaseParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 法律诉讼
 * @author liangyt
 * @date 2017年10月11日 下午2:14:10
 * @version V1.0
 */
public class TianyanchaCaseParser extends TianyanchaBasePaser {

	/***
	 * 法律诉讼
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void caseParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId, driver);
		int pageIndex = 2;
		int pageNum = 0;
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_lawsuit");
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
									By.xpath("//*[@id=\"_container_lawsuit\"]/div/div[last()]/ul/li[last()]/a"));
							((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageBt);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							document = Jsoup.parse(driver.getPageSource(),driver.getCurrentUrl());
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
	 * 法律诉讼解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId, WebDriver driver) {
		Elements contentNodes = document.select("#_container_lawsuit");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					String judicialDate = tdElements.get(0).text().trim();
					String caseTitle = tdElements.get(1).text().trim();
					String caseReason = tdElements.get(2).text().trim();
					String caseIdentity = tdElements.get(3).text().trim();
					String caseNumber = tdElements.get(4).text().trim();
					TycCompanyCaseCrawler tycCompanyCaseCrawler = new TycCompanyCaseCrawler();
					tycCompanyCaseCrawler.setCaseIdentity(caseIdentity);
					tycCompanyCaseCrawler.setCaseNumber(caseNumber);
					tycCompanyCaseCrawler.setCaseReason(caseReason);
					tycCompanyCaseCrawler.setCaseTitle(caseTitle);
					tycCompanyCaseCrawler.setCompanyId(companyId);
					tycCompanyCaseCrawler.setCompanyName(companyName);
					tycCompanyCaseCrawler.setCreatedAt(new Date());
					tycCompanyCaseCrawler.setJudicialDate(judicialDate);
					tycCompanyCaseCrawler.setStatus((byte) 0);
					tycCompanyCaseCrawler.setUpdatedAt(new Date());
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
						tycCompanyCaseCrawler.setJudicialText(judicialText);
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {

						sendJson(tycCompanyCaseCrawler, "tyc_company_case");
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
