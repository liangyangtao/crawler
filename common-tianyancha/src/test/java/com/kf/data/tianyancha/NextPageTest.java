package com.kf.data.tianyancha;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class NextPageTest {

	public static void main(String[] args) {
		new NextPageTest().recruitParser();
	}

	public void recruitParser() {
		System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
		WebDriver driver = null;
		DesiredCapabilities cap = new DesiredCapabilities();
		driver = new FirefoxDriver(cap);
		driver.manage().window().maximize();
		driver.get("https://www.tianyancha.com/company/22822");
		Document document = Jsoup.parse(driver.getPageSource());
		int pageIndex = 2;
		int pageNum = 0;
		// 招聘 处理中
		while (true) {
			Elements contentNodes = document.select("#_container_recruit");
			if (contentNodes.size() > 0) {
				Elements pageElements = contentNodes.first().select(".company_pager");
				if (pageElements.size() > 0) {
					Elements totalElements = pageElements.first().select(".total");
					if (totalElements.size() > 0) {
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
								By.xpath("//*[@id=\"_container_recruit\"]/div/div[2]/ul/li[" + size + "]/a"));
						((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageBt);
						// JavascriptExecutor jsLib = new JavascriptExecutor();
						// jsLib.callEmbeddedSelenium(driver,"triggerMouseEventAt",
						// element,"click", "0,0");

						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						document = Jsoup.parse(driver.getPageSource());
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
		}

	}

}
