package com.kf.data.approved.store;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.NeeqCompanyOnline;
import com.kf.data.mybatis.entity.NeeqCompanyOnlineExample;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.NeeqCompanyOnlineMapper;

/****
 * 
 * @Title: NeeqCompanyOnlineReader.java
 * @Package com.kf.data.approved.store
 * @Description: 读取公司
 * @author liangyt
 * @date 2017年12月19日 上午10:57:46
 * @version V1.0
 */
public class NeeqCompanyOnlineReader {

	/***
	 * 读取公司
	 * 
	 * @return
	 */
	public List<NeeqCompanyOnline> readNeeqCompanyByCode(String code) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("onLineMysql").openSession();
		List<NeeqCompanyOnline> neeqCompanyOnlines = null;
		try {
			NeeqCompanyOnlineMapper neeqCompanyOnlineMapper = sqlSession.getMapper(NeeqCompanyOnlineMapper.class);
			NeeqCompanyOnlineExample example = new NeeqCompanyOnlineExample();
			example.or().andCodeEqualTo(Integer.parseInt(code));
			example.setOrderByClause("id desc limit 1");
			neeqCompanyOnlines = neeqCompanyOnlineMapper.selectByExample(example);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return neeqCompanyOnlines;

	}

}
