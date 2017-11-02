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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kf.data.fetcher.tools.Md5Tools;
import com.kf.data.tianyancha.parser.TianyanchaAbnormalOperationParser;
import com.kf.data.tianyancha.parser.TianyanchaAdmPenaltyParser;
import com.kf.data.tianyancha.parser.TianyanchaAnnouncementParser;
import com.kf.data.tianyancha.parser.TianyanchaBondParser;
import com.kf.data.tianyancha.parser.TianyanchaBranchParser;
import com.kf.data.tianyancha.parser.TianyanchaBusinessParser;
import com.kf.data.tianyancha.parser.TianyanchaCaseNoticeParser;
import com.kf.data.tianyancha.parser.TianyanchaCaseParser;
import com.kf.data.tianyancha.parser.TianyanchaCertificateParser;
import com.kf.data.tianyancha.parser.TianyanchaChangeParser;
import com.kf.data.tianyancha.parser.TianyanchaCheckParser;
import com.kf.data.tianyancha.parser.TianyanchaCommonstockChangeParser;
import com.kf.data.tianyancha.parser.TianyanchaCommonstockParser;
import com.kf.data.tianyancha.parser.TianyanchaCompanyParser;
import com.kf.data.tianyancha.parser.TianyanchaCompetitorsParser;
import com.kf.data.tianyancha.parser.TianyanchaCoreTeamParser;
import com.kf.data.tianyancha.parser.TianyanchaCpoyRightWorksParser;
import com.kf.data.tianyancha.parser.TianyanchaEquityParser;
import com.kf.data.tianyancha.parser.TianyanchaEventsTenderBidParser;
import com.kf.data.tianyancha.parser.TianyanchaHolderParser;
import com.kf.data.tianyancha.parser.TianyanchaIcpParser;
import com.kf.data.tianyancha.parser.TianyanchaImExPortParser;
import com.kf.data.tianyancha.parser.TianyanchaInvestOutSideParser;
import com.kf.data.tianyancha.parser.TianyanchaInvestParser;
import com.kf.data.tianyancha.parser.TianyanchaMortgageParser;
import com.kf.data.tianyancha.parser.TianyanchaPatentParser;
import com.kf.data.tianyancha.parser.TianyanchaProductParser;
import com.kf.data.tianyancha.parser.TianyanchaRecruitParser;
import com.kf.data.tianyancha.parser.TianyanchaRongziParser;
import com.kf.data.tianyancha.parser.TianyanchaSfpmParser;
import com.kf.data.tianyancha.parser.TianyanchaShixinParser;
import com.kf.data.tianyancha.parser.TianyanchaSoftCopyrightParser;
import com.kf.data.tianyancha.parser.TianyanchaStaffParser;
import com.kf.data.tianyancha.parser.TianyanchaTaxArrearsParser;
import com.kf.data.tianyancha.parser.TianyanchaTaxRatingParser;
import com.kf.data.tianyancha.parser.TianyanchaTmParser;
import com.kf.data.tianyancha.parser.TianyanchaWechatParser;
import com.kf.data.tianyancha.parser.TianyanchaYearReportParser;
import com.kf.data.tianyancha.parser.TianyanchaZhixingParser;
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

	private ZhibiaoNumCrawler zhibiaoNumCrawler = new ZhibiaoNumCrawler();

	// 基本信息
	private TianyanchaCompanyParser tianyanchaCompanyParser = new TianyanchaCompanyParser();
	// 主要人员
	private TianyanchaStaffParser tianyanchaStaffParser = new TianyanchaStaffParser();
	// 股东信息
	private TianyanchaHolderParser tianyanchaHolderParser = new TianyanchaHolderParser();
	// 对外投资
	private TianyanchaInvestOutSideParser tianyanchaInvestOutSideParser = new TianyanchaInvestOutSideParser();
	// 变更记录
	private TianyanchaChangeParser tianyanchaChangeParser = new TianyanchaChangeParser();
	// 企业年报
	private TianyanchaYearReportParser tianyanchaYearReportParser = new TianyanchaYearReportParser();
	// 分支机构
	private TianyanchaBranchParser tianyanchaBranchParser = new TianyanchaBranchParser();
	// 融资历史
	private TianyanchaRongziParser tianyanchaRongziParser = new TianyanchaRongziParser();
	// 核心团队
	private TianyanchaCoreTeamParser tianyanchaCoreTeamParser = new TianyanchaCoreTeamParser();
	// 企业业务
	private TianyanchaBusinessParser tianyanchaBusinessParser = new TianyanchaBusinessParser();
	// 投资事件
	private TianyanchaInvestParser tianyanchaInvestParser = new TianyanchaInvestParser();
	// 竞品信息
	private TianyanchaCompetitorsParser tianyanchaCompetitorsParser = new TianyanchaCompetitorsParser();
	// 法律诉讼
	private TianyanchaCaseParser tianyanchaCaseParser = new TianyanchaCaseParser();
	// 法院公告
	private TianyanchaCaseNoticeParser tianyanchaCaseNoticeParser = new TianyanchaCaseNoticeParser();
	// 失信人
	private TianyanchaShixinParser tianyanchaShixinParser = new TianyanchaShixinParser();
	// 被执行人
	private TianyanchaZhixingParser tianyanchaZhixingParser = new TianyanchaZhixingParser();
	// 开庭公告
	private TianyanchaAnnouncementParser tianyanchaAnnouncementParser = new TianyanchaAnnouncementParser();
	// 经营异常
	private TianyanchaAbnormalOperationParser tianyanchaAbnormalOperationParser = new TianyanchaAbnormalOperationParser();
	// 行政处罚
	private TianyanchaAdmPenaltyParser tianyanchaAdmPenaltyParser = new TianyanchaAdmPenaltyParser();

	// 严重违法

	// 股权出质
	private TianyanchaEquityParser tianyanchaEquityParser = new TianyanchaEquityParser();
	// 动产抵押
	private TianyanchaMortgageParser tianyanchaMortgageParser = new TianyanchaMortgageParser();
	// 欠税公告
	private TianyanchaTaxArrearsParser tianyanchaTaxArrearsParser = new TianyanchaTaxArrearsParser();
	// 司法拍卖
	private TianyanchaSfpmParser tianyanchaSfpmParser = new TianyanchaSfpmParser();
	// 招投标
	private TianyanchaEventsTenderBidParser tianyanchaEventsTenderBidParser = new TianyanchaEventsTenderBidParser();
	// 债券信息
	private TianyanchaBondParser tianyanchaBondParser = new TianyanchaBondParser();
	// 购地信息
	// 招聘信息
	private TianyanchaRecruitParser tianyanchaRecruitParser = new TianyanchaRecruitParser();
	// 税务评级
	private TianyanchaTaxRatingParser tianyanchaTaxRatingParser = new TianyanchaTaxRatingParser();
	// 抽查检查
	private TianyanchaCheckParser tianyanchaCheckParser = new TianyanchaCheckParser();
	// 产品信息
	private TianyanchaProductParser tianyanchaProductParser = new TianyanchaProductParser();
	// 进出口
	private TianyanchaImExPortParser tianyanchaImExPortParser = new TianyanchaImExPortParser();
	// 资质证书
	private TianyanchaCertificateParser tianyanchaCertificateParser = new TianyanchaCertificateParser();
	// 微信公众号
	private TianyanchaWechatParser tianyanchaWechatParser = new TianyanchaWechatParser();
	// 商标
	private TianyanchaTmParser tianyanchaTmParser = new TianyanchaTmParser();
	// 专利
	private TianyanchaPatentParser tianyanchaPatentParser = new TianyanchaPatentParser();
	// 软件著作权
	private TianyanchaSoftCopyrightParser tianyanchaSoftCopyrightParser = new TianyanchaSoftCopyrightParser();
	// 作品著作权
	private TianyanchaCpoyRightWorksParser tianyanchaCpoyRightWorksParser = new TianyanchaCpoyRightWorksParser();
	// 域名备案信息
	private TianyanchaIcpParser tianyanchaIcpParser = new TianyanchaIcpParser();
	////////////////////////////////////////////////////
	// 股本
	private TianyanchaCommonstockParser tianyanchaCommonstockParser = new TianyanchaCommonstockParser();
	// 股本变动
	private TianyanchaCommonstockChangeParser tianyanchaCommonstockChangeParser = new TianyanchaCommonstockChangeParser();

	// 邮件
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
		logger.info("开始采集" + companyName);
		try {
			driver.get(url);
			Thread.sleep(8 * 1000);
			String title = driver.getTitle();
			if (title.contains("访问禁止")) {
				logger.info(companyName + "IP 已经被封不能采集了");
				sendMail.sendMail("IP已经被封");
				// pidRecorder.reStart();
				System.exit(0);
			}
			if (title.contains("403")) {
				logger.info(companyName + "IP 已经被封不能采集了");
				sendMail.sendMail("IP已经被封");
				// pidRecorder.reStart();
				System.exit(0);
			}
			String loginUrl = driver.getCurrentUrl();
			if (loginUrl.contains("login")) {
				sendMail.sendMail("IP已经被封");
				logger.info(companyName + "需要登录 不能采集");
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
				// pidRecorder.reStart();
				System.exit(0);
			}
			if (current.contains("www.tianyancha.com/login")) {
				logger.info(companyName + "需要登录 不能采集");
				sendMail.sendMail("IP已经被封");
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
				return;
			}
			try {
				WebElement hrefElement = driver.findElement(
						By.xpath("//*[@id=\"web-content\"]/div/div/div/div[1]/div[3]/div[1]/div[2]/div[1]/a"));
				hrefElement.click();
			} catch (Exception e) {
				e.printStackTrace();
				logger.info(companyName + "没有搜索到结果异常");
				return;
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
				// pidRecorder.reStart();
				System.exit(0);
			}
			if (current.contains("www.tianyancha.com/login")) {
				logger.info(companyName + "需要登录 不能采集");
				sendMail.sendMail("IP已经被封");
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
			driver.switchTo().window(key);
			String baseHtml = driver.getPageSource();

			Document document = Jsoup.parse(baseHtml, "https://www.tianyancha.com");
			// 统一companyId
			String companyId = Md5Tools.GetMD5Code(companyName);

			/********************************************************************************************/
			// 基本信息
			companyName = tianyanchaCompanyParser.paseNode(document, companyId);
			// 主要人员 高管
			tianyanchaStaffParser.paseNode(document, companyName, companyId);
			// 股东信息
			tianyanchaHolderParser.paseNode(document, companyName, companyId);

			Map<String, Integer> zhibiaoNums = new HashMap<String, Integer>();

			zhibiaoNumCrawler.fillZhibiaoNums(zhibiaoNums, document);

			if (zhibiaoNums.get("investAbroadCount") != null && zhibiaoNums.get("investAbroadCount") > 0) {
				// 对外投资
				tianyanchaInvestOutSideParser.investAbroadParser(document, driver, companyName, companyId);
			}
			if (zhibiaoNums.get("changeCount") != null && zhibiaoNums.get("changeCount") > 0) {
				// 变更记录
				tianyanchaChangeParser.changeParser(document, driver, companyName, companyId);

			}
			if (zhibiaoNums.get("reportCount") != null && zhibiaoNums.get("reportCount") > 0) {
				// 年报
				tianyanchaYearReportParser.yearPortParser(document, companyName, companyId, driver);
			}

			if (zhibiaoNums.get("branchCount") != null && zhibiaoNums.get("branchCount") > 0) {
				// 分支机构
				tianyanchaBranchParser.branchParser(document, driver, companyName, companyId);

			}
			/******************************************************************************/
			if (zhibiaoNums.get("rongziCount") != null && zhibiaoNums.get("rongziCount") > 0) {
				// 融资历史
				tianyanchaRongziParser.rongziParser(document, driver, companyName, companyId);
			}

			if (zhibiaoNums.get("coreTeamCount") != null && zhibiaoNums.get("coreTeamCount") > 0) {
				// 核心团队
				tianyanchaCoreTeamParser.coreTeamParser(document, driver, companyName, companyId);
			}

			if (zhibiaoNums.get("businessCount") != null && zhibiaoNums.get("businessCount") > 0) {
				// 企业业务
				tianyanchaBusinessParser.businessParser(document, driver, companyName, companyId);
			}

			if (zhibiaoNums.get("investCount") != null && zhibiaoNums.get("investCount") > 0) {
				// 投资事件
				tianyanchaInvestParser.investParser(document, driver, companyName, companyId);
			}
			if (zhibiaoNums.get("competitorsCount") != null && zhibiaoNums.get("competitorsCount") > 0) {
				// 竞品信息
				tianyanchaCompetitorsParser.competitorsParser(document, driver, companyName, companyId);
			}
			/********************************************************************************/
			if (zhibiaoNums.get("caseCount") != null && zhibiaoNums.get("caseCount") > 0) {
				// 法律诉讼
				tianyanchaCaseParser.caseParser(document, driver, companyName, companyId);
			}
			if (zhibiaoNums.get("caseNoticeCount") != null && zhibiaoNums.get("caseNoticeCount") > 0) {
				// 法院公告
				tianyanchaCaseNoticeParser.caseNoticeParser(document, driver, companyName, companyId);
			}
			if (zhibiaoNums.get("shixinCount") != null && zhibiaoNums.get("shixinCount") > 0) {
				// 失信人
				tianyanchaShixinParser.shixinParser(document, driver, companyName, companyId);
			}
			if (zhibiaoNums.get("zhixingCount") != null && zhibiaoNums.get("zhixingCount") > 0) {
				// 被执行人
				tianyanchaZhixingParser.zhixingParser(document, driver, companyName, companyId);
			}

			if (zhibiaoNums.get("announcementCount") != null && zhibiaoNums.get("announcementCount") > 0) {
				// 开庭公告
				tianyanchaAnnouncementParser.announcementParser(document, driver, companyName, companyId);
			}

			/********************************************************************************/
			if (zhibiaoNums.get("abnormalCount") != null && zhibiaoNums.get("abnormalCount") > 0) {
				// 经营异常
				tianyanchaAbnormalOperationParser.abnormalOperationParser(document, driver, companyName, companyId);
			}
			if (zhibiaoNums.get("admPenaltyCount") != null && zhibiaoNums.get("admPenaltyCount") > 0) {
				// 行政处罚
				tianyanchaAdmPenaltyParser.admPenaltyParser(document, driver, companyName, companyId);
			}

			// 严重违法
			// illegalCount

			if (zhibiaoNums.get("equityCount") != null && zhibiaoNums.get("equityCount") > 0) {
				// 股权出质
				tianyanchaEquityParser.equityParser(document, driver, companyName, companyId);
			}

			if (zhibiaoNums.get("mortgageCount") != null && zhibiaoNums.get("mortgageCount") > 0) {
				// 动产抵押
				tianyanchaMortgageParser.mortgageParser(document, driver, companyName, companyId);

			}

			if (zhibiaoNums.get("taxArrearsCount") != null && zhibiaoNums.get("taxArrearsCount") > 0) {
				// 欠税公告
				tianyanchaTaxArrearsParser.taxArrearsParser(document, driver, companyName, companyId);

			}

			if (zhibiaoNums.get("sfpmCount") != null && zhibiaoNums.get("sfpmCount") > 0) {
				// 司法拍卖
				tianyanchaSfpmParser.sfpmParser(document, driver, companyName, companyId);

			}

			/********************************************************************************/
			if (zhibiaoNums.get("eventsTenderBidCount") != null && zhibiaoNums.get("eventsTenderBidCount") > 0) {
				// 招投标
				tianyanchaEventsTenderBidParser.eventsTenderBidParser(document, driver, companyName, companyId);

			}
			// 债券信息
			if (zhibiaoNums.get("bondCount") != null && zhibiaoNums.get("bondCount") > 0) {
				// 债券信息
				tianyanchaBondParser.bondParser(document, driver, companyName, companyId);
			}

			// 购地信息
			if (zhibiaoNums.get("taxRatingCount") != null && zhibiaoNums.get("taxRatingCount") > 0) {
				// 税务评级
				tianyanchaTaxRatingParser.taxRatingParser(document, driver, companyName, companyId);
			}

			if (zhibiaoNums.get("recruitCount") != null && zhibiaoNums.get("recruitCount") > 0) {
				// 招聘
				tianyanchaRecruitParser.recruitParser(document, driver, companyName, companyId);
			}

			if (zhibiaoNums.get("checkCount") != null && zhibiaoNums.get("checkCount") > 0) {
				// 抽查检查
				tianyanchaCheckParser.checkParser(document, driver, companyName, companyId);
			}

			if (zhibiaoNums.get("productCount") != null && zhibiaoNums.get("productCount") > 0) {
				// 产品信息
				tianyanchaProductParser.productParser(document, driver, companyName, companyId);
			}

			if (zhibiaoNums.get("imExPortCount") != null && zhibiaoNums.get("imExPortCount") > 0) {
				tianyanchaImExPortParser.imExPortParser(document, driver, companyName, companyId);
				// 进出口信息
			}

			if (zhibiaoNums.get("certificateCount") != null && zhibiaoNums.get("certificateCount") > 0) {
				tianyanchaCertificateParser.certificateParser(document, driver, companyName, companyId);
				// 资质证书
			}

			if (zhibiaoNums.get("wechatCount") != null && zhibiaoNums.get("wechatCount") > 0) {
				// 微信公众号
				tianyanchaWechatParser.wechatParser(document, driver, companyName, companyId);
			}

			/********************************************************************************/

			if (zhibiaoNums.get("tmCount") != null && zhibiaoNums.get("tmCount") > 0) {
				// 商标信息 网页
				tianyanchaTmParser.tmParser(document, driver, companyName, companyId);
			}

			if (zhibiaoNums.get("patentCount") != null && zhibiaoNums.get("patentCount") > 0) {
				tianyanchaPatentParser.patentParser(document, driver, companyName, companyId);
				// 专利
			}

			if (zhibiaoNums.get("softCopyrightCount") != null && zhibiaoNums.get("softCopyrightCount") > 0) {
				tianyanchaSoftCopyrightParser.softCopyrightParser(document, driver, companyName, companyId);
				// 软件著作权
			}

			if (zhibiaoNums.get("cpoyRightWorksCount") != null && zhibiaoNums.get("cpoyRightWorksCount") > 0) {
				// 作品著作权
				tianyanchaCpoyRightWorksParser.cpoyRightWorksParser(document, driver, companyName, companyId);

			}
			if (zhibiaoNums.get("icpCount") != null && zhibiaoNums.get("icpCount") > 0) {
				// 网站备案
				tianyanchaIcpParser.ipcParser(document, driver, companyName, companyId);

			}
			/*********************************************************************************/
			if (zhibiaoNums.get("commonstockCount") != null && zhibiaoNums.get("commonstockCount") > 0) {
				// 股本结构
				tianyanchaCommonstockParser.commonstockParser(document, driver, companyName, companyId);
			}
			if (zhibiaoNums.get("commonstockChangeCount") != null && zhibiaoNums.get("commonstockChangeCount") > 0) {
				tianyanchaCommonstockChangeParser.commonstockChangeParser(document, driver, companyName, companyId);
				// 股本变动
			}
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			closeWebDrive(driver);
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
		// DesiredCapabilities cap = new DesiredCapabilities();
		ProfilesIni pi = new ProfilesIni();
		FirefoxProfile profile = pi.getProfile("default");
		driver = new FirefoxDriver(profile);
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
