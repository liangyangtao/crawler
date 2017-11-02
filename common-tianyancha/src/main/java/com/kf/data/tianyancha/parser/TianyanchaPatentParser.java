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
		paseNode(document, companyName, companyId);
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
	 * 专利信息解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_patent");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					if (tdElements.size() == 5) {
						String text = tdElements.get(4).select("span").first().attr("onclick");
						text = StringUtils.substringBetween(text, "openPatentPopup({", "})");
						text = "{" + text + "}";
						JSONObject obj = JSONObject.fromObject(text);
						String classification_main_number = obj.getString("mainCatNum");
						String publish_number = obj.getString("applicationPublishNum");
						String proxy_agency = "";
						try {
							proxy_agency = obj.getString("agency");
						} catch (Exception e) {
							logger.info(companyName + "agency 不存在");
						}
						String inventor = obj.getString("inventor");
						String agent = "";
						try {
							agent = obj.getString("agent");
						} catch (Exception e) {
							logger.info(companyName + "agent 不存在");
							// e.printStackTrace();
						}
						String application_number = obj.getString("patentNum");
						String classification_number = obj.getString("allCatNum");
						String patent_name = obj.getString("patentName");
						String summary = obj.getString("abstracts");
						String address = obj.getString("address");
						String application_date = obj.getString("applicationTime");
						String patent_type = obj.getString("patentType");
						String applicant = obj.getString("applicantName");
						String publish_date = obj.getString("applicationPublishTime");
						publish_date = publish_date.replace("-", "");
						// 修改图片使用专利网的图片
						String imgurl = null;
//						String kfImgUrl = null;
						try {
							imgurl = obj.getString("imgUrl");
//							kfImgUrl = new AliOssSender().uploadObject(imgurl);
//							if (kfImgUrl.startsWith("https://") || kfImgUrl.startsWith("http://")) {
//
//							} else {
//								kfImgUrl = "https:" + kfImgUrl;
//							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						// openPatentPopup({"mainCatNum":"H04L29\u002F06(2006.01)I","applicationPublishNum":"CN203206281U","agency":"北京三友知识产权代理有限公司
						// 11127","inventor":"王海洲;孙成国;揭文云","agent":"陶海萍","applicationPublishTime":"2013-09-18","patentNum":"CN201220716684.1","imgUrl":"http:\u002F\u002Fpic.cnipr.com:8080\u002FXmlData\u002FXX\u002F20130918\u002F201220716684.1\u002F201220716684.gif","allCatNum":"H04L29\u002F06(2006.01)I;G06F21\u002F60(2013.01)I","patentName":"源代码保护机和源代码保护系统","abstracts":"本实用新型实施例提供一种源代码保护机和源代码保护系统，该源代码保护机与局域网中的网络交换机以及预警装置分别相连，所述网络交换机还连接多台终端设备，该源代码保护机包括：用于监听软件开发所在局域网中任意两台终端设备间的通信，对通信传输的数据包进行解码和反编译，并与预先设定的过滤条件进行比较，从而驱动预警装置进行预警的处理器；以及用于连接显示器的外接端口。这样，能够有效地保护源代码，防止软件开发过程中源代码的泄露。","address":"100048
						// 北京市海淀区首体南路9号主语商务中心4号楼","applicationTime":"2012.12.21","uuid":"c72b12493ff14ed39a78d2f6a7df4d86","patentType":"实用新型","applicantName":"中建材集团进出口公司"})

						TycCompanyPatentCrawlerWithBLOBs tycCompanyPatentCrawler = new TycCompanyPatentCrawlerWithBLOBs();
						tycCompanyPatentCrawler.setCompanyId(companyId);
						tycCompanyPatentCrawler.setCompanyName(companyName);
						tycCompanyPatentCrawler.setSummary(summary);
						tycCompanyPatentCrawler.setCreatedAt(new Date());
						tycCompanyPatentCrawler.setClassificationMainNumber(classification_main_number);
						tycCompanyPatentCrawler.setClassificationNumber(classification_number);
						tycCompanyPatentCrawler.setPublishNumber(publish_number);
						tycCompanyPatentCrawler.setProxyAgency(proxy_agency);
						tycCompanyPatentCrawler.setInventor(inventor);
						tycCompanyPatentCrawler.setAgent(agent);
						tycCompanyPatentCrawler.setApplicationNumber(application_number);
						tycCompanyPatentCrawler.setPatentName(patent_name);
						tycCompanyPatentCrawler.setAddress(address);
						// Date date = null;
						// SimpleDateFormat sdf = new
						// SimpleDateFormat("yyyy.MM.dd");
						// try {
						// date = sdf.parse(application_date);
						// } catch (ParseException e) {
						// e.printStackTrace();
						// }
						application_date = application_date.replace(".", "");
						application_date = application_date.replace("-", "");
						tycCompanyPatentCrawler.setApplicationDate(Integer.parseInt(application_date));
						tycCompanyPatentCrawler.setPatentType(patent_type);
						tycCompanyPatentCrawler.setApplicant(applicant);
						tycCompanyPatentCrawler.setPublishDate(publish_date);
						tycCompanyPatentCrawler.setStatus(false);
						tycCompanyPatentCrawler.setImgUrl(imgurl);
						sendJson(tycCompanyPatentCrawler, "tyc_company_patent");
					}

				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		}

	}

}
