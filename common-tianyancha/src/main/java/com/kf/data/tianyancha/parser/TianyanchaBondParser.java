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
						// String text =
						// tdElements.get(5).select("span").first().attr("onclick");
						// text = StringUtils.substringBetween(text,
						// "openBondPopup({", "})");
						// text = "{" + text + "}";
						// JSONObject obj = JSONObject.fromObject(text);
						Element scriptElement = tdElements.get(5).select("script").first();
						String text = StringUtils.substringBetween(scriptElement.toString(),
								"<script type=\"text/html\">", "</script>");
						JSONObject obj = JSONObject.fromObject(text);
						String bondName = null;
						try {
							bondName = obj.getString("bondName");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String bondNum = null;
						try {
							bondNum = obj.getString("bondNum");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String publisherName = null;
						try {
							publisherName = obj.getString("publisherName");
						} catch (Exception e) {
							e.getMessage();
						}
						String bondType = null;
						try {
							bondType = obj.getString("bondType");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String publishTime = null;
						try {
							publishTime = obj.getString("publishTime");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String publishExpireTime = null;
						try {
							publishExpireTime = obj.getString("publishExpireTime");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String bondTimeLimit = null;
						try {
							bondTimeLimit = obj.getString("bondTimeLimit");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String bondTradeTime = null;
						try {
							bondTradeTime = obj.getString("bondTradeTime");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String calInterestType = null;
						try {
							calInterestType = obj.getString("calInterestType");
						} catch (Exception e) {
							e.printStackTrace();
						}

						String bondStopTime = null;
						try {
							bondStopTime = obj.getString("bondStopTime");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String creditRatingGov = null;
						try {

							creditRatingGov = obj.getString("creditRatingGov");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String debtRating = null;
						try {
							debtRating = obj.getString("debtRating");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String faceValue = null;
						try {

							faceValue = obj.getString("faceValue");
						} catch (Exception e) {
							e.printStackTrace();
						}

						String refInterestRate = null;
						try {
							refInterestRate = obj.getString("refInterestRate");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String faceInterestRate = null;

						try {

							faceInterestRate = obj.getString("faceInterestRate");
						} catch (Exception e) {
							e.printStackTrace();
						}

						String realIssuedQuantity = null;
						try {
							realIssuedQuantity = obj.getString("realIssuedQuantity");
						} catch (Exception e) {
							e.printStackTrace();
						}

						String planIssuedQuantity = null;
						try {
							planIssuedQuantity = obj.getString("planIssuedQuantity");
						} catch (Exception e) {
							e.printStackTrace();
						}

						String issuedPrice = null;
						try {

							issuedPrice = obj.getString("issuedPrice");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String interestDiff = null;

						try {
							interestDiff = obj.getString("interestDiff");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String payInterestHZ = null;

						try {
							payInterestHZ = obj.getString("payInterestHZ");
						} catch (Exception e) {
							e.printStackTrace();
						}

						String startCalInterestTime = null;
						try {
							startCalInterestTime = obj.getString("startCalInterestTime");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String exeRightType = null;
						try {
							exeRightType = obj.getString("exeRightType");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String exeRightTime = null;
						try {

							exeRightTime = obj.getString("exeRightTime");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String escrowAgent = null;
						try {
							escrowAgent = obj.getString("escrowAgent");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String flowRange = null;
						try {
							flowRange = obj.getString("flowRange");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String remark = null;
						try {
							remark = obj.getString("remark");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String tip = null;
						try {
							tip = obj.getString("tip");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String createTime = null;
						try {
							createTime = obj.getString("createTime");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String updateTime = null;
						try {
							updateTime = obj.getString("updateTime");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String isDelete = null;
						try {
							isDelete = obj.getString("isDelete");
						} catch (Exception e) {
							e.printStackTrace();
						}
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
