package com.kf.data.fetcher.tools;

/*
 * MD5 算法
 */
public class Md5Tools {

	public static String GetMD5Code(String strObj) {
		String resultString = org.apache.commons.codec.digest.DigestUtils.md5Hex(strObj);
		return resultString;
	}

}
