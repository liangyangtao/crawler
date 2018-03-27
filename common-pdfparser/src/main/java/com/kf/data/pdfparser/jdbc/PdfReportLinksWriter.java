package com.kf.data.pdfparser.jdbc;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.mybatis.entity.PdfReportLinksExample;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.PdfReportLinksMapper;

/***
 * 
 * @author meidi
 *
 */
public class PdfReportLinksWriter {

	public void updatePdfReportRankById(int id, int rank) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfReportLinksMapper pdfReportLinksMapper = sqlSession.getMapper(PdfReportLinksMapper.class);
			PdfReportLinksExample example = new PdfReportLinksExample();
			example.or().andIdEqualTo(id);
			PdfReportLinks record = new PdfReportLinks();
			record.setRank(rank);
			pdfReportLinksMapper.updateByExampleSelective(record, example);
			sqlSession.commit(true);
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}

	}

}
