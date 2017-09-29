package com.kf.data.fetcher.tools;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import net.sf.json.JSONObject;

public class AliOssSender {

	public static String imagesStreamApi = "http://php-service-file.kaifengdata.com/user/uploadfile";

	public static void main(String[] args) {

		System.out.println(new AliOssSender()
				.uploadObject("http://tm-image.tianyancha.com/tm/774c76d01380590081006d2c810fb381.jpg"));
	}

	public String uploadObject(String objUrl) {
		String filePath = "";
		try {
			Document docs = Jsoup.connect(imagesStreamApi).timeout(50000000).data("file", objUrl).post();
			String body = docs.body().text();
			System.out.println(body);
			JSONObject jsonObject = JSONObject.fromObject(body);
			int err_code = jsonObject.getInt("err_code");
			if (err_code == 0) {
				Object data = jsonObject.get("data");
				JSONObject dataJson = JSONObject.fromObject(data);
				filePath = dataJson.getString("filePath");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(objUrl);
		}
		return filePath;
	}

}
