package com.kf.data.fetcher.tools;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReportDataFormat {

	/***
	 * 单位转换
	 * 
	 * @param data
	 * @return
	 */
	public static Double bigUnitChange(String data) {
		Double result = null;
		try {
			String numberValue = formatNumberValue(data);
			if (numberValue == null || numberValue.isEmpty()) {
				result = null;
			} else {
				BigDecimal bigdataValue = null;
				if (numberValue.endsWith("亿")) {
					numberValue = numberValue.replace("亿", "");
					bigdataValue = new BigDecimal(numberValue);
					result = bigdataValue.multiply(new BigDecimal(10000000)).doubleValue();
				} else if (numberValue.endsWith("千万")) {
					numberValue = numberValue.replace("千万", "");
					bigdataValue = new BigDecimal(numberValue);
					result = bigdataValue.multiply(new BigDecimal(10000000)).doubleValue();
				} else if (numberValue.endsWith("百万")) {
					numberValue = numberValue.replace("百万", "");
					bigdataValue = new BigDecimal(numberValue);
					result = bigdataValue.multiply(new BigDecimal(1000000)).doubleValue();
				} else if (data.endsWith("十万")) {
					numberValue = numberValue.replace("十万", "");
					bigdataValue = new BigDecimal(numberValue);
					result = bigdataValue.multiply(new BigDecimal(100000)).doubleValue();
				} else if (numberValue.endsWith("万")) {
					numberValue = numberValue.replace("万", "");
					bigdataValue = new BigDecimal(numberValue);
					result = bigdataValue.multiply(new BigDecimal(10000)).doubleValue();
				} else {
					bigdataValue = new BigDecimal(numberValue);
					result = bigdataValue.doubleValue();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	/**
	 * 字符串转int
	 * 
	 * @param value
	 * @return
	 */
	public static Integer intValueChange(String value) {
		Integer result = null;
		try {
			String formatNumberValue = formatNumberValue(value);
			if (formatNumberValue == null || formatNumberValue.isEmpty()) {
				return null;
			} else {
				result = Integer.parseInt(formatNumberValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 字符串转int
	 * 
	 * @param value
	 * @return
	 */
	public static Double doubleValueChange(String value) {
		Double result = null;
		try {
			String formatNumberValue = formatNumberValue(value);
			if (formatNumberValue == null || formatNumberValue.isEmpty()) {
				return null;
			} else {
				result = Double.parseDouble(formatNumberValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	/***
	 * 
	 * 格式化数字类型的值 - 123432,12321,12.00
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
			data = data.replace("，", "");
			data = data.replace(",", "");
			data = data.replace(" ", "");
			data = data.replace(" ", "");
			data = data.replace("%", "");
			data = data.replace("％", "");
			data = data.replace("。", ".");
			data = data.replace("．", ".");
			data = data.replace(" ", "");
			data = data.replace("‐", "-");
			data = data.replace("一", "-");
			data = data.replace("‐", "-");
			data = data.replace("‐", "-");
			data = data.replace("‐", "-");
			data = data.replace("一", "-");
			data = data.replace("-", "-");
			data = data.replace("--", "");
			data = data.replace("_", "");
			data = data.replace("股", "");
			data = data.replace("年", "");
			data = data.replace("人", "");
			data = data.replace("岁", "");
			data = data.replace("元", "");
			data = data.replace("（注2）", "");
			data = data.replace("￥", "");
			data = data.replace("无", "");
			data = data.replace("【注】", "");
			data = data.replace("―", "");
			// ￥180万元（人民币）
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

	/****
	 * 将值里面的空格去掉
	 * 
	 * @param source
	 * @return
	 */
	public static String replacekong(String source) {
		source = source.replace("  ", "");
		source = source.replace(" ", "");
		source = source.replace("	", "");
		source = source.replace(" ", "");
		return source;
	}

	static char kongChar[] = new char[] { ' ', ' ', ' ', '	', ' ', ' ', '　' };

	/***
	 * tr
	 * 
	 * @param source
	 * @return
	 */
	public String trimStr(String source) {
		char val[] = source.toCharArray();
		int len = val.length;
		int st = 0;
		while (st < len) {
			boolean iskong = false;
			for (char temp : kongChar) {
				if (val[st] == temp) {
					iskong = true;
					break;
				}
			}

			if (!iskong) {
				break;
			}
			st++;
		}
		while (len > 0) {
			boolean iskong = false;
			for (char temp : kongChar) {
				if (val[len - 1] == temp) {
					iskong = true;
					break;
				}
			}
			if (!iskong) {
				break;
			}
			len--;

		}
		return source.substring(st, len);
	}

	/****
	 * 格式化字符型的数据
	 * 
	 * @param data
	 * @return
	 */
	public static String formatVarcharData(String data) {
		if (data == null) {
			return "";
		}
		try {
			data = data.replace("，", ",");
			data = data.replace("。", ".");
			data = data.replace("．", ".");
			data = data.replace("‐", "-");
			data = data.replace("‐", "-");
			data = data.replace("‐", "-");
			data = data.replace("‐", "-");
			data = data.replace("-", "-");
			data = data.replace("-", "-");
			data = data.replace("-", "-");
			data = data.replace("：", ":");
			data = data.replace("_", "");
			data = data.replace("―", "-");
			data = data.replace("；", ";");
			// data = data.replace("�", "");
			if (data.equals("-")) {
				data = "";
			}
			if (data.equals("---")) {
				data = "";
			}
			if (data.endsWith("-")) {
				data = data.replace("-", "");
			}

		} catch (Exception e) {
			e.printStackTrace();
			data = "";
		}
		return data;
	}

	/***
	 * 抽取自2013年1月1日起计算 这种类型的时间
	 * 
	 * @param data
	 * @return
	 */
	public String getDateFromStr(String data) {
		String result = null;
		if (data == null) {
			return "";
		}
		if (data.contains("自") && data.contains("起")) {
			result = getStrByReg("自", "起", data);
		}
		return result;

	}

	public String getStrByReg(String pre, String end, String content) {
		content = replacekong(content);
		end = end.replace("(", "\\(");
		end = end.replace(")", "\\)");
		end = end.replace("|", "\\|");
		end = end.replace("[", "\\[");
		end = end.replace("]", "\\]");
		end = end.replace("?", "\\?");
		end = end.replace("!", "\\!");
		end = end.replace(".", "\\.");
		end = end.replace("*", "\\*");
		end = replacekong(end);
		////////////////////////////////
		pre = pre.replace("(", "\\(");
		pre = pre.replace(")", "\\)");
		pre = pre.replace("|", "\\|");
		pre = pre.replace("[", "\\[");
		pre = pre.replace("]", "\\]");
		pre = pre.replace("?", "\\?");
		pre = pre.replace("!", "\\!");
		pre = pre.replace(".", "\\.");
		pre = pre.replace("*", "\\*");
		pre = replacekong(pre);
		String regEx = pre + "((?!" + pre + "|" + end + ").)+(?=" + end + ")";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			String string = matcher.group();
			string = string.replace(pre, "");
			string = string.replace(end, "");
			string = string.trim();
			return string;
		}
		return null;
	}

	/**
	 * 字符转为时间类型
	 * 
	 * @param string
	 * @return
	 */
	public static Date parseStringToDate(String string) {
		Date result = null;
		try {
			Pattern pattern = Pattern
					.compile("((\\d{4}|\\d{2})(\\-|\\/|\\.)\\d{1,2}\\3\\d{1,2})|(\\d{4}年\\d{1,2}月\\d{1,2}日)");
			Matcher matcher = pattern.matcher(string);

			String dateStr = null;
			if (matcher.find()) {
				dateStr = matcher.group(0);
			}
			if (dateStr != null) {
				String trim = dateStr.replaceAll(".", "/").replaceAll("-", "/").replaceAll("年", "/")
						.replaceAll("月", "/").replaceAll("日", "/").replaceAll(" ", "").trim();
				result = new SimpleDateFormat("yyyy/MM/dd").parse(trim);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

}
