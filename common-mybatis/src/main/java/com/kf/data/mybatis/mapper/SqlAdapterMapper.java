package com.kf.data.mybatis.mapper;

import java.util.List;

import com.kf.data.mybatis.entity.SqlAdapter;

public interface SqlAdapterMapper {

	void executeSQL(SqlAdapter sqlAdapter);

	void executeMapSQL(SqlAdapter sqlAdapter);

	int insertReturnPriKey(SqlAdapter sqlAdapter);
	
    public List<String> showTables(SqlAdapter sqlAdapter);

	
}
