package com.kf.data.tianyancha.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.mybatis.entity.TycCompanyCopyrightCrawler;

/****
 * 
 * @Title: TianyanchaCpoyRightWorksParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017年9月29日 下午3:07:00
 * @version V1.0
 */
public class TianyanchaCpoyRightWorksParser extends TianyanchaBasePaser {
	// <!--软件著作权-->
	// <!-- ngIf: items2.cpoyRCount.show&&dataItemCount.cpoyRCount>0 -->
	public static final String bodyCssPath = "div[ng-if=items2.cpoyRCount.show&&dataItemCount.cpoyRCount>0]";
	public static final String listCssPath = "tr[ng-repeat=check in changeinfoList.result]";
	public static final String pageTotalCssPath = ".total";

	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_copyrightWorks");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					if (tdElements.size() == 6) {
						String name = tdElements.get(0).text().trim();
						String registration_number = tdElements.get(1).text().trim();
						String type = tdElements.get(2).text().trim();
						String create_date = tdElements.get(3).text().trim();
						create_date = create_date.replace("-", "");
						String register_date = tdElements.get(4).text().trim();
						register_date = register_date.replace("-", "");
						String published_date = tdElements.get(5).text().trim();
						published_date = published_date.replace("-", "");
						TycCompanyCopyrightCrawler tycCompanyCopyrightCrawler = new TycCompanyCopyrightCrawler();
						tycCompanyCopyrightCrawler.setCompanyId(companyId);
						tycCompanyCopyrightCrawler.setCompanyName(companyName);
						tycCompanyCopyrightCrawler.setRegisterDate(register_date);
						tycCompanyCopyrightCrawler.setPublishedDate(published_date);
						tycCompanyCopyrightCrawler.setName(name);
						tycCompanyCopyrightCrawler.setShortname(name);
						tycCompanyCopyrightCrawler.setRegistrationNumber(registration_number);
						tycCompanyCopyrightCrawler.setType(type);
						tycCompanyCopyrightCrawler.setCreateDate(create_date);
						tycCompanyCopyrightCrawler.setStatus(false);
						sendJson(tycCompanyCopyrightCrawler, "tyc_company_copyright");

					}

				} catch (Exception e) {
					continue;
				}

			}
		}

	}

}