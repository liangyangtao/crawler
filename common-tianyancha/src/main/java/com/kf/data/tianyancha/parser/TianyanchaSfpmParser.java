package com.kf.data.tianyancha.parser;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.kf.data.fetcher.tools.ReportDataFormat;
import com.kf.data.mybatis.entity.TycCompanySfpmCrawler;

/****
 * 
 * @Title: TianyanchaSfpmParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 司法拍卖信息解析
 * @author liangyt
 * @date 2017年9月30日 下午3:57:51
 * @version V1.0
 */
public class TianyanchaSfpmParser extends TianyanchaBasePaser {

	/***
	 * 司法拍卖
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void sfpmParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_judicialSale");
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
							// *[@id="_container_wechat"]/div/div[11]/ul/li[5]/a
							WebElement nextPageBt = driver.findElement(
									By.xpath("//*[@id=\"_container_judicialSale\"]/div/div[last()]/ul/li[last()]/a"));
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
	 * 司法拍卖信息解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_judicialSale");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					Elements tdElements = element.select("td");
					Elements aElements = tdElements.get(0).select("a");
					String auctionName = aElements.first().text();
					String detailLink = aElements.first().absUrl("href");
					String auctionDate = tdElements.get(1).text();
					String auctionOrg = tdElements.get(2).text();
					Elements threeElements = tdElements.get(3).select("div");
					String goods = null;
					String startPrice = null;
					String valuation = null;
					if (threeElements.size() == 3) {
						goods = threeElements.first().text();
						startPrice = threeElements.get(1).text();
						startPrice = startPrice.replace("起拍价格：", "");
						valuation = threeElements.get(2).text();
						valuation = valuation.replace("评估价格：", "");
					}

					TycCompanySfpmCrawler tycCompanySfpmCrawler = new TycCompanySfpmCrawler();
					try {
						tycCompanySfpmCrawler.setAuctionDate(sdf.parse(auctionDate));
					} catch (Exception e) {
						e.printStackTrace();
						try {
							sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							tycCompanySfpmCrawler.setAuctionDate(sdf.parse(auctionDate));
						} catch (Exception e1) {
							// TODO: handle exception
						}
					}
					tycCompanySfpmCrawler.setAuctionName(auctionName);
					tycCompanySfpmCrawler.setAuctionOrg(auctionOrg);
					tycCompanySfpmCrawler.setCompanyId(companyId);
					tycCompanySfpmCrawler.setCompanyName(companyName);
					tycCompanySfpmCrawler.setCreatedAt(new Date());
					tycCompanySfpmCrawler.setDetailLink(detailLink);
					tycCompanySfpmCrawler.setGoods(goods);
					tycCompanySfpmCrawler.setStartPrice(ReportDataFormat.bigUnitChange(startPrice));
					tycCompanySfpmCrawler.setStatus((byte) 0);
					tycCompanySfpmCrawler.setUpdatedAt(new Date());
					tycCompanySfpmCrawler.setValuation(ReportDataFormat.bigUnitChange(valuation));
					sendJson(tycCompanySfpmCrawler, "tyc_company_sfpm");
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		}

	}

}
