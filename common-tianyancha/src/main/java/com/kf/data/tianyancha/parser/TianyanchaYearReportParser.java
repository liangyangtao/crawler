package com.kf.data.tianyancha.parser;

import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.fetcher.tools.ReportDataFormat;
import com.kf.data.mybatis.entity.TycCompanyShareholdersContributiveCrawler;

/***
 * 
 * @Title: TianyanchaYearReportParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017年10月10日 下午4:46:49
 * @version V1.0
 */
public class TianyanchaYearReportParser extends TianyanchaBasePaser {

	public void paseNode(Document document, String companyName, String companyId) {

		// 股东出资
		Elements contentNodes = document.select(".report_holder");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					String moneyChar = null;
					String name = null;
					Double ratio = null;
					name = tdElements.get(0).text().trim();
					moneyChar = tdElements.get(4).text().trim()+"万";
					TycCompanyShareholdersContributiveCrawler tycCompanyShareholdersContributive = new TycCompanyShareholdersContributiveCrawler();
					tycCompanyShareholdersContributive.setCompanyId(companyId);
					tycCompanyShareholdersContributive.setCompanyName(companyName);
					tycCompanyShareholdersContributive.setCreatedAt(new Date());
					// tycCompanyShareholdersContributive.setCurrencyCode(currencyCode);
					// tycCompanyShareholdersContributive.setCurrencyId(currencyId);
					// tycCompanyShareholdersContributive.setCurrenyName(currenyName);
					// tycCompanyShareholdersContributive.setId(company.getId());
					tycCompanyShareholdersContributive.setMoneyChar(moneyChar);
					tycCompanyShareholdersContributive.setMoney(ReportDataFormat.bigUnitChange(moneyChar));
					tycCompanyShareholdersContributive.setName(name);
					// tycCompanyShareholdersContributive.setPrdName(prdName);
					tycCompanyShareholdersContributive.setRatio(ratio);
					tycCompanyShareholdersContributive.setStatus(false);
					// tycCompanyShareholdersContributive.setStockholderDescribe(stockholderDescribe);
					tycCompanyShareholdersContributive.setStockholderId(new Date().getTime() + "");
					// tycCompanyShareholdersContributive.setStockholderType(stockholderType);
					tycCompanyShareholdersContributive.setUpdatedAt(new Date());
					sendJson(tycCompanyShareholdersContributive, "tyc_company_shareholders_contributive");
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		}

	}

}
