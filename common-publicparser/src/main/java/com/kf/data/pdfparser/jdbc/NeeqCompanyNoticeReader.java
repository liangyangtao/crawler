package com.kf.data.pdfparser.jdbc;

import java.text.SimpleDateFormat;
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
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017年7月31日 上午11:40:55
 * @version V1.0
 */
public class NeeqCompanyNoticeReader {

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

	public List<NeeqCompanyNotice> readNeeqCompanyNoticesByIndex(long id) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		List<NeeqCompanyNotice> neeqCompanyNotices = null;
		try {
			NeeqCompanyNoticeMapper neeqCompanyNoticeMapper = sqlSession.getMapper(NeeqCompanyNoticeMapper.class);
			NeeqCompanyNoticeExample example = new NeeqCompanyNoticeExample();
			example.or().andIdGreaterThan(id);
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

	public List<NeeqCompanyNotice> readNeeqCompanyNoticesByReportDate(String date) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		List<NeeqCompanyNotice> neeqCompanyNotices = null;
		try {
			NeeqCompanyNoticeMapper neeqCompanyNoticeMapper = sqlSession.getMapper(NeeqCompanyNoticeMapper.class);
			NeeqCompanyNoticeExample example = new NeeqCompanyNoticeExample();
			example.or().andReportDateEqualTo(new SimpleDateFormat("yyyy-MM-dd").parse(date));
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
