package com.kf.data.tianyancha.parser;

import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.mybatis.entity.TycCompanyChangeCrawler;

public class TianyanchaChangeParser extends TianyanchaBasePaser {
	// <!--变更信息oocss-->
	// <!-- ngIf: items2.changeCount.show&&dataItemCount.changeCount>0 -->
	// neeq_company_change

	public static final String bodycCssPath = "div[ng-if=items2.changeCount.show&&dataItemCount.changeCount>0]";
	public static final String listCssPath = "tr[ng-repeat=change in changeinfoList.result]";
	public static final String pageTotalCssPath = ".total";

	public void paseNode(Document document, String companyName, String companyId) {

		Elements contentNodes = document.select("#_container_changeinfo");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");

					String changeDate = tdElements.get(0).text();
					changeDate = changeDate.replace("-", "");
					String changeProject = null;
					try {
						changeProject = tdElements.get(1).text();
					} catch (Exception e) {
					}
					String changeFirst = tdElements.get(2).text();
					changeFirst = Jsoup.parse(changeFirst).text();
					changeFirst = changeFirst.replace("<em>", "");
					changeFirst = changeFirst.replace("</em>", "");

					TycCompanyChangeCrawler tycCompanyChange = new TycCompanyChangeCrawler();
					String changeBack = tdElements.get(3).text();
					changeBack = Jsoup.parse(changeBack).text();
					changeBack = changeBack.replace("<em>", "");
					changeBack = changeBack.replace("</em>", "");
					tycCompanyChange.setChangeBack(changeBack);
					tycCompanyChange.setChangeDate(changeDate);
					// tycCompanyChange.setChangeDateMachine(null);
					tycCompanyChange.setChangeFirst(changeFirst);
					tycCompanyChange.setChangeProject(changeProject);
					tycCompanyChange.setCompanyId(companyId);
					tycCompanyChange.setCompanyName(companyName);
					tycCompanyChange.setCreatedAt(new Date());
					// tycCompanyChange.setId(company.getId());
					tycCompanyChange.setStatus(true);
					tycCompanyChange.setUpdatedAt(new Date());
					sendJson(tycCompanyChange, "tyc_company_change");
				} catch (Exception e) {
					continue;
				}
			}
		}
	}

}
