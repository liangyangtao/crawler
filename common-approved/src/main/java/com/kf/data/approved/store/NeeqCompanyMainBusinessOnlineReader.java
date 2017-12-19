package com.kf.data.approved.store;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.NeeqCompanyMainBusinessOnline;
import com.kf.data.mybatis.entity.NeeqCompanyMainBusinessOnlineExample;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.NeeqCompanyMainBusinessOnlineMapper;

/***
 * 
 * @Title: NeeqCompanyMainBusinessReader.java
 * @Package com.kf.data.approved.store
 * @Description: 读取主要业务
 * @author liangyt
 * @date 2017年12月19日 上午10:40:53
 * @version V1.0
 */
public class NeeqCompanyMainBusinessOnlineReader {
	/***
	 * 读取主要业务
	 * 
	 * @return
	 */
	public List<NeeqCompanyMainBusinessOnline> readNeeqCompanyMainBusinessByCode(String code) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("onLineMysql").openSession();
		List<NeeqCompanyMainBusinessOnline> neeqCompanyMainBusinessOnlines = null;
		try {
			NeeqCompanyMainBusinessOnlineMapper neeqCompanyMainBusinessOnlineMapper = sqlSession
					.getMapper(NeeqCompanyMainBusinessOnlineMapper.class);
			NeeqCompanyMainBusinessOnlineExample example = new NeeqCompanyMainBusinessOnlineExample();
			example.or().andStockCodeEqualTo(code);
			example.setOrderByClause("date desc limit 1");
			neeqCompanyMainBusinessOnlines = neeqCompanyMainBusinessOnlineMapper.selectByExampleWithBLOBs(example);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return neeqCompanyMainBusinessOnlines;

	}

}
