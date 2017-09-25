package com.kf.data.pdfparser.jdbc;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.PdfReportTableModel;
import com.kf.data.mybatis.entity.PdfReportTableModelExample;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.PdfReportTableModelMapper;

public class PdfReportTableModelReader {

	public List<PdfReportTableModel> readPdfReportTableModelByModelId(String md5) {
		List<PdfReportTableModel> pdfReportTableModels = null;
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfReportTableModelMapper pdfReportTableModelMapper = sqlSession.getMapper(PdfReportTableModelMapper.class);
			PdfReportTableModelExample example = new PdfReportTableModelExample();
			example.or().andModelIdEqualTo(md5);
			pdfReportTableModels = pdfReportTableModelMapper.selectByExample(example);
			sqlSession.commit(true);
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return pdfReportTableModels;
	}

}
