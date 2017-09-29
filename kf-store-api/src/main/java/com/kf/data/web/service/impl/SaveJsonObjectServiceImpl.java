package com.kf.data.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kf.data.mybatis.entity.SqlAdapter;
import com.kf.data.mybatis.mapper.SqlAdapterMapper;
import com.kf.data.web.service.SaveJsonObjectService;

@Service
public class SaveJsonObjectServiceImpl implements SaveJsonObjectService {

	@Autowired
	SqlAdapterMapper sqlAdapterMaper;

	@Override
	public String parserJson(String json, String type) {
		try {
			Gson gson = new GsonBuilder().create();
			Map<String, Object> map = gson.fromJson(json, new TypeToken<HashMap<String, Object>>() {
			}.getType());
			String sql = "insert into " + type + " ";
			SqlAdapter sqlAdapter = new SqlAdapter();
			sqlAdapter.setSql(sql);
			sqlAdapter.setObj(map);
			sqlAdapterMaper.executeMapSQL(sqlAdapter);
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();

		}
		return "error";
	}

}
