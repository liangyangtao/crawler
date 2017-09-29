package com.kf.data.tianyancha.parser;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kf.data.mybatis.entity.TycCompanyEquityPledgedCrawler;

//import clojure.main;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @TianyanchaEquityParser.java
 * @2017年4月20日
 * @author yinxin
 * @TianyanchaEquityParser
 * @注释：股权出质
 */
public class TianyanchaEquityParser extends TianyanchaBasePaser {
	// <!--股权出质-->
	// <!-- ngIf: items2.equityCount.show&&dataItemCount.equityCount>0 -->
	public static final String bodyCssPath = "#_container_equity";
	public static final String listCssPath = ".companyInfo-table > tbody > tr";

	public void paseNode(Document document, String companyName, String companyID) {
		Element bodyElemnt = getNodeByCssPath(document, bodyCssPath);
		if (bodyElemnt == null) {
			return;
		}
		Elements nodes = bodyElemnt.select(listCssPath);
		for (Element element : nodes) {
			Elements tdElements = element.select("td");
			Element tdElement = tdElements.get(5);
			String onclick = tdElement.select("span").first().attr("onclick");
			// openEquityPopup({"equityAmount":"50
			// 万元","regNumber":"110108002734659_0003","pledgee":"百度在线网络技术（北京）有限公司","regDate":1494259200000,"base":"bj","state":"注销","pledgor":"王湛","certifNumberR":"非公示项","certifNumber":"非公示项"})
			onclick = StringUtils.substringBetween(onclick, "openEquityPopup({", "})");
			onclick = "{" + onclick + "}";
			JSONObject obj = JSONObject.fromObject(onclick);
			// 出质股权数额
			String equityAmount = obj.getString("equityAmount");
			// 登记编号
			String regNumber = obj.getString("regNumber");
			// 状态
			String state = obj.getString("state");
			// 出质人
			String pledgor = obj.getString("pledgor");
			// 质权人号码
			String certifNumberR = obj.getString("certifNumberR");
			// 质权人
			String pledgee = obj.getString("pledgee");
			// 登记日
			String regDate = obj.getString("regDate");

			Date date = timestampToDate(regDate);
			TycCompanyEquityPledgedCrawler tycCompanyEquityPledged = new TycCompanyEquityPledgedCrawler();
			tycCompanyEquityPledged.setCompanyId(companyID);
			tycCompanyEquityPledged.setCompanyName(companyName);
			tycCompanyEquityPledged.setPldNum(equityAmount);
			tycCompanyEquityPledged.setRegisterNumber(regNumber);
			tycCompanyEquityPledged.setPldStatus(state);
			tycCompanyEquityPledged.setPldName(pledgor);
			tycCompanyEquityPledged.setPledgeeNumber(certifNumberR);
			tycCompanyEquityPledged.setPledgee(pledgee);
			tycCompanyEquityPledged.setDtNotice(date);
			tycCompanyEquityPledged.setCreatedAt(new Date());
			sendJson(tycCompanyEquityPledged, "tyc_company_equity_pledged");
		}

	}
}
