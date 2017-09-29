package com.kf.data.tianyancha.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.mybatis.entity.TycCompanyDomainRecordCrawler;

/**
 * @TianyanchaIcpParser.java
 * @2017年4月24日
 * @author yinxin
 * @TianyanchaIcpParser
 * @注释：网站备案
 */
public class TianyanchaIcpParser extends TianyanchaBasePaser {
	// <!-- 网站备案oocss -->
	// <!-- ngIf: items2.icpCount.show&&dataItemCount.icpCount>0 -->
	public static final String bodyCssPath = "div[ng-if=items2.icpCount.show&&dataItemCount.icpCount>0]";
	public static final String listCssPath = "tr[ng-repeat=check in changeinfoList.result]";
	public static final String pageTotalCssPath = ".total";

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
					release_date =release_date.replace("-", "");
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
