package com.kf.data.tianyancha.parser;

import java.net.URLEncoder;
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

import com.kf.data.mybatis.entity.TycCompanyAnnouncementCrawler;

import net.sf.json.JSONObject;

/***
 * 
 * @Title: TianyanchaAnnouncementParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 开庭公告
 * @author liangyt
 * @date 2017年10月11日 下午2:14:10
 * @version V1.0
 */
public class TianyanchaAnnouncementParser extends TianyanchaBasePaser {

	/***
	 * 开庭公告
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void announcementParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_announcementcourt");
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
							WebElement nextPageBt = driver.findElement(By.xpath(
									"//*[@id=\"_container_announcementcourt\"]/div/div[last()]/ul/li[last()]/a"));
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

	/****
	 * 开庭公告解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_announcementcourt");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");

					if (tdElements.size() == 5) {
						String text = tdElements.get(4).select("span").first().attr("onclick");
						text = StringUtils.substringBetween(text, "openAnnouncementPopup({", "})");
						text = "{" + text + "}";
						JSONObject obj = JSONObject.fromObject(text);
						String caseNo = obj.getString("caseNo");
						String caseReason = obj.getString("caseReason");
						String contractors = obj.getString("contractors");
						String court = obj.getString("court");
						String courtRoom = obj.getString("courtroom");
						String defendant = URLEncoder.encode(obj.getJSONArray("defendant").toString(), "utf-8");
						String judge = obj.getString("judge");
						String plaintiff = URLEncoder.encode(obj.getJSONArray("plaintiff").toString(), "utf-8");
						String startDate = obj.getString("startDate");
						TycCompanyAnnouncementCrawler tycCompanyAnnouncementCrawler = new TycCompanyAnnouncementCrawler();
						tycCompanyAnnouncementCrawler.setCaseNo(caseNo);
						tycCompanyAnnouncementCrawler.setCaseReason(caseReason);
						tycCompanyAnnouncementCrawler.setCompanyId(companyId);
						tycCompanyAnnouncementCrawler.setCompanyName(companyName);
						tycCompanyAnnouncementCrawler.setContractors(contractors);
						tycCompanyAnnouncementCrawler.setCourt(court);
						tycCompanyAnnouncementCrawler.setCourtRoom(courtRoom);
						tycCompanyAnnouncementCrawler.setCreatedAt(new Date());
						tycCompanyAnnouncementCrawler.setDefendant(defendant);
						tycCompanyAnnouncementCrawler.setJudge(judge);
						tycCompanyAnnouncementCrawler.setPlaintiff(plaintiff);
						tycCompanyAnnouncementCrawler.setStartDate(startDate);
						tycCompanyAnnouncementCrawler.setStatus((byte) 0);
						tycCompanyAnnouncementCrawler.setUpdatedAt(new Date());
						sendJson(tycCompanyAnnouncementCrawler, "tyc_company_announcement");
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}
}
