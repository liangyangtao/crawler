package com.kf.data.dao;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.PdPedailyOrgCrawler;
import com.kf.data.mybatis.entity.PdPedailyOrgCrawlerExample;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.PdPedailyOrgCrawlerMapper;

/***
 * 
 * @Title: PdPedailyOrgCrawlerStore.java
 * @Package com.kf.data.dao
 * @Description: 投资界投资机构存储
 * @author liangyt
 * @date 2017年11月3日 下午5:37:45
 * @version V1.0
 */
public class PdPedailyOrgCrawlerStore {

	/***
	 * 投资界投资机构存储
	 * 
	 * @param pdPedailyOrgCrawler
	 */
	public void savePdPedailyOrgCrawler(PdPedailyOrgCrawler pdPedailyOrgCrawler) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdPedailyOrgCrawlerMapper pdPedailyOrgCrawlerMapper = sqlSession.getMapper(PdPedailyOrgCrawlerMapper.class);
			if (exit(pdPedailyOrgCrawlerMapper, pdPedailyOrgCrawler)) {
			} else {
				pdPedailyOrgCrawlerMapper.insertSelective(pdPedailyOrgCrawler);
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
	 * @param pdPedailyOrgCrawlerMapper
	 * @param pdPedailyOrgCrawler
	 * @return
	 */
	private boolean exit(PdPedailyOrgCrawlerMapper pdPedailyOrgCrawlerMapper, PdPedailyOrgCrawler pdPedailyOrgCrawler) {
		PdPedailyOrgCrawlerExample example = new PdPedailyOrgCrawlerExample();
		example.or().andFullnameEqualTo(pdPedailyOrgCrawler.getFullname());
		if (pdPedailyOrgCrawlerMapper.selectByExample(example).size() > 0) {
			pdPedailyOrgCrawlerMapper.updateByExampleSelective(pdPedailyOrgCrawler, example);
			return true;
		} else {
			return false;
		}
	}

}
