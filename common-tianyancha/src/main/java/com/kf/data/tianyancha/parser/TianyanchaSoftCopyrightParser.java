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

import com.kf.data.mybatis.entity.TycCompanySoftCopyrightCrawler;

import net.sf.json.JSONObject;

/***
 * 
 * @Title: TianyanchaSoftCopyrightParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 软件著作权
 * @author liangyt
 * @date 2017年10月11日 下午2:14:10
 * @version V1.0
 */
public class TianyanchaSoftCopyrightParser extends TianyanchaBasePaser {

	/***
	 * 软件著作权
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void investParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_copyright");
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
									By.xpath("//*[@id=\"_container_copyright\"]/div/div[last()]/ul/li[last()]/a"));
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
	 * 软件著作权
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_copyright");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					if (tdElements.size() == 7) {
						// openCopyrightPopup({"id":"2797175","regtime":"1505059200000","publishtime":"1503763200000","authorNationality":"北京百度网讯科技有限公司:中国",
						// "simplename":"百度文库","regnum":"2017SR502655","catnum":"30200-0000","fullname":"百度文库Android终端软件","version":"V4.2.4"})
						String text = tdElements.get(6).select("span").first().attr("onclick");
						text = StringUtils.substringBetween(text, "openCopyrightPopup({", "})");
						text = "{" + text + "}";
						JSONObject obj = JSONObject.fromObject(text);
						String authornationality = obj.getString("authorNationality");
						String catnum = obj.getString("catnum");
						String fullname = obj.getString("fullname");
						String publishtime = obj.getString("publishtime");
						String regnum = obj.getString("regnum");
						String regtime = obj.getString("regtime");
						String simplename = obj.getString("simplename");
						String version = obj.getString("version");
						TycCompanySoftCopyrightCrawler tycCompanySoftCopyrightCrawler = new TycCompanySoftCopyrightCrawler();
						tycCompanySoftCopyrightCrawler.setAuthornationality(authornationality);
						tycCompanySoftCopyrightCrawler.setCatnum(catnum);
						tycCompanySoftCopyrightCrawler.setCompanyId(companyId);
						tycCompanySoftCopyrightCrawler.setCompanyName(companyName);
						tycCompanySoftCopyrightCrawler.setCreatedAt(new Date());
						tycCompanySoftCopyrightCrawler.setFullname(fullname);
						tycCompanySoftCopyrightCrawler.setPublishtime(publishtime);
						tycCompanySoftCopyrightCrawler.setRegnum(regnum);
						tycCompanySoftCopyrightCrawler.setRegtime(regtime);
						tycCompanySoftCopyrightCrawler.setSimplename(simplename);
						tycCompanySoftCopyrightCrawler.setStatus((byte) 0);
						tycCompanySoftCopyrightCrawler.setUpdatedAt(new Date());
						tycCompanySoftCopyrightCrawler.setVersion(version);
						sendJson(tycCompanySoftCopyrightCrawler, "tyc_company_soft_copyright");
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}
}
