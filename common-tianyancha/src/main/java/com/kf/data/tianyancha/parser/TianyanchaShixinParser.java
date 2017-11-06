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

import com.kf.data.mybatis.entity.TycCompanyShixinCrawler;

import net.sf.json.JSONObject;

/****
 * 
 * @Title: TianyanchaShixinParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 失信人
 * @author liangyt
 * @date 2017年11月1日 上午11:39:30
 * @version V1.0
 */
public class TianyanchaShixinParser extends TianyanchaBasePaser {

	/***
	 * 失信人
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void shixinParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_dishonest");
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
									By.xpath("//*[@id=\"_container_dishonest\"]/div/div[last()]/ul/li[last()]/a"));
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

	/***
	 * 失信人解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_dishonest");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					if (tdElements.size() == 6) {
						// Element spanElement =
						// tdElements.get(5).select("span").first();
						// String text = spanElement.attr("onclick");
						// text = StringUtils.substringBetween(text,
						// "openDishonestPopup({", "})");
						// text = "{" + text + "}";
						// JSONObject obj = JSONObject.fromObject(text);

						String text = tdElements.get(5).select("script").first().toString();
						text = StringUtils.substringBetween(text, "<script type=\"text/html\">", "</script>");
						JSONObject obj = JSONObject.fromObject(text);
						String iname = null;
						try {
							iname = obj.getString("iname");
						} catch (Exception e) {
						}
						String businessentity = null;
						try {
							businessentity = obj.getString("businessentity");
						} catch (Exception e) {
						}
						String gistid = null;
						try {
							gistid = obj.getString("gistid");
						} catch (Exception e) {
						}
						String areaname = null;
						try {
							areaname = obj.getString("areaname");
						} catch (Exception e) {
						}
						String cardnum = null;
						try {
							cardnum = obj.getString("cardnum");
						} catch (Exception e) {
						}
						String courtname = null;
						try {
							courtname = obj.getString("courtname");
						} catch (Exception e) {
						}
						String type = null;
						try {
							type = obj.getString("type");
						} catch (Exception e) {
						}
						String publishdate = null;
						try {
							publishdate = obj.getString("publishdate");
						} catch (Exception e) {
						}
						String gistunit = null;
						try {
							gistunit = obj.getString("gistunit");
						} catch (Exception e) {
						}
						String duty = null;
						try {
							duty = obj.getString("duty");
						} catch (Exception e) {
						}

						String performance = null;
						try {
							performance = obj.getString("performance");
						} catch (Exception e) {
						}
						String regdate = null;
						try {
							regdate = obj.getString("regdate");
						} catch (Exception e) {
						}
						String disrupttypename = null;
						try {
							disrupttypename = obj.getString("disrupttypename");
						} catch (Exception e) {
						}
						String casecode = null;
						try {
							casecode = obj.getString("casecode");
						} catch (Exception e) {
						}
						TycCompanyShixinCrawler tycCompanyShixinCrawler = new TycCompanyShixinCrawler();
						tycCompanyShixinCrawler.setAreaname(areaname);
						tycCompanyShixinCrawler.setBusinessentity(businessentity);
						tycCompanyShixinCrawler.setCardnum(cardnum);
						tycCompanyShixinCrawler.setCasecode(casecode);
						tycCompanyShixinCrawler.setCompanyId(companyId);
						tycCompanyShixinCrawler.setCompanyName(companyName);
						tycCompanyShixinCrawler.setCourtname(courtname);
						tycCompanyShixinCrawler.setCreatedAt(new Date());
						tycCompanyShixinCrawler.setDisrupttypename(disrupttypename);
						tycCompanyShixinCrawler.setDuty(duty);
						tycCompanyShixinCrawler.setGistid(gistid);
						tycCompanyShixinCrawler.setGistunit(gistunit);
						tycCompanyShixinCrawler.setIname(iname);
						tycCompanyShixinCrawler.setPerformance(performance);
						tycCompanyShixinCrawler.setPublishdate(publishdate);
						tycCompanyShixinCrawler.setRegdate(regdate);
						tycCompanyShixinCrawler.setStatus((byte) 0);
						tycCompanyShixinCrawler.setType(type);
						tycCompanyShixinCrawler.setUpdatedAt(new Date());
						sendJson(tycCompanyShixinCrawler, "tyc_company_shixin");
					}

				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		}

	}

}
