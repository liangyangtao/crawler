package com.kf.data.tianyancha;

import java.util.Set;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WindowsCloseTest {

	public static void main(String[] args) {
		
		System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
		WebDriver driver = null;
		DesiredCapabilities cap = new DesiredCapabilities();
		driver = new FirefoxDriver(cap);
		driver.manage().window().maximize();
		driver.get("http://www.baidu.com");
		String currenWindow = driver.getWindowHandle();
		
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("window.open('http://www.sina.com.cn/')");
		Set<String> allWindows = driver.getWindowHandles();
		for (String string : allWindows) {
			if (string.equals(currenWindow)) {
				continue;
			} else {
				driver.switchTo().window(string);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (driver.getCurrentUrl().equals("http://www.sina.com.cn/")) {
					break;
				}
			}
		}
		
		driver.close();
		driver.switchTo().window(currenWindow);
		
		

	}
}
