package com.kf.data.dao;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.PdPedailyEventCrawler;
import com.kf.data.mybatis.entity.PdPedailyEventCrawlerExample;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.PdPedailyEventCrawlerMapper;

/***
 * 
 * @Title: PdPedailyEventCrawlerStore.java
 * @Package com.kf.data.dao
 * @Description: 投资界募资事件存储
 * @author liangyt
 * @date 2017年11月3日 下午5:37:45
 * @version V1.0
 */
public class PdPedailyEventCrawlerStore {

	/***
	 * 投资界投资机构存储
	 * 
	 * @param pdPedailyEventCrawler
	 */
	public void savePdPedailyEventCrawler(PdPedailyEventCrawler pdPedailyEventCrawler) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdPedailyEventCrawlerMapper pdPedailyEventCrawlerMapper = sqlSession
					.getMapper(PdPedailyEventCrawlerMapper.class);
			if (exit(pdPedailyEventCrawlerMapper, pdPedailyEventCrawler)) {
			} else {
				pdPedailyEventCrawlerMapper.insertSelective(pdPedailyEventCrawler);
			}
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
	}

	/***
	 * 判断是否存在
	 * 
	 * @param pdPedailyEventCrawlerMapper
	 * @param pdPedailyEventCrawler
	 * @return
	 */
	private boolean exit(PdPedailyEventCrawlerMapper pdPedailyEventCrawlerMapper,
			PdPedailyEventCrawler pdPedailyEventCrawler) {
		PdPedailyEventCrawlerExample example = new PdPedailyEventCrawlerExample();
		example.or().andUrlEqualTo(pdPedailyEventCrawler.getUrl());
		if (pdPedailyEventCrawlerMapper.selectByExample(example).size() > 0) {
			pdPedailyEventCrawlerMapper.updateByExampleSelective(pdPedailyEventCrawler, example);
			return true;
		} else {
			return false;
		}
	}

}
