package com.kf.data.wenshu.tools;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Author:杨庆辉 Time:2017年7月19日 上午11:07:51 Desp:
 */
public class WenshuUtil {

	/**
	 * 截图
	 * 
	 * @param driver
	 * @param element
	 * @param path
	 * @param x
	 * @param y
	 * @throws InterruptedException
	 */
	public static void screenShotForElement(WebDriver driver, WebElement element, String path, int x, int y)
			throws InterruptedException {
		// 截取整个页面的图片
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			// 获取元素在所处frame中位置对象
			Point p = element.getLocation();
			// 获取元素的宽与高
			int width = element.getSize().getWidth();
			int height = element.getSize().getHeight();
			// 矩形图像对象
			Rectangle rect = new Rectangle(width, height);
			BufferedImage img = ImageIO.read(scrFile);
			// x、y表示加上当前frame的左边距,上边距
			BufferedImage dest = img.getSubimage(p.getX() + x, p.getY() + y, rect.width, rect.height);
			ImageIO.write(dest, "png", scrFile);
			FileUtils.copyFile(scrFile, new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
