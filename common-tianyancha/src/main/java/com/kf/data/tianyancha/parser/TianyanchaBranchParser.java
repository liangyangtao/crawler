package com.kf.data.tianyancha.parser;

import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.mybatis.entity.TycCompanyBranchCrawler;

/***
 * 
 * @Title: TianyanchaBranchParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017年9月29日 下午2:20:20
 * @version V1.0
 */
public class TianyanchaBranchParser extends TianyanchaBasePaser {
	// <!--[分支机构]-->
	// <!-- ngIf: items2.branchCount.show&&dataItemCount.branchCount>0 -->
	// http://www.tianyancha.com/expanse/branch.json?id=6881781&ps=10&pn=1
	// neeq_company_branch
	public static final String bodyCssPath = "div[ng-if=items2.branchCount.show&&dataItemCount.branchCount>0]";
	public static final String listCssPath = "tr[ng-repeat=node in branchList.result track by $index]";
	public static final String pageTotalCssPath = ".total";

	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_branch");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".companyInfo-table > tbody > tr");
			for (Element element : nodes) {
				try {
					Elements tdElements = element.select("td");
					// /company/919493487
					TycCompanyBranchCrawler tycCompanyBranch = new TycCompanyBranchCrawler();
					String href = tdElements.get(0).select("a").attr("href");
					String branchId = href.replace("/company/", "");
					tycCompanyBranch.setBranchId(branchId);
					tycCompanyBranch.setBranchName(tdElements.get(0).select("a").text());
					tycCompanyBranch.setCompanyId(companyId);
					tycCompanyBranch.setCompanyName(companyName);
					tycCompanyBranch.setCreatedAt(new Date());
					// tycCompanyBranch.setId(company.getId());
					// tycCompanyBranch.setOrganizationCode(obj.getString("estiblishTime"));
					tycCompanyBranch.setStatus(true);
					tycCompanyBranch.setUpdatedAt(new Date());
					sendJson(tycCompanyBranch, "tyc_company_branch");

				} catch (Exception e) {
					continue;
				}

			}
		}
	}

}
