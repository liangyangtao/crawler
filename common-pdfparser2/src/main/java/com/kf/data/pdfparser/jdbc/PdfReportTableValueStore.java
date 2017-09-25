package com.kf.data.pdfparser.jdbc;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.PdfReportTableValue;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.PdfReportTableValueMapper;

public class PdfReportTableValueStore {

	public void savaPdfReportTableValue(PdfReportTableValue pdfReportTableValue) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfReportTableValueMapper pdfReportTableValueMapper = sqlSession.getMapper(PdfReportTableValueMapper.class);
			pdfReportTableValueMapper.insertSelective(pdfReportTableValue);
			sqlSession.commit(true);
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}

	}

}
