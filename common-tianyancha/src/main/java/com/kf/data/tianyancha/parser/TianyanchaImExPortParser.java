package com.kf.data.tianyancha.parser;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.mybatis.entity.TycCompanyImExPortCrawler;

import net.sf.json.JSONObject;

/***
 * 
 * @Title: TianyanchaImExPortParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 进出口信息解析
 * @author liangyt
 * @date 2017年9月30日 下午3:09:14
 * @version V1.0
 */

public class TianyanchaImExPortParser extends TianyanchaBasePaser {

	/***
	 * 进出口信息解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_importAndExport");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					if (tdElements.size() == 4) {
						String text = tdElements.get(3).select("span").attr("onclick");
						text = StringUtils.substringBetween(text, "openImportAndExportPopup({", "})");
						text = "{" + text + "}";
						JSONObject fromObj = JSONObject.fromObject(text);
						JSONObject baseInfo = fromObj.getJSONObject("baseInfo");
						// openImportAndExportPopup(
						// {"creditRating":[{"creditRating":"一般认证企业","authenticationCode":"","identificationTime":"2012-12-27"}],
						// "baseInfo":{"industryCategory":"建材批发","annualReport":"超期未报送","status":"注销","administrativeDivision":"北京市海淀区","managementCategory":"进出口收发货人",
						// "customsRegisteredAddress":"北京关区","validityDate":"2016-07-31","economicDivision":"一般经济区域","recordDate":"1999-12-16","crCode":"1108910143","specialTradeArea":"非特殊区域","types":""},"sanction":[{"penaltyDate":"2016-03-28","decisionNumber":"09460322016000132","party":"中建材集团进出口公司","natureOfCase":""},{"penaltyDate":"2014-08-28","decisionNumber":"60306022013234346","party":"中建材集团进出口公司","natureOfCase":"违规"},{"penaltyDate":"2014-02-20","decisionNumber":"22307022014265641","party":"中建材集团进出口公司","natureOfCase":""},{"penaltyDate":"2014-02-20","decisionNumber":"22307022014265650","party":"中建材集团进出口公司","natureOfCase":""},{"penaltyDate":"2014-02-20","decisionNumber":"22307022014265665","party":"中建材集团进出口公司","natureOfCase":""},{"penaltyDate":"2013-12-11","decisionNumber":"53311022013236863","party":"中建材集团进出口公司","natureOfCase":"违规"},{"penaltyDate":"2013-03-04","decisionNumber":"42311022013154519","party":"中建材集团进出口公司","natureOfCase":""},{"penaltyDate":"2013-03-04","decisionNumber":"42311022013155258","party":"中建材集团进出口公司","natureOfCase":""}]})

						String registerDate = baseInfo.getString("recordDate");
						String registerNumber = baseInfo.getString("crCode");
						String registerOrg = baseInfo.getString("customsRegisteredAddress");
						String adminPrecincts = baseInfo.getString("administrativeDivision");
						String economyPrecincts = baseInfo.getString("economicDivision");
						String operateType = baseInfo.getString("managementCategory");
						String specialPrecincts = baseInfo.getString("specialTradeArea");
						String industryType = baseInfo.getString("industryCategory");
						String declareDate = baseInfo.getString("validityDate");
						String identifier = baseInfo.getString("status");
						String reportStatus = baseInfo.getString("annualReport");
						String electronicType = baseInfo.getString("types");
						TycCompanyImExPortCrawler tycCompanyImExPortCrawler = new TycCompanyImExPortCrawler();
						tycCompanyImExPortCrawler.setAdminPrecincts(adminPrecincts);
						tycCompanyImExPortCrawler.setCompanyId(companyId);
						tycCompanyImExPortCrawler.setCompanyName(companyName);
						tycCompanyImExPortCrawler.setCreatedAt(new Date());
						tycCompanyImExPortCrawler.setDeclareDate(stringToDate(declareDate));
						tycCompanyImExPortCrawler.setEconomyPrecincts(economyPrecincts);
						tycCompanyImExPortCrawler.setElectronicType(electronicType);
						tycCompanyImExPortCrawler.setIdentifier(identifier);
						tycCompanyImExPortCrawler.setIndustryType(industryType);
						tycCompanyImExPortCrawler.setOperateType(operateType);
						tycCompanyImExPortCrawler.setRegisterDate(registerDate);
						tycCompanyImExPortCrawler.setRegisterNumber(registerNumber);
						tycCompanyImExPortCrawler.setRegisterOrg(registerOrg);
						tycCompanyImExPortCrawler.setReportStatus(reportStatus);
						tycCompanyImExPortCrawler.setSpecialPrecincts(specialPrecincts);
						tycCompanyImExPortCrawler.setStatus((byte) 0);
						tycCompanyImExPortCrawler.setUpdatedAt(new Date());
						sendJson(tycCompanyImExPortCrawler, "tyc_company_im_ex_port");
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		}

	}

}
