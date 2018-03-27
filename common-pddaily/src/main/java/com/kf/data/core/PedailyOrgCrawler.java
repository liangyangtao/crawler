package com.kf.data.core;

import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.dao.PdPedailyOrgCrawlerStore;
import com.kf.data.fetcher.Fetcher;
import com.kf.data.mybatis.entity.PdPedailyOrgCrawler;

/****
 * 投资界投资机构
 * 
 * @Title: PedailyOrgCrawler.java
 * @Package com.kf.data.core
 * @Description: 投资界投资机构
 * @author liangyt
 * @date 2017年11月3日 下午4:49:17
 * @version V1.0
 */
public class PedailyOrgCrawler {

	public static void main(String[] args) {
		new PedailyOrgCrawler().crawlerList();
	}

	public void crawlerList() {
		for (int i = 232; i <= (15855 / 20 + 1); i++) {
			System.out.println(i);
			String url = "http://zdb.pedaily.cn/company/all-p" + i;
			String html = Fetcher.getInstance().get(url);
			Document document = Jsoup.parse(html, url);
			Elements companylistElements = document.select("#company-list > li");
			for (Element companyElement : companylistElements) {
				try {
					String href = companyElement.select("a").first().absUrl("href");
					// String companyName =
					// companyElement.select("a").first().text();
					System.out.println(href);
					crawlerDetail(href);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}

	public void crawlerDetail(String url) {
		String html = Fetcher.getInstance().get(url);
		Document document = Jsoup.parse(html, url);
		String description = null;
		String fullName = null;
		String headquarters = null;
		String logoLink = null;
		String logoUrl = null;
		String setupAddress = null;
		String setupTime = null;
		String shortName = null;
		String webSite = null;
		String orgType = null;
		String investType = null;
		String phone = null;
		String address = null;
		String fax = null;
		String zipcode = null;
		String englishName = null;
		String capitalType = null;

		Elements zdbTopElements = document.select(".zdb-top");
		if (zdbTopElements.size() > 0) {
			Element zdbTopElement = zdbTopElements.get(0);
			logoUrl = zdbTopElement.select("div.img > img").attr("src");
			logoLink = zdbTopElement.select("div.img > img").attr("src");
			fullName = zdbTopElement.select(".info >h1").first().textNodes().get(0).text();
			shortName = zdbTopElement.select(".info >h1 > em").first().text();
			try {
				englishName = zdbTopElement.select(".info >h2").first().text();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Elements lis = zdbTopElement.select("ul > li");
			for (Element liElement : lis) {
				String text = liElement.text();
				String title = liElement.select("span").text();
				if (title.equals("资本类型：")) {
					text = text.replace("资本类型：", "");
					capitalType = text;
				} else if (title.equals("机构性质：")) {
					text = text.replace("机构性质：", "");
					orgType = text;
				} else if (title.equals("注册地点：")) {
					text = text.replace("注册地点：", "");
					headquarters = text;
				} else if (title.equals("成立时间：")) {
					text = text.replace("成立时间：", "");
					setupTime = text;
				} else if (title.equals("机构总部：")) {
					text = text.replace("机构总部：", "");
					setupAddress = text;

				} else if (title.equals("官方网站：")) {
					text = text.replace("官方网站：", "");
					webSite = text;
				} else if (title.equals("投资阶段：")) {
					text = text.replace("投资阶段：", "");
					investType = text;
				}

			}

		}
		Elements zdbShowElements = document.select(".zdb-show");
		if (zdbShowElements.size() > 0) {

			// 简介
			Elements descElements = zdbShowElements.first().select("#desc");

			if (descElements.size() > 0) {

				Element descElement = descElements.first();
				try {
					descElement.select("div.zdb-share").remove();
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					descElement.select(".noline").remove();
				} catch (Exception e) {
					e.printStackTrace();
				}

				description = descElement.toString();
			}
			// 联系方式
			Elements contactElements = zdbShowElements.first().select("#contact");
			if (contactElements.size() > 0) {
				Element contactElement = contactElements.first();
				Elements pElements = contactElement.select("p");
				for (Element element : pElements) {
					String title = element.select("span").text();
					String text = element.text();
					if (title.equals("联系电话：")) {
						text = text.replace("联系电话：", "");
						phone = text;
					} else if (title.equals("传 真：")) {
						text = text.replace("传 真：", "");
						fax = text;
					} else if (title.equals("地 址：")) {
						text = text.replace("地 址：", "");
						address = text;
					} else if (title.equals("邮 编：")) {
						text = text.replace("邮 编：", "");
						zipcode = text;
					}
				}
			}
		}
		PdPedailyOrgCrawler pdPedailyOrgCrawler = new PdPedailyOrgCrawler();
		pdPedailyOrgCrawler.setAddress(address);
		pdPedailyOrgCrawler.setCapitalType(capitalType);
		pdPedailyOrgCrawler.setCreatetime(new Date());
		pdPedailyOrgCrawler.setDescription(description);
		pdPedailyOrgCrawler.setEnglishName(englishName);
		pdPedailyOrgCrawler.setEtltime(null);
		pdPedailyOrgCrawler.setFax(fax);
		pdPedailyOrgCrawler.setFullname(fullName);
		pdPedailyOrgCrawler.setHeadquarters(headquarters);
		pdPedailyOrgCrawler.setInvesttype(investType);
		pdPedailyOrgCrawler.setLogolink(logoLink);
		pdPedailyOrgCrawler.setLogourl(logoUrl);
		pdPedailyOrgCrawler.setOrgtype(orgType);
		pdPedailyOrgCrawler.setPhone(phone);
		pdPedailyOrgCrawler.setSetupaddress(setupAddress);
		pdPedailyOrgCrawler.setSetuptime(setupTime);
		pdPedailyOrgCrawler.setShortname(shortName);
		pdPedailyOrgCrawler.setUrl(url);
		pdPedailyOrgCrawler.setWebsite(webSite);
		pdPedailyOrgCrawler.setZipcode(zipcode);
		new PdPedailyOrgCrawlerStore().savePdPedailyOrgCrawler(pdPedailyOrgCrawler);
	}

}
