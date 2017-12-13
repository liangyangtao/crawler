package com.kf.data.seeyii;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kf.data.fetcher.Fetcher;
import com.kf.data.fetcher.tools.KfConstant;

/***
 * 
 * @Title: SeeyiiCrawler.java
 * @Package com.kf.data.seeyii
 * @Description: seeyii 产业监控链爬虫
 * @author liangyt
 * @date 2017年12月11日 下午1:38:33
 * @version V1.0
 */
public class SeeyiiCrawler {

	public static void main(String[] args) {
		KfConstant.init();
		new SeeyiiCrawler().crawler();
	}

	/****
	 * 程序入口
	 */
	public void crawler() {

		WebDriver driver = createWebDrive();
		try {
			driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			// 登录
			login(driver);
			driver.get("https://www.seeyii.com/v2/industryChain.html");
			String html = driver.getPageSource();
			Document document = Jsoup.parse(html);
			Elements chainLinkElements = document.select("#mainChain > div > div > svg > g");
			for (int i = 0; i < chainLinkElements.size(); i++) {
				Element element = chainLinkElements.get(i);
				if (element.hasAttr("data-id")) {
					String dataId = element.attr("data-id");
					String dataName = element.attr("data-name");
					String eParam = ParamsParser.getChainParams("id=" + dataId + "&name=" + dataName);
					String chainUrl = "https://www.seeyii.com/v2/industryChainDetail.html?t=2&e=" + eParam;
					String currenWindow = driver.getWindowHandle();
					try {
						JavascriptExecutor executor = (JavascriptExecutor) driver;
						executor.executeScript("window.open('" + chainUrl + "')");
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Set<String> allWindows = driver.getWindowHandles();
						for (String string : allWindows) {
							if (string.equals(currenWindow)) {
								continue;
							} else {
								driver.switchTo().window(string);
								if (driver.getCurrentUrl().equals(chainUrl)) {
									break;
								}
							}
						}
						parserIndustry(driver, dataName, dataId);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						driver.close();
						driver.switchTo().window(currenWindow);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeWebDrive(driver);
		}

	}

	/****
	 * 登录
	 * 
	 * @param driver
	 */
	public void login(WebDriver driver) {
		String url = "https://www.seeyii.com/v2/login.html";
		driver.get(url);
		WebElement accountElement = driver.findElement(By.id("account"));
		accountElement.sendKeys("18515156630");
		WebElement passwordElement = driver.findElement(By.id("pass"));
		passwordElement.sendKeys("123456");
		WebElement loginElement = driver.findElement(By.id("login"));
		loginElement.click();

	}

	/****
	 * 
	 * @param driver
	 * @param chainLink
	 * @param pdataId
	 */
	public void parserIndustry(WebDriver driver, String chainLink, String pdataId) {
		Document document = Jsoup.parse(driver.getPageSource());
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Elements industryLinkelEments = document.select("#chainMap > div > svg");
			if (industryLinkelEments.size() > 0) {
				map.put("content", industryLinkelEments.first().toString());
				map.put("data_id", pdataId);
				map.put("data_name", chainLink);
				sendJson(map, "seeyii_chain_text");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Elements industryLinkelEments = document.select("#chainMap > div > svg > g");
		for (int i = 0; i < industryLinkelEments.size(); i++) {
			Element element = industryLinkelEments.get(i);
			Elements childElements = element.select(".industry-link");
			if (childElements.size() > 0) {
				Element industryElement = childElements.get(0);
				if (industryElement.hasAttr("data-id")) {
					String dataId = industryElement.attr("data-id");
					String dataName = industryElement.attr("data-name");
					String eParam = ParamsParser.getChainParams("id=" + dataId + "&name=" + dataName);
					String industryUrl = "https://www.seeyii.com/v2/industryDetail.html?t=2&e=" + eParam;
					String currenWindow = driver.getWindowHandle();
					try {
						JavascriptExecutor executor = (JavascriptExecutor) driver;
						executor.executeScript("window.open('" + industryUrl + "')");
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Set<String> allWindows = driver.getWindowHandles();
						for (String string : allWindows) {
							if (string.equals(currenWindow)) {
								continue;
							} else {
								driver.switchTo().window(string);
								if (driver.getCurrentUrl().equals(industryUrl)) {
									break;
								}
							}
						}
						try {
							driver.findElement(By.linkText("公司列表")).click();
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						parser3bCompany(driver, chainLink, dataName, dataId, pdataId);

					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						driver.close();
						driver.switchTo().window(currenWindow);
					}
				}
			}

		}

	}

	/****
	 * 解析三板公司
	 * 
	 * @param driver
	 * @param chainLink
	 * @param dataName
	 * @param dataId
	 * @param pdataId
	 */
	public void parser3bCompany(WebDriver driver, String chainLink, String dataName, String dataId, String pdataId) {
		parsercompany3b(driver, chainLink, dataName, dataId, pdataId);
		while (true) {
			String html = driver.getPageSource();
			Document document = Jsoup.parse(html);
			Elements b3Elements = document.select("#company3b");
			if (b3Elements.size() > 0) {
				Element mainElement = b3Elements.first();
				Elements nextPageElements = mainElement.select(".next-page");
				if (nextPageElements.size() > 0) {
					Element nextPageBt = nextPageElements.first();
					if (nextPageBt.classNames().contains("disabled")) {
						break;
					} else {
						try {
							driver.findElement(By.cssSelector(nextPageBt.cssSelector())).click();
							Thread.sleep(5000);
							parsercompany3b(driver, chainLink, dataName, dataId, pdataId);
						} catch (Exception e) {
							e.printStackTrace();
							break;
						}

					}

				} else {
					break;
				}

			} else {
				break;
			}
		}

	}

	public void parsercompany3b(WebDriver driver, String chainLink, String industryText, String dataId,
			String pdataId) {
		String html = driver.getPageSource();
		Document document = Jsoup.parse(html);
		Elements b3Elements = document.select("#company3b");
		if (b3Elements.size() > 0) {
			Element mainElement = b3Elements.first();
			Elements stockCodes = mainElement.select(".stock-code");
			for (Element element : stockCodes) {
				System.out.println(chainLink + "       " + industryText + element.text());
				String stock = element.text();
				stock = stock.replace("(", "");
				stock = stock.replace(")", "");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("chain", chainLink);
				map.put("industry", industryText);
				map.put("stock_code", stock);
				map.put("data_id", dataId);
				map.put("chain_id", pdataId);
				sendJson(map, "seeyii_chain");
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
	 * 
	 * 
	 * @param object
	 * @param type
	 */
	public void sendJson(Object object, String type) {
		String url = KfConstant.saveJsonIp;
		Map<String, String> params = new HashMap<String, String>();
		Gson gson = new GsonBuilder().create();
		params.put("json", gson.toJson(object));
		params.put("type", type);
		Fetcher.getInstance().postSave(url, params, null, "utf-8");
	}

}
