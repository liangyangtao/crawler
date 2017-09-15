package com.kf.data.wenshu.store;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.WenshucourtDataExample;
import com.kf.data.mybatis.entity.WenshucourtDataWithBLOBs;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.WenshucourtDataMapper;

/***
 * 
 * @author meidi
 *
 */
public class WenshucourtDataStore {

	public void saveWenshucourtData(WenshucourtDataWithBLOBs wenshucourtDataWithBLOBs) {

		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			WenshucourtDataMapper wenshucourtDataMapper = sqlSession.getMapper(WenshucourtDataMapper.class);
			if (!exit(wenshucourtDataMapper, wenshucourtDataWithBLOBs)) {
				wenshucourtDataMapper.insertSelective(wenshucourtDataWithBLOBs);
			}
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
	}

	private boolean exit(WenshucourtDataMapper wenshucourtDataMapper,
			WenshucourtDataWithBLOBs wenshucourtDataWithBLOBs) {
		WenshucourtDataExample example = new WenshucourtDataExample();
		example.or().andCaseIdEqualTo(wenshucourtDataWithBLOBs.getCaseId());
		if (wenshucourtDataMapper.selectByExample(example).size() > 0) {
			return true;
		}
		return false;
	}

}
