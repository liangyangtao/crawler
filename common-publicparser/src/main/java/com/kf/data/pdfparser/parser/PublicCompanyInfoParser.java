package com.kf.data.pdfparser.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.mybatis.entity.PdfCodeTable;
import com.kf.data.mybatis.entity.PdfReportLinks;

/****
 * 
 * @Title: PublicShareholdersParser.java
 * @Package com.kf.data.pdfparser.parser
 * @Description: 公转书_基本信息
 * @author liangyt
 * @date 2017年10月20日 下午2:05:25
 * @version V1.0
 */
public class PublicCompanyInfoParser extends PublicBaseParser {

	/****
	 * 解析公转书 基本信息
	 * 
	 * @param pdfCodeTable
	 * @param pdfReportLinks
	 * @param document
	 * @return
	 */
	public Map<String, Object> getResult(PdfCodeTable pdfCodeTable, PdfReportLinks pdfReportLinks, Document document) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String tableName = pdfCodeTable.getTableName();
			Map<String, String> companyidMap = new HashMap<String, String>();
			companyidMap.put("value", pdfReportLinks.getCompanyId() + "");
			companyidMap.put("tableName", tableName);
			companyidMap.put("property", "source_id");
			// link
			Map<String, String> linkMap = new HashMap<String, String>();
			linkMap.put("value", pdfReportLinks.getLink());
			linkMap.put("tableName", tableName);
			linkMap.put("property", "link");
			// pdfType
			Map<String, String> pdfTypeMap = new HashMap<String, String>();
			pdfTypeMap.put("value", pdfCodeTable.getPdfType());
			pdfTypeMap.put("tableName", tableName);
			pdfTypeMap.put("property", "pdfType");
			// 时间
			Map<String, String> timeMap = new HashMap<String, String>();
			timeMap.put("value", formatDate(new Date()));
			timeMap.put("tableName", tableName);
			timeMap.put("property", "up_time");
			// noticeId
			Map<String, String> noticeIdMap = new HashMap<String, String>();
			noticeIdMap.put("value", pdfReportLinks.getNoticeId() + "");
			noticeIdMap.put("tableName", tableName);
			noticeIdMap.put("property", "notice_id");

			// publishDate
			// Map<String, String> publishDateMap = new
			// HashMap<String, String>();
			// publishDateMap.put("value", publishDate);
			// publishDateMap.put("tableName", tableName);
			// publishDateMap.put("property", "publish_date");

			Map<String, String> reportDateMap = new HashMap<String, String>();
			reportDateMap.put("value", new SimpleDateFormat("yyyy-MM-dd").format(pdfReportLinks.getReportDate()));
			reportDateMap.put("tableName", tableName);
			reportDateMap.put("property", "report_date");
			List<Map<String, String>> infoEntity = new ArrayList<Map<String, String>>();
			List<List<Map<String, String>>> infoList = new ArrayList<List<Map<String, String>>>();
			// 存储开始位置
			List<String> begins = new ArrayList<String>();
			begins.add("一、公司概况");
			begins.add("一、基本情况");
			begins.add("一、基本情况");
			begins.add("一、公司基本情况");
			begins.add("一、公司基本情况");
			begins.add("一、公司基本情况");
			begins.add("一、公司基本情况");
			begins.add("一、公司简介");
			begins.add("一、简要情况");
			// 存储结束位置
			List<String> ends = new ArrayList<String>();
			ends.add("二、公司股票基本情况");
			ends.add("二、股票挂牌情况");
			ends.add("二、股份挂牌情况");
			ends.add("二、股份挂牌情况");
			ends.add("二、公司股份挂牌情况 ");
			ends.add("二、股票代码、股票简称");
			ends.add("二、公司股票基本情况");
			ends.add("二、股票挂牌情况");
			ends.add("二、股份代码、简称、挂牌日期");
			List<String> result = new ArrayList<>();
			Elements pElements = document.select("div").first().children();
			for (int i = 0; i < begins.size(); i++) {
				String preText = begins.get(i);
				String endText = ends.get(i);
				if (document.toString().contains(preText) && document.toString().contains(endText)) {
				} else {
					continue;
				}
				int preIndex = 0;
				int endIndex = pElements.size();
				Map<Integer, Integer> indexs = new HashMap<Integer, Integer>();
				for (int j = 0; j < pElements.size(); j++) {
					Element pElement = pElements.get(j);
					if (pElement.tagName().equals("p")) {
						String pText = pElement.text();
						pText = pText.replace("  ", "");
						pText = pText.replace(" ", "");
						pText = pText.replace("	", "");
						pText = pText.replace(" ", "");
						if (pText.contains(".......")) {
							continue;
						}
						if (pText.length() > 100) {
							continue;
						}
						if (pText.contains(endText)) {
							if (preIndex == 0) {
								continue;
							}
							if (j > endIndex && endIndex > preIndex) {
								continue;
							}
							endIndex = j;
							indexs.put(preIndex, endIndex);
						}
						if (pText.equals(preText)) {
							preIndex = j;
						} else if (pText.equals("、" + preText)) {
							preIndex = j;
						} else if (pText.contains(preText)) {
							for (String string : titleTags) {
								if (pText.contains(string)) {
									if (preIndex < endIndex) {
										preIndex = j;
										break;
									}
								}

							}
						}
					}
				}
				StringBuffer sb = new StringBuffer();
				Set<Integer> preIndexs = indexs.keySet();
				for (Integer preIn : preIndexs) {
					int endIn = indexs.get(preIn);
					for (int j = preIn + 1; j < endIn; j++) {
						Element childElement = pElements.get(j);
						sb.append(childElement.text());
					}
				}
				if (sb.toString().length() > 0) {
					result.add(sb.toString());
				}
			}
			// 多个规则循环结束 进行比较
			if (result.size() > 0) {
				String value = result.get(0);
				int valueCount = Collections.frequency(result, value);
				for (String string : result) {
					int strCount = Collections.frequency(result, string);
					if (value.contains(string)) {
						value = string;
						if (strCount > valueCount) {
							valueCount = strCount;
						}
					} else {
						if (strCount > valueCount) {
							value = string;
							valueCount = strCount;
						}
					}

				}
				// 如果都比不出来 就取最小的那个
				if (valueCount == 1) {
					for (String string : result) {
						if (string.length() < value.length()) {
							value = string;
						}
					}
				}
				if (value.length() > 30) {
					// Map<String, String> resultInfoMap = new HashMap<String,
					// String>();
					// resultInfoMap.put("value", value == null ? "" : value);
					// resultInfoMap.put("tableName", tableName);
					// resultInfoMap.put("property", "value");
					// infoEntity.add(resultInfoMap);
					parserInfo(infoEntity, value, tableName);
					infoEntity.add(companyidMap);
					// link
					infoEntity.add(linkMap);
					// pdfType
					infoEntity.add(pdfTypeMap);
					// 时间
					infoEntity.add(timeMap);
					// noticeId
					infoEntity.add(noticeIdMap);
					infoEntity.add(reportDateMap);
					infoList.add(infoEntity);
				}
			}
			resultMap.put("state", "ok");
			resultMap.put("info", infoList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultMap;
	}

	public static String tagArrays[] = new String[] { "中文名称", "公司名称", "英文名称", "公司英文名称", "法定代表人", "企业类型", "注册资本", "设立日期",
			"有限公司设立日期", "有限公司成立日期", "有限公司设立日", "变更为股份有限公司日期", "股份公司设立日期", "股份公司设立日", "股份公司成立日期", "住所", "办公地址", "邮编",
			"邮政编码", "统一社会信用代码", "组织机构代码", "董事会秘书", "信息披露负责人", "董事会秘书/信息披露负责人", "信息披露联系人", "联系电话", "电话号码", "电话", "传真",
			"传真号码", "电子信箱", "电子邮箱", "互联网网址", "互联网址", "网址", "所属行业", "经营范围", "主营业务", "主要业务", "主营产品" };

	public static final int INDEX_NOT_FOUND = -1;

	private void parserInfo(List<Map<String, String>> infoEntity, String value, String tableName) {
		value = removeSpace(value);
		System.out.println(value);
		if (value.contains("中文名称")) {
			fillInfoEntity(infoEntity, value, "中文名称", "cn_name");
		} else if (value.contains("公司名称")) {
			fillInfoEntity(infoEntity, value, "公司名称", "cn_name");
		}

		if (value.contains("英文名称")) {
			fillInfoEntity(infoEntity, value, "英文名称", "en_name");
		}
		if (value.contains("法定代表人")) {
			fillInfoEntity(infoEntity, value, "法定代表人", "legal_representative");
		}
		if (value.contains("企业类型")) {
			fillInfoEntity(infoEntity, value, "企业类型", "type");
		}
		if (value.contains("注册资本")) {
			fillInfoEntity(infoEntity, value, "注册资本", "reg_capital");
		}

		if (value.contains("有限公司设立日期")) {
			fillInfoEntity(infoEntity, value, "有限公司设立日期", "limit_com_date");
		} else if (value.contains("有限公司成立日期")) {
			fillInfoEntity(infoEntity, value, "有限公司成立日期", "limit_com_date");
		} else if (value.contains("有限公司设立日")) {
			fillInfoEntity(infoEntity, value, "有限公司设立日", "limit_com_date");
		} else if (value.contains("设立日期")) {
			fillInfoEntity(infoEntity, value, "设立日期", "limit_com_date");
		}

		if (value.contains("变更为股份有限公司日期")) {
			fillInfoEntity(infoEntity, value, "变更为股份有限公司日期", "stock_com_date");
		} else if (value.contains("股份公司设立日期")) {
			fillInfoEntity(infoEntity, value, "股份公司设立日期", "stock_com_date");
		} else if (value.contains("股份公司设立日期")) {
			fillInfoEntity(infoEntity, value, "股份公司设立日期", "stock_com_date");
		} else if (value.contains("股份公司成立日")) {
			fillInfoEntity(infoEntity, value, "股份公司设立日", "stock_com_date");
		}

		if (value.contains("住所")) {
			fillInfoEntity(infoEntity, value, "住所", "address");
		} else if (value.contains("办公地址")) {
			fillInfoEntity(infoEntity, value, "办公地址", "address");
		}

		if (value.contains("邮编")) {
			fillInfoEntity(infoEntity, value, "邮编", "postcode");
		} else if (value.contains("邮政编码")) {
			fillInfoEntity(infoEntity, value, "邮政编码", "postcode");
		}
		if (value.contains("统一社会信用代码")) {
			fillInfoEntity(infoEntity, value, "统一社会信用代码", "reg_num");
		}
		if (value.contains("组织机构代码")) {
			fillInfoEntity(infoEntity, value, "组织机构代码", "org_num");
		}

		if (value.contains("董事会秘书")) {
			fillInfoEntity(infoEntity, value, "董事会秘书", "secretary");
		}
		if (value.contains("信息披露负责人")) {
			fillInfoEntity(infoEntity, value, "信息披露负责人", "discloser");
		} else if (value.contains("董事会秘书/信息披露负责人")) {
			fillInfoEntity(infoEntity, value, "董事会秘书/信息披露负责人", "discloser");
		} else if (value.contains("信息披露联系人")) {
			fillInfoEntity(infoEntity, value, "信息披露联系人", "discloser");
		}

		if (value.contains("联系电话")) {
			fillInfoEntity(infoEntity, value, "联系电话", "phone");
		} else if (value.contains("电话号码")) {
			fillInfoEntity(infoEntity, value, "电话号码", "phone");
		} else if (value.contains("电话")) {

		}
		if (value.contains("传真号码")) {
			fillInfoEntity(infoEntity, value, "传真号码", "fax");
		} else if (value.contains("传真")) {
			fillInfoEntity(infoEntity, value, "传真", "fax");
		}
		if (value.contains("电子信箱")) {
			fillInfoEntity(infoEntity, value, "电子信箱", "email");
		} else if (value.contains("电子邮箱")) {
			fillInfoEntity(infoEntity, value, "电子邮箱", "email");
		}
		if (value.contains("互联网网址")) {
			fillInfoEntity(infoEntity, value, "互联网网址", "website");
		} else if (value.contains("网址")) {
			fillInfoEntity(infoEntity, value, "网址", "website");
		}
		if (value.contains("所属行业")) {
			fillInfoEntity(infoEntity, value, "所属行业", "industry_name");
		}
		if (value.contains("经营范围")) {
			fillInfoEntity(infoEntity, value, "经营范围", "business_scope");
		}
		if (value.contains("主营业务")) {
			fillInfoEntity(infoEntity, value, "主营业务", "main_business");
		} else if (value.contains("主要业务")) {
			fillInfoEntity(infoEntity, value, "主要业务", "main_business");
		}
		if (value.contains("主营产品")) {
			fillInfoEntity(infoEntity, value, "主营产品", "main_product");
		}

	}

	public void fillInfoEntity(List<Map<String, String>> infoEntity, String value, String open, String property) {
		if (value.contains(open)) {
			String temp = substringBetween(value, open);
			Map<String, String> resultInfoMap = new HashMap<String, String>();
			resultInfoMap.put("value", temp);
			resultInfoMap.put("tableName", "pdf_public_company_info");
			resultInfoMap.put("property", property);
			infoEntity.add(resultInfoMap);
		}
	}

	public static String substringBetween(String str, String open) {
		if (str == null || open == null) {
			return null;
		}
		int start = str.indexOf(open);
		if (start != INDEX_NOT_FOUND) {
			int end = str.length();
			for (String string : tagArrays) {
				if(string.equals(open)){
					continue;
				}
				int min = str.indexOf(string, start + open.length());
				if (end > min && min != INDEX_NOT_FOUND) {
					end = min;
				}
			}
			if (end != INDEX_NOT_FOUND) {
				return str.substring(start + open.length(), end);
			}
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(removeSpace("注册资本万元 深圳市福田区福华一路"));
	}

	public static String removeSpace(String sentence) {
		sentence = sentence.replace(":", "");
		sentence = sentence.replace("：", "");
		sentence = sentence.replaceAll("[ | | |	| | |　]+(?=[\\u4e00-\\u9fa5]+)+", "");
		return sentence;

	}

}
