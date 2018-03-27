package com.kf.data.pdfparser.jdbc;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.PdfReportTableModel;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.PdfReportTableModelMapper;

public class PdfReportTableModelSore {

	public void savePdfReportTableModel(PdfReportTableModel pdfReportTableModel) {

		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfReportTableModelMapper pdfReportTableModelMapper = sqlSession.getMapper(PdfReportTableModelMapper.class);
			pdfReportTableModelMapper.insertSelective(pdfReportTableModel);
			sqlSession.commit(true);
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}

	}

}
