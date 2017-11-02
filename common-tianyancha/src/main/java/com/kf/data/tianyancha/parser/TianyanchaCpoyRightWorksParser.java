package com.kf.data.tianyancha.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.kf.data.mybatis.entity.TycCompanyCopyrightCrawler;

/****
 * 
 * @Title: TianyanchaCpoyRightWorksParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 作品著作权解析
 * @author liangyt
 * @date 2017年9月29日 下午3:07:00
 * @version V1.0
 */
public class TianyanchaCpoyRightWorksParser extends TianyanchaBasePaser {

	/***
	 * 作品著作权
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void cpoyRightWorksParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_copyrightWorks");
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
							// *[@id="_container_copyrightWorks"]/div/div/ul/li[13]/a
							WebElement nextPageBt = driver.findElement(
									By.xpath("//*[@id=\"_container_copyrightWorks\"]/div/div[last()]/ul/li[last()]/a"));
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

	/***
	 * 作品著作权解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_copyrightWorks");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					if (tdElements.size() == 6) {
						String name = tdElements.get(0).text().trim();
						String registration_number = tdElements.get(1).text().trim();
						String type = tdElements.get(2).text().trim();
						String create_date = tdElements.get(3).text().trim();
						create_date = create_date.replace("-", "");
						String register_date = tdElements.get(4).text().trim();
						register_date = register_date.replace("-", "");
						String published_date = tdElements.get(5).text().trim();
						published_date = published_date.replace("-", "");
						TycCompanyCopyrightCrawler tycCompanyCopyrightCrawler = new TycCompanyCopyrightCrawler();
						tycCompanyCopyrightCrawler.setCompanyId(companyId);
						tycCompanyCopyrightCrawler.setCompanyName(companyName);
						tycCompanyCopyrightCrawler.setRegisterDate(register_date);
						tycCompanyCopyrightCrawler.setPublishedDate(published_date);
						tycCompanyCopyrightCrawler.setName(name);
						tycCompanyCopyrightCrawler.setShortname(name);
						tycCompanyCopyrightCrawler.setRegistrationNumber(registration_number);
						tycCompanyCopyrightCrawler.setType(type);
						tycCompanyCopyrightCrawler.setCreateDate(create_date);
						tycCompanyCopyrightCrawler.setStatus(false);
						sendJson(tycCompanyCopyrightCrawler, "tyc_company_copyright");
					}

				} catch (Exception e) {
					continue;
				}

			}
		}

	}

}