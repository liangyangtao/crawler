//package com.kf.data.tianyancha.parser;
//
//import org.apache.commons.lang3.StringUtils;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import com.kf.data.mybatis.entity.TycCompanyCopyrightCrawler;
//
//import net.sf.json.JSONObject;
//
//public class TianyanchaCpoyRParser extends TianyanchaBasePaser {
//	// <!--软件著作权-->
//	// <!-- ngIf: items2.cpoyRCount.show&&dataItemCount.cpoyRCount>0 -->
//	public static final String bodyCssPath = "div[ng-if=items2.cpoyRCount.show&&dataItemCount.cpoyRCount>0]";
//	public static final String listCssPath = "tr[ng-repeat=check in changeinfoList.result]";
//	public static final String pageTotalCssPath = ".total";
//
//	public void paseNode(Document document, String companyName, String companyId) {
//		Elements contentNodes = document.select("#_container_copyright");
//		if (contentNodes.size() > 0) {
//			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
//			for (Element element : nodes) {
//				try {
//					Elements tdElements = element.select("td");
//					if (tdElements.size() == 7) {
//						String text = tdElements.get(6).select("span").attr("onclick");
//						text = StringUtils.substringBetween(text, "openCopyrightPopup({", "})");
//						text = "{" + text + "}";
//						JSONObject obj = JSONObject.fromObject(text);
//						String register_date = obj.getString("regtime");// 登记日期
//						String published_date = obj.getString("publishtime");// 首次发表日期
//						String shortname = "";
//						try {
//							shortname = obj.getString("simplename");// 软件简称
//						} catch (Exception e) {
//							logger.info(companyName + "simplename不存在");
//							// e.printStackTrace();
//						}
//						String registration_number = obj.getString("regnum");// 登记号
//						String classification_num = obj.getString("catnum");// 分类号
//						String name = obj.getString("fullname");// 软件全称
//						String version = obj.getString("version");// 版本号
//						String copyrightOwner = obj.getString("authorNationality");
//						TycCompanyCopyrightCrawler tycCompanyCopyrightCrawler = new TycCompanyCopyrightCrawler();
//						tycCompanyCopyrightCrawler.setCopyrightOwner(copyrightOwner);
//						tycCompanyCopyrightCrawler.setCompanyId(companyId);
//						tycCompanyCopyrightCrawler.setCompanyName(companyName);
//						tycCompanyCopyrightCrawler.setRegisterDate(register_date);
//						tycCompanyCopyrightCrawler.setRegisterDate(dateToString(register_date));
//						tycCompanyCopyrightCrawler.setPublishedDate(dateToString(published_date));
//						tycCompanyCopyrightCrawler.setShortname(shortname);
//						tycCompanyCopyrightCrawler.setRegistrationNumber(registration_number);
//						tycCompanyCopyrightCrawler.setClassificationNum(classification_num);
//						tycCompanyCopyrightCrawler.setName(name);
//						tycCompanyCopyrightCrawler.setVersion(version);
//						sendJson(tycCompanyCopyrightCrawler, "tyc_company_copyright");
//
//					}
//
//				} catch (Exception e) {
//					continue;
//				}
//
//			}
//		}
//
//	}
//
//}