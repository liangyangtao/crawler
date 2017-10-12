package com.kf.data.mina;

import com.kf.data.fetcher.tools.KfConstant;
import com.kf.data.mina.clent.TLSClinet;

/****
 * 
 * @Title: MutilAppClient.java
 * @Package com.kf.data.mina
 * @Description: 多服务器解析客戶端
 * @author liangyt
 * @date 2017年10月12日 上午11:24:58
 * @version V1.0
 */
public class MutilAppClient {

	public static void main(String[] args) {
		KfConstant.init();
		TLSClinet tLSClinet = new TLSClinet();
		tLSClinet.paser();
	}
}
