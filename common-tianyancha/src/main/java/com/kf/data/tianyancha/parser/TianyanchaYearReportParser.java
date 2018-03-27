package com.kf.data.tianyancha.parser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

/***
 * 
 * @Title: TianyanchaYearReportParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 年报解析
 * @author liangyt
 * @date 2017年10月10日 下午4:46:49
 * @version V1.0
 */
public class TianyanchaYearReportParser extends TianyanchaBasePaser {

	/***
	 * 年报解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 * @param driver
	 */
	public void yearPortParser(Document document, String companyName, String companyId, WebDriver driver) {
		// 企业年报
		Elements reportNodes = document.select(".report_item_2017");
		for (Element element : reportNodes) {
			try {
				Element linkElement = element.select("a").first();
				String reportLink = linkElement.absUrl("href");
				driver.get(reportLink);
				String reportHtml = driver.getPageSource();
				Document reportDocument = Jsoup.parse(reportHtml, reportLink);
				String reportdate = element.select(".pt15").first().text().trim();
				paseNode(reportDocument, companyName, companyId, reportdate);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				driver.navigate().back();
			}

		}

	}

	/***
	 * 年报解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId, String reportdate) {
		Map<String, Object> result = new HashMap<String, Object>();
		Elements bodyElement = document.select(".report_body");
		if (bodyElement.size() > 0) {
			result.put("report_dom", bodyElement.first().toString());
			result.put("company_id", companyId);
			result.put("company_name", companyName);
			result.put("status", 0);
			result.put("created_at", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
			result.put("updated_at", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
			result.put("reportdate", reportdate);
			sendJson(result, "tyc_company_report");
		}
	}

	// /****
	// * 解析股东出资
	// *
	// * @param document
	// * @param companyName
	// * @param companyId
	// */
	// private void parserShareholders(Document document, String companyName,
	// String companyId) {
	// // 股东出资
	// Elements contentNodes = document.select(".report_holder");
	// if (contentNodes.size() > 0) {
	// Elements nodes = contentNodes.first().select(".table > tbody > tr");
	// for (Element element : nodes) {
	// try {
	// Elements tdElements = element.select("td");
	// String moneyChar = null;
	// String name = null;
	// Double ratio = null;
	// name = tdElements.get(0).text().trim();
	// moneyChar = tdElements.get(4).text();
	// moneyChar = moneyChar.replace("万", "");
	// moneyChar = moneyChar.replace("元", "");
	// moneyChar = moneyChar.replace("人民币", "");
	// moneyChar = moneyChar.trim() + "万";
	// TycCompanyShareholdersContributiveCrawler
	// tycCompanyShareholdersContributive = new
	// TycCompanyShareholdersContributiveCrawler();
	// tycCompanyShareholdersContributive.setCompanyId(companyId);
	// tycCompanyShareholdersContributive.setCompanyName(companyName);
	// tycCompanyShareholdersContributive.setCreatedAt(new Date());
	// // tycCompanyShareholdersContributive.setCurrencyCode(currencyCode);
	// // tycCompanyShareholdersContributive.setCurrencyId(currencyId);
	// // tycCompanyShareholdersContributive.setCurrenyName(currenyName);
	// // tycCompanyShareholdersContributive.setId(company.getId());
	// tycCompanyShareholdersContributive.setMoneyChar(moneyChar);
	// tycCompanyShareholdersContributive.setMoney(ReportDataFormat.bigUnitChange(moneyChar));
	// tycCompanyShareholdersContributive.setName(name);
	// // tycCompanyShareholdersContributive.setPrdName(prdName);
	// tycCompanyShareholdersContributive.setRatio(ratio);
	// tycCompanyShareholdersContributive.setStatus(false);
	// //
	// tycCompanyShareholdersContributive.setStockholderDescribe(stockholderDescribe);
	// tycCompanyShareholdersContributive.setStockholderId(new Date().getTime()
	// + "");
	// //
	// tycCompanyShareholdersContributive.setStockholderType(stockholderType);
	// tycCompanyShareholdersContributive.setUpdatedAt(new Date());
	// sendJson(tycCompanyShareholdersContributive,
	// "tyc_company_shareholders_contributive");
	// } catch (Exception e) {
	// e.printStackTrace();
	// continue;
	// }
	//
	// }
	// }
	//
	// }

}
