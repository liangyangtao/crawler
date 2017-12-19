package com.kf.data.pdf2Elasticsearch.jdbc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.NeeqCompanyNotice;
import com.kf.data.mybatis.entity.NeeqCompanyNoticeExample;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.NeeqCompanyNoticeMapper;

/***
 * 
 * @Title: NeeqCompanyNoticeReader.java
 * @Package com.kf.data.jdbc.pdfCode.reader
 * @Description: 读取neeqCompany
 * @author liangyt
 * @date 2017年7月31日 上午11:40:55
 * @version V1.0
 */
public class NeeqCompanyNoticeReader {

	/****
	 * 根据id 读取notic
	 * 
	 * @param id
	 * @return
	 */
	public NeeqCompanyNotice readNeeqCompanyNoticesById(long id) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		NeeqCompanyNotice neeqCompanyNotice = null;
		try {
			NeeqCompanyNoticeMapper neeqCompanyNoticeMapper = sqlSession.getMapper(NeeqCompanyNoticeMapper.class);
			neeqCompanyNotice = neeqCompanyNoticeMapper.selectByPrimaryKey(id);
			sqlSession.commit(true);
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return neeqCompanyNotice;

	}

	/****
	 * 读取未解析的notic
	 * 
	 * @param id
	 * @return
	 */
	public List<NeeqCompanyNotice> readNeeqCompanyNoticesByIndex(long id) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerHisMysql").openSession();
		List<NeeqCompanyNotice> neeqCompanyNotices = null;
		try {
			NeeqCompanyNoticeMapper neeqCompanyNoticeMapper = sqlSession.getMapper(NeeqCompanyNoticeMapper.class);
			NeeqCompanyNoticeExample example = new NeeqCompanyNoticeExample();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date value = simpleDateFormat.parse("2015-12-31");
			example.or().andIdGreaterThan(id).andReportDateEqualTo(value);
			example.setOrderByClause("id limit 100");
			neeqCompanyNotices = neeqCompanyNoticeMapper.selectByExample(example);
			sqlSession.commit(true);
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return neeqCompanyNotices;

	}

	/****
	 * 根据report date 读取neeqCompanyNotic
	 * 
	 * @param date
	 * @param id
	 * @return
	 */
	public List<NeeqCompanyNotice> readNeeqCompanyNoticesByReportDate(String date, long id) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		List<NeeqCompanyNotice> neeqCompanyNotices = null;
		try {
			NeeqCompanyNoticeMapper neeqCompanyNoticeMapper = sqlSession.getMapper(NeeqCompanyNoticeMapper.class);
			NeeqCompanyNoticeExample example = new NeeqCompanyNoticeExample();
			example.or().andReportDateEqualTo(new SimpleDateFormat("yyyy-MM-dd").parse(date)).andIdGreaterThan(id);
			example.setOrderByClause("id limit 20");
			neeqCompanyNotices = neeqCompanyNoticeMapper.selectByExample(example);
			sqlSession.commit(true);
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return neeqCompanyNotices;

	}

}
