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

import com.kf.data.mybatis.entity.TycCompanyChangeCrawler;

/***
 * 
 * @Title: TianyanchaChangeParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 天眼查变更信息解析
 * @author liangyt
 * @date 2017年10月11日 下午2:08:11
 * @version V1.0
 */
public class TianyanchaChangeParser extends TianyanchaBasePaser {

	/****
	 * 变更信息解析
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void changeParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_changeinfo");
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
							// *[@id="_container_changeinfo"]/div/div[2]/ul/li[6]/a
							// *[@id="_container_changeinfo"]/div/div[2]/ul/li[4]/a
							WebElement nextPageBt = driver.findElement(
									By.xpath("//*[@id=\"_container_changeinfo\"]/div/div[last()]/ul/li[last()]/a"));
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
	 * 天眼查变更信息解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {

		Elements contentNodes = document.select("#_container_changeinfo");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");

					String changeDate = tdElements.get(0).text();
					changeDate = changeDate.replace("-", "");
					String changeProject = null;
					try {
						changeProject = tdElements.get(1).text();
					} catch (Exception e) {
					}
					String changeFirst = tdElements.get(2).text();
					changeFirst = Jsoup.parse(changeFirst).text();
					changeFirst = changeFirst.replace("<em>", "");
					changeFirst = changeFirst.replace("</em>", "");

					TycCompanyChangeCrawler tycCompanyChange = new TycCompanyChangeCrawler();
					String changeBack = tdElements.get(3).text();
					changeBack = Jsoup.parse(changeBack).text();
					changeBack = changeBack.replace("<em>", "");
					changeBack = changeBack.replace("</em>", "");
					tycCompanyChange.setChangeBack(changeBack);
					tycCompanyChange.setChangeDate(changeDate);
					// tycCompanyChange.setChangeDateMachine(null);
					tycCompanyChange.setChangeFirst(changeFirst);
					tycCompanyChange.setChangeProject(changeProject);
					tycCompanyChange.setCompanyId(companyId);
					tycCompanyChange.setCompanyName(companyName);
					tycCompanyChange.setCreatedAt(new Date());
					// tycCompanyChange.setId(company.getId());
					tycCompanyChange.setStatus(false);
					tycCompanyChange.setUpdatedAt(new Date());
					sendJson(tycCompanyChange, "tyc_company_change");
				} catch (Exception e) {
					continue;
				}
			}
		}
	}

}
