package com.kf.data.approved.store;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.NeeqCompanyChoiceLayerOnline;
import com.kf.data.mybatis.entity.NeeqCompanyChoiceLayerOnlineExample;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.NeeqCompanyChoiceLayerOnlineMapper;

/***
 * 
 * @Title: NeeqCompanyChoiceLayerOnlineReader.java
 * @Package com.kf.data.approved.store
 * @Description: 读取企业IPO 辅导信息
 * @author liangyt
 * @date 2017年12月19日 上午11:01:45
 * @version V1.0
 */
public class NeeqCompanyChoiceLayerOnlineReader {

	/***
	 * 读取企业IPO 辅导信息
	 * 
	 * @return
	 */
	public List<NeeqCompanyChoiceLayerOnline> readNeeqCompanyChoiceLayerByCode(String code) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("onLineMysql").openSession();
		List<NeeqCompanyChoiceLayerOnline> neeqCompanyChoiceLayerOnlines = null;
		try {
			NeeqCompanyChoiceLayerOnlineMapper neeqCompanyChoiceLayerOnlineMapper = sqlSession
					.getMapper(NeeqCompanyChoiceLayerOnlineMapper.class);
			NeeqCompanyChoiceLayerOnlineExample example = new NeeqCompanyChoiceLayerOnlineExample();
			example.or().andStockCodeEqualTo(code);
			example.setOrderByClause("id desc limit 1");
			neeqCompanyChoiceLayerOnlines = neeqCompanyChoiceLayerOnlineMapper.selectByExample(example);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return neeqCompanyChoiceLayerOnlines;

	}
}
