package com.kf.data.tianyancha.parser;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.kf.data.mybatis.entity.TycCompanyWechatCrawler;

import net.sf.json.JSONObject;

/***
 * 
 * @Title: TianyanchaWechatParser.java
 * @Package com.kf.data.tianyancha.parser
 * @Description: 微信公众号
 * @author liangyt
 * @date 2017年9月30日 下午4:02:01
 * @version V1.0
 */
public class TianyanchaWechatParser extends TianyanchaBasePaser {

	/***
	 * 微信公众号
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	public void wechatParser(Document document, WebDriver driver, String companyName, String companyId) {
		paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_wechat");
				if (contentNodes.size() > 0) {
					Elements pageElements = contentNodes.first().select(".company_pager");
					if (pageElements.size() > 0) {
						Elements totalElements = pageElements.first().select(".total");
						if (totalElements.size() > 0 && pageIndex == 2) {
							String totalStr = totalElements.first().text().trim();
							totalStr = totalStr.replace("共", "");
							totalStr = totalStr.replace("页", "");
							if (totalStr.isEmpty()) {
								pageNum = 0;
							} else {
								pageNum = Integer.parseInt(totalStr);
							}
						}
						if (pageIndex <= pageNum) {
							Elements liElements = pageElements.select("li");
							if (liElements.size() < 3) {
								break;
							}
							// *[@id="_container_wechat"]/div/div[11]/ul/li[5]/a
							WebElement nextPageBt = driver.findElement(
									By.xpath("//*[@id=\"_container_wechat\"]/div/div[last()]/ul/li[last()]/a"));
							((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageBt);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							document = Jsoup.parse(driver.getPageSource(), driver.getCurrentUrl());
							paseNode(document, companyName, companyId);
							if (liElements.last().classNames().contains("disabled")) {
								break;
							}
							pageIndex++;
						} else {
							break;
						}

					} else {
						break;
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}

	/****
	 * 微信公众号 解析
	 * 
	 * @param document
	 * @param companyName
	 * @param companyId
	 */
	public void paseNode(Document document, String companyName, String companyId) {
		Elements contentNodes = document.select("#_container_wechat");
		if (contentNodes.size() > 0) {
			Elements nodes = contentNodes.first().select(".wechat > div");
			for (Element element : nodes) {
				try {

					// Elements wechatImgElements =
					// element.select(".wechatImg");
					String wechatImg = null;

					String wechatName = null;
					String wechatNumber = null;
					String wechatAbort = null;
					Elements itemRightElements = element.select(".itemRight");
					if (itemRightElements.size() > 0) {
						Elements divs = itemRightElements.first().children();
						if (divs.size() == 3) {
							Elements spanElements = divs.get(2).select("span");
							if (spanElements.size() == 3) {
								String text = spanElements.get(2).attr("onclick");
								text = StringUtils.substringBetween(text, "wechatPopup({", "})");
								text = "{" + text + "}";
								JSONObject fromObj = JSONObject.fromObject(text);
								wechatName = fromObj.getString("title");
								wechatNumber = fromObj.getString("publicNum");
								wechatImg = fromObj.getString("titleImgURL");
								wechatAbort = fromObj.getString("recommend");
							}

						}

					}
					if (wechatName == null) {
						continue;
					}
					// wechatPopup({"title":"中建材集团进出口公司",
					// "titleImgURL":"https:\u002F\u002Fimg.tianyancha.com\u002Fsogou\u002FWeChat\u002F9bed6a73dfe15538c0623dcb316d75f9.png@!watermark01",
					// "codeImg":"https:\u002F\u002Fopen.weixin.qq.com\u002Fqr\u002Fcode\u002F?username=cbmie_cnbm",
					// "publicNum":"cbmie_cnbm","recommend":"中建材集团进出口公司是国资委管辖的中国建材集团有限公司旗下的大型国际业务综合服务平台."})
					TycCompanyWechatCrawler tycCompanyWechatCrawler = new TycCompanyWechatCrawler();
					tycCompanyWechatCrawler.setCompanyId(companyId);
					tycCompanyWechatCrawler.setCompanyName(companyName);
					tycCompanyWechatCrawler.setCreatedAt(new Date());
					tycCompanyWechatCrawler.setStatus((byte) 0);
					tycCompanyWechatCrawler.setUpdatedAt(new Date());
					tycCompanyWechatCrawler.setWechatAbort(wechatAbort);
					tycCompanyWechatCrawler.setWechatName(wechatName);
					tycCompanyWechatCrawler.setWechatNumber(wechatNumber);
					tycCompanyWechatCrawler.setWechatImg(wechatImg);
					sendJson(tycCompanyWechatCrawler, "tyc_company_wechat");
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		}

	}

}
