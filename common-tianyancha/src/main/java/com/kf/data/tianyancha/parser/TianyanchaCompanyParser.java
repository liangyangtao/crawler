package com.kf.data.tianyancha.parser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.mybatis.entity.TycBaseCompanyCrawler;

/***
 * 
 * @Title: TianyanchaCompanyParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 天眼查基本信息解析
 * @author liangyt
 * @date 2017年9月29日 下午2:26:33
 * @version V1.0
 */
public class TianyanchaCompanyParser extends TianyanchaBasePaser {

	// <!-- 基本信息 -->
	// company-content
	// b-c-white new-border over-hide mr10
	public static final String loaderCssPath = "#_container_baseInfo > div > div > table > tbody";
	public static final String infoCssPath = "div.base0910";
	public static final String headerCssPath = "div.company_header_width";
	public static final String logoCssPath = ".new-border";

	/***
	 * 天眼查基本信息解析
	 * 
	 * @param document
	 * @param operatingStatus
	 * @param companyID
	 * @return
	 */
	public String paseNode(Document document, String companyID, String registeredCapital, String registrationDate) {
		if (registrationDate != null) {
			registrationDate = registrationDate.replace("-", "");
		}

		String companyName = null;
		try {
			TycBaseCompanyCrawler tycBaseCompanyCrawler = new TycBaseCompanyCrawler();

			// 公司logo
			Elements logoElements = document.select(".companyTitleBox55");
			if (logoElements.size() > 0) {
				Elements logDivElements = logoElements.first().select(".b-c-white");
				if (logDivElements.size() > 0) {
					Elements logoImgElements = logDivElements.first().select("img");
					if (logoImgElements.size() > 0) {
						String logo = logoImgElements.first().attr("src");
						tycBaseCompanyCrawler.setCompanyLogoUrl(logo);
					}
				}
			}

			Element headerElement = getNodeByCssPath(document, headerCssPath);
			/****
			 * toubu
			 */
			String address = null;
			String email = null;
			String tel = null;
			String website = null;

			if (headerElement == null) {
				System.out.println("头部没有");
			} else {
				Elements companyElements = headerElement.select(".f18.in-block.vertival-middle.sec-c2");
				if (companyElements.size() > 0) {
					companyName = companyElements.get(0).text();
					System.out.println("真实的名称是" + companyName);
					tycBaseCompanyCrawler.setCompanyName(companyName);
				}
				// 曾用名称
				String companyHisname = null;
				Elements hisNameElements = headerElement.select(".historyName45Bottom");
				if (hisNameElements.size() > 0) {
					companyHisname = hisNameElements.first().text();
					tycBaseCompanyCrawler.setCompanyHisName(companyHisname);
				}
				// 是否高新企业
				Elements tagElements = headerElement.select(".f14.mt10");
				if (tagElements.size() > 0) {
					String temp = tagElements.first().text();
					if (temp.contains("高新企业")) {
						try {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("company_id", companyID);
							map.put("ishnte", 1);
							map.put("remark", companyName);
							sendJson(map, "tyc_company_ishnte");
							logger.info("保存数据");
						} catch (Exception e1) {
							e1.printStackTrace();
						}

					}

				}

				Elements inBlockElements = headerElement.select(".in-block.vertical-top:not(.sec-c3)");
				for (Element element : inBlockElements) {
					if (element.text().contains("地址：")) {
						address = element.text();
						address = address.replace("地址：", "").trim();
						address = address.replace("暂无", "").trim();
						address = address.replace("附近公司", "");
					} else if (element.text().contains("邮箱：")) {
						email = element.text();
						email = email.replace("邮箱：", "").trim();
						email = email.replace("暂无", "").trim();
					} else if (element.text().contains("网址：")) {
						website = element.text();
						website = website.replace("网址：", "");
						website = website.replace("暂无", "").trim();
					} else if (element.text().contains("电话：")) {
						tel = element.text();
						tel = tel.replace("电话：", "");
						tel = tel.replace("暂无", "").trim();
					}

				}
				String companyDetail = null;
				// sec-c2 over-hide
				Elements secElements = headerElement.select(".sec-c2.over-hide");
				if (secElements.size() > 0) {
					Elements scriptElements = secElements.first().select("#company_base_info_detail");
					if (scriptElements.size() > 0) {
						companyDetail = scriptElements.get(0).toString();
						companyDetail = StringUtils.substringBetween(companyDetail, "company_base_info_detail\">",
								"</script>");
					} else {
						companyDetail = secElements.first().text();
					}
					companyDetail = companyDetail.replace("详情", "");
					tycBaseCompanyCrawler.setCompanyAbout(companyDetail);
				}

			}
			/****
			 * end
			 */
			/****
			 * chuzi
			 */
			String legalRepresentative = null;

			String registrationDateMachine = null;
			// Date dtSetupCorp = null;
			String operatingStatus = null;
			Element loaderElement = getNodeByCssPath(document, loaderCssPath);
			if (loaderElement == null) {
				System.out.println("法人信息没有");
			} else {
				Elements loaderElements = loaderElement.select("tr");
				if (loaderElements.size() > 0) {
					Element element = loaderElements.get(0);

					Element legalRepresentativeElement = element.select("td").get(0);
					if (legalRepresentativeElement
							.select("div.human-top:nth-child(1) > div:nth-child(2) > div:nth-child(1)").size() > 0) {
						legalRepresentative = legalRepresentativeElement.select("a").first().text();
					} else {
						legalRepresentative = legalRepresentativeElement.text();
					}
					Element registeredCapitalElement = element.select("td").get(1);
					// registeredCapital = registeredCapitalElement
					// .select("div:nth-child(1) > div:nth-child(2) >
					// div:nth-child(1)").text().trim();
					// registrationDate = registeredCapitalElement
					// .select("div:nth-child(2) > div:nth-child(2) >
					// div:nth-child(1)").text().trim();
					// registrationDate = registrationDate.replace("-", "");
					operatingStatus = registeredCapitalElement
							.select("div:nth-child(3) > div:nth-child(2) > div:nth-child(1)").text().trim();

				}

			}
			String approvedDate = null;
			String approvedDateMachine = null;
			String businessScope = null;
			String companyProperty = null;
			String companySize = null;
			String companyType = null;
			String companyUsetypeId = null;
			String companyUsetypeName = null;
			String companyUsetypeTable = null;
			String creditCode = null;
			String industry = null;
			String industryRecruitment = null;
			String legalRepresentativeId = null;
			String operatingBeginDate = null;
			String operatingEndDate = null;

			String organizationCode = null;
			String websiteRecruitment = null;

			String registeredAddress = null;
			String registeredCityCode = null;
			String registeredCityName = null;
			String registrationAuthority = null;
			String englishName = null;
			String registrationNumber = null;

			String taxpayerNum = null;

			Element infoElement = getNodeByCssPath(document, infoCssPath);
			if (infoElement == null) {
				System.out.println("主要信息没有");
			} else {
				Elements nodes = infoElement.select("tr");
				for (Element trElement : nodes) {
					try {
						Elements leftElements = trElement.select(".table-left");
						for (Element element : leftElements) {
							if (element.text().startsWith("工商注册号")) {
								registrationNumber = trElement.select("td").get(1).text().trim();
							} else if (element.text().startsWith("组织机构代码")) {
								organizationCode = trElement.select("td").get(3).text().trim();
							} else if (element.text().startsWith("统一信用代码")) {
								creditCode = trElement.select("td").get(1).text().trim();
							} else if (element.text().startsWith("企业类型") || element.text().startsWith("公司类型")) {
								companyType = trElement.select("td").get(3).text().trim();
							} else if (element.text().startsWith("纳税人识别号")) {
								taxpayerNum = trElement.select("td").get(1).text().trim();
							} else if (element.text().startsWith("行业")) {
								industryRecruitment = trElement.select("td").get(3).text().trim();
							} else if (element.text().startsWith("营业期限")) {
								String operating = trElement.select("td").get(1).text().trim();
								String temp[] = operating.split("至");
								if (temp.length == 2) {
									operatingBeginDate = temp[0].trim().trim();
									operatingEndDate = temp[1].trim().trim();
								}
							} else if (element.text().startsWith("核准日期")) {
								approvedDate = trElement.select("td").get(3).text().trim();
								approvedDate = approvedDate.replace("-", "").trim().trim();
							} else if (element.text().startsWith("登记机关")) {
								registrationAuthority = trElement.select("td").get(1).text().trim();
							} else if (element.text().startsWith("英文名称")) {
								englishName = trElement.select("td").get(3).text().trim();
								englishName = englishName.replace("未公开", "");
							} else if (element.text().startsWith("注册地址")) {
								registeredAddress = trElement.select("td").get(1).text().trim();
								registeredAddress = registeredAddress.replace("附近公司", "");
							} else if (element.text().startsWith("经营范围")) {
								Element tdElement = trElement.select("td").get(1);
								Elements spanElements = tdElement.select(".js-full-container");
								if (spanElements.size() > 0) {
									businessScope = spanElements.get(0).text();
								} else {
									businessScope = tdElement.text();
								}
								businessScope = businessScope.replace("收起", "").trim();
								businessScope = businessScope.replace("详细", "").trim();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}

				}
			}
			tycBaseCompanyCrawler.setTaxpayerNum(taxpayerNum);
			tycBaseCompanyCrawler.setAddress(address);
			tycBaseCompanyCrawler.setApprovedDate(approvedDate);

			tycBaseCompanyCrawler.setApprovedDateMachine(approvedDateMachine);

			tycBaseCompanyCrawler.setBusinessScope(businessScope);

			tycBaseCompanyCrawler.setCompanyId(companyID);

			tycBaseCompanyCrawler.setCompanyName(tycBaseCompanyCrawler.getCompanyName());

			tycBaseCompanyCrawler.setCompanyProperty(companyProperty);

			tycBaseCompanyCrawler.setCompanyShortname(tycBaseCompanyCrawler.getCompanyName());

			tycBaseCompanyCrawler.setCompanySize(companySize);

			tycBaseCompanyCrawler.setCompanyType(companyType);

			tycBaseCompanyCrawler.setCompanyUsetypeId(companyUsetypeId);

			tycBaseCompanyCrawler.setCompanyUsetypeName(companyUsetypeName);

			tycBaseCompanyCrawler.setCompanyUsetypeTable(companyUsetypeTable);
			tycBaseCompanyCrawler.setCreatedAt(new Date());

			tycBaseCompanyCrawler.setCreditCode(creditCode);

			tycBaseCompanyCrawler.setDtSetupCorp(null);

			tycBaseCompanyCrawler.setEmail(email);
			tycBaseCompanyCrawler.setCompanyEnglishName(englishName);
			tycBaseCompanyCrawler.setIndustry(industry);

			tycBaseCompanyCrawler.setIndustryRecruitment(industryRecruitment);

			tycBaseCompanyCrawler.setLegalRepresentative(legalRepresentative);

			tycBaseCompanyCrawler.setLegalRepresentativeId(legalRepresentativeId);

			tycBaseCompanyCrawler.setOperatingBeginDate(operatingBeginDate);

			tycBaseCompanyCrawler.setOperatingEndDate(operatingEndDate);

			tycBaseCompanyCrawler.setOperatingStatus(operatingStatus);

			tycBaseCompanyCrawler.setOrganizationCode(organizationCode);

			tycBaseCompanyCrawler.setRegisteredAddress(registeredAddress);

			tycBaseCompanyCrawler.setRegisteredCapital(registeredCapital);

			tycBaseCompanyCrawler.setRegisteredCityCode(registeredCityCode);

			tycBaseCompanyCrawler.setRegisteredCityName(registeredCityName);

			tycBaseCompanyCrawler.setRegistrationAuthority(registrationAuthority);

			tycBaseCompanyCrawler.setRegistrationDate(registrationDate);

			tycBaseCompanyCrawler.setRegistrationDateMachine(registrationDateMachine);

			tycBaseCompanyCrawler.setRegistrationNumber(registrationNumber);
			tycBaseCompanyCrawler.setStatus(false);
			tycBaseCompanyCrawler.setTel(tel);
			tycBaseCompanyCrawler.setUpdatedAt(new Date());
			tycBaseCompanyCrawler.setWebsite(website);
			tycBaseCompanyCrawler.setWebsiteRecruitment(websiteRecruitment);
			sendJson(tycBaseCompanyCrawler, "tyc_base_company");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return companyName;

	}

}
