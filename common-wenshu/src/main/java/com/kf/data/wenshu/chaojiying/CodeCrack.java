package com.kf.data.wenshu.chaojiying;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.kf.data.wenshu.tools.WenshuUtil;

/**
 * Author:杨庆辉 Time:2017年7月19日 上午11:45:39 Desp:
 */
public class CodeCrack {

	/**
	 * 破解webDriver人数过多验证码
	 * 
	 * @param driver
	 * @return
	 */
	public Boolean processingVerificationCodeForPeople(WebDriver driver) {
		System.out.println("------->>>正在处理英文验证码。。。");
		try {
			WebElement imageElement = driver.findElement(By.id("Image1"));
			String codepath = CodeCrack.class.getClassLoader().getResource("").toURI().getPath();
			File file = new File(codepath + File.separator + "imgCode");
			if (!file.exists()) {
				file.mkdirs();
			}
			String path = file.getAbsolutePath() + File.separator + "peoplecode" + ".jpg";
			WenshuUtil.screenShotForElement(driver, imageElement, path, 1, 1);
			String verifycode = new ChaoJiYingOCRService().getVerifycode(new File(path), "1004");
			System.out.println("----------->>>验证码：" + verifycode);
			WebElement inputElement = driver.findElement(By.xpath("/html/body/div/div/div[2]/form/div[2]/input"));
			inputElement.click();
			Thread.sleep(1000);
			inputElement.sendKeys(verifycode);
			Thread.sleep(1000);
			driver.findElement(By.xpath("/html/body/div/div/div[2]/form/div[3]/input")).click();
			Thread.sleep(5000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
		return true;
	}

	/**
	 * 破解webDriver访问次数过多验证码
	 * 
	 * @param driver
	 * @return
	 */
	public Boolean processingVerificationCodeForTime(WebDriver driver) {
		System.out.println("------->>>正在处理数字验证码。。。");
		try {
			WebElement imageElement = driver.findElement(By.name("validateCode"));
			String codepath = CodeCrack.class.getClassLoader().getResource("").toURI().getPath();
			File file = new File(codepath + File.separator + "imgCode");
			if (!file.exists()) {
				file.mkdirs();
			}
			String path = file.getAbsolutePath() + File.separator + "timecode" + ".jpg";
			WenshuUtil.screenShotForElement(driver, imageElement, path, 1, 1);
			String verifycode = new ChaoJiYingOCRService().getVerifycode(new File(path), "1004");
			System.out.println("----------->>>验证码：" + verifycode);
			WebElement inputElement = driver.findElement(By.id("txtValidateCode"));
			inputElement.click();
			Thread.sleep(1000);
			inputElement.sendKeys(verifycode);
			Thread.sleep(1000);
			driver.findElement(By.id("btn_yzmsure")).click();
			Thread.sleep(5000);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 测试webDriver
	 */
	public static void testWebDriver() {
		try {
			DesiredCapabilities cap = new DesiredCapabilities();
			WebDriver driver = new FirefoxDriver(cap);
			driver.get(
					"http://wenshu.court.gov.cn/Html_Pages/VisitRemind.html?DocID=e33ace7e-4aaa-46a9-9d3b-0136817585ba");
			Thread.sleep(5000);
			WebElement imageElement = driver.findElement(By.name("validateCode"));
			// WebElement imageElement =
			// driver.findElement(By.xpath("//*[@id=\"trValidateCode\"]/td[2]/img"));
			String codepath = CodeCrack.class.getResource("").toURI().getPath();
			File file = new File(codepath + File.separator + "imgCode");
			if (!file.exists()) {
				file.mkdirs();
			}
			String path = file.getAbsolutePath() + File.separator + "code" + ".jpg";
			WenshuUtil.screenShotForElement(driver, imageElement, path, 1, 1);
			String verifycode = new ChaoJiYingOCRService().getVerifycode(new File(path), "4004");
			System.out.println("----------->>>验证码：" + verifycode);
			WebElement inputElement = driver.findElement(By.id("txtValidateCode"));
			inputElement.click();
			Thread.sleep(1000);
			inputElement.sendKeys(verifycode);
			Thread.sleep(1000);
			driver.findElement(By.id("btnLogin")).click();
			Thread.sleep(10000);
			driver.quit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * webClient破解验证码
	 * 
	 * @param webClient
	 * @return
	 */
	public Boolean processingVerificationCodeForWebClient(HtmlPage page) {
		System.out.println("------->>>正在处理验证码。。。");
		try {
			HtmlImage image = (HtmlImage) page.getElementById("Image1");
			String codepath = CodeCrack.class.getResource("").toURI().getPath();
			File file = new File(codepath + File.separator + "imgCode");
			if (!file.exists()) {
				file.mkdirs();
			}
			String path = file.getAbsolutePath() + File.separator + "code" + ".jpg";
			// String imageName = CODE_PATH + (Math.random() * 100000) + ".jpg";
			File imageFile = new File(path);
			image.saveAs(imageFile);
			String verifycode = new ChaoJiYingOCRService().getVerifycode(imageFile, "1902");
			System.out.println("----------->>>验证码：" + verifycode);
			HtmlForm form = page.getForms().get(0);
			HtmlTextInput input = form.getInputByName("captcha");
			input.setValueAttribute(verifycode);
			HtmlSubmitInput submitInput = form.getInputByValue("确定");
			HtmlPage result = submitInput.click();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;

	}

	/**
	 * 测试webClient
	 */
	public static void testWebClient() {
		try {
			WebClient webClient = new WebClient(BrowserVersion.FIREFOX_45);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			webClient.getOptions().setThrowExceptionOnScriptError(true);
			webClient.getOptions().setTimeout(10000);
			HtmlPage page = webClient.getPage("http://wenshu.court.gov.cn/waf_verify.htm");
			HtmlImage image = (HtmlImage) page.getElementById("Image1");
			String codepath = CodeCrack.class.getResource("").toURI().getPath();
			File file = new File(codepath + File.separator + "imgCode");
			if (!file.exists()) {
				file.mkdirs();
			}
			String path = file.getAbsolutePath() + File.separator + "code" + ".jpg";
			// String imageName = CODE_PATH + (Math.random() * 100000) + ".jpg";
			File imageFile = new File(path);
			image.saveAs(imageFile);
			String verifycode = new ChaoJiYingOCRService().getVerifycode(imageFile, "1902");
			System.out.println("----------->>>验证码：" + verifycode);
			HtmlForm form = page.getForms().get(0);
			HtmlTextInput input = form.getInputByName("captcha");
			input.setValueAttribute(verifycode);
			HtmlSubmitInput submitInput = form.getInputByValue("确定");
			HtmlPage result = submitInput.click();
			Thread.sleep(5000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {

		testWebDriver();
	}
}
