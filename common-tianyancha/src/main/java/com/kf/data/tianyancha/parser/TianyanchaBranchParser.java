package com.kf.data.tianyancha.parser;

import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.kf.data.mybatis.entity.TycCompanyBranchCrawler;

/***
 * 
 * @Title: TianyanchaBranchParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 天眼查分支机构解析
 * @author liangyt
 * @date 2017年9月29日 下午2:20:20
 * @version V1.0
 */
public class TianyanchaBranchParser extends TianyanchaBasePaser {

	/***
	 * 分支机构
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void branchParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_branch");
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
							// *[@id="_container_wechat"]/div/div[11]/ul/li[5]/a
							WebElement nextPageBt = driver.findElement(
									By.xpath("//*[@id=\"_container_branch\"]/div/div[last()]/ul/li[last()]/a"));
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
	 * 天眼查分支机构解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_branch");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					// /company/919493487
					TycCompanyBranchCrawler tycCompanyBranch = new TycCompanyBranchCrawler();
					String href = tdElements.get(0).select("a").attr("href");
					String branchId = href.replace("/company/", "");
					tycCompanyBranch.setBranchId(branchId);
					tycCompanyBranch.setBranchName(tdElements.get(0).select("a").text());
					tycCompanyBranch.setCompanyId(companyId);
					tycCompanyBranch.setCompanyName(companyName);
					tycCompanyBranch.setCreatedAt(new Date());
					// tycCompanyBranch.setId(company.getId());
					// tycCompanyBranch.setOrganizationCode(obj.getString("estiblishTime"));
					tycCompanyBranch.setStatus(false);
					tycCompanyBranch.setUpdatedAt(new Date());
					sendJson(tycCompanyBranch, "tyc_company_branch");

				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		}
	}

}
