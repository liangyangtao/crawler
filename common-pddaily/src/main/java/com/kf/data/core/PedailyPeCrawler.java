package com.kf.data.core;

import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.dao.PdPedailyEventCrawlerStore;
import com.kf.data.fetcher.Fetcher;
import com.kf.data.mybatis.entity.PdPedailyEventCrawler;
import com.kf.data.parser.PedailyParser;

/****
 * 投资界投资机构
 * 
 * @Title: PedailyOrgCrawler.java
 * @Package com.kf.data.core
 * @Description: 投资界募资
 * @author liangyt
 * @date 2017年11月3日 下午4:49:17
 * @version V1.0
 */
public class PedailyPeCrawler {

	public static void main(String[] args) {
		new PedailyPeCrawler().crawlerList();
	}

	public void crawlerList() {
		for (int i = 1; i <= (13464 / 20 + 1); i++) {
			System.out.println(i);
			String url = "http://zdb.pedaily.cn/pe/p" + i + "/";
			String html = Fetcher.getInstance().get(url);
			Document document = Jsoup.parse(html, url);
			Elements companylistElements = document.select("#pe-list dt.view a");
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
		try {
			String html = Fetcher.getInstance().get(url);
			Document document = Jsoup.parse(html, url);

			String eventtitle = document.select("div.info > h1").first().text();
			if (eventtitle.isEmpty()) {
				return;
			}

			String fundName = PedailyParser.getValueText(html, "基金名称：");
			String setupTime = PedailyParser.getValueText(html, "成立时间：");
			String orgName = PedailyParser.getValueText(html, "管理机构：");
			String capitalType = PedailyParser.getValueText(html, "资本类型：");
			String currencyType = PedailyParser.getValueText(html, "币 种：");
			String collectType = PedailyParser.getValueText(html, "募集状态：");
			String targetScale = PedailyParser.getValueText(html, "目标规模：");
			String collectMoney = PedailyParser.getValueText(html, "募集金额：");
			String description = PedailyParser.getDescriptionValue(html);

			PdPedailyEventCrawler pdPedailyEventCrawler = new PdPedailyEventCrawler();
			pdPedailyEventCrawler.setCapitaltype(capitalType);
			pdPedailyEventCrawler.setCollectmoney(collectMoney);
			pdPedailyEventCrawler.setCollecttype(collectType);
			pdPedailyEventCrawler.setCreatetime(new Date());
			pdPedailyEventCrawler.setCurrencytype(currencyType);
			pdPedailyEventCrawler.setDescription(description);
			pdPedailyEventCrawler.setEtltime(null);
			pdPedailyEventCrawler.setEventtitle(eventtitle);
			pdPedailyEventCrawler.setExcutetime(new Date());
			pdPedailyEventCrawler.setFundname(fundName);
			pdPedailyEventCrawler.setOrgname(orgName);
			pdPedailyEventCrawler.setSetuptime(setupTime);
			pdPedailyEventCrawler.setTargetscale(targetScale);
			pdPedailyEventCrawler.setUpdatetime(new Date());
			pdPedailyEventCrawler.setUrl(url);
			new PdPedailyEventCrawlerStore().savePdPedailyEventCrawler(pdPedailyEventCrawler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
