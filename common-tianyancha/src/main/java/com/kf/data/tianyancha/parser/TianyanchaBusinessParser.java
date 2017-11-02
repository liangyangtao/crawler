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

import com.kf.data.mybatis.entity.TycCompanyBusinessCrawler;

/****
 * 
 * @Title: TianyanchaBusinessParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 企业业务解析
 * @author liangyt
 * @date 2017年9月30日 下午3:57:51
 * @version V1.0
 */
public class TianyanchaBusinessParser extends TianyanchaBasePaser {

	/***
	 * 企业业务
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void businessParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_firmProduct");
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
									By.xpath("//*[@id=\"_container_firmProduct\"]/div/div[last()]/ul/li[last()]/a"));
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
	 * 企业业务解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_firmProduct");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".product-item");
			for (Element element : nodes) {
				try {
					Elements logoElements = element.select(".product-left > img");
					String logo = null;
					if (logoElements.size() > 0) {
						logo = logoElements.first().attr("src");
					}
					String product = null;
					Elements productElemens = element.select(".title");
					if (productElemens.size() > 0) {
						product = productElemens.first().text();
					}
					String industry = null;
					Elements industryElements = element.select(".hangye");
					if (industryElements.size() > 0) {
						industry = industryElements.first().text();
					}
					String business = null;
					Elements businessElements = element.select(".yeweu");
					if (businessElements.size() > 0) {
						business = businessElements.first().text();
					}
					TycCompanyBusinessCrawler tycCompanyBusinessCrawler = new TycCompanyBusinessCrawler();
					tycCompanyBusinessCrawler.setBusiness(business);
					tycCompanyBusinessCrawler.setCompanyId(companyId);
					tycCompanyBusinessCrawler.setCompanyName(companyName);
					tycCompanyBusinessCrawler.setCreatedAt(new Date());
					tycCompanyBusinessCrawler.setIndustry(industry);
					tycCompanyBusinessCrawler.setLogo(logo);
					tycCompanyBusinessCrawler.setProduct(product);
					tycCompanyBusinessCrawler.setStatus((byte) 0);
					tycCompanyBusinessCrawler.setUpdatedAt(new Date());
					sendJson(tycCompanyBusinessCrawler, "tyc_company_business");
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		}

	}

}
