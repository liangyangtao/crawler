package com.kf.data.tianyancha;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kf.data.fetcher.Fetcher;

public class SendTest {
   public static void main(String[] args) {
//		String url = "http://localhost:8080/kf-store-api/saveJson";
//		Map<String, String> params = new HashMap<String, String>();
//		Gson gson = new GsonBuilder().create();
//		params.put("json", gson.toJson("aaa"));
//		params.put("type", "das");
//		Fetcher.getInstance().postSave(url, params, null, "utf-8");
	   
	    String html ="<td><script > adsfsf</script> </td>";
	    System.out.println(Jsoup.parse(html).select("script").text());
}
}
