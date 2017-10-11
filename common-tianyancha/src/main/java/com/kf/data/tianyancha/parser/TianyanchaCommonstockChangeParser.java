package com.kf.data.tianyancha.parser;

import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.fetcher.tools.ReportDataFormat;
import com.kf.data.mybatis.entity.TycCompanyCommonstockChangeCrawler;

/***
 * 
 * @Title: TianyanchaCommonstockChangeParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 天眼查股本变动信息解析
 * @author liangyt
 * @date 2017年9月30日 下午3:01:32
 * @version V1.0
 */
public class TianyanchaCommonstockChangeParser extends TianyanchaBasePaser {

	/***
	 * 天眼查股本变动信息解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_equityChange");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					String date = tdElements.get(0).text();
					String reason = tdElements.get(1).text();
					String aAllEquity = tdElements.get(2).text();
					String aCirculationEquity = tdElements.get(3).text();
					String aLimitEquity = tdElements.get(4).text();
					TycCompanyCommonstockChangeCrawler tycCompanyCommonstockChangeCrawler = new TycCompanyCommonstockChangeCrawler();
					tycCompanyCommonstockChangeCrawler.setaAllEquity(ReportDataFormat.bigUnitChange(aAllEquity));
					tycCompanyCommonstockChangeCrawler
							.setaCirculationEquity(ReportDataFormat.bigUnitChange(aCirculationEquity));
					tycCompanyCommonstockChangeCrawler.setaLimitEquity(ReportDataFormat.bigUnitChange(aLimitEquity));
					tycCompanyCommonstockChangeCrawler.setCompanyId(companyId);
					tycCompanyCommonstockChangeCrawler.setCompanyName(companyName);
					tycCompanyCommonstockChangeCrawler.setCreatedAt(new Date());
					tycCompanyCommonstockChangeCrawler.setDate(stringToDate(date));
					tycCompanyCommonstockChangeCrawler.setReason(reason);
					tycCompanyCommonstockChangeCrawler.setStatus((byte) 0);
					tycCompanyCommonstockChangeCrawler.setUpdatedAt(new Date());
					sendJson(tycCompanyCommonstockChangeCrawler, "tyc_company_commonstock_change");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}
