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

import com.kf.data.mybatis.entity.TycCompanyTaxRatingCrawler;

/***
 * 
 * @Title: TianyanchaTaxRatingParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 税务评级
 * @author liangyt
 * @date 2017年10月11日 下午2:14:10
 * @version V1.0
 */
public class TianyanchaTaxRatingParser extends TianyanchaBasePaser {

	/***
	 * 税务评级
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void taxRatingParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_taxcredit");
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
									By.xpath("//*[@id=\"_container_taxcredit\"]/div/div[last()]/ul/li[last()]/a"));
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
	 * 税务评级
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_taxcredit");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					String identifier = tdElements.get(3).text();
					String rating = tdElements.get(1).text();
					String ratingAgency = tdElements.get(4).text();
					String ratingYear = tdElements.get(0).text();
					String type = tdElements.get(2).text();
					TycCompanyTaxRatingCrawler tycCompanyTaxRatingCrawler = new TycCompanyTaxRatingCrawler();
					tycCompanyTaxRatingCrawler.setCompanyId(companyId);
					tycCompanyTaxRatingCrawler.setCompanyName(companyName);
					tycCompanyTaxRatingCrawler.setCreatedAt(new Date());
					tycCompanyTaxRatingCrawler.setIdentifier(identifier);
					tycCompanyTaxRatingCrawler.setRating(rating);
					tycCompanyTaxRatingCrawler.setRatingAgency(ratingAgency);
					tycCompanyTaxRatingCrawler.setRatingYear(ratingYear);
					tycCompanyTaxRatingCrawler.setStatus((byte) 0);
					tycCompanyTaxRatingCrawler.setType(type);
					tycCompanyTaxRatingCrawler.setUpdatedAt(new Date());
					sendJson(tycCompanyTaxRatingCrawler, "tyc_company_tax_rating");
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}
}
