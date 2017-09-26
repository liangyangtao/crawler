package com.kf.data.pdfparser.jdbc;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.PdfErrorRecord;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.PdfErrorRecordMapper;

public class PdfErrorRecordStore {

	public void savePdfErrorRecord(PdfErrorRecord pdfErrorRecord) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfErrorRecordMapper pdfErrorRecordMapper = sqlSession.getMapper(PdfErrorRecordMapper.class);
			pdfErrorRecordMapper.insertSelective(pdfErrorRecord);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
	}

}
