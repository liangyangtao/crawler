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

import com.kf.data.mybatis.entity.TycCompanyCoreTeamCrawler;

/****
 * 
 * @Title: TianyanchaCoreTeamParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 核心团队解析
 * @author liangyt
 * @date 2017年9月30日 下午3:57:51
 * @version V1.0
 */
public class TianyanchaCoreTeamParser extends TianyanchaBasePaser {

	/***
	 * 核心团队
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void coreTeamParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_teamMember");
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
									By.xpath("//*[@id=\"_container_teamMember\"]/div/div[last()]/ul/li[last()]/a"));
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
	 * 核心团队解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_teamMember");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".team-item");
			for (Element element : nodes) {
				try {
					Elements imgElements = element.select(".img-outer > img");
					String img = null;
					if (imgElements.size() > 0) {
						img = imgElements.first().attr("src");
					}
					Elements nameElements = element.select(".team-name");
					String name = null;
					if (nameElements.size() > 0) {
						name = nameElements.first().text();
					}
					String title = null;
					Elements titleElements = element.select(".team-title");
					if (titleElements.size() > 0) {
						title = titleElements.first().text();
					}
					String record = null;
					Elements recordElements = element.select(".team-right > ul");
					if (recordElements.size() > 0) {
						record = recordElements.first().text();
					}
					TycCompanyCoreTeamCrawler tycCompanyCoreTeamCrawler = new TycCompanyCoreTeamCrawler();
					tycCompanyCoreTeamCrawler.setCompanyId(companyId);
					tycCompanyCoreTeamCrawler.setCompanyName(companyName);
					tycCompanyCoreTeamCrawler.setCreatedAt(new Date());
					tycCompanyCoreTeamCrawler.setImg(img);
					tycCompanyCoreTeamCrawler.setName(name);
					tycCompanyCoreTeamCrawler.setRecord(record);
					tycCompanyCoreTeamCrawler.setStatus((byte) 0);
					tycCompanyCoreTeamCrawler.setTitle(title);
					tycCompanyCoreTeamCrawler.setUpdatedAt(new Date());
					sendJson(tycCompanyCoreTeamCrawler, "tyc_company_core_team");
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		}

	}

}
