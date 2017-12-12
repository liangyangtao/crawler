package com.kf.data.neeq.store;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.NeeqInformationLawsCrawlerWithBLOBs;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.NeeqInformationLawsCrawlerMapper;

public class NeeqInformationLawsCrawlerStore {

	public void saveNeeqInformationLawsCrawler(NeeqInformationLawsCrawlerWithBLOBs neeqInformationLawsCrawler) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			NeeqInformationLawsCrawlerMapper neeqInformationLawsCrawlerMapper = sqlSession
					.getMapper(NeeqInformationLawsCrawlerMapper.class);
			neeqInformationLawsCrawlerMapper.insertSelective(neeqInformationLawsCrawler);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
	}

}
