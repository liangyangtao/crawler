package com.kf.data.tianyancha.parser;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.fetcher.tools.ReportDataFormat;
import com.kf.data.mybatis.entity.TycCompanyChattelMortgageCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TianyanchaMortgageParser extends TianyanchaBasePaser {
	// <!--动产抵押-->
	// <!-- ngIf: items2.mortgageCount.show&&dataItemCount.mortgageCount>0 -->
	public static final String cssPath = "div[ng-if=items2.equityCount.show&&dataItemCount.equityCount>0]";
	public static final String bodyCssPath = "div[ng-if=items2.icpCount.show&&dataItemCount.icpCount>0]";
	public static final String listCssPath = "tr[ng-repeat=check in changeinfoList.result]";
	public static final String pageTotalCssPath = ".total";

	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_mortgage");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					if (tdElements.size() == 7) {
						String text = tdElements.get(6).select("span").attr("onclick");
						text = StringUtils.substringBetween(text, "openMortgagePopup({", "})");
						text = "{" + text + "}";
						// openMortgagePopup({"baseInfo":{"overviewAmount":"55万元","scope":"融资租赁合同项下所有应付款项，包括但不限于租金，租赁费用，逾期利息，违约金，损害赔偿金和实现地债券的费用等。","status":"无效","remark":"","regDate":"2999-08-28","overviewType":"融资租赁合同","type":"融资租赁合同","overviewScope":"融资租赁合同项下所有应付款项，包括但不限于租金，租赁费用，逾期利息，违约金，损害赔偿金和实现地债券的费用等。","id":8775083,"amount":"55万元","overviewRemark":"","overviewTerm":"自
						// 2015年05月27日 至
						// 2017年05月31日","regDepartment":"大连金普新区市场监督管理局","regNum":"2015112","term":"自
						// 2015年05月27日 至
						// 2017年05月31日","base":"ln","publishDate":"Dec 7, 2016
						// 12:00:00
						// AM"},"changeInfoList":[],"pawnInfoList":[],"peopleInfo":[{"licenseNum":"非公示项","peopleName":"仲利国际租赁有限公司","liceseType":""}]})
						JSONObject fromObj = JSONObject.fromObject(text);
						JSONObject baseInfo = fromObj.getJSONObject("baseInfo");
						String credit_guaranteed_amt = null;
						try {
							credit_guaranteed_amt = baseInfo.getString("overviewAmount");// 被担保债权数额
						} catch (Exception e) {

						}
						String guarantee_scope = baseInfo.getString("scope");// 担保范围
						String case_status = baseInfo.getString("status");// 状态
						String dt_register = baseInfo.getString("regDate");// 登记日期
						String credit_guaranteed_type = "";
						try {
							credit_guaranteed_type = baseInfo.getString("overviewType");// 被担保债权类型
						} catch (Exception e) {
							// TODO: handle exception
						}
						String regDepartment = null;
						try {
							regDepartment = baseInfo.getString("regDepartment");// 注册机关
						} catch (Exception e) {
							// TODO: handle exception
						}
						String debt_term = baseInfo.getString("overviewTerm");// 债务人履行债务期限
						String register_number = baseInfo.getString("regNum");// 登记编号
						TycCompanyChattelMortgageCrawler tycCompanyChattelMortgage = new TycCompanyChattelMortgageCrawler();
						tycCompanyChattelMortgage
								.setCreditGuaranteedAmt(ReportDataFormat.bigUnitChange(credit_guaranteed_amt));
						tycCompanyChattelMortgage.setCreditGuaranteedType(credit_guaranteed_type);
						tycCompanyChattelMortgage.setGuaranteeScope(guarantee_scope);
						tycCompanyChattelMortgage.setCaseStatus(case_status);
						tycCompanyChattelMortgage.setDtRegister(stringToDate(dt_register));
						tycCompanyChattelMortgage.setDebtTerm(debt_term);
						tycCompanyChattelMortgage.setRegisterNumber(register_number);
						tycCompanyChattelMortgage.setCompanyId(companyId);
						tycCompanyChattelMortgage.setCompanyName(companyName);
						tycCompanyChattelMortgage.setRegisterAgency(regDepartment);
						JSONArray pepoleInfo = fromObj.getJSONArray("peopleInfo");
						for (Object object : pepoleInfo) {
							JSONObject objs = JSONObject.fromObject(object);
							String mortgagee = objs.getString("peopleName");
							tycCompanyChattelMortgage.setMortgagee(mortgagee);
							String mortgageeIdType = objs.getString("liceseType");
							tycCompanyChattelMortgage.setMortgageeIdType(mortgageeIdType);
							String licenseNum = objs.getString("licenseNum");
							tycCompanyChattelMortgage.setMortgageeIdNumber(licenseNum);
						}
						tycCompanyChattelMortgage.setStatus(false);
						sendJson(tycCompanyChattelMortgage, "tyc_company_chattel_mortgage");
					}

				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}
}
