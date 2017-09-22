package com.kf.data.pdfparser.jdbc;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.PdfCodeTemporary;
import com.kf.data.mybatis.entity.PdfCodeTemporaryExample;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.PdfCodeTemporaryMapper;

/**
 * @Title: PdfCodeTemporaryReader.java
 * @Package com.kf.data.jdbc.pdfCode.reader
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017年5月8日 上午11:05:47
 * @version V1.0
 */
public class PdfCodeTemporaryReader {

	public List<PdfCodeTemporary> readerPdfCodeTemporaryByPdfType(String pdfType) {

		List<PdfCodeTemporary> pdfCodeTemporaries = null;
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfCodeTemporaryMapper pdfCodeTemporaryMapper = sqlSession.getMapper(PdfCodeTemporaryMapper.class);
			PdfCodeTemporaryExample example = new PdfCodeTemporaryExample();
			example.or().andPdfTypeEqualTo(pdfType);
			pdfCodeTemporaries = pdfCodeTemporaryMapper.selectByExample(example);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return pdfCodeTemporaries;
	}

	public List<PdfCodeTemporary> readerPdfCodeTemporaryByPdfTypeAndProPerty(String pdfType, String property) {
		List<PdfCodeTemporary> pdfCodeTemporaries = null;
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfCodeTemporaryMapper pdfCodeTemporaryMapper = sqlSession.getMapper(PdfCodeTemporaryMapper.class);
			PdfCodeTemporaryExample example = new PdfCodeTemporaryExample();
			example.or().andPdfTypeLike(pdfType).andPropertyEqualTo(property);
			pdfCodeTemporaries = pdfCodeTemporaryMapper.selectByExample(example);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return pdfCodeTemporaries;
	}

}
