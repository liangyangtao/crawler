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

import com.kf.data.mybatis.entity.TycCompanyBoundCrawler;

import net.sf.json.JSONObject;

/****
 * 
 * @Title: TianyanchaBoundParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 债券信息
 * @author liangyt
 * @date 2017年11月1日 下午5:08:16
 * @version V1.0
 */
public class TianyanchaBondParser extends TianyanchaBasePaser {

	/***
	 * 债券信息
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void bondParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_bond");
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
									By.xpath("//*[@id=\"_container_bond\"]/div/div[last()]/ul/li[last()]/a"));
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
	 * 债券信息解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_bond");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");

					if (tdElements.size() == 6) {
						String text = tdElements.get(5).select("span").first().attr("onclick");
						text = StringUtils.substringBetween(text, "openBondPopup({", "})");
						text = "{" + text + "}";
						JSONObject obj = JSONObject.fromObject(text);

						String bondName = obj.getString("bondName");
						String bondNum = obj.getString("bondNum");
						String publisherName = obj.getString("publisherName");
						String bondType = obj.getString("bondType");

						String publishTime = obj.getString("publishTime");
						String publishExpireTime = obj.getString("publishExpireTime");
						String bondTimeLimit = obj.getString("bondTimeLimit");
						String bondTradeTime = obj.getString("bondTradeTime");
						String calInterestType = obj.getString("calInterestType");

						String bondStopTime = obj.getString("bondStopTime");
						String creditRatingGov = obj.getString("creditRatingGov");
						String debtRating = obj.getString("debtRating");

						String faceValue = obj.getString("faceValue");
						String refInterestRate = obj.getString("refInterestRate");

						String faceInterestRate = obj.getString("faceInterestRate");
						String realIssuedQuantity = obj.getString("realIssuedQuantity");
						String planIssuedQuantity = obj.getString("planIssuedQuantity");

						String issuedPrice = obj.getString("issuedPrice");
						String interestDiff = obj.getString("interestDiff");
						String payInterestHZ = obj.getString("payInterestHZ");
						String startCalInterestTime = obj.getString("startCalInterestTime");
						String exeRightType = obj.getString("exeRightType");

						String exeRightTime = obj.getString("exeRightTime");
						String escrowAgent = obj.getString("escrowAgent");
						String flowRange = obj.getString("flowRange");
						String remark = obj.getString("remark");
						String tip = obj.getString("tip");
						String createTime = obj.getString("createTime");
						String updateTime = obj.getString("updateTime");
						String isDelete = obj.getString("isDelete");
						TycCompanyBoundCrawler tycCompanyBoundCrawler = new TycCompanyBoundCrawler();
						tycCompanyBoundCrawler.setBondName(bondName);
						tycCompanyBoundCrawler.setBondNum(bondNum);
						tycCompanyBoundCrawler.setBondStopTime(bondStopTime);
						tycCompanyBoundCrawler.setBondTimeLimit(bondTimeLimit);
						tycCompanyBoundCrawler.setBondTradeTime(bondTradeTime);
						tycCompanyBoundCrawler.setBondType(bondType);
						tycCompanyBoundCrawler.setCalInterestType(calInterestType);
						tycCompanyBoundCrawler.setCompanyId(companyId);
						tycCompanyBoundCrawler.setCompanyName(companyName);
						tycCompanyBoundCrawler.setCreatedAt(new Date());
						tycCompanyBoundCrawler.setCreateTime(createTime);
						tycCompanyBoundCrawler.setCreditRatingGov(creditRatingGov);
						tycCompanyBoundCrawler.setDebtRating(debtRating);
						tycCompanyBoundCrawler.setEscrowAgent(escrowAgent);
						tycCompanyBoundCrawler.setExeRightTime(exeRightTime);
						tycCompanyBoundCrawler.setExeRightType(exeRightType);
						tycCompanyBoundCrawler.setFaceInterestRate(faceInterestRate);
						tycCompanyBoundCrawler.setFaceValue(faceValue);
						tycCompanyBoundCrawler.setFlowRange(flowRange);
						tycCompanyBoundCrawler.setInfoDelete(isDelete);
						tycCompanyBoundCrawler.setInterestDiff(interestDiff);
						tycCompanyBoundCrawler.setIssuedPrice(issuedPrice);
						tycCompanyBoundCrawler.setPayInterestHz(payInterestHZ);
						tycCompanyBoundCrawler.setPlanIssuedQuantity(planIssuedQuantity);
						tycCompanyBoundCrawler.setPublisherName(publisherName);
						tycCompanyBoundCrawler.setPublishExpireTime(publishExpireTime);
						tycCompanyBoundCrawler.setPublishTime(publishTime);
						tycCompanyBoundCrawler.setRealIssuedQuantity(realIssuedQuantity);
						tycCompanyBoundCrawler.setRefInterestRate(refInterestRate);
						tycCompanyBoundCrawler.setRemark(remark);
						tycCompanyBoundCrawler.setStartCalInterestTime(startCalInterestTime);
						tycCompanyBoundCrawler.setStatus((byte) 0);
						tycCompanyBoundCrawler.setTip(tip);
						tycCompanyBoundCrawler.setUpdatedAt(new Date());
						tycCompanyBoundCrawler.setUpdateTime(updateTime);
						sendJson(tycCompanyBoundCrawler, "tyc_company_bond");
					}

				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		}

	}

}
