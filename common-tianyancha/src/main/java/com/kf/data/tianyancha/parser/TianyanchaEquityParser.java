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

import com.kf.data.mybatis.entity.TycCompanyEquityPledgedCrawler;

import net.sf.json.JSONObject;

/**
 * @TianyanchaEquityParser.java
 * @2017年4月20日
 * @author yinxin
 * @TianyanchaEquityParser
 * @注释：股权出质
 */
public class TianyanchaEquityParser extends TianyanchaBasePaser {

	/***
	 * 股权出质
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void equityParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_equity");
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
									By.xpath("//*[@id=\"_container_equity\"]/div/div[last()]/ul/li[last()]/a"));
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
	 * 股权出质解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyID
	 */
	public void paseNode(Document document, String companyName, String companyID) {
		try {
			Element bodyElemnt = getNodeByCssPath(document, "#_container_equity");
			if (bodyElemnt == null) {
				return;
			}
			Elements nodes = bodyElemnt.select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				Elements tdElements = element.select("td");
				Element tdElement = tdElements.get(5);
				Elements scriptElements = tdElement.select("script");
				JSONObject obj = null;
				if (scriptElements.size() > 0) {
					String onclick = scriptElements.first().toString();
					onclick = StringUtils.substringBetween(onclick, "text/html\">", "</script>");
					obj = JSONObject.fromObject(onclick);
					// 出质股权数额
					String equityAmount = obj.getString("equityAmount");
					// 登记编号
					String regNumber = obj.getString("regNumber");
					// 状态
					String state = obj.getString("state");
					// 出质人
					String pledgor =null;
					try {
						 pledgor = obj.getString("pledgor");
					} catch (Exception e) {
						e.printStackTrace();
						 pledgor ="";
					}
					// 质权人号码
					String certifNumberR = obj.getString("certifNumberR");
					// 质权人
					String pledgee = obj.getString("pledgee");
					// 登记日
					String regDate = obj.getString("regDate");
					Date date = timestampToDate(regDate);
					TycCompanyEquityPledgedCrawler tycCompanyEquityPledged = new TycCompanyEquityPledgedCrawler();
					tycCompanyEquityPledged.setCompanyId(companyID);
					tycCompanyEquityPledged.setCompanyName(companyName);
					tycCompanyEquityPledged.setPldNum(equityAmount);
					tycCompanyEquityPledged.setRegisterNumber(regNumber);
					tycCompanyEquityPledged.setPldStatus(state);
					tycCompanyEquityPledged.setPldName(pledgor);
					tycCompanyEquityPledged.setPledgeeNumber(certifNumberR);
					tycCompanyEquityPledged.setPledgee(pledgee);
					tycCompanyEquityPledged.setDtNotice(date);
					tycCompanyEquityPledged.setCreatedAt(new Date());
					tycCompanyEquityPledged.setStatus(false);
					sendJson(tycCompanyEquityPledged, "tyc_company_equity_pledged");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
