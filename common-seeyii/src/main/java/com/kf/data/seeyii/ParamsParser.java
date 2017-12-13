package com.kf.data.seeyii;

import sun.misc.BASE64Encoder;

public class ParamsParser {
	public static void main(String[] args) {
		System.out.println(getChainParams("id=729&name=其他畜牧养殖"));
		// aWQ9NyZuYW1lPeeVnOeJp+WPiua4lOS4muS6p+S4mumTvg==
		// aWQ9NyZuYW1lPeeVnOeJp+WPiua4lOS4muS6p+S4mumTvg==
		// https://www.seeyii.com/v2/industryDetail.html?t=2&e=aWQ9NzI5Jm5hbWU95YW25LuW55Wc54mn5YW75q6W
		// aWQ9NzI5Jm5hbWU95YW25LuW55Wc54mn5YW75q6W
	}

	public static String getChainParams(String params) {
		String pass = base64Encode(params.getBytes());
		return pass;
	}

	public static String base64Encode(byte[] bytes) {
		return new BASE64Encoder().encode(bytes);
	}

}
