package com.kf.data.wenshu;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.kf.data.wenshu.chaojiying.CodeCrack;

public class PeopleYzmCrackTest {
	String codeurl = "http://wenshu.court.gov.cn/waf_verify.htm";

	public static void main(String[] args) {
		new PeopleYzmCrackTest().crack();
	}

	public void crack() {
		WebDriver driver = createWebDrive();
		driver.get(codeurl);
		new CodeCrack().processingVerificationCodeForPeople(driver);

	}

	public WebDriver createWebDrive() {
		System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
		WebDriver driver = null;
		DesiredCapabilities cap = new DesiredCapabilities();
		driver = new FirefoxDriver(cap);
		driver.manage().window().maximize();
		return driver;
	}
}
