package com.kf.data.tianyancha.jdbc;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.TycHumans;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.TycHumansMapper;

/****
 * 存储人名
 * 
 * @Title: TycHumansStore.java
 * @Package com.kf.data.tianyancha.jdbc
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017年10月11日 下午2:04:47
 * @version V1.0
 */
public class TycHumansStore {

	/***
	 * 存储人名
	 * 
	 * @param tycHumans
	 */
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
