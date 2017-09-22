package com.kf.data.pdfparser.jdbc;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.PdfCodeTable;
import com.kf.data.mybatis.entity.PdfCodeTableExample;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.PdfCodeTableMapper;

/**
 * @Title: PdfCodetableReader.java
 * @Package com.kf.data.jdbc.pdfCode.reader
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017年5月20日 下午4:23:05
 * @version V1.0
 */
public class PdfCodetableReader {

	public List<PdfCodeTable> readPdfTable() {
		List<PdfCodeTable> pdfCodeTables = null;
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfCodeTableMapper pdfCodeTableMapper = sqlSession.getMapper(PdfCodeTableMapper.class);
			PdfCodeTableExample example = new PdfCodeTableExample();
			example.setOrderByClause("id desc");
			pdfCodeTables = pdfCodeTableMapper.selectByExample(example);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return pdfCodeTables;

	}

}
