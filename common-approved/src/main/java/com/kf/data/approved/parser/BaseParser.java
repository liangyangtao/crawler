package com.kf.data.approved.parser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kf.data.fetcher.Fetcher;
import com.kf.data.fetcher.tools.KfConstant;

public class BaseParser {

	/***
	 * 
	 * 
	 * @param object
	 * @param type
	 */
	public void sendJson(Object object, String type) {
		String url = KfConstant.saveJsonIp;
		Map<String, String> params = new HashMap<String, String>();
		Gson gson = new GsonBuilder().create();
		params.put("json", gson.toJson(object));
		params.put("type", type);
		Fetcher.getInstance().postSave(url, params, null, "utf-8");
	}

	public String changeHanzi(String url) {
		char[] tp = url.toCharArray();
		String now = "";
		for (char ch : tp) {
			if (ch >= 0x4E00 && ch <= 0x9FA5) {
				try {
					now += URLEncoder.encode(ch + "", "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else if (ch == '[') {
				try {
					now += URLEncoder.encode(ch + "", "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else if (ch == ']') {
				try {
					now += URLEncoder.encode(ch + "", "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else if (ch == ' ') {
				try {
					now += URLEncoder.encode(ch + "", "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				now += ch;
			}

		}
		return now;
	}

	public static String replacekong(String source) {
		source = source.replace("  ", "");
		source = source.replace(" ", "");
		source = source.replace("	", "");
		source = source.replace(" ", "");
		source = source.replace("  ", "");
		source = source.replace(" ", "");
		source = source.replace("	", "");
		source = source.replace(" ", "");
		source = source.replace("&nbsp;", "");
		return source;
	}

	/****
	 * 格式化字符型的数据
	 * 
	 * @param data
	 * @return
	 */
	public static String formatNumberValue(String data) {
		if (data == null) {
			return null;
		}
		try {
			data = replacekong(data);

			data = data.replace("‐", "-");
			data = data.replace("一", "-");
			data = data.replace("‐", "-");
			data = data.replace("‐", "-");
			data = data.replace("‐", "-");
			data = data.replace("一", "-");
			data = data.replace("-", "-");
			data = data.replace("--", "-");
			data = data.replace("_", "-");
			data = data.replace("―", "-");

			data = data.replace("股", "");
			data = data.replace("年", "");
			data = data.replace("人", "");
			data = data.replace("岁", "");
			data = data.replace("元", "");
			data = data.replace("（注2）", "");
			data = data.replace("￥", "");
			data = data.replace("无", "");
			data = data.replace("【注】", "");
			data = data.replace("约", "");
			data = replacekong(data);
			if (data.equals("-")) {
				data = data.replace("-", "");
			}
			if (data.endsWith("-")) {
				data = data.replace("-", "");
			}

		} catch (Exception e) {
			e.printStackTrace();
			data = null;
		}
		return data;
	}

	public static String formatRadioValue(String data) {
		try {
			if (data == null) {
				return data;
			}
			data = formatNumberValue(data);
			data = data.replace("，", ".");
			data = data.replace(",", ".");
			// data = data.replace("%", "");
			data = data.replace("％", "%");
			data = data.replace("。", ".");
			data = data.replace("．", ".");
			data = data.replace(" · ", ".");
			data = data.replace("·", ".");
			data = data.replace("&", ".");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;

	}

	public static String formatMoneyValue(String data) {
		if (data == null) {
			return data;
		}
		try {
			data = formatNumberValue(data);
			data = data.replace("，", ".");
			data = data.replace(",", ".");
			data = data.replace("%", ".");
			data = data.replace("％", ".");
			data = data.replace("。", ".");
			data = data.replace("．", ".");
			data = data.replace(" · ", ".");
			data = data.replace("·", ".");
			data = data.replace("&", ".");
			if (data.lastIndexOf(".") > 0) {
				if (data.contains("-")) {
					if (data.startsWith("-")) {
						String temp = data.substring(0, data.lastIndexOf("."));
						temp = temp.replace(".", "");
						data = temp + data.substring(data.lastIndexOf("."));
						if (data.endsWith("(万)")) {
							if (data.length() - data.indexOf(".") > 6) {
								data = data.replace(".", "");
							}
						} else if (data.endsWith("万")) {
							if (data.length() - data.indexOf(".") > 4) {
								data = data.replace(".", "");
							}
						} else {
							if (data.length() - data.indexOf(".") > 3) {
								data = data.replace(".", "");
							}
						}
					} else {
						String dataTemp[] = data.split("-");
						StringBuffer dataBuffer = new StringBuffer();
						for (String sp : dataTemp) {
							if (sp.contains(".")) {
								String temp = sp.substring(0, sp.lastIndexOf("."));
								temp = temp.replace(".", "");
								sp = temp + sp.substring(sp.lastIndexOf("."));
								if (sp.endsWith("(万)")) {
									if (sp.length() - sp.indexOf(".") > 6) {
										sp = sp.replace(".", "");
									}
								} else if (sp.endsWith("万")) {
									if (sp.length() - sp.indexOf(".") > 4) {
										sp = sp.replace(".", "");
									}
								} else {
									if (sp.length() - sp.indexOf(".") > 3) {
										sp = sp.replace(".", "");
									}
								}
							}
							dataBuffer.append(sp);
							dataBuffer.append("-");
						}
						data = dataBuffer.toString();
						data = data.substring(0, data.length() - 1);
					}
				} else {
					String temp = data.substring(0, data.lastIndexOf("."));
					temp = temp.replace(".", "");
					data = temp + data.substring(data.lastIndexOf("."));
					if (data.endsWith("(万)")) {
						if (data.length() - data.indexOf(".") > 6) {
							data = data.replace(".", "");
						}
					} else if (data.endsWith("万")) {
						if (data.length() - data.indexOf(".") > 4) {
							data = data.replace(".", "");
						}
					} else {
						if (data.length() - data.indexOf(".") > 3) {
							data = data.replace(".", "");
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;

	}

}
