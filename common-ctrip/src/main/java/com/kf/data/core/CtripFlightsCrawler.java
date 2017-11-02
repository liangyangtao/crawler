package com.kf.data.core;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.kf.data.fetcher.tools.UUIDTools;
import com.kf.data.parser.CtripBookParser;
import com.kf.data.parser.CtripListParser;

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

	public static Log logger = LogFactory.getLog(CtripFlightsCrawler.class);
	// 订单解析
	CtripBookParser ctripBookParser = new CtripBookParser();
	// 列表页解析
	CtripListParser ctripListParser = new CtripListParser();
	// 控制第一个航班
	private int i = 0;
	// 控制不同价位的座位
	private int j = 0;

	public static void main(String[] args) {
		KfConstant.init();
		String url = "http://flights.ctrip.com/international/beijing-macau-bjs-mfm?2017-11-07&y_s";
		new CtripFlightsCrawler().spider(url);
	}

	/***
	 * 程序入口
	 */
	public void spider(String url) {
		logger.info("开始采集航线" + url);
		WebDriver driver = createWebDrive();
		try {
			driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			WebDriverWait wait = new WebDriverWait(driver, 60);
			// 读取首页链接
			driver.get(url);
			Thread.sleep(10000);
			try {
				// 按照时间点击
				wait.until(new ExpectedCondition<WebElement>() {
					@Override
					public WebElement apply(WebDriver d) {
						return d.findElement(By.xpath("//*[@id=\"sortControls\"]/ul[1]/li[2]/span"));
					}
				}).click();

			} catch (Exception e) {
				e.printStackTrace();
			}
			Thread.sleep(10000);
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
			Thread.sleep(10000);
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
			Thread.sleep(5000);
			String html = driver.getPageSource();
			Document document = Jsoup.parse(html);
			if (document.text().contains("对不起，您访问的太快了，休息一下吧。或者登录您的携程帐号继续访问")) {
				Thread.sleep(60 * 1000);
				String[] loadurls = new String[] { "http://hotels.ctrip.com/", "http://vacations.ctrip.com/",
						"http://bus.ctrip.com/", "http://trains.ctrip.com", "http://piao.ctrip.com/" };
				int n = (int) (Math.random() * 5);
				driver.get(loadurls[n]);
				Thread.sleep(30 * 1000);
				driver.get(url);
				Thread.sleep(30 * 1000);
			}
			// 表明有多少航班
			Elements fightItemElements = document.select(".flight-item");
			int num = fightItemElements.size();
			logger.info(url + " 下的航班信息有" + num + "个航班信息");
			for (i = 0; i < fightItemElements.size(); i++) {
				String uuid = UUIDTools.getUUID();
				logger.info(url + " 开始采集第" + (i + 1) + "个航班");
				try {
					((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
					Thread.sleep(2000);
					((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");

					if (locadling(driver)) {
						logger.info(url + "  " + (i + 1) + "航班信息加载失败");
						try {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("url", url);
							map.put("num", i + 1);
							sendJson(map, "ctrip_error");
							logger.info("保存数据");
						} catch (Exception e) {
							e.printStackTrace();
						}
						continue;
					}
					Element fightItem = fightItemElements.get(i);
					ctripListParser.parserList(url, uuid, fightItem);
					Elements seatElements = fightItem.select(".seat-row");
					for (j = 1; j <= seatElements.size(); j++) {
						try {
							logger.info("采集第" + i + "个航班的第" + j + "个座位信息");
							if (i < 15) {
								// 预定
								try {
									// *[@id="flightList"]/div[1]/div[3]/div[2]/div[6]/div/button
									wait.until(new ExpectedCondition<WebElement>() {
										@Override
										public WebElement apply(WebDriver d) {
											if (j == 1) {
												return d.findElement(By.xpath("//*[@id=\"flightList\"]/div[" + (i + 1)
														+ "]/div[3]/div/div[6]/div/button"));

											} else {
												return d.findElement(By.xpath("//*[@id=\"flightList\"]/div[" + (i + 1)
														+ "]/div[3]/div[" + j + "]/div[6]/div/button"));
											}
										}
									}).click();
								} catch (Exception e) {
									e.printStackTrace();
									logger.info(url + "  " + (i + 1) + "点击预定失败");
									try {
										Map<String, Object> map = new HashMap<String, Object>();
										map.put("url", url);
										map.put("num", i + 1);
										sendJson(map, "ctrip_error");
										logger.info("保存数据");
									} catch (Exception e1) {
										e.printStackTrace();
									}
									continue;
								}
							} else if (i >= 15) {
								try {
									wait.until(new ExpectedCondition<WebElement>() {
										@Override
										public WebElement apply(WebDriver d) {
											if (j == 1) {
												return d.findElement(By.xpath("//*[@id=\"flightList\"]/div/div["
														+ (i - 15 + 1) + "]/div[3]/div/div[6]/div/button"));
											} else {
												return d.findElement(By.xpath("//*[@id=\"flightList\"]/div/div["
														+ (i - 15 + 1) + "]/div[3]/div[" + j + "]/div[6]/div/button"));
											}
										}
									}).click();
								} catch (Exception e) {
									e.printStackTrace();
									logger.info(url + "  " + (i + 1) + "点击预定失败");
									try {
										Map<String, Object> map = new HashMap<String, Object>();
										map.put("url", url);
										map.put("num", i + 1);
										sendJson(map, "ctrip_error");
										logger.info("保存数据");
									} catch (Exception e1) {
										e.printStackTrace();
									}
									continue;
								}
							}

							/// 点击之后
							Thread.sleep(5000);
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
							parserBookDetail(driver, uuid, url);
						} catch (Exception e) {
							e.printStackTrace();
							logger.info(url + "  " + (i + 1) + "点击预定失败");
							try {
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("url", url);
								map.put("num", i + 1);
								sendJson(map, "ctrip_error");
								logger.info("保存数据");
							} catch (Exception e1) {
								e.printStackTrace();
							}
							continue;
						} finally {
							driver.get(url);
							Thread.sleep(60 * 1000);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					logger.info(url + "  " + (i + 1) + "点击预定失败");
					try {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("url", url);
						map.put("num", i + 1);
						sendJson(map, "ctrip_error");
						logger.info("保存数据");
					} catch (Exception e1) {
						e.printStackTrace();
					}
					continue;
				} finally {
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
	private void parserBookDetail(WebDriver driver, String uuid, String url) {
		String html = driver.getPageSource();
		Document document = Jsoup.parse(html);
		Elements sidebarElements = document.select("#sidebar");
		if (sidebarElements.size() > 0) {
			// 先保存一份文本
			try {
				ctripBookParser.parserBook(document, uuid, url);
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
						map.put("uuid", uuid);
						map.put("info", info);
						sendJson(map, "ctrip_flights_supplier");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			logger.info("详情页获取失败");
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
