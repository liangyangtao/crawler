package com.kf.data.approved.parser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.approved.store.NeeqCompanyChoiceLayerOnlineReader;
import com.kf.data.approved.store.NeeqCompanyMainBusinessOnlineReader;
import com.kf.data.approved.store.NeeqCompanyOnlineReader;
import com.kf.data.approved.store.NeeqIpoDetectOnlineReader;
import com.kf.data.fetcher.tools.ReportDataFormat;
import com.kf.data.fetcher.tools.UUIDTools;
import com.kf.data.mybatis.entity.NeeqCompanyChoiceLayerOnline;
import com.kf.data.mybatis.entity.NeeqCompanyMainBusinessOnline;
import com.kf.data.mybatis.entity.NeeqCompanyOnline;
import com.kf.data.mybatis.entity.NeeqIpoDetectOnline;
import com.kf.data.mybatis.entity.PdfReportLinks;

/****
 * 
 * @Title: AchievementPreviewParser.java
 * @Package com.kf.data.approved.parser
 * @Description: 业绩预告
 * @author liangyt
 * @date 2017年12月15日 下午2:53:00
 * @version V1.0
 */
public class AchievementPreviewParser extends BaseParser {

	NeeqCompanyMainBusinessOnlineReader neeqCompanyMainBusinessOnlineReader = new NeeqCompanyMainBusinessOnlineReader();
	NeeqCompanyChoiceLayerOnlineReader neeqCompanyChoiceLayerOnlineReader = new NeeqCompanyChoiceLayerOnlineReader();
	NeeqCompanyOnlineReader neeqCompanyOnlineReader = new NeeqCompanyOnlineReader();
	NeeqIpoDetectOnlineReader neeqIpoDetectOnlineReader = new NeeqIpoDetectOnlineReader();
	ReportDataFormat reportDataFormat = new ReportDataFormat();

	public void parserContent(String html, PdfReportLinks pdfReportLink) {
		String income = null;
		String income_pre = null;
		String income_radio = null;
		String shareholder_net_profit = null;
		String shareholder_net_profit_pre = null;
		String shareholder_net_profit_radio = null;
		Document document = Jsoup.parse(html);
		String period = parsePeriod(html);
		String reason = parseReason(html);

		// 1     公告编号：2017-083
		if (reason != null) {
			reason = reason.replaceAll("1公告编号：\\d{4}-\\d{3}", "");
			reason = reason.replaceAll("1[  | |	| | ]*公告编号：\\d{4}-\\d{3}", "");
			reason = reason.replaceAll("1[  | |	| | ]*公告编号：\\d{4}一\\d{3}", "");
			reason = reason.replaceAll("公告编号：\\d{4}-\\d{3}", "");
			reason = reason.replaceAll("公告编号：\\d{4}一\\d{3}", "");
		}
		Elements tables = document.select("table");
		for (Element table : tables) {
			Elements trElements = table.select("tr");
			String firstTrText = trElements.first().text();
			firstTrText = replacekong(firstTrText);
			if ((firstTrText.contains("项目") || firstTrText.contains("科目")) && firstTrText.contains("期")
					&& (firstTrText.contains("本") || firstTrText.contains("上") || firstTrText.contains("预"))
					&& firstTrText.contains("比")) {
				Elements firstTdElements = trElements.first().select("td");
				if (firstTdElements.size() != 4) {
					continue;
				}
				for (int j = 1; j < trElements.size(); j++) {
					Element trElement = trElements.get(j);
					Elements tdElements = trElement.select("td");
					if (tdElements.size() == 0) {
						continue;
					}
					for (int k = 1; k < tdElements.size(); k++) {
						// String topkey = firstTdElements.get(k).text().trim();
						String leftkey = tdElements.get(0).text().trim();
						leftkey = replacekong(leftkey);
						String value = tdElements.get(k).text().trim();
						if (leftkey.contains("营业收入")) {
							if (k == 1) {
								income = value;
								if (leftkey.contains("万")) {
									income = income + "(万)";
								}
							} else if (k == 2) {
								income_pre = value;
								if (leftkey.contains("万")) {
									income_pre = income_pre + "(万)";
								}
							} else if (k == 3) {
								income_radio = value;
							}
						}
						if (leftkey.contains("净利润")) {
							if (k == 1) {
								shareholder_net_profit = value;
								if (leftkey.contains("万")) {
									shareholder_net_profit = shareholder_net_profit + "(万)";
								}
							} else if (k == 2) {
								shareholder_net_profit_pre = value;
								if (leftkey.contains("万")) {
									shareholder_net_profit_pre = shareholder_net_profit_pre + "(万)";
								}
							} else if (k == 3) {
								shareholder_net_profit_radio = value;
							}

						}
					}
				}
				break;
			}

		}
		income = formatMoneyValue(income);
		income_pre = formatMoneyValue(income_pre);
		shareholder_net_profit = formatMoneyValue(shareholder_net_profit);
		shareholder_net_profit_pre = formatMoneyValue(shareholder_net_profit_pre);
		income_radio = formatRadioValue(income_radio);
		shareholder_net_profit_radio = formatRadioValue(shareholder_net_profit_radio);
		if (period != null) {
			period = replacekong(period);
		}
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("company_name", pdfReportLink.getCompanyName());
			map.put("notice_id", pdfReportLink.getNoticeId() + "");
			map.put("link", pdfReportLink.getLink());
			map.put("stockcode", pdfReportLink.getCompanyId() + "");
			map.put("up_time", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
			map.put("report_date", pdfReportLink.getReportDate());
			map.put("publish_date", pdfReportLink.getPublishDate());
			map.put("period", period);
			map.put("income", income);
			map.put("reason", reason);
			map.put("income_pre", income_pre);
			map.put("shareholder_net_profit", shareholder_net_profit);
			map.put("shareholder_net_profit_pre", shareholder_net_profit_pre);
			map.put("income_radio", income_radio);
			map.put("shareholder_net_profit_radio", shareholder_net_profit_radio);
			sendJson(map, "pdf_company_achievement_preview");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			String year = pdfReportLink.getPublishDate().substring(0, 4);
			if (shareholder_net_profit_radio == null) {
				return;
			}
			String radio = null;
			String radioType = null;
			if (shareholder_net_profit_radio.indexOf("-") > 1) {
				radioType = "预增";
				radio = shareholder_net_profit_radio.split("-")[1];
			} else if (shareholder_net_profit_radio.indexOf("-") == -1) {
				radioType = "预增";
				radio = shareholder_net_profit_radio;
			} else if (shareholder_net_profit_radio.indexOf("-") == 0) {
				radioType = "预减";
				shareholder_net_profit_radio = shareholder_net_profit_radio.replace("-", "");
				radio = shareholder_net_profit_radio;
			}
			try {
				String radioTemp = radio.replace("%", "");
				double radioInt = Double.parseDouble(radioTemp);
				if (radioInt > 100.0) {
					java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
					radio = df.format(radioInt / 100) + "倍";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			String title = pdfReportLink.getCompanyName() + "（" + pdfReportLink.getCompanyId() + "）" + year
					+ "年年度业绩预告出炉 净利润最高" + radioType + radio;

			// String source = "全国中小企业股份转让系统";

			String subtitle = null;

			subtitle = pdfReportLink.getPublishDate() + "，" + pdfReportLink.getCompanyName() + "（"
					+ pdfReportLink.getCompanyId() + "）发布公告，";
			if (income != null && income_radio != null) {
				subtitle = subtitle + "公司预计" + year + "年全年营业收入为" + income + "，同比增长" + income_radio + "，";
			}
			if (shareholder_net_profit != null) {
				subtitle = subtitle + "预计归属于挂牌公司股东的净利润为" + shareholder_net_profit + "，同比增长"
						+ shareholder_net_profit_radio;
			}
			String business = null;
			List<NeeqCompanyMainBusinessOnline> neeqCompanyMainBusinessOnlines = neeqCompanyMainBusinessOnlineReader
					.readNeeqCompanyMainBusinessByCode(pdfReportLink.getCompanyId() + "");
			if (neeqCompanyMainBusinessOnlines.size() > 0) {
				if (neeqCompanyMainBusinessOnlines.get(0).getMainBusiness() != null) {
					business = pdfReportLink.getCompanyName() + "公司主要业务为"
							+ neeqCompanyMainBusinessOnlines.get(0).getMainBusiness();
				}
			}
			String showReason = null;

			if (reason != null) {
				showReason = "公告显示，" + reason;
			}
			String choiceLayer = null;
			List<NeeqCompanyChoiceLayerOnline> neeqCompanyChoiceLayerOnlines = neeqCompanyChoiceLayerOnlineReader
					.readNeeqCompanyChoiceLayerByCode(pdfReportLink.getCompanyId() + "");
			if (neeqCompanyChoiceLayerOnlines.size() > 0) {
				choiceLayer = pdfReportLink.getCompanyName() + "是因业务增长迅速，公司治理规范，为三板慧精选企业";
			}
			List<NeeqIpoDetectOnline> neeqIpoDetectOnlines = neeqIpoDetectOnlineReader
					.readNeeqIpoDetectOnlineByCode(pdfReportLink.getCompanyId() + "");
			String ipo = null;
			if (neeqIpoDetectOnlines.size() > 0) {
				if (neeqIpoDetectOnlines.get(0).getDtStart() == null) {

				} else {
					String ipoDate = new SimpleDateFormat("yyyy年MM月dd")
							.format(neeqIpoDetectOnlines.get(0).getDtStart());
					String specialName = null;
					List<NeeqCompanyOnline> neeqCompanyOnlines = neeqCompanyOnlineReader
							.readNeeqCompanyByCode(pdfReportLink.getCompanyId() + "");
					if (neeqCompanyOnlines.size() > 0) {
						specialName = neeqCompanyOnlines.get(0).getSpecialName();
					}
					ipo = pdfReportLink.getCompanyName() + "于" + ipoDate + "宣布上市辅导，辅导券商为" + specialName;
				}
			}

			StringBuffer textBuffer = new StringBuffer();
			if (subtitle != null) {
				subtitle = reportDataFormat.trimStr(subtitle);
				if (subtitle.endsWith("。")) {
					textBuffer.append("<p>" + subtitle + "</p>");
				} else {
					textBuffer.append("<p>" + subtitle + "。</p>");
				}
			}
			if (business != null) {
				business = reportDataFormat.trimStr(business);

				if (business.endsWith("。")) {
					textBuffer.append("<p>" + business + "</p>");
				} else {
					textBuffer.append("<p>" + business + "。</p>");
				}
			}
			if (showReason != null) {
				showReason = reportDataFormat.trimStr(showReason);
				if (showReason.endsWith("。")) {
					textBuffer.append("<p>" + showReason + "</p>");
				} else {
					textBuffer.append("<p>" + showReason + "。</p>");
				}
			}

			if (choiceLayer != null) {
				choiceLayer = reportDataFormat.trimStr(choiceLayer);
				if (choiceLayer.endsWith("。")) {
					textBuffer.append("<p>" + choiceLayer + "</p>");
				} else {
					textBuffer.append("<p>" + choiceLayer + "。</p>");
				}
			}
			if (ipo != null) {
				ipo = reportDataFormat.trimStr(ipo);
				if (ipo.endsWith("。")) {
					textBuffer.append("<p>" + ipo + "</p>");
				} else {
					textBuffer.append("<p>" + ipo + "。</p>");
				}

			}
			// Map<String, Object> map = new HashMap<String, Object>();
			// map.put("title", title);
			// map.put("source", source);
			// map.put("text", textBuffer.toString());
			// map.put("stockcode", pdfReportLink.getCompanyId() + "");
			// map.put("publish_date", pdfReportLink.getPublishDate());
			// sendJson(map, "pdf_company_achievement_preview_text");
			String uuid = UUIDTools.getUUID();
			Map<String, Object> map = new HashMap<String, Object>();
			String text = textBuffer.toString();
			text = text.replace("	", " ");
			text = text.replace("　", " ");
			map.put("author", "");
			map.put("content", text);
			map.put("publishDate", pdfReportLink.getPublishDate());
			map.put("source", "三板慧");
			map.put("itext", "");
			map.put("title", title);
			map.put("type", "机器新闻");
			map.put("url", uuid);
			map.put("createtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			map.put("crawler_source", "三板慧");
			map.put("uuid", uuid);
			System.out.println(text);
//			sendJson(map, "cnfol_news");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/***
	 * 获取预告期间
	 * 
	 * @param html
	 * @return
	 */
	public String parsePeriod(String html) {
		Document document = Jsoup.parse(html);
		String text = document.text();
		String regEx = "[^：|间|:]+?日.+?日";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			String string = matcher.group();
			return string;
		}
		return null;
	}

	/***
	 * 
	 * @param html
	 * @return
	 */
	public String parseReason(String html) {
		Document document = Jsoup.parse(html);
		String text = document.text();
		String regEx = "(?<=业绩变动.{2}说明).+?(?=四、)";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			String string = matcher.group();
			return string;
		}
		return null;
	}

	public static void main(String[] args) {
		String text = "六、非经常性损益占净利润比重较高的风险报告期内，公司的非经常性损益对净利润的影响较大。2015 年、2016 年，公司非经常性损益分别为 2,851,826.08 元、13,597,430.80 元，公司合并净利润分别为 284,219.74 元、14,161,132.09 元，非经常性损益占当期的净利润比例较大。非经常性损益对公司的经营业绩影响较大，主要系投资收益、同一控制下企业合并被合并方合并前净利润、子公司处置收益、计入当期损益政府补助金额较大导致。未来公司是否还将继续取得政府补助与政府补助政策密切相关，存在不确定性。公司经营业绩对非经常性损益依赖程度较高，存在非经常性损益占净利润比较高的风险。七、子公司租赁房屋存在潜在权属纠纷的风险公司子公司上海益钢仓储有限公司向上海宝钢物流有限公司租赁的仓库位于长江路 255 号地块上，经查询该地块的房地产权证，未发现房屋信息，且出租方上海宝钢物流有限公司未能提供相关证明材料，因房屋建成时间较早，已无法取得相关产权凭证及转让资料，上述房屋产权权属存在一定瑕疵和潜在风险。但上述仓库占长江路 255 号地块的面积约 13%，经测算上述仓库 2016 年全年取得的收入为人民币 204.14 万元，占公司当期总营业收入的 0.54%。若未来上述房产一旦发生权属纠纷导致公司无法继续使用，对公司业务的不利影响较小，且益钢仓储的仓库存储方式较为简单，内部无需大型建设即可使用，公司寻找替代仓库较为容易。虽然益钢仓储向上海宝钢物流有限公司租赁的位于长江路 255 号地块的仓库的产权权属存在一定瑕疵，但上述风险对公司持续经营的不利影响很小。 ";
		String regEx = "[^风险].+?(?=风险)";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			String string = matcher.group();
			System.out.println(string);
		}
		// System.out.println("sdfds.223".lastIndexOf("."));
		// String data = "-aasdfds2.3d";
		// if (data.lastIndexOf(".") > 0) {
		// String temp = data.substring(0, data.lastIndexOf("."));
		// temp = temp.replace(".", "");
		// data = temp + data.substring(data.lastIndexOf("."));
		// }
		// System.out.println(data.length() - data.lastIndexOf("."));
	}

}
