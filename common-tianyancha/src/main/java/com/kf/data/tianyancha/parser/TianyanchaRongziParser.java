package com.kf.data.tianyancha.parser;

import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.fetcher.tools.ReportDataFormat;
import com.kf.data.fetcher.tools.UUIDTools;
import com.kf.data.mybatis.entity.TycEventsInvestCrawler;
import com.kf.data.mybatis.entity.TycEventsInvestInvestorsCrawler;

/***
 * 
 * @Title: TianyanchaRongziParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 融资信息解析
 * @author liangyt
 * @date 2017年9月30日 上午10:21:09
 * @version V1.0
 */
public class TianyanchaRongziParser extends TianyanchaBasePaser {

	/***
	 * 融资信息解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_rongzi");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					TycEventsInvestCrawler tycEventsInvestCrawler = new TycEventsInvestCrawler();
					String date = tdElements.get(0).text();
					date = date.replace("-", "");
					String step = tdElements.get(1).text();
					String valuation = tdElements.get(2).text();
					String investmentAmount = tdElements.get(3).text();
					String investRatio = tdElements.get(4).text();
					String investorName = tdElements.get(5).text();
					String uuid = UUIDTools.getUUID();
					tycEventsInvestCrawler.setDate(date);
					tycEventsInvestCrawler.setStep(step);

					tycEventsInvestCrawler.setValuation(valuation);
					tycEventsInvestCrawler.setInvestmentAmount(investmentAmount);
					tycEventsInvestCrawler.setInvestRatio(ReportDataFormat.doubleValueChange(investRatio));
					tycEventsInvestCrawler.setEventId(uuid);
					tycEventsInvestCrawler.setCompanyName(companyName);
					tycEventsInvestCrawler.setCompanyId(companyId);
					tycEventsInvestCrawler.setCreatedAt(new Date());
					tycEventsInvestCrawler.setUpdatedAt(new Date());
					tycEventsInvestCrawler.setStatus(false);
					sendJson(tycEventsInvestCrawler, "tyc_events_invest");
					if (investorName != null && !investorName.isEmpty()) {
						TycEventsInvestInvestorsCrawler tycEventsInvestInvestorsCrawler = new TycEventsInvestInvestorsCrawler();
						tycEventsInvestInvestorsCrawler.setCreatedAt(new Date());
						tycEventsInvestInvestorsCrawler.setUpdatedAt(new Date());
						tycEventsInvestInvestorsCrawler.setEventId(uuid);
						tycEventsInvestInvestorsCrawler.setInvestorName(investorName);
						sendJson(tycEventsInvestInvestorsCrawler, "tyc_events_invest_investors");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}
