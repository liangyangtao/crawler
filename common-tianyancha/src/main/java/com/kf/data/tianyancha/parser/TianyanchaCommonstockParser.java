package com.kf.data.tianyancha.parser;

import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.fetcher.tools.ReportDataFormat;
import com.kf.data.mybatis.entity.TycCompanyCommonstockCrawler;

/***
 * 
 * @Title: TianyanchaCommonstockParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 天眼查股本解析
 * @author liangyt
 * @date 2017年9月30日 下午2:53:11
 * @version V1.0
 */
public class TianyanchaCommonstockParser extends TianyanchaBasePaser {

	/***
	 * 天眼查股本解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_shareStructure");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					TycCompanyCommonstockCrawler tycCompanyCommonstockCrawler = new TycCompanyCommonstockCrawler();
					String reportDate = tdElements.get(0).text();
					String allEquity = tdElements.get(1).text();
					String aAllEquity = tdElements.get(2).text();
					String aCirculationEquity = tdElements.get(3).text();
					String aLimitEquity = tdElements.get(4).text();
					String hAllEquity = tdElements.get(5).text();
					String hCirculationEquity = tdElements.get(6).text();
					String hLimitEquity = tdElements.get(7).text();
					String reason = tdElements.get(8).text();
					tycCompanyCommonstockCrawler.setaAllEquity(ReportDataFormat.bigUnitChange(aAllEquity));
					tycCompanyCommonstockCrawler
							.setaCirculationEquity(ReportDataFormat.bigUnitChange(aCirculationEquity));
					tycCompanyCommonstockCrawler.setaLimitEquity(ReportDataFormat.bigUnitChange(aLimitEquity));
					tycCompanyCommonstockCrawler.setAllEquity(ReportDataFormat.bigUnitChange(allEquity));
					tycCompanyCommonstockCrawler.setCompanyId(companyId);
					tycCompanyCommonstockCrawler.setCompanyName(companyName);
					tycCompanyCommonstockCrawler.setCreatedAt(new Date());
					tycCompanyCommonstockCrawler.sethAllEquity(ReportDataFormat.bigUnitChange(hAllEquity));
					tycCompanyCommonstockCrawler
							.sethCirculationEquity(ReportDataFormat.bigUnitChange(hCirculationEquity));
					tycCompanyCommonstockCrawler.sethLimitEquity(ReportDataFormat.bigUnitChange(hLimitEquity));
					tycCompanyCommonstockCrawler.setReason(reason);
					tycCompanyCommonstockCrawler.setReportDate(stringToDate(reportDate));
					tycCompanyCommonstockCrawler.setStatus((byte) 0);
					tycCompanyCommonstockCrawler.setUpdatedAt(new Date());
					sendJson(tycCompanyCommonstockCrawler, "tyc_company_commonstock");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}
