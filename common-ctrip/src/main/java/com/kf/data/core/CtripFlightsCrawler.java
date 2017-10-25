package com.kf.data.core;

import java.net.URLDecoder;
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
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kf.data.fetcher.Fetcher;
import com.kf.data.fetcher.tools.KfConstant;

/***
 * 
 * @Title: CtripCrawler.java
 * @Package com.kf.data.core
 * @Description: 携程机票爬取入口
 * @author liangyt
 * @date 2017年10月25日 上午10:21:33
 * @version V1.0
 */
public class CtripFlightsCrawler {

	private int i = 1;

	public static void main(String[] args) {
		KfConstant.init();
		String url = "http://flights.ctrip.com/international/round-beijing-macau-bjs-mfm?2017-10-28&y_s";
		new CtripFlightsCrawler().spider(url);
	}

	/***
	 * 程序入口
	 */
	public void spider(String url) {
		WebDriver driver = createWebDrive();
		try {
			driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			WebDriverWait wait = new WebDriverWait(driver, 60);
			// 读取首页链接
			driver.get(url);
			Thread.sleep(5000);
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
			Thread.sleep(5000);
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
			Thread.sleep(5000);
			String html = driver.getPageSource();
			Document document = Jsoup.parse(html);
			// 表明有多少航班
			Elements fightItemElements = document.select(".flight-item");
			int num = fightItemElements.size();
			System.out.println(num);
			for (i = 1; i <= num; i++) {
				try {
					((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");

					Thread.sleep(2000);
					((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");

					// 选定
					// wait.until(new ExpectedCondition<WebElement>() {
					// @Override
					// public WebElement apply(WebDriver d) {
					// return d.findElement(
					// By.xpath("//*[@id=\"flightList\"]/div[" + i +
					// "]/div[3]/div/div[6]/div/button"));
					// }
					// }).click();
					// Thread.sleep(5000);
					if (locadling(driver)) {
						System.out.println(i + "加载失败");
						continue;
					}
					if (i <= 15) {
						// 预定
						wait.until(new ExpectedCondition<WebElement>() {
							@Override
							public WebElement apply(WebDriver d) {
								return d.findElement(By
										.xpath("//*[@id=\"flightList\"]/div[" + i + "]/div[3]/div/div[6]/div/button"));
							}
						}).click();
					} else if (i > 15) {
						wait.until(new ExpectedCondition<WebElement>() {
							@Override
							public WebElement apply(WebDriver d) {
								return d.findElement(By.xpath("//*[@id=\"flightList\"]/div/div[" + (i - 15)
										+ "]/div[3]/div/div[6]/div/button"));
							}
						}).click();
					}
					Thread.sleep(5000);
					if (locadling(driver)) {
						System.out.println(i + "加载失败");
						continue;
					}
					// close 不登录直接预定
					try {
						Elements ssoElements = Jsoup.parse(driver.getPageSource()).select("#sso_btnDirectBook");
						if (ssoElements.size() > 0) {
							WebElement closeWebElement = driver.findElement(By.id("sso_btnDirectBook"));
							if (closeWebElement.isDisplayed()) {
								closeWebElement.click();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					Thread.sleep(5000);
					parserBookDetail(driver);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					driver.get(url);
					Thread.sleep(5000);
				}
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
	 * 判断页面是否显示加载中
	 * 
	 * @param driver
	 * @return
	 */
	public boolean locadling(WebDriver driver) {
		try {
			Elements locadElements = Jsoup.parse(driver.getPageSource()).select("#flightsLoading");
			if (locadElements.size() > 0) {
				// 表示显示的是正在加载， 没有数据
				Thread.sleep(5000);
				driver.navigate().refresh();
				Thread.sleep(5000);
				locadElements = Jsoup.parse(driver.getPageSource()).select("#flightsLoading");
				if (locadElements.size() > 0) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/***
	 * 解析详细信息
	 * 
	 * @param driver
	 */
	private void parserBookDetail(WebDriver driver) {
		String html = driver.getPageSource();
		Document document = Jsoup.parse(html);
		Elements sidebarElements = document.select("#sidebar");
		if (sidebarElements.size() > 0) {
			// 先保存一份文本
			try {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("side", sidebarElements.first().toString());
				sendJson(map, "ctrip_flights_side");
			} catch (Exception e) {
				e.printStackTrace();
			}
			// #sidebar 保存供应商
			Elements divInfos = document.select("i.ico-supplier");
			if (divInfos.size() > 0) {
				for (Element element : divInfos) {
					try {
						String data = element.attr("data-source");
						String info = URLDecoder.decode(data, "utf-8");
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("info", info);
						sendJson(map, "ctrip_flights_supplier");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	/***
	 * 
	 * 
	 * @param object
	 * @param type
	 */
	public static void sendJson(Object object, String type) {
		String url = KfConstant.saveJsonIp;
		Map<String, String> params = new HashMap<String, String>();
		Gson gson = new GsonBuilder().create();
		params.put("json", gson.toJson(object));
		params.put("type", type);
		Fetcher.getInstance().postSave(url, params, null, "utf-8");
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
	 * 打开一个浏览器
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
}
