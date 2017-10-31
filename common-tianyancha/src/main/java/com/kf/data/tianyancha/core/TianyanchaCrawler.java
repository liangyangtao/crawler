package com.kf.data.tianyancha.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kf.data.fetcher.tools.Md5Tools;
import com.kf.data.tianyancha.parser.TianyanchaBranchParser;
import com.kf.data.tianyancha.parser.TianyanchaBusinessParser;
import com.kf.data.tianyancha.parser.TianyanchaChangeParser;
import com.kf.data.tianyancha.parser.TianyanchaCommonstockChangeParser;
import com.kf.data.tianyancha.parser.TianyanchaCommonstockParser;
import com.kf.data.tianyancha.parser.TianyanchaCompanyParser;
import com.kf.data.tianyancha.parser.TianyanchaCompetitorsParser;
import com.kf.data.tianyancha.parser.TianyanchaCoreTeamParser;
import com.kf.data.tianyancha.parser.TianyanchaCpoyRightWorksParser;
import com.kf.data.tianyancha.parser.TianyanchaEquityParser;
import com.kf.data.tianyancha.parser.TianyanchaHolderParser;
import com.kf.data.tianyancha.parser.TianyanchaIcpParser;
import com.kf.data.tianyancha.parser.TianyanchaImExPortParser;
import com.kf.data.tianyancha.parser.TianyanchaInvestOutSideParser;
import com.kf.data.tianyancha.parser.TianyanchaMortgageParser;
import com.kf.data.tianyancha.parser.TianyanchaPatentParser;
import com.kf.data.tianyancha.parser.TianyanchaRecruitParser;
import com.kf.data.tianyancha.parser.TianyanchaRongziParser;
import com.kf.data.tianyancha.parser.TianyanchaSfpmParser;
import com.kf.data.tianyancha.parser.TianyanchaStaffParser;
import com.kf.data.tianyancha.parser.TianyanchaTmParser;
import com.kf.data.tianyancha.parser.TianyanchaWechatParser;
import com.kf.data.tianyancha.parser.TianyanchaYearReportParser;
import com.kf.data.tianyancha.sender.SendMail;

/**
 * @Title: TianyanchaCrawler.java
 * @Package com.kf.data.crawler.tianyancha.core
 * @Description: 天眼查程序主程序
 * @author liangyt
 * @date 2017年5月2日 下午4:58:47
 * @version V1.0
 */
public class TianyanchaCrawler {

	private final static Logger logger = LoggerFactory.getLogger(TianyanchaCrawler.class);
	private static final String url = "http://www.tianyancha.com";
	ZhibiaoNumCrawler zhibiaoNumCrawler = new ZhibiaoNumCrawler();

	private TianyanchaCompanyParser tianyanchaCompanyParser = new TianyanchaCompanyParser();
	private TianyanchaStaffParser tianyanchaStaffParser = new TianyanchaStaffParser();
	private TianyanchaHolderParser tianyanchaHolderParser = new TianyanchaHolderParser();
	private TianyanchaBranchParser tianyanchaBranchParser = new TianyanchaBranchParser();
	private TianyanchaChangeParser tianyanchaChangeParser = new TianyanchaChangeParser();
	private TianyanchaMortgageParser tianyanchaMortgageParser = new TianyanchaMortgageParser();
	private TianyanchaYearReportParser tianyanchaYearReportParser = new TianyanchaYearReportParser();
	private TianyanchaWechatParser tianyanchaWechatParser = new TianyanchaWechatParser();
	private TianyanchaCpoyRightWorksParser tianyanchaCpoyRightWorksParser = new TianyanchaCpoyRightWorksParser();
	private TianyanchaIcpParser tianyanchaIcpParser = new TianyanchaIcpParser();
	private TianyanchaEquityParser tianyanchaEquityParser = new TianyanchaEquityParser();
	private TianyanchaPatentParser tianyanchaPatentParser = new TianyanchaPatentParser();
	private TianyanchaRecruitParser tianyanchaRecruitParser = new TianyanchaRecruitParser();
	private TianyanchaTmParser tianyanchaTmParser = new TianyanchaTmParser();
	private TianyanchaRongziParser tianyanchaRongziParser = new TianyanchaRongziParser();
	private TianyanchaCommonstockParser tianyanchaCommonstockParser = new TianyanchaCommonstockParser();
	private TianyanchaCommonstockChangeParser tianyanchaCommonstockChangeParser = new TianyanchaCommonstockChangeParser();
	private TianyanchaImExPortParser tianyanchaImExPortParser = new TianyanchaImExPortParser();
	private TianyanchaSfpmParser tianyanchaSfpmParser = new TianyanchaSfpmParser();
	private TianyanchaInvestOutSideParser tianyanchaInvestOutSideParser = new TianyanchaInvestOutSideParser();
	private TianyanchaCoreTeamParser tianyanchaCoreTeamParser = new TianyanchaCoreTeamParser();
	private TianyanchaBusinessParser tianyanchaBusinessParser = new TianyanchaBusinessParser();
	private TianyanchaCompetitorsParser tianyanchaCompetitorsParser = new TianyanchaCompetitorsParser();

	private SendMail sendMail = new SendMail();

	/***
	 * 输入名称进行数据采集入库
	 * 
	 * @param company
	 */
	public void parserTycBaseCompany(String companyName) {
		WebDriver driver = createWebDrive();
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		WebDriver childDriver = null;
		logger.info("开始采集" + companyName);
		try {
			driver.get(url);
			Thread.sleep(5 * 1000);
			String title = driver.getTitle();
			if (title.contains("访问禁止")) {
				logger.info(companyName + "IP 已经被封不能采集了");
				sendMail.sendMail("IP已经被封");
				// int ok = JOptionPane.showConfirmDialog(null, "继续采集");
				// if (ok == JOptionPane.OK_OPTION) {
				//
				// } else {
				//
				// }
				// pidRecorder.reStart();
				System.exit(0);
			}
			if (title.contains("403")) {
				logger.info(companyName + "IP 已经被封不能采集了");
				sendMail.sendMail("IP已经被封");
				// int ok = JOptionPane.showConfirmDialog(null, "继续采集");
				// if (ok == JOptionPane.OK_OPTION) {
				// } else {
				// System.exit(0);
				// }
				// pidRecorder.reStart();
				System.exit(0);
			}
			String loginUrl = driver.getCurrentUrl();
			if (loginUrl.contains("login")) {
				sendMail.sendMail("IP已经被封");
				logger.info(companyName + "需要登录 不能采集");
				// int ok = JOptionPane.showConfirmDialog(null, "继续采集");
				// if (ok == JOptionPane.OK_OPTION) {
				// } else {
				// System.exit(0);
				// }
				// pidRecorder.reStart();
				System.exit(0);
			}

			// 将搜索词输入文本框
			driver.findElement(By.xpath("//*[@id=\"home-main-search\"]")).sendKeys(companyName);
			// 点击搜索按钮
			driver.findElement(By.cssSelector(".search_button")).click();
			// 等待5秒
			Thread.sleep(3 * 1000);
			String current = driver.getCurrentUrl();
			if (current.contains("http://antirobot.tianyancha.com")) {
				logger.info(companyName + "遇到验证码已经被封不能采集了");
				sendMail.sendMail("IP已经被封");
				// int ok = JOptionPane.showConfirmDialog(null, "继续采集");
				// if (ok == JOptionPane.OK_OPTION) {
				//
				// } else {
				// System.exit(0);
				// }
				// pidRecorder.reStart();
				System.exit(0);
			}
			if (current.contains("www.tianyancha.com/login")) {
				logger.info(companyName + "需要登录 不能采集");
				sendMail.sendMail("IP已经被封");
				// int ok = JOptionPane.showConfirmDialog(null, "继续采集");
				// if (ok == JOptionPane.OK_OPTION) {
				//
				// } else {
				// System.exit(0);
				// }
				// pidRecorder.reStart();
				System.exit(0);
			}
			String html = driver.getPageSource();
			if (html.contains("static.tianyancha.com/wap/images/notFound.png")) {
				logger.info(companyName + "没有搜索到结果异常");
				return;
			}
			if (html.contains("static.tianyancha.com/wap/images/notFound-text.png")) {
				logger.info(companyName + "没有搜索到结果异常");
				return;
			}

			Document listDocument = Jsoup.parse(html);
			Elements aElements = listDocument.select(
					"div.search_result_single:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(2)");
			if (aElements.size() > 0) {
			} else {
				logger.info(companyName + "没有搜索到结果异常");
			}
			try {
				WebElement hrefElement = driver.findElement(
						By.xpath("//*[@id=\"web-content\"]/div/div/div/div[1]/div[3]/div[1]/div[2]/div[1]/a"));
				hrefElement.click();
			} catch (Exception e) {
				e.printStackTrace();
				logger.info(companyName + "没有搜索到结果异常");
			}

			try {
				Thread.sleep(8000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			current = driver.getCurrentUrl();
			if (current.contains("http://antirobot.tianyancha.com")) {
				logger.info(companyName + "遇到验证码已经被封不能采集了");
				sendMail.sendMail("IP已经被封");
				// int ok = JOptionPane.showConfirmDialog(null, "继续采集");
				// if (ok == JOptionPane.OK_OPTION) {
				//
				// } else {
				// System.exit(0);
				// }
				// pidRecorder.reStart();
				System.exit(0);
			}
			if (current.contains("www.tianyancha.com/login")) {
				logger.info(companyName + "需要登录 不能采集");
				sendMail.sendMail("IP已经被封");
				// int ok = JOptionPane.showConfirmDialog(null, "继续采集");
				// if (ok == JOptionPane.OK_OPTION) {
				//
				// } else {
				// System.exit(0);
				// }
				System.exit(0);
			}

			String currenWindow = driver.getWindowHandle();
			Set<String> allWindows = driver.getWindowHandles();
			String key = null;
			for (String string : allWindows) {
				if (string.equals(currenWindow)) {
					continue;
				} else {
					key = string;
				}
			}
			childDriver = driver.switchTo().window(key);
			childDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
			childDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			String baseHtml = childDriver.getPageSource();
			baseHtml = childDriver.getPageSource();
			Document document = Jsoup.parse(baseHtml, "https://www.tianyancha.com");
			// 统一companyId
			String companyId = Md5Tools.GetMD5Code(companyName);
			// 基本信息
			companyName = tianyanchaCompanyParser.paseNode(document, companyId);
			// 主要人员 高管
			tianyanchaStaffParser.paseNode(document, companyName, companyId);
			// 股东信息
			tianyanchaHolderParser.paseNode(document, companyName, companyId);

			Map<String, Integer> zhibiaoNums = new HashMap<String, Integer>();
			zhibiaoNumCrawler.fillZhibiaoNums(zhibiaoNums, document);
			if (zhibiaoNums.get("branchCount") != null && zhibiaoNums.get("branchCount") > 0) {
				// 分支机构
				branchParser(document, driver, companyName, companyId);

			}
			if (zhibiaoNums.get("changeCount") != null && zhibiaoNums.get("changeCount") > 0) {
				// 变更记录
				changeParser(document, driver, companyName, companyId);

			}
			// 《动产抵押 》栏目解析
			if (zhibiaoNums.get("mortgageCount") != null && zhibiaoNums.get("mortgageCount") > 0) {
				mortgageParser(document, driver, companyName, companyId);

			}
			if (zhibiaoNums.get("cpoyRightWorksCount") != null && zhibiaoNums.get("cpoyRightWorksCount") > 0) {
				// 著作权 处理中
				cpoyRightWorksParser(document, driver, companyName, companyId);

			}
			if (zhibiaoNums.get("icpCount") != null && zhibiaoNums.get("icpCount") > 0) {
				// 网站备案 处理中
				// *[@id="_container_icp"]/div/div[2]/ul/li[13]/a
				ipcParser(document, driver, companyName, companyId);

			}
			if (zhibiaoNums.get("equityCount") != null && zhibiaoNums.get("equityCount") > 0) {
				// 股权出质 yx 网页
				equityParser(document, driver, companyName, companyId);

			}
			if (zhibiaoNums.get("patentCount") != null && zhibiaoNums.get("patentCount") > 0) {
				patentParser(document, driver, companyName, companyId);
				// 专利

			}
			if (zhibiaoNums.get("recruitCount") != null && zhibiaoNums.get("recruitCount") > 0) {
				recruitParser(document, driver, companyName, companyId);
			}
			if (zhibiaoNums.get("tmCount") != null && zhibiaoNums.get("tmCount") > 0) {
				// 商标信息 网页
				tmParser(document, driver, companyName, companyId);

			}

			if (zhibiaoNums.get("rongziCount") != null && zhibiaoNums.get("rongziCount") > 0) {
				// 融资历史
				rongziParser(document, driver, companyName, companyId);

			}
			if (zhibiaoNums.get("commonstockCount") != null && zhibiaoNums.get("commonstockCount") > 0) {
				// 股本结构

				commonstockParser(document, driver, companyName, companyId);
			}

			if (zhibiaoNums.get("commonstockChangeCount") != null && zhibiaoNums.get("commonstockChangeCount") > 0) {
				commonstockChangeParser(document, driver, companyName, companyId);
				// 股本变动

			}
			if (zhibiaoNums.get("imExPortCount") != null && zhibiaoNums.get("imExPortCount") > 0) {
				imExPortParser(document, driver, companyName, companyId);
				// 进出口信息

			}

			if (zhibiaoNums.get("sfpmCount") != null && zhibiaoNums.get("sfpmCount") > 0) {
				// 司法拍卖
				sfpmParser(document, driver, companyName, companyId);

			}

			if (zhibiaoNums.get("wechatCount") != null && zhibiaoNums.get("wechatCount") > 0) {
				// 微信公众号
				wechatParser(document, driver, companyName, companyId);

			}

			if (zhibiaoNums.get("investAbroadCount") != null && zhibiaoNums.get("investAbroadCount") > 0) {
				// 对外投资
				tianyanchaInvestOutSideParser.investAbroadParser(document, driver, companyName, companyId);

			}
			if (zhibiaoNums.get("coreTeamCount") != null && zhibiaoNums.get("coreTeamCount") > 0) {
				// 核心团队
				tianyanchaCoreTeamParser.coreTeamCountParser(document, driver, companyName, companyId);
			}

			if (zhibiaoNums.get("businessCount") != null && zhibiaoNums.get("businessCount") > 0) {
				// 企业业务
				tianyanchaBusinessParser.coreTeamCountParser(document, driver, companyName, companyId);
			}
			
			if (zhibiaoNums.get("competitorsCount") != null && zhibiaoNums.get("competitorsCount") > 0) {
				// 竞品信息
				tianyanchaCompetitorsParser.coreTeamCountParser(document, driver, companyName, companyId);
			}

			// reportCount
			if (zhibiaoNums.get("reportCount") != null && zhibiaoNums.get("reportCount") > 0) {
				// 企业年报
				Elements reportNodes = document.select(".report_item_2017");
				for (Element element : reportNodes) {
					try {
						Element linkElement = element.select("a").first();
						String reportLink = linkElement.absUrl("href");

						driver.get(reportLink);
						String reportHtml = driver.getPageSource();
						Document reportDocument = Jsoup.parse(reportHtml, reportLink);
						String reportdate = element.select(".pt15").first().text().trim();
						tianyanchaYearReportParser.paseNode(reportDocument, companyName, companyId, reportdate);
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						driver.navigate().back();
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			if (childDriver != null) {
				childDriver.close();
			}
			closeWebDrive(driver);
		}
	}

	/***
	 * 动产抵押
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	private void mortgageParser(Document document, WebDriver driver, String companyName, String companyId) {
		tianyanchaMortgageParser.paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_mortgage");
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
							WebElement nextPageBt = driver.findElement(
									By.xpath("//*[@id=\"_container_mortgage\"]/div/div[last()]/ul/li[last()]/a"));
							((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageBt);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							document = Jsoup.parse(driver.getPageSource());
							tianyanchaMortgageParser.paseNode(document, companyName, companyId);
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

	/***
	 * 股权出质
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	private void equityParser(Document document, WebDriver driver, String companyName, String companyId) {
		tianyanchaEquityParser.paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_equity");
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
							WebElement nextPageBt = driver.findElement(
									By.xpath("//*[@id=\"_container_equity\"]/div/div[last()]/ul/li[last()]/a"));
							((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageBt);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							document = Jsoup.parse(driver.getPageSource());
							tianyanchaEquityParser.paseNode(document, companyName, companyId);
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

	/***
	 * 融资历史
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	private void rongziParser(Document document, WebDriver driver, String companyName, String companyId) {
		tianyanchaRongziParser.paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_rongzi");
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
							WebElement nextPageBt = driver.findElement(
									By.xpath("//*[@id=\"_container_rongzi\"]/div/div[last()]/ul/li[last()]/a"));
							((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageBt);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							document = Jsoup.parse(driver.getPageSource());
							tianyanchaRongziParser.paseNode(document, companyName, companyId);
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

	/***
	 * 股本结构
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	private void commonstockParser(Document document, WebDriver driver, String companyName, String companyId) {
		tianyanchaCommonstockParser.paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_shareStructure");
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
							WebElement nextPageBt = driver.findElement(
									By.xpath("//*[@id=\"_container_shareStructure\"]/div/div[last()]/ul/li[last()]/a"));
							((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageBt);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							document = Jsoup.parse(driver.getPageSource());
							tianyanchaCommonstockParser.paseNode(document, companyName, companyId);
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

	/***
	 * 股本变动
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	private void commonstockChangeParser(Document document, WebDriver driver, String companyName, String companyId) {
		tianyanchaCommonstockChangeParser.paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_equityChange");
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
							WebElement nextPageBt = driver.findElement(
									By.xpath("//*[@id=\"_container_equityChange\"]/div/div[last()]/ul/li[last()]/a"));
							((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageBt);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							document = Jsoup.parse(driver.getPageSource());
							tianyanchaCommonstockChangeParser.paseNode(document, companyName, companyId);
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

	/***
	 * 进出口信息
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	private void imExPortParser(Document document, WebDriver driver, String companyName, String companyId) {
		tianyanchaImExPortParser.paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_importAndExport");
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
							// *[@id="_container_wechat"]/div/div[11]/ul/li[5]/a
							WebElement nextPageBt = driver.findElement(By
									.xpath("//*[@id=\"_container_importAndExport\"]/div/div[last()]/ul/li[last()]/a"));
							((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageBt);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							document = Jsoup.parse(driver.getPageSource());
							tianyanchaImExPortParser.paseNode(document, companyName, companyId);
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

	/***
	 * 司法拍卖
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	private void sfpmParser(Document document, WebDriver driver, String companyName, String companyId) {
		tianyanchaSfpmParser.paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_judicialSale");
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
							// *[@id="_container_wechat"]/div/div[11]/ul/li[5]/a
							WebElement nextPageBt = driver.findElement(
									By.xpath("//*[@id=\"_container_judicialSale\"]/div/div[last()]/ul/li[last()]/a"));
							((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageBt);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							document = Jsoup.parse(driver.getPageSource());
							tianyanchaSfpmParser.paseNode(document, companyName, companyId);
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

	/***
	 * 分支机构
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	private void branchParser(Document document, WebDriver driver, String companyName, String companyId) {
		tianyanchaBranchParser.paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_branch");
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
							// *[@id="_container_wechat"]/div/div[11]/ul/li[5]/a
							WebElement nextPageBt = driver.findElement(
									By.xpath("//*[@id=\"_container_branch\"]/div/div[last()]/ul/li[last()]/a"));
							((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageBt);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							document = Jsoup.parse(driver.getPageSource());
							tianyanchaBranchParser.paseNode(document, companyName, companyId);
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

	/***
	 * 微信公众号
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	private void wechatParser(Document document, WebDriver driver, String companyName, String companyId) {
		tianyanchaWechatParser.paseNode(document, companyName, companyId);
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
							// *[@id="_container_wechat"]/div/div[11]/ul/li[5]/a
							WebElement nextPageBt = driver.findElement(
									By.xpath("//*[@id=\"_container_wechat\"]/div/div[last()]/ul/li[last()]/a"));
							((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageBt);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							document = Jsoup.parse(driver.getPageSource());
							tianyanchaWechatParser.paseNode(document, companyName, companyId);
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

	/***
	 * 商标信息
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	private void tmParser(Document document, WebDriver driver, String companyName, String companyId) {
		tianyanchaTmParser.paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_tmInfo");
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
							int size = liElements.size();
							// *[@id="_container_tmInfo"]/div/div/div[2]/ul/li[13]/a
							// *[@id="_container_tmInfo"]/div/div/div[2]/ul/li[13]/a
							// *[@id="_container_tmInfo"]/div/div/div[2]/ul/li[13]/a
							try {
								WebElement nextPageBt = driver.findElement(
										By.xpath("//*[@id=\"_container_tmInfo\"]/div/div/div[last()]/ul/li[last()]/a"));
								((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageBt);
							} catch (Exception e) {
								e.printStackTrace();
								break;
							}
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							document = Jsoup.parse(driver.getPageSource());
							tianyanchaTmParser.paseNode(document, companyName, companyId);
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

	/***
	 * 专利
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	private void patentParser(Document document, WebDriver driver, String companyName, String companyId) {
		tianyanchaPatentParser.paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_patent");
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
							int size = liElements.size();
							// *[@id="_container_patent"]/div/div/ul/li[13]/a
							WebElement nextPageBt = driver.findElement(
									By.xpath("//*[@id=\"_container_patent\"]/div/div[last()]/ul/li[last()]/a"));
							((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageBt);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							document = Jsoup.parse(driver.getPageSource());
							tianyanchaPatentParser.paseNode(document, companyName, companyId);
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

	/***
	 * 网站备案
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	private void ipcParser(Document document, WebDriver driver, String companyName, String companyId) {
		tianyanchaIcpParser.paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_icp");
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
							int size = liElements.size();
							// *[@id="_container_icp"]/div/div[2]/ul/li[13]/a
							WebElement nextPageBt = driver.findElement(
									By.xpath("//*[@id=\"_container_icp\"]/div/div[last()]/ul/li[last()]/a"));
							((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageBt);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							document = Jsoup.parse(driver.getPageSource());
							tianyanchaIcpParser.paseNode(document, companyName, companyId);
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

	/***
	 * 作品著作权
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	private void cpoyRightWorksParser(Document document, WebDriver driver, String companyName, String companyId) {
		tianyanchaCpoyRightWorksParser.paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_copyrightWorks");
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
							// *[@id="_container_copyrightWorks"]/div/div/ul/li[13]/a
							WebElement nextPageBt = driver.findElement(
									By.xpath("//*[@id=\"_container_copyrightWorks\"]/div/div[last()]/ul/li[last()]/a"));
							((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageBt);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							document = Jsoup.parse(driver.getPageSource());
							tianyanchaCpoyRightWorksParser.paseNode(document, companyName, companyId);
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
	 * 变更信息解析
	 * 
	 * @param document
	 * @param driver
	 * @param companyName
	 * @param companyId
	 */
	private void changeParser(Document document, WebDriver driver, String companyName, String companyId) {
		tianyanchaChangeParser.paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_changeinfo");
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
							// *[@id="_container_changeinfo"]/div/div[2]/ul/li[6]/a
							// *[@id="_container_changeinfo"]/div/div[2]/ul/li[4]/a
							WebElement nextPageBt = driver.findElement(
									By.xpath("//*[@id=\"_container_changeinfo\"]/div/div[last()]/ul/li[last()]/a"));
							((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageBt);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							document = Jsoup.parse(driver.getPageSource());
							tianyanchaChangeParser.paseNode(document, companyName, companyId);
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

	/***
	 * 招聘翻页抽取数据
	 * 
	 * @param document
	 * @param driver
	 */
	public void recruitParser(Document document, WebDriver driver, String companyName, String companyId) {
		tianyanchaRecruitParser.paseNode(document, companyName, companyId);
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			try {
				Elements contentNodes = document.select("#_container_recruit");
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
							int size = liElements.size();
							WebElement nextPageBt = driver.findElement(
									By.xpath("//*[@id=\"_container_recruit\"]/div/div[last()]/ul/li[last()]/a"));
							((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageBt);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							document = Jsoup.parse(driver.getPageSource());
							tianyanchaRecruitParser.paseNode(document, companyName, companyId);
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

	/***
	 * 启动浏览器
	 * 
	 * @return
	 */
	public WebDriver createWebDrive() {
		System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
		WebDriver driver = null;
		DesiredCapabilities cap = new DesiredCapabilities();
		driver = new FirefoxDriver(cap);
		driver.manage().window().maximize();
		return driver;
	}

	/***
	 * 关闭浏览器
	 * 
	 * @param driver
	 */
	public void closeWebDrive(WebDriver driver) {
		if (driver != null) {
			Set<String> allWindows = driver.getWindowHandles();
			for (String string : allWindows) {
				driver.switchTo().window(string).close();
			}
			driver.quit();
		} else {
			System.out.println("关闭的Driver 是空的");
		}

	}

	/***
	 * 根据csspath 获取element
	 * 
	 * @param document
	 * @param cssPath
	 * @return
	 */
	public static Element getNodeByCssPath(Document document, String cssPath) {
		return getNodeByCssPath(document, cssPath, 0);
	}

	/***
	 * 根据csspath 获取第几个 element
	 * 
	 * @param document
	 * @param cssPath
	 * @param index
	 * @return
	 */
	public static Element getNodeByCssPath(Document document, String cssPath, int index) {
		Elements elments = document.select(cssPath);
		if (elments.size() != 0) {
			Element element = elments.get(index);
			return element;
		} else {
			return null;
		}
	}

}
