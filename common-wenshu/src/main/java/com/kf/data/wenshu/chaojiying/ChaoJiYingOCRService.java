package com.kf.data.wenshu.chaojiying;

import java.io.File;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ChaoJiYingOCRService extends AbstractChaoJiYingHandler {
	
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	private static final Logger LOGGER = LoggerFactory.getLogger(ChaoJiYingOCRService.class);
	

	public String getVerifycode(File file, String codeType) throws Exception { //codeType: 1104,6003,5000 参照 http://www.chaojiying.cn/price.html
		Gson gson = new GsonBuilder().create();
		String chaoJiYingResult = super.getVerifycodeByChaoJiYing(codeType, LEN_MIN, TIME_ADD, STR_DEBUG, file.getAbsolutePath()); //超级鹰返回结果 参照 http://www.chaojiying.com/api-10.html
		System.out.println("chaoJiYingResult----------"+chaoJiYingResult);
		String errNo = ChaoJiYingUtils.getErrNo(chaoJiYingResult);
		if (!ChaoJiYingUtils.RESULT_SUCCESS.equals(errNo)) {
			return ChaoJiYingUtils.getErrMsg(errNo);
		}
		
		LOGGER.info("ChaoJiYingResult [CODETYPE={}]: {}", codeType, chaoJiYingResult);
		return (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
	}
	
	
	

}
