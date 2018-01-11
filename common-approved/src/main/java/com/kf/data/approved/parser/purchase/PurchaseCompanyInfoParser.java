package com.kf.data.approved.parser.purchase;

import java.util.Map;

import com.kf.data.approved.parser.BaseParser;

/****
 * 
 * @Title: PurchaseCompanyInfoParser.java
 * @Package com.kf.data.approved.parser.purchase
 * @Description: 公司基本信息
 * @author liangyt
 * @date 2018年1月5日 下午1:42:19
 * @version V1.0
 */
public class PurchaseCompanyInfoParser extends BaseParser {

	public static String tagArrays[] = new String[] { "公司中文全称 ", "中文名称", "公司名称", "企业名称", "名称", "收购人名称", "英文名称", "法定代表人",
			"法人代表", "合伙企业类型", "企业类型", "企业性质", "公司性质", "类型", "注册资本", "实缴出资", "设立日期", "成立日期", "注册日期", "成立时间", "公司设立时间",
			"有限公司设立时间", "有限公司设立日期", "有限公司成立日期", "有限公司设立日", "整体变更为股份公司日期", "整体变更设立股份公司日期", "变更为股份有限公司日期", "股份公司设立日期",
			"股份公司设立时期", "股份公司设立时间", "股份公司设立日", "股份公司成立日期", "企业地址", "公司住所", "住所", "办公地址", "注册地址", "注册地", "邮编", "邮政编码",
			"社会信用代码", "社会统一信用代码", "统一社会信用代码", "注册号/社会统一信用代码", "注册号", "营业执照注册号", "组织机构代码", "董事会秘书", "信息披露负责人", "信息披露人",
			"董事会秘书/信息披露负责人", "信息披露事务负责人", "信息披露联系人", "联系电话", "电话号码", "电话", "传真", "传真号码", "公司邮箱", "电子信箱", "电子邮箱",
			"E-mail地址", "email", "e-mail地址", "e-mail", "邮箱", "互联网网址", "互联网址", "互联网地址", "公司网址", "网址", "网站", "公司网站",
			"所属行业", "行业", "经营范围", "主营业务", "主要业务", "主营产品", "转让方式", "营业执照号", "实收资本", "营业期限", "经营期限", "合伙期限", "执行事务合伙人",
			"执行事务人", "认缴出资额", "实缴资本", "名称", "登记机关", "证券代码", "公司股票公开转让场所", "证券简称", "股票代码", "股票简称", "股权结构", "登记状态",
			"出资情况", "股本总额", "增资后注册资本", "股票上市证券交易所", "股东", "主要经营场所", "上市地", "出资总额", "开办资金", "挂牌时间", "注册资金", "上市日期",
			"宗旨和业务范围" };

	// 整体变更为股份公司日期
	// 2010年 01月 04日股份公司设立时期2015年 09月 2日营业期限2010年 01月 04日至长期
	public static final int INDEX_NOT_FOUND = -1;

	public void parserInfo(Map<String, Object> infoEntity, String value) {
		value = removeSpace(value);
		if (value.contains("中文名称")) {
			fillInfoEntity(infoEntity, value, "中文名称", "cn_name");
		} else if (value.contains("公司名称")) {
			fillInfoEntity(infoEntity, value, "公司名称", "cn_name");
		} else if (value.contains("收购人名称")) {
			fillInfoEntity(infoEntity, value, "收购人名称", "cn_name");
		} else if (value.contains("企业名称")) {
			fillInfoEntity(infoEntity, value, "企业名称", "cn_name");
		} else if (value.contains("名称")) {
			fillInfoEntity(infoEntity, value, "名称", "cn_name");
		} else if (value.contains("公司中文全称")) {
			fillInfoEntity(infoEntity, value, "公司中文全称", "cn_name");
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
		} else if (value.contains("类型")) {
			fillInfoEntity(infoEntity, value, "类型", "type");
		} else if (value.contains("企业性质")) {
			fillInfoEntity(infoEntity, value, "企业性质", "type");
		} else if (value.contains("公司性质")) {
			fillInfoEntity(infoEntity, value, "公司性质", "type");
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
		} else if (value.contains("成立时间")) {
			fillInfoEntity(infoEntity, value, "成立时间", "limit_com_date");
		} else if (value.contains("公司设立时间 ")) {
			fillInfoEntity(infoEntity, value, "公司设立时间 ", "limit_com_date");
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
			fillInfoEntity(infoEntity, value, "电话", "phone");
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
		if (value.contains("互联网网址")) {
			fillInfoEntity(infoEntity, value, "互联网网址", "website");
		} else if (value.contains("互联网址")) {
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
		} else if (value.contains("行业")) {
			fillInfoEntity(infoEntity, value, "行业", "industry_name");
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

		if (value.contains("营业期限")) {
			fillInfoEntity(infoEntity, value, "营业期限", "operating_date");
		} else if (value.contains("合伙期限")) {
			fillInfoEntity(infoEntity, value, "合伙期限", "operating_date");
		} else if (value.contains("经营期限")) {
			fillInfoEntity(infoEntity, value, "经营期限", "operating_date");
		}

	}

	public void fillInfoEntity(Map<String, Object> infoEntity, String value, String open, String property) {
		if (value.contains(open)) {
			String temp = substringBetween(value, open);
			temp = temp.trim();
			infoEntity.put(property, temp);
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
				if (string.equals(open) || open.contains(string)) {
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
