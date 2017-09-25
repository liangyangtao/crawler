package com.kf.data.pdfparser.jdbc;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.PdfReportModelNotice;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.PdfReportModelNoticeMapper;

public class PdfReportModelNoticeStore {

	public void savePdfReportModelNotice(PdfReportModelNotice pdfReportModelNotice) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfReportModelNoticeMapper pdfReportModelNoticeMapper = sqlSession
					.getMapper(PdfReportModelNoticeMapper.class);
			pdfReportModelNoticeMapper.insertSelective(pdfReportModelNotice);
			sqlSession.commit(true);
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
	}

}
