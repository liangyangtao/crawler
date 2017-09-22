package com.kf.data.pdfparser.jdbc;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.PdfCodeLink;
import com.kf.data.mybatis.entity.PdfCodeLinkExample;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.PdfCodeLinkMapper;

/**
 * @Title: PdfCodeLinkReader.java
 * @Package com.kf.data.jdbc.pdfCode.reader
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017年5月8日 下午2:39:03
 * @version V1.0
 */
public class PdfCodeLinkReader {

	public List<PdfCodeLink> readerPdfCodeLinkByTypeAndRank(String pdftype, int rank) {
		List<PdfCodeLink> pdfCodeLinks = null;
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfCodeLinkMapper pdfCodeLinkMapper = sqlSession.getMapper(PdfCodeLinkMapper.class);
			PdfCodeLinkExample example = new PdfCodeLinkExample();
			if (pdftype.contains("_")) {
				pdftype = pdftype.split("_")[0];
			}
			example.or().andPdfTypeEqualTo(pdftype).andRankEqualTo(rank);
			example.setOrderByClause("id desc limit 100");
			pdfCodeLinks = pdfCodeLinkMapper.selectByExample(example);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return pdfCodeLinks;

	}

}
