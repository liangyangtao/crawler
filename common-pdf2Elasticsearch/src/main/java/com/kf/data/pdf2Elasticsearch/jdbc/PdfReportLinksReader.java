package com.kf.data.pdf2Elasticsearch.jdbc;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.mybatis.entity.PdfReportLinksExample;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.PdfReportLinksMapper;

public class PdfReportLinksReader {

	public int savePdfReportLinks(PdfReportLinks pdfReportLinks) {
		int id = 0;
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfReportLinksMapper pdfReportLinksMapper = sqlSession.getMapper(PdfReportLinksMapper.class);
			id = linkExit(pdfReportLinksMapper, pdfReportLinks);
			if (id == 0) {
				pdfReportLinksMapper.insertSelective(pdfReportLinks);
				id = pdfReportLinks.getId();
			}
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return id;
	}

	private int linkExit(PdfReportLinksMapper pdfReportLinksMapper, PdfReportLinks pdfReportLinks) {
		PdfReportLinksExample example = new PdfReportLinksExample();
		example.or().andNoticeIdEqualTo(pdfReportLinks.getNoticeId());
		List<PdfReportLinks> pdfReportLinkses = pdfReportLinksMapper.selectByExample(example);

		if (pdfReportLinkses.size() > 0) {
			return pdfReportLinkses.get(0).getId();
		}
		return 0;
	}
}
