package com.kf.data.parser;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/***
 * 
 * @Title: CtripListParser.java
 * @Package com.kf.data.parser
 * @Description: 携程列表页解析
 * @author liangyt
 * @date 2017年10月30日 下午7:02:20
 * @version V1.0
 */
public class CtripListParser extends CtripBaseParser {

	public void parserList(String url,String uuid,Element fightItem){
	
		Elements fightRowElements = fightItem.select(".flight-row");
		if (fightRowElements.size() > 0) {
			// 航空公司图标
			String airlogo = null;
			Element fightRowElement = fightRowElements.get(0);
			Elements logoImgElements = fightRowElement.select(".airline-logo > img");
			if (logoImgElements.size() > 0) {
				airlogo = logoImgElements.first().attr("src");
			}
			// 航空公司名称
			String airlineName = null;
			Elements airlineNameElements = fightRowElement.select(".airline-name");
			if (airlineNameElements.size() > 0) {
				airlineName = airlineNameElements.first().text();
			}
			// 航空号码 // 飞机类型
			String flightNo = null;
			Elements flightNoElemens = fightRowElement.select(".flight-No");
			if (flightNoElemens.size() > 0) {
				flightNo = flightNoElemens.first().text();
			}
			String planeType = null;
			Elements planeTypeElements = fightRowElement.select(".plane-type");
			if (planeTypeElements.size() > 0) {
				planeType = planeTypeElements.first().text();
				flightNo = flightNo.replace(planeType, "");
			}

			// .flight-detail-depart
			Elements departElements = fightRowElement.select(".flight-detail-depart");
			String departTime = null;
			String departAirport = null;
			if (departElements.size() > 0) {

				// 出发时间

				Elements departTimeElements = departElements.first().select(".flight-detail-time");
				if (departTimeElements.size() > 0) {
					departTime = departTimeElements.first().text();
				}
				// 出发机场

				Elements departAirportElements = departElements.first().select(".flight-detail-airport");
				if (departAirportElements.size() > 0) {
					departAirport = departAirportElements.first().text();
				}
			}
			Elements returnElements = fightRowElement.select(".flight-detail-return");
			String returnTime = null;
			String returnAirport = null;
			if (returnElements.size() > 0) {
				// 到达时间 到达机场

				Elements returnTimeElements = returnElements.first().select(".flight-detail-time");
				if (returnTimeElements.size() > 0) {
					returnTime = returnTimeElements.first().text();
				}

				Elements returnAirportElements = returnElements.first().select(".flight-detail-airport");
				if (returnAirportElements.size() > 0) {
					returnAirport = returnAirportElements.first().text();
				}
			}
			// 飞行总时长
			String totalTime = null;
			Elements totalTimeElements = fightRowElement.select(".flight-total-time");
			if (totalTimeElements.size() > 0) {
				totalTime = totalTimeElements.first().text();
			}
			//
			String stopInfo = null;
			Elements stopInfoElements = fightRowElement.select(".flight-stop-info");
			if (stopInfoElements.size() > 0) {
				stopInfo = stopInfoElements.first().text();
			}
			try {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("url", url);
				map.put("uuid", uuid);
				map.put("airline_name", airlineName);
				map.put("flight_no", flightNo);
				map.put("plane_type", planeType);
				map.put("depart_time", departTime);
				map.put("depart_airport", departAirport);
				map.put("return_time", returnTime);
				map.put("return_airport", returnAirport);
				map.put("stop_info", stopInfo);
				map.put("total_time", totalTime);
				map.put("airlogo", airlogo);
				sendJson(map, "ctrip_flights");
				logger.info("保存数据");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
