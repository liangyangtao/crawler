package com.kf.data.pdfparser.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kf.data.mybatis.entity.PdfCodeTable;
import com.kf.data.mybatis.entity.PdfReportLinks;

/**
 * @Title: KFPdfParser.java
 * @Package com.kf.data.pdf2html
 * @Description: 公转书解析
 * @author liangyt
 * @date 2017年5月22日 下午5:10:13
 * @version V1.0
 */
public class KfPdfParser {

	static String[] titleTags = new String[] { "一)", "二)", "三)", "四)", "五)", "六)", "七)", "八)", "九)", "一）", "二）", "三）",
			"四）", "五）", "六）", "七）", "八）", "九）", "1)", "2)", "3)", "4)", "5)", "6)", "7)", "8)", "9)", "1）", "2）", "3）",
			"4）", "5）", "6）", "7）", "8）", "9）", "第一节", "第二节", "第三节", "第四节", "第五节", "第六节", "第七节", "第八节", "第九节", "第十节",
			"第十一节", "第十二节", "第十三节", "第十四节", "第十五节", "一、", "二、", "三、", "四、", "五、", "六、", "七、", "八、", "九、", "十、" };

	// 风险提示
	PublicRiskParser publicRiskParser = new PublicRiskParser();
	// 商业模式
	PublicBusinessModelParser publicBusinessModelParser = new PublicBusinessModelParser();
	// 股东
	PublicShareholdersParser publicShareholdersParser = new PublicShareholdersParser();
	// 主营业务
	PublicMainBusinessParser publicMainBusinessParser = new PublicMainBusinessParser();
	//
	PublicLiabilitiesParser publicLiabilitiesParser = new PublicLiabilitiesParser();
	PublicProfitParser publicProfitParser = new PublicProfitParser();
	PublicCashParser publicCashParser = new PublicCashParser();
	// 主要客户
	PublicMajorClientParser publicMajorClientParser = new PublicMajorClientParser();

	// 主要供应商
	PublicMajorSupplierParser publicMajorSupplierParser = new PublicMajorSupplierParser();
	// 特许经营权
	PublicFranchiseParser publicFranchiseParser = new PublicFranchiseParser();

	/****
	 * 
	 * @param pdfCodeTable
	 * @param pdfReportLinks
	 * @param document
	 * @return
	 */
	public String parserPdfHtmlByPdfTypeAndLink(PdfCodeTable pdfCodeTable, PdfReportLinks pdfReportLinks,
			Document document) {
		if (pdfReportLinks.getReportDate() == null) {
			pdfReportLinks.setReportDate(new Date());
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			// 83 公转书_证书 pdf_public_certificate 0
			// 84 公转书_特许经营权 pdf_public_franchise 0

			// 53 公转书_母公司资产负债表 pdf_public_liabilities_parent 0
			// 55 公转书_母公司现金流量表 pdf_public_cash_parent 0
			// 54 公转书_母公司利润表 pdf_public_profit_parent 0
			// 47 完成
			// 48 公转书_合并资产负债表 pdf_public_liabilities 0
			// 52 公转书_合并现金流量表 pdf_public_cash 0
			// 51 公转书_合并利润表 pdf_public_profit 0

			// 公转书_股东 pdf_public_shareholders 完成
			// 公转书_商业模式 pdf_public_business_model 完成
			// 公转书_主营业务 pdf_public_main_business 完成
			// 公转书_合并利润表 pdf_public_profit 完成
			// 公转书_合并现金流量表 pdf_public_cash 完成
			// 公转书_合并资产负债表 pdf_public_liabilities 完成
			// 公转书_风险提示 pdf_public_risk_value 完成

			if (pdfCodeTable.getPdfType().equals("公转书_风险提示")) {
				resultMap = publicRiskParser.getResult(pdfCodeTable, pdfReportLinks, document);
			} else if (pdfCodeTable.getPdfType().equals("公转书_商业模式")) {
				resultMap = publicBusinessModelParser.getResult(pdfCodeTable, pdfReportLinks, document);
			} else if (pdfCodeTable.getPdfType().equals("公转书_股东")) {
				resultMap = publicShareholdersParser.getResult(pdfCodeTable, pdfReportLinks, document);
			} else if (pdfCodeTable.getPdfType().equals("公转书_主营业务")) {
				resultMap = publicMainBusinessParser.getResult(pdfCodeTable, pdfReportLinks, document);
			} else if (pdfCodeTable.getPdfType().equals("公转书_合并资产负债表")) {
				resultMap = publicLiabilitiesParser.getResult(pdfCodeTable, pdfReportLinks, document);
			} else if (pdfCodeTable.getPdfType().equals("公转书_合并利润表")) {
				resultMap = publicProfitParser.getResult(pdfCodeTable, pdfReportLinks, document);
			} else if (pdfCodeTable.getPdfType().equals("公转书_合并现金流量表")) {
				resultMap = publicCashParser.getResult(pdfCodeTable, pdfReportLinks, document);
			} else if (pdfCodeTable.getPdfType().equals("公转书_主要客户")) {
				resultMap = publicMajorClientParser.getResult(pdfCodeTable, pdfReportLinks, document);
			} else if (pdfCodeTable.getPdfType().equals("公转书_主要供应商")) {
				resultMap = publicMajorSupplierParser.getResult(pdfCodeTable, pdfReportLinks, document);
			} else if (pdfCodeTable.getPdfType().equals("公转书_特许经营权")) {
				resultMap = publicMajorClientParser.getResult(pdfCodeTable, pdfReportLinks, document);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectToJson(resultMap);

	}

	public List<String> getStrByReg(String pre, String end, String content) {
		content = replacekong(content);
		List<String> results = new ArrayList<String>();
		end = end.replace("(", "\\(");
		end = end.replace(")", "\\)");
		end = end.replace("|", "\\|");
		end = end.replace("[", "\\[");
		end = end.replace("]", "\\]");
		end = end.replace("?", "\\?");
		end = end.replace("!", "\\!");
		end = end.replace(".", "\\.");
		end = end.replace("*", "\\*");
		end = replacekong(end);
		////////////////////////////////
		pre = pre.replace("(", "\\(");
		pre = pre.replace(")", "\\)");
		pre = pre.replace("|", "\\|");
		pre = pre.replace("[", "\\[");
		pre = pre.replace("]", "\\]");
		pre = pre.replace("?", "\\?");
		pre = pre.replace("!", "\\!");
		pre = pre.replace(".", "\\.");
		pre = pre.replace("*", "\\*");
		pre = replacekong(pre);
		String regEx = pre + "((?!" + pre + "|" + end + ").)+(?=" + end + ")";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			String string = matcher.group();
			string = string.replace(pre, "");
			string = string.replace(end, "");
			string = string.trim();
			results.add(string);
		}
		return results;
	}

	protected String objectToJson(Object obj) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		return gson.toJson(obj);
	}

	/****
	 * 
	 * @param str
	 * @param pre
	 * @return
	 */
	public boolean findNeedNode(String str, String pre) {
		char[] ch = str.toCharArray();
		StringBuffer sb = new StringBuffer();
		boolean isExit = false;
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				sb.append(c);
			} else {
				if (sb.toString().equals(pre)) {
					isExit = true;
				}
				sb.delete(0, sb.length());
			}
			if (i == ch.length - 1) {
				if (sb.toString().equals(pre)) {
					isExit = true;
				}
			}

		}
		return isExit;
	}

	/***
	 * 检查 是否是中文
	 * 
	 * @param c
	 * @return
	 */
	public boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				// || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				// || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	/***
	 * 
	 * @param preEnd
	 * @return
	 */
	public String replacekong(String preEnd) {
		preEnd = preEnd.replace("  ", "###");
		preEnd = preEnd.replace(" ", "###");
		preEnd = preEnd.replace("	", "###");
		preEnd = preEnd.replace(" ", "###");
		return preEnd;
	}

	/***
	 * 
	 * @param pre
	 * @param end
	 */
	public void sortPreAndEnd(List<String> pre, List<String> end) {
		for (int i = 0; i < pre.size(); i++) {
			for (int j = i + 1; j < pre.size(); j++) {
				if (pre.get(i).length() < pre.get(j).length()) {
					String temp = pre.get(j);
					pre.set(j, pre.get(i));
					pre.set(i, temp);
					String btemp = end.get(j);
					end.set(j, end.get(i));
					end.set(i, btemp);
				} else if (pre.get(i).length() == pre.get(j).length()) {
					if (!pre.get(i).contains("母公司") && end.get(j).contains("母公司")) {
						String temp = pre.get(j);
						pre.set(j, pre.get(i));
						pre.set(i, temp);
						String btemp = end.get(j);
						end.set(j, end.get(i));
						end.set(i, btemp);
					} else {
						if (end.get(i).length() < end.get(j).length()) {
							String temp = pre.get(j);
							pre.set(j, pre.get(i));
							pre.set(i, temp);
							String btemp = end.get(j);
							end.set(j, end.get(i));
							end.set(i, btemp);
						}
					}

				}
			}
		}

	}

	public String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

}
