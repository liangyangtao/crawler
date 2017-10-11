package com.kf.data.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/***
 * 
 * @Title: CommonController.java
 * @Package com.kf.data.web.controller
 * @Description: 基础类
 * @author liangyt
 * @date 2017年10月11日 下午2:35:34
 * @version V1.0
 */
public class CommonController {

	/***
	 * 实体转换json 串
	 * 
	 * @param obj
	 * @return
	 */
	protected String objectToJson(Object obj) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		return gson.toJson(obj);
	}

	/**
	 * 对象转化成map
	 * 
	 * @param obj
	 * @return
	 */
	protected Map<String, String> objectToString(Object obj) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				if (field.get(obj) != null) {
					map.put(field.getName(), field.get(obj).toString());
					// System.out.println(field.getName()+"="+field.get(obj).toString());
				} else {
					map.put(field.getName(), "");
					System.out.println(field.getName() + "=");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	
	
	protected void responseJson(HttpServletResponse response, Object obj) throws IOException {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write(objectToJson(obj));
		out.close();
	}

}
