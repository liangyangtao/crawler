package com.kf.data.tianyancha.parser;

import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.mybatis.entity.TycCompanyExecutiveCrawler;

public class TianyanchaStaffParser extends TianyanchaBasePaser {
	// <!--[高管信息]-->
	// <!-- ngIf: items2.staffCount.show&&dataItemCount.staffCount>0 -->
	// neeq_company_executive
	public static final String bodyCssPath = "div#_container_staff";
	public static final String listCssPath = "div.staffinfo-module-container";
	public static final String pageTotalCssPath = ".total";

	public void paseNode(Document document, String companyName, String companyId) {
		Element bodyElemnt = getNodeByCssPath(document, bodyCssPath);
		if (bodyElemnt == null) {
			System.out.println("主要人员没有");
			return;
		}
		Elements nodes = bodyElemnt.select(listCssPath);

		for (Element element : nodes) {

			try {
				// ng-if="s.typeJoin.length!=0"
				String persionTitle = "";
				Elements persionTitleElements = element.select(".new-c5");
				if (persionTitleElements.size() > 0) {
					for (Element element2 : persionTitleElements) {
						persionTitle += element2.text().trim();
					}
				}
				String name = null;
				Elements nameElements = element.select("a");
				if (nameElements.size() > 0) {
					name = nameElements.get(0).text();
				}

				TycCompanyExecutiveCrawler tycCompanyExecutive = new TycCompanyExecutiveCrawler();
				// tycCompanyExecutive.setAge(age);
				tycCompanyExecutive.setCompanyId(companyId);
				tycCompanyExecutive.setCompanyName(companyName);
				tycCompanyExecutive.setCreatedAt(new Date());
				// tycCompanyExecutive.setEducation(education);
				// tycCompanyExecutive.setEmail(email);
				// tycCompanyExecutive.setEndTime(endTime);
				// tycCompanyExecutive.setFax(fax);
				// tycCompanyExecutive.setGender(gender);
				// tycCompanyExecutive.setId(company.getId());

				// tycCompanyExecutive.setLinkedin(linkedin);
				tycCompanyExecutive.setName(name);
				tycCompanyExecutive.setTitle(persionTitle);
				// tycCompanyExecutive.setPersonId(personId);
				tycCompanyExecutive.setStatus(false);
				// tycCompanyExecutive.setTel(tel);
				// tycCompanyExecutive.setTerm(term);
				// tycCompanyExecutive.setUpdatedAt(updatedAt);
				// tycCompanyExecutive.setWeibo(weibo);
				//
				sendJson(tycCompanyExecutive, "tyc_company_executive");
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

		}
	}

}
