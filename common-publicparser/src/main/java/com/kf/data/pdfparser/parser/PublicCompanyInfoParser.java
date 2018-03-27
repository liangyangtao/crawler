package com.kf.data.pdfparser.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
			begins.add("公司概况");
			begins.add("公司基本情况");
			begins.add("基本情况");
			begins.add("公司简介");
			begins.add("简要情况");
			Elements pElements = document.select("div").first().children();
			int preIndex = 0;
			int endIndex = pElements.size();
			String preReg = null;
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
					if (preReg != null) {
						if (pText.contains(preReg)) {
							if (preIndex == 0) {
								continue;
							}
							if (j > endIndex && endIndex > preIndex) {
								break;
							}
							endIndex = j;
							indexs.put(preIndex, endIndex);
						}
					} else {
						for (String preText : begins) {
							if (pText.contains(preText)) {
								if (pText.length() >= preText.length() + 8) {
									continue;
								}
								preIndex = j;
								preReg = "二、";
								if (preReg != null) {
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
			if (sb.length() > 30) {
				parserInfo(infoEntity, sb.toString(), tableName);
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

			resultMap.put("state", "ok");
			resultMap.put("info", infoList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultMap;
	}

	public static String tagArrays[] = new String[] { "中文名称", "公司名称", "英文名称", "法人代表", "法定代表人", "企业类型", "注册资本", "设立日期",
			"成立日期", "注册日期", "有限公司设立时间", "有限公司设立日期", "有限公司成立日期", "有限公司设立日", "整体变更为股份公司日期", "整体变更设立股份公司日期", "变更为股份有限公司日期",
			"股份公司设立日期", "股份公司设立时期", "股份公司设立时间", "股份公司设立日", "股份公司成立日期", "公司住所", "住所", "办公地址", "注册地址", "注册地", "邮编",
			"邮政编码", "社会信用代码", "社会统一信用代码", "统一社会信用代码", "注册号/社会统一信用代码", "注册号", "营业执照注册号", "组织机构代码", "董事会秘书", "信息披露负责人",
			"信息披露人", "董事会秘书/信息披露负责人", "信息披露事务负责人", "信息披露联系人", "联系电话", "电话号码", "电话", "传真", "传真号码", "公司邮箱", "电子信箱",
			"电子邮箱", "E-mail地址", "email", "e-mail地址", "e-mail", "邮箱", "互联网网址", "互联网址", "互联网地址", "公司网址", "网址", "网站",
			"公司网站", "所属行业", "经营范围", "主营业务", "主要业务", "主营产品", "转让方式", "营业执照号", "实收资本", "营业期限" };

	// 整体变更为股份公司日期
	// 2010年 01月 04日股份公司设立时期2015年 09月 2日营业期限2010年 01月 04日至长期
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
		} else if (value.contains("法人代表")) {
			fillInfoEntity(infoEntity, value, "法人代表", "legal_representative");
		}
		if (value.contains("企业类型")) {
			fillInfoEntity(infoEntity, value, "企业类型", "type");
		}
		if (value.contains("注册资本")) {
			fillInfoEntity(infoEntity, value, "注册资本", "reg_capital");
		}

		if (value.contains("有限公司设立时间")) {
			fillInfoEntity(infoEntity, value, "有限公司设立时间", "limit_com_date");
		} else if (value.contains("有限公司设立日期")) {
			fillInfoEntity(infoEntity, value, "有限公司设立日期", "limit_com_date");
		} else if (value.contains("有限公司成立日期")) {
			fillInfoEntity(infoEntity, value, "有限公司成立日期", "limit_com_date");
		} else if (value.contains("有限公司设立日")) {
			fillInfoEntity(infoEntity, value, "有限公司设立日", "limit_com_date");
		} else if (value.contains("设立日期")) {
			fillInfoEntity(infoEntity, value, "设立日期", "limit_com_date");
		} else if (value.contains("成立日期")) {
			fillInfoEntity(infoEntity, value, "成立日期", "limit_com_date");
		} else if (value.contains("注册日期")) {
			fillInfoEntity(infoEntity, value, "注册日期", "limit_com_date");
		}

		if (value.contains("变更为股份有限公司日期")) {
			fillInfoEntity(infoEntity, value, "变更为股份有限公司日期", "stock_com_date");
		} else if (value.contains("股份公司设立日期")) {
			fillInfoEntity(infoEntity, value, "股份公司设立日期", "stock_com_date");
		} else if (value.contains("股份公司设立日期")) {
			fillInfoEntity(infoEntity, value, "股份公司设立日期", "stock_com_date");
		} else if (value.contains("股份公司成立日")) {
			fillInfoEntity(infoEntity, value, "股份公司设立日", "stock_com_date");
		} else if (value.contains("股份公司设立时间")) {
			fillInfoEntity(infoEntity, value, "股份公司设立时间", "stock_com_date");
		} else if (value.contains("整体变更设立股份公司日期")) {
			fillInfoEntity(infoEntity, value, "整体变更设立股份公司日期", "stock_com_date");
		} else if (value.contains("股份公司设立时期")) {
			fillInfoEntity(infoEntity, value, "股份公司设立时期", "stock_com_date");
		} else if (value.contains("整体变更为股份公司日期")) {
			fillInfoEntity(infoEntity, value, "整体变更为股份公司日期", "stock_com_date");
		}

		// 公司住所
		if (value.contains("公司住所")) {
			fillInfoEntity(infoEntity, value, "公司住所", "address");
		} else if (value.contains("住所")) {
			fillInfoEntity(infoEntity, value, "住所", "address");
		} else if (value.contains("办公地址")) {
			fillInfoEntity(infoEntity, value, "办公地址", "address");
		} else if (value.contains("注册地址")) {
			fillInfoEntity(infoEntity, value, "注册地址", "address");
		} else if (value.contains("注册地")) {
			fillInfoEntity(infoEntity, value, "注册地", "address");
		}

		if (value.contains("邮编")) {
			fillInfoEntity(infoEntity, value, "邮编", "postcode");
		} else if (value.contains("邮政编码")) {
			fillInfoEntity(infoEntity, value, "邮政编码", "postcode");
		}
		if (value.contains("统一社会信用代码")) {
			fillInfoEntity(infoEntity, value, "统一社会信用代码", "reg_num");
		} else if (value.contains("注册号/社会统一信用代码")) {
			fillInfoEntity(infoEntity, value, "注册号/社会统一信用代码", "reg_num");
		} else if (value.contains("注册号")) {
			fillInfoEntity(infoEntity, value, "注册号", "reg_num");
		} else if (value.contains("社会统一信用代码")) {
			fillInfoEntity(infoEntity, value, "社会统一信用代码", "reg_num");
		} else if (value.contains("社会信用代码")) {
			fillInfoEntity(infoEntity, value, "社会信用代码", "reg_num");
		} else if (value.contains("营业执照号")) {
			fillInfoEntity(infoEntity, value, "营业执照号", "reg_num");
		} else if (value.contains("营业执照注册号")) {
			fillInfoEntity(infoEntity, value, "营业执照注册号", "reg_num");
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
		} else if (value.contains("信息披露事务负责人")) {
			fillInfoEntity(infoEntity, value, "信息披露事务负责人", "discloser");
		} else if (value.contains("信息披露人")) {
			fillInfoEntity(infoEntity, value, "信息披露人", "discloser");
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
		} else if (value.contains("E-mail地址")) {
			fillInfoEntity(infoEntity, value, "E-mail地址", "email");
		} else if (value.contains("e-mail地址")) {
			fillInfoEntity(infoEntity, value, "e-mail地址", "email");
		} else if (value.contains("e-mail")) {
			fillInfoEntity(infoEntity, value, "e-mail", "email");
		} else if (value.contains("公司邮箱")) {
			fillInfoEntity(infoEntity, value, "公司邮箱", "email");
		} else if (value.contains("邮箱")) {
			fillInfoEntity(infoEntity, value, "邮箱", "email");
		}
		//
		// "互联网网址", "互联网址", "互联网地址", "公司网址", "网址", "网站", "公司网站"
		if (value.contains("互联网网址")) {
			fillInfoEntity(infoEntity, value, "互联网网址", "website");
		}
		if (value.contains("互联网址")) {
			fillInfoEntity(infoEntity, value, "互联网址", "website");
		} else if (value.contains("网址")) {
			fillInfoEntity(infoEntity, value, "网址", "website");
		} else if (value.contains("互联网地址")) {
			fillInfoEntity(infoEntity, value, "互联网地址", "website");
		} else if (value.contains("网站")) {
			fillInfoEntity(infoEntity, value, "网站", "website");
		} else if (value.contains("公司网站")) {
			fillInfoEntity(infoEntity, value, "公司网站", "website");
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
			temp = temp.trim();
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
				if (string.equals(open)) {
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
