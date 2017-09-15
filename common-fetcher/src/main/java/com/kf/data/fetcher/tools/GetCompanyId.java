package com.kf.data.fetcher.tools;

import com.kf.data.fetcher.Fetcher;

import net.sf.json.JSONObject;

/**
 * @ClassName: GetCompanyId
 * @Description: TODO(传入公司名获取对应的companyId)
 * @author 尹欣
 * @date 2017年8月31日
 * 
 */
public class GetCompanyId {

	public static String getCompanyId(String companyName) {
		String url = "https://test.kaifengdata.com/etlservice/getCompanyValid?companyName=";
		url = url + companyName;
		String html = Fetcher.getInstance().get(url);
		// {"data":{"id":"69144b10-dac1-e348-356e-598befb6d87d"},"code":200,"message":"ok"}
		if (html.startsWith("{")) {
			JSONObject jsonObject = JSONObject.fromObject(html);
			if (jsonObject.getString("message").equals("ok")) {
				JSONObject data = (JSONObject) jsonObject.get("data");
				String id = data.getString("id");
				return id;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param companyName
	 * @return 转公司简称 获取stockId
	 */
	public static String getStockId(String companyName) {
		String url = "https://test.kaifengdata.com/etlservice/getSecurities?securitiesName=";
		url = url + companyName;
		String html = Fetcher.getInstance().get(url);
		if (html.startsWith("{")) {
			JSONObject jsonObject = JSONObject.fromObject(html);
			if (jsonObject.getString("message").equals("ok")) {
				JSONObject data = (JSONObject) jsonObject.get("data");
				String id = data.getString("id");
				return id;
			}
		}
		return null;

	}

	public static void main(String[] args) {
		System.out.println(getCompanyId("网阔信息"));
	}

}
