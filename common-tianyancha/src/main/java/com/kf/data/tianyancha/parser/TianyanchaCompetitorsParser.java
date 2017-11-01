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

import com.kf.data.mybatis.entity.TycCompanyCompetitorsCrawler;

/****
 * 
 * @Title: TianyanchaCompetitorsParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 竞品信息解析
 * @author liangyt
 * @date 2017年9月30日 下午3:57:51
 * @version V1.0
 */
public class TianyanchaCompetitorsParser extends TianyanchaBasePaser {

	/***
	 * 竞品信息解析
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void competitorsParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 竞品信息解析 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_jingpin");
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
									By.xpath("//*[@id=\"_container_jingpin\"]/div/div[last()]/ul/li[last()]/a"));
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

	/***
	 * 竞品信息解析解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_jingpin");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {

					Elements tdElements = element.select("td");
					String competitorName = tdElements.get(0).text().trim();
					String competitorLogo = null;
					Elements imgElements = tdElements.first().select("img");
					if (imgElements.size() > 0) {
						imgElements.first().attr("src");
					}
					String area = tdElements.get(1).text().trim();
					String turn = tdElements.get(2).text().trim();
					String industry = tdElements.get(3).text().trim();
					String scope = tdElements.get(4).text().trim();
					String regtime = tdElements.get(5).text().trim();
					String assessmentValue = tdElements.get(6).text().trim();
					TycCompanyCompetitorsCrawler tycCompanyCompetitorsCrawler = new TycCompanyCompetitorsCrawler();
					tycCompanyCompetitorsCrawler.setArea(area);
					tycCompanyCompetitorsCrawler.setAssessmentValue(assessmentValue);
					tycCompanyCompetitorsCrawler.setCompanyId(companyId);
					tycCompanyCompetitorsCrawler.setCompanyName(companyName);
					tycCompanyCompetitorsCrawler.setCompetitorLogo(competitorLogo);
					tycCompanyCompetitorsCrawler.setCompetitorName(competitorName);
					tycCompanyCompetitorsCrawler.setCreatedAt(new Date());
					tycCompanyCompetitorsCrawler.setIndustry(industry);
					tycCompanyCompetitorsCrawler.setRegtime(regtime);
					tycCompanyCompetitorsCrawler.setScope(scope);
					tycCompanyCompetitorsCrawler.setStatus((byte) 0);
					tycCompanyCompetitorsCrawler.setTurn(turn);
					tycCompanyCompetitorsCrawler.setUpdatedAt(new Date());
					sendJson(tycCompanyCompetitorsCrawler, "tyc_company_competitors");
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		}

	}

}
