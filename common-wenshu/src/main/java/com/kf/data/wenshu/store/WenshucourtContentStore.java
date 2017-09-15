package com.kf.data.wenshu.store;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.WenshucourtContentWithBLOBs;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.WenshucourtContentMapper;

/***
 * 
 * @author meidi
 *
 */
public class WenshucourtContentStore {

	public void saveWenshucourtContent(WenshucourtContentWithBLOBs wenshucourtContent) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			WenshucourtContentMapper wenshucourtContentMapper = sqlSession.getMapper(WenshucourtContentMapper.class);
			wenshucourtContentMapper.insertSelective(wenshucourtContent);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
	}

}
