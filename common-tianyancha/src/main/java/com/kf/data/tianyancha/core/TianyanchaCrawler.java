package com.kf.data.tianyancha.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kf.data.fetcher.tools.Md5Tools;
import com.kf.data.tianyancha.parser.TianyanchaBranchParser;
import com.kf.data.tianyancha.parser.TianyanchaChangeParser;
import com.kf.data.tianyancha.parser.TianyanchaCommonstockChangeParser;
import com.kf.data.tianyancha.parser.TianyanchaCommonstockParser;
import com.kf.data.tianyancha.parser.TianyanchaCompanyParser;
import com.kf.data.tianyancha.parser.TianyanchaCpoyRightWorksParser;
import com.kf.data.tianyancha.parser.TianyanchaEquityParser;
import com.kf.data.tianyancha.parser.TianyanchaHolderParser;
import com.kf.data.tianyancha.parser.TianyanchaIcpParser;
import com.kf.data.tianyancha.parser.TianyanchaImExPortParser;
import com.kf.data.tianyancha.parser.TianyanchaMortgageParser;
import com.kf.data.tianyancha.parser.TianyanchaPatentParser;
import com.kf.data.tianyancha.parser.TianyanchaRecruitParser;
import com.kf.data.tianyancha.parser.TianyanchaRongziParser;
import com.kf.data.tianyancha.parser.TianyanchaSfpmParser;
import com.kf.data.tianyancha.parser.TianyanchaStaffParser;
import com.kf.data.tianyancha.parser.TianyanchaTmParser;
import com.kf.data.tianyancha.parser.TianyanchaWechatParser;

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
	private TianyanchaCompanyParser tianyanchaCompanyParser = new TianyanchaCompanyParser();
	TianyanchaStaffParser tianyanchaStaffParser = new TianyanchaStaffParser();
	TianyanchaHolderParser tianyanchaHolderParser = new TianyanchaHolderParser();
	TianyanchaBranchParser tianyanchaBranchParser = new TianyanchaBranchParser();
	TianyanchaChangeParser tianyanchaChangeParser = new TianyanchaChangeParser();
	TianyanchaMortgageParser tianyanchaMortgageParser = new TianyanchaMortgageParser();

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
			if (title.contains("403")) {
				logger.info(companyName + "IP 已经被封不能采集了");
				int ok = JOptionPane.showConfirmDialog(null, "继续采集");
				if (ok == JOptionPane.OK_OPTION) {

				} else {
					System.exit(0);
				}
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
				int ok = JOptionPane.showConfirmDialog(null, "继续采集");
				if (ok == JOptionPane.OK_OPTION) {

				} else {
					System.exit(0);
				}
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
			String operatingStatus = null;
			try {
				Elements typeElements = listDocument.select(
						"div.search_result_single:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(2)");

				if (typeElements.size() > 0) {
					operatingStatus = typeElements.get(0).text();
				}
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

			Document document = Jsoup.parse(baseHtml);
			// 统一companyId
			String companyId = Md5Tools.GetMD5Code(companyName);
			// 基本信息
			companyName = tianyanchaCompanyParser.paseNode(document, operatingStatus, companyId);
			// 主要人员 高管
			tianyanchaStaffParser.paseNode(document, companyName, companyId);
			// 股东信息
			tianyanchaHolderParser.paseNode(document, companyName, companyId);

			Map<String, Integer> zhibiaoNums = new HashMap<String, Integer>();
			fillZhibiaoNums(zhibiaoNums, document);
			if (zhibiaoNums.get("branchCount") != null && zhibiaoNums.get("branchCount") > 0) {
				// // 分支机构
				tianyanchaBranchParser.paseNode(document, companyName, companyId);
			}
			if (zhibiaoNums.get("changeCount") != null && zhibiaoNums.get("changeCount") > 0) {
				// // 变更记录 没有去重
				tianyanchaChangeParser.paseNode(document, companyName, companyId);

			}
			// 《动产抵押 》栏目解析
			if (zhibiaoNums.get("mortgageCount") != null && zhibiaoNums.get("mortgageCount") > 0) {
				tianyanchaMortgageParser.paseNode(document, companyName, companyId);

			}
			if (zhibiaoNums.get("cpoyRightWorksCount") != null && zhibiaoNums.get("cpoyRightWorksCount") > 0) {
				// // 著作权 处理中
				// // documentId);
				new TianyanchaCpoyRightWorksParser().paseNode(document, companyName, companyId);

			}
			if (zhibiaoNums.get("icpCount") != null && zhibiaoNums.get("icpCount") > 0) {
				// // 网站备案 处理中
				new TianyanchaIcpParser().paseNode(document, companyName, companyId);

			}
			if (zhibiaoNums.get("equityCount") != null && zhibiaoNums.get("equityCount") > 0) {
				// 股权出质 yx 网页
				new TianyanchaEquityParser().paseNode(document, companyName, companyId);

			}
			if (zhibiaoNums.get("patentCount") != null && zhibiaoNums.get("patentCount") > 0) {

				// // 专利
				new TianyanchaPatentParser().paseNode(document, companyName, companyId);

			}
			if (zhibiaoNums.get("recruitCount") != null && zhibiaoNums.get("recruitCount") > 0) {
				// // 招聘 处理中
				new TianyanchaRecruitParser().paseNode(document, companyName, companyId);

			}
			if (zhibiaoNums.get("tmCount") != null && zhibiaoNums.get("tmCount") > 0) {
				// // 商标信息 网页
				new TianyanchaTmParser().paseNode(document, companyName, companyId);

			}

			if (zhibiaoNums.get("rongziCount") != null && zhibiaoNums.get("rongziCount") > 0) {
				// 融资历史
				new TianyanchaRongziParser().paseNode(document, companyName, companyId);

			}
			if (zhibiaoNums.get("commonstockCount") != null && zhibiaoNums.get("commonstockCount") > 0) {
				// 股本结构
				new TianyanchaCommonstockParser().paseNode(document, companyName, companyId);
			}

			if (zhibiaoNums.get("commonstockChangeCount") != null && zhibiaoNums.get("commonstockChangeCount") > 0) {
				// 股本变动
				new TianyanchaCommonstockChangeParser().paseNode(document, companyName, companyId);
			}
			if (zhibiaoNums.get("imExPortCount") != null && zhibiaoNums.get("imExPortCount") > 0) {
				// 进出口信息
				new TianyanchaImExPortParser().paseNode(document, companyName, companyId);
			}

			if (zhibiaoNums.get("sfpmCount") != null && zhibiaoNums.get("sfpmCount") > 0) {
				// 司法拍卖
				new TianyanchaSfpmParser().paseNode(document, companyName, companyId);
			}

			if (zhibiaoNums.get("wechatCount") != null && zhibiaoNums.get("wechatCount") > 0) {
				// 微信公众号
				new TianyanchaWechatParser().paseNode(document, companyName, companyId);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (childDriver != null) {
				childDriver.close();
			}
			closeWebDrive(driver);
		}
	}

	private void fillZhibiaoNums(Map<String, Integer> zhibiaoNums, Document document) {
		String infoCssPath = "div.companypage_2017";
		Element infoElement = getNodeByCssPath(document, infoCssPath);
		Elements divElements = new Elements();
		if (infoElement != null) {
			divElements = infoElement.select(".float-left");
		}
		for (Element element : divElements) {
			Elements zhibiaoElements = element.select(".company-nav-item-enable,.company-nav-item-disable");
			for (Element zhibiaoElement : zhibiaoElements) {
				String text = zhibiaoElement.text().trim();
				text = text.replace("+", "");
				text = text.replace(" ", "");
				// 股票行情
				if (text.contains("股票行情")) {
					text = text.replace("股票行情", "");
					if (text.isEmpty()) {
						text = "0";
					}

					// zhibiaoNums.put("gphqCount", Integer.parseInt(text));
				} else if (text.contains("企业简介")) {
					text = text.replace("企业简介", "");
					if (text.isEmpty()) {
						text = "0";
					}
					// zhibiaoNums.put("qyjjCount", Integer.parseInt(text));
				} else if (text.contains("高管信息")) {
					text = text.replace("高管信息", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("ggxxCount", Integer.parseInt(text));
				} else if (text.contains("参股控股")) {
					text = text.replace("参股控股", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("cgkgCount", Integer.parseInt(text));
				} else if (text.contains("上市公告")) {
					text = text.replace("上市公告", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("ssggCount", Integer.parseInt(text));
				} else if (text.contains("十大股东")) {
					text = text.replace("十大股东", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("sdgdCount", Integer.parseInt(text));
				} else if (text.contains("十大流通")) {
					text = text.replace("十大流通", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("sdltCount", Integer.parseInt(text));
				} else if (text.contains("发行相关")) {
					text = text.replace("发行相关", "");
					if (text.isEmpty()) {
						text = "0";
					}
					// zhibiaoNums.put("fxxgCount", Integer.parseInt(text));
				} else if (text.contains("股本结构")) {
					text = text.replace("股本结构", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("commonstockCount", Integer.parseInt(text));
				} else if (text.contains("股本变动")) {
					text = text.replace("股本变动", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("commonstockChangeCount", Integer.parseInt(text));
				} else if (text.contains("分红情况")) {
					text = text.replace("分红情况", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("fhqkCount", Integer.parseInt(text));
				} else if (text.contains("配股情况")) {
					text = text.replace("配股情况", "");
					if (text.isEmpty()) {
						text = "0";
					}
					// zhibiaoNums.put("pgqkCount", Integer.parseInt(text));
				} else if (text.contains("基本信息")) {
					// text = text.replace("基本信息", "");
					if (text.isEmpty()) {
						text = "0";
					}
					// zhibiaoNums.put("jbxxCount", Integer.parseInt(text));
				} else if (text.contains("企业关系")) {

					// text = text.replace("企业关系", "");
					if (text.isEmpty()) {
						text = "0";
					}
					// zhibiaoNums.put("qygxCount", Integer.parseInt(text));
				} else if (text.contains("主要人员")) {
					text = text.replace("主要人员", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("zyryCount", Integer.parseInt(text));
				} else if (text.contains("股东信息")) {
					text = text.replace("股东信息", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("gdxxCount", Integer.parseInt(text));
				} else if (text.contains("对外投资")) {
					text = text.replace("对外投资", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("inverstCount", Integer.parseInt(text));
				} else if (text.contains("变更记录")) {
					text = text.replace("变更记录", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("changeCount", Integer.parseInt(text));
				} else if (text.contains("企业年报")) {
					text = text.replace("企业年报", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("qynbCount", Integer.parseInt(text));
				} else if (text.contains("分支机构")) {
					text = text.replace("分支机构", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("branchCount", Integer.parseInt(text));
				} else if (text.contains("融资历史")) {
					text = text.replace("融资历史", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("rongziCount", Integer.parseInt(text));
				} else if (text.contains("核心团队")) {
					text = text.replace("核心团队", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("companyTeammember", Integer.parseInt(text));
				} else if (text.contains("企业业务")) {
					text = text.replace("企业业务", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("companyProduct", Integer.parseInt(text));
				} else if (text.contains("投资事件")) {
					text = text.replace("投资事件", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("jigouTzanli", Integer.parseInt(text));
				} else if (text.contains("竞品信息")) {
					text = text.replace("竞品信息", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("companyJingpin", Integer.parseInt(text));
				} else if (text.contains("法律诉讼")) {
					// text = text.replace("法律诉讼", "");
					if (text.isEmpty()) {
						text = "0";
					}
					// zhibiaoNums.put(, Integer.parseInt(text));
				} else if (text.contains("法院公告")) {
					// text = text.replace("法院公告", "");
					if (text.isEmpty()) {
						text = "0";
					}
					// zhibiaoNums.put(, Integer.parseInt(text));
				} else if (text.contains("失信人")) {
					// text = text.replace("失信人", "");
					if (text.isEmpty()) {
						text = "0";
					}
					// zhibiaoNums.put(, Integer.parseInt(text));
				} else if (text.contains("被执行人")) {
					// text = text.replace("被执行人", "");
					if (text.isEmpty()) {
						text = "0";
					}
					// zhibiaoNums.put(, Integer.parseInt(text));
				} else if (text.contains("开庭公告")) {
					// text = text.replace("开庭公告", "");
					if (text.isEmpty()) {
						text = "0";
					}
					// zhibiaoNums.put(, Integer.parseInt(text));
				} else if (text.contains("经营异常")) {
					text = text.replace("经营异常", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("abnormalCount", Integer.parseInt(text));
				} else if (text.contains("行政处罚")) {
					text = text.replace("行政处罚", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("punishment", Integer.parseInt(text));
				} else if (text.contains("严重违法")) {
					text = text.replace("严重违法", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("illegalCount", Integer.parseInt(text));
				} else if (text.contains("股权出质")) {
					text = text.replace("股权出质", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("equityCount", Integer.parseInt(text));
				} else if (text.contains("动产抵押")) {
					text = text.replace("动产抵押", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("mortgageCount", Integer.parseInt(text));
				} else if (text.contains("欠税公告")) {
					text = text.replace("欠税公告", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("ownTaxCount", Integer.parseInt(text));
				} else if (text.contains("司法拍卖")) {
					text = text.replace("司法拍卖", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("sfpmCount", Integer.parseInt(text));
				} else if (text.contains("招投标")) {
					text = text.replace("招投标", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("bidCount", Integer.parseInt(text));
				} else if (text.contains("债券信息")) {
					// text = text.replace("债券信息", "");
					if (text.isEmpty()) {
						text = "0";
					}
					// zhibiaoNums.put(, Integer.parseInt(text));
				} else if (text.contains("购地信息")) {
					text = text.replace("购地信息", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("goudiCount", Integer.parseInt(text));
				} else if (text.contains("税务评级")) {
					text = text.replace("税务评级", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("taxCreditCount", Integer.parseInt(text));
				} else if (text.contains("招聘")) {
					text = text.replace("招聘", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("recruitCount", Integer.parseInt(text));
				} else if (text.contains("抽查检查")) {
					text = text.replace("抽查检查", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("checkCount", Integer.parseInt(text));
				} else if (text.contains("产品信息")) {
					text = text.replace("产品信息", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("productinfo", Integer.parseInt(text));
				} else if (text.contains("进出口信用")) {
					text = text.replace("进出口信用", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("imExPortCount", Integer.parseInt(text));
				} else if (text.contains("资质证书")) {
					text = text.replace("资质证书", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("qualification", Integer.parseInt(text));
				} else if (text.contains("微信公众号")) {
					text = text.replace("微信公众号", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("wechatCount", Integer.parseInt(text));
				} else if (text.contains("商标信息")) {
					text = text.replace("商标信息", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("tmCount", Integer.parseInt(text));
				} else if (text.contains("专利")) {
					text = text.replace("专利", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("patentCount", Integer.parseInt(text));
				} else if (text.contains("软件著作权")) {
					text = text.replace("软件著作权", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("cpoyRCount", Integer.parseInt(text));
				} else if (text.contains("作品著作权")) {
					text = text.replace("作品著作权", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("cpoyRightWorksCount", Integer.parseInt(text));
				} else if (text.contains("网站备案")) {
					text = text.replace("网站备案", "");
					if (text.isEmpty()) {
						text = "0";
					}
					zhibiaoNums.put("icpCount", Integer.parseInt(text));
				}
			}

		}

	}

	public WebDriver createWebDrive() {
		System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
		WebDriver driver = null;
		DesiredCapabilities cap = new DesiredCapabilities();
		driver = new FirefoxDriver(cap);
		driver.manage().window().maximize();
		return driver;
	}

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

	public static Element getNodeByCssPath(Document document, String cssPath) {
		return getNodeByCssPath(document, cssPath, 0);
	}

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
