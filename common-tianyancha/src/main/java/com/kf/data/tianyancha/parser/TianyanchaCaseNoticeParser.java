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

import com.kf.data.mybatis.entity.TycCompanyCaseNoticeCrawler;

import net.sf.json.JSONObject;

/****
 * 
 * @Title: TianyanchaCaseNoticeParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 法院公告
 * @author liangyt
 * @date 2017年11月1日 上午11:39:30
 * @version V1.0
 */
public class TianyanchaCaseNoticeParser extends TianyanchaBasePaser {

	/***
	 * 法院公告
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void caseNoticeParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_court");
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
									By.xpath("//*[@id=\"_container_court\"]/div/div[last()]/ul/li[last()]/a"));
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
	 * 法院公告解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_court");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					if (tdElements.size() == 6) {
						Elements scriptElement = tdElements.get(5).select("script");
						String text = scriptElement.toString();
						text = StringUtils.substringBetween(text, "<script type=\"text/html\">", "</script>");
						JSONObject obj = JSONObject.fromObject(text);
						String bltnno = null;
						try {
							bltnno = obj.getString("bltnno");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String bltnstate = null;
						try {
							bltnstate = obj.getString("bltnstate");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String bltntype = null;
						try {
							bltntype = obj.getString("bltntype");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String bltntypename = null;
						try {

							bltntypename = obj.getString("bltntypename");
						} catch (Exception e) {
							e.printStackTrace();
						}

						String caseno = null;
						try {
							caseno = obj.getString("caseno");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String content = null;
						try {
							content = obj.getString("content");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String courtflag = null;
						try {
							courtflag = obj.getString("courtflag");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String courtcode = null;
						try {
							courtcode = obj.getString("courtcode");
						} catch (Exception e) {
							e.printStackTrace();
						}

						String customno = null;
						try {
							customno = obj.getString("customno");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String dealgrade = null;
						try {
							dealgrade = obj.getString("dealgrade");
						} catch (Exception e) {
							e.printStackTrace();
						}

						String dealgradename = null;
						try {

							dealgradename = obj.getString("dealgradename");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String judge = null;

						try {

							judge = obj.getString("judge");
						} catch (Exception e) {
							e.printStackTrace();
						}

						String judgephone = null;
						try {
							judgephone = obj.getString("judgephone");
						} catch (Exception e) {
							e.printStackTrace();
						}

						String mobilephone = null;
						try {
							mobilephone = obj.getString("mobilephone");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String party1 = null;
						try {
							party1 = obj.getString("party1");
						} catch (Exception e) {
							e.printStackTrace();
						}

						String party2 = null;
						try {
							party2 = obj.getString("party2");
						} catch (Exception e) {
							e.printStackTrace();
						}

						String companyList = null;
						try {
							companyList = URLEncoder.encode(obj.getJSONArray("companyList").toString(), "utf-8");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String party1Str = null;

						try {
							party1Str = obj.getString("party1Str");
						} catch (Exception e) {
							e.printStackTrace();
						}

						String party2Str = null;
						try {
							party2Str = obj.getString("party2Str");
						} catch (Exception e) {
							e.printStackTrace();
						}

						String province = null;
						try {
							province = obj.getString("province");
						} catch (Exception e) {
							e.printStackTrace();
						}

						String publishdate = null;
						try {
							publishdate = obj.getString("publishdate");
						} catch (Exception e) {
							e.printStackTrace();
						}

						String publishpage = null;
						try {
							publishpage = obj.getString("publishpage");
						} catch (Exception e) {
							e.printStackTrace();
						}

						String reason = null;
						try {
							reason = obj.getString("reason");
						} catch (Exception e) {
							e.printStackTrace();
						}

						String showtxtdate = null;
						try {
							showtxtdate = obj.getString("showtxtdate");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String tmpsaversn = null;
						try {
							tmpsaversn = obj.getString("tmpsaversn");
						} catch (Exception e) {
							e.printStackTrace();
						}

						TycCompanyCaseNoticeCrawler tycCompanyCaseNoticeCrawler = new TycCompanyCaseNoticeCrawler();
						tycCompanyCaseNoticeCrawler.setBltnno(bltnno);
						tycCompanyCaseNoticeCrawler.setBltnstate(bltnstate);
						tycCompanyCaseNoticeCrawler.setBltntype(bltntype);
						tycCompanyCaseNoticeCrawler.setBltntypename(bltntypename);
						tycCompanyCaseNoticeCrawler.setCaseno(caseno);
						tycCompanyCaseNoticeCrawler.setCompanyId(companyId);
						tycCompanyCaseNoticeCrawler.setCompanylist(companyList);
						tycCompanyCaseNoticeCrawler.setCompanyName(companyName);
						tycCompanyCaseNoticeCrawler.setContent(content);
						tycCompanyCaseNoticeCrawler.setCourtcode(courtcode);
						tycCompanyCaseNoticeCrawler.setCourtflag(courtflag);
						tycCompanyCaseNoticeCrawler.setCreatedAt(new Date());
						tycCompanyCaseNoticeCrawler.setCustomno(customno);
						tycCompanyCaseNoticeCrawler.setDealgrade(dealgrade);
						tycCompanyCaseNoticeCrawler.setDealgradename(dealgradename);
						tycCompanyCaseNoticeCrawler.setJudge(judge);
						tycCompanyCaseNoticeCrawler.setJudgephone(judgephone);
						tycCompanyCaseNoticeCrawler.setMobilephone(mobilephone);
						tycCompanyCaseNoticeCrawler.setParty1(party1);
						tycCompanyCaseNoticeCrawler.setParty2(party2);
						tycCompanyCaseNoticeCrawler.setParty2str(party2Str);
						tycCompanyCaseNoticeCrawler.setParty1str(party1Str);
						tycCompanyCaseNoticeCrawler.setProvince(province);
						tycCompanyCaseNoticeCrawler.setPublishdate(publishdate);
						tycCompanyCaseNoticeCrawler.setPublishpage(publishpage);
						tycCompanyCaseNoticeCrawler.setReason(reason);
						tycCompanyCaseNoticeCrawler.setShowtxtdate(showtxtdate);
						tycCompanyCaseNoticeCrawler.setStatus((byte) 0);
						tycCompanyCaseNoticeCrawler.setTmpsaversn(tmpsaversn);
						tycCompanyCaseNoticeCrawler.setUpdatedAt(new Date());
						sendJson(tycCompanyCaseNoticeCrawler, "tyc_company_case_notice");
					}

				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		}

	}

}
