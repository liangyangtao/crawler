package com.kf.data.pdf2Elasticsearch.jdbc;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.PdfReportLinkText;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.PdfReportLinkTextMapper;

public class PdfReportLinkTextReader {

	public void savePdfReportLinkText(PdfReportLinkText pdfReportLinkText) {

		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfReportLinkTextMapper pdfReportLinkTextMapper = sqlSession.getMapper(PdfReportLinkTextMapper.class);
			if (!linkExit(pdfReportLinkTextMapper, pdfReportLinkText)) {
				pdfReportLinkTextMapper.insertSelective(pdfReportLinkText);
			} else {
				pdfReportLinkTextMapper.updateByPrimaryKeyWithBLOBs(pdfReportLinkText);
			}
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
	}

	private boolean linkExit(PdfReportLinkTextMapper pdfReportLinkTextMapper, PdfReportLinkText pdfReportLinkText) {
		if (pdfReportLinkTextMapper.selectByPrimaryKey(pdfReportLinkText.getId()) != null) {
			return true;
		}
		return false;
	}
}
