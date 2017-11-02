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

import com.kf.data.fetcher.tools.AliOssSender;
import com.kf.data.mybatis.entity.TycCompanyTrademarkCrawler;

/***
 * 
 * @Title: TianyanchaTmParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 商标信息解析
 * @author liangyt
 * @date 2017年10月11日 下午2:17:39
 * @version V1.0
 */
public class TianyanchaTmParser extends TianyanchaBasePaser {

	/***
	 * 商标信息
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void tmParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_tmInfo");
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
							// *[@id="_container_tmInfo"]/div/div/div[2]/ul/li[13]/a
							// *[@id="_container_tmInfo"]/div/div/div[2]/ul/li[13]/a
							// *[@id="_container_tmInfo"]/div/div/div[2]/ul/li[13]/a
							try {
								WebElement nextPageBt = driver.findElement(
										By.xpath("//*[@id=\"_container_tmInfo\"]/div/div/div[last()]/ul/li[last()]/a"));
								((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageBt);
							} catch (Exception e) {
								e.printStackTrace();
								break;
							}
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
	 * 商标信息解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {

		Elements contentNodes = document.select("#_container_tmInfo");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");

					TycCompanyTrademarkCrawler tycCompanyTrademarkCrawler = new TycCompanyTrademarkCrawler();

					String trademark_type = tdElements.get(4).text();

					String application_date = tdElements.get(0).text();
					application_date = application_date.replace("-", "");
					String trademark_status = null;
					try {
						trademark_status = tdElements.get(5).text();
					} catch (Exception e) {
					}
					String registration_number = tdElements.get(3).text();
					String trademark_name = null;
					try {
						trademark_name = tdElements.get(2).text();
					} catch (Exception e) {
					}
					String trademark_img_url = tdElements.get(1).select("img").first().attr("src");
					String kfImgUrl = new AliOssSender().uploadObject(trademark_img_url);
					if (kfImgUrl.startsWith("https://") || kfImgUrl.startsWith("http://")) {

					} else {
						kfImgUrl = "https:" + kfImgUrl;
					}
					tycCompanyTrademarkCrawler.setCreatedAt(new Date());
					tycCompanyTrademarkCrawler.setCompanyId(companyId);
					tycCompanyTrademarkCrawler.setCompanyName(companyName);
					tycCompanyTrademarkCrawler.setTrademarkType(trademark_type);
					tycCompanyTrademarkCrawler.setApplicationDate(application_date);
					tycCompanyTrademarkCrawler.setTrademarkStatus(trademark_status);
					tycCompanyTrademarkCrawler.setRegistrationNumber(registration_number);
					// tycCompanyTrademarkCrawler.setTrademarkName(trademark_name);
					tycCompanyTrademarkCrawler.setTrademarkImgUrl(trademark_img_url);
					// tycCompanyTrademarkCrawler.setKfImgUrl(kfImgUrl);
					tycCompanyTrademarkCrawler.setTrademarkId("trademark_id");
					tycCompanyTrademarkCrawler.setStatus(false);
					sendJson(tycCompanyTrademarkCrawler, "tyc_company_trademark");
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}

}
