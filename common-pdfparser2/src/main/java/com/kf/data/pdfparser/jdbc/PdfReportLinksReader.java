package com.kf.data.pdfparser.jdbc;

import java.util.List;

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
public class PdfReportLinksReader {

	public List<PdfReportLinks> readerPdfCodeLinkByTypeAndRank(String pdftype, int rank) {
		List<PdfReportLinks> pdfReportLinks = null;
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfReportLinksMapper pdfReportLinksMapper = sqlSession.getMapper(PdfReportLinksMapper.class);
			PdfReportLinksExample example = new PdfReportLinksExample();
			if (pdftype.contains("_")) {
				pdftype = pdftype.split("_")[0];
			}
			example.or().andPdfTypeEqualTo(pdftype).andRankEqualTo(rank);
			example.setOrderByClause("id desc limit 100");
			pdfReportLinks = pdfReportLinksMapper.selectByExample(example);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return pdfReportLinks;

	}

}
