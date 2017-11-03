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

import com.kf.data.mybatis.entity.TycCompanyRecruitmentCrawler;

import net.sf.json.JSONObject;

/***
 * 
 * @Title: TianyanchaRecruitParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 招聘信息解析
 * @author liangyt
 * @date 2017年10月11日 下午2:15:22
 * @version V1.0
 */
public class TianyanchaRecruitParser extends TianyanchaBasePaser {

	/***
	 * 招聘翻页抽取数据
	 * 
	 * @param document
	 * @param driver
	 */
	public void recruitParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_recruit");
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
									By.xpath("//*[@id=\"_container_recruit\"]/div/div[last()]/ul/li[last()]/a"));
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

	/****
	 * 招聘信息解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_recruit");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					if (tdElements.size() == 7) {
						String text = tdElements.get(6).select("span").attr("onclick");
						text = StringUtils.substringBetween(text, "employePopup({", "})");
						text = "{" + text + "}";
						TycCompanyRecruitmentCrawler tycCompanyRecruitmentCrawler = new TycCompanyRecruitmentCrawler();
						JSONObject fromObj = JSONObject.fromObject(text);
						// 招聘职位
						String job_title = fromObj.getString("title");
						String job_city = fromObj.getString("city");
						String search_area = fromObj.getString("district");
//						String company_name = fromObj.getString("companyName");
						String salary_range = fromObj.getString("oriSalary");
						String surl = fromObj.getString("urlPath");
						String dt_start = fromObj.getString("startdate");
						String dt_end = fromObj.getString("enddate");
						Date start = timestampToDate(dt_start);
						Date end = null;
						if (dt_end != null) {
							end = timestampToDate(dt_end);
						}
						String source = fromObj.getString("source");
						String academic_requirements = fromObj.getString("education");
						String job_count = fromObj.getString("employerNumber");
						String experience_requirements = fromObj.getString("experience");
						String release_date = fromObj.getString("createTime");
						String update_date = fromObj.getString("updateTime");
						String release = dateTo8char(release_date);
						String update = dateTo8char(update_date);
						String job_description = fromObj.getString("description");
						tycCompanyRecruitmentCrawler.setCompanyId(companyId);
						tycCompanyRecruitmentCrawler.setJobTitle(job_title);
						tycCompanyRecruitmentCrawler.setJobCity(job_city);
						tycCompanyRecruitmentCrawler.setSearchArea(search_area);
						tycCompanyRecruitmentCrawler.setCompanyName(companyName);
						tycCompanyRecruitmentCrawler.setSalaryRange(salary_range);
						tycCompanyRecruitmentCrawler.setUrl(surl);
						tycCompanyRecruitmentCrawler.setDtStart(start);
						tycCompanyRecruitmentCrawler.setDtEnd(end);
						tycCompanyRecruitmentCrawler.setSource(source);
						tycCompanyRecruitmentCrawler.setAcademicRequirements(academic_requirements);
						tycCompanyRecruitmentCrawler.setJobCount(job_count);
						tycCompanyRecruitmentCrawler.setExperienceRequirements(experience_requirements);
						tycCompanyRecruitmentCrawler.setReleaseDate(release);
						tycCompanyRecruitmentCrawler.setUpdateDate(update);
						tycCompanyRecruitmentCrawler.setJobDescription(job_description);
						tycCompanyRecruitmentCrawler.setCreatedAt(new Date());
						tycCompanyRecruitmentCrawler.setStatus(false);
						sendJson(tycCompanyRecruitmentCrawler, "tyc_company_recruitment");

					}

				} catch (Exception e) {
					continue;
				}

			}

		}

	}
}
