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

import com.kf.data.mybatis.entity.TycCompanyProductCrawler;

import net.sf.json.JSONObject;

/***
 * 
 * @Title: TianyanchaAbnormalOperationParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 产品信息
 * @author liangyt
 * @date 2017年10月11日 下午2:14:10
 * @version V1.0
 */
public class TianyanchaProductParser extends TianyanchaBasePaser {

	/***
	 * 产品信息
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void productParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_product");
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
									By.xpath("//*[@id=\"_container_product\"]/div/div[last()]/ul/li[last()]/a"));
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
	 * 产品信息解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_product");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					if (tdElements.size() == 6) {
						String text = tdElements.get(5).select("script").first().toString();
						text = StringUtils.substringBetween(text, "<script type=\"text/html\">", "</script>");
						JSONObject obj = JSONObject.fromObject(text);
						String area = obj.getString("classes");
						String description = obj.getString("brief");
						String productLogo = obj.getString("icon");
						String productShortname = obj.getString("filterName");
						String productType = obj.getString("type");
						String productName = obj.getString("name");
						TycCompanyProductCrawler tycCompanyProductCrawler = new TycCompanyProductCrawler();
						tycCompanyProductCrawler.setArea(area);
						tycCompanyProductCrawler.setCompanyId(companyId);
						tycCompanyProductCrawler.setCompanyName(companyName);
						tycCompanyProductCrawler.setCreatedAt(new Date());
						tycCompanyProductCrawler.setDescription(description);
						tycCompanyProductCrawler.setProductLogo(productLogo);
						tycCompanyProductCrawler.setProductName(productName);
						tycCompanyProductCrawler.setProductShortname(productShortname);
						tycCompanyProductCrawler.setProductType(productType);
						tycCompanyProductCrawler.setStatus((byte) 0);
						tycCompanyProductCrawler.setUpdatedAt(new Date());
						sendJson(tycCompanyProductCrawler, "tyc_company_product");
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}
}
