package com.kf.data.parser;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/***
 * 
 * @Title: CtripBookParser.java
 * @Package com.kf.data.parser
 * @Description: 解析携程book
 * @author liangyt
 * @date 2017年10月30日 下午4:31:03
 * @version V1.0
 */
public class CtripBookParser extends CtripBaseParser {

	// 解析订单详情
	public void parserBook(Document document, String uuid, String url) {
		Elements sidebarElements = document.select("#sidebar");

		if (sidebarElements.size() > 0) {
			Elements flightPathElements = sidebarElements.first().select(".flight-path");
			if (flightPathElements.size() > 0) {
				Element flightPathElement = flightPathElements.first();
				try {
					String flight_date = getElementTextByCssPath(flightPathElement, ".flt-date");
					String week = getElementTextByCssPath(flightPathElement, ".week");
					if (week != null) {
						flight_date = flight_date.replace(week, "");
					}
					String depart = getElementTextByCssPath(flightPathElement, ".flt-depart");
					String arrive = getElementTextByCssPath(flightPathElement, ".flt-arrive");
					Elements priceInfoElements = sidebarElements.first().select("#priceInfo");
					String price = null;
					String tax = null;
					String cost = null;
					String pack = null;
					if (priceInfoElements.size() > 0) {
						Element priceInfoElement = priceInfoElements.first();
						pack = getElementTextByCssPath(priceInfoElement, ".cost-package");
						cost = getElementTextByCssPath(priceInfoElement, ".total-price");
						Elements costlistElements = priceInfoElement.select("ul > li");
						if (costlistElements.size() >= 2) {
							price = costlistElements.get(0).select(".price").first().text();
							tax = costlistElements.get(1).select(".price").first().text();
						}
					}
					//// 保存数据
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("flight_date", flight_date);
					map.put("uuid", uuid);
					map.put("week", week);
					map.put("depart", depart);
					map.put("arrive", arrive);
					map.put("price", price);
					map.put("tax", tax);
					map.put("cost", cost);
					map.put("pack", pack);
					map.put("url", url);
					sendJson(map, "ctrip_flights_book");
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {

					String airline_name = null;
					String flight_no = null;
					String plane_type = null;
					String flight_seat = null;
					String depart_time = null;
					String depart_airport = null;
					String arrive_time = null;
					String arrive_airport = null;
					String stop_info = null;
					String total_time = null;
					String airlogo = null;
					String actual_ride = null;

					Elements titleElements = flightPathElement.select(".flight-tit:not(.flight-tit-share)");
					Elements detailElements = flightPathElement.select(".flight-detail");
					if (titleElements.size() == detailElements.size()) {

						for (int i = 0; i < titleElements.size(); i++) {
							
							Element element = titleElements.get(i);
							airline_name = getElementTextByCssPath(element, ".flt-airline");
							plane_type = getElementTextByCssPath(element, ".plane-type");
							flight_seat = getElementTextByCssPath(element, ".flt-seat");
							try {
								airlogo = element.select(".flt-airline").first().select("img").first().attr("src");
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							
							Element detailElement = detailElements.get(i);
							Elements departElements = detailElement.select(".flt-depart");
							if (departElements.size() > 0) {
								Element departElement = departElements.first();
								depart_time = getElementTextByCssPath(departElement, ".time");
								depart_airport = getElementTextByCssPath(departElement, ".airport");
							}
							Elements arriveElements = detailElement.select(".flt-arrive");
							if (arriveElements.size() > 0) {
								Element arriveElement = arriveElements.first();
								arrive_time = getElementTextByCssPath(arriveElement, ".time");
								arrive_airport = getElementTextByCssPath(arriveElement, ".airport");
							}
							Elements arrowElements = detailElement.select(".flt-arrow");
							if (arrowElements.size() > 0) {
								Element arrowElement = arrowElements.first();
								total_time = getElementTextByCssPath(arrowElement, ".cost-time");
							}
							try {
								if (element.nextElementSibling().hasClass("flight-tit-share")) {
									Element nexttitleElement = element.nextElementSibling();
									actual_ride = getElementTextByCssPath(nexttitleElement, ".flt-airline");
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("airline_name", airline_name);
					map.put("flight_no", flight_no);
					map.put("plane_type", plane_type);
					map.put("flight_seat", flight_seat);
					map.put("depart_time", depart_time);
					map.put("depart_airport", depart_airport);
					map.put("arrive_time", arrive_time);
					map.put("arrive_airport", arrive_airport);
					map.put("stop_info", stop_info);
					map.put("total_time", total_time);
					map.put("airlogo", airlogo);
					map.put("actual_ride", actual_ride);
					map.put("url", url);
					map.put("uuid", uuid);
					
					sendJson(map, "ctrip_flights_path");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}

	}

}
