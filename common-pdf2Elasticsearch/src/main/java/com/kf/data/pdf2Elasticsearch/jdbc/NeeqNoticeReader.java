package com.kf.data.pdf2Elasticsearch.jdbc;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.NeeqNotice;
import com.kf.data.mybatis.entity.SqlAdapter;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.NeeqNoticeMapper;

/**
 * @Title: NeeqNoticeReader.java
 * @Package com.kf.data.jdbc.pdfCode.reader
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017年5月19日 下午5:44:48
 * @version V1.0
 */
public class NeeqNoticeReader {

	public List<NeeqNotice> readNeeqNotice(String sql) {
		List<NeeqNotice> neeqNotices = null;
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			NeeqNoticeMapper neeqNoticeMapper = sqlSession.getMapper(NeeqNoticeMapper.class);
			SqlAdapter sqlAdapter = new SqlAdapter();
			sqlAdapter.setSql(sql);
			neeqNotices = neeqNoticeMapper.readerNeeqNotice(sqlAdapter);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return neeqNotices;

	}
}
