package com.kf.data.tianyancha.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.kf.data.mybatis.entity.TycCompanyDomainRecordCrawler;

/**
 * @TianyanchaIcpParser.java
 * @2017年4月24日
 * @author yinxin
 * @TianyanchaIcpParser
 * @注释：网站备案
 */
public class TianyanchaIcpParser extends TianyanchaBasePaser {

	/***
	 * 网站备案
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void ipcParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_icp");
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
							int size = liElements.size();
							// *[@id="_container_icp"]/div/div[2]/ul/li[13]/a
							WebElement nextPageBt = driver.findElement(
									By.xpath("//*[@id=\"_container_icp\"]/div/div[last()]/ul/li[last()]/a"));
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
	 * 网站备案 信息解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyID
	 */
	public void paseNode(Document document, String companyName, String companyID) {
		Elements contentNodes = document.select("#_container_icp");
		if (contentNodes.size() > 0) {

			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {

					Elements tdElements = element.select("td");
					TycCompanyDomainRecordCrawler tycCompanyDomainRecordCrawler = new TycCompanyDomainRecordCrawler();

					String home_url = tdElements.get(2).text();// 网站首页
					String release_date = tdElements.get(0).text().trim();
					release_date = release_date.replace("-", "");
					String host_type = tdElements.get(6).text();// 单位性质
					String site_name = tdElements.get(1).text();// 网站名称
					String domain = tdElements.get(3).text();// 域名
					// String company_name = obj.getString("companyName");//
					// 公司名
					String record_number = tdElements.get(4).text();// 备案号
					tycCompanyDomainRecordCrawler.setCompanyId(companyID);
					tycCompanyDomainRecordCrawler.setHomeUrl(home_url);
					tycCompanyDomainRecordCrawler.setReleaseDate(release_date);
					tycCompanyDomainRecordCrawler.setHostType(host_type);
					tycCompanyDomainRecordCrawler.setSiteName(site_name);
					tycCompanyDomainRecordCrawler.setDomain(domain);
					tycCompanyDomainRecordCrawler.setCompanyName(companyName);
					tycCompanyDomainRecordCrawler.setRecordNumber(record_number);
					tycCompanyDomainRecordCrawler.setStatus(false);
					sendJson(tycCompanyDomainRecordCrawler, "tyc_company_domain_record");
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		}
	}

}
