package com.kf.data.neeq.core;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

public class JsonUtils {

	/**
	 * 处理成json串
	 * @return
	 */
	public static String parseToJsonData(Map<String,String> map){
		String result = null;
		Set<FileUrl> fus = new HashSet<FileUrl>();
		for (String key : map.keySet()){
			FileUrl fu = new FileUrl();
			String value = map.get(key);
			fu.setFileUrl(value);
			if(value!=null){
				if(key.contains(".")){
					fu.setFileTitle(key);
				}else{
					int indexOf = value.lastIndexOf(".");
					String suffix = value.substring(indexOf);
					fu.setFileTitle(key+suffix);
				}
				fus.add(fu);
			}
		}
		if(fus.size()>0){
			JsonData jd = new JsonData(); 
			jd.setFiles(fus);
			Gson gson = new Gson();
			String json = gson.toJson(jd);
			if(json!=null){
				result = json;
			}
		}
		return result;
	}
}
