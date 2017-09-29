package com.kf.data.tianyancha.jdbc;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.TycHumans;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.TycHumansMapper;

public class TycHumansStore {

	public void saveTycHumans(TycHumans tycHumans) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			TycHumansMapper tycHumansMapper = sqlSession.getMapper(TycHumansMapper.class);
			tycHumansMapper.insertSelective(tycHumans);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
	}
}
