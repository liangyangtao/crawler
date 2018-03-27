package com.kf.data.wenshu.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.openqa.selenium.remote.DesiredCapabilities;

import com.kf.data.fetcher.tools.UUIDTools;
import com.kf.data.mybatis.entity.WenshucourtContentWithBLOBs;
import com.kf.data.mybatis.entity.WenshucourtDataWithBLOBs;
import com.kf.data.wenshu.chaojiying.CodeCrack;
import com.kf.data.wenshu.parser.WenshuParser;
import com.kf.data.wenshu.store.WenshucourtContentStore;
import com.kf.data.wenshu.store.WenshucourtDataStore;

public class WenshuCrawler {

	private static final Integer LITTLE_DELAY = 1000;
	private static final Integer BIGGER_DELAY = 10000;

	String homeurl = "http://wenshu.court.gov.cn/";

	String codeurl = "http://wenshu.court.gov.cn/waf_verify.htm";

	public void crawlerWenshuByCompanyName(String companyName) {
		WebDriver driver = createWebDrive();
		try {
			driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.get(homeurl);
			if (!checkPeopleYzm(driver)) {
				return;
			}
			Thread.sleep(LITTLE_DELAY);
			// 输入搜索框
			WebElement inputElement = driver.findElement(By.id("gover_search_key"));
			if (inputElement == null) {
				sleepAndCheck(driver);
				inputElement = driver.findElement(By.id("gover_search_key"));
				if (inputElement == null) {
					sleepAndCheck(driver);
					return;
				}
			}
			inputElement.click();
			Thread.sleep(LITTLE_DELAY);
			inputElement.sendKeys(companyName);
			Thread.sleep(LITTLE_DELAY);
			// 点击搜索
			driver.findElement(By.xpath("//*[@id=\"searchBox\"]/div/div[3]/button")).click();
			sleepAndCheck(driver);
			// 检测页面和选择每页条数
			Integer resultNum = doCheckAndSelector(driver);
			List<WenshucourtDataWithBLOBs> allList = new ArrayList<>();
			if (resultNum == null) {
			} else {
				do {
					List<WenshucourtDataWithBLOBs> wscds = new WenshuParser().parserList(driver.getPageSource());
					if (wscds != null && wscds.size() > 0) {
						allList.addAll(wscds);
					}
					Thread.sleep(LITTLE_DELAY);
				} while (checkHasNextPage(driver));
			}

			if (allList.size() > 0) {

				for (WenshucourtDataWithBLOBs wenshucourtDataWithBLOBs : allList) {
					try {
						String detailurl = wenshucourtDataWithBLOBs.getDetailedUrl();
						String uuid = UUIDTools.getUUID();
						wenshucourtDataWithBLOBs.setUuid(uuid);
						wenshucourtDataWithBLOBs.setCompanyName(companyName);
						wenshucourtDataWithBLOBs.setCreateTime(new Date());
						wenshucourtDataWithBLOBs.setCompleteTime(new Date());
						new WenshucourtDataStore().saveWenshucourtData(wenshucourtDataWithBLOBs);
						driver.get(detailurl);
						Thread.sleep(BIGGER_DELAY);
						if (!checkPeopleYzm(driver)) {
							continue;
						}
						Thread.sleep(LITTLE_DELAY);
						WenshucourtContentWithBLOBs wenshucourtContent = new WenshuParser()
								.parserContent(driver.getPageSource());
						wenshucourtContent.setCreatetime(new Date());
						wenshucourtContent.setDataUuid(uuid);
						wenshucourtContent.setDetailedUrl(detailurl);
						wenshucourtContent.setUpdatetime(new Date());
						new WenshucourtContentStore().saveWenshucourtContent(wenshucourtContent);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("关闭浏览器");
			closeWebDrive(driver);
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

	/**
	 * 休眠和判断是否为验证码页面
	 * 
	 * @throws Exception
	 */
	public static void sleepAndCheck(WebDriver driver) throws Exception {
		Thread.sleep(BIGGER_DELAY);
		new CodeCrack().processingVerificationCodeForTime(driver);
		Thread.sleep(BIGGER_DELAY);

	}

	/***
	 * 访问量比较大，亲输入验证码
	 * 
	 * @param driver
	 */
	private boolean checkPeopleYzm(WebDriver driver) {
		try {
			Thread.sleep(BIGGER_DELAY);
			if (driver.getCurrentUrl().contains("codeurl")) {
				new CodeCrack().processingVerificationCodeForPeople(driver);
				String html = driver.getPageSource();
				Document document = Jsoup.parse(html);
				Elements warnElements = document.select(".warncontenter");
				if (warnElements.size() > 0) {
					return false;
				}
			}
			Thread.sleep(BIGGER_DELAY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;

	}

	/**
	 * 判断是否还有下一页,有则跳转到下一页
	 * 
	 * @throws Exception
	 */
	public static Boolean checkHasNextPage(WebDriver driver) throws Exception {
		Boolean hasNext = false;
		Document document = Jsoup.parse(driver.getPageSource());
		Element pageNumberDiv = document.getElementById("pageNumber");
		Elements as = pageNumberDiv.getElementsByTag("a");
		if (as == null || as.size() == 0) {
			return hasNext;
		}
		Element nextA = null;
		Integer index = null;
		for (int i = 0; i < as.size(); i++) {
			String aText = as.get(i).text();
			if (aText.contains("下一页")) {
				nextA = as.get(i);
				index = i + 1;
				hasNext = true;
			}
		}
		if (hasNext) {
			// 页面跳转
			driver.findElement(By.xpath("//*[@id=\"pageNumber\"]/a[" + index + "]")).click();
			sleepAndCheck(driver);
		}
		return hasNext;
	}

	/**
	 * 检测页面和选择页数
	 * 
	 * @return
	 */
	public static Integer doCheckAndSelector(WebDriver driver) {
		Integer resultNum = null;
		Document document = Jsoup.parse(driver.getPageSource());
		Element dataCountSpan = document.getElementById("span_datacount");
		if (dataCountSpan == null) {
			return resultNum;
		}
		// 先置为0
		resultNum = 0;
		try {
			resultNum = Integer.valueOf(dataCountSpan.ownText());
			System.out.println("共找到" + resultNum + "个结果");
			// 默认5条一页，大于10，设为每页显示20条
			if (resultNum > 10) {
				try {
					Integer num = 14;
					for (int i = 14; i < 30; i++) {
						Element temp = document.getElementById(i + "_input_20");
						if (temp != null) {
							num = i;
							break;
						}
					}
					driver.findElement(By.id(num + "_button")).click();
					Thread.sleep(LITTLE_DELAY);
					driver.findElement(By.id(num + "_input_20")).click();
					sleepAndCheck(driver);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultNum;
	}

}
