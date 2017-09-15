package com.kf.data.wenshu.chaojiying;

public abstract class AbstractChaoJiYingHandler {
	
	private static final String USERNAME = "diao852898681";
	private static final String PASSWORD = "kai2016"; 
	private static final String SOFTID = "892078"; 
	
	public String getVerifycodeByChaoJiYing(String codetype, String len_min, String time_add, String str_debug, String filePath) {
		return ChaoJiYingUtils.PostPic(USERNAME, PASSWORD, SOFTID, codetype, len_min, time_add, str_debug, filePath);
	}

	 
}
