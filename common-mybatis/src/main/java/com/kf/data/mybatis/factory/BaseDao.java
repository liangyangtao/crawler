package com.kf.data.mybatis.factory;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.SqlAdapter;
import com.kf.data.mybatis.mapper.SqlAdapterMapper;
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;


public class BaseDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());  
	public void executeMapSql(String sql, Map<String, Object> colums) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			SqlAdapterMapper sqlAdapterMaper = sqlSession.getMapper(SqlAdapterMapper.class);
			SqlAdapter sqlAdapter = new SqlAdapter();
			sqlAdapter.setSql(sql);
			sqlAdapter.setObj(colums);
			sqlAdapterMaper.executeMapSQL(sqlAdapter);
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("执行Sql异常", e);
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
	}

	public int insertReturnPriKey(String sql, Map<String, Object> colums) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		int id = 0;
		try {
			SqlAdapterMapper sqlAdapterMaper = sqlSession.getMapper(SqlAdapterMapper.class);
			SqlAdapter sqlAdapter = new SqlAdapter();
			sqlAdapter.setSql(sql);
			sqlAdapter.setObj(colums);
			sqlAdapterMaper.insertReturnPriKey(sqlAdapter);
			id = sqlAdapter.getPrikey();
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("执行Sql异常", e);
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return id;
	}

	public void executeSql(String sql) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			SqlAdapterMapper sqlAdapterMaper = sqlSession.getMapper(SqlAdapterMapper.class);
			SqlAdapter sqlAdapter = new SqlAdapter();
			sqlAdapter.setSql(sql);
			sqlAdapterMaper.executeSQL(sqlAdapter);
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("执行Sql异常", e);
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
	}
}
