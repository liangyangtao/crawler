package com.kf.data.pdfparser.jdbc;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.PdfCodeChapter;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.PdfCodeChapterMapper;

public class PdfCodeChapterWriter {

	public void savePdfCodeChapter(PdfCodeChapter pdfCodeChapter) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfCodeChapterMapper pdfCodeChapterMapper = sqlSession.getMapper(PdfCodeChapterMapper.class);
			pdfCodeChapterMapper.insertSelective(pdfCodeChapter);
			sqlSession.commit(true);
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
	}
}
