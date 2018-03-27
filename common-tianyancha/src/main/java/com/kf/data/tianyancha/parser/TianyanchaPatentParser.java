package com.kf.data.tianyancha.parser;

import java.util.Date;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.kf.data.mybatis.entity.TycCompanyPatentCrawlerWithBLOBs;

import net.sf.json.JSONObject;

/**
 * @TianyanchaPatentCountParser.java
 * @2017年4月22日
 * @author yinxin
 * @TianyanchaPatentCountParser
 * @注释：专利信息
 */
public class TianyanchaPatentParser extends TianyanchaBasePaser {

	/***
	 * 专利
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void patentParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId, driver);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_patent");
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
							// *[@id="_container_patent"]/div/div/ul/li[13]/a
							WebElement nextPageBt = driver.findElement(
									By.xpath("//*[@id=\"_container_patent\"]/div/div[last()]/ul/li[last()]/a"));
							((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageBt);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							document = Jsoup.parse(driver.getPageSource(), driver.getCurrentUrl());
							paseNode(document, companyName, companyId, driver);
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
	 * 专利信息解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId, WebDriver driver) {
		Elements contentNodes = document.select("#_container_patent");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					if (tdElements.size() == 5) {
						TycCompanyPatentCrawlerWithBLOBs tycCompanyPatentCrawler = new TycCompanyPatentCrawlerWithBLOBs();

						String currenWindow = driver.getWindowHandle();
						try {
							Element linkElement = tdElements.get(4).select("a").first();
							String reportLink = linkElement.absUrl("href");
							JavascriptExecutor executor = (JavascriptExecutor) driver;
							executor.executeScript("window.open('" + reportLink + "')");
							Set<String> allWindows = driver.getWindowHandles();
							for (String string : allWindows) {
								if (string.equals(currenWindow)) {
									continue;
								} else {
									driver.switchTo().window(string);
									try {
										Thread.sleep(5000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									if (driver.getCurrentUrl().equals(reportLink)) {
										break;
									}
								}
							}
							String reportHtml = driver.getPageSource();
							Document reportDocument = Jsoup.parse(reportHtml, reportLink);
							String patent_name = null;
							String publish_number = null;
							String application_number = null;
							String classification_number = null;
							String inventor = null;
							String applicant = null;
							String application_date = null;
							String publish_date = null;
							String proxy_agency = null;
							String agent = null;
							String address = null;
							String summary = null;

							String imgurl = null;
							try {
								Element imgElement = reportDocument.select(".patent-photo > img").first();
								imgurl = imgElement.attr("src");
							} catch (Exception e) {
								e.printStackTrace();
							}

							try {
								Element tableElement = reportDocument.select(".patent-table").first();
								Elements trElements = tableElement.select("tr");
								for (Element trElement : trElements) {
									Elements leftElements = trElement.select(".patent-name");
									for (Element leftElement : leftElements) {
										if (leftElement.text().startsWith("申请公布号")) {
											publish_number = trElement.select("td").get(1).text().trim();
										} else if (leftElement.text().startsWith("申请号")) {
											application_number = trElement.select("td").get(3).text().trim();
										} else if (leftElement.text().startsWith("分类号")) {
											classification_number = trElement.select("td").get(1).text().trim();
										} else if (leftElement.text().startsWith("发明名称")) {
											patent_name = trElement.select("td").get(1).text().trim();
										} else if (leftElement.text().startsWith("发明人")) {
											inventor = trElement.select("td").get(1).text().trim();
										} else if (leftElement.text().startsWith("申请人")) {
											applicant = trElement.select("td").get(3).text().trim();
										} else if (leftElement.text().startsWith("申请日期")) {
											application_date = trElement.select("td").get(1).text().trim();
										} else if (leftElement.text().startsWith("申请公布日期")) {
											publish_date = trElement.select("td").get(3).text().trim();
										} else if (leftElement.text().startsWith("代理机构")) {
											proxy_agency = trElement.select("td").get(1).text().trim();
										} else if (leftElement.text().startsWith("代理人")) {
											agent = trElement.select("td").get(3).text().trim();
										} else if (leftElement.text().startsWith("地址")) {
											address = trElement.select("td").get(1).text().trim();
										} else if (leftElement.text().startsWith("摘要")) {
											summary = trElement.select("td").get(1).text().trim();
										}

									}

								}

							} catch (Exception e) {
								e.printStackTrace();
							}

							tycCompanyPatentCrawler.setCompanyId(companyId);
							tycCompanyPatentCrawler.setCompanyName(companyName);
							tycCompanyPatentCrawler.setSummary(summary);
							tycCompanyPatentCrawler.setCreatedAt(new Date());
							// tycCompanyPatentCrawler.setClassificationMainNumber(classification_main_number);
							tycCompanyPatentCrawler.setClassificationNumber(classification_number);
							tycCompanyPatentCrawler.setPublishNumber(publish_number);
							tycCompanyPatentCrawler.setProxyAgency(proxy_agency);
							tycCompanyPatentCrawler.setInventor(inventor);
							tycCompanyPatentCrawler.setAgent(agent);
							tycCompanyPatentCrawler.setApplicationNumber(application_number);
							tycCompanyPatentCrawler.setPatentName(patent_name);
							tycCompanyPatentCrawler.setAddress(address);
							application_date = application_date.replace(".", "");
							application_date = application_date.replace("-", "");
							tycCompanyPatentCrawler.setApplicationDate(Integer.parseInt(application_date));
							// tycCompanyPatentCrawler.setPatentType(patent_type);
							tycCompanyPatentCrawler.setApplicant(applicant);
							if (publish_date != null) {
								publish_date = publish_date.replace(".", "");
								publish_date = publish_date.replace("-", "");
							}
							tycCompanyPatentCrawler.setPublishDate(publish_date);
							tycCompanyPatentCrawler.setStatus(false);
							tycCompanyPatentCrawler.setImgUrl(imgurl);

							sendJson(tycCompanyPatentCrawler, "tyc_company_patent");

							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							driver.close();
							driver.switchTo().window(currenWindow);
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		}

	}

}
