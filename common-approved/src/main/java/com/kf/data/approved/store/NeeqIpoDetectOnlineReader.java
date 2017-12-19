package com.kf.data.approved.store;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.NeeqIpoDetectOnline;
import com.kf.data.mybatis.entity.NeeqIpoDetectOnlineExample;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.NeeqIpoDetectOnlineMapper;

/***
 * 
 * @Title: NeeqIpoDetectOnlineReader.java
 * @Package com.kf.data.approved.store
 * @Description: 读取精选企业层信息
 * @author liangyt
 * @date 2017年12月19日 上午11:00:25
 * @version V1.0
 */
public class NeeqIpoDetectOnlineReader {

	/***
	 * 读取精选企业层信息
	 * 
	 * @return
	 */
	public List<NeeqIpoDetectOnline> readNeeqIpoDetectOnlineByCode(String code) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("onLineMysql").openSession();
		List<NeeqIpoDetectOnline> neeqIpoDetectOnlines = null;
		try {
			NeeqIpoDetectOnlineMapper neeqIpoDetectOnlineMapper = sqlSession.getMapper(NeeqIpoDetectOnlineMapper.class);
			NeeqIpoDetectOnlineExample example = new NeeqIpoDetectOnlineExample();
			// stage_latest='1' and stage_name<>'b03'
			example.or().andStockCodeEqualTo(code).andStageLatestEqualTo(true).andStageNameNotEqualTo("b03");
			example.setOrderByClause("id desc limit 1");
			neeqIpoDetectOnlines = neeqIpoDetectOnlineMapper.selectByExample(example);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return neeqIpoDetectOnlines;

	}

}
