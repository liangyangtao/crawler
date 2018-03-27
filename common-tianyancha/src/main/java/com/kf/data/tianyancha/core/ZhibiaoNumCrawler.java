package com.kf.data.tianyancha.core;

import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/***
 * 
 * @Title: ZhibiaoNumCrawler.java
 * @Package com.kf.data.tianyancha.core
 * @Description: 读取栏目的数量
 * @author liangyt
 * @date 2017年10月30日 下午2:56:09
 * @version V1.0
 */
public class ZhibiaoNumCrawler {

	/***
	 * 判断每个栏目是否有数据
	 * 
	 * @param zhibiaoNums
	 * @param document
	 */
	public void fillZhibiaoNums(Map<String, Integer> zhibiaoNums, Document document) {
		String infoCssPath = "div.companypage_2017";
		Element infoElement = getNodeByCssPath(document, infoCssPath);
		Elements divElements = new Elements();
		if (infoElement != null) {
			divElements = infoElement.select(".float-left");
		}
		for (Element element : divElements) {
			Elements zhibiaoElements = element.select(".company-nav-item-enable,.company-nav-item-disable");
			for (Element zhibiaoElement : zhibiaoElements) {
				String text = zhibiaoElement.text().trim();
				text = text.replace("+", "");
				text = text.replace(" ", "");
				// 股票行情
				if (text.contains("股票行情")) {
					text = text.replace("股票行情", "");
					if (text.isEmpty()) {
						text = "0";
					}
					// zhibiaoNums.put("gphqCount", Integer.parseInt(text));
				} else if (text.contains("企业简介")) {
					text = text.replace("企业简介", "");
					if (text.isEmpty()) {
						text = "0";
					}
					// zhibiaoNums.put("qyjjCount", Integer.parseInt(text));
				} else if (text.contains("高管信息")) {
					text = text.replace("高管信息", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("ggxxCount", Integer.parseInt(text));
				} else if (text.contains("参股控股")) {
					text = text.replace("参股控股", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("cgkgCount", Integer.parseInt(text));
				} else if (text.contains("上市公告")) {
					text = text.replace("上市公告", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("ssggCount", Integer.parseInt(text));
				} else if (text.contains("十大股东")) {
					text = text.replace("十大股东", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("sdgdCount", Integer.parseInt(text));
				} else if (text.contains("十大流通")) {
					text = text.replace("十大流通", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("sdltCount", Integer.parseInt(text));
				} else if (text.contains("发行相关")) {
					text = text.replace("发行相关", "");
					if (text.isEmpty()) {
						text = "0";
					}
					// zhibiaoNums.put("fxxgCount", Integer.parseInt(text));
				} else if (text.contains("股本结构")) {
					text = text.replace("股本结构", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("commonstockCount", Integer.parseInt(text));
				} else if (text.contains("股本变动")) {
					text = text.replace("股本变动", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("commonstockChangeCount", Integer.parseInt(text));
				} else if (text.contains("分红情况")) {
					text = text.replace("分红情况", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("fhqkCount", Integer.parseInt(text));
				} else if (text.contains("配股情况")) {
					text = text.replace("配股情况", "");
					if (text.isEmpty()) {
						text = "0";
					}
					// zhibiaoNums.put("pgqkCount", Integer.parseInt(text));
				} else if (text.contains("基本信息")) {
					// text = text.replace("基本信息", "");
					if (text.isEmpty()) {
						text = "0";
					}
					// zhibiaoNums.put("jbxxCount", Integer.parseInt(text));
				} else if (text.contains("企业关系")) {

					// text = text.replace("企业关系", "");
					if (text.isEmpty()) {
						text = "0";
					}
					// zhibiaoNums.put("qygxCount", Integer.parseInt(text));
				} else if (text.contains("主要人员")) {
					text = text.replace("主要人员", "");
					if (text.isEmpty()) {
						text = "0";
					}
					// zhibiaoNums.put("zyryCount", Integer.parseInt(text));
				} else if (text.contains("股东信息")) {
					text = text.replace("股东信息", "");
					if (text.isEmpty()) {
						text = "0";
					}
					// zhibiaoNums.put("gdxxCount", Integer.parseInt(text));
				} else if (text.contains("对外投资")) {
					text = text.replace("对外投资", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("investAbroadCount", Integer.parseInt(text));
				} else if (text.contains("变更记录")) {
					text = text.replace("变更记录", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("changeCount", Integer.parseInt(text));
				} else if (text.contains("企业年报")) {
					text = text.replace("企业年报", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("reportCount", Integer.parseInt(text));
				} else if (text.contains("公司年报")) {
					text = text.replace("公司年报", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("reportCount", Integer.parseInt(text));
				} else if (text.contains("分支机构")) {
					text = text.replace("分支机构", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("branchCount", Integer.parseInt(text));
				} else if (text.contains("融资历史")) {
					text = text.replace("融资历史", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("rongziCount", Integer.parseInt(text));
				} else if (text.contains("核心团队")) {
					text = text.replace("核心团队", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("coreTeamCount", Integer.parseInt(text));
				} else if (text.contains("企业业务")) {
					text = text.replace("企业业务", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("businessCount", Integer.parseInt(text));
				} else if (text.contains("投资事件")) {
					text = text.replace("投资事件", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("investCount", Integer.parseInt(text));
				} else if (text.contains("竞品信息")) {
					text = text.replace("竞品信息", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("competitorsCount", Integer.parseInt(text));
				} else if (text.contains("法律诉讼")) {
					text = text.replace("法律诉讼", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("caseCount", Integer.parseInt(text));
				} else if (text.contains("法院公告")) {
					text = text.replace("法院公告", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("caseNoticeCount", Integer.parseInt(text));
				} else if (text.contains("失信人")) {
					text = text.replace("失信人", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("shixinCount", Integer.parseInt(text));
				} else if (text.contains("被执行人")) {
					text = text.replace("被执行人", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("zhixingCount", Integer.parseInt(text));
				} else if (text.contains("开庭公告")) {
					text = text.replace("开庭公告", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("announcementCount", Integer.parseInt(text));
				} else if (text.contains("经营异常")) {
					text = text.replace("经营异常", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("abnormalCount", Integer.parseInt(text));
				} else if (text.contains("行政处罚")) {
					text = text.replace("行政处罚", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("admPenaltyCount", Integer.parseInt(text));
				} else if (text.contains("严重违法")) {
					text = text.replace("严重违法", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("illegalCount", Integer.parseInt(text));
				} else if (text.contains("股权出质")) {
					text = text.replace("股权出质", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("equityCount", Integer.parseInt(text));
				} else if (text.contains("动产抵押")) {
					text = text.replace("动产抵押", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("mortgageCount", Integer.parseInt(text));
				} else if (text.contains("欠税公告")) {
					text = text.replace("欠税公告", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("taxArrearsCount", Integer.parseInt(text));
				} else if (text.contains("司法拍卖")) {
					text = text.replace("司法拍卖", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("sfpmCount", Integer.parseInt(text));
				} else if (text.contains("招投标")) {
					text = text.replace("招投标", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("eventsTenderBidCount", Integer.parseInt(text));
				} else if (text.contains("债券信息")) {
					text = text.replace("债券信息", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("bondCount", Integer.parseInt(text));
				} else if (text.contains("购地信息")) {
					text = text.replace("购地信息", "");
					if (text.isEmpty()) {
						text = "0";
					}
					// zhibiaoNums.put("", Integer.parseInt(text));
				} else if (text.contains("税务评级")) {
					text = text.replace("税务评级", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("taxRatingCount", Integer.parseInt(text));
				} else if (text.contains("招聘")) {
					text = text.replace("招聘", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("recruitCount", Integer.parseInt(text));
				} else if (text.contains("抽查检查")) {
					text = text.replace("抽查检查", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("checkCount", Integer.parseInt(text));
				} else if (text.contains("产品信息")) {
					text = text.replace("产品信息", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("productCount", Integer.parseInt(text));
				} else if (text.contains("进出口信用")) {
					text = text.replace("进出口信用", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("imExPortCount", Integer.parseInt(text));
				} else if (text.contains("资质证书")) {
					text = text.replace("资质证书", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("certificateCount", Integer.parseInt(text));
				} else if (text.contains("微信公众号")) {
					text = text.replace("微信公众号", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("wechatCount", Integer.parseInt(text));
				} else if (text.contains("商标信息")) {
					text = text.replace("商标信息", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("tmCount", Integer.parseInt(text));
				} else if (text.contains("专利")) {
					text = text.replace("专利", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("patentCount", Integer.parseInt(text));
				} else if (text.contains("软件著作权")) {
					text = text.replace("软件著作权", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("softCopyrightCount", Integer.parseInt(text));
				} else if (text.contains("作品著作权")) {
					text = text.replace("作品著作权", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("cpoyRightWorksCount", Integer.parseInt(text));
				} else if (text.contains("网站备案")) {
					text = text.replace("网站备案", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("icpCount", Integer.parseInt(text));
				}
			}

		}

	}

	/***
	 * 根据csspath 获取element
	 * 
	 * @param document
	 * @param cssPath
	 * @return
	 */
	public static Element getNodeByCssPath(Document document, String cssPath) {
		return getNodeByCssPath(document, cssPath, 0);
	}

	/***
	 * 根据csspath 获取第几个 element
	 * 
	 * @param document
	 * @param cssPath
	 * @param index
	 * @return
	 */
	public static Element getNodeByCssPath(Document document, String cssPath, int index) {
		Elements elments = document.select(cssPath);
		if (elments.size() != 0) {
			Element element = elments.get(index);
			return element;
		} else {
			return null;
		}
	}

}
