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
import com.kf.data.mybatis.entity.PdfCodeTemporary;
import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.pdfparser.jdbc.PdfCodeTemporaryReader;

/**
 * @Title: KFPdfParser.java
 * @Package com.kf.data.pdf2html
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017年5月22日 下午5:10:13
 * @version V1.0
 */
public class KfPdfParser {

	static String megerPdfTypes[] = new String[] { "年报_利润", "年报_负债", "年报_现金", "公转书_合并资产负债表", "公转书_合并利润表", "公转书_合并现金流量表",
			"公转书_母公司资产负债表", "公转书_母公司利润表", "公转书_母公司现金流量表" };

	static String customsTypes[] = new String[] { "公转书_合并资产负债表", "公转书_合并利润表", "公转书_合并现金流量表", "公转书_母公司资产负债表",
			"公转书_母公司利润表", "公转书_母公司现金流量表" };

	static String[] titleTags = new String[] { "一)", "二)", "三)", "四)", "五)", "六)", "七)", "八)", "九)", "一）", "二）", "三）",
			"四）", "五）", "六）", "七）", "八）", "九）", "1)", "2)", "3)", "4)", "5)", "6)", "7)", "8)", "9)", "1）", "2）", "3）",
			"4）", "5）", "6）", "7）", "8）", "9）", "第一节", "第二节", "第三节", "第四节", "第五节", "第六节", "第七节", "第八节", "第九节", "第十节",
			"第十一节", "第十二节", "第十三节", "第十四节", "第十五节", "一、", "二、", "三、", "四、", "五、", "六、", "七、", "八、", "九、", "十、", ":",
			"：" };

	static String[] finances = new String[] { "合并资产负债表", "合并利润表", "合并现金流量表", "合并所有者权益变动表", "合并股东权益变动表", "母公司资产负债表",
			"母公司利润表", "母公司现金流量表", "母公司股东权益变动表", "母公司所有者权益变动表", "资产负债表", "利润表", "现金流量表", "所有者权益变动表", "股东权益变动表", };

	/***
	 * 
	 * @param id
	 * @param pdfType
	 *            年报
	 * @param link
	 * @return
	 */
	public String parserPdfHtmlByPdfTypeAndLink(PdfCodeTable pdfCodeTable, PdfReportLinks pdfReportLinks, Document document) {
		if (pdfReportLinks.getReportDate() == null) {
			pdfReportLinks.setReportDate(new Date());
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			// 读取所有某个类别下的所有解析规则
			List<PdfCodeTemporary> pdfCodeTemporarys = new PdfCodeTemporaryReader()
					.readerPdfCodeTemporaryByPdfType(pdfCodeTable.getPdfType());
			if (pdfCodeTemporarys.size() == 0) {
				resultMap.put("state", "error");
			} else {
				/////////////////////////////////////////
				List<PdfCodeTemporary> pdfCodeTemporarys1 = new ArrayList<PdfCodeTemporary>();
				List<PdfCodeTemporary> pdfCodeTemporarys2 = new ArrayList<PdfCodeTemporary>();
				List<PdfCodeTemporary> pdfCodeTemporarys3 = new ArrayList<PdfCodeTemporary>();
				List<PdfCodeTemporary> pdfCodeTemporarys4 = new ArrayList<PdfCodeTemporary>();
				for (PdfCodeTemporary pdfCodeTemporary : pdfCodeTemporarys) {
					if (pdfCodeTemporary.getCodeType() == 1) {
						pdfCodeTemporarys1.add(pdfCodeTemporary);
					} else if (pdfCodeTemporary.getCodeType() == 2) {
						pdfCodeTemporarys2.add(pdfCodeTemporary);
					} else if (pdfCodeTemporary.getCodeType() == 3) {
						pdfCodeTemporarys3.add(pdfCodeTemporary);
					} else if (pdfCodeTemporary.getCodeType() == 4) {
						pdfCodeTemporarys4.add(pdfCodeTemporary);
					}
				}
				if (pdfCodeTemporarys1.size() > 0) {
					resultMap = new PdfTemporary1Parser().parserDocument(pdfCodeTable, pdfReportLinks, document,
							pdfCodeTemporarys1);
				}
				if (pdfCodeTemporarys2.size() > 0) {
					resultMap = new PdfTemporary2Parser().parserDocument(pdfCodeTable, pdfReportLinks, document,
							pdfCodeTemporarys2);
				}
				if (pdfCodeTemporarys3.size() > 0) {
					resultMap = new PdfTemporary3Parser().parserDocument(pdfCodeTable, pdfReportLinks, document,
							pdfCodeTemporarys3);
				}
				if (pdfCodeTemporarys4.size() > 0) {
					resultMap = new PdfTemporary4Parser().parserDocument(pdfCodeTable, pdfReportLinks, document,
							pdfCodeTemporarys4);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectToJson(resultMap);

	}

	public String formatvalue(String result2) {
		result2 = result2.replace("###", " ");
		result2 = result2.replace("\"", "");
		result2 = result2.replace("“", "");
		result2 = result2.replace("”", "");
		result2 = result2.replace("，", "");
		result2 = result2.replace(",", "");
		result2 = result2.replace("：", "");
		result2 = result2.replace(":", "");
		return result2;
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
