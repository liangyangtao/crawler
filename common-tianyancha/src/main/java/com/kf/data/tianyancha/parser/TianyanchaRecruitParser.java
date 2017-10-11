package com.kf.data.tianyancha.parser;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
	// <!--招聘信息oocss-->
	// <!-- ngIf: items2.recruitCount.show&&dataItemCount.recruitCount>0 -->
	// neeq_company_recruitment
	public static final String cssPath = "div[ng-if=items2.recruitCount.show&&dataItemCount.recruitCount>0]";
	public static final String bodyCssPath = "div[ng-if=dataItemCount.recruitCount>0]";
	public static final String listCssPath = "tr[ng-repeat=check in changeinfoList.result]";
	public static final String pageTotalCssPath = ".total";

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
						String company_name = fromObj.getString("companyName");
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
			// try {
			// Elements pageElements =
			// contentNodes.first().select(".company_pager");
			// int linum = 0;
			// if (pageElements.size() > 0) {
			// Elements liElements = pageElements.first().select("li");
			// if (liElements.size() > 0) {
			// linum = liElements.size();
			// if (i > linum - 2) {
			// break;
			// }
			// if (liElements.last().classNames().contains("disabled")) {
			// break;
			// }
			// }
			// } else {
			// break;
			// }
			// if (linum > 1) {
			// System.out.println(linum+"--------------------------");
			// driver.findElement(
			// By.xpath("//*[@id=\"_container_recruit\"]/div/div[2]/ul/li[last()]/a")).click();
			// Thread.sleep(5000);
			// document = Jsoup.parse(driver.getPageSource());
			// } else {
			// break;
			// }
			// } catch (Exception e) {
			// e.printStackTrace();
			// break;
			// }
			// }

		}

	}
}
