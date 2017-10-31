package com.kf.data.tianyancha.parser;

import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.mybatis.entity.TycCompanyFinancingCrawler;

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
					String date = tdElements.get(0).text();
					String step = tdElements.get(1).text();
					String valuation = tdElements.get(2).text();
					String investmentAmount = tdElements.get(3).text();
					String investRatio = tdElements.get(4).text();
					String investorName = tdElements.get(5).text();
					String source = tdElements.get(6).text();
					TycCompanyFinancingCrawler tycCompanyFinancingCrawler = new TycCompanyFinancingCrawler();
					tycCompanyFinancingCrawler.setAmount(investmentAmount);
					tycCompanyFinancingCrawler.setCompanyId(companyId);
					tycCompanyFinancingCrawler.setCompanyName(companyName);
					tycCompanyFinancingCrawler.setCreatedAt(new Date());
					tycCompanyFinancingCrawler.setFinancDate(date);
					tycCompanyFinancingCrawler.setInvestors(investorName);
					tycCompanyFinancingCrawler.setProportion(investRatio);
					tycCompanyFinancingCrawler.setRound(step);
					tycCompanyFinancingCrawler.setSource(source);
					tycCompanyFinancingCrawler.setStatus((byte) 0);
					tycCompanyFinancingCrawler.setUpdatedAt(new Date());
					tycCompanyFinancingCrawler.setValuation(valuation);
					sendJson(tycCompanyFinancingCrawler, "tyc_company_financing");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}
