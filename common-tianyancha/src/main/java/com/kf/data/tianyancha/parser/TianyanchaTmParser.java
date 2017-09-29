package com.kf.data.tianyancha.parser;

import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.fetcher.tools.AliOssSender;
import com.kf.data.mybatis.entity.TycCompanyTrademarkCrawler;

public class TianyanchaTmParser extends TianyanchaBasePaser {
	// <!-- 商标信息 -->
	// <!-- ngIf: items2.tmCount.show&&dataItemCount.tmCount>0 -->
	public static final String cssPath = "div[ng-if=items2.tmCount.show&&dataItemCount.tmCount>0]";
	public static final String bodyCssPath = "div[ng-if=items2.icpCount.show&&dataItemCount.icpCount>0]";
	public static final String listCssPath = "tr[ng-repeat=check in changeinfoList.result]";
	public static final String pageTotalCssPath = ".total";

	public void paseNode(Document document, String companyName, String companyId) {

		Elements contentNodes = document.select("#_container_tmInfo");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");

					TycCompanyTrademarkCrawler tycCompanyTrademarkCrawler = new TycCompanyTrademarkCrawler();

					String trademark_type = tdElements.get(4).text();

					String application_date = tdElements.get(0).text();
					application_date =application_date.replace("-", "");
					String trademark_status = null;
					try {
						trademark_status = tdElements.get(5).text();
					} catch (Exception e) {
					}
					String registration_number = tdElements.get(3).text();
					String trademark_name = null;
					try {
						trademark_name = tdElements.get(2).text();
					} catch (Exception e) {
					}
					String trademark_img_url = tdElements.get(1).select("img").first().attr("src");
					String kfImgUrl = new AliOssSender().uploadObject(trademark_img_url);
					if (kfImgUrl.startsWith("https://") || kfImgUrl.startsWith("http://")) {

					} else {
						kfImgUrl = "https:" + kfImgUrl;
					}
					tycCompanyTrademarkCrawler.setCreatedAt(new Date());
					tycCompanyTrademarkCrawler.setCompanyId(companyId);
					tycCompanyTrademarkCrawler.setCompanyName(companyName);
					tycCompanyTrademarkCrawler.setTrademarkType(trademark_type);
					tycCompanyTrademarkCrawler.setApplicationDate(application_date);
					tycCompanyTrademarkCrawler.setTrademarkStatus(trademark_status);
					tycCompanyTrademarkCrawler.setRegistrationNumber(registration_number);
//					tycCompanyTrademarkCrawler.setTrademarkName(trademark_name);
					tycCompanyTrademarkCrawler.setTrademarkImgUrl(trademark_img_url);
//					tycCompanyTrademarkCrawler.setKfImgUrl(kfImgUrl);
					tycCompanyTrademarkCrawler.setTrademarkId("trademark_id");
					tycCompanyTrademarkCrawler.setStatus(false);
					sendJson(tycCompanyTrademarkCrawler, "tyc_company_trademark");
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}

}
