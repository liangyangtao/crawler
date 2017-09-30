package com.kf.data.tianyancha.parser;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.fetcher.tools.ReportDataFormat;
import com.kf.data.mybatis.entity.TycCompanyShareholdersContributiveCrawler;

public class TianyanchaHolderParser extends TianyanchaBasePaser {
	// <!--[股东信息]-->
	// <!-- ngIf: items2.holderCount.show&&dataItemCount.holderCount>0 -->
	// neeq_company_shareholders_contributive
	public static final String cssPath = "div[ng-if=items2.holderCount.show&&dataItemCount.holderCount>0]";
	public static final String bodyCssPath = "div#_container_holder";
	public static final String listCssPath = ".companyInfo-table > tbody > tr";
	public static final String pageTotalCssPath = ".total";

	public void paseNode(Document document, String companyName, String companyID) {
		// 是否分页
		// boolean hasNextPage = false;
		Element bodyElemnt = getNodeByCssPath(document, bodyCssPath);
		if (bodyElemnt == null) {
			return;
		}
		Elements nodes = bodyElemnt.select(listCssPath);

		for (Element element : nodes) {
			try {
				Double money = null;
				String moneyChar = null;
				String name = null;
				Double ratio = null;
				Element nameElement = element.select("td > a").get(0);
				name = nameElement.text();

				Element ratioElement = element.select("td").get(1);
				String ratioStr = ratioElement.text();
				ratioStr = ratioStr.replace("%", "");
				ratioStr = ratioStr.replace("<", "");
				try {
					if (ratioStr.contains("未公开")) {
						ratio = 0.0;
					} else {
						ratio = Double.parseDouble(ratioStr.isEmpty() ? "0" : ratioStr);
					}

				} catch (Exception e) {
					e.printStackTrace();
					ratio = 0.0;
				}
				Element moneyElement = element.select("td").get(2);
				String moneyStr = moneyElement.text().trim();
				if (moneyStr.contains("未公开")) {
					moneyStr = "";
				} else {
					moneyStr = StringUtils.substringBefore(moneyStr, "元");
				}
				TycCompanyShareholdersContributiveCrawler tycCompanyShareholdersContributive = new TycCompanyShareholdersContributiveCrawler();
				tycCompanyShareholdersContributive.setCompanyId(companyID);
				tycCompanyShareholdersContributive.setCompanyName(companyName);
				tycCompanyShareholdersContributive.setCreatedAt(new Date());
				// tycCompanyShareholdersContributive.setCurrencyCode(currencyCode);
				// tycCompanyShareholdersContributive.setCurrencyId(currencyId);
				// tycCompanyShareholdersContributive.setCurrenyName(currenyName);
				// tycCompanyShareholdersContributive.setId(company.getId());

				tycCompanyShareholdersContributive.setMoney(null);

				tycCompanyShareholdersContributive.setMoneyChar(moneyStr);
				tycCompanyShareholdersContributive.setMoney(ReportDataFormat.bigUnitChange(moneyStr));
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
